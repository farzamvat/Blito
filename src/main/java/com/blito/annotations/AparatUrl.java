package com.blito.annotations;

import com.blito.validators.AparatUrlValidation;
import com.blito.validators.EmailValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AparatUrlValidation.class)
public @interface AparatUrl {

    public String message() default "Aparat validation failed";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
