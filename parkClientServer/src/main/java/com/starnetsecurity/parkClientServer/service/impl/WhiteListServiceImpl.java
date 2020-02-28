package com.starnetsecurity.parkClientServer.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.util.PoiExcelUtil;
import com.starnetsecurity.commonService.util.DeviceManageUtils;
import com.starnetsecurity.commonService.util.StarnetDeviceUtils;
import com.starnetsecurity.parkClientServer.clientEnum.LogEnum;
import com.starnetsecurity.parkClientServer.entity.*;
import com.starnetsecurity.parkClientServer.entityEnum.payStatusEnum;
import com.starnetsecurity.parkClientServer.entityEnum.payTypeEnum;
import com.starnetsecurity.parkClientServer.init.AppInfo;
import com.starnetsecurity.parkClientServer.quartz.UploadWhiteListThread;
import com.starnetsecurity.parkClientServer.service.LogOperationService;
import com.starnetsecurity.parkClientServer.service.UploadDataToCloudService;
import com.starnetsecurity.parkClientServer.service.WhiteListService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.TransactionException;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.sql.Timestamp;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by JamesBangBang on 2017/10/26.
 */
@Service
public class WhiteListServiceImpl implements WhiteListService{
    private static final Logger LOGGER = LoggerFactory.getLogger(WhiteListServiceImpl.class);

    @Autowired
    HibernateBaseDao hibernateBaseDao;

    @Autowired
    UploadDataToCloudService uploadDataToCloudService;

    @Autowired
    LogOperationService logOperationService;

    public List<Map<String, Object>> getWhiteList(String carNo,
                                                  String chargeTypeName,
                                                  String driverName,
                                                  String driverTelephoneNumber,
                                                  String driverInfo,
                                                  String carparkId,
                                                  String depId,
                                                  String packageKind,String overdueTime,Integer start,Integer length) throws BizException{
        List<Map<String, Object>> ret = new ArrayList<>();
        try {
            String sql = "select t1.menber_no carNo,t1.id,t1.car_park carparkId,t1.valid_menber_count validCount," +
                         "case when t1.pack_kind_id = -3 THEN CONCAT('截止时间：' , t1.effective_end_time) " +
                         "when t1.pack_kind_id = 0 THEN CONCAT('截止时间：' , t1.effective_end_time) " +
                         "when t1.pack_kind_id = 1 THEN CONCAT('剩余次数：' , t1.surplus_number) "+
                         "when t1.pack_kind_id = 2 THEN CONCAT('剩余金额：' , t1.surplus_amount) END AS memberInfo," +
                         "t1.driver_name driverName,t1.driver_telephone_number driverTelephoneNumber,t1.driver_info driverInfo,t1.department_id depId," +
                         "t3.kind_name chargeTypeName," +
                         "t4.carpark_name carparkName " +
                         "from member_wallet t1 " +
                         "LEFT JOIN member_kind t3 ON t1.menber_type_id = t3.id " +
                         "LEFT JOIN carpark_info t4 ON t1.car_park = t4.carpark_id " +
                         "where t1.use_mark >= 0 and (t1.pack_kind_id = '-3' " +
                         "or t1.pack_kind_id = '0' or t1.pack_kind_id = '1' or t1.pack_kind_id = '2')";


            sql = putTogetherInSql(sql, "t1.car_park", "carparkId", carparkId);
            sql = putTogetherEqualSql(sql, "t3.kind_name", "chargeTypeName", chargeTypeName);
            sql = putTogetherLikeSql(sql, "t1.menber_no", "carNo", carNo);
            sql = putTogetherLikeSql(sql, "t1.driver_name", "driverName", driverName);
            sql = putTogetherLikeSql(sql, "t1.driver_telephone_number", "driverTelephoneNumber", driverTelephoneNumber);
            sql = putTogetherLikeSql(sql, "t1.driver_info", "driverInfo", driverInfo);
            sql = putTogetherInSql(sql, "t1.department_id", "depId", depId);
            sql = putTogetherInSql(sql, "t1.pack_kind_id", "packageKind", packageKind);
            if (!StringUtils.isBlank(overdueTime)) {
                sql = sql + " and t1.effective_end_time <= '" + overdueTime + "' "
                        + "AND t1.pack_kind_id <> 1 AND t1.pack_kind_id <> 2";
            }
            sql = sql + " order by t1.add_time desc";

            SQLQuery sqlQuery = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
            setParameterToQuery(sqlQuery, "carparkId", carparkId);
            setParameterToQueryByLike(sqlQuery, "carNo", carNo);
            setParameterToQuery(sqlQuery, "chargeTypeName", chargeTypeName);
            setParameterToQueryByLike(sqlQuery, "driverName", driverName);
            setParameterToQueryByLike(sqlQuery, "driverTelephoneNumber", driverTelephoneNumber);
            setParameterToQueryByLike(sqlQuery, "driverInfo", driverInfo);
            setParameterToQuery(sqlQuery, "depId", depId);
            setParameterToQueryInt(sqlQuery, "packageKind", packageKind);
            sqlQuery.setFirstResult(start).setMaxResults(length);
            ret = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        } catch (Exception e) {
            LOGGER.error("获取白名单信息失败");
        }
        return ret;
    }

    public Integer getWhiteListCount(String carNo,
                                     String chargeTypeName,
                                     String driverName,
                                     String driverTelephoneNumber,
                                     String driverInfo,
                                     String carparkId,
                                     String depId,
                                     String overdueTime) throws BizException{
        List<Map<String, Object>> ret = null;
        try {
            String sql = "select COUNT(*) cnt " +
                    "from member_wallet t1 " +
                    "LEFT JOIN member_kind t3 ON t1.menber_type_id = t3.id " +
                    "LEFT JOIN carpark_info t4 ON t1.car_park = t4.carpark_id " +
                    "where t1.use_mark >= 0 and (t1.pack_kind_id = '-3' " +
                    "or t1.pack_kind_id = '0' or t1.pack_kind_id = '1' or t1.pack_kind_id = '2') ";

            sql = putTogetherInSql(sql, "t1.car_park", "carparkId", carparkId);
            sql = putTogetherEqualSql(sql, "t3.kind_name", "chargeTypeName", chargeTypeName);
            sql = putTogetherLikeSql(sql, "t1.menber_no", "carNo", carNo);
            sql = putTogetherLikeSql(sql, "t1.driver_name", "driverName", driverName);
            sql = putTogetherLikeSql(sql, "t1.driver_telephone_number", "driverTelephoneNumber", driverTelephoneNumber);
            sql = putTogetherLikeSql(sql, "t1.driver_info", "driverInfo", driverInfo);
            sql = putTogetherInSql(sql, "t1.department_id", "depId", depId);
            if (!StringUtils.isBlank(overdueTime))
                sql = sql + " and t1.effective_end_time <= '" + overdueTime + "' ";
            sql = sql + " order by t1.add_time desc";

            SQLQuery sqlQuery = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
            setParameterToQuery(sqlQuery, "carparkId", carparkId);
            setParameterToQueryByLike(sqlQuery, "carNo", carNo);
            setParameterToQuery(sqlQuery, "chargeTypeName", chargeTypeName);
            setParameterToQueryByLike(sqlQuery, "driverName", driverName);
            setParameterToQueryByLike(sqlQuery, "driverTelephoneNumber", driverTelephoneNumber);
            setParameterToQueryByLike(sqlQuery, "driverInfo", driverInfo);
            setParameterToQuery(sqlQuery, "depId", depId);


            ret = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        } catch (HibernateException e) {
            return 0;
        }

        return Integer.parseInt(ret.get(0).get("cnt").toString());
    }

    @Override
    public List getAllCarPark() {
        String hql = "from CarparkInfo where useMark >= 0";
        List<CarparkInfo> carparkInfos = (List<CarparkInfo>)hibernateBaseDao.queryForList(hql);
        List<Map> list = new ArrayList<>();
        for(CarparkInfo carparkInfo :  carparkInfos){
            Map<String,Object> map = new HashedMap();
            map.put("id",carparkInfo.getCarparkId());
            map.put("name",carparkInfo.getCarparkName());
            list.add(map);
        }
        return list;

    }

    @Override
    public List getAllPackage(String carparkId) {
        CarparkInfo carparkInfo = new CarparkInfo();
        carparkInfo.setCarparkId(carparkId);
        String hql = "from MemberKind where (carparkInfo is null or carparkInfo = ? or carparkInfo = '') and useMark >= 0";
        List<MemberKind> memberKinds = (List<MemberKind>)hibernateBaseDao.queryForList(hql,carparkInfo);
        List<Map> list = new ArrayList<>();
        for(MemberKind memberKind :  memberKinds){
            Map<String,Object> map = new HashedMap();
            map.put("id",memberKind.getId());
            map.put("name",memberKind.getKindName());
            map.put("packageKind",memberKind.getPackageKind());
            map.put("monthPackage",memberKind.getMonthPackage());
            map.put("countPackage",memberKind.getCountPackage());
            list.add(map);
        }
        return list;
    }

    @Override
    public void saveOwner(Map params,AdminUser adminUser,String ip) throws BizException {
        try {
            List<String> carparkIdList = (List<String>)params.get("carParkId");
            if (carparkIdList == null || carparkIdList.size() <= 0){
                throw new BizException("车场信息不能为空");
            }
            for (String carparkId : carparkIdList) {
                String memberCarno = (String) params.get("carNo");
                if (",".equals(memberCarno.substring(memberCarno.length() - 1, memberCarno.length())))
                    memberCarno = memberCarno.substring(0, memberCarno.length() - 1);
                String[] carNos = memberCarno.split(",");
                for (String carNo : carNos) {
                    String hql = "select count(1) from MemberWallet where menberNo like ? and carPark = ? and useMark >= 0";
                    Long count = (Long) hibernateBaseDao.getUnique(hql, "%" + carNo + "%", carparkId);
                    if (count > 0) {
                        throw new BizException("套餐[" + carNo + "]信息已存在");
                    }

                }

                String hql = "from DepartmentInfo";
                DepartmentInfo departmentInfo = ((List<DepartmentInfo>) hibernateBaseDao.queryForList(hql)).get(0);

                MemberKind memberKind = (MemberKind) hibernateBaseDao.getById(MemberKind.class, (String) params.get("memberKindId"));
                if (memberKind.getPackageKind().equals(0)) {

                    Double packageCount = Double.parseDouble((String) params.get("surplusAmount")) / Double.parseDouble(memberKind.getMonthPackage());
                    if (packageCount < 1) {
                        throw new BizException("包月至少充值一个月");
                    }
                } else if (memberKind.getPackageKind().equals(1)) {
                    Double packageCount = Double.parseDouble((String) params.get("surplusAmount")) / Double.parseDouble(memberKind.getCountPackage());
                    if (packageCount < 1) {
                        throw new BizException("包次至少充值一次");
                    }
                }

                if (memberKind.getPackageKind().equals(-3) || memberKind.getPackageKind().equals(0)) {
                    Timestamp startT = Timestamp.valueOf((String) params.get("startTime") + " 00:00:00");
                    Timestamp endT = Timestamp.valueOf((String) params.get("endTime") + " 23:59:59");
                    if (startT.getTime() > endT.getTime())
                        throw new BizException("套餐有效开始时间不得大于有效结束时间");
                }

                String carNumber = (String) params.get("carNumber");
                MemberWallet memberWallet = new MemberWallet();
                memberWallet.setMenberNo(memberCarno);
                memberWallet.setDriverName("" + params.get("driverName"));
                memberWallet.setDriverInfo("" + params.get("driverInfo"));
                memberWallet.setDriverTelephoneNumber("" + params.get("driverTelephoneNumber"));
                memberWallet.setParkingLotId((String) params.get("parkingLot"));
                memberWallet.setCarPark(carparkId);
                memberWallet.setMenberTypeId((String) params.get("memberKindId"));
                memberWallet.setPackKindId(memberKind.getPackageKind());
                memberWallet.setPackChlidKind(memberKind.getPackageChildKind());
                memberWallet.setSurplusAmount(Double.parseDouble((String) params.get("surplusAmount")));
                memberWallet.setSurplusNumber(Integer.parseInt((String) params.get("surplusNumber")));
                memberWallet.setEffectiveStartTime(Timestamp.valueOf((String) params.get("startTime") + " 00:00:00"));
                memberWallet.setEffectiveEndTime(Timestamp.valueOf((String) params.get("endTime") + " 23:59:59"));
                memberWallet.setAddTime(CommonUtils.getTimestamp());
                memberWallet.setValidMenberCount(carNumber);
                memberWallet.setDepartmentId(departmentInfo.getDepId());
                memberWallet.setUseMark(0);

                OrderPrechargeRecord orderPrechargeRecord = new OrderPrechargeRecord();
                OrderTransaction orderTransaction = new OrderTransaction();
                if (Double.parseDouble((String) params.get("surplusAmount")) > 0) {
                    orderPrechargeRecord.setOrderPrechargeCarno(memberCarno);
                    orderPrechargeRecord.setOrderPrechargeCarpark(carparkId);
                    orderPrechargeRecord.setOrderPrechargeCarparkName((String) params.get("carParkName"));
                    orderPrechargeRecord.setOrderPrechargeTime(CommonUtils.getTimestamp());
                    orderPrechargeRecord.setChargeMemberKindId(memberKind.getId());
                    orderPrechargeRecord.setChargeMemberKindName(memberKind.getKindName());
                    orderPrechargeRecord.setOrderPrechargeOperatorId(adminUser.getId());
                    orderPrechargeRecord.setOrderPrechargeOperatorName(adminUser.getTrueName());
                    orderPrechargeRecord.setOrderPrechargeReceivableAmount(Double.parseDouble((String) params.get("surplusAmount")));
                    orderPrechargeRecord.setOrderPrechargeActualAmount(Double.parseDouble((String) params.get("surplusAmount")));
                    orderPrechargeRecord.setOrderPrechargeFreeAmount(0D);
                    orderPrechargeRecord.setAddTime(CommonUtils.getTimestamp());
                    hibernateBaseDao.save(orderPrechargeRecord);

                    orderTransaction.setOrderId(orderPrechargeRecord.getOrderPrechargeId());
                    orderTransaction.setPayType(payTypeEnum.B);
                    orderTransaction.setPayTime(CommonUtils.getTimestamp());
                    orderTransaction.setPayStatus(payStatusEnum.HAS_PAID);
                    //orderTransaction.setPayTypeName(payTypeEnum.CASH_PAY.getDesc());
                    orderTransaction.setTotalFee(new BigDecimal((String) params.get("surplusAmount")));
                    orderTransaction.setRealFee(new BigDecimal((String) params.get("surplusAmount")));
                    orderTransaction.setDiscountFee(new BigDecimal(0));
                    orderTransaction.setDiscountType("");
                    orderTransaction.setAddTime(CommonUtils.getTimestamp());
                    orderTransaction.setUpdateTime(CommonUtils.getTimestamp());
                    hibernateBaseDao.save(orderTransaction);
                } else {
                    orderPrechargeRecord = null;
                    orderTransaction = null;
                }

                if (memberWallet.getPackKindId().equals(0) || memberKind.getPackageKind().equals(-3)) {
                    if (carNos.length == 1) {
                        JSONObject whiteParams = new JSONObject();
                        whiteParams.put("GroupId", 0);
                        whiteParams.put("owner", memberWallet.getDriverName());
                        whiteParams.put("plate", memberWallet.getMenberNo());
                        whiteParams.put("card", memberWallet.getDriverTelephoneNumber());
                        whiteParams.put("start", CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss", memberWallet.getEffectiveStartTime()));
                        whiteParams.put("end", CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss", memberWallet.getEffectiveEndTime()));
                        DeviceManageUtils deviceManageUtils = new StarnetDeviceUtils();

                        hql = "from InOutCarRoadInfo where ownCarparkNo = ? and useMark >= 0";
                        List<InOutCarRoadInfo> carRoadInfos = (List<InOutCarRoadInfo>) hibernateBaseDao.queryForList(hql, carparkId);
                        for (InOutCarRoadInfo carRoadInfo : carRoadInfos) {
                            hql = "from DeviceInfo where ownCarRoad = ? and deviceType = '0' and useMark >= 0";
                            List<DeviceInfo> deviceInfos = (List<DeviceInfo>) hibernateBaseDao.queryForList(hql, carRoadInfo.getCarRoadId());
                            for (DeviceInfo deviceInfo : deviceInfos) {
                                deviceManageUtils.addWhiteMember(deviceInfo.getDeviceIp(), Integer.parseInt(deviceInfo.getDevicePort()), deviceInfo.getDeviceUsername(), deviceInfo.getDevicePwd(), whiteParams);
                            }
                        }
                        memberWallet.setSynchronizeIpcList("WhiteList");
                    }
                }
                hibernateBaseDao.save(memberWallet);
                logOperationService.addOperatorLog(LogEnum.operateDriver, "新增车牌：" + memberWallet.getMenberNo() + "的套餐信息", ip, null, adminUser);
                uploadDataToCloudService.uploadMemberWalletInfoToServer(memberWallet, orderPrechargeRecord, orderTransaction);
            }
        } catch (Exception e) {
            throw new BizException("保存白名单信息失败：" + e.getMessage());
        }
    }

    @Override
    public void delOwner(String id,String ip) {
        try {
            //园区的直接删除
            MemberWallet memberWallet = (MemberWallet)hibernateBaseDao.getById(MemberWallet.class,id);
            hibernateBaseDao.deleteById(MemberWallet.class,id);
            if(memberWallet.getPackKindId().equals(0) || memberWallet.getPackKindId().equals(-3)){
                String[] carNos = memberWallet.getMenberNo().split(",");
                if(carNos.length == 1){
                    JSONObject whiteParams = new JSONObject();
                    whiteParams.put("plate",memberWallet.getMenberNo());
                    DeviceManageUtils deviceManageUtils = new StarnetDeviceUtils();

                    String hql = "from InOutCarRoadInfo where ownCarparkNo = ? and useMark >= 0";
                    List<InOutCarRoadInfo> carRoadInfos = (List<InOutCarRoadInfo>)hibernateBaseDao.queryForList(hql,memberWallet.getCarPark());
                    for(InOutCarRoadInfo carRoadInfo : carRoadInfos){
                        hql = "from DeviceInfo where ownCarRoad = ? and deviceType = '0' and useMark >= 0";
                        List<DeviceInfo> deviceInfos = (List<DeviceInfo>)hibernateBaseDao.queryForList(hql,carRoadInfo.getCarRoadId());
                        for(DeviceInfo deviceInfo : deviceInfos){
                            deviceManageUtils.delWhiteMember(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd(),whiteParams);
                        }
                    }
                }
            }
            logOperationService.addOperatorLog(LogEnum.operateDriver,"删除车牌：" + memberWallet.getMenberNo() + "的套餐信息",ip,null,null);
            uploadDataToCloudService.uploadMemberWalletInfoToServer(memberWallet,null,null);
        } catch (NumberFormatException e) {
            LOGGER.error("删除白名单信息失败");
        }
    }

    @Override
    public List editOwner(String id) {

        String sql=" SELECT\n" +
                "\t t1.id,t1.menber_type_id ,t4.carpark_id,t1.menber_no,t1.parking_lot_id,t1.driver_name,t1.driver_telephone_number,t1.valid_menber_count,t1.surplus_amount,t1.surplus_number,t1.car_park,t2.month_package," +
                "t1.driver_info,t4.carpark_name,t1.pack_kind_id,t1.effective_start_time,t1.effective_end_time\n" +
                " FROM\n" +
                "\tmember_wallet t1\n" +
                " LEFT JOIN member_kind t2 ON t1.menber_type_id = t2.id\n" +
                " LEFT JOIN carpark_info t4 ON t1.car_park = t4.carpark_id" +
                " WHERE t1.id='"+id+"' AND  t1.use_mark>=0";
        SQLQuery sqlQuery = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        List list = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list;
    }

    @Override
    public boolean isCarNo(Map params) {
        boolean res = false;
        String carNo = (String) params.get("carNo");
        String carParkId = (String) params.get("carParkId");

        String sql1 = "SELECT menber_no FROM member_wallet \n" +
                "WHERE car_park ='" + carParkId +"'";
        SQLQuery sqlQuery1 = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql1);
        List list = sqlQuery1.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        if(list.size()>0)
        {
            for(Object s:list){
                Map s1=(Map)s;
                if(!StringUtils.isBlank((String)s1.get("car_no")))
                {
                    String s2=(String)s1.get("car_no");

                    String[] b = carNo.split(",");

                    for (int i = 0; i < b.length; i++) {
                        if(s2.indexOf(b[i])!=-1){
                            return true;
                        }
                    }

                }

            }
        }

        return res;
    }

    @Override
    public void updateOwner(JSONObject params, AdminUser adminUser,String ip) throws BizException {
        try {
            String id = params.getString("id");
            String carNumber = params.getString("carNumber");
            MemberWallet memberWallet = (MemberWallet)hibernateBaseDao.getById(MemberWallet.class,id);
            String memberKindId = params.getString("memberKindId");
            MemberKind memberKind = (MemberKind)hibernateBaseDao.getById(MemberKind.class,memberKindId);
            if (memberKind.getPackageKind().equals(-3) || memberKind.getPackageKind().equals(0)){
                Timestamp startT = Timestamp.valueOf((String)params.get("startTime") + " 00:00:00");
                Timestamp endT = Timestamp.valueOf((String)params.get("endTime") + " 23:59:59");
                if (startT.getTime() > endT.getTime())
                    throw  new BizException("套餐有效开始时间不得大于有效结束时间");
            }
            memberWallet.setValidMenberCount(carNumber);
            if(memberKind.getPackageKind().equals(memberWallet.getPackKindId())){
                memberWallet.setParkingLotId((String)params.get("parkingLot"));
                memberWallet.setMenberTypeId((String)params.get("memberKindId"));
                memberWallet.setPackKindId(memberKind.getPackageKind());
                memberWallet.setPackChlidKind(memberKind.getPackageChildKind());
                if(memberKind.getPackageKind().equals(1)){
                    memberWallet.setSurplusNumber(Integer.parseInt((String)params.get("surplusNumber")) + memberWallet.getSurplusNumber());
                }else if(memberKind.getPackageKind().equals(2)){
                    memberWallet.setSurplusAmount(Double.parseDouble((String)params.get("surplusAmount")) + memberWallet.getSurplusAmount());
                }else{
                    memberWallet.setEffectiveStartTime(Timestamp.valueOf((String)params.get("startTime") + " 00:00:00"));
                    memberWallet.setEffectiveEndTime(Timestamp.valueOf((String)params.get("endTime") + " 23:59:59"));
                }
            }else{
                memberWallet.setParkingLotId((String)params.get("parkingLot"));
                memberWallet.setMenberTypeId((String)params.get("memberKindId"));
                memberWallet.setPackKindId(memberKind.getPackageKind());
                memberWallet.setPackChlidKind(memberKind.getPackageChildKind());
                memberWallet.setSurplusAmount(Double.parseDouble((String)params.get("surplusAmount")));
                memberWallet.setSurplusNumber(Integer.parseInt((String)params.get("surplusNumber")));
                memberWallet.setEffectiveStartTime(Timestamp.valueOf((String)params.get("startTime") + " 00:00:00"));
                memberWallet.setEffectiveEndTime(Timestamp.valueOf((String)params.get("endTime") + " 23:59:59"));
            }
            memberWallet.setDriverName(params.getString("driverName"));
            memberWallet.setDriverInfo(params.getString("driverInfo"));
            memberWallet.setDriverTelephoneNumber(params.getString("driverTelephoneNumber"));

            String[] carNos = (memberWallet.getMenberNo()).split(",");
            memberWallet.setSynchronizeIpcList(null);
            if(memberWallet.getPackKindId().equals(0) || memberKind.getPackageKind().equals(-3)){
                if(carNos.length == 1){
                    JSONObject whiteParams = new JSONObject();
                    whiteParams.put("oldplate",memberWallet.getMenberNo());
                    whiteParams.put("GroupId",0);
                    whiteParams.put("owner",memberWallet.getDriverName());
                    whiteParams.put("plate",memberWallet.getMenberNo());
                    whiteParams.put("card",memberWallet.getDriverTelephoneNumber());
                    whiteParams.put("start",CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",memberWallet.getEffectiveStartTime()));
                    whiteParams.put("end",CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",memberWallet.getEffectiveEndTime()));
                    DeviceManageUtils deviceManageUtils = new StarnetDeviceUtils();

                    String hql = "from InOutCarRoadInfo where ownCarparkNo = ? and useMark >= 0";
                    List<InOutCarRoadInfo> carRoadInfos = (List<InOutCarRoadInfo>)hibernateBaseDao.queryForList(hql,memberWallet.getCarPark());
                    for(InOutCarRoadInfo carRoadInfo : carRoadInfos){
                        hql = "from DeviceInfo where ownCarRoad = ? and deviceType = '0' and useMark >= 0";
                        List<DeviceInfo> deviceInfos = (List<DeviceInfo>)hibernateBaseDao.queryForList(hql,carRoadInfo.getCarRoadId());
                        for(DeviceInfo deviceInfo : deviceInfos){
                            deviceManageUtils.updateWhiteMember(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd(),whiteParams);
                        }
                    }
                    memberWallet.setSynchronizeIpcList("WhiteList");
                }
            }

            memberWallet.setUseMark(1);
            hibernateBaseDao.update(memberWallet);

            OrderPrechargeRecord orderPrechargeRecord = new OrderPrechargeRecord();
            OrderTransaction orderTransaction = new OrderTransaction();
            if(Double.parseDouble((String)params.get("surplusAmount")) > 0){
                orderPrechargeRecord.setOrderPrechargeCarno(memberWallet.getMenberNo());
                orderPrechargeRecord.setOrderPrechargeCarpark(memberWallet.getCarPark());
                CarparkInfo carparkInfo = (CarparkInfo)hibernateBaseDao.getById(CarparkInfo.class,memberWallet.getCarPark());
                orderPrechargeRecord.setOrderPrechargeCarparkName(carparkInfo.getCarparkName());
                orderPrechargeRecord.setOrderPrechargeTime(CommonUtils.getTimestamp());
                orderPrechargeRecord.setChargeMemberKindId(memberKind.getId());
                orderPrechargeRecord.setChargeMemberKindName(memberKind.getKindName());
                orderPrechargeRecord.setOrderPrechargeOperatorId(adminUser.getId());
                orderPrechargeRecord.setOrderPrechargeOperatorName(adminUser.getTrueName());
                orderPrechargeRecord.setOrderPrechargeReceivableAmount(Double.parseDouble((String)params.get("surplusAmount")));
                orderPrechargeRecord.setOrderPrechargeActualAmount(Double.parseDouble((String)params.get("surplusAmount")));
                orderPrechargeRecord.setOrderPrechargeFreeAmount(0D);
                orderPrechargeRecord.setAddTime(CommonUtils.getTimestamp());
                hibernateBaseDao.save(orderPrechargeRecord);

                orderTransaction.setOrderId(orderPrechargeRecord.getOrderPrechargeId());
                orderTransaction.setPayType(payTypeEnum.B);
                orderTransaction.setPayTime(CommonUtils.getTimestamp());
                orderTransaction.setPayStatus(payStatusEnum.HAS_PAID);
                //orderTransaction.setPayTypeName(payTypeEnum.CASH_PAY.getDesc());
                orderTransaction.setTotalFee(new BigDecimal((String)params.get("surplusAmount")));
                orderTransaction.setRealFee(new BigDecimal((String)params.get("surplusAmount")));
                orderTransaction.setDiscountFee(new BigDecimal(0));
                orderTransaction.setDiscountType("");
                orderTransaction.setAddTime(CommonUtils.getTimestamp());
                orderTransaction.setUpdateTime(CommonUtils.getTimestamp());
                hibernateBaseDao.save(orderTransaction);
            }else {
                orderPrechargeRecord = null;
                orderTransaction = null;
            }
            uploadDataToCloudService.uploadMemberWalletInfoToServer(memberWallet,orderPrechargeRecord,orderTransaction);
            logOperationService.addOperatorLog(LogEnum.operateDriver,"修改车牌：" + memberWallet.getMenberNo() + "的套餐信息",ip,null,adminUser);
        } catch (Exception e) {
            LOGGER.error("更新白名单信息失败");
        }
    }

    @Override
    public List<Map<String, Object>> getWhiteListExport(String carNo, String chargeTypeName, String driverName, String driverTelephoneNumber, String driverInfo, String carparkId, String depId, String packageKind) throws BizException {
        List<Map<String, Object>> ret = new ArrayList<>();
        try {
            String sql = "SELECT\n" +
                    "t1.menber_no carNo,\n" +
                    "t1.id,\n" +
                    "t1.car_park carparkId,\n" +
                    "t1.valid_menber_count validCount,\n" +
                    "CASE\n" +
                    "WHEN t1.pack_kind_id = 0 or t1.pack_kind_id = -3 THEN\n" +
                    "date_format(t1.effective_start_time ,'%Y-%m-%d')  \n" +
                    "\n" +
                    "else ''\n" +
                    "END AS starTime,\n" +
                    "CASE\n" +
                    "WHEN t1.pack_kind_id = 0 or t1.pack_kind_id = -3 THEN\n" +
                    " date_format(t1.effective_end_time ,'%Y-%m-%d') \n" +
                    "\n" +
                    "else ''\n" +
                    "END AS endTime,\n" +
                    "\n" +
                    "CASE\n" +
                    "WHEN t1.pack_kind_id = 1 THEN\n" +
                    "t1.surplus_number  \n" +
                    "\n" +
                    "else ''\n" +
                    "END AS surplusNumber,\n" +
                    "\n" +
                    "CASE\n" +
                    "WHEN t1.pack_kind_id = 2 THEN\n" +
                    "t1.surplus_amount  \n" +
                    "\n" +
                    "else ''\n" +
                    "END AS surplusAmount,\n" +
                    "\n" +
                    " t1.driver_name driverName,\n" +
                    " t1.driver_telephone_number driverTelephoneNumber,\n" +
                    " t1.driver_info driverInfo,\n" +
                    " t1.department_id depId,\n" +
                    " t3.kind_name chargeTypeName,\n" +
                    " t4.carpark_name carparkName,\n" +
                    " t1.parking_lot_id parkingLot,\n" +
                    " t1.valid_menber_count menberCount\n" +
                    "FROM\n" +
                    "member_wallet t1\n" +
                    "LEFT JOIN member_kind t3 ON t1.menber_type_id = t3.id\n" +
                    "LEFT JOIN carpark_info t4 ON t1.car_park = t4.carpark_id\n" +
                    "WHERE\n" +
                    "t1.use_mark >= 0\n" +
                    "AND (\n" +
                    "t1.pack_kind_id = '-3'\n" +
                    "OR t1.pack_kind_id = '0'\n" +
                    "OR t1.pack_kind_id = '1'\n" +
                    "OR t1.pack_kind_id = '2'\n" +
                    ")\n";


            sql = putTogetherInSql(sql, "t1.car_park", "carparkId", carparkId);
            sql = putTogetherEqualSql(sql, "t3.kind_name", "chargeTypeName", chargeTypeName);
            sql = putTogetherLikeSql(sql, "t1.menber_no", "carNo", carNo);
            sql = putTogetherLikeSql(sql, "t1.driver_name", "driverName", driverName);
            sql = putTogetherLikeSql(sql, "t1.driver_telephone_number", "driverTelephoneNumber", driverTelephoneNumber);
            sql = putTogetherLikeSql(sql, "t1.driver_info", "driverInfo", driverInfo);
            sql = putTogetherInSql(sql, "t1.department_id", "depId", depId);
            sql = putTogetherInSql(sql, "t1.pack_kind_id", "packageKind", packageKind);
            sql = sql + " order by t1.car_park ,t1.menber_type_id ";

            SQLQuery sqlQuery = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
            setParameterToQuery(sqlQuery, "carparkId", carparkId);
            setParameterToQueryByLike(sqlQuery, "carNo", carNo);
            setParameterToQuery(sqlQuery, "chargeTypeName", chargeTypeName);
            setParameterToQueryByLike(sqlQuery, "driverName", driverName);
            setParameterToQueryByLike(sqlQuery, "driverTelephoneNumber", driverTelephoneNumber);
            setParameterToQueryByLike(sqlQuery, "driverInfo", driverInfo);
            setParameterToQuery(sqlQuery, "depId", depId);
            setParameterToQueryInt(sqlQuery, "packageKind", packageKind);
            ret = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        } catch (Exception e) {
            LOGGER.error("导出白名单信息失败");
        }
        return ret;
    }

    @Override
    public void saveOwnerList(List<Map<String, Object>> list, AdminUser adminUser) throws BizException {
        Long beginLong = CommonUtils.getTimestamp().getTime();
        int i= 3;
        try{
            String hqlmem = "from MemberWallet where useMark >= 0 and ((packKindId = 0) or (packKindId = 1) or (packKindId = 2) or (packKindId = -3))";
            List<MemberWallet> memberWallets = ( List<MemberWallet>)hibernateBaseDao.queryForList(hqlmem);
            for(MemberWallet memberWallet : memberWallets){
                hibernateBaseDao.deleteById(MemberWallet.class,memberWallet.getId());
            }

            int importCount = 1;

            String hql = "from DepartmentInfo";
            DepartmentInfo departmentInfo = ((List<DepartmentInfo>)hibernateBaseDao.queryForList(hql)).get(0);
            //获取初始车场信息
            String carparkName = ((String) list.get(3).get("value2")).replaceAll("\\s*","");
            String  hql_CarparkInfo = "from CarparkInfo where carparkName  =? and useMark >= 0";
            CarparkInfo carparkInfo = (CarparkInfo)hibernateBaseDao.getUnique(hql_CarparkInfo,carparkName);
            String carparkId = CommonUtils.isEmpty(carparkInfo) ? "" : carparkInfo.getCarparkId();
            //获取初始车道信息
            String road_hql = "from InOutCarRoadInfo where ownCarparkNo = ? and useMark >= 0";
            List<InOutCarRoadInfo> carRoadInfoList = (List<InOutCarRoadInfo>)hibernateBaseDao.queryForList(road_hql,carparkId);
            //获取初始的收费类型信息
            String memberKindName = ((String) list.get(3).get("value6")).replaceAll("\\s*","");
            String  memberKindHql = "from MemberKind where kindName  =? and useMark >= 0";
            MemberKind memberKind = (MemberKind)hibernateBaseDao.getUnique(memberKindHql,memberKindName);
            if(CommonUtils.isEmpty(memberKind)){
                throw new BizException("收费类型不存在，收费名称为：" + memberKind.getKindName());
            }
            String  memberKindId = memberKind.getId();
            //获取附属设备
            String deviceHql = "from DeviceInfo where subIpcId is not null and subIpcId <> '' and deviceType = '0' and useMark >= 0";
            List<DeviceInfo> subDeviceInfoList = (List<DeviceInfo>)hibernateBaseDao.queryForList(deviceHql);

            List<JSONObject> whiteImportList = new ArrayList<>();
            //需要导入的设备列表
            JSONObject whiteParamsList = new JSONObject();
            for (i = 3; i < list.size(); i++) {
                //组合插入会员的数据
                Map<String,Object> params = new HashMap<>();
                String carNo1 = ((String) list.get(i).get("value1")).replaceAll("\\s*","");
                String carparkNameAfter = ((String) list.get(i).get("value2")).replaceAll("\\s*","");
                //车场发生变化
                if (!carparkNameAfter.equals(carparkName)){
                    //之前的车场要下发
                    System.out.println("新车场：" + carparkNameAfter + " 老车场：" + carparkName);
                    if (whiteParamsList != null){
                        for(InOutCarRoadInfo carRoadInfo : carRoadInfoList){
                            System.out.println("车道名称：" + carRoadInfo.getCarRoadName());
                            String dev_hql = "from DeviceInfo where ownCarRoad = ? and deviceType = '0' and useMark >= 0";
                            List<DeviceInfo> deviceInfos = (List<DeviceInfo>)hibernateBaseDao.queryForList(dev_hql,carRoadInfo.getCarRoadId());
                            for(DeviceInfo deviceInfo : deviceInfos){
                                JSONObject whiteImportParams = new JSONObject();
                                whiteImportParams.put("deviceId",deviceInfo.getDeviceId());
                                whiteImportParams.put("ip",deviceInfo.getDeviceIp());
                                whiteImportParams.put("port",deviceInfo.getDevicePort());
                                whiteImportParams.put("username",deviceInfo.getDeviceUsername());
                                whiteImportParams.put("password",deviceInfo.getDevicePwd());
                                whiteImportParams.put("count",importCount);
                                whiteImportParams.put("whiteParamsList",whiteParamsList);
                                whiteImportList.add(whiteImportParams);
                            }
                        }
                    }
                    whiteParamsList = new JSONObject();
                    importCount = 1;

                    carparkName = carparkNameAfter;
                    carparkInfo = (CarparkInfo)hibernateBaseDao.getUnique(hql_CarparkInfo,carparkName);
                    String carparkIdAfter = CommonUtils.isEmpty(carparkInfo) ? "" : carparkInfo.getCarparkId();
                    carparkId = carparkIdAfter;
                    carRoadInfoList = (List<InOutCarRoadInfo>)hibernateBaseDao.queryForList(road_hql,carparkId);
                }

                String memberKindNameAfter = ((String) list.get(i).get("value6")).replaceAll("\\s*","");
                if (!memberKindNameAfter.equals(memberKindName)){
                    memberKind = (MemberKind)hibernateBaseDao.getUnique(memberKindHql,memberKindNameAfter);
                    if(CommonUtils.isEmpty(memberKind)){
                        throw new BizException("收费类型不存在，收费名称为：" + memberKind.getKindName());
                    }
                    memberKindId = memberKind.getId();
                    memberKindName = memberKindNameAfter;
                }

                String driverName = ((String) list.get(i).get("value3")).replaceAll("\\s*","");

                String driverTelephoneNumber = null;
                String nc = ((String) list.get(i).get("value4")).replaceAll("\\s*","");
                if ((nc).indexOf("E")!=-1 || (nc).indexOf("e")!=-1 || (nc).indexOf("+")!=-1) {
                    BigDecimal bd = new BigDecimal(nc);
                    driverTelephoneNumber = bd.toPlainString();
                }else {
                    driverTelephoneNumber =((String) list.get(i).get("value4")).replaceAll("\\s*","");
                }
                String driverInfo=((String) list.get(i).get("value5")).replaceAll("\\s*","");


                String startTime="";
                String endTime="";
                if(!CommonUtils.isEmpty(list.get(i).get("value7"))){
                    if( list.get(i).get("value7").toString().contains("-")){
                        startTime=list.get(i).get("value7").toString() ;
                        endTime=list.get(i).get("value8").toString();
                    }else {
                        String startTime_=((String) list.get(i).get("value7")).replaceAll("\\s*","").replaceAll("\\-","/");
                        int startTime_number=(int)(Float.parseFloat(startTime_));
                        startTime= PoiExcelUtil.excelDateTodate(startTime_number);
                        if(startTime.contains(".")){
                            startTime= startTime.split("\\.")[0];
                        }
                        String endTime_=((String) list.get(i).get("value8")).replaceAll("\\s*","").replaceAll("\\-","/");
                        if(endTime_.contains(".")){
                            endTime_= endTime_.split("\\.")[0];
                        }
                        int endTime_number= (int)(Float.parseFloat(endTime_));
                        endTime=PoiExcelUtil.excelDateTodate(endTime_number);
                    }
                }


                String  surplusNumber=((String) list.get(i).get("value9")).replaceAll("\\s*","");
                String  surplusAmount=((String) list.get(i).get("value10")).replaceAll("\\s*","");
                String parkingLot=((String) list.get(i).get("value11")).replaceAll("\\s*","");
                String validCountNum=((String) list.get(i).get("value12")).replaceAll("\\s*","");

                params.put("carNo",carNo1);
                params.put("carParkName",carparkName);
                params.put("driverName",driverName);
                params.put("driverTelephoneNumber",driverTelephoneNumber);
                params.put("parkingLot",parkingLot);

                params.put("startTime",startTime);
                params.put("endTime",endTime);
                params.put("surplusNumber",surplusNumber);
                params.put("surplusAmount",surplusAmount);
                params.put("driverInfo",driverInfo);
                params.put("parkingLot",parkingLot);
                params.put("memberKindId",memberKindId);
                params.put("carParkId",carparkId);

                //入数据白名单
                String[] carNos = ((String)params.get("carNo")).split(",");
                for(String carNo : carNos){
                    hql = "select count(1) from MemberWallet where menberNo like ? and carPark = ? and useMark >= 0";
                    Long count = (Long)hibernateBaseDao.getUnique(hql,"%" + carNo + "%",params.get("carParkId"));
                    if(count > 0){
                        throw new BizException("套餐["+carNo+"]信息已存在");
                    }
                }

                MemberWallet memberWallet = new MemberWallet();
                memberWallet.setMenberNo((String)params.get("carNo"));
                memberWallet.setDriverName((String) params.get("driverName"));
                memberWallet.setDriverInfo((String) params.get("driverInfo"));
                memberWallet.setDriverTelephoneNumber((String) params.get("driverTelephoneNumber"));
                memberWallet.setParkingLotId((String)params.get("parkingLot"));
                memberWallet.setCarPark((String)params.get("carParkId"));
                memberWallet.setMenberTypeId((String)params.get("memberKindId"));
                memberWallet.setPackKindId(memberKind.getPackageKind());
                memberWallet.setPackChlidKind(memberKind.getPackageChildKind());
                if(!CommonUtils.isEmpty(params.get("surplusAmount"))){
                    memberWallet.setSurplusAmount(Double.parseDouble((String)params.get("surplusAmount")));
                }
                if(!CommonUtils.isEmpty(params.get("surplusNumber"))){
                    memberWallet.setSurplusNumber(Integer.parseInt((String)params.get("surplusNumber")));
                }

                if(!CommonUtils.isEmpty(params.get("startTime"))){
                    memberWallet.setEffectiveStartTime(Timestamp.valueOf(params.get("startTime")+" 00:00:00"));
                }
                if(!CommonUtils.isEmpty(params.get("endTime"))){
                    memberWallet.setEffectiveEndTime(Timestamp.valueOf(params.get("endTime")+" 23:59:00"));
                }



                memberWallet.setAddTime(CommonUtils.getTimestamp());
                validCountNum = CommonUtils.isEmpty(validCountNum) ? carNos.length + "" : validCountNum;
                memberWallet.setValidMenberCount(validCountNum);
                memberWallet.setDepartmentId(departmentInfo.getDepId());
                memberWallet.setUseMark(0);

                if((memberWallet.getPackKindId().equals(0) || memberWallet.getPackKindId().equals(-3)) && (carNos.length == 1)){
                    whiteParamsList.put("T_GroupId" + importCount,0);
                    whiteParamsList.put("T_owner" + importCount,memberWallet.getDriverName());
                    whiteParamsList.put("T_plate" + importCount,memberWallet.getMenberNo());
                    whiteParamsList.put("T_card" + importCount,memberWallet.getDriverTelephoneNumber());
                    whiteParamsList.put("T_start" + importCount,CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",memberWallet.getEffectiveStartTime()));
                    whiteParamsList.put("T_end" + importCount,CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",memberWallet.getEffectiveEndTime()));
                    importCount++;
                    memberWallet.setSynchronizeIpcList("WhiteList");
                }
                hibernateBaseDao.save(memberWallet);
                if (i == (list.size() - 1)){
                    for(InOutCarRoadInfo carRoadInfo : carRoadInfoList){
                        System.out.println("车道名称：" + carRoadInfo.getCarRoadName());
                        String dev_hql = "from DeviceInfo where ownCarRoad = ? and deviceType = '0' and useMark >= 0";
                        List<DeviceInfo> deviceInfos = (List<DeviceInfo>)hibernateBaseDao.queryForList(dev_hql,carRoadInfo.getCarRoadId());
                        for(DeviceInfo deviceInfo : deviceInfos){
                            JSONObject whiteImportParams = new JSONObject();
                            whiteImportParams.put("deviceId",deviceInfo.getDeviceId());
                            whiteImportParams.put("ip",deviceInfo.getDeviceIp());
                            whiteImportParams.put("port",deviceInfo.getDevicePort());
                            whiteImportParams.put("username",deviceInfo.getDeviceUsername());
                            whiteImportParams.put("password",deviceInfo.getDevicePwd());
                            whiteImportParams.put("count",importCount);
                            whiteImportParams.put("whiteParamsList",whiteParamsList);
                            whiteImportList.add(whiteImportParams);
                        }
                    }
                }

            }

            if (whiteImportList != null && whiteImportList.size() > 0){
                DeviceManageUtils deviceManageUtils = new StarnetDeviceUtils();
                for (JSONObject jsonObject : whiteImportList){
                    boolean isSubDevice = false;
                    String deviceId = jsonObject.getString("deviceId");
                    if (subDeviceInfoList != null && subDeviceInfoList.size() > 0){
                        for (DeviceInfo subDeviceInfo : subDeviceInfoList){
                            if (subDeviceInfo.getSubIpcId().equals(deviceId)){
                                isSubDevice = true;
                                break;
                            }
                        }
                    }

                    if (isSubDevice){
                        continue;
                    }
                    String deviceIp = jsonObject.getString("ip");
                    Integer devicePort = Integer.valueOf(jsonObject.getString("port"));
                    String username = jsonObject.getString("username");
                    String password = jsonObject.getString("password");
                    Integer totalCount = Integer.valueOf(jsonObject.getString("count"));
                    System.out.println(deviceIp + "：" + totalCount);
                    JSONObject whiteParams = jsonObject.getJSONObject("whiteParamsList");
                    deviceManageUtils.addWhiteMemberList(deviceIp,devicePort,username,password,whiteParams,totalCount);
                }
                Long endLong = CommonUtils.getTimestamp().getTime();
                System.out.println("时间间隔为：" + ((endLong - beginLong)/1000));
            }
        }catch (TransactionException e){
            if ("on".equals(AppInfo.isConnectCloud)) {
                UploadWhiteListThread.setSuspend(false);
            }
            throw new BizException("导入白名单超时");
        }catch (Exception e){
            if ("on".equals(AppInfo.isConnectCloud)) {
                UploadWhiteListThread.setSuspend(false);
            }
            LOGGER.error("导入白名单第[" + (i + 1) + "]行错误，错误原因为：" + e.getMessage());
            throw new BizException("导入白名单第[" + (i + 1) + "]行错误，错误原因为：" + e.getMessage());
        }

    }


    //私有方法
    void setParameterToQuery(SQLQuery sqlQuery, String paramName, String paramValue){
        if(!CommonUtils.isEmpty(sqlQuery)){
            if (!("").equals(paramValue) && !CommonUtils.isEmpty(paramValue) && !("null").equals(paramValue)) {
                sqlQuery.setParameter(paramName, paramValue);
            }
        }
    }

    void setParameterToQueryInt(SQLQuery sqlQuery, String paramName, String paramValue){
        if(!CommonUtils.isEmpty(sqlQuery)){
            if (!("").equals(paramValue) && !CommonUtils.isEmpty(paramValue) && !("null").equals(paramValue)) {
                sqlQuery.setInteger(paramName, Integer.parseInt(paramValue));
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
