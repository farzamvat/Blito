package com.blito.models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Entity(name="blit")
@Table
@Inheritance(strategy=InheritanceType.JOINED)
public class Blit {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Long blitId;
	private Integer count;
	private Long totalAmount;
	@ManyToOne(optional=true)
	@JoinColumn(name="userId")
	private User user;
	private String trackCode;
	private String eventName;
	private String eventDateAndTime;
	private Timestamp createdAt;
	private Timestamp eventDate;
	private String customerName;
	private String customerMobileNumber;
	private String customerEmail;
	@Column(columnDefinition = "TEXT")
	private String eventAddress;
	private String blitTypeName;
	private String seatType;
	private String paymentStatus;
	private String paymentError;
	private String token;
	private String refNum;
	private String samanTraceNo;
	private Boolean used = false;
	private String bankGateway;
	private String discountCode;
	private Long primaryAmount;
	
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

	public Long getBlitId() {
		return blitId;
	}

	public void setBlitId(Long blitId) {
		this.blitId = blitId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Long totalAmount) {
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

	public Boolean getUsed() {
		return used;
	}

	public void setUsed(Boolean used) {
		this.used = used;
	}

	public String getBankGateway() {
		return bankGateway;
	}

	public void setBankGateway(String bankGateway) {
		this.bankGateway = bankGateway;
	}

	public String getDiscountCode() {
		return discountCode;
	}

	public void setDiscountCode(String discountCode) {
		this.discountCode = discountCode;
	}

	public Long getPrimaryAmount() {
		return primaryAmount;
	}

	public void setPrimaryAmount(Long primaryAmount) {
		this.primaryAmount = primaryAmount;
	}
}
