package com.blito.rest.viewmodels;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.blito.rest.viewmodels.image.ImageViewModel;
import com.fasterxml.jackson.annotation.JsonView;

public class BannerViewModel {
	@JsonView(View.IndexBanner.class)
	long indexBannerId;
	@JsonView(View.AdminIndexBanner.class)
	boolean active;
	@NotEmpty
	@JsonView(View.IndexBanner.class)
	String title;
	@NotEmpty
	@JsonView(View.IndexBanner.class)
	String description;
	@NotNull
	@JsonView(View.IndexBanner.class)
	ImageViewModel image;
	@NotNull
	@JsonView(View.IndexBanner.class)
	long eventId;
	
	public long getIndexBannerId() {
		return indexBannerId;
	}
	public void setIndexBannerId(long indexBannerId) {
		this.indexBannerId = indexBannerId;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
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
	public ImageViewModel getImage() {
		return image;
	}
	public void setImage(ImageViewModel image) {
		this.image = image;
	}
	public long getEventId() {
		return eventId;
	}
	public void setEventId(long eventId) {
		this.eventId = eventId;
	}
}
