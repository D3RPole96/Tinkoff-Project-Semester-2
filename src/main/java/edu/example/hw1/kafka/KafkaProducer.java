package edu.example.hw1.kafka;

import com.google.gson.Gson;
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
  private final Gson gson = new Gson();

  /**
   * KafkaProducer constructor.
   *
   * @param template  Kafka template
   * @param wipTopic  Kafka wip topic name
   * @param doneTopic Kafka done topic name (will be removed in the next hw)
   */
  public KafkaProducer(KafkaTemplate<String, String> template,
                       @Value("${app.wip-topic}") String wipTopic,
                       @Value("${app.done-topic}") String doneTopic) {
    this.template = template;
    this.wipTopic = wipTopic;
    this.doneTopic = doneTopic;
  }

  /**
   * Kafka writer.
   *
   * @param kafkaWipMessage Message to write
   */
  public void write(KafkaWipMessage kafkaWipMessage) {
    var kafkaWipMessageJson = gson.toJson(kafkaWipMessage);
    template.send(wipTopic, kafkaWipMessageJson);

    // заглушка, пока нету обработчиков, эта штука будет выпилена в след. дз
    var kafkaDoneMessage = new KafkaDoneMessage(UUID.randomUUID(), kafkaWipMessage.getRequestId());
    var kafkaDoneMessageJson = gson.toJson(kafkaDoneMessage);
    template.send(doneTopic, kafkaDoneMessageJson);
  }
}
