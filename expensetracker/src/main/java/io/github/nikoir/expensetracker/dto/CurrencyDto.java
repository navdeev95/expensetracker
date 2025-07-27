package io.github.nikoir.expensetracker.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class CurrencyDto {
    private String code;
    private String name;
    private String symbol;
}
