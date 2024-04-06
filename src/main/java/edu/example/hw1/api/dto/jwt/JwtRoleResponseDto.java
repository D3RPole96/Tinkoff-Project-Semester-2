package edu.example.hw1.api.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data transfer object for getting role claim from JWT token.
 */
@AllArgsConstructor
@Data
public class JwtRoleResponseDto {
  private String role;
}

