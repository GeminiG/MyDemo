package com.example.demo.rocketmqdemo;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author：duomu
 * @date：2017/8/4 18:09
 */
public class consumer {

    public static void main(String[] args) {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test_quick_consumer_name");
        consumer.setNamesrvAddr("10.45.50.65:3370");
        consumer.setConsumeThreadMax(20);
        consumer.setConsumeThreadMin(10);
        consumer.setAdjustThreadPoolNumsThreshold(10000);
        consumer.setConsumeMessageBatchMaxSize(1);
        try {
            consumer.subscribe("OSS_EDM_NIA_TO_CORE_TOPIC_ZX", "*");//可订阅多个tag，但是一个消息只能有一个tag
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            consumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                    Message msg = list.get(0);
                    String str = new String(msg.getBody());
                    System.out.println("key: " + msg.getKeys() + ", body: " + str);
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            consumer.start();
            System.out.println("consumer启动成功");
        } catch (MQClientException e) {
            System.out.println("消费者订阅消息失败，error：" + e);
        }
    }
}

//import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
//import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
//import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
//import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
//import org.apache.rocketmq.client.exception.MQClientException;
//import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
//import org.apache.rocketmq.common.message.MessageExt;
//import org.apache.rocketmq.remoting.common.RemotingHelper;
//import java.util.List;
///**
// * @author 言曌
// * @date 2019-08-04 13:31
// */
//public class consumer {
//    public static final String NAMESRV_ADDR = "10.46.2.198:3370"; //"10.45.50.65:3370";
//    public static void main(String[] args) throws MQClientException {
//        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test_quick_consumer_name");
//        consumer.setNamesrvAddr(NAMESRV_ADDR);
//        //从最后开始消费
//        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
////        consumer.subscribe("test_quick_topic","tagA"); //过滤：消费tag为tagA的消息
//        consumer.subscribe("test_quick_topic", "*"); //消费所有的
//        consumer.registerMessageListener(new MessageListenerConcurrently() {
//            @Override
//            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
//                MessageExt messageExt = list.get(0);
//                try {
//                    String topic = messageExt.getTopic();
//                    String tags = messageExt.getTags();
//                    String keys = messageExt.getKeys();
//                    String msgBody = new String(messageExt.getBody(), RemotingHelper.DEFAULT_CHARSET);
//                    System.out.println("topic: " + topic + ",tags: " + tags + ", keys: " + keys + ", body: " + msgBody);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
//                }
//                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//            }
//        });
//        consumer.start();
//        System.out.println("comsumer start");
//    }
//}