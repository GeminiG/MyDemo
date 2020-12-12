package com.example.demo.mysqltest;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("test2")
public class sqlTest2Controller {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Resource
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    String data = "(NULL,'接收处理机（RPP）到信道处理器（CHP）的主集，分集上行数据校验均错(DSP0)','1','3','3','{EVENT_TIME}',NULL,'2a233a98f35e453dafa20cac828f5f8b',NULL,NULL,'2568','{\\\"en\\\":\\\"memory useage:18%;memory threshold:1%\\\",\\\"zh\\\":\\\"业务内存三级负荷\\\"}','31451',NULL,'BSC',NULL,NULL,NULL,'host=CO1/HOST/01,vmname=CO1/VM/02,vnfcname=CO1/VNFC/01,ms=VRU_TMSP_AGT_5',NULL,'NA','{ALARM_SIGNAL}','{ALARM_SIGNAL}','{ALARM_SIGNAL}','2020-07-22 13:58:30.0','ZTE',NULL,'gidwy20191227MME0002',NULL,NULL,NULL,NULL,'CORE','V2.97','3','008-012-00-102568',NULL,NULL,'1','1','1','5','4','接收处理机（RPP）到信道处理器（CHP）的主集，分集上行数据校验均错(DSP0)',NULL,'Y',NULL,NULL,'0',NULL,NULL,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL)\n";

    String relationData = "('{ALARM_SIGNAL}','0','1','2','2020-07-20 23:59:59','{P_ALARM_SIGNAL}')\n";

    String clearData = "('{ALARM_SIGNAL}','{ALARM_SIGNAL}','{EVENT_TIME}')\n";

    String[] startTimeStrs = {"2020-07-12 00:00:00", "2020-07-18 23:59:59"};

    int startAlarmSignal = 1000000;

    @PutMapping("insert")
    public void insert() throws ParseException {

        int signalInsertAlarms = 4000;
        int insertTime = 150;

        Long startTime = System.currentTimeMillis();

        for (int time = 0; time < insertTime; time++) {
            StringBuffer sb = new StringBuffer("INSERT INTO fm_alarm2 (DB_ID,ORG_ALARM_TITLE,ORG_ALARM_STATUS,ORG_ALARM_TYPE,ORG_SEVERITY,ORG_EVENT_TIME,ORG_CLEAR_TIME,ORG_ALARM_ID,ORG_ALARM_CODE,ORG_ALARM_CODE_NAME,ORG_SPECIFIC_PROBLEM_ID,ORG_SPECIFIC_PROBLEM,ORG_NE_UID,ORG_NE_NAME,ORG_NE_TYPE,ORG_OBJECT_UID,ORG_OBJECT_NAME,ORG_OBJECT_TYPE,ORG_LOCATION_INFO,ORG_ADDITIONAL_INFO,ORG_NETWORK_SLICE_ID,PHD_ALARM_SIGNAL,PHD_FP,PHD_C_FP,PHD_DISCOVERY_TIME,PHD_VENDOR_ID,PHD_ALARM_SOURCE,PHD_GID,RES_CITY_ID,RES_AREA_ID,RES_SITE_ID,RES_ROOM_ID,STD_SPECIALTY_TYPE,STD_VENDOR_VERSION,STD_INMS_SEVERITY,STD_INMS_ALARM_ID,STD_ALARM_EXPLAIN,STD_ALARM_REPAIR_ADVICE,STD_ALARM_TYPE,STD_ALARM_LOGIC_TYPE,STD_ALARM_LOGIC_SUB_TYPE,STD_EFFECT_NE,STD_EFFECT_SERVICE,STD_ALARM_NAME,STD_SEND_IT_FLAG,STD_FLAG,PROJ_NUM,PROJ_NAME,PRS_ACK_FLAG,PRS_FORWARD_FLAG,PRS_TT_ID,PRS_TT_FLAG,PRS_NOTICE_FLAG,PRS_PREPROCESS_FLAG,PRS_RELATED_RULE_SET_ID,PRS_RELATED_RULE_TYPE,PRS_RELATED_RULE_ID,PRS_RELATED_RULE_NAME,PRS_RELATED_DATE,PRS_P_ALARM_SIGNAL,PRS_COMPRESSION_KEY,PRS_COMPRESSION_ALARM_NUM,PRS_SCAN_FLAG,PRS_FILTER_FLAG,PRS_REMARK_FLAG,PRS_LOAD_DB_TIME) VALUES \n");

            for (int i = 0; i < signalInsertAlarms; i++) {
                String newData = data.replace("{EVENT_TIME}", sdf.format(randomDate(startTimeStrs[0], startTimeStrs[1])));
                newData = newData.replace("{ALARM_SIGNAL}", startAlarmSignal + time * signalInsertAlarms + i + "");
                sb.append(newData);
                sb.append(",");
            }

            jdbcTemplate.update(sb.toString().substring(0, sb.toString().length() - 1));
        }

        System.out.println("插入共用时： " + (System.currentTimeMillis() - startTime));

    }

    @PutMapping("update")
    public void updateRelationAndClear() {
        this.updateRelation();
        this.updateCleared();
    }

    @PutMapping("insertRC")
    public void insertRelationAndClear() {
        this.insertRelation();
        this.insertClear();
    }

    @PutMapping("s1")
    public void select1() {

        String sql1 = "select `ID`, `DB_ID`, `ORG_ALARM_TITLE`, `ORG_ALARM_STATUS`, `ORG_ALARM_TYPE` , `ORG_SEVERITY`, `ORG_EVENT_TIME`, `ORG_ALARM_ID`, `ORG_SPECIFIC_PROBLEM_ID`, `ORG_SPECIFIC_PROBLEM` , `ORG_NE_UID`, `ORG_NE_NAME`, `ORG_NE_TYPE`, `ORG_CLEAR_TIME`, `ORG_OBJECT_UID` , `ORG_OBJECT_NAME`, `ORG_OBJECT_TYPE`, `ORG_LOCATION_INFO`, `ORG_ADDITIONAL_INFO`, `ORG_NETWORK_SLICE_ID` , `PHD_ALARM_SIGNAL`, `PHD_FP`, `PHD_C_FP`, `PHD_DISCOVERY_TIME`, `PHD_VENDOR_ID` , `PHD_GID`, `RES_CITY_ID`, `RES_SITE_ID`, `RES_ROOM_ID`, `STD_SPECIALTY_TYPE` , `STD_VENDOR_VERSION`, `STD_INMS_SEVERITY`, `STD_INMS_ALARM_ID`, `STD_ALARM_EXPLAIN`, `STD_ALARM_REPAIR_ADVICE` , `STD_ALARM_TYPE`, `STD_ALARM_LOGIC_TYPE`, `STD_ALARM_LOGIC_SUB_TYPE`, `STD_EFFECT_NE`, `STD_EFFECT_SERVICE` , `STD_ALARM_NAME`, `STD_SEND_IT_FLAG`, `STD_FLAG`, `PROJ_NUM`, `PROJ_NAME` , `PRS_ACK_FLAG`, `PRS_FORWARD_FLAG`, `PRS_TT_ID`, `PRS_TT_FLAG`, `PRS_NOTICE_FLAG` , `PRS_PREPROCESS_FLAG`, `PRS_RELATED_RULE_TYPE`, `PRS_RELATED_RULE_ID`, `PRS_RELATED_RULE_NAME`, `PRS_RELATED_DATE` , `PRS_P_ALARM_SIGNAL`, `PRS_COMPRESSION_KEY`, `PRS_COMPRESSION_ALARM_NUM`, `PRS_REMARK_FLAG`, `PRS_LOAD_DB_TIME` from fm_alarm t where 1 = 1 and ORG_EVENT_TIME > '2020-07-17 00:00:00' and ORG_EVENT_TIME < '2020-07-17 23:59:59' order by ORG_EVENT_TIME desc limit 0, 500";
        String sql2 = "select `ID`, `DB_ID`, `ORG_ALARM_TITLE`, `ORG_ALARM_STATUS`, `ORG_ALARM_TYPE` , `ORG_SEVERITY`, `ORG_EVENT_TIME`, `ORG_ALARM_ID`, `ORG_SPECIFIC_PROBLEM_ID`, `ORG_SPECIFIC_PROBLEM` , `ORG_NE_UID`, `ORG_NE_NAME`, `ORG_NE_TYPE`, `ORG_CLEAR_TIME`, `ORG_OBJECT_UID` , `ORG_OBJECT_NAME`, `ORG_OBJECT_TYPE`, `ORG_LOCATION_INFO`, `ORG_ADDITIONAL_INFO`, `ORG_NETWORK_SLICE_ID` , `PHD_ALARM_SIGNAL`, `PHD_FP`, `PHD_C_FP`, `PHD_DISCOVERY_TIME`, `PHD_VENDOR_ID` , `PHD_GID`, `RES_CITY_ID`, `RES_SITE_ID`, `RES_ROOM_ID`, `STD_SPECIALTY_TYPE` , `STD_VENDOR_VERSION`, `STD_INMS_SEVERITY`, `STD_INMS_ALARM_ID`, `STD_ALARM_EXPLAIN`, `STD_ALARM_REPAIR_ADVICE` , `STD_ALARM_TYPE`, `STD_ALARM_LOGIC_TYPE`, `STD_ALARM_LOGIC_SUB_TYPE`, `STD_EFFECT_NE`, `STD_EFFECT_SERVICE` , `STD_ALARM_NAME`, `STD_SEND_IT_FLAG`, `STD_FLAG`, `PROJ_NUM`, `PROJ_NAME` , `PRS_ACK_FLAG`, `PRS_FORWARD_FLAG`, `PRS_TT_ID`, `PRS_TT_FLAG`, `PRS_NOTICE_FLAG` , `PRS_PREPROCESS_FLAG`, `PRS_RELATED_RULE_TYPE`, `PRS_RELATED_RULE_ID`, `PRS_RELATED_RULE_NAME`, `PRS_RELATED_DATE` , `PRS_P_ALARM_SIGNAL`, `PRS_COMPRESSION_KEY`, `PRS_COMPRESSION_ALARM_NUM`, `PRS_REMARK_FLAG`, `PRS_LOAD_DB_TIME` from fm_alarm t where 1 = 1 and ORG_EVENT_TIME > '2020-07-17 00:00:00' and ORG_EVENT_TIME < '2020-07-17 23:59:59' and ORG_ALARM_STATUS = '0' order by ORG_EVENT_TIME desc limit 0, 500";
        String sql3 = "select `ID`, `DB_ID`, `ORG_ALARM_TITLE`, `ORG_ALARM_STATUS`, `ORG_ALARM_TYPE` , `ORG_SEVERITY`, `ORG_EVENT_TIME`, `ORG_ALARM_ID`, `ORG_SPECIFIC_PROBLEM_ID`, `ORG_SPECIFIC_PROBLEM` , `ORG_NE_UID`, `ORG_NE_NAME`, `ORG_NE_TYPE`, `ORG_CLEAR_TIME`, `ORG_OBJECT_UID` , `ORG_OBJECT_NAME`, `ORG_OBJECT_TYPE`, `ORG_LOCATION_INFO`, `ORG_ADDITIONAL_INFO`, `ORG_NETWORK_SLICE_ID` , `PHD_ALARM_SIGNAL`, `PHD_FP`, `PHD_C_FP`, `PHD_DISCOVERY_TIME`, `PHD_VENDOR_ID` , `PHD_GID`, `RES_CITY_ID`, `RES_SITE_ID`, `RES_ROOM_ID`, `STD_SPECIALTY_TYPE` , `STD_VENDOR_VERSION`, `STD_INMS_SEVERITY`, `STD_INMS_ALARM_ID`, `STD_ALARM_EXPLAIN`, `STD_ALARM_REPAIR_ADVICE` , `STD_ALARM_TYPE`, `STD_ALARM_LOGIC_TYPE`, `STD_ALARM_LOGIC_SUB_TYPE`, `STD_EFFECT_NE`, `STD_EFFECT_SERVICE` , `STD_ALARM_NAME`, `STD_SEND_IT_FLAG`, `STD_FLAG`, `PROJ_NUM`, `PROJ_NAME` , `PRS_ACK_FLAG`, `PRS_FORWARD_FLAG`, `PRS_TT_ID`, `PRS_TT_FLAG`, `PRS_NOTICE_FLAG` , `PRS_PREPROCESS_FLAG`, `PRS_RELATED_RULE_TYPE`, `PRS_RELATED_RULE_ID`, `PRS_RELATED_RULE_NAME`, `PRS_RELATED_DATE` , `PRS_P_ALARM_SIGNAL`, `PRS_COMPRESSION_KEY`, `PRS_COMPRESSION_ALARM_NUM`, `PRS_REMARK_FLAG`, `PRS_LOAD_DB_TIME` from fm_alarm t where 1 = 1 and ORG_EVENT_TIME > '2020-07-17 00:00:00' and ORG_EVENT_TIME < '2020-07-17 23:59:59' and ORG_ALARM_STATUS = '1' order by ORG_EVENT_TIME desc limit 0, 500";

        Long startTime = System.currentTimeMillis();
        jdbcTemplate.queryForList(sql1);
        Long endTime1 = System.currentTimeMillis();
        System.out.println("查询18号全部告警耗时： " + (endTime1 - startTime));
        jdbcTemplate.queryForList(sql2);
        Long endTime2 = System.currentTimeMillis();
        System.out.println("查询18号清除告警耗时： " + (endTime2 - endTime1));
        jdbcTemplate.queryForList(sql3);
        Long endTime3 = System.currentTimeMillis();
        System.out.println("查询18号活动告警耗时： " + (endTime3 - endTime2));
    }

    @PutMapping("s2")
    public void select2() {
        String sql1 = "select t.`ID`, t.`DB_ID`, t.`ORG_ALARM_TITLE`, fac.`ORG_ALARM_STATUS`, t.`ORG_ALARM_TYPE` , t.`ORG_SEVERITY`, t.`ORG_EVENT_TIME`, t.`ORG_ALARM_ID`, t.`ORG_SPECIFIC_PROBLEM_ID`, t.`ORG_SPECIFIC_PROBLEM` , t.`ORG_NE_UID`, t.`ORG_NE_NAME`, t.`ORG_NE_TYPE`, fac.`ORG_CLEAR_TIME`, t.`ORG_OBJECT_UID` , t.`ORG_OBJECT_NAME`, t.`ORG_OBJECT_TYPE`, t.`ORG_LOCATION_INFO`, t.`ORG_ADDITIONAL_INFO`, t.`ORG_NETWORK_SLICE_ID` , t.`PHD_ALARM_SIGNAL`, t.`PHD_FP`, t.`PHD_C_FP`, t.`PHD_DISCOVERY_TIME`, t.`PHD_VENDOR_ID` , t.`PHD_GID`, t.`RES_CITY_ID`, t.`RES_SITE_ID`, t.`RES_ROOM_ID`, t.`STD_SPECIALTY_TYPE` , t.`STD_VENDOR_VERSION`, t.`STD_INMS_SEVERITY`, t.`STD_INMS_ALARM_ID`, t.`STD_ALARM_EXPLAIN`, t.`STD_ALARM_REPAIR_ADVICE` , t.`STD_ALARM_TYPE`, t.`STD_ALARM_LOGIC_TYPE`, t.`STD_ALARM_LOGIC_SUB_TYPE`, t.`STD_EFFECT_NE`, t.`STD_EFFECT_SERVICE` , t.`STD_ALARM_NAME`, t.`STD_SEND_IT_FLAG`, t.`STD_FLAG`, t.`PROJ_NUM`, t.`PROJ_NAME` , t.`PRS_ACK_FLAG`, t.`PRS_FORWARD_FLAG`, t.`PRS_TT_ID`, t.`PRS_TT_FLAG`, t.`PRS_NOTICE_FLAG` , t.`PRS_PREPROCESS_FLAG`, far.`PRS_RELATED_RULE_TYPE`, far.`PRS_RELATED_RULE_ID`, far.`PRS_RELATED_RULE_NAME`, far.`PRS_RELATED_DATE` , far.`PRS_P_ALARM_SIGNAL`, t.`PRS_COMPRESSION_KEY`, t.`PRS_COMPRESSION_ALARM_NUM`, t.`PRS_REMARK_FLAG`, t.`PRS_LOAD_DB_TIME` from fm_alarm2 t left join fm_alarm2_relative far on t.PHD_ALARM_SIGNAL = far.PHD_ALARM_SIGNAL left join fm_alarm2_clear fac on t.PHD_ALARM_SIGNAL =fac.PHD_ALARM_SIGNAL where t.ORG_EVENT_TIME > '2020-07-18 00:00:00' and t.ORG_EVENT_TIME < '2020-07-18 23:59:59' order by t.ORG_EVENT_TIME desc limit 0, 500";
        String sql2 = "select t.`ID`, t.`DB_ID`, t.`ORG_ALARM_TITLE`, '1', t.`ORG_ALARM_TYPE` , t.`ORG_SEVERITY`, t.`ORG_EVENT_TIME`, t.`ORG_ALARM_ID`, t.`ORG_SPECIFIC_PROBLEM_ID`, t.`ORG_SPECIFIC_PROBLEM` , t.`ORG_NE_UID`, t.`ORG_NE_NAME`, t.`ORG_NE_TYPE`, t.`ORG_OBJECT_UID` , t.`ORG_OBJECT_NAME`, t.`ORG_OBJECT_TYPE`, t.`ORG_LOCATION_INFO`, t.`ORG_ADDITIONAL_INFO`, t.`ORG_NETWORK_SLICE_ID` , t.`PHD_ALARM_SIGNAL`, t.`PHD_FP`, t.`PHD_C_FP`, t.`PHD_DISCOVERY_TIME`, t.`PHD_VENDOR_ID` , t.`PHD_GID`, t.`RES_CITY_ID`, t.`RES_SITE_ID`, t.`RES_ROOM_ID`, t.`STD_SPECIALTY_TYPE` , t.`STD_VENDOR_VERSION`, t.`STD_INMS_SEVERITY`, t.`STD_INMS_ALARM_ID`, t.`STD_ALARM_EXPLAIN`, t.`STD_ALARM_REPAIR_ADVICE` , t.`STD_ALARM_TYPE`, t.`STD_ALARM_LOGIC_TYPE`, t.`STD_ALARM_LOGIC_SUB_TYPE`, t.`STD_EFFECT_NE`, t.`STD_EFFECT_SERVICE` , t.`STD_ALARM_NAME`, t.`STD_SEND_IT_FLAG`, t.`STD_FLAG`, t.`PROJ_NUM`, t.`PROJ_NAME` , t.`PRS_ACK_FLAG`, t.`PRS_FORWARD_FLAG`, t.`PRS_TT_ID`, t.`PRS_TT_FLAG`, t.`PRS_NOTICE_FLAG` , t.`PRS_PREPROCESS_FLAG`, far.`PRS_RELATED_RULE_TYPE`, far.`PRS_RELATED_RULE_ID`, far.`PRS_RELATED_RULE_NAME`, far.`PRS_RELATED_DATE` , far.`PRS_P_ALARM_SIGNAL`, t.`PRS_COMPRESSION_KEY`, t.`PRS_COMPRESSION_ALARM_NUM`, t.`PRS_REMARK_FLAG`, t.`PRS_LOAD_DB_TIME` from fm_alarm2 t  left join fm_alarm2_relative far on t.PHD_ALARM_SIGNAL = far.PHD_ALARM_SIGNAL where EXISTS (select 1 from fm_alarm2_clear fac where t.PHD_ALARM_SIGNAL = fac.PHD_ALARM_SIGNAL ) and t.ORG_EVENT_TIME > '2020-07-18 00:00:00' and t.ORG_EVENT_TIME < '2020-07-18 23:59:59' order by t.ORG_EVENT_TIME desc limit 0, 500";
        String sql3 = "select t.`ID`, t.`DB_ID`, t.`ORG_ALARM_TITLE`, t.`ORG_ALARM_TYPE` , t.`ORG_SEVERITY`, t.`ORG_EVENT_TIME`, t.`ORG_ALARM_ID`, t.`ORG_SPECIFIC_PROBLEM_ID`, t.`ORG_SPECIFIC_PROBLEM` , t.`ORG_NE_UID`, t.`ORG_NE_NAME`, t.`ORG_NE_TYPE`, t.`ORG_OBJECT_UID` , t.`ORG_OBJECT_NAME`, t.`ORG_OBJECT_TYPE`, t.`ORG_LOCATION_INFO`, t.`ORG_ADDITIONAL_INFO`, t.`ORG_NETWORK_SLICE_ID` , t.`PHD_ALARM_SIGNAL`, t.`PHD_FP`, t.`PHD_C_FP`, t.`PHD_DISCOVERY_TIME`, t.`PHD_VENDOR_ID` , t.`PHD_GID`, t.`RES_CITY_ID`, t.`RES_SITE_ID`, t.`RES_ROOM_ID`, t.`STD_SPECIALTY_TYPE` , t.`STD_VENDOR_VERSION`, t.`STD_INMS_SEVERITY`, t.`STD_INMS_ALARM_ID`, t.`STD_ALARM_EXPLAIN`, t.`STD_ALARM_REPAIR_ADVICE` , t.`STD_ALARM_TYPE`, t.`STD_ALARM_LOGIC_TYPE`, t.`STD_ALARM_LOGIC_SUB_TYPE`, t.`STD_EFFECT_NE`, t.`STD_EFFECT_SERVICE` , t.`STD_ALARM_NAME`, t.`STD_SEND_IT_FLAG`, t.`STD_FLAG`, t.`PROJ_NUM`, t.`PROJ_NAME` , t.`PRS_ACK_FLAG`, t.`PRS_FORWARD_FLAG`, t.`PRS_TT_ID`, t.`PRS_TT_FLAG`, t.`PRS_NOTICE_FLAG` , t.`PRS_PREPROCESS_FLAG`, far.`PRS_RELATED_RULE_TYPE`, far.`PRS_RELATED_RULE_ID`, far.`PRS_RELATED_RULE_NAME`, far.`PRS_RELATED_DATE` , far.`PRS_P_ALARM_SIGNAL`, t.`PRS_COMPRESSION_KEY`, t.`PRS_COMPRESSION_ALARM_NUM`, t.`PRS_REMARK_FLAG`, t.`PRS_LOAD_DB_TIME` from fm_alarm2 t  left join fm_alarm2_relative far on t.PHD_ALARM_SIGNAL = far.PHD_ALARM_SIGNAL  where not EXISTS (select 1 from fm_alarm2_clear fac where t.PHD_ALARM_SIGNAL = fac.PHD_ALARM_SIGNAL ) and t.ORG_EVENT_TIME > '2020-07-18 00:00:00' and t.ORG_EVENT_TIME < '2020-07-18 23:59:59' order by t.ORG_EVENT_TIME desc limit 0, 500";

        Long startTime = System.currentTimeMillis();
        System.out.println(jdbcTemplate.queryForList(sql1).size());
        Long endTime1 = System.currentTimeMillis();
        System.out.println("fm_alarm2查询18号全部告警耗时： " + (endTime1 - startTime));
        System.out.println(jdbcTemplate.queryForList(sql2).size());
        Long endTime2 = System.currentTimeMillis();
        System.out.println("fm_alarm2查询18号清除告警耗时： " + (endTime2 - endTime1));
        System.out.println(jdbcTemplate.queryForList(sql3).size());
        Long endTime3 = System.currentTimeMillis();
        System.out.println("fm_alarm2查询18号活动告警耗时： " + (endTime3 - endTime2));
    }

    @PutMapping("s3")
    public void select3() {

        String sql1 = "select `ID`, `DB_ID`, `ORG_ALARM_TITLE`, `ORG_ALARM_STATUS`, `ORG_ALARM_TYPE` , `ORG_SEVERITY`, `ORG_EVENT_TIME`, `ORG_ALARM_ID`, `ORG_SPECIFIC_PROBLEM_ID`, `ORG_SPECIFIC_PROBLEM` , `ORG_NE_UID`, `ORG_NE_NAME`, `ORG_NE_TYPE`, `ORG_CLEAR_TIME`, `ORG_OBJECT_UID` , `ORG_OBJECT_NAME`, `ORG_OBJECT_TYPE`, `ORG_LOCATION_INFO`, `ORG_ADDITIONAL_INFO`, `ORG_NETWORK_SLICE_ID` , `PHD_ALARM_SIGNAL`, `PHD_FP`, `PHD_C_FP`, `PHD_DISCOVERY_TIME`, `PHD_VENDOR_ID` , `PHD_GID`, `RES_CITY_ID`, `RES_SITE_ID`, `RES_ROOM_ID`, `STD_SPECIALTY_TYPE` , `STD_VENDOR_VERSION`, `STD_INMS_SEVERITY`, `STD_INMS_ALARM_ID`, `STD_ALARM_EXPLAIN`, `STD_ALARM_REPAIR_ADVICE` , `STD_ALARM_TYPE`, `STD_ALARM_LOGIC_TYPE`, `STD_ALARM_LOGIC_SUB_TYPE`, `STD_EFFECT_NE`, `STD_EFFECT_SERVICE` , `STD_ALARM_NAME`, `STD_SEND_IT_FLAG`, `STD_FLAG`, `PROJ_NUM`, `PROJ_NAME` , `PRS_ACK_FLAG`, `PRS_FORWARD_FLAG`, `PRS_TT_ID`, `PRS_TT_FLAG`, `PRS_NOTICE_FLAG` , `PRS_PREPROCESS_FLAG`, `PRS_RELATED_RULE_TYPE`, `PRS_RELATED_RULE_ID`, `PRS_RELATED_RULE_NAME`, `PRS_RELATED_DATE` , `PRS_P_ALARM_SIGNAL`, `PRS_COMPRESSION_KEY`, `PRS_COMPRESSION_ALARM_NUM`, `PRS_REMARK_FLAG`, `PRS_LOAD_DB_TIME` from fm_alarm2 t where 1 = 1 and ORG_EVENT_TIME > '2020-07-18 00:00:00' and ORG_EVENT_TIME < '2020-07-18 23:59:59' order by ORG_EVENT_TIME desc limit 0, 500";
        String sql2 = "select PHD_ALARM_SIGNAL, ORG_ALARM_STATUS, ORG_CLEAR_TIME from fm_alarm2_clear t where 1 = 1 and ORG_CLEAR_TIME > '2020-07-18 00:00:00' and ORG_CLEAR_TIME < '2020-07-18 23:59:59' and ORG_ALARM_STATUS = '0' order by ORG_CLEAR_TIME desc limit 0, 500";
        String sql3 = "select `ID`, `DB_ID`, `ORG_ALARM_TITLE`, `ORG_ALARM_STATUS`, `ORG_ALARM_TYPE` , `ORG_SEVERITY`, `ORG_EVENT_TIME`, `ORG_ALARM_ID`, `ORG_SPECIFIC_PROBLEM_ID`, `ORG_SPECIFIC_PROBLEM` , `ORG_NE_UID`, `ORG_NE_NAME`, `ORG_NE_TYPE`, `ORG_CLEAR_TIME`, `ORG_OBJECT_UID` , `ORG_OBJECT_NAME`, `ORG_OBJECT_TYPE`, `ORG_LOCATION_INFO`, `ORG_ADDITIONAL_INFO`, `ORG_NETWORK_SLICE_ID` , `PHD_ALARM_SIGNAL`, `PHD_FP`, `PHD_C_FP`, `PHD_DISCOVERY_TIME`, `PHD_VENDOR_ID` , `PHD_GID`, `RES_CITY_ID`, `RES_SITE_ID`, `RES_ROOM_ID`, `STD_SPECIALTY_TYPE` , `STD_VENDOR_VERSION`, `STD_INMS_SEVERITY`, `STD_INMS_ALARM_ID`, `STD_ALARM_EXPLAIN`, `STD_ALARM_REPAIR_ADVICE` , `STD_ALARM_TYPE`, `STD_ALARM_LOGIC_TYPE`, `STD_ALARM_LOGIC_SUB_TYPE`, `STD_EFFECT_NE`, `STD_EFFECT_SERVICE` , `STD_ALARM_NAME`, `STD_SEND_IT_FLAG`, `STD_FLAG`, `PROJ_NUM`, `PROJ_NAME` , `PRS_ACK_FLAG`, `PRS_FORWARD_FLAG`, `PRS_TT_ID`, `PRS_TT_FLAG`, `PRS_NOTICE_FLAG` , `PRS_PREPROCESS_FLAG`, `PRS_RELATED_RULE_TYPE`, `PRS_RELATED_RULE_ID`, `PRS_RELATED_RULE_NAME`, `PRS_RELATED_DATE` , `PRS_P_ALARM_SIGNAL`, `PRS_COMPRESSION_KEY`, `PRS_COMPRESSION_ALARM_NUM`, `PRS_REMARK_FLAG`, `PRS_LOAD_DB_TIME` from fm_alarm2 t where 1 = 1 and ORG_EVENT_TIME > '2020-07-18 00:00:00' and ORG_EVENT_TIME < '2020-07-18 23:59:59' order by ORG_EVENT_TIME desc limit 0, 1500";

        String sql4 = "select far.PHD_ALARM_SIGNAL, far.PRS_P_ALARM_SIGNAL , far.PRS_RELATED_RULE_ID , far.PRS_RELATED_RULE_NAME , far.PRS_RELATED_RULE_SET_ID , far.PRS_RELATED_RULE_TYPE FROM fm_alarm2_relative far WHERE PHD_ALARM_SIGNAL IN (:alarmSignal)";
        String sql5 = "select fac.PHD_ALARM_SIGNAL , fac.ORG_ALARM_STATUS , fac.ORG_CLEAR_TIME from fm_alarm2_clear fac where PHD_ALARM_SIGNAL in (:alarmSignal)";

        String sql6 = "select * from fm_alarm2 fac where PHD_ALARM_SIGNAL in (:alarmSignal)";

        Long startTime = System.currentTimeMillis();
        queryOneTime(sql1, sql4, sql5);
        Long endTime1 = System.currentTimeMillis();
        System.out.println("查询18号全部告警耗时： " + (endTime1 - startTime));
        queryOneTime(sql2, sql4, sql6);
        Long endTime2 = System.currentTimeMillis();
        System.out.println("查询18号清除告警耗时： " + (endTime2 - endTime1));
        queryOneTime(sql3, sql4, sql5);
        Long endTime3 = System.currentTimeMillis();
        System.out.println("查询18号活动告警耗时： " + (endTime3 - endTime2));
    }

    @PutMapping("d")
    public void delete() {
        deleteC();
        deleteA();
        deleteR();
    }

    public void deleteR() {
        Long startTime = System.currentTimeMillis();

        int startAlarm = 0;
        int signalInsertAlarms = 300;
        int updateTime = 1000;

        for (int time = 0; time < updateTime; time++) {
            StringBuffer sb = new StringBuffer("delete from fm_alarm2_relative WHERE PHD_ALARM_SIGNAL IN (");
            for (int i = 0; i < signalInsertAlarms; i++) {
                sb.append("'");
                sb.append(startAlarm + signalInsertAlarms * time + i + "");
                sb.append("',");
            }
            sb.deleteCharAt(sb.toString().length()-1);
            sb.append(")");

            jdbcTemplate.update(sb.toString());
        }

        Long endTime = System.currentTimeMillis();

        System.out.println("删除关联表关联共耗时： " + (endTime - startTime));
    }


    public void deleteC() {
        Long startTime = System.currentTimeMillis();

        int startAlarm = 0;
        int signalInsertAlarms = 300;
        int updateTime = 2000;

        for (int time = 0; time < updateTime; time++) {
            StringBuffer sb = new StringBuffer("delete from fm_alarm2_clear WHERE PHD_ALARM_SIGNAL IN (");
            for (int i = 0; i < signalInsertAlarms; i++) {
                sb.append("'");
                sb.append(startAlarm + signalInsertAlarms * time + i + "");
                sb.append("',");
            }
            sb.deleteCharAt(sb.toString().length()-1);
            sb.append(")");

            jdbcTemplate.update(sb.toString());
        }

        Long endTime = System.currentTimeMillis();

        System.out.println("删除清除表清除共耗时： " + (endTime - startTime));
    }

    public void deleteA() {
        Long startTime = System.currentTimeMillis();

        int startAlarm = 0;
        int signalInsertAlarms = 300;
        int updateTime = 2000;

        for (int time = 0; time < updateTime; time++) {
            StringBuffer sb = new StringBuffer("delete from fm_alarm2 WHERE PHD_ALARM_SIGNAL IN (");
            for (int i = 0; i < signalInsertAlarms; i++) {
                sb.append("'");
                sb.append(startAlarm + signalInsertAlarms * time + i + "");
                sb.append("',");
            }
            sb.deleteCharAt(sb.toString().length()-1);
            sb.append(")");

            System.out.println(jdbcTemplate.update(sb.toString()));
        }

        Long endTime = System.currentTimeMillis();

        System.out.println("删除活动表清除共耗时： " + (endTime - startTime));
    }

    public void queryOneTime(String tmpSql, String sql4, String sql5) {
        List<Map<String, Object>> result1 = jdbcTemplate.queryForList(tmpSql);
        Map<String, Map<String, Object>> mapResult1 = list2Map(result1);
        List<String> alarmSignalList1 = new ArrayList<>(mapResult1.keySet());
        Map<String, Object> sqlParam = new HashedMap();
        sqlParam.put("alarmSignal", alarmSignalList1);
        List<Map<String, Object>> tempResult1 = namedParameterJdbcTemplate.queryForList(sql4, sqlParam);
        addResultToMap(mapResult1, tempResult1);
        List<Map<String, Object>> tempResult2 = namedParameterJdbcTemplate.queryForList(sql5, sqlParam);
        addResultToMap(mapResult1, tempResult2);
        System.out.println(mapResult1.size());
    }

    public void addResultToMap(Map<String, Map<String, Object>> allResult, List<Map<String, Object>> tmpResult) {
        for (Map<String, Object> tmpMap: tmpResult) {
            String alarmSignal = tmpMap.get("PHD_ALARM_SIGNAL").toString();
            allResult.get(alarmSignal).putAll(tmpMap);
        }
    }

    public Map<String, Map<String, Object>> list2Map(List<Map<String, Object>> param) {
        Map<String, Map<String, Object>> result = new HashMap<>();
        for (Map<String, Object> obj: param) {
            result.put(obj.get("PHD_ALARM_SIGNAL").toString(), obj);
        }
        return result;
    }

    public void insertRelation() {
        Long startTime = System.currentTimeMillis();

        int signalInsertAlarms = 300;
        int insertTime = 1000;

        for (int time = 0; time < insertTime; time++) {
            StringBuffer sb = new StringBuffer("INSERT INTO fm_alarm2_relative (PHD_ALARM_SIGNAL,PRS_RELATED_RULE_TYPE,PRS_RELATED_RULE_ID,PRS_RELATED_RULE_NAME,PRS_RELATED_DATE,PRS_P_ALARM_SIGNAL) VALUES \n");

            for (int i = 0; i < signalInsertAlarms; i++) {
                String newData = relationData.replace("{ALARM_SIGNAL}", time * signalInsertAlarms + i + "");
                newData = newData.replace("{P_ALARM_SIGNAL}", time + "");
                sb.append(newData);
                sb.append(",");
            }

            jdbcTemplate.update(sb.toString().substring(0, sb.toString().length() - 1));
        }

        Long endTime = System.currentTimeMillis();

        System.out.println("更新关联共耗时： " + (endTime - startTime));
    }

    public void insertClear() {
        Long startTime = System.currentTimeMillis();

        int signalInsertAlarms = 300;
        int insertTime = 2000;

        for (int time = 0; time < insertTime; time++) {
            StringBuffer sb = new StringBuffer("INSERT INTO fm_alarm2_clear (PHD_ALARM_SIGNAL,PHD_C_FP,ORG_CLEAR_TIME) VALUES \n");

            for (int i = 0; i < signalInsertAlarms; i++) {
                String newData = clearData.replace("{EVENT_TIME}", sdf.format(randomDate(startTimeStrs[0], startTimeStrs[1])));
                newData = newData.replace("{ALARM_SIGNAL}", time * signalInsertAlarms + i + "");
                sb.append(newData);
                sb.append(",");
            }

            jdbcTemplate.update(sb.toString().substring(0, sb.toString().length() - 1));
        }

        Long endTime = System.currentTimeMillis();

        System.out.println("更新清除共耗时： " + (endTime - startTime));
    }

    public void updateRelation() {
        Long startTime = System.currentTimeMillis();

        int startAlarm = 0;
        int signalInsertAlarms = 300;
        int updateTime = 1000;

        for (int time = 0; time < updateTime; time++) {
            StringBuffer sb = new StringBuffer("UPDATE fm_alarm SET PRS_RELATED_RULE_NAME = 'TEST_RULE_NAME' , PRS_RELATED_RULE_TYPE = '1', PRS_RELATED_RULE_ID = '11'  WHERE PHD_ALARM_SIGNAL IN (");
            for (int i = 0; i < signalInsertAlarms; i++) {
                sb.append("'");
                sb.append(startAlarm + signalInsertAlarms * time + i + "");
                sb.append("',");
            }
            sb.deleteCharAt(sb.toString().length()-1);
            sb.append(")");

            System.out.println(jdbcTemplate.update(sb.toString()));
        }

        Long endTime = System.currentTimeMillis();

        System.out.println("更新关联共耗时： " + (endTime - startTime));
    }

    public void updateCleared() {
        Long startTime = System.currentTimeMillis();

        int startAlarm = 0;
        int signalInsertAlarms = 300;
        int updateTime = 3000;

        for (int time = 0; time < updateTime; time++) {
            StringBuffer sb = new StringBuffer("UPDATE fm_alarm SET ORG_ALARM_STATUS = '0' , ORG_CLEAR_TIME = '2020-7-31 16:16:00'  WHERE PHD_ALARM_SIGNAL IN (");
            for (int i = 0; i < signalInsertAlarms; i++) {
                sb.append("'");
                sb.append(startAlarm + signalInsertAlarms * time + i + "");
                sb.append("',");
            }
            sb.deleteCharAt(sb.toString().length()-1);
            sb.append(")");

            System.out.println(jdbcTemplate.update(sb.toString()));
        }

        Long endTime = System.currentTimeMillis();

        System.out.println("更新清除共耗时： " + (endTime - startTime));
    }

    /**
     * 生成随机时间
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public Date randomDate(String beginDate, String endDate) {
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
