package io.github.nikoir.expensetracker.service;

import io.github.nikoir.expensetracker.domain.entity.Budget;
import io.github.nikoir.expensetracker.domain.entity.Category;
import io.github.nikoir.expensetracker.domain.entity.Currency;
import io.github.nikoir.expensetracker.domain.entity.enums.BudgetPeriodType;
import io.github.nikoir.expensetracker.domain.entity.specification.BudgetSpecification;
import io.github.nikoir.expensetracker.domain.repo.BudgetPeriodRepository;
import io.github.nikoir.expensetracker.domain.repo.BudgetRepository;
import io.github.nikoir.expensetracker.domain.repo.CategoryRepository;
import io.github.nikoir.expensetracker.domain.repo.CurrencyRepository;
import io.github.nikoir.expensetracker.dto.request.BudgetCreateDto;
import io.github.nikoir.expensetracker.dto.request.BudgetSearchRequestDto;
import io.github.nikoir.expensetracker.dto.response.BudgetViewDto;
import io.github.nikoir.expensetracker.enums.BudgetSortField;
import io.github.nikoir.expensetracker.exception.AlreadyExistsException;
import io.github.nikoir.expensetracker.exception.NotFoundException;
import io.github.nikoir.expensetracker.exception.ValidationException;
import io.github.nikoir.expensetracker.mapper.BudgetMapper;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;
import java.util.Optional;

import static io.github.nikoir.expensetracker.domain.entity.enums.BudgetPeriodType.CUSTOM;
import static io.github.nikoir.expensetracker.service.EntityType.*;

@Service
@RequiredArgsConstructor
public class BudgetService {
    @Setter
    private Clock clock = Clock.systemDefaultZone();
    private final BudgetRepository budgetRepository;
    private final BudgetPeriodRepository budgetPeriodRepository;
    private final CurrencyRepository currencyRepository;
    private final CategoryRepository categoryRepository;
    private final BudgetMapper budgetMapper;
    private final CurrentUserService currentUserService;

    private Instant getCurrentInstant() {
        return Instant.now(clock);
    }


    public PagedModel<BudgetViewDto> getAllBudgets(BudgetSearchRequestDto filterDto) {
        Sort sort = Sort.by(Sort.Direction.fromString(filterDto.direction()), mapSortValue(filterDto.sortBy()));
        Pageable pageable = PageRequest.of(filterDto.page(), filterDto.size(), sort);

        Specification<Budget> spec = Specification.allOf(
                BudgetSpecification.isDeleted(filterDto.isDeleted()),
                BudgetSpecification.hasCategoryId(filterDto.categoryId()),
                BudgetSpecification.hasCurrencyCode(filterDto.currencyCode()),
                BudgetSpecification.hasPeriodCode(filterDto.periodCode()),
                BudgetSpecification.hasAmountBetween(filterDto.minAmount(), filterDto.maxAmount()),
                BudgetSpecification.hasDateBetween(filterDto.periodFrom(), filterDto.periodTo()),
                BudgetSpecification.hasUserId(currentUserService.getCurrentUserId()));

        return budgetMapper.toViewDtoPage(budgetRepository.findAll(spec, pageable));
    }

    public BudgetViewDto createBudget(BudgetCreateDto budgetCreateDto) {
        checkCreateEntity(budgetCreateDto);

        ZoneId zoneId;
        try {
            zoneId = ZoneId.of(budgetCreateDto.zoneId());
        } catch (DateTimeException _) {
            throw new NotFoundException(String.format("Не найден часовой пояс: %s", budgetCreateDto.zoneId()));
        }
        Instant now = getCurrentInstant();

        BudgetPeriodType periodType = BudgetPeriodType
                .getByCodeIgnoreCase(budgetCreateDto.periodCode())
                .orElseThrow(() -> new NotFoundException(BUDGET_PERIOD, budgetCreateDto.periodCode()));

        Currency currency = currencyRepository
                .findByCodeIgnoreCase(budgetCreateDto.currencyCode())
                .orElseThrow(() -> new NotFoundException(CURRENCY, budgetCreateDto.currencyCode()));

        Category category = categoryRepository
                .findByIdAndUserId(budgetCreateDto.categoryId(), currentUserService.getCurrentUserId())
                .orElseThrow(() -> new NotFoundException(CATEGORY, budgetCreateDto.categoryId()));

        Instant startDate = getStartDate(budgetCreateDto, zoneId, now, periodType);
        Instant endDate = getEndDate(budgetCreateDto, zoneId, now, periodType);

        if (budgetRepository.existsByCategoryAndDates(category.getId(), startDate, endDate)) {
            throw new AlreadyExistsException(String.format("Найден бюджет с пересекающимся периодом в категории '%s'", category.getName()));
        }

        Budget newBudget = Budget.builder()
                .category(category)
                .budgetPeriod(budgetPeriodRepository.getReferenceById(budgetCreateDto.periodCode()))
                .amount(budgetCreateDto.amount())
                .currency(currency)
                .startDate(startDate)
                .endDate(endDate)
                .isActive(false) //TODO: переименовать в isDeleted
                .build();

        return budgetMapper.toViewDto(budgetRepository.save(newBudget));
    }

    private Instant getStartDate(BudgetCreateDto budgetCreateDto,
                                 ZoneId zoneId,
                                 Instant now,
                                 BudgetPeriodType budgetPeriodType) {
        ZonedDateTime midnightToday = now.atZone(zoneId)
                .with(LocalTime.MIDNIGHT);

        return switch (budgetPeriodType) {
            case DAY -> midnightToday.toInstant();
            case WEEK -> {
                int dayOfWeek = midnightToday.getDayOfWeek().getValue();
                yield midnightToday.plusDays(1 - dayOfWeek).toInstant();
            }
            case MONTH -> midnightToday
                    .withDayOfMonth(1)
                    .toInstant();
            case YEAR -> midnightToday
                    .withMonth(1)
                    .withDayOfMonth(1)
                    .toInstant();
            case CUSTOM -> ZonedDateTime
                    .of(budgetCreateDto.startDate(), LocalTime.MIDNIGHT, zoneId)
                    .toInstant();
        };
    }

    private Instant getEndDate(BudgetCreateDto budgetCreateDto,
                               ZoneId zoneId,
                               Instant now,
                               BudgetPeriodType budgetPeriodType) {
        ZonedDateTime endOfToday = now.atZone(zoneId)
                .with(LocalTime.MAX);

        return switch (budgetPeriodType) {
            case DAY -> endOfToday.toInstant();
            case WEEK -> endOfToday
                    .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                    .with(LocalTime.MAX)
                    .toInstant();
            case MONTH -> endOfToday
                    .with(TemporalAdjusters.lastDayOfMonth())
                    .with(LocalTime.MAX)
                    .toInstant();

            case YEAR -> endOfToday
                    .withMonth(12)
                    .withDayOfMonth(31)
                    .toInstant();

            case CUSTOM -> ZonedDateTime
                    .of(budgetCreateDto.endDate(), LocalTime.MAX, zoneId)
                    .toInstant();
        };
    }

    private String mapSortValue(String sortBy) {
        Optional<BudgetSortField> enumValue = BudgetSortField.getValueByName(sortBy);
        if (enumValue.isPresent()) {
            return switch (enumValue.get()) {
                case CATEGORY -> "category.name";
                case PERIOD -> "budgetPeriod.name";
                case CURRENCY -> "currency.code";
                default -> enumValue.get().getFieldName();
            };
        }
        return BudgetSortField.ID.getFieldName();
    }

    private void checkCreateEntity(BudgetCreateDto budgetCreateDto) {
        if (Objects.equals(budgetCreateDto.periodCode(), CUSTOM.getCode())) {
            if (budgetCreateDto.startDate() == null || budgetCreateDto.endDate() == null) {
                throw new ValidationException("При выборе произвольного типа периода необходимо заполнить начальную и конечную даты");
            }
            if (budgetCreateDto.startDate().isAfter(budgetCreateDto.endDate())) {
                throw new ValidationException("Конечная дата должна быть больше или равна начальной дате");
            }
        }
    }

}
