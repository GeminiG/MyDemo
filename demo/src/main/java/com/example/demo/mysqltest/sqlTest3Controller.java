package com.example.demo.mysqltest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("test3")
public class sqlTest3Controller {

    @Autowired
    OperService operService;

    int alarmSignal = 2000000;
    int activeAlarmSignal = 1000000;

    List<String> alarmSignalList = new ArrayList<>();
    List<String> activeAlarmSignalList = new ArrayList<>();
    List<String> delAlarmSignalList = new ArrayList<>();

    boolean flag = true;

    @PutMapping("stop")
    public void stop() {
        flag = false;
    }

    @PutMapping("i")
    public void insert() {
        for (int i = 0; i < 300; i++) {
            createAlarmSignalList();
            operService.insert(activeAlarmSignalList);
        }
    }


    @PutMapping("t")
    public void test() throws InterruptedException, ParseException {
//        System.out.println("thread name ： " + Thread.currentThread().getName());
//        createAlarmSignalList();
//        operService.insert(alarmSignalList);
//        operService.insertC(alarmSignalList);
//        Thread.sleep(1000);
//        moveAlarmSignalToDel();
        int i = 0;
        while (flag) {
//            operService.deleteC(delAlarmSignalList);
//            operService.delete(delAlarmSignalList);
            createAlarmSignalList();
            operService.insert(alarmSignalList);
            Thread.sleep(1000);
            moveAlarmSignalToDel();
            operService.insertC(delAlarmSignalList);
            Thread.sleep(1000);
            i++;
        }
//        operService.deleteC(delAlarmSignalList);
//        operService.delete(delAlarmSignalList);
    }

    private void createActiveAlarmSignalList() {
        for (int i = 0; i < 2000; i++) {
            if (activeAlarmSignal == Integer.MAX_VALUE) {
                activeAlarmSignal = 1000000;
            }
            activeAlarmSignal += 1;
            activeAlarmSignalList.add(activeAlarmSignal + "");
        }
    }

    private void createAlarmSignalList() {
        for (int i = 0; i < 1500; i++) {
            if (alarmSignal == Integer.MAX_VALUE) {
                alarmSignal = 2000000;
            }
            alarmSignal += 1;
            alarmSignalList.add(alarmSignal + "");
        }
    }

    private void moveAlarmSignalToDel() {
        delAlarmSignalList.clear();
        delAlarmSignalList.addAll(alarmSignalList);
        alarmSignalList.clear();
    }
}
