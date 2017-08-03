package com.blito.rest.viewmodels.account;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.blito.annotations.Email;
import com.blito.annotations.MobileNumber;
import com.blito.configs.Constants;

public class RegisterVm {
	@NotEmpty
	String firstname;
	@NotEmpty
	String lastname;
	@Email
	String email;
	@Size(min=Constants.PASSWORD_MIN_LENGTH, max=Constants.PASSWORD_MAX_LENGTH)
	String password;
	@Size(min=Constants.PASSWORD_MIN_LENGTH, max=Constants.PASSWORD_MAX_LENGTH)
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
		this.email = email.toLowerCase();
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
