package com.blito.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blito.configs.Constants;
import com.blito.enums.ImageType;
import com.blito.enums.Response;
import com.blito.enums.State;
import com.blito.exceptions.AlreadyExistsException;
import com.blito.exceptions.InconsistentDataException;
import com.blito.exceptions.NotAllowedException;
import com.blito.exceptions.NotFoundException;
import com.blito.mappers.BlitTypeMapper;
import com.blito.mappers.DiscountMapper;
import com.blito.mappers.EventDateMapper;
import com.blito.mappers.EventFlatMapper;
import com.blito.mappers.EventMapper;
import com.blito.mappers.ImageMapper;
import com.blito.models.BlitType;
import com.blito.models.Discount;
import com.blito.models.Event;
import com.blito.models.EventHost;
import com.blito.models.Image;
import com.blito.repositories.BlitTypeRepository;
import com.blito.repositories.DiscountRepository;
import com.blito.repositories.EventHostRepository;
import com.blito.repositories.EventRepository;
import com.blito.repositories.ImageRepository;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.discount.DiscountViewModel;
import com.blito.rest.viewmodels.event.EventFlatViewModel;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.image.ImageViewModel;
import com.blito.search.SearchViewModel;
import com.blito.security.SecurityContextHolder;

@Service
public class EventService {
	@Autowired
	EventFlatMapper eventFlatMapper;
	@Autowired
	EventMapper eventMapper;
	@Autowired
	EventDateMapper eventDateCreateMapper;
	@Autowired
	EventHostRepository eventHostRepository;
	@Autowired
	BlitTypeMapper blitTypeMapper;
	@Autowired
	ImageRepository imageRepository;
	@Autowired
	EventRepository eventRepository;
	@Autowired
	ImageMapper imageMapper;
	@Autowired
	DiscountMapper discountMapper;
	@Autowired
	BlitTypeRepository blitTypeRepository;
	@Autowired
	DiscountRepository discountRepository;
	@Autowired
	UserRepository userRepository;

	@Transactional
	public EventViewModel create(EventViewModel vmodel) {
		if (vmodel.getBlitSaleStartDate().after(vmodel.getBlitSaleEndDate())) {
			throw new InconsistentDataException(ResourceUtil.getMessage(Response.INCONSISTENT_DATES));
		}
		if (vmodel.getImages().size() == 0) {
			vmodel.setImages(Arrays.asList(new ImageViewModel(Constants.DEFAULT_EVENT_PHOTO, ImageType.EVENT_PHOTO),
					new ImageViewModel(Constants.DEFAULT_EVENT_BANNER, ImageType.BANNER)));
		}
		EventHost eventHost = Optional.ofNullable(eventHostRepository.findOne(vmodel.getEventHostId())).map(eh -> eh)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_HOST_NOT_FOUND)));
		List<Image> images = imageRepository.findByImageUUIDIn(
				vmodel.getImages().stream().map(iv -> iv.getImageUUID()).collect(Collectors.toList()));
		images = imageMapper.setImageTypeFromImageViewModels(images, vmodel.getImages());

		Event event = eventMapper.createFromViewModel(vmodel);
		event.setImages(images);
		event.setEventHost(eventHost);
		event.setEventLink(generateEventLink(event));
		return eventMapper.createFromEntity(eventRepository.save(event));
	}

	public EventFlatViewModel getFlatEventByLink(String link) {
		return eventRepository.findByEventLink(link).map(eventFlatMapper::createFromEntity)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND)));
	}

	public EventFlatViewModel getFlatEventById(long eventId) {
		Event event = Optional.ofNullable(eventRepository.findOne(eventId)).map(e -> e)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND)));
		return eventFlatMapper.createFromEntity(event);
	}

	public EventViewModel getEventById(long eventId) {
		Event event = Optional.ofNullable(eventRepository.findOne(eventId)).map(e -> e)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND)));
		return eventMapper.createFromEntity(event);
	}

	@Transactional
	public EventViewModel update(EventViewModel vmodel) {
		if (vmodel.getBlitSaleStartDate().after(vmodel.getBlitSaleEndDate())) {
			throw new InconsistentDataException(ResourceUtil.getMessage(Response.INCONSISTENT_DATES));
		}
		EventHost eventHost = Optional.ofNullable(eventHostRepository.findOne(vmodel.getEventHostId())).map(eh -> eh)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_HOST_NOT_FOUND)));

		if (eventHost.getUser().getUserId() != SecurityContextHolder.currentUser().getUserId()) {
			throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
		}

		List<Image> images = imageRepository.findByImageUUIDIn(
				vmodel.getImages().stream().map(iv -> iv.getImageUUID()).collect(Collectors.toList()));
		images = imageMapper.setImageTypeFromImageViewModels(images, vmodel.getImages());

		Event event = Optional.ofNullable(eventRepository.findOne(vmodel.getEventId())).map(e -> e)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND)));

		event = eventMapper.updateEntity(vmodel, event);
		event.setImages(images);
		event.setEventHost(eventHost);
		Optional<Event> eventResult = eventRepository.findByEventLink(vmodel.getEventLink());
		if (eventResult.isPresent() && eventResult.get().getEventId() != vmodel.getEventId()) {
			throw new AlreadyExistsException(ResourceUtil.getMessage(Response.EVENT_LINK_EXISTS));
		}
		event.setEventLink(vmodel.getEventLink());
		return eventMapper.createFromEntity(event);
	}

	@Transactional
	public void delete(long eventId) {
		Optional<Event> eventResult = Optional.ofNullable(eventRepository.findOne(eventId));
		if (!eventResult.isPresent()) {
			throw new NotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND));
		} else {
			if (eventResult.get().getEventHost().getUser().getUserId() != SecurityContextHolder.currentUser()
					.getUserId()) {
				throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
			} else {
				eventResult.get().getEventHost().getEvents().remove(eventResult.get());//// check
																						//// check
																						//// check
																						//// check
																						//// check
				eventRepository.delete(eventId);
			}
		}
	}

	public Page<EventViewModel> getAllEvents(Pageable pageable) {
		return eventMapper.toPage(new PageImpl<>(eventRepository.findAll().stream()
				.filter(e -> e.getEventState() == State.OPEN || e.getEventState() == State.SOLD)
				.skip(pageable.getPageNumber() * pageable.getPageSize()).limit(pageable.getPageSize())
				.collect(Collectors.toList())), eventMapper::createFromEntity);
	}

	private String generateEventLink(Event event) {
		String eventLink = event.getEventName().replaceAll(" ", "-") + "-" + RandomUtil.generateLinkRandomNumber();
		while (eventRepository.findByEventLink(eventLink).isPresent()) {
			eventLink = event.getEventName().replaceAll(" ", "-") + "-" + RandomUtil.generateLinkRandomNumber();
		}
		return eventLink;
	}

	public Page<EventViewModel> searchEvents(SearchViewModel<Event> searchViewModel, Pageable pageable) {
		/*
		 * empty search handling ...
		 */
		return searchViewModel.getRestrictions().stream().map(r -> r.action())
				.reduce((s1, s2) -> Specifications.where(s1).and(s2))
				.map(specification -> new PageImpl<>(
						eventMapper.createFromEntities(eventRepository.findAll(specification)).stream()
								.skip(pageable.getPageNumber() * pageable.getPageSize()).limit(pageable.getPageSize())
								.collect(Collectors.toList())))
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.SEARCH_UNSUCCESSFUL)));
	}

	@Transactional
	public BlitType getBlitTypeFromRepository(long blitTypeId) {
		return Optional.ofNullable(blitTypeRepository.findOne(blitTypeId)).map(e -> e)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_TYPE_NOT_FOUND)));
	}

	@Transactional
	public DiscountViewModel setDiscountCode(DiscountViewModel vmodel) {
		if (vmodel.getEffectDate().after(vmodel.getExpirationDate()))
			throw new InconsistentDataException(ResourceUtil.getMessage(Response.INCONSISTENT_DATES));
		if(discountRepository.findByCode(vmodel.getCode()).isPresent())
			throw new AlreadyExistsException(ResourceUtil.getMessage(Response.DISCOUNT_CODE_ALREADY_EXISTS));
		Discount discount = discountMapper.createFromViewModel(vmodel);
		discount.setUser(userRepository.findOne(SecurityContextHolder.currentUser().getUserId()));
		discount.setBlitTypes(
				vmodel.getBlitTypeIds().stream().map(bt -> getBlitTypeFromRepository(bt)).collect(Collectors.toList()));
		
		discount = discountRepository.save(discount);
		return discountMapper.createFromEntity(discount);
	}
}
