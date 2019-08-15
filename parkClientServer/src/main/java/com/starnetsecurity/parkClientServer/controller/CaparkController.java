package com.starnetsecurity.parkClientServer.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.controller.BaseController;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.util.DateUtils;
import com.starnetsecurity.common.web.MediaTypes;
import com.starnetsecurity.parkClientServer.entity.*;
import com.starnetsecurity.parkClientServer.service.CarparkService;
import com.starnetsecurity.parkClientServer.service.MemberKindService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by fuhh on 2016-10-16.
 */
@Controller
@RequestMapping("carpark")
public class CaparkController extends BaseController {

    @Autowired
    CarparkService carparkService;

    @Autowired
    MemberKindService memberKindService;



    //获取车场
    @ResponseBody
    @RequestMapping(value = "getPark",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map getParks(ModelMap modelMap, HttpSession session) throws NoSuchFieldException {
        AdminUser adminUser = (AdminUser)getUser();
        String userId=adminUser.getId();
        Map<String,Object> res = new HashedMap();
        try{
            List data= carparkService.getCaparks(userId);
            res.put("data",data);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    //单个停车场的信息
    //剩余车位从这取
    @ResponseBody
    @RequestMapping(value = "getRemainPark" , method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public String getRemainPark(ModelMap modelMap, @RequestParam(value = "parkId", required = true) String parkId) throws NoSuchFieldException {
        CarparkInfo carparkInfo = carparkService.getRemainPark(parkId);
        modelMap.put("data", carparkInfo);
        this.success(modelMap);
        return JSON.toJSONString(modelMap);
    }

    @ResponseBody
    @RequestMapping(value = "get7DayInCount" , method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public String get7DayInCount(ModelMap modelMap, @RequestParam(value = "parkId", required = true) String parkId) throws NoSuchFieldException {
        Integer count = carparkService.get7DayInCount(parkId);
        modelMap.put("data", count);
        this.success(modelMap);
        return JSON.toJSONString(modelMap);
    }

    /**
     * 获取停车场地图信息
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getParksMapInfoList", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public String getParksMapInfoList(){
        AdminUser adminUser = (AdminUser)getUser();
        List list = carparkService.getCaparks(adminUser.getId());
//        List resList = parksService.getParksMapInfoList(list);
        Map<String,Object> res = new HashedMap();
        res.put("data",list);
        this.success(res);
        return JSON.toJSONString(res);
    }

    //近七天剩余停车位记录
    @ResponseBody
    @RequestMapping(value = "getparkRecords" , method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public String getparkRecords(ModelMap modelMap,  @RequestParam(value = "parkId", required = true) String parkId) throws NoSuchFieldException {
        List<Object[]> parkRecords= carparkService.getRemainParkhistory(parkId);

        List<Object[]> data = new ArrayList<>();
        Date now = new Date();
        for(int i = 6; i >= 0; i--){
            int j = -i;
            Date tmp = DateUtils.addDay(now,j);
            boolean isExist = false;
            Object[] findObjects = null;
            for(Object[] objects : parkRecords){
                if(DateUtils.formatDate(tmp).equals(String.valueOf(objects[0]))){
                    isExist = true;
                    findObjects = objects;
                }
            }
            if(!isExist){
                Object[] objects = new Object[2];
                objects[0] = DateUtils.formatDate(tmp);
                objects[1] = 0;
                data.add(objects);
            }else{
                data.add(findObjects);
            }
        }

        modelMap.put("data", data);
        this.success(modelMap);
        return JSON.toJSONString(modelMap);
    }

    @ResponseBody
    @RequestMapping(value = "getCarParksInfoList", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map getCarParksInfoList(@RequestParam(value="start", required=false,defaultValue = "1") Integer start,
                                   @RequestParam(value="length", required=false,defaultValue = "10") Integer length,
                                   @RequestParam(value="draw", required=true) Integer draw){
        Map<String, Object> res = new HashedMap();
        AdminUser adminUser = (AdminUser)getUser();
        List<Map<String,Object>> caparksOld = carparkService.getCaparks(adminUser.getId());
        List<CarparkInfo> caparks=new ArrayList<>();
        if(!CommonUtils.isEmpty(caparksOld)){
            for(   Map<String,Object> ss :caparksOld){
                CarparkInfo carparkInfo =new CarparkInfo();
                carparkInfo.setCarparkId((String) ss.get("carparkId"));
                Timestamp now = new Timestamp(System.currentTimeMillis());
                carparkInfo.setAddTime(now);
                caparks.add(carparkInfo);
            }
        }
        List list = carparkService.getCarParksInfoList(caparks,start,length);
        Long total = carparkService.countCarParksInfo(caparks);
        res.put("data", list);
        res.put("draw", draw);
        res.put("recordsTotal", total);
        res.put("recordsFiltered", total);
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "updateCarparkInfo", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map updateCarparkInfo(@RequestBody Map params, HttpServletRequest request){

        Map<String,Object> res = new HashMap<>();
        try {
            //修改云平台的车场信息
            String carparkId = (String) params.get("carparkId");
            CarparkInfo carks =carparkService.getRemainPark(carparkId);
            carks.setCarparkName(params.get("carparkName").toString());
            Integer preAvaliable = carks.getAvailableCarSpace();
            if (!(preAvaliable.equals(Integer.valueOf(params.get("availableCarSpace").toString())))){   //更新了剩余车位
                carks.setCarparkNo(Integer.valueOf(params.get("availableCarSpace").toString()));
            }
            carks.setTotalCarSpace(Integer.valueOf(params.get("totalCarSpace").toString()));
            carks.setAvailableCarSpace(Integer.valueOf(params.get("availableCarSpace").toString()));
            carks.setPassTimeWhenBig(params.get("passTimeWhenBig").toString());
            if(params.get("isTestRunning").toString().equals("yes")){
                carks.setIsTestRunning("1");
            }else {
                carks.setIsTestRunning("0");
            }
            if(params.get("isOverdueAutoOpen").toString().equals("yes")){
                carks.setIsOverdueAutoOpen("1");
            }else {
                carks.setIsOverdueAutoOpen("0");
            }
            carks.setIsClose(params.get("isClose").toString());
            if(params.get("isClose").toString().equals("yes")){
                carks.setIsClose("1");
            }else {
                carks.setIsClose("0");
            }
            if(params.get("closeType").toString().equals("yes")){
                carks.setCloseType("0");
            }else {
                carks.setCloseType("1");
            }

            if(params.get("ifIncludeCaculate").toString().equals("yes")){
                carks.setIfIncludeCaculate(1);
            }else {
                carks.setIfIncludeCaculate(0);
            }

            if(params.get("isAutoOpen").toString().equals("yes")){
                carks.setIsAutoOpen("0");
            }else {
                carks.setIsAutoOpen("1");
            }
            if (!StringUtils.isBlank(params.get("criticalValue").toString()))
                carks.setCriticalValue(Integer.valueOf(params.get("criticalValue").toString()));
            carks.setLedMemberCriticalValue(Integer.valueOf(params.get("ledMemberCriticalValue").toString()));
            if (!StringUtils.isBlank(params.get("lat").toString()))
                carks.setLat(new BigDecimal(params.get("lat").toString()));
            if (!StringUtils.isBlank(params.get("lon").toString()))
                carks.setLon(new BigDecimal(params.get("lon").toString()));

            String ip = CommonUtils.getRuestIpAddress(request);

            carparkService.saveParks(carks,ip,(AdminUser)getUser());
            this.success(res);
        } catch (BizException e) {
            this.failed(res, e);
        }
        return res;
    }


    /**
     * 获取停车场信息
     * @param carParkId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getCarParksInfo", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public String getCarParksInfo(@RequestParam(value="carParkId", required=true) String carParkId){
        Map<String, Object> res = new HashedMap();
        Map carParksInfo = carparkService.getCarParksInfo(carParkId);
        res.put("data", carParksInfo);
        this.success(res);
        return JSON.toJSONString(res);
    }

    @ResponseBody
    @RequestMapping(value = "/last/inout/record",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map getLastInOutRecord(String carparkId){
        Map<String,Object> res = new HashedMap();
        List data = carparkService.getLastCarparkInOutRecord(carparkId);
        res.put("data",data);
        this.success(res);
        return res;
    }

    /*
    *
    * 获取用户停车场单位的接口
    *
    * */
    @ResponseBody
    @RequestMapping(value = "getUserDepartments", method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map getUserDepartments(ModelMap modelMap) throws NoSuchFieldException {
        AdminUser adminUser = (AdminUser)getUser();
        String userId= adminUser.getId();
        Map ret = new HashedMap();
        try{
            List<Map<String, Object>> departments= carparkService.getUserDepartments(userId);
            ret.put("data", departments);
            this.success(ret);
        }catch (BizException e){
            this.failed(ret, e);
        }
        return ret;
    }

    /*
    *
    * 获取用户停车场所属车场的接口
    *
    * */
    @ResponseBody
    @RequestMapping(value = "getOwnCarparkInfo", method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map getOwnCarparkInfo(ModelMap modelMap) throws NoSuchFieldException {
        AdminUser adminUser = (AdminUser)getUser();
        String userId= adminUser.getId();
        Map ret = new HashedMap();
        try{
            List<Map<String, Object>> carparkInfo= carparkService.getOwnCarparkInfo(userId);
            ret.put("data", carparkInfo);
            this.success(ret);
        }catch (BizException e){
            this.failed(ret, e);
        }
        return ret;
    }


    /*
*
* 获取用户停车场单位下的停车场
*
* */
    @ResponseBody
    @RequestMapping(value = "getUserDepartmentsParks",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map getUserDepartmentsParks(ModelMap modelMap,
                                       @RequestParam(value = "departmentId", required = true) String departmentId  ) throws NoSuchFieldException {
        Map ret = new HashedMap();
        AdminUser adminUser = (AdminUser)getUser();
        String userId= adminUser.getId();
        try{
            List<Map<String, Object>> parks= carparkService.getUserDepartmentsParks(userId,departmentId);
            ret.put("data", parks);
            this.success(ret);
        }catch (BizException e){
            this.failed(ret, e);
        }
        return ret;
    }

    /*
     *保存车场信息
     *controlMode:add-新增车场；edit-修改车场
     */

    @ResponseBody
    @RequestMapping(value = "saveCarparkInfo",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map saveCarparkInfo(@RequestBody Map params,HttpServletRequest request){
        Map<String,Object> res = new HashMap<>();
        try {
            carparkService.insertCarparkInfo(params,(AdminUser)getUser(),CommonUtils.getRuestIpAddress(request));
            this.success(res);
        } catch (BizException e) {
            this.failed(res, e);
        }
        return res;
    }

    /*
     *判断车场名称是否重复
     *controlMode:add-新增车场；edit-修改车场
     */
    @ResponseBody
    @RequestMapping(value = "isCarparkNameRepeat",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public boolean isCarparkNameRepeat(@RequestBody Map params){
        boolean res = false;
        res = carparkService.isCarparkNameRepeat(params.get("controlMode").toString(),
                "",
                params.get("carparkId").toString(),
                params.get("carparkName").toString()
        );
        return res;
    }

    /*
     *删除车场信息
     *车场信息的删除要一起删除车道及设备信息
     */
    @ResponseBody
    @RequestMapping(value = "deleteCarParksInfo",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map deleteCarParksInfo(@RequestParam(value = "carParkId",required = true) String id,HttpServletRequest request){
        Map<String,Object> res = new HashMap<>();
        try {
            carparkService.deleteCarparkInfo(id,(AdminUser)getUser(),CommonUtils.getRuestIpAddress(request));
            this.success(res);
        }catch (BizException e) {
            this.failed(res, e);
        }
        return res;
    }

//获取所有创建的车场信息
@ResponseBody
@RequestMapping(value = "getAllCarParksInfo",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
public Map getAllCarParksInfo(){
    Map res = new HashedMap();
    try {
        List list=new ArrayList();
        list=carparkService.getAllCarParksInfo();
        res.put("data",list);
        this.success(res);
    }catch (BizException e) {
        this.failed(res, e);
    }
    return res;
}

//获取所有创建的车场信息
@ResponseBody
@RequestMapping(value = "getAllPost",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
public Map getAllPost(){
    Map res = new HashedMap();
    try {
        List list=new ArrayList();
        list=carparkService.getAllPostInfo();
        res.put("data",list);
        this.success(res);
    }catch (BizException e) {
        this.failed(res, e);
    }
    return res;
}

//保存车道信息
@ResponseBody
@RequestMapping(value = "saveCarRoad",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
public Map saveCarRoad(@RequestBody Map params,HttpServletRequest request){
    Map<String,Object> res = new HashMap<>();
    try {
        carparkService.saveCarRoad(params,(AdminUser)getUser(),CommonUtils.getRuestIpAddress(request));
        this.success(res);
    } catch (BizException e) {
        this.failed(res, e);
    }
    return res;
}


//    判断同一个车场下车道名是否相同
    @ResponseBody
    @RequestMapping(value = "isCarRoadNameRepeat",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public boolean isCarRoadNameRepeat(@RequestBody Map params){
        boolean res = false;
        res=carparkService.isisCarRoadNameRepeat(params);
        return res;
    }


//    获取车道表格信息
    @ResponseBody
    @RequestMapping(value = "getCarRoadInfoList", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map getCarRoadInfoList(@RequestParam(value="start", required=false,defaultValue = "1") Integer start,
                                   @RequestParam(value="length", required=false,defaultValue = "10") Integer length,
                                   @RequestParam(value="draw", required=true) Integer draw){
        Map<String, Object> res = new HashedMap();
        AdminUser adminUser = (AdminUser)getUser();
        List<Map<String,Object>> caparksOld = carparkService.getCaparks(adminUser.getId());
        List list=carparkService.getCarRoadInfoList(caparksOld,start,length);
        Long total = carparkService.countCarRoadInfoList(caparksOld);
        res.put("data", list);
        res.put("draw", draw);
        res.put("recordsTotal", total);
        res.put("recordsFiltered", total);
        return res;
    }

    //车道信息删除
    @ResponseBody
    @RequestMapping(value = "deleteCarRoadInfo",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map deleteCarRoadInfo(@RequestParam(value = "carRoadId",required = true) String id,HttpServletRequest request){
        Map<String,Object> res = new HashMap<>();
        try {
            carparkService.deleteCarRoadInfo(id,(AdminUser)getUser(),CommonUtils.getRuestIpAddress(request));
            this.success(res);
        }catch (BizException e) {
            this.failed(res, e);
        }
        return res;
    }

    //车道详情
    @ResponseBody
    @RequestMapping(value = "detailsCarRoadInfo", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> detailsCarRoadInfo(@RequestBody Map params){
        Map<String, Object> res = new HashedMap();
        Map detailsCarRoadInfo = carparkService.detailsCarRoadInfo(params);
        res.put("data", detailsCarRoadInfo);
        this.success(res);
        return res;
    }

    //打开车队模式
    @ResponseBody
    @RequestMapping(value = "openFleetMode", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> openFleetMode(@RequestBody Map params){
        Map<String, Object> res = new HashedMap();
        if (carparkService.controlFleetMode("open",params.get("carRoadId").toString()))
            this.success(res);
        else
            this.failed(res);
        return res;
    }

    //关闭车队模式
    @ResponseBody
    @RequestMapping(value = "closeFleetMode", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> closeFleetMode(@RequestBody Map params){
        Map<String, Object> res = new HashedMap();
        if (carparkService.controlFleetMode("close",params.get("carRoadId").toString()))
            this.success(res);
        else
            this.failed(res);
        return res;
    }


    //修改车道信息
    @ResponseBody
    @RequestMapping(value = "updateCarRoadInfo", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map updateCarRoadInfo(@RequestBody Map params,HttpServletRequest request){
        Map<String,Object> res = new HashMap<>();
        try {
            String carRoadId=(String) params.get("carRoadId");
            InOutCarRoadInfo carRoads =carparkService.getRemainCarRoad(carRoadId);
            carparkService.updateCarRoad(carRoads,params,(AdminUser)getUser(),CommonUtils.getRuestIpAddress(request));
            this.success(res);
        } catch (BizException e) {
            this.failed(res, e);
        }
        return res;
    }

}
