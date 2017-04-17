package com.blito.models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="exchange_blit")
public class ExchangeBlit {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private String exchangeBlitId;
	
	private BlitType blitType;
	
	private String fisrtname;
	
	private String lastname;
	
	private Timestamp eventDate;
	
	private double blitCost;
	
	private boolean isBlitoEvent;
	
	private long trackingCode;
	
	private String phoneNumber;
	
	private String email;
	
	private String eventAddress;
	
	private String vendorAddress;
	
	private String description;

	public String getExchangeBlitId() {
		return exchangeBlitId;
	}

	public void setExchangeBlitId(String exchangeBlitId) {
		this.exchangeBlitId = exchangeBlitId;
	}

	public BlitType getBlitType() {
		return blitType;
	}

	public void setBlitType(BlitType blitType) {
		this.blitType = blitType;
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

	public long getTrackingCode() {
		return trackingCode;
	}

	public void setTrackingCode(long trackingCode) {
		this.trackingCode = trackingCode;
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
