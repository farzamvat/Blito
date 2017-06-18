package com.blito.models;

import java.sql.Timestamp;
import java.util.ArrayList;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
	
	@ManyToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
    @JoinTable(name="event_image" , joinColumns=@JoinColumn(name="event_id"), 
    inverseJoinColumns=@JoinColumn(name="image_id"))
	private List<Image> images; 
	
	@ElementCollection(fetch=FetchType.EAGER)
	@Enumerated(EnumType.STRING)
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
	
	@Column(name="sold_date")
	private Timestamp eventSoldDate;
	
	@Column(name="created_at")
	private Timestamp createdAt;
	
	private String address;
	
	private String description;
	
	Double longitude;
	
	Double latitude;
	
	String eventLink;
	
	@Enumerated(EnumType.STRING)
	State eventState;
	
	@Enumerated(EnumType.STRING)
	OperatorState operatorState;
	
	String aparatDisplayCode;
	
	int orderNumber;
	
	boolean isEvento = false;
	
	public Event() {
		offers = new ArrayList<>();
		images = new ArrayList<>();
		eventDates = new ArrayList<>();
	}
	
	public Timestamp getEventSoldDate() {
		return eventSoldDate;
	}

	public void setEventSoldDate(Timestamp eventSoldDate) {
		this.eventSoldDate = eventSoldDate;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isEvento() {
		return isEvento;
	}

	public void setEvento(boolean isEvento) {
		this.isEvento = isEvento;
	}

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
//		eventDates.forEach(ed->ed.setEvent(this));
	}

	public EventHost getEventHost() {
		return eventHost;
	}

	public void setEventHost(EventHost eventHost) {
		this.eventHost = eventHost;
		eventHost.getEvents().add(this);
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
	
	public void addEventDate(EventDate eventDate)
	{
		this.eventDates.add(eventDate);
		eventDate.setEvent(this);
	}
}
