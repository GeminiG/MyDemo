package com.example.demo.selfdefinecachedemo;

import java.util.Scanner;

public class testDemo {
    public static void main(String[] args) throws InterruptedException {
        MapCacheDemo mapCacheDemo = new MapCacheDemo();

        long addStartTime = System.currentTimeMillis();
        for (int i = 0; i < 300000; i++) {
            mapCacheDemo.add(i+"", null, -1);
        }
        long addEndTime = System.currentTimeMillis();

        System.out.println("新增条数据共耗时： " + (addEndTime - addStartTime));


        long startTime = System.currentTimeMillis();
        /*
        for (int i = 0; i < 30000; i++) {
            Random random = new Random();
            int key = random.nextInt(30000);
            mapCacheDemo.get(key+"");
        }*/
        mapCacheDemo.get(300001+"");
        long endTime = System.currentTimeMillis();
        System.out.println("共耗时： " + (endTime - startTime));
        System.out.println("Hit 次数： " + mapCacheDemo.getHitCount());
        System.out.println("Miss 次数: " + mapCacheDemo.getMissCount());
        System.out.println("Remove 次数: " + mapCacheDemo.getRemoveCount());
        System.out.println("Clear Thread Work Success 次数: " + mapCacheDemo
            .getCleanUpPeriodInSec());
        System.out.println("最大的map值为: " + mapCacheDemo.getMaxMapSize());


        System.out.println("\n是否退出程序：Y/N");
        Scanner input = new Scanner(System.in);
        String Y_N = input.nextLine();
        if(Y_N.equals('Y')||Y_N.equals('y')) {
            System.exit(0);
        }


    }
}
