package com.unishare.unishare.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EduEmailValidator implements ConstraintValidator<EduEmail, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(value == null  || value.isBlank()) return false;
        return value.trim().toLowerCase().endsWith(".edu");
    }
}
