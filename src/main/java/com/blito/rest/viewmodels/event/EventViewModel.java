package com.blito.rest.viewmodels.event;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.blito.enums.EventType;
import com.blito.enums.OfferTypeEnum;
import com.blito.enums.OperatorState;
import com.blito.enums.State;
import com.blito.rest.viewmodels.View;
import com.blito.rest.viewmodels.eventdate.EventDateViewModel;
import com.blito.rest.viewmodels.image.ImageViewModel;
import com.fasterxml.jackson.annotation.JsonView;

public class EventViewModel {
	@JsonView(View.SimpleEvent.class)
	private long eventId;
	
	@NotNull
	@JsonView(View.SimpleEvent.class)
	private String EventName;
	
	@NotNull
	@JsonView(View.SimpleEvent.class)
	private EventType eventType;
	
	@JsonView(View.SimpleEvent.class)
	private Timestamp blitSaleStartDate;
	
	@JsonView(View.SimpleEvent.class)
	private Timestamp blitSaleEndDate;
	
	@JsonView(View.Event.class)
	private String address;
	
	@JsonView(View.Event.class)
	private String description;
	
	@JsonView(View.Event.class)
	private Double latitude;
	
	@JsonView(View.Event.class)
	private Double longitude;
	
	@JsonView(View.Event.class)
	private String aparatDisplayCode;
	
	@JsonView(View.SimpleEvent.class)
	long eventHostId;
	
	@JsonView(View.SimpleEvent.class)
	String eventHostName;
	
	@JsonView(View.SimpleEvent.class)
	List<OfferTypeEnum> offers;
	
	@JsonView(View.Event.class)
	List<EventDateViewModel> eventDates;
	
	@JsonView(View.SimpleEvent.class)
	List<ImageViewModel> images;
	
	@JsonView(View.Event.class)
	private String eventLink;
	
	@JsonView(View.Event.class)
	private State eventState;
	
	@JsonView(View.Event.class)
	private OperatorState operatorState;
	
	@JsonView(View.AdminEvent.class)
	private int orderNumber;
	
	@JsonView(View.SimpleEvent.class)
	private boolean isEvento;
	
	
	
	public EventViewModel()
	{
		eventDates = new ArrayList<>();
		images = new ArrayList<>();
		offers = new ArrayList<>();
	}
	
	
	public boolean isEvento() {
		return isEvento;
	}


	public void setEvento(boolean isEvento) {
		this.isEvento = isEvento;
	}


	public String getEventHostName() {
		return eventHostName;
	}


	public void setEventHostName(String eventHostName) {
		this.eventHostName = eventHostName;
	}


	public List<OfferTypeEnum> getOffers() {
		return offers;
	}

	public void setOffers(List<OfferTypeEnum> offers) {
		this.offers = offers;
	}

	public String getEventLink() {
		return eventLink;
	}

	public void setEventLink(String eventLink) {
		this.eventLink = eventLink;
	}

	public State getEventState() {
		return eventState;
	}

	public void setEventState(State eventState) {
		this.eventState = eventState;
	}

	public OperatorState getOperatorState() {
		return operatorState;
	}

	public void setOperatorState(OperatorState operatorState) {
		this.operatorState = operatorState;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	public String getEventName() {
		return EventName;
	}

	public void setEventName(String eventName) {
		EventName = eventName;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public Timestamp getBlitSaleStartDate() {
		return blitSaleStartDate;
	}

	public void setBlitSaleStartDate(Timestamp blitSaleStartDate) {
		this.blitSaleStartDate = blitSaleStartDate;
	}

	public Timestamp getBlitSaleEndDate() {
		return blitSaleEndDate;
	}

	public void setBlitSaleEndDate(Timestamp blitSaleEndDate) {
		this.blitSaleEndDate = blitSaleEndDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getAparatDisplayCode() {
		return aparatDisplayCode;
	}

	public void setAparatDisplayCode(String aparatDisplayCode) {
		this.aparatDisplayCode = aparatDisplayCode;
	}

	public long getEventHostId() {
		return eventHostId;
	}

	public void setEventHostId(long eventHostId) {
		this.eventHostId = eventHostId;
	}

	public List<EventDateViewModel> getEventDates() {
		return eventDates;
	}

	public void setEventDates(List<EventDateViewModel> eventDates) {
		this.eventDates = eventDates;
	}

	public List<ImageViewModel> getImages() {
		return images;
	}

	public void setImages(List<ImageViewModel> images) {
		this.images = images;
	}
}
