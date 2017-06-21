package com.blito.models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.blito.enums.BankGateway;
import com.blito.enums.SeatType;
import com.blito.enums.PaymentStatus;

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
	SeatType seatType;
	PaymentStatus paymentStatus;
	String paymentError;
	String samanBankToken;
	String samanBankRefNumber;
	String samanTraceNo;
	boolean used = false;
	BankGateway bankGateway;
	
	public BankGateway getBankGateway() {
		return bankGateway;
	}
	public void setBankGateway(BankGateway bankGateway) {
		this.bankGateway = bankGateway;
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
	public SeatType getSeatType() {
		return seatType;
	}
	public void setSeatType(SeatType seatType) {
		this.seatType = seatType;
	}
	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getPaymentError() {
		return paymentError;
	}
	public void setPaymentError(String paymentError) {
		this.paymentError = paymentError;
	}
	public String getSamanBankToken() {
		return samanBankToken;
	}
	public void setSamanBankToken(String samanBankToken) {
		this.samanBankToken = samanBankToken;
	}
	public String getSamanBankRefNumber() {
		return samanBankRefNumber;
	}
	public void setSamanBankRefNumber(String samanBankRefNumber) {
		this.samanBankRefNumber = samanBankRefNumber;
	}
}
