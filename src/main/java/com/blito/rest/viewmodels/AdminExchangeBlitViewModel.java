package com.blito.rest.viewmodels;

import java.sql.Timestamp;

import com.blito.enums.OperatorState;
import com.blito.enums.ExchangeBlitType;
import com.blito.enums.State;

public class AdminExchangeBlitViewModel {
	private long exchangeBlitId;
	private String firstname;
	private String lastname;
	private Timestamp eventDate;
	private String title;
	private double blitCost;
	private boolean isBlitoEvent;
	private String phoneNumber;
	private String email;
	private String eventAddress;
	private String vendorAddress;
	private String description;
	private State state;
	private ExchangeBlitType type;
	private OperatorState operatorState;
	long userId;
	
	public long getExchangeBlitId() {
		return exchangeBlitId;
	}
	public void setExchangeBlitId(long exchangeBlitId) {
		this.exchangeBlitId = exchangeBlitId;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public Timestamp getEventDate() {
		return eventDate;
	}
	public void setEventDate(Timestamp eventDate) {
		this.eventDate = eventDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public double getBlitCost() {
		return blitCost;
	}
	public void setBlitCost(double blitCost) {
		this.blitCost = blitCost;
	}
	public boolean isBlitoEvent() {
		return isBlitoEvent;
	}
	public void setBlitoEvent(boolean isBlitoEvent) {
		this.isBlitoEvent = isBlitoEvent;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEventAddress() {
		return eventAddress;
	}
	public void setEventAddress(String eventAddress) {
		this.eventAddress = eventAddress;
	}
	public String getVendorAddress() {
		return vendorAddress;
	}
	public void setVendorAddress(String vendorAddress) {
		this.vendorAddress = vendorAddress;
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
	public OperatorState getOperatorState() {
		return operatorState;
	}
	public void setOperatorState(OperatorState operatorState) {
		this.operatorState = operatorState;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
}
