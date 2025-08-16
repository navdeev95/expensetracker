package io.github.nikoir.expensetracker.service;

import io.github.nikoir.expensetracker.domain.repo.BudgetPeriodRepository;
import io.github.nikoir.expensetracker.domain.repo.BudgetRepository;
import io.github.nikoir.expensetracker.domain.repo.CategoryRepository;
import io.github.nikoir.expensetracker.domain.repo.CurrencyRepository;
import io.github.nikoir.expensetracker.dto.request.BudgetCreateDto;
import io.github.nikoir.expensetracker.exception.NotFoundException;
import io.github.nikoir.expensetracker.exception.ValidationException;
import io.github.nikoir.expensetracker.mapper.BudgetMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.nikoir.expensetracker.domain.entity.enums.BudgetPeriodType.CUSTOM;
import static io.github.nikoir.expensetracker.domain.entity.enums.BudgetPeriodType.MONTH;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class BudgetServiceUnitTest {
    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private BudgetPeriodRepository budgetPeriodRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BudgetMapper budgetMapper;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private BudgetService budgetService;

    @Test
    public void testBlankDates() {
        BudgetCreateDto budgetCreateDto = BudgetCreateDto.builder()
                .categoryId(1L)
                .periodCode(CUSTOM.getCode())
                .build();

        assertThrows(ValidationException.class, () -> budgetService.createBudget(budgetCreateDto));
    }

    @Test
    public void testIncorrectZone() {
        BudgetCreateDto budgetCreateDto = BudgetCreateDto.builder()
                .categoryId(1L)
                .periodCode(MONTH.getCode())
                .zoneId("12345")
                .build();
        assertThrows(NotFoundException.class, () -> budgetService.createBudget(budgetCreateDto));
    }

}
