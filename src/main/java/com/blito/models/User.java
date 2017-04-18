package com.blito.models;

import java.util.List;

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
	
	@ManyToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
    @JoinTable(name="user_role" , joinColumns=@JoinColumn(name="user_id"), 
    inverseJoinColumns=@JoinColumn(name="role_id"))
    private List<Role> roles;
	
	@OneToMany(mappedBy="user",targetEntity=EventHost.class, cascade=CascadeType.ALL)
	private List<EventHost> eventHosts;
	
	@OneToMany(mappedBy="user",targetEntity=Blit.class, cascade=CascadeType.ALL)
	private List<Blit> blits;
	
	@OneToMany(mappedBy="user", targetEntity=ExchangeBlit.class, fetch=FetchType.LAZY)
	private List<ExchangeBlit> exchangeBlits;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
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
