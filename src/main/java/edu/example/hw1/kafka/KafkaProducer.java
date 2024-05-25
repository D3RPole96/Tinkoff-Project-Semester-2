package edu.example.hw1.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.example.hw1.kafka.models.KafkaDoneMessage;
import edu.example.hw1.kafka.models.KafkaWipMessage;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka producer.
 */
@Component
public class KafkaProducer {
  private final KafkaTemplate<String, String> template;
  private final String wipTopic;
  private final String doneTopic;
  private final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * KafkaProducer constructor.
   *
   * @param template  Kafka template
   * @param wipTopic  Kafka wip topic name
   * @param doneTopic Kafka done topic name (will be removed in the next hw)
   */
  public KafkaProducer(KafkaTemplate<String, String> template,
                       @Value("${app.wip-topic}") String wipTopic,
                       @Value("${app.done-topic") String doneTopic) {
    this.template = template;
    this.wipTopic = wipTopic;
    this.doneTopic = doneTopic;
  }

  /**
   * Kafka writer.
   *
   * @param kafkaWipMessage Message to write
   */
  public void write(KafkaWipMessage kafkaWipMessage) throws JsonProcessingException {
    var kafkaWipMessageJson = objectMapper.writeValueAsString(kafkaWipMessage);
    template.send(wipTopic, kafkaWipMessageJson);

    // заглушка, пока нету обработчиков, эта штука будет выпилена в след. дз
    var kafkaDoneMessage = new KafkaDoneMessage(UUID.randomUUID(), kafkaWipMessage.getRequestId());
    var kafkaDoneMessageJson = objectMapper.writeValueAsString(kafkaDoneMessage);
    template.send(doneTopic, kafkaDoneMessageJson);
  }
}
