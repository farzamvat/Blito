package com.blito.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.blito.annotations.Email;


public class EmailValidation implements ConstraintValidator<Email, String> {

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		if (email == null) return false;
		Pattern pattern = Pattern.compile(
				"^[-!#$%&'*+/0-9=?A-Z^_a-z{|}~](\\.?[-!#$%&'*+/0-9=?A-Z^_a-z{|}~])*@[a-zA-Z](-?[a-zA-Z0-9])*(\\.[a-zA-Z](-?[a-zA-Z0-9])*)+$");
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	@Override
	public void initialize(Email constraintAnnotation) {
	}

}
