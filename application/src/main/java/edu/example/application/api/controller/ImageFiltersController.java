package edu.example.application.api.controller;

import edu.example.application.api.dto.ApplyImageFiltersResponse;
import edu.example.application.api.dto.GetModifiedImageByRequestIdResponse;
import edu.example.application.api.dto.UiSuccessContainer;
import edu.example.application.api.mapper.ImageFilterRequestMapper;
import edu.example.application.domain.service.ImageFilterRequestService;
import edu.example.application.domain.service.JwtService;
import edu.example.application.kafka.models.Filter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Image filters controller.
 */
@RestController
@Tag(name = "ImageController",
    description = "Базовый CRUD API для работы "
        + "с пользовательскими запросами на редактирование картинок")
@RequestMapping("/api/v1")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ImageFiltersController {
  private final JwtService jwtService;
  private final ImageFilterRequestService imageFilterRequestService;
  private final ImageFilterRequestMapper imageFilterRequestMapper;

  /**
   * Apply filters to image.
   *
   * @param imageId Image id
   * @return ApplyImageFiltersResponse Apply image filters response
   */
  @Operation(summary = "Применение указанных фильтров к изображению",
      operationId = "applyImageFilters")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успех выполнения операции",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = ApplyImageFiltersResponse.class))),
      @ApiResponse(responseCode = "404", description = "Файл не найден в системе или недоступен",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = UiSuccessContainer.class))),
      @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = UiSuccessContainer.class)))
  })
  @PostMapping(value = "/image/{image-id}/filters/apply")
  public ApplyImageFiltersResponse applyImageFilters(@PathVariable("image-id") String imageId,
                                                     @RequestParam String[] filters,
                                                     @RequestHeader(HttpHeaders.AUTHORIZATION)
                                                     String bearerToken) {
    var jwtToken = bearerToken.substring("Bearer ".length());
    var authorUsername = jwtService.getUsernameFromToken(jwtToken);
    var uuidImageId = UUID.fromString(imageId);
    var parsedFilters = Arrays.stream(filters).map(Filter::valueOf).toList();

    var responseId = imageFilterRequestService
        .applyImageFilters(uuidImageId, parsedFilters, authorUsername);
    return new ApplyImageFiltersResponse(responseId.toString());
  }

  /**
   * Get modified image ID by request ID.
   *
   * @param imageId Image id
   * @return ApplyImageFiltersResponse Apply image filters response
   */
  @Operation(summary = "Получение ИД измененного файла по ИД запроса",
      operationId = "getModifiedImageByRequestId")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успех выполнения операции",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = GetModifiedImageByRequestIdResponse.class))),
      @ApiResponse(responseCode = "404", description = "Файл не найден в системе или недоступен",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = UiSuccessContainer.class))),
      @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = UiSuccessContainer.class)))
  })
  @PostMapping(value = "/image/{image-id}/filters/{request-id}")
  public GetModifiedImageByRequestIdResponse getModifiedImageByRequestId(
      @PathVariable("image-id") String imageId,
      @PathVariable("request-id") String requestId,
      @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {
    var jwtToken = bearerToken.substring("Bearer ".length());
    var authorUsername = jwtService.getUsernameFromToken(jwtToken);

    var uuidImageId = UUID.fromString(imageId);
    var uuidRequestId = UUID.fromString(requestId);

    var imageFilterRequest = imageFilterRequestService
        .getModifiedImageByRequestId(uuidImageId, uuidRequestId, authorUsername);

    return imageFilterRequestMapper
        .imageFilterRequestEntityToGetModifiedImageByRequestIdResponse(imageFilterRequest);
  }
}
