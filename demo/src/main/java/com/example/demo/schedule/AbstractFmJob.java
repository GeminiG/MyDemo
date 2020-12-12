package com.example.demo.schedule;

import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
//@EnableScheduling
//@Scope("prototype")
public abstract class AbstractFmJob implements SchedulingConfigurer {

    private String cron = "";

    private String state = "A";

    public void setCorn(String cron) {
        this.cron = cron;
    }

    public String getCron() {
        return this.cron;
    }

    public void stopJob() {
        this.state = "X";
    }

    public abstract void doWork();

    private void processJob() {
        if ("A".equals(state)) {
            doWork();
        }
        else {
            System.out.println("本次不执行");
        }
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        System.out.println("添加定时任务");
        scheduledTaskRegistrar.addTriggerTask(() -> {
            this.processJob();
        }, triggerContext -> {
            CronTrigger trigger = new CronTrigger(cron);
            Date nextExecDate = trigger.nextExecutionTime(triggerContext);
            return nextExecDate;
        });
    }
}
