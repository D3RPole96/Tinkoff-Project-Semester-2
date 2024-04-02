package edu.example.hw1.api.controller;

import edu.example.hw1.api.dto.GetImagesResponse;
import edu.example.hw1.api.dto.UiSuccessContainer;
import edu.example.hw1.api.dto.UploadImageResponse;
import edu.example.hw1.api.mapper.ImageMapper;
import edu.example.hw1.domain.service.ImageService;
import edu.example.hw1.domain.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;

import java.util.UUID;

@RestController
@Tag(name = "ImageController", description = "Базовый CRUD API для работы с картинками")
@RequestMapping("/api/v1")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    private final ImageMapper imageMapper;
    private final JwtService jwtService;

    @Tags(value = { @Tag(name = "Image Controller") })
    @Operation(summary = "Загрузка нового изображения в систему", operationId = "uploadImage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успех выполнения операции"),
            @ApiResponse(responseCode = "400", description = "Файл не прошел валидацию"),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка")
    })
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadImageResponse uploadImage(@RequestParam MultipartFile file,
                                           @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) throws Exception {
        var jwtToken = bearerToken.substring("Bearer ".length());
        var authorUsername = jwtService.getUsernameFromToken(jwtToken);

        return imageMapper.imageEntityToUploadImageResponse(imageService.uploadImageToUser(file, authorUsername));
    }

    @Tags(value = { @Tag(name = "Image Controller") })
    @Operation(summary = "Скачивание файла по ИД", operationId = "downloadImage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успех выполнения операции"),
            @ApiResponse(responseCode = "404", description = "Файл не найден в системе или недоступен"),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка")
    })
    @GetMapping(value = "/image/{image-id}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] downloadImage(@PathVariable("image-id") String imageId,
                           @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) throws Exception {
        var jwtToken = bearerToken.substring("Bearer ".length());
        var authorUsername = jwtService.getUsernameFromToken(jwtToken);
        var imageIdUUID = UUID.fromString(imageId);

        return imageService.downloadImage(imageIdUUID, authorUsername);
    }

    @Tags(value = { @Tag(name = "Image Controller") })
    @Operation(summary = "Удаление файла по ИД", operationId = "deleteImage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успех выполнения операции"),
            @ApiResponse(responseCode = "404", description = "Файл не найден в системе или недоступен"),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка")
    })
    @DeleteMapping(value = "/image/{image-id}")
    public UiSuccessContainer deleteImage(@PathVariable("image-id") String imageId,
                                          @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) throws Exception {
        var jwtToken = bearerToken.substring("Bearer ".length());
        var authorUsername = jwtService.getUsernameFromToken(jwtToken);
        var imageIdUUID = UUID.fromString(imageId);

        imageService.deleteImage(imageIdUUID, authorUsername);

        return new UiSuccessContainer();
    }

    @Tags(value = { @Tag(name = "Image Controller") })
    @Operation(summary = "Получение списка изображений, которые доступны пользователю", operationId = "getImages")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успех выполнения операции"),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка")
    })
    @GetMapping(value = "/images")
    public GetImagesResponse getImages(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) throws Exception {
        var jwtToken = bearerToken.substring("Bearer ".length());
        var authorUsername = jwtService.getUsernameFromToken(jwtToken);

        return imageMapper.imageEntitiesToGetImagesResponse(imageService.getUserImages(authorUsername));
    }
}
