package com.starnetsecurity.parkClientServer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;

import com.starnetsecurity.parkClientServer.entity.CarparkInfo;
import com.starnetsecurity.parkClientServer.entity.CarparkRegister;
import com.starnetsecurity.parkClientServer.entity.DepartmentInfo;
import com.starnetsecurity.parkClientServer.service.ParkService;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by 宏炜 on 2017-08-01.
 */
@Service
public class ParkServiceImpl implements ParkService {

    @Autowired
    HibernateBaseDao hibernateBaseDao;

    @Override
    public List getRegisteredParks() {
        return hibernateBaseDao.getAll(CarparkRegister.class);
    }

    @Override
    public void addChargeReceiveRecord(JSONObject params) throws BizException {
        Map orderPark = (Map)params.get("orderPark");
        String sql = "insert into charge_receive_record( " +
                "charge_receive_id, " +
                "charge_receive_car_no, " +
                "charge_receive_type, " +
                "charge_receive_sub_type, " +
                "charge_receive_carpark, " +
                "charge_receive_post, " +
                "charge_receive_post_name, " +
                "charge_receive_amount, " +
                "charge_discount_amount, " +
                "charge_receive_operator_id, " +
                "charge_receive_time, " +
                "charge_receive_operation, " +
                "operation_source, " +
                "charge_release_id, " +
                "add_time, " +
                "charge_discount_info, " +
                "memo ) values ( " +
                ":uuid, " +
                ":carNo, " +
                ":payType, " +
                "'微信缴费', " +
                ":parkId, " +
                "'WECHAT_PAY', " +
                "'微信支付', " +
                ":fee, " +
                ":discount, " +
                "1, " +
                ":receiveTime, " +
                ":receiveOperation, " +
                "''," +
                " '', " +
                ":addTime, " +
                ":discountInfo," +
                " '' )";
        String hql = "from CarparkRegisterEntity where serviceId = ?";
        CarparkRegister carparkRegisterEntity = (CarparkRegister)hibernateBaseDao.getUnique(hql,orderPark.get("parkId"));

        SQLQuery sqlQuery = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter("uuid", UUID.randomUUID().toString().replaceAll("-",""));
        sqlQuery.setParameter("carNo", orderPark.get("carNo"));
        sqlQuery.setParameter("payType", "WECHAT_PAY");
        sqlQuery.setParameter("parkId", carparkRegisterEntity.getId());
        sqlQuery.setParameter("fee",orderPark.get("fee"));
        sqlQuery.setParameter("discount", 0);
        sqlQuery.setParameter("receiveTime", new Timestamp((Long)orderPark.get("payTime")));
        sqlQuery.setParameter("receiveOperation", "charge");
        sqlQuery.setParameter("addTime", CommonUtils.getTimestamp());
        sqlQuery.setParameter("discountInfo","无优惠");
        sqlQuery.executeUpdate();

    }

    @Override
    public void addChargeReceiveRecord(String payTypeName,String payServerName,String payTypeTag,String opreator_id,String carNo,String total,String parkId) throws BizException {
        String sql = "insert into charge_receive_record( " +
                "charge_receive_id, " +
                "charge_receive_car_no, " +
                "charge_receive_type, " +
                "charge_receive_sub_type, " +
                "charge_receive_carpark, " +
                "charge_receive_post, " +
                "charge_receive_post_name, " +
                "charge_receive_amount, " +
                "charge_discount_amount, " +
                "charge_receive_operator_id, " +
                "charge_receive_time, " +
                "charge_receive_operation, " +
                "operation_source, " +
                "charge_release_id, " +
                "add_time, " +
                "charge_discount_info, " +
                "memo ) values ( " +
                ":uuid, " +
                ":carNo, " +
                ":payType, " +
                "'"+payTypeName+"', " +
                ":parkId, " +
                "'BOLINK_PAY', " +
                "'"+payServerName+"', " +
                ":fee, " +
                ":discount, " +
                opreator_id +", " +
                ":receiveTime, " +
                ":receiveOperation, " +
                "'" + payTypeName + "'," +
                " '', " +
                ":addTime, " +
                ":discountInfo," +
                " '' )";
        SQLQuery sqlQuery = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter("uuid", UUID.randomUUID().toString().replaceAll("-",""));
        sqlQuery.setParameter("carNo", carNo);
        sqlQuery.setParameter("payType", payTypeTag);
        sqlQuery.setParameter("parkId", parkId);
        sqlQuery.setParameter("fee",total);
        sqlQuery.setParameter("discount", 0);
        sqlQuery.setParameter("receiveTime", CommonUtils.getTimestamp());
        sqlQuery.setParameter("receiveOperation", "charge");
        sqlQuery.setParameter("addTime", CommonUtils.getTimestamp());
        sqlQuery.setParameter("discountInfo","无优惠");
        sqlQuery.executeUpdate();

    }

    @Override
    /**
     * id : member_id，主键
     * parkid : 不是serviceid
     * fee : 缴费金额
     * paytime : 缴费单时间
     * kindid : 套餐ID（0-包月，1-包次，2-临时）
     * effectiveEndTime : 包月有效结束时间
     * surplusNum : 包次剩余次数
     * surplusAmount : 剩余金额
     */
    public void addRechargeRecord(String id,String carno,String parkId,String fee,String payTime,
                                  String kindId,String effectiveEndTime,
                                  String surplusNum,String surplusAmount,String kindName) {
        String strSql = "";
        String sql = "";
        String memo = "";
        strSql = "insert into charge_receive_record( " +
                 "charge_receive_id, " +
                 "charge_receive_car_no, " +
                 "charge_receive_type, " +
                 "charge_receive_sub_type, " +
                 "charge_receive_carpark, " +
                 "charge_receive_post, " +
                 "charge_receive_post_name, " +
                 "charge_receive_amount, " +
                 "charge_discount_amount, " +
                 "charge_receive_operator_id, " +
                 "charge_receive_time, " +
                 "charge_receive_operation, " +
                 "operation_source, " +
                 "charge_release_id, " +
                 "add_time, " +
                 "charge_discount_info, " +
                 "memo ) values ( " +
                 ":uuid, " +
                 ":carNo, " +
                 ":payType, " +
                 ":kindName, " +
                 ":parkId, " +
                 "'WECHAT_PAY', " +
                 "'微信支付', " +
                 ":fee, " +
                 ":discount, " +
                 "1, " +
                 ":receiveTime, " +
                 ":receiveOperation, " +
                 "''," +
                 " '', " +
                 ":addTime, " +
                 ":discountInfo," +
                 ":memoInfo )";

        if (kindId.equals("0")){
            memo = "有效截止时间：" + effectiveEndTime;
        }
        else if (kindId.equals("1")){
            memo = "剩余次数：" + surplusNum;
        }
        else{
            memo = "剩余金额：" + surplusAmount + "元";
        }

        SQLQuery sqlQuery = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(strSql);
        sqlQuery.setParameter("uuid", UUID.randomUUID().toString().replaceAll("-",""));
        sqlQuery.setParameter("carNo", carno);
        sqlQuery.setParameter("payType", "wechat_recharge");
        sqlQuery.setParameter("kindName", kindName);
        sqlQuery.setParameter("parkId", parkId);
        sqlQuery.setParameter("fee",fee);
        sqlQuery.setParameter("discount", 0);
        sqlQuery.setParameter("receiveTime", Timestamp.valueOf(payTime));
        sqlQuery.setParameter("receiveOperation", "charge");
        sqlQuery.setParameter("addTime", CommonUtils.getTimestamp());
        sqlQuery.setParameter("discountInfo","无优惠");
        sqlQuery.setParameter("memoInfo",memo);
        sqlQuery.executeUpdate();

        if (kindId.equals("0")){
            strSql = "update member_wallet set "
                    + "effective_end_time = :effectiveEndTime,"
                    + "synchronize_ipc_list = '',"
                    + "add_time = :addTime "
                    + "where id = :id";

            sql = "update white_list set "
                    + "effective_end_time = :effectiveEndTime,"
                    + "synchronize_ipc_list = '',"
                    + "add_time = :addTime "
                    + "where id = :id";
            SQLQuery sqlQuery1 = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(strSql);
            sqlQuery1.setParameter("effectiveEndTime", new Timestamp(Long.parseLong(effectiveEndTime)));
            sqlQuery1.setParameter("id",id);
            sqlQuery1.setParameter("addTime", CommonUtils.getTimestamp());
            sqlQuery1.executeUpdate();

            SQLQuery sqlQuery2 = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
            sqlQuery2.setParameter("effectiveEndTime", new Timestamp(Long.parseLong(effectiveEndTime)));
            sqlQuery2.setParameter("id",id);
            sqlQuery2.setParameter("addTime", CommonUtils.getTimestamp());
            sqlQuery2.executeUpdate();
        }
        else if (kindId.equals("1")){
            strSql = "update member_wallet set "
                    + "surplus_number = :surplusNum,"
                    + "add_time = :addTime "
                    + "where id = :id";

            sql = "update white_list set "
                    + "surplus_number = :surplusNum,"
                    + "add_time = :addTime "
                    + "where id = :id";

            SQLQuery sqlQuery1 = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(strSql);
            sqlQuery1.setParameter("surplusNum", surplusNum);
            sqlQuery1.setParameter("id",id);
            sqlQuery1.setParameter("addTime", CommonUtils.getTimestamp());
            sqlQuery1.executeUpdate();

            SQLQuery sqlQuery2 = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
            sqlQuery2.setParameter("surplusNum", surplusNum);
            sqlQuery2.setParameter("id",id);
            sqlQuery2.setParameter("addTime", CommonUtils.getTimestamp());
            sqlQuery2.executeUpdate();
        }
        else {
            strSql = "update member_wallet set "
                    + "surplus_amount = :surplusAmount,"
                    + "add_time = :addTime "
                    + "where id = :id";

            sql = "update white_list set "
                    + "surplus_amount = :surplusAmount,"
                    + "add_time = :addTime "
                    + "where id = :id";
            SQLQuery sqlQuery1 = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(strSql);

            sqlQuery1.setParameter("surplusAmount", surplusAmount);
            sqlQuery1.setParameter("id",id);
            sqlQuery1.setParameter("addTime", CommonUtils.getTimestamp());
            sqlQuery1.executeUpdate();

            SQLQuery sqlQuery2 = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
            sqlQuery2.setParameter("surplusAmount", surplusAmount);
            sqlQuery2.setParameter("id",id);
            sqlQuery2.setParameter("addTime", CommonUtils.getTimestamp());
            sqlQuery2.executeUpdate();
        }



    }

    @Override
    public void getCarparkList() {
        DepartmentInfo departmentInfo = (DepartmentInfo)hibernateBaseDao.getById(DepartmentInfo.class,"1ac8cfc3badf11e79abea4bf0116715b");
        Set<CarparkInfo> carparkInfos = departmentInfo.getCarparkInfos();
    }

    /**
     * id : member_id，主键
     * parkid : 不是serviceid
     * fee : 缴费金额
     * paytime : 缴费单时间
     * kindid : 套餐ID（0-包月，1-包次，2-临时）
     * effectiveEndTime : 包月有效结束时间
     * surplusNum : 包次剩余次数
     * surplusAmount : 剩余金额
     */
    @Override
    public void addBolinkRechargeRecord(String id, String carno, String parkId, String fee, String payTime, String kindId, String effectiveEndTime, String surplusNum, String surplusAmount, String kindName) {
        String strSql = "";
        String sql = "";
        String memo = "";
        strSql = "insert into charge_receive_record( " +
                "charge_receive_id, " +
                "charge_receive_car_no, " +
                "charge_receive_type, " +
                "charge_receive_sub_type, " +
                "charge_receive_carpark, " +
                "charge_receive_post, " +
                "charge_receive_post_name, " +
                "charge_receive_amount, " +
                "charge_discount_amount, " +
                "charge_receive_operator_id, " +
                "charge_receive_time, " +
                "charge_receive_operation, " +
                "operation_source, " +
                "charge_release_id, " +
                "add_time, " +
                "charge_discount_info, " +
                "use_mark, " +
                "memo ) values ( " +
                ":uuid, " +
                ":carNo, " +
                ":payType, " +
                ":kindName, " +
                ":parkId, " +
                "'BOLINK_PAY', " +
                "'泊链云平台', " +
                ":fee, " +
                ":discount, " +
                "1, " +
                ":receiveTime, " +
                ":receiveOperation, " +
                "''," +
                " '', " +
                ":addTime, " +
                ":discountInfo," +
                "0," +
                ":memoInfo )";

        if (kindId.equals("0")){
            memo = "有效截止时间：" + effectiveEndTime;
        }
        else if (kindId.equals("1")){
            memo = "剩余次数：" + surplusNum;
        }
        else{
            memo = "剩余金额：" + surplusAmount + "元";
        }

        SQLQuery sqlQuery = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(strSql);
        sqlQuery.setParameter("uuid", UUID.randomUUID().toString().replaceAll("-",""));
        sqlQuery.setParameter("carNo", carno);
        sqlQuery.setParameter("payType", "BOLINK_PAY");
        sqlQuery.setParameter("kindName", kindName);
        sqlQuery.setParameter("parkId", parkId);
        sqlQuery.setParameter("fee",fee);
        sqlQuery.setParameter("discount", 0);
        sqlQuery.setParameter("receiveTime", Timestamp.valueOf(payTime));
        sqlQuery.setParameter("receiveOperation", "charge");
        sqlQuery.setParameter("addTime", CommonUtils.getTimestamp());
        sqlQuery.setParameter("discountInfo","无优惠");
        sqlQuery.setParameter("memoInfo",memo);
        sqlQuery.executeUpdate();

        if (kindId.equals("0")){
            strSql = "update member_wallet set "
                    + "effective_end_time = :effectiveEndTime,"
                    + "synchronize_ipc_list = '',"
                    + "add_time = :addTime,"
                    + "use_mark = 2 "
                    + "where id = :id";

            sql = "update white_list set "
                    + "effective_end_time = :effectiveEndTime,"
                    + "synchronize_ipc_list = '',"
                    + "add_time = :addTime," +
                    "use_mark = 0 "
                    + "where id = :id";
            SQLQuery sqlQuery1 = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(strSql);
            sqlQuery1.setParameter("effectiveEndTime", Timestamp.valueOf(effectiveEndTime));
            sqlQuery1.setParameter("id",id);
            sqlQuery1.setParameter("addTime", CommonUtils.getTimestamp());
            sqlQuery1.executeUpdate();

            SQLQuery sqlQuery2 = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
            sqlQuery2.setParameter("effectiveEndTime", Timestamp.valueOf(effectiveEndTime));
            sqlQuery2.setParameter("id",id);
            sqlQuery2.setParameter("addTime", CommonUtils.getTimestamp());
            sqlQuery2.executeUpdate();
        }
        else if (kindId.equals("1")){
            strSql = "update member_wallet set "
                    + "surplus_number = :surplusNum,"
                    + "add_time = :addTime "
                    + "where id = :id";

            sql = "update white_list set "
                    + "surplus_number = :surplusNum,"
                    + "add_time = :addTime "
                    + "where id = :id";

            SQLQuery sqlQuery1 = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(strSql);
            sqlQuery1.setParameter("surplusNum", surplusNum);
            sqlQuery1.setParameter("id",id);
            sqlQuery1.setParameter("addTime", CommonUtils.getTimestamp());
            sqlQuery1.executeUpdate();

            SQLQuery sqlQuery2 = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
            sqlQuery2.setParameter("surplusNum", surplusNum);
            sqlQuery2.setParameter("id",id);
            sqlQuery2.setParameter("addTime", CommonUtils.getTimestamp());
            sqlQuery2.executeUpdate();
        }
        else {
            strSql = "update member_wallet set "
                    + "surplus_amount = :surplusAmount,"
                    + "add_time = :addTime "
                    + "where id = :id";

            sql = "update white_list set "
                    + "surplus_amount = :surplusAmount,"
                    + "add_time = :addTime "
                    + "where id = :id";
            SQLQuery sqlQuery1 = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(strSql);

            sqlQuery1.setParameter("surplusAmount", surplusAmount);
            sqlQuery1.setParameter("id",id);
            sqlQuery1.setParameter("addTime", CommonUtils.getTimestamp());
            sqlQuery1.executeUpdate();

            SQLQuery sqlQuery2 = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
            sqlQuery2.setParameter("surplusAmount", surplusAmount);
            sqlQuery2.setParameter("id",id);
            sqlQuery2.setParameter("addTime", CommonUtils.getTimestamp());
            sqlQuery2.executeUpdate();
        }
    }
}
