package com.blito.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity(name="user")
public class User {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	long userId;
	
	@ManyToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
    @JoinTable(name="role" , joinColumns=@JoinColumn(name="user_id"), 
    inverseJoinColumns=@JoinColumn(name="role_id"))
    private List<Role> roles;

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
	
}
