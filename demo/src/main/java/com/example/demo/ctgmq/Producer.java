package com.example.demo.ctgmq;

import com.ctg.mq.api.CTGMQFactory;
import com.ctg.mq.api.IMQProducer;
import com.ctg.mq.api.PropertyKeyConst;
import com.ctg.mq.api.bean.MQMessage;
import com.ctg.mq.api.bean.MQSendResult;
import com.ctg.mq.api.exception.MQException;
import com.ctg.mq.api.exception.MQProducerException;

import java.util.Properties;

/**
 * Producer，发送消息
 */
public class Producer {
    public static void main(String[] args) throws InterruptedException, MQException {

        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.ProducerGroupName, "producer_group");
        properties.setProperty(PropertyKeyConst.NamesrvAddr, "192.168.1.207:7542");
        properties.setProperty(PropertyKeyConst.NamesrvAuthID, "ctgtest");
        properties.setProperty(PropertyKeyConst.NamesrvAuthPwd, "ctgtest"); properties.setProperty(PropertyKeyConst.ClusterName, "func_mq");
        properties.setProperty(PropertyKeyConst.TenantID, "1");

        IMQProducer producer = CTGMQFactory.createProducer(properties);//建议应用启动时创建
        int connectResult = producer.connect();
        if(connectResult != 0){
            return;
        }
        String msg = "{\n" +
                "\t\"VendorSeverity\": \"3\",\n" +
                "\t\"GID\": \"HUAWEI_U2000_RC,NE=264,BTS=1110\",\n" +
                "\t\"objectClass\": \"NDF\",\n" +
                "\t\"OmcAlarmId\": \"1234567890000000000008\",\n" +
                "\t\"vendorId\": \"ZTE\",\n" +
                "\t\"TimeStamp\": 1584342675004,\n" +
                "\t\"LocateInfo\": \"host=CO1/HOST/01,vmname=CO1/VM/01,vnfcname=CO1/VNFC/01,ms=VRU_TMSP_AGT_5\",\n" +
                "\t\"ActiveStatus\": \"1\",\n" +
                "\t\"ProbableCause\": \"3306553345\",\n" +
                "\t\"SLICEID\": \"NA\",\n" +
                "\t\"AlarmText\": \"{\\\\\\\"en\\\\\\\":\\\\\\\"memory useage:18%;memory threshold:1%\\\\\\\",\\\\\\\"zh\\\\\\\":\\\\\\\"业务内存三级负荷\\\\\\\"}\",\n" +
                "\t\"EventTime\": \"2020-03-30 11:05:00\",\n" +
                "\t\"VendorType\": \"3\",\n" +
                "\t\"vendor_id\": \"ZTE\",\n" +
                "\t\"ObjectClass\": \"VIM\",\n" +
                "\t\"ProbableCauseTxt\": \"{\\\\\\\"en\\\\\\\":\\\\\\\"memory useage:18%;memory threshold:1%\\\\\\\",\\\\\\\"zh\\\\\\\":\\\\\\\"业务内存三级负荷\\\\\\\"}\",\n" +
                "\t\"iDN\":\"\",\n" +
                "\t\"eqp_iDN\":\"\"\n" +
                "}";
        for (int i = 0; i < 1000; i++) {
            try {
                MQMessage message = new MQMessage(
                        "CM_TOPIC_TEST_5GC",// topic
                        ""+i,// key
                        "ORDER_TAG",//tag
                        msg.getBytes()// body
                );
                MQSendResult sendResult = producer.send(message);
                System.out.println(sendResult);
                Thread.sleep(5000);
            } catch (MQProducerException e) {
                System.out.println(e);
            }
        }
        producer.close();//建议应用关闭时关闭
    }
}
