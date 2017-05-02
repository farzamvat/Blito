package com.blito.mappers;

import org.springframework.stereotype.Component;

import com.blito.models.EventDate;
import com.blito.rest.viewmodels.eventdate.EventDateCreateViewModel;

@Component
public class EventDateCreateMapper implements GenericMapper<EventDate,EventDateCreateViewModel> {

	@Override
	public EventDate createFromViewModel(EventDateCreateViewModel vmodel) {
		EventDate eventDate = new EventDate();
		eventDate.setDate(vmodel.getDate());
		eventDate.setDayOfWeek(vmodel.getDayOfWeek());
		return eventDate;
	}

	@Override
	public EventDateCreateViewModel createFromEntity(EventDate entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventDate updateEntity(EventDateCreateViewModel viewModel, EventDate entity) {
		// TODO Auto-generated method stub
		return null;
	}

}
