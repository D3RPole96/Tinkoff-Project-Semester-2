package edu.example.application.domain.service;

import edu.example.application.api.exceptions.EntityNotFoundException;
import edu.example.application.domain.entity.UserEntity;
import edu.example.application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of users service.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  @Override
  public UserEntity getUserByUsername(String username) {
    var user = userRepository
        .findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException(
            "Пользователь с указанным именем пользователя не найден"));

    if (user.isDeleted()) {
      throw new EntityNotFoundException("Пользователь с указанным именем пользователя удален");
    }

    return user;
  }
}
