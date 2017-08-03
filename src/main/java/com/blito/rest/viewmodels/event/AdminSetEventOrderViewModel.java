package com.blito.rest.viewmodels.event;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class AdminSetEventOrderViewModel {
	@NotNull
	long eventId;
	@NotNull
	@Min(0)
	@Max(10)
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
