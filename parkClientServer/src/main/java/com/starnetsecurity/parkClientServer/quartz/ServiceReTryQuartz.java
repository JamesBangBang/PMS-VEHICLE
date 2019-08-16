package com.starnetsecurity.parkClientServer.quartz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.nio.SemaphoreExecutor;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.util.HttpRequestUtils;
import com.starnetsecurity.parkClientServer.entity.WechatPayFailUrl;
import com.starnetsecurity.parkClientServer.init.AppInfo;
import com.starnetsecurity.parkClientServer.service.NewOrderParkService;
import com.starnetsecurity.parkClientServer.service.OrderParkService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by 宏炜 on 2017-09-07.
 */
@Component
public class ServiceReTryQuartz {

    @Autowired
    NewOrderParkService newOrderParkService;


    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceReTryQuartz.class);

    public void run() {

        try {
            try{
                if ("on".equals(AppInfo.isConnectCloud)) {
                    List<WechatPayFailUrl> wechatPayFailUrlList = newOrderParkService.getWechatPayFailUrlList();
                    if (!CollectionUtils.isEmpty(wechatPayFailUrlList)) {
                        for (WechatPayFailUrl wechatPayFailUrl : wechatPayFailUrlList) {
                            JSONObject orderElement = newOrderParkService.getOrderInfoByUrl(wechatPayFailUrl.getIsUsed(), wechatPayFailUrl.getUrl());
                            if (wechatPayFailUrl.getIsUsed().equals(0)) {
                                try {
                                    String responseStr = HttpRequestUtils.postJson("http://" + AppInfo.cloudIp + ":" + AppInfo.cloudPort + "/payment/parkInfo/uploadInParkOrder", orderElement);
                                    if (CommonUtils.isEmpty(responseStr)) {
                                        LOGGER.info("入场订单数据重传失败" + responseStr);
                                    }
                                    JSONObject res = JSON.parseObject(responseStr);
                                    if ("200".equals(res.getString("code"))) {
                                        newOrderParkService.deleteSuccessInfo(wechatPayFailUrl.getId());
                                    } else {
                                        newOrderParkService.handleFailInfo(wechatPayFailUrl);
                                    }
                                } catch (IOException e) {
                                    newOrderParkService.handleFailInfo(wechatPayFailUrl);
                                    LOGGER.error("入场订单数据重传失败" + e.getMessage());
                                }
                            } else if (wechatPayFailUrl.getIsUsed().equals(1)) {
                                try {
                                    String responseStr = HttpRequestUtils.postJson("http://" + AppInfo.cloudIp + ":" + AppInfo.cloudPort + "/payment/parkInfo/uploadOutParkOrder", orderElement);
                                    if (CommonUtils.isEmpty(responseStr)) {
                                        LOGGER.info("出场订单数据重传失败" + responseStr);
                                    }
                                    JSONObject res = JSON.parseObject(responseStr);
                                    if ("200".equals(res.getString("code"))) {
                                        newOrderParkService.deleteSuccessInfo(wechatPayFailUrl.getId());
                                    } else {
                                        LOGGER.info("出场订单数据重传失败" + responseStr);
                                    }
                                } catch (IOException e) {
                                    LOGGER.error("出场订单数据重传失败" + e.getMessage());
                                }
                            } else if (wechatPayFailUrl.getIsUsed().equals(2)) {
                                try {
                                    String responseStr = HttpRequestUtils.postJson("http://" + AppInfo.cloudIp + ":" + AppInfo.cloudPort + "/payment/parkInfo/uploadInChargeTimeOrder", orderElement);
                                    if (CommonUtils.isEmpty(responseStr)) {
                                        LOGGER.info("订单数据重传失败");
                                    }
                                    JSONObject res = JSON.parseObject(responseStr);
                                    if ("200".equals(res.getString("code"))) {
                                        newOrderParkService.deleteSuccessInfo(wechatPayFailUrl.getId());
                                    } else {
                                        LOGGER.info("订单数据重传失败");
                                    }
                                } catch (IOException e) {
                                    LOGGER.info("订单数据重传失败");
                                }
                            }
                        }
                    }
                }
            }catch (BizException ex){
                LOGGER.error(ex.getMessage());
            }
        } catch (Exception ex) {
            LOGGER.error("数据业务重传定时器错误:", ex);
        }
    }
}
