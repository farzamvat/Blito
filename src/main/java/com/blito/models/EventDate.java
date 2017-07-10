package com.blito.models;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
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

import com.blito.enums.State;

@Entity(name="event_time")
public class EventDate {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	long eventDateId;
	
	Timestamp date;
	
	String eventDateState;
	
	@OneToMany(mappedBy="eventDate", targetEntity=BlitType.class,fetch=FetchType.LAZY, cascade=CascadeType.ALL,orphanRemoval=true)
	Set<BlitType> blitTypes;
	
	@ManyToOne
	@JoinColumn(name="eventId")
	Event event;
	
	@ManyToOne(optional=true)
	@JoinColumn(name="salonId")
	Salon salon;
	
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
	public void removeBlitTypeById(Long id)
	{
		Optional<BlitType> bt = this.blitTypes.stream().filter(b -> b.getBlitTypeId() == id).findFirst();
		if(bt.isPresent())
		{
			this.blitTypes.removeIf(b -> b.getBlitTypeId() == id);
		}
	}
}
