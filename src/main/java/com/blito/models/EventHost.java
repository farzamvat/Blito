package com.blito.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.blito.enums.HostType;

@Entity(name="event_host")
public class EventHost {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	long eventHostID;
	
	@OneToMany(mappedBy="event_host")
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
	
	@Column(name="event_type")
	@Enumerated(EnumType.STRING)
	private HostType hostType;
	
	public long getEventHostID() {
		return eventHostID;
	}

	public void setEventHostID(long eventHostID) {
		this.eventHostID = eventHostID;
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
