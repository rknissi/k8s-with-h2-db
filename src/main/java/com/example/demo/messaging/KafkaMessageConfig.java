package com.example.demo.messaging;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerPartitionAssignor;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@EnableKafka
@Configuration
@Profile("!test")
public class KafkaMessageConfig {
    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    //@Value(value = "${kafka.groupId}")
    //private String groupid;
    private String groupid = String.valueOf(new Random().nextInt(50));

    @Value(value = "${schemaRegistry.bootstrapAddress}")
    private String schemaRegistryAddress;
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        //From which group the consumer is. If it's the same group, they will divide the consumer job
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                groupid);
        //If it will consume all messages from the beggining
        props.put(
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                "earliest");
        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                KafkaAvroDeserializer.class);
        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                KafkaAvroDeserializer.class);
        props.put("schema.registry.url", schemaRegistryAddress);
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");

        //If you want manual commits
        //props.put("enable.auto.commit", false);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String>
    kafkaListenerContainerFactory(KafkaOperations<String, ConsumerPartitionAssignor.Assignment> KafkaTemplate) {

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(KafkaTemplate,
                (r, e) ->
                        new TopicPartition(r.topic() + ".DLQ", 1)
        );

        DefaultErrorHandler defaultErrorHandler = new DefaultErrorHandler(new FixedBackOff(1000L, 3L));

        factory.setCommonErrorHandler(defaultErrorHandler);

        return factory;
    }
}
