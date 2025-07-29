package io.github.nikoir.expensetracker.service;


import io.github.nikoir.expensetracker.domain.entity.User;
import io.github.nikoir.expensetracker.domain.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public void createUserIfNotExists(String userId) {
        if (userRepository.existsById(userId)) {
            return;
        }
        userRepository.save(User
                .builder()
                .id(userId)
                .build());
    }
}