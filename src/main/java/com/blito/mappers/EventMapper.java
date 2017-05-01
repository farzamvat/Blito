package com.blito.mappers;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blito.enums.OperatorState;
import com.blito.enums.State;
import com.blito.models.BlitType;
import com.blito.models.Event;
import com.blito.models.EventDate;
import com.blito.rest.viewmodels.BlitTypeCreateViewModel;
import com.blito.rest.viewmodels.BlitTypeViewModel;
import com.blito.rest.viewmodels.EventCreateViewModel;
import com.blito.rest.viewmodels.EventDateCreateViewModel;
import com.blito.rest.viewmodels.EventDateViewModel;
import com.blito.rest.viewmodels.EventViewModel;

@Component
public class EventMapper extends AbstractMapper {

	@Autowired
	ImageMapper imageMapper;

	public Event eventCreateViewModelToEvent(EventCreateViewModel vmodel, Event event) {
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

	public EventDate eventDateCreationViewModelToEventDate(EventDateCreateViewModel vmodel, EventDate eventDate) {
		eventDate.setDate(vmodel.getDate());
		eventDate.setDayOfWeek(vmodel.getDayOfWeek());
		return eventDate;
	}

	public BlitType blitTypeViewModelToBlitType(BlitTypeCreateViewModel vmodel, BlitType blitType) {
		blitType.setName(vmodel.getName());
		blitType.setCapacity(vmodel.getCapacity());
		blitType.setFree(vmodel.isFree());
		blitType.setPrice(vmodel.getPrice());
		return blitType;
	}

	public EventViewModel eventToEventViewModel(Event event) {
		EventViewModel vmodel = new EventViewModel();
		vmodel.setEventId(event.getEventId());
		vmodel.setEventname(event.getEventName());
		vmodel.setEventType(event.getEventType());
		vmodel.setBlitSaleStartDate(event.getBlitSaleStartDate());
		vmodel.setBlitSaleEndDate(event.getBlitSaleEndDate());
		vmodel.setAddress(event.getAddress());
		vmodel.setDescrption(event.getDescription());
		vmodel.setLatitude(event.getLatitude());
		vmodel.setLongitude(event.getLongitude());
		vmodel.setEventLink(event.getEventLink());
		vmodel.setEventState(event.getEventState());
		vmodel.setAparatDisplayCode(event.getAparatDisplayCode());
		vmodel.setOffers(event.getOffers());
//		vmodel.setEventDates(event.getEventDates().stream()
//													.map(ed -> ed.getBlitTypes())
//													.flatMap(et -> et.stream())
//													.distinct()
//													.collect(Collectors.toList()));
		vmodel.setEventHostId(event.getEventHost().getEventHostId());
		vmodel.setImages(
				event.getImages().stream().map(i -> imageMapper.imageToImageViewModel(i)).collect(Collectors.toList()));
		return vmodel;
	}

	

	public EventDateViewModel eventDateToEventDateViewModelFlat(EventDate eventDate, BlitType blitType) {
		EventDateViewModel vmodel = new EventDateViewModel();
		vmodel.setEventDateId(eventDate.getEventDateId());
		vmodel.setDate(eventDate.getDate());
		vmodel.setDayOfWeek(eventDate.getDayOfWeek());
		vmodel.setEventState(eventDate.getEventState());
		vmodel.setBlitTypeId(blitType.getBlitTypeId());
		vmodel.setName(blitType.getName());
		vmodel.setPrice(blitType.getPrice());
		vmodel.setCapacity(blitType.getCapacity());
		vmodel.setSoldCount(blitType.getSoldCount());
		vmodel.setBlitTypeState(blitType.getBlitTypeState());
		vmodel.setFree(blitType.isFree());
		return vmodel;
	}

	public BlitTypeViewModel blitTypeToBlitTypeViewModel(BlitType blitType) {
		BlitTypeViewModel vmodel = new BlitTypeViewModel();
		vmodel.setBlitTypeId(blitType.getBlitTypeId());
		vmodel.setName(blitType.getName());
		vmodel.setPrice(blitType.getPrice());
		vmodel.setCapacity(blitType.getCapacity());
		vmodel.setSoldCount(blitType.getSoldCount());
		vmodel.setBlitTypeState(blitType.getBlitTypeState());
		vmodel.setFree(blitType.isFree());
		return vmodel;
	}
}
