package edu.example.hw1.domain.service;

import edu.example.hw1.domain.entity.ImageEntity;
import edu.example.hw1.domain.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public interface UserService {
  UserEntity getUserByUsername(String username);
}
