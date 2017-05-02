package com.blito.mappers;

import org.springframework.stereotype.Component;

import com.blito.models.BlitType;
import com.blito.models.EventDate;
import com.blito.rest.viewmodels.eventdate.EventDateFlatViewModel;

@Component
public class EventDateFlatMapper implements GenericMapper<EventDate,EventDateFlatViewModel> {

	public EventDateFlatViewModel eventDateToEventDateViewModelFlat(EventDate eventDate, BlitType blitType) {
		EventDateFlatViewModel vmodel = new EventDateFlatViewModel();
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
	
	@Override
	public EventDate createFromViewModel(EventDateFlatViewModel viewModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventDateFlatViewModel createFromEntity(EventDate entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventDate updateEntity(EventDateFlatViewModel viewModel, EventDate entity) {
		// TODO Auto-generated method stub
		return null;
	}

}
