package com.blito.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name="salon")
public class Salon {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private long salonId;
	private String salonUid;
	private String name;
	private Double latitude;
	private Double longitude;
	private String address;
	private String planPath;
	@OneToMany(mappedBy="salon",targetEntity=Seat.class,cascade = CascadeType.ALL)
	Set<Seat> seats;
	@OneToMany(mappedBy="salon",targetEntity=EventDate.class,cascade = CascadeType.ALL)
	Set<EventDate> eventDates;
	@Column(columnDefinition="TEXT")
	private String salonSvg;

	public Salon() {
		seats = new HashSet<>();
		eventDates = new HashSet<>();
	}

	public String getSalonSvg() {
		return salonSvg;
	}

	public void setSalonSvg(String salonSvg) {
		this.salonSvg = salonSvg;
	}

	public void addSeat(Seat seat) {
		this.seats.add(seat);
		seat.setSalon(this);
	}

	public String getSalonUid() {
		return salonUid;
	}
	public void setSalonUid(String salonUid) {
		this.salonUid = salonUid;
	}
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
