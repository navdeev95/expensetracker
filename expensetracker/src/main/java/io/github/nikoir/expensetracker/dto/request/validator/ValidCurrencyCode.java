package io.github.nikoir.expensetracker.dto.request.validator;

import io.github.nikoir.expensetracker.dto.request.validator.implementation.ValidCurrencyCodeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidCurrencyCodeValidator.class)
public @interface ValidCurrencyCode {
    String message() default "Невалидный код валюты";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    boolean required() default true;
}
