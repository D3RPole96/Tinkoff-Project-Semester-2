package edu.example.hw1.api.exceptions;

/**
 * Entity not found exception (404).
 */
public class EntityNotFoundException extends RuntimeException {
  public EntityNotFoundException(String errorMessage) {
    super(errorMessage);
  }
}
