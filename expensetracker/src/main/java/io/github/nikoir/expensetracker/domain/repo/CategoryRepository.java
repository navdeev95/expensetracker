package io.github.nikoir.expensetracker.domain.repo;

import io.github.nikoir.expensetracker.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByIdAndUserId(Long id, String userId);
}
