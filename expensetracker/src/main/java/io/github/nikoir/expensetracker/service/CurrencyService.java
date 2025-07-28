package io.github.nikoir.expensetracker.service;

import io.github.nikoir.expensetracker.domain.entity.Currency;
import io.github.nikoir.expensetracker.domain.repo.CurrencyRepository;
import io.github.nikoir.expensetracker.dto.CurrencyDto;
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

    public List<CurrencyDto> getAll() {
        return currencyMapper.toDtoList(currencyRepository.findAll());
    }

    public CurrencyDto getByCode(String currencyCode) {
        return currencyMapper.toDto(
                currencyRepository.findByCodeIgnoreCase(currencyCode)
                        .orElseThrow(() -> new NotFoundException(ENTITY_TYPE, currencyCode))
        );
    }

    public CurrencyDto create(CurrencyDto currencyDto) {
        if (currencyRepository.existsByCodeIgnoreCase(currencyDto.getCode())) {
            throw new AlreadyExistsException(ENTITY_TYPE, currencyDto.getCode());
        }
        Currency currency = currencyMapper.toCreateEntity(currencyDto);
        return currencyMapper.toDto(currencyRepository.save(currency));
    }

    //TODO: написать тест, который проверяет оптимистичную блокировку
    @Transactional //обязателен при использовани механизма оптимистичных блокировок, так как hibernate сравнивает значение в persistence context и в БД и выбрасывает Optimistic lock Exception
    public CurrencyDto update(CurrencyDto currencyDto) {
        Currency currency = currencyRepository
                .findByCodeIgnoreCase(currencyDto.getCode())
                .orElseThrow(() -> new NotFoundException(ENTITY_TYPE, currencyDto.getCode()));
        currencyMapper.toUpdateEntity(currencyDto, currency);
        return currencyMapper.toDto(currency); //нет необходимости вызывать метод save, так как сущность находится в состоянии managed и обновления сразу улетят в базу
    }
}
