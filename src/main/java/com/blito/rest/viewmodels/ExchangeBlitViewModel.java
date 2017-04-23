package com.blito.rest.viewmodels;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import com.blito.annotations.Email;
import com.blito.annotations.MobileNumber;
import com.blito.enums.ExchangeBlitOperatorState;
import com.blito.enums.ExchangeBlitState;
import com.blito.enums.ExchangeBlitType;

public class ExchangeBlitViewModel {
	private long exchangeBlitId;
	@NotNull
	private String fisrtname;
	@NotNull
	private String lastname;
	@NotNull
	private Timestamp eventDate;
	@NotNull
	private double blitCost;
	@NotNull
	private boolean isBlitoEvent;
	@MobileNumber
	private String phoneNumber;
	@Email
	private String email;
	@NotNull
	private String eventAddress;
	@NotNull
	private String vendorAddress;
	
	private String description;
	
	private ExchangeBlitState state;
	
	private ExchangeBlitType type;
	
	private ExchangeBlitOperatorState operatorState;
	
	public ExchangeBlitOperatorState getOperatorState() {
		return operatorState;
	}

	public void setOperatorState(ExchangeBlitOperatorState operatorState) {
		this.operatorState = operatorState;
	}

	public ExchangeBlitType getType() {
		return type;
	}

	public void setType(ExchangeBlitType type) {
		this.type = type;
	}

	public long getExchangeBlitId() {
		return exchangeBlitId;
	}

	public void setExchangeBlitId(long exchangeBlitId) {
		this.exchangeBlitId = exchangeBlitId;
	}

	public String getFisrtname() {
		return fisrtname;
	}

	public void setFisrtname(String fisrtname) {
		this.fisrtname = fisrtname;
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

	public ExchangeBlitState getState() {
		return state;
	}

	public void setState(ExchangeBlitState state) {
		this.state = state;
	}
}
