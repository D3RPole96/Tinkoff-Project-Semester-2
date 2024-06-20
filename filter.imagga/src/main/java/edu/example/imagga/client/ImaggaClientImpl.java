package edu.example.imagga.client;

import static java.lang.Math.min;

import io.github.bucket4j.Bucket;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import jakarta.xml.ws.http.HTTPException;
import java.time.Duration;
import java.util.Base64;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

/**
 * Implementation of Imagga client.
 */
@Component
public class ImaggaClientImpl implements ImaggaClient {
  private final String apiKey;
  private final String secret;
  private final int topTagsCount;
  private final Bucket bucket;
  private final CircuitBreaker circuitBreaker;
  private final RateLimiter rateLimiter;
  private final Retry retry;

  /**
   * Constructor of Imagga client.
   */
  public ImaggaClientImpl(Bucket bucket,
                          @Value("${imagga.secrets.api-key}") String apiKey,
                          @Value("${imagga.secrets.secret}") String secret,
                          @Value("${imagga.top-tags}") String topTags) {
    this.apiKey = apiKey;
    this.secret = secret;
    this.topTagsCount = Integer.parseInt(topTags);
    this.bucket = bucket;

    var circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();
    this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("imagga");

    var rateLimiterConfig = RateLimiterConfig.custom()
        .limitRefreshPeriod(Duration.ofSeconds(10))
        .limitForPeriod(10)
        .timeoutDuration(Duration.ofSeconds(20))
        .build();
    var rateLimiterRegistry = RateLimiterRegistry.of(rateLimiterConfig);
    this.rateLimiter = rateLimiterRegistry.rateLimiter("imagga");

    var retryConfig = RetryConfig.custom()
        .waitDuration(Duration.ofSeconds(3))
        .maxAttempts(5)
        .retryOnException(this::isExceptionSuitableForRetry)
        .build();
    var retryRegistry = RetryRegistry.of(retryConfig);
    this.retry = retryRegistry.retry("imagga");
  }

  private boolean isExceptionSuitableForRetry(Throwable throwable) {
    if (throwable.getCause() instanceof HTTPException) {
      int statusCode = ((HTTPException) throwable.getCause()).getStatusCode();
      return statusCode == 429 || (statusCode >= 500 && statusCode <= 599);
    }
    return false;
  }

  @Override
  public String uploadImage(byte[] image) {
    var isConsumed = bucket.tryConsume(1);
    if (!isConsumed) {
      throw new RuntimeException();
    }

    return Decorators.ofSupplier(() -> uploadImageInternal(image))
        .withCircuitBreaker(circuitBreaker)
        .withRateLimiter(rateLimiter)
        .withRetry(retry)
        .get();
  }

  @Override
  public String[] getTopTags(String uploadId) {
    var isConsumed = bucket.tryConsume(1);
    if (!isConsumed) {
      throw new RuntimeException();
    }

    return Decorators.ofSupplier(() -> getTopTagsInternal(uploadId))
        .withCircuitBreaker(circuitBreaker)
        .withRateLimiter(rateLimiter)
        .withRetry(retry)
        .get();
  }

  private String uploadImageInternal(byte[] image) {
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("image", image);

    var httpResponse = RestClient.builder()
        .defaultHeader("Authorization",
            "Basic " + Base64.getEncoder().encodeToString((apiKey + ":" + secret).getBytes()))
        .build()
        .post()
        .uri("https://api.imagga.com/v2/uploads")
        .body(body)
        .retrieve()
        .toEntity(String.class);
    if (!httpResponse.getStatusCode().is2xxSuccessful()) {
      throw new HTTPException(httpResponse.getStatusCode().value());
    }

    var jsonObject = new JSONObject(httpResponse.getBody());

    if (jsonObject.has("result")) {
      return jsonObject.getJSONObject("result").getString("upload_id");
    }

    return null;
  }

  private String[] getTopTagsInternal(String uploadId) {
    var httpResponse = RestClient.builder()
        .defaultHeader("Authorization",
            "Basic " + Base64.getEncoder().encodeToString((apiKey + ":" + secret).getBytes()))
        .build()
        .get()
        .uri("https://api.imagga.com/v2/tags?image_upload_id=" + uploadId)
        .retrieve()
        .toEntity(String.class);
    if (!httpResponse.getStatusCode().is2xxSuccessful()) {
      throw new HTTPException(httpResponse.getStatusCode().value());
    }

    var jsonObject = new JSONObject(httpResponse.getBody());

    if (jsonObject.has("result")) {
      var tags = jsonObject.getJSONObject("result").getJSONArray("tags");

      var topTags = new String[topTagsCount];
      for (var i = 0; i < min(topTagsCount, tags.length()); i++) {
        topTags[i] = tags.getJSONObject(i).getJSONObject("tag").getString("en");
      }

      return topTags;
    }

    return null;
  }
}
