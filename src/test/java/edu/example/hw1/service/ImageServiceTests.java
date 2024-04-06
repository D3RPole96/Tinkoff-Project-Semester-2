package edu.example.hw1.service;

import edu.example.hw1.api.exceptions.EntityNotFoundException;
import edu.example.hw1.api.exceptions.FileAccessException;
import edu.example.hw1.config.PostgreTestConfig;
import edu.example.hw1.domain.entity.ImageEntity;
import edu.example.hw1.domain.entity.UserEntity;
import edu.example.hw1.domain.entity.UserRole;
import edu.example.hw1.domain.service.ImageService;
import edu.example.hw1.domain.service.ImageServiceImpl;
import edu.example.hw1.domain.service.MinioService;
import edu.example.hw1.domain.service.UserService;
import edu.example.hw1.repository.ImageRepository;
import edu.example.hw1.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@ContextConfiguration(initializers = PostgreTestConfig.Initializer.class)
public class ImageServiceTests {
    private final String testUserUsername = "testUser";
    private final String secondTestUserUsername = "testUser2";
    private final String testImageLink = "testLink";

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Mock
    private MinioService minioService;
    private ImageService imageService;

    @AfterEach
    @BeforeEach
    public void prepare() throws Exception {
        imageRepository.deleteAll();
        userRepository.deleteAll();

        doReturn(new ImageEntity().setLink(testImageLink).setName("testName").setSize(1337L)).when(minioService).uploadImage(any());
        doNothing().when(minioService).deleteImage(anyString());
        doReturn(new byte[0]).when(minioService).downloadImage(anyString());

        imageService = new ImageServiceImpl(imageRepository, minioService, userService);
    }

    @Test
    public void downloadImage_ShouldReturnBytes_WhenUserHasAccess() throws Exception {
        // given
        createTestUserWithOneImage();
        var savedImageId = imageRepository.findImageEntityByLink(testImageLink).get().getId();

        // when
        var imageBytes = imageService.downloadImage(savedImageId, testUserUsername);

        // then
        assertNotNull(imageBytes);
    }

    @Test
    public void downloadImage_ShouldReturnBytes_WhenUserDoesNotHaveAccess() throws Exception {
        // given
        createTestUserWithOneImage();
        createSecondTestUser();
        var savedImageId = imageRepository.findImageEntityByLink(testImageLink).get().getId();

        // when
        Executable action = () -> imageService.downloadImage(savedImageId, secondTestUserUsername);

        // then
        var exception = assertThrows(FileAccessException.class,
                action);
        assertEquals("Нет доступа к этому файлу", exception.getMessage());
    }

    @Test
    public void downloadImage_ShouldThrowEntityNotFoundException_WhenImageDoesNotExist() throws Exception {
        // given
        var randomUUID = UUID.randomUUID();

        // when
        Executable action = () -> imageService.downloadImage(randomUUID, secondTestUserUsername);

        // then
        var exception = assertThrows(EntityNotFoundException.class,
                action);
        assertEquals("Картинка с указанным ID не найдена", exception.getMessage());
    }

    @Test
    public void deleteImage_ShouldDoesNotThrowException_WhenUserHasAccess() throws Exception {
        // given
        createTestUserWithOneImage();
        var savedImageId = imageRepository.findImageEntityByLink(testImageLink).get().getId();

        // when
        Executable action = () -> imageService.deleteImage(savedImageId, testUserUsername);

        // then
        assertDoesNotThrow(action);
    }

    @Test
    public void deleteImage_ShouldReturnBytes_WhenUserDoesNotHaveAccess() throws Exception {
        // given
        createTestUserWithOneImage();
        createSecondTestUser();
        var savedImageId = imageRepository.findImageEntityByLink(testImageLink).get().getId();

        // when
        Executable action = () -> imageService.deleteImage(savedImageId, secondTestUserUsername);

        // then
        var exception = assertThrows(FileAccessException.class,
                action);
        assertEquals("Нет доступа к этому файлу", exception.getMessage());
    }

    @Test
    public void getUserImages_ShouldDoesNotThrowException_WhenUserHasAccess() throws Exception {
        // given
        createTestUserWithOneImage();
        var savedImageId = imageRepository.findImageEntityByLink(testImageLink).get().getId();

        // when
        Executable action = () -> imageService.deleteImage(savedImageId, testUserUsername);

        // then
        assertDoesNotThrow(action);
    }

    @Test
    public void deleteImage_ShouldThrowEntityNotFoundException_WhenImageDoesNotExist() throws Exception {
        // given
        var randomUUID = UUID.randomUUID();

        // when
        Executable action = () -> imageService.deleteImage(randomUUID, secondTestUserUsername);

        // then
        var exception = assertThrows(EntityNotFoundException.class,
                action);
        assertEquals("Картинка с указанным ID не найдена", exception.getMessage());
    }

    @Test
    public void getUserImages_ShouldThrowEntityNotFoundException_WhenImageDoesNotExist() throws Exception {
        // given
        createTestUserWithOneImage();

        // when
        var userImages = imageService.getUserImages(testUserUsername);

        // then
        assertEquals(1, userImages.size());
        assertEquals("testLink", userImages.get(0).getLink());
        assertEquals("testName", userImages.get(0).getName());
        assertEquals(1337L, userImages.get(0).getSize());
    }

    private void createTestUserWithOneImage() {
        var user = new UserEntity()
                .setUsername(testUserUsername)
                .setFirstName("Test")
                .setLastName("User")
                .setPassword("1")
                .setCreationTime(LocalDateTime.now(ZoneOffset.UTC))
                .setRole(UserRole.USER)
                .setDeleted(false);
        var savedUser = userRepository.save(user);

        var image = new ImageEntity()
                .setLink("testLink")
                .setName("testName")
                .setSize(1337L)
                .setUser(savedUser);
        imageRepository.save(image);
    }

    private void createSecondTestUser() {
        var user = new UserEntity()
                .setUsername(secondTestUserUsername)
                .setFirstName("Test")
                .setLastName("User2")
                .setPassword("2")
                .setCreationTime(LocalDateTime.now(ZoneOffset.UTC))
                .setRole(UserRole.USER)
                .setDeleted(false);
        userRepository.save(user);
    }
}
