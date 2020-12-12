package com.example.demo.selfdefinecachedemo;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhang.xiaocs
 * 使用ConcurrentHashMap的内存缓存
 * 使用  ConcurrentHashMap，线程安全的要求。
 * 使用SoftReference <Object>  作为映射值，因为软引用可以保证在抛出OutOfMemory之前，如果缺少内存，将删除引用的对象。
 * 在构造函数中，创建了一个守护程序线程，每5秒扫描一次并清理过期的对象。
 */
public class MapCacheDemo {

    //与UTC的偏移量（北京为东八区，则+8）
    private static final int UTC_OFFSET = 8;

    //守护线程执行频率
    private static final int CLEAN_UP_PERIOD_IN_SEC = 5;

    //默认的过期时间
    private static final Long DEFAULT_EXPIRE_TIE = 3600000L;

    //初始即设定最大的容量，防止扩容
    private static final int MAP_SIZE_INIT = 500000;

    //设置MAP的最大值为2^13
    private static final int MAX_MAP_SIZE = 300000;

    private int hitCount = 0;

    private int missCount = 0;

    private int removeCount = 0;

    private int clearThreadWorkCount = 0;

    private long maxMapSize = 0;

    private final ConcurrentHashMap<String, SoftReference<CacheObject>> cache = new ConcurrentHashMap<>(MAP_SIZE_INIT);
    //private final ConcurrentHashMap<String, CacheObject> cache = new ConcurrentHashMap<>(MAP_SIZE_INIT);

    private final SortedMap<Long, String> synTreeMap = Collections.synchronizedSortedMap(new TreeMap<>());

    private final String value = "\"{\\\"OBJECT_ID\\\":138473,\\\"ID\\\":138473,\\\"NAME\\\":\\\"MB-FER-Ro8\\\",\\\"FULL_NAME\\\":\\\"MB-FER-Ro8\\\",\\\"ALIAS\\\":\\\"MB-FER-Ro8\\\",\\\"RELATIVE_NAME\\\":\\\"Huawei_U2000_DATACOM,MB-FER-Ro8\\\",\\\"RMUID\\\":\\\"Huawei_U2000_DATACOM,MB-FER-Ro8\\\",\\\"OBJECT_CLASS\\\":\\\"IPRT\\\",\\\"NETWORK_DOMAIN\\\":null,\\\"PROVINCE_ID\\\":128,\\\"REGION_NAME\\\":null,\\\"REGION_CODE\\\":\\\"128\\\",\\\"SITE_ID\\\":690,\\\"ROOM_ID\\\":null,\\\"FACILITY_ID\\\":null,\\\"HARDWARE_ID\\\":null,\\\"MODEL_ID\\\":\\\"M1703231010T17032310112882\\\",\\\"VENDOR_ID\\\":\\\"HUAWEI\\\",\\\"SPECIALTY_TYPE\\\":null,\\\"STRUCTURE\\\":\\\"C\\\",\\\"HW_VER\\\":\\\"SPH051\\\",\\\"SW_VER\\\":\\\"NE40EV800R007C10SPC100(VRPV800R010C00)\\\",\\\"LONGITUDE\\\":null,\\\"LATITUDE\\\":null,\\\"INSERVICE_DATE\\\":null,\\\"OPERATIONING_STATE\\\":\\\"A\\\",\\\"HEALTH_STATE\\\":\\\"a\\\",\\\"IP_ADDR\\\":\\\"10.2.10.8\\\",\\\"TENANT_ID\\\":null,\\\"RSP_ID\\\":null,\\\"EMS_ID\\\":\\\"Huawei_U2000_DATACOM\\\",\\\"PIM_ID\\\":\\\"Huawei_U2000_DATACOM\\\",\\\"VIM_ID\\\":\\\"Huawei_U2000_DATACOM\\\",\\\"SERIAL_NUMBER\\\":null,\\\"CREATE_USER\\\":-9,\\\"CREATE_DATE\\\":{\\\"date\\\":18,\\\"hours\\\":15,\\\"seconds\\\":17,\\\"month\\\":3,\\\"nanos\\\":0,\\\"timezoneOffset\\\":-480,\\\"year\\\":119,\\\"minutes\\\":57,\\\"time\\\":1555574237000,\\\"day\\\":4},\\\"MODIFY_USER\\\":-9,\\\"MODIFY_DATE\\\":{\\\"date\\\":18,\\\"hours\\\":15,\\\"seconds\\\":17,\\\"month\\\":3,\\\"nanos\\\":0,\\\"timezoneOffset\\\":-480,\\\"year\\\":119,\\\"minutes\\\":57,\\\"time\\\":1555574237000,\\\"day\\\":4},\\\"NOTES\\\":null,\\\"SOURCE_FLAG\\\":\\\"1\\\",\\\"ASSET_NO\\\":null,\\\"NAME_SEQ\\\":null,\\\"TEMPLATE_ID\\\":\\\"T17032310162892\\\",\\\"ADDRESS_ID\\\":null,\\\"ADDRESS_DESC\\\":null,\\\"IS_VIRTUAL\\\":null,\\\"WIDTH\\\":null,\\\"HEIGHT\\\":null,\\\"DEPTH\\\":null,\\\"IF_FACE\\\":null,\\\"IS_USER_RES\\\":\\\"N\\\",\\\"DEVICE_ROLE\\\":null,\\\"IS_DOUBLE_FACE\\\":null,\\\"GEO_FLAG\\\":null,\\\"PLAN_DATE\\\":null,\\\"FIX_TYPE\\\":null,\\\"SOURCE_ID\\\":null,\\\"LOCATION_TYPE\\\":null}\"";


    public MapCacheDemo() {
        Thread cleanerThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(CLEAN_UP_PERIOD_IN_SEC * 1000);
                    if (System.currentTimeMillis()/3600000%24+UTC_OFFSET == 2) {
                        System.out.println("clear all data");
                        clear();
                    }
                    else {
                        clearThreadWorkCount ++;
                        while (synTreeMap.size() >0 && synTreeMap.firstKey() < System.currentTimeMillis()) {
                            Long firstKey = synTreeMap.firstKey();
                            cache.remove(synTreeMap.get(firstKey));
                            synTreeMap.remove(firstKey);
                        }

                        /*
                        cache.entrySet().removeIf(
                            entry -> Optional.ofNullable(entry.getValue()).map(SoftReference::get)
                                .map(CacheObject::isExpired).orElse(false));*/
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        cleanerThread.setDaemon(true);
        cleanerThread.start();
    }

    public void add(String key, Object value, long periodInMillis) {
        if (key == null) {
            return;
        }
        if (periodInMillis == -1) {
            periodInMillis = DEFAULT_EXPIRE_TIE;
        }
        if (value == null) {
            //cache.remove(key);
            value = key + this.value;
        }
        {
            if (size() >= MAX_MAP_SIZE) {
                String keyNeedRemove = synTreeMap.get(synTreeMap.firstKey());
                synTreeMap.remove(synTreeMap.firstKey());
                remove(keyNeedRemove);
                removeCount++;
            }
            if (size() > maxMapSize) {
                maxMapSize = size();
            }

            long expiryTime = System.currentTimeMillis() + periodInMillis;
            cache.put(key, new SoftReference<>(new CacheObject(value, expiryTime)));
            //cache.put(key, new CacheObject(value, expiryTime));
            synTreeMap.put(expiryTime, key);
        }
    }

    public void setExpiryTime(String key, long periodInMillis) {
        CacheObject cacheObject = Optional.ofNullable(cache.get(key)).map(SoftReference::get).orElse(null);
        cacheObject.setExpiryTime(System.currentTimeMillis() + periodInMillis);
    }

    public void remove(String key) {
        cache.remove(key);
    }

    public Object get(String key) {
        if (cache.containsKey(key)) {
            hitCount++;
            setExpiryTime(key, DEFAULT_EXPIRE_TIE);
            return Optional.ofNullable(cache.get(key)).map(SoftReference::get).map(CacheObject::getValue).orElse(null);
        }
        else {
            missCount++;
//            try {
//                Thread.sleep(1);
//            }
//            catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }

            Object value = key + this.value;
            long startTime = System.currentTimeMillis();
            add(key, value, DEFAULT_EXPIRE_TIE);
            long endtime = System.currentTimeMillis();
            System.out.println("add cost time: " + (endtime - startTime));
            return value;
        }

    }

    public void clear() {
        cache.clear();
        synTreeMap.clear();
    }

    public long size() {
        return cache.size(); // entrySet().stream().filter(entry -> Optional.ofNullable(entry.getValue()).map(SoftReference::get).map(cacheObject -> !cacheObject.isExpired()).orElse(false)).count();
    }

    public int getMissCount() {
        return missCount;
    }

    public int getHitCount() {
        return hitCount;
    }

    public int getRemoveCount() {
        return removeCount;
    }

    public int getCleanUpPeriodInSec() {
        return clearThreadWorkCount;
    }

    public long getMaxMapSize() {
        return maxMapSize;
    }

    /**
     * 缓存对象value
     */
    private static class CacheObject {
        private Object value;
        private long expiryTime;

        private CacheObject(Object value, long expiryTime) {
            this.value = value;
            this.expiryTime = expiryTime;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public void setExpiryTime(long expiryTime) {
            this.expiryTime = expiryTime;
        }
    }
}
