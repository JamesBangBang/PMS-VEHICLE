package com.starnetsecurity.parkClientServer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.commonService.util.*;
import com.starnetsecurity.parkClientServer.entity.*;
import com.starnetsecurity.parkClientServer.entityEnum.payStatusEnum;
import com.starnetsecurity.parkClientServer.init.AppInfo;
import com.starnetsecurity.parkClientServer.service.*;
import com.starnetsecurity.parkClientServer.sockServer.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.apache.shiro.codec.Base64;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import com.starnetsecurity.parkClientServer.init.AppInfo;


/**
 * Created by 宏炜 on 2017-12-14.
 */
@Service
public class DeviceBizServiceImpl implements DeviceBizService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceBizServiceImpl.class);

    //一路顺风以后均属于四行显示屏专用
    private String[] ledDisplayKeyWords= {"当前时间","空位xxxx","车牌号码","剩余xxx","停车xx小时xx分钟","车辆类型","车牌号码-车辆类型",
                                          "缴：xxx","xx场空位xx","请缴费xx元","禁行原因","欢迎光临","一路顺风","请交费xx元","一位多车提示",
                                          "车牌号码-车主姓名","空闲时间"};

    private static int aucCRCHi[] = {
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
            0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
            0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40
    };

    private static int aucCRCLo[] = {
            0x00, 0xC0, 0xC1, 0x01, 0xC3, 0x03, 0x02, 0xC2, 0xC6, 0x06, 0x07, 0xC7,
            0x05, 0xC5, 0xC4, 0x04, 0xCC, 0x0C, 0x0D, 0xCD, 0x0F, 0xCF, 0xCE, 0x0E,
            0x0A, 0xCA, 0xCB, 0x0B, 0xC9, 0x09, 0x08, 0xC8, 0xD8, 0x18, 0x19, 0xD9,
            0x1B, 0xDB, 0xDA, 0x1A, 0x1E, 0xDE, 0xDF, 0x1F, 0xDD, 0x1D, 0x1C, 0xDC,
            0x14, 0xD4, 0xD5, 0x15, 0xD7, 0x17, 0x16, 0xD6, 0xD2, 0x12, 0x13, 0xD3,
            0x11, 0xD1, 0xD0, 0x10, 0xF0, 0x30, 0x31, 0xF1, 0x33, 0xF3, 0xF2, 0x32,
            0x36, 0xF6, 0xF7, 0x37, 0xF5, 0x35, 0x34, 0xF4, 0x3C, 0xFC, 0xFD, 0x3D,
            0xFF, 0x3F, 0x3E, 0xFE, 0xFA, 0x3A, 0x3B, 0xFB, 0x39, 0xF9, 0xF8, 0x38,
            0x28, 0xE8, 0xE9, 0x29, 0xEB, 0x2B, 0x2A, 0xEA, 0xEE, 0x2E, 0x2F, 0xEF,
            0x2D, 0xED, 0xEC, 0x2C, 0xE4, 0x24, 0x25, 0xE5, 0x27, 0xE7, 0xE6, 0x26,
            0x22, 0xE2, 0xE3, 0x23, 0xE1, 0x21, 0x20, 0xE0, 0xA0, 0x60, 0x61, 0xA1,
            0x63, 0xA3, 0xA2, 0x62, 0x66, 0xA6, 0xA7, 0x67, 0xA5, 0x65, 0x64, 0xA4,
            0x6C, 0xAC, 0xAD, 0x6D, 0xAF, 0x6F, 0x6E, 0xAE, 0xAA, 0x6A, 0x6B, 0xAB,
            0x69, 0xA9, 0xA8, 0x68, 0x78, 0xB8, 0xB9, 0x79, 0xBB, 0x7B, 0x7A, 0xBA,
            0xBE, 0x7E, 0x7F, 0xBF, 0x7D, 0xBD, 0xBC, 0x7C, 0xB4, 0x74, 0x75, 0xB5,
            0x77, 0xB7, 0xB6, 0x76, 0x72, 0xB2, 0xB3, 0x73, 0xB1, 0x71, 0x70, 0xB0,
            0x50, 0x90, 0x91, 0x51, 0x93, 0x53, 0x52, 0x92, 0x96, 0x56, 0x57, 0x97,
            0x55, 0x95, 0x94, 0x54, 0x9C, 0x5C, 0x5D, 0x9D, 0x5F, 0x9F, 0x9E, 0x5E,
            0x5A, 0x9A, 0x9B, 0x5B, 0x99, 0x59, 0x58, 0x98, 0x88, 0x48, 0x49, 0x89,
            0x4B, 0x8B, 0x8A, 0x4A, 0x4E, 0x8E, 0x8F, 0x4F, 0x8D, 0x4D, 0x4C, 0x8C,
            0x44, 0x84, 0x85, 0x45, 0x87, 0x47, 0x46, 0x86, 0x82, 0x42, 0x43, 0x83,
            0x41, 0x81, 0x80, 0x40
    };

    private class CarnoMatchInfo{
        public String matchCarno;
        public Integer matchPercent;
        public String matchInTime;
        public String matchSecChar;
        public String matchPrioCity;
    }


    @Autowired
    HibernateBaseDao baseDao;

    @Autowired
    CalculateFeeNewService calculateFeeNewService;

    @Autowired
    ClientBizService clientBizService;


    @Autowired
    NewOrderParkService newOrderParkService;

    @Override
    public void mergeDevice(JSONObject json, String IP, SocketClient socketClient) {
        try {
            String DeviceSN = json.getString("DeviceSN");
            String DeviceName = Base64.decodeToString(json.getString("DeviceName"));
            String AuthInfoBase64 = json.getString("AuthInfo");
            String ServerPort = json.getString("ServerPort");
            String AuthInfo = Base64.decodeToString(AuthInfoBase64);
            String[] tmp = AuthInfo.split(":");
            String username = tmp[0];
            String password = tmp[1];

            String hql = "from DeviceInfo where deviceSn = ? ";
            DeviceInfo deviceInfo = (DeviceInfo)baseDao.getUnique(hql,DeviceSN);
            if(CommonUtils.isEmpty(deviceInfo)){
                deviceInfo = new DeviceInfo();
                deviceInfo.setAddTime(CommonUtils.getTimestamp());

                DeviceManageUtils deviceManageUtils = new StarnetDeviceUtils();
                String deviceName = deviceManageUtils.getDeviceName(IP,Integer.parseInt(ServerPort),username,password);
                deviceInfo.setDeviceName(deviceName);

                deviceInfo.setDevStatus("1");
                deviceInfo.setDeviceType("0");
                deviceInfo.setRtspPort("554");
                deviceInfo.setUartDeviceAddr(";8081;;;1");
                deviceInfo.setUartFlowCtrl(0);
                deviceInfo.setUartProtocolType(0);
                deviceInfo.setRelateTime("5");
                deviceInfo.setUseMark(0);
                deviceInfo.setUartBaudRate("");
                deviceInfo.setUartProtocolType(0);
                deviceInfo.setDeviceSn(DeviceSN);

            }
            deviceInfo.setDeviceIp(IP);
            deviceInfo.setDevicePort(ServerPort);
            deviceInfo.setDeviceUsername(username);
            deviceInfo.setDevicePwd(password);
            deviceInfo.setDevStatus("1");
            if(!StringUtils.isBlank(deviceInfo.getOwnCarRoad())){
                InOutCarRoadInfo inOutCarRoadInfo = (InOutCarRoadInfo)baseDao.getById(InOutCarRoadInfo.class,deviceInfo.getOwnCarRoad());
                if(!CommonUtils.isEmpty(inOutCarRoadInfo)){
                    PostComputerManage postComputerManage = (PostComputerManage)baseDao.getById(PostComputerManage.class,inOutCarRoadInfo.getManageComputerId());
                    socketClient.setPostComputerManage(postComputerManage);
                }
            }


            baseDao.save(deviceInfo);
            socketClient.setChannelId(deviceInfo.getDeviceId());
            socketClient.setDeviceInfo(deviceInfo);

            hql = "from DeviceInfo where parentDeviceId = ? and deviceType = ?";
            DeviceInfo ledDevInfo = (DeviceInfo)baseDao.getUnique(hql,deviceInfo.getDeviceId(),"3");
            if (CommonUtils.isEmpty(ledDevInfo)){
                hql = "from DeviceInfo where parentDeviceId = ? and deviceType = ?";
                ledDevInfo = (DeviceInfo)baseDao.getUnique(hql,deviceInfo.getDeviceId(),"11");
                if (CommonUtils.isEmpty(ledDevInfo)){
                    hql = "from DeviceInfo where parentDeviceId = ? and deviceType = ?";
                    ledDevInfo = (DeviceInfo)baseDao.getUnique(hql,deviceInfo.getDeviceId(),"12");
                }
            }
            boolean isNeedCreateLed = false;
            if(CommonUtils.isEmpty(ledDevInfo)){
                ledDevInfo = new DeviceInfo();
                ledDevInfo.setDeviceName(deviceInfo.getDeviceName()+"_显示屏");
                ledDevInfo.setDeviceType("3");
                ledDevInfo.setDeviceChannelNo("2");
                ledDevInfo.setParentDeviceId(deviceInfo.getDeviceId());
                ledDevInfo.setDeviceSubType("0");
                ledDevInfo.setAddTime(CommonUtils.getTimestamp());
                ledDevInfo.setDevStatus("0");
                ledDevInfo.setUartFlowCtrl(0);
                ledDevInfo.setUartProtocolType(0);
                ledDevInfo.setVoiceChannel(0);
                ledDevInfo.setUseMark(0);
                ledDevInfo.setUartBaudRate("");
                ledDevInfo.setUartDeviceAddr("");
                baseDao.save(ledDevInfo);

                String sql = "INSERT INTO `led_display_config` (`id`, `dev_id`, `scene_no`, `scene`, `top_row_content`, `middle_row_content`, `buttom_row_content`, `voice_broadcast`, `voice_broadcast_volume`, `display_mode`, `moving_direction`, `movement_speed`, `operation_source`, `add_time`) " +
                        "VALUES (replace(uuid(),'-',''), '" + ledDevInfo.getDeviceId() + "', '3', '出口（常规）', '当前时间', '车牌号码', '一路顺风', '一路顺风', '7', '0', '000', '000', '', '" + CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",CommonUtils.getTimestamp()) + "')";
                SQLQuery sqlQuery =  baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
                sqlQuery.executeUpdate();
                sql = "INSERT INTO `led_display_config` (`id`, `dev_id`, `scene_no`, `scene`, `top_row_content`, `middle_row_content`, `buttom_row_content`, `voice_broadcast`, `voice_broadcast_volume`, `display_mode`, `moving_direction`, `movement_speed`, `operation_source`, `add_time`) " +
                        "VALUES (replace(uuid(),'-',''), '" + ledDevInfo.getDeviceId() + "', '6', '禁行状态', '车牌号码-车辆类型', '禁行原因', '禁止通行', '禁止通行', '0', '0', '000', '000', '', '" + CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",CommonUtils.getTimestamp()) + "')";
                sqlQuery =  baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
                sqlQuery.executeUpdate();
                sql = "INSERT INTO `led_display_config` (`id`, `dev_id`, `scene_no`, `scene`, `top_row_content`, `middle_row_content`, `buttom_row_content`, `voice_broadcast`, `voice_broadcast_volume`, `display_mode`, `moving_direction`, `movement_speed`, `operation_source`, `add_time`) " +
                        "VALUES (replace(uuid(),'-',''), '" + ledDevInfo.getDeviceId() + "', '5', '综合空位显示', 'xx场空位xx', 'xx场空位xx', 'xx场空位xx', '', '7', '0', '000', '000', '', '" + CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",CommonUtils.getTimestamp()) + "')";
                sqlQuery =  baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
                sqlQuery.executeUpdate();
                sql = "INSERT INTO `led_display_config` (`id`, `dev_id`, `scene_no`, `scene`, `top_row_content`, `middle_row_content`, `buttom_row_content`, `voice_broadcast`, `voice_broadcast_volume`, `display_mode`, `moving_direction`, `movement_speed`, `operation_source`, `add_time`) " +
                        "VALUES (replace(uuid(),'-',''), '" + ledDevInfo.getDeviceId() + "', '0', '车道没有车辆', '当前时间', '空位xxxx', '欢迎光临', '', '7', '0', '000', '000', '', '" + CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",CommonUtils.getTimestamp()) + "')";
                sqlQuery =  baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
                sqlQuery.executeUpdate();
                sql = "INSERT INTO `led_display_config` (`id`, `dev_id`, `scene_no`, `scene`, `top_row_content`, `middle_row_content`, `buttom_row_content`, `voice_broadcast`, `voice_broadcast_volume`, `display_mode`, `moving_direction`, `movement_speed`, `operation_source`, `add_time`) " +
                        "VALUES (replace(uuid(),'-',''), '" + ledDevInfo.getDeviceId() + "', '2', '入口(缴费到期30天内提醒)', '当前时间', '车牌号码', '请及时缴费', '剩余xxx', '7', '0', '000', '000', '', '" + CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",CommonUtils.getTimestamp()) + "')";
                sqlQuery =  baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
                sqlQuery.executeUpdate();
                sql = "INSERT INTO `led_display_config` (`id`, `dev_id`, `scene_no`, `scene`, `top_row_content`, `middle_row_content`, `buttom_row_content`, `voice_broadcast`, `voice_broadcast_volume`, `display_mode`, `moving_direction`, `movement_speed`, `operation_source`, `add_time`) " +
                        "VALUES (replace(uuid(),'-',''), '" + ledDevInfo.getDeviceId() + "', '1', '入口识别到车辆', '当前时间', '车牌号码', '欢迎光临', '欢迎光临', '7', '0', '000', '000', '', '" + CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",CommonUtils.getTimestamp()) + "')";
                sqlQuery =  baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
                sqlQuery.executeUpdate();
                sql = "INSERT INTO `led_display_config` (`id`, `dev_id`, `scene_no`, `scene`, `top_row_content`, `middle_row_content`, `buttom_row_content`, `voice_broadcast`, `voice_broadcast_volume`, `display_mode`, `moving_direction`, `movement_speed`, `operation_source`, `add_time`) " +
                        "VALUES (replace(uuid(),'-',''), '" + ledDevInfo.getDeviceId() + "', '4', '出口（收费）', '停车xx小时xx分钟', '车牌号码-车辆类型', '缴：xxx', '请缴费xx元', '7', '0', '000', '000', '', '" + CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",CommonUtils.getTimestamp()) + "')";
                sqlQuery =  baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
                sqlQuery.executeUpdate();

                isNeedCreateLed = true;
            }
            hql = "from DeviceInfo where parentDeviceId = ? and deviceType = ?";
            DeviceInfo audioDevInfo = (DeviceInfo)baseDao.getUnique(hql,deviceInfo.getDeviceId(),"10");
            if(CommonUtils.isEmpty(audioDevInfo)){
                audioDevInfo = new DeviceInfo();
                audioDevInfo.setDeviceName(deviceInfo.getDeviceName()+"_语音播报器");
                audioDevInfo.setParentDeviceId(deviceInfo.getDeviceId());
                audioDevInfo.setDeviceType("10");
                audioDevInfo.setDeviceChannelNo("3");

                audioDevInfo.setDeviceSubType("0");
                audioDevInfo.setAddTime(CommonUtils.getTimestamp());
                audioDevInfo.setDevStatus("0");
                audioDevInfo.setUartFlowCtrl(0);
                audioDevInfo.setUartProtocolType(0);
                audioDevInfo.setVoiceChannel(0);
                audioDevInfo.setUseMark(0);
                audioDevInfo.setUartBaudRate("");
                audioDevInfo.setUartDeviceAddr("");
                baseDao.save(audioDevInfo);

                String sql = "INSERT INTO `led_display_config` (`id`, `dev_id`, `scene_no`, `scene`, `top_row_content`, `middle_row_content`, `buttom_row_content`, `voice_broadcast`, `voice_broadcast_volume`, `display_mode`, `moving_direction`, `movement_speed`, `operation_source`, `add_time`) " +
                        "VALUES (replace(uuid(),'-',''), '" + audioDevInfo.getDeviceId() + "', '2', '入口(缴费到期30天内提醒)', NULL, '', '', '剩余xxx', '7', '', '', '', '', '" + CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",CommonUtils.getTimestamp()) + "')";
                SQLQuery sqlQuery =  baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
                sqlQuery.executeUpdate();
                sql = "INSERT INTO `led_display_config` (`id`, `dev_id`, `scene_no`, `scene`, `top_row_content`, `middle_row_content`, `buttom_row_content`, `voice_broadcast`, `voice_broadcast_volume`, `display_mode`, `moving_direction`, `movement_speed`, `operation_source`, `add_time`) " +
                        "VALUES (replace(uuid(),'-',''), '" + audioDevInfo.getDeviceId() + "', '4', '出口（收费）', NULL, '', '', '请缴费xx元', '7', '', '', '', '', '" + CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",CommonUtils.getTimestamp()) + "')";
                sqlQuery =  baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
                sqlQuery.executeUpdate();
                sql = "INSERT INTO `led_display_config` (`id`, `dev_id`, `scene_no`, `scene`, `top_row_content`, `middle_row_content`, `buttom_row_content`, `voice_broadcast`, `voice_broadcast_volume`, `display_mode`, `moving_direction`, `movement_speed`, `operation_source`, `add_time`) " +
                        "VALUES (replace(uuid(),'-',''), '" + audioDevInfo.getDeviceId() + "', '3', '出口（常规）', NULL, '', '', '一路顺风', '7', '', '', '', '', '" + CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",CommonUtils.getTimestamp()) + "')";
                sqlQuery =  baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
                sqlQuery.executeUpdate();
                sql = "INSERT INTO `led_display_config` (`id`, `dev_id`, `scene_no`, `scene`, `top_row_content`, `middle_row_content`, `buttom_row_content`, `voice_broadcast`, `voice_broadcast_volume`, `display_mode`, `moving_direction`, `movement_speed`, `operation_source`, `add_time`) " +
                        "VALUES (replace(uuid(),'-',''), '" + audioDevInfo.getDeviceId() + "', '6', '禁行状态', NULL, '', '', '禁止通行', '0', '', '', '', '', '" + CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",CommonUtils.getTimestamp()) + "')";
                sqlQuery =  baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
                sqlQuery.executeUpdate();
                sql = "INSERT INTO `led_display_config` (`id`, `dev_id`, `scene_no`, `scene`, `top_row_content`, `middle_row_content`, `buttom_row_content`, `voice_broadcast`, `voice_broadcast_volume`, `display_mode`, `moving_direction`, `movement_speed`, `operation_source`, `add_time`) " +
                        "VALUES (replace(uuid(),'-',''), '" + audioDevInfo.getDeviceId() + "', '1', '入口识别到车辆', NULL, '', '', '欢迎光临', '7', '', '', '', '', '" + CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",CommonUtils.getTimestamp()) + "')";
                sqlQuery =  baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
                sqlQuery.executeUpdate();
            }

            hql = "from DeviceInfo where parentDeviceId = ? and deviceType = ?";
            DeviceInfo gateDevInfo = (DeviceInfo)baseDao.getUnique(hql,deviceInfo.getDeviceId(),"2");
            if(CommonUtils.isEmpty(gateDevInfo)){
                gateDevInfo = new DeviceInfo();
                gateDevInfo.setDeviceName(deviceInfo.getDeviceName()+"_道闸");
                gateDevInfo.setParentDeviceId(deviceInfo.getDeviceId());
                gateDevInfo.setDeviceType("2");

                gateDevInfo.setDevicePort("");
                gateDevInfo.setRelateRoadGateId("");
                gateDevInfo.setVoice("");
                gateDevInfo.setRtspPort("");
                gateDevInfo.setDeviceSubType("");
                gateDevInfo.setDeviceSn("");
                gateDevInfo.setAddTime(CommonUtils.getTimestamp());
                gateDevInfo.setDevStatus("0");
                gateDevInfo.setUartDeviceAddr("1");
                gateDevInfo.setUartDataBit(3);
                gateDevInfo.setUartFlowCtrl(0);
                gateDevInfo.setUartProtocolType(0);
                gateDevInfo.setUseMark(0);
                gateDevInfo.setUartBaudRate("-99999");
                baseDao.save(gateDevInfo);

            }
            if (isNeedCreateLed){
                StarnetDeviceUtils starnetDeviceUtils = new StarnetDeviceUtils();
                JSONObject params = new JSONObject();
                params.put("uartID",2);
                params.put("uart.baudrate",6);
                params.put("uart.databit",8);
                params.put("uart.stopbit",1);
                params.put("uart.checkbit",2);
                params.put("uart.streamcontrol",0);
                params.put("uart.decompilertype",0);
                params.put("uart.decompileraddr",0);
                starnetDeviceUtils.setFourEightFiveInfo(deviceInfo.getDeviceIp(),Integer.valueOf(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),
                        deviceInfo.getDevicePwd(),params);
            }
        } catch (Exception e) {
            LOGGER.error("设备注册异常");
        }

    }

    @Override
    public void pushCarPlateIndMsg(SocketClient socketClient, JSONObject params ) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException {
        if(CommonUtils.isEmpty(socketClient.getDeviceInfo())){
            return;
        }
        if(CommonUtils.isEmpty(socketClient.getDeviceInfo().getOwnCarRoad())){
            return;
        }

        DeviceManageUtils deviceManageUtils = new StarnetDeviceUtils();
        String carPlate = params.getString("CarPlate");
        String CarPlateColor = params.getString("Color");
        String carType = "1";          //默认为小型车
        if(CarPlateColor.equals("1"))
            carType = "1";
        else if(CarPlateColor.equals("2"))
            carType = "2";
        else
            carType = "0";
        if(!StringUtils.isBlank(carPlate)){
            byte[] carplates = Base64.decode(carPlate);
            try {
                carPlate = new String(carplates,"GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        String Time = params.getString("Time");
        String[] timeArrays = Time.substring(0,Time.length()).split("_");
        String date = timeArrays[0].replaceAll("-","");
        String time = timeArrays[1].replaceAll(":","");
        String fileName = carPlate.substring(1,carPlate.length()-1) + "_" + date + "_" + time + ".jpg";
        String fileUri = AppInfo.contextPath + "/resources/carPlateImages/" + date + "/" + socketClient.getSocket().getInetAddress().getHostAddress() + "/" + fileName;
        params.put("photoUri", fileUri);
        params.put("photoFileName", fileName);
        updateRealChannelConfig(socketClient.getDeviceInfo(),carPlate,CommonUtils.getTimestamp(),fileUri);

        JSONObject json = new JSONObject();
        params.put("deviceId",socketClient.getDeviceInfo().getDeviceId());

        InOutCarRoadInfo inOutCarRoadInfo = (InOutCarRoadInfo)baseDao.getById(InOutCarRoadInfo.class,socketClient.getDeviceInfo().getOwnCarRoad());
        JSONObject roadData = new JSONObject();
        roadData.put("autoPassType",inOutCarRoadInfo.getAutoPassType());
        roadData.put("carRoadId",inOutCarRoadInfo.getCarRoadId());
        roadData.put("carRoadName",Base64.encodeToString(inOutCarRoadInfo.getCarRoadName().getBytes()));
        roadData.put("carRoadType",inOutCarRoadInfo.getCarRoadType());
        json.put("roadData",roadData);
        if (CommonUtils.isEmpty(inOutCarRoadInfo.getManageComputerId())){
            LOGGER.error("手动录入记录失败：车道未绑定岗亭");
            return;
        }
        PostComputerManage postComputerManage = (PostComputerManage)baseDao.getById(PostComputerManage.class,inOutCarRoadInfo.getManageComputerId());

        CarparkInfo carparkInfo = (CarparkInfo)baseDao.getById(CarparkInfo.class,inOutCarRoadInfo.getOwnCarparkNo());
        JSONObject carparkData = new JSONObject();
        carparkData.put("availableCarSpace",carparkInfo.getAvailableCarSpace());
        carparkData.put("carparkId",carparkInfo.getCarparkId());
        carparkData.put("carparkName",Base64.encodeToString(carparkInfo.getCarparkName().getBytes()));
        carparkData.put("closeType",carparkInfo.getCloseType());
        carparkData.put("criticalValue",carparkInfo.getCriticalValue());
        carparkData.put("ownCarparkNo",carparkInfo.getOwnCarparkNo());
        carparkData.put("passTimeWhenBig",carparkInfo.getPassTimeWhenBig());
        carparkData.put("totalCarSpace",carparkInfo.getTotalCarSpace());
        carparkData.put("isClose",carparkInfo.getIsClose());
        carparkData.put("ifIncludeCaculate",carparkInfo.getIfIncludeCaculate());
        carparkData.put("isTestRunning",carparkInfo.getIsTestRunning());
        carparkData.put("isOverdueAutoOpen",carparkInfo.getIsOverdueAutoOpen());
        json.put("carparkData",carparkData);
        String oldCarno = "";
        if ("1".equals(inOutCarRoadInfo.getIsAutoMatchCarNo())){
            if (!IsWhiteListCarno(carPlate,carparkInfo.getCarparkId())){
                String correctionCarno = RedressCarno(carPlate,"0",inOutCarRoadInfo.getIsAutoMatchCarNo(),
                        String.valueOf(inOutCarRoadInfo.getAutoMatchLeastBite()),inOutCarRoadInfo.getAutoMatchCarNoPos(),
                        carparkInfo.getCarparkId(),inOutCarRoadInfo.getWhiteIntelligentCorrection());
                if (!correctionCarno.equals(carPlate)){
                    oldCarno = carPlate;
                    carPlate = correctionCarno;
                }
            }
        }
        if (!CommonUtils.isEmpty(inOutCarRoadInfo.getIntelligentCorrection())){
            String correctionCarno = clientBizService.CarnoIntelligentCorrect(inOutCarRoadInfo.getIntelligentCorrection(),carPlate);
            oldCarno = carPlate;
            carPlate = correctionCarno;
        }
        params.put("CarPlate",Base64.encodeToString(carPlate.getBytes()));
        json.put("deviceData",params);


        String hql = "from DeviceInfo where deviceType = ? and parentDeviceId = ? AND useMark >= 0 ";
        String uartDeviceAddr = "1";            //默认是全部开闸
        DeviceInfo childDevice = (DeviceInfo)baseDao.getUnique(hql,"2",socketClient.getDeviceInfo().getDeviceId());
        if(!CommonUtils.isEmpty(childDevice)){
            JSONObject openGateData = new JSONObject();
            uartDeviceAddr = childDevice.getUartDeviceAddr();
            openGateData.put("uartDeviceAddr",uartDeviceAddr);
            json.put("openGateData",openGateData);
        }
        //根据IPC的ID和车道ID获取LED信息
        String ledId = "";
        String ledType = "";
        hql = "from DeviceInfo where parentDeviceId = ? and deviceType = ? and useMark >= 0";
        DeviceInfo ledDeviceInfo = (DeviceInfo)baseDao.getUnique(hql,socketClient.getDeviceInfo().getDeviceId() ,"3");
        if (CommonUtils.isEmpty(ledDeviceInfo)){
            ledDeviceInfo = (DeviceInfo)baseDao.getUnique(hql,socketClient.getDeviceInfo().getDeviceId() ,"11");
        }
        if (!CommonUtils.isEmpty(ledDeviceInfo)){
            ledId = ledDeviceInfo.getDeviceId();
            ledType = ledDeviceInfo.getDeviceType();
        }

        //获取会员及收费类型信息
        hql = "from MemberWallet where menberNo like ? and carPark = ? AND useMark >= 0";
        MemberWallet memberWallet = (MemberWallet)baseDao.getUnique(hql,"%" + carPlate + "%",carparkInfo.getCarparkId());
        if (!CommonUtils.isEmpty(memberWallet)) {
            Map<String,String> memberWalletSent = BeanUtils.describe(memberWallet);
            memberWalletSent.put("menberNo",Base64.encodeToString(memberWalletSent.get("menberNo").getBytes()));
            memberWalletSent.put("driverName",Base64.encodeToString(memberWalletSent.get("driverName").getBytes()));
            memberWalletSent.put("driverInfo",Base64.encodeToString(memberWalletSent.get("driverInfo").getBytes()));
            if (CommonUtils.isEmpty(memberWalletSent.get("parkingLotId")))
                memberWalletSent.put("parkingLotId","");
            else
                memberWalletSent.put("parkingLotId",Base64.encodeToString(memberWalletSent.get("parkingLotId").getBytes()));
            json.put("memberWallet", memberWalletSent);
        }else
            json.put("memberWallet",Collections.EMPTY_MAP);

        String isUseParkinglot = "0";
        String isMultiCarno = "0";
        if(inOutCarRoadInfo.getCarRoadType().equals("0")){
            Timestamp inTime = new Timestamp(System.currentTimeMillis());
            inTime = GetInOutTime(params.getString("Time"));
            Map<String,String> openGateInfo = clientBizService.IsNeedOpenGate(carPlate,0,inTime,carparkInfo,memberWallet,uartDeviceAddr,0);
            if (CommonUtils.isEmpty(openGateInfo)){
                return;
            }
            MemberKind memberKind = new MemberKind();
            if(!CommonUtils.isEmpty(memberWallet)){
                json.put("isMember","true");
                isUseParkinglot = openGateInfo.get("isUseParkinglot").toString();
                json.put("isUseParkinglot",isUseParkinglot);
                isMultiCarno = openGateInfo.get("isMultiCarno").toString();
                String memberKindId;
                if(StringUtils.isBlank(memberWallet.getMenberTypeId())){
                    memberKindId = "";
                }else{
                    memberKindId = memberWallet.getMenberTypeId();
                }
                memberKind = (MemberKind)baseDao.getById(MemberKind.class,memberKindId);
                if(CommonUtils.isEmpty(memberKind)){
                    //是会员但是没有套餐ID-预约车或是黑名单
                    if(openGateInfo.get("isOverdue").equals("1")){       //预约车和黑名单过期
                        hql = "from MemberKind where carparkInfo = ? AND useType = 1 AND useMark >= 0";
                        memberKind = (MemberKind)baseDao.getUnique(hql,carparkInfo);
                        JSONObject memberKindData = new JSONObject();
                        memberKindData.put("id",memberKind.getId());
                        memberKindData.put("kindName",Base64.encodeToString(memberKind.getKindName().getBytes()));
                        if (!StringUtils.isBlank(memberKind.getMemo()))
                            memberKindData.put("memo",Base64.encodeToString(memberKind.getMemo().getBytes()));
                        else
                            memberKindData.put("memo","");
                        memberKindData.put("packageKind",memberKind.getPackageKind());
                        memberKindData.put("packageChildKind",memberKind.getPackageChildKind());
                        memberKindData.put("isDelete",memberKind.getIsDelete());
                        memberKindData.put("isStatistic",memberKind.getIsStatistic());
                        memberKindData.put("voicePath",memberKind.getVoicePath());
                        memberKindData.put("showColor",memberKind.getShowColor());
                        memberKindData.put("useType",memberKind.getUseType());
                        json.put("memberKind",memberKindData);
                    }else {
                        json.put("memberKind", Collections.EMPTY_MAP);
                    }
                }else{
                    if(openGateInfo.get("isOverdue").equals("1")|| (openGateInfo.get("isUseWallet").equals("0"))){  //过期或是未使用套餐,使用内部车临时收费
                        hql = "from MemberKind where carparkInfo = ? AND useType = 1 AND useMark >= 0";
                        memberKind = (MemberKind)baseDao.getUnique(hql,carparkInfo);
                    }
                    JSONObject memberKindData = new JSONObject();
                    memberKindData.put("id",memberKind.getId());
                    memberKindData.put("kindName",Base64.encodeToString(memberKind.getKindName().getBytes()));
                    if (!StringUtils.isBlank(memberKind.getMemo()))
                        memberKindData.put("memo",Base64.encodeToString(memberKind.getMemo().getBytes()));
                    else
                        memberKindData.put("memo","");
                    memberKindData.put("packageKind",memberKind.getPackageKind());
                    memberKindData.put("packageChildKind",memberKind.getPackageChildKind());
                    memberKindData.put("isDelete",memberKind.getIsDelete());
                    memberKindData.put("isStatistic",memberKind.getIsStatistic());
                    memberKindData.put("voicePath",memberKind.getVoicePath());
                    memberKindData.put("showColor",memberKind.getShowColor());
                    memberKindData.put("useType",memberKind.getUseType());
                    json.put("memberKind",memberKindData);
                }
            }else{
                json.put("memberKind",Collections.EMPTY_MAP);
                json.put("isMember","false");
                json.put("isUseParkinglot","0");
                hql = "from MemberKind where carparkInfo = ? AND useType = 0 AND useMark >= 0";
                memberKind = (MemberKind)baseDao.getUnique(hql,carparkInfo);
                if(CommonUtils.isEmpty(memberKind)){
                    json.put("memberKind", Collections.EMPTY_MAP);
                }else{
                    JSONObject memberKindData = new JSONObject();
                    memberKindData.put("id",memberKind.getId());
                    memberKindData.put("kindName",Base64.encodeToString(memberKind.getKindName().getBytes()));
                    if (!StringUtils.isBlank(memberKind.getMemo()))
                        memberKindData.put("memo",Base64.encodeToString(memberKind.getMemo().getBytes()));
                    else
                        memberKindData.put("memo","");
                    memberKindData.put("packageKind",memberKind.getPackageKind());
                    memberKindData.put("packageChildKind",memberKind.getPackageChildKind());
                    memberKindData.put("isDelete",memberKind.getIsDelete());
                    memberKindData.put("isStatistic",memberKind.getIsStatistic());
                    memberKindData.put("voicePath",memberKind.getVoicePath());
                    memberKindData.put("showColor",memberKind.getShowColor());
                    memberKindData.put("useType",memberKind.getUseType());
                    json.put("memberKind",memberKindData);
                }
            }
            json.put("isMultiCarno",isMultiCarno);
            json.put("openGateMode",Integer.valueOf(openGateInfo.get("isNeedOpenGate")));

            if (openGateInfo.get("isNeedOpenGate").equals("1")) {
                if (!CommonUtils.isEmpty(memberWallet)) {
                    if (uartDeviceAddr.equals("1") && (!StringUtils.isBlank(memberWallet.getSynchronizeIpcList()))){
                        //不用开闸
                    }else{
                        if (carparkInfo.getCarparkName().equals("海西1号地库") || carparkInfo.getCarparkName().equals("海西2号地库")){
                            LOGGER.error(carPlate + "在" +  inOutCarRoadInfo.getCarRoadName() + "开闸入场");
                        }
                        deviceManageUtils.openRoadGate(socketClient.getDeviceInfo().getDeviceIp(), Integer.parseInt(socketClient.getDeviceInfo().getDevicePort()),
                                socketClient.getDeviceInfo().getDeviceUsername(), socketClient.getDeviceInfo().getDevicePwd());
                    }
                }else {
                    if (carparkInfo.getCarparkName().equals("海西1号地库") || carparkInfo.getCarparkName().equals("海西2号地库")){
                        LOGGER.error(carPlate + "在" +  inOutCarRoadInfo.getCarRoadName() + "开闸入场");
                    }
                    deviceManageUtils.openRoadGate(socketClient.getDeviceInfo().getDeviceIp(), Integer.parseInt(socketClient.getDeviceInfo().getDevicePort()),
                            socketClient.getDeviceInfo().getDeviceUsername(), socketClient.getDeviceInfo().getDevicePwd());
                }
                JSONObject autoReleaseParams = new JSONObject();
                autoReleaseParams.put("inOutCarno", carPlate);
                autoReleaseParams.put("inOuCarnoOld", oldCarno);
                autoReleaseParams.put("inOutTime", inTime.toString());
                autoReleaseParams.put("carplateColor", CarPlateColor);
                autoReleaseParams.put("inOutCameraId", socketClient.getDeviceInfo().getDeviceId());
                autoReleaseParams.put("inOutPicName",fileUri);
                autoReleaseParams.put("releaseMode", "0");
                autoReleaseParams.put("releaseType", "");
                autoReleaseParams.put("releaseReason", openGateInfo.get("limiReason"));
                autoReleaseParams.put("isUseParkinglot",isUseParkinglot);
                autoReleaseParams.put("ledId",ledId);
                clientBizService.handleAutoRelease(autoReleaseParams, carparkInfo, inOutCarRoadInfo, memberKind, memberWallet);
            }else {
                if ("0".equals(postComputerManage.getIsAutoDeal())){
                    JSONObject autoReleaseParams = new JSONObject();
                    autoReleaseParams.put("inOutCarno", carPlate);
                    autoReleaseParams.put("inOuCarnoOld", oldCarno);
                    autoReleaseParams.put("inOutTime", inTime.toString());
                    autoReleaseParams.put("carplateColor", CarPlateColor);
                    autoReleaseParams.put("inOutCameraId", socketClient.getDeviceInfo().getDeviceId());
                    autoReleaseParams.put("inOutPicName",fileUri);
                    autoReleaseParams.put("releaseMode", "0");
                    autoReleaseParams.put("releaseType", "");
                    autoReleaseParams.put("releaseReason", openGateInfo.get("limiReason"));
                    autoReleaseParams.put("isUseParkinglot",isUseParkinglot);
                    autoReleaseParams.put("ledId",ledId);
                    clientBizService.handleAutoRelease(autoReleaseParams, carparkInfo, inOutCarRoadInfo, memberKind, memberWallet);
                    json.put("openGateMode",1);
                }
            }

            //LED显示播报
            if (!StringUtils.isBlank(ledId)) {
                JSONObject playParams = new JSONObject();
                Map<String,Object> ledMap = GetLedPlaySence(ledId, 0, carparkInfo.getLedMemberCriticalValue(),
                        Integer.valueOf(openGateInfo.get("isNeedOpenGate")), inTime, memberWallet, "");
                LedDisplayConfig ledDisplayConfig = (LedDisplayConfig) ledMap.get("ledDisplayConfig");
                String memberLeft = (String) ledMap.get("memberLeft");

                playParams.put("carPlate", carPlate);
                playParams.put("carparkName", carparkInfo.getCarparkName());
                playParams.put("availableCarSpace", carparkInfo.getAvailableCarSpace());
                playParams.put("inOutTime", inTime.toString());
                playParams.put("parkingLength", "0");
                playParams.put("kindName", CommonUtils.isEmpty(memberKind) ? "" : memberKind.getKindName());
                playParams.put("chargeAmount", "0");
                playParams.put("limitReason", openGateInfo.get("limiReason"));
                playParams.put("memberLeft", memberLeft);
                playParams.put("isMultiCarno",isMultiCarno);
                //playParams.put("driverName",driverName);
                PlayLedInfo(socketClient.getDeviceInfo().getDeviceId(), 0, playParams, ledDisplayConfig,ledType,ledId);
            }

        }else{
            //组织出场数据
            Double chargeAmount = 0D;  //应收金额
            Double chargeAmountForMember = 0D;  //会员收费类型为临时车的时候
            Timestamp outTime = GetInOutTime(params.getString("Time"));

            //获取userType
            String useType = "0";//默认为外部临时收费
            MemberKind memberKind;
            Map<String,String> memberUseInfo = clientBizService.IsNeedOpenGate(carPlate,1,outTime,carparkInfo,memberWallet,uartDeviceAddr,chargeAmount);
            if(!CommonUtils.isEmpty(memberWallet)){
                json.put("isMember","true");
                isUseParkinglot = memberUseInfo.get("isUseParkinglot").toString();
                json.put("isUseParkinglot",isUseParkinglot);
                isMultiCarno = memberUseInfo.get("isMultiCarno").toString();
                String memberKindId;
                if(StringUtils.isBlank(memberWallet.getMenberTypeId())){
                    memberKindId = "";
                }else{
                    memberKindId = memberWallet.getMenberTypeId();
                }
                memberKind = (MemberKind)baseDao.getById(MemberKind.class,memberKindId);
                if(CommonUtils.isEmpty(memberKind)){
                    //是会员但是没有套餐ID-预约车或是黑名单
                    useType = "1";
                    if(memberUseInfo.get("isOverdue").equals("1")){       //预约车和黑名单过期
                        hql = "from MemberKind where carparkInfo = ? AND useType = 1 AND useMark >= 0";
                        memberKind = (MemberKind)baseDao.getUnique(hql,carparkInfo);
                        JSONObject memberKindData = new JSONObject();
                        memberKindData.put("id",memberKind.getId());
                        memberKindData.put("kindName",Base64.encodeToString(memberKind.getKindName().getBytes()));
                        if (!StringUtils.isBlank(memberKind.getMemo()))
                            memberKindData.put("memo",Base64.encodeToString(memberKind.getMemo().getBytes()));
                        else
                            memberKindData.put("memo","");
                        memberKindData.put("packageKind",memberKind.getPackageKind());
                        memberKindData.put("packageChildKind",memberKind.getPackageChildKind());
                        memberKindData.put("isDelete",memberKind.getIsDelete());
                        memberKindData.put("isStatistic",memberKind.getIsStatistic());
                        memberKindData.put("voicePath",memberKind.getVoicePath());
                        memberKindData.put("showColor",memberKind.getShowColor());
                        memberKindData.put("useType",memberKind.getUseType());
                        json.put("memberKind",memberKindData);
                    }else {
                        json.put("memberKind", Collections.EMPTY_MAP);
                    }
                }else{
                    if(memberUseInfo.get("isOverdue").equals("1") || (memberUseInfo.get("isUseWallet").equals("0"))){ //过期或是未使用套餐,使用内部车临时收费
                        useType = "1";
                        hql = "from MemberKind where carparkInfo = ? AND useType = 1 AND useMark >= 0";
                        memberKind = (MemberKind)baseDao.getUnique(hql,carparkInfo);
                    }
                    JSONObject memberKindData = new JSONObject();
                    memberKindData.put("id",memberKind.getId());
                    memberKindData.put("kindName",Base64.encodeToString(memberKind.getKindName().getBytes()));
                    if (!StringUtils.isBlank(memberKind.getMemo()))
                        memberKindData.put("memo",Base64.encodeToString(memberKind.getMemo().getBytes()));
                    else
                        memberKindData.put("memo","");
                    memberKindData.put("packageKind",memberKind.getPackageKind());
                    memberKindData.put("packageChildKind",memberKind.getPackageChildKind());
                    memberKindData.put("isDelete",memberKind.getIsDelete());
                    memberKindData.put("isStatistic",memberKind.getIsStatistic());
                    memberKindData.put("voicePath",memberKind.getVoicePath());
                    memberKindData.put("showColor",memberKind.getShowColor());
                    memberKindData.put("useType",memberKind.getUseType());
                    json.put("memberKind",memberKindData);
                }
            }else{
                json.put("memberKind",Collections.EMPTY_MAP);
                json.put("isMember","false");
                json.put("isUseParkinglot","0");
                hql = "from MemberKind where carparkInfo = ? AND useType = 0 AND useMark >= 0";
                useType = "0";
                memberKind = (MemberKind)baseDao.getUnique(hql,carparkInfo);
                if(CommonUtils.isEmpty(memberKind)){
                    json.put("memberKind", Collections.EMPTY_MAP);
                }else{
                    JSONObject memberKindData = new JSONObject();
                    memberKindData.put("id",memberKind.getId());
                    memberKindData.put("kindName",Base64.encodeToString(memberKind.getKindName().getBytes()));
                    if (!StringUtils.isBlank(memberKind.getMemo()))
                        memberKindData.put("memo",Base64.encodeToString(memberKind.getMemo().getBytes()));
                    else
                        memberKindData.put("memo","");
                    memberKindData.put("packageKind",memberKind.getPackageKind());
                    memberKindData.put("packageChildKind",memberKind.getPackageChildKind());
                    memberKindData.put("isDelete",memberKind.getIsDelete());
                    memberKindData.put("isStatistic",memberKind.getIsStatistic());
                    memberKindData.put("voicePath",memberKind.getVoicePath());
                    memberKindData.put("showColor",memberKind.getShowColor());
                    memberKindData.put("useType",memberKind.getUseType());
                    json.put("memberKind",memberKindData);
                }
            }
            json.put("isMultiCarno",isMultiCarno);

            //匹配的入场信息
            OrderInoutRecord matchInInfo = clientBizService.GetMatchInfo(carPlate,carparkInfo.getCarparkId(),outTime);
            Timestamp inTime = new Timestamp(System.currentTimeMillis());
            Timestamp chargeBeginTime = new Timestamp(System.currentTimeMillis()); //计费时间
            String parkingLen = "";
            Long stayTime = 0L;
            if(!CommonUtils.isEmpty(matchInInfo)){
                json.put("isMatch","true");
                JSONObject matchData = new JSONObject();
                matchData.put("matchCarno",Base64.encodeToString(matchInInfo.getCarNo().getBytes()));
                matchData.put("matchRoadId",matchInInfo.getInCarRoadId());
                matchData.put("matchRoadName",Base64.encodeToString(matchInInfo.getInCarRoadName().getBytes()));
                matchData.put("matchInTime",CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",matchInInfo.getInTime()));

                if (!CommonUtils.isEmpty(memberWallet)) {
                    if (memberUseInfo.get("isOverdue").equals("1")) {//出场时白名单过期
                        if (memberWallet.getPackKindId().equals(0) || memberWallet.getPackKindId().equals(-3)) {
                            chargeBeginTime = memberWallet.getEffectiveEndTime().getTime() >= matchInInfo.getInTime().getTime() ? memberWallet.getEffectiveEndTime() : matchInInfo.getInTime();
                        } else
                            chargeBeginTime = matchInInfo.getInTime();
                    } else
                        chargeBeginTime = matchInInfo.getInTime();
                }else
                    chargeBeginTime = matchInInfo.getInTime();

                if (!CommonUtils.isEmpty(matchInInfo.getInChargeTime())){
                    chargeBeginTime = matchInInfo.getInChargeTime().getTime() >= chargeBeginTime.getTime() ? matchInInfo.getInChargeTime() : chargeBeginTime;
                }

                inTime = matchInInfo.getInTime();
                stayTime = (outTime.getTime() - inTime.getTime()) / 1000;
                int parkingHour = (int)(stayTime/3600);
                int parkingMinute = (int)((stayTime-parkingHour*3600)/60);
                parkingLen = "停车" + String.valueOf(parkingHour) + "时" + String.valueOf(parkingMinute) + "分";
                matchData.put("stayTime",stayTime);
                matchData.put("matchInPic",matchInInfo.getInPictureName());
                json.put("matchData",matchData);
            }else{
                parkingLen = "停车时长未知";
                json.put("isMatch", "false");
                json.put("matchData", Collections.EMPTY_MAP);
                chargeBeginTime = new Timestamp(outTime.getTime() - 60 * 60 * 1000);
            }

            //提前缴费金额
            Double chargePreAmount = 0D;
            BigDecimal accountSum = new BigDecimal("0.00");
            Timestamp prechargeTime = outTime;
            //3、核算流水订单缴纳费用
            if (!CommonUtils.isEmpty(matchInInfo)) {
                String accountHql = "from OrderTransaction where orderId = ? and payStatus = ? order by addTime desc";
                List<OrderTransaction> orderTransactionList = (List<OrderTransaction>)baseDao.queryForList(accountHql,matchInInfo.getChargeInfoId(), payStatusEnum.HAS_PAID);
                if (orderTransactionList.size() > 0) {
                    prechargeTime = orderTransactionList.get(0).getAddTime();
                    //流水表收费总金额
                    for (OrderTransaction orderTransaction : orderTransactionList){
                        accountSum = accountSum.add(orderTransaction.getTotalFee());
                    }
                }
                //写入提前缴费金额
                if(CommonUtils.isEmpty(accountSum)){
                    accountSum = new BigDecimal("0.00");
                }
                matchInInfo.setChargePreAmount(accountSum.doubleValue());
                chargePreAmount = matchInInfo.getChargePreAmount();
            }

            boolean isNeedCalculateCharge = false;      //是否结算金额
            if (chargePreAmount > 0){
                isNeedCalculateCharge = outTime.getTime() - prechargeTime.getTime() > Integer.valueOf(carparkInfo.getPassTimeWhenBig()) * 60 * 1000 ? true : false;

            }else {
                isNeedCalculateCharge = true;
            }

            try {
                //使用未过期的白名单或是未匹配出场且车场试运行的金额为0
                boolean isValid = false;
                boolean isUseWallet = false;
                boolean isOverdue = false;
                if (!CommonUtils.isEmpty(memberWallet)){
                    isValid = memberUseInfo.get("isValid").equals("1") ? true : false;
                    isUseWallet = memberUseInfo.get("isUseWallet").equals("1") ? true : false;
                    isOverdue = memberUseInfo.get("isOverdue").equals("1") ? true : false;
                }
                if((isValid && isUseWallet && (!isOverdue)) || (CommonUtils.isEmpty(matchInInfo) && carparkInfo.getIsTestRunning().equals("1"))) {
                    if (memberWallet.getPackKindId().equals(2))
                        chargeAmountForMember = calculateFeeNewService.calculateParkingFeeNew(carPlate, useType, carType,carparkInfo.getCarparkId(), chargeBeginTime, outTime);
                    chargeAmount = 0D;
                }else
                    chargeAmount = calculateFeeNewService.calculateParkingFeeNew(carPlate, useType, carType,carparkInfo.getCarparkId(), chargeBeginTime, outTime);
            }catch (Exception e){
                chargeAmount = 0D;
            }

            //2、获取入场订单信息
            if (!CommonUtils.isEmpty(matchInInfo)) {
                matchInInfo.setChargeReceivableAmount(chargeAmount);
                matchInInfo.setUpdateTime(CommonUtils.getTimestamp());
                baseDao.update(matchInInfo);
            }

            //获取应缴费金额
            Double chargeActualAmount = 0D;
            if (isNeedCalculateCharge){
                chargeActualAmount = chargeAmount - chargePreAmount;
            }else {
                chargeAmount = chargePreAmount;
            }

            chargeActualAmount = chargeActualAmount < 0 ? 0D : chargeActualAmount;
            Map<String,String> openGateModeInfo = clientBizService.IsNeedOpenGate(carPlate,1,outTime,carparkInfo,memberWallet,
                    uartDeviceAddr,chargeActualAmount);

            boolean isNoSensePay = false;
            if (AppInfo.isUseUparkPay.equals("1") && (!CommonUtils.isEmpty(matchInInfo)) && (matchInInfo.getReleaseType().equals(4))){
                //符合无感支付的车辆，如果金额大于300
                if (chargeAmount > 300){
                    //不抬杆，推送账单至智慧停车云平台
                    String payAmt = (new BigDecimal(chargeAmount).multiply(new BigDecimal("100"))).setScale(0,BigDecimal.ROUND_UP) + "";
                    newOrderParkService.pushBillToUparkCloud(matchInInfo.getChargeInfoId(),carPlate,carparkInfo.getCarparkId(),carparkInfo.getCarparkName(),payAmt,payAmt
                            ,inTime.toString(),outTime.toString(),"");
                }else {
                    //抬杆，调用离场通知接口推送消息至智慧停车平台
                    openGateModeInfo.put("isNeedOpenGate","1");
                    openGateModeInfo.put("limiReason","");
                    isNoSensePay = true;
                }
            }

            json.put("openGateMode",Integer.valueOf(openGateModeInfo.get("isNeedOpenGate")));
            if((accountSum.compareTo(new BigDecimal(chargeAmount.toString())) >= 0) || isNoSensePay){
                //场内缴费金额大于或等于应缴费金额，或是使用了无感支付，直接自动放行
                if (openGateModeInfo.get("isNeedOpenGate").equals("1")){
                    //开闸
                    if (!CommonUtils.isEmpty(memberWallet)) {
                        if (uartDeviceAddr.equals("1") && (!StringUtils.isBlank(memberWallet.getSynchronizeIpcList()))){
                            //不用开闸
                        }else{
                            if (carparkInfo.getCarparkName().equals("海西1号地库") || carparkInfo.getCarparkName().equals("海西2号地库")){
                                LOGGER.error(carPlate + "在" +  inOutCarRoadInfo.getCarRoadName() + "开闸出场");
                            }
                            deviceManageUtils.openRoadGate(socketClient.getDeviceInfo().getDeviceIp(),Integer.parseInt(socketClient.getDeviceInfo().getDevicePort()),
                                    socketClient.getDeviceInfo().getDeviceUsername(),socketClient.getDeviceInfo().getDevicePwd());
                        }
                    }else {
                        if (carparkInfo.getCarparkName().equals("海西1号地库") || carparkInfo.getCarparkName().equals("海西2号地库")){
                            LOGGER.error(carPlate + "在" +  inOutCarRoadInfo.getCarRoadName() + "开闸出场");
                        }
                        deviceManageUtils.openRoadGate(socketClient.getDeviceInfo().getDeviceIp(),Integer.parseInt(socketClient.getDeviceInfo().getDevicePort()),
                                socketClient.getDeviceInfo().getDeviceUsername(),socketClient.getDeviceInfo().getDevicePwd());
                    }

                    JSONObject autoReleaseParams = new JSONObject();
                    autoReleaseParams.put("inOutCarno", carPlate);
                    autoReleaseParams.put("inOuCarnoOld", oldCarno);
                    autoReleaseParams.put("inOutTime", outTime.toString());
                    autoReleaseParams.put("carplateColor", CarPlateColor);
                    autoReleaseParams.put("inOutCameraId", socketClient.getDeviceInfo().getDeviceId());
                    autoReleaseParams.put("inOutPicName",fileUri);
                    autoReleaseParams.put("releaseMode", "0");
                    autoReleaseParams.put("chargeAmount", chargeAmount.toString());
                    autoReleaseParams.put("chargeAmountForMember", chargeAmountForMember.toString());
                    autoReleaseParams.put("releaseType", "3");
                    autoReleaseParams.put("releaseReason", openGateModeInfo.get("limiReason"));
                    autoReleaseParams.put("chargeActualAmount", chargeActualAmount.toString());
                    autoReleaseParams.put("stayTime",stayTime);
                    autoReleaseParams.put("isUseParkinglot",isUseParkinglot);
                    autoReleaseParams.put("ledId",ledId);
                    clientBizService.handleAutoRelease(autoReleaseParams, carparkInfo, inOutCarRoadInfo, memberKind, memberWallet);
                }else {
                    if ("0".equals(postComputerManage.getIsAutoDeal())){
                        JSONObject autoReleaseParams = new JSONObject();
                        autoReleaseParams.put("inOutCarno", carPlate);
                        autoReleaseParams.put("inOuCarnoOld", oldCarno);
                        autoReleaseParams.put("inOutTime", outTime.toString());
                        autoReleaseParams.put("carplateColor", CarPlateColor);
                        autoReleaseParams.put("inOutCameraId", socketClient.getDeviceInfo().getDeviceId());
                        autoReleaseParams.put("inOutPicName",fileUri);
                        autoReleaseParams.put("releaseMode", "0");
                        autoReleaseParams.put("chargeAmount", chargeAmount.toString());
                        autoReleaseParams.put("chargeAmountForMember", chargeAmountForMember.toString());
                        autoReleaseParams.put("releaseType", "3");
                        autoReleaseParams.put("releaseReason", openGateModeInfo.get("limiReason"));
                        autoReleaseParams.put("chargeActualAmount", chargeActualAmount.toString());
                        autoReleaseParams.put("stayTime",stayTime);
                        autoReleaseParams.put("isUseParkinglot",isUseParkinglot);
                        autoReleaseParams.put("ledId",ledId);
                        clientBizService.handleAutoRelease(autoReleaseParams, carparkInfo, inOutCarRoadInfo, memberKind, memberWallet);
                        json.put("openGateMode",1);
                    }
                }
            }
            json.put("chargeAmount",chargeAmount);
            json.put("chargePreAmount",chargePreAmount);

            //如果存在LED显示屏
            if (!StringUtils.isBlank(ledId)){
                JSONObject playParams = new JSONObject();
                Map<String,Object> ledMap = GetLedPlaySence(ledId,1,carparkInfo.getLedMemberCriticalValue(),
                        Integer.valueOf(openGateModeInfo.get("isNeedOpenGate")),outTime,memberWallet,"");
                LedDisplayConfig ledDisplayConfig = (LedDisplayConfig) ledMap.get("ledDisplayConfig");
                String memberLeft = (String) ledMap.get("memberLeft");

                playParams.put("carPlate",carPlate);
                playParams.put("carparkName",carparkInfo.getCarparkName());
                playParams.put("availableCarSpace",carparkInfo.getAvailableCarSpace());
                playParams.put("inOutTime",outTime.toString());
                playParams.put("parkingLength",parkingLen);
                playParams.put("kindName",CommonUtils.isEmpty(memberKind) ? "" : memberKind.getKindName());
                playParams.put("chargeAmount",String.valueOf(chargeAmount));
                playParams.put("limitReason",openGateModeInfo.get("limiReason"));
                playParams.put("memberLeft",memberLeft);
                playParams.put("isMultiCarno",isMultiCarno);
                //playParams.put("driverName",driverName);
                PlayLedInfo(socketClient.getDeviceInfo().getDeviceId(),1,playParams,ledDisplayConfig,ledType,ledId);
            }

        }

        try {
            for (int i = 0; i < ParkSocketServer.getClientsCount(); i++) {
                SocketClient client = ParkSocketServer.getClient(i);
                if (inOutCarRoadInfo.getManageComputerId().equals(client.getChannelId())) {
                    json.put("result", "0");
                    SocketUtils.sendBizPackage(client, json, DeviceSIPEnum.CarPlateIndPrev.name());
                }
            }
        } catch (BizException ex) {
            LOGGER.error("车辆扫牌信息推送失败:{},车牌:{},车道:{}", ex.getMessage(), carPlate, inOutCarRoadInfo.getCarRoadId());
        }

    }

    @Override
    public void updateDeviceStatus(String deviceId, String status) {
        if(!CommonUtils.isEmpty(deviceId)){
            DeviceInfo deviceInfo = (DeviceInfo)baseDao.getById(DeviceInfo.class,deviceId);
            if(deviceInfo == null){
                return;
            }
            deviceInfo.setDevStatus(status);
            baseDao.update(deviceInfo);
            //更新附属IPC的状态信息
            if (!CommonUtils.isEmpty(deviceInfo.getSubIpcId())) {
                DeviceInfo deviceInfoSub = (DeviceInfo) baseDao.getById(DeviceInfo.class, deviceInfo.getSubIpcId());
                deviceInfoSub.setDevStatus(status);
                baseDao.update(deviceInfo);
            }
        }


    }

    @Override
    public void PlayLedInfo(String ipcDevId, Integer inOutType, JSONObject ledPlayInfo, LedDisplayConfig ledDisplayConfig,String ledType,String ledId) {
        if(ledPlayInfo.containsKey("isForbidLed")){
            String isForbidLed = ledPlayInfo.getString("isForbidLed");
            if("1".equals(isForbidLed)){
                return;
            }
        }

        //调用播放接口
        StarnetDeviceUtils starnetDeviceUtils = new StarnetDeviceUtils();
        DeviceInfo deviceInfoMain = (DeviceInfo)baseDao.getById(DeviceInfo.class,ipcDevId);

        List<DeviceInfo> deviceInfoList = new ArrayList<>();
        deviceInfoList.add(deviceInfoMain);
        //注释以下代码则代表只是主IPC播报，反之则代表全部播报
        if (!CommonUtils.isEmpty(deviceInfoMain.getSubIpcId())){
            DeviceInfo deviceInfoSub = (DeviceInfo)baseDao.getById(DeviceInfo.class,deviceInfoMain.getSubIpcId());
            deviceInfoList.add(deviceInfoSub);
        }

        for (DeviceInfo deviceInfo : deviceInfoList) {
            if ("3".equals(ledType)) {
                JSONObject voiceParams = new JSONObject();
                voiceParams.put("enabled", 1);
                voiceParams.put("parkName", ledPlayInfo.getString("carparkName"));
                voiceParams.put("LEDSound", 1);
                voiceParams.put("TTSSound", 0);
                Integer voice = ledPlayInfo.getInteger("voiceBroadcastVolume");
                if (CommonUtils.isEmpty(voice)) {
                    voiceParams.put("LEDVolume", ledDisplayConfig.getVoiceBroadcastVolume());
                    voiceParams.put("TTSVolume", ledDisplayConfig.getVoiceBroadcastVolume());
                } else {
                    voiceParams.put("LEDVolume", voice);
                    voiceParams.put("TTSVolume", voice);
                }
                starnetDeviceUtils.updateBroadcastDisplayInfo(deviceInfo.getDeviceIp(), Integer.valueOf(deviceInfo.getDevicePort()),
                        deviceInfo.getDeviceUsername(), deviceInfo.getDevicePwd(), voiceParams);

                List<JSONObject> ledParamsList = new ArrayList<>();
                String playContent;
                JSONObject params;

                //除了空闲状态和综合空位提示不播语音
                if (ledDisplayConfig.getSceneNo().equals(1) || ledDisplayConfig.getSceneNo().equals(2) || ledDisplayConfig.getSceneNo().equals(3)
                        || ledDisplayConfig.getSceneNo().equals(4) || ledDisplayConfig.getSceneNo().equals(6)) {
                    playContent = GetPlayStr(ledPlayInfo, ledDisplayConfig, 4);
                    if ("剩余xxx".equals(playContent)) {
                        playContent = ledPlayInfo.getString("memberLeft");
                    }
                    DeviceInfo ledDeviceInfo = (DeviceInfo) baseDao.getById(DeviceInfo.class, ledId);
                    Integer playPos = CommonUtils.isEmpty(ledDeviceInfo.getVoiceChannel()) ? 1 : (ledDeviceInfo.getVoiceChannel() + 1);
                    params = new JSONObject();
                    params.put("type", 1);
                    params.put("pos", playPos);
                    params.put("str", playContent);
                    params.put("bscoll", 0);
                    params.put("action","set");
                    ledParamsList.add(params);
                }

                playContent = GetPlayStr(ledPlayInfo, ledDisplayConfig, 1);
                params = new JSONObject();
                params.put("type", 0);
                params.put("pos", 1);
                params.put("str", playContent);
                params.put("bscoll", 1);
                params.put("action","set");
                ledParamsList.add(params);

                playContent = GetPlayStr(ledPlayInfo, ledDisplayConfig, 2);
                params = new JSONObject();
                params.put("type", 0);
                params.put("pos", 2);
                params.put("str", playContent);
                params.put("bscoll", 1);
                params.put("action","set");
                ledParamsList.add(params);

                playContent = GetPlayStr(ledPlayInfo, ledDisplayConfig, 3);
                params = new JSONObject();
                params.put("type", 0);
                params.put("pos", 3);
                params.put("str", playContent);
                params.put("bscoll", 1);
                params.put("action","set");
                ledParamsList.add(params);

                DeviceLedCgiThread deviceLedCgiThread = new DeviceLedCgiThread();
                deviceLedCgiThread.setParamsList(ledParamsList);
                deviceLedCgiThread.setUsername(deviceInfo.getDeviceUsername());
                deviceLedCgiThread.setPassword(deviceInfo.getDevicePwd());
                deviceLedCgiThread.setIp(deviceInfo.getDeviceIp());
                deviceLedCgiThread.setPort(Integer.parseInt(deviceInfo.getDevicePort()));
                deviceLedCgiThread.start();

                JSONObject freeParams = new JSONObject();
                if (ledDisplayConfig.getSceneNo().equals(4) || ledDisplayConfig.getSceneNo().equals(6)) {
                    freeParams.put("isNeedPlay", "false");
                } else {
                    freeParams.put("isNeedPlay", "true");
                }
                String hql = "FROM LedDisplayConfig WHERE devId = ? AND sceneNo = ?";
                LedDisplayConfig ledDisplayConfigForFree = (LedDisplayConfig) baseDao.getUnique(hql, ledId, 5);
                String playContentFirst = GetPlayStr(ledPlayInfo, ledDisplayConfigForFree, 1);
                String playContentSecond = GetPlayStr(ledPlayInfo, ledDisplayConfigForFree, 2);
                String playContentThird = GetPlayStr(ledPlayInfo, ledDisplayConfigForFree, 3);
                freeParams.put("playType", "three");
                freeParams.put("deviceInfo", deviceInfo);
                freeParams.put("playTime", CommonUtils.getTimestamp().toString());
                freeParams.put("playContentFirst", playContentFirst);
                freeParams.put("playContentSecond", playContentSecond);
                freeParams.put("playContentThird", playContentThird);
                LedPlayFreeInfoServer.setSuspend(true);
                LedPlayFreeInfoServer.addPlayListInfo(freeParams);
                LedPlayFreeInfoServer.setSuspend(false);
            } else {
                List<JSONObject> ledParamsList = new ArrayList<>();
                String playContent, needCheckContent, crcContent, finalContent;
                String playStr, dlength, textLen;
                String playInfo = "";
                JSONObject params;
                DeviceInfo ledDeviceInfo = (DeviceInfo) baseDao.getById(DeviceInfo.class, ledId);
                Integer playPos = CommonUtils.isEmpty(ledDeviceInfo.getDeviceChannelNo()) ? 3 : Integer.valueOf(ledDeviceInfo.getDeviceChannelNo());
                //分四行播放
                if (ledDisplayConfig.getSceneNo().equals(1) || ledDisplayConfig.getSceneNo().equals(2) || ledDisplayConfig.getSceneNo().equals(3)
                        || ledDisplayConfig.getSceneNo().equals(4) || ledDisplayConfig.getSceneNo().equals(6)) {
                    playContent = GetPlayStrFour(ledPlayInfo, ledDisplayConfig, 4);         //已获取要播报的内容了
                    try {
                        playInfo = ChangePlayInfoToOx(playContent);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Integer strLen = playInfo.length() / 2 + 1;
                    dlength = (strLen.toHexString(strLen)).length() < 2 ? "0" + strLen.toHexString(strLen) : strLen.toHexString(strLen);
                    needCheckContent = "0064FFFF30" + dlength + "01" + playInfo;
                    crcContent = GetCrcCheckInfo(needCheckContent);

                    finalContent = "0064FFFF30" + dlength + "01" + playInfo + crcContent;
                    playStr = GetLedPlayStrToBase(finalContent);
                    params = new JSONObject();
                    params.put("uartID", playPos);
                    params.put("write_data", playStr);
                    params.put("need_rspdata", 0);
                    params.put("action","set");
                    ledParamsList.add(params);
                }

                //显示第一行
                playContent = GetPlayStrFour(ledPlayInfo, ledDisplayConfig, 0);
                playStr = playStrToFourLed(playContent,"0");
                params = new JSONObject();
                params.put("uartID", playPos);
                params.put("write_data", playStr);
                params.put("need_rspdata", 0);
                params.put("action","set");
                ledParamsList.add(params);
                //显示第二行
                playContent = GetPlayStrFour(ledPlayInfo, ledDisplayConfig, 1);
                playStr = playStrToFourLed(playContent,"1");
                params = new JSONObject();
                params.put("uartID", playPos);
                params.put("write_data", playStr);
                params.put("need_rspdata", 0);
                params.put("action","set");
                ledParamsList.add(params);
                //显示第三行
                playContent = GetPlayStrFour(ledPlayInfo, ledDisplayConfig, 2);
                playStr = playStrToFourLed(playContent,"2");
                params = new JSONObject();
                params.put("uartID", playPos);
                params.put("write_data", playStr);
                params.put("need_rspdata", 0);
                params.put("action","set");
                ledParamsList.add(params);

                //显示第四行
                playContent = GetPlayStrFour(ledPlayInfo, ledDisplayConfig, 3);
                playStr = playStrToFourLed(playContent,"3");
                params = new JSONObject();
                params.put("uartID", playPos);
                params.put("write_data", playStr);
                params.put("need_rspdata", 0);
                params.put("action","set");
                ledParamsList.add(params);

                DeviceLedFourThread deviceLedFourThread = new DeviceLedFourThread();
                deviceLedFourThread.setParamsList(ledParamsList);
                deviceLedFourThread.setUsername(deviceInfo.getDeviceUsername());
                deviceLedFourThread.setPassword(deviceInfo.getDevicePwd());
                deviceLedFourThread.setIp(deviceInfo.getDeviceIp());
                deviceLedFourThread.setPort(Integer.parseInt(deviceInfo.getDevicePort()));
                deviceLedFourThread.start();

                //设置空闲状态参数
                JSONObject freeParams = new JSONObject();
                if (ledDisplayConfig.getSceneNo().equals(4) || ledDisplayConfig.getSceneNo().equals(6)) {
                    freeParams.put("isNeedPlay", "false");
                } else {
                    freeParams.put("isNeedPlay", "true");
                }
                String hql = "FROM LedDisplayConfig WHERE devId = ? AND sceneNo = ?";
                LedDisplayConfig ledDisplayConfigForFree = (LedDisplayConfig) baseDao.getUnique(hql, ledId, 5);

                playContent = GetPlayStrFour(ledPlayInfo, ledDisplayConfigForFree, 0);
                if (playContent.indexOf("年")>0 && playContent.indexOf("月")>0 && playContent.indexOf("日")>0)
                    freeParams.put("isFlashTimeOne", "1");
                else
                    freeParams.put("isFlashTimeOne", "0");
                String playContentFirst = playStrToFourLed(playContent,"0");

                playContent = GetPlayStrFour(ledPlayInfo, ledDisplayConfigForFree, 1);
                if (playContent.indexOf("年")>0 && playContent.indexOf("月")>0 && playContent.indexOf("日")>0)
                    freeParams.put("isFlashTimeTwo", "1");
                else
                    freeParams.put("isFlashTimeTwo", "0");
                String playContentSecond = playStrToFourLed(playContent,"1");

                playContent = GetPlayStrFour(ledPlayInfo, ledDisplayConfigForFree, 2);
                if (playContent.indexOf("年")>0 && playContent.indexOf("月")>0 && playContent.indexOf("日")>0)
                    freeParams.put("isFlashTimeThree", "1");
                else
                    freeParams.put("isFlashTimeThree", "0");
                String playContentThird = playStrToFourLed(playContent,"2");;

                playContent = GetPlayStrFour(ledPlayInfo, ledDisplayConfigForFree, 3);
                if (playContent.indexOf("年")>0 && playContent.indexOf("月")>0 && playContent.indexOf("日")>0)
                    freeParams.put("isFlashTimeFour", "1");
                else
                    freeParams.put("isFlashTimeFour", "0");
                String playContentFour = playStrToFourLed(playContent,"3");
                freeParams.put("playType", "four");
                freeParams.put("playPos", playPos);
                freeParams.put("deviceInfo", deviceInfo);
                freeParams.put("playTime", CommonUtils.getTimestamp().toString());
                freeParams.put("playContentFirst", playContentFirst);
                freeParams.put("playContentSecond", playContentSecond);
                freeParams.put("playContentThird", playContentThird);
                freeParams.put("playContentFour", playContentFour);
                LedPlayFreeInfoServer.setSuspend(true);
                LedPlayFreeInfoServer.addPlayListInfo(freeParams);
                LedPlayFreeInfoServer.setSuspend(false);
            }
        }
    }

    @Override
    public Map<String,Object> GetLedPlaySence(String ledId,Integer inOutType, Integer criticalDay, Integer isNeedOpenGate, Timestamp inOutTime,
                                            MemberWallet memberWallet,String memberLeftInfo) {
        Map<String,Object> resMap = new HashedMap();
        String memberLeft = "";
        String hql = "FROM LedDisplayConfig WHERE devId = ? AND sceneNo = ?";
        Integer res = 6;
        if (inOutType.equals(0)){
            if (isNeedOpenGate.equals(0)){
                res = 6;
            }else {
                if (!CommonUtils.isEmpty(memberWallet)) {
                    switch (memberWallet.getPackKindId()){
                        case 0 :
                        case -3 :
                        case -2 :{
                            //预约，免费，包月
                            long restTime = memberWallet.getEffectiveEndTime().getTime() - inOutTime.getTime();
                            Integer leftDay = Integer.parseInt(restTime/(24*60*60*1000) + "");
                            if (inOutTime.getTime() > memberWallet.getEffectiveStartTime().getTime() &&
                                    inOutTime.getTime() < memberWallet.getEffectiveEndTime().getTime() &&
                                    criticalDay.compareTo(leftDay) >= 0) {
                                res = 2;
                                memberLeft = "有效期还有" + String.valueOf(leftDay) + "天";
                            }else {
                                res = 1;
                            }
                            break;
                        }
                        case 1 : {
                            //包次
                            res = memberWallet.getSurplusNumber() <= 5 ? 2 : 1;
                            if (res.equals(2)) {
                                memberLeft = "剩余" + memberWallet.getSurplusNumber() + "次";
                            }
                            break;
                        }
                        case 2 : {
                            //充值金额
                            res = memberWallet.getSurplusAmount() <= 5 ? 2 : 1;
                            if (res.equals(2)) {
                                memberLeft = "剩余" + memberWallet.getSurplusAmount() + "元";
                            }
                            break;
                        }
                        default:{
                            res = 1;
                            break;
                        }
                    }
                }else
                    res = 1;
            }
        }
        else{   //出场
            res = isNeedOpenGate.equals(0) ? 4 : 3;
        }
        LedDisplayConfig ledDisplayConfig = (LedDisplayConfig) baseDao.getUnique(hql,ledId,res);
        resMap.put("ledDisplayConfig",ledDisplayConfig);
        resMap.put("memberLeft",memberLeft);
        return  resMap;

    }

    @Override
    public String RedressCarno(String oldCarno, String isUseWhileList, String isAutoMatch, String matchBite,
                               String autoMatchPosition, String carparkId, String whiteCorrection) {

        if (StringUtils.isBlank(matchBite) || matchBite.equals("null"))
            matchBite = "7";
        String res = oldCarno;
        String aMatchBite = matchBite;
        String aAutoMatchPosition = autoMatchPosition;
        String aIsAutoMatch = isAutoMatch;
        String aIsUseWhileList = isUseWhileList.equals("1")? "0" : "1";

        if (!aIsAutoMatch.equals("1")){
            aMatchBite = "7";
            aAutoMatchPosition = "";
            return res;
        }

        if (StringUtils.isBlank(aAutoMatchPosition) || StringUtils.isBlank(aMatchBite) || aIsUseWhileList.equals("0") ||
                StringUtils.isBlank(carparkId)){
            if (!StringUtils.isBlank(whiteCorrection)){
                res = IntelligentCorrection(oldCarno,whiteCorrection,carparkId,false);
            }
            return res;
        }

        String aPosArr = aAutoMatchPosition;
        String newCarno = "";
        String aCorrectionChar = "_";

        for (int i = 1;i <= oldCarno.length();i++){
            if (aPosArr.indexOf(String.valueOf(i)) >= 0){
                newCarno = newCarno + aCorrectionChar;
            }else {
                newCarno = newCarno + oldCarno.substring(i-1,i);
            }
        }
        if (StringUtils.isBlank(newCarno))
            newCarno = oldCarno;

        String hql = "from MemberWallet where packKindId in (:packKindIds) and carPark = :carparkId and useMark >= 0";
        Query query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
        List<Integer> packKindIds = new ArrayList<>();
        packKindIds.add(-3);
        packKindIds.add(0);
        packKindIds.add(1);
        packKindIds.add(2);
        query.setParameterList("packKindIds",packKindIds);
        query.setParameter("carparkId",carparkId);



        List<MemberWallet> memberWalletList = (List<MemberWallet>)query.list();
        List<CarnoMatchInfo> carnoMatchInfoList = new ArrayList<CarnoMatchInfo>();
        if (memberWalletList.size() > 0) {
            for (int i = 0; i < memberWalletList.size(); i++) {
                List<String> carnoList = CutStrToList((memberWalletList.get(i)).getMenberNo(), ",");
                if (carnoList.size() > 1){
                    for (int j = 0; j < carnoList.size(); j++) {
                        String aTmpCarno = carnoList.get(j);
                        CarnoMatchInfo carnoMatchInfo = new CarnoMatchInfo();
                        carnoMatchInfo.matchCarno = aTmpCarno;
                        carnoMatchInfo.matchPercent = CompareCarnoInByteEx(aTmpCarno, oldCarno, autoMatchPosition);
                        carnoMatchInfoList.add(carnoMatchInfo);

                        if (aTmpCarno.equals(oldCarno))
                            carnoMatchInfoList.get(carnoMatchInfoList.size() - 1).matchPercent = carnoMatchInfoList.get(carnoMatchInfoList.size() - 1).matchPercent + 1;
                    }
                }
            }
            if (carnoMatchInfoList.size() == 0){
                if (!StringUtils.isBlank(whiteCorrection)){
                    res = IntelligentCorrection(oldCarno,whiteCorrection,carparkId,false);
                    return res;
                }
            }
            Integer maxIndex = 0;
            newCarno = "";

            for (int i = 0;i < carnoMatchInfoList.size();i++){
                if (carnoMatchInfoList.get(maxIndex).matchPercent <= carnoMatchInfoList.get(i).matchPercent){
                    maxIndex = i;
                    if (carnoMatchInfoList.get(maxIndex).matchPercent >= Integer.valueOf(matchBite))
                        newCarno = carnoMatchInfoList.get(maxIndex).matchCarno;
                }
            }
            if (!StringUtils.isBlank(newCarno))
                res = newCarno;
            else{
                if (!StringUtils.isBlank(whiteCorrection))
                    res = IntelligentCorrection(oldCarno,whiteCorrection,carparkId,false);
            }

        }else {
            if (!StringUtils.isBlank(whiteCorrection))
                res = IntelligentCorrection(oldCarno,whiteCorrection,carparkId,false);
        }
        return  res;
    }

    @Override
    public boolean IsWhiteListCarno(String carno, String carparkId) {
        boolean res = false;
        String hql = "select t1.id FROM member_wallet t1 join member_kind t2 on t1.menber_type_id = t2.id and "
                   + "t2.use_mark >=0 and t1.car_park = '" + carparkId + "' "
                   + "and t1.menber_no LIKE '%" + carno + "%' and t1.use_mark >=0";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
        res = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list().size() > 0 ? true : false;
        return res;
    }

    @Override
    public String GetTmpMatchCarno(String oldCarno, String isUseWhileList, String isAutoMatch, String matchBite, String autoMatchPosition, String carparkId, String intelCorrection) {
        String res = oldCarno;
        String secChar = "";
        if (oldCarno.length() > 2)
            secChar = oldCarno.substring(1,2);
        String newCarno = "";
        String correctChar = "_";
        for (int i = 1;i<=oldCarno.length();i++){
            if (autoMatchPosition.indexOf(String.valueOf(i))>=0){
                newCarno = newCarno + correctChar;
            }else {
                newCarno = newCarno + oldCarno.substring(i-1,i);
            }
        }
        if (StringUtils.isBlank(newCarno))
            newCarno = oldCarno;

        String hql = "select car_no,in_time from order_inout_record where carparkId = '" + carparkId + "' "
                   + "and use_mark>= 0 and out_time is null order by in_time desc";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
        List<Map<String,Object>> mapList = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        List<CarnoMatchInfo> carnoMatchInfoListFir = new ArrayList<>();
        if (mapList.size() > 0){
            for (Map map : mapList){
                CarnoMatchInfo carnoMatchInfo = new CarnoMatchInfo();
                carnoMatchInfo.matchCarno = map.get("car_no").toString();
                carnoMatchInfo.matchPercent = CompareCarnoInByteEx(carnoMatchInfo.matchCarno,oldCarno,autoMatchPosition);
                carnoMatchInfo.matchInTime = map.get("in_time").toString();
                carnoMatchInfoListFir.add(carnoMatchInfo);
            }
            if (carnoMatchInfoListFir.size() == 0){
                if (!StringUtils.isBlank(intelCorrection)){
                    res = IntelligentCorrection(oldCarno,intelCorrection,carparkId,true);
                }
                return res;
            }
            List<CarnoMatchInfo> carnoMatchInfoListSec = new ArrayList<>();
            List<CarnoMatchInfo> carnoMatchInfoListThird = new ArrayList<>();
            List<CarnoMatchInfo> carnoMatchInfoListFour = new ArrayList<>();
            if (carnoMatchInfoListFir.size() == 1){
                res = carnoMatchInfoListFir.get(0).matchCarno;
                return res;
            }else {
                for (int i = 0;i<carnoMatchInfoListFir.size();i++){
                    if (carnoMatchInfoListFir.get(i).matchPercent >= Integer.valueOf(matchBite)){
                        CarnoMatchInfo carnoMatchInfo = new CarnoMatchInfo();
                        carnoMatchInfo.matchCarno = carnoMatchInfoListFir.get(i).matchCarno;
                        carnoMatchInfo.matchInTime = carnoMatchInfoListFir.get(i).matchInTime;
                        if (carnoMatchInfoListFir.get(i).matchCarno.length() > 2)
                            carnoMatchInfo.matchSecChar = carnoMatchInfoListFir.get(i).matchCarno.substring(1,2);
                        else
                            carnoMatchInfo.matchSecChar = "";
                        carnoMatchInfoListSec.add(carnoMatchInfo);
                    }
                }
            }

            if (carnoMatchInfoListSec.size() == 1){
                res = carnoMatchInfoListSec.get(0).matchCarno;
                return res;
            }else if (carnoMatchInfoListSec.size() > 1){
                for (int i =0;i<carnoMatchInfoListSec.size();i++){
                    if (secChar.equals(carnoMatchInfoListSec.get(i).matchSecChar)){
                        CarnoMatchInfo carnoMatchInfo = new CarnoMatchInfo();
                        carnoMatchInfo.matchCarno = carnoMatchInfoListSec.get(i).matchCarno;
                        carnoMatchInfo.matchInTime = carnoMatchInfoListSec.get(i).matchInTime;
                        carnoMatchInfo.matchPrioCity = carnoMatchInfoListSec.get(i).matchCarno.substring(0,1);
                        carnoMatchInfoListThird.add(carnoMatchInfo);
                    }
                }
                if (carnoMatchInfoListThird.size() == 0){
                    for (int i =0;i<carnoMatchInfoListSec.size();i++){
                        CarnoMatchInfo carnoMatchInfo = new CarnoMatchInfo();
                        carnoMatchInfo.matchCarno = carnoMatchInfoListSec.get(i).matchCarno;
                        carnoMatchInfo.matchInTime = carnoMatchInfoListSec.get(i).matchInTime;
                        carnoMatchInfo.matchPrioCity = carnoMatchInfoListSec.get(i).matchCarno.substring(0,1);
                        carnoMatchInfoListThird.add(carnoMatchInfo);

                    }
                }
            }else {
                if (!StringUtils.isBlank(intelCorrection)){
                    res = IntelligentCorrection(oldCarno,intelCorrection,carparkId,true);
                }
                return res;
            }

            if (carnoMatchInfoListThird.size() == 1){
                res = carnoMatchInfoListThird.get(0).matchCarno;
                return res;
            }else if (carnoMatchInfoListThird.size() > 1){
                String prioCity = "闽";
                hql = "from DepartmentInfo where useMark >= 0";
                DepartmentInfo departmentInfo = (DepartmentInfo)baseDao.getUnique(hql);
                prioCity = departmentInfo.getPriorityCity();
                for (int i =0;i<carnoMatchInfoListThird.size();i++){
                    if (prioCity.equals(carnoMatchInfoListThird.get(i).matchPrioCity)){
                        CarnoMatchInfo carnoMatchInfo = new CarnoMatchInfo();
                        carnoMatchInfo.matchCarno = carnoMatchInfoListThird.get(i).matchCarno;
                        carnoMatchInfo.matchInTime = carnoMatchInfoListThird.get(i).matchInTime;
                        carnoMatchInfoListFour.add(carnoMatchInfo);
                    }
                }
                if (carnoMatchInfoListFour.size() == 0){
                    for (int i =0;i<carnoMatchInfoListThird.size();i++){
                        CarnoMatchInfo carnoMatchInfo = new CarnoMatchInfo();
                        carnoMatchInfo.matchCarno = carnoMatchInfoListThird.get(i).matchCarno;
                        carnoMatchInfo.matchInTime = carnoMatchInfoListThird.get(i).matchInTime;
                        carnoMatchInfoListThird.add(carnoMatchInfo);
                    }
                }
            }else {
                if (!StringUtils.isBlank(intelCorrection)){
                    res = IntelligentCorrection(oldCarno,intelCorrection,carparkId,true);
                }
                return res;
            }

            //根据消费金额
            int maxIndex = 0;
            if (carnoMatchInfoListFour.size() == 1){
                res = carnoMatchInfoListFour.get(0).matchCarno;
                return res;
            }else if (carnoMatchInfoListFour.size() > 1){
                for (int i =0;i<carnoMatchInfoListFour.size();i++){
                    if (!StringUtils.isBlank(carnoMatchInfoListFour.get(i).matchInTime)){
                        if (Timestamp.valueOf(carnoMatchInfoListFour.get(maxIndex).matchInTime).getTime() < Timestamp.valueOf(carnoMatchInfoListFour.get(i).matchInTime).getTime()){
                            maxIndex = i;
                            res = carnoMatchInfoListFour.get(maxIndex).matchCarno;
                        }
                    }
                }
                res = carnoMatchInfoListFour.get(maxIndex).matchCarno;
            }else {
                if (!StringUtils.isBlank(intelCorrection)){
                    res = IntelligentCorrection(oldCarno,intelCorrection,carparkId,true);
                }
                return res;
            }
        }else {
            if (carnoMatchInfoListFir.size() == 0){
                if (!StringUtils.isBlank(intelCorrection)){
                    res = IntelligentCorrection(oldCarno,intelCorrection,carparkId,true);
                }
                return res;
            }
        }

        return res;
    }

    @Override
    public void updateRealChannelConfig(DeviceInfo deviceInfo, String carNo, Timestamp realTime,String fileName) {

        RealChannelConfig realChannelConfig;
        String hql = "from RealChannelConfig where devId = ? and channelId = ?";
        realChannelConfig = (RealChannelConfig)baseDao.getUnique(hql,deviceInfo.getDeviceId(),deviceInfo.getOwnCarRoad());
        if(CommonUtils.isEmpty(realChannelConfig)){
            realChannelConfig = new RealChannelConfig();
        }
        InOutCarRoadInfo inOutCarRoadInfo = (InOutCarRoadInfo)baseDao.getById(InOutCarRoadInfo.class,deviceInfo.getOwnCarRoad());
        realChannelConfig.setPostId(inOutCarRoadInfo.getManageComputerId());
        realChannelConfig.setCarNo(carNo);
        realChannelConfig.setChannelId(deviceInfo.getOwnCarRoad());
        realChannelConfig.setIsOpened(0);
        realChannelConfig.setDevId(deviceInfo.getDeviceId());
        realChannelConfig.setUpdateTime(CommonUtils.getTimestamp());
        realChannelConfig.setFileName(fileName);
        baseDao.save(realChannelConfig);
    }

    @Override
    public void dealCarPlateInd(SocketClient socketClient, JSONObject params) {
        String CarPlate = params.getString("CarPlate");
        if(!StringUtils.isBlank(CarPlate)){
            byte[] carplates = Base64.decode(CarPlate);
            try {
                CarPlate = new String(carplates,"GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        String Time = params.getString("Time");
        String[] timeArrays = Time.substring(0,Time.length()).split("_");
        String date = timeArrays[0].replaceAll("-","");
        String time = timeArrays[1].replaceAll(":","");
        String fileName = CarPlate.substring(1,CarPlate.length()-1) + "_" + date + "_" + time;

        String fileUri = CommonUtils.saveCarPlateImage("carPlateImages",params.getString("Photo"),
                AppInfo.appRootRealPath,AppInfo.contextPath,
                socketClient.getSocket().getInetAddress().getHostAddress(),date,fileName);

    }

    @Override
    public void dealHistoryCarPlateInd(SocketClient socketClient, JSONObject params) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException {
        if(CommonUtils.isEmpty(socketClient.getDeviceInfo())){
            return;
        }
        if(CommonUtils.isEmpty(socketClient.getDeviceInfo().getOwnCarRoad())){
            return;
        }

        String CarPlate = params.getString("CarPlate");
        String CarPlateColor = params.getString("Color");
        if(!StringUtils.isBlank(CarPlate)){
            byte[] carplates = Base64.decode(CarPlate);
            try {
                CarPlate = new String(carplates,"GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        String Time = params.getString("Time");
        String[] timeArrays = Time.substring(0,Time.length()).split("_");
        String date = timeArrays[0].replaceAll("-","");
        String time = timeArrays[1].replaceAll(":","");
        String fileName = CarPlate.substring(1,CarPlate.length()) + "_" + date + "_" + time + ".jpg";
        String fileUri = AppInfo.contextPath + "/resources/carPlateImages/" + date + "/" + socketClient.getSocket().getInetAddress().getHostAddress() + "/" + fileName;

        InOutCarRoadInfo inOutCarRoadInfo = (InOutCarRoadInfo)baseDao.getById(InOutCarRoadInfo.class,socketClient.getDeviceInfo().getOwnCarRoad());
        CarparkInfo carparkInfo = (CarparkInfo)baseDao.getById(CarparkInfo.class,inOutCarRoadInfo.getOwnCarparkNo());

        String oldCarno = "";
        if ("1".equals(inOutCarRoadInfo.getIsAutoMatchCarNo())){
            if (!IsWhiteListCarno(CarPlate,carparkInfo.getCarparkId())){
                String correctionCarno = RedressCarno(CarPlate,"0",inOutCarRoadInfo.getIsAutoMatchCarNo(),
                        String.valueOf(inOutCarRoadInfo.getAutoMatchLeastBite()),inOutCarRoadInfo.getAutoMatchCarNoPos(),
                        carparkInfo.getCarparkId(),inOutCarRoadInfo.getWhiteIntelligentCorrection());
                if (!correctionCarno.equals(CarPlate)){
                    oldCarno = CarPlate;
                    CarPlate = correctionCarno;
                }
            }
        }

        //获取会员及收费类型信息
        String hql = "from MemberWallet where menberNo like ? and carPark = ? AND useMark >= 0";
        MemberWallet memberWallet = (MemberWallet)baseDao.getUnique(hql,"%" + CarPlate + "%",carparkInfo.getCarparkId());
        String packId = CommonUtils.isEmpty(memberWallet) ? "2" : String.valueOf(memberWallet.getPackKindId());
        String carType = clientBizService.getCarTypeInfo(packId);

        if(inOutCarRoadInfo.getCarRoadType().equals("0")){
            Timestamp inTime = GetInOutTime(params.getString("Time"));
            InoutRecordInfo inoutRecordInfo = new InoutRecordInfo();
            inoutRecordInfo.setCarNoOld(oldCarno);
            inoutRecordInfo.setCarNo(CarPlate);
            inoutRecordInfo.setInoutTime(inTime);
            inoutRecordInfo.setInoutFlag((byte)0);
            inoutRecordInfo.setInoutStatus((byte)3);
            inoutRecordInfo.setCarparkId(carparkInfo.getCarparkId());
            inoutRecordInfo.setCarparkName(carparkInfo.getCarparkName());
            inoutRecordInfo.setCarRoadId(inOutCarRoadInfo.getCarRoadId());
            inoutRecordInfo.setCarRoadName(inOutCarRoadInfo.getCarRoadName());
            inoutRecordInfo.setPostId(socketClient.getPostComputerManage().getPostComputerId());
            inoutRecordInfo.setPostName(socketClient.getPostComputerManage().getPostComputerName());
            inoutRecordInfo.setCameraId(socketClient.getDeviceInfo().getDeviceId());
            inoutRecordInfo.setCarNoColor((byte)Integer.parseInt(CarPlateColor));
            inoutRecordInfo.setCarType(carType);
            hql = "from Operator where operatorUserName = ?";
            Operator operator = (Operator)baseDao.getUnique(hql,"elecOperator");
            inoutRecordInfo.setOperatorId(operator.getOperatorId());
            inoutRecordInfo.setOperatorName(operator.getOperatorName());
            inoutRecordInfo.setUseMark((byte)0);
            inoutRecordInfo.setAddTime(CommonUtils.getTimestamp());
            baseDao.save(inoutRecordInfo);

            //插入进出订单表
            hql = "from OrderInoutRecord where carNo = ? and carparkId = ? and outRecordId is null";
            OrderInoutRecord orderInoutRecord = (OrderInoutRecord)baseDao.getUnique(hql,CarPlate,carparkInfo.getCarparkId());
            if (CommonUtils.isEmpty(orderInoutRecord)){
                //原先没有入场记录
                orderInoutRecord = new OrderInoutRecord();
                orderInoutRecord.setCarNo(CarPlate);
                orderInoutRecord.setCarparkId(carparkInfo.getCarparkId());
                orderInoutRecord.setCarparkName(carparkInfo.getCarparkName());
                orderInoutRecord.setAddTime(CommonUtils.getTimestamp());
                orderInoutRecord.setInCarRoadId(inOutCarRoadInfo.getCarRoadId());
                orderInoutRecord.setInCarRoadName(inOutCarRoadInfo.getCarRoadName());
                orderInoutRecord.setInPictureName("");
                orderInoutRecord.setInTime(inTime);
                orderInoutRecord.setInRecordId(inoutRecordInfo.getInoutRecordId());
                orderInoutRecord.setCarType(carType);
                orderInoutRecord.setRemark("入场离线数据");
                baseDao.save(orderInoutRecord);
            }else {
                orderInoutRecord.setUpdateTime(CommonUtils.getTimestamp());
                orderInoutRecord.setInCarRoadId(inOutCarRoadInfo.getCarRoadId());
                orderInoutRecord.setInCarRoadName(inOutCarRoadInfo.getCarRoadName());
                orderInoutRecord.setInPictureName("");
                orderInoutRecord.setInTime(inTime);
                orderInoutRecord.setInRecordId(inoutRecordInfo.getInoutRecordId());
                orderInoutRecord.setCarType(carType);
                orderInoutRecord.setRemark("入场离线数据");
                baseDao.save(orderInoutRecord);
            }
            clientBizService.WorkForMultiCarno(CarPlate,0,carparkInfo.getCarparkId(),orderInoutRecord);
        }else{
            Timestamp outTime = GetInOutTime(params.getString("Time"));
            InoutRecordInfo inoutRecordInfo = new InoutRecordInfo();
            inoutRecordInfo.setCarNoOld(oldCarno);
            inoutRecordInfo.setCarNo(CarPlate);
            inoutRecordInfo.setInoutTime(outTime);
            inoutRecordInfo.setInoutFlag((byte)1);
            inoutRecordInfo.setInoutStatus((byte)3);
            inoutRecordInfo.setCarparkId(carparkInfo.getCarparkId());
            inoutRecordInfo.setCarparkName(carparkInfo.getCarparkName());
            inoutRecordInfo.setCarRoadId(inOutCarRoadInfo.getCarRoadId());
            inoutRecordInfo.setCarRoadName(inOutCarRoadInfo.getCarRoadName());
            inoutRecordInfo.setPostId(socketClient.getPostComputerManage().getPostComputerId());
            inoutRecordInfo.setPostName(socketClient.getPostComputerManage().getPostComputerName());
            inoutRecordInfo.setCameraId(socketClient.getDeviceInfo().getDeviceId());
            inoutRecordInfo.setCarNoColor((byte)Integer.parseInt(CarPlateColor));
            inoutRecordInfo.setCarType(carType);
            hql = "from Operator where operatorUserName = ?";
            Operator operator = (Operator)baseDao.getUnique(hql,"elecOperator");
            inoutRecordInfo.setOperatorId(operator.getOperatorId());
            inoutRecordInfo.setOperatorName(operator.getOperatorName());
            inoutRecordInfo.setUseMark((byte)0);
            inoutRecordInfo.setAddTime(CommonUtils.getTimestamp());
            baseDao.save(inoutRecordInfo);

            OrderInoutRecord matchInInfo;

            matchInInfo = clientBizService.GetMatchInfo(CarPlate,carparkInfo.getCarparkId(),outTime);

            if (!CommonUtils.isEmpty(matchInInfo)){
                baseDao.deleteById(OrderInoutRecord.class,matchInInfo.getChargeInfoId());
            }

        }

    }



    //转换出入场时间
    private Timestamp GetInOutTime(String timeStr){
        Timestamp resTime = new Timestamp(System.currentTimeMillis());
        String[] inTimeArr = timeStr.split("_");
        if(inTimeArr.length == 2){
            String inTimeStr = inTimeArr[0] + " " + inTimeArr[1];
            resTime = Timestamp.valueOf(inTimeStr);
        }
        return  resTime;
    }

    //根据播放模板确定播放内容
    @Override
    public String GetPlayStr(JSONObject params,LedDisplayConfig ledDisplayConfig,int playChannel){
        String res = "";
        try {
            String playModel = "";
            int displayIndex = ledDisplayKeyWords.length;
            String isMultiCarno = (String)params.get("isMultiCarno"); //0或1
            switch (playChannel){
                case 1:
                    playModel = ledDisplayConfig.getTopRowContent();
                    break;
                case 2:
                    playModel = ledDisplayConfig.getMiddleRowContent();
                    break;
                case 3:
                    playModel = ledDisplayConfig.getButtomRowContent();
                    break;
                case 4:
                    playModel = ledDisplayConfig.getVoiceBroadcast();
                    break;
            }
            for (int i = 0;i < ledDisplayKeyWords.length;i++){
                if (playModel.equals(ledDisplayKeyWords[i])){
                    displayIndex = i;
                    break;
                }
            }
            switch (displayIndex){
                case 0:
                    res = CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",CommonUtils.getTimestamp());
                    break;
                case 1:
                    Integer availableCarCount = params.getInteger("availableCarSpace");
                    String availableCarSpace = availableCarCount.toString();
                    if (availableCarCount < 100 && availableCarCount > 10){
                        availableCarSpace = "0" + availableCarSpace;
                    }else if (availableCarCount < 10){
                        availableCarSpace = "00" + availableCarSpace;
                    }
                    res = "空位 " + availableCarSpace;
                    break;
                case 2:
                    res = params.getString("carPlate");
                    break;
                case 3:
                    res = params.getString("memberLeft");
                    break;
                case 4:
                    res = params.getString("parkingLength");
                    break;
                case 5:
                    res = "1".equals(isMultiCarno) ? params.getString("kindName") + "-AB车" : params.getString("kindName");
                    break;
                case 6:
                    res = params.getString("carPlate") + "-" + params.getString("kindName");
                    res = "1".equals(isMultiCarno) ? res + "-AB车" : res;
                    break;
                case 7:
                    res = "缴" + params.getString("chargeAmount") + "元";
                    break;
                case 8:
                    res = params.getString("carparkName") + "空" + params.getString("availableCarSpace");
                    break;
                case 9:
                    res = "请缴费" + params.getString("chargeAmount")+ "元";
                    break;
                case 10:
                    res = params.getString("limitReason");
                    break;
                case 11:
                    res = "欢迎光临";
                    break;
                case 12:
                    res = "一路顺风";
                    break;
                case 13:
                    res = "请缴费" + params.getString("chargeAmount") + "元";
                    break;
                case 14:
                    res = "一位多车 套餐已使用";
                    break;
                case  15:
                    res = params.get("carPlate") + "-" + params.get("driverName") + "老师";
                    break;
                case 16:
                    Calendar nowTime = Calendar.getInstance();
                    res = nowTime.get(Calendar.YEAR) + "年" + (nowTime.get(Calendar.MONTH) + 1) + "月" +  nowTime.get(Calendar.DAY_OF_MONTH) + "日 " +
                            nowTime.get(Calendar.HOUR_OF_DAY) + "时" + nowTime.get(Calendar.MINUTE) + "分" ;
                    break;
                default:
                    res = playModel;    //如果都不匹配，则按照显示的播放
                    break;
            }
        } catch (Exception e) {
            LOGGER.error("组织三行播报屏播报信息异常");
        }
        return res;
    }

    //根据播放模板确定播放内容，四行显示屏专用
    private String GetPlayStrFour(JSONObject params,LedDisplayConfig ledDisplayConfig,int playChannel){
        String res = "";
        try {
            String playModel = "";
            String isMultiCarno = (String)params.get("isMultiCarno"); //0或1
            int displayIndex = ledDisplayKeyWords.length;
            switch (playChannel){
                case 0:
                    playModel = ledDisplayConfig.getTopRowContent();
                    break;
                case 1:
                    playModel = ledDisplayConfig.getMiddleRowContent();
                    break;
                case 2:
                    playModel = ledDisplayConfig.getButtomRowContent();
                    break;
                case 3:
                    playModel = ledDisplayConfig.getFourthRowContent();
                    break;
                case 4:
                    playModel = ledDisplayConfig.getVoiceBroadcast();
                    break;
            }
            for (int i = 0;i < ledDisplayKeyWords.length;i++){
                if (playModel.equals(ledDisplayKeyWords[i])){
                    displayIndex = i;
                    break;
                }
            }
            switch (displayIndex){
                case 0:
                    res = CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",CommonUtils.getTimestamp());
                    break;
                case 1:
                    res = "空位 " + params.getString("availableCarSpace");
                    break;
                case 2:
                    res = params.getString("carPlate");
                    break;
                case 3:
                    res = params.getString("memberLeft");
                    break;
                case 4:
                    res = params.getString("parkingLength");
                    break;
                case 5:
                    res = "1".equals(isMultiCarno) ? params.getString("kindName") + "-AB车" : params.getString("kindName");
                    break;
                case 6:
                    res = params.getString("carPlate") + "-" + params.getString("kindName");
                    res = "1".equals(isMultiCarno) ? res + "-AB车" : res;
                    break;
                case 7:
                    res = "缴" + params.getString("chargeAmount") + "元";
                    break;
                case 8:
                    res = params.getString("carparkName") + "空" + params.getString("availableCarSpace");
                    break;
                case 9:
                    res = "请缴费" + params.getString("chargeAmount") + "元";
                    break;
                case 10:
                    res = params.getString("limitReason");
                    break;
                case 11:
                    res = "欢迎光临";
                    break;
                case 12:
                    res = "一路顺风";
                    break;
                case 13:
                    res = "请交费" + params.getString("chargeAmount") + "元";
                    break;
                case 14:
                    res = "一位多车 套餐已使用";
                    break;
                case  15:
                    res = params.get("carPlate") + "-" + params.get("driverName") + "老师";
                    break;
                case 16:
                    Calendar nowTime = Calendar.getInstance();
                    res = nowTime.get(Calendar.YEAR) + "年" + (nowTime.get(Calendar.MONTH) + 1) + "月" +  nowTime.get(Calendar.DAY_OF_MONTH) + "日 " +
                          nowTime.get(Calendar.HOUR_OF_DAY) + "时" + nowTime.get(Calendar.MINUTE) + "分" ;
                    break;
                default:
                    res = playModel;    //如果都不匹配，则按照显示的播放
                    break;
            }

        /*finalRes = finalRes + "0064FFFF62";                          //DA + SP + PN[2] + 0x62，基本参数
        finalRes = finalRes + "DL";                                  //DL，19 个字节再加上文本长度
        finalRes = finalRes + String.valueOf(playChannel);           //TWID，播放的窗体（0-3）
        finalRes = finalRes + "15";                                  //ETM，文字进入窗口的方式。取值范围（0，0x15 ）,0为立即显示，0x15为连续左移
        finalRes = finalRes + "01";                                  //ETS，ETS为1
        finalRes = finalRes + "00";                                  //DM，文字停留方式，0x00-停留1秒单位，0x01为停留 1/10 秒单位
        finalRes = finalRes + "02";                                  //DT，为文字停留的时间，取值范围为0-255
        finalRes = finalRes + "15";                                  //EXM，0x00-立即显示，0x15-连续左移
        finalRes = finalRes + "00";                                  //EXS，EXS为0
        finalRes = finalRes + "03";                                  //FINDEX，为文字的字体索引值。取值03
        finalRes = finalRes + "00";                                  //DRS，为显示的次数。取值范围为0-255，当为 0 的时，表示无限循环显示
        finalRes = finalRes + "FF000000";                            //TC[4]，为文字的颜色值。存储结构为 R G B A 三基色，各占 8位
        finalRes = finalRes + "00000000";                            //BC[4]，为背景的颜色值
        finalRes = finalRes + "DL";                                  //TL[2]，为文字的长度。16 位数据类型，小端模式。目前最大长度为100 字节
        finalRes = finalRes + "";                                    //TEXT[...]，为显示的文字内容
        finalRes = finalRes + "F4F5";                                //CRC[2]， 个字节再加上文本长度*/
        } catch (Exception e) {
            LOGGER.error("组织四行播报屏播报信息异常");
        }

        return res;
    }

    //将播放的字符串转为Base64位加密
    private static String GetLedPlayStrToBase(String playStr){
        byte[] data = new byte[playStr.length()/2];
        for(int i = 0,j=0;i < playStr.length();i+=2,j++){
            String tmp = playStr.substring(i,i+2);
            Integer tmpint = Integer.parseInt(tmp,16);
            data[j] = (byte)(tmpint.intValue());
        }
        return Base64.encodeToString(data);
    }

    //将汉字转化为16进制
    private static String ChangePlayInfoToOx(String playStr) throws UnsupportedEncodingException{
        byte[] text = playStr.getBytes(Charset.forName("GB2312"));
        if (text == null) {
            return "BBB6D3ADB9E2C1D9";
        }
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[text.length * 2];
        for (int j = 0; j < text.length; j++) {
            int v = text[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        String str = new String(hexChars);
        return str.toUpperCase();
    }

    //CRC校验
    private static String GetCrcCheckInfo(String inputStr){
        int usLen = inputStr.length()/2;
        int[] pucFrame = new int[usLen];
        for (int i = 0;i < usLen;i++){
            pucFrame[i] = Integer.parseInt(inputStr.substring(i*2,i*2+2),16);
        }

        int ucCRCHi = 0xFF;
        int ucCRCLo = 0xFF;
        int i;
        int iIndex = 0;

        for (i = 0; i < usLen; i++){
            iIndex = ucCRCLo ^ pucFrame[i];
            ucCRCLo = ucCRCHi ^ aucCRCHi[iIndex];
            ucCRCHi = aucCRCLo[iIndex];
        }
        int intRes = ucCRCHi << 8 | ucCRCLo;
        String res = Integer.toHexString(intRes);
        switch (res.length()){
            case 1 :
                res = "000" + res;
                break;
            case 2 :
                res = "00" + res;
                break;
            case 3 :
                res = "0" + res;
                break;
        }
        String[] data = res.split("");
        res = data[2] + data[3] + data[0] + data[1];
        return res.toUpperCase();
    }



    //智能纠错
    private String IntelligentCorrection(String oldCarno,String correctionStr,String carparkId,boolean isTmpCarno){
        String res = oldCarno;
        if (StringUtils.isBlank(oldCarno) || StringUtils.isBlank(correctionStr) || StringUtils.isBlank(carparkId))
            return res;
        List<String> charList = CutStrToList(correctionStr,";");
        List<String> charList2 = new ArrayList<>();
        String hql;
        String aPos = "";
        if (isTmpCarno) {
            hql = "select car_no,in_time from order_inout_record where carpark_id = '" + carparkId + "' "
                + "and use_mark >= 0";
        }
        else {
            hql = "select menber_no from member_wallet where pack_kind_id in (0,1,2,-3) and car_park = '"
                    + carparkId + "' and use_mark >= 0";
        }
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
        List<Map<String,Object>> mapList = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        if (mapList.size() > 0){
            String aTmpCarno = "";
            int aLen = 0;
            List<String> tempCarnoList = new ArrayList<>();
            List<CarnoMatchInfo> chooseCarnoList = new ArrayList<CarnoMatchInfo>();
            for (Map map : mapList){
                aTmpCarno = isTmpCarno ? map.get("car_no").toString() : map.get("menber_no").toString();
                tempCarnoList = CutStrToList(aTmpCarno,",");
                boolean isFind = true;
                for (int i = 0;i < tempCarnoList.size();i++){
                    aTmpCarno = tempCarnoList.get(i);
                    isFind = true;
                    if (!aTmpCarno.equals(oldCarno)){
                        isFind = false;
                        for (int j = 0;j < charList.size();j++){
                            charList2 = CutStrToList(charList.get(j),",");
                             if (aTmpCarno.length() == oldCarno.length() && charList2.get(0).length() == charList2.get(1).length()){
                                List<String> aNewCarnoList = new ArrayList<>();
                                aNewCarnoList = GetOldCarnoMatchList(oldCarno,charList2.get(0),charList2.get(1));
                                for (int k=0;k<aNewCarnoList.size();k++){
                                    if (aNewCarnoList.get(k).equals(aTmpCarno)){
                                        isFind = true;
                                        break;
                                    }
                                }
                            }else {
                                isFind = true;
                                if (!aTmpCarno.equals(oldCarno)){
                                    isFind = false;
                                    aPos = MyPos(oldCarno,charList2.get(0));
                                    isFind = IsFindMatchCarno(charList.get(j),aPos,oldCarno,aTmpCarno);
                                }
                            }
                            if (isFind)
                                break;
                        }
                    }else
                        isFind = false;

                    if (isFind){
                        aLen++;
                        chooseCarnoList.get(aLen-1).matchCarno = aTmpCarno;
                        if (isTmpCarno)
                            chooseCarnoList.get(aLen-1).matchInTime = map.get("in_time").toString();
                        else
                            chooseCarnoList.get(aLen-1).matchInTime = CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",CommonUtils.getTimestamp());
                    }
                }
            }
            int matchIndex = 0;
            if (chooseCarnoList.size() > 0 && aLen > 0){
                matchIndex = 0;
                for (int i = 0;i < chooseCarnoList.size();i++){
                    if (!StringUtils.isBlank(chooseCarnoList.get(i).matchInTime)){
                        if (Timestamp.valueOf(chooseCarnoList.get(matchIndex).matchInTime).getTime() < Timestamp.valueOf(chooseCarnoList.get(i).matchInTime).getTime()){
                            matchIndex = i;
                        }
                    }
                }
                res = chooseCarnoList.get(matchIndex).matchCarno;
            }
        }
        return res;
    }

    //模糊匹配
    private String FuzzyMatch(String oldCarno,String isUseWhileList, String isAutoMatch, String matchBite,
                              String autoMatchPosition, String carparkId, String intelligentCorrection){
        String res = oldCarno;

        return res;
    }

    private Integer CompareCarnoInByteEx(String outCarno,String inCarno,String matchPosition){
        Integer res = 0;
        if (!StringUtils.isBlank(matchPosition)){
            List<String> positionList = new ArrayList<>();
            positionList.add("Start");
            if (matchPosition.indexOf("1")>=0)
                positionList.add("1");
            else
                positionList.add("555");
            if (matchPosition.indexOf("2")>=0)
                positionList.add("2");
            else
                positionList.add("555");
            if (matchPosition.indexOf("3")>=0)
                positionList.add("3");
            else
                positionList.add("555");
            if (matchPosition.indexOf("4")>=0)
                positionList.add("4");
            else
                positionList.add("555");
            if (matchPosition.indexOf("5")>=0)
                positionList.add("5");
            else
                positionList.add("555");
            if (matchPosition.indexOf("6")>=0)
                positionList.add("6");
            else
                positionList.add("555");
            if (matchPosition.indexOf("7")>=0)
                positionList.add("7");
            else
                positionList.add("555");

            int minLen = outCarno.length() > inCarno.length()? inCarno.length() : outCarno.length();
            for (int i = 1;i<=7;i++){
                if (!positionList.get(i).equals(String.valueOf(i))){
                    if (minLen >= i){
                        String outTmpCarno = outCarno.substring(i-1,i);
                        String inTmpCarno = inCarno.substring(i-1,i);
                        if (outTmpCarno.equals(inTmpCarno) && (!StringUtils.isBlank(inTmpCarno)) && (!StringUtils.isBlank(outTmpCarno)))
                            res++;
                    }
                }else
                    res++;
            }
        }
        return res;
    }

    private List<String> CutStrToList(String handleStr,String tagStr){
        List<String> res = new ArrayList<>();
        String[] strArr = handleStr.split(tagStr);
        if (strArr.length > 0) {
            for (int i = 0; i < strArr.length; i++) {
                res.add(strArr[i]);
            }
        }


       /* Integer selPos = 0;
        selPos = handleStr.indexOf(tagStr);
        if (selPos >= 0) {
            while (selPos >= 0) {
                String valueStr = handleStr.substring(0,selPos);
                if (!StringUtils.isBlank(valueStr))
                    res.add(valueStr);
                handleStr = handleStr.substring(selPos,handleStr.length()-1);
                selPos = handleStr.indexOf(tagStr);
            }
        }else {
            res.add(handleStr);
        }*/
        return res;
    }

    private List<String> GetOldCarnoMatchList(String oldCarno,String oldChar,String newChar){
        int aLen = oldChar.length();
        int aIndex;
        List<String> aNewCarnoList = new ArrayList<>();

        for (int i = 0;i <= oldCarno.length()-aLen;i++){
            aIndex = 0;
            String aCurCarno = oldCarno;
            for (int j = 0;j <= aLen-1;j++){
                String aTempOldChar = oldCarno.substring(i+j,i+j+1);
                String aTempNewChar = oldChar.substring(j,j+1);
                String aTempChangeChar = newChar.substring(j,j+1);
                if (aTempOldChar.equals(aTempNewChar)){
                    aIndex++;
                    aCurCarno = aCurCarno.substring(0,i+j) + aTempChangeChar + aCurCarno.substring(i+j+1,aCurCarno.length());
                }else
                    break;
            }
            if (aIndex == aLen){
                aNewCarnoList.add(aCurCarno);
            }
        }
        for (int i = 0;i<=aNewCarnoList.size()-1;i++){
            if (oldChar.indexOf(aNewCarnoList.get(i)) >= 0){
                List<String> aSubNewCarnoList = GetOldCarnoMatchList(aNewCarnoList.get(i),oldChar,newChar);
                for (int j = 0;j<aSubNewCarnoList.size()-1;j++){
                    aNewCarnoList.add(aSubNewCarnoList.get(j));
                }
            }
        }
        return aNewCarnoList;
    }

    private String MyPos(String srcStr,String matchStr){
        String res = "-1";
        if (StringUtils.isBlank(srcStr) || StringUtils.isBlank(matchStr))
            return res;
        else {
            int srcLen = srcStr.length();
            int matchLen = matchStr.length();
            int begPos = 0;
            if (srcLen >= matchLen){
                for (int i = 0;i <= (srcLen-matchLen);i++){
                    int aIndex = 0;
                    for (int j=0;j< matchLen;j++){
                        String tempDstChar = srcStr.substring(i+j,i+j+1);
                        String tempMatchChar = matchStr.substring(j,j+1);
                        if (j==0)
                            begPos = i + j;
                        if (tempDstChar.equals(tempMatchChar))
                            aIndex++;
                        else
                            break;
                    }
                    if (aIndex == matchLen){
                        if (res.equals("-1"))
                            res = String.valueOf(begPos);
                        else
                            res = res + "," + String.valueOf(begPos);
                    }
                }
            }
        }
        return  res;
    }

    private boolean IsFindMatchCarno(String charListStr,String aPos,String oldCarno,String tmpCarno){
        boolean res = false;
        if (StringUtils.isBlank(charListStr))
            return res;
        String tmpOldCarno = oldCarno;
        List<String> charList2 = CutStrToList(charListStr,",");
        if (!aPos.equals("-1")){
            List<String> posList = CutStrToList(aPos,",");
            for (int i = 0;i<posList.size();i++){
                tmpOldCarno = StrReplaceFromPos(tmpOldCarno,charList2.get(0),charList2.get(1),Integer.valueOf(posList.get(i)));
                if (tmpOldCarno.equals(tmpCarno)){
                    res = true;
                    break;
                }else{
                    if (posList.size() > 1){
                        String tmpPos = aPos;
                        tmpPos =tmpPos.substring(2,tmpPos.length());
                        res = IsFindMatchCarno(charListStr,tmpPos,tmpOldCarno,tmpCarno);
                    }
                }
            }
        }
        return res;
    }

    private String StrReplaceFromPos(String oldCarno,String delStr,String replaceStr,Integer aPos){
        if (aPos.equals(null))
            aPos = 1;
        String res = oldCarno;
        if (aPos < 0 || oldCarno.length() < delStr.length() || replaceStr.length() == 0)
            return res;
        oldCarno = oldCarno.substring(0,aPos) + replaceStr + oldCarno.substring(aPos + delStr.length(),oldCarno.length());
        res = oldCarno;
        return res;
    }

    private String playStrToFourLed(String playContent,String playIndex){
        String playInfo = "";
        try {
            playInfo = ChangePlayInfoToOx(playContent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Integer strLen = playInfo.length() / 2 + 19;
        Integer teLen = playInfo.length() / 2;
        String dlength = (Integer.toHexString(strLen)).length() < 2 ? "0" + Integer.toHexString(strLen) : Integer.toHexString(strLen);
        String textLen = (Integer.toHexString(teLen)).length() < 2 ? "0" + Integer.toHexString(teLen) : Integer.toHexString(teLen);
        String needCheckContent = "0064FFFF62" + dlength.toUpperCase() + "0" + playIndex + "1501000215010300FF00000000000000" + textLen.toUpperCase() + "00"
                + playInfo;
        String crcContent = GetCrcCheckInfo(needCheckContent);
        String finalContent = needCheckContent + crcContent;
        String res = GetLedPlayStrToBase(finalContent);
        return res;
    }
}
