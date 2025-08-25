package io.github.nikoir.expensetracker.domain;
import io.github.nikoir.expensetracker.domain.repo.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.*;

import static io.github.nikoir.expensetracker.testdata.TestConstants.CATEGORY_FOOD_ID;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class BudgetRepositoryTest {
    @Autowired
    private BudgetRepository budgetRepository;

    @Test
    public void checkBudgetExists_ShouldReturnTrue() {
        Instant fromDate = LocalDateTime.of(LocalDate.of(2024, 12, 20), LocalTime.MIDNIGHT)
                .atZone(ZoneOffset.UTC)
                .toInstant();
        Instant toDate = LocalDateTime.of(LocalDate.of(2025, 2, 20), LocalTime.MAX)
                .atZone(ZoneOffset.UTC)
                .toInstant();
        assertTrue(budgetRepository.existsByCategoryAndDates(CATEGORY_FOOD_ID, fromDate, toDate));
    }

    @Test
    public void checkBudgetExists_ShouldReturnFalse() {
        Instant fromDate = LocalDateTime.of(LocalDate.of(2025, 2, 1), LocalTime.MIDNIGHT)
                .atZone(ZoneOffset.UTC)
                .toInstant();
        Instant toDate = LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.MAX)
                .atZone(ZoneOffset.UTC)
                .toInstant();
        assertFalse(budgetRepository.existsByCategoryAndDates(CATEGORY_FOOD_ID, fromDate, toDate));
    }

}
