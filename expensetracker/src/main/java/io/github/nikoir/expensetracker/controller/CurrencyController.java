package io.github.nikoir.expensetracker.controller;

import io.github.nikoir.expensetracker.dto.CurrencyModifyDto;
import io.github.nikoir.expensetracker.dto.CurrencyViewDto;
import io.github.nikoir.expensetracker.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/currencies")
@RequiredArgsConstructor
@SecurityRequirement(name = "security_auth")
@Tag(name = "Currency Controller", description = "Операции со справочниками валют")
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping
    @Operation(summary = "get all", description = "Получить список всех валют")
    public List<CurrencyViewDto> getAll() {
        return currencyService.getAll();
    }

    @GetMapping("/by-code/{code}")
    @Operation(summary = "get by code", description = "Получить валюту по коду")
    public CurrencyViewDto getByCode(@PathVariable String code) {
        return currencyService.getByCode(code);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "create", description = "Создать новую валюту (доступно только для пользователей с ролью админ)")
    public CurrencyViewDto create(@Valid @RequestBody CurrencyModifyDto currencyModifyDto) {
        return currencyService.create(currencyModifyDto);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "update", description = "Обновить информацию о существующей валюте (доступно только для пользователей с ролью админ)")
    public CurrencyViewDto update(@Valid @RequestBody CurrencyModifyDto currencyModifyDto) {
        currencyModifyDto.toString();
        return currencyService.update(currencyModifyDto);
    }
}
