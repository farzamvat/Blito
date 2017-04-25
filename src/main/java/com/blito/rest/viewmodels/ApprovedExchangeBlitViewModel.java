package com.blito.rest.viewmodels;

import java.sql.Timestamp;

import com.blito.enums.State;
import com.blito.enums.ExchangeBlitType;

public class ApprovedExchangeBlitViewModel {
	private String title;
	private Timestamp eventDate;
	private double blitCost;
	private String description;
	private State state;
	private ExchangeBlitType type;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Timestamp getEventDate() {
		return eventDate;
	}
	public void setEventDate(Timestamp eventDate) {
		this.eventDate = eventDate;
	}
	public double getBlitCost() {
		return blitCost;
	}
	public void setBlitCost(double blitCost) {
		this.blitCost = blitCost;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public ExchangeBlitType getType() {
		return type;
	}
	public void setType(ExchangeBlitType type) {
		this.type = type;
	}
}
