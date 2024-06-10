package edu.example.filter.second.service;

import edu.example.filter.second.config.MinioProperties;
import edu.example.filter.second.models.ImageDto;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import java.io.ByteArrayInputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Implementation of MinIO S3 storage service.
 */
@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {
  private final MinioClient client;
  private final MinioProperties properties;

  /**
   * Upload image to S3.
   */
  public ImageDto uploadImage(MultipartFile file) throws Exception {
    var fileId = UUID.randomUUID().toString();

    var inputStream = new ByteArrayInputStream(file.getBytes());
    client.putObject(
        PutObjectArgs
            .builder()
            .bucket(properties.getBucket())
            .object(fileId)
            .stream(inputStream, file.getSize(), properties.getImageSize())
            .contentType(file.getContentType())
            .build()
    );

    return new ImageDto()
        .setName(file.getOriginalFilename())
        .setSize(file.getSize())
        .setLink(fileId);
  }

  /**
   * Download image by link from S3.
   */
  public byte[] downloadImage(String link) throws Exception {
    return IOUtils.toByteArray(client.getObject(
        GetObjectArgs.builder()
            .bucket(properties.getBucket())
            .object(link)
            .build()));
  }

  /**
   * Delete image by link from S3.
   */
  public void deleteImage(String link) throws Exception {
    client.removeObject(
        RemoveObjectArgs.builder()
            .bucket(properties.getBucket())
            .object(link)
            .build());
  }
}
