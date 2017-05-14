package com.blito.rest.viewmodels.exchangeblit;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import com.blito.annotations.Email;
import com.blito.annotations.MobileNumber;
import com.blito.enums.ExchangeBlitType;
import com.blito.enums.OperatorState;
import com.blito.enums.State;
import com.blito.rest.viewmodels.View;
import com.blito.rest.viewmodels.image.ImageViewModel;
import com.fasterxml.jackson.annotation.JsonView;

public class ExchangeBlitViewModel {
	@JsonView(View.SimpleExchangeBlit.class)
	private long exchangeBlitId;
	@NotNull
	@JsonView(View.SimpleExchangeBlit.class)
	private String title;
	@NotNull
	@JsonView(View.ExchangeBlit.class)
	private String firstname;
	@NotNull
	@JsonView(View.ExchangeBlit.class)
	private String lastname;
	@NotNull
	@JsonView(View.SimpleExchangeBlit.class)
	private Timestamp eventDate;
	@NotNull
	@JsonView(View.SimpleExchangeBlit.class)
	private double blitCost;
	@NotNull
	@JsonView(View.ExchangeBlit.class)
	private boolean isBlitoEvent;
	@MobileNumber
	@JsonView(View.ExchangeBlit.class)
	private String phoneNumber;
	@Email
	@JsonView(View.ExchangeBlit.class)
	private String email;
	@NotNull
	@JsonView(View.ExchangeBlit.class)
	private String eventAddress;
	@NotNull
	@JsonView(View.ExchangeBlit.class)
	private String vendorAddress;
	@JsonView(View.ExchangeBlit.class)
	private String description;
	@JsonView(View.SimpleExchangeBlit.class)
	private State state;
	@JsonView(View.SimpleExchangeBlit.class)
	private ExchangeBlitType type;
	@JsonView(View.SimpleExchangeBlit.class)
	private OperatorState operatorState;
	@JsonView(View.SimpleExchangeBlit.class)
	private ImageViewModel image;
	
	public ImageViewModel getImage() {
		return image;
	}

	public void setImage(ImageViewModel image) {
		this.image = image;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public void setType(ExchangeBlitType type) {
		this.type = type;
	}

	public long getExchangeBlitId() {
		return exchangeBlitId;
	}

	public void setExchangeBlitId(long exchangeBlitId) {
		this.exchangeBlitId = exchangeBlitId;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
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

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
}
