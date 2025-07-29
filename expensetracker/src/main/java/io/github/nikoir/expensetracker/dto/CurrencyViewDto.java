package io.github.nikoir.expensetracker.dto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@Setter
public class CurrencyViewDto {
    private String code;
    private String name;
    private String symbol;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
