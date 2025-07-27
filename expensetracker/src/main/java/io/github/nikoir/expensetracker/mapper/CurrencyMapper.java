package io.github.nikoir.expensetracker.mapper;

import io.github.nikoir.expensetracker.domain.entity.Currency;
import io.github.nikoir.expensetracker.dto.CurrencyDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {
    CurrencyDto toDto(Currency currency);
    List<CurrencyDto> toDtoList(List<Currency> currencyList);
    Currency toEntity(CurrencyDto currencyDto);
}
