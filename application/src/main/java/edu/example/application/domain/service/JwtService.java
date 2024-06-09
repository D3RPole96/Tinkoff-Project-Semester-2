package edu.example.application.domain.service;

import io.jsonwebtoken.Claims;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Interface of JWT Bearer token service.
 */
public interface JwtService {
  String getUsernameFromToken(String jwtToken);

  String getRoleFromToken(String jwtToken);

  <T> T getClaim(String jwtToken, Function<Claims, T> claimsResolver);

  String generateToken(UserDetails userDetails);

  String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

  boolean isTokenValid(String jwtToken, UserDetails userDetails);
}

