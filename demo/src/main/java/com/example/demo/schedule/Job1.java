package com.example.demo.schedule;

import org.springframework.stereotype.Component;

@Component
public class Job1 extends AbstractFmJob {

    private String cron = "0 0 0 1 1/3 ?";

    public void initJob1() {
        this.setCorn(cron);
    }

    @Override
    public void doWork() {
        System.out.println("【Job1 开始执行定时任务】" + getCron());
    }
}
