package com.blito.enums;

import com.blito.validators.ValidationInterface;

public enum RegisterEnumValidation implements ValidationInterface {
	FIRSTNAME("validation.firstname"),
	LASTNAME("validation.lastname"),
	EMAIL("validation.email"),
	PASSWORD("validation.password"),
	MOBILE("validation.mobile");
	
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
