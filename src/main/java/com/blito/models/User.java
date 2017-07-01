package com.blito.models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity(name="user")
public class User {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	long userId;
	
	@Column(name="first_name")
	private String firstname;
	
	@Column(name="last_name")
	private String lastname;
	
	@NotNull
	private String email;
	
	@Size(min=8, max=60)
	private String password;
	
	private String mobile;
	
	private String activationKey;
	
	private String resetKey;
	
	private Integer wrongTry;
	
	private boolean banned = false;
	
	private boolean isActive = false;
	
	private boolean isFirstTimeLogin = true;
	
	private String refreshToken;
	
	private boolean isOldUser = false;
	
	private Timestamp createdAt;
	
	@ManyToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
    @JoinTable(name="user_role" , joinColumns=@JoinColumn(name="user_id"), 
    inverseJoinColumns=@JoinColumn(name="role_id"))
    private Set<Role> roles;
	
	@OneToMany(mappedBy="user",targetEntity=EventHost.class, cascade=CascadeType.ALL, orphanRemoval = true)
	private Set<EventHost> eventHosts;
	
	@OneToMany(mappedBy="user",targetEntity=Blit.class, cascade=CascadeType.ALL)
	private Set<Blit> blits;
	
	@OneToMany(mappedBy="user", targetEntity=ExchangeBlit.class, fetch=FetchType.LAZY)
	private Set<ExchangeBlit> exchangeBlits;
	
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	
	public void removeExchangeBlit(ExchangeBlit blit) {
		this.exchangeBlits.removeIf(b -> b.getExchangeBlitId() == blit.getExchangeBlitId());
		blit.removeUser();
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public User() {
		roles = new HashSet<>();
		eventHosts = new HashSet<>();
		blits = new HashSet<>();
		exchangeBlits = new HashSet<>();
	}
	
	public void addBlits(Blit blit)
	{
		this.blits.add(blit);
		blit.setUser(this);
	}
	
	
	public boolean isOldUser() {
		return isOldUser;
	}



	public void setOldUser(boolean isOldUser) {
		this.isOldUser = isOldUser;
	}



	public String getResetKey() {
		return resetKey;
	}

	public void setResetKey(String resetKey) {
		this.resetKey = resetKey;
	}

	public Integer getWrongTry() {
		return wrongTry;
	}

	public void setWrongTry(Integer wrongTry) {
		this.wrongTry = wrongTry;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public boolean isFirstTimeLogin() {
		return isFirstTimeLogin;
	}

	public void setFirstTimeLogin(boolean isFirstTimeLogin) {
		this.isFirstTimeLogin = isFirstTimeLogin;
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

	public String getActivationKey() {
		return activationKey;
	}

	public void setActivationKey(String activationKey) {
		this.activationKey = activationKey;
	}

	public Set<EventHost> getEventHosts() {
		return eventHosts;
	}

	public void setEventHosts(Set<EventHost> eventHosts) {
		this.eventHosts = eventHosts;
	}

	public Set<Blit> getBlits() {
		return blits;
	}

	public void setBlits(Set<Blit> blits) {
		this.blits = blits;
	}

	public Set<ExchangeBlit> getExchangeBlits() {
		return exchangeBlits;
	}

	public void setExchangeBlits(Set<ExchangeBlit> exchangeBlits) {
		this.exchangeBlits = exchangeBlits;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
}
