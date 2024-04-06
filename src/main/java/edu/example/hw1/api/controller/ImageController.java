package edu.example.hw1.api.controller;

import edu.example.hw1.api.dto.GetImagesResponse;
import edu.example.hw1.api.dto.UiSuccessContainer;
import edu.example.hw1.api.dto.UploadImageResponse;
import edu.example.hw1.api.mapper.ImageMapper;
import edu.example.hw1.domain.service.ImageService;
import edu.example.hw1.domain.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Base64;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Image controller.
 */
@RestController
@Tag(name = "ImageController", description = "Базовый CRUD API для работы с картинками")
@RequestMapping("/api/v1")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ImageController {
  private final ImageService imageService;
  private final ImageMapper imageMapper;
  private final JwtService jwtService;

  /**
   * Upload image.
   *
   * @param file Image
   * @return uploadImageResponse Image id
   * @throws Exception One of the errors
   */
  @Operation(summary = "Загрузка нового изображения в систему", operationId = "uploadImage")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успех выполнения операции",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = UploadImageResponse.class))),
      @ApiResponse(responseCode = "400", description = "Файл не прошел валидацию",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = UiSuccessContainer.class))),
      @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = UiSuccessContainer.class)))
  })
  @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public UploadImageResponse uploadImage(@RequestParam MultipartFile file,
                                         @RequestHeader(HttpHeaders.AUTHORIZATION)
                                         String bearerToken) throws Exception {
    var jwtToken = bearerToken.substring("Bearer ".length());
    var authorUsername = jwtService.getUsernameFromToken(jwtToken);

    return imageMapper
        .imageEntityToUploadImageResponse(imageService.uploadImageToUser(file, authorUsername));
  }

  /**
   * Get image by ID.
   *
   * @param imageId Image id
   * @return String Image Base64 coding
   * @throws Exception One of the errors
   */
  @Operation(summary = "Скачивание файла по ИД", operationId = "downloadImage")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успех выполнения операции",
          content = @Content(mediaType = MediaType.ALL_VALUE)),
      @ApiResponse(responseCode = "404", description = "Файл не найден в системе или недоступен",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = UiSuccessContainer.class))),
      @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = UiSuccessContainer.class)))
  })
  @GetMapping(value = "/image/{image-id}")
  public String downloadImage(@PathVariable("image-id") String imageId,
                              @RequestHeader(HttpHeaders.AUTHORIZATION)
                              String bearerToken) throws Exception {
    var jwtToken = bearerToken.substring("Bearer ".length());
    var authorUsername = jwtService.getUsernameFromToken(jwtToken);
    var imageIdUuid = UUID.fromString(imageId);

    return Base64.getEncoder()
        .encodeToString(imageService.downloadImage(imageIdUuid, authorUsername));
  }

  /**
   * Delete image by ID.
   *
   * @param imageId Image id
   * @return uiSuccessContainer Success status
   * @throws Exception One of the errors
   */
  @Operation(summary = "Удаление файла по ИД", operationId = "deleteImage")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успех выполнения операции"),
      @ApiResponse(responseCode = "404", description = "Файл не найден в системе или недоступен"),
      @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка")
  })
  @DeleteMapping(value = "/image/{image-id}")
  public UiSuccessContainer deleteImage(@PathVariable("image-id") String imageId,
                                        @RequestHeader(HttpHeaders.AUTHORIZATION)
                                        String bearerToken) throws Exception {
    var jwtToken = bearerToken.substring("Bearer ".length());
    var authorUsername = jwtService.getUsernameFromToken(jwtToken);
    var imageIdUuid = UUID.fromString(imageId);

    imageService.deleteImage(imageIdUuid, authorUsername);

    return new UiSuccessContainer();
  }

  /**
   * Get all user's images.
   *
   * @return getImagesResponse Image meta info
   */
  @Operation(summary = "Получение списка изображений, которые доступны пользователю",
      operationId = "getImages")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успех выполнения операции"),
      @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка")
  })
  @GetMapping(value = "/images")
  public GetImagesResponse getImages(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {
    var jwtToken = bearerToken.substring("Bearer ".length());
    var authorUsername = jwtService.getUsernameFromToken(jwtToken);

    return imageMapper.imageEntitiesToGetImagesResponse(imageService.getUserImages(authorUsername));
  }
}
