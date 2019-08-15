package com.starnetsecurity.parkClientServer.controller;


import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.controller.BaseController;
import com.starnetsecurity.common.util.Constant;
import com.starnetsecurity.parkClientServer.entity.DeviceInfo;
import com.starnetsecurity.parkClientServer.service.CarparkService;
import com.starnetsecurity.parkClientServer.entity.AdminUser;
import com.starnetsecurity.parkClientServer.service.DeviceManageService;
import com.starnetsecurity.parkClientServer.service.SystemService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 宏炜 on 2016-12-22.
 */
@Controller
@RequestMapping("system")
public class SystemController extends BaseController {

    @Autowired
    CarparkService carparkService;


    @Autowired
    SystemService systemService;

    @RequestMapping("index")
    public String index(ModelMap modelMap) {
        AdminUser adminUser = (AdminUser)getUser();

        List<Map<String, Object>> parkids= carparkService.getParkId(adminUser);
        String parkId = null;
        String parkName=null;
        String departmentId=null;
        for(Map<String, Object> ss:parkids){
            parkId= (String) ss.get("res_id");
            parkName=(String) ss.get("carpark_name");
            departmentId=(String) ss.get("department_id");
        }
        modelMap.put("departmentId", departmentId);
        modelMap.put("parkName", parkName);
        modelMap.put("parkId", parkId);
        if(Constant.getPropertie("isUseBasicPlatform").equals("1")){
            modelMap.put("isUseBasicPlatform","1");
        }
        modelMap.put("basicAddress",Constant.getPropertie("SYNC.BASIC_SERVER_IP") + ":" + Constant.getPropertie("SYNC.BASIC_SERVER_PORT"));

        return "index";
    }

    @RequestMapping("info/set")
    public String systemSet(ModelMap modelMap){
        Map sysInfo = systemService.getSystemInfo();
        modelMap.put("sysInfo",sysInfo);
        return "admin/systemSet";
    }

    @ResponseBody
    @RequestMapping(value = "info/update",method = RequestMethod.POST)
    public Map systemUpdate(@RequestBody JSONObject params){
        Map res = new HashedMap();
        systemService.updateSystemInfo(params);
        this.success(res);
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "info/getMemberInfo",method = RequestMethod.POST)
    public Map<String,Object> GetMemberInfo(){
        Map<String,Object> res = new HashedMap();
        List<Map<String,Object>> list = new ArrayList<>();
        list = systemService.getMemberInfo();
        res.put("data",list);
        this.success(res);
        return res;
    }

}
