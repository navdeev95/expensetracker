package io.github.nikoir.expensetracker.service;

import io.github.nikoir.expensetracker.domain.repo.BudgetRepository;
import io.github.nikoir.expensetracker.dto.request.BudgetSearchRequestDto;
import io.github.nikoir.expensetracker.dto.response.BudgetViewDto;
import io.github.nikoir.expensetracker.enums.BudgetSortField;
import io.github.nikoir.expensetracker.mapper.BudgetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository repository;
    private final BudgetMapper budgetMapper;
    private final CurrentUserService currentUserService;

    public Page<BudgetViewDto> getAllBudgets(BudgetSearchRequestDto filterDto) {
        Sort sort = Sort.by(Sort.Direction.fromString(filterDto.direction()), mapSortValue(filterDto.sortBy()));
        Pageable pageable = PageRequest.of(filterDto.page(), filterDto.size(), sort);
        return budgetMapper.toViewDtoPage(repository.findAllForUser(
                filterDto.isActive(),
                currentUserService.getCurrentUserId(),
                pageable));
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

}
