package com.starnetsecurity.parkClientServer.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.mongo.MCondition;
import com.starnetsecurity.common.mongo.MRel;
import com.starnetsecurity.common.mongo.MongoTable;
import com.starnetsecurity.common.mongoDao.MongoBaseDao;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.util.DateUtils;
import com.starnetsecurity.parkClientServer.service.InoutRecordsService;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class InoutRecordServiceImpl implements InoutRecordsService {

    @Autowired
    HibernateBaseDao baseDao;
//    @Autowired
//    MongoBaseDao mongoBaseDao;


    @Override
    public List<Object[]> getInOutRecords(String parkId) {
        String getInOutRecords_sql ="SELECT\n" +
                "\tDATE_FORMAT(add_time, '%Y-%m-%d'),\n" +
                "\tcount(1)\n" +
                "FROM\n" +
                "\tinout_record_info\n" +
                "WHERE\n" +
                "\tcarpark_id = '" + parkId + "'\n" +
                "AND inout_flag = 1\n" +
                "AND add_time >= DATE_SUB(\n" +
                "\tDATE_FORMAT(now(), '%Y-%m-%d'),\n" +
                "\tINTERVAL 6 DAY\n" +
                ")\n" +
                "GROUP BY\n" +
                "\tDATE_FORMAT(add_time, '%Y-%m-%d')";
        return (List<Object[]>) baseDao.getSQLList(getInOutRecords_sql);
    }

    @Override
    public Integer getInOutRecordsToday(String parkId) {
        String getInOutRecords_sql ="SELECT\n" +
                "\tcount(1)\n" +
                "FROM\n" +
                "\tinout_record_info\n" +
                "WHERE\n" +
                "\tcarpark_id = '" + parkId + "'\n" +
                "AND inout_flag = 1\n" +
                "AND  inout_status < 2 " +
                "AND add_time >= '" + CommonUtils.getTodayStartTimeStamp() + "' " +
                "AND add_time <= '" + CommonUtils.getTodayEndTimeStamp() + "' ";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(getInOutRecords_sql);
        BigInteger bigInteger = (BigInteger)sqlQuery.uniqueResult();
        return bigInteger.intValue();
    }

    @Override
    public List<Map> getInOutRecordsTodayDetail(String parkId) {
        String getInOutRecords_sql ="SELECT\n" +
                "\tcount(1) AS car_count,\n" +
                "\tcar_type\n" +
                "FROM\n" +
                "\tinout_record_info\n" +
                "WHERE\n" +
                "\tcarpark_id = :parkId\n" +
                "AND add_time BETWEEN :startTime\n" +
                "AND :endTime\n" +
                "GROUP BY\n" +
                "\tcar_type";

        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(getInOutRecords_sql);
        sqlQuery.setParameter("parkId",parkId);
        sqlQuery.setParameter("startTime",CommonUtils.getTodayStartTimeStamp());
        sqlQuery.setParameter("endTime",CommonUtils.getTodayEndTimeStamp());
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map> list = sqlQuery.list();
        return list;
    }

//    @Override
//    public List<DBObject> getInOutRecordsTodayRun(String parkId) {
//        MCondition mc = MCondition.create(MRel.and);
//        String parkLotNo = parkId;
//        mc.append("in_out_parking_lot_no", parkLotNo);
//        mc.append(MCondition.create(MRel.lte).append("inTime", new Date()));
//        mc.append(MCondition.create(MRel.gte).append("inTime",DateUtils.addSecond(new Date(), -3600)));
//        List<DBObject> items=null;
//        try {
//            items = (List<DBObject>) mongoBaseDao.getPageList(mc.toDBObject().toMap(), DBObject.class, 10, 1, MongoTable.in_out_num_record, new BasicDBObject("inTime", Sort.Direction.DESC.toString()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return items;
//    }


    @Override
    public List<Map> getCarparkOutToday(Integer pageNum, Integer pageSize,String parkId) {
        String getCarparkInOutToday_sql ="SELECT\n" +
                "\t*\n" +
                "FROM\n" +
                "\t(\n" +
                "\t\tSELECT\n" +
                "\t\t\tin_out_car_no,\n" +
                "\t\t\tin_out_time,\n" +
                "\t\t\t0 AS statues,\n" +
                "\t\t\tin_out_car_road_id AS road,\n" +
                "\t\t\tcar_road_name AS road_name,\n" +
                "\t\t\toperation_source AS operation_id,\n" +
                "\t\t\toperator_name AS operation_name\n" +
                "\t\tFROM\n" +
                "\t\t\tin_out_record\n" +
                "\t\tWHERE\n" +
                "\t\t\t\tin_out_parking_lot_no = '" +parkId+"'"+
                "and in_out_time >= DATE_SUB(\n" +
                "\tDATE_FORMAT(now(), '%Y-%m-%d'),\n" +
                "\tINTERVAL 0 DAY\n" +
                ")\n" +
                "\t\tUNION ALL\n" +
                "\t\t\tSELECT\n" +
                "\t\t\t\tcar_no,\n" +
                "\t\t\t\tout_time AS in_out_time,\n" +
                "\t\t\t\t1 AS statues,\n" +
                "\t\t\t\tout_car_road_no AS road,\n" +
                "\t\t\t\tout_car_road_name AS road_name,\n" +
                "\t\t\t\toperation_source AS operation_id,\n" +
                "\t\t\t\toperator_name AS operation_name\n" +
                "\t\t\tFROM\n" +
                "\t\t\t\tin_out_fee_release\n" +
                "\t\t\tWHERE\n" +
                "\t\t\t\tparking_lot_no = '" +parkId+"'"+
                "and out_time >= DATE_SUB(\n" +
                "\tDATE_FORMAT(now(), '%Y-%m-%d'),\n" +
                "\tINTERVAL 0 DAY\n" +
                ")\n" +
                "\t) rr \n" +
                "ORDER BY\n" +
                "\tin_out_time DESC";
        List<Map>  inoutrecords = (List<Map>) baseDao.getSQLList(getCarparkInOutToday_sql,pageNum,pageSize);
        return inoutrecords;
    }

    @Override
    public Long getCarparkOutTodayCount(Integer pageNum, Integer pageSize,String parkId) {
        String getCarparkInOutToday_sql ="SELECT\n" +
                "\tcount(1)\n" +
                "FROM\n" +
                "\t(\n" +
                "\t\tSELECT\n" +
                "\t\t\tin_out_car_no,\n" +
                "\t\t\tin_out_time,\n" +
                "\t\t\t0 AS statues,\n" +
                "\t\t\tin_out_car_road_id AS road,\n" +
                "\t\t\tcar_road_name AS road_name,\n" +
                "\t\t\toperation_source AS operation_id,\n" +
                "\t\t\toperator_name AS operation_name\n" +
                "\t\tFROM\n" +
                "\t\t\tin_out_record\n" +
                "\t\tWHERE\n" +
                "\t\t\t\tin_out_parking_lot_no = '" +parkId+"'"+
                "and in_out_time >= DATE_SUB(\n" +
                "\tDATE_FORMAT(now(), '%Y-%m-%d'),\n" +
                "\tINTERVAL 0 DAY\n" +
                ")\n" +
                "\t\tUNION ALL\n" +
                "\t\t\tSELECT\n" +
                "\t\t\t\tcar_no,\n" +
                "\t\t\t\tout_time AS in_out_time,\n" +
                "\t\t\t\t1 AS statues,\n" +
                "\t\t\t\tout_car_road_no AS road,\n" +
                "\t\t\t\tout_car_road_name AS road_name,\n" +
                "\t\t\t\toperation_source AS operation_id,\n" +
                "\t\t\t\toperator_name AS operation_name\n" +
                "\t\t\tFROM\n" +
                "\t\t\t\tin_out_fee_release\n" +
                "\t\t\tWHERE\n" +
                "\t\t\t\tparking_lot_no = '" +parkId+"'"+
                "and out_time >= DATE_SUB(\n" +
                "\tDATE_FORMAT(now(), '%Y-%m-%d'),\n" +
                "\tINTERVAL 0 DAY\n" +
                ")\n" +
                "\t) rr \n" +
                "ORDER BY\n" +
                "\tin_out_time DESC";
        SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().
                createSQLQuery(getCarparkInOutToday_sql);
        return ((BigInteger)query.uniqueResult()).longValue();
    }

    @Override
    public List<Map> getCarparkOutTodaydetail(Integer pageNum, Integer pageSize, String parkId,String start,String end,String carName) {
        String getCarparkInOutToday_sql ="select car_no,inout_time,inout_flag,car_road_name,operator_name from inout_record_info where carpark_id = '" +parkId+"' ";
        if (!CommonUtils.isEmpty(carName)) {
            getCarparkInOutToday_sql+="and car_no like '"+("%"+carName+"%")+"' \n" ;
        }
        if (!CommonUtils.isEmpty(start)&&!CommonUtils.isEmpty(end)) {
            getCarparkInOutToday_sql+="and inout_time >='"+start+"' and inout_time <='"+end+"'\n" ;
        }

        getCarparkInOutToday_sql += "ORDER BY\n" +
                "\tinout_time DESC";
        List<Map>  inoutrecords = (List<Map>) baseDao.getSQLList(getCarparkInOutToday_sql,pageNum,pageSize);
        return inoutrecords;
    }
    @Override
    public Long getCarparkOutTodaydetailCount(Integer pageNum, Integer pageSize, String parkId,String start,String end,String carName) {
        String getCarparkInOutToday_sql ="select count(1) from inout_record_info where carpark_id = '" +parkId+"' ";
        if (!CommonUtils.isEmpty(carName)) {
            getCarparkInOutToday_sql+="and car_no like '"+("%"+carName+"%")+"' \n" ;
        }
        if (!CommonUtils.isEmpty(start)&&!CommonUtils.isEmpty(end)) {
            getCarparkInOutToday_sql+="and inout_time >='"+start+"' and inout_time <='"+end+"'\n" ;
        }

        SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().
                createSQLQuery(getCarparkInOutToday_sql);
        return ((BigInteger)query.uniqueResult()).longValue();
    }


}
