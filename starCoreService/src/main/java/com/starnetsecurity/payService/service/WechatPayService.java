package com.starnetsecurity.payService.service;

import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.payService.entity.OrderTransaction;
import me.chanjar.weixin.common.exception.WxErrorException;

import java.util.Map;

/**
 * Created by JamesBangBang on 2017/7/13.
 * 微信支付服务接口
 */
public interface WechatPayService {

    Map savePlaceWxOrder(String wxConfigId,String body,Double fee,String openId,String notifyURL,String userIP) throws Exception;

    Map<String,Object> updateQueryOrderResult(String transactionId,String wxConfigId) throws WxErrorException;

    OrderTransaction updateAndCheckOrderTransaction(String xmlString) throws BizException, WxErrorException;

}
