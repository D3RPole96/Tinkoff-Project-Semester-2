package edu.example.hw1.api.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
