package com.blito.rest.viewmodels.eventdate;

import javax.validation.constraints.NotNull;

import com.blito.enums.State;

public class ChangeEventDateStateVm {

	@NotNull
	long eventDateId;
	
	@NotNull
	State eventDateState;

	public long getEventDateId() {
		return eventDateId;
	}

	public void setEventDateId(long eventDateId) {
		this.eventDateId = eventDateId;
	}

	public State getEventDateState() {
		return eventDateState;
	}

	public void setEventDateState(State eventDateState) {
		this.eventDateState = eventDateState;
	}
	
}
