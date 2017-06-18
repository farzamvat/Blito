package com.blito.models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

import com.blito.enums.DayOfWeek;
import com.blito.enums.State;

@Entity(name="event_time")
public class EventDate {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	long eventDateId;
	
	@Enumerated(EnumType.STRING)
	DayOfWeek dayOfWeek;
	
	Timestamp date;
	
	@Enumerated(EnumType.STRING)
	State eventDateState;
	
	@OneToMany(mappedBy="eventDate", targetEntity=BlitType.class,fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	List<BlitType> blitTypes;
	
	@ManyToOne
	@JoinColumn(name="eventId")
	Event event;
	
	@ManyToOne(optional=true)
	@JoinColumn(name="salonId")
	Salon salon;
	
	public EventDate()
	{
		blitTypes = new ArrayList<>();
	}

	public State getEventDateState() {
		return eventDateState;
	}

	public void setEventDateState(State eventDateState) {
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
	}

	public long getEventDateId() {
		return eventDateId;
	}

	public void setEventDateId(long eventDateId) {
		this.eventDateId = eventDateId;
	}

	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(DayOfWeek dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public List<BlitType> getBlitTypes() {
		return blitTypes;
	}

	public void setBlitTypes(List<BlitType> blitTypes) {
		this.blitTypes = blitTypes;
		blitTypes.forEach(bt->bt.setEventDate(this));
	}
	
	public void addBlitType(BlitType blitType)
	{
		this.blitTypes.add(blitType);
		blitType.setEventDate(this);
	}
}
