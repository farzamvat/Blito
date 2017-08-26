package com.blito.services;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blito.configs.Constants;
import com.blito.enums.ImageType;
import com.blito.enums.OperatorState;
import com.blito.enums.Response;
import com.blito.enums.State;
import com.blito.exceptions.NotAllowedException;
import com.blito.exceptions.NotFoundException;
import com.blito.exceptions.ResourceNotFoundException;
import com.blito.mappers.ExchangeBlitMapper;
import com.blito.models.Event;
import com.blito.models.ExchangeBlit;
import com.blito.models.Image;
import com.blito.models.User;
import com.blito.repositories.ExchangeBlitRepository;
import com.blito.repositories.ImageRepository;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.exchangeblit.ExchangeBlitChangeStateViewModel;
import com.blito.rest.viewmodels.exchangeblit.ExchangeBlitViewModel;
import com.blito.rest.viewmodels.image.ImageViewModel;
import com.blito.search.Operation;
import com.blito.search.SearchViewModel;
import com.blito.search.Simple;
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
	@Autowired
	SearchService searchService;
	@Autowired
	ImageService imageService;

	private ExchangeBlit findByExchangeBlitId(long id) {
		return exchangeBlitRepository.findByExchangeBlitIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_NOT_FOUND)));
	}

	@Transactional
	public ExchangeBlitViewModel create(ExchangeBlitViewModel vmodel) {
		if (vmodel.getImage() == null) {
			vmodel.setImage(new ImageViewModel(Constants.DEFAULT_EXCHANGEBLIT_PHOTO, ImageType.EXCHANGEBLIT_PHOTO));
		}

		Image image = imageRepository.findByImageUUID(vmodel.getImage().getImageUUID()).map(i -> {
			i.setImageType(vmodel.getImage().getType().name());
			return i;
		}).orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND)));

		ExchangeBlit exchangeBlit = exchangeBlitMapper.createFromViewModel(vmodel);
		exchangeBlit.setImage(image);
		User user = userRepository.findOne(SecurityContextHolder.currentUser().getUserId());
		exchangeBlit.setUser(user);
		exchangeBlit.setExchangeLink(generateExchangeBlitLink(exchangeBlit));
		return exchangeBlitMapper.createFromEntity(exchangeBlitRepository.save(exchangeBlit));
	}

	@Transactional
	public ExchangeBlitViewModel update(ExchangeBlitViewModel vmodel) {
		ExchangeBlit exchangeBlit = findByExchangeBlitId(vmodel.getExchangeBlitId());
		if (exchangeBlit.getUser().getUserId() != SecurityContextHolder.currentUser().getUserId()) {
			throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
		}
		if (exchangeBlit.getState().equals(State.SOLD)) {
			throw new NotAllowedException(ResourceUtil.getMessage(Response.EXCHANGE_BLIT_IS_SOLD));
		}
		if (vmodel.getImage() == null) {
			vmodel.setImage(new ImageViewModel(Constants.DEFAULT_EXCHANGEBLIT_PHOTO, ImageType.EXCHANGEBLIT_PHOTO));
		}
		Image image = imageRepository.findByImageUUID(vmodel.getImage().getImageUUID()).map(i -> {
			i.setImageType(vmodel.getImage().getType().name());
			return i;
		}).orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND)));

		exchangeBlit = exchangeBlitMapper.updateEntity(vmodel, exchangeBlit);
		exchangeBlit.setImage(image);
		return exchangeBlitMapper.createFromEntity(exchangeBlit);
	}

	@Transactional
	public void delete(long exchangeBlitId) {
		ExchangeBlit exchangeBlit = findByExchangeBlitId(exchangeBlitId);
		if (exchangeBlit.getUser().getUserId() != SecurityContextHolder.currentUser().getUserId()) {
			throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
		}
		imageService.delete(exchangeBlit.getImage().getImageUUID());
		exchangeBlit.setImage(null);
		exchangeBlit.setDeleted(true);
	}

	public String generateExchangeBlitLink(ExchangeBlit exchangeBlit) {
		String exchangeLink = exchangeBlit.getTitle().replaceAll(" ", "-") + "-"
				+ RandomUtil.generateLinkRandomNumber();
		while (exchangeBlitRepository.findByExchangeLinkAndIsDeletedFalse(exchangeLink).isPresent()) {
			exchangeLink = exchangeBlit.getTitle().replaceAll(" ", "-") + "-" + RandomUtil.generateLinkRandomNumber();
		}
		return exchangeLink;
	}

	public Page<ExchangeBlitViewModel> currentUserExchangeBlits(Pageable pageable) {

		User user = Optional.ofNullable(userRepository.findOne(SecurityContextHolder.currentUser().getUserId()))
				.map(u -> u).orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.USER_NOT_FOUND)));
		return exchangeBlitRepository.findByUserUserIdAndIsDeletedFalse(user.getUserId(), pageable)
				.map(exchangeBlitMapper::createFromEntity);

	}

	public Page<ExchangeBlitViewModel> searchExchangeBlits(SearchViewModel<ExchangeBlit> searchViewModel,
			Pageable pageable) {
		Simple<ExchangeBlit> isDeletedRestriction = new Simple<>(Operation.eq, "isDeleted", "false");
		Simple<ExchangeBlit> isApprovedRestriction = new Simple<>(Operation.eq, "operatorState", OperatorState.APPROVED.name()); 
		searchViewModel.getRestrictions().addAll(Arrays.asList(isDeletedRestriction, isApprovedRestriction));
		return searchService.search(searchViewModel, pageable, exchangeBlitMapper, exchangeBlitRepository);
	}

	@Transactional
	public void changeState(ExchangeBlitChangeStateViewModel vmodel) {
		ExchangeBlit exchangeBlit = findByExchangeBlitId(vmodel.getExchangeBlitId());
		if (exchangeBlit.getUser().getUserId() != SecurityContextHolder.currentUser().getUserId()) {
			throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
		}
		if (exchangeBlit.getOperatorState().equals(OperatorState.PENDING.name())
				|| exchangeBlit.getOperatorState().equals(OperatorState.REJECTED.name())) {
			throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
		}
		exchangeBlit.setState(vmodel.getState().name());
	}

	public ExchangeBlitViewModel getExchangeBlitByLink(String exchangeLink) {
		return exchangeBlitMapper.createFromEntity(
				exchangeBlitRepository.findByExchangeLinkAndIsDeletedFalse(exchangeLink)
				.orElseThrow(() -> new ResourceNotFoundException(ResourceUtil.getMessage(Response.BLIT_NOT_FOUND))));
	}
}
