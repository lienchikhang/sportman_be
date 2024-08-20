package com.sportman.annotations;

import com.sportman.validators.IsAfterStartYearValidator;
import com.sportman.validators.IsValidYearValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.Year;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME) // annotation được sử lý lúc nào - luc runtime
@Constraint(validatedBy = IsAfterStartYearValidator.class)
public @interface IsAfterStartYear {

    String message() default "INVALID_YEAR";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
