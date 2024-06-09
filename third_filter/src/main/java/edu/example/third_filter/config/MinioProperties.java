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
public class MinioProperties extends edu.example.common.components.minio.config.MinioProperties {
}
