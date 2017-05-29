package com.blito.mappers;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blito.models.BlitType;
import com.blito.models.EventDate;
import com.blito.rest.viewmodels.eventdate.EventDateViewModel;

@Component
public class EventDateMapper implements GenericMapper<EventDate,EventDateViewModel> {

	@Autowired
	BlitTypeMapper blitTypeMapper;
	
	@Override
	public EventDate createFromViewModel(EventDateViewModel vmodel) {
		EventDate eventDate = new EventDate();
		eventDate.setDate(vmodel.getDate());
		eventDate.setDayOfWeek(vmodel.getDayOfWeek());
		vmodel.getBlitTypes().forEach(bt -> {
			eventDate.addBlitType(blitTypeMapper.createFromViewModel(bt));
		});
//		eventDate.setBlitTypes(vmodel.getBlitTypes().stream().map(blitTypeMapper::createFromViewModel).collect(Collectors.toList()));
		return eventDate;
	}

	@Override
	public EventDateViewModel createFromEntity(EventDate eventDate) {
		EventDateViewModel vmodel = new EventDateViewModel();
		vmodel.setDate(eventDate.getDate());
		vmodel.setDayOfWeek(eventDate.getDayOfWeek());
		vmodel.setBlitTypes(eventDate.getBlitTypes().stream().map(blitTypeMapper::createFromEntity).collect(Collectors.toList()));
		vmodel.setEventDateId(eventDate.getEventDateId());
		return vmodel;
	}

	@Override
	public EventDate updateEntity(EventDateViewModel vmodel, EventDate eventDate) {
		eventDate.setDate(vmodel.getDate());
		eventDate.setDayOfWeek(vmodel.getDayOfWeek());
		eventDate.setBlitTypes(vmodel.getBlitTypes().stream().map(bvm -> {
			Optional<BlitType> optionalBlitType =
					eventDate.getBlitTypes().stream().filter(b -> b.getBlitTypeId() == bvm.getBlitTypeId()).findFirst();
			if(optionalBlitType.isPresent())
			{
				return blitTypeMapper.updateEntity(bvm, optionalBlitType.get());
			}
			else {
				return blitTypeMapper.createFromViewModel(bvm);
			}
		}).collect(Collectors.toList()));
		eventDate.setEventState(vmodel.getState());
		return eventDate;
	}

}
