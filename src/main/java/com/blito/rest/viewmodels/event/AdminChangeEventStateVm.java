package com.blito.rest.viewmodels.event;

import javax.validation.constraints.NotNull;

import com.blito.enums.State;

public class AdminChangeEventStateVm {
	@NotNull
	long eventId;
	@NotNull
	State state;
	
	public long getEventId() {
		return eventId;
	}
	public void setEventId(long eventId) {
		this.eventId = eventId;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
}
