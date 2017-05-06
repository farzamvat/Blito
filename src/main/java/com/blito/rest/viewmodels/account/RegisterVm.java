package com.blito.rest.viewmodels.account;

import javax.validation.constraints.NotNull;

import com.blito.annotations.Email;
import com.blito.annotations.MobileNumber;

public class RegisterVm {
	@NotNull
	String firstname;
	@NotNull
	String lastname;
	@Email
	String email;
	@NotNull
	String password;
	@NotNull
	String confirmPassword;
	@MobileNumber
	String mobile;
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
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}