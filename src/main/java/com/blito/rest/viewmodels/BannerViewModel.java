package com.blito.rest.viewmodels;

import javax.validation.constraints.NotNull;

import com.blito.rest.viewmodels.image.ImageViewModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(Include.NON_EMPTY)
public class BannerViewModel {
	@JsonView(View.IndexBanner.class)
	long indexBannerId;
	@JsonView(View.IndexBanner.class)
	String title;
	@JsonView(View.IndexBanner.class)
	String description;
	@NotNull
	@JsonView(View.IndexBanner.class)
	ImageViewModel image;
	@JsonView(View.IndexBanner.class)
	String eventLink;
	
	public String getEventLink() {
		return eventLink;
	}
	public void setEventLink(String eventLink) {
		this.eventLink = eventLink;
	}
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
	public ImageViewModel getImage() {
		return image;
	}
	public void setImage(ImageViewModel image) {
		this.image = image;
	}
}
