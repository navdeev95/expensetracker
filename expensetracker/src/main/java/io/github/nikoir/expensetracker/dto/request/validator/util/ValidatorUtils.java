package io.github.nikoir.expensetracker.dto.request.validator.util;

import jakarta.validation.ConstraintValidatorContext;

public class ValidatorUtils {
    public static void buildViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}
