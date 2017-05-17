package com.blito.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.blito.enums.HostType;

@Entity(name="event_host")
public class EventHost {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	long eventHostId;
	
	@OneToMany(mappedBy="eventHost",targetEntity=Event.class)
	List<Event> events;

	@Column(name="host_name")
	private String hostName;
	
	@NotNull
	private String telephone;
	
	@Column(name="website_link")
	private String websiteLink;
	
	@Column(name="telegram_link")
	private String telegramLink;
	
	@Column(name="instagram_link")
	private String instagramLink;
	
	@Column(name="twitter_link")
	private String twitterLink;
	
	@Column(name="linkedin_link")
	private String linkedinLink;
	
	@Column(name="event_type")
	@Enumerated(EnumType.STRING)
	private HostType hostType;
	
	@ManyToOne(targetEntity=User.class)
	@JoinColumn(name="userId")
	User user;
	
	@OneToMany
	@Cascade({CascadeType.ALL})
	@JoinColumn(name="eventHostId")
	List<Image> images;
	
	String description;
	
	public EventHost() {
		images = new ArrayList<>();
		events = new ArrayList<>();
	}
	
	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLinkedinLink() {
		return linkedinLink;
	}

	public void setLinkedinLink(String linkedinLink) {
		this.linkedinLink = linkedinLink;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getEventHostId() {
		return eventHostId;
	}

	public void setEventHostId(long eventHostId) {
		this.eventHostId = eventHostId;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
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

	public String getWebsiteLink() {
		return websiteLink;
	}

	public void setWebsiteLink(String websiteLink) {
		this.websiteLink = websiteLink;
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

	public HostType getHostType() {
		return hostType;
	}

	public void setHostType(HostType hostType) {
		this.hostType = hostType;
	}
}
