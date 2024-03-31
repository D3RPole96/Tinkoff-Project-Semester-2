package edu.example.hw1.api.exceptionHandlers;

import edu.example.hw1.api.dto.UiSuccessContainer;
import edu.example.hw1.api.exceptions.BadRequestException;
import edu.example.hw1.api.exceptions.EntityNotFoundException;
import edu.example.hw1.api.exceptions.FileAccessException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("errors",
                ex.getBindingResult().getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .filter(Objects::nonNull)
                        .filter(s -> !s.isBlank())
                        .toList());
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<? extends UiSuccessContainer> handleEntityNotFoundException(EntityNotFoundException entityNotFoundException) {
        return handleException(entityNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<? extends UiSuccessContainer> handleDataIntegrityViolationException(DataIntegrityViolationException dataIntegrityViolationException) {
        return handleException(dataIntegrityViolationException.getCause().getLocalizedMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<? extends UiSuccessContainer> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        return handleException(illegalArgumentException.getCause().getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessException.class)
    public ResponseEntity<? extends UiSuccessContainer> handleAccessException(AccessException exception) {
        return handleException("Доступ запрещен: " + exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<? extends UiSuccessContainer> handleBadRequestException(BadRequestException exception) {
        return handleException("Bad Request: " + exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<? extends UiSuccessContainer> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception) {
        return handleException("File upload failed: File too large!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileAccessException.class)
    public ResponseEntity<? extends UiSuccessContainer> handleFileAccessException(FileAccessException fileAccessException) {
        return handleException(fileAccessException.getMessage(), HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<? extends UiSuccessContainer> handleException(String exceptionMessage, HttpStatusCode status) {
        var body = new UiSuccessContainer(false, exceptionMessage);
        return new ResponseEntity<>(body, status);
    }
}
