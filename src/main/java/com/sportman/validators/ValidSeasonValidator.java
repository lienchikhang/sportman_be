package com.sportman.validators;

import com.sportman.annotations.NonZero;
import com.sportman.annotations.ValidSeason;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Collections;
import java.util.List;

public class ValidSeasonValidator implements ConstraintValidator<ValidSeason, List<Integer>> {
    @Override
    public void initialize(ValidSeason constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<Integer> value, ConstraintValidatorContext context) {

        if (value.isEmpty()) return true;

        int startYear = value.get(0);
        int endYear = value.get(1);

        return startYear < endYear;
    }
}
