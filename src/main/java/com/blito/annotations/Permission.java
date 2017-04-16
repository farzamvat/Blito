package com.blito.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.blito.enums.ApiBusinessName;

@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {
	ApiBusinessName value();
}
