package io.github.nikoir.expensetracker.dto.request.validator.implementation;

import io.github.nikoir.expensetracker.dto.request.validator.ValidPeriodCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

import static io.github.nikoir.expensetracker.dto.request.validator.util.ValidatorUtils.buildViolation;

public class ValidPeriodCodeValidator implements ConstraintValidator<ValidPeriodCode, String> {
    private boolean required;
    @Override
    public void initialize(ValidPeriodCode constraintAnnotation) {
        this.required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
            if (required) {
                buildViolation(context,"Значение кода периода должно быть указано");
                return false;
            }
            return true;
        }

        if (value.length() > 20) {
            buildViolation(context, "Код периода не должен быть больше 20 символов");
            return false;
        }

        return true;
    }
}
