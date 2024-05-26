package edu.example.hw1.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.example.hw1.domain.utils.Status;
import edu.example.hw1.kafka.models.KafkaDoneMessage;
import edu.example.hw1.kafka.models.KafkaWipMessage;
import edu.example.hw1.repository.ImageFilterRequestRepository;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FilterConsumer {
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final ImageFilterRequestRepository imageFilterRequestRepository;

  /**
   * Filter listener.
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
      throws JsonProcessingException {
    var result = objectMapper.readValue(record.value(), KafkaWipMessage.class);

    var filterImageInfo = imageFilterRequestRepository.findById(result.getRequestId()).orElse(null);

    if (filterImageInfo != null) {
      filterImageInfo.setEditedImageId(result.getImageId());
      filterImageInfo.setStatus(Status.DONE);
      imageFilterRequestRepository.save(filterImageInfo);
    }

    acknowledgment.acknowledge();
  }
}
