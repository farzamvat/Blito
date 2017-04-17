package com.blito.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name="blit")
public class Blit {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private String blitId;
	
	private int count;
	
	@ManyToOne
	private BlitType blitType;

	public String getBlitId() {
		return blitId;
	}

	public void setBlitId(String blitId) {
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
