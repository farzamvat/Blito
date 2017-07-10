package com.blito.models;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.blito.enums.State;

@Entity(name="blit_type")
public class BlitType {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private long blitTypeId;
	
	private boolean isFree;
	
	private int capacity;
	
	private int soldCount;
	
	private long price;
	
	private String blitTypeState;
	
	@ManyToOne
	@JoinColumn(name="eventDateId")
	private EventDate eventDate;
	
	private String name;
	
	@OneToMany(mappedBy="blitType",targetEntity=BlitTypeSeat.class)
	Set<BlitTypeSeat> blitTypeSeats;
	
	@OneToMany(mappedBy="blitType",targetEntity=CommonBlit.class)
	Set<CommonBlit> commonBlits;
	
	@ManyToMany
	private Set<Discount> discounts;
	
	public Set<Discount> getDiscounts() {
		return discounts;
	}

	public void setDiscounts(Set<Discount> discounts) {
		this.discounts = discounts;
	}

	public BlitType() {
		blitTypeSeats = new HashSet<>();
		commonBlits = new HashSet<>();
		discounts = new HashSet<>();
	}
	
	public Set<BlitTypeSeat> getBlitTypeSeats() {
		return blitTypeSeats;
	}

	public void setBlitTypeSeats(Set<BlitTypeSeat> blitTypeSeats) {
		this.blitTypeSeats = blitTypeSeats;
	}

	public Set<CommonBlit> getCommonBlits() {
		return commonBlits;
	}

	public void setCommonBlits(Set<CommonBlit> commonBlits) {
		this.commonBlits = commonBlits;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBlitTypeState() {
		return blitTypeState;
	}

	public void setBlitTypeState(String blitTypeState) {
		this.blitTypeState = blitTypeState;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public EventDate getEventDate() {
		return eventDate;
	}

	public void setEventDate(EventDate eventDate) {
		this.eventDate = eventDate;
	}

	public long getBlitTypeId() {
		return blitTypeId;
	}

	public void setBlitTypeId(long blitTypeId) {
		this.blitTypeId = blitTypeId;
	}

	public boolean isFree() {
		return isFree;
	}

	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getSoldCount() {
		return soldCount;
	}

	public void setSoldCount(int soldCount) {
		this.soldCount = soldCount;
	}
}
