package com.bankapp.bankapp.app.impl;


import com.bankapp.bankapp.app.exception.validation.annotation.IDChecker;
import jakarta.validation.ConstraintValidator;

import jakarta.validation.ConstraintValidatorContext;
import java.util.Optional;

public class IDCheckerConstraint  implements ConstraintValidator<IDChecker, String> {

    private static final String PATTERN = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";

    @Override
    public void initialize(IDChecker constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return Optional.ofNullable(s)
                .filter(p->!p.isBlank())
                .map(p->p.matches(PATTERN))
                .orElse(false);
    }
}