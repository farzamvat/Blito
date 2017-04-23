package com.blito.models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.blito.enums.ExchangeBlitOperatorState;
import com.blito.enums.ExchangeBlitState;
import com.blito.enums.ExchangeBlitType;

@Entity(name="exchange_blit")
public class ExchangeBlit {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private long exchangeBlitId;
	
	private String fisrtname;
	
	private String lastname;
	
	private Timestamp eventDate;
	
	private double blitCost;
	
	private boolean isBlitoEvent;
	
	private String phoneNumber;
	
	private String email;
	
	private String eventAddress;
	
	private String vendorAddress;
	
	private String description;
	
	@Enumerated(EnumType.STRING)
	private ExchangeBlitState state;
	@Enumerated(EnumType.STRING)
	private ExchangeBlitType type;
	@Enumerated(EnumType.STRING)
	private ExchangeBlitOperatorState operatorState;
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;
	
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

	public ExchangeBlitState getState() {
		return state;
	}

	public void setState(ExchangeBlitState state) {
		this.state = state;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
	
	
}
