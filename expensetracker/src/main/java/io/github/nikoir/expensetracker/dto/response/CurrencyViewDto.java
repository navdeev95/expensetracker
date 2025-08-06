package io.github.nikoir.expensetracker.dto.response;

public record CurrencyViewDto (
    String code,
    String name,
    String symbol
) {}
