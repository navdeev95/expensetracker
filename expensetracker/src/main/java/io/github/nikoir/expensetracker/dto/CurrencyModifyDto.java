package io.github.nikoir.expensetracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class CurrencyModifyDto {
    @NotBlank(message = "Код валюты не может быть пустым")
    @Size(min = 3, max = 3, message = "Код валюты должен состоять из 3 символов")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Код валюты должен быть в формате ISO 4217 (например, USD, EUR)")
    private String code;

    @NotBlank(message = "Название валюты не может быть пустым")
    @Size(max = 50, message = "Название валюты не должно превышать 50 символов")
    private String name;

    @NotBlank(message = "Символ валюты не может быть пустым")
    @Size(max = 5, message = "Символ валюты не должен превышать 5 символов")
    private String symbol;
}
