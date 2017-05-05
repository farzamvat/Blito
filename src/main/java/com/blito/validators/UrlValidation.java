package com.blito.validators;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.blito.annotations.Url;
import com.blito.configs.Constants;


public class UrlValidation implements ConstraintValidator<Url, String> {

	@Override
	public void initialize(Url constraintAnnotation) {
		
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value == null)
			return true;
		return Pattern.compile(Constants.LINK_REGEX).matcher(value).matches();
	}

}
