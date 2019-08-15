package com.starnetsecurity.parkClientServer.service;

import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.parkClientServer.entity.DeviceInfo;
import com.starnetsecurity.parkClientServer.entity.LedDisplayConfig;
import com.starnetsecurity.parkClientServer.entity.MemberWallet;
import com.starnetsecurity.parkClientServer.sockServer.ClientBizEnum;
import com.starnetsecurity.parkClientServer.sockServer.SocketClient;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Map;

/**
 * Created by 宏炜 on 2017-12-14.
 */
public interface DeviceBizService {

    void mergeDevice(JSONObject json,String IP, SocketClient socketClient);

    /**
     * 获取需要推送的入场信息
     * @param socketClient
     * @param params
     * @return
     */
    void pushCarPlateIndMsg(SocketClient socketClient,JSONObject params) throws BizException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException;

    void updateDeviceStatus(String deviceId,String status);

    /**
     * LED屏幕显示和语音播报
     * @param ipcDevId
     * @param inOutType
     * @param ledPlayInfo
     * @param ledDisplayConfig
     * @return
     */
    void PlayLedInfo(String ipcDevId,Integer inOutType,JSONObject ledPlayInfo,LedDisplayConfig ledDisplayConfig,
                     String ledType,String ledId);

    /**
     * 获取LED播报场景
     * @param ledId
     * @param inOutType
     * @param criticalDay
     * @param isNeedOpenGate
     * @param inOutTime
     * @param memberWallet
     * @param memberLeftInfo
     * @return
     */
    Map<String,Object> GetLedPlaySence(String ledId, Integer inOutType, Integer criticalDay, Integer isNeedOpenGate,
                                       Timestamp inOutTime, MemberWallet memberWallet, String memberLeftInfo);

    /**
     * 车牌模糊匹配
     * @param oldCarno
     * @param isUseWhileList
     * @param isAutoMatch
     * @param matchBite
     * @param carparkId
     * @param whiteCorrection
     * @return
     */
    String RedressCarno(String oldCarno,String isUseWhileList,String isAutoMatch,String matchBite,String autoMatchPosition,
                        String carparkId,String whiteCorrection);


    /**
     * 判断是否为白名单
     * @param carno
     * @param carparkId
     * @return
     */
     boolean IsWhiteListCarno(String carno,String carparkId);

    /**
     * 未匹配出场车牌纠正
     * @param carparkId
     * @return
     */
    String GetTmpMatchCarno(String oldCarno,String isUseWhileList,String isAutoMatch,String matchBite,String autoMatchPosition,
                            String carparkId,String intelCorrection);



    void updateRealChannelConfig(DeviceInfo deviceInfo,String carNo,Timestamp realTime,String fileName);


    void dealCarPlateInd(SocketClient socketClient,JSONObject params);

    void dealHistoryCarPlateInd(SocketClient socketClient,JSONObject params) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException;

    String GetPlayStr(JSONObject params,LedDisplayConfig ledDisplayConfig,int playChannel);
}
