package com.blito.mappers;

import org.springframework.stereotype.Component;

import com.blito.models.BlitType;
import com.blito.models.Event;
import com.blito.models.EventDate;
import com.blito.rest.viewmodels.BlitTypeCreateViewModel;
import com.blito.rest.viewmodels.EventCreateViewModel;
import com.blito.rest.viewmodels.EventDateCreateViewModel;

@Component
public class EventMapper extends AbstractMapper {
	public Event eventCreateViewModelToEvent(EventCreateViewModel vmodel,Event event)
	{
		event.setEventName(vmodel.getEventName());
		event.setAddress(vmodel.getAddress());
		event.setAparatDisplayCode(vmodel.getAparatDisplayCode());
		event.setBlitSaleEndDate(vmodel.getBlitSaleEndDate());
		event.setBlitSaleStartDate(vmodel.getBlitSalteStartDate());
		event.setDescription(vmodel.getDescription());
		event.setEventType(vmodel.getEventType());
		event.setLatitude(vmodel.getLatitude());
		event.setLongitude(vmodel.getLongitude());
		return event;
	}
	
	public EventDate eventDateCreationViewModelToEventDate(EventDateCreateViewModel vmodel,EventDate eventDate)
	{
		eventDate.setDate(vmodel.getDate());
		eventDate.setDayOfWeek(vmodel.getDayOfWeek());
		return eventDate;
	}
	
	public BlitType blitTypeViewModelToBlitType(BlitTypeCreateViewModel vmodel,BlitType blitType)
	{
		blitType.setName(vmodel.getName());
		blitType.setCount(vmodel.getCount());
		blitType.setFree(vmodel.isFree());
		blitType.setPrice(vmodel.getPrice());
		return blitType;
	}
}
