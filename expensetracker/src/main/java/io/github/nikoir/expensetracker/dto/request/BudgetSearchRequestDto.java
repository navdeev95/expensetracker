package io.github.nikoir.expensetracker.dto.request;

import io.github.nikoir.expensetracker.dto.request.validator.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record BudgetSearchRequestDto(
        /*
         * Пагинация и сортировка
         */
        @RequestParam(defaultValue = "0")
        @Min(value = 0, message = "Номер страницы не может быть отрицательным")
        int page,

        @RequestParam(defaultValue = "10")
        @Min(value = 1, message = "Размер странцы должен быть от 1 до 100")
        @Max(value = 100, message = "Размер странцы должен быть от 1 до 100")
        int size,

        @RequestParam(required = false)
        @ValidBudgetSortField
        String sortBy,

        @RequestParam(defaultValue = "ASC")
        @Pattern(regexp = "ASC|DESC", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Направление сортировки: ASC или DESC")
        String direction,

        /*
         * Фильтры
         */
        @RequestParam(required = false)
        Boolean isDeleted,

        @RequestParam(required = false)
        @ValidId(name = "Id категории", required = false)
        Long categoryId,

        @RequestParam(required = false)
        @ValidCurrencyCode(required = false)
        String currencyCode,

        @RequestParam(required = false)
        @ValidPeriodCode(required = false)
        String periodCode,

        @RequestParam(required = false)
        @ValidAmount(required = false)
        BigDecimal minAmount,

        @RequestParam(required = false)
        @ValidAmount(required = false)
        BigDecimal maxAmount,

        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate periodFrom,

        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate periodTo

) {
}
