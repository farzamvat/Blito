package com.blito.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.blito.annotations.Email;
import com.blito.configs.Constants;


public class EmailValidation implements ConstraintValidator<Email, String> {

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		if (email == null) return false;
		Pattern pattern = Pattern.compile(
				Constants.EMAIL_REGEX);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	@Override
	public void initialize(Email constraintAnnotation) {
	}

}
