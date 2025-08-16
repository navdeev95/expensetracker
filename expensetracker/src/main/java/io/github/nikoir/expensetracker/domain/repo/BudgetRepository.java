package io.github.nikoir.expensetracker.domain.repo;

import io.github.nikoir.expensetracker.domain.entity.Budget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long>, JpaSpecificationExecutor<Budget> {

    @Query("SELECT COUNT(b.id) > 0 " +
            "FROM " +
            "Budget b JOIN " +
            "Category c ON b.category.id = c.id " +
            "WHERE c.id = :categoryId AND " +
            "b.startDate <= :toDate AND " +
            "(b.endDate IS NULL OR b.endDate >= :fromDate)")
    boolean existsByCategoryAndDates(@Param("categoryId") Long categoryId,
                                     @Param("fromDate") Instant fromDate,
                                     @Param("toDate") Instant toDate);
}
