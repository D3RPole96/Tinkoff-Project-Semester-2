package edu.example.hw1.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data transfer object for GetImagesResponse.
 */
@Data
@AllArgsConstructor
public class Image {
  String imageId;
  String filename;
  long size;
}
