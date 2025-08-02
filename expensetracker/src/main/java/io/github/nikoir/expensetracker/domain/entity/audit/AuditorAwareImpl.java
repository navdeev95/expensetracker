package io.github.nikoir.expensetracker.domain.entity.audit;

import io.github.nikoir.expensetracker.domain.entity.User;
import io.github.nikoir.expensetracker.domain.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<User> {
    private final UserRepository userRepository;
    @Override
    public Optional<User> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
            //TODO: оптимизировать через кеши поход в базу
            return userRepository.findById(jwt.getSubject()); //забираем поле sub - уникальный идентификатор пользователя в keycloak
        }
        return Optional.empty();
    }
}
