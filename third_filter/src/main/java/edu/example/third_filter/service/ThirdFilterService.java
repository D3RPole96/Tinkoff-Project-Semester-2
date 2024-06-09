package edu.example.third_filter.service;

import java.io.IOException;

/**
 * Interface of third filter service.
 */
public interface ThirdFilterService {
  byte[] apply(byte[] imageBytes) throws IOException;
}
