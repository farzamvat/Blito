package com.blito.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity(name="index_banner")
public class IndexBanner {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	long indexBannerId;
	String title;
	String description;
	@OneToOne
	Image image;
	@OneToOne(optional=true)
	Event event;
	public long getIndexBannerId() {
		return indexBannerId;
	}
	public void setIndexBannerId(long indexBannerId) {
		this.indexBannerId = indexBannerId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	
}
