package com.blito.validators;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.blito.annotations.AdditionalFields;
import com.blito.configs.Constants;

public class AdditionalFieldsValidation implements ConstraintValidator<AdditionalFields, Map<String,String>> {

	@Override
	public void initialize(AdditionalFields constraintAnnotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValid(Map<String, String> value, ConstraintValidatorContext context) {
		if(value.isEmpty() || value == null)
			return true;
		if(value.keySet().stream().distinct().count() != value.keySet().stream().count())
			return false;
		List<String> schemaTypes = Arrays.asList(Constants.FIELD_DOUBLE_TYPE,Constants.FIELD_IMAGE_TYPE,Constants.FIELD_INT_TYPE,Constants.FIELD_STRING_TYPE);
		if(value.values().stream().filter(type -> type.equals(Constants.FIELD_IMAGE_TYPE)).count() > 1)
			return false;
		if(value.values().stream().allMatch(field -> schemaTypes.contains(field)))
			return true;
		return false;
	}

}
