package com.blito.models;


import java.util.ArrayList;
import java.util.List;

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
	
	@Enumerated(EnumType.STRING)
	State blitTypeState;
	
	@ManyToOne
	@JoinColumn(name="eventDateId")
	private EventDate eventDate;
	
	private String name;
	
	@OneToMany(mappedBy="blitType",targetEntity=BlitTypeSeat.class)
	List<BlitTypeSeat> blitTypeSeats;
	
	@OneToMany(mappedBy="blitType",targetEntity=CommonBlit.class)
	List<CommonBlit> commonBlits;
	
	@ManyToMany
	private List<Discount> discounts;
	
	public List<Discount> getDiscounts() {
		return discounts;
	}

	public void setDiscounts(List<Discount> discounts) {
		this.discounts = discounts;
	}

	public BlitType() {
		blitTypeSeats = new ArrayList<>();
		commonBlits = new ArrayList<>();
		discounts = new ArrayList<>();
	}
	
	public List<BlitTypeSeat> getBlitTypeSeats() {
		return blitTypeSeats;
	}

	public void setBlitTypeSeats(List<BlitTypeSeat> blitTypeSeats) {
		this.blitTypeSeats = blitTypeSeats;
	}

	public List<CommonBlit> getCommonBlits() {
		return commonBlits;
	}

	public void setCommonBlits(List<CommonBlit> commonBlits) {
		this.commonBlits = commonBlits;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public State getBlitTypeState() {
		return blitTypeState;
	}

	public void setBlitTypeState(State blitTypeState) {
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
		eventDate.getBlitTypes().add(this);
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
