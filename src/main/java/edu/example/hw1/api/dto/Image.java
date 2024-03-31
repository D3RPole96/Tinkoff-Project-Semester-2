package edu.example.hw1.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Image {
    String imageId;
    String filename;
    long size;
}
