package com.blito.rest.viewmodels.event;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.blito.enums.OfferTypeEnum;

public class AdminChangeOfferTypeViewModel {
	
	@NotNull
	long eventId;
	@NotNull
	List<OfferTypeEnum> offers;
	
	public AdminChangeOfferTypeViewModel() {
		offers = new ArrayList<>();
	}
	
	public long getEventId() {
		return eventId;
	}
	public void setEventId(long eventId) {
		this.eventId = eventId;
	}
	public List<OfferTypeEnum> getOffers() {
		return offers;
	}
	public void setOffers(List<OfferTypeEnum> offers) {
		this.offers = offers;
	}
}
