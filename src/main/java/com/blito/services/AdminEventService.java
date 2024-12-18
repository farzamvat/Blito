package com.blito.services;

import com.blito.configs.Constants;
import com.blito.enums.*;
import com.blito.exceptions.NotAllowedException;
import com.blito.exceptions.NotFoundException;
import com.blito.mappers.*;
import com.blito.models.*;
import com.blito.repositories.*;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.ResultVm;
import com.blito.rest.viewmodels.adminreport.BlitBuyerViewModel;
import com.blito.rest.viewmodels.blittype.ChangeBlitTypeStateVm;
import com.blito.rest.viewmodels.event.*;
import com.blito.rest.viewmodels.eventdate.ChangeEventDateStateVm;
import com.blito.rest.viewmodels.exception.ExceptionViewModel;
import com.blito.rest.viewmodels.image.ImageViewModel;
import com.blito.search.SearchViewModel;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminEventService {
	private static final Logger log = LoggerFactory.getLogger(AdminEventService.class);
	@Autowired
	EventRepository eventRepository;
	@Autowired
	EventDateRepository eventDateRepository;
	@Autowired
	EventFlatMapper eventFlatMapper;
	@Autowired
	EventMapper eventMapper;
	@Autowired
	AdminReportsMapper adminReportsMapper;
	@Autowired
	DiscountMapper discountMapper;
	@Autowired
	DiscountRepository discountRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	BlitTypeRepository blitTypeRepository;
	@Autowired
	SearchService searchService;
	@Autowired
	EventService eventService;
	@Autowired
	ImageService imageService;
	@Autowired
	SmsService smsService;

	public Event getEventFromRepository(long eventId) {
		Event event = eventRepository.findByEventIdAndIsDeletedFalse(eventId)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND)));
		return event;
	}

	private String fillOperatorStateSmsMessage(OperatorState state, Event event) {
		String message;
		switch (state) {
			case APPROVED:
				message = String.format(ResourceUtil.getMessage(SmsMessage.OPERATOR_STATE_BASE_MESSAGE),
						event.getEventHost().getUser().getFirstname(),
						event.getEventName(),
						ResourceUtil.getMessage(SmsMessage.OPERATOR_STATE_ACCEPTED),
						ResourceUtil.getMessage(SmsMessage.OPERATOR_STATE_ACCEPTED_MESSAGE));
				break;
			case REJECTED:
				message = String.format(ResourceUtil.getMessage(SmsMessage.OPERATOR_STATE_BASE_MESSAGE),
						event.getEventHost().getUser().getFirstname(),
						event.getEventName(),
						ResourceUtil.getMessage(SmsMessage.OPERATOR_STATE_REJECTED),
						ResourceUtil.getMessage(SmsMessage.OPERATOR_STATE_REJECTED_MESSAGE));
				break;
			case EDIT_REJECTED:
				message = String.format(ResourceUtil.getMessage(SmsMessage.OPERATOR_STATE_EDIT_REJECTED_MESSAGE),
						event.getEventHost().getUser().getFirstname(),
						event.getEventName(),
						ResourceUtil.getMessage(SmsMessage.OPERATOR_STATE_REJECTED_MESSAGE));
				break;

			default:
				message = ResourceUtil.getMessage(SmsMessage.OPERATOR_STATE_DEFAULT_MESSAGE);
				break;

		}
		return message;
	}

	@Transactional
	public void changeEventState(ChangeEventStateVm vmodel) {
		Event event = getEventFromRepository(vmodel.getEventId());
		checkEventRestricitons(event);
		if(vmodel.getState() == State.CLOSED || vmodel.getState() == State.ENDED)
			event.getEventDates().forEach(ed -> {
				ed.setEventDateState(State.CLOSED.name());
				ed.getBlitTypes().forEach(bt -> bt.setBlitTypeState(State.CLOSED.name()));
			});

		if(vmodel.getState() == State.ENDED)
		    event.setEndDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()));

		event.setEventState(vmodel.getState().name());
	}

	@Transactional
	public void changeEventDateState(ChangeEventDateStateVm vmodel) {
		EventDate eventDate = Optional.ofNullable(eventDateRepository.findOne(vmodel.getEventDateId()))
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_DATE_NOT_FOUND)));
		if(vmodel.getEventDateState() == State.CLOSED)
		{
			eventDate.getBlitTypes().forEach(bt -> bt.setBlitTypeState(State.CLOSED.name()));
		}
		checkEventRestricitons(eventDate.getEvent());
		eventDate.setEventDateState(vmodel.getEventDateState().name());
	}
	
	@Transactional
	public void changeBlitTypeState(ChangeBlitTypeStateVm vmodel) {
		BlitType blitType = Optional.ofNullable(blitTypeRepository.findOne(vmodel.getBlitTypeId()))
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_TYPE_NOT_FOUND)));
		checkEventRestricitons(blitType.getEventDate().getEvent());
		blitType.setBlitTypeState(vmodel.getBlitTypeState().name());
		return;
	}

	@Transactional
	public void changeOperatorState(AdminChangeEventOperatorStateVm vmodel) {
		Event event = getEventFromRepository(vmodel.getEventId());
		checkEventRestricitons(event);
		if(event.getOperatorState().equals(OperatorState.APPROVED.name()) &&
				(vmodel.getOperatorState().equals(OperatorState.EDIT_REJECTED) ||
						vmodel.getOperatorState().equals(OperatorState.OPERATOR_IGNORE) ||
						vmodel.getOperatorState().equals(OperatorState.EDITED))) {
			throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
		}
		if(event.getOperatorState().equals(OperatorState.EDITED.name())
				&& event.getEditedVersion() != null
				&& vmodel.getOperatorState().name().equals(OperatorState.APPROVED.name())) {
			EventViewModel editedVersionViewModel = eventMapper.createFromEntity(event.getEditedVersion());
			eventMapper.updateEntity(editedVersionViewModel,event);
			event.setEventLink(editedVersionViewModel.getEventLink());
			event.setImages(event.getEditedVersion().getImages());
			event.setEventHost(event.getEditedVersion().getEventHost());
			event.setOperatorState(OperatorState.APPROVED.name());
			event.setEditedVersion(null);
		} else {
			if((vmodel.getOperatorState().equals(OperatorState.PENDING.name())
					|| vmodel.getOperatorState().equals(OperatorState.REJECTED.name())) && event.getEditedVersion() != null) {
				event.setEditedVersion(null);
			}
			event.setOperatorState(vmodel.getOperatorState().name());
		}
		Future.runRunnable(() -> smsService.sendOperatorStatusSms(event.getEventHost().getUser().getMobile(),
				fillOperatorStateSmsMessage(vmodel.getOperatorState(),event)))
				.onFailure(throwable -> log.debug("Error in sending sms in change operator state '{}'",throwable));

	}

	@Transactional
	public void setIsEvento(AdminSetIsEventoViewModel vmodel) {
		Event event = getEventFromRepository(vmodel.getEventId());
		checkEventRestricitons(event);
		event.setEvento(vmodel.isEvento());
	}

	@Transactional
	public void deleteEvent(long eventId) {
		Optional<Event> eventResult = eventRepository.findByEventIdAndIsDeletedFalse(eventId);
		if (!eventResult.isPresent()) {
			throw new NotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND));
		} else {
			eventResult.get().getImages().forEach(i->imageService.delete(i.getImageUUID()));
			eventResult.get().setImages(new HashSet<>());
			eventResult.get().setDeleted(true);
		}
	}
	
	private void checkEventRestricitons(Event event)
	{
		if(event.getEventState().equals(State.ENDED.name())) {
			throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
		}
	}

	@Transactional
	public void setEventOffers(AdminChangeOfferTypeViewModel vmodel) {
		Event event = getEventFromRepository(vmodel.getEventId());
		checkEventRestricitons(event);
		vmodel.getOffers().forEach(offer -> {
			if(!event.getOffers().contains(offer))
				event.getOffers().add(offer.name());
		});
	}
	
	@Transactional
	public void removeEventOffers(AdminChangeOfferTypeViewModel vmodel) {
		Event event = getEventFromRepository(vmodel.getEventId());
		checkEventRestricitons(event);
		vmodel.getOffers().forEach(offer -> {
			event.getOffers().remove(offer.name());
		});
	}

	@Transactional
	public void setEventOrderNumber(long eventId, int order) {
		Event event = getEventFromRepository(eventId);
		checkEventRestricitons(event);
		event.setOrderNumber(order);
		return;
	}

	public Page<EventFlatViewModel> getAllEvents(Pageable page) {
		return eventFlatMapper.toPage(eventRepository.findByIsDeletedFalse(page), eventFlatMapper::createFromEntity);
	}

	public EventFlatViewModel getFlatEvent(long eventId) {
		Event event = getEventFromRepository(eventId);

		return eventFlatMapper.createFromEntity(event);
	}

	@Transactional
	public Page<BlitBuyerViewModel> getEventBlitBuyersByEventDate(long eventDateId, Pageable pageable) {
		EventDate eventDate = Optional.ofNullable(eventDateRepository.findOne(eventDateId))
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_DATE_NOT_FOUND)));

		Page<CommonBlit> page = new PageImpl<CommonBlit>(eventDate.getBlitTypes().stream()
				.flatMap(bt -> bt.getCommonBlits().stream()).skip(pageable.getPageNumber() * pageable.getPageSize())
				.limit(pageable.getPageSize()).collect(Collectors.toList()));
		return adminReportsMapper.toPage(page, adminReportsMapper::toBlitBuyerReport);
	}

	@Transactional
	public BlitType getBlitTypeFromRepository(long blitTypeId) {
		return Optional.ofNullable(blitTypeRepository.findOne(blitTypeId)).map(e -> e)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_TYPE_NOT_FOUND)));
	}

	public Page<EventViewModel> getAllPendingEvents(Pageable pageable) {
		return eventMapper
				.toPage(eventRepository.findByOperatorStateAndIsDeletedFalse(OperatorState.PENDING.name(), pageable));
	}
	
	public <V> Page<V> searchEvents(SearchViewModel<Event> searchViewModel, Pageable pageable,GenericMapper<Event,V> mapper ) {
		return searchService.search(searchViewModel, pageable, mapper, eventRepository);
	}

	@Transactional
	public Either<ExceptionViewModel, ?> uploadEventPhoto(MultipartFile file, long eventId) {
		Either<ExceptionViewModel, ImageViewModel> either = imageService.saveMultipartFile(file);
		if(either.isRight()) {
			Event event = eventRepository.findByEventIdAndIsDeletedFalse(eventId)
					.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND)));
			Image eventPhoto = event.getImages()
					.stream()
					.filter(image -> image.getImageType().equals(ImageType.EVENT_PHOTO.name()))
					.findFirst()
					.get();
			eventPhoto.setImageUUID(either.get().getImageUUID());
			return  Either.right(new ResultVm(ResourceUtil.getMessage(Response.UPLOAD_SUCCESSFUL)));
		} else {
			return Either.left(either.getLeft());
		}
	}


}
