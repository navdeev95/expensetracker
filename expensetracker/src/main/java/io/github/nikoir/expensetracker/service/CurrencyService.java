package io.github.nikoir.expensetracker.service;

import io.github.nikoir.expensetracker.domain.entity.Currency;
import io.github.nikoir.expensetracker.domain.repo.CurrencyRepository;
import io.github.nikoir.expensetracker.dto.CurrencyDto;
import io.github.nikoir.expensetracker.exception.AlreadyExistsException;
import io.github.nikoir.expensetracker.exception.NotFoundException;
import io.github.nikoir.expensetracker.mapper.CurrencyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private static final EntityType ENTITY_TYPE = EntityType.CURRENCY; //TODO: когда будет совсем нечего делать, подумаю, как тут использовать AOP

    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;

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
        Currency currency = currencyMapper.toEntity(currencyDto);
        return currencyMapper.toDto(currencyRepository.save(currency));
    }
}
