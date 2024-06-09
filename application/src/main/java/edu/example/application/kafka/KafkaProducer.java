package edu.example.application.kafka;

import com.google.gson.Gson;
import edu.example.common.components.kafka.models.KafkaWipMessage;
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
  private final Gson gson = new Gson();

  /**
   * KafkaProducer constructor.
   *
   * @param template  Kafka template
   * @param wipTopic  Kafka wip topic name
   */
  public KafkaProducer(KafkaTemplate<String, String> template,
                       @Value("${app.wip-topic}") String wipTopic) {
    this.template = template;
    this.wipTopic = wipTopic;
  }

  /**
   * Kafka writer.
   *
   * @param kafkaWipMessage Message to write
   */
  public void write(KafkaWipMessage kafkaWipMessage) {
    var kafkaWipMessageJson = gson.toJson(kafkaWipMessage);
    template.send(wipTopic, kafkaWipMessageJson);
  }
}
