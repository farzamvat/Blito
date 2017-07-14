package com.blito.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.blito.validators.AdditionalFieldsValidation;

@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AdditionalFieldsValidation.class)
public @interface AdditionalFields {
	String message() default "additional fields validation failed";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
