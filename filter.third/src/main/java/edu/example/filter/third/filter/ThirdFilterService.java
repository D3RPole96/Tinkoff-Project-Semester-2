package edu.example.filter.third.filter;

import java.io.IOException;

/**
 * Interface of third filter service.
 */
public interface ThirdFilterService {
  byte[] apply(byte[] imageBytes) throws IOException;
}
