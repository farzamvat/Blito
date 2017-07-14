package com.blito.validators;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.blito.annotations.MobileNumber;
import com.blito.configs.Constants;


public class MobileNumberValidation implements ConstraintValidator<MobileNumber, String>{

	@Override
	public void initialize(MobileNumber constraintAnnotation) {
		
	}

	@Override
	public boolean isValid(String mobileNumber, ConstraintValidatorContext context) {
		if(mobileNumber == null || mobileNumber.isEmpty()) return false;
		return Pattern.compile(Constants.PHONE_REGEX).matcher(mobileNumber).matches();
	}

}
