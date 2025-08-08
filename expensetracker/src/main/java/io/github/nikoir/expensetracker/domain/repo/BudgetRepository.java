package io.github.nikoir.expensetracker.domain.repo;

import io.github.nikoir.expensetracker.domain.entity.Budget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    @EntityGraph(attributePaths = {"category.user", "budgetPeriod", "currency"})
    @Query("""
            SELECT b FROM Budget b
            JOIN b.category c
            WHERE (:isActive IS NULL OR b.isActive = :isActive) AND c.user.id = :userId
            """)
    Page<Budget> findAllForUser(
            @Param("isActive") Boolean isActive,
            @Param("userId") String userId,
            Pageable pageable
    );
}
