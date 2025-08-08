package io.github.nikoir.expensetracker.dto.request;
import io.github.nikoir.expensetracker.dto.request.validator.ValidAmount;
import io.github.nikoir.expensetracker.dto.request.validator.ValidId;
import io.github.nikoir.expensetracker.dto.request.validator.ValidPeriodCode;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BudgetModifyDto(
        @ValidId
        Long id,

        @ValidId(name = "Id категории")
        Long categoryId,

        @ValidPeriodCode
        String periodCode,

        @ValidAmount
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
