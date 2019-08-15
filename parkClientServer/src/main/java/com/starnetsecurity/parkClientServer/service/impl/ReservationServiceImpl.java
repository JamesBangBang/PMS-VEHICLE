package com.starnetsecurity.parkClientServer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.commonService.util.DeviceManageUtils;
import com.starnetsecurity.commonService.util.StarnetDeviceUtils;
import com.starnetsecurity.parkClientServer.entity.*;
import com.starnetsecurity.parkClientServer.service.ReservationService;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by 陈峰 on 2017/10/20.
 */
@Service
public class ReservationServiceImpl implements ReservationService{
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ReservationServiceImpl.class);

    @Autowired
    HibernateBaseDao hibernateBaseDao;

    @Override
    public List<Map<String, Object>> getReservattionList(Integer start,
                                                  Integer length,
                                                  String reservationTypeName,
                                                  String carNo,
                                                  String driverName,
                                                  String driverTelephoneNumber,
                                                  String driverInfo,
                                                  String beginTimeMin,
                                                  String beginTimeMax,
                                                  String endTimeMin,
                                                  String endTimeMax,
                                                  String driverFileId,
                                                  String carparkId,
                                                  String depId) throws BizException{
        String sql =  "select t1.menber_no carNo, t1.driver_name driverName, " +
                      "t1.driver_telephone_number driverTelephoneNumber," +
                      "t1.driver_info driverInfo," +
                      "t1.department_id depId," +
                      "Date_Format(t1.effective_start_time, '%Y-%m-%d %T') effectiveStartTime," +
                      "Date_Format(t1.effective_end_time, '%Y-%m-%d %T') effectiveEndTime," +
                      "t1.car_park carparkId," +
                      "t1.id memberWalletId," +
                      "t3.carpark_name carparkName " +
                      "from member_wallet t1 " +
                      "LEFT JOIN carpark_info t3 ON t1.car_park = t3.carpark_id " +
                      "where t1.use_mark >= 0 and t1.pack_kind_id = '-2' ";
        sql = putTogetherInSql(sql, "t1.car_park", "carparkId", carparkId);
        sql = putTogetherLESql(sql, "t1.effective_end_time", "endTimeMax", endTimeMax);

        //条件查询语句
        //sql = putTogetherEqualSql(sql, "t1.reservation_type_name", "reservationTypeName", reservationTypeName);
        sql = putTogetherLikeSql(sql, "t1.menber_no", "carNo", carNo);
        sql = putTogetherLikeSql(sql, "t1.driver_name", "driverName", driverName);
        sql = putTogetherLikeSql(sql, "t1.driver_telephone_number", "driverTelephoneNumber", driverTelephoneNumber);
        sql = putTogetherLikeSql(sql, "t1.driver_info", "driverInfo", driverInfo);
        sql = putTogetherInSql(sql, "t1.department_id", "depId", depId);
        sql = sql + " order by t1.add_time desc";

        SQLQuery sqlQuery = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        setParameterToQuery(sqlQuery, "carparkId", carparkId);
        //设置值
        setParameterToQuery(sqlQuery, "endTimeMax", endTimeMax);
        setParameterToQueryByLike(sqlQuery, "carNo", carNo);
        setParameterToQueryByLike(sqlQuery, "driverName", driverName);
        setParameterToQueryByLike(sqlQuery, "driverTelephoneNumber", driverTelephoneNumber);
        setParameterToQueryByLike(sqlQuery, "driverInfo", driverInfo);
        setParameterToQuery(sqlQuery, "depId", depId);

        if (!CommonUtils.isEmpty(start)){
            sqlQuery.setFirstResult(start);
        }
        if(!CommonUtils.isEmpty(length)){
            sqlQuery.setMaxResults(length);
        }

        List<Map<String, Object>> ret = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return ret;
    }

    @Override
    public Integer getReservattionListCount(Integer start,
                                          Integer length,
                                          String reservationTypeName,
                                          String carNo,
                                          String driverName,
                                          String driverTelephoneNumber,
                                          String driverInfo,
                                          String beginTimeMin,
                                          String beginTimeMax,
                                          String endTimeMin,
                                          String endTimeMax,
                                          String driverFileId,
                                          String carparkId,
                                          String depId) throws BizException{
        String sql =  "select COUNT(*) cnt " +
                "from member_wallet t1 " +
                "LEFT JOIN carpark_info t3 ON t1.car_park = t3.carpark_id " +
                "where t1.use_mark >= 0 and t1.pack_kind_id = '-2' ";
        sql = putTogetherLESql(sql, "t1.effective_end_time", "endTimeMax", endTimeMax);

        sql = putTogetherLikeSql(sql, "t1.menber_no", "carNo", carNo);
        sql = putTogetherLikeSql(sql, "t1.driver_name", "driverName", driverName);
        sql = putTogetherLikeSql(sql, "t1.driver_telephone_number", "driverTelephoneNumber", driverTelephoneNumber);
        sql = putTogetherLikeSql(sql, "t1.driver_info", "driverInfo", driverInfo);
        sql = putTogetherInSql(sql, "t1.department_id", "depId", depId);
        //sql = sql + " order by t1.add_time desc";

        SQLQuery sqlQuery = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        setParameterToQuery(sqlQuery, "endTimeMax", endTimeMax);
        setParameterToQueryByLike(sqlQuery, "carNo", carNo);
        setParameterToQueryByLike(sqlQuery, "driverName", driverName);
        setParameterToQueryByLike(sqlQuery, "driverTelephoneNumber", driverTelephoneNumber);
        setParameterToQueryByLike(sqlQuery, "driverInfo", driverInfo);
        setParameterToQuery(sqlQuery, "depId", depId);

        List<Map<String, Object>> ret = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return Integer.parseInt(ret.get(0).get("cnt").toString());
    }

    @Override
    public Integer deleteReservation(String driverFileId, String memberWalletId, String carNo) throws BizException{
        //伪删除
        String sql = "update member_wallet set use_mark = -1 where use_mark >= 0 and id = :memberWalletId";
        SQLQuery sqlQuery = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        setParameterToQuery(sqlQuery, "memberWalletId", memberWalletId);
        //更新数据返回值
        Integer ret = sqlQuery.executeUpdate();
        return ret;
    }

    @Override
    public Integer addReservationRecord(String driverFileId,
                                        String memberWalletId,
                                        String carparkId,
                                        String carNo,
                                        String reservationTypeName,
                                        String driverName,
                                        String driverTelephoneNumber,
                                        String driverInfo,
                                        String effectiveStartTime,
                                        String effectiveEndTime) throws BizException{
        DepartmentInfo departmentInfo = ((List<DepartmentInfo>)hibernateBaseDao.queryForList("from DepartmentInfo")).get(0);

        Integer ret = -1;

        Timestamp startTime = Timestamp.valueOf(effectiveStartTime.replaceAll("/","-"));
        Timestamp endTime = Timestamp.valueOf(effectiveEndTime.replaceAll("/","-"));
        if (startTime.getTime() > endTime.getTime()) {
            throw new BizException("有效开始时间不得大于有效结束时间！");
        }

        //先判断在同个车场下是否有相同车牌号的预约信息，有的话，直接更新该车牌的信息
        String hql = "from MemberWallet where menberNo = ? and carPark = ? and useMark >0";
        MemberWallet memberWallet = (MemberWallet)hibernateBaseDao.getUnique(hql,carNo,carparkId);
        if (CommonUtils.isEmpty(memberWallet)){
            MemberWallet memberWalletAdd = new MemberWallet();
            memberWalletAdd.setMenberNo(carNo);
            memberWalletAdd.setDriverName(driverName);
            memberWalletAdd.setDriverInfo(driverInfo);
            memberWalletAdd.setDriverTelephoneNumber(driverTelephoneNumber);
            memberWalletAdd.setCarPark(carparkId);
            memberWalletAdd.setPackKindId(-2);
            memberWalletAdd.setPackKindId(-1);
            memberWalletAdd.setEffectiveStartTime(startTime);
            memberWalletAdd.setEffectiveEndTime(endTime);
            memberWalletAdd.setAddTime(CommonUtils.getTimestamp());
            memberWalletAdd.setDepartmentId(departmentInfo.getDepId());
            memberWalletAdd.setUseMark(0);
            hibernateBaseDao.save(memberWalletAdd);
            ret = 0;
        }else {
            throw new BizException("车牌已办理过套餐，请重新操作！");
        }

        return ret;
    }

    @Override
    public Integer updateReservationRecord(String driverFileId,
                                           String memberWalletId,
                                           String carparkId,
                                           String carNo,
                                           String reservationTypeName,
                                           String driverName,
                                           String driverTelephoneNumber,
                                           String driverInfo,
                                           String effectiveStartTime,
                                           String effectiveEndTime) throws BizException{
        DepartmentInfo departmentInfo = ((List<DepartmentInfo>)hibernateBaseDao.queryForList("from DepartmentInfo")).get(0);

        Integer ret = -1;
        if (("").equals(memberWalletId) || CommonUtils.isEmpty(memberWalletId)) return ret;
        Timestamp startTime = Timestamp.valueOf(effectiveStartTime.replaceAll("/","-"));
        Timestamp endTime = Timestamp.valueOf(effectiveEndTime.replaceAll("/","-"));
        if (startTime.getTime() > endTime.getTime()) {
            throw new BizException("有效开始时间不得大于有效结束时间！");
        }

        String hql = "from MemberWallet where id <> ? and menberNo = ? and carPark = ? and useMark >0";
        MemberWallet memberWallet = (MemberWallet)hibernateBaseDao.getUnique(hql,memberWalletId,carNo,carparkId);
        if (CommonUtils.isEmpty(memberWallet)){
            MemberWallet memberWalletUpdate = (MemberWallet) hibernateBaseDao.getById(MemberWallet.class,memberWalletId);
            memberWalletUpdate.setMenberNo(carNo);
            memberWalletUpdate.setDriverName(driverName);
            memberWalletUpdate.setDriverInfo(driverInfo);
            memberWalletUpdate.setDriverTelephoneNumber(driverTelephoneNumber);
            memberWalletUpdate.setCarPark(carparkId);
            memberWalletUpdate.setPackKindId(-2);
            memberWalletUpdate.setPackKindId(-1);
            memberWalletUpdate.setEffectiveStartTime(startTime);
            memberWalletUpdate.setEffectiveEndTime(endTime);
            memberWalletUpdate.setAddTime(CommonUtils.getTimestamp());
            memberWalletUpdate.setDepartmentId(departmentInfo.getDepId());
            memberWalletUpdate.setUseMark(1);
            hibernateBaseDao.save(memberWalletUpdate);
            ret = 0;
        }else {
            throw new BizException("车牌已办理过套餐，请重新操作！");
        }

        return ret;
    }

    @Override
    public Integer applyReservation(JSONObject params) {
        Integer ret = -1;
        try {
            Integer applyMode = params.getInteger("applyMode");
            CarparkInfo carparkInfo = ((List<CarparkInfo>)hibernateBaseDao.queryForList("from CarparkInfo where useMark >= 0")).get(0);
            String effectiveStartTime = params.get("effectiveStartTime") + "";
            String effectiveEndTime = params.get("effectiveEndTime") + "";
            String carNo = params.get("carNo") + "";
            String driverName = params.get("driverName") + "";
            String driverInfo = params.get("driverInfo") + "";
            String driverTelephoneNumber = params.get("driverTelephoneNumber") + "";

            Timestamp startTime = Timestamp.valueOf(effectiveStartTime.replaceAll("/","-"));
            Timestamp endTime = Timestamp.valueOf(effectiveEndTime.replaceAll("/","-"));
            if (startTime.getTime() > endTime.getTime()) {
                throw new BizException("有效开始时间不得大于有效结束时间！");
            }

            //先判断是否是否录入过系统
            String hql = "from MemberWallet where menberNo = ? and carPark = ?";
            MemberWallet memberWallet = (MemberWallet)hibernateBaseDao.getUnique(hql,carNo,carparkInfo.getCarparkId());
            if (CommonUtils.isEmpty(memberWallet)){
                MemberWallet memberWalletAdd = new MemberWallet();
                memberWalletAdd.setMenberNo(carNo);
                memberWalletAdd.setDriverName(driverName);
                memberWalletAdd.setDriverInfo(driverInfo);
                memberWalletAdd.setDriverTelephoneNumber(driverTelephoneNumber);
                memberWalletAdd.setCarPark(carparkInfo.getCarparkId());
                memberWalletAdd.setPackKindId(-2);
                memberWalletAdd.setPackChlidKind(-2);  //-2为待审核
                memberWalletAdd.setEffectiveStartTime(startTime);
                memberWalletAdd.setEffectiveEndTime(endTime);
                memberWalletAdd.setAddTime(CommonUtils.getTimestamp());
                memberWalletAdd.setDepartmentId(carparkInfo.getDepartmentInfo().getDepId());
                if (applyMode.equals(0)) {
                    //applyMode：0-直接提交，无需审核；1-需要审核
                    memberWalletAdd.setUseMark(0);
                }else {
                    memberWalletAdd.setUseMark(-3);
                }
                hibernateBaseDao.save(memberWalletAdd);
                ret = 0;
            }else {
                if (memberWallet.getPackKindId().equals(-2)){
                    memberWallet.setDriverName(driverName);
                    memberWallet.setDriverInfo(driverInfo);
                    memberWallet.setDriverTelephoneNumber(driverTelephoneNumber);
                    memberWallet.setCarPark(carparkInfo.getCarparkId());
                    memberWallet.setPackKindId(-2);
                    memberWallet.setPackChlidKind(-2);
                    memberWallet.setEffectiveStartTime(startTime);
                    memberWallet.setEffectiveEndTime(endTime);
                    memberWallet.setDepartmentId(carparkInfo.getDepartmentInfo().getDepId());
                    if (applyMode.equals(0)) {
                        //applyMode：0-直接提交，无需审核；1-需要审核
                        memberWallet.setUseMark(0);
                    }else {
                        memberWallet.setUseMark(-3);
                    }
                    hibernateBaseDao.update(memberWallet);
                    ret = 0;
                }else {
                    throw new BizException("该车辆在此车场已办理过套餐，预约申请失败");
                }

            }
        } catch (BizException e) {
            LOGGER.error("提交预约车信息失败");
        }

        return ret;
    }

    @Override
    public List<JSONObject> showReservationList(JSONObject params) {
        List<JSONObject> res = new ArrayList<>();
        try {
            Integer applyMode = params.getInteger("applyMode");
            String hql = "select * from member_wallet where pack_kind_id = -2 and pack_chlid_kind = -2 and use_mark >=0";
            if (applyMode.equals(1)){
                //当applymode为1时，只显示未审核的车辆
                hql = "select * from member_wallet where pack_kind_id = -2 and pack_chlid_kind = -2 and use_mark = -3";
            }
            if (params.containsKey("carNo") && (!CommonUtils.isEmpty(params.get("carNo") + ""))){
                hql = hql + " and menber_no like '%" + params.getString("carNo") + "%'";
            }
            if (params.containsKey("driverName") && (!CommonUtils.isEmpty(params.get("driverName") + ""))){
                hql = hql + " and driver_name like '%" + params.getString("driverName") + "%'";
            }
            if (params.containsKey("beginTime") && (!CommonUtils.isEmpty(params.get("beginTime") + ""))){
                hql = hql + " and effective_end_time >= '" + params.getString("beginTime") + "'";
            }
            if (params.containsKey("endTime") && (!CommonUtils.isEmpty(params.get("endTime") + ""))){
                hql = hql + " and effective_end_time <= '" + params.getString("endTime") + "'";
            }
            hql = hql + " order by add_time DESC";
            SQLQuery sqlQuery = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
            if (params.containsKey("startPosition") && (!CommonUtils.isEmpty(params.get("startPosition") + ""))){
                sqlQuery.setFirstResult(Integer.valueOf(params.getInteger("startPosition")));
            }
            if (params.containsKey("length") && (!CommonUtils.isEmpty(params.get("length") + ""))){
                sqlQuery.setMaxResults(Integer.valueOf(params.getInteger("length")));
            }
            List<Map> list = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
            if (list.size() > 0){
                for (Map map : list){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id",map.get("id") + "");
                    jsonObject.put("carNo",map.get("menber_no") + "");
                    jsonObject.put("driverName",map.get("driver_name") + "");
                    jsonObject.put("driverInfo",map.get("driver_info") + "");
                    jsonObject.put("driverTelephoneNumber",map.get("driver_telephone_number") + "");
                    jsonObject.put("effectiveStartTime",map.get("effective_start_time") + "");
                    jsonObject.put("effectiveEndTime",map.get("effective_end_time") + "");
                    res.add(jsonObject);
                }
            }
        } catch (Exception e) {
            LOGGER.info("获取预约车申请列表失败");
        }
        return res;
    }

    @Override
    public Integer handleReservation(JSONObject params) {
        Integer ret = -1;
        try {
            String carNo = params.get("carNo") + "";
            Integer handleMode = params.getInteger("handleMode");
            //查找看看是否有待审核的预约车
            String hql = "from MemberWallet where menberNo = ? and packKindId = -2 and packChlidKind = -2 and useMark = -3";
            MemberWallet memberWallet = (MemberWallet)hibernateBaseDao.getUnique(hql,carNo);
            if (!CommonUtils.isEmpty(memberWallet)){
                if (handleMode.equals(1)) {
                    memberWallet.setUseMark(0);
                    hibernateBaseDao.update(memberWallet);
                    ret = 0;
                }else {
                    memberWallet.setUseMark(-1);
                    hibernateBaseDao.update(memberWallet);
                    ret = 0;
                }
            }else {
                ret = -1;
            }
        } catch (BizException e) {
            LOGGER.error("提交预约车信息失败");
        }

        return ret;
    }

    @Override
    public Boolean isCarparkRepeat(String carparkId, String carNo, String memberWalletId) throws BizException{
        String sql = "select count(*) cnt from member_wallet " +
                "where use_mark >= 0 and id <> :memberWalletId " +
                "and menber_no = :carNo and car_park = :carparkId";
        SQLQuery sqlQuery = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter("memberWalletId", memberWalletId);
        sqlQuery.setParameter("carNo", carNo);
        sqlQuery.setParameter("carparkId", carparkId);
        List<Map<String, Object>> list = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        Integer cnt = Integer.parseInt(list.get(0).get("cnt").toString());
        if(cnt > 0){
            return true;
        }else return false;
    }

    @Override
    public List<Map<String, Object>> getBlacklist(Integer start,
                                                  Integer length,
                                                  String carNo,
                                                  String driverName,
                                                  String carparkId,
                                                  String beginTimeMin,
                                                  String beginTimeMax,
                                                  String endTimeMin,
                                                  String endTimeMax,
                                                  String depId)throws BizException {
        String sql =  "select t1.menber_no carNo, t1.driver_name driverName, " +
                "t1.driver_telephone_number driverTelephoneNumber," +
                "t1.driver_info driverInfo," +
                "t1.department_id depId," +
                "Date_Format(t1.effective_start_time, '%Y-%m-%d %T') effectiveStartTime," +
                "Date_Format(t1.effective_end_time, '%Y-%m-%d %T') effectiveEndTime," +
                "t1.car_park carparkId," +
                "t1.id memberWalletId," +
                "t3.carpark_name carparkName " +
                "from member_wallet t1 " +
                "LEFT JOIN carpark_info t3 ON t1.car_park = t3.carpark_id " +
                "where t1.use_mark >= 0 and t1.pack_kind_id = '-1' ";
        sql = putTogetherInSql(sql, "t1.car_park", "carparkId", carparkId);
        sql = putTogetherLESql(sql, "t1.effective_end_time", "endTimeMax", endTimeMax);

        //条件查询语句
        //sql = putTogetherEqualSql(sql, "t1.reservation_type_name", "reservationTypeName", reservationTypeName);
        sql = putTogetherLikeSql(sql, "t1.menber_no", "carNo", carNo);
        sql = putTogetherLikeSql(sql, "t1.driver_name", "driverName", driverName);
        sql = putTogetherInSql(sql, "t1.department_id", "depId", depId);
        sql = sql + " order by t1.add_time desc";

        SQLQuery sqlQuery = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        setParameterToQuery(sqlQuery, "carparkId", carparkId);
        //设置值
        setParameterToQuery(sqlQuery, "endTimeMax", endTimeMax);
        setParameterToQueryByLike(sqlQuery, "carNo", carNo);
        setParameterToQueryByLike(sqlQuery, "driverName", driverName);
        setParameterToQuery(sqlQuery, "depId", depId);

        if (!CommonUtils.isEmpty(start)){
            sqlQuery.setFirstResult(start);
        }
        if(!CommonUtils.isEmpty(length)){
            sqlQuery.setMaxResults(length);
        }

        List<Map<String, Object>> ret = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return ret;
    }

    @Override
    public Integer getBlacklistCount(Integer start,
                                  Integer length,
                                  String carNo,
                                  String driverName,
                                  String carparkId,
                                  String beginTimeMin,
                                  String beginTimeMax,
                                  String endTimeMin,
                                  String endTimeMax,
                                  String depId) {
        String sql =  "select COUNT(*) cnt " +
                "from member_wallet t1 " +
                "LEFT JOIN carpark_info t3 ON t1.car_park = t3.carpark_id " +
                "where t1.use_mark >= 0 and t1.pack_kind_id = '-1' ";
        sql = putTogetherLESql(sql, "t1.effective_end_time", "endTimeMax", endTimeMax);

        sql = putTogetherLikeSql(sql, "t1.menber_no", "carNo", carNo);
        sql = putTogetherLikeSql(sql, "t1.driver_name", "driverName", driverName);
        sql = putTogetherInSql(sql, "t1.department_id", "depId", depId);
        //sql = sql + " order by t1.add_time desc";

        SQLQuery sqlQuery = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        setParameterToQuery(sqlQuery, "endTimeMax", endTimeMax);
        setParameterToQueryByLike(sqlQuery, "carNo", carNo);
        setParameterToQueryByLike(sqlQuery, "driverName", driverName);
        setParameterToQuery(sqlQuery, "depId", depId);

        List<Map<String, Object>> ret = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return Integer.parseInt(ret.get(0).get("cnt").toString());

    }

    @Override
    public Boolean isBlacklistCarparkRepeat(String carparkId, String carNo, String memberWalletId) {
        String sql = "select count(*) cnt from member_wallet " +
                "where use_mark >= 0 and id <> :memberWalletId " +
                "and menber_no = :carNo and car_park = :carparkId";
        SQLQuery sqlQuery = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter("memberWalletId", memberWalletId);
        sqlQuery.setParameter("carNo", carNo);
        sqlQuery.setParameter("carparkId", carparkId);

        List<Map<String, Object>> list = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        Integer cnt = Integer.parseInt(list.get(0).get("cnt").toString());
        if(cnt > 0){
            return true;
        }else return false;
    }

    @Override
    public void saveBlacklist(Map params) {
        String controllerTag=(String)params.get("controlMode");
        String memberWalletId=(String)params.get("memberWalletId");
        String driverFileId=(String)params.get("driverFileId");
        String carNo=(String)params.get("carNo");
        String carparkId=(String)params.get("carparkId");
        String depId=(String)params.get("depId");
        if (CommonUtils.isEmpty(depId)){
            String hql = "from DepartmentInfo where useMark >= 0";
            DepartmentInfo departmentInfo = (DepartmentInfo)hibernateBaseDao.getUnique(hql);
            depId = departmentInfo.getDepId();
        }

        String driverInfo=(String)params.get("driverInfo");
        String driverName=(String)params.get("driverName");
        String driverTelephoneNumber=(String)params.get("driverTelephoneNumber");

        Timestamp effectiveStartTime = Timestamp.valueOf(params.get("effectiveStartTime") + "");

        Timestamp effectiveEndTime = Timestamp.valueOf(params.get("effectiveEndTime") + "");

        if (effectiveStartTime.getTime() > effectiveEndTime.getTime()) {
            throw new BizException("有效开始时间不得大于有效结束时间！");
        }
        if ("0".equals(controllerTag)) {
            //先判断在同个车场下是否有相同车牌号的预约信息，有的话，直接更新该车牌的信息
            String hql = "from MemberWallet where menberNo = ? and carPark = ? and useMark > 0";
            MemberWallet memberWallet = (MemberWallet) hibernateBaseDao.getUnique(hql, carNo, carparkId);
            if (CommonUtils.isEmpty(memberWallet)) {
                MemberWallet memberWalletAdd = new MemberWallet();
                memberWalletAdd.setMenberNo(carNo);
                memberWalletAdd.setDriverName(driverName);
                memberWalletAdd.setDriverInfo(driverInfo);
                memberWalletAdd.setDriverTelephoneNumber(driverTelephoneNumber);
                memberWalletAdd.setCarPark(carparkId);
                memberWalletAdd.setPackKindId(-1);
                memberWalletAdd.setPackChlidKind(-1);
                memberWalletAdd.setEffectiveStartTime(effectiveStartTime);
                memberWalletAdd.setEffectiveEndTime(effectiveEndTime);
                memberWalletAdd.setAddTime(CommonUtils.getTimestamp());
                memberWalletAdd.setDepartmentId(depId);
                memberWalletAdd.setUseMark(0);
                hibernateBaseDao.save(memberWalletAdd);

                //下发到IPC
                JSONObject balckParams = new JSONObject();
                balckParams.put("plate",carNo);
                balckParams.put("owner",driverName);
                DeviceManageUtils deviceManageUtils = new StarnetDeviceUtils();

                hql = "from InOutCarRoadInfo where ownCarparkNo = ?";
                List<InOutCarRoadInfo> carRoadInfos = (List<InOutCarRoadInfo>)hibernateBaseDao.queryForList(hql,memberWalletAdd.getCarPark());
                for(InOutCarRoadInfo carRoadInfo : carRoadInfos){
                    hql = "from DeviceInfo where ownCarRoad = ? and deviceType = '0' and useMark >= 0";
                    List<DeviceInfo> deviceInfos = (List<DeviceInfo>)hibernateBaseDao.queryForList(hql,carRoadInfo.getCarRoadId());
                    for(DeviceInfo deviceInfo : deviceInfos){
                        deviceManageUtils.addBlackMember(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd(),balckParams);
                    }
                }
            } else {
                throw new BizException("车牌已办理过套餐，请重新操作！");
            }
        }else {
            if (("").equals(memberWalletId) || CommonUtils.isEmpty(memberWalletId)) return;
            String hql = "from MemberWallet where id <> ? and menberNo = ? and carPark = ? and useMark >0";
            MemberWallet memberWallet = (MemberWallet)hibernateBaseDao.getUnique(hql,memberWalletId,carNo,carparkId);
            if (CommonUtils.isEmpty(memberWallet)){
                MemberWallet memberWalletUpdate = (MemberWallet) hibernateBaseDao.getById(MemberWallet.class,memberWalletId);
                String oldPlate = memberWalletUpdate.getMenberNo();
                memberWalletUpdate.setMenberNo(carNo);
                memberWalletUpdate.setDriverName(driverName);
                memberWalletUpdate.setDriverInfo(driverInfo);
                memberWalletUpdate.setDriverTelephoneNumber(driverTelephoneNumber);
                memberWalletUpdate.setCarPark(carparkId);
                memberWalletUpdate.setPackKindId(-1);
                memberWalletUpdate.setPackChlidKind(-1);
                memberWalletUpdate.setEffectiveStartTime(effectiveStartTime);
                memberWalletUpdate.setEffectiveEndTime(effectiveEndTime);
                memberWalletUpdate.setAddTime(CommonUtils.getTimestamp());
                memberWalletUpdate.setDepartmentId(depId);
                memberWalletUpdate.setUseMark(1);
                hibernateBaseDao.save(memberWalletUpdate);

                //下发到IPC
                JSONObject balckParams = new JSONObject();
                balckParams.put("oldplate",oldPlate);
                balckParams.put("owner",driverName);
                balckParams.put("plate",carNo);
                DeviceManageUtils deviceManageUtils = new StarnetDeviceUtils();

                hql = "from InOutCarRoadInfo where ownCarparkNo = ?";
                List<InOutCarRoadInfo> carRoadInfos = (List<InOutCarRoadInfo>)hibernateBaseDao.queryForList(hql,memberWalletUpdate.getCarPark());
                for(InOutCarRoadInfo carRoadInfo : carRoadInfos){
                    hql = "from DeviceInfo where ownCarRoad = ? and deviceType = '0' and useMark >= 0";
                    List<DeviceInfo> deviceInfos = (List<DeviceInfo>)hibernateBaseDao.queryForList(hql,carRoadInfo.getCarRoadId());
                    for(DeviceInfo deviceInfo : deviceInfos){
                        deviceManageUtils.updateBlackMember(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd(),balckParams);
                    }
                }
            }else {
                throw new BizException("车牌已办理过套餐，请重新操作！");
            }

        }


    }

    @Override
    public List<Map<String, Object>> getEditBlacklist(Map params) {
        String sql =  "select t1.menber_no carNo, t1.driver_name driverName, " +
                "t1.driver_telephone_number driverTelephoneNumber," +
                "t1.driver_info driverInfo," +
                "t1.department_id depId," +
                "t1.pack_chlid_kind packChlidKind," +
                "Date_Format(t1.effective_start_time, '%Y-%m-%d %T') effectiveStartTime," +
                "Date_Format(t1.effective_end_time, '%Y-%m-%d %T') effectiveEndTime," +
                "t1.car_park carparkId," +
                "t1.id memberWalletId," +
                "t3.carpark_name carparkName " +
                "from member_wallet t1 " +
                "LEFT JOIN carpark_info t3 ON t1.car_park = t3.carpark_id " +
                "where t1.use_mark >= 0 and t1.pack_kind_id = '-1' ";


        String id=(String) params.get("id");
        sql = putTogetherInSql(sql, "t1.id", "id", id);

        SQLQuery sqlQuery = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

        setParameterToQuery(sqlQuery, "id", id);
        List<Map<String, Object>> res = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return res;
    }

    @Override
    public void delBlacklist(JSONObject params) {
        String memberWalletId=params.getString("memberWalletId");
        if(!StringUtils.isBlank(memberWalletId)){
            MemberWallet memberWallet = (MemberWallet) hibernateBaseDao.getById(MemberWallet.class,memberWalletId);
            memberWallet.setUseMark(-1);
            hibernateBaseDao.update(memberWallet);

            //下发到IPC
            JSONObject balckParams = new JSONObject();
            balckParams.put("plate",memberWallet.getMenberNo());
            DeviceManageUtils deviceManageUtils = new StarnetDeviceUtils();

            String hql = "from InOutCarRoadInfo where ownCarparkNo = ?";
            List<InOutCarRoadInfo> carRoadInfos = (List<InOutCarRoadInfo>)hibernateBaseDao.queryForList(hql,memberWallet.getCarPark());
            for(InOutCarRoadInfo carRoadInfo : carRoadInfos){
                hql = "from DeviceInfo where ownCarRoad = ? and deviceType = '0' and useMark >= 0";
                List<DeviceInfo> deviceInfos = (List<DeviceInfo>)hibernateBaseDao.queryForList(hql,carRoadInfo.getCarRoadId());
                for(DeviceInfo deviceInfo : deviceInfos){
                    deviceManageUtils.delBlackMember(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd(),balckParams);
                }
            }
        }



    }

    //方法
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
}
