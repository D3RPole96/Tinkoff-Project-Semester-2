package edu.example.hw1.domain.service;

import edu.example.hw1.domain.entity.UserEntity;

/**
 * Interface of authentication service.
 */
public interface AuthenticationService {
  String register(UserEntity user);

  String authenticate(UserEntity user);

}
