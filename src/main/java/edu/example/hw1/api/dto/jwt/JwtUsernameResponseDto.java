package edu.example.hw1.api.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data transfer object for getting username claim from JWT token.
 */
@AllArgsConstructor
@Data
public class JwtUsernameResponseDto {
  private String username;
}

