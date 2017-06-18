package com.blito.mappers;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blito.enums.OperatorState;
import com.blito.enums.State;
import com.blito.models.Event;
import com.blito.models.EventDate;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.eventdate.EventDateViewModel;

@Component
public class EventMapper implements GenericMapper<Event, EventViewModel> {

	@Autowired
	EventDateMapper eventDateMapper;
	@Autowired
	ImageMapper imageMapper;

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
		vmodel.getEventDates().forEach(ed -> {
			event.addEventDate(eventDateMapper.createFromViewModel(ed));
		});
		event.setCreatedAt(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()));
		event.setEventState(State.CLOSED);
		event.setEvento(false);
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
		vmodel.setOffers(event.getOffers());
		vmodel.setLatitude(event.getLatitude());
		vmodel.setLongitude(event.getLongitude());
		vmodel.setEvento(event.isEvento());
		vmodel.setImages(imageMapper.createFromEntities(event.getImages()));
		return vmodel;
	}

	@Override
	public Event updateEntity(EventViewModel vmodel, Event event) {
		event.setAddress(vmodel.getAddress());
		event.setAparatDisplayCode(event.getAparatDisplayCode());
		event.setBlitSaleEndDate(vmodel.getBlitSaleStartDate());
		event.setBlitSaleEndDate(vmodel.getBlitSaleEndDate());
		event.setDescription(vmodel.getDescription());
		event.setEventName(vmodel.getEventName());
		event.setEventState(State.CLOSED);
		event.setOperatorState(OperatorState.PENDING);
		event.setLongitude(vmodel.getLongitude());
		event.setLatitude(vmodel.getLatitude());
		event.setEventLink(vmodel.getEventLink());
		event.setEventType(vmodel.getEventType());
		event.setEventDates(vmodel.getEventDates().stream().map(edvm -> {
			return event.getEventDates().stream().filter(ed -> ed.getEventDateId() == edvm.getEventDateId()).findFirst()
					.map(e -> eventDateMapper.updateEntity(edvm, e))
					.orElseGet(() -> {EventDate ed = eventDateMapper.createFromViewModel(edvm);
										ed.setEvent(event);
										return ed;});
		}).collect(Collectors.toList()));
		return event;
	}
}
