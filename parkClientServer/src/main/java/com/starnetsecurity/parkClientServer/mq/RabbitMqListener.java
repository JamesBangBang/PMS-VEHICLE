package com.starnetsecurity.parkClientServer.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.util.Constant;
import com.starnetsecurity.commonService.util.DeviceManageUtils;
import com.starnetsecurity.commonService.util.StarnetDeviceUtils;
import com.starnetsecurity.parkClientServer.entity.*;
import com.starnetsecurity.parkClientServer.entityEnum.payStatusEnum;
import com.starnetsecurity.parkClientServer.entityEnum.payTypeEnum;
import com.starnetsecurity.parkClientServer.init.AppInfo;
import com.starnetsecurity.parkClientServer.service.BasicDataSyncService;
import com.starnetsecurity.parkClientServer.service.NewOrderParkService;
import com.starnetsecurity.parkClientServer.sockServer.DeviceSIPEnum;
import com.starnetsecurity.parkClientServer.sockServer.ParkSocketServer;
import com.starnetsecurity.parkClientServer.sockServer.SocketClient;
import com.starnetsecurity.parkClientServer.sockServer.SocketUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.codec.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.LongAccumulator;

@Component
@Transactional
public class RabbitMqListener implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqListener.class);

    @Autowired
    NewOrderParkService newOrderParkService;

    @Autowired
    HibernateBaseDao baseDao;

    public void onMessage(Message message) {
        if (!("on".equals(AppInfo.isConnectCloud))) return;
        String data = new String(message.getBody());
        JSONObject jsonObject = JSON.parseObject(data);
        try {
            synChargeInfo(jsonObject);
        } catch (BizException e) {
            LOGGER.error("同步缴费信息失败：" + e.getMessage());
        }

    }

    public void synChargeInfo(JSONObject jsonObject) throws BizException{
        try {
            newOrderParkService.handleMqInfo(jsonObject);
        }catch (BizException e){
            throw new BizException(e.getMessage());
        }
    }

}





