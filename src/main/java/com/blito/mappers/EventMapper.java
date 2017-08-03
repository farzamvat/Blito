package com.blito.mappers;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blito.enums.EventType;
import com.blito.enums.OfferTypeEnum;
import com.blito.enums.OperatorState;
import com.blito.enums.State;
import com.blito.models.Event;
import com.blito.models.EventDate;
import com.blito.rest.viewmodels.event.EventViewModel;

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
		event.setEventType(vmodel.getEventType().name());
		event.setLatitude(vmodel.getLatitude());
		event.setLongitude(vmodel.getLongitude());
		event.setOperatorState(OperatorState.PENDING.name());
		vmodel.getEventDates().forEach(ed -> {
			event.addEventDate(eventDateMapper.createFromViewModel(ed));
		});
		event.setAdditionalFields(vmodel.getAdditionalFields());
		event.setMembers(vmodel.getMembers());
		event.setCreatedAt(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()));
		event.setEventState(State.CLOSED.name());
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
		vmodel.setEventType(Enum.valueOf(EventType.class, event.getEventType()));
		vmodel.setOrderNumber(event.getOrderNumber());
		vmodel.setOperatorState(Enum.valueOf(OperatorState.class, event.getOperatorState()));
		vmodel.setEventState(Enum.valueOf(State.class, event.getEventState()));
		vmodel.setEventDates(eventDateMapper.createFromEntities(event.getEventDates()));
		vmodel.setOffers(event.getOffers().stream().map(offer -> Enum.valueOf(OfferTypeEnum.class,offer)).collect(Collectors.toSet()));
		vmodel.setLatitude(event.getLatitude());
		vmodel.setLongitude(event.getLongitude());
		vmodel.setEvento(event.isEvento());
		vmodel.setImages(imageMapper.createFromEntities(event.getImages()));
		vmodel.setCreatedAt(event.getCreatedAt());
		vmodel.setMembers(event.getMembers());
		vmodel.setEventSoldDate(event.getEventSoldDate());
		vmodel.setDeleted(event.isDeleted());
		vmodel.setViews(event.getViews());
		vmodel.setAdditionalFields(event.getAdditionalFields());
		return vmodel;
	}

	@Override
	public Event updateEntity(EventViewModel vmodel, Event event) {
		event.setAddress(vmodel.getAddress());
		event.setAparatDisplayCode(event.getAparatDisplayCode());
		event.setBlitSaleStartDate(vmodel.getBlitSaleStartDate());
		event.setBlitSaleEndDate(vmodel.getBlitSaleEndDate());
		event.setDescription(vmodel.getDescription());
		event.setEventName(vmodel.getEventName());
		event.setEventState(State.CLOSED.name());
		event.setOperatorState(OperatorState.PENDING.name());
		event.setLongitude(vmodel.getLongitude());
		event.setLatitude(vmodel.getLatitude());
		event.setEventLink(vmodel.getEventLink());
		event.setEventType(vmodel.getEventType().name());
		event.setMembers(vmodel.getMembers());
		
		List<Long> oldOnes = vmodel.getEventDates().stream().map(b -> b.getEventDateId()).filter(id -> id > 0).collect(Collectors.toList());
		List<Long> shouldDelete = new ArrayList<>();
		event.getEventDates().forEach(bt -> {
			if(!oldOnes.contains(bt.getEventDateId()))
			{
				shouldDelete.add(bt.getEventDateId());
			}
		});
		shouldDelete.forEach(id -> {
			event.removeEventDateById(id);
		});
		

		vmodel.getEventDates().stream().forEach(edvm -> {
			Optional<EventDate> optionalEventDate = event.getEventDates().stream().filter(ed -> ed.getEventDateId() == edvm.getEventDateId()).findFirst();
			if(optionalEventDate.isPresent())
			{
				eventDateMapper.updateEntity(edvm, optionalEventDate.get());
			}
			else {
				event.addEventDate(eventDateMapper.createFromViewModel(edvm));
			}

		});
		
		return event;
	}
}
