package edu.example.hw1.domain.service;

import edu.example.hw1.domain.entity.UserEntity;
import edu.example.hw1.domain.entity.UserRole;
import edu.example.hw1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Override
    public String register(UserEntity user) {
        encodeUserPassword(user);

        user.setRole(UserRole.USER);
        user.setDeleted(false);
        user.setCreationTime(LocalDateTime.now(ZoneOffset.UTC));
        userRepository.save(user);

        return jwtService.generateToken(user);
    }

    @Override
    public String authenticate(UserEntity user) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );
        var userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        return jwtService.generateToken(userDetails);
    }

    private void encodeUserPassword(UserEntity user) {
        var userPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(userPassword));
    }
}


