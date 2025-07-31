package io.github.nikoir.expensetracker.domain;

import io.github.nikoir.expensetracker.domain.entity.Currency;
import io.github.nikoir.expensetracker.domain.repo.CurrencyRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Поведение по умолчанию - PER_METHOD - для каждого теста создается новый экземпляр класса
 * Аннотация @Transactional по умолчанию включена в DataJpaTest.
 * Это значит, что после каждого изменения данные в БД, сохраненные в тесте, откатываются
 */
@DataJpaTest //Поднимает частичный Spring-контекст (только для JPA), создает H2-сущность
public class CurrencyRepositoryTest {
    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private EntityManager primaryEm;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    @Order(2)
    public void testFindByCodeIgnoreCase() {
        Optional<Currency> currency = currencyRepository.findByCodeIgnoreCase("usd");
        assertTrue(currency.isPresent());
        assertEquals("USD", currency.get().getCode());
    }

    @Test
    public void testExistsByCodeIgnoreCase() {
        boolean result = currencyRepository.existsByCodeIgnoreCase("usd");
        assertTrue(result);
    }

    //TODO: возможно переписать тест на тест-контейнеры. H2-база данных блокирует строки с помощью пессимистичных блокировок до окончания транзакции
    @Test
    void testOptimisticLockUpdate() {
        transactionTemplate.setPropagationBehavior(
                TransactionDefinition.PROPAGATION_REQUIRES_NEW
        ); //используем всегда новую транзакцию, чтобы получить новый экземпляр сущности из разных persistence-контекстов

        Currency currency1 = transactionTemplate.execute(_ -> currencyRepository.findByCodeIgnoreCase("USD").orElseThrow());
        String initialName = currency1.getName();

        Currency currency2 = transactionTemplate.execute(_ -> currencyRepository.findByCodeIgnoreCase("USD").orElseThrow());

        transactionTemplate.execute(_ -> {
            currency1.setName("Грязная зеленая бумажка");
            return currencyRepository.save(currency1);
        });

        assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            transactionTemplate.execute(_ -> {
                currency2.setName("Джонни, сраный ты ковбой");
                return currencyRepository.save(currency2);
            });
        });

        /* возвращаем все назад, как было, потому что мы изменяли данные в отдельных транзакциях, которые были закоммичены
         * тест должен вернуться в то же состояние, в котором он был до выполнения */

        currencyRepository.resetEntity(currency1.getCode(), initialName, 0L);
    }
}
