package com.starnetsecurity.parkClientServer.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.controller.BaseController;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.parkClientServer.service.CarRoadInfoService;
import com.starnetsecurity.parkClientServer.service.DeviceManageService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Created by 宏炜 on 2017-12-29.
 */
@Controller
@RequestMapping("/device/manage")
public class DeviceManageController extends BaseController {

    @Autowired
    DeviceManageService deviceManageService;

    @Autowired
    CarRoadInfoService carRoadInfoService;

    @RequestMapping("/index")
    public String index(ModelMap modelMap){
        List carRaods = carRoadInfoService.getCarRoadSelect();
        List ipcDevs = deviceManageService.getIPCSelect();
        modelMap.put("carRoads",carRaods);
        modelMap.put("ipcDevs",ipcDevs);
        return "admin/deviceManage";
    }

    @ResponseBody
    @RequestMapping("/page/list")
    public Map list(@RequestParam(value="start", required=false,defaultValue = "0") Integer start,
                    @RequestParam(value="length", required=false,defaultValue = "10") Integer length,
                    @RequestParam(value="draw", required=false) Integer draw){
        Map<String,Object> res = new HashedMap();
        try{
            List list = deviceManageService.getPageList(length,start);
            Long total = deviceManageService.getCount();
            res.put("data",list);
            res.put("draw",draw);
            res.put("recordsTotal", total);
            res.put("recordsFiltered", total);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping("/device/info")
    public Map list(String id){
        Map<String,Object> res = new HashedMap();
        try{
            Map data = deviceManageService.getDeviceInfo(id);
            res.put("data",data);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping("/update/basic")
    public Map updateDeviceBasic(@RequestBody JSONObject params){
        Map<String,Object> res = new HashedMap();
        try{
            deviceManageService.updateDeviceBasicData(params);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping("/update/system")
    public Map updateDeviceSystem(@RequestBody JSONObject params){
        Map<String,Object> res = new HashedMap();
        try{
            deviceManageService.updateDeviceSystem(params);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping("/update/light")
    public Map updateDeviceLight(@RequestBody JSONObject params){
        Map<String,Object> res = new HashedMap();
        try{
            deviceManageService.updateLightAndOsd(params);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping("/update/scene")
    public Map updateDeviceScene(@RequestBody JSONObject params){
        Map<String,Object> res = new HashedMap();
        try{
            deviceManageService.updateScene(params);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping("/reboot")
    public Map reboot(String devId){
        Map<String,Object> res = new HashedMap();
        try{
            deviceManageService.rebootDevice(devId);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping("/sdcard/format")
    public Map sdcardFormat(String devId){
        Map<String,Object> res = new HashedMap();
        try{
            deviceManageService.formatSDCard(devId);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping("/check/subIpc")
    public Map checSubIpc(String id,String subId){
        Map<String,Object> res = new HashedMap();
        try{
            deviceManageService.checkSubIpc(id,subId);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }


    @ResponseBody
    @RequestMapping("/check/relateIpc")
    public Map checRelateIpc(String id,String relateId){
        Map<String,Object> res = new HashedMap();
        try{
            deviceManageService.checkRelateIpc(id,relateId);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping("/led/list")
    public Map ledList(String devId){
        Map<String,Object> res = new HashedMap();
        try{
            List list = deviceManageService.getLedList(devId);
            res.put("data",list);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "/led/update",method = RequestMethod.POST)
    public Map ledUpdate(@RequestBody JSONObject params){
        Map<String,Object> res = new HashedMap();
        try{
            deviceManageService.updateLed(params);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "/audio/list",method = RequestMethod.POST)
    public Map audioList(String devId){
        Map<String,Object> res = new HashedMap();
        try{
            List list = deviceManageService.getAudioList(devId);
            res.put("data",list);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "/audio/update",method = RequestMethod.POST)
    public Map audioUpdate(@RequestBody JSONObject params){
        Map<String,Object> res = new HashedMap();
        try{
            deviceManageService.updateAudio(params);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "/park/device/list",method = RequestMethod.POST)
    public Map parkDeviceList(@RequestParam(value = "parkId") String parkId){
        Map<String,Object> res = new HashedMap();
        try{
            List data = deviceManageService.getIpcInfoList(parkId);
            res.put("data",data);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "/update/time",method = RequestMethod.POST)
    public Map updateDeviceTime(){
        Map<String,Object> res = new HashedMap();
        try{
            deviceManageService.formatDeviceTime();
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "/update/file/sys",method = RequestMethod.POST)
    public String updateDeviceSystem(@RequestParam(value = "sysDevId", required = true) String deviceId,
                                  @RequestParam(value = "devSoftware", required = true) MultipartFile file){
        Map<String,Object> res = new HashedMap();
        try{
            if (file.getSize() != 0) {
                deviceManageService.updateDeviceSystem(deviceId, file);
                this.success(res);
            }else
                this.failed(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }catch (IllegalStateException ex){
            this.failed(res);
        }
        return JSON.toJSONString(res);
    }

    @ResponseBody
    @RequestMapping(value = "/test/led",method = RequestMethod.POST)
    public String testLed(@RequestBody JSONObject params){
        Map<String,Object> res = new HashedMap();
        try{
            deviceManageService.testLed(params);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }catch (IllegalStateException ex){
            this.success(res);
        }
        return JSON.toJSONString(res);
    }
}


