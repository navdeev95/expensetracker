package io.github.nikoir.expensetracker.dto.request.validator.implementation;
import io.github.nikoir.expensetracker.dto.request.validator.ValidId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import static io.github.nikoir.expensetracker.dto.request.validator.utils.ValidatorUtils.buildViolation;

public class ValidIdValidator implements ConstraintValidator<ValidId, Long> {
    private String name;
    private boolean required;
    @Override
    public void initialize(ValidId constraintAnnotation) {
        this.name = constraintAnnotation.name();
        this.required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) {
            if (required) {
                buildViolation(context, String.format("Значение параметра %s должно быть указано", name));
                return false;
            }
            return true;
        }

        if (value <= 0) {
            buildViolation(context, String.format("Значение параметра %s должно быть больше 0", name));
            return false;
        }

        return true;
    }
}
