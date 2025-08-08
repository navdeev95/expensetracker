package io.github.nikoir.expensetracker.dto.request.validator;

import io.github.nikoir.expensetracker.dto.request.validator.implementation.ValidIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidIdValidator.class)
public @interface ValidId {
    String message() default "Невалидный id";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String name() default "Id";
    boolean required() default true;
}
