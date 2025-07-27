package io.github.nikoir.expensetracker.domain.repo;
import io.github.nikoir.expensetracker.domain.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, String> {
    Optional<Currency> findByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCase(String code);

    List<Currency> findByNameContainingIgnoreCase(String namePart);
}
