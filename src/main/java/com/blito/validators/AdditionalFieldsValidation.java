package com.blito.validators;

import com.blito.annotations.AdditionalFields;
import com.blito.configs.Constants;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

public class AdditionalFieldsValidation implements ConstraintValidator<AdditionalFields, Map<String,String>> {

	@Override
	public void initialize(AdditionalFields constraintAnnotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValid(Map<String, String> value, ConstraintValidatorContext context) {
		if(value == null || value.isEmpty())
			return true;
		if(value.keySet().stream().distinct().count() != value.keySet().size())
			return false;
		else if(!value.values().stream().allMatch(field -> field.equals(Constants.FIELD_STRING_TYPE)))
			return false;
		return true;
//		List<String> schemaTypes = Arrays.asList(Constants.FIELD_DOUBLE_TYPE,Constants.FIELD_IMAGE_TYPE,Constants.FIELD_INT_TYPE,Constants.FIELD_STRING_TYPE);

//		if(value.values().stream().filter(type -> type.equals(Constants.FIELD_IMAGE_TYPE)).count() > 1)
//			return false;
	}

}
