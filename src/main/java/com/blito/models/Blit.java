package com.blito.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.blito.enums.BlitTypeEnum;

@Entity(name="blit")
@Table
@Inheritance(strategy=InheritanceType.JOINED)
public class Blit {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	long blitId;
	int count;
	long totalAmount;
	@ManyToOne
	@JoinColumn(name="userId")
	User user;
	String trackCode;
	String eventName;
	String eventDateAndTime;
	String customerName;
	String address;
	BlitTypeEnum type;
	
	public BlitTypeEnum getType() {
		return type;
	}

	public void setType(BlitTypeEnum type) {
		this.type = type;
	}

	public long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(long totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getTrackCode() {
		return trackCode;
	}

	public void setTrackCode(String trackCode) {
		this.trackCode = trackCode;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventDateAndTime() {
		return eventDateAndTime;
	}

	public void setEventDateAndTime(String eventDateAndTime) {
		this.eventDateAndTime = eventDateAndTime;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getBlitId() {
		return blitId;
	}

	public void setBlitId(long blitId) {
		this.blitId = blitId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
