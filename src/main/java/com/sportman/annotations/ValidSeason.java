package com.sportman.annotations;

import com.sportman.validators.NonZeroValidator;
import com.sportman.validators.ValidSeasonValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME) // annotation được sử lý lúc nào - luc runtime
@Constraint(validatedBy = ValidSeasonValidator.class)
public @interface ValidSeason {

    String message() default "SEASON_YEAR_INVALID";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
