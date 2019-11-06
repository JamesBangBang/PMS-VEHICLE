package com.starnetsecurity.parkClientServer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.commonService.util.DeviceManageUtils;
import com.starnetsecurity.commonService.util.StarnetDeviceUtils;
import com.starnetsecurity.parkClientServer.clientEnum.LogEnum;
import com.starnetsecurity.parkClientServer.entity.*;
import com.starnetsecurity.parkClientServer.entityEnum.payStatusEnum;
import com.starnetsecurity.parkClientServer.init.AppInfo;
import com.starnetsecurity.parkClientServer.service.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import org.apache.shiro.codec.Base64;

/**
 * Created by 宏炜 on 2017-07-18.
 */
@Service
public class OrderParkServiceImpl implements OrderParkService {

    @Autowired
    HibernateBaseDao baseDao;

    @Autowired
    CalculateFeeNewService calculateFeeNewService;

    @Autowired
    LogOperationService logOperationService;

    @Autowired
    ClientBizService clientBizService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientBizServiceImpl.class);

    @Override
    public String addUploadParkImage(String imagePath) throws BizException {
        String serverImage = "";
        if(!StringUtils.isBlank(imagePath)){
            String hql = "from UpImageFile where filePath = ?";
            UpImageFile file = (UpImageFile)baseDao.getUnique(hql,imagePath);
            if(!CommonUtils.isEmpty(file)){
                return "http://" + AppInfo.qiniuDomain + "/" + file.getUpFileName();
            }
            UpImageFile upImageFile = new UpImageFile();
            upImageFile.setCreateTime(CommonUtils.getTimestamp());
            upImageFile.setFilePath(imagePath);
            Byte isUp = 0;
            upImageFile.setIsUpload(isUp);
            String fileName = UUID.randomUUID().toString().replaceAll("-","") + ".jpg";
            upImageFile.setUpFileName(fileName);
            baseDao.save(upImageFile);
            serverImage = "http://" + AppInfo.qiniuDomain + "/" + fileName;
        }
        return serverImage;
    }

    @Override
    public Map getManualRoadInfo() {
        JSONObject param = new JSONObject();
        String roadId = "";
        String roadName = "";
        String roadType = "";
        String departmentId = getDepartmentId();
        String  hql = "SELECT b.car_road_id, b.car_road_name,b.car_road_type " +
                "FROM in_out_car_road_info b LEFT JOIN carpark_info a " +
                "ON a.carpark_id = b.own_carpark_no " +
                "AND a.use_mark >= 0 " +
                "WHERE " +
                "b.use_mark >= 0 AND b.own_carpark_no <> ''";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
        List<Map<String,Object>> roadList = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        for (Map map : roadList){
            if (!CommonUtils.isEmpty(map)) {
                roadId = roadId + map.get("car_road_id").toString() + "#";
                roadName = roadName + Base64.encodeToString((map.get("car_road_name").toString() + "#").getBytes());
                roadType = roadType + map.get("car_road_type").toString() + "#";
            }
        }
        param.put("roadId",roadId);
        param.put("roadName",roadName);
        param.put("roadType",roadType);
        return param;
    }

    @Override
    public Map getShiftInfo(String operatorUserName, String loginTime, String token) {
        JSONObject param = new JSONObject();
        String hql = "from Operator where operatorUserName = ? AND useMark >= 0";
        Operator operator = (Operator)baseDao.getUnique(hql,operatorUserName);
        param.put("operatorName",Base64.encodeToString(operator.getOperatorName().getBytes()));
        //获取全部用户登录名称
        String operatorLoginName = "";
        hql = "SELECT operator_user_name FROM Operator where use_mark >= 0";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
        List<Map<String,Object>> OperatorList = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        for (Map map : OperatorList){
            operatorLoginName = operatorLoginName + map.get("operator_user_name").toString()+ "#";
        }
        param.put("operatorLoginName",operatorLoginName);
        hql = "SELECT SUM(charge_actual_amount) AS chargeActualAmount FROM order_inout_record " +
              "WHERE charge_operator_id = '" + operator.getOperatorId() + "' " +
              "AND out_time >= '" +  loginTime + "' " +
              "AND out_time <= '" + CommonUtils.getTimestamp().toString() + "'";
        sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
        List<Map<String,Object>> chargeRecord = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        String chargeActualAmount = "0";
        if (chargeRecord.size() > 0) {
            for (Map map : chargeRecord) {
                chargeActualAmount = CommonUtils.isEmpty(map.get("chargeActualAmount")) ? "" : String.valueOf(map.get("chargeActualAmount"));
                break;
            }
        }
        if (CommonUtils.isEmpty(chargeActualAmount))
            chargeActualAmount = "0";
        param.put("chargeActualAmount",chargeActualAmount);

        return param;
    }

    @Override
    public Map getMemberWalletInfo(String carno) {
        JSONObject param = new JSONObject();
        String memberNo = "";
        String carparkName = "";
        String kindName = "";
        String memo = "";
        String driverName = "";
        String driverTelephoneNumber = "";
        String driverInfo = "";
        String departmentId = getDepartmentId();
        String hql = "SELECT a.menber_no,a.pack_kind_id,a.driver_name,a.driver_telephone_number,a.driver_info" +
                ",a.effective_start_time,a.effective_end_time,a.surplus_amount,a.surplus_number," +
                "b.kind_name,c.carpark_name\n" +
                "FROM member_wallet a\n" +
                "LEFT JOIN member_kind b ON\n" +
                "a.menber_type_id = b.id AND b.use_mark >= 0\n" +
                "LEFT JOIN carpark_info c ON\n" +
                "a.car_park = c.carpark_id AND c.department_id = '" + departmentId + "' " +
                "WHERE a.menber_no = '" + carno + "' AND a.use_mark >= 0";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
        List<Map<String, Object>> memberList = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        for (Map map : memberList) {
            memberNo = (String)map.get("menber_no");
            driverName = Base64.encodeToString((map.get("driver_name") + "").getBytes());
            driverTelephoneNumber = map.get("driver_telephone_number") + "";
            driverInfo = Base64.encodeToString((map.get("driver_info") + "").getBytes());
            kindName = kindName + (String)map.get("kind_name") + "#";
            carparkName = carparkName + Base64.encodeToString(map.get("carpark_name").toString().getBytes()) + "#";
            if (map.get("pack_kind_id").toString().equals("2"))
                memo = memo + Base64.encodeToString(("剩余金额：" + map.get("surplus_amount").toString()).getBytes()) + "#";
            else if (map.get("pack_kind_id").toString().equals("1"))
                memo = memo + Base64.encodeToString(("剩余次数：" + map.get("surplus_number").toString()).getBytes()) + "#";
            else
                memo = memo + Base64.encodeToString(( "有效时间：" + map.get("effective_start_time").toString() + "至" + map.get("effective_end_time").toString()).getBytes()) + "#";
        }
        if (CommonUtils.isEmpty(memberNo))
            param.put("isMember", "0");
        else {
            param.put("isMember", "1");
            param.put("memberNo", Base64.encodeToString(memberNo.getBytes()));
            param.put("carparkName", carparkName);
            param.put("kindName", kindName);
            param.put("memo", memo);
            param.put("driverName",driverName);
            param.put("driverTelephoneNumber",driverTelephoneNumber);
            param.put("driverInfo",driverInfo);
        }

        //加载出入记录
        String inOutTime = "";
        String inOutFlag = "";
        String inOutRoadName = "";
        hql = "SELECT inout_time,inout_flag,car_road_name FROM inout_record_info WHERE car_no = '" + carno + "' AND inout_status <=1 ORDER BY add_time DESC limit 10";
        sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
        List<Map<String, Object>> inoutList = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        if (inoutList.size() > 0){
            param.put("hasInoutList","1");      //代表有
            for (Map map : inoutList){
                inOutFlag = inOutFlag + map.get("inout_flag").toString() + "#";
                inOutTime = inOutTime + map.get("inout_time").toString() + "#";
                inOutRoadName = inOutRoadName + Base64.encodeToString(map.get("car_road_name").toString().getBytes()) + "#";
            }
            param.put("inOutFlag",inOutFlag);
            param.put("inOutTime",inOutTime);
            param.put("inOutRoadName",inOutRoadName);
        }else {
            param.put("hasInoutList","0");
        }

        return param;

        }

    @Override
    public Map getDeviceInfo() {
        JSONObject param = new JSONObject();
        String deviceId = "";
        String deviceName = "";
        String deviceIP = "";
        String deviceUserName = "";
        String devicePort = "";
        String devicePwd = "";
        String deviceRtspPort = "";
        String hql = "select * FROM " +
                     "(SELECT device_id,device_name,device_port,device_ip, device_username, device_pwd, rtsp_port,own_car_road FROM device_info where device_type=0 and use_mark >= 0) a "+
                     "LEFT OUTER JOIN " +
                     "(select car_road_id, car_road_name from in_out_car_road_info where use_mark >= 0) b " +
                     "ON a.own_car_road = b.car_road_id order by device_name";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
        List<Map<String,Object>> devList = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        for (Map map : devList){
            deviceId = deviceId + map.get("device_id") + "#";
            deviceName = deviceName + Base64.encodeToString((map.get("device_name") + "#").getBytes());
            deviceIP = deviceIP + map.get("device_ip") + "#";
            deviceUserName = deviceUserName + map.get("device_username") + "#";
            devicePwd = devicePwd + map.get("device_pwd") + "#";
            devicePort = devicePort + map.get("device_port") + "#";
            deviceRtspPort = deviceRtspPort + map.get("rtsp_port") + "#";
        }
        param.put("deviceId",deviceId);
        param.put("deviceName",deviceName);
        param.put("deviceIP",deviceIP);
        param.put("deviceUserName",deviceUserName);
        param.put("devicePwd",devicePwd);
        param.put("devicePort",devicePort);
        param.put("deviceRtspPort",deviceRtspPort);
        return  param;
    }

    @Override
    public Map getParkingInfo(String ips,String operatorUserName,String loginTime) {
        JSONObject res = new JSONObject();
        PostComputerManage postComputerManage = null;
        String[] ipArray = ips.split(",");
        for(String ip : ipArray){
            String hql = "from PostComputerManage where postComputerIp = ? and useMark >= 0";
            postComputerManage = (PostComputerManage)baseDao.getUnique(hql,ip);
            if(!CommonUtils.isEmpty(postComputerManage)){
                break;
            }
        }
        if(CommonUtils.isEmpty(postComputerManage)){
            res.put("finalRes","-1");
            return res;
        }

        String hql = "from InOutCarRoadInfo where manageComputerId = ? and useMark >=0";
        InOutCarRoadInfo inOutCarRoadInfo = new InOutCarRoadInfo();
        List<InOutCarRoadInfo> roadInfoList = (List<InOutCarRoadInfo>)baseDao.queryForList(hql,postComputerManage.getPostComputerId());
        if (CommonUtils.isEmpty(roadInfoList)){
            res.put("finalRes","-2");
            return res;
        }else{
            inOutCarRoadInfo = roadInfoList.get(0);
        }
        String carparkId = inOutCarRoadInfo.getOwnCarparkNo();
        hql = "from Operator where operatorUserName = ? and useMark >= 0";
        Operator operator = new Operator();
        operator = (Operator)baseDao.getUnique(hql,operatorUserName);
        String operatorId = operator.getOperatorId();
        CarparkInfo carparkInfo = (CarparkInfo) baseDao.getById(CarparkInfo.class,carparkId);
        res.put("carparkName",Base64.encodeToString(carparkInfo.getCarparkName().getBytes()));
        res.put("totalCarSpace",carparkInfo.getTotalCarSpace().toString());
        res.put("availableCarSpace",carparkInfo.getAvailableCarSpace().toString());
        if (AppInfo.isUseAllinpay.equals("1")) {
            hql = "from InoutRecordInfo where carparkId = ? and inoutTime <= ? AND inoutStatus = 1 order by inoutTime desc";
        }else {
            hql = "from InoutRecordInfo where carparkId = ? and inoutTime <= ? AND inoutStatus <= 1 order by inoutTime desc";
        }
        Integer inOutCnt = baseDao.pageQuery(hql,1,10,carparkId,CommonUtils.getTimestamp()).size();
        String inOutCarno = "";
        String inOutTime = "";
        String inOutFlag = "";
        String chargeInfo = "";
        List<InoutRecordInfo> listRecord = (List<InoutRecordInfo>)baseDao.pageQuery(hql,1,10,carparkId,CommonUtils.getTimestamp());
        for (Integer i = 0;i<= (inOutCnt-1);i++){
            InoutRecordInfo inoutRecordInfo = listRecord.get(i);
            if (!CommonUtils.isEmpty(inOutCarRoadInfo)){
                inOutCarno = inOutCarno + Base64.encodeToString(inoutRecordInfo.getCarNo().getBytes()) + "#";
                inOutTime = inOutTime + CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",inoutRecordInfo.getInoutTime()) + "#";
                if (inoutRecordInfo.getInoutFlag()==0){
                    inOutFlag = inOutFlag + "0" + "#";
                    chargeInfo = chargeInfo + "0" + "#";
                }else {
                    inOutFlag = inOutFlag + "1" + "#";
                    hql = "from OrderInoutRecord where outRecordId = ?";
                    OrderInoutRecord orderInoutRecord = (OrderInoutRecord)baseDao.getUnique(hql,inoutRecordInfo.getInoutRecordId());
                    if (!CommonUtils.isEmpty(orderInoutRecord)) {
                        if (!CommonUtils.isEmpty(orderInoutRecord.getChargeReceivableAmount()))
                            chargeInfo = chargeInfo + orderInoutRecord.getChargeReceivableAmount().toString() + "#";
                        else
                            chargeInfo = chargeInfo + "0" + "#";
                    }else
                        chargeInfo = chargeInfo + "0" + "#";
                }

            }
        }
        hql = "SELECT inout_record_id FROM inout_record_info WHERE\n" +
                "post_id = '" + postComputerManage.getPostComputerId() + "' " +
                "and inout_time >= '" + loginTime +  "' " +
                "AND inout_time <= '" + CommonUtils.getTimestamp().toString() + "' " +
                "AND inout_status <= 1 " +
                "AND inout_flag = 0";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
        List<Map<String,Object>> inRecord = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        Integer inRecordCnt = inRecord.size();
        res.put("inRecordCnt",inRecordCnt.toString());
        hql = "SELECT inout_record_id FROM inout_record_info WHERE\n" +
                "post_id = '" + postComputerManage.getPostComputerId() + "' " +
                "and inout_time >= '" + loginTime +  "' " +
                "AND inout_time <= '" + CommonUtils.getTimestamp().toString() + "' " +
                "AND inout_status <= 1 " +
                "AND inout_flag = 1";
        sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
        List<Map<String,Object>> outRecord = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        Integer outRecordCnt = outRecord.size();
        res.put("finalRes","0");
        res.put("outRecordCnt",outRecordCnt.toString());

        res.put("inOutCnt",inOutCnt.toString());
        res.put("inOutCarno",inOutCarno);
        res.put("inOutTime",inOutTime);
        res.put("inOutFlag",inOutFlag);
        res.put("chargeInfo",chargeInfo);
        return res;
    }

    @Override
    public Map controlOpenGate(String deviceIp, String controlMode, String operatorName, String postIp) {
        JSONObject res = new JSONObject();
        String hql = "from DeviceInfo where deviceIp = ? and useMark >= 0";
        DeviceInfo deviceInfo = (DeviceInfo)baseDao.getUnique(hql,deviceIp);
        InOutCarRoadInfo inOutCarRoadInfo = (InOutCarRoadInfo)baseDao.getById(InOutCarRoadInfo.class,deviceInfo.getOwnCarRoad());
        String channelName = CommonUtils.isEmpty(inOutCarRoadInfo) ? "" : inOutCarRoadInfo.getCarRoadName();
        hql = "from Operator where operatorUserName = ? and useMark >= 0";
        Operator operator = (Operator) baseDao.getUnique(hql,operatorName);
        DeviceManageUtils deviceManageUtils = new StarnetDeviceUtils();
        String resume = "";
        if ("open".equals(controlMode)) {
            deviceManageUtils.openRoadGate(deviceIp,Integer.parseInt(deviceInfo.getDevicePort()),
                    deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd());
            logOperationService.addOperatorLog(LogEnum.controlGate,"操作员：" + operatorName + "在客户端手动开闸",postIp,null,null);
            resume = "视频开闸";
            /*bolinkService.upload_liftrod("upload_liftrod","cloud",UUID.randomUUID().toString().replaceAll("-",""),CommonUtils.getTimestamp().getTime(),
                    CommonUtils.getTimestamp().getTime(),0,channelName,operator.getOperatorId(),"","",resume,deviceIp);*/
        }else if ("close".equals(controlMode)){
            deviceManageUtils.closeRoadGate(deviceIp,Integer.parseInt(deviceInfo.getDevicePort()),
                    deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd());
            logOperationService.addOperatorLog(LogEnum.controlGate,"操作员：" + operatorName + "在客户端手动关闸",postIp,null,null);
        }else {
            deviceManageUtils.stopRoadGate(deviceIp,Integer.parseInt(deviceInfo.getDevicePort()),
                    deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd());
            logOperationService.addOperatorLog(LogEnum.controlGate,"操作员：" + operatorName + "在客户端手动停闸",postIp,null,null);
        }
        return res;
    }

    @Override
    public Map getMatchCarnoInfo(String carparkId,String carno,Integer pageNum,Long startTime,Long endTime) throws UnsupportedEncodingException {

        JSONObject res = new JSONObject();
        String hql = "from OrderInoutRecord where carparkId = ? and outRecordId is null and carNo like ? and (inTime BETWEEN ? and ?) order by inTime desc";
        List<OrderInoutRecord> orderInoutRecords = (List<OrderInoutRecord>)baseDao.pageQuery(hql,pageNum,6,carparkId,"%" + carno + "%",new Timestamp(startTime * 1000),new Timestamp(endTime * 1000));

        hql = "select count(1) from OrderInoutRecord where carparkId = ? and outRecordId is null and carNo like ? and (inTime BETWEEN ? and ?)";
        Long total = (Long)baseDao.getUnique(hql,carparkId,"%" + carno + "%",new Timestamp(startTime * 1000),new Timestamp(endTime * 1000));
        String inRecordId = "";
        String carnos = "";
        String inTime = "";
        String inPicName = "";
        for (OrderInoutRecord orderInoutRecord : orderInoutRecords){
            inRecordId = inRecordId + orderInoutRecord.getInRecordId() + "#";
            carnos = carnos + Base64.encodeToString(orderInoutRecord.getCarNo().getBytes()) + "#";
            inTime = inTime + CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",orderInoutRecord.getInTime()) + "#";
            inPicName = inPicName + (StringUtils.isBlank(orderInoutRecord.getInPictureName()) ? Base64.encodeToString("empty".getBytes("GBK")) : Base64.encodeToString(orderInoutRecord.getInPictureName().getBytes("GBK"))) + "#";
        }
        res.put("inRecordId",inRecordId);
        res.put("carno",carnos);
        res.put("inTime",inTime);
        res.put("inPicName",inPicName);
        res.put("total",total);
        return res;
    }

    @Override
    public Map getRematchCarnoInfo(String inRecordId, String outMatchTime,String outCarno,String carnoColor) {
        JSONObject res = new JSONObject();
        String userType = "0";           //默认是外部收费
        String finalRes = "0";
        InoutRecordInfo inoutRecordInfo = (InoutRecordInfo) baseDao.getById(InoutRecordInfo.class,inRecordId);
        String hql = "from MemberWallet where menberNo like ? and carPark = ? AND useMark >= 0";
        MemberWallet memberWallet = (MemberWallet)baseDao.getUnique(hql,"%" + outCarno + "%",inoutRecordInfo.getCarparkId());
        if (!CommonUtils.isEmpty(memberWallet))
            userType = "1";

        Timestamp inTime = inoutRecordInfo.getInoutTime();
        Timestamp outTime = Timestamp.valueOf(outMatchTime.replaceAll("/","-"));
        Long stayTime = 0L;
        stayTime = (outTime.getTime() - inTime.getTime()) / 1000;
        int parkingHour = (int)(stayTime/3600);
        int parkingMinute = (int)((stayTime-parkingHour*3600)/60);

        Double chargeAmount = 0D;  //应收金额
        try{
            chargeAmount = calculateFeeNewService.calculateParkingFeeNew(inoutRecordInfo.getCarNo(), userType, carnoColor,inoutRecordInfo.getCarparkId(), inTime, outTime);
        }catch (Exception e){
            finalRes = "-1";
            LOGGER.error("车牌匹配计费异常",e);
            chargeAmount = 0D;
        }
        res.put("matchCarno",Base64.encodeToString(inoutRecordInfo.getCarNo().getBytes()));
        res.put("matchInTime",CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",inoutRecordInfo.getInoutTime()));
        res.put("matchInRoadId",inoutRecordInfo.getCarRoadId());
        res.put("matchInRoadName",Base64.encodeToString(inoutRecordInfo.getCarRoadName().getBytes()));
        res.put("matchInCameraId",inoutRecordInfo.getCameraId());
        res.put("matchInPicUrl",inoutRecordInfo.getPhotoCapturePicName());
        res.put("stayTime",stayTime);
        res.put("chargeAmount",chargeAmount);

        res.put("finalRes",finalRes);
        return res;
    }

    @Override
    public Map getPrechargeInfo(String carparkId,String outCarno,String outTime) {
        JSONObject res = new JSONObject();
        Timestamp reChargeOutTime = Timestamp.valueOf(outTime.replaceAll("/","-"));
        String finalRes = "0";
        Double preChargeAmount = 0.0;
        OrderInoutRecord orderInoutRecord = clientBizService.GetMatchInfo(outCarno,carparkId,reChargeOutTime);
        if (!CommonUtils.isEmpty(orderInoutRecord)){
            BigDecimal accountSum = new BigDecimal("0");
            String accountHql = "from OrderTransaction where orderId = ? and payStatus = ? order by addTime desc";
            List<OrderTransaction> orderTransactionList = (List<OrderTransaction>)baseDao.queryForList(accountHql,orderInoutRecord.getChargeInfoId(), payStatusEnum.HAS_PAID);
            if (orderTransactionList.size() > 0) {
                //流水表收费总金额
                for (OrderTransaction orderTransaction : orderTransactionList){
                    accountSum = accountSum.add(orderTransaction.getTotalFee());
                }
            }
            //写入提前缴费金额
            if(CommonUtils.isEmpty(accountSum)){
                accountSum = new BigDecimal("0");
            }
            preChargeAmount = accountSum.doubleValue();
            if (preChargeAmount > 0)
                finalRes = "1";
        }
        res.put("finalRes",finalRes);
        res.put("preChargeAmount",String.valueOf(preChargeAmount));
        res.put("outCarno",Base64.encodeToString(outCarno.getBytes()));
        return res;
    }

    private String getDepartmentId(){
        String res = "";
        String hql = "from DepartmentInfo where useMark >= 0";
        DepartmentInfo departmentInfo = (DepartmentInfo)baseDao.getUnique(hql);
        res = departmentInfo.getDepId();
        return res;
    }

}
