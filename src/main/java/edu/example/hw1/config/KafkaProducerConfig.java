package edu.example.hw1.config;

import java.util.Map;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.RoundRobinPartitioner;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {
  public static final String TRANSACTIONAL_KAFKA_TEMPLATE = "transactionalKafkaTemplate";
  public static final String SINGLE_ACKS_KAFKA_TEMPLATE = "singleAcksKafkaTemplate";
  public static final String ZERO_ACKS_KAFKA_TEMPLATE = "zeroAcksKafkaTemplate";
  public static final String ALL_ACKS_KAFKA_TEMPLATE = "allAcksKafkaTemplate";

  private final KafkaProperties properties;

  @Bean
  public NewTopic topic(
      @Value("${app.wip-topic}") String topic,
      @Value("${app.partitions}") int partitions,
      @Value("${app.replicas}") short replicas) {

    return new NewTopic(topic, partitions, replicas);
  }

  @Bean(ZERO_ACKS_KAFKA_TEMPLATE)
  public KafkaTemplate<String, String> zeroAcksKafkaTemplate() {
    return new KafkaTemplate<>(producerFactory(props -> props.put(ProducerConfig.ACKS_CONFIG, "0")));
  }

  @Bean(SINGLE_ACKS_KAFKA_TEMPLATE)
  public KafkaTemplate<String, String> singleAcksKafkaTemplate() {
    return new KafkaTemplate<>(producerFactory(props -> props.put(ProducerConfig.ACKS_CONFIG, "1")));
  }

  @Bean(ALL_ACKS_KAFKA_TEMPLATE)
  public KafkaTemplate<String, String> allAcksKafkaTemplate() {
    return new KafkaTemplate<>(producerFactory(props -> props.put(ProducerConfig.ACKS_CONFIG, "all")));
  }

  @Bean(TRANSACTIONAL_KAFKA_TEMPLATE)
  public KafkaTemplate<String, String> transactionalKafkaTemplate() {
    return new KafkaTemplate<>(producerFactory(props -> {
      props.put(ProducerConfig.ACKS_CONFIG, "all");
      props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "custom-tx-id");
    }));
  }

  private ProducerFactory<String, String> producerFactory(Consumer<Map<String, Object>> enchanter) {
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

    // До-обогащаем конфигурацию
    enchanter.accept(props);

    return new DefaultKafkaProducerFactory<>(props);
  }

}
