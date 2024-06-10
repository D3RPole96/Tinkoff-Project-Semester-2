package edu.example.application.kafka;

import com.google.gson.Gson;
import edu.example.application.domain.entity.MultipartFileImplementation;
import edu.example.application.domain.service.ImageService;
import edu.example.application.domain.service.MinioService;
import edu.example.application.domain.utils.Status;
import edu.example.application.kafka.models.KafkaDoneMessage;
import edu.example.application.repository.ImageFilterRequestRepository;
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
  private final ImageFilterRequestRepository imageFilterRequestRepository;
  private final ImageService imageService;
  private final MinioService minioService;

  /**
   * Kafka listener.
   *
   * @param record         ConsumerRecord
   * @param acknowledgment Acknowledgment
   */
  @KafkaListener(
      topics = "${app.done-topic}",
      groupId = "${app.group-id}",
      concurrency = "${app.done-partitions}",
      properties = {
          ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG + "=false",
          ConsumerConfig.ISOLATION_LEVEL_CONFIG + "=read_committed",
          ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG
              + "=org.apache.kafka.clients.consumer.RoundRobinAssignor"
      }
  )
  public void consume(ConsumerRecord<String, String> record, Acknowledgment acknowledgment)
      throws Exception {
    var result = gson.fromJson(record.value(), KafkaDoneMessage.class);

    var image = minioService.downloadImage(result.getLink());

    var multipartFile = new MultipartFileImplementation(image,
        result.getImageName(),
        null);
    var imageEntity = imageService.uploadImageToUser(multipartFile, result.getAuthorUsername());

    var filterImageInfo = imageFilterRequestRepository.findById(result.getRequestId()).orElse(null);
    if (filterImageInfo != null) {
      filterImageInfo.setEditedImageId(imageEntity.getId());
      filterImageInfo.setStatus(Status.DONE);
      imageFilterRequestRepository.save(filterImageInfo);
    }

    acknowledgment.acknowledge();
  }
}
