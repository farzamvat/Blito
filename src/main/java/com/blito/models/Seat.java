package com.blito.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name="seat")
public class Seat {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private long seatId;
	private String seatName;
	private String seatUid;
	private String rowName;
	private String rowUid;
	private String sectionName;
	private String sectionUid;
	@ManyToOne(optional=false)
	@JoinColumn(name="salonId")
	private Salon salon;

	public Seat(String seatName, String seatUid, String rowName, String rowUid, String sectionName, String sectionUid) {
		this.seatName = seatName;
		this.seatUid = seatUid;
		this.rowName = rowName;
		this.rowUid = rowUid;
		this.sectionName = sectionName;
		this.sectionUid = sectionUid;
	}

	public Seat() {
	}

	public long getSeatId() {
		return seatId;
	}

	public void setSeatId(long seatId) {
		this.seatId = seatId;
	}

	public String getSeatName() {
		return seatName;
	}

	public void setSeatName(String seatName) {
		this.seatName = seatName;
	}

	public String getSeatUid() {
		return seatUid;
	}

	public void setSeatUid(String seatUid) {
		this.seatUid = seatUid;
	}

	public String getRowName() {
		return rowName;
	}

	public void setRowName(String rowName) {
		this.rowName = rowName;
	}

	public String getRowUid() {
		return rowUid;
	}

	public void setRowUid(String rowUid) {
		this.rowUid = rowUid;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getSectionUid() {
		return sectionUid;
	}

	public void setSectionUid(String sectionUid) {
		this.sectionUid = sectionUid;
	}

	public Salon getSalon() {
		return salon;
	}

	public void setSalon(Salon salon) {
		this.salon = salon;
		salon.getSeats().add(this);
	}
}
