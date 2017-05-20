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
	
	private String vendorAddress;
	
	private String description;
	
	@Enumerated(EnumType.STRING)
	private State state;
	@Enumerated(EnumType.STRING)
	private ExchangeBlitType type;
	@Enumerated(EnumType.STRING)
	private OperatorState operatorState;
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;
	
	@OneToOne
	Image image;
	
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public OperatorState getOperatorState() {
		return operatorState;
	}

	public void setOperatorState(OperatorState operatorState) {
		this.operatorState = operatorState;
	}

	public ExchangeBlitType getType() {
		return type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setType(ExchangeBlitType type) {
		this.type = type;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		user.getExchangeBlits().add(this);
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
