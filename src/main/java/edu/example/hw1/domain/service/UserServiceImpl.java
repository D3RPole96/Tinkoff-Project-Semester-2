package edu.example.hw1.domain.service;

import edu.example.hw1.api.exceptions.EntityNotFoundException;
import edu.example.hw1.domain.entity.UserEntity;
import edu.example.hw1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserEntity getUserByUsername(String username) {
        var user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с указанным именем пользователя не найден"));

        if (user.isDeleted()) {
            throw new EntityNotFoundException("Пользователь с указанным именем пользователя удален");
        }

        return user;
    }
}
