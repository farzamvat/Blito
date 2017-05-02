package com.blito.rest.viewmodels.account;

import javax.validation.constraints.NotNull;

import com.blito.annotations.MobileNumber;

public class UserInfoViewModel {
	@NotNull
	private String firstname;
	@NotNull
	private String lastname;
	@MobileNumber
	private String mobile;

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
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
