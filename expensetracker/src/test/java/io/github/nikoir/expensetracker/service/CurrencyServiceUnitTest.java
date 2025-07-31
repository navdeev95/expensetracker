package io.github.nikoir.expensetracker.service;

import io.github.nikoir.expensetracker.domain.entity.Currency;
import io.github.nikoir.expensetracker.domain.repo.CurrencyRepository;
import io.github.nikoir.expensetracker.dto.CurrencyViewDto;
import io.github.nikoir.expensetracker.mapper.CurrencyMapper;
import io.github.nikoir.expensetracker.mapper.CurrencyMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) //позволяет использовать Mock-и без поднятия Spring-контекста
class CurrencyServiceUnitTest {

    @Mock
    private CurrencyRepository repository;

    @Spy
    private CurrencyMapper currencyMapper = new CurrencyMapperImpl();

    @InjectMocks
    private CurrencyService service;

    @Test
    void testGetByCode() {
        when(repository.findByCodeIgnoreCase("USD")).thenReturn(Optional.of(Currency
                .builder()
                        .code("USD")
                        .symbol("$")
                        .name("US Dollar")
                                .build()));

        CurrencyViewDto currency = service.getByCode("USD");

        assertNotNull(currency);
        assertEquals("USD", currency.getCode());
        assertEquals("$", currency.getSymbol());
        assertEquals("US Dollar", currency.getName());
    }
}
