package io.github.nikoir.expensetracker.domain.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
public enum BudgetPeriodType {
    DAY("DAY", "День"),
    WEEK("WEEK", "Неделя"),
    MONTH("MONTH", "Месяц"),
    YEAR("YEAR", "Год"),
    CUSTOM("CUSTOM", "Произвольный");

    private final String code;
    private final String name;

    public static Optional<BudgetPeriodType> getByCodeIgnoreCase(String code) {
        return Arrays.stream(values())
                .filter(v -> StringUtils.equalsIgnoreCase(v.code, code))
                .findFirst();
    }
}
