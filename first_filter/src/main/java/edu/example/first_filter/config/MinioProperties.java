package edu.example.first_filter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * MinIO properties for MinIO config and MinIO service.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "ff-minio")
public class MinioProperties extends edu.example.common.components.minio.config.MinioProperties {
}
