package com.blito.rest.viewmodels.blittype;

import com.blito.enums.State;
import com.blito.rest.viewmodels.View;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

public class BlitTypeViewModel {
	@JsonView(View.BlitType.class)
	private long blitTypeId;
	@JsonView(View.BlitType.class)
	@NotEmpty
	private String name;
	@JsonView(View.BlitType.class)
	@NotNull
	@Min(1)
	private int capacity;
	@JsonView(View.BlitType.class)
	private int soldCount;
	@JsonView(View.BlitType.class)
	private long price;
	@JsonView(View.BlitType.class)
	private State blitTypeState;
	@JsonView(View.BlitType.class)
	@NotNull
	private boolean isFree;
	@JsonView(View.BlitType.class)
	private Set<String> seatUids;

	public BlitTypeViewModel() {
		this.setSeatUids(new HashSet<>());
	}

	public Set<String> getSeatUids() {
		return seatUids;
	}

	public void setSeatUids(Set<String> seatUids) {
		this.seatUids = seatUids;
	}

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

	@Override
	public String toString() {
		return "BlitTypeViewModel{" +
				"blitTypeId=" + blitTypeId +
				", name='" + name + '\'' +
				", capacity=" + capacity +
				", soldCount=" + soldCount +
				", price=" + price +
				", blitTypeState=" + blitTypeState +
				", isFree=" + isFree +
				", seatUids=" + seatUids +
				'}';
	}
}
