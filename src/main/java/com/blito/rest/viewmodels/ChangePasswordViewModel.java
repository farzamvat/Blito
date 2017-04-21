package com.blito.rest.viewmodels;

import javax.validation.constraints.NotNull;

public class ChangePasswordViewModel {
	@NotNull
	String oldPassowrd;
	@NotNull
	String newPassword;
	@NotNull
	String confirmNewPassword;
	public String getOldPassowrd() {
		return oldPassowrd;
	}
	public void setOldPassowrd(String oldPassowrd) {
		this.oldPassowrd = oldPassowrd;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}
	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}
	
}
