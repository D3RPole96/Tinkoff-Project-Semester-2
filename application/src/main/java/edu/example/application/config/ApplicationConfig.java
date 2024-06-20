package edu.example.application.config;

import io.github.bucket4j.Bucket;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Config for Spring Boot application.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
  private final UserDetailsService userDetailsService;

  /**
   * Authentication provider bean with UserDetailsService and PasswordEncoder.
   *
   * @return AuthenticationProvider
   */
  @Bean
  public AuthenticationProvider authenticationProvider() {
    var authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }

  /**
   * Authentication manager bean.
   *
   * @param configuration Authentication configuration
   * @return AuthenticationManager
   * @throws Exception One of the authentication errors
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }

  /**
   * Password encoder bean.
   *
   * @return BCryptPasswordEncoder
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public Bucket createBucket() {
    return Bucket.builder()
        .addLimit(limit -> limit.capacity(10).refillGreedy(10, Duration.ofMinutes(1)))
        .build();
  }
}

