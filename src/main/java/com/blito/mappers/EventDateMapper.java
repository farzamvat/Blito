package com.blito.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blito.enums.State;
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
		eventDate.setEventDateState(State.CLOSED);
		vmodel.getBlitTypes().forEach(bt -> {
			eventDate.addBlitType(blitTypeMapper.createFromViewModel(bt));
		});
		return eventDate;
	}

	@Override
	public EventDateViewModel createFromEntity(EventDate eventDate) {
		EventDateViewModel vmodel = new EventDateViewModel();
		vmodel.setDate(eventDate.getDate());
		vmodel.setDayOfWeek(eventDate.getDayOfWeek());
		vmodel.setBlitTypes(eventDate.getBlitTypes().stream().map(blitTypeMapper::createFromEntity).collect(Collectors.toList()));
		vmodel.setEventDateId(eventDate.getEventDateId());
		vmodel.setState(eventDate.getEventDateState());
		return vmodel;
	}

	@Override
	public EventDate updateEntity(EventDateViewModel vmodel, EventDate eventDate) {
		eventDate.setDate(vmodel.getDate());
		eventDate.setDayOfWeek(vmodel.getDayOfWeek());
		
		
		List<Long> oldOnes = vmodel.getBlitTypes().stream().map(b -> b.getBlitTypeId()).filter(id -> id > 0).collect(Collectors.toList());
		List<Long> shouldDelete = new ArrayList<>();
		eventDate.getBlitTypes().forEach(bt -> {
			if(!oldOnes.contains(bt.getBlitTypeId()))
			{
				shouldDelete.add(bt.getBlitTypeId());
			}
		});
		shouldDelete.forEach(id -> {
			eventDate.removeBlitTypeById(id);
		});
		
		vmodel.getBlitTypes().stream().forEach(bvm -> {
			Optional<BlitType> optionalBlitType =
					eventDate.getBlitTypes().stream().filter(b -> b.getBlitTypeId() == bvm.getBlitTypeId()).findFirst();
			if(optionalBlitType.isPresent())
			{
				blitTypeMapper.updateEntity(bvm, optionalBlitType.get());
			}
			else {
				eventDate.addBlitType(blitTypeMapper.createFromViewModel(bvm));
			}
		});
	
		eventDate.setEventDateState(State.CLOSED);
		return eventDate;
	}

}
