package com.starnetsecurity.parkClientServer.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.util.HttpRequestUtils;
import com.starnetsecurity.parkClientServer.chargeDetail.ChargeTypeDetail;
import com.starnetsecurity.parkClientServer.chargeDetail.FeeSetDetail;
import com.starnetsecurity.parkClientServer.entity.*;
import com.starnetsecurity.parkClientServer.init.AppInfo;
import com.starnetsecurity.parkClientServer.service.UploadDataToCloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by JAMESBANG on 2018/11/18.
 */
@Service
public class UploadDataToCloudServiceImpl implements UploadDataToCloudService {
    @Autowired
    HibernateBaseDao baseDao;

    @Override
    public void uploadParkInfoToServer(CarparkInfo carparkInfo) {
        if (!("on".equals(AppInfo.isConnectCloud))){
            return;
        }
        JSONObject carparkInfoElement = new JSONObject();
        carparkInfoElement.put("carparkId",carparkInfo.getCarparkId());
        carparkInfoElement.put("carparkName",carparkInfo.getCarparkName());
        carparkInfoElement.put("totalCarSpace",carparkInfo.getTotalCarSpace());
        carparkInfoElement.put("availableCarSpace",carparkInfo.getAvailableCarSpace());
        carparkInfoElement.put("ownCarparkNo",carparkInfo.getOwnCarparkNo());
        carparkInfoElement.put("operationSource",carparkInfo.getOperationSource());
        carparkInfoElement.put("passTimeWhenBig",carparkInfo.getPassTimeWhenBig());
        carparkInfoElement.put("ifIncludeCaculate",carparkInfo.getIfIncludeCaculate());
        carparkInfoElement.put("isTestRunning",carparkInfo.getIsTestRunning());
        carparkInfoElement.put("isClose",carparkInfo.getIsClose());
        carparkInfoElement.put("criticalValue",carparkInfo.getCriticalValue());
        carparkInfoElement.put("isAutoOpen",carparkInfo.getIsAutoOpen());
        carparkInfoElement.put("isOverdueAutoOpen",carparkInfo.getIsOverdueAutoOpen());
        carparkInfoElement.put("closeType",carparkInfo.getCloseType());
        carparkInfoElement.put("ledMemberCriticalValue",carparkInfo.getLedMemberCriticalValue());
        carparkInfoElement.put("serviceId",AppInfo.cloudServerId);
        carparkInfoElement.put("carparkKey",AppInfo.cloudCarparkCode);
        carparkInfoElement.put("useMark",carparkInfo.getUseMark());
        try {
            String responseStr = HttpRequestUtils.postJson("http://" + AppInfo.cloudIp + ":" + AppInfo.cloudPort + "/payment/parkInfo/uploadParkInfo",carparkInfoElement);
            if (CommonUtils.isEmpty(responseStr)){
                throw new BizException("云服务器无信息返回");
            }
            JSONObject res = JSON.parseObject(responseStr);
            if ("200".equals(res.getString("code"))){
                if (carparkInfo.getUseMark().equals(-1)){
                    carparkInfo.setUseMark(-2);
                }else {
                    carparkInfo.setUseMark(2);
                }
                baseDao.update(carparkInfo);
            }else {
                throw new BizException("推送数据至云服务器失败，车场名称：" + carparkInfo.getCarparkName());
            }
        }catch (IOException e){
            throw new BizException("推送数据至云服务器失败，车场名称：" + carparkInfo.getCarparkName());
        }

    }

    @Override
    public void uploadRoadInfoToServer(InOutCarRoadInfo inOutCarRoadInfo) {
        if (!("on".equals(AppInfo.isConnectCloud))){
            return;
        }
        JSONObject roadElement = new JSONObject();
        roadElement.put("carRoadId",inOutCarRoadInfo.getCarRoadId());
        roadElement.put("carRoadName",inOutCarRoadInfo.getCarRoadName());
        roadElement.put("carRoadType",inOutCarRoadInfo.getCarRoadType());
        roadElement.put("ownCarparkNo",inOutCarRoadInfo.getOwnCarparkNo());
        roadElement.put("operationSource",inOutCarRoadInfo.getOperationSource());
        roadElement.put("manageComputerId",inOutCarRoadInfo.getManageComputerId());
        roadElement.put("autoPassType",inOutCarRoadInfo.getAutoPassType());
        roadElement.put("serviceId",AppInfo.cloudServerId);
        roadElement.put("carparkKey",AppInfo.cloudCarparkCode);
        roadElement.put("useMark",inOutCarRoadInfo.getUseMark());
        try {
            String responseStr = HttpRequestUtils.postJson("http://" + AppInfo.cloudIp + ":" + AppInfo.cloudPort + "/payment/parkInfo/uploadRoadInfo",roadElement);
            if (CommonUtils.isEmpty(responseStr)){
                throw new BizException("云服务器无信息返回");
            }
            JSONObject res = JSON.parseObject(responseStr);
            if ("200".equals(res.getString("code"))){
                if (inOutCarRoadInfo.getUseMark().equals(-1)){
                    inOutCarRoadInfo.setUseMark(-2);
                }else {
                    inOutCarRoadInfo.setUseMark(2);
                }
                baseDao.update(inOutCarRoadInfo);
            }else {
                throw new BizException("推送数据至云服务器失败，车道名称：" + inOutCarRoadInfo.getCarRoadName());
            }
        }catch (IOException e){
            throw new BizException("推送数据至云服务器失败，车道名称：" + inOutCarRoadInfo.getCarRoadName());
        }
    }

    @Override
    public void uploadOperatorInfoToServer(Operator operator) {
        if (!("on".equals(AppInfo.isConnectCloud))){
            return;
        }
        JSONObject operatorElement = new JSONObject();
        operatorElement.put("operatorId",operator.getOperatorId());
        operatorElement.put("operatorUserName",operator.getOperatorUserName());
        operatorElement.put("operatorUserPwd",operator.getOperatorUserPwd());
        operatorElement.put("operatorName",operator.getOperatorName());
        operatorElement.put("operationSource",operator.getOperationSource());
        operatorElement.put("serviceId",AppInfo.cloudServerId);
        operatorElement.put("carparkKey",AppInfo.cloudCarparkCode);
        operatorElement.put("useMark",operator.getUseMark());
        try {
            String responseStr = HttpRequestUtils.postJson("http://" + AppInfo.cloudIp + ":" + AppInfo.cloudPort + "/payment/parkInfo/uploadOperatorInfo",operatorElement);
            //String responseStr = HttpRequestUtils.postJson("http://" + AppInfo.cloudIp + ":" + AppInfo.cloudPort + "/parkInfo/uploadOperatorInfo",operatorElement);
            if (CommonUtils.isEmpty(responseStr)){
                throw new BizException("云服务器无信息返回");
            }
            JSONObject res = JSON.parseObject(responseStr);
            if ("200".equals(res.getString("code"))){
                if (operator.getUseMark().equals(-1)){
                    operator.setUseMark(-2);
                }else {
                    operator.setUseMark(2);
                }
                baseDao.update(operator);
            }else {
                throw new BizException("推送数据至云服务器失败，操作员名称：" + operator.getOperatorUserName());
            }
        }catch (IOException e){
            throw new BizException("推送数据至云服务器失败，操作员名称：" + operator.getOperatorUserName());
        }
    }

    @Override
    public void uploadPostInfoToServer(PostComputerManage postComputerManage) {
        if (!("on".equals(AppInfo.isConnectCloud))){
            return;
        }
        JSONObject postElement = new JSONObject();
        postElement.put("postComputerId",postComputerManage.getPostComputerId());
        postElement.put("postComputerName",postComputerManage.getPostComputerName());
        postElement.put("postComputerIp",postComputerManage.getPostComputerIp());
        postElement.put("operationSource",postComputerManage.getOperationSource());
        postElement.put("status",postComputerManage.getStatus());
        postElement.put("serviceId",AppInfo.cloudServerId);
        postElement.put("carparkKey",AppInfo.cloudCarparkCode);
        postElement.put("useMark",postComputerManage.getUseMark());
        try {
            String responseStr = HttpRequestUtils.postJson("http://" + AppInfo.cloudIp + ":" + AppInfo.cloudPort + "/payment/parkInfo/uploadPostInfo",postElement);
            if (CommonUtils.isEmpty(responseStr)){
                throw new BizException("云服务器无信息返回");
            }
            JSONObject res = JSON.parseObject(responseStr);
            if ("200".equals(res.getString("code"))){
                if (postComputerManage.getUseMark().equals(-1)){
                    postComputerManage.setUseMark(-2);
                }else {
                    postComputerManage.setUseMark(2);
                }
                baseDao.update(postComputerManage);
            }else {
                throw new BizException("推送数据至云服务器失败，岗亭名称：" + postComputerManage.getPostComputerName());
            }
        }catch (IOException e){
            throw new BizException("推送数据至云服务器失败，岗亭名称：" + postComputerManage.getPostComputerName());
        }
    }

    @Override
    public void uploadMemberKindInfoToServer(MemberKind memberKind, ChargeTypeDetail chargeTypeDetail, FeeSetDetail feeSetDetail) {
        if (!("on".equals(AppInfo.isConnectCloud))) {
            return;
        }
        JSONObject memberKindElement = new JSONObject();
        memberKindElement.put("id",memberKind.getId());
        memberKindElement.put("kindName",memberKind.getKindName());
        memberKindElement.put("memo",memberKind.getMemo());
        memberKindElement.put("packageKind",memberKind.getPackageKind());
        memberKindElement.put("monthPackage",memberKind.getMonthPackage());
        memberKindElement.put("countPackage",memberKind.getCountPackage());
        memberKindElement.put("packageChildKind",memberKind.getPackageChildKind());
        memberKindElement.put("chargeRuleTemplate",memberKind.getChargeRuleTemplate());
        memberKindElement.put("chargeRuleList",memberKind.getChargeRuleList());
        memberKindElement.put("parkId",CommonUtils.isEmpty(memberKind.getCarparkInfo()) ? "" : memberKind.getCarparkInfo().getCarparkId());
        memberKindElement.put("isDelete",memberKind.getIsDelete());
        memberKindElement.put("isStatistic",memberKind.getIsStatistic());
        memberKindElement.put("useType",memberKind.getUseType());
        memberKindElement.put("serviceId",AppInfo.cloudServerId);
        memberKindElement.put("carparkKey",AppInfo.cloudCarparkCode);
        memberKindElement.put("useMark",memberKind.getUseMark());
        memberKindElement.put("chargeTypeElement",chargeTypeDetail);
        memberKindElement.put("feeSetElement",feeSetDetail);
            try {
                String responseStr = HttpRequestUtils.postJson("http://" + AppInfo.cloudIp + ":" + AppInfo.cloudPort + "/payment/parkInfo/uploadMemberKindInfo",memberKindElement);
                if (CommonUtils.isEmpty(responseStr)){
                    throw new BizException("云服务器无信息返回");
                }
                JSONObject res = JSON.parseObject(responseStr);
                if ("200".equals(res.getString("code"))){
                    if (memberKind.getUseMark().equals(-1)){
                        memberKind.setUseMark(-2);
                    }else {
                        memberKind.setUseMark(2);
                    }
                    baseDao.update(memberKind);
                }else {
                    throw new BizException("推送数据至云服务器失败，收费类型名称为：" + memberKind.getKindName());
                }
            }catch (IOException e){
                throw new BizException("推送数据至云服务器失败，收费类型名称：" + memberKind.getKindName());
            }
        }


    @Override
    public void uploadMemberWalletInfoToServer(MemberWallet memberWallet,OrderPrechargeRecord orderPrechargeRecord,OrderTransaction orderTransaction) {
        if (!("on".equals(AppInfo.isConnectCloud))){
            return;
        }
        if (!memberWallet.getPackKindId().equals(0)){
            return;
        }
        JSONObject memberWalletElement = new JSONObject();
        memberWalletElement.put("id",memberWallet.getId());
        memberWalletElement.put("memberNo",memberWallet.getMenberNo());
        memberWalletElement.put("driverName",memberWallet.getDriverName());
        memberWalletElement.put("driverInfo",memberWallet.getDriverInfo());
        memberWalletElement.put("driverTelephoneNumber",memberWallet.getDriverTelephoneNumber());
        memberWalletElement.put("parkingLotId",memberWallet.getParkingLotId());
        memberWalletElement.put("carPark",memberWallet.getCarPark());
        memberWalletElement.put("memberTypeId",memberWallet.getMenberTypeId());
        memberWalletElement.put("packKindId",memberWallet.getPackKindId());
        memberWalletElement.put("packChlidKind",memberWallet.getPackChlidKind());
        memberWalletElement.put("surplusAmount",memberWallet.getSurplusAmount());
        memberWalletElement.put("surplusNumber",memberWallet.getSurplusNumber());
        memberWalletElement.put("effectiveStartTime",memberWallet.getEffectiveStartTime());
        memberWalletElement.put("effectiveEndTime",memberWallet.getEffectiveEndTime());
        memberWalletElement.put("validMemberCount",memberWallet.getValidMenberCount());
        memberWalletElement.put("serviceId",AppInfo.cloudServerId);
        memberWalletElement.put("carparkKey",AppInfo.cloudCarparkCode);
        memberWalletElement.put("orderPrechargeRecord",orderPrechargeRecord);
        memberWalletElement.put("orderTransactionElement",orderTransaction);
        memberWalletElement.put("useMark",memberWallet.getUseMark());
        try {
            String responseStr = HttpRequestUtils.postJson("http://" + AppInfo.cloudIp + ":" + AppInfo.cloudPort + "/payment/parkInfo/uploadMemberWalletInfo",memberWalletElement);
            if (CommonUtils.isEmpty(responseStr)){
                throw new BizException("云服务器无信息返回");
            }
            JSONObject res = JSON.parseObject(responseStr);
            if ("200".equals(res.getString("code"))){
                if (memberWallet.getUseMark().equals(-1)){
                    memberWallet.setUseMark(-2);
                }else {
                    memberWallet.setUseMark(2);
                }
                baseDao.update(memberWallet);
            }else {
                throw new BizException("推送数据至云服务器失败，车牌号：" + memberWallet.getMenberNo());
            }
        }catch (IOException e){
            throw new BizException("推送数据至云服务器失败，车牌号：" + memberWallet.getMenberNo());
        }
    }



}
