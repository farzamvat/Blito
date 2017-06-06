package com.blito.rest.viewmodels.event;

import java.sql.Timestamp;

import com.blito.enums.EventType;
import com.blito.enums.OperatorState;
import com.blito.enums.State;

public class EventSimpleViewModel {
	long eventId;
	String eventName;
	EventType eventType;
	State eventState;
	OperatorState operatorState;
	Timestamp blitSaleStartDate;
	Timestamp blitSaleEndDate;
	
	public long getEventId() {
		return eventId;
	}
	public void setEventId(long eventId) {
		this.eventId = eventId;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public EventType getEventType() {
		return eventType;
	}
	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
	public State getEventState() {
		return eventState;
	}
	public void setEventState(State eventState) {
		this.eventState = eventState;
	}
	public OperatorState getOperatorState() {
		return operatorState;
	}
	public void setOperatorState(OperatorState operatorState) {
		this.operatorState = operatorState;
	}
	public Timestamp getBlitSaleStartDate() {
		return blitSaleStartDate;
	}
	public void setBlitSaleStartDate(Timestamp blitSaleStartDate) {
		this.blitSaleStartDate = blitSaleStartDate;
	}
	public Timestamp getBlitSaleEndDate() {
		return blitSaleEndDate;
	}
	public void setBlitSaleEndDate(Timestamp blitSaleEndDate) {
		this.blitSaleEndDate = blitSaleEndDate;
	}
}
