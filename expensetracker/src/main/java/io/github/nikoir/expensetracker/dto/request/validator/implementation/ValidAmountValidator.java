package io.github.nikoir.expensetracker.dto.request.validator.implementation;

import io.github.nikoir.expensetracker.dto.request.validator.ValidAmount;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import static io.github.nikoir.expensetracker.dto.request.validator.utils.ValidatorUtils.buildViolation;

public class ValidAmountValidator implements ConstraintValidator<ValidAmount, BigDecimal> {

    private boolean required;
    private static final BigDecimal MIN_VALUE = new BigDecimal("0.01");
    private static final int MAX_INTEGER_DIGITS = 13;
    private static final int MAX_FRACTION_DIGITS = 2;

    @Override
    public void initialize(ValidAmount constraintAnnotation) {
        this.required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (value == null) {
            if (required) {
                buildViolation(context, "Сумма не может быть пустой");
                return false;
            }
            return true;
        }

        // Проверка минимального значения
        if (value.compareTo(MIN_VALUE) < 0) {
            buildViolation(context, "Сумма должна быть не меньше 0.01");
            return false;
        }

        // Проверка количества цифр
        int integerDigits = value.precision() - value.scale();
        int fractionDigits = Math.max(value.scale(), 0);

        if (integerDigits > MAX_INTEGER_DIGITS || fractionDigits > MAX_FRACTION_DIGITS) {
            buildViolation(context, "Сумма должна содержать до 2 знаков после запятой");
            return false;
        }

        return true;
    }
}