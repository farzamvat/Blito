package com.blito.models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name="blit_type")
public class BlitType {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private String blitTypeId;
	
	private boolean isFree;
	
	private int count;
	
	private Timestamp date;
	
	@ManyToOne
	private Event event;
	

	public String getBlitTypeId() {
		return blitTypeId;
	}

	public void setBlitTypeId(String blitTypeId) {
		this.blitTypeId = blitTypeId;
	}

	public boolean isFree() {
		return isFree;
	}

	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}
	
	
}
