package com.blito.models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;

@Entity(name="event")
public class Event {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private long eventId;
	
	@OneToMany(mappedBy="event",targetEntity=EventDate.class,cascade=CascadeType.ALL,fetch=FetchType.EAGER,orphanRemoval=true)
	@OrderBy("date DESC")
	private Set<EventDate> eventDates;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="eventHostId")
	private EventHost eventHost;
	
	@ManyToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
    @JoinTable(name="event_image" , joinColumns=@JoinColumn(name="event_id"), 
    inverseJoinColumns=@JoinColumn(name="image_id"))
	private Set<Image> images; 
	
	@ElementCollection(fetch=FetchType.EAGER)
	Set<String> offers;
	
	@Column(name="event_name")
	private String eventName;
	
	@Column(name="event_type")
	private String eventType;
	
	@Column(name="blit_sale_start_date")
	private Timestamp blitSaleStartDate;
	
	@Column(name="blit_sale_end_date")
	private Timestamp blitSaleEndDate;
	
	@Column(name="sold_date")
	private Timestamp eventSoldDate;
	
	@Column(name="created_at")
	private Timestamp createdAt;
	@Column(columnDefinition="TEXT")
	private String address;
	@Column(columnDefinition="TEXT")
	private String description;
	@Column(columnDefinition="TEXT")
	private String members;
	
	private long views;
	
	private Double longitude;
	
	private Double latitude;
	
	@Column(unique=true,nullable=true)
	private String eventLink;
	
	private String eventState;
	
	private String operatorState;
	
	private boolean isDeleted = false;
	@Column(columnDefinition="TEXT")
	private String aparatDisplayCode;
	
	private int orderNumber;
	
	private boolean isEvento = false;
	
	private boolean isOpenInit = false;
	
	private boolean isClosedInit = false;
	
	private boolean isPrivate = false;

	private Timestamp endDate;

	@OneToOne(optional = true, orphanRemoval = true, cascade = CascadeType.ALL)
	private Event editedVersion;

	@ElementCollection(fetch=FetchType.EAGER)
	@Column(name="fields")
	private Map<String,String> additionalFields;

	public Event getEditedVersion() {
		return editedVersion;
	}

	public void setEditedVersion(Event editedVersion) {
		this.editedVersion = editedVersion;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	
	public Event() {
		offers = new HashSet<>();
		images = new HashSet<>();
		eventDates = new HashSet<>();
		additionalFields = new HashMap<>();
	}

	public Map<String, String> getAdditionalFields() {
		return additionalFields;
	}

	public void setAdditionalFields(Map<String, String> additionalFields) {
		this.additionalFields = additionalFields;
	}

	public long getViews() {
		return views;
	}


	public void setViews(long views) {
		this.views = views;
	}


	public String getMembers() {
		return members;
	}


	public boolean isOpenInit() {
		return isOpenInit;
	}


	public void setOpenInit(boolean isOpenInit) {
		this.isOpenInit = isOpenInit;
	}


	public boolean isClosedInit() {
		return isClosedInit;
	}


	public void setClosedInit(boolean isClosedInit) {
		this.isClosedInit = isClosedInit;
	}


	public void setMembers(String members) {
		this.members = members;
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

	public Set<String> getOffers() {
		return offers;
	}

	public void setOffers(Set<String> offers) {
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

	public String getEventState() {
		return eventState;
	}

	public void setEventState(String eventState) {
		this.eventState = eventState;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getOperatorState() {
		return operatorState;
	}

	public void setOperatorState(String operatorState) {
		this.operatorState = operatorState;
	}

	public Set<Image> getImages() {
		return images;
	}

	public void setImages(Set<Image> images) {
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

	public Set<EventDate> getEventDates() {
		return eventDates;
	}

	public void setEventDates(Set<EventDate> eventDates) {
		this.eventDates = eventDates;
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

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
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
		if(!this.eventDates.contains(eventDate))
		{
			this.eventDates.add(eventDate);
			eventDate.setEvent(this);
		}
	}
	
	public void removeEventDateByUid(String uid)
	{
		this.eventDates.removeIf(b -> Objects.nonNull(b.getUid()) && b.getUid().equals(uid));
	}
}
