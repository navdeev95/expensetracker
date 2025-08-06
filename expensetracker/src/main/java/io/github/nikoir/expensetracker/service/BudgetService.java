package io.github.nikoir.expensetracker.service;

import io.github.nikoir.expensetracker.domain.entity.Budget;
import io.github.nikoir.expensetracker.domain.repo.BudgetRepository;
import io.github.nikoir.expensetracker.dto.request.BudgetFilterDto;
import io.github.nikoir.expensetracker.dto.response.BudgetViewDto;
import io.github.nikoir.expensetracker.mapper.BudgetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository repository;
    private final BudgetMapper budgetMapper;

    public Page<BudgetViewDto> getAllBudgets(BudgetFilterDto filterDto) {
        Sort sort = Sort.by(Sort.Direction.fromString(filterDto.direction()), mapSortValue(filterDto.sortBy()));
        Pageable pageable = PageRequest.of(filterDto.page(), filterDto.size(), sort);
        return budgetMapper.toViewDtoPage(repository.findAll(filterDto.isActive(), pageable));
    }

    private String mapSortValue(String sortBy) {
        return switch (sortBy) {
            case "category" -> "category.name";
            case "periodName" -> "budgetPeriod.name";
            default -> sortBy;
        };
    }

}
