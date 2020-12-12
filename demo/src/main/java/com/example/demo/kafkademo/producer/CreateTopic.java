package com.example.demo.kafkademo.producer;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewPartitions;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CreateTopic implements CommandLineRunner {

    @Value("${ftf.kafka.properties.topic.1}")
    private String topic;

    @Autowired AdminClient adminClient;


    @Override public void run(String... args) throws Exception {
        List<NewTopic> topics = new ArrayList<>();
        List<String> deleteTopics = new ArrayList<>();
        NewTopic newTopic = new NewTopic(topic, 3, (short)1);
        topics.add(newTopic);
        deleteTopics.add(topic);

        NewPartitions newPartitions = NewPartitions.increaseTo(2);
        Map<String, NewPartitions> partitionsMap = new HashMap<>();
        partitionsMap.put(topic, newPartitions);

        //adminClient.deleteTopics(deleteTopics);
        //adminClient.createTopics(Arrays.asList(newTopic));
        adminClient.createPartitions(partitionsMap);
        adminClient.close();
    }
}
