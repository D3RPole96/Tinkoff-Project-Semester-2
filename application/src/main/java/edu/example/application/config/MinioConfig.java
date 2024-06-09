package edu.example.application.config;

import io.minio.MinioClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MinIO config.
 */
@Configuration
@EnableConfigurationProperties(MinioProperties.class)
public class MinioConfig {
  @Bean
  MinioClient getMinioClient(MinioProperties properties) {
    return MinioClient.builder()
        .credentials(properties.getAccessKey(), properties.getSecretKey())
        .endpoint(properties.getUrl(), properties.getPort(), properties.isSecure())
        .build();
  }
}
