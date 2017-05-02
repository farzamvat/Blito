package com.blito.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blito.enums.Response;
import com.blito.enums.State;
import com.blito.exceptions.EventHostNotFoundException;
import com.blito.exceptions.EventNotFoundException;
import com.blito.mappers.BlitTypeMapper;
import com.blito.mappers.EventDateCreateMapper;
import com.blito.mappers.EventMapper;
import com.blito.mappers.ImageMapper;
import com.blito.models.BlitType;
import com.blito.models.Event;
import com.blito.models.EventDate;
import com.blito.models.EventHost;
import com.blito.models.Image;
import com.blito.repositories.EventHostRepository;
import com.blito.repositories.EventRepository;
import com.blito.repositories.ImageRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.event.EventCreateViewModel;
import com.blito.rest.viewmodels.event.EventUpdateViewModel;
import com.blito.rest.viewmodels.event.EventViewModel;

@Service
public class EventService {
	@Autowired
	EventMapper eventMapper;
	@Autowired
	EventDateCreateMapper eventDateCreateMapper;
	@Autowired
	EventHostRepository eventHostRepository;
	@Autowired
	BlitTypeMapper blitTypeMapper;
	@Autowired
	ImageRepository imageRepository;
	@Autowired
	EventRepository eventRepository;
	@Autowired
	ImageMapper imageMapper;

	public Event createEvent(EventCreateViewModel vmodel) {
		if (vmodel.getBlitSaleStartDate().after(vmodel.getBlitSaleEndDate())) {
			throw new RuntimeException("start date is after end date");
		}
		EventHost eventHost = Optional.ofNullable(eventHostRepository.findOne(vmodel.getEventHostId())).map(eh -> eh)
				.orElseThrow(
						() -> new EventHostNotFoundException(ResourceUtil.getMessage(Response.EVENT_HOST_NOT_FOUND)));
		List<Image> images = imageRepository.findByImageUUIDIn(
				vmodel.getImages().stream().map(iv -> iv.getImageUUID()).collect(Collectors.toList()));
		images = imageMapper.setImageTypeFromImageViewModels(images, vmodel.getImages());

		Event event = eventMapper.eventCreateViewModelToEvent(vmodel);
		event.setEventDates(vmodel.getEventDates().stream().map(ed -> {
			EventDate eventDate = eventDateCreateMapper.createFromViewModel(ed);
			eventDate.setBlitTypes(ed.getBlitTypes().stream().map(bt -> {
				BlitType blitType = blitTypeMapper.createFromBlitTypeCreateViewModel(bt);
				blitType.setBlitTypeState(State.CLOSED);
				return blitType;
			}).collect(Collectors.toList()));
			eventDate.setEventState(State.CLOSED);
			return eventDate;
		}).collect(Collectors.toList()));
		event.setImages(images);
		event.setEventHost(eventHost);
		//
		event.setEventLink(generateEventLink(event));
		return eventRepository.save(event);
	}
	
	public EventViewModel getEvent(long eventId) {
		Event event = Optional.ofNullable(eventRepository.findOne(eventId))
				.map(e->e)
				.orElseThrow(() -> new EventNotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND)));
		return eventMapper.createFromEntity(event);
	}
	
	public EventViewModel update(EventUpdateViewModel vmodel) {
		if (vmodel.getBlitSaleStartDate().after(vmodel.getBlitSaleEndDate())) {
			throw new RuntimeException("start date is after end date");
		}
		EventHost eventHost = Optional.ofNullable(eventHostRepository.findOne(vmodel.getEventHostId())).map(eh -> eh)
				.orElseThrow(
						() -> new EventHostNotFoundException(ResourceUtil.getMessage(Response.EVENT_HOST_NOT_FOUND)));
		List<Image> images = imageRepository.findByImageUUIDIn(
				vmodel.getImages().stream().map(iv -> iv.getImageUUID()).collect(Collectors.toList()));
		images = imageMapper.setImageTypeFromImageViewModels(images, vmodel.getImages());

		Event event = Optional.ofNullable(eventRepository.findOne(vmodel.getEventId()))
				.map(e -> e)
				.orElseThrow(() -> new EventNotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND)));
		
		event = eventMapper.updateEventFromUpdateViewModel(vmodel, event);
		event.setEventDates(vmodel.getEventDates().stream().map(ed -> {
			EventDate eventDate = eventDateCreateMapper.createFromViewModel(ed);
			eventDate.setBlitTypes(ed.getBlitTypes().stream().map(bt -> {
				BlitType blitType = blitTypeMapper.createFromBlitTypeCreateViewModel(bt);
				blitType.setBlitTypeState(State.CLOSED);
				return blitType;
			}).collect(Collectors.toList()));
			eventDate.setEventState(State.CLOSED);
			return eventDate;
		}).collect(Collectors.toList()));
		event.setImages(images);
		event.setEventHost(eventHost);
		//
		event.setEventLink(generateEventLink(event));
		return eventMapper.createFromEntity(eventRepository.save(event));
	}
	
	
	private String generateEventLink(Event event)
	{
		String eventLink = event.getEventName().replaceAll(" ", "-") + '-' + RandomUtil.generateLinkRandomNumber();
		while(eventRepository.findByEventLink(eventLink).isPresent())
		{
			eventLink = event.getEventName().replaceAll(" ", "-") + '-' + RandomUtil.generateLinkRandomNumber();
		}
		return eventLink;
	}
}
