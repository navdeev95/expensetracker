package io.github.nikoir.expensetracker.dto.request;

import io.github.nikoir.expensetracker.controller.validator.ValidSortField;
import io.github.nikoir.expensetracker.enums.BudgetSortField;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.bind.annotation.RequestParam;

//TODO: сделать фильтр с возможностью выбора параметров для поиска
public record BudgetFilterDto(
        @RequestParam(defaultValue = "0")
        @Min(value = 0, message = "Номер страницы не может быть отрицательным")
        int page,

        @RequestParam(defaultValue = "10")
        @Min(value = 1, message = "Размер странцы должен быть от 1 до 100")
        @Max(value = 100, message = "Размер странцы должен быть от 1 до 100")
        int size,

        @RequestParam
        @ValidSortField
        String sortBy,

        @RequestParam(defaultValue = "ASC")
        @Pattern(regexp = "ASC|DESC", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Направление сортировки: ASC или DESC")
        String direction,

        @RequestParam
        Boolean isActive
) {
}
