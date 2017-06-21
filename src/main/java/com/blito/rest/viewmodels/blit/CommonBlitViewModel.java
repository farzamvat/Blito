package com.blito.rest.viewmodels.blit;

import java.sql.Timestamp;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.blito.annotations.Email;
import com.blito.annotations.MobileNumber;
import com.blito.enums.BankGateway;
import com.blito.enums.SeatType;
import com.blito.enums.PaymentStatus;
import com.blito.rest.viewmodels.View;
import com.fasterxml.jackson.annotation.JsonView;

public class CommonBlitViewModel {
	@JsonView(View.SimpleBlit.class)
	long blitId;
	@NotEmpty
	@JsonView(View.SimpleBlit.class)
	long blitTypeId;
	@JsonView(View.SimpleBlit.class)
	@Min(1)
	@Max(10)
	int count;
	@JsonView(View.SimpleBlit.class)
	long totalAmount;
	@JsonView(View.SimpleBlit.class)
	String trackCode;
	@JsonView(View.SimpleBlit.class)
	@NotEmpty
	String eventName;
	@JsonView(View.Blit.class)
	@NotEmpty
	String eventDateAndTime;
	@JsonView(View.Blit.class)
	@NotEmpty
	String customerName;
	@JsonView(View.Blit.class)
	@NotNull
	Timestamp eventDate;
	@JsonView(View.Blit.class)
	@MobileNumber
	String customerMobileNumber;
	@JsonView(View.Blit.class)
	@Email
	String customerEmail;
	@JsonView(View.Blit.class)
	@NotEmpty
	String eventAddress;
	@JsonView(View.Blit.class)
	@NotEmpty
	String blitTypeName;
	@JsonView(View.Blit.class)
	@NotNull
	SeatType seatType;
	@JsonView(View.Blit.class)
	PaymentStatus paymentStatus;
	@JsonView(View.AdminBlit.class)
	String paymentError;
	@JsonView(View.AdminBlit.class)
	String samanBankToken;
	@JsonView(View.AdminBlit.class)
	String samanBankRefNumber;
	@JsonView(View.Blit.class)
	BankGateway bankGateway;
	
	public BankGateway getBankGateway() {
		return bankGateway;
	}
	public void setBankGateway(BankGateway bankGateway) {
		this.bankGateway = bankGateway;
	}
	public Timestamp getEventDate() {
		return eventDate;
	}
	public void setEventDate(Timestamp eventDate) {
		this.eventDate = eventDate;
	}
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
