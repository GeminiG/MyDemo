package com.example.demo.kafkademo.producer;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class KafkaProducerConfig {

    @Value("${ftf.kafka.properties.server}")
    private String server;

    @Value("${ftf.kafka.properties.retires}")
    private int retires;

    @Value("${ftf.kafka.properties.batch.size}")
    private int batchSize;

    @Value("${ftf.kafka.properties.linger}")
    private int linger;

    @Value("${ftf.kafka.properties.buffer.memory}")
    private int bufferMemory;

    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        props.put(ProducerConfig.RETRIES_CONFIG, retires);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        props.put(ProducerConfig.LINGER_MS_CONFIG, linger);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return props;
    }

    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, String> producerKafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        return new KafkaAdmin(props);
    }

    @Bean
    public AdminClient adminClient() {
        return AdminClient.create(kafkaAdmin().getConfig());
    }

}
