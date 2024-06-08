package edu.example.hw1.config;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.RoundRobinPartitioner;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin.NewTopics;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * Config for Kafka.
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(KafkaProperties.class)
@EnableKafka
public class KafkaConfig {
  private final KafkaProperties properties;

  /**
   * Topics bean.
   *
   * @param wipTopic  WIP topic
   * @param doneTopic DONE topic
   * @param partitions partitions
   * @param replicas replicas count
   * @return NewTopics topics
   */
  @Bean
  public NewTopics topic(
      @Value("${app.wip-topic}") String wipTopic,
      @Value("${app.done-topic}") String doneTopic,
      @Value("${app.partitions}") int partitions,
      @Value("${app.replicas}") short replicas) {

    return new NewTopics(new NewTopic(wipTopic, partitions, replicas),
        new NewTopic(doneTopic, partitions, replicas));
  }

  /**
   * KafkaTemplate bean.
   *
   * @return KafkaTemplate Kafka template
   */
  @Bean
  public KafkaTemplate<String, String> kafkaTemplate() {
    var props = properties.buildProducerProperties(null);

    // Работаем со строками
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

    // Партиция одна, так что все равно как роутить
    props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, RoundRobinPartitioner.class);

    // Отправляем сообщения сразу
    props.put(ProducerConfig.LINGER_MS_CONFIG, 0);

    // 3 попытки на все про все
    props.put(ProducerConfig.RETRIES_CONFIG, 3);

    // 30 секунд на попытку
    props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG,
        (int) TimeUnit.SECONDS.toMillis(30));

    // ALL_ACKS_KAFKA_TEMPLATE
    props.put(ProducerConfig.ACKS_CONFIG, "all");

    var defaultKafkaProducerFactory = new DefaultKafkaProducerFactory<String, String>(props);
    return new KafkaTemplate<>(defaultKafkaProducerFactory);
  }

}
