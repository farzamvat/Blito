package com.blito.services;

import com.blito.configs.Constants;
import com.blito.enums.ImageType;
import com.blito.enums.OperatorState;
import com.blito.enums.Response;
import com.blito.enums.State;
import com.blito.exceptions.*;
import com.blito.mappers.*;
import com.blito.models.*;
import com.blito.repositories.*;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.blittype.BlitTypeViewModel;
import com.blito.rest.viewmodels.event.ChangeEventStateVm;
import com.blito.rest.viewmodels.event.EventFlatViewModel;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.eventdate.EventDateViewModel;
import com.blito.rest.viewmodels.image.ImageViewModel;
import com.blito.search.Operation;
import com.blito.search.SearchViewModel;
import com.blito.search.Simple;
import com.blito.security.SecurityContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventService {
	@Autowired
	private EventFlatMapper eventFlatMapper;
	@Autowired
	private EventMapper eventMapper;
	@Autowired
	private EventDateMapper eventDateCreateMapper;
	@Autowired
	private EventHostRepository eventHostRepository;
	@Autowired
	private BlitTypeMapper blitTypeMapper;
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private ImageMapper imageMapper;
	@Autowired
	private DiscountMapper discountMapper;
	@Autowired
	private BlitTypeRepository blitTypeRepository;
	@Autowired
	private DiscountRepository discountRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EventDateRepository eventDateRepository;
	@Autowired
	private SearchService searchService;
	@Autowired
	private ImageService imageService;
	@Autowired
	private SalonRepository salonRepository;
	@Autowired
	private SeatPickerService seatPickerService;

	private final Logger log = LoggerFactory.getLogger(getClass());



	// NOTE : Check if more than one blit type contains a seat && count of seat uids equals capacity
	private boolean validateDisjointSeatsInBlitTypeViewModel(Set<BlitTypeViewModel> blitTypes) {
		Set<BlitTypeViewModel> filteredBlitTypesContainSeatUids =
				blitTypes.stream().filter(blitType -> blitType.getSeatUids() != null
						&& !blitType.getSeatUids().isEmpty())
						.collect(Collectors.toSet());
		return (filteredBlitTypesContainSeatUids.stream().flatMap(blitType -> blitType.getSeatUids().stream()).distinct().count()
				== blitTypes.stream().mapToLong(blitType -> blitType.getSeatUids().size()).sum())
				&& filteredBlitTypesContainSeatUids.stream().allMatch(blitType -> blitType.getSeatUids().size() == blitType.getCapacity());
	}

	// NOTE : Selection validation of all salon's seat picked
	private boolean validateConsistencyOfSeatCounts(EventDateViewModel eventDateViewModel, String salonUid) {
	    Salon salon = salonRepository.findBySalonUid(salonUid).orElseThrow(() -> new FileNotFoundException(ResourceUtil.getMessage(Response.SALON_NOT_FOUND)));
	    return eventDateViewModel.getBlitTypes().stream()
                .filter(blitType ->blitType.getSeatUids()!=null && !blitType.getSeatUids().isEmpty())
                .mapToLong(blitType->blitType.getSeatUids().size()).sum() == salon.getSeats().size();

    }

	private void validateEventViewModel(EventViewModel vmodel) {
		if(vmodel.getEventDates().stream().anyMatch(eventDateViewModel -> !validateDisjointSeatsInBlitTypeViewModel(eventDateViewModel.getBlitTypes())))
		{
			throw new InconsistentDataException(ResourceUtil.getMessage(Response.SHARED_SEAT_AND_INCONSISTENT_CAPACITY_ERROR));
		}

		if(vmodel.getSalonUid() != null && vmodel.getEventDates().stream().anyMatch(eventDateViewModel -> !validateConsistencyOfSeatCounts(eventDateViewModel, vmodel.getSalonUid()))){
		    throw new InconsistentDataException(ResourceUtil.getMessage(Response.INCONSISTENT_SEAT_COUNTS));
        }

		if (vmodel.getBlitSaleStartDate().after(vmodel.getBlitSaleEndDate())) {
			throw new InconsistentDataException(ResourceUtil.getMessage(Response.INCONSISTENT_DATES));
		}
		if (vmodel.getEventDates().stream().flatMap(ed -> ed.getBlitTypes().stream()).anyMatch(bt ->
				bt.isFree() ? bt.getPrice() != 0 : bt.getPrice() <= 0)) {
			throw new InconsistentDataException(ResourceUtil.getMessage(Response.ISFREE_AND_PRICE_NOT_MATCHED));
		}
	}

	@Transactional
	public EventViewModel create(EventViewModel vmodel) {
		validateEventViewModel(vmodel);
		if(vmodel.getBlitSaleStartDate().before(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()))
				|| vmodel.getBlitSaleEndDate().before(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant())))
			throw new InconsistentDataException(ResourceUtil.getMessage(Response.INVALID_START_END_DATE));

		if (vmodel.getImages().stream().filter(i -> i.getType().equals(ImageType.EVENT_PHOTO)).count() == 0) {
			vmodel.getImages().add(new ImageViewModel(Constants.DEFAULT_EVENT_PHOTO, ImageType.EVENT_PHOTO));
		}

		Set<Image> images = imageRepository.findByImageUUIDIn(
				vmodel.getImages().stream().map(ImageViewModel::getImageUUID).collect(Collectors.toSet()));
		if (images.size() != vmodel.getImages().size()) {
			throw new NotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND));
		}
		images = imageMapper.setImageTypeFromImageViewModels(images, vmodel.getImages());

		EventHost eventHost = eventHostRepository.findByEventHostIdAndIsDeletedFalse(vmodel.getEventHostId())
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
				.orElseThrow(() -> new ResourceNotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND)));
		event.setViews(event.getViews() + 1);
		this.openOrCloseEventOnSaleDateConditions(event);
		return eventFlatMapper.createFromEntity(event);
	}

	@Transactional
	public EventViewModel getEventByLink(String eventLink) {
		Event event = eventRepository.findByEventLinkAndIsDeletedFalse(eventLink)
				.orElseThrow(() -> new ResourceNotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND)));
		event.setViews(event.getViews() + 1);
		this.openOrCloseEventOnSaleDateConditions(event);
		return eventMapper.createFromEntity(event);
	}

	public EventFlatViewModel getFlatEventById(long eventId) {
		Event event = eventRepository.findByEventIdAndIsDeletedFalse(eventId)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND)));
		this.openOrCloseEventOnSaleDateConditions(event);
		return eventFlatMapper.createFromEntity(event);
	}

	public EventViewModel getEventById(long eventId) {
		Event event = eventRepository.findByEventIdAndIsDeletedFalse(eventId)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND)));

		return eventMapper.createFromEntity(event);
	}

	@Transactional
	public EventViewModel update(EventViewModel vmodel) {
		validateEventViewModel(vmodel);
		Event event = eventRepository.findByEventIdAndIsDeletedFalse(vmodel.getEventId())
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND)));
		if(event.getAdditionalFields().size() > vmodel.getAdditionalFields().size()) {
			if(event.getEventDates().stream().flatMap(eventDate -> eventDate.getBlitTypes().stream()).anyMatch(
					blitType -> blitType.getSoldCount() > 0 ? true : false))
			{
				throw new AdditionalFieldsValidationException(ResourceUtil.getMessage(Response.ADDITIONAL_FIELDS_VALIDATION_ERROR));
			}
		}

		EventHost eventHost = eventHostRepository.findByEventHostIdAndIsDeletedFalse(vmodel.getEventHostId())
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_HOST_NOT_FOUND)));

		if (eventHost.getUser().getUserId() != SecurityContextHolder.currentUser().getUserId()) {
			throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
		}

		if (event.getEventState().equals(State.ENDED.name())) {
			throw new NotAllowedException(ResourceUtil.getMessage(Response.CANNOT_EDIT_EVENT_WHEN_CLOSED));
		}
		vmodel.setEventLink(vmodel.getEventLink().replaceAll(" ", "-"));
		if (!vmodel.getEventLink().equals(event.getEventLink())) {
			Optional<Event> eventResult = eventRepository.findByEventLinkAndIsDeletedFalse(vmodel.getEventLink());
			if (eventResult.isPresent() && eventResult.get().getEventId() != vmodel.getEventId()) {
				throw new AlreadyExistsException(ResourceUtil.getMessage(Response.EVENT_LINK_EXISTS));
			}
		}

		Set<Image> images = imageRepository.findByImageUUIDIn(
				vmodel.getImages().stream().map(ImageViewModel::getImageUUID).collect(Collectors.toSet()));
		images = imageMapper.setImageTypeFromImageViewModels(images, vmodel.getImages());

		event = eventMapper.updateEntity(vmodel, event);
		event.setImages(images);
		event.setEventHost(eventHost);
		return eventMapper.createFromEntity(eventRepository.save(event));
	}

	@Transactional
	public void delete(long eventId) {
		eventRepository.findByEventIdAndIsDeletedFalse(eventId).map(event -> {
			if(!event.getOperatorState().equals(OperatorState.REJECTED.name()))
				throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
			if (event.getEventHost().getUser().getUserId() != SecurityContextHolder.currentUser().getUserId()) {
				throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
			} else {
				event.getImages().forEach(i -> imageService.delete(i.getImageUUID()));
				event.setImages(new HashSet<>());
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

	private void openOrCloseEventOnSaleDateConditions(Event event) {
		Timestamp now = Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant());
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
				&& event.getEventState().equals(State.OPEN.name())
				&& event.getOperatorState().equals(OperatorState.APPROVED.name())
				&& !event.isClosedInit()) {
			event.setClosedInit(true);
			event.setEventState(State.CLOSED.name());
			event.getEventDates().forEach(ed -> {
				ed.setEventDateState(State.CLOSED.name());
				ed.getBlitTypes().forEach(bt -> {
					bt.setBlitTypeState(State.CLOSED.name());
				});
			});
		}
	}

	@Transactional
	public <V> Page<V> searchEvents(SearchViewModel<Event> searchViewModel, Pageable pageable,
			GenericMapper<Event, V> mapper) {
		Simple<Event> isDeletedRestriction = new Simple<>(Operation.eq, "isDeleted", "false");
		Simple<Event> isPrivateRestriction = new Simple<>(Operation.eq, "isPrivate", "false");
		Simple<Event> isApprovedRestriction = new Simple<>(Operation.eq, "operatorState", OperatorState.APPROVED.name());
		searchViewModel.getRestrictions().addAll(Arrays.asList(isDeletedRestriction, isPrivateRestriction, isApprovedRestriction));
		Page<Event> page = searchService.search(searchViewModel, pageable, eventRepository);

		page.forEach(this::openOrCloseEventOnSaleDateConditions);
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
	// TODO test if image deletes from event or not
	@Transactional
	public void deleteEventGalleryPhoto(long eventId, String uuid) {
		Event event = eventRepository.findByEventIdAndIsDeletedFalse(eventId)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND)));

		event.getImages().remove(imageRepository.findByImageUUID(uuid)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND))));
		
		imageService.delete(uuid);
	}
}
