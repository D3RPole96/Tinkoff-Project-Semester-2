package edu.example.first.filter.kafka;

import com.google.gson.Gson;
import edu.example.first.filter.kafka.models.KafkaDoneMessage;
import edu.example.first.filter.kafka.models.KafkaWipMessage;
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
   * @param doneTopic Kafka done topic name
   */
  public KafkaProducer(KafkaTemplate<String, String> template,
                       @Value("${app.wip-topic}") String wipTopic,
                       @Value("${app.done-topic}") String doneTopic) {
    this.template = template;
    this.wipTopic = wipTopic;
    this.doneTopic = doneTopic;
  }

  /**
   * Kafka wip topic writer.
   *
   * @param kafkaWipMessage Message to write
   */
  public void writeWip(KafkaWipMessage kafkaWipMessage) {
    var kafkaWipMessageJson = gson.toJson(kafkaWipMessage);
    template.send(wipTopic, kafkaWipMessageJson);
  }

  /**
   * Kafka done topic writer.
   *
   * @param kafkaDoneMessage Message to write
   */
  public void writeDone(KafkaDoneMessage kafkaDoneMessage) {
    var kafkaWipMessageJson = gson.toJson(kafkaDoneMessage);
    template.send(doneTopic, kafkaWipMessageJson);
  }
}
