package com.blito.models;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.blito.enums.EventType;
import com.blito.enums.OfferTypeEnum;
import com.blito.enums.OperatorState;
import com.blito.enums.State;

@Entity(name="event")
public class Event {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private long eventId;
	
	@OneToMany(mappedBy="event",targetEntity=EventDate.class,cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	List<EventDate> eventDates;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="eventHostId")
	private EventHost eventHost;
	
	@OneToMany
	@JoinColumn(name="eventId")
	private List<Image> images; 
	
	@ElementCollection(fetch=FetchType.EAGER)
	List<OfferTypeEnum> offers;
	
	@Column(name="event_name")
	private String eventName;
	
	@Column(name="event_type")
	@Enumerated(EnumType.STRING)
	private EventType eventType;
	
	@Column(name="blit_sale_start_date")
	private Timestamp blitSaleStartDate;
	
	@Column(name="blit_sale_end_date")
	private Timestamp blitSaleEndDate;
	
	private String address;
	
	private String description;
	
	Double longitude;
	
	Double latitude;
	
	String eventLink;
	
	String indexTitle;
	
	String indexDescription;
	
	State eventState;
	
	OperatorState operatorState;
	
	String aparatDisplayCode;
	
	int orderNumber;
	
	
	
	
	public List<OfferTypeEnum> getOffers() {
		return offers;
	}

	public void setOffers(List<OfferTypeEnum> offers) {
		this.offers = offers;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getAparatDisplayCode() {
		return aparatDisplayCode;
	}

	public void setAparatDisplayCode(String aparatDisplayCode) {
		this.aparatDisplayCode = aparatDisplayCode;
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

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public String getEventLink() {
		return eventLink;
	}

	public void setEventLink(String eventLink) {
		this.eventLink = eventLink;
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

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	public List<EventDate> getEventDates() {
		return eventDates;
	}

	public void setEventDates(List<EventDate> eventDates) {
		this.eventDates = eventDates;
	}

	public EventHost getEventHost() {
		return eventHost;
	}

	public void setEventHost(EventHost eventHost) {
		this.eventHost = eventHost;
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
}
