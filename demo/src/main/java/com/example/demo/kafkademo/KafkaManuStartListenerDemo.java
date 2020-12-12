/**
 *
 */
package com.example.demo.kafkademo;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.Acknowledgment;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class KafkaManuStartListenerDemo implements CommandLineRunner{


    @Autowired
    private KafkaListenerEndpointRegistry registry;

    private static final int SESSION_OUT_TIME = 5000;

    Map<Integer, Long> partitinOffset = new HashMap<>();

    //kafka id
    private static final String KAFKA_ID = "report_kafka_listener_id";


    public synchronized void setNum(int partition, Long offset) {
        partitinOffset.put(partition, offset);
        Long sumOffset = 0L;
        for (Map.Entry<Integer, Long> entry: partitinOffset.entrySet()) {
            sumOffset += entry.getValue();
        }
        if (sumOffset > 10) {
            stopKafkaListener();
        }
    }

    @KafkaListener(id = "report_kafka_listener_id", topics = {
        "${kafka.consumer.topic.subs}"
    }, containerFactory = "kafkaListenerContainerFactory2")
    public void kafkaMsgListener(List<ConsumerRecord<?, ?>> messageList,Acknowledgment ack) {
        Date startTime = new Date();
        try {
            ack.acknowledge(); //后提交
        }
        catch (Exception e) {
        }
        finally {
            setNum(messageList.get(0).partition(), messageList.get(0).offset());
            Date endTime = new Date();
            long timeGrap = endTime.getTime() - startTime.getTime();
            System.out.println(timeGrap);
        }
    }


    @Override
    public void run(String... args) throws Exception {
        // startKafkaListener();
    }


    public void stopKafkaListener() {
        if (registry.getListenerContainer(KAFKA_ID).isRunning()) {
            registry.getListenerContainer(KAFKA_ID).stop();

        }

    }

    public void startKafkaListener() {
        if (!registry.getListenerContainer(KAFKA_ID).isRunning()) {
            registry.getListenerContainer(KAFKA_ID).start();
        }
    }

}
