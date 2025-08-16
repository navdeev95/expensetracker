package io.github.nikoir.expensetracker.domain.repo;

import io.github.nikoir.expensetracker.domain.entity.BudgetPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetPeriodRepository extends JpaRepository<BudgetPeriod, String> {
}
