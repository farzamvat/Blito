package com.blito.validators;

import com.blito.annotations.AdditionalFields;
import com.blito.configs.Constants;
import com.blito.rest.viewmodels.event.AdditionalField;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdditionalFieldsValidation implements ConstraintValidator<AdditionalFields, List<AdditionalField>> {

	@Override
	public void initialize(AdditionalFields constraintAnnotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValid(List<AdditionalField> additionalFieldList, ConstraintValidatorContext context) {
		Map<String,String> value =
				additionalFieldList
						.stream()
						.collect(Collectors.toMap(AdditionalField::getKey,AdditionalField::getValue));
		if(value == null || value.isEmpty())
			return true;
		if(value.keySet().stream().distinct().count() != value.keySet().size())
			return false;
		else if(value.values().stream().anyMatch(field -> !field.equals(Constants.FIELD_STRING_TYPE)))
			return false;
		return true;
	}
}
