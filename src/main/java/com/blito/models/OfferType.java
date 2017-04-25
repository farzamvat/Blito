package com.blito.models;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.blito.enums.OfferTypeEnum;

@Entity(name="offer_type")
public class OfferType {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	long offerTypeId;
	
	@Enumerated(EnumType.STRING)
	OfferTypeEnum type;

	public long getOfferTypeId() {
		return offerTypeId;
	}

	public void setOfferTypeId(long offerTypeId) {
		this.offerTypeId = offerTypeId;
	}

	public OfferTypeEnum getType() {
		return type;
	}

	public void setType(OfferTypeEnum type) {
		this.type = type;
	}
}
