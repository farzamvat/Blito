package com.blito.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.blito.validators.EmailValidation;


@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidation.class)
public @interface Email {
	public String message() default "Email validation failed";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	
}