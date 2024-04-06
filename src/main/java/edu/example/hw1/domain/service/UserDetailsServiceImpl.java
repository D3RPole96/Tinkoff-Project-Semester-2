package edu.example.hw1.domain.service;

import edu.example.hw1.api.exceptions.EntityNotFoundException;
import edu.example.hw1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) throws EntityNotFoundException {
    return userService.getUserByUsername(username);
  }
}

