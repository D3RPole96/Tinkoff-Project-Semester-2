package edu.example.hw1.api.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static edu.example.hw1.config.KafkaProducerConfig.ALL_ACKS_KAFKA_TEMPLATE;
import static edu.example.hw1.config.KafkaProducerConfig.SINGLE_ACKS_KAFKA_TEMPLATE;
import static edu.example.hw1.config.KafkaProducerConfig.TRANSACTIONAL_KAFKA_TEMPLATE;
import static edu.example.hw1.config.KafkaProducerConfig.ZERO_ACKS_KAFKA_TEMPLATE;


@Slf4j
@RestController
public class KafkaController {
  private final Map<String, KafkaTemplate<String, String>> templates;
  private final KafkaTemplate<String, String> transactionalTemplate;

  @Value("${app.wip-topic}")
  private String topic;

  public KafkaController(
      @Qualifier(ZERO_ACKS_KAFKA_TEMPLATE)
      KafkaTemplate<String, String> zeroAcksKafkaTemplate,

      @Qualifier(SINGLE_ACKS_KAFKA_TEMPLATE)
      KafkaTemplate<String, String> singleAcksKafkaTemplate,

      @Qualifier(ALL_ACKS_KAFKA_TEMPLATE)
      KafkaTemplate<String, String> allAcksKafkaTemplate,

      @Qualifier(TRANSACTIONAL_KAFKA_TEMPLATE)
      KafkaTemplate<String, String> transactionalKafkaTemplate
  ) {
    templates = Map.of(
        "0", zeroAcksKafkaTemplate,
        "1", singleAcksKafkaTemplate,
        "all", allAcksKafkaTemplate);

    transactionalTemplate = transactionalKafkaTemplate;
  }

  @PostMapping("/send")
  public String sendMessage(@RequestParam String message, @RequestParam String acks) {
    log.info("Отправляем сообщение {}, ACKS: {}", message, acks);
    templates.get(acks.toLowerCase().trim()).send(topic, message);
    return "sent";
  }

  @PostMapping("/send/tx")
  public String sendMessageInTx(
      @RequestParam String message,
      @RequestParam(required = false, defaultValue = "10") int repeat,
      @RequestParam(required = false, defaultValue = "1000") int delayMs) {

    log.info("Отправляем сообщение {} х {} в транзакции (время ожидания {} мс)", message, repeat, delayMs);
    transactionalTemplate.executeInTransaction(it -> {
      for (int i = 0; i < repeat; i++) {
        transactionalTemplate.send(topic, message);
      }
      try {
        Thread.sleep(delayMs);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      return null;
    });
    return "sent";
  }
}
