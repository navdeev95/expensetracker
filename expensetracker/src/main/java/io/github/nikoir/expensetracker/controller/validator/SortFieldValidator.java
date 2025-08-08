package io.github.nikoir.expensetracker.controller.validator;

import io.github.nikoir.expensetracker.enums.BudgetSortField;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SortFieldValidator implements ConstraintValidator<ValidSortField, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
            value = BudgetSortField.ID.getFieldName();
        }
        String finalValue = value;
        boolean isValid = Arrays.stream(BudgetSortField.values())
                .map(BudgetSortField::getFieldName)
                .anyMatch(field -> field.equalsIgnoreCase(finalValue));

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Недопустимое поле для сортировки. Допустимые значения: "
                            + Arrays.stream(BudgetSortField.values())
                            .map(BudgetSortField::getFieldName)
                            .collect(Collectors.joining(", "))
            ).addConstraintViolation();
        }

        return isValid;
    }
}
