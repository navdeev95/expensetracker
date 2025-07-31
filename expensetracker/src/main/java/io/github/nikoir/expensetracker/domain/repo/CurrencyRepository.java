package io.github.nikoir.expensetracker.domain.repo;
import io.github.nikoir.expensetracker.domain.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, String> {
    Optional<Currency> findByCodeIgnoreCase(String code);
    boolean existsByCodeIgnoreCase(String code);

    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE currencies SET version = :version, name = :name WHERE code = :code")
    void resetEntity(@Param("code") String code, @Param("name") String name, @Param("version") Long version);
}
