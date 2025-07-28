package io.github.nikoir.expensetracker.controller;

import io.github.nikoir.expensetracker.dto.CurrencyDto;
import io.github.nikoir.expensetracker.service.CurrencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/currencies")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping
    public List<CurrencyDto> getAll() {
        return currencyService.getAll();
    }

    @GetMapping("/by-code/{code}")
    public CurrencyDto getByCode(@PathVariable String code) {
        return currencyService.getByCode(code);
    }

    @PostMapping
    public CurrencyDto create(@Valid @RequestBody CurrencyDto currencyDto) {
        return currencyService.create(currencyDto);
    }

    @PutMapping
    public CurrencyDto update(@Valid @RequestBody CurrencyDto currencyDto) {
        return currencyService.update(currencyDto);
    }

}
