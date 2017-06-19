package com.blito.services;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.blito.enums.PaymentStatus;
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
import com.blito.rest.viewmodels.blit.SamanPaymentRequestResponseViewModel;
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
	@Value("{saman.bank.merchantCode}")
	String samanMerchantCode;

	private final Logger log = LoggerFactory.getLogger(BlitService.class);

	@Transactional
	public CompletableFuture<Object> createCommonBlit(CommonBlitViewModel vmodel) {

		CommonBlit commonBlit = commonBlitMapper.createFromViewModel(vmodel);
		BlitType blitType = Optional.ofNullable(blitTypeRepository.findOne(vmodel.getBlitTypeId()))
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_TYPE_NOT_FOUND)));

		User user = userRepository.findOne(SecurityContextHolder.currentUser().getUserId());
		System.out.println("Thread with id : " + Thread.currentThread().getId()
				+ " is running inside createCommonBlit methodxxxxxx");
		if (blitType.isFree()) {
			System.out.println("Thread with id : " + Thread.currentThread().getId()
					+ " is running inside createCommonBlit method");
			return CompletableFuture.completedFuture(commonBlitMapper.createFromEntity(reserveFreeBlit(blitType, commonBlit, user)));
		} else {
			if (commonBlit.getCount() * blitType.getPrice() != commonBlit.getTotalAmount())
				throw new InconsistentDataException("total amount is not equal to blitType * count");
			return buyCommonBlit(blitType, commonBlit, user).thenApply(blit -> {
				SamanPaymentRequestResponseViewModel samanResponse = new SamanPaymentRequestResponseViewModel();
				samanResponse.setToken(blit.getSamanBankToken());
				samanResponse.setRedirectURL("http://localhost:8085/ws");
				return samanResponse;
			});
			
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	private CompletableFuture<CommonBlit> buyCommonBlit(BlitType blitType, CommonBlit commonBlit, User user) {
		checkBlitTypeRestrictionsForBuy(blitType, commonBlit);
		blitType.setReservedCount(blitType.getReservedCount() + commonBlit.getCount());
		commonBlit.setTrackCode(generateTrackCode());
		commonBlit.setBlitType(blitType);
		user.addBlits(commonBlit);

		return samanBankService.requestToken(commonBlit.getTrackCode(), commonBlit.getTotalAmount())
				.thenApply(token -> {
					commonBlit.setSamanBankToken(token);
					commonBlit.setPaymentStatus(PaymentStatus.PENDING);
					return commonBlitRepository.save(commonBlit);
				});
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	private CommonBlit reserveFreeBlit(BlitType blitType, CommonBlit commonBlit, User user) {
		checkBlitTypeRestrictionsForBuy(blitType, commonBlit);
		blitType.setSoldCount(blitType.getSoldCount() + commonBlit.getCount());
		commonBlit.setTrackCode(generateTrackCode());
		commonBlit.setBlitType(blitType);
		commonBlit.setPaymentStatus(PaymentStatus.FREE);
		user.addBlits(commonBlit);
		//
		//
		//
		//
		// sending email asynchronously
		return commonBlitRepository.save(commonBlit);
	}

	private void checkBlitTypeRestrictionsForBuy(BlitType blitType, CommonBlit commonBlit) {
		blitType = blitTypeRepository.findOne(blitType.getBlitTypeId());

		if (blitType.getBlitTypeState().equals(State.SOLD))
			throw new RuntimeException("sold out");

		if (blitType.getBlitTypeState().equals(State.CLOSED))
			throw new RuntimeException("closed");
		
		if(blitType.isFree())
			if(commonBlit.getCount() + blitType.getSoldCount() > blitType.getCapacity())
				throw new InconsistentDataException("more than blit type capacity");
		else
			if (commonBlit.getCount() + blitType.getSoldCount() + blitType.getReservedCount() > blitType.getCapacity())
				throw new InconsistentDataException("more than blit type capacity");
		
	}

	private String generateTrackCode() {
		String trackCode = RandomUtil.generateTrackCode();
		while (blitRepository.findByTrackCode(trackCode).isPresent()) {
			return generateTrackCode();
		}
		return trackCode;
	}
}
