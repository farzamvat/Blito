package com.blito.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.Set;

@Entity(name="seat_blit")
@PrimaryKeyJoinColumn(referencedColumnName="blitId")
public class SeatBlit extends Blit {
	@OneToMany(mappedBy="seatBlit",targetEntity=BlitTypeSeat.class,cascade = CascadeType.ALL)
	Set<BlitTypeSeat> blitTypeSeats;
	String seats;
	public Set<BlitTypeSeat> getBlitTypeSeats() {
		return blitTypeSeats;
	}
	public void setBlitTypeSeats(Set<BlitTypeSeat> blitTypeSeats) {
		this.blitTypeSeats = blitTypeSeats;
	}
	public String getSeats() {
		return seats;
	}
	public void setSeats(String seats) {
		this.seats = seats;
	}
}
