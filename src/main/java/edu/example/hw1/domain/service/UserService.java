package edu.example.hw1.domain.service;

import edu.example.hw1.domain.entity.UserEntity;

/**
 * Interface of users service.
 */
public interface UserService {
  UserEntity getUserByUsername(String username);
}
