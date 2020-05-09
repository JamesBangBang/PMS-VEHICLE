package com.starnetsecurity.parkClientServer.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.util.HttpRequestUtils;
import com.starnetsecurity.commonService.util.DeviceManageUtils;
import com.starnetsecurity.commonService.util.StarnetDeviceUtils;
import com.starnetsecurity.parkClientServer.clientEnum.LogEnum;
import com.starnetsecurity.parkClientServer.entity.*;
import com.starnetsecurity.parkClientServer.entityEnum.*;
import com.starnetsecurity.parkClientServer.init.AppInfo;
import com.starnetsecurity.parkClientServer.service.*;
import com.starnetsecurity.parkClientServer.sockServer.*;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import com.starnetsecurity.parkClientServer.service.ClientBizService;


/**
 * Created by 宏炜 on 2017-12-11.
 */
@Service
public class ClientBizServiceImpl implements ClientBizService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientBizServiceImpl.class);

    @Autowired
    HibernateBaseDao baseDao;

    @Autowired
    DeviceBizService deviceBizService;

    @Autowired
    CalculateFeeNewService calculateFeeNewService;

    @Autowired
    InOutMsgService inOutMsgService;

    @Autowired
    LogOperationService logOperationService;

    @Autowired
    NewOrderParkService newOrderParkService;

    /**
     * @Author chenbinbin
     * @Description 计算车位
     * @Date 9:42 2020/4/29
     * @Param [carparkId, inoutType, carno]
     * @return void
     **/
    private void calculateParkSpace(String carparkId,Integer inoutType,String carno){
        CarparkInfo carparkInfo = (CarparkInfo)baseDao.getById(CarparkInfo.class,carparkId);
        if (!CommonUtils.isEmpty(carparkInfo)){
            if (inoutType.equals(0)){
                Integer availableCarSpace = carparkInfo.getAvailableCarSpace() > 0 ? carparkInfo.getAvailableCarSpace() - 1 : 0;
                if (carparkInfo.getCarparkName().equals("海西1号地库") || carparkInfo.getCarparkName().equals("海西2号地库")){
                    LOGGER.error(carno + "入场真实车位数为：" +  availableCarSpace);
                }
                carparkInfo.setAvailableCarSpace(availableCarSpace);
                carparkInfo.setCarparkNo(carparkInfo.getCarparkNo()-1);             //隐藏值
                baseDao.save(carparkInfo);
            }else {
                if (carparkInfo.getCarparkNo() >= 0) {
                    Integer availableCarSpace = carparkInfo.getAvailableCarSpace() < carparkInfo.getTotalCarSpace() ? carparkInfo.getAvailableCarSpace() + 1 : carparkInfo.getTotalCarSpace();
                    if (carparkInfo.getCarparkName().equals("海西1号地库") || carparkInfo.getCarparkName().equals("海西2号地库")){
                        LOGGER.error(carno + "出场真实车位数为：" +  availableCarSpace);
                    }
                    carparkInfo.setAvailableCarSpace(availableCarSpace);
                }
                Integer realAvailableCarSpace = carparkInfo.getCarparkNo() < carparkInfo.getTotalCarSpace() ? carparkInfo.getCarparkNo() + 1 : carparkInfo.getTotalCarSpace();
                carparkInfo.setCarparkNo(realAvailableCarSpace);
                baseDao.save(carparkInfo);
            }
        }
    }

    @Override
    public JSONObject login(String username, String password, String ips, SocketClient socketClient) throws BizException {
        JSONObject res = null;
        try {
            String hql = "from Operator where operatorUserName = ? and useMark >= 0";
            String errorInfo = "";
            Operator operator = (Operator)baseDao.getUnique(hql,username);
            if(CommonUtils.isEmpty(operator)){
                errorInfo = "用户名不存在";
                throw new BizException(errorInfo);
            }
            String md5Pwd = new SimpleHash("md5", password, null, 1).toString();
            if(!operator.getOperatorUserPwd().equals(md5Pwd)){
                errorInfo = "用户密码错误";
                throw new BizException(errorInfo);
            }

            for(int i = 0; i < ParkSocketServer.getClientsCount(); i++){
                SocketClient client = ParkSocketServer.getClient(i);
                if(!CommonUtils.isEmpty(client.getOperator())){
                    if(client.getOperator().getOperatorUserName().equals(username)){
                        errorInfo = "该用户已登录";
                        throw new BizException(errorInfo);
                    }
                }
            }

            PostComputerManage postComputerManage = null;
            String[] ipArray = ips.split(",");
            for(String ip : ipArray){
                hql = "from PostComputerManage where postComputerIp = ? and useMark >= 0";
                postComputerManage = (PostComputerManage)baseDao.getUnique(hql,ip);
                if(!CommonUtils.isEmpty(postComputerManage)){
                    break;
                }
            }
            if(CommonUtils.isEmpty(postComputerManage)){
                errorInfo = "岗亭IP信息不存在";
                throw new BizException(errorInfo);
            }
            postComputerManage.setStatus(1);
            baseDao.update(postComputerManage);
            socketClient.setChannelId(postComputerManage.getPostComputerId());
            socketClient.setOperator(operator);
            socketClient.setPostComputerManage(postComputerManage);

            res = new JSONObject();
            JSONObject token = new JSONObject();
            token.put("username",username);
            token.put("password",password);
            token.put("ips",ips);
            token.put("timestamp",CommonUtils.getTimestamp().getTime() / 1000);
            token.put("noneStr", UUID.randomUUID().toString().replaceAll("-",""));

            String deToken = JSON.toJSONString(token, SerializerFeature.WriteMapNullValue);
            AesCipherService aesCipherService = new AesCipherService();
            String encodeToken = aesCipherService.encrypt(deToken.getBytes(), Hex.decode(HttpRequestUtils.aesKey)).toHex();
            res.put("token",encodeToken);

            hql = "from DepartmentInfo where useMark >= 0";
            DepartmentInfo departmentInfo = (DepartmentInfo)baseDao.getUnique(hql);
            res.put("departmentName",Base64.encodeToString(departmentInfo.getDepName().getBytes()));
            res.put("priorityCity",Base64.encodeToString(departmentInfo.getPriorityCity().getBytes()));
            logOperationService.addOperatorLog(LogEnum.clientLogin,"用户：" + username + "登录客户端",socketClient.getSocket().getInetAddress().getHostAddress(),socketClient.getOperator(),null);
        } catch (Exception e) {
            LOGGER.error("登录异常");
        }
        return res;
    }

    @Override
    public JSONObject logout(SocketClient socketClient) throws BizException {
        JSONObject res = null;
        try {
            if(!CommonUtils.isEmpty(socketClient.getPostComputerManage())){
                PostComputerManage postComputerManage = socketClient.getPostComputerManage();
                postComputerManage.setStatus(0);
                baseDao.update(postComputerManage);
            }
            String operatorName = socketClient.getOperator().getOperatorName();
            String userName = socketClient.getOperator().getOperatorUserName();
            socketClient.setOperator(null);
            socketClient.setPostComputerManage(null);
            SocketUtils.closeSocket(socketClient.getSocket());
            ParkSocketServer.removeClient(socketClient);

            res = new JSONObject();
            logOperationService.addOperatorLog(LogEnum.clientLogin,"用户：" + operatorName + "登出客户端",socketClient.getSocket().getInetAddress().getHostAddress(),socketClient.getOperator(),null);
        } catch (Exception e) {
            LOGGER.error("登出异常");
        }
        return res;
    }

    @Override
    public JSONObject shiftLogout(SocketClient socketClient) throws BizException {
        JSONObject res = null;
        try {
            if(!CommonUtils.isEmpty(socketClient.getPostComputerManage())){
                PostComputerManage postComputerManage = socketClient.getPostComputerManage();
                postComputerManage.setStatus(0);
                baseDao.update(postComputerManage);
            }
            socketClient.setOperator(null);
            socketClient.setPostComputerManage(null);
            SocketUtils.closeSocket(socketClient.getSocket());
            ParkSocketServer.removeClient(socketClient);

            res = new JSONObject();
        } catch (Exception e) {
            LOGGER.error("交接班异常");
        }
        return res;
    }

    @Override
    public JSONObject handleInOutRecord(JSONObject params,SocketClient socketClient) throws BizException, IOException {
        //获取基础信息
        JSONObject res = null;
        try {
            JSONObject basicInfo = params.getJSONObject("basic");
            String carparkId = basicInfo.getString("carparkId");
            CarparkInfo carparkInfo = (CarparkInfo)baseDao.getById(CarparkInfo.class,carparkId);
            String carparkName = carparkInfo.getCarparkName();
            String carplateColor = basicInfo.getString("carplateColor");
            String packKind = basicInfo.getString("packKind");
            String kindId = basicInfo.getString("kindId");
            String memberId = basicInfo.getString("memberId");
            String isUseParkinglot = basicInfo.getString("isUseParkinglot");
            MemberWallet memberWallet = (MemberWallet)baseDao.getById(MemberWallet.class,memberId);
            MemberKind memberKind = (MemberKind)baseDao.getById(MemberKind.class,kindId);
            String operatorName = ChangeBase64ToStr(basicInfo.getString("operatorName"));
            String operatorId = "";
            String hql = "from Operator where operatorUserName = ? and useMark >= 0";
            Operator operator = (Operator)baseDao.getUnique(hql,operatorName);
            if (!CommonUtils.isEmpty(operator))
                operatorId = operator.getOperatorId();

            String postId = "";
            String postName = "";
            String carType = getCarTypeInfo(packKind);
            //获取进出场信息
            Integer inOutType = Integer.parseInt(params.getString("inOutType"));
            String remarkInfo = ChangeBase64ToStr(params.getString("remarkInfo"));
            JSONObject inOutData = params.getJSONObject("inOutData");
            OrderInoutRecord orderInoutRecord = new OrderInoutRecord();
            Double pushFee = 0D;
            DeviceManageUtils deviceManageUtils = new StarnetDeviceUtils();

            Integer openGateMode = params.getInteger("openGateMode");
            boolean isRepeatIn = false;     //是否重复入场
            boolean isNeedPushData = true;  //是否需要推送数据到首页

            if(inOutType.equals(0)){
                String inCarno = ChangeBase64ToStr(inOutData.getString("inCarno"));
                String inCarnoOld = ChangeBase64ToStr(inOutData.getString("inCarnoOld"));
                String inTime = inOutData.getString("inTime");
                String inRoadId = inOutData.getString("inRoadId");
                if ((inCarno.equals(AppInfo.lastInCarno)) && (carparkId.equals(AppInfo.lastInCarparkId)) && (inTime.equals(AppInfo.lastInTime))){
                    res = new JSONObject();
                    res.put("data","1");
                    return res;
                }else {
                    AppInfo.lastInCarno = inCarno;
                    AppInfo.lastInCarparkId = carparkId;
                    AppInfo.lastInTime = inTime;
                }
                InOutCarRoadInfo inOutCarRoadInfo = (InOutCarRoadInfo)baseDao.getById(InOutCarRoadInfo.class,inRoadId);
                String inRoadName = inOutCarRoadInfo.getCarRoadName();
                String inCameraId = inOutData.getString("inCameraId");
                DeviceInfo deviceInfo = (DeviceInfo)baseDao.getById(DeviceInfo.class,inCameraId);
                String inPicName = inOutData.getString("inPicName");
                String releaseMode = inOutData.getString("releaseMode");
                PostComputerManage postComputerManage = getPostComputerManage(inRoadId);
                if(!CommonUtils.isEmpty(postComputerManage)){
                    postId = postComputerManage.getPostComputerId();
                    postName = postComputerManage.getPostComputerName();
                }

                //根据IPC的ID和车道ID获取LED信息
                String ledId = null;
                String ledType = null;
                hql = "from DeviceInfo where parentDeviceId = ? and deviceType = ? and useMark >= 0";
                DeviceInfo ledDeviceInfo = (DeviceInfo)baseDao.getUnique(hql,inCameraId ,"3");
                if (CommonUtils.isEmpty(ledDeviceInfo)){
                    ledDeviceInfo = (DeviceInfo)baseDao.getUnique(hql,inCameraId ,"11");
                }
                if (!CommonUtils.isEmpty(ledDeviceInfo)){
                    ledId = ledDeviceInfo.getDeviceId();
                    ledType = ledDeviceInfo.getDeviceType();
                }

                //插入进出进入表
                InoutRecordInfo inoutRecordInfo = new InoutRecordInfo();
                inoutRecordInfo.setCarNoOld(inCarnoOld);
                inoutRecordInfo.setCarNo(inCarno);
                inoutRecordInfo.setInoutTime(Timestamp.valueOf(inTime.replaceAll("/","-")));
                inoutRecordInfo.setInoutFlag(Byte.valueOf("0"));
                inoutRecordInfo.setInoutStatus(Byte.valueOf(releaseMode));
                inoutRecordInfo.setCarparkId(carparkId);
                inoutRecordInfo.setCarparkName(carparkName);
                inoutRecordInfo.setCarRoadId(inRoadId);
                inoutRecordInfo.setCarRoadName(inRoadName);
                inoutRecordInfo.setPostId(postId);
                inoutRecordInfo.setPostName(postName);
                inoutRecordInfo.setCameraId(inCameraId);
                inoutRecordInfo.setPhotoCapturePicName(inPicName);
                inoutRecordInfo.setCarNoColor(Byte.valueOf(carplateColor));
                inoutRecordInfo.setCarType(carType);
                inoutRecordInfo.setOperatorId(operatorId);
                inoutRecordInfo.setOperatorName(operatorName);
                inoutRecordInfo.setRemark(remarkInfo);
                inoutRecordInfo.setAddTime(CommonUtils.getTimestamp());
                baseDao.save(inoutRecordInfo);
                if (releaseMode.equals("0") || releaseMode.equals("1")){
                    //插入进出订单表
                    hql = "from OrderInoutRecord where carNo = ? and carparkId = ? and outRecordId is null";
                    orderInoutRecord = (OrderInoutRecord)baseDao.getUnique(hql,inCarno,carparkId);
                    //原先有记录的
                    String orderOldId = "";
                    if (!CommonUtils.isEmpty(orderInoutRecord)) {
                        orderOldId = orderInoutRecord.getChargeInfoId();
                        if (!CommonUtils.isEmpty(orderInoutRecord.getChargePreAmount())){
                            if (orderInoutRecord.getChargePreAmount() > 0){             //出场时未匹配到但是已经提前缴费了
                                hql = "select transaction_id as id from order_transaction where order_id = :orderId";
                                SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
                                query.setParameter("orderId",orderInoutRecord.getChargeInfoId());
                                query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                                List<Map> mapList = query.list();
                                for (Map map : mapList){
                                    OrderTransaction orderTransaction = (OrderTransaction)baseDao.getById(OrderTransaction.class,(String)map.get("id"));
                                    if (CommonUtils.isEmpty(orderTransaction.getTransactionMark())) {
                                        orderTransaction.setTransactionMark("已场内缴费，但无出场记录");
                                    }else {
                                        orderTransaction.setTransactionMark(orderTransaction.getTransactionMark() + "（已场内缴费，但无出场记录）");
                                    }
                                    baseDao.update(orderTransaction);
                                }

                            }
                        }
                        baseDao.delete(orderInoutRecord);
                        isRepeatIn = true;
                    }
                    orderInoutRecord = new OrderInoutRecord();
                    orderInoutRecord.setCarNo(inCarno);
                    orderInoutRecord.setCarparkId(carparkId);
                    orderInoutRecord.setCarparkName(carparkName);
                    orderInoutRecord.setAddTime(CommonUtils.getTimestamp());
                    orderInoutRecord.setInCarRoadId(inRoadId);
                    orderInoutRecord.setInCarRoadName(inRoadName);
                    orderInoutRecord.setInPictureName(inPicName);
                    orderInoutRecord.setInTime(Timestamp.valueOf(inTime.replaceAll("/","-")));
                    orderInoutRecord.setInRecordId(inoutRecordInfo.getInoutRecordId());
                    orderInoutRecord.setCarType(carType);
                    orderInoutRecord.setRemark(remarkInfo);
                    baseDao.save(orderInoutRecord);

                    WorkForMultiCarno(inCarno,0,carparkId,orderInoutRecord);

                    //先开闸，再播报语音
                    if (releaseMode.equals("1")){
                        if (carparkName.equals("海西1号地库") || carparkName.equals("海西2号地库")){
                            LOGGER.error(inCarno + "在" +  inRoadName + "开闸入场");
                        }
                        deviceManageUtils.openRoadGate(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),
                                deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd());
                    }else if (releaseMode.equals("2")){
                        isNeedPushData = false;
                        deviceManageUtils.closeRoadGate(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),
                                deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd());
                    }

                    //如果纳入车位统计，车场剩余车位需要减一
                    if (!("1".equals(isUseParkinglot))) {       //不使用固定车位
                        if (!isRepeatIn) {                      //没有重复入场
                            if (!CommonUtils.isEmpty(memberKind)) {
                                if (memberKind.getIsStatistic().equals("1")) {
                                    calculateParkSpace(carparkId,0,inCarno);
                                }
                            }
                        }
                    }

                    //如果存在LED显示屏,入场放行只有两种模式，手动放行和禁止通行
                    if (!StringUtils.isBlank(ledId)){
                        JSONObject playParams = new JSONObject();
                        String memberLeft = "";
                        hql = "FROM LedDisplayConfig WHERE devId = ? AND sceneNo = ?";
                        LedDisplayConfig ledDisplayConfig = new LedDisplayConfig();
                        if (releaseMode.equals("1")){
                            //手动放行或是自动放行，则选择入口常规
                            ledDisplayConfig = (LedDisplayConfig) baseDao.getUnique(hql,ledId,1);
                        }else if (releaseMode.equals("2")){
                            ledDisplayConfig = (LedDisplayConfig) baseDao.getUnique(hql,ledId,6);
                        }
                        playParams.put("isForbidLed",basicInfo.get("isForbidLed"));
                        playParams.put("carPlate",inCarno);
                        playParams.put("carparkName",carparkInfo.getCarparkName());
                        playParams.put("availableCarSpace",carparkInfo.getAvailableCarSpace());
                        playParams.put("inOutTime",inTime);
                        playParams.put("parkingLength","0");
                        playParams.put("kindName",CommonUtils.isEmpty(memberKind) ? "" : memberKind.getKindName());
                        playParams.put("chargeAmount","0");
                        playParams.put("limitReason","禁止入场");
                        playParams.put("memberLeft",memberLeft);
                        if (releaseMode.equals("1") || releaseMode.equals("2")) {
                            deviceBizService.PlayLedInfo(inCameraId, 0, playParams, ledDisplayConfig,ledType,ledId);
                        }
                    }else {
                        if ("1".equals(AppInfo.isUseGuideScreen)){
                            JSONObject guideParam = new JSONObject();
                            guideParam.put("inOutType",0);
                            guideParam.put("deviceId",deviceInfo.getDeviceId());
                            guideParam.put("deviceIp",deviceInfo.getDeviceIp());
                            guideParam.put("devicePort",deviceInfo.getDevicePort());
                            guideParam.put("deviceUserName",deviceInfo.getDeviceUsername());
                            guideParam.put("devicePwd",deviceInfo.getDevicePwd());
                            guideParam.put("carparkId",carparkId);
                            guideParam.put("availableNum",carparkInfo.getAvailableCarSpace());
                            guideParam.put("totalNum",carparkInfo.getTotalCarSpace());
                            GuideScreenThread.setSuspend(true);
                            GuideScreenThread.addPlayListInfo(guideParam);
                            GuideScreenThread.setSuspend(false);
                        }
                    }
                    newOrderParkService.addInParkOrderToCloud(inoutRecordInfo,orderOldId,orderInoutRecord);
                    logOperationService.addOperatorLog(LogEnum.dealCarno,"用户：" + operatorName + "放行车辆" + inCarno + "入场",socketClient.getSocket().getInetAddress().getHostAddress(),socketClient.getOperator(),null);
                }else{
                    isNeedPushData = false;
                    logOperationService.addOperatorLog(LogEnum.dealCarno,"用户：" + operatorName + "禁止车辆" + inCarno + "入场",socketClient.getSocket().getInetAddress().getHostAddress(),socketClient.getOperator(),null);
                    newOrderParkService.addInParkOrderToCloud(inoutRecordInfo,"",null);
                }

            }else{
                //插入出场信息
                String outCarno = ChangeBase64ToStr(inOutData.getString("outCarno"));
                String outCarnoOld = ChangeBase64ToStr(inOutData.getString("outCarnoOld"));
                String outTime = inOutData.getString("outTime");
                if ((outCarno.equals(AppInfo.lastOutCarno)) && (carparkId.equals(AppInfo.lastOutCarparkId)) && (outTime.equals(AppInfo.lastOutTime))){
                    res = new JSONObject();
                    res.put("data","1");
                    return res;
                }else {
                    AppInfo.lastOutCarno = outCarno;
                    AppInfo.lastOutCarparkId = carparkId;
                    AppInfo.lastOutTime = outTime;
                }
                String outRoadId = inOutData.getString("outRoadId");
                InOutCarRoadInfo inOutCarRoadInfo = (InOutCarRoadInfo)baseDao.getById(InOutCarRoadInfo.class,outRoadId);
                String outRoadName = inOutCarRoadInfo.getCarRoadName();
                String outCameraId = inOutData.getString("outCameraId");
                DeviceInfo deviceInfo = (DeviceInfo)baseDao.getById(DeviceInfo.class,outCameraId);
                String outPicName = inOutData.getString("outPicName");
                String releaseMode = inOutData.getString("releaseMode");
                String chargeAmount = inOutData.getString("chargeAmount");
                String releaseType = inOutData.getString("releaseType");
                String releaseReason = ChangeBase64ToStr(inOutData.getString("releaseReason"));
                String discountFee = inOutData.getString("discountFee");
                String discountType = ChangeBase64ToStr(inOutData.getString("discountType"));
                String chargeActualAmount = inOutData.getString("chargeActualAmount");
                String stayTime = inOutData.getString("stayTime");

                PostComputerManage postComputerManage = getPostComputerManage(outRoadId);
                if(!CommonUtils.isEmpty(postComputerManage)){
                    postId = postComputerManage.getPostComputerId();
                    postName = postComputerManage.getPostComputerName();
                }

                String ledId = "";
                String ledType = "";
                hql = "from DeviceInfo where parentDeviceId = ? and deviceType = ? and useMark >= 0";
                DeviceInfo ledDeviceInfo = (DeviceInfo)baseDao.getUnique(hql,outCameraId ,"3");
                if (CommonUtils.isEmpty(ledDeviceInfo)){
                    ledDeviceInfo = (DeviceInfo)baseDao.getUnique(hql,outCameraId ,"11");
                }
                if (!CommonUtils.isEmpty(ledDeviceInfo)){
                    ledId = ledDeviceInfo.getDeviceId();
                    ledType = ledDeviceInfo.getDeviceType();
                }

                //插入进出记录表
                InoutRecordInfo inoutRecordInfo = new InoutRecordInfo();
                inoutRecordInfo.setCarNoOld(outCarnoOld);
                inoutRecordInfo.setCarNo(outCarno);
                inoutRecordInfo.setInoutTime(Timestamp.valueOf(outTime.replaceAll("/","-")));
                inoutRecordInfo.setInoutFlag(Byte.valueOf("1"));
                inoutRecordInfo.setInoutStatus(Byte.valueOf(releaseMode));
                inoutRecordInfo.setCarparkId(carparkId);
                inoutRecordInfo.setCarparkName(carparkName);
                inoutRecordInfo.setCarRoadId(outRoadId);
                inoutRecordInfo.setCarRoadName(outRoadName);
                inoutRecordInfo.setPostId(postId);
                inoutRecordInfo.setPostName(postName);
                inoutRecordInfo.setCameraId(outCameraId);
                inoutRecordInfo.setPhotoCapturePicName(outPicName);
                inoutRecordInfo.setCarNoColor(Byte.valueOf(carplateColor));
                inoutRecordInfo.setCarType(carType);
                inoutRecordInfo.setOperatorId(operatorId);
                inoutRecordInfo.setOperatorName(operatorName);
                inoutRecordInfo.setRemark(remarkInfo);
                inoutRecordInfo.setAddTime(CommonUtils.getTimestamp());
                baseDao.save(inoutRecordInfo);
                //插入进出订单表
                if (releaseMode.equals("0") || releaseMode.equals("1")){
                    //同一车场的同一车牌且入场时间小的
                    hql = "from OrderInoutRecord where carNo = ? and carparkId = ? and inTime <= ? and outRecordId is null";
                    orderInoutRecord = (OrderInoutRecord)baseDao.getUnique(hql,outCarno,carparkId,Timestamp.valueOf(outTime.replaceAll("/","-")));
                    if(!CommonUtils.isEmpty(orderInoutRecord)){
                        InoutRecordInfo inRecordInfo = (InoutRecordInfo)baseDao.getById(InoutRecordInfo.class,orderInoutRecord.getInRecordId());
                        //匹配入场记录
                        orderInoutRecord.setUpdateTime(CommonUtils.getTimestamp());
                        orderInoutRecord.setCarNo(outCarno);
                        orderInoutRecord.setCarparkId(carparkId);
                        orderInoutRecord.setCarparkName(carparkName);
                        orderInoutRecord.setOutCarRoadId(outRoadId);
                        orderInoutRecord.setOutCarRoadName(outRoadName);
                        orderInoutRecord.setOutPictureName(outPicName);
                        orderInoutRecord.setChargeTime(CommonUtils.getTimestamp());
                        orderInoutRecord.setOutTime(Timestamp.valueOf(outTime.replaceAll("/","-")));
                        orderInoutRecord.setStayTime(Integer.valueOf(stayTime));
                        orderInoutRecord.setChargeReceivableAmount(Double.valueOf(chargeAmount));
                        orderInoutRecord.setChargeActualAmount(Double.valueOf(chargeActualAmount));
                        orderInoutRecord.setOutRecordId(inoutRecordInfo.getInoutRecordId());
                        orderInoutRecord.setReleaseType(Byte.valueOf(releaseType));
                        orderInoutRecord.setReleaseReason(releaseReason);
                        orderInoutRecord.setCarNoAttribute(Byte.valueOf(packKind));
                        orderInoutRecord.setCarType(carType);
                        orderInoutRecord.setChargeOperatorId(operatorId);
                        orderInoutRecord.setChargeOperatorName(operatorName);
                        orderInoutRecord.setChargePostId(postId);
                        orderInoutRecord.setChargePostName(postName);
                        orderInoutRecord.setRemark(remarkInfo);
                        baseDao.save(orderInoutRecord);
                        //如果纳入车位统计，车场剩余车位需要加一（未匹配出场不纳入车位统计）
                        if (!"1".equals(isUseParkinglot)) {                   //不使用固定车位
                            if (!(inRecordInfo.getInoutStatus().equals(3))){    //入场车辆不是离线数据
                                if (!CommonUtils.isEmpty(memberKind)) {
                                    if (memberKind.getIsStatistic().equals("1")) {
                                        //计算车位
                                        calculateParkSpace(carparkId,1,outCarno);
                                    }
                                }
                            }
                        }
                    }else{
                        orderInoutRecord = new OrderInoutRecord();
                        orderInoutRecord.setCarNo(outCarno);
                        orderInoutRecord.setCarparkId(carparkId);
                        orderInoutRecord.setCarparkName(carparkName);
                        orderInoutRecord.setOutCarRoadId(outRoadId);
                        orderInoutRecord.setOutCarRoadName(outRoadName);
                        orderInoutRecord.setOutPictureName(outPicName);
                        orderInoutRecord.setChargeTime(CommonUtils.getTimestamp());
                        orderInoutRecord.setOutTime(Timestamp.valueOf(outTime.replaceAll("/","-")));
                        orderInoutRecord.setStayTime(Integer.valueOf(stayTime));
                        orderInoutRecord.setChargeReceivableAmount(Double.valueOf(chargeAmount));
                        orderInoutRecord.setChargeActualAmount(Double.valueOf(chargeActualAmount));
                        orderInoutRecord.setOutRecordId(inoutRecordInfo.getInoutRecordId());
                        orderInoutRecord.setReleaseType(Byte.valueOf(releaseType));
                        orderInoutRecord.setReleaseReason(releaseReason);
                        orderInoutRecord.setCarNoAttribute(Byte.valueOf(packKind));
                        orderInoutRecord.setCarType(carType);
                        orderInoutRecord.setChargeOperatorId(operatorId);
                        orderInoutRecord.setChargeOperatorName(operatorName);
                        orderInoutRecord.setChargePostId(postId);
                        orderInoutRecord.setChargePostName(postName);
                        orderInoutRecord.setRemark(remarkInfo);
                        orderInoutRecord.setAddTime(CommonUtils.getTimestamp());
                        baseDao.save(orderInoutRecord);
                    }
                    WorkForMultiCarno(outCarno,1,carparkId,orderInoutRecord);

                    if (releaseMode.equals("1")){
                        if (carparkName.equals("海西1号地库") || carparkName.equals("海西2号地库")){
                            LOGGER.error(outCarno + "在" +  outRoadName + "开闸出场");
                        }
                        deviceManageUtils.openRoadGate(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),
                                deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd());
                    }else if (releaseMode.equals("2")){
                        isNeedPushData = false;
                        deviceManageUtils.closeRoadGate(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),
                                deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd());
                    }
                    //如果存在LED显示屏,入场放行只有两种模式，入口常规和禁止通行
                    if (!StringUtils.isBlank(ledId)){
                        JSONObject playParams = new JSONObject();
                        String memberLeft = "";
                        hql = "FROM LedDisplayConfig WHERE devId = ? AND sceneNo = ?";
                        LedDisplayConfig ledDisplayConfig = new LedDisplayConfig();
                        if (releaseMode.equals("1")){
                            //手动放行或是自动放行，则选择出口常规
                            ledDisplayConfig = (LedDisplayConfig) baseDao.getUnique(hql,ledId,3);
                        }else if (releaseMode.equals("2")){
                            ledDisplayConfig = (LedDisplayConfig) baseDao.getUnique(hql,ledId,6);
                        }
                        playParams.put("isForbidLed",basicInfo.get("isForbidLed"));
                        playParams.put("carPlate",outCarno);
                        playParams.put("carparkName",carparkInfo.getCarparkName());
                        playParams.put("availableCarSpace",carparkInfo.getAvailableCarSpace());
                        playParams.put("inOutTime",outTime);
                        playParams.put("parkingLength","0");
                        playParams.put("kindName",CommonUtils.isEmpty(memberKind) ? "" : memberKind.getKindName());
                        playParams.put("chargeAmount","0");
                        playParams.put("limitReason","禁止出场");
                        playParams.put("memberLeft",memberLeft);
                        if (releaseMode.equals("1") || releaseMode.equals("2")) {
                            deviceBizService.PlayLedInfo(outCameraId, 1, playParams, ledDisplayConfig,ledType,ledId);
                        }
                    }else {
                        if ("on".equals(AppInfo.isUseGuideScreen)){
                            JSONObject guideParam = new JSONObject();
                            guideParam.put("deviceInfo",deviceInfo);
                            guideParam.put("availableNum",carparkInfo.getAvailableCarSpace());
                            guideParam.put("totalNum",carparkInfo.getTotalCarSpace());
                            GuideScreenThread.setSuspend(true);
                            GuideScreenThread.addPlayListInfo(guideParam);
                            GuideScreenThread.setSuspend(false);
                        }
                    }
                    //将数据传到云端

                    if (Double.valueOf(chargeActualAmount) > 0){
                        //岗亭缴费超过0元，插入流水表
                        OrderTransaction orderTransaction = new OrderTransaction();
                        orderTransaction.setOrderId(orderInoutRecord.getChargeInfoId());
                        orderTransaction.setPayType(payTypeEnum.A);
                        orderTransaction.setPayTime(CommonUtils.getTimestamp());
                        orderTransaction.setPayStatus(payStatusEnum.HAS_PAID);
                        //orderTransaction.setPayTypeName(payTypeEnum.CASH_PAY.getDesc());
                        orderTransaction.setTotalFee(BigDecimal.valueOf(Double.valueOf(chargeActualAmount)+Double.valueOf(discountFee)));
                        orderTransaction.setRealFee(BigDecimal.valueOf(Double.valueOf(chargeActualAmount)));
                        orderTransaction.setDiscountFee(BigDecimal.valueOf(Double.valueOf(discountFee)));
                        orderTransaction.setDiscountType(discountType);
                        orderTransaction.setAddTime(CommonUtils.getTimestamp());
                        baseDao.save(orderTransaction);
                        newOrderParkService.addOutParkOrderToCloud(inoutRecordInfo,orderInoutRecord,orderTransaction);
                    }else {
                        newOrderParkService.addOutParkOrderToCloud(inoutRecordInfo,orderInoutRecord,null);
                    }
                    if (!CommonUtils.isEmpty(memberWallet)){
                        if (memberWallet.getPackKindId().equals(1)){
                            Integer restNumber =  memberWallet.getSurplusNumber() - 1;
                            restNumber = restNumber >= 0 ? restNumber : 0;
                            memberWallet.setSurplusNumber(restNumber);
                            baseDao.save(memberWallet);
                        }else if (memberWallet.getPackKindId().equals(2)){
                            Double restAmount = memberWallet.getSurplusAmount() - Double.valueOf(chargeActualAmount);
                            restAmount = restAmount >= 0 ? restAmount : 0;
                            memberWallet.setSurplusAmount(restAmount);
                            baseDao.save(memberWallet);
                        }
                    }

                    logOperationService.addOperatorLog(LogEnum.dealCarno,"用户：" + operatorName + "放行车辆" + outCarno + "出场",socketClient.getSocket().getInetAddress().getHostAddress(),socketClient.getOperator(),null);
                }else {
                    isNeedPushData = false;
                    newOrderParkService.addOutParkOrderToCloud(inoutRecordInfo,null,null);
                    logOperationService.addOperatorLog(LogEnum.dealCarno,"用户：" + operatorName + "禁止车辆" + outCarno + "出场",socketClient.getSocket().getInetAddress().getHostAddress(),socketClient.getOperator(),null);
                }

            }
            if (isNeedPushData) {
                if (inOutType.equals(0)) {
                    String inCarno = ChangeBase64ToStr(inOutData.getString("inCarno"));
                    String inTime = inOutData.getString("inTime");
                    String inRoadId = inOutData.getString("inRoadId");
                    InOutCarRoadInfo inOutCarRoadInfo = (InOutCarRoadInfo) baseDao.getById(InOutCarRoadInfo.class, inRoadId);
                    inOutMsgService.carInMsgPush(carparkId, inCarno, inTime, inOutCarRoadInfo.getCarRoadName());
                } else {
                    String outCarno = ChangeBase64ToStr(inOutData.getString("outCarno"));
                    String outTime = inOutData.getString("outTime");
                    String outRoadId = inOutData.getString("outRoadId");
                    InOutCarRoadInfo inOutCarRoadInfo = (InOutCarRoadInfo) baseDao.getById(InOutCarRoadInfo.class, outRoadId);
                    String chargeActualAmount = inOutData.getString("chargeActualAmount");
                    inOutMsgService.carOutMsgPush(carparkId, outCarno, outTime, inOutCarRoadInfo.getCarRoadName(), new BigDecimal(chargeActualAmount));
                }
            }

            res = new JSONObject();
            res.put("data","1");
        } catch (Exception e) {
            LOGGER.error("处理进出场记录失败：" + e.getMessage());
        }
        return res;
    }

    @Override
    public void handleAutoRelease(JSONObject params, CarparkInfo carparkInfo, InOutCarRoadInfo inOutCarRoadInfo, MemberKind memberKind,MemberWallet memberWallet) throws IOException {
        //自动开闸处理
        try {
            String inOutCarno = params.getString("inOutCarno");
            String inOuCarnoOld = params.getString("inOuCarnoOld");
            String inOutTime = params.getString("inOutTime");
            String carplateColor = params.getString("carplateColor");
            String inOutRoadId = inOutCarRoadInfo.getCarRoadId();
            String inOutRoadName = inOutCarRoadInfo.getCarRoadName();
            String inOutCameraId = params.getString("inOutCameraId");
            DeviceInfo deviceInfo = (DeviceInfo)baseDao.getById(DeviceInfo.class,inOutCameraId);
            String inOutPicName = params.getString("inOutPicName");
            String releaseMode = params.getString("releaseMode");
            String releaseType = params.getString("releaseType");
            String ledId = params.getString("ledId");
            //String releaseReason = params.getString("releaseReason");
            String releaseReason = "";
            String isUseParkinglot = params.getString("isUseParkinglot");

            PostComputerManage postComputerManage = getPostComputerManage(inOutCarRoadInfo.getCarRoadId());
            String postId = "";
            String postName = "";
            String carparkId = carparkInfo.getCarparkId();
            String carparkName = carparkInfo.getCarparkName();
            String carType = CommonUtils.isEmpty(memberKind) ? getCarTypeInfo(memberWallet.getPackKindId().toString()) : getCarTypeInfo(memberKind.getPackageKind().toString());
            if(!CommonUtils.isEmpty(postComputerManage)){
                postId = postComputerManage.getPostComputerId();
                postName = postComputerManage.getPostComputerName();
            }

            String hql = "FROM Operator WHERE operatorUserName = ? AND useMark >=0";
            Operator operator = (Operator)baseDao.getUnique(hql,"elecOperator");
            String operatorId = operator.getOperatorId();
            String operatorName = operator.getOperatorName();

            if(inOutCarRoadInfo.getCarRoadType().equals("0")){
                //插入进出进入表
                boolean isRepeatIn = false;     //是否重复入场
                InoutRecordInfo inoutRecordInfo = new InoutRecordInfo();
                inoutRecordInfo.setCarNoOld(inOuCarnoOld);
                inoutRecordInfo.setCarNo(inOutCarno);
                inoutRecordInfo.setInoutTime(Timestamp.valueOf(inOutTime));
                inoutRecordInfo.setInoutFlag(Byte.valueOf("0"));
                inoutRecordInfo.setInoutStatus(Byte.valueOf(releaseMode));
                inoutRecordInfo.setCarparkId(carparkId);
                inoutRecordInfo.setCarparkName(carparkName);
                inoutRecordInfo.setCarRoadId(inOutRoadId);
                inoutRecordInfo.setCarRoadName(inOutRoadName);
                inoutRecordInfo.setPostId(postId);
                inoutRecordInfo.setPostName(postName);
                inoutRecordInfo.setCameraId(inOutCameraId);
                inoutRecordInfo.setPhotoCapturePicName(inOutPicName);
                inoutRecordInfo.setCarNoColor(Byte.valueOf(carplateColor));
                inoutRecordInfo.setCarType(carType);
                inoutRecordInfo.setOperatorId(operatorId);
                inoutRecordInfo.setOperatorName(operatorName);
                inoutRecordInfo.setAddTime(CommonUtils.getTimestamp());
                baseDao.save(inoutRecordInfo);

                //插入进出订单表
                hql = "from OrderInoutRecord where carNo = ? and carparkId = ? and outRecordId is null";
                OrderInoutRecord orderInoutRecord = (OrderInoutRecord)baseDao.getUnique(hql,inOutCarno,carparkId);
                String orderOldId = "";
                if (!CommonUtils.isEmpty(orderInoutRecord)){
                    orderOldId = orderInoutRecord.getChargeInfoId();
                    if (!CommonUtils.isEmpty(orderInoutRecord.getChargePreAmount())){
                        if (orderInoutRecord.getChargePreAmount() > 0){             //出场时未匹配到但是已经提前缴费了
                            hql = "select transaction_id as id from order_transaction where order_id = :orderId";
                            SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
                            query.setParameter("orderId",orderInoutRecord.getChargeInfoId());
                            query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                            List<Map> mapList = query.list();
                            for (Map map : mapList){
                                OrderTransaction orderTransaction = (OrderTransaction)baseDao.getById(OrderTransaction.class,(String)map.get("id"));
                                if (CommonUtils.isEmpty(orderTransaction.getTransactionMark())) {
                                    orderTransaction.setTransactionMark("已场内缴费，但无出场记录");
                                }else {
                                    orderTransaction.setTransactionMark(orderTransaction.getTransactionMark() + "（已场内缴费，但无出场记录）");
                                }
                                baseDao.update(orderTransaction);
                            }

                        }
                    }
                    isRepeatIn = true;     //是否重复入场
                    baseDao.delete(orderInoutRecord);
                }
                orderInoutRecord = new OrderInoutRecord();
                orderInoutRecord.setCarNo(inOutCarno);
                orderInoutRecord.setCarparkId(carparkId);
                orderInoutRecord.setCarparkName(carparkName);
                orderInoutRecord.setAddTime(CommonUtils.getTimestamp());
                orderInoutRecord.setInCarRoadId(inOutRoadId);
                orderInoutRecord.setInCarRoadName(inOutRoadName);
                orderInoutRecord.setInPictureName(inOutPicName);
                orderInoutRecord.setInTime(Timestamp.valueOf(inOutTime));
                orderInoutRecord.setInRecordId(inoutRecordInfo.getInoutRecordId());
                orderInoutRecord.setCarType(carType);
                baseDao.save(orderInoutRecord);

                WorkForMultiCarno(inOutCarno,0,carparkId,orderInoutRecord);
                //如果纳入车位统计，车场剩余车位需要减一
                if (!("1".equals(isUseParkinglot))) {
                    if (!isRepeatIn) {
                        if (!CommonUtils.isEmpty(memberKind)) {
                            if (memberKind.getIsStatistic().equals("1")) {
                                calculateParkSpace(carparkId,0,inOutCarno);
                            }
                        }
                    }
                }
                newOrderParkService.addInParkOrderToCloud(inoutRecordInfo,orderOldId,orderInoutRecord);
                if (("1".equals(AppInfo.isUseGuideScreen)) && (CommonUtils.isEmpty(ledId))){
                    JSONObject guideParam = new JSONObject();
                    guideParam.put("inOutType",0);
                    guideParam.put("deviceId",deviceInfo.getDeviceId());
                    guideParam.put("deviceIp",deviceInfo.getDeviceIp());
                    guideParam.put("devicePort",deviceInfo.getDevicePort());
                    guideParam.put("deviceUserName",deviceInfo.getDeviceUsername());
                    guideParam.put("devicePwd",deviceInfo.getDevicePwd());
                    guideParam.put("carparkId",carparkId);
                    guideParam.put("availableNum",carparkInfo.getAvailableCarSpace());
                    guideParam.put("totalNum",carparkInfo.getTotalCarSpace());
                    GuideScreenThread.setSuspend(true);
                    GuideScreenThread.addPlayListInfo(guideParam);
                    GuideScreenThread.setSuspend(false);
                }
                inOutMsgService.carInMsgPush(carparkId, inOutCarno, inOutTime, inOutCarRoadInfo.getCarRoadName());
            }else {
                String chargeAmount = params.getString("chargeAmount");
                String chargeActualAmount = params.getString("chargeActualAmount");
                String chargeAmountForMember = params.getString("chargeAmountForMember");
                String stayTime = params.getString("stayTime");
                //插入进出记录表
                InoutRecordInfo inoutRecordInfo = new InoutRecordInfo();
                inoutRecordInfo.setCarNoOld(inOuCarnoOld);
                inoutRecordInfo.setCarNo(inOutCarno);
                inoutRecordInfo.setInoutTime(Timestamp.valueOf(inOutTime));
                inoutRecordInfo.setInoutFlag(Byte.valueOf("1"));
                inoutRecordInfo.setInoutStatus(Byte.valueOf(releaseMode));
                inoutRecordInfo.setCarparkId(carparkId);
                inoutRecordInfo.setCarparkName(carparkName);
                inoutRecordInfo.setCarRoadId(inOutCarRoadInfo.getCarRoadId());
                inoutRecordInfo.setCarRoadName(inOutRoadName);
                inoutRecordInfo.setPostId(postId);
                inoutRecordInfo.setPostName(postName);
                inoutRecordInfo.setCameraId(inOutCameraId);
                inoutRecordInfo.setPhotoCapturePicName(inOutPicName);
                inoutRecordInfo.setCarNoColor(Byte.valueOf(carplateColor));
                inoutRecordInfo.setCarType(carType);
                inoutRecordInfo.setOperatorId(operatorId);
                inoutRecordInfo.setOperatorName(operatorName);
                inoutRecordInfo.setAddTime(CommonUtils.getTimestamp());
                baseDao.save(inoutRecordInfo);
                //插入进出订单表

                if (releaseMode.equals("0") || releaseMode.equals("1")){
                    //同一车场的同一车牌且入场时间小的
                    hql = "from OrderInoutRecord where carNo = ? and carparkId = ? and inTime <= ? and outRecordId is null";
                    OrderInoutRecord orderInoutRecord = (OrderInoutRecord)baseDao.getUnique(hql,inOutCarno,carparkId,Timestamp.valueOf(inOutTime));
                    if(!CommonUtils.isEmpty(orderInoutRecord)){
                        InoutRecordInfo inRecordInfo = (InoutRecordInfo)baseDao.getById(InoutRecordInfo.class,orderInoutRecord.getInRecordId());
                        //匹配入场记录
                        orderInoutRecord.setUpdateTime(CommonUtils.getTimestamp());
                        orderInoutRecord.setCarNo(inOutCarno);
                        orderInoutRecord.setCarparkId(carparkId);
                        orderInoutRecord.setCarparkName(carparkName);
                        orderInoutRecord.setOutCarRoadId(inOutCarRoadInfo.getCarRoadId());
                        orderInoutRecord.setOutCarRoadName(inOutRoadName);
                        orderInoutRecord.setOutPictureName(inOutPicName);
                        orderInoutRecord.setChargeTime(CommonUtils.getTimestamp());
                        orderInoutRecord.setOutTime(Timestamp.valueOf(inOutTime));
                        orderInoutRecord.setStayTime(Integer.valueOf(stayTime));
                        orderInoutRecord.setChargeReceivableAmount(Double.valueOf(chargeAmount));
                        orderInoutRecord.setChargeActualAmount(Double.valueOf(chargeActualAmount));
                        orderInoutRecord.setOutRecordId(inoutRecordInfo.getInoutRecordId());
                        orderInoutRecord.setReleaseType(Byte.valueOf(releaseType));
                        orderInoutRecord.setReleaseReason(releaseReason);
                        orderInoutRecord.setCarNoAttribute(Byte.valueOf(carplateColor));
                        orderInoutRecord.setCarType(carType);
                        orderInoutRecord.setChargeOperatorId(operatorId);
                        orderInoutRecord.setChargeOperatorName(operatorName);
                        orderInoutRecord.setChargePostId(postId);
                        orderInoutRecord.setChargePostName(postName);
                        baseDao.save(orderInoutRecord);
                        //如果纳入车位统计，车场剩余车位需要加一（未匹配出场不纳入车位统计）
                        if (!("1".equals(isUseParkinglot))) {                   //不使用固定车位
                            if (!(inRecordInfo.getInoutStatus().equals(3))){    //入场车辆不是离线数据
                                if (!CommonUtils.isEmpty(memberKind)) {
                                    if (memberKind.getIsStatistic().equals("1")) {
                                        calculateParkSpace(carparkId,1,inOutCarno);
                                    }
                                }
                            }
                        }
                    }else{
                        orderInoutRecord = new OrderInoutRecord();
                        orderInoutRecord.setCarNo(inOutCarno);
                        orderInoutRecord.setCarparkId(carparkId);
                        orderInoutRecord.setCarparkName(carparkName);
                        orderInoutRecord.setOutCarRoadId(inOutCarRoadInfo.getCarRoadId());
                        orderInoutRecord.setOutCarRoadName(inOutRoadName);
                        orderInoutRecord.setOutPictureName(inOutPicName);
                        orderInoutRecord.setChargeTime(CommonUtils.getTimestamp());
                        orderInoutRecord.setOutTime(Timestamp.valueOf(inOutTime));
                        orderInoutRecord.setStayTime(Integer.valueOf(stayTime));
                        orderInoutRecord.setChargeReceivableAmount(Double.valueOf(chargeAmount));
                        orderInoutRecord.setChargeActualAmount(Double.valueOf(chargeActualAmount));
                        orderInoutRecord.setOutRecordId(inoutRecordInfo.getInoutRecordId());
                        orderInoutRecord.setReleaseType(Byte.valueOf(releaseType));
                        orderInoutRecord.setReleaseReason(releaseReason);
                        orderInoutRecord.setCarNoAttribute(Byte.valueOf(carplateColor));
                        orderInoutRecord.setCarType(carType);
                        orderInoutRecord.setChargeOperatorId(operatorId);
                        orderInoutRecord.setChargeOperatorName(operatorName);
                        orderInoutRecord.setChargePostId(postId);
                        orderInoutRecord.setChargePostName(postName);
                        orderInoutRecord.setAddTime(CommonUtils.getTimestamp());
                        baseDao.save(orderInoutRecord);
                    }
                    WorkForMultiCarno(inOutCarno,1,carparkId,orderInoutRecord);
                    if (!CommonUtils.isEmpty(memberWallet)){
                        if (memberWallet.getPackKindId().equals(1)){
                            Integer restNumber =  memberWallet.getSurplusNumber() - 1;
                            restNumber = restNumber >= 0 ? restNumber : 0;
                            memberWallet.setSurplusNumber(restNumber);
                            baseDao.save(memberWallet);
                        }else if (memberWallet.getPackKindId().equals(2)){
                            Double restAmount = memberWallet.getSurplusAmount() - Double.valueOf(chargeAmountForMember);
                            restAmount = restAmount >= 0 ? restAmount : 0;
                            memberWallet.setSurplusAmount(restAmount);
                            baseDao.save(memberWallet);
                        }
                    }
                    newOrderParkService.addOutParkOrderToCloud(inoutRecordInfo,orderInoutRecord,null);
                }
                inOutMsgService.carOutMsgPush(carparkId,inOutCarno,inOutTime,inOutCarRoadInfo.getCarRoadName(),new BigDecimal(chargeActualAmount));
                JSONObject guideParam = new JSONObject();
                guideParam.put("inOutType",1);
                guideParam.put("carparkId",carparkId);
                guideParam.put("availableNum",carparkInfo.getAvailableCarSpace());
                guideParam.put("totalNum",carparkInfo.getTotalCarSpace());
                GuideScreenThread.setSuspend(true);
                GuideScreenThread.addPlayListInfo(guideParam);
                GuideScreenThread.setSuspend(false);
            }
        } catch (Exception e) {
            LOGGER.error("自动处理记录异常");
        }

    }


    @Override
    public JSONObject pushManualRecord(JSONObject params, SocketClient socketClient) throws BizException, IOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        JSONObject json = new JSONObject();
        try {
            /** 基础信息获取 **/
            String carPlate = ChangeBase64ToStr(params.getString("carno"));
            String carnoColor = params.getString("carnoColor");
            String roadId = params.getString("roadId");
            String inOutTime = params.getString("inOutTime");
            String picUrl = params.getString("picUrl");
            String picName = params.getString("picName");
            ClientBizEnum bizEnum = ClientBizEnum.CarPlateManual;

            /** 获取车道绑定的IPC信息 **/
            String hql = "select device_id,sub_ipc_id from device_info where device_type = '0' and own_car_road = '" + roadId + "' and use_mark >= 0";
            SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
            List<Map<String,String>> list = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
            String deviceId = "";
            if (list.size() > 1) {
                for (Map map : list) {
                    if (!StringUtils.isBlank(map.get("sub_ipc_id").toString())){
                        deviceId = map.get("device_id").toString();
                        break;
                    }
                }
            }else {
                if (!CommonUtils.isEmpty(list)) {
                    deviceId = list.get(0).get("device_id").toString();
                }
            }
            DeviceInfo deviceInfo = new DeviceInfo();
            if (StringUtils.isBlank(deviceId)) {
                LOGGER.error("手动录入出入记录失败记录：车道未绑定设备");
                return null;
            } else {
                deviceInfo = (DeviceInfo) baseDao.getById(DeviceInfo.class, deviceId);
            }

            /** 获取LED信息 **/
            //根据IPC的ID和车道ID获取LED信息
            String ledId = "";
            String ledType = "";
            hql = "from DeviceInfo where parentDeviceId = ? and deviceType = ? and useMark >= 0";
            DeviceInfo ledDeviceInfo = (DeviceInfo)baseDao.getUnique(hql,deviceId ,"3");
            if (CommonUtils.isEmpty(ledDeviceInfo)){
                ledDeviceInfo = (DeviceInfo)baseDao.getUnique(hql,deviceId ,"11");
            }
            if (!CommonUtils.isEmpty(ledDeviceInfo)){
                ledId = ledDeviceInfo.getDeviceId();
                ledType = ledDeviceInfo.getDeviceType();
            }

            JSONObject devData = new JSONObject();
            devData.put("Attribute","1");
            devData.put("CarBodyColor","0");
            devData.put("CarPlatePos","");
            devData.put("CmdType",bizEnum);
            devData.put("Color",carnoColor);
            devData.put("Direction","1");
            devData.put("Roadway","0");
            devData.put("Time",inOutTime.replaceAll("/","-"));
            devData.put("Type","0");
            devData.put("bHistory","0");
            devData.put("deviceId",deviceId);
            devData.put("photoUri",picUrl);
            devData.put("photoFileName", picName);

            InOutCarRoadInfo inOutCarRoadInfo = (InOutCarRoadInfo)baseDao.getById(InOutCarRoadInfo.class,roadId);
            JSONObject roadData = new JSONObject();
            roadData.put("autoPassType",inOutCarRoadInfo.getAutoPassType());
            roadData.put("carRoadId",inOutCarRoadInfo.getCarRoadId());
            roadData.put("carRoadName",Base64.encodeToString(inOutCarRoadInfo.getCarRoadName().getBytes()));
            roadData.put("carRoadType",inOutCarRoadInfo.getCarRoadType());
            json.put("roadData",roadData);
            if (CommonUtils.isEmpty(inOutCarRoadInfo.getManageComputerId())){
                LOGGER.error("手动录入记录失败：车道未绑定岗亭");
                return null;
            }
            PostComputerManage postComputerManage = (PostComputerManage)baseDao.getById(PostComputerManage.class,inOutCarRoadInfo.getManageComputerId());

            CarparkInfo carparkInfo = (CarparkInfo)baseDao.getById(CarparkInfo.class,inOutCarRoadInfo.getOwnCarparkNo());
            JSONObject carparkData = new JSONObject();
            carparkData.put("availableCarSpace",carparkInfo.getAvailableCarSpace());
            carparkData.put("carparkId",carparkInfo.getCarparkId());
            carparkData.put("carparkName",Base64.encodeToString(carparkInfo.getCarparkName().getBytes()));
            carparkData.put("closeType",carparkInfo.getCloseType());
            carparkData.put("criticalValue",carparkInfo.getCriticalValue());
            carparkData.put("ownCarparkNo",carparkInfo.getOwnCarparkNo());
            carparkData.put("passTimeWhenBig",carparkInfo.getPassTimeWhenBig());
            carparkData.put("totalCarSpace",carparkInfo.getTotalCarSpace());
            carparkData.put("isClose",carparkInfo.getIsClose());
            carparkData.put("ifIncludeCaculate",carparkInfo.getIfIncludeCaculate());
            carparkData.put("isTestRunning",carparkInfo.getIsTestRunning());
            carparkData.put("isOverdueAutoOpen",carparkInfo.getIsOverdueAutoOpen());
            json.put("carparkData",carparkData);

            /** 车牌模糊匹配，针对一位多车 **/
            String oldCarno = "";
            if ("1".equals(inOutCarRoadInfo.getIsAutoMatchCarNo())){
                if (!deviceBizService.IsWhiteListCarno(carPlate,carparkInfo.getCarparkId())){
                    String correctionCarno = deviceBizService.RedressCarno(carPlate,"0",inOutCarRoadInfo.getIsAutoMatchCarNo(),
                            String.valueOf(inOutCarRoadInfo.getAutoMatchLeastBite()),inOutCarRoadInfo.getAutoMatchCarNoPos(),
                            carparkInfo.getCarparkId(),inOutCarRoadInfo.getWhiteIntelligentCorrection());
                    if (!correctionCarno.equals(carPlate)){
                        oldCarno = carPlate;
                        carPlate = correctionCarno;
                    }
                }
            }
            if (!CommonUtils.isEmpty(inOutCarRoadInfo.getIntelligentCorrection())){
                String correctionCarno = CarnoIntelligentCorrect(inOutCarRoadInfo.getIntelligentCorrection(),carPlate);
                oldCarno = carPlate;
                carPlate = correctionCarno;
            }
            devData.put("CarPlate",Base64.encodeToString(carPlate.getBytes()));
            json.put("deviceData",devData);

            /** 获取道闸信息 **/
            DeviceManageUtils deviceManageUtils = new StarnetDeviceUtils();
            hql = "from DeviceInfo where deviceType = ? and parentDeviceId = ? AND useMark >= 0 ";
            String uartDeviceAddr = "1";            //默认是白名单开闸
            DeviceInfo childDevice = (DeviceInfo)baseDao.getUnique(hql,"2",deviceId);
            if(!CommonUtils.isEmpty(childDevice)){
                JSONObject openGateData = new JSONObject();
                uartDeviceAddr = childDevice.getUartDeviceAddr();
                openGateData.put("uartDeviceAddr",uartDeviceAddr);
                json.put("openGateData",openGateData);
            }

            /** 获取会员信息 **/
            hql = "from MemberWallet where menberNo like ? and carPark = ? AND useMark >= 0";
            MemberWallet memberWallet = (MemberWallet)baseDao.getUnique(hql,"%" + carPlate + "%",carparkInfo.getCarparkId());
            if (!CommonUtils.isEmpty(memberWallet)) {
                Map<String,String> memberWalletSent = BeanUtils.describe(memberWallet);
                memberWalletSent.put("menberNo",Base64.encodeToString(memberWalletSent.get("menberNo").getBytes()));
                memberWalletSent.put("driverName",Base64.encodeToString(memberWalletSent.get("driverName").getBytes()));
                memberWalletSent.put("driverInfo",Base64.encodeToString(memberWalletSent.get("driverInfo").getBytes()));
                if (CommonUtils.isEmpty(memberWalletSent.get("parkingLotId")))
                    memberWalletSent.put("parkingLotId","");
                else
                    memberWalletSent.put("parkingLotId",Base64.encodeToString(memberWalletSent.get("parkingLotId").getBytes()));
                json.put("memberWallet", memberWalletSent);
            }else
                json.put("memberWallet", Collections.EMPTY_MAP);


            String isUseParkinglot = "0";       //是否使用固定车位
            String isMultiCarno = "0";          //是否是一位多车
            if(inOutCarRoadInfo.getCarRoadType().equals("0")){
                Timestamp inTime = Timestamp.valueOf(inOutTime.replaceAll("/","-"));
                /** 获取是否开闸等信息 **/
                Map<String,String> openGateInfo = IsNeedOpenGate(carPlate,0,inTime,carparkInfo,memberWallet,uartDeviceAddr,0);
                if (CommonUtils.isEmpty(openGateInfo)){
                    return null;
                }

                /** 获取收费类型信息 **/
                MemberKind memberKind = new MemberKind();
                if(!CommonUtils.isEmpty(memberWallet)){
                    json.put("isMember","true");
                    isUseParkinglot = openGateInfo.get("isUseParkinglot").toString();
                    json.put("isUseParkinglot",isUseParkinglot);
                    isMultiCarno = openGateInfo.get("isMultiCarno").toString();
                    String memberKindId;
                    if(StringUtils.isBlank(memberWallet.getMenberTypeId())){
                        memberKindId = "";
                    }else{
                        memberKindId = memberWallet.getMenberTypeId();
                    }
                    memberKind = (MemberKind)baseDao.getById(MemberKind.class,memberKindId);
                    if(CommonUtils.isEmpty(memberKind)){
                        //是会员但是没有套餐ID-预约车或是黑名单
                        if(openGateInfo.get("isOverdue").equals("1")){       //预约车和黑名单过期
                            hql = "from MemberKind where carparkInfo = ? AND useType = 1 AND useMark >= 0";
                            memberKind = (MemberKind)baseDao.getUnique(hql,carparkInfo);
                            JSONObject memberKindData = new JSONObject();
                            memberKindData.put("id",memberKind.getId());
                            memberKindData.put("kindName",Base64.encodeToString(memberKind.getKindName().getBytes()));
                            if (!StringUtils.isBlank(memberKind.getMemo()))
                                memberKindData.put("memo",Base64.encodeToString(memberKind.getMemo().getBytes()));
                            else
                                memberKindData.put("memo","");
                            memberKindData.put("packageKind",memberKind.getPackageKind());
                            memberKindData.put("packageChildKind",memberKind.getPackageChildKind());
                            memberKindData.put("isDelete",memberKind.getIsDelete());
                            memberKindData.put("isStatistic",memberKind.getIsStatistic());
                            memberKindData.put("voicePath",memberKind.getVoicePath());
                            memberKindData.put("showColor",memberKind.getShowColor());
                            memberKindData.put("useType",memberKind.getUseType());
                            json.put("memberKind",memberKindData);
                        }else {
                            json.put("memberKind", Collections.EMPTY_MAP);
                        }
                    }else{
                        if("1".equals(openGateInfo.get("isOverdue"))|| ("0".equals(openGateInfo.get("isUseWallet")))){ //过期或是未使用套餐,使用内部车临时收费
                            hql = "from MemberKind where carparkInfo = ? AND useType = 1 AND useMark >= 0";
                            memberKind = (MemberKind)baseDao.getUnique(hql,carparkInfo);
                        }
                        JSONObject memberKindData = new JSONObject();
                        memberKindData.put("id",memberKind.getId());
                        memberKindData.put("kindName",Base64.encodeToString(memberKind.getKindName().getBytes()));
                        if (!StringUtils.isBlank(memberKind.getMemo()))
                            memberKindData.put("memo",Base64.encodeToString(memberKind.getMemo().getBytes()));
                        else
                            memberKindData.put("memo","");
                        memberKindData.put("packageKind",memberKind.getPackageKind());
                        memberKindData.put("packageChildKind",memberKind.getPackageChildKind());
                        memberKindData.put("isDelete",memberKind.getIsDelete());
                        memberKindData.put("isStatistic",memberKind.getIsStatistic());
                        memberKindData.put("voicePath",memberKind.getVoicePath());
                        memberKindData.put("showColor",memberKind.getShowColor());
                        memberKindData.put("useType",memberKind.getUseType());
                        json.put("memberKind",memberKindData);
                    }
                }else{
                    json.put("isMember","false");
                    json.put("isUseParkinglot","0");
                    hql = "from MemberKind where carparkInfo = ? AND useType = 0 AND useMark >= 0";
                    memberKind = (MemberKind)baseDao.getUnique(hql,carparkInfo);
                    if(CommonUtils.isEmpty(memberKind)){
                        json.put("memberKind", Collections.EMPTY_MAP);
                    }else{
                        JSONObject memberKindData = new JSONObject();
                        memberKindData.put("id",memberKind.getId());
                        memberKindData.put("kindName",Base64.encodeToString(memberKind.getKindName().getBytes()));
                        if (!StringUtils.isBlank(memberKind.getMemo()))
                            memberKindData.put("memo",Base64.encodeToString(memberKind.getMemo().getBytes()));
                        else
                            memberKindData.put("memo","");
                        memberKindData.put("packageKind",memberKind.getPackageKind());
                        memberKindData.put("packageChildKind",memberKind.getPackageChildKind());
                        memberKindData.put("isDelete",memberKind.getIsDelete());
                        memberKindData.put("isStatistic",memberKind.getIsStatistic());
                        memberKindData.put("voicePath",memberKind.getVoicePath());
                        memberKindData.put("showColor",memberKind.getShowColor());
                        memberKindData.put("useType",memberKind.getUseType());
                        json.put("memberKind",memberKindData);
                    }
                }
                json.put("isMultiCarno",isMultiCarno);
                json.put("openGateMode",Integer.valueOf(openGateInfo.get("isNeedOpenGate")));

                //自动处理模式
                if (openGateInfo.get("isNeedOpenGate").equals("1")) {
                    hql = "FROM Operator WHERE operatorUserName = ? AND useMark >=0";
                    Operator operator = (Operator)baseDao.getUnique(hql,"elecOperator");
                    String operatorId = operator.getOperatorId();
                    if (!CommonUtils.isEmpty(memberWallet)) {
                        if (uartDeviceAddr.equals("1") && (!StringUtils.isBlank(memberWallet.getSynchronizeIpcList()))){
                            //不用开闸
                        }else{
                            deviceManageUtils.openRoadGate(deviceInfo.getDeviceIp(), Integer.parseInt(deviceInfo.getDevicePort()),
                                    deviceInfo.getDeviceUsername(), deviceInfo.getDevicePwd());
                        }
                    }else {
                        deviceManageUtils.openRoadGate(deviceInfo.getDeviceIp(), Integer.parseInt(deviceInfo.getDevicePort()),
                                deviceInfo.getDeviceUsername(), deviceInfo.getDevicePwd());
                    }
                    JSONObject autoReleaseParams = new JSONObject();
                    autoReleaseParams.put("inOutCarno", carPlate);
                    autoReleaseParams.put("inOuCarnoOld", oldCarno);
                    autoReleaseParams.put("inOutTime", inTime.toString());
                    autoReleaseParams.put("carplateColor", carnoColor);
                    autoReleaseParams.put("inOutCameraId", deviceInfo.getDeviceId());
                    autoReleaseParams.put("inOutPicName","");
                    autoReleaseParams.put("releaseMode", "0");
                    autoReleaseParams.put("releaseType", "3");
                    autoReleaseParams.put("releaseReason", openGateInfo.get("limiReason"));
                    autoReleaseParams.put("isUseParkinglot",isUseParkinglot);
                    autoReleaseParams.put("ledId",ledId);
                    handleAutoRelease(autoReleaseParams, carparkInfo, inOutCarRoadInfo, memberKind, memberWallet);
                }else {
                    if ("0".equals(postComputerManage.getIsAutoDeal())) {
                        JSONObject autoReleaseParams = new JSONObject();
                        autoReleaseParams.put("inOutCarno", carPlate);
                        autoReleaseParams.put("inOuCarnoOld", oldCarno);
                        autoReleaseParams.put("inOutTime", inTime.toString());
                        autoReleaseParams.put("carplateColor", carnoColor);
                        autoReleaseParams.put("inOutCameraId", deviceInfo.getDeviceId());
                        autoReleaseParams.put("inOutPicName", "");
                        autoReleaseParams.put("releaseMode", "0");
                        autoReleaseParams.put("releaseType", "3");
                        autoReleaseParams.put("releaseReason", openGateInfo.get("limiReason"));
                        autoReleaseParams.put("isUseParkinglot", isUseParkinglot);
                        autoReleaseParams.put("ledId", ledId);
                        handleAutoRelease(autoReleaseParams, carparkInfo, inOutCarRoadInfo, memberKind, memberWallet);
                        json.put("openGateMode",1);
                    }
                }

                //如果存在LED显示屏
                if (!StringUtils.isBlank(ledId)){
                    JSONObject playParams = new JSONObject();

                    Map<String,Object> ledMap = deviceBizService.GetLedPlaySence(ledId,0,carparkInfo.getLedMemberCriticalValue(),
                                                                                         Integer.valueOf(openGateInfo.get("isNeedOpenGate")),inTime,memberWallet,"");
                    LedDisplayConfig ledDisplayConfig = (LedDisplayConfig) ledMap.get("ledDisplayConfig");
                    String memberLeft = (String) ledMap.get("memberLeft");

                    playParams.put("carPlate",carPlate);
                    playParams.put("carparkName",carparkInfo.getCarparkName());
                    playParams.put("availableCarSpace",carparkInfo.getAvailableCarSpace());
                    playParams.put("inOutTime",inOutTime.replaceAll("/","-"));
                    playParams.put("parkingLength","0");
                    playParams.put("kindName",CommonUtils.isEmpty(memberKind)? "" : memberKind.getKindName());
                    playParams.put("chargeAmount","0");
                    playParams.put("limitReason",openGateInfo.get("limiReason"));
                    playParams.put("memberLeft",memberLeft);
                    playParams.put("isMultiCarno",isMultiCarno);
                    //playParams.put("driverName",driverName);
                    deviceBizService.PlayLedInfo(deviceId,0,playParams,ledDisplayConfig,ledType,ledId);
                }

            }else{
                //// 组织出场数据
                //1.计算费用
                Double chargeAmount = 0D;  //应收金额
                Double chargeAmountForMember = 0D;  //会员收费类型为临时车的时候
                Timestamp outTime = Timestamp.valueOf(inOutTime.replaceAll("/","-"));

                String useType = "0";//默认为外部临时收费
                Map<String,String> memberUseInfo = IsNeedOpenGate(carPlate,1,outTime,carparkInfo,memberWallet,uartDeviceAddr,chargeAmount);

                MemberKind memberKind = new MemberKind();
                if(!CommonUtils.isEmpty(memberWallet)){
                    json.put("isMember","true");
                    isUseParkinglot = memberUseInfo.get("isUseParkinglot").toString();
                    json.put("isUseParkinglot",isUseParkinglot);
                    isMultiCarno = memberUseInfo.get("isMultiCarno").toString();
                    String memberKindId;
                    if(StringUtils.isBlank(memberWallet.getMenberTypeId())){
                        memberKindId = "";
                    }else{
                        memberKindId = memberWallet.getMenberTypeId();
                    }
                    memberKind = (MemberKind)baseDao.getById(MemberKind.class,memberKindId);
                    if(CommonUtils.isEmpty(memberKind)){
                        //是会员但是没有套餐ID-预约车或是黑名单
                        useType = "1";
                        if(memberUseInfo.get("isOverdue").equals("1")){       //预约车和黑名单过期
                            hql = "from MemberKind where carparkInfo = ? AND useType = 1 AND useMark >= 0";
                            memberKind = (MemberKind)baseDao.getUnique(hql,carparkInfo);
                            JSONObject memberKindData = new JSONObject();
                            memberKindData.put("id",memberKind.getId());
                            memberKindData.put("kindName",Base64.encodeToString(memberKind.getKindName().getBytes()));
                            if (!StringUtils.isBlank(memberKind.getMemo()))
                                memberKindData.put("memo",Base64.encodeToString(memberKind.getMemo().getBytes()));
                            else
                                memberKindData.put("memo","");
                            memberKindData.put("packageKind",memberKind.getPackageKind());
                            memberKindData.put("packageChildKind",memberKind.getPackageChildKind());
                            memberKindData.put("isDelete",memberKind.getIsDelete());
                            memberKindData.put("isStatistic",memberKind.getIsStatistic());
                            memberKindData.put("voicePath",memberKind.getVoicePath());
                            memberKindData.put("showColor",memberKind.getShowColor());
                            memberKindData.put("useType",memberKind.getUseType());
                            json.put("memberKind",memberKindData);
                        }else {
                            json.put("memberKind", Collections.EMPTY_MAP);
                        }
                    }else{
                        if(memberUseInfo.get("isOverdue").equals("1") || (memberUseInfo.get("isUseWallet").equals("0"))){ //过期或是未使用套餐,使用内部车临时收费
                            useType = "1";
                            hql = "from MemberKind where carparkInfo = ? AND useType = 1 AND useMark >= 0";
                            memberKind = (MemberKind)baseDao.getUnique(hql,carparkInfo);
                        }
                        JSONObject memberKindData = new JSONObject();
                        memberKindData.put("id",memberKind.getId());
                        memberKindData.put("kindName",Base64.encodeToString(memberKind.getKindName().getBytes()));
                        if (!StringUtils.isBlank(memberKind.getMemo()))
                            memberKindData.put("memo",Base64.encodeToString(memberKind.getMemo().getBytes()));
                        else
                            memberKindData.put("memo","");
                        memberKindData.put("packageKind",memberKind.getPackageKind());
                        memberKindData.put("packageChildKind",memberKind.getPackageChildKind());
                        memberKindData.put("isDelete",memberKind.getIsDelete());
                        memberKindData.put("isStatistic",memberKind.getIsStatistic());
                        memberKindData.put("voicePath",memberKind.getVoicePath());
                        memberKindData.put("showColor",memberKind.getShowColor());
                        memberKindData.put("useType",memberKind.getUseType());
                        json.put("memberKind",memberKindData);
                    }
                }else{
                    json.put("memberKind",Collections.EMPTY_MAP);
                    json.put("isMember","false");
                    json.put("isUseParkinglot","0");
                    hql = "from MemberKind where carparkInfo = ? AND useType = 0 AND useMark >= 0";
                    useType = "0";
                    memberKind = (MemberKind)baseDao.getUnique(hql,carparkInfo);
                    if(CommonUtils.isEmpty(memberKind)){
                        json.put("memberKind", Collections.EMPTY_MAP);
                    }else{
                        JSONObject memberKindData = new JSONObject();
                        memberKindData.put("id",memberKind.getId());
                        memberKindData.put("kindName",Base64.encodeToString(memberKind.getKindName().getBytes()));
                        if (!StringUtils.isBlank(memberKind.getMemo()))
                            memberKindData.put("memo",Base64.encodeToString(memberKind.getMemo().getBytes()));
                        else
                            memberKindData.put("memo","");
                        memberKindData.put("packageKind",memberKind.getPackageKind());
                        memberKindData.put("packageChildKind",memberKind.getPackageChildKind());
                        memberKindData.put("isDelete",memberKind.getIsDelete());
                        memberKindData.put("isStatistic",memberKind.getIsStatistic());
                        memberKindData.put("voicePath",memberKind.getVoicePath());
                        memberKindData.put("showColor",memberKind.getShowColor());
                        memberKindData.put("useType",memberKind.getUseType());
                        json.put("memberKind",memberKindData);
                    }
                }
                json.put("isMultiCarno",isMultiCarno);

                //匹配的入场信息
                OrderInoutRecord matchInInfo = GetMatchInfo(carPlate,carparkInfo.getCarparkId(),outTime);
                Timestamp inTime = new Timestamp(System.currentTimeMillis());           //入场时间
                Timestamp chargeBeginTime = new Timestamp(System.currentTimeMillis());  //计费开始时间
                String parkingLen = "";
                Long stayTime = 0L;
                if(!CommonUtils.isEmpty(matchInInfo)){
                    json.put("isMatch","true");
                    JSONObject matchData = new JSONObject();
                    matchData.put("matchCarno",Base64.encodeToString(matchInInfo.getCarNo().getBytes()));
                    matchData.put("matchRoadId",matchInInfo.getInCarRoadId());
                    matchData.put("matchRoadName",Base64.encodeToString(matchInInfo.getInCarRoadName().getBytes()));
                    matchData.put("matchInTime",CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",matchInInfo.getInTime()));

                    if (!CommonUtils.isEmpty(memberWallet)) {
                        if (memberUseInfo.get("isOverdue").equals("1")) {//出场时白名单过期
                            if (memberWallet.getPackKindId().equals(0) || memberWallet.getPackKindId().equals(-3)) {
                                chargeBeginTime = memberWallet.getEffectiveEndTime().getTime() >= matchInInfo.getInTime().getTime() ? memberWallet.getEffectiveEndTime() : matchInInfo.getInTime();
                            } else
                                chargeBeginTime = matchInInfo.getInTime();
                        } else
                            chargeBeginTime = matchInInfo.getInTime();
                    }else
                        chargeBeginTime = matchInInfo.getInTime();

                    if (!CommonUtils.isEmpty(matchInInfo.getInChargeTime())){
                        chargeBeginTime = matchInInfo.getInChargeTime().getTime() >= chargeBeginTime.getTime() ? matchInInfo.getInChargeTime() : chargeBeginTime;
                    }

                    inTime = matchInInfo.getInTime();
                    stayTime = (outTime.getTime() - inTime.getTime()) / 1000;
                    int parkingHour = (int)(stayTime/3600);
                    int parkingMinute = (int)((stayTime-parkingHour*3600)/60);
                    parkingLen = "停车" + String.valueOf(parkingHour) + "时" + String.valueOf(parkingMinute) + "分";
                    matchData.put("stayTime",stayTime);
                    matchData.put("matchInPic",matchInInfo.getInPictureName());
                    json.put("matchData",matchData);
                }else{
                    parkingLen = "停车时长未知";
                    json.put("isMatch", "false");
                    json.put("matchData", Collections.EMPTY_MAP);
                    chargeBeginTime = new Timestamp(outTime.getTime() - 60 * 60 * 1000);
                }

                //提前缴费金额
                Double chargePreAmount = 0D;
                BigDecimal accountSum = new BigDecimal("0.00");
                Timestamp prechargeTime = outTime;
                //3、核算流水订单缴纳费用
                if (!CommonUtils.isEmpty(matchInInfo)) {
                    String accountHql = "from OrderTransaction where orderId = ? and payStatus = ? order by addTime desc";
                    List<OrderTransaction> orderTransactionList = (List<OrderTransaction>)baseDao.queryForList(accountHql,matchInInfo.getChargeInfoId(),payStatusEnum.HAS_PAID);
                    if (orderTransactionList.size() > 0) {
                        prechargeTime = orderTransactionList.get(0).getAddTime();
                        //流水表收费总金额
                        for (OrderTransaction orderTransaction : orderTransactionList){
                            accountSum = accountSum.add(orderTransaction.getTotalFee());
                        }
                    }
                    //写入提前缴费金额
                    if(CommonUtils.isEmpty(accountSum)){
                        accountSum = new BigDecimal("0.00");
                    }
                    matchInInfo.setChargePreAmount(accountSum.doubleValue());
                    chargePreAmount = matchInInfo.getChargePreAmount();
                }

                boolean isNeedCalculateCharge = false;      //是否结算金额
                if (chargePreAmount > 0){
                    isNeedCalculateCharge = outTime.getTime() - prechargeTime.getTime() > Integer.valueOf(carparkInfo.getPassTimeWhenBig()) * 60 * 1000 ? true : false;
                }else {
                    isNeedCalculateCharge = true;
                }

                try {
                    //使用未过期的白名单或是未匹配出场且车场试运行的金额为0
                    boolean isValid = false;
                    boolean isUseWallet = false;
                    boolean isOverdue = false;
                    if (!CommonUtils.isEmpty(memberWallet)){
                        isValid = memberUseInfo.get("isValid").equals("1") ? true : false;
                        isUseWallet = memberUseInfo.get("isUseWallet").equals("1") ? true : false;
                        isOverdue = memberUseInfo.get("isOverdue").equals("1") ? true : false;
                    }
                    if((isValid && isUseWallet && (!isOverdue)) || (CommonUtils.isEmpty(matchInInfo) && carparkInfo.getIsTestRunning().equals("1"))) {
                        if (memberWallet.getPackKindId().equals(2))
                            chargeAmountForMember = calculateFeeNewService.calculateParkingFeeNew(carPlate, useType, carnoColor,carparkInfo.getCarparkId(), chargeBeginTime, outTime);
                        chargeAmount = 0D;
                    }else {
                        chargeAmount = calculateFeeNewService.calculateParkingFeeNew(carPlate, useType, carnoColor, carparkInfo.getCarparkId(), chargeBeginTime, outTime);
                    }
                }catch (Exception e){
                    chargeAmount = 0D;
                }

                //2、获取入场订单信息
                if (!CommonUtils.isEmpty(matchInInfo)){
                    matchInInfo.setChargeReceivableAmount(chargeAmount);
                    matchInInfo.setUpdateTime(CommonUtils.getTimestamp());
                    baseDao.update(matchInInfo);
                }

                //获取应缴费金额
                Double chargeActualAmount = 0D;
                if (isNeedCalculateCharge){
                    chargeActualAmount = chargeAmount - chargePreAmount;
                }else {
                    chargeAmount = chargePreAmount;
                }
                chargeActualAmount = chargeActualAmount < 0 ? 0D : chargeActualAmount;
                Map<String,String> openGateModeInfo = IsNeedOpenGate(carPlate,1,outTime,carparkInfo,memberWallet,uartDeviceAddr,chargeActualAmount);

                boolean isNoSensePay = false;
                if (AppInfo.isUseUparkPay.equals("1") && (!CommonUtils.isEmpty(matchInInfo)) && (matchInInfo.getReleaseType().equals(4))){
                    //符合无感支付的车辆，如果金额大于300
                    if (chargeAmount > 300){
                        //不抬杆，推送账单至智慧停车云平台
                        String payAmt = (new BigDecimal(chargeAmount).multiply(new BigDecimal("100"))).setScale(0,BigDecimal.ROUND_UP) + "";
                        newOrderParkService.pushBillToUparkCloud(matchInInfo.getChargeInfoId(),carPlate,carparkInfo.getCarparkId(),carparkInfo.getCarparkName(),payAmt,payAmt
                                ,inTime.toString(),outTime.toString(),"");
                    }else {
                        //抬杆，调用离场通知接口推送消息至智慧停车平台
                        openGateModeInfo.put("isNeedOpenGate","1");
                        openGateModeInfo.put("limiReason","");
                        isNoSensePay = true;
                    }
                }

                json.put("openGateMode",Integer.valueOf(openGateModeInfo.get("isNeedOpenGate")));
                if((accountSum.compareTo(new BigDecimal(chargeAmount.toString())) >= 0) || isNoSensePay){
                    //满足缴费金额
                    if (openGateModeInfo.get("isNeedOpenGate").equals("1")){
                        hql = "FROM Operator WHERE operatorUserName = ? AND useMark >=0";
                        Operator operator = (Operator)baseDao.getUnique(hql,"elecOperator");
                        String operatorId = operator.getOperatorId();
                        //直接放行出场走handleAutoRelease
                        if (!CommonUtils.isEmpty(memberWallet)) {
                            if (uartDeviceAddr.equals("1") && (!StringUtils.isBlank(memberWallet.getSynchronizeIpcList()))){
                                //不用开闸
                            }else{
                                deviceManageUtils.openRoadGate(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),
                                        deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd());
                            }
                        }else {
                            deviceManageUtils.openRoadGate(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),
                                    deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd());
                        }
                        JSONObject autoReleaseParams = new JSONObject();
                        autoReleaseParams.put("inOutCarno", carPlate);
                        autoReleaseParams.put("inOutCarnoOld", oldCarno);
                        autoReleaseParams.put("inOutTime", outTime.toString());
                        autoReleaseParams.put("carplateColor", carnoColor);
                        autoReleaseParams.put("inOutCameraId", deviceInfo.getDeviceId());
                        autoReleaseParams.put("inOutPicName","");
                        autoReleaseParams.put("releaseMode", "0");
                        autoReleaseParams.put("chargeAmount", chargeAmount.toString());
                        autoReleaseParams.put("chargeAmountForMember", chargeAmountForMember.toString());
                        autoReleaseParams.put("releaseType", "3");
                        autoReleaseParams.put("releaseReason", openGateModeInfo.get("limiReason"));
                        autoReleaseParams.put("chargeActualAmount", chargeActualAmount.toString());
                        autoReleaseParams.put("stayTime",stayTime);
                        autoReleaseParams.put("isUseParkinglot",isUseParkinglot);
                        autoReleaseParams.put("ledId",ledId);
                        handleAutoRelease(autoReleaseParams, carparkInfo, inOutCarRoadInfo, memberKind, memberWallet);
                    }else {
                        if ("0".equals(postComputerManage.getIsAutoDeal())){
                            JSONObject autoReleaseParams = new JSONObject();
                            autoReleaseParams.put("inOutCarno", carPlate);
                            autoReleaseParams.put("inOutCarnoOld", oldCarno);
                            autoReleaseParams.put("inOutTime", outTime.toString());
                            autoReleaseParams.put("carplateColor", carnoColor);
                            autoReleaseParams.put("inOutCameraId", deviceInfo.getDeviceId());
                            autoReleaseParams.put("inOutPicName","");
                            autoReleaseParams.put("releaseMode", "0");
                            autoReleaseParams.put("chargeAmount", chargeAmount.toString());
                            autoReleaseParams.put("chargeAmountForMember", chargeAmountForMember.toString());
                            autoReleaseParams.put("releaseType", "3");
                            autoReleaseParams.put("releaseReason", openGateModeInfo.get("limiReason"));
                            autoReleaseParams.put("chargeActualAmount", chargeActualAmount.toString());
                            autoReleaseParams.put("stayTime",stayTime);
                            autoReleaseParams.put("isUseParkinglot",isUseParkinglot);
                            autoReleaseParams.put("ledId",ledId);
                            handleAutoRelease(autoReleaseParams, carparkInfo, inOutCarRoadInfo, memberKind, memberWallet);
                            json.put("openGateMode",1);
                        }
                    }
                }/*else {
                    //全部放行自动处理
                    if (uartDeviceAddr.equals("0") || uartDeviceAddr.equals("2")){
                        JSONObject autoReleaseParams = new JSONObject();
                        autoReleaseParams.put("inOutCarno", carPlate);
                        autoReleaseParams.put("inOutCarnoOld", oldCarno);
                        autoReleaseParams.put("inOutTime", outTime.toString());
                        autoReleaseParams.put("carplateColor", carnoColor);
                        autoReleaseParams.put("inOutCameraId", deviceInfo.getDeviceId());
                        autoReleaseParams.put("inOutPicName","");
                        autoReleaseParams.put("releaseMode", "0");
                        autoReleaseParams.put("chargeAmount", chargeAmount.toString());
                        autoReleaseParams.put("chargeAmountForMember", chargeAmountForMember.toString());
                        autoReleaseParams.put("releaseType", "3");
                        autoReleaseParams.put("releaseReason", openGateModeInfo.get("limiReason"));
                        autoReleaseParams.put("chargeActualAmount", chargeActualAmount.toString());
                        autoReleaseParams.put("stayTime",stayTime);
                        autoReleaseParams.put("isUseParkinglot",isUseParkinglot);
                        autoReleaseParams.put("ledId",ledId);
                        handleAutoRelease(autoReleaseParams, carparkInfo, inOutCarRoadInfo, memberKind, memberWallet);
                    }
                }*/

                json.put("chargeAmount",chargeAmount);
                json.put("chargePreAmount",chargePreAmount);
                //如果存在LED显示屏
                if (!StringUtils.isBlank(ledId)){
                    JSONObject playParams = new JSONObject();
                    Map<String,Object> ledMap = deviceBizService.GetLedPlaySence(ledId,1,carparkInfo.getLedMemberCriticalValue(),
                            Integer.valueOf(openGateModeInfo.get("isNeedOpenGate")),outTime,memberWallet,"");
                    LedDisplayConfig ledDisplayConfig = (LedDisplayConfig) ledMap.get("ledDisplayConfig");
                    String memberLeft = (String) ledMap.get("memberLeft");

                    playParams.put("carPlate",carPlate);
                    playParams.put("carparkName",carparkInfo.getCarparkName());
                    playParams.put("availableCarSpace",carparkInfo.getAvailableCarSpace());
                    playParams.put("inOutTime",outTime.toString());
                    playParams.put("parkingLength",parkingLen);
                    playParams.put("kindName",CommonUtils.isEmpty(memberKind) ? "" : memberKind.getKindName());
                    playParams.put("chargeAmount",String.valueOf(chargeAmount));
                    playParams.put("limitReason",openGateModeInfo.get("limiReason"));
                    playParams.put("memberLeft",memberLeft);
                    playParams.put("isMultiCarno",isMultiCarno);
                    //playParams.put("driverName",driverName);
                    deviceBizService.PlayLedInfo(deviceId,1,playParams,ledDisplayConfig,ledType,ledId);
                }

            }
        } catch (Exception e) {
            LOGGER.error("推送手动录入记录异常" + e.getMessage());
        }
        return json;
    }

    @Override
    public JSONObject shiftOperator(JSONObject params, SocketClient socketClient) throws BizException {
        //系统登录
        String userName = params.get("username").toString();
        String passWord = params.get("password").toString();
        String shiftGiver = params.get("shiftGiver").toString();
        String realAmount = params.get("realAmount").toString();
        String hql = "from Operator where operatorUserName = ? and useMark >= 0";
        Operator operator = (Operator)baseDao.getUnique(hql,userName);
        if(CommonUtils.isEmpty(operator)){
            throw new BizException("用户名不存在");
        }
        String md5Pwd = new SimpleHash("md5", passWord, null, 1).toString();
        if(!operator.getOperatorUserPwd().equals(md5Pwd)){
            throw new BizException("用户密码错误");
        }

        for(int i = 0; i < ParkSocketServer.getClientsCount(); i++){
            SocketClient client = ParkSocketServer.getClient(i);
            if(!CommonUtils.isEmpty(client.getOperator())){
                if(client.getOperator().getOperatorUserName().equals(userName)){
                    throw new BizException("该用户已登录");
                }
            }
        }
        socketClient.setOperator(operator);
        String carparkId = "";
        if (!CommonUtils.isEmpty(socketClient.getDeviceInfo())) {
            InOutCarRoadInfo inOutCarRoadInfo = (InOutCarRoadInfo) baseDao.getById(InOutCarRoadInfo.class, socketClient.getDeviceInfo().getOwnCarRoad());
            if (!CommonUtils.isEmpty(inOutCarRoadInfo))
                carparkId = inOutCarRoadInfo.getOwnCarparkNo();
        }
        PostComputerManage postComputerManage = socketClient.getPostComputerManage();
        ShiftChangeStatInfo shiftChangeStatInfo = new ShiftChangeStatInfo();
        shiftChangeStatInfo.setShiftGiver(shiftGiver);
        shiftChangeStatInfo.setShiftGiverWorkTime(CommonUtils.getTimestamp());
        shiftChangeStatInfo.setShiftTaker(userName);
        shiftChangeStatInfo.setShiftChangeTime(CommonUtils.getTimestamp());
        shiftChangeStatInfo.setRealAmount(Double.valueOf(realAmount));
        shiftChangeStatInfo.setOperationSource(postComputerManage.getPostComputerId());
        shiftChangeStatInfo.setAddTime(CommonUtils.getTimestamp());
        shiftChangeStatInfo.setBoothName(postComputerManage.getPostComputerName());
        shiftChangeStatInfo.setDepartmentId(postComputerManage.getDepartmentId());
        shiftChangeStatInfo.setCarparkId(carparkId);
        baseDao.save(shiftChangeStatInfo);



        //返回数据
        JSONObject res = new JSONObject();
        res.put("userName",userName);
        res.put("passWord",passWord);
        logOperationService.addOperatorLog(LogEnum.clientLogin,userName + "用户交接" + shiftGiver + "用户",socketClient.getSocket().getInetAddress().getHostAddress(),socketClient.getOperator(),null);
        return res;
    }

    @Override
    public void reconnectLogin(String username, String password, String ips, SocketClient socketClient) throws BizException {
        String hql = "from Operator where operatorUserName = ? and useMark >= 0";
        Operator operator = (Operator)baseDao.getUnique(hql,username);
        if(CommonUtils.isEmpty(operator)){
            throw new BizException("用户名不存在");
        }
        String md5Pwd = new SimpleHash("md5", password, null, 1).toString();
        if(!operator.getOperatorUserPwd().equals(md5Pwd)){
            throw new BizException("用户密码错误");
        }

        PostComputerManage postComputerManage = null;
        String[] ipArray = ips.split(",");
        for(String ip : ipArray){
            hql = "from PostComputerManage where postComputerIp = ? and useMark >= 0";
            postComputerManage = (PostComputerManage)baseDao.getUnique(hql,ip);
            if(!CommonUtils.isEmpty(postComputerManage)){
                break;
            }
        }
        if(CommonUtils.isEmpty(postComputerManage)){
            throw new BizException("岗亭IP信息不存在");
        }
        socketClient.setChannelId(postComputerManage.getPostComputerId());
        socketClient.setOperator(operator);
        socketClient.setPostComputerManage(postComputerManage);
    }

    @Override
    public void updatePostComputerStatus(PostComputerManage computerManage, int status) {
        if(!CommonUtils.isEmpty(computerManage)){
            computerManage.setStatus(status);
            baseDao.update(computerManage);
        }
    }

    @Override
    public PostComputerManage getPostComputerManage(String roadId){
        PostComputerManage postComputerManage = new PostComputerManage();
        String hql = "from InOutCarRoadInfo where carRoadId = ? and useMark >= 0";
        InOutCarRoadInfo inOutCarRoadInfo = (InOutCarRoadInfo)baseDao.getUnique(hql,roadId);
        hql = "from PostComputerManage where postComputerId = ? and useMark >= 0";
        postComputerManage = (PostComputerManage)baseDao.getUnique(hql,inOutCarRoadInfo.getManageComputerId());
        return postComputerManage;
    }

    @Override
    public String getCarTypeInfo(String packKind){
        String res = "";
        if (packKind.equals("-1")){
            res = "黑名单";
        }else if (packKind.equals("-2")){
            res = "预约车";
        }else if (packKind.equals("-3")){
            res = "免费车";
        }else if (packKind.equals("0")){
            res = "包月车";
        }else if (packKind.equals("1")){
            res = "包次车";
        }else {
            res = "临时车";
        }
        return res;
    }

    @Override
    public Map<String,String> IsNeedOpenGate(String carPlate,Integer inOutType,Timestamp inOutTime,CarparkInfo carparkInfo,MemberWallet memberWallet,
                                   String gateState,double chargeAmount){
        Map<String,String> res = new HashedMap();
        String isNeedOpenGate = "0";             //0-不开闸；1-开闸
        Integer openGateState;              //开闸模式
        String limiReason = "临时车限制通行";

        if((gateState.equals("0")) || (gateState.equals("2")))
            openGateState = 1;              //全部开闸
        else if (gateState.equals("3"))
            openGateState = -1;             //全部不开闸
        else
            openGateState = 0;              //白名单开闸

        if (!CommonUtils.isEmpty(memberWallet)) {
            res = GetMemberWalletInfo(carPlate, inOutType, inOutTime, memberWallet);
        }
        if(inOutType.equals(0)){
            switch (openGateState){
                case -1:
                {
                    isNeedOpenGate = "0";
                    limiReason = "全部车辆限行";
                    break;
                }
                case 0:
                {
                    if(!CommonUtils.isEmpty(memberWallet)){
                        //非临时车
                        limiReason = "";
                        if(res.get("isValid").toString().equals("1")) {
                            //车场封闭
                            if ("1".equals(carparkInfo.getIsClose())){
                                if ("0".equals(carparkInfo.getCloseType())){    //封闭车场类型为剩余总车位临界值
                                    if (carparkInfo.getCriticalValue()>carparkInfo.getAvailableCarSpace()){
                                        if (res.get("isUseParkinglot").toString().equals("1")) { //使用固定车位
                                            //过期车辆不允许入场，车辆过期
                                            isNeedOpenGate = (carparkInfo.getIsOverdueAutoOpen().equals("1") && res.get("isOverdue").toString().equals("1"))? "0" : "1";
                                            if (isNeedOpenGate.equals("0"))
                                                limiReason = "过期车辆禁止入场";
                                        }else {
                                            isNeedOpenGate = "0";
                                            limiReason = "车场封闭限制通行";
                                        }
                                    }else {
                                        isNeedOpenGate = (carparkInfo.getIsOverdueAutoOpen().equals("1") && res.get("isOverdue").toString().equals("1"))? "0" : "1";
                                        if (isNeedOpenGate.equals("0"))
                                            limiReason = "过期车辆禁止入场";
                                    }
                                }else {                                         //封闭车场类型为剩余大场车位临界值
                                    String hql = "from CarparkInfo where ownCarparkNo = ? and useMark >= 0";
                                    List<CarparkInfo> carparkInfoList = (List<CarparkInfo>)baseDao.queryForList(hql,carparkInfo.getCarparkId());
                                    Integer carSpaceSonPark = 0;        //子车场的车位数
                                    for (CarparkInfo carparkInfoSon : carparkInfoList){
                                        carSpaceSonPark = carSpaceSonPark + carparkInfoSon.getAvailableCarSpace();
                                    }
                                    Integer carSpaceFatherPark = carparkInfo.getAvailableCarSpace() - carSpaceSonPark;  //剩余大场车位数
                                    if (carparkInfo.getCriticalValue() > carSpaceFatherPark){
                                        if (res.get("isUseParkinglot").toString().equals("1")) { //使用固定车位
                                            //过期车辆不允许入场，车辆过期
                                            isNeedOpenGate = (carparkInfo.getIsOverdueAutoOpen().equals("1") && res.get("isOverdue").toString().equals("1"))? "0" : "1";
                                            if (isNeedOpenGate.equals("0"))
                                                limiReason = "过期车辆禁止入场";
                                        }else {
                                            isNeedOpenGate = "0";
                                            limiReason = "车场封闭限制通行";
                                        }
                                    }else {
                                        isNeedOpenGate = (carparkInfo.getIsOverdueAutoOpen().equals("1") && res.get("isOverdue").toString().equals("1"))? "0" : "1";
                                        if (isNeedOpenGate.equals("0"))
                                            limiReason = "过期车辆禁止入场";
                                    }
                                }
                            }else {
                                isNeedOpenGate = (carparkInfo.getIsOverdueAutoOpen().equals("1") && res.get("isOverdue").toString().equals("1"))? "0" : "1";
                                if (isNeedOpenGate.equals("0"))
                                    limiReason = "过期车辆禁止入场";
                            }
                        }else{
                            //黑名单车辆
                            isNeedOpenGate = "0";
                            limiReason = "黑名单禁止入场";
                        }

                    }else{
                        //临时车
                        isNeedOpenGate = "0";
                        limiReason = "临时车限制通行";
                    }
                    break;
                }
                case 1:
                {
                    isNeedOpenGate = "1";
                    String hql = "from CarparkInfo where ownCarparkNo = ? and useMark >= 0";
                    List<CarparkInfo> carparkInfoList = (List<CarparkInfo>)baseDao.queryForList(hql,carparkInfo.getCarparkId());
                    Integer carSpaceSonPark = 0;        //子车场的车位数
                    for (CarparkInfo carparkInfoSon : carparkInfoList){
                        carSpaceSonPark = carSpaceSonPark + carparkInfoSon.getAvailableCarSpace();
                    }
                    Integer carSpaceFatherPark = carparkInfo.getAvailableCarSpace() - carSpaceSonPark;  //剩余大场车位数
                    if (carparkInfo.getCriticalValue() > carSpaceFatherPark){
                        if (res.containsKey("isUseParkinglot") && res.get("isUseParkinglot").toString().equals("1")) { //使用固定车位
                            //过期车辆不允许入场，车辆过期
                            isNeedOpenGate = (carparkInfo.getIsOverdueAutoOpen().equals("1") && res.get("isOverdue").toString().equals("1"))? "0" : "1";
                            if (isNeedOpenGate.equals("0"))
                                limiReason = "过期车辆禁止入场";
                        }else {
                            /*isNeedOpenGate = "0";
                            limiReason = "车场封闭限制通行";*/
                            return null;
                        }
                    }
                    break;
                }
            }
        }else{      //出场车辆
            switch (openGateState){
                case -1:
                {
                    isNeedOpenGate = "0";
                    limiReason = "全部车辆限行";
                    break;
                }
                case 0:
                {
                    if(!CommonUtils.isEmpty(memberWallet)){
                        //非临时车
                        //res =  GetMemberWalletInfo(carPlate,inOutType,inOutTime,memberWallet);
                        if(res.get("isValid").toString().equals("1")){
                            //白名单出场
                            if(chargeAmount > 0){
                                isNeedOpenGate = "0";
                                limiReason = "缴费车辆";

                            }else{
                                //收费0元自动放行只针对外部临时车
                                isNeedOpenGate =  "1";
                                limiReason = "";
                            }
                        }else{
                            //黑名单禁止出场
                            isNeedOpenGate = "0";
                            limiReason = "黑名单限行";
                        }
                    }else{
                        //临时车出场
                        if(chargeAmount > 0){
                            isNeedOpenGate = "0";
                            limiReason = "缴费车辆";
                        }else{
                            isNeedOpenGate =  carparkInfo.getIsAutoOpen().equals("0") ? "1" : "0";
                            if (isNeedOpenGate.equals("0"))
                                limiReason = "缴费0元停车检查";
                            else
                                limiReason = "";
                        }
                    }
                    break;
                }
                case 1:
                {
                    isNeedOpenGate = "1";
                    limiReason = "";
                    break;
                }
            }
        }
        res.put("isNeedOpenGate",isNeedOpenGate);
        res.put("limiReason",limiReason);
        return res;
    }
    @Override
    //获取会员信息
    public Map<String,String> GetMemberWalletInfo(String carPlate,Integer inOutType,Timestamp inOutTime,MemberWallet memberWallet){

        Map<String,String> res = new HashedMap();
        //黑名单车无效,置为0
        if(memberWallet.getPackKindId().equals(-1))
            res.put("isValid","0");
        else
            res.put("isValid","1");

        //判断是否过期
        switch(memberWallet.getPackKindId()){
            case -3:case -2:case 0:{
                if(memberWallet.getEffectiveStartTime().getTime() <= inOutTime.getTime() && inOutTime.getTime() <= memberWallet.getEffectiveEndTime().getTime())
                    res.put("isOverdue","0");
                else
                    res.put("isOverdue","1");
                break;
            }
            case 1:{
                if(memberWallet.getSurplusNumber() > 0)
                    res.put("isOverdue","0");
                else
                    res.put("isOverdue","1");
                break;
            }
            case 2:{
                if(memberWallet.getSurplusAmount() > 0)
                    res.put("isOverdue","0");
                else
                    res.put("isOverdue","1");
                break;
            }
            default:{
                //黑名单车统一算作过期
                res.put("isOverdue","0");
                break;
            }
        }

        //两者相等代表是单辆车，否者的话代表的是A,B车
        if(carPlate.equals(memberWallet.getMenberNo())){
            res.put("isUseWallet","1");
            String isUseParkinglot = CommonUtils.isEmpty(memberWallet.getParkingLotId()) ? "0" : "1";
            res.put("isUseParkinglot",isUseParkinglot);
            res.put("isMultiCarno","0");    //isMultiCarno是否是AB车 0-否；1-是
        }else{
            res.put("isMultiCarno","1");
            if(inOutType.equals(0)){
                String multiCarno = GetOtherCarno(carPlate,memberWallet.getMenberNo());
                String hql = "select charge_info_id from order_inout_record where"
                        + " car_no in (" + multiCarno + ")"
                        + " and carpark_id = '" + memberWallet.getCarPark()
                        + "' and in_time <= '" + inOutTime.toString()
                        + "' and out_time IS NULL";
                SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
                List<Map<String, Object>> orderInoutRecordList = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

                //场内车辆数
                Integer occupiedNum = orderInoutRecordList.size();
                String isUseWallet = occupiedNum >= Integer.valueOf(memberWallet.getValidMenberCount()) ? "0" : "1";
                String isUseParkinglot = occupiedNum >= memberWallet.getParkingLotId().split(",").length ? "0" : "1";
                res.put("isUseWallet",isUseWallet);
                res.put("isUseParkinglot",isUseParkinglot);
            }
            else{
                String multiCarno = GetOtherCarno(carPlate,memberWallet.getMenberNo());
                String hql = "select charge_info_id from order_inout_record where"
                        + " car_no in (" + multiCarno + ")"
                        + " and carpark_id = '" + memberWallet.getCarPark()
                        + "' and in_time <= '" + inOutTime.toString()
                        + "' and out_time IS NULL";
                SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
                List<Map<String, Object>> orderInoutRecordList = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
                //场内车辆数
                Integer occupiedNum = orderInoutRecordList.size();
                String isUseWallet = occupiedNum >= Integer.valueOf(memberWallet.getValidMenberCount()) ? "0" : "1";
                String isUseParkinglot = occupiedNum >= memberWallet.getParkingLotId().split(",").length ? "0" : "1";
                res.put("isUseWallet",isUseWallet);
                res.put("isUseParkinglot",isUseParkinglot);
            }
        }
        return res;
    }

    @Override
    //获取A,B车其他车辆
    public String GetOtherCarno(String carPlate,String memberNo){
        String res = "";
        StringBuffer resBuf = new StringBuffer();
        String[] resArray = memberNo.split(",");
        for (int i = 0;i<resArray.length;i++){
            if (!resArray[i].equals(carPlate) && !StringUtils.isBlank(resArray[i]))
                resBuf.append("'" + resArray[i] + "',");
        }
        res = resBuf.toString();

        String resLastStr = res.substring(res.length() - 1,res.length());
        if (resLastStr.equals(","))
            res = res.substring(0,res.length() -1);
        return  res;
    }

    @Override
    //获取入场匹配信息
    public OrderInoutRecord GetMatchInfo(String carPlate,String carparkId,Timestamp outTime){
        OrderInoutRecord matchInfo = new OrderInoutRecord();

        String hql = "from OrderInoutRecord where carNo = ? and carparkId = ? and "
                + "inTime <= ? and outRecordId IS NULL";
        matchInfo = (OrderInoutRecord)baseDao.getUnique(hql,carPlate,carparkId,outTime);
        return  matchInfo;
    }

    @Override
    //base64的转化
    public String ChangeBase64ToStr(String str){
        String res = "";
        if(!StringUtils.isBlank(str)){
            byte[] strs = Base64.decode(str);
            try {
                res = new String(strs,"GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return  res;
    }

    @Override
    public void WorkForMultiCarno(String carno,Integer inOutType,String carparkId,OrderInoutRecord orderInoutRecord){
        try {
            String hql = "from MemberWallet where menberNo like ? and carPark = ? AND useMark >= 0";
            MemberWallet memberWallet = (MemberWallet)baseDao.getUnique(hql,"%" + carno + "%",carparkId);
            //只有白名单才可以加入判断
            if (!CommonUtils.isEmpty(memberWallet)){
                //只操作包月或免费的
                if (memberWallet.getPackKindId().equals(0) || memberWallet.getPackKindId().equals(-3)){
                    //白名单包含多车牌
                    if (!carno.equals(memberWallet.getMenberNo())){
                        //车牌数大于有效数
                        if (memberWallet.getMenberNo().split(",").length > Integer.valueOf(memberWallet.getValidMenberCount())){
                            String multiCarno = GetOtherCarno(carno,memberWallet.getMenberNo());
                            hql = "select charge_info_id from order_inout_record where"
                                    + " car_no in (" + multiCarno + ")"
                                    + " and carpark_id = '" + memberWallet.getCarPark()
                                    + "' and out_time IS NULL";
                            SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
                            List<Map<String, Object>> idLiST = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
                            if (inOutType.equals(0)){
                                if (idLiST.size() <= 0) {
                                    orderInoutRecord.setInChargeTime(orderInoutRecord.getInTime());
                                    baseDao.save(orderInoutRecord);
                                    newOrderParkService.addChargeInTimeInfoToCloud(orderInoutRecord.getChargeInfoId(),orderInoutRecord.getInTime().toString());
                                }else {
                                    List<OrderInoutRecord> orderInoutRecordList = new ArrayList<>();
                                    for (Map map : idLiST){
                                        String orderId = map.get("charge_info_id").toString();
                                        OrderInoutRecord orderInoutRecord1 = (OrderInoutRecord) baseDao.getById(OrderInoutRecord.class,orderId);
                                        orderInoutRecordList.add(orderInoutRecord1);
                                    }

                                    Timestamp chargeInTime = orderInoutRecord.getInTime();
                                    //选择最近的入场时间作为计费的开始时间
                                    for (OrderInoutRecord orderInoutRecord1 : orderInoutRecordList){
                                        if (orderInoutRecord1.getInTime().getTime() >= chargeInTime.getTime())
                                            chargeInTime = orderInoutRecord1.getInTime();
                                    }
                                    for (OrderInoutRecord orderInoutRecord1 : orderInoutRecordList){
                                        orderInoutRecord1.setInChargeTime(chargeInTime);
                                        baseDao.save(orderInoutRecord1);
                                        newOrderParkService.addChargeInTimeInfoToCloud(orderInoutRecord1.getChargeInfoId(),orderInoutRecord1.getInTime().toString());
                                    }
                                    orderInoutRecord.setInChargeTime(chargeInTime);
                                    baseDao.save(orderInoutRecord);
                                    newOrderParkService.addChargeInTimeInfoToCloud(orderInoutRecord.getChargeInfoId(),orderInoutRecord.getInTime().toString());
                                }

                            }else{
                                if (idLiST.size() > 0) {
                                    List<OrderInoutRecord> orderInoutRecordList = new ArrayList<>();
                                    for (Map map : idLiST){
                                        String orderId = map.get("charge_info_id").toString();
                                        OrderInoutRecord orderInoutRecord1 = (OrderInoutRecord) baseDao.getById(OrderInoutRecord.class,orderId);
                                        orderInoutRecordList.add(orderInoutRecord1);
                                    }
                                    //入场缴费时间更新
                                    Timestamp chargeInTime = orderInoutRecordList.get(0).getInTime();

                                    for (OrderInoutRecord orderInoutRecord1 : orderInoutRecordList){
                                        if (orderInoutRecord1.getInTime().getTime() >= chargeInTime.getTime())
                                            chargeInTime = orderInoutRecord1.getInTime();
                                    }
                                    for (OrderInoutRecord orderInoutRecord1 : orderInoutRecordList){
                                        orderInoutRecord1.setInChargeTime(chargeInTime);
                                        baseDao.save(orderInoutRecord1);
                                        newOrderParkService.addChargeInTimeInfoToCloud(orderInoutRecord1.getChargeInfoId(),chargeInTime.toString());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("处理AB车异常");
        }
    }

    @Override
    public String CarnoIntelligentCorrect(String correctStr,String oldCarno) {
        String resultStr = oldCarno;
        String[] correctArr = correctStr.split(";");
        if (correctArr.length > 0){
            for (int i = 0;i<correctArr.length;i++){
                String [] singleArr = correctArr[i].split(",");
                if (singleArr.length == 2){
                    if (oldCarno.equals(singleArr[0]))
                        resultStr = singleArr[1];
                }
            }
        }
        return resultStr;
    }

}
