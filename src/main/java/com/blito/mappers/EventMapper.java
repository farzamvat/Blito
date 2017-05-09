package com.blito.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blito.enums.OperatorState;
import com.blito.enums.State;
import com.blito.models.Event;
import com.blito.rest.viewmodels.event.EventViewModel;

@Component
public class EventMapper implements GenericMapper<Event,EventViewModel> {

	@Autowired
	EventDateMapper eventDateMapper;
	
	@Override
	public Event createFromViewModel(EventViewModel vmodel) {
		Event event = new Event();
		event.setEventName(vmodel.getEventName());
		event.setAddress(vmodel.getAddress());
		event.setAparatDisplayCode(vmodel.getAparatDisplayCode());
		event.setBlitSaleEndDate(vmodel.getBlitSaleEndDate());
		event.setBlitSaleStartDate(vmodel.getBlitSaleStartDate());
		event.setDescription(vmodel.getDescription());
		event.setEventType(vmodel.getEventType());
		event.setLatitude(vmodel.getLatitude());
		event.setLongitude(vmodel.getLongitude());
		event.setOperatorState(OperatorState.PENDING);
		event.setEventState(State.CLOSED);
		return event;
	}

	@Override
	public EventViewModel createFromEntity(Event event) {
		EventViewModel vmodel = new EventViewModel();
		vmodel.setAddress(event.getAddress());
		vmodel.setAparatDisplayCode(event.getAparatDisplayCode());
		vmodel.setBlitSaleEndDate(event.getBlitSaleEndDate());
		vmodel.setBlitSaleStartDate(event.getBlitSaleStartDate());
		vmodel.setDescription(event.getDescription());
		vmodel.setEventHostId(event.getEventHost().getEventHostId());
		vmodel.setEventHostName(event.getEventHost().getHostName());
		vmodel.setEventId(event.getEventId());
		vmodel.setEventLink(event.getEventLink());
		vmodel.setEventName(event.getEventName());
		vmodel.setEventType(event.getEventType());
		vmodel.setOrderNumber(event.getOrderNumber());
		vmodel.setOperatorState(event.getOperatorState());
		vmodel.setEventState(event.getEventState());
		vmodel.setEventDates(eventDateMapper.createFromEntities(event.getEventDates()));
		return vmodel;
	}

	@Override
	public Event updateEntity(EventViewModel viewModel, Event entity) {
		// TODO Auto-generated method stub
		/////////////////////////////////////
		/////////////////////////////////////
		/////////////////////////////////////
		/////////////////////////////////////
		/////////////////////////////////////
		/////////////////////////////////////
		return null;
	}

}
