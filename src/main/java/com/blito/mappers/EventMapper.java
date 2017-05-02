package com.blito.mappers;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blito.enums.OperatorState;
import com.blito.enums.State;
import com.blito.models.Event;
import com.blito.rest.viewmodels.event.EventCreateViewModel;
import com.blito.rest.viewmodels.event.EventUpdateViewModel;
import com.blito.rest.viewmodels.event.EventViewModel;

@Component
public class EventMapper implements GenericMapper<Event,EventViewModel> {

	@Autowired EventDateFlatMapper flatMapper;
	@Autowired ImageMapper imageMapper;
	
	@Override
	public Event createFromViewModel(EventViewModel viewModel) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Event updateEventFromUpdateViewModel(EventUpdateViewModel vmodel,Event event)
	{
		event.setAddress(vmodel.getAddress());
		event.setAparatDisplayCode(vmodel.getAparatDisplayCode());
		event.setBlitSaleEndDate(vmodel.getBlitSaleEndDate());
		event.setBlitSaleStartDate(vmodel.getBlitSaleStartDate());
		event.setDescription(vmodel.getDescription());
		event.setEventName(vmodel.getEventName());
		event.setEventType(vmodel.getEventType());
		event.setLongitude(vmodel.getLongitude());
		event.setLatitude(vmodel.getLatitude());
		return event;
	}
	
	public Event eventCreateViewModelToEvent(EventCreateViewModel vmodel)
	{
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
		vmodel.setEventId(event.getEventId());
		vmodel.setEventName(event.getEventName());
		vmodel.setEventType(event.getEventType());
		vmodel.setBlitSaleStartDate(event.getBlitSaleStartDate());
		vmodel.setBlitSaleEndDate(event.getBlitSaleEndDate());
		vmodel.setAddress(event.getAddress());
		vmodel.setDescription(event.getDescription());
		vmodel.setLatitude(event.getLatitude());
		vmodel.setLongitude(event.getLongitude());
		vmodel.setEventLink(event.getEventLink());
		vmodel.setEventState(event.getEventState());
		vmodel.setAparatDisplayCode(event.getAparatDisplayCode());
		vmodel.setOffers(event.getOffers());
		
		vmodel.setEventDates(event.getEventDates().stream()
													.flatMap(ed -> ed.getBlitTypes().stream())
													.map(bt->flatMapper.eventDateToEventDateViewModelFlat(bt.getEventDate(), bt))
													.collect(Collectors.toList()));		
		vmodel.setEventHostId(event.getEventHost().getEventHostId());
		vmodel.setImages(
				event.getImages().stream().map(i -> imageMapper.createFromEntity(i)).collect(Collectors.toList()));
		return vmodel;
	}

	@Override
	public Event updateEntity(EventViewModel viewModel, Event entity) {
		// TODO Auto-generated method stub
		return null;
	}

}
