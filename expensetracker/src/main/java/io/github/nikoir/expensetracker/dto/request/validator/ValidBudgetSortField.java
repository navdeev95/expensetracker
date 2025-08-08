package io.github.nikoir.expensetracker.dto.request.validator;

import io.github.nikoir.expensetracker.dto.request.validator.implementation.ValidBudgetSortFieldValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidBudgetSortFieldValidator.class)
public @interface ValidBudgetSortField {
    String message() default "Недопустимое поле для сортировки";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}