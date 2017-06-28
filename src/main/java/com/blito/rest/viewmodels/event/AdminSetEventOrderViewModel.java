package com.blito.rest.viewmodels.event;

import javax.validation.constraints.NotNull;

public class AdminSetEventOrderViewModel {
	@NotNull
	long eventId;
	@NotNull
	int order;
	public long getEventId() {
		return eventId;
	}
	public void setEventId(long eventId) {
		this.eventId = eventId;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	
}
