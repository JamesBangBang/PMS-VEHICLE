package com.starnetsecurity.parkClientServer.service;

import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.parkClientServer.entity.WechatPayFailUrl;
import com.starnetsecurity.starParkService.entityEnum.OrderParkEnum;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created by 宏炜 on 2017-07-18.
 */
public interface OrderParkService {
    String addUploadParkImage(String imagePath);

    //获取车道信息
    Map getManualRoadInfo();
    //获取交接班信息
    Map getShiftInfo(String operatorUserName,String loginTime,String token);
    //获取会员信息
    Map getMemberWalletInfo(String carno);
    //获取设备信息
    Map getDeviceInfo();
    //获取车场及进出记录信息
    Map getParkingInfo(String ips,String operatorUserName,String loginTime);
    //控制道闸开关闸
    Map controlOpenGate(String deviceIp,String controlMode,String operatorName,String postIp);
    //获取入场记录
    Map getMatchCarnoInfo(String carparkId,String carno,Integer pageNum,Long startTime,Long endTime) throws UnsupportedEncodingException;
    //重新匹配信息
    Map getRematchCarnoInfo(String inRecordId,String outMatchTime,String outCarno,String carnoColor);
    //获取场内缴费金额
    Map getPrechargeInfo(String carparkId,String outCarno,String outTime);
}
