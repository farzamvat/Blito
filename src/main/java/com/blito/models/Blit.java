package com.blito.models;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name="blit")
@Table
@Inheritance(strategy=InheritanceType.JOINED)
public class Blit {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	long blitId;
	int count;
	long totalAmount;
	@ManyToOne(optional=true)
	@JoinColumn(name="userId")
	User user;
	String trackCode;
	String eventName;
	String eventDateAndTime;
	Timestamp createdAt;
	Timestamp eventDate;
	String customerName;
	String customerMobileNumber;
	String customerEmail;
	String eventAddress;
	String blitTypeName;
	String seatType;
	String paymentStatus;
	String paymentError;
	String token;
	String refNum;
	String samanTraceNo;
	boolean used = false;
	String bankGateway;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@Column(name="fields")
	private Map<String,String> additionalFields;
	
	public Blit()
	{
		additionalFields = new HashMap<String,String>();
	}
	
	public Map<String, String> getAdditionalFields() {
		return additionalFields;
	}

	public void setAdditionalFields(Map<String, String> additionalFields) {
		this.additionalFields = additionalFields;
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
	public long getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(long totalAmount) {
		this.totalAmount = totalAmount;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
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
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public Timestamp getEventDate() {
		return eventDate;
	}
	public void setEventDate(Timestamp eventDate) {
		this.eventDate = eventDate;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerMobileNumber() {
		return customerMobileNumber;
	}
	public void setCustomerMobileNumber(String customerMobileNumber) {
		this.customerMobileNumber = customerMobileNumber;
	}
	public String getCustomerEmail() {
		return customerEmail;
	}
	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}
	public String getEventAddress() {
		return eventAddress;
	}
	public void setEventAddress(String eventAddress) {
		this.eventAddress = eventAddress;
	}
	public String getBlitTypeName() {
		return blitTypeName;
	}
	public void setBlitTypeName(String blitTypeName) {
		this.blitTypeName = blitTypeName;
	}
	public String getSeatType() {
		return seatType;
	}
	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getPaymentError() {
		return paymentError;
	}
	public void setPaymentError(String paymentError) {
		this.paymentError = paymentError;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getRefNum() {
		return refNum;
	}
	public void setRefNum(String refNum) {
		this.refNum = refNum;
	}
	public String getSamanTraceNo() {
		return samanTraceNo;
	}
	public void setSamanTraceNo(String samanTraceNo) {
		this.samanTraceNo = samanTraceNo;
	}
	public boolean isUsed() {
		return used;
	}
	public void setUsed(boolean used) {
		this.used = used;
	}
	public String getBankGateway() {
		return bankGateway;
	}
	public void setBankGateway(String bankGateway) {
		this.bankGateway = bankGateway;
	}
}
