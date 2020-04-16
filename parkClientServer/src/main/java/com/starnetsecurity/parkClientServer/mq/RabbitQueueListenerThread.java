package com.starnetsecurity.parkClientServer.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.util.RabbitMqUtils;
import com.starnetsecurity.parkClientServer.clientEnum.ServiceEnum;
import com.starnetsecurity.parkClientServer.init.AppInfo;
import com.starnetsecurity.parkClientServer.service.ParkService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Map;

/**
 * Created by 宏炜 on 2017-08-01.
 */
public class RabbitQueueListenerThread extends Thread {

    private static Logger log = LoggerFactory.getLogger(RabbitQueueListenerThread.class);

    private Channel channel;

    private String queueName;

    private QueueingConsumer.Delivery delivery;

    private QueueingConsumer consumer;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    private ParkService parkService;

    public ParkService getParkService() {
        return parkService;
    }

    public void setParkService(ParkService parkService) {
        this.parkService = parkService;
    }

    @Override
    public void run() {
        this.channel = RabbitMqUtils.getReceiveChannel(queueName);
        this.consumer = new QueueingConsumer(this.channel);
        try {
            if(this.channel == null){
                checkQueueAndReopen();
            }else{
                this.channel.basicConsume(queueName, false, this.consumer);
            }

        } catch (IOException e) {

        }
        while (AppInfo.rabbitMqStatus){
            try {
                this.delivery = consumer.nextDelivery(5000);
                if(this.delivery == null){
                    continue;
                }
                String body = new String(this.delivery.getBody());
                if(!StringUtils.isBlank(body)){
                    JSONObject params = JSON.parseObject(body);
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
                }
                this.channel.basicAck(this.delivery.getEnvelope().getDeliveryTag(), false);
            } catch (InterruptedException e) {
                checkQueueAndReopen();
            } catch (IOException e) {
                checkQueueAndReopen();
            }catch (Exception e){
                checkQueueAndReopen();
            }
        }
//        log.error("消息队列接收监听器退出接收，queueName：{}",queueName);
    }

    public void checkQueueAndReopen(){
        while(this.channel == null || !this.channel.isOpen()){
            try {
                this.channel = RabbitMqUtils.getReceiveChannel(queueName);
                if(this.channel == null){
                    throw new BizException("消息队列重连失败，五秒后继续尝试");
                }
                this.consumer = new QueueingConsumer(this.channel);
                this.channel.basicConsume(queueName, false, this.consumer);
            } catch (IOException e) {

            }catch (BizException e){

            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
