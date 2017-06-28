package com.blito.services;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blito.enums.OperatorState;
import com.blito.enums.Response;
import com.blito.enums.State;
import com.blito.exceptions.AlreadyExistsException;
import com.blito.exceptions.InconsistentDataException;
import com.blito.exceptions.NotAllowedException;
import com.blito.exceptions.NotFoundException;
import com.blito.mappers.AdminReportsMapper;
import com.blito.mappers.DiscountMapper;
import com.blito.mappers.EventFlatMapper;
import com.blito.mappers.EventMapper;
import com.blito.mappers.GenericMapper;
import com.blito.models.BlitType;
import com.blito.models.CommonBlit;
import com.blito.models.Discount;
import com.blito.models.Event;
import com.blito.models.EventDate;
import com.blito.repositories.BlitTypeRepository;
import com.blito.repositories.DiscountRepository;
import com.blito.repositories.EventDateRepository;
import com.blito.repositories.EventRepository;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.adminreport.BlitBuyerViewModel;
import com.blito.rest.viewmodels.blittype.ChangeBlitTypeStateVm;
import com.blito.rest.viewmodels.discount.DiscountViewModel;
import com.blito.rest.viewmodels.event.AdminChangeEventOperatorStateVm;
import com.blito.rest.viewmodels.event.AdminChangeOfferTypeViewModel;
import com.blito.rest.viewmodels.event.AdminSetIsEventoViewModel;
import com.blito.rest.viewmodels.event.ChangeEventStateVm;
import com.blito.rest.viewmodels.event.EventFlatViewModel;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.eventdate.ChangeEventDateStateVm;
import com.blito.search.SearchViewModel;
import com.blito.security.SecurityContextHolder;

@Service
public class AdminEventService {

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

	public Event getEventFromRepository(long eventId) {
		Event event = eventRepository.findByEventIdAndIsDeletedFalse(eventId)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND)));
		return event;
	}

	@Transactional
	public void changeEventState(ChangeEventStateVm vmodel) {
		Event event = getEventFromRepository(vmodel.getEventId());
		checkEventRestricitons(event);
		event.setEventState(vmodel.getState());
		return;
	}

	@Transactional
	public void changeEventDateState(ChangeEventDateStateVm vmodel) {
		EventDate eventDate = Optional.ofNullable(eventDateRepository.findOne(vmodel.getEventDateId())).map(ed -> ed)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_DATE_NOT_FOUND)));
		checkEventRestricitons(eventDate.getEvent());
		eventDate.setEventDateState(vmodel.getEventDateState());
		return;
	}
	
	@Transactional
	public void changeBlitTypeState(ChangeBlitTypeStateVm vmodel) {
		BlitType blitType = Optional.ofNullable(blitTypeRepository.findOne(vmodel.getBlitTypeId())).map(bt -> bt)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.BLIT_TYPE_NOT_FOUND)));
		checkEventRestricitons(blitType.getEventDate().getEvent());
		blitType.setBlitTypeState(vmodel.getBlitTypeState());
		return;
	}

	@Transactional
	public void changeOperatorState(AdminChangeEventOperatorStateVm vmodel) {
		Event event = getEventFromRepository(vmodel.getEventId());
		checkEventRestricitons(event);
		event.setOperatorState(vmodel.getOperatorState());
		return;
	}

	@Transactional
	public void setIsEvento(AdminSetIsEventoViewModel vmodel) {
		Event event = getEventFromRepository(vmodel.getEventId());
		checkEventRestricitons(event);
		event.setEvento(vmodel.isEvento());
		return;
	}

	@Transactional
	public void deleteEvent(long eventId) {
		Optional<Event> eventResult = eventRepository.findByEventIdAndIsDeletedFalse(eventId);
		if (!eventResult.isPresent()) {
			throw new NotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND));
		} else {
			eventResult.get().setDeleted(true);
		}
	}
	
	private void checkEventRestricitons(Event event)
	{
		if(event.getEventState() == State.ENDED || event.getEventState() == State.SOLD) {
			throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
		}
	}

	@Transactional
	public void setEventOffers(AdminChangeOfferTypeViewModel vmodel) {
		Event event = getEventFromRepository(vmodel.getEventId());
		checkEventRestricitons(event);
		vmodel.getOffers().forEach(offer -> {
			if(!event.getOffers().contains(offer))
				event.getOffers().add(offer);
		});
	}
	
	@Transactional
	public void removeEventOffers(AdminChangeOfferTypeViewModel vmodel) {
		Event event = getEventFromRepository(vmodel.getEventId());
		checkEventRestricitons(event);
		vmodel.getOffers().forEach(offer -> {
			event.getOffers().remove(offer);
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
		EventDate eventDate = Optional.ofNullable(eventDateRepository.findOne(eventDateId)).map(ed -> ed)
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

	@Transactional
	public DiscountViewModel setDiscountCode(DiscountViewModel vmodel) {
		return eventService.setDiscountCode(vmodel);
	}

	public Page<EventViewModel> getAllPendingEvents(Pageable pageable) {
		return eventMapper
				.toPage(eventRepository.findByOperatorStateAndIsDeletedFalse(OperatorState.PENDING, pageable));
	}
	
	public <V> Page<V> searchEvents(SearchViewModel<Event> searchViewModel, Pageable pageable,GenericMapper<Event,V> mapper ) {
		return searchService.search(searchViewModel, pageable, mapper, eventRepository);
	}

}
