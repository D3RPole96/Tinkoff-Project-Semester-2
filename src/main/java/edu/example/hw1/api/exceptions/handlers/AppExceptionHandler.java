package edu.example.hw1.api.exceptions.handlers;

import edu.example.hw1.api.dto.UiSuccessContainer;
import edu.example.hw1.api.exceptions.BadRequestException;
import edu.example.hw1.api.exceptions.EntityNotFoundException;
import edu.example.hw1.api.exceptions.FileAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * Exception handler for Spring Boot application.
 * Returns UiSuccessContainer with status code.
 */
@RestControllerAdvice
public class AppExceptionHandler {
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<UiSuccessContainer> handleEntityNotFoundException(
      EntityNotFoundException entityNotFoundException) {
    return handleException(entityNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<UiSuccessContainer> handleDataIntegrityViolationException(
      DataIntegrityViolationException dataIntegrityViolationException) {
    return handleException(dataIntegrityViolationException.getCause().getLocalizedMessage(),
        HttpStatus.CONFLICT);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<UiSuccessContainer> handleIllegalArgumentException(
      IllegalArgumentException illegalArgumentException) {
    return handleException(illegalArgumentException.getCause().getLocalizedMessage(),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AccessException.class)
  public ResponseEntity<UiSuccessContainer> handleAccessException(AccessException exception) {
    return handleException("Доступ запрещен: " + exception.getMessage(), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<UiSuccessContainer> handleBadRequestException(
      BadRequestException exception) {
    return handleException("Bad Request: " + exception.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<UiSuccessContainer> handleMaxUploadSizeExceededException() {
    return handleException("Размер файла превышает 10МБ", HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(FileAccessException.class)
  public ResponseEntity<UiSuccessContainer> handleFileAccessException(
      FileAccessException fileAccessException) {
    return handleException(fileAccessException.getMessage(), HttpStatus.NOT_FOUND);
  }

  private ResponseEntity<UiSuccessContainer> handleException(String exceptionMessage,
                                                             HttpStatusCode status) {
    var body = new UiSuccessContainer(false, exceptionMessage);
    return new ResponseEntity<>(body, status);
  }
}
