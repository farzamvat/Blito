package com.blito.services;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.blito.enums.OperatorState;
import com.blito.enums.Response;
import com.blito.exceptions.NotFoundException;
import com.blito.mappers.AdminReportsMapper;
import com.blito.mappers.EventFlatMapper;
import com.blito.models.CommonBlit;
import com.blito.models.Event;
import com.blito.models.EventDate;
import com.blito.repositories.EventDateRepository;
import com.blito.repositories.EventRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.adminreport.BlitBuyerViewModel;
import com.blito.rest.viewmodels.event.AdminChangeEventOperatorStateVm;
import com.blito.rest.viewmodels.event.AdminChangeEventStateVm;
import com.blito.rest.viewmodels.event.EventFlatViewModel;

@Service
public class AdminEventService {

	@Autowired
	EventRepository eventRepository;
	@Autowired
	EventDateRepository eventDateRepository;
	@Autowired
	EventFlatMapper eventMapper;
	@Autowired
	AdminReportsMapper adminReportsMapper;

	public Event getEventFromRepository(long eventId) {
		Event event = Optional.ofNullable(eventRepository.findOne(eventId)).map(e -> e)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND)));
		return event;
	}

	public void changeEventState(AdminChangeEventStateVm vmodel) {
		Event event = getEventFromRepository(vmodel.getEventId());
		event.setEventState(vmodel.getState());
		eventRepository.save(event);
		return;
	}

	public void changeOperatorState(AdminChangeEventOperatorStateVm vmodel) {
		Event event = getEventFromRepository(vmodel.getEventId());
		event.setOperatorState(vmodel.getOperatorState());
		eventRepository.save(event);
		return;
	}
	
	public void setEventOrderNumber(long eventId, int order) {
		Event event = getEventFromRepository(eventId);
		event.setOrderNumber(order);
		eventRepository.save(event);
		return;
	}

	public Page<EventFlatViewModel> getAllEvents(Pageable page) {
		return eventMapper.toPage(eventRepository.findAll(page), eventMapper::createFromEntity);
	}

	// ViewModel Issues!!
	public EventFlatViewModel getEvent(long eventId) {
		Event event = getEventFromRepository(eventId);
		return eventMapper.createFromEntity(event);
	}

	public EventFlatViewModel updateEvent(EventFlatViewModel vmodel) {
		Event event = getEventFromRepository(vmodel.getEventId());
		return eventMapper
				.createFromEntity(eventRepository.save(eventMapper.updateEntity(vmodel, event)));
	}

	public Page<BlitBuyerViewModel> getEventBlitBuyersByEventDate(long eventDateId, Pageable pageable) {
		EventDate eventDate = Optional.ofNullable(eventDateRepository.findOne(eventDateId)).map(ed -> ed)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_DATE_NOT_FOUND)));

		Page<CommonBlit> page = new PageImpl<CommonBlit>(eventDate.getBlitTypes().stream()
				.flatMap(bt -> bt.getCommonBlits().stream()).skip(pageable.getPageNumber() * pageable.getPageSize())
				.limit(pageable.getPageSize()).collect(Collectors.toList()));
		return adminReportsMapper.toPage(page, adminReportsMapper::toBlitBuyerReport);
	}

}
