package com.blito.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.blito.enums.OperatorState;
import com.blito.enums.State;
import com.blito.enums.ExchangeBlitType;

@Entity(name="exchange_blit")
public class ExchangeBlit {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private long exchangeBlitId;
	
	private Timestamp eventDate;
	
	private String title;
	
	private double blitCost;
	
	private boolean isBlitoEvent;
	
	private String phoneNumber;
	
	private String email;
	
	private String eventAddress;
	
	private double latitude;
	
	private double longitude;
	
	private String description;
	
	private Timestamp createdAt;
	
	private boolean isDeleted = false;
	@Column(unique=true,nullable=true)
	private String exchangeLink;
	private String state;
	private String exchangeBlitType;
	private String operatorState;
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;
	
	@OneToOne
	private Image image;
	
	
	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public String getExchangeLink() {
		return exchangeLink;
	}

	public void setExchangeLink(String exchangeLink) {
		this.exchangeLink = exchangeLink;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getExchangeBlitType() {
		return exchangeBlitType;
	}

	public void setExchangeBlitType(String exchangeBlitType) {
		this.exchangeBlitType = exchangeBlitType;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String getOperatorState() {
		return operatorState;
	}

	public void setOperatorState(String operatorState) {
		this.operatorState = operatorState;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		user.getExchangeBlits().add(this);
	}
	
	public void removeUser()
	{
		user = null;
	}

	public long getExchangeBlitId() {
		return exchangeBlitId;
	}

	public void setExchangeBlitId(long exchangeBlitId) {
		this.exchangeBlitId = exchangeBlitId;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
