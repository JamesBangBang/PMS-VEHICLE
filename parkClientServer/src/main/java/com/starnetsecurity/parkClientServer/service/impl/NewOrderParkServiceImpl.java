package com.starnetsecurity.parkClientServer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.commonService.util.DeviceManageUtils;
import com.starnetsecurity.commonService.util.StarnetDeviceUtils;
import com.starnetsecurity.parkClientServer.cloudTransport.CloudTransEnum;
import com.starnetsecurity.parkClientServer.cloudTransport.CloudTransPackage;
import com.starnetsecurity.parkClientServer.entity.*;
import com.starnetsecurity.parkClientServer.entityEnum.payStatusEnum;
import com.starnetsecurity.parkClientServer.entityEnum.payTypeEnum;
import com.starnetsecurity.parkClientServer.init.AppInfo;
import com.starnetsecurity.parkClientServer.mq.RabbitMqListener;
import com.starnetsecurity.parkClientServer.service.NewOrderParkService;
import com.starnetsecurity.parkClientServer.sockServer.*;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.codec.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional
public class NewOrderParkServiceImpl implements NewOrderParkService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqListener.class);

    @Autowired
    HibernateBaseDao baseDao;


    @Override
    public void addInParkOrderToCloud(InoutRecordInfo inoutRecordInfo, String orderOldId,OrderInoutRecord orderInoutRecord) throws BizException {
        if(!AppInfo.isConnectCloud.equals("on")){
            return;
        }

        if (("金山园区".equals(inoutRecordInfo.getCarparkName())) || ("海西园区".equals(inoutRecordInfo.getCarparkName()))){
            return;
        }

        JSONObject inParkElement = new JSONObject();
        if (CommonUtils.isEmpty(inoutRecordInfo)){
            throw new BizException("入场记录错误");
        }
        if (CommonUtils.isEmpty(orderInoutRecord)){
            throw new BizException("入场订单错误");
        }
        inParkElement.put("inoutRecordId",inoutRecordInfo.getInoutRecordId());
        inParkElement.put("carNo",inoutRecordInfo.getCarNo());
        inParkElement.put("inoutTime",inoutRecordInfo.getInoutTime());
        inParkElement.put("inoutFlag",inoutRecordInfo.getInoutFlag());
        inParkElement.put("inoutStatus",inoutRecordInfo.getInoutStatus());
        inParkElement.put("carparkId",inoutRecordInfo.getCarparkId());
        inParkElement.put("carparkName",inoutRecordInfo.getCarparkName());
        inParkElement.put("carRoadId",inoutRecordInfo.getCarRoadId());
        inParkElement.put("carRoadName",inoutRecordInfo.getCarRoadName());
        inParkElement.put("postId",inoutRecordInfo.getPostId());
        inParkElement.put("postName",inoutRecordInfo.getPostName());
        inParkElement.put("cameraId",inoutRecordInfo.getCameraId());
        inParkElement.put("carNoColor",inoutRecordInfo.getCarNoColor());
        inParkElement.put("carType",inoutRecordInfo.getCarType());
        inParkElement.put("operatorId",inoutRecordInfo.getOperatorId());
        inParkElement.put("operatorName",inoutRecordInfo.getOperatorName());
        inParkElement.put("remark",inoutRecordInfo.getRemark());
        inParkElement.put("serviceId",AppInfo.cloudServerId);
        inParkElement.put("orderOldId",orderOldId);
        inParkElement.put("chargeInfoId",orderInoutRecord.getChargeInfoId());
        inParkElement.put("orderRemark",orderInoutRecord.getRemark());
        inParkElement.put("isUseUparkPay",AppInfo.isUseUparkPay);
        inParkElement.put("bussCode",AppInfo.bussCode);
        inParkElement.put("bussKey",AppInfo.bussKey);
        CloudTransPackage cloudTransPackage = new CloudTransPackage();
        cloudTransPackage.setUploadData(inParkElement);
        cloudTransPackage.setCloudTransEnum(CloudTransEnum.inPark);
        AppInfo.cloudTransQueue.offer(cloudTransPackage);
    }

    @Override
    public void addOutParkOrderToCloud(InoutRecordInfo inoutRecordInfo, OrderInoutRecord orderInoutRecord, OrderTransaction orderTransaction) throws BizException {
        if(!AppInfo.isConnectCloud.equals("on")){
            return;
        }

        if (("金山园区".equals(inoutRecordInfo.getCarparkName())) || ("海西园区".equals(inoutRecordInfo.getCarparkName()))){
            return;
        }

        JSONObject outParkElement = new JSONObject();
        if (CommonUtils.isEmpty(inoutRecordInfo)){
            throw new BizException("出场记录错误");
        }
        if (CommonUtils.isEmpty(orderInoutRecord)){
            throw new BizException("出场订单错误");
        }
        String transId = "";
        if (!CommonUtils.isEmpty(orderTransaction))
            transId = orderTransaction.getTransactionId();
        outParkElement.put("inoutRecordId",inoutRecordInfo.getInoutRecordId());
        outParkElement.put("carNo",inoutRecordInfo.getCarNo());
        outParkElement.put("inoutTime",inoutRecordInfo.getInoutTime());
        outParkElement.put("inoutFlag",inoutRecordInfo.getInoutFlag());
        outParkElement.put("inoutStatus",inoutRecordInfo.getInoutStatus());
        outParkElement.put("carparkId",inoutRecordInfo.getCarparkId());
        outParkElement.put("carparkName",inoutRecordInfo.getCarparkName());
        outParkElement.put("carRoadId",inoutRecordInfo.getCarRoadId());
        outParkElement.put("carRoadName",inoutRecordInfo.getCarRoadName());
        outParkElement.put("postId",inoutRecordInfo.getPostId());
        outParkElement.put("postName",inoutRecordInfo.getPostName());
        outParkElement.put("cameraId",inoutRecordInfo.getCameraId());
        outParkElement.put("carNoColor",inoutRecordInfo.getCarNoColor());
        outParkElement.put("carType",inoutRecordInfo.getCarType());
        outParkElement.put("operatorId",inoutRecordInfo.getOperatorId());
        outParkElement.put("operatorName",inoutRecordInfo.getOperatorName());
        outParkElement.put("remark",inoutRecordInfo.getRemark());
        outParkElement.put("serviceId",AppInfo.cloudServerId);
        outParkElement.put("chargeInfoId",orderInoutRecord.getChargeInfoId());
        outParkElement.put("inCarRoadId",orderInoutRecord.getInCarRoadId());
        outParkElement.put("inCarRoadName",orderInoutRecord.getInCarRoadName());
        outParkElement.put("chargeTime",orderInoutRecord.getChargeTime());
        outParkElement.put("inTime",orderInoutRecord.getInTime());
        outParkElement.put("stayTime",orderInoutRecord.getStayTime());
        outParkElement.put("chargeReceivableAmount",orderInoutRecord.getChargeReceivableAmount());
        outParkElement.put("chargeActualAmount",orderInoutRecord.getChargeActualAmount());
        outParkElement.put("chargePreAmount",orderInoutRecord.getChargePreAmount());
        outParkElement.put("inRecordId",orderInoutRecord.getInRecordId());
        outParkElement.put("releaseType",orderInoutRecord.getReleaseType());
        outParkElement.put("releaseReason",orderInoutRecord.getReleaseReason());
        outParkElement.put("carNoAttribute",orderInoutRecord.getCarNoAttribute());
        outParkElement.put("orderRemark",orderInoutRecord.getRemark());
        outParkElement.put("transId",transId);
        outParkElement.put("orderTransactionElement",orderTransaction);
        outParkElement.put("isUseUparkPay",AppInfo.isUseUparkPay);
        outParkElement.put("bussCode",AppInfo.bussCode);
        outParkElement.put("bussKey",AppInfo.bussKey);
        CloudTransPackage cloudTransPackage = new CloudTransPackage();
        cloudTransPackage.setUploadData(outParkElement);
        cloudTransPackage.setCloudTransEnum(CloudTransEnum.outPark);
        AppInfo.cloudTransQueue.offer(cloudTransPackage);

        if ((orderInoutRecord.getChargeActualAmount() <= 300) && (orderInoutRecord.getReleaseType().equals(4))){
            //金额小于300且无感支付，推送账单
            String payAmt = (new BigDecimal(orderInoutRecord.getChargeActualAmount()).multiply(new BigDecimal("100"))).setScale(0,BigDecimal.ROUND_UP) + "";
            pushBillToUparkCloud(orderInoutRecord.getChargeInfoId(),orderInoutRecord.getCarNo(),orderInoutRecord.getCarparkId(),orderInoutRecord.getCarparkName(),
                    payAmt,payAmt,orderInoutRecord.getInTime().toString(), orderInoutRecord.getOutTime().toString(),"");
        }
    }

    @Override
    public void addChargeInTimeInfoToCloud(String orderId, String inChargeTime) {
        if(!AppInfo.isConnectCloud.equals("on")){
            return;
        }
        JSONObject chargeInTimeElement = new JSONObject();
        chargeInTimeElement.put("orderId",orderId);
        chargeInTimeElement.put("inChargeTime",inChargeTime);
        CloudTransPackage cloudTransPackage = new CloudTransPackage();
        cloudTransPackage.setUploadData(chargeInTimeElement);
        cloudTransPackage.setCloudTransEnum(CloudTransEnum.chargeInTime);
        AppInfo.cloudTransQueue.offer(cloudTransPackage);
    }

    @Override
    public List<WechatPayFailUrl> getWechatPayFailUrlList() {
        //result为0代表记录尚未更新
        String hql = "from WechatPayFailUrl where result = 0";
        return (List<WechatPayFailUrl>)baseDao.queryForList(hql);
    }

    @Override
    public JSONObject getOrderInfoByUrl(Integer isUsed, String url) {
        JSONObject res = new JSONObject();
        if (isUsed.equals(0)){
            String[] orderArr = url.split(",");
            Integer arrLen = Integer.valueOf(orderArr.length);
            String orderOldId = "";
            if (arrLen.equals(3)){
                orderOldId = orderArr[2];
            }
            InoutRecordInfo inoutRecordInfo = (InoutRecordInfo) baseDao.getById(InoutRecordInfo.class,orderArr[0]);
            if (CommonUtils.isEmpty(inoutRecordInfo)){
                return null;
            }
            OrderInoutRecord orderInoutRecord = (OrderInoutRecord) baseDao.getById(OrderInoutRecord.class,orderArr[1]);
            if (CommonUtils.isEmpty(orderInoutRecord)){
                return null;
            }
            if (!CommonUtils.isEmpty(orderInoutRecord.getOutRecordId())){
                //重传的时候已经出场
                return null;
            }
            res.put("inoutRecordId",inoutRecordInfo.getInoutRecordId());
            res.put("carNo",inoutRecordInfo.getCarNo());
            res.put("inoutTime",inoutRecordInfo.getInoutTime());
            res.put("inoutFlag",inoutRecordInfo.getInoutFlag());
            res.put("inoutStatus",inoutRecordInfo.getInoutStatus());
            res.put("carparkId",inoutRecordInfo.getCarparkId());
            res.put("carparkName",inoutRecordInfo.getCarparkName());
            res.put("carRoadId",inoutRecordInfo.getCarRoadId());
            res.put("carRoadName",inoutRecordInfo.getCarRoadName());
            res.put("postId",inoutRecordInfo.getPostId());
            res.put("postName",inoutRecordInfo.getPostName());
            res.put("cameraId",inoutRecordInfo.getCameraId());
            res.put("carNoColor",inoutRecordInfo.getCarNoColor());
            res.put("carType",inoutRecordInfo.getCarType());
            res.put("operatorId",inoutRecordInfo.getOperatorId());
            res.put("operatorName",inoutRecordInfo.getOperatorName());
            res.put("remark",inoutRecordInfo.getRemark());
            res.put("serviceId",AppInfo.cloudServerId);
            res.put("orderOldId",orderOldId);
            res.put("chargeInfoId",orderInoutRecord.getChargeInfoId());
            res.put("chargeTime",orderInoutRecord.getChargeTime());
            res.put("inChargeTime",orderInoutRecord.getInChargeTime());
            res.put("releaseType",orderInoutRecord.getReleaseType());
            res.put("releaseReason",orderInoutRecord.getReleaseReason());
            res.put("carNoAttribute",orderInoutRecord.getCarNoAttribute());
            res.put("orderRemark",orderInoutRecord.getRemark());
            res.put("isUseUparkPay",AppInfo.isUseUparkPay);
            res.put("bussCode",AppInfo.bussCode);
            res.put("bussKey",AppInfo.bussKey);
        }else if (isUsed.equals(1)){
            String[] orderArr = url.split(",");
            Integer arrLen = Integer.valueOf(orderArr.length);
            String transId = "";
            if (arrLen.equals(3)){
                transId = orderArr[2];
            }
            InoutRecordInfo inoutRecordInfo = (InoutRecordInfo) baseDao.getById(InoutRecordInfo.class,orderArr[0]);
            OrderInoutRecord orderInoutRecord = (OrderInoutRecord) baseDao.getById(OrderInoutRecord.class,orderArr[1]);
            res.put("inoutRecordId",inoutRecordInfo.getInoutRecordId());
            res.put("carNo",inoutRecordInfo.getCarNo());
            res.put("inoutTime",inoutRecordInfo.getInoutTime());
            res.put("inoutFlag",inoutRecordInfo.getInoutFlag());
            res.put("inoutStatus",inoutRecordInfo.getInoutStatus());
            res.put("carparkId",inoutRecordInfo.getCarparkId());
            res.put("carparkName",inoutRecordInfo.getCarparkName());
            res.put("carRoadId",inoutRecordInfo.getCarRoadId());
            res.put("carRoadName",inoutRecordInfo.getCarRoadName());
            res.put("postId",inoutRecordInfo.getPostId());
            res.put("postName",inoutRecordInfo.getPostName());
            res.put("cameraId",inoutRecordInfo.getCameraId());
            res.put("carNoColor",inoutRecordInfo.getCarNoColor());
            res.put("carType",inoutRecordInfo.getCarType());
            res.put("operatorId",inoutRecordInfo.getOperatorId());
            res.put("operatorName",inoutRecordInfo.getOperatorName());
            res.put("remark",inoutRecordInfo.getRemark());
            res.put("serviceId",AppInfo.cloudServerId);
            res.put("chargeInfoId",orderInoutRecord.getChargeInfoId());
            res.put("inCarRoadId",orderInoutRecord.getInCarRoadId());
            res.put("inCarRoadName",orderInoutRecord.getInCarRoadName());
            res.put("chargeTime",orderInoutRecord.getChargeTime());
            res.put("inTime",orderInoutRecord.getInTime());
            res.put("inChargeTime",orderInoutRecord.getInChargeTime());
            res.put("stayTime",orderInoutRecord.getStayTime());
            res.put("chargeReceivableAmount",orderInoutRecord.getChargeReceivableAmount());
            res.put("chargeActualAmount",orderInoutRecord.getChargeActualAmount());
            res.put("chargePreAmount",orderInoutRecord.getChargePreAmount());
            res.put("inRecordId",orderInoutRecord.getInRecordId());
            res.put("releaseType",orderInoutRecord.getReleaseType());
            res.put("releaseReason",orderInoutRecord.getReleaseReason());
            res.put("carNoAttribute",orderInoutRecord.getCarNoAttribute());
            res.put("orderRemark",orderInoutRecord.getRemark());
            res.put("transId",transId);
            res.put("isUseUparkPay",AppInfo.isUseUparkPay);
            res.put("bussCode",AppInfo.bussCode);
            res.put("bussKey",AppInfo.bussKey);
            if (!CommonUtils.isEmpty(transId)) {
                OrderTransaction orderTransaction = (OrderTransaction)baseDao.getById(OrderTransaction.class,transId);
                res.put("orderTransactionElement",orderTransaction);
            }else{
                res.put("orderTransactionElement",null);
            }

        }else if (isUsed.equals(2)){
            String[] orderArr = url.split(",");
            res.put("orderId",orderArr[0]);
            res.put("inChargeTime",orderArr[1]);
        }
        return res;
    }

    @Override
    public void deleteSuccessInfo(String failId) {
        baseDao.deleteById(WechatPayFailUrl.class,failId);
    }

    @Override
    public void handleFailInfo(WechatPayFailUrl wechatPayFailUrl) {
        String[] orderArr = wechatPayFailUrl.getUrl().split(",");
        Integer arrLen = Integer.valueOf(orderArr.length);
        InoutRecordInfo inoutRecordInfo = (InoutRecordInfo) baseDao.getById(InoutRecordInfo.class,orderArr[0]);
        OrderInoutRecord orderInoutRecord = (OrderInoutRecord) baseDao.getById(OrderInoutRecord.class,orderArr[1]);
        if (CommonUtils.isEmpty(orderInoutRecord.getOutRecordId())){
            String inTime = inoutRecordInfo.getInoutTime().toString();
            Timestamp inTimeNew = CommonUtils.getTimestamp();
            inoutRecordInfo.setInoutTime(inTimeNew);
            inoutRecordInfo.setRemark("数据异常，原入场时间：" + inTime);
            baseDao.update(inoutRecordInfo);
            orderInoutRecord.setInTime(inTimeNew);
            orderInoutRecord.setRemark("数据异常，原入场时间：" + inTime);
            baseDao.update(orderInoutRecord);
        }
    }

    @Override
    public void handleMqInfo(JSONObject jsonObject) throws BizException {
        String mqType = jsonObject.getString("mqType") + "";
        if ("monthCardRenewal".equals(mqType)){
            monthCardRenewal(jsonObject);
        }else if ("parkingCharge".equals(mqType)){
            parkingChargeRenewal(jsonObject);
        }else if ("markPayCar".equals(mqType)){
            //标记无感支付订单
            uparkChargeMarkRenewal(jsonObject);
        }else if ("uparkPaySuccess".equals(mqType)){
            uparkPaySuccessRenewal(jsonObject);
        }else if ("orderRePush".equals(mqType)){
            inParkInfoRePush(jsonObject);
        } else {
            throw new BizException("未知的mq类型");
        }
    }

    @Override
    public void handleInParkSubmitFail(String inoutId, String orderId, String orderOldId) {
        WechatPayFailUrl wechatPayFailUrl = new WechatPayFailUrl();
        wechatPayFailUrl.setUrl(inoutId+"," + orderId + "," + orderOldId);
        wechatPayFailUrl.setIsUsed(0);  //上传信息标识0-入场；1-出场
        wechatPayFailUrl.setAddTime(CommonUtils.getTimestamp());
        wechatPayFailUrl.setResult(0);  //0-待上传
        baseDao.save(wechatPayFailUrl);
    }

    @Override
    public void handleOutParkSubmitFail(String inoutId, String orderId, String transId) {
        WechatPayFailUrl wechatPayFailUrl = new WechatPayFailUrl();
        wechatPayFailUrl.setUrl(inoutId+"," + orderId + "," +transId);
        wechatPayFailUrl.setIsUsed(1);  //上传信息标识0-入场；1-出场
        wechatPayFailUrl.setAddTime(CommonUtils.getTimestamp());
        wechatPayFailUrl.setResult(0);  //0-待上传
        baseDao.save(wechatPayFailUrl);
    }

    @Override
    public void handleChargeInTimeSubmitFail(String orderId, String inChargeTime) {
        WechatPayFailUrl wechatPayFailUrl = new WechatPayFailUrl();
        wechatPayFailUrl.setUrl(orderId + "," +inChargeTime);
        wechatPayFailUrl.setIsUsed(2);  //上传信息标识0-入场；1-出场
        wechatPayFailUrl.setAddTime(CommonUtils.getTimestamp());
        wechatPayFailUrl.setResult(0);  //0-待上传
        baseDao.save(wechatPayFailUrl);
    }

    @Override
    public void monthCardRenewal(JSONObject jsonObject) {
        try {
            JSONObject orderTransaction = jsonObject.getJSONObject("orderTransaction");
            JSONObject orderPrechargeRecord = jsonObject.getJSONObject("orderPrechargeRecord");
            OrderPrechargeRecord orderPrechargeRecordRes = new OrderPrechargeRecord();
            if (!CommonUtils.isEmpty(orderPrechargeRecord)){
                String hql = "from OrderPrechargeRecord where memo = ?";
                orderPrechargeRecordRes = (OrderPrechargeRecord)baseDao.getUnique(hql,orderPrechargeRecord.get("orderPrechargeId") + "");
                if (!CommonUtils.isEmpty(orderPrechargeRecordRes)){
                    throw new BizException("消息重复");
                }else {
                    orderPrechargeRecordRes = new OrderPrechargeRecord();
                    //设置参数
                    orderPrechargeRecordRes.setOrderPrechargeCarno(orderPrechargeRecord.get("orderPrechargeCarno") + "");
                    orderPrechargeRecordRes.setOrderPrechargeCarpark(orderPrechargeRecord.get("orderPrechargeCarpark") + "");
                    orderPrechargeRecordRes.setOrderPrechargeCarparkName(orderPrechargeRecord.get("orderPrechargeCarparkName") + "");
                    orderPrechargeRecordRes.setOrderPrechargeTime(new Timestamp(Long.valueOf(orderPrechargeRecord.get("orderPrechargeTime") + "")));
                    orderPrechargeRecordRes.setChargeMemberKindId(orderPrechargeRecord.get("chargeMemberKindId") + "");
                    orderPrechargeRecordRes.setChargeMemberKindName(orderPrechargeRecord.get("chargeMemberKindName") + "");
                    orderPrechargeRecordRes.setOrderPrechargeOperatorId(orderPrechargeRecord.get("orderPrechargeOperatorId") + "");
                    orderPrechargeRecordRes.setOrderPrechargeOperatorName(orderPrechargeRecord.get("orderPrechargeOperatorName") + "");
                    orderPrechargeRecordRes.setOrderPrechargeReceivableAmount(Double.valueOf(orderPrechargeRecord.get("orderPrechargeReceivableAmount") + ""));
                    orderPrechargeRecordRes.setOrderPrechargeActualAmount(Double.valueOf(orderPrechargeRecord.get("orderPrechargeActualAmount") + ""));
                    orderPrechargeRecordRes.setOrderPrechargeFreeAmount(Double.valueOf(orderPrechargeRecord.get("orderPrechargeFreeAmount") + ""));
                    orderPrechargeRecordRes.setFreeReason(orderPrechargeRecord.get("freeReason") + "");
                    orderPrechargeRecordRes.setMemo(orderPrechargeRecord.get("orderPrechargeId") + "");
                    orderPrechargeRecordRes.setAddTime(CommonUtils.getTimestamp());
                    baseDao.save(orderPrechargeRecordRes);
                }
            }else {
                throw new BizException("同步月卡续费信息错误");
            }

            if (!CommonUtils.isEmpty(orderTransaction)){
                //把传入的ID放入transactionMark
                String hql = "from OrderTransaction where transactionMark = ?";
                OrderTransaction orderTransactionRes = (OrderTransaction)baseDao.getUnique(hql,orderTransaction.get("transactionId") + "");
                if (!CommonUtils.isEmpty(orderTransactionRes)){
                    throw new BizException("消息重复");
                }else {
                    orderTransactionRes = new OrderTransaction();
                    orderTransactionRes.setOrderId(orderPrechargeRecordRes.getOrderPrechargeId());
                    orderTransactionRes.setPayType(payTypeEnum.D);
                    orderTransactionRes.setPayTime(new Timestamp(Long.valueOf(orderTransaction.get("payTime") + "")));
                    orderTransactionRes.setPayStatus(payStatusEnum.HAS_PAID);
                    //orderTransactionRes.setPayTypeName(payTypeEnum.WX_PAY.getDesc());
                    orderTransactionRes.setTotalFee(BigDecimal.valueOf(Double.valueOf(orderTransaction.get("totalFee") + "")));
                    orderTransactionRes.setRealFee(BigDecimal.valueOf(Double.valueOf(orderTransaction.get("totalFee") + "")));
                    orderTransactionRes.setDiscountType(orderTransaction.get("discountType") + "");
                    orderTransactionRes.setDiscountFee(BigDecimal.valueOf(Double.valueOf(orderTransaction.get("totalFee") + "")));
                    orderTransactionRes.setAddTime(CommonUtils.getTimestamp());
                    orderTransactionRes.setTransactionMark(orderTransaction.get("transactionId") + "");
                    baseDao.save(orderTransactionRes);
                }
            }else {
                throw new BizException("同步月卡续费信息错误");
            }
            //更新白名单信息,有效结束时间放在memo里面
            Timestamp effectiveEndTime = Timestamp.valueOf(orderPrechargeRecord.get("memo") + "");
            String hql = "from MemberWallet where menberNo = ? and useMark >= 0";
            List<MemberWallet> memberWalletList = (List<MemberWallet>)baseDao.queryForList(hql,orderPrechargeRecordRes.getOrderPrechargeCarno());
            for (MemberWallet memberWallet : memberWalletList){
                if (memberWallet.getCarPark().equals(orderPrechargeRecordRes.getOrderPrechargeCarpark())){
                    memberWallet.setEffectiveEndTime(effectiveEndTime);
                    String[] carNos = (memberWallet.getMenberNo()).split(",");
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

                        hql = "from InOutCarRoadInfo where ownCarparkNo = ?";
                        String ipcListInfo = "";
                        List<InOutCarRoadInfo> carRoadInfos = (List<InOutCarRoadInfo>)baseDao.queryForList(hql,memberWallet.getCarPark());
                        for(InOutCarRoadInfo carRoadInfo : carRoadInfos){
                            hql = "from DeviceInfo where ownCarRoad = ? and deviceType = '0' and useMark >= 0";
                            List<DeviceInfo> deviceInfos = (List<DeviceInfo>)baseDao.queryForList(hql,carRoadInfo.getCarRoadId());
                            for(DeviceInfo deviceInfo : deviceInfos){
                                deviceManageUtils.updateWhiteMember(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd(),whiteParams);
                                ipcListInfo = ipcListInfo + deviceInfo.getDeviceId() + ",";
                            }
                        }
                        if (!StringUtils.isBlank(ipcListInfo)){
                            ipcListInfo = ipcListInfo.substring(0,ipcListInfo.length()-1);
                            memberWallet.setSynchronizeIpcList(ipcListInfo);
                        }
                    }else {
                        memberWallet.setSynchronizeIpcList("");
                    }
                    baseDao.update(memberWallet);
                }else {
                    if (memberWallet.getPackKindId().equals(-3)){
                        //免费的也更新
                        memberWallet.setEffectiveEndTime(effectiveEndTime);
                        String[] carNos = (memberWallet.getMenberNo()).split(",");
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

                            hql = "from InOutCarRoadInfo where ownCarparkNo = ?";
                            String ipcListInfo = "";
                            List<InOutCarRoadInfo> carRoadInfos = (List<InOutCarRoadInfo>)baseDao.queryForList(hql,memberWallet.getCarPark());
                            for(InOutCarRoadInfo carRoadInfo : carRoadInfos){
                                hql = "from DeviceInfo where ownCarRoad = ? and deviceType = '0' and useMark >= 0";
                                List<DeviceInfo> deviceInfos = (List<DeviceInfo>)baseDao.queryForList(hql,carRoadInfo.getCarRoadId());
                                for(DeviceInfo deviceInfo : deviceInfos){
                                    deviceManageUtils.updateWhiteMember(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd(),whiteParams);
                                    ipcListInfo = ipcListInfo + deviceInfo.getDeviceId() + ",";
                                }
                            }
                            if (!StringUtils.isBlank(ipcListInfo)){
                                ipcListInfo = ipcListInfo.substring(0,ipcListInfo.length()-1);
                                memberWallet.setSynchronizeIpcList(ipcListInfo);
                            }
                        }else {
                            memberWallet.setSynchronizeIpcList("");
                        }
                        baseDao.update(memberWallet);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("处理mqtt返回的月卡缴费数据失败");
        }
    }

    @Override
    public void parkingChargeRenewal(JSONObject jsonObject) {
        try {
            JSONObject orderTransaction = jsonObject.getJSONObject("orderTransaction");
            JSONObject orderInoutRecord = jsonObject.getJSONObject("orderInoutRecord");
            OrderInoutRecord orderInoutRecordRes = new OrderInoutRecord();

            if (!CommonUtils.isEmpty(orderInoutRecord)){
                orderInoutRecordRes = (OrderInoutRecord) baseDao.getById(OrderInoutRecord.class,orderInoutRecord.get("chargeInfoId") + "");
                if (!CommonUtils.isEmpty(orderInoutRecordRes)){
                    orderInoutRecordRes.setChargePreAmount(Double.valueOf(orderInoutRecord.get("chargePreAmount") + ""));
                    baseDao.update(orderInoutRecordRes);
                }else {
                    throw new BizException("进出订单无法查询");
                }
            }

            if (!CommonUtils.isEmpty(orderTransaction)){
                //把传入的ID放入transactionMark
                String hql = "from OrderTransaction where transactionMark = ?";
                OrderTransaction orderTransactionRes = (OrderTransaction)baseDao.getUnique(hql,orderTransaction.get("transactionId") + "");
                if (!CommonUtils.isEmpty(orderTransactionRes)){
                    throw new BizException("消息重复");
                }else {
                    orderTransactionRes = new OrderTransaction();
                    orderTransactionRes.setOrderId(orderInoutRecordRes.getChargeInfoId());
                    orderTransactionRes.setPayType(payTypeEnum.C);
                    orderTransactionRes.setPayTime(new Timestamp(Long.valueOf(orderTransaction.get("payTime") + "")));
                    orderTransactionRes.setPayStatus(payStatusEnum.HAS_PAID);
                    //orderTransactionRes.setPayTypeName(payTypeEnum.WX_PAY.getDesc());
                    orderTransactionRes.setTotalFee(BigDecimal.valueOf(Double.valueOf(orderTransaction.get("totalFee") + "")));
                    orderTransactionRes.setRealFee(BigDecimal.valueOf(Double.valueOf(orderTransaction.get("totalFee") + "")));
                    orderTransactionRes.setDiscountType(orderTransaction.get("discountType") + "");
                    orderTransactionRes.setDiscountFee(BigDecimal.valueOf(Double.valueOf(orderTransaction.get("totalFee") + "")));
                    orderTransactionRes.setAddTime(CommonUtils.getTimestamp());
                    orderTransactionRes.setTransactionMark(orderTransaction.get("transactionId") + "");
                    baseDao.save(orderTransactionRes);
                }
            }
            try{
                JSONObject json = new JSONObject();
                json.put("carno", Base64.encodeToString(orderInoutRecordRes.getCarNo().getBytes()));
                json.put("intime",orderInoutRecordRes.getInTime().toString());
                json.put("chargePreAmount",orderInoutRecordRes.getChargePreAmount().toString());
                for (int i = 0; i < ParkSocketServer.getClientsCount(); i++) {
                    SocketClient client = ParkSocketServer.getClient(i);
                    json.put("result","0");
                    SocketUtils.sendBizPackage(client, json, "WxChatPay");
                }
            }catch (BizException ex) {
                LOGGER.error("微信缴费信息推送失败:{},车牌:{},车道:{}", ex.getMessage(), orderInoutRecordRes.getCarNo(), orderInoutRecordRes.getInCarRoadName());
            }
        } catch (Exception e) {
            LOGGER.error("处理mqtt返回的临停缴费处理数据");
        }

    }

    @Override
    public void inParkInfoRePush(JSONObject jsonObject) {
        try {
            /*String carno = jsonObject.getString("carno");
            String hql = "from OrderInoutRecord where carNo = ? and outRecordId is null order by inTime desc";
            List<OrderInoutRecord> list = (List<OrderInoutRecord>)baseDao.queryForList(hql,carno);
            if (list.size() > 0){
                OrderInoutRecord orderInoutRecord = list.get(0);
                if (("金山园区".equals(orderInoutRecord.getCarparkName())) || ("海西园区".equals(orderInoutRecord.getCarparkName()))){
                    return;
                }
                InoutRecordInfo inoutRecordInfo = (InoutRecordInfo)baseDao.getById(InoutRecordInfo.class,orderInoutRecord.getInRecordId());
                addInParkOrderToCloud(inoutRecordInfo,"",orderInoutRecord);
            }*/
        } catch (Exception e) {
            LOGGER.error("mq传回的数据处理失败：" + jsonObject.toJSONString());
        }

    }

    @Override
    public void uparkChargeMarkRenewal(JSONObject jsonObject) {
        try {
            String chargeInfoId = jsonObject.getString("chargeInfoId");
            OrderInoutRecord orderInoutRecord = (OrderInoutRecord)baseDao.getById(OrderInoutRecord.class,chargeInfoId);
            if (!CommonUtils.isEmpty(orderInoutRecord)){
                //放行方式为4的时候是无感支付
                orderInoutRecord.setReleaseType(Byte.valueOf("4"));
                baseDao.update(orderInoutRecord);
            }
        }catch (Exception e){
            LOGGER.error("处理mqtt返回的无感支付标识数据失败");
        }
    }

    @Override
    public void uparkPaySuccessRenewal(JSONObject jsonObject) {
        try {
            OrderInoutRecord orderInoutRecord = (OrderInoutRecord) baseDao.getById(OrderInoutRecord.class,jsonObject.getString("chargeInfoId"));
            if (!CommonUtils.isEmpty(orderInoutRecord) && CommonUtils.isEmpty(orderInoutRecord.getOutRecordId())){
                BigDecimal chargeAmount = (new BigDecimal(jsonObject.getString("chargeAmount"))).divide(new BigDecimal(100),0,BigDecimal.ROUND_UP);
                OrderTransaction orderTransaction = new OrderTransaction();
                orderTransaction.setOrderId(orderInoutRecord.getChargeInfoId());
                orderTransaction.setPayType(payTypeEnum.A);
                orderTransaction.setPayTime(CommonUtils.getTimestamp());
                orderTransaction.setPayStatus(payStatusEnum.HAS_PAID);
                //orderTransaction.setPayTypeName(payTypeEnum.CASH_PAY.getDesc());
                orderTransaction.setTotalFee(chargeAmount);
                orderTransaction.setRealFee(chargeAmount);
                orderTransaction.setDiscountFee(BigDecimal.valueOf(Double.valueOf("0")));
                orderTransaction.setDiscountType("");
                orderTransaction.setAddTime(CommonUtils.getTimestamp());
                baseDao.save(orderTransaction);

                try {
                    JSONObject json = new JSONObject();
                    json.put("carno", Base64.encodeToString(orderInoutRecord.getCarNo().getBytes()));
                    json.put("intime", orderInoutRecord.getInTime().toString());
                    json.put("chargePreAmount", chargeAmount.toString());
                    for (int i = 0; i < ParkSocketServer.getClientsCount(); i++) {
                        SocketClient client = ParkSocketServer.getClient(i);
                        json.put("result", "0");
                        SocketUtils.sendBizPackage(client, json, "WxChatPay");
                    }
                } catch (BizException ex) {
                    LOGGER.error("银联支付信息推送失败:{},车牌:{},车道:{}", ex.getMessage(), orderInoutRecord.getCarNo(), orderInoutRecord.getInCarRoadName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pushBillToUparkCloud(String billNo,String usrNum,String parkNo,String parkName,String billAt,String payAt,
                                     String startTime,String payTime,String reserve) {
        if(!AppInfo.isConnectCloud.equals("on")){
            return;
        }

        if (!AppInfo.isUseUparkPay.equals("1")){
            return;
        }

        try {
            JSONObject billElement = new JSONObject();
            billElement.put("buss_cd",AppInfo.bussCode);
            billElement.put("bill_no",billNo);
            billElement.put("usr_num",usrNum);
            billElement.put("park_no",parkNo);
            billElement.put("park_name",parkName);
            billElement.put("bill_at",billAt);
            billElement.put("pay_at",payAt);
            billElement.put("start_time",startTime);
            billElement.put("pay_time",payTime);
            billElement.put("reserve",reserve);
            billElement.put("isUseUparkPay",AppInfo.isUseUparkPay);
            billElement.put("bussCode",AppInfo.bussCode);
            billElement.put("bussKey",AppInfo.bussKey);
            billElement.put("serviceId",AppInfo.cloudServerId);
            CloudTransPackage cloudTransPackage = new CloudTransPackage();
            cloudTransPackage.setUploadData(billElement);
            cloudTransPackage.setCloudTransEnum(CloudTransEnum.uparkBillPush);
            AppInfo.cloudTransQueue.offer(cloudTransPackage);
        }catch (Exception e){
            LOGGER.error("银联支付账单推送执行失败！");
        }

    }
}