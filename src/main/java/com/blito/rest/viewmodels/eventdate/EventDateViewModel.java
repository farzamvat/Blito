package com.blito.rest.viewmodels.eventdate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.blito.enums.DayOfWeek;
import com.blito.enums.State;
import com.blito.rest.viewmodels.blittype.BlitTypeViewModel;

public class EventDateViewModel {
	long eventDateId;
	DayOfWeek dayOfWeek;
	@NotEmpty
	Timestamp date;
	@NotNull
	List<BlitTypeViewModel> blitTypes;
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
		blitTypes = new ArrayList<>();
	}
	
	public List<BlitTypeViewModel> getBlitTypes() {
		return blitTypes;
	}
	public void setBlitTypes(List<BlitTypeViewModel> blitTypes) {
		this.blitTypes = blitTypes;
	}

	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(DayOfWeek dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}
	
}
