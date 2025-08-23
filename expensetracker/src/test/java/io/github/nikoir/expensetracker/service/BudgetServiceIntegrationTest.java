package io.github.nikoir.expensetracker.service;
import io.github.nikoir.expensetracker.domain.entity.Budget;
import io.github.nikoir.expensetracker.domain.entity.enums.BudgetPeriodType;
import io.github.nikoir.expensetracker.domain.repo.BudgetRepository;
import io.github.nikoir.expensetracker.dto.request.BudgetCreateDto;
import io.github.nikoir.expensetracker.dto.request.BudgetSearchRequestDto;
import io.github.nikoir.expensetracker.dto.request.BudgetUpdateDto;
import io.github.nikoir.expensetracker.dto.response.BudgetViewDto;
import io.github.nikoir.expensetracker.enums.BudgetSortField;
import io.github.nikoir.expensetracker.exception.AlreadyExistsException;
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
import java.time.*;

import static io.github.nikoir.expensetracker.domain.entity.enums.BudgetPeriodType.CUSTOM;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

//TODO: добавить проверку корректности записи в БД
@DataJpaTest
@Import({BudgetService.class, BudgetMapperImpl.class})
public class BudgetServiceIntegrationTest {
    @MockitoBean
    private CurrentUserService currentUserService;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetService budgetService;

    private final ZoneId zoneId = ZoneId.of("UTC+7");

    private ZonedDateTime zonedNow = ZonedDateTime.of(
            2024, 1, 16, 12, 0, 0, 0,
            zoneId);

    private Clock fixedClock = Clock.fixed(zonedNow.toInstant(), zoneId);

    @BeforeEach
    void setUp() {
        when(currentUserService.getCurrentUserId()).thenReturn("bf005a04-26b6-4a8a-9427-46ae6997962d");
        budgetService.setClock(fixedClock);
    }

    @Test
    public void testSaveDay() {
        testBudget(BudgetPeriodType.DAY,
                LocalDate.of(2024, 1, 16),
                LocalDate.of(2024, 1, 16));
    }

    @Test
    public void testSaveWeek() {
        testBudget(BudgetPeriodType.WEEK,
                LocalDate.of(2024, 1, 15),
                LocalDate.of(2024, 1, 21));
    }

    @Test
    public void testSaveMonth() {
        testBudget(BudgetPeriodType.MONTH,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 31));
    }

    @Test
    public void testSaveYear() {
        testBudget(BudgetPeriodType.YEAR,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31));
    }

    @Test
    public void testSaveCustom() {
        testBudget(BudgetPeriodType.CUSTOM,
                LocalDate.of(2024, 7, 13),
                LocalDate.of(2024, 9, 14));
    }

    @Test
    public void testSaveWithOverlappingBudget() {
        zonedNow = ZonedDateTime.of(
                2025, 1, 1, 12, 0, 0, 0,
                zoneId);

        fixedClock = Clock.fixed(zonedNow.toInstant(), zoneId);

        budgetService.setClock(fixedClock);

        assertThrows(AlreadyExistsException.class, () -> saveBudget(BudgetPeriodType.MONTH));
    }

    @Test
    public void testGetAllCorrect() {
        BudgetSearchRequestDto searchRequestDto = BudgetSearchRequestDto.builder()
                .page(0)
                .size(10)
                .sortBy(BudgetSortField.ID.getFieldName())
                .direction("ASC")
                .currencyCode("RUB")
                .zoneId("UTC+7")
                .build();

        PagedModel<BudgetViewDto> searchResult = budgetService.getAllBudgets(searchRequestDto);
        assertNotNull(searchResult);
        assertNotNull(searchResult.getMetadata());
        assertNotNull(searchResult.getContent());

        assertEquals(1, searchResult.getContent().size());
    }

    @Test
    public void testGetAllIncorrect() {
        BudgetSearchRequestDto searchRequestDto = BudgetSearchRequestDto.builder()
                .page(0)
                .size(10)
                .sortBy(BudgetSortField.ID.getFieldName())
                .direction("ASC")
                .currencyCode("USD")
                .zoneId("UTC+7")
                .build();

        PagedModel<BudgetViewDto> searchResult = budgetService.getAllBudgets(searchRequestDto);
        assertNotNull(searchResult);
        assertNotNull(searchResult.getMetadata());
        assertNotNull(searchResult.getContent());

        assertEquals(0, searchResult.getContent().size());
    }

    @Test
    public void testUpdate() {
        BudgetUpdateDto budgetUpdateDto = BudgetUpdateDto.builder()
                .Id(1L)
                .amount(BigDecimal.valueOf(40000))
                .build();

        budgetService.updateBudget(budgetUpdateDto);

        Budget budget = getBudgetById(budgetUpdateDto.Id());

        assertEquals(budgetUpdateDto.amount(), budget.getAmount());
    }

    @Test
    public void testDelete() {
        budgetService.deleteBudget(1L);

        Budget budget = getBudgetById(1L);

        assertEquals(true, budget.getIsDeleted());
    }

    private void testBudget(BudgetPeriodType periodType,
                            LocalDate expectedStartDate,
                            LocalDate expectedEndDate) {

        BudgetViewDto budgetViewDto = periodType == CUSTOM ? saveBudget(periodType, expectedStartDate, expectedEndDate):
                saveBudget(periodType);

        assertNotNull(budgetViewDto.id());
        assertEquals("Еда", budgetViewDto.categoryName());
        assertEquals(periodType.getName(), budgetViewDto.periodName());
        assertEquals(BigDecimal.valueOf(30000), budgetViewDto.amount());
        assertEquals("Russian Ruble", budgetViewDto.currency());
        assertEquals(LocalDateTime.of(expectedStartDate, LocalTime.MIDNIGHT),
                budgetViewDto.startDate().atZone(zoneId).toLocalDateTime());

        assertEquals(LocalDateTime.of(expectedEndDate, LocalTime.MAX),
                budgetViewDto.endDate().atZone(zoneId).toLocalDateTime());
    }

    private BudgetViewDto saveBudget(BudgetPeriodType periodType) {
        return saveBudget(periodType, null, null);
    }

    private BudgetViewDto saveBudget(BudgetPeriodType periodType,
                            LocalDate startDate,
                            LocalDate endDate) {
        BudgetCreateDto budgetCreateDto = BudgetCreateDto.builder()
                .categoryId(1L)
                .periodCode(periodType.getCode())
                .currencyCode("RUB")
                .amount(BigDecimal.valueOf(30000))
                .zoneId(zoneId.getId())
                .startDate(startDate)
                .endDate(endDate)
                .build();

        return budgetService.createBudget(budgetCreateDto);
    }

    private Budget getBudgetById(Long id) {
        return budgetRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(EntityType.BUDGET, id));
    }
}
