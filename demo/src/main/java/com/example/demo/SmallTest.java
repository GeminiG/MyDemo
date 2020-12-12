package com.example.demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class SmallTest {
    static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); //HH:mm:ss
    static SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

    public static void main(String[] args) throws ParseException {
        String str = "01:00";
        String str2 = "08:00";
        System.out.println((sdf.parse(str2).getTime() - sdf.parse(str).getTime())/1000/60/60);

        String str3 = "";
        System.out.println(str3.length() == 0);
    }

    public static void testRandomDate() {
        Date newDate = randomDate("2020-07-12 00:00:00", "2020-07-12 01:00:00");
        System.out.println(newDate);
        System.out.println(sdf.format(newDate));
    }

    public static Date randomDate(String beginDate, String endDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = format.parse(beginDate);// 构造开始日期
            Date end = format.parse(endDate);// 构造结束日期
            // getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
            if (start.getTime() >= end.getTime()) {
                return null;
            }
            long date = random(start.getTime(), end.getTime());
            Date newDate = new Date(date);
            return newDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long random(long begin, long end) {
        long next = (long) (Math.random() * (end - begin));
        System.out.println(next);
        long rtn = begin + next;
        System.out.println(rtn);
        // 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
        if (rtn == begin || rtn == end) {
            return random(begin, end);
        }
        return rtn;
    }

    public static void testList() {
        List<Map> list = new ArrayList<>();
        Map<String, Map> map = new HashMap<>();
        Map<String, Object> map1 = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        Map<String, Object> map3 = new HashMap<>();
        Map<String, Object> map4 = new HashMap<>();
        map.put("1", map1);
        map.put("2", map2);
        map.put("3", map3);
        map.put("4", map4);
        map1.put("1", "1");
        map1.put("p", "");
        map4.put("4", "4");
        map4.put("p", "2");
        map2.put("2", "2");
        map2.put("p", "1");
        map3.put("3", "3");
        map3.put("p", "2");
        list.add(map1);
        list.add(map2);
        list.add(map3);
        list.add(map4);
        System.out.println(list);

        Iterator<Map> iterator = list.iterator();
        while(iterator.hasNext()) {
            Map tempMap = iterator.next();
            String p = tempMap.get("p").toString();
            if (map.containsKey(p)) {
                List tempChildren = new ArrayList();
                if (map.get(p).get("c") != null) {
                    tempChildren = (List) map.get(p).get("c");
                }
                tempChildren.add(tempMap);
                map.get(p).put("c", tempChildren);
                iterator.remove();
            }
        }
        System.out.println(list);
    }

     public static void testMap2() {
        Map map1 = new HashMap();
        map1.put("a1", "b1");
        map1.put("b1", "c1");
        map1.put("a1", "b2");
        map1.put("b1", "c2");
        map1.put("b2", "c3");

        System.out.println(map1.containsValue("c3"));

     }

    static Comparator<Map<String, Object>> eventTimeComparator = new Comparator<Map<String, Object>>() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        @Override
        public int compare(Map<String, Object> o1, Map<String, Object> o2) {
            try {
                Date o1EventTime = sdf.parse(String.valueOf(o1.get("EVENT_TIME")));
                Date o2EventTime = sdf.parse(String.valueOf(o2.get("EVENT_TIME")));
                return o2EventTime.compareTo(o1EventTime);
            }
            catch (ParseException e) {
                return 1;
            }
        }
    };

    public static void testComparator() {
        Map map1 = new HashMap();
        map1.put("EVENT_TIME", "2020-03-11 11:11:11");

        Map map2 = new HashMap();
        map2.put("EVENT_TIME", "2020-4-11 11:12:13");


        List list = new ArrayList();
        list.add(map1);
        list.add(map2);
        System.out.println(list);

        Collections.sort(list, eventTimeComparator);

        System.out.println(list);


    }

    public static void testMap() {
        Map map1 = new ConcurrentHashMap(50);
        Map map2 = new ConcurrentHashMap(50);

        Map<String, Map> map3 = new HashMap<>();

        map1.put("key1", 1);
        map2.put("key2", 2);

        map3.put("map1", map1);
        map3.put("map2", map2);

        List<Map> list1 = new ArrayList<>(map3.values());

        System.out.println(list1);

        map1.put("key1", 2);

        System.out.println(list1);

        Map map4 = new HashMap();
        map4.putAll(map2);
        map2.put("key2", 3);
        System.out.println(map2);
        System.out.println(map4);

    }

    public static void testSyncTreeMap() throws ParseException {
        SortedMap<Date, String> synTreeMap = Collections.synchronizedSortedMap((new TreeMap<>()));

        synTreeMap = Collections.synchronizedSortedMap(synTreeMap);

        String date1 = "2020-04-24 23:59:56";
        String date2 = "2020-04-25 23:59:50";
        String date3 = "2020-03-25 23:59:50";
        String date4 = "2020-05-25 23:59:50";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        synTreeMap.put(sdf.parse(date1), "1");
        synTreeMap.put(sdf.parse(date2), "2");
        synTreeMap.put(sdf.parse(date3), "3");
        synTreeMap.put(sdf.parse(date4), "4");

        synTreeMap.forEach((k, v) -> {
            System.out.println(k + " " + v);
        });

    }


    public static void testConcurrentHashMap() {
        Map<String, Integer> concurrentHashMap = new ConcurrentHashMap<>();

        concurrentHashMap.put("test", 1);

        for (int i = 0; i < 50; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    concurrentHashMap.put("test", 0);

                    System.out.println(concurrentHashMap.get("test"));
                }
            }).start();
        }
    }

    public static void testSimpleDateFormate() {
        String dateStr = "2020-04-24 23:59:59";
        System.out.println(dateStr.substring(dateStr.length() - 8));
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");


    }

    public static void testMapLoop() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");

        Map<String, String> map = new HashMap<>();
        map.put("2","");
        map.put("5","");
        map.put("4","");
        map.put("1","");

        Iterator<String> iter = list.iterator();
        while(iter.hasNext()) {
            String str = iter.next();
            boolean exist = false;
            for(Map.Entry<String, String> entry: map.entrySet()) {
                if (entry.getKey().equals(str)) {
                    map.remove(entry.getKey());
                    exist = true;
                    System.out.println(map);
                    break;
                }
            }
            if(!exist){
                iter.remove();
            }
        }

        System.out.println("---");
        System.out.println(list);
        System.out.println(map);
        System.out.println("-----");

        for (Map.Entry<String, String> entry: map.entrySet()) {
            list.add(entry.getKey());
        }

        System.out.println(list);
    }
}
