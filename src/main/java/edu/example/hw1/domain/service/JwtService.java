package edu.example.hw1.domain.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String getUsernameFromToken(String jwtToken);
    String getRoleFromToken(String jwtToken);
    <T> T getClaim(String jwtToken, Function<Claims, T> claimsResolver);
    String generateToken(UserDetails userDetails);
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);
    boolean isTokenValid(String jwtToken, UserDetails userDetails);
}

