package com.sportman.validators;

import com.sportman.annotations.IsAfterStartYear;
import com.sportman.annotations.IsValidYear;
import com.sportman.dto.request.SeasonCreateRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Year;
import java.util.Objects;

public class IsAfterStartYearValidator implements ConstraintValidator<IsAfterStartYear, SeasonCreateRequest> {


    @Override
    public void initialize(IsAfterStartYear constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);

    }

    @Override
    public boolean isValid(SeasonCreateRequest value, ConstraintValidatorContext context) {

        int yearStart = value.getYearStart();
        int yearEnd = value.getYearEnd();

        if (yearStart < yearEnd) return true;

        return false;
    }

}
