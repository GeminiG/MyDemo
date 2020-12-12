package com.example.demo.kafkademo.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class KafkaConsumerTest {

    @Autowired
    private KafkaListenerEndpointRegistry registry;

    private static final String  KAFKA_ID_1 = "kafka_id_1";

    private static final String  KAFKA_ID_2 = "kafka_id_2";

    //@KafkaListener(id = KAFKA_ID_1, topics = "${ftf.kafka.properties.topic.1}", containerFactory = "testKafkaListenerContainerFactory")
    public void kafkaListener1(ConsumerRecord<?, ?> record) {
        doSomething(record);
    }

    //@KafkaListener(id = KAFKA_ID_2, topics = "${ftf.kafka.properties.topic.1}", containerFactory = "testKafkaListenerContainerFactory")
    public void kafkaListener2(ConsumerRecord<?, ?> record) {
        doSomething(record);
    }


    public void kafkaListener(
        ConsumerRecord<?, ?> record,
        //List<ConsumerRecord<?, ?>> records,
        Acknowledgment ack) throws InterruptedException {
        System.out.println("get records start, now thread id is " + Thread.currentThread().getId());
        //for (ConsumerRecord record: records) {
            System.out.println(record.value());
        //}
        Thread.sleep(0);
        System.out.println();
        ack.acknowledge();
    }


    @GetMapping(value = "listener/start")
    public void startKafkaListener() {
        if (!registry.getListenerContainer(KAFKA_ID_1).isRunning()) {
            registry.getListenerContainer(KAFKA_ID_1).start();
        }
    }

    @GetMapping(value = "listener/stop")
    public void stopKafkaListener() {
        if (registry.getListenerContainer(KAFKA_ID_1).isRunning()) {
            registry.getListenerContainer(KAFKA_ID_1).stop();
        }
    }

    public void doSomething(ConsumerRecord record) {
        System.out.println("get record start, now thread id is " + Thread.currentThread().getId());
        System.out.println("topic: " + record.topic());
        System.out.println("partition: " + record.partition());
        System.out.println("offset: " + record.offset());
        System.out.println("value: " + record.value().toString());
        System.out.println();

    }

}
