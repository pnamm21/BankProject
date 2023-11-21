package com.bankapp.bankapp.app.exception.validation.annotation;

import com.bankapp.bankapp.app.impl.IDCheckerConstraint;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IDCheckerConstraint.class)
public @interface IDChecker {

    String message() default "----ILLEGAL ID----";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}