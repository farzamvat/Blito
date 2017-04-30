package com.blito.models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.blito.enums.BlitTypeSeatState;

@Entity(name="blit_type_seat")
public class BlitTypeSeat {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	long blitTypeSeatId;
	Timestamp soldDate;
	BlitTypeSeatState state;
	@ManyToOne
	@JoinColumn(name="seatId")
	Seat seat;
	@ManyToOne
	@JoinColumn(name="blitTypeId")
	BlitType blitType;
	@ManyToOne(optional=true)
	@JoinColumn(name="blitId")
	SeatBlit seatBlit;
	
	public long getBlitTypeSeatId() {
		return blitTypeSeatId;
	}
	public void setBlitTypeSeatId(long blitTypeSeatId) {
		this.blitTypeSeatId = blitTypeSeatId;
	}
	public Timestamp getSoldDate() {
		return soldDate;
	}
	public void setSoldDate(Timestamp soldDate) {
		this.soldDate = soldDate;
	}
	public BlitTypeSeatState getState() {
		return state;
	}
	public void setState(BlitTypeSeatState state) {
		this.state = state;
	}
	public Seat getSeat() {
		return seat;
	}
	public void setSeat(Seat seat) {
		this.seat = seat;
	}
	public BlitType getBlitType() {
		return blitType;
	}
	public void setBlitType(BlitType blitType) {
		this.blitType = blitType;
	}
	public SeatBlit getSeatBlit() {
		return seatBlit;
	}
	public void setSeatBlit(SeatBlit seatBlit) {
		this.seatBlit = seatBlit;
	}
	
}
