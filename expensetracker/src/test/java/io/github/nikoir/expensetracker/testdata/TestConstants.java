package io.github.nikoir.expensetracker.testdata;

import java.math.BigDecimal;
import java.time.*;

public class TestConstants {
    // ID и коды
    public static final String TEST_USER_ID = "bf005a04-26b6-4a8a-9427-46ae6997962d";
    public static final Long CATEGORY_FOOD_ID = 1L; //TODO: сделать так, чтобы ID в коде и в liquibase были синхронизированы
    public static final String CATEGORY_FOOD_NAME = "Еда";
    public static final String CURRENCY_RUB_CODE = "RUB";
    public static final String CURRENCY_RUB_NAME = "Russian Ruble";
    public static final String CURRENCY_USD_CODE = "USD";
    public static final Long BUDGET_MONTHLY_FOOD_ID = 1L;
    public static final Long BUDGET_UNEXISTING_ID = 2L;

    // Значения
    public static final BigDecimal BUDGET_MONTHLY_FOOD_AMOUNT = BigDecimal.valueOf(30000);
    public static final String TEST_ZONE_ID = "UTC+7";

    // Базовые даты (только LocalDate)
    public static final LocalDate TEST_DATE = LocalDate.of(2024, 1, 16);
    public static final LocalDate BUDGET_START_DATE = LocalDate.of(2025, 1, 1);
    public static final LocalDate BUDGET_END_DATE = LocalDate.of(2025, 1, 31);
}