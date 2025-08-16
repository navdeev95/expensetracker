package io.github.nikoir.expensetracker.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EntityType {
    CURRENCY("Currency"),
    BUDGET_PERIOD("BudgetPeriod"),
    CATEGORY("Category");
    private final String displayName;

    @Override
    public String toString() {
        return this.displayName;
    }
}
