package com.blito.rest.viewmodels.eventdate;

import com.blito.enums.State;
import com.blito.rest.viewmodels.blittype.BlitTypeViewModel;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class EventDateViewModel {
	long eventDateId;
	@NotNull
	private Timestamp date;
	@Valid
	@NotEmpty
	private Set<BlitTypeViewModel> blitTypes;
	private State state;
	private boolean hasSalon;

	public boolean isHasSalon() {
		return hasSalon;
	}

	public void setHasSalon(boolean hasSalon) {
		this.hasSalon = hasSalon;
	}

	public long getEventDateId() {
		return eventDateId;
	}

	public void setEventDateId(long eventDateId) {
		this.eventDateId = eventDateId;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public EventDateViewModel()
	{
		blitTypes = new HashSet<>();
	}
	
	public Set<BlitTypeViewModel> getBlitTypes() {
		return blitTypes;
	}
	public void setBlitTypes(Set<BlitTypeViewModel> blitTypes) {
		this.blitTypes = blitTypes;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "EventDateViewModel{" +
				"eventDateId=" + eventDateId +
				", date=" + date +
				", blitTypes=" + blitTypes +
				", state=" + state +
				", hasSalon=" + hasSalon +
				'}';
	}
}
