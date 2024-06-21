package edu.example.application.domain.service;

import edu.example.application.domain.entity.UserEntity;

/**
 * Interface of authentication service.
 */
public interface AuthenticationService {
  String register(UserEntity user);

  String authenticate(UserEntity user);

}
