package edu.example.hw1.api.controller;

import edu.example.hw1.api.dto.jwt.JwtRoleResponseDto;
import edu.example.hw1.api.dto.jwt.JwtUsernameResponseDto;
import edu.example.hw1.domain.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Jwt bearer controller.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jwt")
public class JwtBearerController {
  private final JwtService jwtService;

  /**
   * Get username by JWT token.
   *
   * @return JwtUsernameResponseDto Username
   */
  @GetMapping("/username")
  public JwtUsernameResponseDto getUsernameByJwt(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {
    var jwtToken = bearerToken.substring("Bearer ".length());
    var username = jwtService.getUsernameFromToken(jwtToken);

    return new JwtUsernameResponseDto(username);
  }

  /**
   * Get user role by JWT token.
   *
   * @return JwtRoleResponseDto User role
   */
  @GetMapping("/role")
  public JwtRoleResponseDto getRoleByJwt(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {
    var jwtToken = bearerToken.substring("Bearer ".length());
    var role = jwtService.getRoleFromToken(jwtToken);

    return new JwtRoleResponseDto(role);
  }
}

