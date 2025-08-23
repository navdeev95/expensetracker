package io.github.nikoir.expensetracker.testdata;

import io.github.nikoir.expensetracker.dto.request.BudgetCreateDto;
import java.time.LocalDate;
import static io.github.nikoir.expensetracker.domain.entity.enums.BudgetPeriodType.CUSTOM;
import static io.github.nikoir.expensetracker.testdata.TestConstants.*;

public class TestDtoFactory {
    public static BudgetCreateDto createPeriodicFoodBudgetDto(String periodCode, String zoneId) {
        return BudgetCreateDto.builder()
                .categoryId(BUDGET_MONTHLY_FOOD_ID)
                .periodCode(periodCode)
                .currencyCode(CURRENCY_RUB_CODE)
                .amount(BUDGET_MONTHLY_FOOD_AMOUNT)
                .zoneId(zoneId)
                .build();
    }
    
    public static BudgetCreateDto createCustomFoodBudgetDto(LocalDate startDate, LocalDate endDate) {
        return BudgetCreateDto.builder()
                .categoryId(BUDGET_MONTHLY_FOOD_ID)
                .periodCode(CUSTOM.getCode())
                .currencyCode(CURRENCY_RUB_CODE)
                .amount(BUDGET_MONTHLY_FOOD_AMOUNT)
                .zoneId(TEST_ZONE_ID)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
