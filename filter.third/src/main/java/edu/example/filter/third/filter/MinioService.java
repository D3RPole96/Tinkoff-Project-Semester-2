package edu.example.filter.third.filter;

import edu.example.filter.third.models.ImageDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * Interface of MinIO S3 storage service.
 */
public interface MinioService {
  ImageDto uploadImage(MultipartFile file) throws Exception;

  byte[] downloadImage(String link) throws Exception;

  void deleteImage(String link) throws Exception;
}