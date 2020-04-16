package com.starnetsecurity.commonService.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by 宏炜 on 2017-11-28.
 */
public interface DeviceManageUtils {

    /**
     * 更新设备名称
     * @param ip
     * @param username
     * @param password
     * @param deviceName
     */
    void updateDeviceName(String ip,Integer port,String username,String password,String deviceName);

    /**
     * 获取设备名称
     * @param ip
     * @param username
     * @param password
     * @return
     */
    String getDeviceName(String ip,Integer port,String username,String password);

    /**
     * 获取设备信息
     * @param ip
     * @param username
     * @param password
     * @return
     */
    JSONObject getDeviceInfo(String ip,Integer port,String username,String password);

    /**
     * 更新设备时间设置
     * @param ip
     * @param username
     * @param password
     * @param params
     */
    void updateDeviceTimeSet(String ip,Integer port,String username,String password,JSONObject params);

    /**
     * 获取设备时间设置
     * @param ip
     * @param username
     * @param password
     * @return
     */
    JSONObject getDeviceTimeSet(String ip,Integer port,String username,String password);

    /**
     * 获取设备日志
     * @param ip
     * @param username
     * @param password
     * @return
     */
    List getDeviceLog(String ip,Integer port, String username, String password,JSONObject params);


    /**
     * 更新场景配置信息
     * @param ip
     * @param username
     * @param password
     * @param params
     */
    void updateSceneInfo(String ip,Integer port,String username,String password,JSONObject params);

    /**
     * 获取场景配置信息
     * @param ip
     * @param username
     * @param password
     * @return
     */
    JSONObject getSceneInfo(String ip,Integer port,String username,String password);

    /**
     * 更新识别配置信息
     * @param ip
     * @param username
     * @param password
     * @param params
     */
    void updateRecoSceneInfo(String ip,Integer port,String username,String password,JSONObject params);

    /**
     * 获取识别配置信息
     * @param ip
     * @param username
     * @param password
     * @return
     */
    JSONObject getRecoSceneInfo(String ip,Integer port,String username,String password);

    /**
     * 更新系统配置信息
     * @param ip
     * @param username
     * @param password
     * @param params
     */
    void updateSysInfo(String ip,Integer port,String username,String password,JSONObject params);

    /**
     * 获取系统配置信息
     * @param ip
     * @param username
     * @param password
     * @return
     */
    JSONObject getSysInfo(String ip,Integer port,String username,String password);

    /**
     * 更新白名单配置信息
     * @param ip
     * @param username
     * @param password
     * @param params
     */
    void updateWhiteSet(String ip,Integer port,String username,String password,JSONObject params);

    /**
     * 获取白名单配置信息
     * @param ip
     * @param username
     * @param password
     * @return
     */
    JSONObject getWhiteSet(String ip,Integer port,String username,String password);

    /**
     * @Author chenbinbin
     * @Description 添加单个白名单
     * @Date 14:26 2020/3/2
     * @Param [ip, port, username, password, params]
     * @return java.lang.Integer
     **/
    Integer addWhiteMember(String ip,Integer port,String username,String password,JSONObject params);

    /**
     * 添加白名单数据
     * @param ip
     * @param username
     * @param password
     */
    void addWhiteMemberList(String ip,Integer port,String username,String password,JSONObject params,int count);

    /**
     * @Author chenbinbin
     * @Description 删除白名单数据
     * @Date 14:20 2020/3/2
     * @Param [ip, port, username, password, params]
     * @return java.lang.Integer
     **/
    Integer delWhiteMember(String ip,Integer port,String username,String password,JSONObject params);

    /**
     * 获取所有白名单数据
     * @param ip
     * @param username
     * @param password
     * @return
     */
    List getWhiteMemberList(String ip,Integer port,String username,String password);

    /**
     * @Author chenbinbin
     * @Description 更新单个白名单
     * @Date 14:27 2020/3/2
     * @Param [ip, port, username, password, params]
     * @return void
     **/
    Integer updateWhiteMember(String ip,Integer port,String username,String password,JSONObject params);

    /**
     * 导入白名单数据
     * @param ip
     * @param username
     * @param password
     */
    void importWhiteMemberList(String ip,Integer port,String username,String password,List<JSONObject> list);

    /**
     * 查询白名单数据
     * @param ip
     * @param username
     * @param password
     * @param params
     * @return
     */
    JSONObject queryWhiteMember(String ip,Integer port,String username,String password,JSONObject params);

    /**
     * 增加黑名单
     * @param ip
     * @param port
     * @param username
     * @param password
     * @param params
     */
    void addBlackMember(String ip,Integer port,String username,String password,JSONObject params);

    /**
     * 更新黑名单
     * @param ip
     * @param port
     * @param username
     * @param password
     * @param params
     */
    void updateBlackMember(String ip,Integer port,String username,String password,JSONObject params);

    /**
     * 删除黑名单
     * @param ip
     * @param port
     * @param username
     * @param password
     * @param params
     */
    void delBlackMember(String ip,Integer port,String username,String password,JSONObject params);

    /**
     * 更新道闸控制信息
     * @param ip
     * @param port
     * @param username
     * @param password
     * @param params
     */
    void UpdateGateControlInfo(String ip,Integer port,String username,String password,JSONObject params);

    /**
     * 开启道闸
     * @param ip
     * @param username
     * @param password
     */
    void openRoadGate(String ip,Integer port,String username,String password);

    /**
     * 关闭道闸
     * @param ip
     * @param username
     * @param password
     */
    void closeRoadGate(String ip,Integer port,String username,String password);

    /**
     * 停止道闸
     * @param ip
     * @param username
     * @param password
     */
    void stopRoadGate(String ip,Integer port,String username,String password);

    /**
     * 更新显示播报信息
     * @param ip
     * @param username
     * @param password
     * @param params
     */
    void updateBroadcastDisplayInfo(String ip,Integer port,String username,String password,JSONObject params);

    /**
     * 获取显示播报信息
     * @param ip
     * @param username
     * @param password
     * @return
     */
    JSONObject getBroadcastDisplayInfo(String ip,Integer port,String username,String password);

    /**
     * 显示屏输出控制
     * @param ip
     * @param username
     * @param password
     * @param params
     */
    public void screenOutput(String ip,Integer port, String username, String password, JSONObject params,List<JSONObject> resList);

    /**
     * 更新智能纠错信息
     * @param ip
     * @param username
     * @param password
     */
    void updateAutoErrorRecoInfo(String ip,Integer port,String username,String password, List<JSONObject> list,int enabledIntellMatch);

    /**
     * 获取智能纠错信息
     * @param ip
     * @param username
     * @param password
     * @return
     */
    JSONObject getAutoErrorRecoInfo(String ip,Integer port,String username,String password);

    /**
     * 语音播报器输出控制
     * @param ip
     * @param username
     * @param password
     * @param params
     */
    void audioOutput(String ip,Integer port,String username,String password,JSONObject params);

    /**
     * 显示屏语音测试
     * @param ip
     * @param username
     * @param password
     * @param params
     */
    void showScreenAudioTest(String ip,Integer port,String username,String password,JSONObject params);

    /**
     * 语音播报器语音测试
     * @param ip
     * @param username
     * @param password
     * @param params
     */
    void showAudioBroadcastTest(String ip,Integer port,String username,String password,JSONObject params);

    /**
     * 更新视频配置信息
     * @param ip
     * @param username
     * @param password
     * @param params
     */
    void updateVideoSetInfo(String ip,Integer port,String username,String password,JSONObject params);

    /**
     * 获取视频配置信息
     * @param ip
     * @param username
     * @param password
     * @return
     */
    JSONObject getVideoSetInfo(String ip,Integer port,String username,String password);

    /**
     * 更新通道名称和时间
     * @param ip
     * @param username
     * @param password
     * @param params
     */
    void updateChannelNameAndTime(String ip,Integer port,String username,String password,JSONObject params);

    /**
     * 更新字符叠加
     * @param ip
     * @param username
     * @param password
     * @param params
     */
    void updateOsdoverlay(String ip,Integer port,String username,String password,JSONObject params);

    /**
     * 获取通道名称和时间
     * @param ip
     * @param username
     * @param password
     * @return
     */
    JSONObject getChannelNameAndTime(String ip,Integer port,String username,String password);

    /**
     * 获取字符叠加
     * @param ip
     * @param username
     * @param password
     * @return
     */
    JSONObject getOsdoverlay(String ip,Integer port,String username,String password);

    /**
     * 获取设备基本网络参数
     * @param ip
     * @param username
     * @param password
     * @return
     */
    JSONObject getDeviceNetParams(String ip,Integer port,String username,String password);

    /**
     * 设置设备基本网路参数
     */
    void setDeviceNetParams(String ip,Integer port,String username,String password,JSONObject params);

    /**
     * 获取端口信息
     * @param ip
     * @param username
     * @param password
     */
    JSONObject getDevicePortInfo(String ip,Integer port,String username,String password);

    /**
     * 设置设备端口信息
     * @param ip
     * @param username
     * @param password
     * @param params
     */
    void setDevicePortInfo(String ip,Integer port,String username,String password,JSONObject params);

    /**
     * 获取摄像机配置
     * @param ip
     * @param username
     * @param password
     * @return
     */
    JSONObject getIPCParams(String ip,Integer port,String username,String password);

    /**
     * 设置摄像机配置
     * @param ip
     * @param username
     * @param password
     * @return
     */
    void setIPCParams(String ip,Integer port,String username,String password);

    /**
     * 获取补光信息
     * @param ip
     * @param username
     * @param password
     * @return
     */
    JSONObject getLightInfo(String ip,Integer port,String username,String password);

    /**
     * 设置补光信息
     * @param ip
     * @param username
     * @param password
     */
    void setLightInfo(String ip,Integer port,String username,String password,JSONObject params);

    /**
     * 重启设备
     * @param ip
     * @param port
     * @param username
     * @param password
     */
    void reboot(String ip,Integer port,String username,String password);

    /**
     * 格式化SD卡
     * @param ip
     * @param port
     * @param username
     * @param password
     */
    void formatSD(String ip,Integer port,String username,String password);

    /**
     * 获取道闸控制信息
     * @param ip
     * @param port
     * @param username
     * @param password
     * @return
     */
    JSONObject getCarwhitelinkInfo(String ip,Integer port,String username,String password);

    /**
     * 更新道闸控制信息
     * @param ip
     * @param port
     * @param username
     * @param password
     * @param params
     * @return
     */
    void updateCarwhitelinkInfo(String ip,Integer port,String username,String password,JSONObject params);

    /**
     * 升级设备
     * @param ip
     * @param port
     * @param username
     * @param password
     * @param file
     */
    void updateDeviceSystem(String ip, Integer port, String username, String password, MultipartFile file);

    /**
     * 设置播报音量
     * @param ip
     * @param port
     * @param username
     * @param password
     */
    void setDeviceVoice(String ip, Integer port, String username, String password,Integer voice);

    /**
     * 更新智能纠错
     * @param ip
     * @param port
     * @param username
     * @param password
     * @param params
     */
    void updateSetIntellMatch(String ip,Integer port,String username,String password,JSONObject params);

    /**
     * 更新485配置信息
     * @param ip
     * @param port
     * @param username
     * @param password
     * @param params
     */
    void  setFourEightFiveInfo(String ip,Integer port,String username,String password,JSONObject params);


}
