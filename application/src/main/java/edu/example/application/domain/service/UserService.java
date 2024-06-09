package edu.example.application.domain.service;

import edu.example.application.domain.entity.UserEntity;

/**
 * Interface of users service.
 */
public interface UserService {
  UserEntity getUserByUsername(String username);
}
