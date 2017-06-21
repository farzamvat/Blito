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
import com.blito.exceptions.AlreadyExistsException;
import com.blito.exceptions.InconsistentDataException;
import com.blito.exceptions.NotAllowedException;
import com.blito.exceptions.NotFoundException;
import com.blito.mappers.AdminReportsMapper;
import com.blito.mappers.DiscountMapper;
import com.blito.mappers.EventFlatMapper;
import com.blito.mappers.EventMapper;
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
import com.blito.rest.viewmodels.discount.DiscountViewModel;
import com.blito.rest.viewmodels.event.AdminChangeEventOperatorStateVm;
import com.blito.rest.viewmodels.event.AdminChangeEventStateVm;
import com.blito.rest.viewmodels.event.AdminChangeOfferTypeViewModel;
import com.blito.rest.viewmodels.event.AdminSetIsEventoViewModel;
import com.blito.rest.viewmodels.event.EventFlatViewModel;
import com.blito.rest.viewmodels.event.EventViewModel;
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
	
	
	
	public Event getEventFromRepository(long eventId) {
		Event event =eventRepository.findByEventIdAndIsDeletedFalse(eventId).map(e -> e)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND)));
		
		return event;
	}
	@Transactional
	public void changeEventState(AdminChangeEventStateVm vmodel) {
		Event event = getEventFromRepository(vmodel.getEventId());
		event.setEventState(vmodel.getState());
		return;
	}

	@Transactional
	public void changeOperatorState(AdminChangeEventOperatorStateVm vmodel) {
		Event event = getEventFromRepository(vmodel.getEventId());
		event.setOperatorState(vmodel.getOperatorState());
		return;
	}
	
	@Transactional
	public void setIsEvento(AdminSetIsEventoViewModel vmodel) {
		Event event = getEventFromRepository(vmodel.getEventId());
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
	
	@Transactional
	public void setEventOffers(AdminChangeOfferTypeViewModel vmodel) {
		Event event = getEventFromRepository(vmodel.getEventId());
		event.setOffers(vmodel.getOffers());
		return;
	}
	
	@Transactional
	public void setEventOrderNumber(long eventId, int order) {
		Event event = getEventFromRepository(eventId);
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
	public EventFlatViewModel updateEvent(EventViewModel vmodel) {
		Event event = getEventFromRepository(vmodel.getEventId());
		return eventFlatMapper
				.createFromEntity(eventMapper.updateEntity(vmodel, event));
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
	
	
	public Page<EventViewModel> getAllPendingEvents(Pageable pageable) {
		return eventMapper.toPage(eventRepository.findByOperatorStateAndIsDeletedFalse(OperatorState.PENDING, pageable));
	}

	
}
