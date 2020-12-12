package com.example.demo.kafkademo.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${ftf.kafka.properties.server}")
    private String servers;

    @Value("${ftf.kafka.properties.sessionTimeoutMs}")
    private String sessionTimeout;

    @Value("${ftf.kafka.properties.group}")
    private String groupId;

    @Value("${ftf.kafka.properties.autoOffsetReset}")
    private String autoOffsetReset;



    public Map<String, Object> consumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 12000);
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);

        //propsMap.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1048576);
        //propsMap.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 10000);

        propsMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 2);

        propsMap.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 11000);

        return propsMap;
    }


    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> testKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(1);
        factory.getContainerProperties().setPollTimeout(10000);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.BATCH);
        //factory.setAutoStartup(false);
        //factory.setBatchListener(true);
        return factory;
    }
}
