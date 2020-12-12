package com.example.demo.kafkademo.producer;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/kafka")
public class KafkaProducerTest {

    @Value("${ftf.kafka.properties.topic.1}")
    private String topic;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    private static int flag = 0;

    @RequestMapping("send/{num}")
    public void sendMessage(@PathVariable("num") int num) {
        Map<String, Object> message = makeMessage();
        for (int i = 0; i < num; i++) {
            flag++;
            message.put("NUM", flag);
            kafkaTemplate.send(topic, JSONObject.toJSONString(message));
            //kafkaTemplate.send(topic, i, "key", JSONObject.toJSONString(message));
        }
    }


    public Map<String, Object> makeMessage() {
        Map<String, Object> message = new HashMap<>();
        message.put("NAME", "message");
        return message;
    }
}
