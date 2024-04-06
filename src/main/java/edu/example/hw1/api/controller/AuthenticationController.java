package edu.example.hw1.api.controller;

import edu.example.hw1.api.dto.auth.AuthenticationLoginDto;
import edu.example.hw1.api.dto.auth.AuthenticationRegisterDto;
import edu.example.hw1.api.dto.auth.AuthenticationResponseDto;
import edu.example.hw1.api.mapper.AuthenticationMapper;
import edu.example.hw1.domain.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication controller.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
  private final AuthenticationService authenticationService;
  private final AuthenticationMapper authenticationMapper;

  /**
   * Register user.
   *
   * @param authenticationRegisterDto register user body
   * @return authenticationResponseDto jwtToken
   */
  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public AuthenticationResponseDto register(
      @RequestBody AuthenticationRegisterDto authenticationRegisterDto) {
    var user = authenticationMapper.authenticationRegisterDtoToUser(authenticationRegisterDto);

    return new AuthenticationResponseDto(authenticationService.register(user));
  }

  /**
   * Authenticate user.
   *
   * @param authenticationLoginDto authenticate user body
   * @return authenticationResponseDto jwtToken
   */
  @PostMapping("/authenticate")
  public AuthenticationResponseDto authenticate(
      @RequestBody AuthenticationLoginDto authenticationLoginDto) {
    var user = authenticationMapper.authenticationLoginDtoToUser(authenticationLoginDto);

    return new AuthenticationResponseDto(authenticationService.authenticate(user));
  }
}


