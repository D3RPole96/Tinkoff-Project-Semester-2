package edu.example.hw1.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.example.hw1.domain.service.ImageService;
import edu.example.hw1.domain.utils.Status;
import edu.example.hw1.kafka.models.KafkaDoneMessage;
import edu.example.hw1.kafka.models.KafkaWipMessage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class Filter1 implements Filter {
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final KafkaTemplate<String, String> template;
  private final String doneTopic;
  private final ImageService imageService;

  public Filter1(ImageService imageService,
                 KafkaTemplate<String, String> template,
                 @Value("${app.done-topic") String doneTopic) {
    this.imageService = imageService;
    this.template = template;
    this.doneTopic = doneTopic;
  }

  @Override
  public void write(KafkaDoneMessage kafkaDoneMessage) throws JsonProcessingException {
    var kafkaDoneMessageJson = objectMapper.writeValueAsString(kafkaDoneMessage);
    template.send(doneTopic, kafkaDoneMessageJson);
  }

  @KafkaListener(
      topics = "${app.wip-topic}",
      groupId = "${app.wip-group-id-1}",
      concurrency = "${app.wip-partitions}",
      properties = {
          ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG + "=false",
          ConsumerConfig.ISOLATION_LEVEL_CONFIG + "=read_committed",
          ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG
              + "=org.apache.kafka.clients.consumer.RoundRobinAssignor"
      }
  )
  @Override
  public void consume(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) throws Exception {
    var result = objectMapper.readValue(record.value(), KafkaWipMessage.class);
    var image = imageService.downloadImageWithoutAuthentication(result.getImageId());
    acknowledgment.acknowledge();

    apply(image);
  }

  @Override
  public byte[] apply(byte[] bytes) {
    return new byte[0];
  }
}
