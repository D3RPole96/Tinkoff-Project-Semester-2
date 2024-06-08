package edu.example.hw1.kafka.models;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Kafka done message.
 */
@Data
@AllArgsConstructor
public class KafkaDoneMessage implements Serializable {
  UUID imageId;
  UUID requestId;
}
