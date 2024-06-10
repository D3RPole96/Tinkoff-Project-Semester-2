package edu.example.third_filter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * MinIO properties for MinIO config and MinIO service.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "tf-minio")
public class MinioProperties {
  private String url;
  private int port;
  private String accessKey;
  private String secretKey;
  private boolean secure;
  private String bucket;
  private long imageSize;
}
