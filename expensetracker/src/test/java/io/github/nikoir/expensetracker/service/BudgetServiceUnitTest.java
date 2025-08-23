package io.github.nikoir.expensetracker.service;

import io.github.nikoir.expensetracker.domain.entity.Budget;
import io.github.nikoir.expensetracker.domain.entity.BudgetPeriod;
import io.github.nikoir.expensetracker.domain.entity.enums.BudgetPeriodType;
import io.github.nikoir.expensetracker.domain.repo.BudgetPeriodRepository;
import io.github.nikoir.expensetracker.domain.repo.BudgetRepository;
import io.github.nikoir.expensetracker.domain.repo.CategoryRepository;
import io.github.nikoir.expensetracker.domain.repo.CurrencyRepository;
import io.github.nikoir.expensetracker.dto.request.BudgetCreateDto;
import io.github.nikoir.expensetracker.dto.request.BudgetUpdateDto;
import io.github.nikoir.expensetracker.dto.response.BudgetViewDto;
import io.github.nikoir.expensetracker.exception.AlreadyExistsException;
import io.github.nikoir.expensetracker.exception.NotFoundException;
import io.github.nikoir.expensetracker.exception.ValidationException;
import io.github.nikoir.expensetracker.mapper.BudgetMapper;
import io.github.nikoir.expensetracker.mapper.BudgetMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import static io.github.nikoir.expensetracker.domain.entity.enums.BudgetPeriodType.*;
import static io.github.nikoir.expensetracker.testdata.TestConstants.*;
import static io.github.nikoir.expensetracker.testdata.TestDataFactory.*;
import static io.github.nikoir.expensetracker.testdata.TestDataFactory.createFixedClock;
import static io.github.nikoir.expensetracker.testdata.TestDtoFactory.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
    private CurrentUserService currentUserService;

    private final BudgetMapper budgetMapper = new BudgetMapperImpl();

    private BudgetService budgetService;

    @BeforeEach
    public void setUp() {
        budgetService = new BudgetService(budgetRepository,
                budgetPeriodRepository,
                currencyRepository,
                categoryRepository,
                budgetMapper,
                currentUserService);
    }

    @Test
    public void createBudget_ShouldThrowExceptionWhenBlankDates() {
        assertThrows(ValidationException.class, () ->
                budgetService.createBudget(createCustomFoodBudgetDto(null, null)));
    }

    @Test
    public void createBudget_ShouldThrowExceptionWhenNotExistingPeriod() {
        assertThrows(NotFoundException.class, () ->
                budgetService.createBudget(createPeriodicFoodBudgetDto("UNEXISTING_PERIOD_CODE",
                        TEST_ZONE_ID)));
    }

    @Test
    public void createBudget_ShouldThrowExceptionWhenIncorrectZone() {
        assertThrows(NotFoundException.class, () ->
                budgetService.createBudget(createPeriodicFoodBudgetDto(MONTH.getCode(),
                        "INCORRECT_ZONE")));
    }

    @Test
    public void createBudget_ShouldThrowExceptionWhenOverlappingBudget() {
        when(currentUserService.getCurrentUserId())
                .thenReturn(TEST_USER_ID);

        when(currencyRepository.findByCodeIgnoreCase(CURRENCY_RUB_CODE))
                .thenAnswer(_ -> Optional.of(createRubCurrency()));

        when(categoryRepository.findByIdAndUserId(CATEGORY_FOOD_ID, TEST_USER_ID))
                .thenAnswer(_ -> Optional.of(createFoodCategory()));

        when(budgetRepository.existsByCategoryAndDates(CATEGORY_FOOD_ID,
                createBudgetStartInstant(),
                createBudgetEndInstant()))
                .thenReturn(true);

        budgetService.setClock(createFixedClock());

        assertThrows(AlreadyExistsException.class, () -> budgetService.createBudget(
                createCustomFoodBudgetDto(BUDGET_START_DATE, BUDGET_END_DATE)));

    }

    @ParameterizedTest
    @MethodSource("periodTypeProvider")
    void createBudget_ShouldCalculateDatesForAllPeriodTypes(BudgetPeriodType periodType,
                                                            LocalDate expectedStart,
                                                            LocalDate expectedEnd) {
        BudgetPeriod budgetPeriod = createBudgetPeriod(periodType);

        when(currentUserService.getCurrentUserId())
                .thenReturn(TEST_USER_ID);

        when(budgetRepository.save(any(Budget.class)))
                .thenAnswer(invocation -> invocation.<Budget>getArgument(0));

        when(currencyRepository.findByCodeIgnoreCase(CURRENCY_RUB_CODE))
                .thenAnswer(_ -> Optional.of(createRubCurrency()));

        when(categoryRepository.findByIdAndUserId(CATEGORY_FOOD_ID, TEST_USER_ID))
                .thenAnswer(_ -> Optional.of(createFoodCategory()));

        when(budgetPeriodRepository.getReferenceById(periodType.getCode()))
                .thenAnswer(_ -> budgetPeriod);

        budgetService.setClock(createFixedClock());

        BudgetCreateDto createDto = periodType == CUSTOM ?
                createCustomFoodBudgetDto(expectedStart, expectedEnd):
                createPeriodicFoodBudgetDto(periodType.getCode(), TEST_ZONE_ID);

        BudgetViewDto result = budgetService.createBudget(createDto);

        assertEquals(CATEGORY_FOOD_NAME, result.categoryName());
        assertEquals(createDto.amount(), result.amount());
        assertEquals(CURRENCY_RUB_NAME, result.currency());
        assertEquals(budgetPeriod.getName(), result.periodName());
        assertEquals(expectedStart, result.startDate().atZone(createZoneId()).toLocalDate());
        assertEquals(expectedEnd, result.endDate().atZone(createZoneId()).toLocalDate());
    }

    @Test
    void updateBudget_ShouldThrowExceptionWhenNoFound() {
        when(budgetRepository.findById(BUDGET_UNEXISTING_ID))
                .thenReturn(Optional.empty());

        BudgetUpdateDto updateDto = BudgetUpdateDto.builder()
                .Id(BUDGET_UNEXISTING_ID)
                .build();

        assertThrows(NotFoundException.class, () -> budgetService.updateBudget(updateDto));
    }

    @Test
    void updateBudget_ShouldUpdateExistingBudget() {
        when(budgetRepository.findById(BUDGET_MONTHLY_FOOD_ID))
                .thenAnswer(_ -> Optional.of(createMonthlyFoodBudget()));

        BudgetUpdateDto updateDto = BudgetUpdateDto.builder()
                .Id(BUDGET_MONTHLY_FOOD_ID)
                .amount(BigDecimal.valueOf(40000))
                .build();

        BudgetViewDto budgetViewDto = budgetService.updateBudget(updateDto);

        assertEquals(updateDto.amount(), budgetViewDto.amount());
    }

    @Test
    void deleteBudget_ShouldThrowExceptionWhenNoFound() {
        when(budgetRepository.findById(BUDGET_UNEXISTING_ID))
                .thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> budgetService.deleteBudget(BUDGET_UNEXISTING_ID));
    }

    @Test
    void deleteBudget_ShouldDeleteExistingBudget() {
        Budget monthlyFoodBudget = createMonthlyFoodBudget();

        when(budgetRepository.findById(BUDGET_MONTHLY_FOOD_ID))
                .thenAnswer(_ -> Optional.of(monthlyFoodBudget));

        budgetService.deleteBudget(BUDGET_MONTHLY_FOOD_ID);

        assertEquals(true, monthlyFoodBudget.getIsDeleted());
    }

    private static Stream<Arguments> periodTypeProvider() {
        return Stream.of(
                Arguments.of(DAY, LocalDate.of(2024, 1, 16), LocalDate.of(2024, 1, 16)),
                Arguments.of(WEEK, LocalDate.of(2024, 1, 15), LocalDate.of(2024, 1, 21)),
                Arguments.of(MONTH, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)),
                Arguments.of(YEAR, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)),
                Arguments.of(CUSTOM, LocalDate.of(2024, 7, 13), LocalDate.of(2024, 9, 14))
        );
    }

}
