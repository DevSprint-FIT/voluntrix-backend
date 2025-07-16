package com.DevSprint.voluntrix_backend.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.DevSprint.voluntrix_backend.enums.UserType;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresRole {
    UserType[] value();
    String message() default "Access denied: insufficient privileges";
}
