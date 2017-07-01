package com.blito.rest.viewmodels.event;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.blito.enums.EventType;
import com.blito.rest.viewmodels.eventdate.EventDateViewModel;
import com.blito.rest.viewmodels.image.ImageViewModel;

public class EventUpdateViewModel {
	long eventId;
	long eventHostId;
	@NotNull
	private String EventName;
	@NotNull
	private EventType eventType;
	private Timestamp blitSaleStartDate;
	private Timestamp blitSaleEndDate;
	private String address;
	private String description;
	private Double latitude;
	private Double longitude;
	private String aparatDisplayCode;
	private String eventLink;
	Set<EventDateViewModel> eventDates;
	Set<ImageViewModel> images;
	
	public String getEventLink() {
		return eventLink;
	}

	public void setEventLink(String eventLink) {
		this.eventLink = eventLink;
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

	public EventUpdateViewModel()
	{
		eventDates = new HashSet<>();
		images = new HashSet<>();
	}

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	public long getEventHostId() {
		return eventHostId;
	}

	public void setEventHostId(long eventHostId) {
		this.eventHostId = eventHostId;
	}

	public Set<EventDateViewModel> getEventDates() {
		return eventDates;
	}

	public void setEventDates(Set<EventDateViewModel> eventDates) {
		this.eventDates = eventDates;
	}

	public Set<ImageViewModel> getImages() {
		return images;
	}

	public void setImages(Set<ImageViewModel> images) {
		this.images = images;
	}
}
