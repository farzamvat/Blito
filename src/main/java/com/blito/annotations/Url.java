package com.blito.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.blito.validators.UrlValidation;

@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UrlValidation.class)
public @interface Url {
	String message() default "url validation failed";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
