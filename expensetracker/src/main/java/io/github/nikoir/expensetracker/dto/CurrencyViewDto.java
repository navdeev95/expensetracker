package io.github.nikoir.expensetracker.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class CurrencyViewDto {
    private String code;
    private String name;
    private String symbol;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
