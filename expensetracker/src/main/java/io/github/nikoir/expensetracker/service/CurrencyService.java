package io.github.nikoir.expensetracker.service;

import io.github.nikoir.expensetracker.domain.entity.Currency;
import io.github.nikoir.expensetracker.domain.repo.CurrencyRepository;
import io.github.nikoir.expensetracker.dto.request.CurrencyModifyDto;
import io.github.nikoir.expensetracker.dto.response.CurrencyViewDto;
import io.github.nikoir.expensetracker.exception.AlreadyExistsException;
import io.github.nikoir.expensetracker.exception.NotFoundException;
import io.github.nikoir.expensetracker.mapper.CurrencyMapper;
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

    //TODO: использовать Query Cache
    //TODO: добавить использование Paging and sorting
    public List<CurrencyViewDto> getAll() {
        return currencyMapper.toViewDtoList(currencyRepository.findAll());
    }

    //TODO: использовать L2-cache
    public CurrencyViewDto getByCode(String currencyCode) {
        return currencyMapper.toViewDto(
                currencyRepository.findByCodeIgnoreCase(currencyCode)
                        .orElseThrow(() -> new NotFoundException(ENTITY_TYPE, currencyCode))
        );
    }

    public CurrencyViewDto create(CurrencyModifyDto currencyModifyDto) {
        if (currencyRepository.existsByCodeIgnoreCase(currencyModifyDto.code())) {
            throw new AlreadyExistsException(ENTITY_TYPE, currencyModifyDto.code());
        }
        Currency currency = currencyMapper.toCreateEntity(currencyModifyDto);
        return currencyMapper.toViewDto(currencyRepository.save(currency));
    }

    //TODO: добавить механизм Rertyable
    @Transactional //обязателен при использовани механизма оптимистичных блокировок, так как hibernate сравнивает значение в persistence context и в БД и выбрасывает Optimistic lock Exception
    public CurrencyViewDto update(CurrencyModifyDto currencyModifyDto) {
        Currency currency = currencyRepository
                .findByCodeIgnoreCase(currencyModifyDto.code())
                .orElseThrow(() -> new NotFoundException(ENTITY_TYPE, currencyModifyDto.code()));
        currencyMapper.toUpdateEntity(currencyModifyDto, currency);

        //Сущность находится в состоянии merged и выполнять save необязательно, чтобы данные закоммитились в конце транзакции

        return currencyMapper.toViewDto(currency);
    }
}
