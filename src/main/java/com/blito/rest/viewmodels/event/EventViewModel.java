package com.blito.rest.viewmodels.event;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.blito.enums.EventType;
import com.blito.enums.OfferTypeEnum;
import com.blito.enums.State;
import com.blito.rest.viewmodels.image.ImageViewModel;

public class EventViewModel {
	long eventId;
	String eventname;
	EventType eventType;
	Timestamp blitSaleStartDate;
	Timestamp blitSaleEndDate;
	String address;
	String descrption;
	Double longitude;
	Double latitude;
	String eventLink; 
	State eventState;
	String aparatDisplayCode;

	long eventHostId;
	
	List<OfferTypeEnum> offers;
	
	List<EventDateViewModel> eventDates;
	List<ImageViewModel> images;
	
	public EventViewModel()
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
	public String getEventname() {
		return eventname;
	}
	public void setEventname(String eventname) {
		this.eventname = eventname;
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
	public String getDescrption() {
		return descrption;
	}
	public void setDescrption(String descrption) {
		this.descrption = descrption;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
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
	public List<OfferTypeEnum> getOffers() {
		return offers;
	}
	public void setOffers(List<OfferTypeEnum> offers) {
		this.offers = offers;
	}
}
