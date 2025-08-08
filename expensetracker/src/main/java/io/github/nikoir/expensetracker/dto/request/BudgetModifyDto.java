package io.github.nikoir.expensetracker.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BudgetModifyDto(
        @NotNull(message = "Id не может быть пустым")
        @Min(value = 1, message = "id должен быть больше 1")
        Long id,

        @NotNull(message = "Id категории не может быть пустым")
        @Min(value = 1, message = "id должен быть больше 1")
        Long categoryId,

        @NotBlank(message = "Код периода не может быть пустым")
        @Size(min = 1, max = 20, message = "Длина кода периода должна быть от 1 до 20")
        String periodCode,

        @NotNull(message = "Сумма не может быть пустой")
        @DecimalMin(value = "0.01", message = "Сумма должна быть не меньше 0.01")
        @Digits(integer = 13, fraction = 2, message = "Сумма должна содержать до 2 знаков после запятой")
        BigDecimal amount,

        LocalDate startDate,
        LocalDate endDate) {
        @AssertTrue(message = "startDate и endDate должны быть либо оба null, либо endDate >= startDate")
        public boolean isDateRangeValid() {
            if (startDate == null && endDate == null) {
                return true;
            }
            if (startDate != null && endDate != null) {
                return endDate.isEqual(startDate) || endDate.isAfter(startDate);
            }
            return false;
        }
}
