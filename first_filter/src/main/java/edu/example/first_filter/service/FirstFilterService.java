package edu.example.first_filter.service;

import java.io.IOException;

/**
 * Interface of first filter service.
 */
public interface FirstFilterService {
  byte[] apply(byte[] imageBytes) throws IOException;
}
