package edu.example.hw1.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data transfer object for authentication response.
 */
@Data
@AllArgsConstructor
public class AuthenticationResponseDto {
  private String jwtToken;
}

