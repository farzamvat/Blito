package com.blito.rest.viewmodels.blittype;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.blito.enums.State;
import com.blito.rest.viewmodels.View;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

public class BlitTypeViewModel {
	@JsonView(View.BlitType.class)
	long blitTypeId;
	@JsonView(View.BlitType.class)
	@NotEmpty
	String name;
	@JsonView(View.BlitType.class)
	@NotNull
	@Min(1)
	int capacity;
	@JsonView(View.BlitType.class)
	int soldCount;
	@JsonView(View.BlitType.class)
	long price;
	@JsonView(View.BlitType.class)
	State blitTypeState;
	@JsonView(View.BlitType.class)
	@NotNull
	boolean isFree;
	public long getBlitTypeId() {
		return blitTypeId;
	}
	public void setBlitTypeId(long blitTypeId) {
		this.blitTypeId = blitTypeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public State getBlitTypeState() {
		return blitTypeState;
	}
	public void setBlitTypeState(State blitTypeState) {
		this.blitTypeState = blitTypeState;
	}
	@JsonProperty("isFree")
	public boolean isFree() {
		return isFree;
	}
	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}
	
	
	
}
