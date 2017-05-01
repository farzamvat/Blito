package com.blito.rest.viewmodels;

import com.blito.enums.State;

public class BlitTypeViewModel {
	long blitTypeId;
	String name;
	int capacity;
	int soldCount;
	long price;
	State blitTypeState;
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
	public boolean isFree() {
		return isFree;
	}
	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}
	
	
	
}
