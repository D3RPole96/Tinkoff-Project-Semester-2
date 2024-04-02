package edu.example.hw1.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class UploadImageResponse implements Serializable {
    private String imageId;
}
