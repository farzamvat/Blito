package com.blito.validators;

import com.blito.annotations.AparatUrl;
import com.blito.configs.Constants;
import io.vavr.control.Option;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AparatUrlValidation implements ConstraintValidator<AparatUrl,String> {
    @Override
    public void initialize(AparatUrl aparatUrl) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return Option.of(value).map(url -> {
            if(!value.startsWith(Constants.APARAT_STARTING_URL) || value.endsWith(Constants.APARAT_STARTING_URL))
                return false;
            else {
                return true;
            }
        }).getOrElse(true);

    }
}
