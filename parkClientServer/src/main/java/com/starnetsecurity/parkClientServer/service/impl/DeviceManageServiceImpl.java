package com.starnetsecurity.parkClientServer.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.util.DateUtils;
import com.starnetsecurity.commonService.util.DeviceManageUtils;
import com.starnetsecurity.commonService.util.StarnetDeviceUtils;
import com.starnetsecurity.parkClientServer.entity.*;
import com.starnetsecurity.parkClientServer.service.DeviceBizService;
import com.starnetsecurity.parkClientServer.service.DeviceManageService;
import com.starnetsecurity.parkClientServer.sockServer.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.codec.Base64;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by 宏炜 on 2017-12-29.
 */
@Service
public class DeviceManageServiceImpl implements DeviceManageService {

    @Autowired
    HibernateBaseDao baseDao;

    @Autowired
    DeviceBizService deviceBizService;

    @Override
    public void initDeviceStatus() {
        String hql = "from DeviceInfo where useMark >= 0 and deviceType = 0";
        List<DeviceInfo> deviceInfoList = (List<DeviceInfo>)baseDao.queryForList(hql);
        if (deviceInfoList != null && deviceInfoList.size() > 0){
            for (DeviceInfo deviceInfo :deviceInfoList){
                deviceInfo.setDevStatus("0");
                baseDao.update(deviceInfo);
            }
        }
    }

    @Override
    public List<Map<String, Object>> getPageList(Integer size, Integer page) {
        page = page/size + 1;
        String hql = "from DeviceInfo where useMark >= 0 and deviceType = 0";
        List<DeviceInfo> deviceInfos = (List<DeviceInfo>)baseDao.pageQuery(hql,page,size);
        List<Map<String,Object>> list = new ArrayList<>();
        for(DeviceInfo deviceInfo : deviceInfos){
            Map<String,Object> objectMap = new HashedMap();
            objectMap.put("deviceName",deviceInfo.getDeviceName());
            objectMap.put("deviceIp",deviceInfo.getDeviceIp());
            objectMap.put("devicePort",deviceInfo.getDevicePort());
            objectMap.put("deviceUsername",deviceInfo.getDeviceUsername());
            objectMap.put("devicePwd",deviceInfo.getDevicePwd());
            objectMap.put("devStatus",deviceInfo.getDevStatus());
            objectMap.put("deviceType",deviceInfo.getDeviceType());
            if(!StringUtils.isBlank(deviceInfo.getOwnCarRoad())){
                InOutCarRoadInfo carRoadInfo = (InOutCarRoadInfo)baseDao.getById(InOutCarRoadInfo.class,deviceInfo.getOwnCarRoad());
                if(!CommonUtils.isEmpty(carRoadInfo)){
                    objectMap.put("carRoadName",carRoadInfo.getCarRoadName());
                    objectMap.put("carRoadId",carRoadInfo.getCarRoadId());
                }else{
                    objectMap.put("carRoadName","未配置");
                    objectMap.put("carRoadId","");
                }
            }else{
                objectMap.put("carRoadId","");
                objectMap.put("carRoadName","未配置");
            }
            objectMap.put("deviceId",deviceInfo.getDeviceId());
            objectMap.put("subIpcId",deviceInfo.getSubIpcId());
            objectMap.put("relateIpcId",deviceInfo.getRelateIpcId());
            objectMap.put("relateTime",deviceInfo.getRelateTime());
            list.add(objectMap);
        }
        return list;
    }

    @Override
    public Long getCount() {
        String hql = "select count(*) from DeviceInfo where useMark >= 0 and deviceType = 0";
        return (Long)baseDao.getUnique(hql);
    }

    @Override
    public List getIPCSelect() {
        String hql = "from DeviceInfo where useMark >= 0 and deviceType = 0";
        List<DeviceInfo> deviceInfos = (List<DeviceInfo>)baseDao.queryForList(hql);
        List<Map<String,Object>> list = new ArrayList<>();
        for(DeviceInfo deviceInfo : deviceInfos){
            Map<String,Object> objectMap = new HashedMap();
            objectMap.put("name",deviceInfo.getDeviceName());
            objectMap.put("id",deviceInfo.getDeviceId());
            list.add(objectMap);
        }
        return list;
    }

    @Override
    public Map getDeviceInfo(String id) {
        
        DeviceInfo deviceInfo = (DeviceInfo) baseDao.getById(DeviceInfo.class,id);
        StarnetDeviceUtils starnetDeviceUtils = new StarnetDeviceUtils();
        JSONObject netParams = starnetDeviceUtils.getDeviceNetParams(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd());
        String deviceName = starnetDeviceUtils.getDeviceName(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd());
        JSONObject portParams = starnetDeviceUtils.getDevicePortInfo(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd());
        JSONObject sysParams = starnetDeviceUtils.getSysInfo(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd());


        JSONObject lightParams = starnetDeviceUtils.getLightInfo(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd());
        JSONObject osdParams = starnetDeviceUtils.getChannelNameAndTime(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd());
        JSONObject osdoverlayParams = starnetDeviceUtils.getOsdoverlay(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd());


        JSONObject secneInfo = starnetDeviceUtils.getSceneInfo(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd());
        JSONObject recoSceneInfo = starnetDeviceUtils.getRecoSceneInfo(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd());
        JSONObject gateMode = starnetDeviceUtils.getCarwhitelinkInfo(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd());

        String hql = "from DeviceInfo where parentDeviceId = ? and deviceType = ?";
        DeviceInfo ledDevInfo = (DeviceInfo)baseDao.getUnique(hql,id,"3");
        if (CommonUtils.isEmpty(ledDevInfo)) {
            ledDevInfo = (DeviceInfo) baseDao.getUnique(hql, id, "11");
            if (CommonUtils.isEmpty(ledDevInfo)) {
                ledDevInfo = (DeviceInfo) baseDao.getUnique(hql, id, "12");
            }

        }

        hql = "from DeviceInfo where parentDeviceId = ? and deviceType = ?";
        DeviceInfo audioDevInfo = (DeviceInfo)baseDao.getUnique(hql,id,"10");

        hql = "from DeviceInfo where parentDeviceId = ? and deviceType = ?";
        DeviceInfo gateDevInfo = (DeviceInfo)baseDao.getUnique(hql,id,"2");
//        sysParams.put("openMode",gateDevInfo.getUartDeviceAddr());
        gateMode.put("openMode",gateDevInfo.getUartDeviceAddr());

        Map<String,Object> data = new HashedMap();
        data.put("netParams",netParams);
        data.put("sysParams",sysParams);
        data.put("deviceName",deviceName);
        data.put("portParams",portParams);
        data.put("lightParams",lightParams);
        data.put("osdParams",osdParams);
        data.put("osdoverlayParams",osdoverlayParams);
        data.put("secneInfo",secneInfo);
        data.put("recoSceneInfo",recoSceneInfo);
        data.put("gateMode",gateMode);
        data.put("ledDevInfo",ledDevInfo);
        data.put("audioDevInfo",audioDevInfo);
        return data;
    }

    @Override
    public void deleteDeviceInfo(String id) {
        DeviceInfo deviceInfo = (DeviceInfo)baseDao.getById(DeviceInfo.class,id);
        if (CommonUtils.isEmpty(deviceInfo)){
            throw new BizException("删除设备失败：设备信息不存在");
        }
        baseDao.deleteById(DeviceInfo.class,id);
        String hql = "from DeviceInfo where parentDeviceId = ?";
        List<DeviceInfo> deviceInfoList = (List<DeviceInfo>)baseDao.queryForList(hql,id);
        if (deviceInfoList != null && deviceInfoList.size() > 0){
            for (DeviceInfo subDeviceInfo : deviceInfoList){
                baseDao.deleteById(DeviceInfo.class,subDeviceInfo.getDeviceId());
            }
        }

        hql = "from DeviceInfo where subIpcId = ?";
        List<DeviceInfo> mainDeviceInfoList = (List<DeviceInfo>)baseDao.queryForList(hql,id);
        if (mainDeviceInfoList != null && mainDeviceInfoList.size() > 0){
            for (DeviceInfo mainDeviceInfo : mainDeviceInfoList){
                mainDeviceInfo.setSubIpcId(null);
                baseDao.update(mainDeviceInfo);
            }
        }
    }

    @Override
    public void updateDeviceBasicData(JSONObject params) {
        String deviceId = params.getString("deviceId");
        String deviceName = params.getString("deviceName");
        DeviceInfo deviceInfo = (DeviceInfo)baseDao.getById(DeviceInfo.class,deviceId);
        deviceInfo.setDeviceName(deviceName);
        StarnetDeviceUtils starnetDeviceUtils = new StarnetDeviceUtils();
        starnetDeviceUtils.setDevicePortInfo(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd(),params);
        starnetDeviceUtils.setDeviceNetParams(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd(),params);
        starnetDeviceUtils.updateDeviceName(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd(),deviceName);

    }

    @Override
    public void checkSubIpc(String id,String subId) throws BizException {
        if(StringUtils.isBlank(subId)){
            return;
        }
        if(id.equals(subId)){
            throw new BizException("不能添加自身为附属设备");
        }

        String hql = "from DeviceInfo where subIpcId = ?";
        DeviceInfo deviceInfo = (DeviceInfo)baseDao.getUnique(hql,id);
        if(!CommonUtils.isEmpty(deviceInfo)){
            throw new BizException("当前设备已是附属设备，无法再添加附属设备");
        }
        hql = "from DeviceInfo where subIpcId = ? and deviceId != ?";
        DeviceInfo subDeviceInfo = (DeviceInfo)baseDao.getUnique(hql,subId,id);
        if(!CommonUtils.isEmpty(subDeviceInfo)){
            throw new BizException("所选择的附属设备已被设备[" + subDeviceInfo.getDeviceName() + "]添加为附属设备");
        }
        DeviceInfo sub = (DeviceInfo)baseDao.getById(DeviceInfo.class,subId);
        if(!StringUtils.isBlank(sub.getSubIpcId())){
            throw new BizException("所选择的附属设备已拥有附属设备");
        }
    }

    @Override
    public void checkRelateIpc(String id, String relateId) throws BizException {
        if(StringUtils.isBlank(relateId)){
            return;
        }
        if(id.equals(relateId)){
            throw new BizException("不能选择自身为关联设备");
        }

        /*String hql = "from DeviceInfo where relateIpcId = ?";
        DeviceInfo deviceInfo = (DeviceInfo)baseDao.getUnique(hql,id);
        if(!CommonUtils.isEmpty(deviceInfo)){
            throw new BizException("当前设备已被关联设备，无法再关联设备");
        }*/
        String hql = "from DeviceInfo where relateIpcId = ? and deviceId != ?";
        DeviceInfo subDeviceInfo = (DeviceInfo)baseDao.getUnique(hql,relateId,id);
        if(!CommonUtils.isEmpty(subDeviceInfo)){
            throw new BizException("所选择的设备已被设备[" + subDeviceInfo.getDeviceName() + "]关联");
        }
        /*DeviceInfo relateIpc = (DeviceInfo)baseDao.getById(DeviceInfo.class,relateId);
        if(!StringUtils.isBlank(relateIpc.getRelateIpcId())){
            throw new BizException("所选择的关联设备已拥有关联设备");
        }*/
    }

    @Override
    public void updateDeviceSystem(JSONObject params) throws BizException {
        String carRoadId = params.getString("carRoadId");
        InOutCarRoadInfo inOutCarRoadInfo = (InOutCarRoadInfo)baseDao.getById(InOutCarRoadInfo.class,carRoadId);
        if(CommonUtils.isEmpty(inOutCarRoadInfo)){
            throw new BizException("所属车道不能为空");
        }
        StarnetDeviceUtils starnetDeviceUtils = new StarnetDeviceUtils();
        String deviceId = params.getString("deviceId");
        DeviceInfo deviceInfo = (DeviceInfo)baseDao.getById(DeviceInfo.class,deviceId);
        String oldSubIpcId = deviceInfo.getSubIpcId();
        String relateId = params.getString("relateIpcId");
        String subId = params.getString("subIpcId");
        String relateTime = params.getString("associateIPC.landtimeout");
        String preSubId = deviceInfo.getSubIpcId();
        deviceInfo.setOwnCarRoad(carRoadId);
        deviceInfo.setRelateIpcId(relateId);
        deviceInfo.setSubIpcId(subId);
        deviceInfo.setRelateTime(relateTime);
        if (!subId.equals(oldSubIpcId)){
            if (!CommonUtils.isEmpty(oldSubIpcId)){
                DeviceInfo deviceInfoSub = (DeviceInfo)baseDao.getById(DeviceInfo.class,oldSubIpcId);
                deviceInfoSub.setDevStatus("0");
                baseDao.update(deviceInfoSub);
            }
        }
        for(int i = 0; i < DeviceSIPServer.getClientsCount(); i++){
            SocketClient socketClient = DeviceSIPServer.getClient(i);
            if(socketClient.getChannelId().equals(deviceInfo.getDeviceId())){
                if(!CommonUtils.isEmpty(inOutCarRoadInfo)){
                    PostComputerManage postComputerManage = (PostComputerManage)baseDao.getById(PostComputerManage.class,inOutCarRoadInfo.getManageComputerId());
                    socketClient.setPostComputerManage(postComputerManage);
                    socketClient.setDeviceInfo(deviceInfo);
                }
            }
        }


        //配置附属IPC开始
        if(StringUtils.isBlank(subId)){
            //取消附属IPC
            params.put("bEnableSlaveIPC",0);
            params.put("slaveIPC.addr","");
            params.put("slaveIPC.port","");
            if(!StringUtils.isBlank(preSubId)){
                //恢复附属IPC主从属性
                DeviceInfo subDev = (DeviceInfo)baseDao.getById(DeviceInfo.class,preSubId);
                JSONObject subSysParams = starnetDeviceUtils.getSysInfo(subDev.getDeviceIp(),Integer.parseInt(subDev.getDevicePort()),subDev.getDeviceUsername(),subDev.getDevicePwd());
                subSysParams.put("bSlave",0);
                starnetDeviceUtils.updateSysInfo(subDev.getDeviceIp(),Integer.parseInt(subDev.getDevicePort()),subDev.getDeviceUsername(),subDev.getDevicePwd(),subSysParams);
            }
        }else{
            //验证并设置附属IPC参数
            checkSubIpc(deviceId,subId);
            DeviceInfo subDev = (DeviceInfo)baseDao.getById(DeviceInfo.class,params.getString("subIpcId"));
            JSONObject subSysParams = starnetDeviceUtils.getSysInfo(subDev.getDeviceIp(),Integer.parseInt(subDev.getDevicePort()),subDev.getDeviceUsername(),subDev.getDevicePwd());
            subSysParams.put("bSlave",1);
            //设置附属IPC从属性
            starnetDeviceUtils.updateSysInfo(subDev.getDeviceIp(),Integer.parseInt(subDev.getDevicePort()),subDev.getDeviceUsername(),subDev.getDevicePwd(),subSysParams);
            params.put("bSlave",0);
            params.put("bEnableSlaveIPC",1);
            params.put("slaveIPC.addr",subDev.getDeviceIp());
            params.put("slaveIPC.port",10004);
        }
        //配置附属IPC结束
        //配置关联IPC开始
        if(StringUtils.isBlank(relateId)){
            params.put("bEnableAssociateIPC",0);
            params.put("associateIPC.addr","");
            params.put("associateIPC.port","");
        }else{
            checkRelateIpc(deviceId,relateId);
            DeviceInfo relateDev = (DeviceInfo)baseDao.getById(DeviceInfo.class,params.getString("relateIpcId"));
            relateDev.setRelateIpcId(deviceId);
            relateDev.setRelateTime(relateTime);
            baseDao.save(relateDev);
            //关联IPC两边都要配置管理
            JSONObject relateDevParams = starnetDeviceUtils.getSysInfo(relateDev.getDeviceIp(),Integer.parseInt(relateDev.getDevicePort()),relateDev.getDeviceUsername(),relateDev.getDevicePwd());
            relateDevParams.put("bEnableAssociateIPC",1);
            relateDevParams.put("associateIPC.addr",deviceInfo.getDeviceIp());
            relateDevParams.put("associateIPC.port",10004);
            relateDevParams.put("associateIPC.landtimeout",Integer.valueOf(relateTime));
            starnetDeviceUtils.updateSysInfo(relateDev.getDeviceIp(),Integer.parseInt(relateDev.getDevicePort()),relateDev.getDeviceUsername(),relateDev.getDevicePwd(),relateDevParams);

            params.put("bEnableAssociateIPC",1);
            params.put("associateIPC.addr",relateDev.getDeviceIp());
            params.put("associateIPC.port",10004);
            params.put("associateIPC.landtimeout",Integer.valueOf(relateTime));
        }
        //配置关联IPC结束

        if("0".equals(inOutCarRoadInfo.getCarRoadType())){
            params.put("direction",0);
        }else{
            params.put("direction",1);
        }

        starnetDeviceUtils.updateSysInfo(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd(),params);
        JSONObject whiteSet = starnetDeviceUtils.getWhiteSet(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd());


        //更新配置道闸控制信息
        String openMode = params.getString("openMode");
        String hql = "from DeviceInfo where parentDeviceId = ? and deviceType = ?";
        DeviceInfo gateDevInfo = (DeviceInfo)baseDao.getUnique(hql,deviceInfo.getDeviceId(),"2");
        gateDevInfo.setUartDeviceAddr(openMode);

        if("1".equals(openMode)){
            JSONObject gateMode = new JSONObject();
            gateMode.put("enabled","1");
            gateMode.put("openMode",openMode);
            gateMode.put("enabledCmd","1");
            gateMode.put("enabledTriger","1");
            gateMode.put("openDelay","0");
            gateMode.put("closeDelay","0");
            whiteSet.put("enabled",1);
            starnetDeviceUtils.updateCarwhitelinkInfo(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd(),gateMode);
            starnetDeviceUtils.updateWhiteSet(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd(),whiteSet);

        }else if("2".equals(openMode)){
            JSONObject gateMode = new JSONObject();
            gateMode.put("enabled","1");
            gateMode.put("openMode",openMode);
            gateMode.put("enabledCmd","1");
            gateMode.put("enabledTriger","1");
            gateMode.put("openDelay","0");
            gateMode.put("closeDelay","0");
            starnetDeviceUtils.updateCarwhitelinkInfo(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd(),gateMode);
        }else{
            JSONObject gateMode = new JSONObject();
            gateMode.put("enabled","1");
            gateMode.put("openMode","1");
            gateMode.put("enabledCmd","1");
            gateMode.put("enabledTriger","1");
            gateMode.put("openDelay","0");
            gateMode.put("closeDelay","0");
            starnetDeviceUtils.updateCarwhitelinkInfo(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd(),gateMode);
            whiteSet.put("enabled",0);
            starnetDeviceUtils.updateWhiteSet(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd(),whiteSet);
        }
    }

    @Override
    public void updateLightAndOsd(JSONObject params) throws BizException {
        StarnetDeviceUtils starnetDeviceUtils = new StarnetDeviceUtils();
        String deviceId = params.getString("deviceId");
        DeviceInfo deviceInfo = (DeviceInfo)baseDao.getById(DeviceInfo.class,deviceId);

        starnetDeviceUtils.setLightInfo(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd(),params);

        params.put("channelname",deviceInfo.getDeviceName());
        params.put("nameposition_x",21);
        params.put("nameposition_y",61);
        params.put("timeposition_x",21);
        params.put("timeposition_y",19);
        params.put("fontsize",0);

        starnetDeviceUtils.updateChannelNameAndTime(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd(),params);

        params.put("chanoverlay_x5",1800);
        params.put("chanoverlay_y5",1);
        params.put("chanoverlaytype5",1);
        params.put("chanoverlay_x1","0");
        params.put("chanoverlay_x2","0");
        params.put("chanoverlay_x3","0");
        params.put("chanoverlay_x4","0");
        params.put("chanoverlay_y1","0");
        params.put("chanoverlay_y2","0");
        params.put("chanoverlay_y3","0");
        params.put("chanoverlay_y4","0");
        params.put("chanoverlaydsp1","0");
        params.put("chanoverlaydsp2","0");
        params.put("chanoverlaydsp3","0");
        params.put("chanoverlaydsp4","0");
        params.put("chanoverlaystr1","");
        params.put("chanoverlaystr2","");
        params.put("chanoverlaystr3","");
        params.put("chanoverlaystr4","");
        params.put("chanoverlaystr5","");
        params.put("chanoverlaytype1","0");
        params.put("chanoverlaytype2","0");
        params.put("chanoverlaytype3","0");
        params.put("chanoverlaytype4","0");
        params.put("platecolor1","1");
        params.put("platecolor2","1");
        params.put("platecolor3","1");
        params.put("platecolor4","1");
        params.put("platecolor5","1");
        params.put("platetime1","0");
        params.put("platetime2","0");
        params.put("platetime3","0");
        params.put("platetime4","0");
        params.put("platetime5","0");


        starnetDeviceUtils.updateOsdoverlay(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd(),params);

    }

    @Override
    public void updateScene(JSONObject params) throws BizException {
        StarnetDeviceUtils starnetDeviceUtils = new StarnetDeviceUtils();
        String deviceId = params.getString("deviceId");
        DeviceInfo deviceInfo = (DeviceInfo)baseDao.getById(DeviceInfo.class,deviceId);
        starnetDeviceUtils.updateSceneInfo(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd(),params);
        starnetDeviceUtils.updateRecoSceneInfo(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd(),params);
    }

    @Override
    public void rebootDevice(String id) throws BizException {
        StarnetDeviceUtils starnetDeviceUtils = new StarnetDeviceUtils();
        DeviceInfo deviceInfo = (DeviceInfo)baseDao.getById(DeviceInfo.class,id);
        starnetDeviceUtils.reboot(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd());
    }

    @Override
    public void formatSDCard(String id) throws BizException {
        StarnetDeviceUtils starnetDeviceUtils = new StarnetDeviceUtils();
        DeviceInfo deviceInfo = (DeviceInfo)baseDao.getById(DeviceInfo.class,id);
        starnetDeviceUtils.formatSD(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd());

    }

    @Override
    public List getLedList(String devId) throws BizException {
        String hql = "from DeviceInfo where parentDeviceId = ? and deviceType = ?";
        DeviceInfo deviceInfo = (DeviceInfo)baseDao.getUnique(hql,devId,"3");
        if (CommonUtils.isEmpty(deviceInfo)) {
            deviceInfo = (DeviceInfo) baseDao.getUnique(hql, devId, "11");
            if (CommonUtils.isEmpty(deviceInfo)){
                deviceInfo = (DeviceInfo) baseDao.getUnique(hql, devId, "12");
            }
        }
        hql = "from LedDisplayConfig where devId = ?";
        return baseDao.queryForList(hql,deviceInfo.getDeviceId());
    }

    @Override
    public void updateLed(JSONObject params) throws BizException {
        JSONObject channelSet = params.getJSONObject("channelSet");
        DeviceInfo ledDevice = (DeviceInfo)baseDao.getById(DeviceInfo.class,channelSet.getString("devId"));
        if(!CommonUtils.isEmpty(ledDevice)){
            ledDevice.setDeviceChannelNo(channelSet.getString("deviceChannelNo"));
            String voiceChannel = channelSet.getString("voiceChannel");
            ledDevice.setVoiceChannel(Integer.parseInt(voiceChannel));
            String ledPlayType = channelSet.getString("ledPlayType");
            ledDevice.setDeviceType(ledPlayType);
            baseDao.update(ledDevice);
        }

        if (!("12".equals(channelSet.getString("ledPlayType")))) {
            JSONArray ledList = params.getJSONArray("list");
            for (Object led : ledList) {
                JSONObject json = (JSONObject) led;
                String configId = json.getString("id");
                LedDisplayConfig ledDisplayConfig = (LedDisplayConfig) baseDao.getById(LedDisplayConfig.class, configId);
                if (!CommonUtils.isEmpty(ledDisplayConfig)) {

                    ledDisplayConfig.setTopRowContent(json.getString("topRowContent"));
                    ledDisplayConfig.setMiddleRowContent(json.getString("middleRowContent"));
                    ledDisplayConfig.setButtomRowContent(json.getString("buttomRowContent"));
                    ledDisplayConfig.setFourthRowContent(json.getString("fourthRowContent"));
                    //ledDisplayConfig.setMovingDirection(json.getString("movingDirection"));
                    //ledDisplayConfig.setMovementSpeed(json.getString("movementSpeed"));
                    ledDisplayConfig.setMovingDirection("000");
                    ledDisplayConfig.setMovementSpeed("000");
                    ledDisplayConfig.setVoiceBroadcast(json.getString("voiceBroadcast"));
                    String voiceVolume = (String) json.get("voiceBroadcastVolume");
                    if (CommonUtils.isEmpty(voiceVolume) || "null".equals(voiceVolume) || "underfined".equals(voiceVolume)) {
                        ledDisplayConfig.setVoiceBroadcastVolume(7);
                    } else {
                        ledDisplayConfig.setVoiceBroadcastVolume(Integer.parseInt(json.getString("voiceBroadcastVolume")));
                    }
                    baseDao.update(ledDisplayConfig);
                }

            }
        }

        if ("3".equals(channelSet.getString("ledPlayType"))){
            DeviceInfo deviceInfo = (DeviceInfo) baseDao.getById(DeviceInfo.class,ledDevice.getParentDeviceId());
            StarnetDeviceUtils starnetDeviceUtils = new StarnetDeviceUtils();
            JSONObject paramsLed = new JSONObject();
            paramsLed.put("uartID",Integer.valueOf(channelSet.getString("deviceChannelNo")));
            paramsLed.put("uart.baudrate",6);
            paramsLed.put("uart.databit",8);
            paramsLed.put("uart.stopbit",1);
            paramsLed.put("uart.checkbit",2);
            paramsLed.put("uart.streamcontrol",0);
            paramsLed.put("uart.decompilertype",0);
            paramsLed.put("uart.decompileraddr",0);
            starnetDeviceUtils.setFourEightFiveInfo(deviceInfo.getDeviceIp(),Integer.valueOf(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),
                    deviceInfo.getDevicePwd(),paramsLed);
        }else {
            DeviceInfo deviceInfo = (DeviceInfo) baseDao.getById(DeviceInfo.class,ledDevice.getParentDeviceId());
            StarnetDeviceUtils starnetDeviceUtils = new StarnetDeviceUtils();
            JSONObject paramsLed = new JSONObject();
            paramsLed.put("uartID",Integer.valueOf(channelSet.getString("deviceChannelNo")));
            paramsLed.put("uart.baudrate",6);
            paramsLed.put("uart.databit",8);
            paramsLed.put("uart.stopbit",1);
            paramsLed.put("uart.checkbit",2);
            paramsLed.put("uart.streamcontrol",0);
            paramsLed.put("uart.decompilertype",0);
            paramsLed.put("uart.decompileraddr",0);
            starnetDeviceUtils.setFourEightFiveInfo(deviceInfo.getDeviceIp(),Integer.valueOf(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),
                    deviceInfo.getDevicePwd(),paramsLed);
        }

    }

    @Override
    public List getAudioList(String devId) throws BizException {
        String hql = "from DeviceInfo where parentDeviceId = ? and deviceType = ?";
        DeviceInfo deviceInfo = (DeviceInfo)baseDao.getUnique(hql,devId,"10");
        hql = "from LedDisplayConfig where devId = ?";
        return baseDao.queryForList(hql,deviceInfo.getDeviceId());
    }

    @Override
    public void updateAudio(JSONObject params) throws BizException {
        JSONObject channelSet = params.getJSONObject("channelSet");
        DeviceInfo audioDevice = (DeviceInfo)baseDao.getById(DeviceInfo.class,channelSet.getString("devId"));
        if(!CommonUtils.isEmpty(audioDevice)){
            audioDevice.setDeviceChannelNo(channelSet.getString("deviceChannelNo"));
            String voiceChannel = channelSet.getString("voiceChannel");
            audioDevice.setVoiceChannel(Integer.parseInt(voiceChannel));
            baseDao.update(audioDevice);
        }

        JSONArray ledList = params.getJSONArray("list");
        for(Object led : ledList){
            JSONObject json = (JSONObject)led;
            LedDisplayConfig ledDisplayConfig = (LedDisplayConfig)baseDao.getById(LedDisplayConfig.class,json.getString("id"));
            if(!CommonUtils.isEmpty(ledDisplayConfig)){

                ledDisplayConfig.setVoiceBroadcast(json.getString("voiceBroadcast"));
                ledDisplayConfig.setVoiceBroadcastVolume(Integer.parseInt(json.getString("voiceBroadcastVolume")));
                baseDao.update(ledDisplayConfig);
            }

        }
    }

    @Override
    public List getIpcInfoList() throws BizException {
        String hql = "from DeviceInfo where useMark >= 0 and deviceType = 0";
        List<DeviceInfo> deviceInfos = (List<DeviceInfo>)baseDao.queryForList(hql);
        return deviceInfos;
    }

    @Override
    public List getIpcInfoList(String parkId) throws BizException {
        String hql = "select carRoadId from InOutCarRoadInfo where ownCarparkNo = ?";
        List<String> ownCarRoads = (List<String>)baseDao.queryForList(hql,parkId);
        if(CollectionUtils.isEmpty(ownCarRoads)){
            return Collections.EMPTY_LIST;
        }
        hql = "from DeviceInfo where useMark >= 0 and deviceType = '0' and ownCarRoad in (:ownCarRoads) ";
        Query query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
        query.setParameterList("ownCarRoads",ownCarRoads);
        return query.list();
    }

    @Override
    public void formatDeviceTime() throws BizException {
        Timestamp now = CommonUtils.getTimestamp();
        String hql = "from DeviceInfo where useMark >= 0 and deviceType = 0";
        List<DeviceInfo> deviceInfos = (List<DeviceInfo>)baseDao.queryForList(hql);
        DeviceManageUtils deviceManageUtils = new StarnetDeviceUtils();
        for(DeviceInfo deviceInfo : deviceInfos){
            JSONObject params = new JSONObject();
            params.put("manual.year", DateUtils.getYear(now));
            params.put("manual.month", DateUtils.getMonth(now));
            params.put("manual.day", DateUtils.getDay(now));
            params.put("manual.hour", DateUtils.getHour(now));
            params.put("manual.minute", DateUtils.getMinute(now));
            params.put("manual.second", DateUtils.getSecond(now));
            deviceManageUtils.updateDeviceTimeSet(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd(),params);
        }
    }

    @Override
    public void updateDeviceSystem(String deviceId, MultipartFile file) {
        DeviceInfo deviceInfo = (DeviceInfo)baseDao.getById(DeviceInfo.class,deviceId);
        DeviceManageUtils deviceManageUtils = new StarnetDeviceUtils();
        deviceManageUtils.updateDeviceSystem(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd(),file);
    }

    @Override
    public void testLed(JSONObject params) {
        String devId = params.getString("devId");
        String ledId = params.getString("id");
        String ledType = params.getString("ledType");
        LedDisplayConfig ledDisplayConfig = (LedDisplayConfig)baseDao.getById(LedDisplayConfig.class,ledId);
        DeviceInfo ledDevice = (DeviceInfo)baseDao.getById(DeviceInfo.class,devId);
        DeviceInfo deviceInfo = (DeviceInfo)baseDao.getById(DeviceInfo.class,ledDevice.getParentDeviceId());

        InOutCarRoadInfo inOutCarRoadInfo = (InOutCarRoadInfo)baseDao.getById(InOutCarRoadInfo.class,deviceInfo.getOwnCarRoad());
        Integer inoutType = 0;
        if(!CommonUtils.isEmpty(inOutCarRoadInfo)){
            inoutType = Integer.parseInt(inOutCarRoadInfo.getCarRoadType());
        }
        JSONObject playParams = new JSONObject();
        playParams.put("availableCarSpace",1000);
        playParams.put("carPlate","闽A12345");
        playParams.put("memberLeft","月租车剩余1天");
        playParams.put("parkingLength","停车6时6分");
        playParams.put("kindName","测试车场外部车收费类型");
        playParams.put("chargeAmount","0");
        playParams.put("carparkName","测试车场");
        playParams.put("limitReason","临时车限制通行");
        playParams.put("voiceBroadcastVolume",params.getInteger("voiceBroadcastVolume"));
        deviceBizService.PlayLedInfo(deviceInfo.getDeviceId(),inoutType,playParams,ledDisplayConfig,ledType,devId);

        String data = "0064FFFF300901BBB6D3ADB9E2C1D93258";
        //AGT//zAJAbu206254sHZMlg=

    }

    @Override
    public void testAudio(JSONObject params) {

    }



}
