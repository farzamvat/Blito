package com.blito.rest.viewmodels.event;

import com.blito.annotations.AdditionalFields;
import com.blito.enums.EventType;
import com.blito.enums.OfferTypeEnum;
import com.blito.enums.OperatorState;
import com.blito.enums.State;
import com.blito.rest.viewmodels.View;
import com.blito.rest.viewmodels.eventdate.EventDateViewModel;
import com.blito.rest.viewmodels.image.ImageViewModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.*;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventViewModel {
	@JsonView(View.SimpleEvent.class)
	private long eventId;

	@NotEmpty
	@JsonView(View.SimpleEvent.class)
	private String eventName;

	@NotNull
	@JsonView(View.SimpleEvent.class)
	private EventType eventType;

	@NotNull
	@JsonView(View.Event.class)
	private Timestamp blitSaleStartDate;

	@NotNull
	@JsonView(View.Event.class)
	private Timestamp blitSaleEndDate;
	
	@JsonView(View.Event.class)
	private Timestamp eventSoldDate;

	@JsonView(View.Event.class)
	@NotEmpty
	private String address;

	@JsonView(View.Event.class)
	private String description;

	@JsonView(View.Event.class)
	private Double latitude;
	
	@JsonView(View.Event.class)
	private Timestamp createdAt;

	@JsonView(View.Event.class)
	private Double longitude;

	@JsonView(View.Event.class)
	private String aparatDisplayCode;

	@JsonView(View.Event.class)
	@NotNull
	long eventHostId;

	@JsonView(View.SimpleEvent.class)
	String eventHostName;

	@JsonView(View.SimpleEvent.class)
	Set<OfferTypeEnum> offers;

	@JsonView(View.Event.class)
	@NotEmpty
	Set<EventDateViewModel> eventDates;

	@JsonView(View.SimpleEvent.class)
	Set<ImageViewModel> images;
	
	@JsonView(View.SimpleEvent.class)
	private long views;

	@JsonView(View.SimpleEvent.class)
	private String eventLink;

	@JsonView(View.SimpleEvent.class)
	private State eventState;

	@JsonView(View.Event.class)
	private OperatorState operatorState;

	@JsonView(View.AdminEvent.class)
	private int orderNumber;

	@JsonView(View.SimpleEvent.class)
	private boolean isEvento;
	@JsonView(View.Event.class)
	private String members;
	@JsonView(View.AdminEvent.class)
	private boolean isDeleted;

	@Valid
	@AdditionalFields
	@JsonView(View.Event.class)
	private List<AdditionalField> additionalFields;
	
	@JsonView(View.AdminEvent.class)
	private boolean isPrivate;
	@JsonView(View.Event.class)
	private String salonUid;

	public EventViewModel() {
		eventDates = new HashSet<>();
		images = new HashSet<>();
		offers = new HashSet<>();
		additionalFields = new ArrayList<>();
	}

	@Override
	public String toString() {
		return "EventViewModel{" +
				"eventId=" + eventId +
				", eventName='" + eventName + '\'' +
				", eventType=" + eventType +
				", blitSaleStartDate=" + blitSaleStartDate +
				", blitSaleEndDate=" + blitSaleEndDate +
				", eventSoldDate=" + eventSoldDate +
				", address='" + address + '\'' +
				", description='" + description + '\'' +
				", latitude=" + latitude +
				", createdAt=" + createdAt +
				", longitude=" + longitude +
				", aparatDisplayCode='" + aparatDisplayCode + '\'' +
				", eventHostId=" + eventHostId +
				", eventHostName='" + eventHostName + '\'' +
				", offers=" + offers +
				", eventDates=" + eventDates +
				", images=" + images +
				", views=" + views +
				", eventLink='" + eventLink + '\'' +
				", eventState=" + eventState +
				", operatorState=" + operatorState +
				", orderNumber=" + orderNumber +
				", isEvento=" + isEvento +
				", members='" + members + '\'' +
				", isDeleted=" + isDeleted +
				", additionalFields=" + additionalFields +
				", isPrivate=" + isPrivate +
				", salonUid='" + salonUid + '\'' +
				'}';
	}

	public String getSalonUid() {
		return salonUid;
	}

	public void setSalonUid(String salonUid) {
		this.salonUid = salonUid;
	}
	public List<AdditionalField> getAdditionalFields() {
		return additionalFields;
	}

	public void setAdditionalFields(List<AdditionalField> additionalFields) {
		this.additionalFields = additionalFields;
	}

	public long getViews() {
		return views;
	}

	public void setViews(long views) {
		this.views = views;
	}

	public Timestamp getEventSoldDate() {
		return eventSoldDate;
	}

	public void setEventSoldDate(Timestamp eventSoldDate) {
		this.eventSoldDate = eventSoldDate;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getMembers() {
		return members;
	}

	public void setMembers(String members) {
		this.members = members;
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

	public String getEventHostName() {
		return eventHostName;
	}

	public void setEventHostName(String eventHostName) {
		this.eventHostName = eventHostName;
	}

	public Set<OfferTypeEnum> getOffers() {
		return offers;
	}

	public void setOffers(Set<OfferTypeEnum> offers) {
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

	@JsonProperty("isPrivate")
	public boolean isPrivate() {
		return isPrivate;
	}


	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	
	
}
