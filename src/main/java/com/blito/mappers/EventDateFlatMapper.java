package com.blito.mappers;

import org.springframework.stereotype.Component;

import com.blito.enums.State;
import com.blito.models.BlitType;
import com.blito.models.EventDate;
import com.blito.rest.viewmodels.eventdate.EventDateFlatViewModel;

@Component
public class EventDateFlatMapper implements GenericMapper<EventDate,EventDateFlatViewModel> {

	public EventDateFlatViewModel eventDateToEventDateViewModelFlat(EventDate eventDate, BlitType blitType) {
		EventDateFlatViewModel vmodel = new EventDateFlatViewModel();
		vmodel.setEventDateId(eventDate.getEventDateId());
		vmodel.setDate(eventDate.getDate());
		vmodel.setEventState(Enum.valueOf(State.class, eventDate.getEventDateState()));
		vmodel.setBlitTypeId(blitType.getBlitTypeId());
		vmodel.setName(blitType.getName());
		vmodel.setPrice(blitType.getPrice());
		vmodel.setCapacity(blitType.getCapacity());
		vmodel.setSoldCount(blitType.getSoldCount());
		vmodel.setBlitTypeState(Enum.valueOf(State.class, blitType.getBlitTypeState()));
		vmodel.setFree(blitType.isFree());
		return vmodel;
	}
	
	@Override
	public EventDate createFromViewModel(EventDateFlatViewModel viewModel) {
		return null;
	}

	@Override
	public EventDateFlatViewModel createFromEntity(EventDate entity) {
		return null;
	}

	@Override
	public EventDate updateEntity(EventDateFlatViewModel vmodel, EventDate eventDate) {

		return null;
	}

}
