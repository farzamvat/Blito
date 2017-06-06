package com.blito.rest.viewmodels.event;

import javax.validation.constraints.NotNull;

import com.blito.enums.OperatorState;

public class AdminChangeEventOperatorStateVm {
	@NotNull
	long eventId;
	@NotNull
	OperatorState operatorState;
	
	public long getEventId() {
		return eventId;
	}
	public void setEventId(long eventId) {
		this.eventId = eventId;
	}
	public OperatorState getOperatorState() {
		return operatorState;
	}
	public void setOperatorState(OperatorState operatorState) {
		this.operatorState = operatorState;
	}
}
