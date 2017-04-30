package com.blito.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity(name="seat_blit")
@PrimaryKeyJoinColumn(referencedColumnName="blitId")
public class SeatBlit extends Blit {
	@OneToMany(mappedBy="seatBlit",targetEntity=BlitTypeSeat.class)
	List<BlitTypeSeat> blitTypeSeats;
	String seats;
	public List<BlitTypeSeat> getBlitTypeSeats() {
		return blitTypeSeats;
	}
	public void setBlitTypeSeats(List<BlitTypeSeat> blitTypeSeats) {
		this.blitTypeSeats = blitTypeSeats;
	}
	public String getSeats() {
		return seats;
	}
	public void setSeats(String seats) {
		this.seats = seats;
	}
}
