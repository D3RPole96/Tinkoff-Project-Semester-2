package edu.example.third_filter.kafka;

import com.google.gson.Gson;
import edu.example.common.components.kafka.models.KafkaDoneMessage;
import edu.example.common.components.kafka.models.KafkaWipMessage;
import edu.example.common.components.minio.MinioService;
import edu.example.common.components.minio.models.MultipartFileImplementation;
import edu.example.third_filter.config.MinioProperties;
import edu.example.third_filter.models.RequestEntity;
import edu.example.third_filter.repository.RequestRepository;
import edu.example.third_filter.service.ThirdFilterService;
import io.minio.MinioClient;
import java.util.UUID;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import static edu.example.common.components.kafka.models.Filter.GRAYSCALE;

/**
 * Kafka consumer.
 */
@Component
public class KafkaConsumer {
  private final Gson gson;
  private final ThirdFilterService thirdFilterService;
  private final MinioService minioService;
  private final KafkaProducer kafkaProducer;
  private final RequestRepository requestRepository;

  /**
   * Constructor of Kafka consumer.
   */
  public KafkaConsumer(ThirdFilterService thirdFilterService,
                       KafkaProducer kafkaProducer,
                       RequestRepository requestRepository,
                       MinioProperties minioProperties,
                       MinioClient minioClient) {
    gson = new Gson();
    this.thirdFilterService = thirdFilterService;
    this.kafkaProducer = kafkaProducer;
    this.requestRepository = requestRepository;
    minioService = new MinioService(minioClient, minioProperties);
  }

  /**
   * Kafka listener.
   *
   * @param record ConsumerRecord
   * @param acknowledgment Acknowledgment
   */
  @KafkaListener(
      topics = "${app.wip-topic}",
      groupId = "${app.group-id}",
      concurrency = "${app.wip-partitions}",
      properties = {
          ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG + "=false",
          ConsumerConfig.ISOLATION_LEVEL_CONFIG + "=read_committed",
          ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG
              + "=org.apache.kafka.clients.consumer.RoundRobinAssignor"
      }
  )
  public void consume(ConsumerRecord<String, String> record, Acknowledgment acknowledgment)
      throws Exception {
    var result = gson.fromJson(record.value(), KafkaWipMessage.class);

    if (result.getFilters().get(0) != GRAYSCALE) {
      acknowledgment.acknowledge();
      return;
    }

    var request = new RequestEntity(UUID.randomUUID(),
        result.getImageId(),
        result.getRequestId());
    requestRepository.save(request);

    var image = minioService.downloadImage(result.getLink());
    var filteredImage = thirdFilterService.apply(image);

    if (result.getFilters().size() == 1) {
      var multipartFile = new MultipartFileImplementation(filteredImage,
          result.getImageName() + " filtered",
          null);
      var imageEntity = minioService.uploadImage(multipartFile);

      var doneMessage = new KafkaDoneMessage(result.getImageId(),
          result.getRequestId(),
          result.getAuthorUsername(),
          imageEntity.getLink(),
          result.getImageName());
      kafkaProducer.writeDone(doneMessage);
    }
    else {
      var multipartFile = new MultipartFileImplementation(filteredImage,
          result.getImageName(),
          null);
      var imageEntity = minioService.uploadImage(multipartFile);

      var remainedFilters = result.getFilters().stream().skip(1).toList();
      var wipMessage = new KafkaWipMessage(result.getImageId(),
          result.getRequestId(),
          result.getAuthorUsername(),
          imageEntity.getLink(),
          result.getImageName(),
          remainedFilters);
      kafkaProducer.writeWip(wipMessage);
    }

    acknowledgment.acknowledge();
  }
}
