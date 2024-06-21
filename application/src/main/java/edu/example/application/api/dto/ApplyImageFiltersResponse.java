package edu.example.application.api.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data transfer object for applying filters.
 */
@Data
@AllArgsConstructor
public class ApplyImageFiltersResponse implements Serializable {
  String requestId;
}
