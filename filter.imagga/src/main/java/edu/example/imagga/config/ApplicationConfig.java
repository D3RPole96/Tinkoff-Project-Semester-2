package edu.example.imagga.config;

import io.github.bucket4j.Bucket;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Config for Spring Boot application.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
  @Bean
  public Bucket createBucket() {
    return Bucket.builder()
        .addLimit(limit -> limit.capacity(10).refillGreedy(10, Duration.ofMinutes(1)))
        .build();
  }
}

