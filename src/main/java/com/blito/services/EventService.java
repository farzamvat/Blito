package com.blito.services;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.blito.exceptions.AlreadyExistsException;
import com.blito.exceptions.InconsistentDataException;
import com.blito.exceptions.NotAllowedException;
import com.blito.exceptions.NotFoundException;
import com.blito.mappers.BlitTypeMapper;
import com.blito.mappers.DiscountMapper;
import com.blito.mappers.EventDateMapper;
import com.blito.mappers.EventFlatMapper;
import com.blito.mappers.EventMapper;
import com.blito.mappers.GenericMapper;
import com.blito.mappers.ImageMapper;
import com.blito.models.BlitType;
import com.blito.models.Discount;
import com.blito.models.Event;
import com.blito.models.EventHost;
import com.blito.models.Image;
import com.blito.models.User;
import com.blito.payments.saman.SamanBankService;
import com.blito.repositories.BlitTypeRepository;
import com.blito.repositories.DiscountRepository;
import com.blito.repositories.EventDateRepository;
import com.blito.repositories.EventHostRepository;
import com.blito.repositories.EventRepository;
import com.blito.repositories.ImageRepository;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.discount.DiscountViewModel;
import com.blito.rest.viewmodels.event.ChangeEventStateVm;
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
	@Autowired
	EventDateRepository eventDateRepository;
	@Autowired
	SearchService searchService;
	@Autowired
	ImageService imageService;
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Transactional
	public EventViewModel create(EventViewModel vmodel) {
		if (vmodel.getBlitSaleStartDate().after(vmodel.getBlitSaleEndDate())) {
			throw new InconsistentDataException(ResourceUtil.getMessage(Response.INCONSISTENT_DATES));
		}
		if (vmodel.getEventDates().stream().flatMap(ed -> ed.getBlitTypes().stream()).anyMatch(bt -> {
			return bt.isFree() ? bt.getPrice() != 0 : bt.getPrice() <= 0;
		})) {
			throw new InconsistentDataException(ResourceUtil.getMessage(Response.ISFREE_AND_PRICE_NOT_MATCHED));
		}
		if (vmodel.getImages().stream().filter(i -> i.getType().equals(ImageType.EVENT_PHOTO)).count() == 0) {
			vmodel.getImages().add(new ImageViewModel(Constants.DEFAULT_EVENT_PHOTO, ImageType.EVENT_PHOTO));
		}

		Set<Image> images = imageRepository.findByImageUUIDIn(
				vmodel.getImages().stream().map(iv -> iv.getImageUUID()).collect(Collectors.toSet()));
		if (images.size() != vmodel.getImages().size()) {
			throw new NotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND));
		}
		images = imageMapper.setImageTypeFromImageViewModels(images, vmodel.getImages());

		EventHost eventHost = eventHostRepository.findByEventHostIdAndIsDeletedFalse(vmodel.getEventHostId())
				.map(eh -> eh)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_HOST_NOT_FOUND)));

		Event event = eventMapper.createFromViewModel(vmodel);
		event.setImages(images);
		event.setEventHost(eventHost);
		event.setEventLink(generateEventLink(event));
		return eventMapper.createFromEntity(eventRepository.save(event));
	}

	@Transactional
	public EventFlatViewModel getFlatEventByLink(String link) {
		Event event = eventRepository.findByEventLinkAndIsDeletedFalse(link)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND)));
		event.setViews(event.getViews() + 1);
		return eventFlatMapper.createFromEntity(event);
	}

	@Transactional
	public EventViewModel getEventByLink(String eventLink) {
		Event event = eventRepository.findByEventLinkAndIsDeletedFalse(eventLink)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND)));
		event.setViews(event.getViews() + 1);
		return eventMapper.createFromEntity(event);
	}

	public EventFlatViewModel getFlatEventById(long eventId) {
		Event event = eventRepository.findByEventIdAndIsDeletedFalse(eventId).map(e -> e)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND)));
		return eventFlatMapper.createFromEntity(event);
	}

	public EventViewModel getEventById(long eventId) {
		Event event = eventRepository.findByEventIdAndIsDeletedFalse(eventId).map(e -> e)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND)));

		return eventMapper.createFromEntity(event);
	}

	@Transactional
	public EventViewModel update(EventViewModel vmodel) {
		if (vmodel.getBlitSaleStartDate().after(vmodel.getBlitSaleEndDate())) {
			throw new InconsistentDataException(ResourceUtil.getMessage(Response.INCONSISTENT_DATES));
		}

		if (vmodel.getEventDates().stream().flatMap(ed -> ed.getBlitTypes().stream()).anyMatch(bt -> {
			return bt.isFree() ? bt.getPrice() != 0 : bt.getPrice() <= 0;
		})) {
			throw new InconsistentDataException(ResourceUtil.getMessage(Response.ISFREE_AND_PRICE_NOT_MATCHED));
		}

		Event event = eventRepository.findByEventIdAndIsDeletedFalse(vmodel.getEventId()).map(e -> e)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND)));

		EventHost eventHost = eventHostRepository.findByEventHostIdAndIsDeletedFalse(vmodel.getEventHostId())
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_HOST_NOT_FOUND)));

		if (eventHost.getUser().getUserId() != SecurityContextHolder.currentUser().getUserId()) {
			throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
		}

		// handle exception message
		if (event.getEventState().equals(State.SOLD.name()) || event.getEventState().equals(State.ENDED.name())) {
			throw new NotAllowedException(ResourceUtil.getMessage(Response.EVENT_IS_SOLD));
		}

		if (!vmodel.getEventLink().equals(event.getEventLink())) {
			Optional<Event> eventResult = eventRepository.findByEventLinkAndIsDeletedFalse(vmodel.getEventLink());
			if (eventResult.isPresent() && eventResult.get().getEventId() != vmodel.getEventId()) {
				throw new AlreadyExistsException(ResourceUtil.getMessage(Response.EVENT_LINK_EXISTS));
			}
		}

		Set<Image> images = imageRepository.findByImageUUIDIn(
				vmodel.getImages().stream().map(iv -> iv.getImageUUID()).collect(Collectors.toSet()));
		images = imageMapper.setImageTypeFromImageViewModels(images, vmodel.getImages());

		event = eventMapper.updateEntity(vmodel, event);
		event.setImages(images);
		event.setEventHost(eventHost);
		return eventMapper.createFromEntity(event);
	}

	@Transactional
	public void delete(long eventId) {
		eventRepository.findByEventIdAndIsDeletedFalse(eventId).map(event -> {
			if (event.getEventHost().getUser().getUserId() != SecurityContextHolder.currentUser().getUserId()) {
				throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
			} else {
				event.getImages().forEach(i -> imageService.delete(i.getImageUUID()));
				event.setDeleted(true);
			}
			return event;
		}).orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND)));
	}

	public Page<EventViewModel> getAllEvents(Pageable pageable) {
		return eventRepository
				.findByEventStateOrEventStateOrderByCreatedAtDesc(State.SOLD.name(), State.OPEN.name(), pageable)
				.map(eventMapper::createFromEntity);
	}

	private String generateEventLink(Event event) {
		String eventLink = event.getEventName().replaceAll(" ", "-") + "-" + RandomUtil.generateLinkRandomNumber();
		while (eventRepository.findByEventLinkAndIsDeletedFalse(eventLink).isPresent()) {
			eventLink = event.getEventName().replaceAll(" ", "-") + "-" + RandomUtil.generateLinkRandomNumber();
		}
		return eventLink;
	}

	@Transactional
	public <V> Page<V> searchEvents(SearchViewModel<Event> searchViewModel, Pageable pageable,
			GenericMapper<Event, V> mapper) {
		Page<Event> page = searchService.search(searchViewModel, pageable, eventRepository);
		Timestamp now = Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant());
		page.forEach(event -> {
			if (event.getBlitSaleStartDate().before(now) && !event.isDeleted()
					&& !event.getEventState().equals(State.ENDED.name())
					&& event.getOperatorState().equals(OperatorState.APPROVED.name())
					&& !event.getEventState().equals(State.OPEN.name()) && !event.isOpenInit()) {
				event.setOpenInit(true);
				event.setEventState(State.OPEN.name());
				event.getEventDates().forEach(ed -> {
					ed.setEventDateState(State.OPEN.name());
					ed.getBlitTypes().forEach(bt -> {
						bt.setBlitTypeState(State.OPEN.name());
					});
				});
			}

			if (event.getBlitSaleEndDate().before(now) && !event.isDeleted()
					&& !event.getEventState().equals(State.ENDED.name())
					&& event.getOperatorState().equals(OperatorState.APPROVED.name())
					&& !event.getEventState().equals(State.CLOSED.name()) && !event.isClosedInit()) {
				event.setClosedInit(true);
				event.setEventState(State.CLOSED.name());
				event.getEventDates().forEach(ed -> {
					ed.setEventDateState(State.CLOSED.name());
					ed.getBlitTypes().forEach(bt -> {
						bt.setBlitTypeState(State.CLOSED.name());
					});
				});
			}
		});
		return page.map(mapper::createFromEntity);
	}

	public <V> Set<V> searchEvents(SearchViewModel<Event> searchViewModel, GenericMapper<Event, V> mapper) {
		return searchService.search(searchViewModel, mapper, eventRepository);
	}

	@Transactional
	public BlitType getBlitTypeFromRepository(long blitTypeId) {
		return Optional.ofNullable(blitTypeRepository.findOne(blitTypeId))
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_TYPE_NOT_FOUND)));
	}

	@Transactional
	public DiscountViewModel setDiscountCode(DiscountViewModel vmodel) {
		if (vmodel.getEffectDate().after(vmodel.getExpirationDate()))
			throw new InconsistentDataException(ResourceUtil.getMessage(Response.INCONSISTENT_DATES));
		if (vmodel.isPercent()) {
			if (!(vmodel.getPercent() > 0 && vmodel.getPercent() < 100))
				throw new InconsistentDataException(ResourceUtil.getMessage(Response.INCONSISTENT_PERCENT));
			if (vmodel.getAmount() != 0)
				throw new InconsistentDataException(
						ResourceUtil.getMessage(Response.INCONSISTENT_AMOUNT_WHEN_PERCENT_IS_TRUE));
		} else {
			if (vmodel.getAmount() <= 0)
				throw new InconsistentDataException(ResourceUtil.getMessage(Response.INCONSISTENT_AMOUNT));
			if (vmodel.getPercent() > 0)
				throw new InconsistentDataException(
						ResourceUtil.getMessage(Response.INCONSISTENT_PERCENTAGE_WHEN_PERCENT_IS_FALSE));
		}
		if (discountRepository.findByCode(vmodel.getCode()).isPresent())
			throw new AlreadyExistsException(ResourceUtil.getMessage(Response.DISCOUNT_CODE_ALREADY_EXISTS));
		Discount discount = discountMapper.createFromViewModel(vmodel);
		discount.setUser(userRepository.findOne(SecurityContextHolder.currentUser().getUserId()));
		discount.setBlitTypes(
				vmodel.getBlitTypeIds().stream().map(bt -> getBlitTypeFromRepository(bt)).collect(Collectors.toSet()));

		discount = discountRepository.save(discount);
		return discountMapper.createFromEntity(discount);
	}

	public Page<EventViewModel> getUserEvents(Pageable pageable) {
		User user = userRepository.findOne(SecurityContextHolder.currentUser().getUserId());
		Page<Event> events = eventRepository.findByEventHostUserUserIdAndIsDeletedFalse(user.getUserId(), pageable);
		return eventMapper.toPage(events);
	}

	@Transactional
	public void changeEventState(ChangeEventStateVm vmodel) {
		if (vmodel.getState() == State.ENDED || vmodel.getState() == State.SOLD)
			throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));

		Event event = eventRepository.findByEventIdAndIsDeletedFalse(vmodel.getEventId()).map(e -> e)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND)));

		if (event.getEventHost().getUser().getUserId() != SecurityContextHolder.currentUser().getUserId()) {
			throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
		}

		if (event.getEventState().equals(State.ENDED.name()) || event.getEventState().equals(State.SOLD.name()))
			throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));

		if (event.getOperatorState().equals(OperatorState.REJECTED.name())
				|| event.getOperatorState().equals(OperatorState.PENDING.name())) {
			throw new NotAllowedException(ResourceUtil.getMessage(Response.EVENT_NOT_APPROVED));
		}
		event.setEventState(vmodel.getState().name());
		return;
	}

	@Transactional
	public void deleteEventGalleryPhoto(long eventId, String uuid) {
		Event event = eventRepository.findByEventIdAndIsDeletedFalse(eventId)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND)));

		event.getImages().remove(imageRepository.findByImageUUID(uuid)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND))));
		
		imageService.delete(uuid);
	}
}
