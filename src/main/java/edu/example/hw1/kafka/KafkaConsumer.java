package edu.example.hw1.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class KafkaConsumer {
  @KafkaListener(
      topics = "${app.done-topic}",
      groupId = "${app.group-id}",
      concurrency = "${app.partitions}",
      properties = {
          ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG + "=false",
          //                    ConsumerConfig.ISOLATION_LEVEL_CONFIG + "=read_uncommitted",
          ConsumerConfig.ISOLATION_LEVEL_CONFIG + "=read_committed",
          ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG +
              "=org.apache.kafka.clients.consumer.RoundRobinAssignor"
      }
  )
  public void consume(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
    log.info("""
                 Получено следующее сообщение из топика {}:
                 key: {},
                 value: {}
                 """, record.topic(), record.key(), record.value());
    acknowledgment.acknowledge();
  }
}
