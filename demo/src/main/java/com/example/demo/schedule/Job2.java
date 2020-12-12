package com.example.demo.schedule;

import org.springframework.stereotype.Component;

@Component
public class Job2 extends AbstractFmJob {

    private String cron = "0 * * * * ?";

    public void initJob2() {
        this.setCorn(cron);
    }

    @Override
    public void doWork() {
        System.out.println("【Job2 开始执行定时任务】" + getCron());
        stopJob();
        System.out.println("下次就不执行了");
    }
}
