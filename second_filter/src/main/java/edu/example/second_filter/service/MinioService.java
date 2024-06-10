package edu.example.second_filter.service;

import edu.example.second_filter.models.ImageDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * Interface of MinIO S3 storage service.
 */
public interface MinioService {
  ImageDto uploadImage(MultipartFile file) throws Exception;

  byte[] downloadImage(String link) throws Exception;

  void deleteImage(String link) throws Exception;
}
