package edu.example.application.api.dto;

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
