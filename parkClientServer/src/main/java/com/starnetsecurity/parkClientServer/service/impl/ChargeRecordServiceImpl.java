package com.starnetsecurity.parkClientServer.service.impl;

import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.parkClientServer.entity.AdminUser;
import com.starnetsecurity.parkClientServer.entity.CarparkInfo;
import com.starnetsecurity.parkClientServer.entity.MemberKind;
import com.starnetsecurity.parkClientServer.entityEnum.payTypeEnum;
import com.starnetsecurity.parkClientServer.service.ChargeRecordsService;
import org.apache.commons.collections.map.HashedMap;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ChargeRecordServiceImpl implements ChargeRecordsService {

    @Autowired
    HibernateBaseDao baseDao;
//    @Autowired
//    MongoBaseDao mongoBaseDao;


    @Override
    public List<Object[]> getChargeRecords(String parkId) {
        String getChargeRecords_sql ="SELECT\n" +
                "\tDATE_FORMAT(a.add_time, '%Y-%m-%d'),\n" +
                "\tsum(a.real_fee) as charge_receive_amount\n" +
                "FROM\n" +
                "\torder_transaction a left join order_inout_record b on a.order_id = b.charge_info_id\n" +
                "WHERE\n" +
                "\tb.carpark_id ='" + parkId + "' and a.pay_status = 'HAS_PAID'\n" +
                "AND a.add_time >= DATE_SUB(\n" +
                "\tDATE_FORMAT(now(), '%Y-%m-%d'),\n" +
                "\tINTERVAL 6 DAY\n" +
                ")\n" +
                "GROUP BY\n" +
                "\tDATE_FORMAT(add_time, '%Y-%m-%d');\n";
        /*String getChargeRecords_sql ="SELECT\n" +
                "\tDATE_FORMAT(a.add_time, '%Y-%m-%d'),\n" +
                "\tsum(a.real_fee) as charge_receive_amount\n" +
                "FROM\n" +
                "\torder_transaction a \n" +
                "WHERE\n" +
                "a.add_time >= DATE_SUB(\n" +
                "\tDATE_FORMAT(now(), '%Y-%m-%d'),\n" +
                "\tINTERVAL 6 DAY\n" +
                ")\n" +
                "GROUP BY\n" +
                "\tDATE_FORMAT(a.add_time, '%Y-%m-%d');\n";*/
        return (List<Object[]>) baseDao.getSQLList(getChargeRecords_sql);
    }

    @Override
    public Double getChargeRecordsToday(String parkId) {
        Timestamp start = CommonUtils.getTodayStartTimeStamp();
        Timestamp end = CommonUtils.getTodayEndTimeStamp();
        String sql ="SELECT\n" +
                "\tsum(a.real_fee) as total\n" +
                "FROM\n" +
                "\torder_transaction a\n" +
                "LEFT JOIN order_inout_record b ON a.order_id = b.charge_info_id\n" +
                "WHERE\n" +
                "\tpay_status = 'HAS_PAID'\n" +
                "AND b.carpark_id = :parkId\n" +
                "AND a.pay_time BETWEEN \n" +
                "\t:startTime\n" +
                "\tAND :endTime";
        /*String sql ="SELECT\n" +
                "\tsum(a.real_fee) as total\n" +
                "FROM\n" +
                "\torder_transaction a\n" +
                "WHERE\n" +
                "\ta.pay_status = 'HAS_PAID'\n" +
                "AND a.pay_time BETWEEN \n" +
                "\t:startTime\n" +
                "\tAND :endTime";*/
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter("parkId",parkId);
        sqlQuery.setParameter("startTime",start);
        sqlQuery.setParameter("endTime",end);
        BigDecimal bigDecimal = (BigDecimal) sqlQuery.uniqueResult();
        if(CommonUtils.isEmpty(bigDecimal)){
            return 0D;
        }
        return bigDecimal.doubleValue();
    }

    @Override
    public List<Map> getChargeRecordsTodayDetail(String parkId) {

        /*String getChargeRecordsToday_sql ="SELECT\n" +
                "\tsum(a.real_fee) as fee_value,\n" +
                "\ta.pay_type as pay_type\n" +
                "FROM\n" +
                "\torder_transaction a\n" +
                "LEFT JOIN order_inout_record b ON b.charge_info_id = a.order_id\n" +
                "WHERE\n" +
                "\tb.carpark_id = :parkId\n" +
                "AND a.pay_status = 'HAS_PAID'\n" +
                "AND a.pay_time BETWEEN :startTime\n" +
                "AND :endTime\n" +
                "GROUP BY\n" +
                "\ta.pay_type";*/
        String getChargeRecordsToday_sql ="SELECT\n" +
                "\tsum(a.real_fee) as fee_value,\n" +
                "\ta.pay_type as pay_type\n" +
                "FROM\n" +
                "\torder_transaction a\n" +
                "WHERE\n" +
                " a.pay_status = 'HAS_PAID'\n" +
                "AND a.pay_time BETWEEN :startTime\n" +
                "AND :endTime\n" +
                "GROUP BY\n" +
                "\ta.pay_type";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(getChargeRecordsToday_sql);
        //sqlQuery.setParameter("parkId",parkId);
        sqlQuery.setParameter("startTime",CommonUtils.getTodayStartTimeStamp());
        sqlQuery.setParameter("endTime",CommonUtils.getTodayEndTimeStamp());
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map> list = sqlQuery.list();
        for(Map map : list){
            map.put("pay_type", payTypeEnum.valueOf((String)map.get("pay_type")).getDesc());
        }
        return list;
    }

    @Override
    public List<Map> getCarparkChargeToday(Integer pageNum, Integer pageSize, String parkId) {
        String getCarparkChargeToday_sql =" SELECT charge_receive_car_no,pm.post_computer_name,c.charge_receive_sub_type,charge_receive_amount,c.charge_receive_time,c.memo from charge_receive_record c\n" +
                " LEFT JOIN post_computer_manage pm  on  pm.post_computer_id=c.charge_receive_post \n" +
                " where c.charge_receive_time >= DATE_SUB(\n" +
                " DATE_FORMAT(now(), '%Y-%m-%d'),\n" +
                " INTERVAL 0 DAY\n" +
                ")\n" +
                "  and c.charge_receive_operation='charge'\n" +
                "  and c.charge_receive_carpark = '" +parkId+"'"+
                "  ORDER BY c.charge_receive_time DESC ";
        List<Map>  chargerecords = (List<Map>) baseDao.getSQLList(getCarparkChargeToday_sql,pageNum,pageSize);
        return chargerecords;
    }

    @Override
    public Long getCarparkChargeTodayCount(Integer pageNum, Integer pageSize, String parkId) {
        String getCarparkChargeTodayCount_sql ="SELECT\n" +
                " count(1)" +
                " FROM" +
                "  charge_receive_record c " +
                " LEFT JOIN post_computer_manage pm ON pm.post_computer_id = c.charge_receive_post" +
                " WHERE " +
                " c.charge_receive_time >= DATE_SUB( " +
                " DATE_FORMAT(now(), '%Y-%m-%d')," +
                " INTERVAL 0 DAY " +
                " ) " +
                " AND c.charge_receive_carpark = '" +parkId+"'"+
                " AND c.charge_receive_operation = 'charge'" +
                " ORDER BY " +
                " c.charge_receive_time DESC";
        SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().
                createSQLQuery(getCarparkChargeTodayCount_sql);
        return ((BigInteger)query.uniqueResult()).longValue();
    }

    @Override
    public List<Map> getOnlineCharge(Integer pageNum, Integer pageSize, String parkId,String start,String end,String carName) {
        String getCarparkChargeToday_sql ="SELECT\n" +
                "\tb.car_no,\n" +
                "\tb.charge_post_name,\n" +
                "\ta.real_fee,\n" +
                "\tb.car_type,\n" +
                "\ta.pay_type_name,\n" +
                "\ta.pay_time\n" +
                "FROM\n" +
                "\torder_transaction a\n" +
                "LEFT JOIN order_inout_record b ON a.order_id = b.charge_info_id\n" +
                "WHERE\n" +
                "\ta.pay_status = 'HAS_PAID'\n" +
                "AND a.pay_type != 'CASH_PAY'\n" +
                "AND b.charge_info_id IS NOT NULL\n" +
                "AND b.carpark_id = '" + parkId + "' ";
        if (!CommonUtils.isEmpty(carName)) {
            getCarparkChargeToday_sql+="and b.car_no like '"+("%"+carName+"%")+"' \n" ;
        }
        if (!CommonUtils.isEmpty(start)&&!CommonUtils.isEmpty(end)) {
            getCarparkChargeToday_sql+="and a.pay_time >='"+start+"' and a.pay_time <='"+end+"'\n" ;
        }
        getCarparkChargeToday_sql+=
                "ORDER BY\n" +
                        "\ta.pay_time DESC";
        List<Map>  chargerecords = (List<Map>) baseDao.getSQLList(getCarparkChargeToday_sql,pageNum,pageSize);
        return chargerecords;
    }


    @Override
    public Long getOnlineChargeCount(Integer pageNum, Integer pageSize, String parkId,String start,String end,String carName) {
        String getCarparkChargeTodayCount_sql ="SELECT\n" +
                "count(1) " +
                "FROM\n" +
                "\torder_transaction a\n" +
                "LEFT JOIN order_inout_record b ON a.order_id = b.charge_info_id\n" +
                "WHERE\n" +
                "\ta.pay_status = 'HAS_PAID'\n" +
                "AND a.pay_type != 'CASH_PAY'\n" +
                "AND b.charge_info_id IS NOT NULL\n" +
                "AND b.carpark_id = '" + parkId + "' ";
        if (!CommonUtils.isEmpty(carName)) {
            getCarparkChargeTodayCount_sql+="and b.car_no like '"+("%"+carName+"%")+"' \n" ;
        }
        if (!CommonUtils.isEmpty(start)&&!CommonUtils.isEmpty(end)) {
            getCarparkChargeTodayCount_sql+="and a.pay_time >='"+start+"' and a.pay_time <='"+end+"'\n" ;
        }
        SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().
                createSQLQuery(getCarparkChargeTodayCount_sql);
        return ((BigInteger)query.uniqueResult()).longValue();
    }

    @Override
    public Long getChargeHistoryCount(Integer pageNum, Integer pageSize, String parkId, String start, String end,String carName) {
        String getCarparkChargeTodayCount_sql ="SELECT count(1) " +
                " FROM\n" +
                "\torder_inout_record\n" +
                "WHERE\n" +
                "  carpark_id = '" +parkId+"' and out_record_id is not null and out_record_id != '' ";
        if (!CommonUtils.isEmpty(carName)) {
            getCarparkChargeTodayCount_sql+="and car_no like '"+("%"+carName+"%")+"' \n" ;
        }
        if (!CommonUtils.isEmpty(start)&&!CommonUtils.isEmpty(end)) {
            getCarparkChargeTodayCount_sql+="and charge_time >='"+start+"' and charge_time <='"+end+"'\n" ;
        }
        SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().
                createSQLQuery(getCarparkChargeTodayCount_sql);
        return ((BigInteger)query.uniqueResult()).longValue();
    }

    @Override
    public List<Map> getChargeHistory(Integer pageNum, Integer pageSize, String parkId, String start, String end,String carName) {
        String getCarparkChargeTodayCount_sql ="SELECT\n" +
                "\tcar_no as charge_receive_car_no,\n" +
                "\tcharge_post_name as post_computer_name,\n" +
                "\tcar_type as charge_receive_sub_type,\n" +
                "\tcharge_receivable_amount as charge_receive_amount,\n" +
                "\tcharge_time as charge_receive_time,\n" +
                "\tremark as memo " +
                " FROM\n" +
                "\torder_inout_record\n" +
                "WHERE\n" +
                "  carpark_id = '" +parkId+"' and out_record_id is not null and out_record_id != ''";
        if (!CommonUtils.isEmpty(carName)) {
            getCarparkChargeTodayCount_sql+="and car_no like '"+("%"+carName+"%")+"' \n" ;
        }
        if (!CommonUtils.isEmpty(start)&&!CommonUtils.isEmpty(end)) {
            getCarparkChargeTodayCount_sql+="and charge_time >='"+start+"' and charge_time <='"+end+"'\n" ;
        }
        getCarparkChargeTodayCount_sql+=
                "ORDER BY\n" +
                        "\tcharge_time DESC";
        List<Map>  chargerecords = (List<Map>) baseDao.getSQLList(getCarparkChargeTodayCount_sql,pageNum,pageSize);
        return chargerecords;
    }

    @Override
    public List<Map> getChargeRules(String userId, Integer pageNum, Integer pageSize) {

        String hql = "from AdminUser where userName = ?";
        AdminUser adminUser = (AdminUser) baseDao.getUnique(hql,"root");


        List<MemberKind> memberKinds;
        if(!adminUser.getId().equals(userId)){
            hql = "select resId from AdminOrgResource where adminUserId = ? and resType = 'PARK'";
            List<String> carparkIds = (List<String>) baseDao.queryForList(hql, userId);

            hql = "from CarparkInfo where carparkId in (:carparkIds)";
            Query query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
            query.setParameterList("carparkIds",carparkIds);
            List<CarparkInfo> carparkInfos = query.list();

            hql = "from MemberKind where useMark >= 0 and (carparkInfo in (:carparkInfos) or carparkInfo is null) order by addTime desc";
            query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
            query.setParameterList("carparkInfos",carparkInfos);
            query.setFirstResult(pageNum);
            query.setMaxResults(pageSize);
            memberKinds = (List<MemberKind>)query.list();
        }else{
            pageNum = pageNum / pageSize + 1;
            hql = "from MemberKind where useMark >= 0 order by addTime desc";
            memberKinds = (List<MemberKind>)baseDao.pageQuery(hql,pageNum,pageSize);

        }

        List<Map> list = new ArrayList<>();
        for(MemberKind memberKind : memberKinds){
            Map<String,Object> map = new HashedMap();
            map.put("id",memberKind.getId());
            map.put("kindName",memberKind.getKindName());
            try{
                if(!CommonUtils.isEmpty(memberKind.getCarparkInfo())){
                    map.put("parkName",memberKind.getCarparkInfo().getCarparkName());
                }else{
                    map.put("parkName","所有车场");
                }

            }catch (ObjectNotFoundException e){
                map.put("parkName","所有车场");
            }

            map.put("packageKind",memberKind.getPackageKind());
            map.put("isDelete",CommonUtils.isEmpty(memberKind.getIsDelete()) ? 0 : memberKind.getIsDelete());
            map.put("monthPackage",CommonUtils.isEmpty(memberKind.getMonthPackage()) ? "" : memberKind.getMonthPackage());
            map.put("countPackage",CommonUtils.isEmpty(memberKind.getCountPackage()) ? "" : memberKind.getCountPackage());
            map.put("isStatistic",memberKind.getIsStatistic());
            map.put("memo",memberKind.getMemo());
            map.put("packageChildKind",CommonUtils.isEmpty(memberKind.getPackageChildKind()) ? "" : memberKind.getPackageChildKind());
            list.add(map);
        }
        return list;
    }

    @Override
    public Long getChargeRulesCount(String userId) {

        String hql = "from AdminUser where userName = ?";
        AdminUser adminUser = (AdminUser) baseDao.getUnique(hql,"root");

        if(!adminUser.getId().equals(userId)){
            hql = "select resId from AdminOrgResource where adminUserId = ? and resType = 'PARK'";
            List<String> carparkIds = (List<String>) baseDao.queryForList(hql, userId);

            hql = "from CarparkInfo where carparkId in (:carparkIds)";
            Query query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
            query.setParameterList("carparkIds",carparkIds);
            List<CarparkInfo> carparkInfos = query.list();

            hql = "select count(*) from MemberKind where useMark >= 0 and (carparkInfo in (:carparkInfos) or carparkInfo is null) order by addTime desc";
            query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
            query.setParameterList("carparkInfos",carparkInfos);

            return (Long)query.uniqueResult();
        }else{

            hql = "select count(*) from MemberKind where useMark >= 0 order by addTime desc";
            return (Long)baseDao.getUnique(hql);
        }

    }

    @Override
    public List getPreChargeHistoryData(Integer pageNum, Integer pageSize, String parkId, String start, String end, String carName) {
        String getCarparkChargeTodayCount_sql ="SELECT\n" +
                "\torder_precharge_carno as charge_receive_car_no,\n" +
                "\torder_precharge_carpark_name as post_computer_name,\n" +
                "\tcharge_member_kind_name as charge_receive_sub_type,\n" +
                "\torder_precharge_receivable_amount as charge_receive_amount,\n" +
                "\torder_precharge_time as charge_receive_time,\n" +
                "\tmemo " +
                " FROM\n" +
                "\torder_precharge_record\n" +
                "WHERE\n" +
                "  order_precharge_carpark = '" +parkId+"' ";
        if (!CommonUtils.isEmpty(carName)) {
            getCarparkChargeTodayCount_sql+="and order_precharge_carno like '"+("%"+carName+"%")+"' \n" ;
        }
        if (!CommonUtils.isEmpty(start)&&!CommonUtils.isEmpty(end)) {
            getCarparkChargeTodayCount_sql+="and order_precharge_time >='"+start+"' and order_precharge_time <='"+end+"'\n" ;
        }
        getCarparkChargeTodayCount_sql+=
                "ORDER BY\n" +
                        "\torder_precharge_time DESC";
        List<Map>  chargerecords = (List<Map>) baseDao.getSQLList(getCarparkChargeTodayCount_sql,pageNum,pageSize);
        return chargerecords;
    }

    @Override
    public Long getPreChargeHistoryCount(String parkId, String start, String end, String carName) {

        String getCarparkChargeTodayCount_sql ="SELECT\n" +
                "\tcount(1)\n" +
                " FROM\n" +
                "\torder_precharge_record\n" +
                "WHERE\n" +
                "  order_precharge_carpark = '" +parkId+"' ";
        if (!CommonUtils.isEmpty(carName)) {
            getCarparkChargeTodayCount_sql+="and order_precharge_carno like '"+("%"+carName+"%")+"' \n" ;
        }
        if (!CommonUtils.isEmpty(start)&&!CommonUtils.isEmpty(end)) {
            getCarparkChargeTodayCount_sql+="and order_precharge_time >='"+start+"' and order_precharge_time <='"+end+"'\n" ;
        }


        SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().
                createSQLQuery(getCarparkChargeTodayCount_sql);
        return ((BigInteger)query.uniqueResult()).longValue();
    }


//    @Override
//    public  Map getChargeRulesInfo(String carParkId) {
//
//        /*换parksId*/
//        String hql = "from Parks where clientId = ?";
//        Parks parks = (Parks)baseDao.getUnique(hql,carParkId);
//        String parkId=parks.getId();
//        MCondition mc = MCondition.create(MRel.and);
//        String parkLotNo = parkId;
//        mc.append("service_park_id", parkLotNo);
////        mc.append(MCondition.create(MRel.lte).append("inTime", new Date()));
////        mc.append(MCondition.create(MRel.gte).append("inTime", DateUtils.addSecond(new Date(), -3600)));
//        List<DBObject> items=null;
//        try {
//            items = (List<DBObject>) mongoBaseDao.getPageList(mc.toDBObject().toMap(), DBObject.class, 10, 1, MongoTable.park_charge_rule,null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        items.get(0);
//        DBObject chargeRulesList=  items.get(0);
//        Map  List= (Map) chargeRulesList.get("rules");
//        Object inSideList=  List.get("inSide");
//        Object outSideList=  List.get("outSide");
//        Map inSideListNew= (Map) inSideList;
//        Map outSideListNew= (Map) outSideList;
//        Map res=new HashMap();
//        res.put("inSideList",inSideListNew);
//        res.put("outSideList",outSideListNew);
//        return  res;
//
//    }

}
