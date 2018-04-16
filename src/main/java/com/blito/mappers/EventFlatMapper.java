package com.blito.mappers;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blito.enums.EventType;
import com.blito.enums.OfferTypeEnum;
import com.blito.enums.OperatorState;
import com.blito.enums.State;
import com.blito.models.Event;
import com.blito.rest.viewmodels.event.EventFlatViewModel;
import com.blito.rest.viewmodels.event.EventUpdateViewModel;

@Component
public class EventFlatMapper implements GenericMapper<Event,EventFlatViewModel> {

	@Autowired EventDateFlatMapper flatMapper;
	@Autowired ImageMapper imageMapper;
	
	@Override
	public Event createFromViewModel(EventFlatViewModel vmodel) {
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
		event.setEventType(vmodel.getEventType().name());
		event.setLongitude(vmodel.getLongitude());
		event.setLatitude(vmodel.getLatitude());
		event.setEventLink(vmodel.getEventLink());
		event.setOperatorState(OperatorState.PENDING.name());
		return event;
	}
	
//	public EventUpdateViewModel createUpdateViewModelFromEntity(Event event)
//	{
//		EventUpdateViewModel vmodel = new EventUpdateViewModel();
//		vmodel.setEventId(event.getEventId());
//		vmodel.setEventName(event.getEventName());
//		vmodel.setEventType(event.getEventType());
//		vmodel.setBlitSaleStartDate(event.getBlitSaleStartDate());
//		vmodel.setBlitSaleEndDate(event.getBlitSaleEndDate());
//		vmodel.setAddress(event.getAddress());
//		vmodel.setDescription(event.getDescription());
//		vmodel.setLatitude(event.getLatitude());
//		vmodel.setLongitude(event.getLongitude());
//		vmodel.setEventLink(event.getEventLink());
//		vmodel.setAparatDisplayCode(event.getAparatDisplayCode());
//		vmodel.setEventDates(eventDates);
//		
//	}
	
	

	@Override
	public EventFlatViewModel createFromEntity(Event event) {
		EventFlatViewModel vmodel = new EventFlatViewModel();
		vmodel.setEventId(event.getEventId());
		vmodel.setEventName(event.getEventName());
		vmodel.setEventType(Enum.valueOf(EventType.class, event.getEventType()));
		vmodel.setBlitSaleStartDate(event.getBlitSaleStartDate());
		vmodel.setBlitSaleEndDate(event.getBlitSaleEndDate());
		vmodel.setAddress(event.getAddress());
		vmodel.setDescription(event.getDescription());
		vmodel.setLatitude(event.getLatitude());
		vmodel.setLongitude(event.getLongitude());
		vmodel.setEventLink(event.getEventLink());
		vmodel.setEventState(Enum.valueOf(State.class, event.getEventState()));
		vmodel.setAparatDisplayCode(event.getAparatDisplayCode());
		vmodel.setEvento(event.isEvento());
		vmodel.setOffers(event.getOffers().stream().map(offer -> Enum.valueOf(OfferTypeEnum.class, offer)).collect(Collectors.toSet()));
		vmodel.setCreatedAt(event.getCreatedAt());
		vmodel.setEndDate(event.getEndDate());
		vmodel.setMembers(event.getMembers());
		vmodel.setOperatorState(Enum.valueOf(OperatorState.class, event.getOperatorState()));
		vmodel.setOrderNumber(event.getOrderNumber());
		vmodel.setDeleted(event.isDeleted());
		vmodel.setViews(event.getViews());
		vmodel.setEventHostName(event.getEventHost().getHostName());
		vmodel.setEventDates(event.getEventDates().stream()
													.flatMap(ed -> ed.getBlitTypes().stream())
													.map(bt->flatMapper.eventDateToEventDateViewModelFlat(bt.getEventDate(), bt))
													.collect(Collectors.toSet()));		
		vmodel.setEventHostId(event.getEventHost().getEventHostId());
		if(!event.getImages().isEmpty())
			vmodel.setImages(imageMapper.createFromEntities(event.getImages()));
		vmodel.setAdditionalFields(event.getAdditionalFields());
		vmodel.setPrivate(event.isPrivate());
		return vmodel;
	}

	@Override
	public Event updateEntity(EventFlatViewModel vmodel, Event event) {
		event.setEventName(vmodel.getEventName());
		event.setAddress(vmodel.getAddress());
		event.setAparatDisplayCode(vmodel.getAparatDisplayCode());
		event.setBlitSaleEndDate(vmodel.getBlitSaleEndDate());
		event.setBlitSaleStartDate(vmodel.getBlitSaleStartDate());
		event.setDescription(vmodel.getDescription());
		event.setEventType(vmodel.getEventType().name());
		event.setLatitude(vmodel.getLatitude());
		event.setLongitude(vmodel.getLongitude());
		event.setOperatorState(OperatorState.PENDING.name());
		event.setEventState(State.CLOSED.name());
		event.setPrivate(vmodel.isPrivate());
		return event;
	}

}
