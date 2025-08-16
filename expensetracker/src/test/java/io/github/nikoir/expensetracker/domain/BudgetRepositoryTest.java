package io.github.nikoir.expensetracker.domain;
import io.github.nikoir.expensetracker.domain.repo.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class BudgetRepositoryTest {
    @Autowired
    private BudgetRepository budgetRepository;

    @Test
    public void testBudgetExistsByCategoryAndDates() {
        Instant fromDate = LocalDateTime.of(LocalDate.of(2024, 12, 20), LocalTime.MIDNIGHT)
                .atZone(ZoneOffset.UTC)
                .toInstant();
        Instant toDate = LocalDateTime.of(LocalDate.of(2025, 2, 20), LocalTime.MAX)
                .atZone(ZoneOffset.UTC)
                .toInstant();
        assertTrue(budgetRepository.existsByCategoryAndDates(1L, fromDate, toDate));
    }

    @Test
    public void testBudgetNotExists() {
        Instant fromDate = LocalDateTime.of(LocalDate.of(2025, 2, 1), LocalTime.MIDNIGHT)
                .atZone(ZoneOffset.UTC)
                .plusNanos(1) //если разница одна наносекунда, то даты считаются равными
                .toInstant();
        Instant toDate = LocalDateTime.of(LocalDate.of(2025, 2, 28), LocalTime.MAX)
                .atZone(ZoneOffset.UTC)
                .toInstant();
        assertFalse(budgetRepository.existsByCategoryAndDates(1L, fromDate, toDate));
    }

}
