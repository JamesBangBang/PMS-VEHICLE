package com.starnetsecurity.parkClientServer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.parkClientServer.entity.MemberKind;
import com.starnetsecurity.parkClientServer.entityEnum.payStatusEnum;
import com.starnetsecurity.parkClientServer.entityEnum.payTypeEnum;
import com.starnetsecurity.parkClientServer.service.ReportStatisticsService;
import org.apache.commons.collections.map.HashedMap;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 陈峰 on 2017/10/13.
 */
@Service
public class ReportStatisticsServiceImpl implements ReportStatisticsService {
    @Autowired
    HibernateBaseDao baseDao;

    @Override
    public List<Map<String, Object>> getChargeTypeInfo(String depId, String carparkId) throws BizException{
        String sql = "select kind_name kindName,package_kind packageKind from member_kind " +
                "where use_mark >= 0 ";
        //sql = putTogetherInSql(sql, "department_id", "depId", depId);
        sql = sql + " group by kind_name";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        //setParameterToQuery(sqlQuery, "depId", depId);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @Override
    public List prePayment(Integer start, Integer length, String orderOrechargeCarno, String chargeMemberKindName, String orderPrechargeCarparkName, String startTime, String endTime, String excelStart, String excelEnd) {

        String sql = "SELECT t1.order_precharge_carno,t1.order_precharge_carpark_name," +
                "t1.charge_member_kind_name,t1.order_precharge_operator_name," +
                "t1.order_precharge_receivable_amount,t1.order_precharge_actual_amount," +
                "t1.order_precharge_free_amount," +
                "CASE WHEN t1.free_reason IS NULL " +
                "THEN '' " +
                "ELSE t1.free_reason END " +
                "free_reason," +
                "CASE WHEN  t1.memo IS NULL " +
                "THEN '' " +
                "ELSE t1.memo END " +
                "memo," +
                "t1.order_precharge_time,t1.order_precharge_id FROM order_precharge_record  t1 Where 1=1 ";
        sql = putTogetherLikeSql(sql, "t1.order_precharge_carno", "orderPrechargeCarno", orderOrechargeCarno);
        sql=putTogetherEqualSql(sql,"t1.charge_member_kind_id","chargeMemberKindName",chargeMemberKindName);
        sql = putTogetherLikeSql(sql, "t1.order_precharge_carpark_name", "orderPrechargeCarparkName", orderPrechargeCarparkName);
        sql=putTogetherRESql(sql,"t1.order_precharge_time","startTime",startTime);
        sql=putTogetherLESql(sql,"t1.order_precharge_time","endTime",endTime);
        sql=putTogetherRESql(sql,"t1.add_time","excelStart",excelStart);
        sql=putTogetherLESql(sql,"t1.add_time","excelEnd",excelEnd);
        sql +=" ORDER BY order_precharge_time DESC";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        setParameterToQueryByLike(sqlQuery, "orderPrechargeCarno", orderOrechargeCarno);
        setParameterToQuery(sqlQuery, "chargeMemberKindName", chargeMemberKindName);
        setParameterToQueryByLike(sqlQuery, "orderPrechargeCarparkName", orderPrechargeCarparkName);
        setParameterToQuery(sqlQuery, "startTime", startTime);
        setParameterToQuery(sqlQuery, "endTime", endTime);
        setParameterToQuery(sqlQuery, "excelStart", excelStart);
        setParameterToQuery(sqlQuery, "excelEnd", excelEnd);
        if (!CommonUtils.isEmpty(start)){
            sqlQuery.setFirstResult(start);
        }
        if(!CommonUtils.isEmpty(length)){
            sqlQuery.setMaxResults(length);
        }
        List<Map<String, Object>> list = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return  list;
    }

    @Override
    public Long countPrePayment(String orderOrechargeCarno, String chargeMemberKindName, String orderPrechargeCarparkName, String startTime, String endTime, String excelStart, String excelEnd) {
        String sql = "SELECT count(1) FROM order_precharge_record  t1 Where 1=1 ";

        sql = putTogetherLikeSql(sql, "t1.order_precharge_carno", "orderPrechargeCarno", orderOrechargeCarno);
        sql=putTogetherEqualSql(sql,"t1.charge_member_kind_id","chargeMemberKindName",chargeMemberKindName);
        sql = putTogetherLikeSql(sql, "t1.order_precharge_carpark_name", "orderPrechargeCarparkName", orderPrechargeCarparkName);
        sql=putTogetherRESql(sql,"t1.order_precharge_time","startTime",startTime);
        sql=putTogetherLESql(sql,"t1.order_precharge_time","endTime",endTime);
        sql=putTogetherRESql(sql,"t1.add_time","excelStart",excelStart);
        sql=putTogetherLESql(sql,"t1.add_time","excelEnd",excelEnd);

        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        setParameterToQueryByLike(sqlQuery, "orderPrechargeCarno", orderOrechargeCarno);
        setParameterToQuery(sqlQuery, "chargeMemberKindName", chargeMemberKindName);
        setParameterToQueryByLike(sqlQuery, "orderPrechargeCarparkName", orderPrechargeCarparkName);
        setParameterToQuery(sqlQuery, "startTime", startTime);
        setParameterToQuery(sqlQuery, "endTime", endTime);
        setParameterToQuery(sqlQuery, "excelStart", excelStart);
        setParameterToQuery(sqlQuery, "excelEnd", excelEnd);

        return ((BigInteger) sqlQuery.uniqueResult()).longValue();

    }
    @Override
    public List<Map<String, Object>> prePaymentType() throws BizException {
        List<Map<String, Object>>list=new ArrayList<>();
        String hql="FROM MemberKind";
        List<MemberKind> memberKindList = (List<MemberKind>) baseDao.queryForList(hql);
        for(MemberKind s:memberKindList){
            String id=s.getId();
            String kindName=s.getKindName();
            Map mk=new HashMap();
            mk.put("id",id);
            mk.put("kind_name",kindName);
            list.add(mk);
        }
        return list;
    }

    @Override
    public List prePaymentList(String orderOrechargeCarno, String chargeMemberKindName, String orderPrechargeCarparkName, String startTime, String endTime,String excelStart, String excelEnd) {

        String hql = "SELECT * FROM order_precharge_record a Where 1=1 \n";
        if (!CommonUtils.isEmpty(orderOrechargeCarno)) {
            hql += " And a.order_precharge_carno like'" + ("%" + orderOrechargeCarno + "%") + "' \n";
        }
        if (!CommonUtils.isEmpty(chargeMemberKindName)) {
            hql += " And a.charge_member_kind_name ='" + chargeMemberKindName+ "' \n";
        }
        if (!CommonUtils.isEmpty(orderPrechargeCarparkName)) {
            hql += " And a.order_precharge_carpark_name = '" +orderPrechargeCarparkName+ "' \n";
        }
        if (!CommonUtils.isEmpty(startTime) && !CommonUtils.isEmpty(endTime)) {
            hql += " And a.order_precharge_time >= '" + startTime + "'And a.order_precharge_time <= '" + endTime + "' \n";
        }
        if (!CommonUtils.isEmpty(excelStart) && !CommonUtils.isEmpty(excelEnd)) {
            hql += " And order_precharge_time  >= '" + excelStart + "'And order_precharge_time  <= '" + excelEnd + "' \n";
        }
        SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public List tranSaction(Integer start, Integer length, String startTime, String endTime, String payType, String payStatus, String excelStart,
                            String excelEnd,String markInfo,String carparkName,String carno) {
        String hql = "SELECT  a.pay_time,a.pay_type,a.pay_status,a.total_fee,a.real_fee,a.discount_fee,a.transaction_mark,"
                   + "b.car_no,b.carpark_name "
                   + "FROM order_transaction a LEFT JOIN order_inout_record b on a.order_id = b.charge_info_id WHERE pay_status = 'HAS_PAID' "
                   + "AND  pay_type NOT IN ('B','D','F')";      //只统计临停的

        if (!CommonUtils.isEmpty(carno)){
            hql += " AND b.car_no LIKE '%" + carno + "%'";
        }

        if (!CommonUtils.isEmpty(carparkName)){
            hql += " And b.carpark_name LIKE '%" + carparkName + "%'";
        }

        if (!CommonUtils.isEmpty(payType)) {
            hql += " And a.pay_type ='" + payType + "' ";
        }
        /*if (!CommonUtils.isEmpty(payStatus)) {
            hql += " And a.pay_status = '" + payStatus + "' ";
        }*/
        if (!CommonUtils.isEmpty(startTime) && !CommonUtils.isEmpty(endTime)) {
            hql += " And b.out_time >= '" + startTime + "' And b.out_time <= '" + endTime + "' ";
        }

        if (!CommonUtils.isEmpty(markInfo)){
            if ("0".equals(markInfo)) {
                hql += " AND (a.transaction_mark = '' or a.transaction_mark IS NULL) ";
            }else {
                hql += " AND a.transaction_mark <> '' ";
            }
        }

        hql += " ORDER BY b.out_time DESC";

        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
        if (!CommonUtils.isEmpty(start)){
            sqlQuery.setFirstResult(start);
        }
        if(!CommonUtils.isEmpty(length)){
            sqlQuery.setMaxResults(length);
        }
        List<Map> list = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        for(Map<String,Object> map : list){
            payTypeEnum typeEnum = payTypeEnum.valueOf(String.valueOf(map.get("pay_type")));
            map.put("pay_type",typeEnum.getDesc());

            /*payStatusEnum payStatusEnum = null;
            payStatusEnum = payStatusEnum.valueOf(String.valueOf(map.get("pay_status")));
            map.put("pay_status",payStatusEnum.getDesc());*/
        }
        return list;
    }

    @Override
    public Long countTranSaction(String startTime, String endTime, String payType, String payStatus, String excelStart,
                                 String excelEnd,String markInfo,String carparkName,String carno) {

        String hql = "SELECT count(1) FROM order_transaction a LEFT JOIN order_inout_record b on a.order_id = b.charge_info_id WHERE pay_status = 'HAS_PAID' "
                   + "AND  pay_type NOT IN ('B','D','F')";      //只统计临停的;

        if (!CommonUtils.isEmpty(carno)){
            hql += " AND b.car_no LIKE '%" + carno + "%'";
        }

        if (!CommonUtils.isEmpty(carparkName)){
            hql += " And b.carpark_name LIKE '%" + carparkName + "%'";
        }

        if (!CommonUtils.isEmpty(payType)) {
            hql += " And a.pay_type ='" + payType + "' ";
        }
        if (!CommonUtils.isEmpty(startTime) && !CommonUtils.isEmpty(endTime)) {
            hql += " And b.out_time >= '" + startTime + "' And b.out_time <= '" + endTime + "' ";
        }
        if (!CommonUtils.isEmpty(markInfo)){
            if ("0".equals(markInfo)) {
                hql += " AND (a.transaction_mark = '' or a.transaction_mark IS NULL) ";
            }else {
                hql += " AND a.transaction_mark <> '' ";
            }
        }

        SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
        return ((BigInteger) query.uniqueResult()).longValue();
    }

    @Override
    public List tranSactionList(String startTime, String endTime, String payType, String payStatus, String excelStart,
                                String excelEnd,String markInfo,String carparkName,String carno) {
        String hql = "SELECT  a.pay_time,a.pay_type,a.pay_status,a.total_fee,a.real_fee,a.discount_fee,a.transaction_mark,"
                + "b.car_no,b.carpark_name "
                + "FROM order_transaction a LEFT JOIN order_inout_record b on a.order_id = b.charge_info_id WHERE pay_status = 'HAS_PAID' "
                + "AND  pay_type NOT IN ('B','D','F')";      //只统计临停的;

        if (!CommonUtils.isEmpty(carno)){
            hql += " AND b.car_no LIKE '%" + carno + "%'";
        }

        if (!CommonUtils.isEmpty(carparkName)){
            hql += " And b.carpark_name LIKE '%" + carparkName + "%'";
        }

        if (!CommonUtils.isEmpty(payType)) {
            hql += " And a.pay_type ='" + payType + "' ";
        }
        if (!CommonUtils.isEmpty(startTime) && !CommonUtils.isEmpty(endTime)) {
            hql += " And b.out_time >= '" + startTime + "' And b.out_time <= '" + endTime + "' ";
        }
        if (!CommonUtils.isEmpty(markInfo)){
            if ("0".equals(markInfo)) {
                hql += " AND (a.transaction_mark = '' or a.transaction_mark IS NULL) ";
            }else {
                hql += " AND a.transaction_mark <> '' ";
            }
        }

        hql += " ORDER BY b.out_time DESC";

        SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);

        List<Map> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        for(Map<String,Object> map : list){
            payTypeEnum typeEnum = payTypeEnum.valueOf(String.valueOf(map.get("pay_type")));
            map.put("pay_type",typeEnum.getDesc());
        }
        return list;
    }

    @Override
    public Map getTranSactionFee(String startTime, String endTime, String payType, String payStatus, String excelStart, String excelEnd,
                                 String markInfo, String carparkName,String carno) {
        JSONObject res = new JSONObject();
        String hql = "SELECT a.pay_type as payType, a.total_fee as totalFee "
                + "FROM order_transaction a LEFT JOIN order_inout_record b on a.order_id = b.charge_info_id WHERE pay_status = 'HAS_PAID' "
                + "AND  pay_type NOT IN ('B','D','F')";      //只统计临停的;


        if (!CommonUtils.isEmpty(carno)){
            hql += " AND b.car_no LIKE '%" + carno + "%'";
        }

        if (!CommonUtils.isEmpty(carparkName)){
            hql += " And b.carpark_name LIKE '%" + carparkName + "%'";
        }

        if (!CommonUtils.isEmpty(payType)) {
            hql += " And a.pay_type ='" + payType + "' ";
        }
        if (!CommonUtils.isEmpty(startTime) && !CommonUtils.isEmpty(endTime)) {
            hql += " And b.out_time >= '" + startTime + "' And b.out_time <= '" + endTime + "' ";
        }

        if (!CommonUtils.isEmpty(markInfo)){
            if ("0".equals(markInfo)) {
                hql += " AND (a.transaction_mark = '' or a.transaction_mark IS NULL) ";
            }else {
                hql += " AND a.transaction_mark <> '' ";
            }
        }

        SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map> mapList = query.list();
        Double totalFee = 0D;
        Double cashFee = 0D;
        Double onlineFee = 0D;
        String payTypeInfo = "";
        for (Map map : mapList){
            totalFee = totalFee + Double.valueOf(map.get("totalFee") + "");
            payTypeInfo = map.get("payType") + "";
            if (("BOSS_PAY".equals(payTypeInfo)) || ("G".equals(payTypeInfo))){
                onlineFee = onlineFee + Double.valueOf(map.get("totalFee") + "");
            }else {
                cashFee = cashFee + Double.valueOf(map.get("totalFee") + "");
            }
        }
        res.put("totalFee",totalFee);
        res.put("cashFee",cashFee);
        res.put("onlineFee",onlineFee);
        return  res;
    }

    @Override
    public  Map getDeptId(String carparkId) {
        String sql="select department_id from carpark_info where carpark_id= '"+carparkId+"'";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        List list=sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        Map s=new HashMap();
        if(list.size()>0)
        {
           s= (Map)list.get(0);
        }
        return s;


    }

    @Override
    public List<Map<String, Object>> getChargeReceiveSubType(String depId, String carparkId) throws BizException{
        String sql = "select t1.charge_receive_sub_type chargeReceiveSubType from charge_receive_record t1 " +
                "left join carpark_info t2 on t1.charge_receive_carpark = t2.carpark_id " +
                "where t1.use_mark >= 0 ";
        sql = putTogetherInSql(sql, "t2.department_id", "depId", depId);
        sql = putTogetherInSql(sql, "t1.charge_receive_carpark", "carparkId", carparkId);
        sql = sql + " group by t1.charge_receive_sub_type";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        setParameterToQuery(sqlQuery, "depId", depId);
        setParameterToQuery(sqlQuery, "carparkId", carparkId);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @Override
    public List<Map<String, Object>> getCarparkList(String depId, String carparkId, String ownCarparkNo) throws BizException{
        String sql = "select carpark_id carparkId," +
                "carpark_name carparkName," +
                "total_car_space totalCarSpace," +
                "available_car_space availableCarSpace," +
                "own_carpark_no ownCarparkNo," +
                "pass_time_when_big passTimeWhenBig," +
                "carpark_no carparkNo," +
                "carpark_type carparkType," +
                "check_count checkCount," +
                "check_time checkTime," +
                "if_include_caculate ifIncludeCaculate," +
                "is_test_running isTestRunning," +
                "is_close isClose," +
                "critical_value criticalValue," +
                "is_multicar isMulticar," +
                "is_auto_open isAutoOpen," +
                "is_overdue_auto_open isOverdueAutoOpen," +
                "close_type closeType," +
                "led_member_critical_value ledMemberCriticalValue," +
                "use_mark useMark," +
                "department_id departmentId," +
                "operation_source operationSource," +
                "add_time addTime " +
                " from carpark_info t1 where t1.use_mark >= 0 ";
        sql = putTogetherInSql(sql, "t1.department_id", "depId", depId);
        sql = putTogetherInSql(sql, "t1.carpark_id", "carparkId", carparkId);
        sql = putTogetherEqualSql(sql, "t1.own_carpark_no", "ownCarparkNo", ownCarparkNo);
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        setParameterToQuery(sqlQuery, "depId", depId);
        setParameterToQuery(sqlQuery, "carparkId", carparkId);
        setParameterToQuery(sqlQuery, "ownCarparkNo", ownCarparkNo);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @Override
    public List<Map<String, Object>> getBoothList(String depId, String boothId) throws  BizException{
        String sql = "select post_computer_id boothId, " +
                "post_computer_name boothName," +
                "post_computer_ip boothIp," +
                "post_computer_mac boothMac," +
                "syn_port synPort," +
                "status status," +
                "is_auto_deal isAutoDeal," +
                "is_voice_play isVoicePlay," +
                "use_mark useMark," +
                "department_id departmentId," +
                "operation_source operationSource," +
                "add_time addTimne " +
                "from post_computer_manage " +
                "where use_mark >= 0 ";
        sql = putTogetherEqualSql(sql, "department_id", "depId", depId);
        sql = putTogetherEqualSql(sql, "post_computer_id", "boothId", boothId);
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        setParameterToQuery(sqlQuery, "depId", depId);
        setParameterToQuery(sqlQuery, "boothId", boothId);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    /*9岗亭车流统计报表*/
    @Override
    public List<Map<String, Object>> getBoothVehicleFlowReport(Integer page,
                                                                  Integer size,
                                                                  String beginTime,
                                                                  String endTime,
                                                                  String depId,
                                                                  String depName,
                                                                  String carparkId,
                                                                  String carparkName,
                                                                  String boothId,
                                                                  String boothName,
                                                                  Integer inCountMin,
                                                                  Integer inCountMax,
                                                                  Integer outCountMin,
                                                                  Integer outCountMax,
                                                                  Integer carCountMin,
                                                                  Integer carCountMax,
                                                                  Integer sumMinuteMin,
                                                                  Integer sumMinuteMax,
                                                                  Integer maxMinuteMin,
                                                                  Integer maxMinuteMax,
                                                                  Integer minMinuteMin,
                                                                  Integer minMinuteMax,
                                                                  Integer avgMinuteMin,
                                                                  Integer avgMinuteMax,
                                                                  String reportType) throws BizException{
        String sql = "select " +
                "t1.booth_vehicle_flow_dep depId," +
                "t1.booth_vehicle_flow_dep_name depName," +
                "t1.booth_vehicle_flow_carpark carparkId," +
                "t1.booth_vehicle_flow_carpark_name carparkName," +
                "t1.booth_vehicle_flow_post boothId," +
                "t1.booth_vehicle_flow_post_name boothName,";
        if(("0").equals(reportType) || ("1").equals(reportType))
            sql = sql + "DATE_FORMAT(t1.booth_vehicle_flow_time, '%Y-%m-%d') statisticsTime,";
        else if(reportType.equals("2"))
            sql = sql + "DATE_FORMAT(t1.booth_vehicle_flow_time, '%Y-%m') statisticsTime,";
        else
            sql = sql + "DATE_FORMAT(t1.booth_vehicle_flow_time, '%Y') statisticsTime,";

        sql = sql +
                "sum(t1.booth_vehicle_flow_in_count) inCount," +
                "sum(t1.booth_vehicle_flow_out_count) outCount," +
                "sum(t1.booth_vehicle_flow_car_count) carCount," +
                "sum(t1.booth_vehicle_flow_sum_minute) sumMinute," +
                "max(t1.booth_vehicle_flow_max_minute) maxMinute," +
                "min(t1.booth_vehicle_flow_min_minute) minMinute," +
                "(sum(t1.booth_vehicle_flow_sum_minute) / sum(t1.booth_vehicle_flow_car_count)) avgMinute " +
                "from booth_vehicle_flow_report t1 " +
                "where " +
                "t1.use_mark >= 0 ";
        sql = putTogetherRESql(sql, "t1.booth_vehicle_flow_time", "beginTime", beginTime);
        sql = putTogetherLESql(sql, "t1.booth_vehicle_flow_time", "endTime", endTime);
        sql = putTogetherInSql(sql, "t1.booth_vehicle_flow_dep", "depId", depId);
        sql = putTogetherLikeSql(sql, "t1.booth_vehicle_flow_dep_name", "depName", depName);
        sql = putTogetherInSql(sql, "t1.booth_vehicle_flow_carpark", "carparkId", carparkId);
        sql = putTogetherLikeSql(sql, "t1.booth_vehicle_flow_carpark_name", "carparkName", carparkName);
        sql = putTogetherInSql(sql, "t1.booth_vehicle_flow_post", "boothId", boothId);
        sql = putTogetherLikeSql(sql, "t1.booth_vehicle_flow_post_name", "boothName", boothName);

        if(("0").equals(reportType) || ("1").equals(reportType)) {
            sql = sql + " group by t1.booth_vehicle_flow_dep, t1.booth_vehicle_flow_carpark, t1.booth_vehicle_flow_post, DATE_FORMAT(t1.booth_vehicle_flow_time, '%Y-%m-%d') ORDER BY t1.booth_vehicle_flow_dep, t1.booth_vehicle_flow_carpark, t1.booth_vehicle_flow_post, DATE_FORMAT(t1.booth_vehicle_flow_time, '%Y-%m-%d') DESC ";
        }else if(("2").equals(reportType)){
            sql = sql + " group by t1.booth_vehicle_flow_dep, t1.booth_vehicle_flow_carpark, t1.booth_vehicle_flow_post, DATE_FORMAT(t1.booth_vehicle_flow_time, '%Y-%m') ORDER BY t1.booth_vehicle_flow_dep, t1.booth_vehicle_flow_carpark, t1.booth_vehicle_flow_post, DATE_FORMAT(t1.booth_vehicle_flow_time, '%Y-%m-%d') DESC ";
        }else{
            sql = sql + " group by t1.booth_vehicle_flow_dep, t1.booth_vehicle_flow_carpark, t1.booth_vehicle_flow_post, DATE_FORMAT(t1.booth_vehicle_flow_time, '%Y') ORDER BY t1.booth_vehicle_flow_dep, t1.booth_vehicle_flow_carpark, t1.booth_vehicle_flow_post, DATE_FORMAT(t1.booth_vehicle_flow_time, '%Y-%m-%d') DESC ";
        }
        sql = "SELECT * FROM (" + sql + ") tb1 WHERE 1 = 1 ";
        sql = putTogetherRESql(sql, "tb1.inCount", "inCountMin", String.valueOf(inCountMin));
        sql = putTogetherLESql(sql, "tb1.inCount", "inCountMax", String.valueOf(inCountMax));
        sql = putTogetherRESql(sql, "tb1.outCount", "outCountMin", String.valueOf(outCountMin));
        sql = putTogetherLESql(sql, "tb1.outCount", "outCountMax", String.valueOf(outCountMax));
        sql = putTogetherRESql(sql, "tb1.carCount", "carCountMin", String.valueOf(carCountMin));
        sql = putTogetherLESql(sql, "tb1.carCount", "carCountMax", String.valueOf(carCountMax));
        sql = putTogetherRESql(sql, "tb1.sumMinute", "sumMinuteMin", String.valueOf(sumMinuteMin));
        sql = putTogetherLESql(sql, "tb1.sumMinute", "sumMinuteMax", String.valueOf(sumMinuteMax));
        sql = putTogetherRESql(sql, "tb1.maxMinute", "maxMinuteMin", String.valueOf(maxMinuteMin));
        sql = putTogetherLESql(sql, "tb1.maxMinute", "maxMinuteMax", String.valueOf(maxMinuteMax));
        sql = putTogetherRESql(sql, "tb1.minMinute", "minMinuteMin", String.valueOf(minMinuteMin));
        sql = putTogetherLESql(sql, "tb1.minMinute", "minMinuteMax", String.valueOf(minMinuteMax));
        sql = putTogetherRESql(sql, "tb1.avgMinute", "avgMinuteMin", String.valueOf(avgMinuteMin));
        sql = putTogetherLESql(sql, "tb1.avgMinute", "avgMinuteMax", String.valueOf(avgMinuteMax));

        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().
                createSQLQuery(sql);

        setParameterToQuery(sqlQuery, "beginTime", beginTime);
        setParameterToQuery(sqlQuery, "endTime", endTime);
        setParameterToQuery(sqlQuery, "depId", depId);
        setParameterToQueryByLike(sqlQuery, "depName", depName);
        setParameterToQuery(sqlQuery, "carparkId", carparkId);
        setParameterToQueryByLike(sqlQuery, "carparkName", carparkName);
        setParameterToQuery(sqlQuery, "boothId", boothId);
        setParameterToQueryByLike(sqlQuery, "boothName", boothName);

        setParameterToQuery(sqlQuery, "inCountMin", String.valueOf(inCountMin));
        setParameterToQuery(sqlQuery, "inCountMax", String.valueOf(inCountMax));
        setParameterToQuery(sqlQuery, "outCountMin", String.valueOf(outCountMin));
        setParameterToQuery(sqlQuery, "outCountMax", String.valueOf(outCountMax));
        setParameterToQuery(sqlQuery, "carCountMin", String.valueOf(carCountMin));
        setParameterToQuery(sqlQuery, "carCountMax", String.valueOf(carCountMax));
        setParameterToQuery(sqlQuery, "sumMinuteMin", String.valueOf(sumMinuteMin));
        setParameterToQuery(sqlQuery, "sumMinuteMax", String.valueOf(sumMinuteMax));
        setParameterToQuery(sqlQuery, "maxMinuteMin", String.valueOf(maxMinuteMin));
        setParameterToQuery(sqlQuery, "maxMinuteMax", String.valueOf(maxMinuteMax));
        setParameterToQuery(sqlQuery, "minMinuteMin", String.valueOf(minMinuteMin));
        setParameterToQuery(sqlQuery, "minMinuteMax", String.valueOf(minMinuteMax));
        setParameterToQuery(sqlQuery, "avgMinuteMin", String.valueOf(avgMinuteMin));
        setParameterToQuery(sqlQuery, "avgMinuteMax", String.valueOf(avgMinuteMax));

        if(page != null){
            sqlQuery.setFirstResult(page);
        }
        if(size != null){
            sqlQuery.setMaxResults(size);
        }

        List<Map<String, Object>> ret =  sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return ret;
    }
    /*9岗亭车流统计报表记录总数*/
    @Override
    public Integer getBoothVehicleFlowReportCount(Integer page,
                                                  Integer size,
                                                  String beginTime,
                                                  String endTime,
                                                  String depId,
                                                  String depName,
                                                  String carparkId,
                                                  String carparkName,
                                                  String boothId,
                                                  String boothName,
                                                  Integer inCountMin,
                                                  Integer inCountMax,
                                                  Integer outCountMin,
                                                  Integer outCountMax,
                                                  Integer carCountMin,
                                                  Integer carCountMax,
                                                  Integer sumMinuteMin,
                                                  Integer sumMinuteMax,
                                                  Integer maxMinuteMin,
                                                  Integer maxMinuteMax,
                                                  Integer minMinuteMin,
                                                  Integer minMinuteMax,
                                                  Integer avgMinuteMin,
                                                  Integer avgMinuteMax,
                                                  String reportType) throws BizException{
        String sql = "select " +
                "t1.booth_vehicle_flow_dep depId," +
                "t1.booth_vehicle_flow_dep_name depName," +
                "t1.booth_vehicle_flow_carpark carparkId," +
                "t1.booth_vehicle_flow_carpark_name carparkName," +
                "t1.booth_vehicle_flow_post boothId," +
                "sum(t1.booth_vehicle_flow_in_count) inCount," +
                "sum(t1.booth_vehicle_flow_out_count) outCount," +
                "sum(t1.booth_vehicle_flow_car_count) carCount," +
                "sum(t1.booth_vehicle_flow_sum_minute) sumMinute," +
                "max(t1.booth_vehicle_flow_max_minute) maxMinute," +
                "min(t1.booth_vehicle_flow_min_minute) minMinute," +
                "(sum(t1.booth_vehicle_flow_sum_minute) / sum(t1.booth_vehicle_flow_car_count)) avgMinute " +
                "from booth_vehicle_flow_report t1 " +
                "where " +
                "t1.use_mark >= 0 ";
        sql = putTogetherRESql(sql, "t1.booth_vehicle_flow_time", "beginTime", beginTime);
        sql = putTogetherLESql(sql, "t1.booth_vehicle_flow_time", "endTime", endTime);
        sql = putTogetherInSql(sql, "t1.booth_vehicle_flow_dep", "depId", depId);
        sql = putTogetherLikeSql(sql, "t1.booth_vehicle_flow_dep_name", "depName", depName);
        sql = putTogetherInSql(sql, "t1.booth_vehicle_flow_carpark", "carparkId", carparkId);
        sql = putTogetherLikeSql(sql, "t1.booth_vehicle_flow_carpark_name", "carparkName", carparkName);
        sql = putTogetherInSql(sql, "t1.booth_vehicle_flow_post", "boothId", boothId);
        sql = putTogetherLikeSql(sql, "t1.booth_vehicle_flow_post_name", "boothName", boothName);

        if(("0").equals(reportType) || ("1").equals(reportType)) {
            sql = sql + " group by t1.booth_vehicle_flow_dep, t1.booth_vehicle_flow_carpark, t1.booth_vehicle_flow_post, DATE_FORMAT(t1.booth_vehicle_flow_time, '%Y-%m-%d') ";
        }else if(("2").equals(reportType)){
            sql = sql + " group by t1.booth_vehicle_flow_dep, t1.booth_vehicle_flow_carpark, t1.booth_vehicle_flow_post, DATE_FORMAT(t1.booth_vehicle_flow_time, '%Y-%m') ";
        }else{
            sql = sql + " group by t1.booth_vehicle_flow_dep, t1.booth_vehicle_flow_carpark, t1.booth_vehicle_flow_post, DATE_FORMAT(t1.booth_vehicle_flow_time, '%Y') ";
        }
        sql = "SELECT count(*) cnt FROM (" + sql + ") tb1 WHERE 1 = 1 ";
        sql = putTogetherRESql(sql, "tb1.inCount", "inCountMin", String.valueOf(inCountMin));
        sql = putTogetherLESql(sql, "tb1.inCount", "inCountMax", String.valueOf(inCountMax));
        sql = putTogetherRESql(sql, "tb1.outCount", "outCountMin", String.valueOf(outCountMin));
        sql = putTogetherLESql(sql, "tb1.outCount", "outCountMax", String.valueOf(outCountMax));
        sql = putTogetherRESql(sql, "tb1.carCount", "carCountMin", String.valueOf(carCountMin));
        sql = putTogetherLESql(sql, "tb1.carCount", "carCountMax", String.valueOf(carCountMax));
        sql = putTogetherRESql(sql, "tb1.sumMinute", "sumMinuteMin", String.valueOf(sumMinuteMin));
        sql = putTogetherLESql(sql, "tb1.sumMinute", "sumMinuteMax", String.valueOf(sumMinuteMax));
        sql = putTogetherRESql(sql, "tb1.maxMinute", "maxMinuteMin", String.valueOf(maxMinuteMin));
        sql = putTogetherLESql(sql, "tb1.maxMinute", "maxMinuteMax", String.valueOf(maxMinuteMax));
        sql = putTogetherRESql(sql, "tb1.minMinute", "minMinuteMin", String.valueOf(minMinuteMin));
        sql = putTogetherLESql(sql, "tb1.minMinute", "minMinuteMax", String.valueOf(minMinuteMax));
        sql = putTogetherRESql(sql, "tb1.avgMinute", "avgMinuteMin", String.valueOf(avgMinuteMin));
        sql = putTogetherLESql(sql, "tb1.avgMinute", "avgMinuteMax", String.valueOf(avgMinuteMax));

        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().
                createSQLQuery(sql);

        setParameterToQuery(sqlQuery, "beginTime", beginTime);
        setParameterToQuery(sqlQuery, "endTime", endTime);
        setParameterToQuery(sqlQuery, "depId", depId);
        setParameterToQueryByLike(sqlQuery, "depName", depName);
        setParameterToQuery(sqlQuery, "carparkId", carparkId);
        setParameterToQueryByLike(sqlQuery, "carparkName", carparkName);
        setParameterToQuery(sqlQuery, "boothId", boothId);
        setParameterToQueryByLike(sqlQuery, "boothName", boothName);

        setParameterToQuery(sqlQuery, "inCountMin", String.valueOf(inCountMin));
        setParameterToQuery(sqlQuery, "inCountMax", String.valueOf(inCountMax));
        setParameterToQuery(sqlQuery, "outCountMin", String.valueOf(outCountMin));
        setParameterToQuery(sqlQuery, "outCountMax", String.valueOf(outCountMax));
        setParameterToQuery(sqlQuery, "carCountMin", String.valueOf(carCountMin));
        setParameterToQuery(sqlQuery, "carCountMax", String.valueOf(carCountMax));
        setParameterToQuery(sqlQuery, "sumMinuteMin", String.valueOf(sumMinuteMin));
        setParameterToQuery(sqlQuery, "sumMinuteMax", String.valueOf(sumMinuteMax));
        setParameterToQuery(sqlQuery, "maxMinuteMin", String.valueOf(maxMinuteMin));
        setParameterToQuery(sqlQuery, "maxMinuteMax", String.valueOf(maxMinuteMax));
        setParameterToQuery(sqlQuery, "minMinuteMin", String.valueOf(minMinuteMin));
        setParameterToQuery(sqlQuery, "minMinuteMax", String.valueOf(minMinuteMax));
        setParameterToQuery(sqlQuery, "avgMinuteMin", String.valueOf(avgMinuteMin));
        setParameterToQuery(sqlQuery, "avgMinuteMax", String.valueOf(avgMinuteMax));

        List<Map<String, Object>> ret =  sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return Integer.parseInt(ret.get(0).get("cnt").toString());
    }

    /**导出车辆属性车流统计表8*/
    @Override
    public List<Map<String, Object>> getVehicleAttributeReport(Integer page,
                                                              Integer size,
                                                              String beginTime,
                                                              String endTime,
                                                              String depId,
                                                              String depName,
                                                              String carparkId,
                                                              String carparkName,
                                                              String carType,
                                                              Integer inCountMin,
                                                              Integer inCountMax,
                                                              Integer outCountMin,
                                                              Integer outCountMax,
                                                              Integer carCountMin,
                                                              Integer carCountMax,
                                                              Integer sumMinuteMin,
                                                              Integer sumMinuteMax,
                                                              Integer maxMinuteMin,
                                                              Integer maxMinuteMax,
                                                              Integer minMinuteMin,
                                                              Integer minMinuteMax,
                                                              Integer avgMinuteMin,
                                                              Integer avgMinuteMax,
                                                              String reportType) throws BizException{
        String sql = "select " +
                "t1.vehicle_attribute_flow_dep depId," +
                "t1.vehicle_attribute_flow_dep_name depName," +
                "t1.vehicle_attribute_flow_carpark carparkId," +
                "t1.vehicle_attribute_flow_carpark_name carparkName," +
                "t1.vehicle_attribute_flow_car_type carType,";
        if(("0").equals(reportType) || ("1").equals(reportType))
            sql = sql + "DATE_FORMAT(t1.vehicle_attribute_flow_time, '%Y-%m-%d') statisticsTime,";
        else if(("2").equals(reportType))
            sql = sql + "DATE_FORMAT(t1.vehicle_attribute_flow_time, '%Y-%m') statisticsTime,";
        else
            sql = sql + "DATE_FORMAT(t1.vehicle_attribute_flow_time, '%Y') statisticsTime,";

        sql = sql +
                "sum(t1.vehicle_attribute_flow_in_count) inCount," +
                "sum(t1.vehicle_attribute_flow_out_count) outCount," +
                "sum(t1.vehicle_attribute_flow_car_count) carCount," +
                "sum(t1.vehicle_attribute_flow_sum_minute) sumMinute," +
                "max(t1.vehicle_attribute_flow_max_minute) maxMinute," +
                "min(t1.vehicle_attribute_flow_min_minute) minMinute," +
                "(sum(t1.vehicle_attribute_flow_sum_minute) / sum(t1.vehicle_attribute_flow_car_count)) avgMinute " +
                "from vehicle_attribute_flow_report t1 " +
                "where " +
                "t1.use_mark >= 0 ";
        sql = putTogetherRESql(sql, "t1.vehicle_attribute_flow_time", "beginTime", beginTime);
        sql = putTogetherLESql(sql, "t1.vehicle_attribute_flow_time", "endTime", endTime);
        sql = putTogetherInSql(sql, "t1.vehicle_attribute_flow_dep", "depId", depId);
        sql = putTogetherLikeSql(sql, "t1.vehicle_attribute_flow_dep_name", "depName", depName);
        sql = putTogetherInSql(sql, "t1.vehicle_attribute_flow_carpark", "carparkId", carparkId);
        sql = putTogetherLikeSql(sql, "t1.vehicle_attribute_flow_carpark_name", "carparkName", carparkName);
        sql = putTogetherLikeSql(sql, "t1.vehicle_attribute_flow_car_type", "carType", carType);

        if(("0").equals(reportType) || ("1").equals(reportType)) {
            sql = sql + " group by t1.vehicle_attribute_flow_dep, t1.vehicle_attribute_flow_carpark, t1.vehicle_attribute_flow_car_type, DATE_FORMAT(t1.vehicle_attribute_flow_time, '%Y-%m-%d') ORDER BY t1.vehicle_attribute_flow_dep, t1.vehicle_attribute_flow_carpark, t1.vehicle_attribute_flow_car_type, DATE_FORMAT(t1.vehicle_attribute_flow_time, '%Y-%m-%d') DESC ";
        }else if(("2").equals(reportType)){
            sql = sql + " group by t1.vehicle_attribute_flow_dep, t1.vehicle_attribute_flow_carpark, t1.vehicle_attribute_flow_car_type, DATE_FORMAT(t1.vehicle_attribute_flow_time, '%Y-%m') ORDER BY t1.vehicle_attribute_flow_dep, t1.vehicle_attribute_flow_carpark, t1.vehicle_attribute_flow_car_type, DATE_FORMAT(t1.vehicle_attribute_flow_time, '%Y-%m-%d') DESC ";
        }else{
            sql = sql + " group by t1.vehicle_attribute_flow_dep, t1.vehicle_attribute_flow_carpark, t1.vehicle_attribute_flow_car_type, DATE_FORMAT(t1.vehicle_attribute_flow_time, '%Y') ORDER BY t1.vehicle_attribute_flow_dep, t1.vehicle_attribute_flow_carpark, t1.vehicle_attribute_flow_car_type, DATE_FORMAT(t1.vehicle_attribute_flow_time, '%Y-%m-%d') DESC ";
        }
        sql = "SELECT * FROM (" + sql + ") tb1 WHERE 1 = 1 ";
        sql = putTogetherRESql(sql, "tb1.inCount", "inCountMin", String.valueOf(inCountMin));
        sql = putTogetherLESql(sql, "tb1.inCount", "inCountMax", String.valueOf(inCountMax));
        sql = putTogetherRESql(sql, "tb1.outCount", "outCountMin", String.valueOf(outCountMin));
        sql = putTogetherLESql(sql, "tb1.outCount", "outCountMax", String.valueOf(outCountMax));
        sql = putTogetherRESql(sql, "tb1.carCount", "carCountMin", String.valueOf(carCountMin));
        sql = putTogetherLESql(sql, "tb1.carCount", "carCountMax", String.valueOf(carCountMax));
        sql = putTogetherRESql(sql, "tb1.sumMinute", "sumMinuteMin", String.valueOf(sumMinuteMin));
        sql = putTogetherLESql(sql, "tb1.sumMinute", "sumMinuteMax", String.valueOf(sumMinuteMax));
        sql = putTogetherRESql(sql, "tb1.maxMinute", "maxMinuteMin", String.valueOf(maxMinuteMin));
        sql = putTogetherLESql(sql, "tb1.maxMinute", "maxMinuteMax", String.valueOf(maxMinuteMax));
        sql = putTogetherRESql(sql, "tb1.minMinute", "minMinuteMin", String.valueOf(minMinuteMin));
        sql = putTogetherLESql(sql, "tb1.minMinute", "minMinuteMax", String.valueOf(minMinuteMax));
        sql = putTogetherRESql(sql, "tb1.avgMinute", "avgMinuteMin", String.valueOf(avgMinuteMin));
        sql = putTogetherLESql(sql, "tb1.avgMinute", "avgMinuteMax", String.valueOf(avgMinuteMax));

        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().
                createSQLQuery(sql);

        setParameterToQuery(sqlQuery, "beginTime", beginTime);
        setParameterToQuery(sqlQuery, "endTime", endTime);
        setParameterToQuery(sqlQuery, "depId", depId);
        setParameterToQueryByLike(sqlQuery, "depName", depName);
        setParameterToQuery(sqlQuery, "carparkId", carparkId);
        setParameterToQueryByLike(sqlQuery, "carparkName", carparkName);
        setParameterToQueryByLike(sqlQuery, "carType", carType);

        setParameterToQuery(sqlQuery, "inCountMin", String.valueOf(inCountMin));
        setParameterToQuery(sqlQuery, "inCountMax", String.valueOf(inCountMax));
        setParameterToQuery(sqlQuery, "outCountMin", String.valueOf(outCountMin));
        setParameterToQuery(sqlQuery, "outCountMax", String.valueOf(outCountMax));
        setParameterToQuery(sqlQuery, "carCountMin", String.valueOf(carCountMin));
        setParameterToQuery(sqlQuery, "carCountMax", String.valueOf(carCountMax));
        setParameterToQuery(sqlQuery, "sumMinuteMin", String.valueOf(sumMinuteMin));
        setParameterToQuery(sqlQuery, "sumMinuteMax", String.valueOf(sumMinuteMax));
        setParameterToQuery(sqlQuery, "maxMinuteMin", String.valueOf(maxMinuteMin));
        setParameterToQuery(sqlQuery, "maxMinuteMax", String.valueOf(maxMinuteMax));
        setParameterToQuery(sqlQuery, "minMinuteMin", String.valueOf(minMinuteMin));
        setParameterToQuery(sqlQuery, "minMinuteMax", String.valueOf(minMinuteMax));
        setParameterToQuery(sqlQuery, "avgMinuteMin", String.valueOf(avgMinuteMin));
        setParameterToQuery(sqlQuery, "avgMinuteMax", String.valueOf(avgMinuteMax));

        if(page != null){
            sqlQuery.setFirstResult(page);
        }
        if(size != null){
            sqlQuery.setMaxResults(size);
        }

        List<Map<String, Object>> ret =  sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return ret;
    }
    /**导出车辆属性车流统计表记录总数8*/
    @Override
    public Integer getVehicleAttributeReportCount(Integer page,
                                                              Integer size,
                                                              String beginTime,
                                                              String endTime,
                                                              String depId,
                                                              String depName,
                                                              String carparkId,
                                                              String carparkName,
                                                              String carType,
                                                              Integer inCountMin,
                                                              Integer inCountMax,
                                                              Integer outCountMin,
                                                              Integer outCountMax,
                                                              Integer carCountMin,
                                                              Integer carCountMax,
                                                              Integer sumMinuteMin,
                                                              Integer sumMinuteMax,
                                                              Integer maxMinuteMin,
                                                              Integer maxMinuteMax,
                                                              Integer minMinuteMin,
                                                              Integer minMinuteMax,
                                                              Integer avgMinuteMin,
                                                              Integer avgMinuteMax,
                                                              String reportType) throws BizException{
        String sql = "select " +
                "t1.vehicle_attribute_flow_dep depId," +
                "t1.vehicle_attribute_flow_dep_name depName," +
                "t1.vehicle_attribute_flow_carpark carparkId," +
                "t1.vehicle_attribute_flow_carpark_name carparkName," +
                "t1.vehicle_attribute_flow_car_type carType,";
        sql = sql +
                "sum(t1.vehicle_attribute_flow_in_count) inCount," +
                "sum(t1.vehicle_attribute_flow_out_count) outCount," +
                "sum(t1.vehicle_attribute_flow_car_count) carCount," +
                "sum(t1.vehicle_attribute_flow_sum_minute) sumMinute," +
                "max(t1.vehicle_attribute_flow_max_minute) maxMinute," +
                "min(t1.vehicle_attribute_flow_min_minute) minMinute," +
                "(sum(t1.vehicle_attribute_flow_sum_minute) / sum(t1.vehicle_attribute_flow_car_count)) avgMinute " +
                "from vehicle_attribute_flow_report t1 " +
                "where " +
                "t1.use_mark >= 0 ";
        sql = putTogetherRESql(sql, "t1.vehicle_attribute_flow_time", "beginTime", beginTime);
        sql = putTogetherLESql(sql, "t1.vehicle_attribute_flow_time", "endTime", endTime);
        sql = putTogetherInSql(sql, "t1.vehicle_attribute_flow_dep", "depId", depId);
        sql = putTogetherLikeSql(sql, "t1.vehicle_attribute_flow_dep_name", "depName", depName);
        sql = putTogetherInSql(sql, "t1.vehicle_attribute_flow_carpark", "carparkId", carparkId);
        sql = putTogetherLikeSql(sql, "t1.vehicle_attribute_flow_carpark_name", "carparkName", carparkName);
        sql = putTogetherLikeSql(sql, "t1.vehicle_attribute_flow_car_type", "carType", carType);

        if(("0").equals(reportType) || ("1").equals(reportType)) {
            sql = sql + " group by t1.vehicle_attribute_flow_dep, t1.vehicle_attribute_flow_carpark, t1.vehicle_attribute_flow_car_type, DATE_FORMAT(t1.vehicle_attribute_flow_time, '%Y-%m-%d') ";
        }else if(("2").equals(reportType)){
            sql = sql + " group by t1.vehicle_attribute_flow_dep, t1.vehicle_attribute_flow_carpark, t1.vehicle_attribute_flow_car_type, DATE_FORMAT(t1.vehicle_attribute_flow_time, '%Y-%m') ";
        }else{
            sql = sql + " group by t1.vehicle_attribute_flow_dep, t1.vehicle_attribute_flow_carpark, t1.vehicle_attribute_flow_car_type, DATE_FORMAT(t1.vehicle_attribute_flow_time, '%Y') ";
        }
        sql = "SELECT count(*) cnt FROM (" + sql + ") tb1 WHERE 1 = 1 ";
        sql = putTogetherRESql(sql, "tb1.inCount", "inCountMin", String.valueOf(inCountMin));
        sql = putTogetherLESql(sql, "tb1.inCount", "inCountMax", String.valueOf(inCountMax));
        sql = putTogetherRESql(sql, "tb1.outCount", "outCountMin", String.valueOf(outCountMin));
        sql = putTogetherLESql(sql, "tb1.outCount", "outCountMax", String.valueOf(outCountMax));
        sql = putTogetherRESql(sql, "tb1.carCount", "carCountMin", String.valueOf(carCountMin));
        sql = putTogetherLESql(sql, "tb1.carCount", "carCountMax", String.valueOf(carCountMax));
        sql = putTogetherRESql(sql, "tb1.sumMinute", "sumMinuteMin", String.valueOf(sumMinuteMin));
        sql = putTogetherLESql(sql, "tb1.sumMinute", "sumMinuteMax", String.valueOf(sumMinuteMax));
        sql = putTogetherRESql(sql, "tb1.maxMinute", "maxMinuteMin", String.valueOf(maxMinuteMin));
        sql = putTogetherLESql(sql, "tb1.maxMinute", "maxMinuteMax", String.valueOf(maxMinuteMax));
        sql = putTogetherRESql(sql, "tb1.minMinute", "minMinuteMin", String.valueOf(minMinuteMin));
        sql = putTogetherLESql(sql, "tb1.minMinute", "minMinuteMax", String.valueOf(minMinuteMax));
        sql = putTogetherRESql(sql, "tb1.avgMinute", "avgMinuteMin", String.valueOf(avgMinuteMin));
        sql = putTogetherLESql(sql, "tb1.avgMinute", "avgMinuteMax", String.valueOf(avgMinuteMax));

        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().
                createSQLQuery(sql);

        setParameterToQuery(sqlQuery, "beginTime", beginTime);
        setParameterToQuery(sqlQuery, "endTime", endTime);
        setParameterToQuery(sqlQuery, "depId", depId);
        setParameterToQueryByLike(sqlQuery, "depName", depName);
        setParameterToQuery(sqlQuery, "carparkId", carparkId);
        setParameterToQueryByLike(sqlQuery, "carparkName", carparkName);
        setParameterToQueryByLike(sqlQuery, "carType", carType);

        setParameterToQuery(sqlQuery, "inCountMin", String.valueOf(inCountMin));
        setParameterToQuery(sqlQuery, "inCountMax", String.valueOf(inCountMax));
        setParameterToQuery(sqlQuery, "outCountMin", String.valueOf(outCountMin));
        setParameterToQuery(sqlQuery, "outCountMax", String.valueOf(outCountMax));
        setParameterToQuery(sqlQuery, "carCountMin", String.valueOf(carCountMin));
        setParameterToQuery(sqlQuery, "carCountMax", String.valueOf(carCountMax));
        setParameterToQuery(sqlQuery, "sumMinuteMin", String.valueOf(sumMinuteMin));
        setParameterToQuery(sqlQuery, "sumMinuteMax", String.valueOf(sumMinuteMax));
        setParameterToQuery(sqlQuery, "maxMinuteMin", String.valueOf(maxMinuteMin));
        setParameterToQuery(sqlQuery, "maxMinuteMax", String.valueOf(maxMinuteMax));
        setParameterToQuery(sqlQuery, "minMinuteMin", String.valueOf(minMinuteMin));
        setParameterToQuery(sqlQuery, "minMinuteMax", String.valueOf(minMinuteMax));
        setParameterToQuery(sqlQuery, "avgMinuteMin", String.valueOf(avgMinuteMin));
        setParameterToQuery(sqlQuery, "avgMinuteMax", String.valueOf(avgMinuteMax));

        List<Map<String, Object>> ret =  sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return Integer.parseInt(ret.get(0).get("cnt").toString());
    }

    /**导出停车时长分段统计表7*/
    @Override
    public List<Map<String, Object>> getParkingSituationReport(Integer page,
                                                              Integer size,
                                                              String beginTime,
                                                              String endTime,
                                                              String depId,
                                                              String depName,
                                                              String carparkId,
                                                              String carparkName,
                                                              String parkingDurationSection,
                                                              Integer inCountMin,
                                                              Integer inCountMax,
                                                              Integer outCountMin,
                                                              Integer outCountMax,
                                                              Integer carCountMin,
                                                              Integer carCountMax,
                                                              Integer sumMinuteMin,
                                                              Integer sumMinuteMax,
                                                              Integer maxMinuteMin,
                                                              Integer maxMinuteMax,
                                                              Integer minMinuteMin,
                                                              Integer minMinuteMax,
                                                              Integer avgMinuteMin,
                                                              Integer avgMinuteMax,
                                                              String reportType,
                                                               /*0-不需要停车时长， 1-需要停车时长*/
                                                               String isExistDuration) throws BizException{
        /*#parking_duration_section停车时长分段如下：30分钟以内，30~60分钟，1~3小时，3~12
        #小时，12~24小时，24小时以上
        */
        String sql = "select " +
                "t1.parking_duration_section_dep depId," +
                "t1.parking_duration_section_dep_name depName," +
                "t1.parking_duration_section_carpark carparkId," +
                "t1.parking_duration_section_carpark_name carparkName,";
        if (isExistDuration.equals("1"))
            sql = sql + "t1.parking_duration_section parkingDurationSection,";
        if(("0").equals(reportType) || ("1").equals(reportType))
            sql = sql + "DATE_FORMAT(t1.parking_duration_section_time, '%Y-%m-%d') statisticsTime,";
        else if(("2").equals(reportType))
            sql = sql + "DATE_FORMAT(t1.parking_duration_section_time, '%Y-%m') statisticsTime,";
        else
            sql = sql + "DATE_FORMAT(t1.parking_duration_section_time, '%Y') statisticsTime,";

        sql = sql +
                "sum(t1.parking_duration_section_in_count) inCount," +
                "sum(t1.parking_duration_section_out_count) outCount," +
                "sum(t1.parking_duration_section_car_count) carCount," +
                "sum(t1.parking_duration_section_sum_minute) sumMinute," +
                "max(t1.parking_duration_section_max_minute) maxMinute," +
                "min(t1.parking_duration_section_min_minute) minMinute," +
                "(sum(t1.parking_duration_section_sum_minute) / sum(t1.parking_duration_section_car_count)) avgMinute " +
                "from parking_duration_section_report t1 " +
                "where " +
                "t1.use_mark >= 0 ";
        sql = putTogetherRESql(sql, "t1.parking_duration_section_time", "beginTime", beginTime);
        sql = putTogetherLESql(sql, "t1.parking_duration_section_time", "endTime", endTime);
        sql = putTogetherInSql(sql, "t1.parking_duration_section_dep", "depId", depId);
        sql = putTogetherLikeSql(sql, "t1.parking_duration_section_dep_name", "depName", depName);
        sql = putTogetherInSql(sql, "t1.parking_duration_section_carpark", "carparkId", carparkId);
        sql = putTogetherLikeSql(sql, "t1.parking_duration_section_carpark_name", "carparkName", carparkName);
        if (isExistDuration.equals("1"))
            sql = putTogetherLikeSql(sql, "t1.parking_duration_section", "parkingDurationSection", parkingDurationSection);

        if(("0").equals(reportType) || ("1").equals(reportType)) {
            if (isExistDuration.equals("1"))
                sql = sql + " group by t1.parking_duration_section_dep, t1.parking_duration_section_carpark, t1.parking_duration_section, DATE_FORMAT(t1.parking_duration_section_time, '%Y-%m-%d') ORDER BY t1.parking_duration_section_dep, t1.parking_duration_section_carpark, t1.parking_duration_section, DATE_FORMAT(t1.parking_duration_section_time, '%Y-%m-%d') DESC ";
            else
                sql = sql + " group by t1.parking_duration_section_dep, t1.parking_duration_section_carpark, DATE_FORMAT(t1.parking_duration_section_time, '%Y-%m-%d') ORDER BY t1.parking_duration_section_dep, t1.parking_duration_section_carpark, DATE_FORMAT(t1.parking_duration_section_time, '%Y-%m-%d') DESC ";
        }else if(("2").equals(reportType)){
            if (isExistDuration.equals("1"))
                sql = sql + " group by t1.parking_duration_section_dep, t1.parking_duration_section_carpark, t1.parking_duration_section, DATE_FORMAT(t1.parking_duration_section_time, '%Y-%m') ORDER BY t1.parking_duration_section_dep, t1.parking_duration_section_carpark, t1.parking_duration_section, DATE_FORMAT(t1.parking_duration_section_time, '%Y-%m-%d') DESC ";
            else
                sql = sql + " group by t1.parking_duration_section_dep, t1.parking_duration_section_carpark, DATE_FORMAT(t1.parking_duration_section_time, '%Y-%m') ORDER BY t1.parking_duration_section_dep, t1.parking_duration_section_carpark, DATE_FORMAT(t1.parking_duration_section_time, '%Y-%m-%d') DESC ";
        }else{
            if (isExistDuration.equals("1"))
                sql = sql + " group by t1.parking_duration_section_dep, t1.parking_duration_section_carpark, t1.parking_duration_section, DATE_FORMAT(t1.parking_duration_section_time, '%Y') ORDER BY t1.parking_duration_section_dep, t1.parking_duration_section_carpark, t1.parking_duration_section, DATE_FORMAT(t1.parking_duration_section_time, '%Y-%m-%d') DESC ";
            else
                sql = sql + " group by t1.parking_duration_section_dep, t1.parking_duration_section_carpark, DATE_FORMAT(t1.parking_duration_section_time, '%Y') ORDER BY t1.parking_duration_section_dep, t1.parking_duration_section_carpark, DATE_FORMAT(t1.parking_duration_section_time, '%Y-%m-%d') DESC ";
        }
        sql = "SELECT * FROM (" + sql + ") tb1 WHERE 1 = 1 ";
        sql = putTogetherRESql(sql, "tb1.inCount", "inCountMin", String.valueOf(inCountMin));
        sql = putTogetherLESql(sql, "tb1.inCount", "inCountMax", String.valueOf(inCountMax));
        sql = putTogetherRESql(sql, "tb1.outCount", "outCountMin", String.valueOf(outCountMin));
        sql = putTogetherLESql(sql, "tb1.outCount", "outCountMax", String.valueOf(outCountMax));
        sql = putTogetherRESql(sql, "tb1.carCount", "carCountMin", String.valueOf(carCountMin));
        sql = putTogetherLESql(sql, "tb1.carCount", "carCountMax", String.valueOf(carCountMax));
        sql = putTogetherRESql(sql, "tb1.sumMinute", "sumMinuteMin", String.valueOf(sumMinuteMin));
        sql = putTogetherLESql(sql, "tb1.sumMinute", "sumMinuteMax", String.valueOf(sumMinuteMax));
        sql = putTogetherRESql(sql, "tb1.maxMinute", "maxMinuteMin", String.valueOf(maxMinuteMin));
        sql = putTogetherLESql(sql, "tb1.maxMinute", "maxMinuteMax", String.valueOf(maxMinuteMax));
        sql = putTogetherRESql(sql, "tb1.minMinute", "minMinuteMin", String.valueOf(minMinuteMin));
        sql = putTogetherLESql(sql, "tb1.minMinute", "minMinuteMax", String.valueOf(minMinuteMax));
        sql = putTogetherRESql(sql, "tb1.avgMinute", "avgMinuteMin", String.valueOf(avgMinuteMin));
        sql = putTogetherLESql(sql, "tb1.avgMinute", "avgMinuteMax", String.valueOf(avgMinuteMax));

        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().
                createSQLQuery(sql);

        setParameterToQuery(sqlQuery, "beginTime", beginTime);
        setParameterToQuery(sqlQuery, "endTime", endTime);
        setParameterToQuery(sqlQuery, "depId", depId);
        setParameterToQueryByLike(sqlQuery, "depName", depName);
        setParameterToQuery(sqlQuery, "carparkId", carparkId);
        setParameterToQueryByLike(sqlQuery, "carparkName", carparkName);
        if (isExistDuration.equals("1"))
            setParameterToQueryByLike(sqlQuery, "parkingDurationSection", parkingDurationSection);

        setParameterToQuery(sqlQuery, "inCountMin", String.valueOf(inCountMin));
        setParameterToQuery(sqlQuery, "inCountMax", String.valueOf(inCountMax));
        setParameterToQuery(sqlQuery, "outCountMin", String.valueOf(outCountMin));
        setParameterToQuery(sqlQuery, "outCountMax", String.valueOf(outCountMax));
        setParameterToQuery(sqlQuery, "carCountMin", String.valueOf(carCountMin));
        setParameterToQuery(sqlQuery, "carCountMax", String.valueOf(carCountMax));
        setParameterToQuery(sqlQuery, "sumMinuteMin", String.valueOf(sumMinuteMin));
        setParameterToQuery(sqlQuery, "sumMinuteMax", String.valueOf(sumMinuteMax));
        setParameterToQuery(sqlQuery, "maxMinuteMin", String.valueOf(maxMinuteMin));
        setParameterToQuery(sqlQuery, "maxMinuteMax", String.valueOf(maxMinuteMax));
        setParameterToQuery(sqlQuery, "minMinuteMin", String.valueOf(minMinuteMin));
        setParameterToQuery(sqlQuery, "minMinuteMax", String.valueOf(minMinuteMax));
        setParameterToQuery(sqlQuery, "avgMinuteMin", String.valueOf(avgMinuteMin));
        setParameterToQuery(sqlQuery, "avgMinuteMax", String.valueOf(avgMinuteMax));

        if(page != null){
            sqlQuery.setFirstResult(page);
        }
        if(size != null){
            sqlQuery.setMaxResults(size);
        }

        List<Map<String, Object>> ret =  sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return ret;
    }
    /**导出停车时长分段统计表记录总数7*/
    @Override
    public Integer getParkingSituationReportCount(Integer page,
                                                  Integer size,
                                                  String beginTime,
                                                  String endTime,
                                                  String depId,
                                                  String depName,
                                                  String carparkId,
                                                  String carparkName,
                                                  String parkingDurationSection,
                                                  Integer inCountMin,
                                                  Integer inCountMax,
                                                  Integer outCountMin,
                                                  Integer outCountMax,
                                                  Integer carCountMin,
                                                  Integer carCountMax,
                                                  Integer sumMinuteMin,
                                                  Integer sumMinuteMax,
                                                  Integer maxMinuteMin,
                                                  Integer maxMinuteMax,
                                                  Integer minMinuteMin,
                                                  Integer minMinuteMax,
                                                  Integer avgMinuteMin,
                                                  Integer avgMinuteMax,
                                                  String reportType,
                                                   /*0-不需要停车时长， 1-需要停车时长*/
                                                   String isExistDuration) throws BizException{
        /*#parking_duration_section停车时长分段如下：30分钟以内，30~60分钟，1~3小时，3~12
        #小时，12~24小时，24小时以上
        */
        String sql = "select " +
                "t1.parking_duration_section_dep depId," +
                "t1.parking_duration_section_dep_name depName," +
                "t1.parking_duration_section_carpark carparkId," +
                "t1.parking_duration_section_carpark_name carparkName," +
                "sum(t1.parking_duration_section_in_count) inCount," +
                "sum(t1.parking_duration_section_out_count) outCount," +
                "sum(t1.parking_duration_section_car_count) carCount," +
                "sum(t1.parking_duration_section_sum_minute) sumMinute," +
                "max(t1.parking_duration_section_max_minute) maxMinute," +
                "min(t1.parking_duration_section_min_minute) minMinute," +
                "(sum(t1.parking_duration_section_sum_minute) / sum(t1.parking_duration_section_car_count)) avgMinute " +
                "from parking_duration_section_report t1 " +
                "where " +
                "t1.use_mark >= 0 ";
        sql = putTogetherRESql(sql, "t1.parking_duration_section_time", "beginTime", beginTime);
        sql = putTogetherLESql(sql, "t1.parking_duration_section_time", "endTime", endTime);
        sql = putTogetherInSql(sql, "t1.parking_duration_section_dep", "depId", depId);
        sql = putTogetherLikeSql(sql, "t1.parking_duration_section_dep_name", "depName", depName);
        sql = putTogetherInSql(sql, "t1.parking_duration_section_carpark", "carparkId", carparkId);
        sql = putTogetherLikeSql(sql, "t1.parking_duration_section_carpark_name", "carparkName", carparkName);
        if (isExistDuration.equals("1"))
            sql = putTogetherLikeSql(sql, "t1.parking_duration_section", "parkingDurationSection", parkingDurationSection);

        if(("0").equals(reportType) || ("1").equals(reportType)) {
            if (isExistDuration.equals("1"))
                sql = sql + " group by t1.parking_duration_section_dep, t1.parking_duration_section_carpark, t1.parking_duration_section, DATE_FORMAT(t1.parking_duration_section_time, '%Y-%m-%d') ";
            else
                sql = sql + " group by t1.parking_duration_section_dep, t1.parking_duration_section_carpark, DATE_FORMAT(t1.parking_duration_section_time, '%Y-%m-%d') ";
        }else if(("2").equals(reportType)){
            if (isExistDuration.equals("1"))
                sql = sql + " group by t1.parking_duration_section_dep, t1.parking_duration_section_carpark, t1.parking_duration_section, DATE_FORMAT(t1.parking_duration_section_time, '%Y-%m') ";
            else
                sql = sql + " group by t1.parking_duration_section_dep, t1.parking_duration_section_carpark, DATE_FORMAT(t1.parking_duration_section_time, '%Y-%m') ";
        }else{
            if (isExistDuration.equals("1"))
                sql = sql + " group by t1.parking_duration_section_dep, t1.parking_duration_section_carpark, t1.parking_duration_section, DATE_FORMAT(t1.parking_duration_section_time, '%Y') ";
            else
                sql = sql + " group by t1.parking_duration_section_dep, t1.parking_duration_section_carpark, DATE_FORMAT(t1.parking_duration_section_time, '%Y') ";
        }
        sql = "SELECT count(*) cnt FROM (" + sql + ") tb1 WHERE 1 = 1 ";
        sql = putTogetherRESql(sql, "tb1.inCount", "inCountMin", String.valueOf(inCountMin));
        sql = putTogetherLESql(sql, "tb1.inCount", "inCountMax", String.valueOf(inCountMax));
        sql = putTogetherRESql(sql, "tb1.outCount", "outCountMin", String.valueOf(outCountMin));
        sql = putTogetherLESql(sql, "tb1.outCount", "outCountMax", String.valueOf(outCountMax));
        sql = putTogetherRESql(sql, "tb1.carCount", "carCountMin", String.valueOf(carCountMin));
        sql = putTogetherLESql(sql, "tb1.carCount", "carCountMax", String.valueOf(carCountMax));
        sql = putTogetherRESql(sql, "tb1.sumMinute", "sumMinuteMin", String.valueOf(sumMinuteMin));
        sql = putTogetherLESql(sql, "tb1.sumMinute", "sumMinuteMax", String.valueOf(sumMinuteMax));
        sql = putTogetherRESql(sql, "tb1.maxMinute", "maxMinuteMin", String.valueOf(maxMinuteMin));
        sql = putTogetherLESql(sql, "tb1.maxMinute", "maxMinuteMax", String.valueOf(maxMinuteMax));
        sql = putTogetherRESql(sql, "tb1.minMinute", "minMinuteMin", String.valueOf(minMinuteMin));
        sql = putTogetherLESql(sql, "tb1.minMinute", "minMinuteMax", String.valueOf(minMinuteMax));
        sql = putTogetherRESql(sql, "tb1.avgMinute", "avgMinuteMin", String.valueOf(avgMinuteMin));
        sql = putTogetherLESql(sql, "tb1.avgMinute", "avgMinuteMax", String.valueOf(avgMinuteMax));

        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().
                createSQLQuery(sql);

        setParameterToQuery(sqlQuery, "beginTime", beginTime);
        setParameterToQuery(sqlQuery, "endTime", endTime);
        setParameterToQuery(sqlQuery, "depId", depId);
        setParameterToQueryByLike(sqlQuery, "depName", depName);
        setParameterToQuery(sqlQuery, "carparkId", carparkId);
        setParameterToQueryByLike(sqlQuery, "carparkName", carparkName);
        if (isExistDuration.equals("1"))
            setParameterToQueryByLike(sqlQuery, "parkingDurationSection", parkingDurationSection);

        setParameterToQuery(sqlQuery, "inCountMin", String.valueOf(inCountMin));
        setParameterToQuery(sqlQuery, "inCountMax", String.valueOf(inCountMax));
        setParameterToQuery(sqlQuery, "outCountMin", String.valueOf(outCountMin));
        setParameterToQuery(sqlQuery, "outCountMax", String.valueOf(outCountMax));
        setParameterToQuery(sqlQuery, "carCountMin", String.valueOf(carCountMin));
        setParameterToQuery(sqlQuery, "carCountMax", String.valueOf(carCountMax));
        setParameterToQuery(sqlQuery, "sumMinuteMin", String.valueOf(sumMinuteMin));
        setParameterToQuery(sqlQuery, "sumMinuteMax", String.valueOf(sumMinuteMax));
        setParameterToQuery(sqlQuery, "maxMinuteMin", String.valueOf(maxMinuteMin));
        setParameterToQuery(sqlQuery, "maxMinuteMax", String.valueOf(maxMinuteMax));
        setParameterToQuery(sqlQuery, "minMinuteMin", String.valueOf(minMinuteMin));
        setParameterToQuery(sqlQuery, "minMinuteMax", String.valueOf(minMinuteMax));
        setParameterToQuery(sqlQuery, "avgMinuteMin", String.valueOf(avgMinuteMin));
        setParameterToQuery(sqlQuery, "avgMinuteMax", String.valueOf(avgMinuteMax));

        List<Map<String, Object>> ret =  sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return Integer.parseInt(ret.get(0).get("cnt").toString());
    }

    /**导出收费金额分段统计表5*/
    @Override
    public List<Map<String, Object>> getAmountSectionReport(Integer page,
                                                              Integer size,
                                                              String beginTime,
                                                              String endTime,
                                                              String depId,
                                                              String depName,
                                                              String carparkId,
                                                              String carparkName,
                                                              String chargeAmountSection,
                                                              Integer chargeCountMin,
                                                              Integer chargeCountMax,
                                                              Float availSumAmountMin,
                                                              Float availSumAmountMax,
                                                              Float availMaxAmountMin,
                                                              Float availMaxAmountMax,
                                                              Float availMinAmountMin,
                                                              Float availMinAmountMax,
                                                              Float availAvgAmountMin,
                                                              Float availAvgAmountMax,
                                                              Float realSumAmountMin,
                                                              Float realSumAmountMax,
                                                              Float realMaxAmountMin,
                                                              Float realMaxAmountMax,
                                                              Float realMinAmountMin,
                                                              Float realMinAmountMax,
                                                              Float realAvgAmountMin,
                                                              Float realAvgAmountMax,
                                                              String reportType) throws BizException{
        String sql = "select " +
                "t1.charge_amount_section_dep depId," +
                "t1.charge_amount_section_dep_name depName," +
                "t1.charge_amount_section_carpark carparkId," +
                "t1.charge_amount_section_carpark_name carparkName," +
                "t1.charge_amount_section chargeAmountSection,";
        if(("0").equals(reportType) || ("1").equals(reportType))
            sql = sql + "DATE_FORMAT(t1.charge_amount_section_time, '%Y-%m-%d') statisticsTime,";
        else if(("2").equals(reportType))
            sql = sql + "DATE_FORMAT(t1.charge_amount_section_time, '%Y-%m') statisticsTime,";
        else
            sql = sql + "DATE_FORMAT(t1.charge_amount_section_time, '%Y') statisticsTime,";

        sql = sql +
                "sum(t1.charge_amount_section_count) chargeCount," +
                "sum(t1.charge_amount_section_avail_sum_amount) availSumAmount," +
                "max(t1.charge_amount_section_avail_max_amount) availMaxAmount," +
                "min(t1.charge_amount_section_avail_min_amount) availMinAmout," +
                "(sum(t1.charge_amount_section_avail_sum_amount) / sum(t1.charge_amount_section_count)) availAvgAmout," +
                "sum(t1.charge_amount_section_real_sum_amount) realSumAmount," +
                "max(t1.charge_amount_section_real_max_amount) realMaxAmount," +
                "min(t1.charge_amount_section_real_min_amount) realMinAmout," +
                "(sum(t1.charge_amount_section_real_sum_amount) / sum(t1.charge_amount_section_count)) realAvgAmout " +
                "from charge_amount_section_report t1 " +
                "where " +
                "t1.use_mark >= 0 ";
        sql = putTogetherRESql(sql, "t1.charge_amount_section_time", "beginTime", beginTime);
        sql = putTogetherLESql(sql, "t1.charge_amount_section_time", "endTime", endTime);
        sql = putTogetherInSql(sql, "t1.charge_amount_section_dep", "depId", depId);
        sql = putTogetherLikeSql(sql, "t1.charge_amount_section_dep_name", "depName", depName);
        sql = putTogetherInSql(sql, "t1.charge_amount_section_carpark", "carparkId", carparkId);
        sql = putTogetherLikeSql(sql, "t1.charge_amount_section_carpark_name", "carparkName", carparkName);
        sql = putTogetherLikeSql(sql, "t1.charge_amount_section_car_type", "chargeAmountSection", chargeAmountSection);


        if(("0").equals(reportType) || ("1").equals(reportType))
            sql = sql + " group by t1.charge_amount_section_dep, t1.charge_amount_section_carpark, t1.charge_amount_section, DATE_FORMAT(t1.charge_amount_section_time, '%Y-%m-%d') ORDER BY t1.charge_amount_section_dep, t1.charge_amount_section_carpark, t1.charge_amount_section, DATE_FORMAT(t1.charge_amount_section_time, '%Y-%m-%d') DESC ";
        else if(("2").equals(reportType))
            sql = sql + " group by t1.charge_amount_section_dep, t1.charge_amount_section_carpark, t1.charge_amount_section, DATE_FORMAT(t1.charge_amount_section_time, '%Y-%m') ORDER BY t1.charge_amount_section_dep, t1.charge_amount_section_carpark, t1.charge_amount_section, DATE_FORMAT(t1.charge_amount_section_time, '%Y-%m') DESC ";
        else
            sql = sql + " group by t1.charge_amount_section_dep, t1.charge_amount_section_carpark, t1.charge_amount_section, DATE_FORMAT(t1.charge_amount_section_time, '%Y') ORDER BY t1.charge_amount_section_dep, t1.charge_amount_section_carpark, t1.charge_amount_section, DATE_FORMAT(t1.charge_amount_section_time, '%Y') DESC  ";

        sql = "SELECT * FROM (" + sql + ") tb1 WHERE 1 = 1 ";
        sql = putTogetherRESql(sql, "tb1.chargeCount", "chargeCountMin", String.valueOf(chargeCountMin));
        sql = putTogetherLESql(sql, "tb1.chargeCount", "chargeCountMax", String.valueOf(chargeCountMax));
        sql = putTogetherRESql(sql, "tb1.availSumAmount", "availSumAmountMin", String.valueOf(availSumAmountMin));
        sql = putTogetherLESql(sql, "tb1.availSumAmount", "availSumAmountMax", String.valueOf(availSumAmountMax));
        sql = putTogetherRESql(sql, "tb1.availMaxAmount", "availMaxAmountMin", String.valueOf(availMaxAmountMin));
        sql = putTogetherLESql(sql, "tb1.availMaxAmount", "availMaxAmountMax", String.valueOf(availMaxAmountMax));
        sql = putTogetherRESql(sql, "tb1.availMinAmount", "availMinAmountMin", String.valueOf(availMinAmountMin));
        sql = putTogetherLESql(sql, "tb1.availMinAmount", "availMinAmountMax", String.valueOf(availMinAmountMax));
        sql = putTogetherRESql(sql, "tb1.availAvgAmount", "availAvgAmountMin", String.valueOf(availAvgAmountMin));
        sql = putTogetherLESql(sql, "tb1.availAvgAmount", "availAvgAmountMax", String.valueOf(availAvgAmountMax));
        sql = putTogetherRESql(sql, "tb1.realSumAmount", "realSumAmountMin", String.valueOf(realSumAmountMin));
        sql = putTogetherLESql(sql, "tb1.realSumAmount", "realSumAmountMax", String.valueOf(realSumAmountMax));
        sql = putTogetherRESql(sql, "tb1.realMaxAmount", "realMaxAmountMin", String.valueOf(realMaxAmountMin));
        sql = putTogetherLESql(sql, "tb1.realMaxAmount", "realMaxAmountMax", String.valueOf(realMaxAmountMax));
        sql = putTogetherRESql(sql, "tb1.realMinAmount", "realMinAmountMin", String.valueOf(realMinAmountMin));
        sql = putTogetherLESql(sql, "tb1.realMinAmount", "realMinAmountMax", String.valueOf(realMinAmountMax));
        sql = putTogetherRESql(sql, "tb1.realAvgAmount", "realAvgAmountMin", String.valueOf(realAvgAmountMin));
        sql = putTogetherLESql(sql, "tb1.realAvgAmount", "realAvgAmountMax", String.valueOf(realAvgAmountMax));
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().
                createSQLQuery(sql);

        setParameterToQuery(sqlQuery, "beginTime", beginTime);
        setParameterToQuery(sqlQuery, "endTime", endTime);
        setParameterToQuery(sqlQuery, "depId", depId);
        setParameterToQueryByLike(sqlQuery, "depName", depName);
        setParameterToQuery(sqlQuery, "carparkId", carparkId);
        setParameterToQueryByLike(sqlQuery, "carparkName", carparkName);
        setParameterToQueryByLike(sqlQuery, "chargeAmountSection", chargeAmountSection);

        setParameterToQuery(sqlQuery, "chargeCountMin", String.valueOf(chargeCountMin));
        setParameterToQuery(sqlQuery, "chargeCountMax", String.valueOf(chargeCountMax));
        setParameterToQuery(sqlQuery, "availSumAmountMin", String.valueOf(availSumAmountMin));
        setParameterToQuery(sqlQuery, "availSumAmountMax", String.valueOf(availSumAmountMax));
        setParameterToQuery(sqlQuery, "availMaxAmountMin", String.valueOf(availMaxAmountMin));
        setParameterToQuery(sqlQuery, "availMaxAmountMax", String.valueOf(availMaxAmountMax));
        setParameterToQuery(sqlQuery, "availMinAmountMin", String.valueOf(availMinAmountMin));
        setParameterToQuery(sqlQuery, "availMinAmountMax", String.valueOf(availMinAmountMax));
        setParameterToQuery(sqlQuery, "availAvgAmountMin", String.valueOf(availAvgAmountMin));
        setParameterToQuery(sqlQuery, "availAvgAmountMax", String.valueOf(availAvgAmountMax));
        setParameterToQuery(sqlQuery, "realSumAmountMin", String.valueOf(realSumAmountMin));
        setParameterToQuery(sqlQuery, "realSumAmountMax", String.valueOf(realSumAmountMax));
        setParameterToQuery(sqlQuery, "realMaxAmountMin", String.valueOf(realMaxAmountMin));
        setParameterToQuery(sqlQuery, "realMaxAmountMax", String.valueOf(realMaxAmountMax));
        setParameterToQuery(sqlQuery, "realMinAmountMin", String.valueOf(realMinAmountMin));
        setParameterToQuery(sqlQuery, "realMinAmountMax", String.valueOf(realMinAmountMax));
        setParameterToQuery(sqlQuery, "realAvgAmountMin", String.valueOf(realAvgAmountMin));
        setParameterToQuery(sqlQuery, "realAvgAmountMax", String.valueOf(realAvgAmountMax));

        if(page != null){
            sqlQuery.setFirstResult(page);
        }
        if(size != null){
            sqlQuery.setMaxResults(size);
        }

        List<Map<String, Object>> ret =  sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return ret;
    }

    /**导出收费金额分段统计表记录总数5*/
    @Override
    public Integer getAmountSectionReportCount(Integer page,
                                                  Integer size,
                                                  String beginTime,
                                                  String endTime,
                                                  String depId,
                                                  String depName,
                                                  String carparkId,
                                                  String carparkName,
                                                  String chargeAmountSection,
                                                  Integer chargeCountMin,
                                                  Integer chargeCountMax,
                                                  Float availSumAmountMin,
                                                  Float availSumAmountMax,
                                                  Float availMaxAmountMin,
                                                  Float availMaxAmountMax,
                                                  Float availMinAmountMin,
                                                  Float availMinAmountMax,
                                                  Float availAvgAmountMin,
                                                  Float availAvgAmountMax,
                                                  Float realSumAmountMin,
                                                  Float realSumAmountMax,
                                                  Float realMaxAmountMin,
                                                  Float realMaxAmountMax,
                                                  Float realMinAmountMin,
                                                  Float realMinAmountMax,
                                                  Float realAvgAmountMin,
                                                  Float realAvgAmountMax,
                                                  String reportType) throws BizException{
        String sql = "select " +
                "t1.charge_amount_section_dep depId," +
                "t1.charge_amount_section_dep_name depName," +
                "t1.charge_amount_section_carpark carparkId," +
                "t1.charge_amount_section_carpark_name carparkName," +
                "t1.charge_amount_section chargeAmountSection," +
                "t1.charge_amount_section_time statisticsTime," +
                "sum(t1.charge_amount_section_count) chargeCount," +
                "sum(t1.charge_amount_section_avail_sum_amount) availSumAmount," +
                "max(t1.charge_amount_section_avail_max_amount) availMaxAmount," +
                "min(t1.charge_amount_section_avail_min_amount) availMinAmout," +
                "(sum(t1.charge_amount_section_avail_sum_amount) / sum(t1.charge_amount_section_count)) availAvgAmout," +
                "sum(t1.charge_amount_section_real_sum_amount) realSumAmount," +
                "max(t1.charge_amount_section_real_max_amount) realMaxAmount," +
                "min(t1.charge_amount_section_real_min_amount) realMinAmout," +
                "(sum(t1.charge_amount_section_real_sum_amount) / sum(t1.charge_amount_section_count)) realAvgAmout " +
                "from charge_amount_section_report t1 " +
                "where " +
                "t1.use_mark >= 0 ";
        sql = putTogetherRESql(sql, "t1.charge_amount_section_time", "beginTime", beginTime);
        sql = putTogetherLESql(sql, "t1.charge_amount_section_time", "endTime", endTime);
        sql = putTogetherInSql(sql, "t1.charge_amount_section_dep", "depId", depId);
        sql = putTogetherLikeSql(sql, "t1.charge_amount_section_dep_name", "depName", depName);
        sql = putTogetherInSql(sql, "t1.charge_amount_section_carpark", "carparkId", carparkId);
        sql = putTogetherLikeSql(sql, "t1.charge_amount_section_carpark_name", "carparkName", carparkName);
        sql = putTogetherLikeSql(sql, "t1.charge_amount_section_car_type", "chargeAmountSection", chargeAmountSection);


        if(("0").equals(reportType) || ("1").equals(reportType))
            sql = sql + " group by t1.charge_amount_section_dep, t1.charge_amount_section_carpark, t1.charge_amount_section, DATE_FORMAT(t1.charge_amount_section_time, '%Y-%m-%d') ";
        else if(("2").equals(reportType))
            sql = sql + " group by t1.charge_amount_section_dep, t1.charge_amount_section_carpark, t1.charge_amount_section, DATE_FORMAT(t1.charge_amount_section_time, '%Y-%m') ";
        else
            sql = sql + " group by t1.charge_amount_section_dep, t1.charge_amount_section_carpark, t1.charge_amount_section, DATE_FORMAT(t1.charge_amount_section_time, '%Y') ";

        sql = "SELECT COUNT(*) cnt FROM (" + sql + ") tb1 WHERE 1 = 1 ";
        sql = putTogetherRESql(sql, "tb1.chargeCount", "chargeCountMin", String.valueOf(chargeCountMin));
        sql = putTogetherLESql(sql, "tb1.chargeCount", "chargeCountMax", String.valueOf(chargeCountMax));
        sql = putTogetherRESql(sql, "tb1.availSumAmount", "availSumAmountMin", String.valueOf(availSumAmountMin));
        sql = putTogetherLESql(sql, "tb1.availSumAmount", "availSumAmountMax", String.valueOf(availSumAmountMax));
        sql = putTogetherRESql(sql, "tb1.availMaxAmount", "availMaxAmountMin", String.valueOf(availMaxAmountMin));
        sql = putTogetherLESql(sql, "tb1.availMaxAmount", "availMaxAmountMax", String.valueOf(availMaxAmountMax));
        sql = putTogetherRESql(sql, "tb1.availMinAmount", "availMinAmountMin", String.valueOf(availMinAmountMin));
        sql = putTogetherLESql(sql, "tb1.availMinAmount", "availMinAmountMax", String.valueOf(availMinAmountMax));
        sql = putTogetherRESql(sql, "tb1.availAvgAmount", "availAvgAmountMin", String.valueOf(availAvgAmountMin));
        sql = putTogetherLESql(sql, "tb1.availAvgAmount", "availAvgAmountMax", String.valueOf(availAvgAmountMax));
        sql = putTogetherRESql(sql, "tb1.realSumAmount", "realSumAmountMin", String.valueOf(realSumAmountMin));
        sql = putTogetherLESql(sql, "tb1.realSumAmount", "realSumAmountMax", String.valueOf(realSumAmountMax));
        sql = putTogetherRESql(sql, "tb1.realMaxAmount", "realMaxAmountMin", String.valueOf(realMaxAmountMin));
        sql = putTogetherLESql(sql, "tb1.realMaxAmount", "realMaxAmountMax", String.valueOf(realMaxAmountMax));
        sql = putTogetherRESql(sql, "tb1.realMinAmount", "realMinAmountMin", String.valueOf(realMinAmountMin));
        sql = putTogetherLESql(sql, "tb1.realMinAmount", "realMinAmountMax", String.valueOf(realMinAmountMax));
        sql = putTogetherRESql(sql, "tb1.realAvgAmount", "realAvgAmountMin", String.valueOf(realAvgAmountMin));
        sql = putTogetherLESql(sql, "tb1.realAvgAmount", "realAvgAmountMax", String.valueOf(realAvgAmountMax));
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().
                createSQLQuery(sql);

        setParameterToQuery(sqlQuery, "beginTime", beginTime);
        setParameterToQuery(sqlQuery, "endTime", endTime);
        setParameterToQuery(sqlQuery, "depId", depId);
        setParameterToQueryByLike(sqlQuery, "depName", depName);
        setParameterToQuery(sqlQuery, "carparkId", carparkId);
        setParameterToQueryByLike(sqlQuery, "carparkName", carparkName);
        setParameterToQueryByLike(sqlQuery, "charge_amount_section", chargeAmountSection);

        setParameterToQuery(sqlQuery, "chargeCountMin", String.valueOf(chargeCountMin));
        setParameterToQuery(sqlQuery, "chargeCountMax", String.valueOf(chargeCountMax));
        setParameterToQuery(sqlQuery, "availSumAmountMin", String.valueOf(availSumAmountMin));
        setParameterToQuery(sqlQuery, "availSumAmountMax", String.valueOf(availSumAmountMax));
        setParameterToQuery(sqlQuery, "availMaxAmountMin", String.valueOf(availMaxAmountMin));
        setParameterToQuery(sqlQuery, "availMaxAmountMax", String.valueOf(availMaxAmountMax));
        setParameterToQuery(sqlQuery, "availMinAmountMin", String.valueOf(availMinAmountMin));
        setParameterToQuery(sqlQuery, "availMinAmountMax", String.valueOf(availMinAmountMax));
        setParameterToQuery(sqlQuery, "availAvgAmountMin", String.valueOf(availAvgAmountMin));
        setParameterToQuery(sqlQuery, "availAvgAmountMax", String.valueOf(availAvgAmountMax));
        setParameterToQuery(sqlQuery, "realSumAmountMin", String.valueOf(realSumAmountMin));
        setParameterToQuery(sqlQuery, "realSumAmountMax", String.valueOf(realSumAmountMax));
        setParameterToQuery(sqlQuery, "realMaxAmountMin", String.valueOf(realMaxAmountMin));
        setParameterToQuery(sqlQuery, "realMaxAmountMax", String.valueOf(realMaxAmountMax));
        setParameterToQuery(sqlQuery, "realMinAmountMin", String.valueOf(realMinAmountMin));
        setParameterToQuery(sqlQuery, "realMinAmountMax", String.valueOf(realMinAmountMax));
        setParameterToQuery(sqlQuery, "realAvgAmountMin", String.valueOf(realAvgAmountMin));
        setParameterToQuery(sqlQuery, "realAvgAmountMax", String.valueOf(realAvgAmountMax));

        List<Map<String, Object>> ret =  sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return Integer.parseInt(ret.get(0).get("cnt").toString());
    }

    /**导出车辆属性收费分析表4*/
    @Override
    public List<Map<String, Object>> getAttributeChargeReport(Integer page,
                                                              Integer size,
                                                              String beginTime,
                                                              String endTime,
                                                              String depId,
                                                              String depName,
                                                              String carparkId,
                                                              String carparkName,
                                                              String carType,
                                                              Integer chargeCountMin,
                                                              Integer chargeCountMax,
                                                              Float availSumAmountMin,
                                                              Float availSumAmountMax,
                                                              Float availMaxAmountMin,
                                                              Float availMaxAmountMax,
                                                              Float availMinAmountMin,
                                                              Float availMinAmountMax,
                                                              Float availAvgAmountMin,
                                                              Float availAvgAmountMax,
                                                              Float realSumAmountMin,
                                                              Float realSumAmountMax,
                                                              Float realMaxAmountMin,
                                                              Float realMaxAmountMax,
                                                              Float realMinAmountMin,
                                                              Float realMinAmountMax,
                                                              Float realAvgAmountMin,
                                                              Float realAvgAmountMax,
                                                              String reportType) throws BizException{
        String sql = "select " +
                "t1.attribute_charge_dep depId," +
                "t1.attribute_charge_dep_name depName," +
                "t1.attribute_charge_carpark carparkId," +
                "t1.attribute_charge_carpark_name carparkName," +
                "t1.attribute_charge_car_type carType,";
        if(("0").equals(reportType) || ("1").equals(reportType))
            sql = sql + "DATE_FORMAT(t1.attribute_charge_time, '%Y-%m-%d') statisticsTime,";
        else if(("2").equals(reportType))
            sql = sql + "DATE_FORMAT(t1.attribute_charge_time, '%Y-%m') statisticsTime,";
        else
            sql = sql + "DATE_FORMAT(t1.attribute_charge_time, '%Y') statisticsTime,";

        sql = sql +
                "sum(t1.attribute_charge_count) chargeCount," +
                "sum(t1.attribute_charge_avail_sum_amount) availSumAmount," +
                "max(t1.attribute_charge_avail_max_amount) availMaxAmount," +
                "min(t1.attribute_charge_avail_min_amount) availMinAmout," +
                "(sum(t1.attribute_charge_avail_sum_amount) / sum(t1.attribute_charge_count)) availAvgAmout," +
                "sum(t1.attribute_charge_real_sum_amount) realSumAmount," +
                "max(t1.attribute_charge_real_max_amount) realMaxAmount," +
                "min(t1.attribute_charge_real_min_amount) realMinAmout," +
                "(sum(t1.attribute_charge_real_sum_amount) / sum(t1.attribute_charge_count)) realAvgAmout " +
                "from attribute_charge_report t1 " +
                "where " +
                "t1.use_mark >= 0 ";
        sql = putTogetherRESql(sql, "t1.attribute_charge_time", "beginTime", beginTime);
        sql = putTogetherLESql(sql, "t1.attribute_charge_time", "endTime", endTime);
        sql = putTogetherInSql(sql, "t1.attribute_charge_dep", "depId", depId);
        sql = putTogetherLikeSql(sql, "t1.attribute_charge_dep_name", "depName", depName);
        sql = putTogetherInSql(sql, "t1.attribute_charge_carpark", "carparkId", carparkId);
        sql = putTogetherLikeSql(sql, "t1.attribute_charge_carpark_name", "carparkName", carparkName);
        sql = putTogetherLikeSql(sql, "t1.attribute_charge_car_type", "carType", carType);


        if(("0").equals(reportType) || ("1").equals(reportType))
            sql = sql + " group by t1.attribute_charge_dep, t1.attribute_charge_carpark, t1.attribute_charge_car_type, DATE_FORMAT(t1.attribute_charge_time, '%Y-%m-%d') ORDER BY t1.attribute_charge_dep, t1.attribute_charge_carpark, t1.attribute_charge_car_type, DATE_FORMAT(t1.attribute_charge_time, '%Y-%m-%d') DESC ";
        else if(("2").equals(reportType))
            sql = sql + " group by t1.attribute_charge_dep, t1.attribute_charge_carpark, t1.attribute_charge_car_type, DATE_FORMAT(t1.attribute_charge_time, '%Y-%m') ORDER BY t1.attribute_charge_dep, t1.attribute_charge_carpark, t1.attribute_charge_car_type, DATE_FORMAT(t1.attribute_charge_time, '%Y-%m') DESC ";
        else
            sql = sql + " group by t1.attribute_charge_dep, t1.attribute_charge_carpark, t1.attribute_charge_car_type, DATE_FORMAT(t1.attribute_charge_time, '%Y') ORDER BY t1.attribute_charge_dep, t1.attribute_charge_carpark, t1.attribute_charge_car_type, DATE_FORMAT(t1.attribute_charge_time, '%Y') DESC  ";

        sql = "SELECT * FROM (" + sql + ") tb1 WHERE 1 = 1 ";
        sql = putTogetherRESql(sql, "tb1.chargeCount", "chargeCountMin", String.valueOf(chargeCountMin));
        sql = putTogetherLESql(sql, "tb1.chargeCount", "chargeCountMax", String.valueOf(chargeCountMax));
        sql = putTogetherRESql(sql, "tb1.availSumAmount", "availSumAmountMin", String.valueOf(availSumAmountMin));
        sql = putTogetherLESql(sql, "tb1.availSumAmount", "availSumAmountMax", String.valueOf(availSumAmountMax));
        sql = putTogetherRESql(sql, "tb1.availMaxAmount", "availMaxAmountMin", String.valueOf(availMaxAmountMin));
        sql = putTogetherLESql(sql, "tb1.availMaxAmount", "availMaxAmountMax", String.valueOf(availMaxAmountMax));
        sql = putTogetherRESql(sql, "tb1.availMinAmount", "availMinAmountMin", String.valueOf(availMinAmountMin));
        sql = putTogetherLESql(sql, "tb1.availMinAmount", "availMinAmountMax", String.valueOf(availMinAmountMax));
        sql = putTogetherRESql(sql, "tb1.availAvgAmount", "availAvgAmountMin", String.valueOf(availAvgAmountMin));
        sql = putTogetherLESql(sql, "tb1.availAvgAmount", "availAvgAmountMax", String.valueOf(availAvgAmountMax));
        sql = putTogetherRESql(sql, "tb1.realSumAmount", "realSumAmountMin", String.valueOf(realSumAmountMin));
        sql = putTogetherLESql(sql, "tb1.realSumAmount", "realSumAmountMax", String.valueOf(realSumAmountMax));
        sql = putTogetherRESql(sql, "tb1.realMaxAmount", "realMaxAmountMin", String.valueOf(realMaxAmountMin));
        sql = putTogetherLESql(sql, "tb1.realMaxAmount", "realMaxAmountMax", String.valueOf(realMaxAmountMax));
        sql = putTogetherRESql(sql, "tb1.realMinAmount", "realMinAmountMin", String.valueOf(realMinAmountMin));
        sql = putTogetherLESql(sql, "tb1.realMinAmount", "realMinAmountMax", String.valueOf(realMinAmountMax));
        sql = putTogetherRESql(sql, "tb1.realAvgAmount", "realAvgAmountMin", String.valueOf(realAvgAmountMin));
        sql = putTogetherLESql(sql, "tb1.realAvgAmount", "realAvgAmountMax", String.valueOf(realAvgAmountMax));
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().
                createSQLQuery(sql);

        setParameterToQuery(sqlQuery, "beginTime", beginTime);
        setParameterToQuery(sqlQuery, "endTime", endTime);
        setParameterToQuery(sqlQuery, "depId", depId);
        setParameterToQueryByLike(sqlQuery, "depName", depName);
        setParameterToQuery(sqlQuery, "carparkId", carparkId);
        setParameterToQueryByLike(sqlQuery, "carparkName", carparkName);
        setParameterToQueryByLike(sqlQuery, "carType", carType);

        setParameterToQuery(sqlQuery, "chargeCountMin", String.valueOf(chargeCountMin));
        setParameterToQuery(sqlQuery, "chargeCountMax", String.valueOf(chargeCountMax));
        setParameterToQuery(sqlQuery, "availSumAmountMin", String.valueOf(availSumAmountMin));
        setParameterToQuery(sqlQuery, "availSumAmountMax", String.valueOf(availSumAmountMax));
        setParameterToQuery(sqlQuery, "availMaxAmountMin", String.valueOf(availMaxAmountMin));
        setParameterToQuery(sqlQuery, "availMaxAmountMax", String.valueOf(availMaxAmountMax));
        setParameterToQuery(sqlQuery, "availMinAmountMin", String.valueOf(availMinAmountMin));
        setParameterToQuery(sqlQuery, "availMinAmountMax", String.valueOf(availMinAmountMax));
        setParameterToQuery(sqlQuery, "availAvgAmountMin", String.valueOf(availAvgAmountMin));
        setParameterToQuery(sqlQuery, "availAvgAmountMax", String.valueOf(availAvgAmountMax));
        setParameterToQuery(sqlQuery, "realSumAmountMin", String.valueOf(realSumAmountMin));
        setParameterToQuery(sqlQuery, "realSumAmountMax", String.valueOf(realSumAmountMax));
        setParameterToQuery(sqlQuery, "realMaxAmountMin", String.valueOf(realMaxAmountMin));
        setParameterToQuery(sqlQuery, "realMaxAmountMax", String.valueOf(realMaxAmountMax));
        setParameterToQuery(sqlQuery, "realMinAmountMin", String.valueOf(realMinAmountMin));
        setParameterToQuery(sqlQuery, "realMinAmountMax", String.valueOf(realMinAmountMax));
        setParameterToQuery(sqlQuery, "realAvgAmountMin", String.valueOf(realAvgAmountMin));
        setParameterToQuery(sqlQuery, "realAvgAmountMax", String.valueOf(realAvgAmountMax));

        if(page != null){
            sqlQuery.setFirstResult(page);
        }
        if(size != null){
            sqlQuery.setMaxResults(size);
        }

        List<Map<String, Object>> ret =  sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return ret;
    }

    /**导出车辆属性收费分析表记录总数4*/
    @Override
    public Integer getAttributeChargeReportCount(Integer page,
                                                 Integer size,
                                                 String beginTime,
                                                 String endTime,
                                                 String depId,
                                                 String depName,
                                                 String carparkId,
                                                 String carparkName,
                                                 String carType,
                                                 Integer chargeCountMin,
                                                 Integer chargeCountMax,
                                                 Float availSumAmountMin,
                                                 Float availSumAmountMax,
                                                 Float availMaxAmountMin,
                                                 Float availMaxAmountMax,
                                                 Float availMinAmountMin,
                                                 Float availMinAmountMax,
                                                 Float availAvgAmountMin,
                                                 Float availAvgAmountMax,
                                                 Float realSumAmountMin,
                                                 Float realSumAmountMax,
                                                 Float realMaxAmountMin,
                                                 Float realMaxAmountMax,
                                                 Float realMinAmountMin,
                                                 Float realMinAmountMax,
                                                 Float realAvgAmountMin,
                                                 Float realAvgAmountMax,
                                                 String reportType) throws BizException{
        String sql = "select " +
                "t1.attribute_charge_dep depId," +
                "t1.attribute_charge_dep_name depName," +
                "t1.attribute_charge_carpark carparkId," +
                "t1.attribute_charge_carpark_name carparkName," +
                "t1.attribute_charge_car_type carType," +
                "t1.attribute_charge_time statisticsTime," +
                "sum(t1.attribute_charge_count) chargeCount," +
                "sum(t1.attribute_charge_avail_sum_amount) availSumAmount," +
                "max(t1.attribute_charge_avail_max_amount) availMaxAmount," +
                "min(t1.attribute_charge_avail_min_amount) availMinAmout," +
                "(sum(t1.attribute_charge_avail_sum_amount) / sum(t1.attribute_charge_count)) availAvgAmout," +
                "sum(t1.attribute_charge_real_sum_amount) realSumAmount," +
                "max(t1.attribute_charge_real_max_amount) realMaxAmount," +
                "min(t1.attribute_charge_real_min_amount) realMinAmout," +
                "(sum(t1.attribute_charge_real_sum_amount) / sum(t1.attribute_charge_count)) realAvgAmout " +
                "from attribute_charge_report t1 " +
                "where " +
                "t1.use_mark >= 0 ";
        sql = putTogetherRESql(sql, "t1.attribute_charge_time", "beginTime", beginTime);
        sql = putTogetherLESql(sql, "t1.attribute_charge_time", "endTime", endTime);
        sql = putTogetherInSql(sql, "t1.attribute_charge_dep", "depId", depId);
        sql = putTogetherLikeSql(sql, "t1.attribute_charge_dep_name", "depName", depName);
        sql = putTogetherInSql(sql, "t1.attribute_charge_carpark", "carparkId", carparkId);
        sql = putTogetherLikeSql(sql, "t1.attribute_charge_carpark_name", "carparkName", carparkName);
        sql = putTogetherLikeSql(sql, "t1.attribute_charge_car_type", "carType", carType);


        if(("0").equals(reportType) || ("1").equals(reportType))
            sql = sql + " group by t1.attribute_charge_dep, t1.attribute_charge_carpark, t1.attribute_charge_car_type, DATE_FORMAT(t1.attribute_charge_time, '%Y-%m-%d') ";
        else if(("2").equals(reportType))
            sql = sql + " group by t1.attribute_charge_dep, t1.attribute_charge_carpark, t1.attribute_charge_car_type, DATE_FORMAT(t1.attribute_charge_time, '%Y-%m') ";
        else
            sql = sql + " group by t1.attribute_charge_dep, t1.attribute_charge_carpark, t1.attribute_charge_car_type, DATE_FORMAT(t1.attribute_charge_time, '%Y') ";

        sql = "SELECT COUNT(*) cnt FROM (" + sql + ") tb1 WHERE 1 = 1 ";
        sql = putTogetherRESql(sql, "tb1.chargeCount", "chargeCountMin", String.valueOf(chargeCountMin));
        sql = putTogetherLESql(sql, "tb1.chargeCount", "chargeCountMax", String.valueOf(chargeCountMax));
        sql = putTogetherRESql(sql, "tb1.availSumAmount", "availSumAmountMin", String.valueOf(availSumAmountMin));
        sql = putTogetherLESql(sql, "tb1.availSumAmount", "availSumAmountMax", String.valueOf(availSumAmountMax));
        sql = putTogetherRESql(sql, "tb1.availMaxAmount", "availMaxAmountMin", String.valueOf(availMaxAmountMin));
        sql = putTogetherLESql(sql, "tb1.availMaxAmount", "availMaxAmountMax", String.valueOf(availMaxAmountMax));
        sql = putTogetherRESql(sql, "tb1.availMinAmount", "availMinAmountMin", String.valueOf(availMinAmountMin));
        sql = putTogetherLESql(sql, "tb1.availMinAmount", "availMinAmountMax", String.valueOf(availMinAmountMax));
        sql = putTogetherRESql(sql, "tb1.availAvgAmount", "availAvgAmountMin", String.valueOf(availAvgAmountMin));
        sql = putTogetherLESql(sql, "tb1.availAvgAmount", "availAvgAmountMax", String.valueOf(availAvgAmountMax));
        sql = putTogetherRESql(sql, "tb1.realSumAmount", "realSumAmountMin", String.valueOf(realSumAmountMin));
        sql = putTogetherLESql(sql, "tb1.realSumAmount", "realSumAmountMax", String.valueOf(realSumAmountMax));
        sql = putTogetherRESql(sql, "tb1.realMaxAmount", "realMaxAmountMin", String.valueOf(realMaxAmountMin));
        sql = putTogetherLESql(sql, "tb1.realMaxAmount", "realMaxAmountMax", String.valueOf(realMaxAmountMax));
        sql = putTogetherRESql(sql, "tb1.realMinAmount", "realMinAmountMin", String.valueOf(realMinAmountMin));
        sql = putTogetherLESql(sql, "tb1.realMinAmount", "realMinAmountMax", String.valueOf(realMinAmountMax));
        sql = putTogetherRESql(sql, "tb1.realAvgAmount", "realAvgAmountMin", String.valueOf(realAvgAmountMin));
        sql = putTogetherLESql(sql, "tb1.realAvgAmount", "realAvgAmountMax", String.valueOf(realAvgAmountMax));
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().
                createSQLQuery(sql);

        setParameterToQuery(sqlQuery, "beginTime", beginTime);
        setParameterToQuery(sqlQuery, "endTime", endTime);
        setParameterToQuery(sqlQuery, "depId", depId);
        setParameterToQueryByLike(sqlQuery, "depName", depName);
        setParameterToQuery(sqlQuery, "carparkId", carparkId);
        setParameterToQueryByLike(sqlQuery, "carparkName", carparkName);
        setParameterToQueryByLike(sqlQuery, "carType", carType);

        setParameterToQuery(sqlQuery, "chargeCountMin", String.valueOf(chargeCountMin));
        setParameterToQuery(sqlQuery, "chargeCountMax", String.valueOf(chargeCountMax));
        setParameterToQuery(sqlQuery, "availSumAmountMin", String.valueOf(availSumAmountMin));
        setParameterToQuery(sqlQuery, "availSumAmountMax", String.valueOf(availSumAmountMax));
        setParameterToQuery(sqlQuery, "availMaxAmountMin", String.valueOf(availMaxAmountMin));
        setParameterToQuery(sqlQuery, "availMaxAmountMax", String.valueOf(availMaxAmountMax));
        setParameterToQuery(sqlQuery, "availMinAmountMin", String.valueOf(availMinAmountMin));
        setParameterToQuery(sqlQuery, "availMinAmountMax", String.valueOf(availMinAmountMax));
        setParameterToQuery(sqlQuery, "availAvgAmountMin", String.valueOf(availAvgAmountMin));
        setParameterToQuery(sqlQuery, "availAvgAmountMax", String.valueOf(availAvgAmountMax));
        setParameterToQuery(sqlQuery, "realSumAmountMin", String.valueOf(realSumAmountMin));
        setParameterToQuery(sqlQuery, "realSumAmountMax", String.valueOf(realSumAmountMax));
        setParameterToQuery(sqlQuery, "realMaxAmountMin", String.valueOf(realMaxAmountMin));
        setParameterToQuery(sqlQuery, "realMaxAmountMax", String.valueOf(realMaxAmountMax));
        setParameterToQuery(sqlQuery, "realMinAmountMin", String.valueOf(realMinAmountMin));
        setParameterToQuery(sqlQuery, "realMinAmountMax", String.valueOf(realMinAmountMax));
        setParameterToQuery(sqlQuery, "realAvgAmountMin", String.valueOf(realAvgAmountMin));
        setParameterToQuery(sqlQuery, "realAvgAmountMax", String.valueOf(realAvgAmountMax));

        List<Map<String, Object>> ret =  sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return Integer.parseInt(ret.get(0).get("cnt").toString());
    }

    /**导出收费类型统计分析表3*/
    @Override
    public List<Map<String, Object>> getChargeTypeReport(Integer page,
                                                         Integer size,
                                                         String beginTime,
                                                         String endTime,
                                                         String depId,
                                                         String depName,
                                                         String carparkId,
                                                         String carparkName,
                                                         String chargeSubType,
                                                         Integer chargeCountMin,
                                                         Integer chargeCountMax,
                                                         Float availSumAmountMin,
                                                         Float availSumAmountMax,
                                                         Float availMaxAmountMin,
                                                         Float availMaxAmountMax,
                                                         Float availMinAmountMin,
                                                         Float availMinAmountMax,
                                                         Float availAvgAmountMin,
                                                         Float availAvgAmountMax,
                                                         Float realSumAmountMin,
                                                         Float realSumAmountMax,
                                                         Float realMaxAmountMin,
                                                         Float realMaxAmountMax,
                                                         Float realMinAmountMin,
                                                         Float realMinAmountMax,
                                                         Float realAvgAmountMin,
                                                         Float realAvgAmountMax,
                                                         String reportType) throws BizException{
        String sql = "select " +
                "t1.charge_type_dep depId," +
                "t1.charge_type_dep_name depName," +
                "t1.charge_type_carpark carparkId," +
                "t1.charge_type_carpark_name carparkName," +
                "t1.charge_type_sub_type chargeSubType,";

        if(("0").equals(reportType) || ("1").equals(reportType))
            sql = sql + "DATE_FORMAT(t1.charge_type_time, '%Y-%m-%d') statisticsTime,";
        else if(("2").equals(reportType))
            sql = sql + "DATE_FORMAT(t1.charge_type_time, '%Y-%m') statisticsTime,";
        else
            sql = sql + "DATE_FORMAT(t1.charge_type_time, '%Y') statisticsTime,";

        sql = sql +
                "sum(t1.charge_type_count) chargeCount," +
                "sum(t1.charge_type_avail_sum_amount) availSumAmount," +
                "max(t1.charge_type_avail_max_amount) availMaxAmount," +
                "min(t1.charge_type_avail_min_amount) availMinAmout," +
                "(sum(t1.charge_type_avail_sum_amount) / sum(t1.charge_type_count)) availAvgAmout," +
                "sum(t1.charge_type_real_sum_amount) realSumAmount," +
                "max(t1.charge_type_real_max_amount) realMaxAmount," +
                "min(t1.charge_type_real_min_amount) realMinAmout," +
                "(sum(t1.charge_type_real_sum_amount) / sum(t1.charge_type_count)) realAvgAmout " +
                "from charge_type_report t1 " +
                "where " +
                "t1.use_mark >= 0 ";
        sql = putTogetherRESql(sql, "t1.charge_type_time", "beginTime", beginTime);
        sql = putTogetherLESql(sql, "t1.charge_type_time", "endTime", endTime);
        sql = putTogetherInSql(sql, "t1.charge_type_dep", "depId", depId);
        sql = putTogetherLikeSql(sql, "t1.charge_type_dep_name", "depName", depName);
        sql = putTogetherInSql(sql, "t1.charge_type_carpark", "carparkId", carparkId);
        sql = putTogetherLikeSql(sql, "t1.charge_type_carpark_name", "carparkName", carparkName);
        sql = putTogetherLikeSql(sql, "t1.charge_type_sub_type", "chargeSubType", chargeSubType);

        if(("0").equals(reportType) || ("1").equals(reportType))
            sql = sql + " group by t1.charge_type_dep, t1.charge_type_carpark, t1.charge_type_sub_type, DATE_FORMAT(t1.charge_type_time, '%Y-%m-%d') ORDER BY t1.charge_type_dep, t1.charge_type_carpark, t1.charge_type_sub_type, DATE_FORMAT(t1.charge_type_time, '%Y-%m-%d') DESC ";
        else if(("2").equals(reportType))
            sql = sql + " group by t1.charge_type_dep, t1.charge_type_carpark, t1.charge_type_sub_type, DATE_FORMAT(t1.charge_type_time, '%Y-%m') ORDER BY t1.charge_type_dep, t1.charge_type_carpark, t1.charge_type_sub_type, DATE_FORMAT(t1.charge_type_time, '%Y-%m') DESC ";
        else
            sql = sql + " group by t1.charge_type_dep, t1.charge_type_carpark, t1.charge_type_sub_type, DATE_FORMAT(t1.charge_type_time, '%Y') ORDER BY t1.charge_type_dep, t1.charge_type_carpark, t1.charge_type_sub_type, DATE_FORMAT(t1.charge_type_time, '%Y') DESC  ";

        sql = "SELECT * FROM (" + sql + ") tb1 WHERE 1 = 1 ";
        sql = putTogetherRESql(sql, "tb1.chargeCount", "chargeCountMin", String.valueOf(chargeCountMin));
        sql = putTogetherLESql(sql, "tb1.chargeCount", "chargeCountMax", String.valueOf(chargeCountMax));
        sql = putTogetherRESql(sql, "tb1.availSumAmount", "availSumAmountMin", String.valueOf(availSumAmountMin));
        sql = putTogetherLESql(sql, "tb1.availSumAmount", "availSumAmountMax", String.valueOf(availSumAmountMax));
        sql = putTogetherRESql(sql, "tb1.availMaxAmount", "availMaxAmountMin", String.valueOf(availMaxAmountMin));
        sql = putTogetherLESql(sql, "tb1.availMaxAmount", "availMaxAmountMax", String.valueOf(availMaxAmountMax));
        sql = putTogetherRESql(sql, "tb1.availMinAmount", "availMinAmountMin", String.valueOf(availMinAmountMin));
        sql = putTogetherLESql(sql, "tb1.availMinAmount", "availMinAmountMax", String.valueOf(availMinAmountMax));
        sql = putTogetherRESql(sql, "tb1.availAvgAmount", "availAvgAmountMin", String.valueOf(availAvgAmountMin));
        sql = putTogetherLESql(sql, "tb1.availAvgAmount", "availAvgAmountMax", String.valueOf(availAvgAmountMax));
        sql = putTogetherRESql(sql, "tb1.realSumAmount", "realSumAmountMin", String.valueOf(realSumAmountMin));
        sql = putTogetherLESql(sql, "tb1.realSumAmount", "realSumAmountMax", String.valueOf(realSumAmountMax));
        sql = putTogetherRESql(sql, "tb1.realMaxAmount", "realMaxAmountMin", String.valueOf(realMaxAmountMin));
        sql = putTogetherLESql(sql, "tb1.realMaxAmount", "realMaxAmountMax", String.valueOf(realMaxAmountMax));
        sql = putTogetherRESql(sql, "tb1.realMinAmount", "realMinAmountMin", String.valueOf(realMinAmountMin));
        sql = putTogetherLESql(sql, "tb1.realMinAmount", "realMinAmountMax", String.valueOf(realMinAmountMax));
        sql = putTogetherRESql(sql, "tb1.realAvgAmount", "realAvgAmountMin", String.valueOf(realAvgAmountMin));
        sql = putTogetherLESql(sql, "tb1.realAvgAmount", "realAvgAmountMax", String.valueOf(realAvgAmountMax));
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().
                createSQLQuery(sql);

        setParameterToQuery(sqlQuery, "beginTime", beginTime);
        setParameterToQuery(sqlQuery, "endTime", endTime);
        setParameterToQuery(sqlQuery, "depId", depId);
        setParameterToQueryByLike(sqlQuery, "depName", depName);
        setParameterToQuery(sqlQuery, "carparkId", carparkId);
        setParameterToQueryByLike(sqlQuery, "carparkName", carparkName);
        setParameterToQueryByLike(sqlQuery, "chargeSubType", chargeSubType);

        setParameterToQuery(sqlQuery, "chargeCountMin", String.valueOf(chargeCountMin));
        setParameterToQuery(sqlQuery, "chargeCountMax", String.valueOf(chargeCountMax));
        setParameterToQuery(sqlQuery, "availSumAmountMin", String.valueOf(availSumAmountMin));
        setParameterToQuery(sqlQuery, "availSumAmountMax", String.valueOf(availSumAmountMax));
        setParameterToQuery(sqlQuery, "availMaxAmountMin", String.valueOf(availMaxAmountMin));
        setParameterToQuery(sqlQuery, "availMaxAmountMax", String.valueOf(availMaxAmountMax));
        setParameterToQuery(sqlQuery, "availMinAmountMin", String.valueOf(availMinAmountMin));
        setParameterToQuery(sqlQuery, "availMinAmountMax", String.valueOf(availMinAmountMax));
        setParameterToQuery(sqlQuery, "availAvgAmountMin", String.valueOf(availAvgAmountMin));
        setParameterToQuery(sqlQuery, "availAvgAmountMax", String.valueOf(availAvgAmountMax));
        setParameterToQuery(sqlQuery, "realSumAmountMin", String.valueOf(realSumAmountMin));
        setParameterToQuery(sqlQuery, "realSumAmountMax", String.valueOf(realSumAmountMax));
        setParameterToQuery(sqlQuery, "realMaxAmountMin", String.valueOf(realMaxAmountMin));
        setParameterToQuery(sqlQuery, "realMaxAmountMax", String.valueOf(realMaxAmountMax));
        setParameterToQuery(sqlQuery, "realMinAmountMin", String.valueOf(realMinAmountMin));
        setParameterToQuery(sqlQuery, "realMinAmountMax", String.valueOf(realMinAmountMax));
        setParameterToQuery(sqlQuery, "realAvgAmountMin", String.valueOf(realAvgAmountMin));
        setParameterToQuery(sqlQuery, "realAvgAmountMax", String.valueOf(realAvgAmountMax));

        if(page != null){
            sqlQuery.setFirstResult(page);
        }
        if(size != null){
            sqlQuery.setMaxResults(size);
        }

        List<Map<String, Object>> ret =  sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return ret;
    }
    /**导出收费类型统计分析表记录总数3*/
    @Override
    public Integer getChargeTypeReportCount(Integer page,
                                            Integer size,
                                            String beginTime,
                                            String endTime,
                                            String depId,
                                            String depName,
                                            String carparkId,
                                            String carparkName,
                                            String chargeSubType,
                                            Integer chargeCountMin,
                                            Integer chargeCountMax,
                                            Float availSumAmountMin,
                                            Float availSumAmountMax,
                                            Float availMaxAmountMin,
                                            Float availMaxAmountMax,
                                            Float availMinAmountMin,
                                            Float availMinAmountMax,
                                            Float availAvgAmountMin,
                                            Float availAvgAmountMax,
                                            Float realSumAmountMin,
                                            Float realSumAmountMax,
                                            Float realMaxAmountMin,
                                            Float realMaxAmountMax,
                                            Float realMinAmountMin,
                                            Float realMinAmountMax,
                                            Float realAvgAmountMin,
                                            Float realAvgAmountMax,
                                            String reportType) throws BizException{
        String sql = "select " +
                "t1.charge_type_dep depId," +
                "t1.charge_type_dep_name depName," +
                "t1.charge_type_carpark_name carparkName," +
                "t1.charge_type_sub_type chargeSubType," +
                "t1.charge_type_time statisticsTime," +
                "sum(t1.charge_type_count) chargeCount," +
                "sum(t1.charge_type_avail_sum_amount) availSumAmount," +
                "max(t1.charge_type_avail_max_amount) availMaxAmount," +
                "min(t1.charge_type_avail_min_amount) availMinAmout," +
                "(sum(t1.charge_type_avail_sum_amount) / sum(t1.charge_type_count)) availAvgAmout," +
                "sum(t1.charge_type_real_sum_amount) realSumAmount," +
                "max(t1.charge_type_real_max_amount) realMaxAmount," +
                "min(t1.charge_type_real_min_amount) realMinAmout," +
                "(sum(t1.charge_type_real_sum_amount) / sum(t1.charge_type_count)) realAvgAmout " +
                "from charge_type_report t1 " +
                "where " +
                "t1.use_mark >= 0 ";
        sql = putTogetherRESql(sql, "t1.charge_type_time", "beginTime", beginTime);
        sql = putTogetherLESql(sql, "t1.charge_type_time", "endTime", endTime);
        sql = putTogetherInSql(sql, "t1.charge_type_dep", "depId", depId);
        sql = putTogetherLikeSql(sql, "t1.charge_type_dep_name", "depName", depName);
        sql = putTogetherInSql(sql, "t1.charge_type_carpark", "carparkId", carparkId);
        sql = putTogetherLikeSql(sql, "t1.charge_type_carpark_name", "carparkName", carparkName);
        sql = putTogetherLikeSql(sql, "t1.charge_type_sub_type", "chargeSubType", chargeSubType);

        if(("0").equals(reportType) || ("1").equals(reportType))
            sql = sql + " group by t1.charge_type_dep, t1.charge_type_carpark, t1.charge_type_sub_type, DATE_FORMAT(t1.charge_type_time, '%Y-%m-%d') ";
        else if(("2").equals(reportType))
            sql = sql + " group by t1.charge_type_dep, t1.charge_type_carpark, t1.charge_type_sub_type, DATE_FORMAT(t1.charge_type_time, '%Y-%m') ";
        else
            sql = sql + " group by t1.charge_type_dep, t1.charge_type_carpark, t1.charge_type_sub_type, DATE_FORMAT(t1.charge_type_time, '%Y') ";

        sql = "SELECT COUNT(*) cnt FROM (" + sql + ") tb1 WHERE 1 = 1 ";
        sql = putTogetherRESql(sql, "tb1.chargeCount", "chargeCountMin", String.valueOf(chargeCountMin));
        sql = putTogetherLESql(sql, "tb1.chargeCount", "chargeCountMax", String.valueOf(chargeCountMax));
        sql = putTogetherRESql(sql, "tb1.availSumAmount", "availSumAmountMin", String.valueOf(availSumAmountMin));
        sql = putTogetherLESql(sql, "tb1.availSumAmount", "availSumAmountMax", String.valueOf(availSumAmountMax));
        sql = putTogetherRESql(sql, "tb1.availMaxAmount", "availMaxAmountMin", String.valueOf(availMaxAmountMin));
        sql = putTogetherLESql(sql, "tb1.availMaxAmount", "availMaxAmountMax", String.valueOf(availMaxAmountMax));
        sql = putTogetherRESql(sql, "tb1.availMinAmount", "availMinAmountMin", String.valueOf(availMinAmountMin));
        sql = putTogetherLESql(sql, "tb1.availMinAmount", "availMinAmountMax", String.valueOf(availMinAmountMax));
        sql = putTogetherRESql(sql, "tb1.availAvgAmount", "availAvgAmountMin", String.valueOf(availAvgAmountMin));
        sql = putTogetherLESql(sql, "tb1.availAvgAmount", "availAvgAmountMax", String.valueOf(availAvgAmountMax));
        sql = putTogetherRESql(sql, "tb1.realSumAmount", "realSumAmountMin", String.valueOf(realSumAmountMin));
        sql = putTogetherLESql(sql, "tb1.realSumAmount", "realSumAmountMax", String.valueOf(realSumAmountMax));
        sql = putTogetherRESql(sql, "tb1.realMaxAmount", "realMaxAmountMin", String.valueOf(realMaxAmountMin));
        sql = putTogetherLESql(sql, "tb1.realMaxAmount", "realMaxAmountMax", String.valueOf(realMaxAmountMax));
        sql = putTogetherRESql(sql, "tb1.realMinAmount", "realMinAmountMin", String.valueOf(realMinAmountMin));
        sql = putTogetherLESql(sql, "tb1.realMinAmount", "realMinAmountMax", String.valueOf(realMinAmountMax));
        sql = putTogetherRESql(sql, "tb1.realAvgAmount", "realAvgAmountMin", String.valueOf(realAvgAmountMin));
        sql = putTogetherLESql(sql, "tb1.realAvgAmount", "realAvgAmountMax", String.valueOf(realAvgAmountMax));
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().
                createSQLQuery(sql);

        setParameterToQuery(sqlQuery, "beginTime", beginTime);
        setParameterToQuery(sqlQuery, "endTime", endTime);
        setParameterToQuery(sqlQuery, "depId", depId);
        setParameterToQueryByLike(sqlQuery, "depName", depName);
        setParameterToQuery(sqlQuery, "carparkId", carparkId);
        setParameterToQueryByLike(sqlQuery, "carparkName", carparkName);
        setParameterToQueryByLike(sqlQuery, "chargeSubType", chargeSubType);

        setParameterToQuery(sqlQuery, "chargeCountMin", String.valueOf(chargeCountMin));
        setParameterToQuery(sqlQuery, "chargeCountMax", String.valueOf(chargeCountMax));
        setParameterToQuery(sqlQuery, "availSumAmountMin", String.valueOf(availSumAmountMin));
        setParameterToQuery(sqlQuery, "availSumAmountMax", String.valueOf(availSumAmountMax));
        setParameterToQuery(sqlQuery, "availMaxAmountMin", String.valueOf(availMaxAmountMin));
        setParameterToQuery(sqlQuery, "availMaxAmountMax", String.valueOf(availMaxAmountMax));
        setParameterToQuery(sqlQuery, "availMinAmountMin", String.valueOf(availMinAmountMin));
        setParameterToQuery(sqlQuery, "availMinAmountMax", String.valueOf(availMinAmountMax));
        setParameterToQuery(sqlQuery, "availAvgAmountMin", String.valueOf(availAvgAmountMin));
        setParameterToQuery(sqlQuery, "availAvgAmountMax", String.valueOf(availAvgAmountMax));
        setParameterToQuery(sqlQuery, "realSumAmountMin", String.valueOf(realSumAmountMin));
        setParameterToQuery(sqlQuery, "realSumAmountMax", String.valueOf(realSumAmountMax));
        setParameterToQuery(sqlQuery, "realMaxAmountMin", String.valueOf(realMaxAmountMin));
        setParameterToQuery(sqlQuery, "realMaxAmountMax", String.valueOf(realMaxAmountMax));
        setParameterToQuery(sqlQuery, "realMinAmountMin", String.valueOf(realMinAmountMin));
        setParameterToQuery(sqlQuery, "realMinAmountMax", String.valueOf(realMinAmountMax));
        setParameterToQuery(sqlQuery, "realAvgAmountMin", String.valueOf(realAvgAmountMin));
        setParameterToQuery(sqlQuery, "realAvgAmountMax", String.valueOf(realAvgAmountMax));

        List<Map<String, Object>> ret =  sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return Integer.parseInt(ret.get(0).get("cnt").toString());
    }

    /**导出岗亭收费情况分析表2*/
    @Override
    public List<Map<String, Object>> getBoothChargesReport(Integer page,
                                                           Integer size,
                                                           String beginTime,
                                                           String endTime,
                                                           String depId,
                                                           String depName,
                                                           String carparkId,
                                                           String carparkName,
                                                           String boothId,
                                                           String boothName,
                                                           Integer chargeCountMin,
                                                           Integer chargeCountMax,
                                                           Float availSumAmountMin,
                                                           Float availSumAmountMax,
                                                           Float availMaxAmountMin,
                                                           Float availMaxAmountMax,
                                                           Float availMinAmountMin,
                                                           Float availMinAmountMax,
                                                           Float availAvgAmountMin,
                                                           Float availAvgAmountMax,
                                                           Float realSumAmountMin,
                                                           Float realSumAmountMax,
                                                           Float realMaxAmountMin,
                                                           Float realMaxAmountMax,
                                                           Float realMinAmountMin,
                                                           Float realMinAmountMax,
                                                           Float realAvgAmountMin,
                                                           Float realAvgAmountMax,
                                                           String reportType) throws BizException{
        String sql = "select " +
                "t1.booth_charge_dep depId," +
                "t1.booth_charge_dep_name depName," +
                "t1.booth_charge_carpark carparkId," +
                "t1.booth_charge_carpark_name carparkName," +
                "t1.booth_charge_post boothId," +
                "t1.booth_charge_post_name boothName,";
        if(("0").equals(reportType) || ("1").equals(reportType))
            sql = sql + "DATE_FORMAT(t1.booth_charge_time, '%Y-%m-%d') statisticsTime,";
        else if(("2").equals(reportType))
            sql = sql + "DATE_FORMAT(t1.booth_charge_time, '%Y-%m') statisticsTime,";
        else
            sql = sql + "DATE_FORMAT(t1.booth_charge_time, '%Y') statisticsTime,";

        sql = sql +
                "sum(t1.booth_charge_count) chargeCount," +
                "sum(t1.booth_charge_avail_sum_amount) availSumAmount," +
                "max(t1.booth_charge_avail_max_amount) availMaxAmount," +
                "min(t1.booth_charge_avail_min_amount) availMinAmout," +
                "(sum(t1.booth_charge_avail_sum_amount) / sum(t1.booth_charge_count)) availAvgAmout," +
                "sum(t1.booth_charge_real_sum_amount) realSumAmount," +
                "max(t1.booth_charge_real_max_amount) realMaxAmount," +
                "min(t1.booth_charge_real_min_amount) realMinAmout," +
                "(sum(t1.booth_charge_real_sum_amount) / sum(t1.booth_charge_count)) realAvgAmout " +
                "from booth_charge_report t1 " +
                "where " +
                "t1.use_mark >= 0 ";
        sql = putTogetherRESql(sql, "t1.booth_charge_time", "beginTime", beginTime);
        sql = putTogetherLESql(sql, "t1.booth_charge_time", "endTime", endTime);
        sql = putTogetherInSql(sql, "t1.booth_charge_dep", "depId", depId);
        sql = putTogetherLikeSql(sql, "t1.booth_charge_dep_name", "depName", depName);
        sql = putTogetherInSql(sql, "t1.booth_charge_carpark", "carparkId", carparkId);
        sql = putTogetherLikeSql(sql, "t1.booth_charge_carpark_name", "carparkName", carparkName);
        sql = putTogetherInSql(sql, "t1.booth_charge_post", "boothId", boothId);
        sql = putTogetherLikeSql(sql, "t1.booth_charge_post_name", "boothName", boothName);


        if(("0").equals(reportType) || ("1").equals(reportType))
            sql = sql + " group by t1.booth_charge_dep, t1.booth_charge_carpark, t1.booth_charge_post, DATE_FORMAT(t1.booth_charge_time, '%Y-%m-%d') ORDER BY t1.booth_charge_dep, t1.booth_charge_carpark, t1.booth_charge_post, DATE_FORMAT(t1.booth_charge_time, '%Y-%m-%d') DESC ";
        else if(("2").equals(reportType))
            sql = sql + " group by t1.booth_charge_dep, t1.booth_charge_carpark, t1.booth_charge_post, DATE_FORMAT(t1.booth_charge_time, '%Y-%m') ORDER BY t1.booth_charge_dep, t1.booth_charge_carpark, t1.booth_charge_post, DATE_FORMAT(t1.booth_charge_time, '%Y-%m') DESC ";
        else
            sql = sql + " group by t1.booth_charge_dep, t1.booth_charge_carpark, t1.booth_charge_post, DATE_FORMAT(t1.booth_charge_time, '%Y') ORDER BY t1.booth_charge_dep, t1.booth_charge_carpark, t1.booth_charge_post, DATE_FORMAT(t1.booth_charge_time, '%Y') DESC  ";

        sql = "SELECT * FROM (" + sql + ") tb1 WHERE 1 = 1 ";
        sql = putTogetherRESql(sql, "tb1.chargeCount", "chargeCountMin", String.valueOf(chargeCountMin));
        sql = putTogetherLESql(sql, "tb1.chargeCount", "chargeCountMax", String.valueOf(chargeCountMax));
        sql = putTogetherRESql(sql, "tb1.availSumAmount", "availSumAmountMin", String.valueOf(availSumAmountMin));
        sql = putTogetherLESql(sql, "tb1.availSumAmount", "availSumAmountMax", String.valueOf(availSumAmountMax));
        sql = putTogetherRESql(sql, "tb1.availMaxAmount", "availMaxAmountMin", String.valueOf(availMaxAmountMin));
        sql = putTogetherLESql(sql, "tb1.availMaxAmount", "availMaxAmountMax", String.valueOf(availMaxAmountMax));
        sql = putTogetherRESql(sql, "tb1.availMinAmount", "availMinAmountMin", String.valueOf(availMinAmountMin));
        sql = putTogetherLESql(sql, "tb1.availMinAmount", "availMinAmountMax", String.valueOf(availMinAmountMax));
        sql = putTogetherRESql(sql, "tb1.availAvgAmount", "availAvgAmountMin", String.valueOf(availAvgAmountMin));
        sql = putTogetherLESql(sql, "tb1.availAvgAmount", "availAvgAmountMax", String.valueOf(availAvgAmountMax));
        sql = putTogetherRESql(sql, "tb1.realSumAmount", "realSumAmountMin", String.valueOf(realSumAmountMin));
        sql = putTogetherLESql(sql, "tb1.realSumAmount", "realSumAmountMax", String.valueOf(realSumAmountMax));
        sql = putTogetherRESql(sql, "tb1.realMaxAmount", "realMaxAmountMin", String.valueOf(realMaxAmountMin));
        sql = putTogetherLESql(sql, "tb1.realMaxAmount", "realMaxAmountMax", String.valueOf(realMaxAmountMax));
        sql = putTogetherRESql(sql, "tb1.realMinAmount", "realMinAmountMin", String.valueOf(realMinAmountMin));
        sql = putTogetherLESql(sql, "tb1.realMinAmount", "realMinAmountMax", String.valueOf(realMinAmountMax));
        sql = putTogetherRESql(sql, "tb1.realAvgAmount", "realAvgAmountMin", String.valueOf(realAvgAmountMin));
        sql = putTogetherLESql(sql, "tb1.realAvgAmount", "realAvgAmountMax", String.valueOf(realAvgAmountMax));
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().
                createSQLQuery(sql);

        setParameterToQuery(sqlQuery, "beginTime", beginTime);
        setParameterToQuery(sqlQuery, "endTime", endTime);
        setParameterToQuery(sqlQuery, "depId", depId);
        setParameterToQueryByLike(sqlQuery, "depName", depName);
        setParameterToQuery(sqlQuery, "carparkId", carparkId);
        setParameterToQueryByLike(sqlQuery, "carparkName", carparkName);
        setParameterToQuery(sqlQuery, "boothId", boothId);
        setParameterToQueryByLike(sqlQuery, "boothName", boothName);

        setParameterToQuery(sqlQuery, "chargeCountMin", String.valueOf(chargeCountMin));
        setParameterToQuery(sqlQuery, "chargeCountMax", String.valueOf(chargeCountMax));
        setParameterToQuery(sqlQuery, "availSumAmountMin", String.valueOf(availSumAmountMin));
        setParameterToQuery(sqlQuery, "availSumAmountMax", String.valueOf(availSumAmountMax));
        setParameterToQuery(sqlQuery, "availMaxAmountMin", String.valueOf(availMaxAmountMin));
        setParameterToQuery(sqlQuery, "availMaxAmountMax", String.valueOf(availMaxAmountMax));
        setParameterToQuery(sqlQuery, "availMinAmountMin", String.valueOf(availMinAmountMin));
        setParameterToQuery(sqlQuery, "availMinAmountMax", String.valueOf(availMinAmountMax));
        setParameterToQuery(sqlQuery, "availAvgAmountMin", String.valueOf(availAvgAmountMin));
        setParameterToQuery(sqlQuery, "availAvgAmountMax", String.valueOf(availAvgAmountMax));
        setParameterToQuery(sqlQuery, "realSumAmountMin", String.valueOf(realSumAmountMin));
        setParameterToQuery(sqlQuery, "realSumAmountMax", String.valueOf(realSumAmountMax));
        setParameterToQuery(sqlQuery, "realMaxAmountMin", String.valueOf(realMaxAmountMin));
        setParameterToQuery(sqlQuery, "realMaxAmountMax", String.valueOf(realMaxAmountMax));
        setParameterToQuery(sqlQuery, "realMinAmountMin", String.valueOf(realMinAmountMin));
        setParameterToQuery(sqlQuery, "realMinAmountMax", String.valueOf(realMinAmountMax));
        setParameterToQuery(sqlQuery, "realAvgAmountMin", String.valueOf(realAvgAmountMin));
        setParameterToQuery(sqlQuery, "realAvgAmountMax", String.valueOf(realAvgAmountMax));

        if(page != null){
            sqlQuery.setFirstResult(page);
        }
        if(size != null){
            sqlQuery.setMaxResults(size);
        }

        List<Map<String, Object>> ret =  sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return ret;
    }

    /**导出岗亭收费情况分析表记录总数2*/
    @Override
    public Integer getBoothChargesReportCount(Integer page,
                                              Integer size,
                                              String beginTime,
                                              String endTime,
                                              String depId,
                                              String depName,
                                              String carparkId,
                                              String carparkName,
                                              String boothId,
                                              String boothName,
                                              Integer chargeCountMin,
                                              Integer chargeCountMax,
                                              Float availSumAmountMin,
                                              Float availSumAmountMax,
                                              Float availMaxAmountMin,
                                              Float availMaxAmountMax,
                                              Float availMinAmountMin,
                                              Float availMinAmountMax,
                                              Float availAvgAmountMin,
                                              Float availAvgAmountMax,
                                              Float realSumAmountMin,
                                              Float realSumAmountMax,
                                              Float realMaxAmountMin,
                                              Float realMaxAmountMax,
                                              Float realMinAmountMin,
                                              Float realMinAmountMax,
                                              Float realAvgAmountMin,
                                              Float realAvgAmountMax,
                                              String reportType) throws BizException{
        String sql = "select " +
                "t1.booth_charge_dep_name depName," +
                "t1.booth_charge_carpark_name carparkName," +
                "t1.booth_charge_post_name postName," +
                "t1.booth_charge_time statisticsTime," +
                "sum(t1.booth_charge_count) chargeCount," +
                "sum(t1.booth_charge_avail_sum_amount) availSumAmount," +
                "max(t1.booth_charge_avail_max_amount) availMaxAmount," +
                "min(t1.booth_charge_avail_min_amount) availMinAmout," +
                "(sum(t1.booth_charge_avail_sum_amount) / sum(t1.booth_charge_count)) availAvgAmout," +
                "sum(t1.booth_charge_real_sum_amount) realSumAmount," +
                "max(t1.booth_charge_real_max_amount) realMaxAmount," +
                "min(t1.booth_charge_real_min_amount) realMinAmout," +
                "(sum(t1.booth_charge_real_sum_amount) / sum(t1.booth_charge_count)) realAvgAmout " +
                "from booth_charge_report t1 " +
                "where " +
                "t1.use_mark >= 0 ";
        sql = putTogetherRESql(sql, "t1.booth_charge_time", "beginTime", beginTime);
        sql = putTogetherLESql(sql, "t1.booth_charge_time", "endTime", endTime);
        sql = putTogetherInSql(sql, "t1.booth_charge_dep", "depId", depId);
        sql = putTogetherLikeSql(sql, "t1.booth_charge_dep_name", "depName", depName);
        sql = putTogetherInSql(sql, "t1.booth_charge_carpark", "carparkId", carparkId);
        sql = putTogetherLikeSql(sql, "t1.booth_charge_carpark_name", "carparkName", carparkName);
        sql = putTogetherInSql(sql, "t1.booth_charge_post", "boothId", boothId);
        sql = putTogetherLikeSql(sql, "t1.booth_charge_post_name", "boothName", boothName);


        if(("0").equals(reportType) || ("1").equals(reportType))
            sql = sql + " group by t1.booth_charge_dep, t1.booth_charge_carpark, t1.booth_charge_post, DATE_FORMAT(t1.booth_charge_time, '%Y-%m-%d') ";
        else if(("2").equals(reportType))
            sql = sql + " group by t1.booth_charge_dep, t1.booth_charge_carpark, t1.booth_charge_post, DATE_FORMAT(t1.booth_charge_time, '%Y-%m') ";
        else
            sql = sql + " group by t1.booth_charge_dep, t1.booth_charge_carpark, t1.booth_charge_post, DATE_FORMAT(t1.booth_charge_time, '%Y') ";

        sql = "SELECT COUNT(*) cnt FROM (" + sql + ") tb1 WHERE 1 = 1 ";
        sql = putTogetherRESql(sql, "tb1.chargeCount", "chargeCountMin", String.valueOf(chargeCountMin));
        sql = putTogetherLESql(sql, "tb1.chargeCount", "chargeCountMax", String.valueOf(chargeCountMax));
        sql = putTogetherRESql(sql, "tb1.availSumAmount", "availSumAmountMin", String.valueOf(availSumAmountMin));
        sql = putTogetherLESql(sql, "tb1.availSumAmount", "availSumAmountMax", String.valueOf(availSumAmountMax));
        sql = putTogetherRESql(sql, "tb1.availMaxAmount", "availMaxAmountMin", String.valueOf(availMaxAmountMin));
        sql = putTogetherLESql(sql, "tb1.availMaxAmount", "availMaxAmountMax", String.valueOf(availMaxAmountMax));
        sql = putTogetherRESql(sql, "tb1.availMinAmount", "availMinAmountMin", String.valueOf(availMinAmountMin));
        sql = putTogetherLESql(sql, "tb1.availMinAmount", "availMinAmountMax", String.valueOf(availMinAmountMax));
        sql = putTogetherRESql(sql, "tb1.availAvgAmount", "availAvgAmountMin", String.valueOf(availAvgAmountMin));
        sql = putTogetherLESql(sql, "tb1.availAvgAmount", "availAvgAmountMax", String.valueOf(availAvgAmountMax));
        sql = putTogetherRESql(sql, "tb1.realSumAmount", "realSumAmountMin", String.valueOf(realSumAmountMin));
        sql = putTogetherLESql(sql, "tb1.realSumAmount", "realSumAmountMax", String.valueOf(realSumAmountMax));
        sql = putTogetherRESql(sql, "tb1.realMaxAmount", "realMaxAmountMin", String.valueOf(realMaxAmountMin));
        sql = putTogetherLESql(sql, "tb1.realMaxAmount", "realMaxAmountMax", String.valueOf(realMaxAmountMax));
        sql = putTogetherRESql(sql, "tb1.realMinAmount", "realMinAmountMin", String.valueOf(realMinAmountMin));
        sql = putTogetherLESql(sql, "tb1.realMinAmount", "realMinAmountMax", String.valueOf(realMinAmountMax));
        sql = putTogetherRESql(sql, "tb1.realAvgAmount", "realAvgAmountMin", String.valueOf(realAvgAmountMin));
        sql = putTogetherLESql(sql, "tb1.realAvgAmount", "realAvgAmountMax", String.valueOf(realAvgAmountMax));
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().
                createSQLQuery(sql);

        setParameterToQuery(sqlQuery, "beginTime", beginTime);
        setParameterToQuery(sqlQuery, "endTime", endTime);
        setParameterToQuery(sqlQuery, "depId", depId);
        setParameterToQueryByLike(sqlQuery, "depName", depName);
        setParameterToQuery(sqlQuery, "carparkId", carparkId);
        setParameterToQueryByLike(sqlQuery, "carparkName", carparkName);
        setParameterToQuery(sqlQuery, "boothId", boothId);
        setParameterToQueryByLike(sqlQuery, "boothName", boothName);

        setParameterToQuery(sqlQuery, "chargeCountMin", String.valueOf(chargeCountMin));
        setParameterToQuery(sqlQuery, "chargeCountMax", String.valueOf(chargeCountMax));
        setParameterToQuery(sqlQuery, "availSumAmountMin", String.valueOf(availSumAmountMin));
        setParameterToQuery(sqlQuery, "availSumAmountMax", String.valueOf(availSumAmountMax));
        setParameterToQuery(sqlQuery, "availMaxAmountMin", String.valueOf(availMaxAmountMin));
        setParameterToQuery(sqlQuery, "availMaxAmountMax", String.valueOf(availMaxAmountMax));
        setParameterToQuery(sqlQuery, "availMinAmountMin", String.valueOf(availMinAmountMin));
        setParameterToQuery(sqlQuery, "availMinAmountMax", String.valueOf(availMinAmountMax));
        setParameterToQuery(sqlQuery, "availAvgAmountMin", String.valueOf(availAvgAmountMin));
        setParameterToQuery(sqlQuery, "availAvgAmountMax", String.valueOf(availAvgAmountMax));
        setParameterToQuery(sqlQuery, "realSumAmountMin", String.valueOf(realSumAmountMin));
        setParameterToQuery(sqlQuery, "realSumAmountMax", String.valueOf(realSumAmountMax));
        setParameterToQuery(sqlQuery, "realMaxAmountMin", String.valueOf(realMaxAmountMin));
        setParameterToQuery(sqlQuery, "realMaxAmountMax", String.valueOf(realMaxAmountMax));
        setParameterToQuery(sqlQuery, "realMinAmountMin", String.valueOf(realMinAmountMin));
        setParameterToQuery(sqlQuery, "realMinAmountMax", String.valueOf(realMinAmountMax));
        setParameterToQuery(sqlQuery, "realAvgAmountMin", String.valueOf(realAvgAmountMin));
        setParameterToQuery(sqlQuery, "realAvgAmountMax", String.valueOf(realAvgAmountMax));

        List<Map<String, Object>> ret =  sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return Integer.parseInt(ret.get(0).get("cnt").toString());
    }

    /**导出收费情况统计报表1*/
    @Override
    public List<Map<String, Object>> getAllChargesReport(Integer page,
                                                         Integer size,
                                                         String beginTime,
                                                         String endTime,
                                                         String chargeReceiveCarNo,
                                                         String depName,
                                                         String carparkName,
                                                         String chargeReceiveSubType,
                                                         Float chargeReceiveAmountMin,
                                                         Float chargeReceiveAmountMax,
                                                         String chargeDiscountInfo,
                                                         String chargeReceivePostName,
                                                         String memo,
                                                         String operatorName,
                                                         String chargeReceiveId,
                                                         String carparkId,
                                                         String depId,
                                                         String chargeReceivePost,
                                                         String chargeReceiveOperatorId,
                                                         String chargeReceiveCarpark) throws BizException {

        String sql = "select t1.charge_receive_car_no chargeReceiveCarNo," +
                "t1.charge_receive_sub_type chargeReceiveSubType," +
                "t1.charge_discount_info chargeDiscountInfo," +
                "t1.charge_receive_post_name chargeReceivePostName," +
                "date_format(t1.charge_receive_time, '%Y-%m-%d %T') chargeReceiveTime," +
                "t1.memo memo," +
                "t1.charge_receive_id chargeReceiveId," +
                "t1.charge_receive_carpark chargeReceiveCarpark," +
                "t1.charge_receive_post chargeReceivePost," +
                "t1.charge_receive_operator_id chargeReceiveOperatorId," +
                "t5.dep_id depId, " +
                "CASE WHEN (t1.charge_receive_amount - t1.charge_discount_amount) < 0 THEN 0 ELSE (t1.charge_receive_amount - t1.charge_discount_amount) END chargeRealAmount," +
                "case when t1.charge_receive_operator_id = \"-2\" then \"云支付\" " +
                "when t1.charge_receive_operator_id = \"1\" then \"微信支付\" else t3.operator_name end as operatorName, " +
                "case when t1.charge_receive_post = \"0ae8172b7957a7a295cb0c291d204688\" then \"云平台\" " +
                "when t1.charge_receive_post = \"WECHAT_PAY\" then \"云平台\" else t4.post_computer_name end as postComputerName, " +
                "case when t2.carpark_name is NULL THEN '' else t2.carpark_name end carparkName, " +
                "CASE WHEN t5.dep_name is NULL THEN \"\" ELSE t5.dep_name END depName " +
                "from charge_receive_record t1 " +
                "LEFT JOIN carpark_info t2 ON t1.charge_receive_carpark = t2.carpark_id " +
                "left join operator t3 on " +
                "t1.charge_receive_operator_id = t3.operator_id " +
                "left join post_computer_manage t4 on t1.charge_receive_post = t4.post_computer_id " +
                "LEFT JOIN department_info t5 ON t5.dep_id = t2.department_id " +
                "where " +
                "t1.charge_receive_operation = \"charge\"  and t1.use_mark >= 0 ";

        sql = putTogetherLikeSql(sql, "t1.charge_receive_car_no", "chargeReceiveCarNo", chargeReceiveCarNo);
        sql = putTogetherLikeSql(sql, "t5.dep_name", "depName", depName);
        sql = putTogetherLikeSql(sql, "t2.carpark_name", "carparkName", carparkName);
        sql = putTogetherRESql(sql, "t1.charge_receive_time", "beginTime", beginTime);
        sql = putTogetherLESql(sql, "t1.charge_receive_time", "endTime", endTime);
        sql = putTogetherEqualSql(sql, "t5.dep_id", "depId", depId);
        sql = putTogetherInSql(sql, "t1.charge_receive_carpark", "chargeReceiveCarpark", chargeReceiveCarpark);
        sql = putTogetherEqualSql(sql, "t1.charge_receive_sub_type", "chargeReceiveSubType", chargeReceiveSubType);
        sql = putTogetherInSql(sql, "t1.charge_receive_carpark", "chargeReceiveCarpark", chargeReceiveCarpark);
        sql = putTogetherRESql(sql, "(t1.charge_receive_amount - t1.charge_discount_amount)", "chargeReceiveAmountMin", String.valueOf(chargeReceiveAmountMin));
        sql = putTogetherLESql(sql, "(t1.charge_receive_amount - t1.charge_discount_amount)", "chargeReceiveAmountMax", String.valueOf(chargeReceiveAmountMax));
        sql = putTogetherEqualSql(sql, "t1.charge_discount_info", "chargeDiscountInfo", chargeDiscountInfo);
        sql = putTogetherEqualSql(sql, "t1.charge_receive_post", "chargeReceivePost", chargeReceivePost);
        sql = putTogetherLikeSql(sql, "t1.memo", "memo", memo);
        sql = putTogetherEqualSql(sql, "t1.charge_receive_operator_id", "chargeReceiveOperatorId", chargeReceiveOperatorId);
        sql = putTogetherEqualSql(sql, "t1.charge_receive_id", "chargeReceiveId", chargeReceiveId);
        sql = sql + "order by t1.charge_receive_time desc";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().
                createSQLQuery(sql);
        setParameterToQueryByLike(sqlQuery, "chargeReceiveCarNo", chargeReceiveCarNo);
        setParameterToQueryByLike(sqlQuery, "depName", depName);
        setParameterToQueryByLike(sqlQuery, "carparkName", carparkName);
        setParameterToQuery(sqlQuery, "beginTime", beginTime);
        setParameterToQuery(sqlQuery, "endTime", endTime);
        setParameterToQuery(sqlQuery, "depId", depId);
        setParameterToQuery(sqlQuery, "chargeReceiveCarpark", chargeReceiveCarpark);
        setParameterToQuery(sqlQuery, "chargeReceiveSubType", chargeReceiveSubType);
        setParameterToQuery(sqlQuery, "chargeReceiveAmountMin", String.valueOf(chargeReceiveAmountMin));
        setParameterToQuery(sqlQuery, "chargeReceiveAmountMax", String.valueOf(chargeReceiveAmountMax));
        setParameterToQuery(sqlQuery, "chargeDiscountInfo", chargeDiscountInfo);
        setParameterToQuery(sqlQuery, "chargeReceivePost", chargeReceivePost);
        setParameterToQueryByLike(sqlQuery, "memo", memo);
        setParameterToQuery(sqlQuery, "chargeReceiveOperatorId", chargeReceiveOperatorId);
        setParameterToQuery(sqlQuery, "chargeReceiveId", chargeReceiveId);

        if(page != null){
            sqlQuery.setFirstResult(page);
        }
        if(size != null){
            sqlQuery.setMaxResults(size);
        }

        List<Map<String, Object>> ret =  sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return ret;
    }

    @Override
    public List<Map<String, Object>> getDepInfo(String carparkId) throws BizException{

        String hql = "from CarparkInfo where carparkId = ?";
        List<Map<String, Object>> ret = (List<Map<String, Object>>) baseDao.queryForList(hql, carparkId);

//        CarparkInfo carparkInfo = (CarparkInfo) baseDao.getById(CarparkInfo.class, carparkId);
//        if (!StringUtils.isBlank(carparkInfo.getDepartmentInfo().getDepId())) {
//            String s=carparkInfo.getDepartmentInfo().getDepId();
//        }

        return ret;
    }

    @Override
    public Integer getAllChargesReportCount(Integer page,
                                            Integer size,
                                            String beginTime,
                                            String endTime,
                                            String chargeReceiveCarNo,
                                            String depName,
                                            String carparkName,
                                            String chargeReceiveSubType,
                                            Float chargeReceiveAmountMin,
                                            Float chargeReceiveAmountMax,
                                            String chargeDiscountInfo,
                                            String chargeReceivePostName,
                                            String memo,
                                            String operatorName,
                                            String chargeReceiveId,
                                            String carparkId,
                                            String depId,
                                            String chargeReceivePost,
                                            String chargeReceiveOperatorId,
                                            String chargeReceiveCarpark) throws BizException{
        String sql = "select COUNT(t1.charge_receive_car_no) cnt " +
                "from charge_receive_record t1 " +
                "LEFT JOIN carpark_info t2 ON t1.charge_receive_carpark = t2.carpark_id " +
                "left join operator t3 on " +
                "t1.charge_receive_operator_id = t3.operator_id " +
                "left join post_computer_manage t4 on t1.charge_receive_post = t4.post_computer_id " +
                "LEFT JOIN department_info t5 ON t5.dep_id = t2.department_id " +
                "where " +
                "t1.charge_receive_operation = \"charge\" and t1.use_mark >= 0 ";

        sql = putTogetherLikeSql(sql, "t1.charge_receive_car_no", "chargeReceiveCarNo", chargeReceiveCarNo);
        sql = putTogetherLikeSql(sql, "t5.dep_name", "depName", depName);
        sql = putTogetherLikeSql(sql, "t2.carpark_name", "carparkName", carparkName);
        sql = putTogetherRESql(sql, "t1.charge_receive_time", "beginTime", beginTime);
        sql = putTogetherLESql(sql, "t1.charge_receive_time", "endTime", endTime);
        sql = putTogetherEqualSql(sql, "t5.dep_id", "depId", depId);
        sql = putTogetherInSql(sql, "t1.charge_receive_carpark", "chargeReceiveCarpark", chargeReceiveCarpark);
        sql = putTogetherEqualSql(sql, "t1.charge_receive_sub_type", "chargeReceiveSubType", chargeReceiveSubType);
        sql = putTogetherInSql(sql, "t1.charge_receive_carpark", "chargeReceiveCarpark", chargeReceiveCarpark);
        sql = putTogetherRESql(sql, "(t1.charge_receive_amount - t1.charge_discount_amount)", "chargeReceiveAmountMin", String.valueOf(chargeReceiveAmountMin));
        sql = putTogetherLESql(sql, "(t1.charge_receive_amount - t1.charge_discount_amount)", "chargeReceiveAmountMax", String.valueOf(chargeReceiveAmountMax));
        sql = putTogetherEqualSql(sql, "t1.charge_discount_info", "chargeDiscountInfo", chargeDiscountInfo);
        sql = putTogetherEqualSql(sql, "t1.charge_receive_post", "chargeReceivePost", chargeReceivePost);
        sql = putTogetherLikeSql(sql, "t1.memo", "memo", memo);
        sql = putTogetherEqualSql(sql, "t1.charge_receive_operator_id", "chargeReceiveOperatorId", chargeReceiveOperatorId);
        sql = putTogetherEqualSql(sql, "t1.charge_receive_id", "chargeReceiveId", chargeReceiveId);
        //sql = sql + "order by t1.add_time desc";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().
                createSQLQuery(sql);
        setParameterToQueryByLike(sqlQuery, "chargeReceiveCarNo", chargeReceiveCarNo);
        setParameterToQueryByLike(sqlQuery, "depName", depName);
        setParameterToQueryByLike(sqlQuery, "carparkName", carparkName);
        setParameterToQuery(sqlQuery, "beginTime", beginTime);
        setParameterToQuery(sqlQuery, "endTime", endTime);
        setParameterToQuery(sqlQuery, "depId", depId);
        setParameterToQuery(sqlQuery, "chargeReceiveCarpark", chargeReceiveCarpark);
        setParameterToQuery(sqlQuery, "chargeReceiveSubType", chargeReceiveSubType);
        setParameterToQuery(sqlQuery, "chargeReceiveAmountMin", String.valueOf(chargeReceiveAmountMin));
        setParameterToQuery(sqlQuery, "chargeReceiveAmountMax", String.valueOf(chargeReceiveAmountMax));
        setParameterToQuery(sqlQuery, "chargeDiscountInfo", chargeDiscountInfo);
        setParameterToQuery(sqlQuery, "chargeReceivePost", chargeReceivePost);
        setParameterToQueryByLike(sqlQuery, "memo", memo);
        setParameterToQuery(sqlQuery, "chargeReceiveOperatorId", chargeReceiveOperatorId);
        setParameterToQuery(sqlQuery, "chargeReceiveId", chargeReceiveId);
        List<Map<String, Object>> ret =  sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return Integer.parseInt(ret.get(0).get("cnt").toString());
    }

    void setParameterToQuery(SQLQuery sqlQuery, String paramName, String paramValue){
        if(!CommonUtils.isEmpty(sqlQuery)){
            if (!("").equals(paramValue) && !CommonUtils.isEmpty(paramValue) && !("null").equals(paramValue)) {
                sqlQuery.setParameter(paramName, paramValue);
            }
        }
    }
    void setParameterToQueryByLike(SQLQuery sqlQuery, String paramName, String paramValue){
        if(!CommonUtils.isEmpty(sqlQuery)){
            if (!("").equals(paramValue) && !CommonUtils.isEmpty(paramValue) && !("null").equals(paramValue)) {
                sqlQuery.setString(paramName, "%" + paramValue + "%");
            }
        }
    }

    String putTogetherEqualSql(String sql, String columnName, String columnAliasName, String columnAliasValue){
        if (!("").equals(columnAliasValue) && !CommonUtils.isEmpty(columnAliasValue) && !("null").equals(columnAliasValue)) {
            sql = sql + "and " + columnName + " = :" + columnAliasName + " ";
            return sql;
        }else return sql;
    }
    String putTogetherLESql(String sql, String columnName, String columnAliasName, String columnAliasValue){
        if (!("").equals(columnAliasValue) && !CommonUtils.isEmpty(columnAliasValue) && !("null").equals(columnAliasValue)) {
            sql = sql + "and " + columnName + " <= :" + columnAliasName + " ";
            return sql;
        }else return sql;
    }
    String putTogetherRESql(String sql, String columnName, String columnAliasName, String columnAliasValue){
        if (!("").equals(columnAliasValue) && !CommonUtils.isEmpty(columnAliasValue) && !("null").equals(columnAliasValue)) {
            sql = sql + "and " + columnName + " >= :" + columnAliasName + " ";
            return sql;
        }else return sql;
    }
    String putTogetherLikeSql(String sql, String columnName, String columnAliasName, String columnAliasValue){
        if (!("").equals(columnAliasValue) && !CommonUtils.isEmpty(columnAliasValue) && !("null").equals(columnAliasValue)) {
            sql = sql + "and " + columnName + " like :" + columnAliasName + " ";
            return sql;
        }else return sql;
    }
    String putTogetherInSql(String sql, String columnName, String columnAliasName, String columnAliasValue){
        if (!("").equals(columnAliasValue) && !CommonUtils.isEmpty(columnAliasValue) && !("null").equals(columnAliasValue)) {
            sql = sql + "and " + columnName + " IN (:" + columnAliasName + ") ";
            return sql;
        }else return sql;
    }


    /*public List<?> getSQLList(final String sql,final Integer start,final Integer length){
        return hibernateTemplate.execute(new HibernateCallback<List>() {
            public List doInHibernate(Session session) throws HibernateException {
                Query query = session.createSQLQuery(sql);
                if(start != null){
                    query.setFirstResult(start);
                }
                if(length != null){
                    query.setMaxResults(length);
                }
                return query.list();
            }
        });
    }*/

    public List<Map<String, Object>> listToListMapEx(String keyName, List lists) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        try {
            for (Object list : lists) {
                Map<String, Object> map = new HashedMap();
                PropertyDescriptor pd = new PropertyDescriptor(keyName, list.getClass());
                Method getMethod = pd.getReadMethod();// 获得get方法
                Object o = getMethod.invoke(list);// 执行get方法返回一个Object
                map.put(o.toString(), list);
                listMap.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listMap;
    }

    public <T> List<Map<String, T>> listToListMap(String keyName, List<T> lists) {
        List<Map<String, T>> listMap = new ArrayList<>();
        try {
            for (T list : lists) {
                Map<String, T> map = new HashedMap();
                PropertyDescriptor pd = new PropertyDescriptor(keyName, list.getClass());
                Method getMethod = pd.getReadMethod();// 获得get方法
                Object o = getMethod.invoke(list);// 执行get方法返回一个Object
                map.put(o.toString(), list);
                listMap.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listMap;
    }

}