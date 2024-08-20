package com.sportman.annotations;

import com.sportman.validators.IsValidYearValidator;
import com.sportman.validators.NonZeroValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME) // annotation được sử lý lúc nào - luc runtime
@Constraint(validatedBy = NonZeroValidator.class)
public @interface NonZero {
    String message() default "SEASON_YEAR_INVALID";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
