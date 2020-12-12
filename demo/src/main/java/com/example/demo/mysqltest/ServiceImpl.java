package com.example.demo.mysqltest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ServiceImpl implements OperService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Resource
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    String data = "(NULL,'接收处理机（RPP）到信道处理器（CHP）的主集，分集上行数据校验均错(DSP0)','1','3','3','{EVENT_TIME}',NULL,'2a233a98f35e453dafa20cac828f5f8b',NULL,NULL,'2568','{\\\"en\\\":\\\"memory useage:18%;memory threshold:1%\\\",\\\"zh\\\":\\\"业务内存三级负荷\\\"}','31451',NULL,'BSC',NULL,NULL,NULL,'host=CO1/HOST/01,vmname=CO1/VM/02,vnfcname=CO1/VNFC/01,ms=VRU_TMSP_AGT_5',NULL,'NA','{ALARM_SIGNAL}','{ALARM_SIGNAL}','{ALARM_SIGNAL}','2020-07-22 13:58:30.0','ZTE',NULL,'gidwy20191227MME0002',NULL,NULL,NULL,NULL,'CORE','V2.97','3','008-012-00-102568',NULL,NULL,'1','1','1','5','4','接收处理机（RPP）到信道处理器（CHP）的主集，分集上行数据校验均错(DSP0)',NULL,'Y',NULL,NULL,'0',NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL)\n";

    String clearData = "('{ALARM_SIGNAL}','{ALARM_SIGNAL}','{EVENT_TIME}')\n";

    String[] startTimeStrs = {"2020-07-19 00:00:00", "2020-07-19 23:59:59"};


    @Override
    @Async
    public void insert(List<String> alarmSignalList) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println("insert thread name: " + Thread.currentThread().getName());
        List<String> tmpAlarmSignalList = new ArrayList<>();
        tmpAlarmSignalList.addAll(alarmSignalList);

        Long startTime = System.currentTimeMillis();

        StringBuffer sb = new StringBuffer("INSERT INTO fm_alarm2 (DB_ID,ORG_ALARM_TITLE,ORG_ALARM_STATUS,ORG_ALARM_TYPE,ORG_SEVERITY,ORG_EVENT_TIME,ORG_CLEAR_TIME,ORG_ALARM_ID,ORG_ALARM_CODE,ORG_ALARM_CODE_NAME,ORG_SPECIFIC_PROBLEM_ID,ORG_SPECIFIC_PROBLEM,ORG_NE_UID,ORG_NE_NAME,ORG_NE_TYPE,ORG_OBJECT_UID,ORG_OBJECT_NAME,ORG_OBJECT_TYPE,ORG_LOCATION_INFO,ORG_ADDITIONAL_INFO,ORG_NETWORK_SLICE_ID,PHD_ALARM_SIGNAL,PHD_FP,PHD_C_FP,PHD_DISCOVERY_TIME,PHD_VENDOR_ID,PHD_ALARM_SOURCE,PHD_GID,RES_CITY_ID,RES_AREA_ID,RES_SITE_ID,RES_ROOM_ID,STD_SPECIALTY_TYPE,STD_VENDOR_VERSION,STD_INMS_SEVERITY,STD_INMS_ALARM_ID,STD_ALARM_EXPLAIN,STD_ALARM_REPAIR_ADVICE,STD_ALARM_TYPE,STD_ALARM_LOGIC_TYPE,STD_ALARM_LOGIC_SUB_TYPE,STD_EFFECT_NE,STD_EFFECT_SERVICE,STD_ALARM_NAME,STD_SEND_IT_FLAG,STD_FLAG,PROJ_NUM,PROJ_NAME,PRS_ACK_FLAG,PRS_FORWARD_FLAG,PRS_TT_ID,PRS_TT_FLAG,PRS_NOTICE_FLAG,PRS_PREPROCESS_FLAG,PRS_RELATED_RULE_SET_ID,PRS_RELATED_RULE_TYPE,PRS_RELATED_RULE_ID,PRS_RELATED_RULE_NAME,PRS_RELATED_DATE,PRS_P_ALARM_SIGNAL,PRS_COMPRESSION_KEY,PRS_COMPRESSION_ALARM_NUM,PRS_SCAN_FLAG,PRS_FILTER_FLAG,PRS_REMARK_FLAG,PRS_LOAD_DB_TIME) VALUES \n");

        for (String alarmSignal: tmpAlarmSignalList) {
            String newData = data.replace("{EVENT_TIME}", sdf.format(randomDate(startTimeStrs[0], startTimeStrs[1])));
            newData = newData.replace("{ALARM_SIGNAL}",  alarmSignal);
            sb.append(newData);
            sb.append(",");
        }

        jdbcTemplate.update(sb.toString().substring(0, sb.toString().length() - 1));

        System.out.println("插入告警共用时： " + (System.currentTimeMillis() - startTime));


    }

    @Override
    @Async
    public void insertC(List<String> alarmSignalList) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println("insertC thread name: " + Thread.currentThread().getName());

        List<String> tmpAlarmSignalList = new ArrayList<>();
        tmpAlarmSignalList.addAll(alarmSignalList);

        Long startTime = System.currentTimeMillis();

        StringBuffer sb = new StringBuffer("INSERT INTO fm_alarm2_clear (PHD_ALARM_SIGNAL,PHD_C_FP,ORG_CLEAR_TIME) VALUES \n");
        for (String alarmSignal: tmpAlarmSignalList) {

            String newData = clearData.replace("{EVENT_TIME}", sdf.format(randomDate(startTimeStrs[0], startTimeStrs[1])));
            newData = newData.replace("{ALARM_SIGNAL}", alarmSignal);
            sb.append(newData);
            sb.append(",");
        }

        jdbcTemplate.update(sb.toString().substring(0, sb.toString().length() - 1));

        Long endTime = System.currentTimeMillis();

        System.out.println("插入清除共耗时： " + (endTime - startTime));


    }

    @Override
    @Async
    public void delete(List<String> alarmSignalList) {
//        System.out.println("in size: " + alarmSignalList.size());
//        System.out.println("delete thread name: " + Thread.currentThread().getName());
        Long startTime = System.currentTimeMillis();

        List<String> tmpAlarmSignalList = new ArrayList<>();
        tmpAlarmSignalList.addAll(alarmSignalList);


        StringBuffer sb = new StringBuffer("delete from fm_alarm2 WHERE PHD_ALARM_SIGNAL IN (");

        for (String alarmSignal: tmpAlarmSignalList) {
            sb.append("'");
            sb.append(alarmSignal);
            sb.append("',");
        }
        sb.deleteCharAt(sb.toString().length()-1);
        sb.append(")");

        int result = jdbcTemplate.update(sb.toString());

        if (result != 1000) {
            System.out.println(new Date() + " " + result);
        }

        Long endTime = System.currentTimeMillis();

        System.out.println("删除活动表清除共耗时： " + (endTime - startTime));

    }

    @Override
    @Async
    public void deleteC(List<String> alarmSignalList) {
//        System.out.println("C in size: " + alarmSignalList.size());
//        System.out.println("deleteC thread name: " + Thread.currentThread().getName());
//        /*
        Long startTime = System.currentTimeMillis();

        List<String> tmpAlarmSignalList = new ArrayList<>();
        tmpAlarmSignalList.addAll(alarmSignalList);

        StringBuffer sb = new StringBuffer("delete from fm_alarm2_clear WHERE PHD_ALARM_SIGNAL IN (");

        for (String alarmSignal: tmpAlarmSignalList) {
            sb.append("'");
            sb.append(alarmSignal);
            sb.append("',");
        }

        sb.deleteCharAt(sb.toString().length()-1);
        sb.append(")");

        int result = jdbcTemplate.update(sb.toString());

        if (result != 1000) {
            System.out.println(new Date() + " " + result);
        }

        Long endTime = System.currentTimeMillis();

        System.out.println("删除清除表清除共耗时： " + (endTime - startTime));

    }

    @Override
    @Async
    public void update(List<String> alarmSignalList) {
//        System.out.println("C in size: " + alarmSignalList.size());
//        System.out.println("deleteC thread name: " + Thread.currentThread().getName());
//        /*
        Long startTime = System.currentTimeMillis();

        List<String> tmpAlarmSignalList = new ArrayList<>();
        tmpAlarmSignalList.addAll(alarmSignalList);

        StringBuffer sb = new StringBuffer("update fm_alarm2 set ORG_ALARM_STATUS = 0, ORG_CLEAR_TIME = '2020-08-01 00:00:00' WHERE PHD_ALARM_SIGNAL IN (");

        for (String alarmSignal: tmpAlarmSignalList) {
            sb.append("'");
            sb.append(alarmSignal);
            sb.append("',");
        }

        sb.deleteCharAt(sb.toString().length()-1);
        sb.append(")");

        int result = jdbcTemplate.update(sb.toString());

        if (result != 1000) {
            System.out.println(new Date() + " " + result);
        }

        Long endTime = System.currentTimeMillis();

        System.out.println("更新告警表共耗时： " + (endTime - startTime));

    }

    /**
     * 生成随机时间
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public Date randomDate(String beginDate, String endDate) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date start = sdf.parse(beginDate);// 构造开始日期
            Date end = sdf.parse(endDate);// 构造结束日期
            // getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
            if (start.getTime() >= end.getTime()) {
                return null;
            }
            long date = random(start.getTime(), end.getTime());
            return new Date(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public long random(long begin, long end) {
        long rtn = begin + (long) (Math.random() * (end - begin));
        // 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
        if (rtn == begin || rtn == end) {
            return random(begin, end);
        }
        return rtn;
    }
}
