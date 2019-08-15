package com.starnetsecurity.parkClientServer.service.impl;

import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.parkClientServer.service.UnusualRecordsService;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Service
public class UnusualRecordServiceImpl implements UnusualRecordsService {

    @Autowired
    HibernateBaseDao baseDao;



    @Override
    public List<Object[]> getUnusualRecords(String parkId) {
        String getUnusualRecords_sql ="SELECT\n" +
                "\tDATE_FORMAT(out_time, '%Y-%m-%d') out_time,\n" +
                "\tcount(*)\n" +
                "FROM\n" +
                "\torder_inout_record\n" +
                "WHERE\n" +
                "\tifnull(in_record_id,'')=''\n" +
                "\t AND carpark_id = '" + parkId + "' AND out_time >= DATE_SUB(\n" +
                "\tDATE_FORMAT(now(), '%Y-%m-%d'),\n" +
                "\tINTERVAL 6 DAY\n" +
                ")\n" +
                "GROUP BY\n" +
                "\tDATE_FORMAT(out_time, '%Y-%m-%d')";
        return (List<Object[]>) baseDao.getSQLList(getUnusualRecords_sql);
    }

    @Override
    public Integer getUnusualRecordsToday(String parkId) {
        String sql = "select count(1) from order_inout_record where carpark_id = :parkId and ifnull(in_record_id,'')='' and out_time BETWEEN :startTime and :endTime";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter("parkId",parkId);
        sqlQuery.setParameter("startTime", CommonUtils.getTodayStartTimeStamp());
        sqlQuery.setParameter("endTime",CommonUtils.getTodayEndTimeStamp());
        BigInteger bigInteger = (BigInteger)sqlQuery.uniqueResult();
        return bigInteger.intValue();
    }
}
