package edu.example.hw1.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

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
