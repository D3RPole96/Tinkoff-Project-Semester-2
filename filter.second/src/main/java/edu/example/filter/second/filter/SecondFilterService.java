package edu.example.filter.second.filter;

import java.io.IOException;

/**
 * Interface of second filter service.
 */
public interface SecondFilterService {
  byte[] apply(byte[] imageBytes) throws IOException;
}
