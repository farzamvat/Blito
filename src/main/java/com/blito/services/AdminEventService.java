package com.blito.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.blito.enums.OperatorState;
import com.blito.enums.Response;
import com.blito.enums.State;
import com.blito.exceptions.NotFoundException;
import com.blito.mappers.AdminReportsMapper;
import com.blito.mappers.EventMapper;
import com.blito.models.CommonBlit;
import com.blito.models.Event;
import com.blito.models.EventDate;
import com.blito.repositories.EventDateRepository;
import com.blito.repositories.EventRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.adminreport.BlitBuyerViewModel;
import com.blito.rest.viewmodels.event.EventSimpleViewModel;
import com.blito.rest.viewmodels.event.EventUpdateViewModel;
import com.blito.rest.viewmodels.event.EventViewModel;

@Service
public class AdminEventService {

	@Autowired
	EventRepository eventRepository;
	@Autowired
	EventDateRepository eventDateRepository;
	@Autowired
	EventMapper eventMapper;
	@Autowired
	AdminReportsMapper adminReportsMapper;

	public Event getEventFromRepository(long eventId) {
		Event event = Optional.ofNullable(eventRepository.findOne(eventId)).map(e -> e)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND)));
		return event;
	}

	public void closeEvent(long eventId) {
		Event event = getEventFromRepository(eventId);
		event.setEventState(State.CLOSED);
		eventRepository.save(event);
		return;
	}

	public void approveEvent(long eventId) {
		Event event = getEventFromRepository(eventId);
		event.setOperatorState(OperatorState.APPROVED);
		eventRepository.save(event);
		return;
	}

	public void rejectEvent(long eventId) {
		Event event = getEventFromRepository(eventId);
		event.setOperatorState(OperatorState.REJECTED);
		eventRepository.save(event);
		return;
	}

	public void setEventOrderNumber(long eventId, int order) {
		Event event = getEventFromRepository(eventId);
		event.setOrderNumber(order);
		eventRepository.save(event);
		return;
	}

	public Page<EventSimpleViewModel> getAllEvents(Pageable page) {
		return eventMapper.toPage(eventRepository.findAll(page), eventMapper::eventToEventSimpleViewModel);
	}

	// ViewModel Issues!!
	public EventViewModel getEvent(long eventId) {
		Event event = getEventFromRepository(eventId);
		return eventMapper.createFromEntity(event);
	}

	// ViewModel Issues!!
	public EventViewModel updateEvent(EventUpdateViewModel vmodel) {
		Event event = getEventFromRepository(vmodel.getEventId());
		return eventMapper
				.createFromEntity(eventRepository.save(eventMapper.updateEventFromUpdateViewModel(vmodel, event)));
	}

	public Page<BlitBuyerViewModel> getEventBlitBuyersByEventDate(long eventDateId, Pageable pageable) {
		EventDate eventDate = Optional.ofNullable(eventDateRepository.findOne(eventDateId)).map(ed -> ed)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_DATE_NOT_FOUND)));
		List<CommonBlit> blits = eventDate.getBlitTypes().stream().flatMap(bt->bt.getCommonBlits().stream()).collect(Collectors.toList());
		
		Page<CommonBlit> page = new PageImpl<CommonBlit>(blits,pageable, blits.size());
		return adminReportsMapper.toPage(page, adminReportsMapper::toBlitBuyerReport);
	}

}
