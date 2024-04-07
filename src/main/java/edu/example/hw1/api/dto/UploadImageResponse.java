package edu.example.hw1.api.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data transfer object for uploading image.
 */
@Data
@AllArgsConstructor
public class UploadImageResponse implements Serializable {
  private String imageId;
}
