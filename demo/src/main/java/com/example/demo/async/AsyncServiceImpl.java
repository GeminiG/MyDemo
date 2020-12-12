package com.example.demo.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncServiceImpl implements AsyncService {

    @Override
    @Async
    public void generateReport() {
        System.out.println("线程名称： 【" + Thread.currentThread().getName() + "】");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
}
