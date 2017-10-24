package com.blito.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name="seat_blit")
@PrimaryKeyJoinColumn(referencedColumnName="blitId")
public class SeatBlit extends Blit {
	@OneToMany(mappedBy="seatBlit",targetEntity=BlitTypeSeat.class,cascade = CascadeType.ALL)
	private Set<BlitTypeSeat> blitTypeSeats;
	@Column(columnDefinition="TEXT")
	private String seats;

	public SeatBlit() {
		blitTypeSeats = new HashSet<>();
	}

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
