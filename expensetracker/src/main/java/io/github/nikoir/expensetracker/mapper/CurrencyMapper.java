package io.github.nikoir.expensetracker.mapper;

import io.github.nikoir.expensetracker.domain.entity.Currency;
import io.github.nikoir.expensetracker.dto.CurrencyModifyDto;
import io.github.nikoir.expensetracker.dto.CurrencyViewDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {
    CurrencyViewDto toViewDto(Currency currency);
    List<CurrencyViewDto> toViewDtoList(List<Currency> currencyList);
    Currency toCreateEntity(CurrencyModifyDto currencyModifyDto);
    void toUpdateEntity(CurrencyModifyDto currencyModifyDto, @MappingTarget Currency currency);
}
