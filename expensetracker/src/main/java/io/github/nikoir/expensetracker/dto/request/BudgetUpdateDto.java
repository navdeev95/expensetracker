package io.github.nikoir.expensetracker.dto.request;

import io.github.nikoir.expensetracker.dto.request.validator.ValidAmount;
import io.github.nikoir.expensetracker.dto.request.validator.ValidId;
import io.github.nikoir.expensetracker.dto.request.validator.ValidPeriodCode;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record BudgetUpdateDto(
        @ValidId(name="Id бюджета")
        Long Id,

        @ValidId(name = "Id категории")
        Long categoryId,

        @ValidPeriodCode
        String periodCode,

        @ValidAmount
        BigDecimal amount,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate startDate,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate endDate,

        @NotBlank
        String zoneId
) {
}
