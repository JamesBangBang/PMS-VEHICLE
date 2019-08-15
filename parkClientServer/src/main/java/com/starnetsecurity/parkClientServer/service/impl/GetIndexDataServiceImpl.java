package com.starnetsecurity.parkClientServer.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.parkClientServer.entity.CarparkInfo;
import com.starnetsecurity.parkClientServer.entity.InoutRecordInfo;
import com.starnetsecurity.parkClientServer.entity.OrderInoutRecord;
import com.starnetsecurity.parkClientServer.service.GetIndexDataService;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JAMESBANG on 2018/8/26.
 */
@Service
public class GetIndexDataServiceImpl implements GetIndexDataService {
    @Autowired
    HibernateBaseDao baseDao;

    @Override
    public Integer getInCount(Integer inParkDay) {
        //获取某一天的开始和结束时间
        Timestamp beginTime = new Timestamp(CommonUtils.getTodayStartTimeStamp().getTime() - inParkDay * 24 * 60 * 60 * 1000);
        Timestamp endTime = new Timestamp(CommonUtils.getTodayEndTimeStamp().getTime() - inParkDay * 24 * 60 * 60 * 1000);
        String sql = "SELECT\n" +
                "\tcount(1)\n" +
                "FROM\n" +
                "\tinout_record_info\n" +
                "WHERE\n" +
                "inout_flag = 0\n" +
                "AND inout_time >= '" + beginTime.toString() + "'" +
                " AND inout_time <= '" + endTime.toString() + "'";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        BigInteger bigInteger = (BigInteger)sqlQuery.uniqueResult();
        return bigInteger.intValue();
    }

    @Override
    public Integer getOutCount(Integer outParkDay) {
        //获取某一天的开始和结束时间
        Timestamp beginTime = new Timestamp(CommonUtils.getTodayStartTimeStamp().getTime() - outParkDay * 24 * 60 * 60 * 1000);
        Timestamp endTime = new Timestamp(CommonUtils.getTodayEndTimeStamp().getTime() - outParkDay * 24 * 60 * 60 * 1000);
        String sql = "SELECT\n" +
                "\tcount(1)\n" +
                "FROM\n" +
                "\tinout_record_info\n" +
                "WHERE\n" +
                "inout_flag = 1\n" +
                "AND inout_time >= '" + beginTime.toString() + "'" +
                " AND inout_time <= '" + endTime.toString() + "'";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        BigInteger bigInteger = (BigInteger)sqlQuery.uniqueResult();
        return bigInteger.intValue();
    }

    @Override
    public Integer getTotalPark() {
        Integer res = 0;
        String hql = "from CarparkInfo where useMark >= 0";
        List<CarparkInfo> carparkInfoList = (List<CarparkInfo>)baseDao.queryForList(hql);
        if (carparkInfoList != null && carparkInfoList.size() > 0){
            for (CarparkInfo carparkInfo : carparkInfoList){
                res = res + carparkInfo.getTotalCarSpace();
            }
        }
        return res;
    }

    @Override
    public Integer getSurplusPark() {
        Integer res = 0;
        String hql = "from CarparkInfo where useMark >= 0";
        List<CarparkInfo> carparkInfoList = (List<CarparkInfo>)baseDao.queryForList(hql);
        if (carparkInfoList != null && carparkInfoList.size() > 0){
            for (CarparkInfo carparkInfo : carparkInfoList){
                res = res + carparkInfo.getAvailableCarSpace();
            }
        }
        return res;
    }

    @Override
    public Integer getWarningLine() {
        Integer res = 0;
        String hql = "from CarparkInfo where useMark >= 0";
        List<CarparkInfo> carparkInfoList = (List<CarparkInfo>)baseDao.queryForList(hql);
        if (carparkInfoList != null && carparkInfoList.size() > 0){
            for (CarparkInfo carparkInfo : carparkInfoList){
                res = res + (CommonUtils.isEmpty(carparkInfo.getCriticalValue()) ? 0 : carparkInfo.getCriticalValue());
            }
        }
        return res;
    }

    @Override
    public JSONObject getInOutCountToDayByTime(Integer beginIndex) {
        JSONObject res = new JSONObject();
        Timestamp beginTime = new Timestamp(CommonUtils.getTodayStartTimeStamp().getTime() + beginIndex * 2 * 60 * 60 * 1000);
        Timestamp endTime = new Timestamp(CommonUtils.getTodayStartTimeStamp().getTime() + (beginIndex * 2 + 2)  * 60 * 60 * 1000);
        String sql = "SELECT\n" +
                "\tcount(1)\n" +
                "FROM\n" +
                "\tinout_record_info\n" +
                "WHERE\n" +
                "inout_flag = 0\n" +
                "AND inout_time >= '" + beginTime.toString() + "'" +
                " AND inout_time < '" + endTime.toString() + "'";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        BigInteger inCount = (BigInteger)sqlQuery.uniqueResult();

        sql = "SELECT\n" +
                "\tcount(1)\n" +
                "FROM\n" +
                "\tinout_record_info\n" +
                "WHERE\n" +
                "inout_flag = 1\n" +
                "AND inout_time >= '" + beginTime.toString() + "'" +
                " AND inout_time < '" + endTime.toString() + "'";
        sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        BigInteger outCount = (BigInteger)sqlQuery.uniqueResult();
        res.put("date",beginIndex * 2 < 10 ? "0" + beginIndex * 2 + ":00" : beginIndex * 2 + ":00");
        res.put("驶入车辆",inCount);
        res.put("驶出车辆",outCount);
        return  res;
    }

    @Override
    public JSONObject getInOutCountYesterdayByTime(Integer beginIndex) {
        JSONObject res = new JSONObject();
        Timestamp yesterday = new Timestamp(CommonUtils.getTodayStartTimeStamp().getTime() - 24*60*60*1000);
        Timestamp beginTime = new Timestamp(yesterday.getTime() + beginIndex * 2 * 60 * 60 * 1000);
        Timestamp endTime = new Timestamp(yesterday.getTime() + (beginIndex * 2 + 2)  * 60 * 60 * 1000);
        String sql = "SELECT\n" +
                "\tcount(1)\n" +
                "FROM\n" +
                "\tinout_record_info\n" +
                "WHERE\n" +
                "inout_flag = 0\n" +
                "AND inout_time >= '" + beginTime.toString() + "'" +
                " AND inout_time < '" + endTime.toString() + "'";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        BigInteger inCount = (BigInteger)sqlQuery.uniqueResult();

        sql = "SELECT\n" +
                "\tcount(1)\n" +
                "FROM\n" +
                "\tinout_record_info\n" +
                "WHERE\n" +
                "inout_flag = 1\n" +
                "AND inout_time >= '" + beginTime.toString() + "'" +
                " AND inout_time < '" + endTime.toString() + "'";
        sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        BigInteger outCount = (BigInteger)sqlQuery.uniqueResult();
        res.put("date",beginIndex * 2 < 10 ? "0" + beginIndex * 2 + ":00" : beginIndex * 2 + ":00");
        res.put("驶入车辆",inCount);
        res.put("驶出车辆",outCount);
        return  res;
    }

    @Override
    public Integer getInCountByCarType(String carType,Integer inParkDay) {
        Timestamp beginTime = new Timestamp(CommonUtils.getTodayStartTimeStamp().getTime() - inParkDay * 24 * 60 * 60 * 1000);
        Timestamp endTime = new Timestamp(CommonUtils.getTodayEndTimeStamp().getTime() - inParkDay * 24 * 60 * 60 * 1000);
        String sql = "SELECT\n" +
                "\tcount(1)\n" +
                "FROM\n" +
                "\tinout_record_info\n" +
                "WHERE\n" +
                "\tcar_type = '" + carType + "'\n" +
                "AND inout_flag = 0\n" +
                "AND inout_time >= '" + beginTime.toString() + "'" +
                " AND inout_time <= '" + endTime.toString() + "'";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        BigInteger bigInteger = (BigInteger)sqlQuery.uniqueResult();
        return bigInteger.intValue();
    }

    @Override
    public JSONObject getTodayInoutInfo() {
        JSONObject res = new JSONObject();
        List<String> dateList = new ArrayList<>();
        List<BigInteger> inCarnoList = new ArrayList<>();
        List<BigInteger> outCarnoList = new ArrayList<>();
        for (int i = 0;i < 12;i++){
            Timestamp beginTime = new Timestamp(CommonUtils.getTodayStartTimeStamp().getTime() + i * 2 * 60 * 60 * 1000);
            Timestamp endTime = new Timestamp(CommonUtils.getTodayStartTimeStamp().getTime() + (i * 2 + 2)  * 60 * 60 * 1000);
            String sql = "SELECT\n" +
                    "\tcount(1)\n" +
                    "FROM\n" +
                    "\tinout_record_info\n" +
                    "WHERE\n" +
                    "inout_flag = 0\n" +
                    "AND inout_time >= '" + beginTime.toString() + "'" +
                    " AND inout_time < '" + endTime.toString() + "'";
            SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
            BigInteger inCount = (BigInteger)sqlQuery.uniqueResult();

            sql = "SELECT\n" +
                    "\tcount(1)\n" +
                    "FROM\n" +
                    "\tinout_record_info\n" +
                    "WHERE\n" +
                    "inout_flag = 1\n" +
                    "AND inout_time >= '" + beginTime.toString() + "'" +
                    " AND inout_time < '" + endTime.toString() + "'";
            sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
            BigInteger outCount = (BigInteger)sqlQuery.uniqueResult();
            dateList.add(i * 2 < 10 ? "0" + i * 2 + ":00" : i * 2 + ":00");
            inCarnoList.add(inCount);
            outCarnoList.add(outCount);
        }
        JSONObject dateJson = new JSONObject();
        JSONObject inCarJson = new JSONObject();
        JSONObject outCarJson = new JSONObject();
        dateJson.put("data",dateList);
        res.put("xAxis",dateJson);
        inCarJson.put("name","驶入车辆");
        inCarJson.put("data",inCarnoList);
        outCarJson.put("name","驶出车辆");
        outCarJson.put("data",outCarnoList);
        List<JSONObject> seriesList = new ArrayList<>();
        seriesList.add(inCarJson);
        seriesList.add(outCarJson);
        res.put("series",seriesList);
        return res;
    }

    @Override
    public JSONArray getRealTableInfo(Integer showCount) {
        JSONArray res = new JSONArray();
        try {
            String hql = "from InoutRecordInfo where inoutTime <= ? AND inoutStatus <= 1 order by inoutTime desc";
            Integer inOutCnt = baseDao.pageQuery(hql,1,showCount,CommonUtils.getTimestamp()).size();
            List<InoutRecordInfo> listRecord = (List<InoutRecordInfo>)baseDao.pageQuery(hql,1,showCount,CommonUtils.getTimestamp());
            for (Integer i = 0;i<= (inOutCnt-1);i++){
                InoutRecordInfo inoutRecordInfo = listRecord.get(i);
                JSONObject inOutData = new JSONObject();
                inOutData.put("vehicleCardName",inoutRecordInfo.getCarNo());
                inOutData.put("date",CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",inoutRecordInfo.getInoutTime()));
                if (inoutRecordInfo.getInoutFlag()==0) {
                    inOutData.put("inOut", "进");
                }else {
                    inOutData.put("inOut", "出");
                }

                inOutData.put("abnormal","无");
                res.add(inOutData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public void deleteUselessData() {
        Long beginL = CommonUtils.getTodayStartTimeStamp().getTime() - 5 * 24 * 60 * 60 * 1000L;
        Timestamp beginTime = new Timestamp(beginL);
        String hql = "from OrderInoutRecord where outRecordId is null and inTime <= ? and carpark_name = ?";
        List<OrderInoutRecord> list = (List<OrderInoutRecord>)baseDao.queryForList(hql,beginTime,"金山园区");
        if (list != null && list.size() > 0){
            for (OrderInoutRecord orderInoutRecord : list){
                baseDao.deleteById(InoutRecordInfo.class,orderInoutRecord.getInRecordId());
                baseDao.deleteById(OrderInoutRecord.class,orderInoutRecord.getChargeInfoId());
            }
        }
    }
}
