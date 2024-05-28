package edu.example.hw1.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.example.hw1.kafka.models.KafkaDoneMessage;
import java.util.function.UnaryOperator;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.web.multipart.MultipartFile;

public interface Filter extends UnaryOperator<byte[]> {
  void write(KafkaDoneMessage kafkaDoneMessage) throws JsonProcessingException;

  void consume(ConsumerRecord<String, String> record, Acknowledgment acknowledgment)
      throws Exception;
}
