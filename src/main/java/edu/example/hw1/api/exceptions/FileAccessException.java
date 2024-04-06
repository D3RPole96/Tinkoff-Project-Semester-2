package edu.example.hw1.api.exceptions;

public class FileAccessException extends RuntimeException {
  public FileAccessException(String message) {
    super(message);
  }
}
