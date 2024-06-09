package edu.example.third_filter.config;

import io.minio.MinioClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Config for MinIO S3 storage.
 */
@Configuration
@EnableConfigurationProperties(MinioProperties.class)
public class MinioConfig {
  /**
   * Minio client bean for working with MinIO S3 storage.
   *
   * @param properties MinIO properties
   * @return MinioClient
   */
  @Bean
  public MinioClient minioClient(MinioProperties properties) {
    return MinioClient.builder()
        .credentials(properties.getAccessKey(), properties.getSecretKey())
        .endpoint(properties.getUrl(), properties.getPort(), properties.isSecure())
        .build();
  }
}
