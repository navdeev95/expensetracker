package io.github.nikoir.expensetracker.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
public enum BudgetSortField {
    ID("id"),
    AMOUNT("amount"),
    CATEGORY("category"),
    PERIOD("period"),
    CURRENCY("currency"),
    START_DATE("startDate"),
    END_DATE("endDate");

    private final String fieldName;

    public static Optional<BudgetSortField> getValueByName(String fieldName) {
        return Arrays.stream(values())
                .filter(v -> StringUtils.equals(v.getFieldName(), fieldName))
                .findFirst();
    }
}
