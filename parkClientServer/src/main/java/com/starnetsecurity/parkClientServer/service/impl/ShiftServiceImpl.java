package com.starnetsecurity.parkClientServer.service.impl;

import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.parkClientServer.service.ShiftService;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Service
public class ShiftServiceImpl implements ShiftService {

    @Autowired
    HibernateBaseDao baseDao;


    @Override
    public List<Map> getShiftMapInfoList(Integer pageNum, Integer pageSize,String parkId,String boxName) {
        String getShiftMapInfoList_sql ="SELECT\n" +
                "\tbooth_name,\n" +
                "\tshift_giver,\n" +
                "\tshift_giver_work_time,\n" +
                "\tshift_taker,\n" +
                "\tshift_change_time,\n" +
                "   receivable_amount,\n" +
                "s.derate_amount,\n" +
                "real_amount\n" +
                "FROM\n" +
                "\tshift_change_stat_info s \n";
        if (!CommonUtils.isEmpty(boxName)) {
            getShiftMapInfoList_sql+="WHERE booth_name like '"+("%"+boxName+"%")+"'  \n" ;
        }
        getShiftMapInfoList_sql +="  ORDER BY s.shift_change_time desc ";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().
                createSQLQuery(getShiftMapInfoList_sql);
          List<Map>  shifts = (List<Map>) baseDao.getSQLList(getShiftMapInfoList_sql,pageNum,pageSize);
        return shifts;
    }


    @Override
    public Long getShiftMapInfoCount(Integer pageNum, Integer pageSize, String parkId,String start,String end,String boxName) {
        String getShiftMapInfoList_sql ="SELECT\n" +
               "count(1)"+
                "FROM\n" +
                "\tshift_change_stat_info s \n";
        if (!CommonUtils.isEmpty(boxName)) {
            getShiftMapInfoList_sql+="WHERE booth_name like '"+("%"+boxName+"%")+"' \n" ;
        }
        SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().
                createSQLQuery(getShiftMapInfoList_sql);
        return ((BigInteger)query.uniqueResult()).longValue();
    }

}
