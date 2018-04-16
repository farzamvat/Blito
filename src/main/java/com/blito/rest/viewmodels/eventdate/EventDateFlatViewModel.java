package com.blito.rest.viewmodels.eventdate;

import java.sql.Timestamp;

import com.blito.enums.State;
import com.blito.rest.viewmodels.View;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

public class EventDateFlatViewModel {
	@JsonView(View.EventDateFlat.class)
	Timestamp date;
	@JsonView(View.EventDateFlat.class)
	long eventDateId;
	@JsonView(View.EventDateFlat.class)
	State eventState;
	@JsonView(View.EventDateFlat.class)
	long blitTypeId;
	@JsonView(View.EventDateFlat.class)
	String name;
	@JsonView(View.EventDateFlat.class)
	int capacity;
	@JsonView(View.EventDateFlat.class)
	int soldCount;
	@JsonView(View.EventDateFlat.class)
	long price;
	@JsonView(View.EventDateFlat.class)
	State blitTypeState;
	@JsonView(View.EventDateFlat.class)
	boolean isFree;
	@JsonView(View.EventDateFlat.class)
	private String dateTime;

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public long getEventDateId() {
		return eventDateId;
	}

	public void setEventDateId(long eventDateId) {
		this.eventDateId = eventDateId;
	}

	public State getEventState() {
		return eventState;
	}

	public void setEventState(State eventState) {
		this.eventState = eventState;
	}

	public long getBlitTypeId() {
		return blitTypeId;
	}

	public void setBlitTypeId(long blitTypeId) {
		this.blitTypeId = blitTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getSoldCount() {
		return soldCount;
	}

	public void setSoldCount(int soldCount) {
		this.soldCount = soldCount;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public State getBlitTypeState() {
		return blitTypeState;
	}

	public void setBlitTypeState(State blitTypeState) {
		this.blitTypeState = blitTypeState;
	}
	@JsonProperty("isFree")
	public boolean isFree() {
		return isFree;
	}

	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}
	
	
}
