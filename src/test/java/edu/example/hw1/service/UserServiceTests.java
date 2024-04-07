package edu.example.hw1.service;

import edu.example.hw1.api.exceptions.EntityNotFoundException;
import edu.example.hw1.config.PostgreTestConfig;
import edu.example.hw1.domain.entity.UserEntity;
import edu.example.hw1.domain.entity.UserRole;
import edu.example.hw1.domain.service.UserService;
import edu.example.hw1.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ContextConfiguration(initializers = PostgreTestConfig.Initializer.class)
public class UserServiceTests {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    @BeforeEach
    public void clear() {
        userRepository.deleteAll();
    }

    @Test
    public void getUserByUsername_ShouldReturnUser_WhenUserExists() {
        // given
        var userToSave = new UserEntity()
                .setUsername("testUser")
                .setFirstName("F")
                .setLastName("L")
                .setPassword("1")
                .setCreationTime(LocalDateTime.now(ZoneOffset.UTC))
                .setRole(UserRole.USER)
                .setDeleted(false);
        var savedUser = userRepository.save(userToSave);

        var userUsername = savedUser.getUsername();

        // when
        var foundUser = userService.getUserByUsername(userUsername);

        // then
        assertEquals(savedUser.getId(), foundUser.getId());
        assertEquals(savedUser.getUsername(), foundUser.getUsername());
        assertEquals(savedUser.getFirstName(), foundUser.getFirstName());
        assertEquals(savedUser.getLastName(), foundUser.getLastName());
        assertEquals(savedUser.getPassword(), foundUser.getPassword());
        assertTrue(ChronoUnit.SECONDS.between(savedUser.getCreationTime(), foundUser.getCreationTime()) <= 1);
        assertEquals(savedUser.getRole(), foundUser.getRole());
        assertEquals(savedUser.isDeleted(), foundUser.isDeleted());
    }

    @Test
    public void getUserByUsername_ShouldThrowEntityNotFoundException_WhenUserDoesNotExist() {
        // given
        var userUsername = "testUser";

        // when
        Executable action = () -> userService.getUserByUsername(userUsername);

        // then
        var exception = assertThrows(EntityNotFoundException.class,
                action);
        assertEquals("Пользователь с указанным именем пользователя не найден", exception.getMessage());
    }

    @Test
    public void getUserByUsername_ShouldThrowEntityNotFoundException_WhenUserIsDeleted() {
        // given
        var userToSave = new UserEntity()
                .setUsername("testUser")
                .setFirstName("F")
                .setLastName("L")
                .setPassword("1")
                .setCreationTime(LocalDateTime.now(ZoneOffset.UTC))
                .setRole(UserRole.USER)
                .setDeleted(true);
        var savedUser = userRepository.save(userToSave);

        var userUsername = savedUser.getUsername();

        // when
        Executable action = () -> userService.getUserByUsername(userUsername);

        // then
        var exception = assertThrows(EntityNotFoundException.class,
                action);
        assertEquals("Пользователь с указанным именем пользователя удален", exception.getMessage());
    }
}
