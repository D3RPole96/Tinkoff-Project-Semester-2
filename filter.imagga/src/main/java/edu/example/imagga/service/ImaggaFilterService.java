package edu.example.imagga.service;

import java.io.IOException;

/**
 * Interface of first filter service.
 */
public interface ImaggaFilterService {
  byte[] apply(byte[] imageBytes, String contentType) throws IOException;
}
