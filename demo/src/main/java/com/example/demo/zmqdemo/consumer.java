package com.example.demo.zmqdemo;

import com.ztesoft.mq.client.api.MQClientFactory;
import com.ztesoft.mq.client.api.common.PropertyKey;
import com.ztesoft.mq.client.api.common.exception.MQClientApiException;
import com.ztesoft.mq.client.api.consumer.BatchConsumer;
import com.ztesoft.mq.client.api.consumer.MQMessageBatchHandler;
import com.ztesoft.mq.client.api.consumer.MessageHandlerContext;
import com.ztesoft.mq.client.api.model.MQMessage;
import com.ztesoft.mq.client.impl.MQClientFactoryImpl;
import com.ztesoft.mq.client.impl.consumer.ProcessStatus;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;

public class consumer {
    public static int num = 0;
    public static void main(String[] args) {
        consumer1();
//        consumer2();

    }

    static void consumer1() {
        try {
            MQClientFactory factory = new MQClientFactoryImpl();
            //消费者的属性信息
            Properties properties = new Properties();
            properties.put(PropertyKey.Consumer_Id, "zx_consumer1");
            properties.put(PropertyKey.Namesrv_Addr, "10.45.61.25:9876;10.45.61.27:9876");
            properties.put(PropertyKey.NAMESPACE, "oss");
            properties.put(PropertyKey.Consumer_Worker_Size, "1");
//            properties.put(PropertyKey.Pull_interval, "5000");

            //消息处理器
            final MQMessageBatchHandler handler = new MQMessageBatchHandler() {
                @Override
                public ProcessStatus process(List<MQMessage> messages, MessageHandlerContext context) {
                    System.out.println(Thread.currentThread().getId() + " ,this time has " + messages.size() + " messages");
                    for (MQMessage message : messages) {
                        String body = new String(message.getBody(), Charset.forName("UTF-8"));
                        System.out.println(" TOPIC is " + message.getTopic() + " , body is " + body);
                    }
                    return ProcessStatus.Done;
                }
            };
            BatchConsumer consumer = factory.createBatchConsumer(properties);
            //订阅消息，可以调用多次，以订阅不同的topic
            consumer.subscribe("ZX_TEST", handler);
            consumer.start();
        } catch (MQClientApiException e) {
            System.out.println(e);
        }
    }

    static void consumer2() {
        try {
            MQClientFactory factory = new MQClientFactoryImpl();
            //消费者的属性信息
            Properties properties = new Properties();
            properties.put(PropertyKey.Consumer_Id, "zx_consumer2");
            properties.put(PropertyKey.Namesrv_Addr, "10.45.61.25:9876;10.45.61.27:9876");
            properties.put(PropertyKey.NAMESPACE, "oss");
            properties.put(PropertyKey.Consumer_Worker_Size, "1");
//            properties.put(PropertyKey.Pull_interval, "5000");

            //消息处理器
            final MQMessageBatchHandler handler = new MQMessageBatchHandler() {
                @Override
                public ProcessStatus process(List<MQMessage> messages, MessageHandlerContext context) {
                    System.out.println(Thread.currentThread().getId() + " ,this time has " + messages.size() + " messages");
                    for (MQMessage message : messages) {
                        String body = new String(message.getBody(), Charset.forName("UTF-8"));
                        System.out.println(" TOPIC is " + message.getTopic() + " , body is " + body);
                    }
                    return ProcessStatus.Done;
                }
            };
            BatchConsumer consumer = factory.createBatchConsumer(properties);
            //订阅消息，可以调用多次，以订阅不同的topic
            consumer.subscribe("CM_TOPIC_TEST", handler);
            consumer.start();
        } catch (MQClientApiException e) {
            System.out.println(e);
        }
    }

}
