package edu.example.filter.third.kafka;

import com.google.gson.Gson;
import edu.example.filter.third.kafka.models.Filter;
import edu.example.filter.third.kafka.models.KafkaDoneMessage;
import edu.example.filter.third.kafka.models.KafkaWipMessage;
import edu.example.filter.third.models.MultipartFileImplementation;
import edu.example.filter.third.models.RequestEntity;
import edu.example.filter.third.repository.RequestRepository;
import edu.example.filter.third.service.MinioService;
import edu.example.filter.third.service.ThirdFilterService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer.
 */
@Component
@RequiredArgsConstructor
public class KafkaConsumer {
  private final Gson gson = new Gson();
  private final ThirdFilterService thirdFilterService;
  private final MinioService minioService;
  private final KafkaProducer kafkaProducer;
  private final RequestRepository requestRepository;

  /**
   * Kafka listener.
   *
   * @param record         ConsumerRecord
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

    if (result.getFilters().get(0) != Filter.GRAYSCALE) {
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
          result.getContentType());
      var imageEntity = minioService.uploadImage(multipartFile);

      var doneMessage = new KafkaDoneMessage(result.getImageId(),
          result.getRequestId(),
          result.getAuthorUsername(),
          imageEntity.getLink(),
          result.getImageName(),
          result.getContentType());
      kafkaProducer.writeDone(doneMessage);
    } else {
      var multipartFile = new MultipartFileImplementation(filteredImage,
          result.getImageName(),
          result.getContentType());
      var imageEntity = minioService.uploadImage(multipartFile);

      var remainedFilters = result.getFilters().stream().skip(1).toList();
      var wipMessage = new KafkaWipMessage(result.getImageId(),
          result.getRequestId(),
          result.getAuthorUsername(),
          imageEntity.getLink(),
          result.getImageName(),
          result.getContentType(),
          remainedFilters);
      kafkaProducer.writeWip(wipMessage);
    }

    acknowledgment.acknowledge();
  }
}