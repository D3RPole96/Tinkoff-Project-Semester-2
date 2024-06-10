package edu.example.first.filter.service;

import java.io.IOException;

/**
 * Interface of first filter service.
 */
public interface FirstFilterService {
  byte[] apply(byte[] imageBytes) throws IOException;
}
