package edu.example.first.filter.models;

import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Image Entity data transfer object.
 */
@Data
@Accessors(chain = true)
public class ImageDto {
  private UUID id;
  private String name;
  private Long size;
  private String link;
  String contentType;
}
