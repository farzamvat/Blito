package com.blito.rest.viewmodels.event;

import javax.validation.constraints.NotNull;

public class AdminSetIsEventoViewModel {

	@NotNull
	long eventId;
	@NotNull
	boolean isEvento;
	
	public long getEventId() {
		return eventId;
	}
	public void setEventId(long eventId) {
		this.eventId = eventId;
	}
	public boolean isEvento() {
		return isEvento;
	}
	public void setEvento(boolean isEvento) {
		this.isEvento = isEvento;
	}
}
