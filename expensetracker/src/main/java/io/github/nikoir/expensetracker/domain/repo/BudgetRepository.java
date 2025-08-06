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
    @EntityGraph(attributePaths = {"category", "budgetPeriod"})
    @Query("SELECT b FROM Budget b WHERE (:isActive IS NULL OR b.isActive = :isActive)")
    Page<Budget> findAll(
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );
}
