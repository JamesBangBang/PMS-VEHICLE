package com.starnetsecurity.parkClientServer.cloudTransport;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.util.HttpRequestUtils;
import com.starnetsecurity.parkClientServer.entity.OrderTransaction;
import com.starnetsecurity.parkClientServer.entity.WechatPayFailUrl;
import com.starnetsecurity.parkClientServer.init.AppInfo;
import com.starnetsecurity.parkClientServer.service.NewOrderParkService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by JAMESBANG on 2018/11/21.
 */
//后期增加暂停代码，在进行定时检索时，停止线程运转
public class CloudTransThread extends Thread{
    private static final Logger LOGGER = LoggerFactory.getLogger(CloudTransThread.class);

    private NewOrderParkService newOrderParkService;

    public CloudTransThread(NewOrderParkService newOrderParkService){
        this.newOrderParkService = newOrderParkService;
    }

    @Override
    public void run(){
        while (true){
            if (!CollectionUtils.isEmpty(AppInfo.cloudTransQueue)){
                CloudTransPackage cloudTransPackage = AppInfo.cloudTransQueue.poll();
                switch (cloudTransPackage.getCloudTransEnum()){
                    case inPark: {
                        JSONObject inParkElement = cloudTransPackage.getUploadData();
                        try {
                            String responseStr = HttpRequestUtils.postJson("http://" + AppInfo.cloudIp + ":" + AppInfo.cloudPort + "/payment/orderInfo/uploadInParkOrder",inParkElement);
                            if (CommonUtils.isEmpty(responseStr)){
                                newOrderParkService.handleInParkSubmitFail(inParkElement.get("inoutRecordId") + "",inParkElement.get("chargeInfoId") + "",inParkElement.get("orderOldId") + "");
                            }else {
                                JSONObject res = JSON.parseObject(responseStr);
                                if (!"200".equals(res.getString("code"))) {
                                    newOrderParkService.handleInParkSubmitFail(inParkElement.get("inoutRecordId") + "", inParkElement.get("chargeInfoId") + "", inParkElement.get("orderOldId") + "");
                                }
                            }
                        }catch (IOException e){
                            newOrderParkService.handleInParkSubmitFail(inParkElement.get("inoutRecordId") + "",inParkElement.get("chargeInfoId") + "",inParkElement.get("orderOldId") + "");
                        }
                        break;
                    }
                    case outPark: {
                        JSONObject outParkElement = cloudTransPackage.getUploadData();
                        try {
                            String responseStr = HttpRequestUtils.postJson("http://" + AppInfo.cloudIp + ":" + AppInfo.cloudPort + "/payment/orderInfo/uploadOutParkOrder",outParkElement);
                            if (CommonUtils.isEmpty(responseStr)){
                                newOrderParkService.handleOutParkSubmitFail(outParkElement.get("inoutRecordId") + "",outParkElement.get("chargeInfoId") + "",outParkElement.get("transId") + "");
                            }else {
                                JSONObject res = JSON.parseObject(responseStr);
                                if (!"200".equals(res.getString("code"))) {
                                    newOrderParkService.handleOutParkSubmitFail(outParkElement.get("inoutRecordId") + "", outParkElement.get("chargeInfoId") + "", outParkElement.get("transId") + "");
                                }
                            }
                        }catch (IOException e){
                            newOrderParkService.handleOutParkSubmitFail(outParkElement.get("inoutRecordId") + "",outParkElement.get("chargeInfoId") + "",outParkElement.get("transId") + "");
                        }
                        break;
                    }
                    case chargeInTime:{
                        JSONObject chargeInTimeElement = cloudTransPackage.getUploadData();
                        try {
                            String responseStr = HttpRequestUtils.postJson("http://" + AppInfo.cloudIp + ":" + AppInfo.cloudPort + "/payment/orderInfo/uploadInChargeTimeOrder",chargeInTimeElement);
                            if (CommonUtils.isEmpty(responseStr)){
                                newOrderParkService.handleChargeInTimeSubmitFail(chargeInTimeElement.get("orderId") + "",chargeInTimeElement.get("inChargeTime") + "");
                            }else {
                                JSONObject res = JSON.parseObject(responseStr);
                                if (!"200".equals(res.getString("code"))) {
                                    newOrderParkService.handleChargeInTimeSubmitFail(chargeInTimeElement.get("orderId") + "", chargeInTimeElement.get("inChargeTime") + "");
                                }
                            }
                        }catch (IOException e){
                            newOrderParkService.handleChargeInTimeSubmitFail(chargeInTimeElement.get("orderId") + "",chargeInTimeElement.get("inChargeTime") + "");
                        }
                        break;
                    }
                    case liftRod: {
                        break;
                    }
                    case uparkBillPush:{
                        JSONObject billElement = cloudTransPackage.getUploadData();
                        try {
                            String responseStr = HttpRequestUtils.postJson("http://" + AppInfo.cloudIp + ":" + AppInfo.cloudPort + "/payment/orderInfo/uploadUparkBillInfo",billElement);
                            if (CommonUtils.isEmpty(responseStr)){
                                //newOrderParkService.handleChargeInTimeSubmitFail(chargeInTimeElement.get("orderId") + "",chargeInTimeElement.get("inChargeTime") + "");
                            }else {
                                JSONObject res = JSON.parseObject(responseStr);
                                if (!"200".equals(res.getString("code"))) {
                                    //newOrderParkService.handleChargeInTimeSubmitFail(chargeInTimeElement.get("orderId") + "", chargeInTimeElement.get("inChargeTime") + "");
                                }
                            }
                        }catch (IOException e){
                            //newOrderParkService.handleChargeInTimeSubmitFail(chargeInTimeElement.get("orderId") + "",chargeInTimeElement.get("inChargeTime") + "");
                        }
                        break;
                    }
                }
            }
        }
    }

}
