package com.blito.rest.viewmodels;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import com.blito.annotations.Email;
import com.blito.annotations.MobileNumber;
import com.blito.enums.ExchangeBlitType;
import com.blito.enums.State;

public class UserEditExchangeBlitViewModel {
	private long exchangeBlitId;
	@NotNull
	private String title;
	@NotNull
	private String firstname;
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
	
	private State state;
	
	private ExchangeBlitType type;

	public long getExchangeBlitId() {
		return exchangeBlitId;
	}

	public void setExchangeBlitId(long exchangeBlitId) {
		this.exchangeBlitId = exchangeBlitId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
	
	
}
