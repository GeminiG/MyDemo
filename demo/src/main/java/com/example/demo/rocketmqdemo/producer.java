package com.example.demo.rocketmqdemo;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.UUID;

/**
 * Created by xy on 2018/11/16.
 */
public class producer {
    private static DefaultMQProducer producer = null;

    public static void main(String[] args) {
        System.out.print("[----------]Start\n");
        int pro_count = 1;
        if (args.length > 0) {
            pro_count = Integer.parseInt(args[0]);
        }
        boolean result = false;
        try {
            ProducerStart();
            for (int i = 0; i < pro_count; i++) {
                String msg = "hello rocketmq "+ i+"".toString();
                SendMessage("ZX_TEST",              //topic
                        "Tag"+i,                           //tag
                        "Key"+i,                           //key
                        msg);                                  //body
                System.out.print(msg + "\n");
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        finally {
            producer.shutdown();
        }
        System.out.print("[----------]Succeed\n");
    }

    private static boolean ProducerStart() {
        producer = new DefaultMQProducer("zx_test");
        producer.setNamesrvAddr("10.45.61.25:9876,10.45.61.27:9876");//10.45.50.65:3370
        producer.setInstanceName(UUID.randomUUID().toString());
        producer.setClusterName("ZMQ_Cluster");
        try {
            producer.start();
            producer.setSendMsgTimeout(15000);
//            producer.setVipChannelEnabled(false);
        } catch(MQClientException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static boolean SendMessage(String topic,String tag,String key, String str) {
        Message msg = new Message(topic,tag,key,str.getBytes());
        try {
            SendResult result = producer.send(msg);
            SendStatus status = result.getSendStatus();
            System.out.println("___________________________SendMessage: " + result);
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}