package edu.example.hw1.api.exceptions;

/**
 * Bad request exception (400).
 */
public class BadRequestException extends RuntimeException {
  public BadRequestException(String message) {
    super(message);
  }
}

