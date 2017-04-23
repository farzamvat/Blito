package com.blito.enums.validation;

import com.blito.validators.ValidationInterface;

public enum RegisterEnumValidation implements ValidationInterface {
	FIRSTNAME("validation.register.firstname"),
	LASTNAME("validation.register.lastname"),
	EMAIL("validation.register.email"),
	PASSWORD("validation.register.password"),
	MOBILE("validation.register.mobile");
	
	private final String message;
	
	private RegisterEnumValidation(String value)
	{
		this.message = value;
	}
	@Override
	public String get() {
		return this.message;
	}
}
