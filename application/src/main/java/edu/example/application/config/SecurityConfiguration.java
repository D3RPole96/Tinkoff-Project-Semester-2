package edu.example.application.config;

import edu.example.application.auth.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

/**
 * Spring Security configuration.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final AuthenticationProvider authenticationProvider;
  private final LogoutHandler logoutHandler;

  /**
   * Spring Security permissions settings.
   *
   * @param httpSecurity httpSecurity
   * @return SecurityFilterChain
   * @throws Exception One of the authentication errors
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(request -> request
            .requestMatchers("/swagger-ui/**").permitAll()
            .requestMatchers("/v3/api-docs/**").permitAll()
            .requestMatchers("/api/v1/auth/**").permitAll()
            .anyRequest()
            .authenticated())
        .sessionManagement(configurer -> configurer
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .logout(logout -> logout.logoutUrl("/auth/logout")
            .addLogoutHandler(logoutHandler)
            .logoutSuccessHandler((request, response, authentication)
                -> SecurityContextHolder.clearContext())
        );

    return httpSecurity.build();
  }
}

