package com.blito.services;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blito.enums.Response;
import com.blito.exceptions.NotFoundException;
import com.blito.mappers.AdminReportsMapper;
import com.blito.mappers.EventFlatMapper;
import com.blito.mappers.EventMapper;
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
import com.blito.rest.viewmodels.event.EventViewModel;

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

	public Event getEventFromRepository(long eventId) {
		Event event = Optional.ofNullable(eventRepository.findOne(eventId)).map(e -> e)
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
	public void setEventOrderNumber(long eventId, int order) {
		Event event = getEventFromRepository(eventId);
		event.setOrderNumber(order);
		return;
	}

	public Page<EventFlatViewModel> getAllEvents(Pageable page) {
		return eventFlatMapper.toPage(eventRepository.findAll(page), eventFlatMapper::createFromEntity);
	}

	public EventFlatViewModel getEvent(long eventId) {
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

}
