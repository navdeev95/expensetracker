package io.github.nikoir.expensetracker.dto.request.validator.implementation;

import io.github.nikoir.expensetracker.dto.request.validator.ValidCurrencyCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

import static io.github.nikoir.expensetracker.dto.request.validator.util.ValidatorUtils.buildViolation;

public class ValidCurrencyCodeValidator implements ConstraintValidator<ValidCurrencyCode, String> {
    private static final String ISO_4217_PATTERN = "^[A-Z]{3}$";
    private boolean required;

    @Override
    public void initialize(ValidCurrencyCode constraintAnnotation) {
        this.required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
            if (required) {
                buildViolation(context,"Значение кода валюты должно быть указано");
                return false;
            }
            return true;
        }

        if (!value.matches(ISO_4217_PATTERN)) {
            buildViolation(context, "Код валюты должен быть в формате ISO 4217 (например, USD, EUR)");
            return false;
        }

        return true;
    }
}
