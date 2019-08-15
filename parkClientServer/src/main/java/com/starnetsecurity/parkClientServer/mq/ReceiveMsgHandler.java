package com.starnetsecurity.parkClientServer.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.parkClientServer.clientEnum.ServiceEnum;
import com.starnetsecurity.parkClientServer.service.ParkService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by 宏炜 on 2017-10-13.
 */
public class ReceiveMsgHandler {

    ReceiveMsgHandler(ParkService parkService ){
        this.parkService = parkService;
    }

    private ParkService parkService;

    public ParkService getParkService() {
        return parkService;
    }

    public void setParkService(ParkService parkService) {
        this.parkService = parkService;
    }

    private static Logger log = LoggerFactory.getLogger(ReceiveMsgHandler.class);

    public void handleMessage(byte[] data) throws UnsupportedEncodingException {
        String msg = new String(data,"gbk");

        if(!StringUtils.isBlank(msg)){
            JSONObject params = JSON.parseObject(msg);
            ServiceEnum serviceEnum = ServiceEnum.valueOf(params.get("serviceType").toString());
            switch (serviceEnum){
                case payNotify:
                    parkService.addChargeReceiveRecord(params);
                    break;
                case whitePayNotify:
                    Map whiteObject = (Map)params.get("whiteObject");
                    String memberId = whiteObject.get("id").toString();
                    String parkId = whiteObject.get("carPark").toString();
                    String carno = whiteObject.get("memberNo").toString();
                    String fee = whiteObject.get("fee").toString();
                    String payTime = whiteObject.get("payTime").toString();
                    String kindId = whiteObject.get("packKindId").toString();
                    String effectiveEndTime = String.valueOf(whiteObject.get("effectiveEndTime"));
                    String surplusNum = String.valueOf(whiteObject.get("surplusNumber"));
                    String surplusAmount = String.valueOf(whiteObject.get("surplusAmount"));
                    String kindName = String.valueOf(whiteObject.get("kindName"));

                    parkService.addRechargeRecord(memberId,carno,parkId,fee,payTime,kindId,effectiveEndTime,surplusNum,surplusAmount,kindName);
                    break;
            }
            log.info("收到队列：{} 的消息：{}",msg);
        }
    }
}
