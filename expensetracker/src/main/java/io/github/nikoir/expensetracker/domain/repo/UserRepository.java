package io.github.nikoir.expensetracker.domain.repo;

import io.github.nikoir.expensetracker.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
