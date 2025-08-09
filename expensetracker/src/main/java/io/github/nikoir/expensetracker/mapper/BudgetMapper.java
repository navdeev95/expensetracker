package io.github.nikoir.expensetracker.mapper;

import io.github.nikoir.expensetracker.domain.entity.Budget;
import io.github.nikoir.expensetracker.dto.response.BudgetViewDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;

@Mapper(componentModel = "spring")
public interface BudgetMapper {
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "periodName", source = "budgetPeriod.name")
    @Mapping(target = "currency", source = "currency.name")
    BudgetViewDto toViewDto(Budget budget);

    default PagedModel<BudgetViewDto> toViewDtoPage(Page<Budget> budgetPage) {
        return budgetPage == null ?
                new PagedModel<>(Page.empty()): new PagedModel<>(budgetPage.map(this::toViewDto));
    }
}
