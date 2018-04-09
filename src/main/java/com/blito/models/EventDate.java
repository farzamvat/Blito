package com.blito.models;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name="event_time")
public class EventDate {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	long eventDateId;
	
	private Timestamp date;
	
	private String eventDateState;
	
	@OneToMany(mappedBy="eventDate", targetEntity=BlitType.class,fetch=FetchType.EAGER, cascade=CascadeType.ALL,orphanRemoval=true)
	private Set<BlitType> blitTypes;
	
	@ManyToOne
	@JoinColumn(name="eventId")
	private Event event;
	
	@ManyToOne(optional=true)
	@JoinColumn(name="salonId")
	private Salon salon;

	private String dateTime;

	private String uid;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public EventDate()
	{
		blitTypes = new HashSet<>();
	}

	public String getEventDateState() {
		return eventDateState;
	}

	public void setEventDateState(String eventDateState) {
		this.eventDateState = eventDateState;
	}

	public Salon getSalon() {
		return salon;
	}

	public void setSalon(Salon salon) {
		this.salon = salon;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
		event.getEventDates().add(this);
	}

	public long getEventDateId() {
		return eventDateId;
	}

	public void setEventDateId(long eventDateId) {
		this.eventDateId = eventDateId;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public Set<BlitType> getBlitTypes() {
		return blitTypes;
	}

	public void setBlitTypes(Set<BlitType> blitTypes) {
		this.blitTypes = blitTypes;
//		blitTypes.forEach(bt->bt.setEventDate(this));
	}
	
	public void addBlitType(BlitType blitType)
	{
		this.blitTypes.add(blitType);
		blitType.setEventDate(this);
	}
	
	public void removeBlitType(BlitType blitType)
	{
		this.blitTypes.remove(blitType);
		blitType.setEventDate(null);
	}
	public void removeBlitTypeByUid(String uid)
	{
		this.blitTypes.stream().filter(b -> b.getUid().equals(uid))
                .findFirst().ifPresent(blitType -> this.blitTypes.removeIf(b -> b.getUid().equals(uid)));

	}
}
