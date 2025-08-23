package io.github.nikoir.expensetracker.testdata;

import io.github.nikoir.expensetracker.domain.entity.*;
import io.github.nikoir.expensetracker.domain.entity.enums.BudgetPeriodType;

import java.time.*;

import static io.github.nikoir.expensetracker.domain.entity.enums.BudgetPeriodType.MONTH;
import static io.github.nikoir.expensetracker.testdata.TestConstants.*;

public class TestDataFactory {

    public static ZoneId createZoneId() {
        return ZoneId.of(TestConstants.TEST_ZONE_ID);
    }

    public static ZonedDateTime createZonedNow() {
        return ZonedDateTime.of(
                TestConstants.TEST_DATE,
                LocalTime.of(12, 0, 0, 0),
                createZoneId()
        );
    }

    public static Clock createFixedClock() {
        return Clock.fixed(createZonedNow().toInstant(), createZoneId());
    }

    public static Instant createBudgetStartInstant() {
        return TestConstants.BUDGET_START_DATE
                .atTime(LocalTime.MIDNIGHT)
                .atZone(createZoneId())
                .toInstant();
    }

    public static Instant createBudgetEndInstant() {
        return TestConstants.BUDGET_END_DATE
                .atTime(LocalTime.MAX)
                .atZone(createZoneId())
                .toInstant();
    }

    public static User createUser() {
        return new User(TEST_USER_ID);
    }

    public static BudgetPeriod createBudgetPeriod(BudgetPeriodType budgetPeriodType) {
        return BudgetPeriod.builder()
                .code(budgetPeriodType.getCode())
                .name(budgetPeriodType.getName())
                .build();
    }

    public static Category createFoodCategory() {
        return Category.builder()
                .id(CATEGORY_FOOD_ID)
                .name(CATEGORY_FOOD_NAME)
                .user(createUser())
                .build();
    }

    public static Currency createRubCurrency() {
        return Currency.builder()
                .code(CURRENCY_RUB_CODE)
                .name(CURRENCY_RUB_NAME)
                .build();
    }

    public static Budget createMonthlyFoodBudget() {
        return Budget.builder()
                .id(BUDGET_MONTHLY_FOOD_ID)
                .category(createFoodCategory())
                .budgetPeriod(createBudgetPeriod(MONTH))
                .amount(BUDGET_MONTHLY_FOOD_AMOUNT)
                .currency(createRubCurrency())
                .startDate(createBudgetStartInstant())
                .endDate(createBudgetEndInstant())
                .isDeleted(false)
                .build();
    }
}
