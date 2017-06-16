package com.blito.rest.viewmodels.blit;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.blito.enums.BlitTypeEnum;

public class BlitViewModel {
	long blitId;
	long blitTypeId;
	@Min(1)
	int count;
	
	double totalAmount;
	
	long userId;
	
	String trackCode;
	
	String eventName;
	
	@NotEmpty
	String eventDateAndTime;
	
	String customerName;
	
	String address;
	
	@NotNull
	BlitTypeEnum type;

	public long getBlitId() {
		return blitId;
	}

	public void setBlitId(long blitId) {
		this.blitId = blitId;
	}

	public long getBlitTypeId() {
		return blitTypeId;
	}

	public void setBlitTypeId(long blitTypeId) {
		this.blitTypeId = blitTypeId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

	public BlitTypeEnum getType() {
		return type;
	}

	public void setType(BlitTypeEnum type) {
		this.type = type;
	}
}
