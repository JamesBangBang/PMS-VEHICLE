package com.starnetsecurity.parkClientServer.service;

import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.parkClientServer.entity.*;
import com.starnetsecurity.parkClientServer.sockServer.SocketClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Map;

/**
 * Created by 宏炜 on 2017-12-11.
 */
public interface ClientBizService {

    JSONObject login(String username, String password, String ips, SocketClient socketClient) throws BizException;

    JSONObject logout(SocketClient socketClient) throws BizException;

    JSONObject shiftLogout(SocketClient socketClient) throws BizException;

    JSONObject handleInOutRecord(JSONObject params,SocketClient socketClient) throws BizException, IOException;

    void handleAutoRelease(JSONObject params, CarparkInfo carparkInfo, InOutCarRoadInfo inOutCarRoadInfo, MemberKind memberKind, MemberWallet memberWallet) throws IOException;

    JSONObject pushManualRecord(JSONObject params,SocketClient socketClient) throws BizException, IOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException;

    JSONObject shiftOperator(JSONObject params,SocketClient socketClient) throws  BizException;

    void reconnectLogin(String username, String password, String ips, SocketClient socketClient) throws BizException;

    void updatePostComputerStatus(PostComputerManage computerManage,int status);

    PostComputerManage getPostComputerManage(String roadId);

    String getCarTypeInfo(String packKind);

    /**
     * 获取是否自动开闸等信息
     * @param carPlate
     * @param inOutType
     * @param inOutTime
     * @param carparkInfo
     * @param memberWallet
     * @param gateState
     * @param chargeAmount
     * @return
     */
    Map<String,String> IsNeedOpenGate(String carPlate, Integer inOutType, Timestamp inOutTime, CarparkInfo carparkInfo, MemberWallet memberWallet,
                                      String gateState, double chargeAmount);

    Map<String,String> GetMemberWalletInfo(String carPlate,Integer inOutType,Timestamp inOutTime,MemberWallet memberWallet);

    String GetOtherCarno(String carPlate,String memberNo);

    OrderInoutRecord GetMatchInfo(String carPlate,String carparkId,Timestamp outTime);

    String ChangeBase64ToStr(String str);

    void WorkForMultiCarno(String carno,Integer inOutType,String carparkId,OrderInoutRecord orderInoutRecord);

    String CarnoIntelligentCorrect(String correctStr,String oldCarno);
}
