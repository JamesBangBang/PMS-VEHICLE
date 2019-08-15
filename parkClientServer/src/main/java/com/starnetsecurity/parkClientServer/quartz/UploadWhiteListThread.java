package com.starnetsecurity.parkClientServer.quartz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.util.HttpRequestUtils;
import com.starnetsecurity.commonService.util.DeviceLedCgiThread;
import com.starnetsecurity.commonService.util.DeviceLedFourThread;
import com.starnetsecurity.parkClientServer.entity.DeviceInfo;
import com.starnetsecurity.parkClientServer.entity.MemberWallet;
import com.starnetsecurity.parkClientServer.init.AppInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

/**
 * Created by JAMESBANG on 2018/11/20.
 */
public class UploadWhiteListThread extends Thread{
    @Autowired
    HibernateBaseDao baseDao;

    private static String control = "";

    private static boolean suspend = false;

    public boolean isSuspend() {
        return suspend;
    }

    public static void setSuspend(boolean suspendTemp) {
        if (!suspendTemp){
            synchronized (control){
                control.notifyAll();
            }
        }
        suspend = suspendTemp;
    }

    private static List<MemberWallet> uploadWhiteList;

    public static void addWhiteListInfo(MemberWallet memberWallet){
        uploadWhiteList.add(memberWallet);
    }

    private static void initServer(){
        uploadWhiteList = new ArrayList<>();
    }


    @Override
    public void run(){
        initServer();
        while (true){
            synchronized (control){
                if (!suspend){
                    if (uploadWhiteList.size() > 0){
                        for (int i = 0;i<uploadWhiteList.size();i++){
                            if (!("on".equals(AppInfo.isConnectCloud))){
                                return;
                            }
                            MemberWallet memberWallet = uploadWhiteList.get(i);
                            if (!memberWallet.getPackKindId().equals(0)){
                                continue;
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
                            memberWalletElement.put("orderPrechargeRecord",null);
                            memberWalletElement.put("orderTransactionElement",null);
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
                                    uploadWhiteList.remove(i);
                                }else {
                                    throw new BizException("推送数据至云服务器失败，车牌号：" + memberWallet.getMenberNo());
                                }
                            }catch (IOException e){
                                throw new BizException("推送数据至云服务器失败，车牌号：" + memberWallet.getMenberNo());
                            }
                        }
                    }
                }
            }
        }
    }



}
