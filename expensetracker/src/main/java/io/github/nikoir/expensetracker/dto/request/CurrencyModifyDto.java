package io.github.nikoir.expensetracker.dto.request;

import io.github.nikoir.expensetracker.dto.request.validator.ValidCurrencyCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CurrencyModifyDto (
    @ValidCurrencyCode
    String code,

    @NotBlank(message = "Название валюты не может быть пустым")
    @Size(max = 50, message = "Название валюты не должно превышать 50 символов")
    String name,

    @NotBlank(message = "Символ валюты не может быть пустым")
    @Size(max = 5, message = "Символ валюты не должен превышать 5 символов")
    String symbol
) {}
