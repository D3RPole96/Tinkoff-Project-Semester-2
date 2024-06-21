package edu.example.first.filter.filter;

import java.io.IOException;

/**
 * Interface of first filter service.
 */
public interface FirstFilterService {
  byte[] apply(byte[] imageBytes, String contentType) throws IOException;
}
