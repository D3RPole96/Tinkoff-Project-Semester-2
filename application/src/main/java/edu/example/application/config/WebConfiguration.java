package edu.example.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web Mvc configuration. Just disables CORS.
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
  /**
   * Adds CORS mappings. Just allows all requests.
   *
   * @param registry CorsRegistry
   */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**").allowedMethods("*").allowedHeaders("*").allowedOrigins("*");
  }
}
