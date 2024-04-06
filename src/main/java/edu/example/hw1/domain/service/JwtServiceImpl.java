package edu.example.hw1.domain.service;

import edu.example.hw1.api.exceptions.BadRequestException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
  @Value("${application.auth.jwt.secret-key}")
  private String secretKey;

  @Value("${application.auth.jwt.expiration-timeout-milliseconds}")
  private Long expirationTimeoutMilliseconds;


  @Override
  public String getUsernameFromToken(String jwtToken) {
    return getClaim(jwtToken, Claims::getSubject);
  }

  @Override
  public String getRoleFromToken(String jwtToken) {
    return getClaim(jwtToken, claims -> claims.get("role", String.class).substring("ROLE_".length()));
  }

  @Override
  public <T> T getClaim(String jwtToken, @NotNull Function<Claims, T> claimsResolver) {
    var claims = getClaimsFromToken(jwtToken);

    return claimsResolver.apply(claims);
  }

  @Override
  public String generateToken(UserDetails userDetails) {
    var extraClaims = new HashMap<String, Object>();
    extraClaims.put("role", userDetails.getAuthorities().stream().findFirst()
        .orElseThrow(() -> new RuntimeException("У пользователя нет ролей")).getAuthority());

    return generateToken(extraClaims, userDetails);
  }

  @Override
  public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return Jwts
        .builder()
        .claims()
        .add(extraClaims)
        .and()
        .subject(userDetails.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + expirationTimeoutMilliseconds))
        .signWith(getSignInKey(), Jwts.SIG.HS256)
        .compact();
  }

  @Override
  public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
    var userEmail = getUsernameFromToken(jwtToken);

    return (userEmail.equals(userDetails.getUsername())) && isTokenNotExpired(jwtToken);
  }

  private boolean isTokenNotExpired(String jwtToken) {
    return getTokenExpiration(jwtToken).after(new Date(System.currentTimeMillis()));
  }

  private Date getTokenExpiration(String jwtToken) {
    return getClaim(jwtToken, Claims::getExpiration);
  }

  private Claims getClaimsFromToken(String jwtToken) {
    try {
      return Jwts
          .parser()
          .verifyWith(getSignInKey())
          .build()
          .parseSignedClaims(jwtToken)
          .getPayload();
    } catch (Exception ex) {
      throw new BadRequestException("Invalid JWT");
    }
  }

  private SecretKey getSignInKey() {
    var keyBytes = Decoders.BASE64.decode(secretKey);

    return Keys.hmacShaKeyFor(keyBytes);
  }
}


