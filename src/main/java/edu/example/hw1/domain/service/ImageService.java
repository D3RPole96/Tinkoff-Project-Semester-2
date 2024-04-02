package edu.example.hw1.domain.service;

import edu.example.hw1.domain.entity.ImageEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ImageService {
    byte[] downloadImage(UUID imageId, String authorUsername) throws Exception;
    ImageEntity uploadImageToUser(MultipartFile file, String authorUsername) throws Exception;
    void deleteImage(UUID imageId, String authorUsername) throws Exception;
    List<ImageEntity> getUserImages(String username);
}