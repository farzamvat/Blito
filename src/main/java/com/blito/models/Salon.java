package com.blito.models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name="salon")
public class Salon {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private long salonId;
	private String name;
	private Double latitude;
	private Double longitude;
	private String address;
	private String planPath;
	@OneToMany(mappedBy="salon",targetEntity=Seat.class)
	Set<Seat> seats;
	@OneToMany(mappedBy="salon",targetEntity=EventDate.class)
	Set<EventDate> eventDates;
	
	public Set<EventDate> getEventDates() {
		return eventDates;
	}
	public void setEventDates(Set<EventDate> eventDates) {
		this.eventDates = eventDates;
	}
	public long getSalonId() {
		return salonId;
	}
	public void setSalonId(long salonId) {
		this.salonId = salonId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPlanPath() {
		return planPath;
	}
	public void setPlanPath(String planPath) {
		this.planPath = planPath;
	}
	public Set<Seat> getSeats() {
		return seats;
	}
	public void setSeats(Set<Seat> seats) {
		this.seats = seats;
	}
}
