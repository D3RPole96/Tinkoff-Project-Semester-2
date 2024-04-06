package edu.example.hw1.service;

import edu.example.hw1.api.exceptions.EntityNotFoundException;
import edu.example.hw1.config.PostgreTestConfig;
import edu.example.hw1.domain.entity.UserEntity;
import edu.example.hw1.domain.entity.UserRole;
import edu.example.hw1.domain.service.AuthenticationService;
import edu.example.hw1.domain.service.JwtService;
import edu.example.hw1.domain.service.UserService;
import edu.example.hw1.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(initializers = PostgreTestConfig.Initializer.class)
public class JwtServiceTests {
    private final String testUserUsername = "testUser";
    private final UserRole testUserRole = UserRole.USER;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void getUsernameFromToken_ShouldReturnCorrectUsername() {
        // given
        var testJwtToken = getTestJwtToken();

        // when
        var username = jwtService.getUsernameFromToken(testJwtToken);

        // then
        assertEquals(testUserUsername, username);
    }

    @Test
    public void getRoleFromToken_ShouldReturnCorrectRole() {
        // given
        var testJwtToken = getTestJwtToken();

        // when
        var role = jwtService.getRoleFromToken(testJwtToken);

        // then
        assertEquals(testUserRole.toString(), role);
    }

    @Test
    public void getClaim_ShouldReturnClaim() {
        // given
        var testJwtToken = getTestJwtToken();

        // when
        var claimValue = jwtService.getClaim(testJwtToken, Claims::getSubject);

        // then
        assertEquals(testUserUsername, claimValue);
    }

    @Test
    public void generateToken_ShouldGenerateValidToken() {
        // given
        var testUser = new UserEntity()
                .setUsername(testUserUsername)
                .setFirstName("Test")
                .setLastName("User")
                .setPassword(passwordEncoder.encode("1"))
                .setCreationTime(LocalDateTime.now(ZoneOffset.UTC))
                .setRole(testUserRole)
                .setDeleted(false);

        // when
        var token = jwtService.generateToken(testUser);

        // then
        assertEquals(testUserUsername, jwtService.getUsernameFromToken(token));
        assertEquals(testUserRole.toString(), jwtService.getRoleFromToken(token));
        assertTrue(jwtService.isTokenValid(token, testUser));
    }

    @Test
    public void generateToken_ShouldThrowRuntimeException_WhenUserDoesNotHaveRole() {
        // given
        var testRolelessUser = new RolelessUser();

        // when
        Executable action = () -> jwtService.generateToken(testRolelessUser);

        // then
        var exception = assertThrows(RuntimeException.class,
                action);
        assertEquals("У пользователя нет ролей", exception.getMessage());
    }

    private String getTestJwtToken() {
        var user = new UserEntity()
                .setUsername(testUserUsername)
                .setFirstName("Test")
                .setLastName("User")
                .setPassword(passwordEncoder.encode("1"))
                .setCreationTime(LocalDateTime.now(ZoneOffset.UTC))
                .setRole(testUserRole)
                .setDeleted(false);

        return jwtService.generateToken(user);
    }

    private class RolelessUser implements UserDetails {

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return new ArrayList<>();
        }

        @Override
        public String getPassword() {
            return null;
        }

        @Override
        public String getUsername() {
            return null;
        }

        @Override
        public boolean isAccountNonExpired() {
            return false;
        }

        @Override
        public boolean isAccountNonLocked() {
            return false;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return false;
        }

        @Override
        public boolean isEnabled() {
            return false;
        }
    }
}
