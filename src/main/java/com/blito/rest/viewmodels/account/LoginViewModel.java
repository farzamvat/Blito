package com.blito.rest.viewmodels.account;

import javax.validation.constraints.NotNull;

import com.blito.annotations.Email;

public class LoginViewModel {
	@Email
	String email;
	@NotNull
	String password;
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
}
