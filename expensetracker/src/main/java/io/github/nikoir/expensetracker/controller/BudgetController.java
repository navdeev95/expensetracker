package io.github.nikoir.expensetracker.controller;

import io.github.nikoir.expensetracker.dto.request.BudgetCreateDto;
import io.github.nikoir.expensetracker.dto.request.BudgetSearchRequestDto;
import io.github.nikoir.expensetracker.dto.request.BudgetUpdateDto;
import io.github.nikoir.expensetracker.dto.response.BudgetViewDto;
import io.github.nikoir.expensetracker.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/budgets")
@RequiredArgsConstructor
@SecurityRequirement(name = "security_auth")
@Tag(name = "Budget Controller", description = "Операции с бюджетами")
public class BudgetController {
    private final BudgetService budgetService;

    @GetMapping
    @Operation(summary = "get all", description = "Получить список бюджетов текущего пользователя с пагинацией")
    public PagedModel<BudgetViewDto> getAll(@Valid BudgetSearchRequestDto request) {
        return budgetService.getAllBudgets(request);
    }

    @PostMapping
    @Operation(summary = "create", description = "Создать новый бюджет для текущего пользователя")
    public BudgetViewDto create(@Valid BudgetCreateDto budgetCreateDto) {
        return budgetService.createBudget(budgetCreateDto);
    }

    @PutMapping
    @Operation(summary = "update", description = "Обновление бюджета для текущего пользователя")
    public BudgetViewDto update(@Valid BudgetUpdateDto budgetUpdateDto) {
        return budgetService.updateBudget(budgetUpdateDto);
    }
}
