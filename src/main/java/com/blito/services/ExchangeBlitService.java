package com.blito.services;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blito.configs.Constants;
import com.blito.enums.ImageType;
import com.blito.enums.OperatorState;
import com.blito.enums.Response;
import com.blito.enums.State;
import com.blito.exceptions.NotAllowedException;
import com.blito.exceptions.NotFoundException;
import com.blito.mappers.ExchangeBlitMapper;
import com.blito.models.ExchangeBlit;
import com.blito.models.User;
import com.blito.repositories.ExchangeBlitRepository;
import com.blito.repositories.ImageRepository;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.exchangeblit.ExchangeBlitChangeStateViewModel;
import com.blito.rest.viewmodels.exchangeblit.ExchangeBlitViewModel;
import com.blito.rest.viewmodels.image.ImageViewModel;
import com.blito.security.SecurityContextHolder;

@Service
public class ExchangeBlitService {
	@Autowired
	ExchangeBlitMapper exchangeBlitMapper;
	@Autowired
	ExchangeBlitRepository exchangeBlitRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	ImageRepository imageRepository;

	private ExchangeBlit findByExchangeBlitId(long id) {
		return Optional.ofNullable(exchangeBlitRepository.findOne(id)).map(e -> e)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_NOT_FOUND)));
	}

	@Transactional
	public ExchangeBlitViewModel create(ExchangeBlitViewModel vmodel) {
		ExchangeBlit exchangeBlit = exchangeBlitMapper.createFromViewModel(vmodel);
		exchangeBlit.setState(State.CLOSED);
		exchangeBlit.setOperatorState(OperatorState.PENDING);
		if (vmodel.getImage() == null) {
			vmodel.setImage(new ImageViewModel(Constants.DEFAULT_EXCHANGEBLIT_PHOTO, ImageType.EXCHANGEBLIT_PHOTO));
		}
		exchangeBlit.setImage(imageRepository.findByImageUUID(vmodel.getImage().getImageUUID()).map(i -> {
			i.setImageType(vmodel.getImage().getType());
			return i;
		}).orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND))));

		User user = userRepository.findOne(SecurityContextHolder.currentUser().getUserId());
		exchangeBlit.setUser(user);
		return exchangeBlitMapper.createFromEntity(exchangeBlitRepository.save(exchangeBlit));
	}

	@Transactional
	public ExchangeBlitViewModel update(ExchangeBlitViewModel vmodel) {
		ExchangeBlit exchangeBlit = findByExchangeBlitId(vmodel.getExchangeBlitId());
		if (exchangeBlit.getState().equals(State.SOLD) || exchangeBlit.getOperatorState().equals(OperatorState.PENDING)
				|| exchangeBlit.getState().equals(State.CLOSED)
				|| exchangeBlit.getUser().getUserId() != SecurityContextHolder.currentUser().getUserId()) {
			throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
		}
		if (vmodel.getImage() == null) {
			vmodel.setImage(new ImageViewModel(Constants.DEFAULT_EXCHANGEBLIT_PHOTO, ImageType.EXCHANGEBLIT_PHOTO));
		}
		exchangeBlit.setImage(imageRepository.findByImageUUID(vmodel.getImage().getImageUUID()).map(i -> {
			i.setImageType(vmodel.getImage().getType());
			return i;
		}).orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND))));

		exchangeBlit = exchangeBlitMapper.updateEntity(vmodel, exchangeBlit);
		exchangeBlit.setOperatorState(OperatorState.PENDING);
		exchangeBlit.setState(State.CLOSED);

		return exchangeBlitMapper.createFromEntity(exchangeBlit);
	}

	@Transactional
	public void delete(long exchangeBlitId) {
		ExchangeBlit exchangeBlit = findByExchangeBlitId(exchangeBlitId);
		SecurityContextHolder.currentUser().getExchangeBlits().remove(exchangeBlit);
		exchangeBlitRepository.delete(exchangeBlit);
	}

	@Transactional
	public Page<ExchangeBlitViewModel> currentUserExchangeBlits(Pageable pageable) {

		User user = Optional.ofNullable(userRepository.findOne(SecurityContextHolder.currentUser().getUserId()))
				.map(u -> u).orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.USER_NOT_FOUND)));
		return exchangeBlitMapper.toPage(
				new PageImpl<>(user.getExchangeBlits().stream().skip(pageable.getPageNumber() * pageable.getPageSize())
						.limit(pageable.getPageSize()).collect(Collectors.toList())),
				exchangeBlitMapper::createFromEntity);
	}

	@Transactional
	public void changeState(ExchangeBlitChangeStateViewModel vmodel) {
		ExchangeBlit exchangeBlit = findByExchangeBlitId(vmodel.getExchangeBlitId());
		if (exchangeBlit.getOperatorState() == OperatorState.PENDING || exchangeBlit.getOperatorState() == OperatorState.REJECTED) {
			throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
		}
		exchangeBlit.setState(vmodel.getState());
	}

	public Page<ExchangeBlitViewModel> getApprovedAndNotClosedOrSoldBlits(Pageable pageable) {
		return exchangeBlitMapper.toPage(
				exchangeBlitRepository.findByStateAndOperatorState(State.OPEN, OperatorState.APPROVED, pageable),
				exchangeBlitMapper::createFromEntity);

	}

	public ExchangeBlitViewModel getExchangeBlitById(long exchangeBlitId) {
		return exchangeBlitMapper.createFromEntity(findByExchangeBlitId(exchangeBlitId));
	}
}
