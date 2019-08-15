package com.starnetsecurity.parkClientServer.allinpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.parkClientServer.entity.OrderTransaction;
import com.starnetsecurity.parkClientServer.entityEnum.payStatusEnum;
import com.starnetsecurity.parkClientServer.service.AllinpayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JAMESBANG on 2019/1/24.
 */
public class AllinQueryThread extends Thread{
    private static final Logger LOGGER = LoggerFactory.getLogger(AllinQueryThread.class);

    private AllinpayService allinpayService;

    public AllinQueryThread(AllinpayService allinpayService){
        this.allinpayService = allinpayService;
    }

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

    private static List<JSONObject> transIdList;

    public static void addTransIdToList(JSONObject jsonObject){
        transIdList.add(jsonObject);
    }

    private static void initServer(){
        transIdList = new ArrayList<>();
    }

    @Override
    public void run(){
        initServer();
        while (true){
            synchronized (control){
                if (!suspend){
                    if (transIdList.size() > 0){
                        for (int i = 0;i < transIdList.size();i++){
                            try {
                                JSONObject transInfo = transIdList.get(i);
                                String transactionId = transInfo.get("transactionId") + "";
                                String chargeTime = transInfo.get("chargeTime") + "";
                                Timestamp transTime = Timestamp.valueOf(chargeTime);
                                long timeNow = CommonUtils.getTimestamp().getTime() / 1000;
                                long timeTrans = transTime.getTime() / 1000;
                                int timeDistance = (int) (timeNow - timeTrans);
                                //超过十秒未支付
                                if (timeDistance > 10) {
                                    OrderTransaction orderTransaction = allinpayService.getOrderTransById(transactionId);
                                    if (!CommonUtils.isEmpty(orderTransaction)) {
                                        if (orderTransaction.getPayStatus().equals(payStatusEnum.UN_PAID)) {
                                            //超过5分钟未支付，当做垃圾订单处理，删除掉
                                            if (timeDistance > 600) {
                                                LOGGER.info("垃圾订单删除");
                                                allinpayService.deleteOrderTransById(transactionId);
                                                transIdList.remove(i);
                                            } else {
                                                //开始订单查询
                                                if (allinpayService.queryOrderStatus(transactionId)) {
                                                    transIdList.remove(i);
                                                }
                                            }

                                        } else {
                                            transIdList.remove(i);
                                        }
                                    } else {
                                        transIdList.remove(i);
                                    }
                                }
                            } catch (Exception e) {
                                allinpayService.deleteOrderTransById(transIdList.get(i).get("transactionId") + "");
                                continue;
                            }

                        }
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

}
