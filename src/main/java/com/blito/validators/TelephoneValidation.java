package com.blito.validators;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.blito.annotations.Telephone;
import com.blito.configs.Constants;

public class TelephoneValidation implements ConstraintValidator<Telephone, String> {

	@Override
	public void initialize(Telephone arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValid(String telephone, ConstraintValidatorContext context) {
		if(telephone == null) return false;
		return Pattern.compile(Constants.TEL_REGEX).matcher(telephone).matches();
	}

}
