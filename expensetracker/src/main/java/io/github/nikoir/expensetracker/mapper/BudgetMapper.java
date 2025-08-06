package io.github.nikoir.expensetracker.mapper;

import io.github.nikoir.expensetracker.domain.entity.Budget;
import io.github.nikoir.expensetracker.dto.response.BudgetViewDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface BudgetMapper {
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "periodName", source = "budgetPeriod.name")
    BudgetViewDto toViewDto(Budget budget);

    default Page<BudgetViewDto> toViewDtoPage(Page<Budget> budgetPage) {
        return budgetPage == null ? Page.empty(): budgetPage.map(this::toViewDto);
    }
}
