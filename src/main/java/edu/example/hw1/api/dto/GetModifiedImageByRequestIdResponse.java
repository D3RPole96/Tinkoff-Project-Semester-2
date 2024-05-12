package edu.example.hw1.api.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data transfer object for getting modified image by request ID.
 */
@Data
@AllArgsConstructor
public class GetModifiedImageByRequestIdResponse implements Serializable {
  String imageId;
  String status;
}
