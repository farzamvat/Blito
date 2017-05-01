package com.blito.rest.viewmodels;

import java.sql.Timestamp;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.blito.enums.EventType;

public class EventCreateViewModel {
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
	private String indexTitle;
	private String indexDescription;
	private String aparatDisplayCode;
	private long eventHostId;
	
	List<EventDateCreateViewModel> eventDates;
	List<ImageViewModel> images;
	

	public List<ImageViewModel> getImages() {
		return images;
	}

	public void setImages(List<ImageViewModel> images) {
		this.images = images;
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

	public void setBlitSaleStartDate(Timestamp blitSalteStartDate) {
		this.blitSaleStartDate = blitSalteStartDate;
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

	public String getIndexTitle() {
		return indexTitle;
	}

	public void setIndexTitle(String indexTitle) {
		this.indexTitle = indexTitle;
	}

	public String getIndexDescription() {
		return indexDescription;
	}

	public void setIndexDescription(String indexDescription) {
		this.indexDescription = indexDescription;
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

	public List<EventDateCreateViewModel> getEventDates() {
		return eventDates;
	}

	public void setEventDates(List<EventDateCreateViewModel> eventDates) {
		this.eventDates = eventDates;
	}
	
}
