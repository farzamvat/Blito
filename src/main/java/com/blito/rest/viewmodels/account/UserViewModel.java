package com.blito.rest.viewmodels.account;

import java.util.List;

import com.blito.rest.viewmodels.blit.BlitViewModel;
import com.blito.rest.viewmodels.eventhost.EventHostViewModel;
import com.blito.rest.viewmodels.exchangeblit.ExchangeBlitViewModel;

public class UserViewModel {
	long userId;
	String firstname;
	String lastname;
	String email;
	String mobile;
	String password;
	boolean isActive;
	boolean banned;
	
	List<EventHostViewModel> eventHosts;
	List<BlitViewModel> blits;
	List<ExchangeBlitViewModel> exchangeBlits;

	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public boolean isBanned() {
		return banned;
	}
	public void setBanned(boolean banned) {
		this.banned = banned;
	}
	public List<EventHostViewModel> getEventHosts() {
		return eventHosts;
	}
	public void setEventHosts(List<EventHostViewModel> eventHosts) {
		this.eventHosts = eventHosts;
	}
	public List<BlitViewModel> getBlits() {
		return blits;
	}
	public void setBlits(List<BlitViewModel> blits) {
		this.blits = blits;
	}
	public List<ExchangeBlitViewModel> getExchangeBlits() {
		return exchangeBlits;
	}
	public void setExchangeBlits(List<ExchangeBlitViewModel> exchangeBlits) {
		this.exchangeBlits = exchangeBlits;
	}
	
	
	
	
}
