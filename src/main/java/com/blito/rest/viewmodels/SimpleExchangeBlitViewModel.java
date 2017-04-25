package com.blito.rest.viewmodels;

import java.sql.Timestamp;

import com.blito.enums.OperatorState;
import com.blito.enums.State;
import com.blito.enums.ExchangeBlitType;

public class SimpleExchangeBlitViewModel {
	private long exchangeBlitId;
	private String title;
	private Timestamp eventDate;
	private double blitCost;
	private State state;
	private ExchangeBlitType type;
	private OperatorState operatorState;

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getExchangeBlitId() {
		return exchangeBlitId;
	}
	public void setExchangeBlitId(long exchangeBlitId) {
		this.exchangeBlitId = exchangeBlitId;
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
	public OperatorState getOperatorState() {
		return operatorState;
	}
	public void setOperatorState(OperatorState operatorState) {
		this.operatorState = operatorState;
	}
	
}
