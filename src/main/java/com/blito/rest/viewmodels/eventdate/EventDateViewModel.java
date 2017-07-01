package com.blito.rest.viewmodels.eventdate;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.validator.constraints.NotEmpty;

import com.blito.enums.State;
import com.blito.rest.viewmodels.blittype.BlitTypeViewModel;

public class EventDateViewModel {
	long eventDateId;
	@NotEmpty
	Timestamp date;
	@NotEmpty
	Set<BlitTypeViewModel> blitTypes;
	State state;
	
	public long getEventDateId() {
		return eventDateId;
	}

	public void setEventDateId(long eventDateId) {
		this.eventDateId = eventDateId;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public EventDateViewModel()
	{
		blitTypes = new HashSet<>();
	}
	
	public Set<BlitTypeViewModel> getBlitTypes() {
		return blitTypes;
	}
	public void setBlitTypes(Set<BlitTypeViewModel> blitTypes) {
		this.blitTypes = blitTypes;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}
	
}
