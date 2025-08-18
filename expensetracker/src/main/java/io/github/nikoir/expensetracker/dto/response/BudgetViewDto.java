package io.github.nikoir.expensetracker.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public record BudgetViewDto (
        Long id,
        String categoryName,
        String periodName,
        BigDecimal amount,
        String currency,
        Instant startDate,
        Instant endDate) { }
