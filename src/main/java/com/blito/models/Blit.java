package com.blito.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name="blit")
public class Blit {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private long blitId;
	
	private int count;
	
	private long totalAmount;
	
	@ManyToOne
	@JoinColumn(name="blitTypeId")
	private BlitType blitType;
	
	@ManyToOne
	@JoinColumn(name="userId")
	User user;
	
	public long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(long totalAmount) {
		this.totalAmount = totalAmount;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getBlitId() {
		return blitId;
	}

	public void setBlitId(long blitId) {
		this.blitId = blitId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public BlitType getBlitType() {
		return blitType;
	}

	public void setBlitType(BlitType blitType) {
		this.blitType = blitType;
	}
	
	
}
