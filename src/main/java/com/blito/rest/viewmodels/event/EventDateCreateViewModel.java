package com.blito.rest.viewmodels.event;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.blito.enums.DayOfWeek;

public class EventDateCreateViewModel {
	DayOfWeek dayOfWeek;
	Timestamp date;
	List<BlitTypeCreateViewModel> blitTypes;
	
	public EventDateCreateViewModel()
	{
		blitTypes = new ArrayList<>();
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
	public List<BlitTypeCreateViewModel> getBlitTypes() {
		return blitTypes;
	}
	public void setBlitTypes(List<BlitTypeCreateViewModel> blitTypes) {
		this.blitTypes = blitTypes;
	}
}
