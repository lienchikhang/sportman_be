package com.sportman.validators;

import com.sportman.annotations.IsValidYear;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Year;
import java.util.Objects;

public class IsValidYearValidator implements ConstraintValidator<IsValidYear, Integer> {


    @Override
    public void initialize(IsValidYear constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {

        if (Objects.isNull(value)) return true;

        return Year.now().getValue() > value;
    }

}
