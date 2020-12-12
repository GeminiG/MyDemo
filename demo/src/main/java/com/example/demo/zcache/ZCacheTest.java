package com.example.demo.zcache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/zcache")
public class ZCacheTest {

    @Autowired
    RedisTemplate redisTemplate;

    private static final int RECORD_SIZE = 10000;

    private static final String REDIS_KEY_PREFIX = "ZX:TEST:KEY";

    private String value = "{\"VENDOR_NAME\":\"HUAWEI\",\"ORG_SEVERITY\":1,\"EQP_INT_ID\":\"SAME/NE\",\"OBJECT_CLASS\":\"SAE-GW\",\"ACK_FLAG\":\"0\",\"STANDARD_ALARM_ID\":\"1.3.6.1.4.1.2011.2.235.1.1.500.10.34.97\",\"STATE\":\"A\",\"PV_FLAG\":\"VNF\",\"TIME_STAMP\":\"2019-11-15 10:33:30\",\"VIM_TYPE\":\"IT\",\"VENDOR_SEVERITY\":1,\"INT_ID\":\"190919113679693060\",\"OMC_ALARM_ID\":\"5483518427501\",\"OPER_TYPE\":\"1406\",\"ALARM_TEXT\":\"AlarmText-ACPI is in the soft-off state.\",\"ORG_TYPE\":\"1\",\"PROBABLE_CAUSE\":\"1.3.6.1.4.1.2011.2.235.1.1.500.10.34.97\",\"MSG_SEQ\":\"1231909301383\",\"EQP_OBJECT_CLASS\":\"SAE-GW\",\"PROVINCE_ID\":\"NJ\",\"SPECIFIC_PROBLEM_ID\":\"1.3.6.1.4.1.2011.2.235.1.1.500.10.34.97\",\"STANDARD_FLAG\":\"0\",\"NE_LABEL\":\"NanJing/SGW/001\",\"ALARM_RESOURCE_STATUS\":\"0\",\"GID\":\"190918172476490680\",\"EVENT_TIME\":\"2019-12-09 19:34:54\",\"TITLE_TEXT\":\"Server power down[ACPI is in the soft-off state.]\",\"VENDOR_ID\":0,\"LOCATE_INFO\":\"Nanjing\",\"ALARM_SIGNAL\":\"000968729fee48979064c1880cabd7fa\",\"EQP_LABEL\":\"NanJing/SGW/001\",\"C_FP0\":\"000968729fee48979064c1880cabd7fa\",\"ACTIVE_STATUS\":1,\"FORWARD_FLAG\":\"0\",\"PROBABLE_CAUSE_TXT\":\"Server power down[ACPI is in the soft-off state.]\"}";


    @GetMapping("add")
    public void addValueToRedis() {

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < RECORD_SIZE; i++) {
            redisTemplate.opsForValue().set(String.valueOf(i), value);
        }
        long cost = System.currentTimeMillis() - startTime;
        System.out.println("插入数据共用时: " + cost);

    }

    @GetMapping("padd")
    public void pipelinedAdd() {
        long startTime = System.currentTimeMillis();
        List<String> result  = redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public String doInRedis(RedisConnection redisConnection) {
                redisConnection.openPipeline();
                for (int i = 0; i < RECORD_SIZE; i++) {
                    redisConnection.set((REDIS_KEY_PREFIX + i).getBytes(), "value".getBytes()); //value.getBytes());
                }
                return null;
            }
        });
        long cost = System.currentTimeMillis() - startTime;
        System.out.println("管道插入共耗时： " + cost);
        //System.out.println(result);
    }

    @GetMapping("pget")
    public void pget() {
        long startTime = System.currentTimeMillis();
        List<String> result = redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public String doInRedis(RedisConnection redisConnection) {
                for (int i = 0; i< RECORD_SIZE; i++) {
                    Random random = new Random();
                    int key = random.nextInt(RECORD_SIZE);
                    redisConnection.get((REDIS_KEY_PREFIX + key).getBytes());
                }
                return null;
            }
        });
        System.out.println(result.size());
        long cost = System.currentTimeMillis() - startTime;
        System.out.println("管道get花费的时间: " + cost);
    }

    @GetMapping("pdelete")
    public void pDelete() {
        long startTime = System.currentTimeMillis();
        List<String> result = redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public String doInRedis(RedisConnection redisConnection) {
                for (int i = 0; i < RECORD_SIZE; i++) {
                    redisConnection.del((REDIS_KEY_PREFIX + i).getBytes());
                }
                return null;
            }
        });
        long cost = System.currentTimeMillis() - startTime;
        System.out.println("使用管道删除所有的记录共花费: " + cost);
    }
}
