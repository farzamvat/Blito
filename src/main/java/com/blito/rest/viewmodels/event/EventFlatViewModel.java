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
import com.blito.rest.viewmodels.eventdate.EventDateFlatViewModel;
import com.blito.rest.viewmodels.image.ImageViewModel;
import com.fasterxml.jackson.annotation.JsonView;

public class EventFlatViewModel {
	@JsonView(View.SimpleEvent.class)
	private long eventId;
	@JsonView(View.Event.class)
	private String eventLink; 
	@JsonView(View.SimpleEvent.class)
	private State eventState;
	@JsonView(View.Event.class)
	private long eventHostId;
	@JsonView(View.SimpleEvent.class)
	private List<OfferTypeEnum> offers;
	@JsonView(View.Event.class)
	private List<EventDateFlatViewModel> eventDates;
	@JsonView(View.SimpleEvent.class)
	private List<ImageViewModel> images;
	@JsonView(View.SimpleEvent.class)
	@NotNull
	private String eventName;
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
	private OperatorState operatorState;
	private int orderNumber;
	
	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public OperatorState getOperatorState() {
		return operatorState;
	}

	public void setOperatorState(OperatorState operatorState) {
		this.operatorState = operatorState;
	}

	public EventFlatViewModel()
	{
		offers = new ArrayList<>();
		eventDates = new ArrayList<>();
		images = new ArrayList<>();
	}

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
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

	public long getEventHostId() {
		return eventHostId;
	}

	public void setEventHostId(long eventHostId) {
		this.eventHostId = eventHostId;
	}

	public List<OfferTypeEnum> getOffers() {
		return offers;
	}

	public void setOffers(List<OfferTypeEnum> offers) {
		this.offers = offers;
	}

	public List<EventDateFlatViewModel> getEventDates() {
		return eventDates;
	}

	public void setEventDates(List<EventDateFlatViewModel> eventDates) {
		this.eventDates = eventDates;
	}

	public List<ImageViewModel> getImages() {
		return images;
	}

	public void setImages(List<ImageViewModel> images) {
		this.images = images;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
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
	
}
