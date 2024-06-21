package edu.example.application.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Data transfer object for authentication register.
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class AuthenticationRegisterDto {
  private String username;
  private String firstName;
  private String lastName;
  private String password;
}
