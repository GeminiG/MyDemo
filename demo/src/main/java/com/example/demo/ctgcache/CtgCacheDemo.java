//package com.example.demo.ctgcache;
//
//import com.ctg.itrdc.cache.pool.CtgJedisPool;
//import com.ctg.itrdc.cache.pool.CtgJedisPoolConfig;
//import com.ctg.itrdc.cache.pool.CtgJedisPoolException;
//import com.ctg.itrdc.cache.pool.ProxyJedis;
//import com.ctg.itrdc.cache.vjedis.jedis.JedisPoolConfig;
//import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
//import org.springframework.stereotype.Component;
//import redis.clients.jedis.HostAndPort;
//import redis.clients.jedis.exceptions.JedisConnectionException;
//
//import java.util.ArrayList;
//import java.util.List;
//
////@Component
//public class CtgCacheDemo {
//    public void jedisTest() {
//        List<HostAndPort> hostAndPortList = new ArrayList<>();
//
//        HostAndPort host1 = new HostAndPort("192.168.1.159", 5052);
//        hostAndPortList.add(host1);
//
//        GenericObjectPoolConfig poolConfig = new JedisPoolConfig();
//        poolConfig.setMaxIdle(3);
//        poolConfig.setMaxTotal(10);
//        poolConfig.setMinIdle(3);
//        poolConfig.setMaxWaitMillis(3000);
//        CtgJedisPoolConfig config = new CtgJedisPoolConfig(hostAndPortList);
//        config.setDatabase(10)
//                .setPassword("testredis2#Testredis2")
//                .setPoolConfig(poolConfig)
//                .setPeriod(3000)
//                .setMonitorTimeout(200);
//        CtgJedisPool pool = new CtgJedisPool(config);
//        ProxyJedis jedis = null;
//        for (int i = 1; i <= 1000; i++) {
//            try {
//                jedis = pool.getResource();
//                String hKey = "CTG_TEST";
//                String key = "key " + i;
//                String value = "value " + i;
//                jedis.set(key, value);
//                jedis.hset(hKey, key, value);
//            }
//            catch (CtgJedisPoolException e) {
//                System.out.println(e);
//            }
//            catch (JedisConnectionException e) {
//                System.out.println(e);
//            }
//            finally {
//                try {
//                    jedis.close();
//                }
//                catch (Throwable e) {
//                    System.out.println(e);
//                }
//            }
//        }
//        pool.close();
//
//    }
//}
