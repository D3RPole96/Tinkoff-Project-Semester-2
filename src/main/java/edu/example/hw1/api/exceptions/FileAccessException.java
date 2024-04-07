package edu.example.hw1.api.exceptions;

/**
 * File access exception (403).
 */
public class FileAccessException extends RuntimeException {
  public FileAccessException(String message) {
    super(message);
  }
}
