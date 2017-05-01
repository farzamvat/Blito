package com.blito.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blito.enums.Response;
import com.blito.enums.State;
import com.blito.exceptions.EventHostNotFoundException;
import com.blito.exceptions.EventNotFoundException;
import com.blito.exceptions.ImageNotFoundException;
import com.blito.mappers.EventMapper;
import com.blito.models.BlitType;
import com.blito.models.Event;
import com.blito.models.EventDate;
import com.blito.models.EventHost;
import com.blito.models.Image;
import com.blito.repositories.EventHostRepository;
import com.blito.repositories.EventRepository;
import com.blito.repositories.ImageRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.EventCreateViewModel;
import com.blito.rest.viewmodels.EventViewModel;

@Service
public class EventService {
	@Autowired
	EventMapper eventMapper;
	@Autowired
	EventHostRepository eventHostRepository;
	@Autowired
	ImageRepository imageRepository;
	@Autowired
	EventRepository eventRepository;

	@Transactional
	public Event createEvent(EventCreateViewModel vmodel) {
		if (vmodel.getBlitSaleStartDate().after(vmodel.getBlitSaleEndDate())) {
			throw new RuntimeException("start date is after end date");
		}
		EventHost eventHost = Optional.ofNullable(eventHostRepository.findOne(vmodel.getEventHostId())).map(eh -> eh)
				.orElseThrow(
						() -> new EventHostNotFoundException(ResourceUtil.getMessage(Response.EVENT_HOST_NOT_FOUND)));
		List<Image> images = imageRepository.findByImageUUIDIn(
				vmodel.getImages().stream().map(iv -> iv.getImageUUID()).collect(Collectors.toList()));
		images = images.stream().map(im -> vmodel.getImages().stream()
				.filter(imv -> imv.getImageUUID().equals(im.getImageUUID())).map(imageViewModel -> {
					im.setImageType(imageViewModel.getType());
					return im;
				}).findFirst().map(i -> i)
				.orElseThrow(() -> new ImageNotFoundException(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND))))
				.collect(Collectors.toList());

		Event event = new Event();
		event = eventMapper.eventCreateViewModelToEvent(vmodel, event);
		event.setEventDates(vmodel.getEventDates().stream().map(ed -> {
			EventDate eventDate = new EventDate();
			eventDate = eventMapper.eventDateCreationViewModelToEventDate(ed, eventDate);
			eventDate.setBlitTypes(ed.getBlitTypes().stream().map(bt -> {
				BlitType blitType = new BlitType();
				blitType = eventMapper.blitTypeViewModelToBlitType(bt, blitType);
				blitType.setBlitTypeState(State.CLOSED);
				return blitType;
			}).collect(Collectors.toList()));
			eventDate.setEventState(State.CLOSED);
			return eventDate;
		}).collect(Collectors.toList()));
		event.setImages(images);
		event.setEventHost(eventHost);
		return eventRepository.save(event);
	}
	
	public EventViewModel getEvent(long eventId) {
		Event event = Optional.ofNullable(eventRepository.findOne(eventId))
				.map(e->e)
				.orElseThrow(() -> new EventNotFoundException(ResourceUtil.getMessage(Response.EVENT_NOT_FOUND)));
		return eventMapper.eventToEventViewModel(event);
	}
}
