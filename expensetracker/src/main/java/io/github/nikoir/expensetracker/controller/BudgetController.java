package io.github.nikoir.expensetracker.controller;

import io.github.nikoir.expensetracker.dto.request.BudgetSearchRequestDto;
import io.github.nikoir.expensetracker.dto.response.BudgetViewDto;
import io.github.nikoir.expensetracker.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
