package com.blito.models;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity(name="blit_type_seat")
public class BlitTypeSeat {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private long blitTypeSeatId;
	private Timestamp soldDate;
	private String state;
	@ManyToOne
	@JoinColumn(name="seatId")
	private Seat seat;
	@ManyToOne
	@JoinColumn(name="blitTypeId")
	private BlitType blitType;
	@ManyToOne(optional=true)
	@JoinColumn(name="blitId")
	private SeatBlit seatBlit;

	public BlitTypeSeat(String state, Seat seat, BlitType blitType) {
		setState(state);
		setSeat(seat);
		setBlitType(blitType);
	}

	public BlitTypeSeat() {
	}

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
	public String getState() {
		return state;
	}
	public void setState(String state) {
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
		blitType.getBlitTypeSeats().add(this);
	}
	public SeatBlit getSeatBlit() {
		return seatBlit;
	}
	public void setSeatBlit(SeatBlit seatBlit) {
		this.seatBlit = seatBlit;
		seatBlit.getBlitTypeSeats().add(this);
	}
	
}
