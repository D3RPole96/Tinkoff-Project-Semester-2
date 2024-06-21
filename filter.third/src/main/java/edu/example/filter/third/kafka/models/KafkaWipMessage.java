package edu.example.filter.third.kafka.models;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Kafka wip message.
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class KafkaWipMessage implements Serializable {
  UUID imageId;
  UUID requestId;
  String authorUsername;
  String link;
  String imageName;
  String contentType;
  List<Filter> filters;
}
