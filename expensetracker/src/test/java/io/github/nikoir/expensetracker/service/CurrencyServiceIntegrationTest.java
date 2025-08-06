package io.github.nikoir.expensetracker.service;

import io.github.nikoir.expensetracker.domain.entity.audit.JpaAuditingConfig;
import io.github.nikoir.expensetracker.domain.repo.CurrencyRepository;
import io.github.nikoir.expensetracker.dto.request.CurrencyModifyDto;
import io.github.nikoir.expensetracker.dto.response.CurrencyViewDto;
import io.github.nikoir.expensetracker.exception.AlreadyExistsException;
import io.github.nikoir.expensetracker.mapper.CurrencyMapper;
import io.github.nikoir.expensetracker.mapper.CurrencyMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({CurrencyService.class, CurrencyMapperImpl.class, JpaAuditingConfig.class})
public class CurrencyServiceIntegrationTest {
    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CurrencyMapper currencyMapper;

    @Autowired
    private CurrencyService currencyService;

    @Test
    void testSave() {
        currencyService.create(new CurrencyModifyDto("KZT", "Казахстанский тенге", "₸"));

        CurrencyViewDto dto = currencyService.getByCode("KZT");

        assertEquals("KZT", dto.code());
        assertEquals("₸", dto.symbol());
        assertEquals("Казахстанский тенге", dto.name());
    }

    @Test
    void testSaveWithExistingCode() {
        assertThrows(AlreadyExistsException.class, () ->
                currencyService.create(new CurrencyModifyDto("USD", "US Dollar", "$")));
    }


    @Test
    void testUpdate() {
        CurrencyViewDto updatedCurrency = currencyService.update(
                new CurrencyModifyDto("USD", "грязная зеленая бумажка", "$"));

        assertEquals("USD", updatedCurrency.code());
        assertEquals("грязная зеленая бумажка", updatedCurrency.name());
        assertEquals("$", updatedCurrency.symbol());

    }
}
