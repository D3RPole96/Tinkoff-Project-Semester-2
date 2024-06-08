package edu.example.hw1.kafka;

import com.google.gson.Gson;
import edu.example.hw1.domain.utils.Status;
import edu.example.hw1.kafka.models.KafkaDoneMessage;
import edu.example.hw1.repository.ImageFilterRequestRepository;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer.
 */
@Component
@AllArgsConstructor
public class KafkaConsumer {
  private final Gson gson = new Gson();
  private final ImageFilterRequestRepository imageFilterRequestRepository;

  /**
   * Kafka listener.
   *
   * @param record ConsumerRecord
   * @param acknowledgment Acknowledgment
   */
  @KafkaListener(
      topics = "${app.done-topic}",
      groupId = "${app.group-id}",
      concurrency = "${app.partitions}",
      properties = {
          ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG + "=false",
          ConsumerConfig.ISOLATION_LEVEL_CONFIG + "=read_committed",
          ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG
              + "=org.apache.kafka.clients.consumer.RoundRobinAssignor"
      }
  )
  public void consume(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
    var result = gson.fromJson(record.value(), KafkaDoneMessage.class);

    var filterImageInfo = imageFilterRequestRepository.findById(result.getRequestId()).orElse(null);

    if (filterImageInfo != null) {
      filterImageInfo.setEditedImageId(result.getImageId());
      filterImageInfo.setStatus(Status.DONE);
      imageFilterRequestRepository.save(filterImageInfo);
    }

    acknowledgment.acknowledge();
  }
}
