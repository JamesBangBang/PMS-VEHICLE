package com.starnetsecurity.parkClientServer.service;

import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.exception.BizException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Created by 宏炜 on 2017-12-29.
 */
public interface DeviceManageService {

    List<Map<String,Object>> getPageList(Integer size, Integer page);

    Long getCount();

    List getIPCSelect();

    Map getDeviceInfo(String id);

    void updateDeviceBasicData(JSONObject params);

    void checkSubIpc(String id,String subId) throws BizException;

    void checkRelateIpc(String id,String relateId) throws BizException;

    void updateDeviceSystem(JSONObject params) throws BizException;

    void updateLightAndOsd(JSONObject params) throws BizException;

    void updateScene(JSONObject params) throws BizException;

    void rebootDevice(String id) throws BizException;

    void formatSDCard(String id) throws BizException;

    List getLedList(String devId) throws BizException;

    void updateLed(JSONObject params) throws BizException;

    List getAudioList(String devId) throws BizException;

    void updateAudio(JSONObject params) throws BizException;

    List getIpcInfoList() throws BizException;

    List getIpcInfoList(String parkId) throws BizException;

    void formatDeviceTime() throws BizException;

    void updateDeviceSystem(String deviceId, MultipartFile file);

    void testLed(JSONObject params);

    void testAudio(JSONObject params);
}
