package com.blito.rest.viewmodels.eventhost;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.blito.annotations.Url;
import com.blito.enums.HostType;
import com.blito.rest.viewmodels.View;
import com.blito.rest.viewmodels.image.ImageViewModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

public class EventHostViewModel {
	@JsonView(View.SimpleEventHost.class)
	long eventHostId;
	
	@JsonView(View.SimpleEventHost.class)
	@NotNull
	String hostName;
	
	@JsonView(View.EventHost.class)
	String description;
	
	@JsonView(View.EventHost.class)
	//@Telephone
	String telephone;
	
	@JsonView(View.EventHost.class)
	@Url
	String telegramLink;
	
	@JsonView(View.EventHost.class)
	@Url
	String instagramLink;
	
	@JsonView(View.EventHost.class)
	@Url
	String twitterLink;
	
	@JsonView(View.EventHost.class)
	@Url
	String linkedinLink;
	
	@JsonView(View.EventHost.class)
	@Url
	String websiteLink;
	
	@JsonView(View.EventHost.class)
	@NotNull
	HostType hostType;
	
	@JsonView(View.AdminEventHost.class)
	boolean isDeleted;
	
	@JsonView(View.SimpleEventHost.class)
	private String eventHostLink;
	
	@JsonView(View.SimpleEventHost.class)
	Set<ImageViewModel> images;
	
	public EventHostViewModel()
	{
		images = new HashSet<>();
	}
	
	@JsonProperty("isDeleted")
	public boolean isDeleted() {
		return isDeleted;
	}

	public String getEventHostLink() {
		return eventHostLink;
	}

	public void setEventHostLink(String eventHostLink) {
		this.eventHostLink = eventHostLink;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Set<ImageViewModel> getImages() {
		return images;
	}
	public void setImages(Set<ImageViewModel> images) {
		this.images = images;
	}
	public long getEventHostId() {
		return eventHostId;
	}
	public void setEventHostId(long eventHostId) {
		this.eventHostId = eventHostId;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public String getTelegramLink() {
		return telegramLink;
	}
	public void setTelegramLink(String telegramLink) {
		this.telegramLink = telegramLink;
	}
	public String getInstagramLink() {
		return instagramLink;
	}
	public void setInstagramLink(String instagramLink) {
		this.instagramLink = instagramLink;
	}
	public String getTwitterLink() {
		return twitterLink;
	}
	public void setTwitterLink(String twitterLink) {
		this.twitterLink = twitterLink;
	}
	public String getLinkedinLink() {
		return linkedinLink;
	}
	public void setLinkedinLink(String linkedinLink) {
		this.linkedinLink = linkedinLink;
	}
	public String getWebsiteLink() {
		return websiteLink;
	}
	public void setWebsiteLink(String websiteLink) {
		this.websiteLink = websiteLink;
	}
	public HostType getHostType() {
		return hostType;
	}
	public void setHostType(HostType hostType) {
		this.hostType = hostType;
	}
}
