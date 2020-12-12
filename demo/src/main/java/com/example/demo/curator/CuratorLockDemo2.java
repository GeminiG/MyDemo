package com.example.demo.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.stereotype.Component;

@Component
public class CuratorLockDemo2 {

    static InterProcessMutex lock;

    private static final String KAFKA_LISTENER_LOCK_NODE = "/HAClusterLock/fm_mq_consumer_lock";

    private static String zkAddress = "10.45.50.66:2181";


    public static void main(String[] args) {
        lock = startKafkaListenerLock();
        Thread2().start();
    }

    private static InterProcessMutex startKafkaListenerLock() {
        // 1 重试策略：初试时间为1s 重试10次
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
        // 2 通过工厂创建连接
        CuratorFramework cf = CuratorFrameworkFactory.builder().connectString(zkAddress)
                .sessionTimeoutMs(5000).retryPolicy(retryPolicy).build();
        // 3 开启连接
        cf.start();

        // 4 分布式锁
        final InterProcessMutex lock = new InterProcessMutex(cf, KAFKA_LISTENER_LOCK_NODE);

        return lock;
    }

    public static void doSomething(String name) {
        System.out.println(name + " do something");
    }

    public static Thread Thread1() {
        return new Thread(() -> {
            try {
                lock.acquire();
            } catch (Exception e) {
                System.out.println(e);
            }
            Thread.currentThread().setName("Thread 1");
            doSomething(Thread.currentThread().getName());
            try {
                Thread.sleep(10000);
                lock.release();
                System.out.println("Thread 1 release");
            } catch (InterruptedException e) {
                System.out.println(e);
            } catch (Exception e) {
                System.out.println(e);
            }
        });
    }

    public static Thread Thread2() {
        return new Thread(() -> {
            try {
                lock.acquire();
            } catch (Exception e) {
                System.out.println(e);
            }
            Thread.currentThread().setName("Thread 2");
            doSomething(Thread.currentThread().getName());
            try {
                lock.release();
            } catch (Exception e) {
                System.out.println(e);
            }
        });
    }



}
