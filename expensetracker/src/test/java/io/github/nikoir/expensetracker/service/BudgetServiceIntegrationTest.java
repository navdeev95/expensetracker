package io.github.nikoir.expensetracker.service;
import io.github.nikoir.expensetracker.domain.entity.Budget;
import io.github.nikoir.expensetracker.domain.repo.BudgetRepository;
import io.github.nikoir.expensetracker.dto.request.BudgetCreateDto;
import io.github.nikoir.expensetracker.dto.request.BudgetSearchRequestDto;
import io.github.nikoir.expensetracker.dto.request.BudgetUpdateDto;
import io.github.nikoir.expensetracker.dto.response.BudgetViewDto;
import io.github.nikoir.expensetracker.exception.NotFoundException;
import io.github.nikoir.expensetracker.mapper.BudgetMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.PagedModel;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDate;

import static io.github.nikoir.expensetracker.service.EntityType.BUDGET;
import static io.github.nikoir.expensetracker.testdata.TestConstants.*;
import static io.github.nikoir.expensetracker.testdata.TestDataFactory.createFixedClock;
import static io.github.nikoir.expensetracker.testdata.TestDataFactory.createZoneId;
import static io.github.nikoir.expensetracker.testdata.TestDtoFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
@Import({BudgetService.class, BudgetMapperImpl.class})
public class BudgetServiceIntegrationTest {
    @MockitoBean
    private CurrentUserService currentUserService;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetService budgetService;

    @BeforeEach
    void setUp() {
        when(currentUserService.getCurrentUserId()).thenReturn(TEST_USER_ID);
        budgetService.setClock(createFixedClock());
    }

    @Test
    public void getBudgets_ShouldReturnBudget() {
        BudgetSearchRequestDto searchRequestDto = createBudgetSearchRequestDto(CURRENCY_RUB_CODE);

        PagedModel<BudgetViewDto> searchResult = budgetService.getAllBudgets(searchRequestDto);
        assertNotNull(searchResult);
        assertNotNull(searchResult.getMetadata());
        assertNotNull(searchResult.getContent());

        assertEquals(1, searchResult.getContent().size());
    }

    @Test
    public void getBudgets_ShouldReturnEmptyResult() {
        BudgetSearchRequestDto searchRequestDto = createBudgetSearchRequestDto(CURRENCY_USD_CODE);

        PagedModel<BudgetViewDto> searchResult = budgetService.getAllBudgets(searchRequestDto);
        assertNotNull(searchResult);
        assertNotNull(searchResult.getMetadata());
        assertNotNull(searchResult.getContent());

        assertEquals(0, searchResult.getContent().size());
    }

    @Test
    public void createBudget_ShouldCreateNewBudget() {
        BudgetCreateDto createDto = createCustomFoodBudgetDto(LocalDate.of(2024, 7, 13),
                LocalDate.of(2024, 9, 14));

        BudgetViewDto viewDto = budgetService.createBudget(createDto);

        Budget createdBudget = getBudgetById(viewDto.id());

        assertNotNull(createdBudget);
        assertEquals(createDto.categoryId(), createdBudget.getCategory().getId());
        assertEquals(createDto.periodCode(), createdBudget.getBudgetPeriod().getCode());
        assertEquals(createDto.amount(), createdBudget.getAmount());
        assertEquals(createDto.currencyCode(), createdBudget.getCurrency().getCode());
        assertEquals(createDto.startDate(), createdBudget.getStartDate().atZone(createZoneId()).toLocalDate());
        assertEquals(createDto.endDate(), createdBudget.getEndDate().atZone(createZoneId()).toLocalDate());
    }

    @Test
    public void updateBudget_ShouldUpdateExistingBudget() {
        BudgetUpdateDto budgetUpdateDto = createBudgetUpdateDto(BigDecimal.valueOf(40000));

        budgetService.updateBudget(budgetUpdateDto);

        Budget budget = getBudgetById(BUDGET_MONTHLY_FOOD_ID);

        assertEquals(budgetUpdateDto.amount(), budget.getAmount());
    }

    @Test
    public void deleteBudget_ShouldDeleteExistingBudget() {
        budgetService.deleteBudget(BUDGET_MONTHLY_FOOD_ID);

        Budget budget = getBudgetById(BUDGET_MONTHLY_FOOD_ID);

        assertEquals(true, budget.getIsDeleted());
    }


    private Budget getBudgetById(Long budgetId) {
        return budgetRepository
                .findById(budgetId)
                .orElseThrow(() -> new NotFoundException(BUDGET, budgetId));
    }
}
