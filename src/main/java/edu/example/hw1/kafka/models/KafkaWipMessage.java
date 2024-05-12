package edu.example.hw1.kafka.models;

import edu.example.hw1.domain.utils.Filter;
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
public class KafkaWipMessage {
  UUID imageId;
  UUID requestId;
  List<Filter> filters;
}
