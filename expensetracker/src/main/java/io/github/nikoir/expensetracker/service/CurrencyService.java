package io.github.nikoir.expensetracker.service;

import io.github.nikoir.expensetracker.domain.entity.Currency;
import io.github.nikoir.expensetracker.domain.repo.CurrencyRepository;
import io.github.nikoir.expensetracker.dto.CurrencyModifyDto;
import io.github.nikoir.expensetracker.dto.CurrencyViewDto;
import io.github.nikoir.expensetracker.exception.AlreadyExistsException;
import io.github.nikoir.expensetracker.exception.NotFoundException;
import io.github.nikoir.expensetracker.mapper.CurrencyMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private static final EntityType ENTITY_TYPE = EntityType.CURRENCY; //TODO: когда будет совсем нечего делать, подумаю, как тут использовать AOP

    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;

    @PersistenceContext
    private final EntityManager entityManager;

    public List<CurrencyViewDto> getAll() {
        return currencyMapper.toViewDtoList(currencyRepository.findAll());
    }

    public CurrencyViewDto getByCode(String currencyCode) {
        return currencyMapper.toViewDto(
                currencyRepository.findByCodeIgnoreCase(currencyCode)
                        .orElseThrow(() -> new NotFoundException(ENTITY_TYPE, currencyCode))
        );
    }

    public CurrencyViewDto create(CurrencyModifyDto currencyModifyDto) {
        if (currencyRepository.existsByCodeIgnoreCase(currencyModifyDto.getCode())) {
            throw new AlreadyExistsException(ENTITY_TYPE, currencyModifyDto.getCode());
        }
        Currency currency = currencyMapper.toCreateEntity(currencyModifyDto);
        return currencyMapper.toViewDto(currencyRepository.save(currency));
    }

    //TODO: написать тест, который проверяет оптимистичную блокировку
    @Transactional //обязателен при использовани механизма оптимистичных блокировок, так как hibernate сравнивает значение в persistence context и в БД и выбрасывает Optimistic lock Exception
    public CurrencyViewDto update(CurrencyModifyDto currencyModifyDto) {
        Currency currency = currencyRepository
                .findByCodeIgnoreCase(currencyModifyDto.getCode())
                .orElseThrow(() -> new NotFoundException(ENTITY_TYPE, currencyModifyDto.getCode()));
        currencyMapper.toUpdateEntity(currencyModifyDto, currency);

        /*
         * Сущность находится в состоянии merged и выполнять save необязательно, чтобы данные закоммитились в конце транзакции
         * Однако! Hibernate кеширует состояние сущности и мы не получаем акутальное значение поля updatedAt
         * Поэтому принудительно пишем изменение в базу и обновляем сущность
         */
        currencyRepository.saveAndFlush(currency);

        entityManager.refresh(currency);

        return currencyMapper.toViewDto(currency);
    }
}
