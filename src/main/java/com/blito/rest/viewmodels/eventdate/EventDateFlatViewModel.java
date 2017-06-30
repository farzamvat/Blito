package com.blito.rest.viewmodels.eventdate;

import java.sql.Timestamp;

import com.blito.enums.DayOfWeek;
import com.blito.enums.State;

public class EventDateFlatViewModel {
	Timestamp date;
	long eventDateId;
	State eventState;
	
	long blitTypeId;
	String name;
	int capacity;
	int soldCount;
	long price;
	State blitTypeState;
	boolean isFree;

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

	public boolean isFree() {
		return isFree;
	}

	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}
	
	
}
