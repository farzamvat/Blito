package com.blito.enums.validation;

import com.blito.validators.ValidationInterface;

public enum LoginEnumValidation implements ValidationInterface {
	EMAIL("validation.login.email"),
	PASSWORD("validation.login.password");
	
	private final String message;
	
	LoginEnumValidation(String message)
	{
		this.message = message;
	}

	@Override
	public String get() {
		return this.message;
	}
}