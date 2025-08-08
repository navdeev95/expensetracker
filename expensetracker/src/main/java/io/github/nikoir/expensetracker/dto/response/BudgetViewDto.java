package io.github.nikoir.expensetracker.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BudgetViewDto (
        Long id,
        String categoryName,
        String periodName,
        BigDecimal amount,
        String currency,
        LocalDate startDate,
        LocalDate endDate) { }
