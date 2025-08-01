package io.github.nikoir.expensetracker.controller;

import io.github.nikoir.expensetracker.dto.CurrencyModifyDto;
import io.github.nikoir.expensetracker.dto.CurrencyViewDto;
import io.github.nikoir.expensetracker.service.CurrencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/currencies")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping
    public List<CurrencyViewDto> getAll(Authentication authentication) {
        return currencyService.getAll();
    }

    @GetMapping("/by-code/{code}")
    public CurrencyViewDto getByCode(Authentication authentication, @PathVariable String code) {
        return currencyService.getByCode(code);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CurrencyViewDto create(Authentication authentication, @Valid @RequestBody CurrencyModifyDto currencyModifyDto) {
        return currencyService.create(currencyModifyDto);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CurrencyViewDto update(@Valid @RequestBody CurrencyModifyDto currencyModifyDto) {
        return currencyService.update(currencyModifyDto);
    }

}
