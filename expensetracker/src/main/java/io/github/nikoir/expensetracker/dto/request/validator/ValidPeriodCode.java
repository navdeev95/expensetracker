package io.github.nikoir.expensetracker.dto.request.validator;

import io.github.nikoir.expensetracker.dto.request.validator.implementation.ValidPeriodCodeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPeriodCodeValidator.class)
public @interface ValidPeriodCode {
    String message() default "Невалидный код периода";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    boolean required() default true;
}
