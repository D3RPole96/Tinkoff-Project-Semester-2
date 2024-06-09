package edu.example.application.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data transfer object for errors or success results without response body.
 */
@Data
@AllArgsConstructor
public class UiSuccessContainer {
  boolean success;
  String message;

  public UiSuccessContainer() {
    success = true;
    message = "Операция выполнена успешно";
  }
}
