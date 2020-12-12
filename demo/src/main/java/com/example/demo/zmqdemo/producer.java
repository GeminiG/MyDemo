package com.example.demo.zmqdemo;

import com.ztesoft.mq.client.api.MQClientFactory;
import com.ztesoft.mq.client.api.common.PropertyKey;
import com.ztesoft.mq.client.api.common.exception.MQClientApiException;
import com.ztesoft.mq.client.api.model.MQMessage;
import com.ztesoft.mq.client.api.model.ProduceResult;
import com.ztesoft.mq.client.api.producer.Producer;
import com.ztesoft.mq.client.impl.MQClientFactoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class producer {
    public static void main(String[] args) {

        String alarm = "{\n" +
                "  \"ALARM_BODY\": \"(X32)-AMF-01][7f4729de-68f0-11ea-8d3b-fa163e05f04e], [AMF Register Update Failure Rate:0.000]\",\n" +
                "  \"ALARM_CODE\": \"AMF_ALARM\",\n" +
                "  \"ALARM_ID\": \"7f4729de-68f0-11ea-8d3b-fa163e05f04eAMF_ALARM1\",\n" +
                "  \"ALARM_LEVEL\": \"4\",\n" +
                "  \"ALARM_STATUS\": \"1\",\n" +
                "  \"ALARM_TITLE\": \"AMF Register Update Failure Rate Exceed\",\n" +
                "  \"BTIME\": \"2020-05-27 10:10:00\",\n" +
                "  \"CREATE_DATE\": \"2020-05-27 10:51:30\",\n" +
                "  \"EMS_CODE\": \"ZTE_VNF\",\n" +
                "  \"EMS_TYPE\": \"VNF\",\n" +
                "  \"EMS_TYPE_REL_ID\": \"16\",\n" +
                "  \"EMS_VER_CODE\": \"ZTEVNF1.0\",\n" +
                "  \"ETIME\": \"2020-05-27 10:15:00\",\n" +
                "  \"MESSAGE_TYPE\": \"0\",\n" +
                "  \"MO_GID\": \"6\"\n" +
                "}"; //"OrderId:" + i, ("+++++++ body " + i + "+++++++++++").getBytes()

        Producer producer = null;
        try {
            //创建生产者工厂
            MQClientFactory factory = new MQClientFactoryImpl();
            //生产者属性信息
            Properties properties = new Properties();
            properties.put(PropertyKey.Producer_Id, "zx_producer");
            properties.put(PropertyKey.Namesrv_Addr, "10.45.61.25:9876;10.45.61.27:9876");
            properties.put(PropertyKey.NAMESPACE, "oss");
            //创建生产者
            producer = factory.createProducer(properties);
            //启动生产者
            producer.start();
            System.out.println("**************************** start ********************************************");
            //构造消息
            List<MQMessage> msgs = new ArrayList<>();
            for (int i= 0; i < 1; i++) {
                MQMessage msg = new MQMessage("FM_TOPIC_INF_GA_NORAML", "", "key", alarm.getBytes());
//                MQMessage msg2 = new MQMessage("CM_TOPIC_TEST", "", "key", alarm.getBytes());
                msgs.add(msg);
            }
            //发送消息
            ProduceResult result = producer.send(msgs);
            System.out.println("***** result *****" + result);
        } catch (MQClientApiException e) {
            System.out.println(e);
        } finally {
            if (null != producer) {
            //关闭生产者
                try {
                    producer.shutdown();
                } catch (MQClientApiException e) {
                    System.out.println(e);
                }
            }
        }

    }
}
