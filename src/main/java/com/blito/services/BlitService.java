package com.blito.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.blito.enums.Response;
import com.blito.enums.State;
import com.blito.exceptions.InconsistentDataException;
import com.blito.exceptions.NotFoundException;
import com.blito.mappers.CommonBlitMapper;
import com.blito.models.BlitType;
import com.blito.models.CommonBlit;
import com.blito.models.User;
import com.blito.payments.saman.SamanBankService;
import com.blito.repositories.BlitRepository;
import com.blito.repositories.BlitTypeRepository;
import com.blito.repositories.CommonBlitRepository;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.blit.CommonBlitViewModel;
import com.blito.security.SecurityContextHolder;

@Service
public class BlitService {

	@Autowired
	private CommonBlitMapper commonBlitMapper;
	@Autowired
	private CommonBlitRepository commonBlitRepository;
	@Autowired
	private BlitTypeRepository blitTypeRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BlitRepository blitRepository;
	@Autowired
	private SamanBankService samanBankService;
	
	private final Object freeBlitLock = new Object();
	private final Object blitLock = new Object();

	@Transactional
	public CommonBlitViewModel createCommonBlit(CommonBlitViewModel vmodel) {

		CommonBlit commonBlit = commonBlitMapper.createFromViewModel(vmodel);
		BlitType blitType = Optional.ofNullable(blitTypeRepository.findOne(vmodel.getBlitTypeId()))
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_TYPE_NOT_FOUND)));
		
		User user = userRepository.findOne(SecurityContextHolder.currentUser().getUserId());
		if(blitType.isFree())
		{
			return commonBlitMapper.createFromEntity(reserveFreeBlit(blitType, commonBlit, user));
		}
		else {
			if(commonBlit.getCount() * blitType.getPrice() != commonBlit.getTotalAmount())
				throw new InconsistentDataException("total amount is not equal to blitType * count");
			synchronized (blitLock) {	
				blitType.setSoldCount(blitType.getSoldCount() + commonBlit.getCount());
				if(blitType.getSoldCount() == blitType.getCapacity())
					blitType.setBlitTypeState(State.SOLD);
				commonBlit.setTrackCode(generateTrackCode());
				commonBlit.setBlitType(blitType);
				commonBlit.setUser(user);
			}
		}
		return null;
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW,isolation=Isolation.SERIALIZABLE)
	private CommonBlit reserveFreeBlit(BlitType blitType,CommonBlit commonBlit,User user)
	{
		checkBlitTypeRestrictionsForBuy(blitType, commonBlit);
		blitType.setSoldCount(blitType.getSoldCount() + commonBlit.getCount());
		if(blitType.getSoldCount() == blitType.getCapacity())
			blitType.setBlitTypeState(State.SOLD);
		commonBlit.setTrackCode(generateTrackCode());
		commonBlit.setBlitType(blitType);
		user.addBlits(commonBlit);
		//
		//
		//
		//
		// sending email asynchronously
		return commonBlitRepository.save(commonBlit);
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW,isolation=Isolation.SERIALIZABLE)
	private CommonBlit buyCommonBlit(BlitType blitType,CommonBlit commonBlit,User user)
	{
		return null;
	}
	
	private void checkBlitTypeRestrictionsForBuy(BlitType blitType,CommonBlit commonBlit)
	{
		blitType = blitTypeRepository.findOne(blitType.getBlitTypeId());
		
		if(blitType.getBlitTypeState().equals(State.SOLD))
			throw new RuntimeException("sold out");
		
		if(blitType.getBlitTypeState().equals(State.CLOSED))
			throw new RuntimeException("closed");
		
		if(commonBlit.getCount() + blitType.getSoldCount() > blitType.getCapacity())
			throw new InconsistentDataException("more than blit type capacity");
	}
	
	private String generateTrackCode()
	{
		String trackCode = RandomUtil.generateTrackCode();
		while(blitRepository.findByTrackCode(trackCode).isPresent())
		{
			return generateTrackCode();
		}
		return trackCode;
	}
}
