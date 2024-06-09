package edu.example.common.components.minio.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;

/**
 * MinIO properties for MinIO config and MinIO service.
 */
@Getter
@Setter
public class MinioProperties {
  private String url;
  private int port;
  private String accessKey;
  private String secretKey;
  private boolean secure;
  private String bucket;
  private long imageSize;
}
