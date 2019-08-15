package com.starnetsecurity.parkClientServer.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.starnetsecurity.common.controller.BaseController;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.Constant;
import com.starnetsecurity.common.util.DateUtils;
import com.starnetsecurity.common.web.MediaTypes;
import com.starnetsecurity.parkClientServer.entity.AdminUser;
import com.starnetsecurity.parkClientServer.service.ChargeRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by fuhh on 2016-10-16.
 */
@Controller
@RequestMapping("charge")
public class ChargeController extends BaseController {

    @Autowired
    ChargeRecordsService chargeRecordsService;


    //近七天停车场收费记录
    @ResponseBody
    @RequestMapping(value = "getChargeRecords" , method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public String getInoutRecords(ModelMap modelMap,  @RequestParam(value = "parkId", required = true) String parkId) {
        List<Object[]> chargeRecords= chargeRecordsService.getChargeRecords(parkId);
        List<Object[]> data = new ArrayList<>();
        Date now = new Date();
        for(int i = 6; i >= 0; i--){
            int j = -i;
            Date tmp = DateUtils.addDay(now,j);
            boolean isExist = false;
            Object[] findObjects = null;
            for(Object[] objects : chargeRecords){
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

    /**
     * 查询当天的收费记录
     * @param modelMap
     * @param parkId
     * @return
     * @throws NoSuchFieldException
     */
    @ResponseBody
    @RequestMapping(value = "getChargeRecordsToday" , method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public String getChargeRecordsToday(ModelMap modelMap,  @RequestParam(value = "parkId", required = true) String parkId) {
        Double total = chargeRecordsService.getChargeRecordsToday(parkId);
        modelMap.put("data", total);
        this.success(modelMap);
        return JSON.toJSONString(modelMap);
    }

    /**
     * 查询今日金额组成
     * @param modelMap
     * @param parkId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getChargeRecordsTodayDetail" , method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public String getChargeRecordsTodayDetail(ModelMap modelMap,  @RequestParam(value = "parkId", required = true) String parkId) {
        List<Map> chargeRecords= chargeRecordsService.getChargeRecordsTodayDetail(parkId);
        modelMap.put("data", chargeRecords);
        this.success(modelMap);
        return JSON.toJSONString(modelMap);
    }
//查询实时缴费
@ResponseBody
@RequestMapping(value = "getChargeRecordsTodayHistory" , method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
public String getChargeRecordsTodayHistory(ModelMap modelMap,  @RequestParam(value = "parkId", required = true) String parkId,
                                   @RequestParam(value="start", required=false,defaultValue = "1") Integer start,
                                   @RequestParam(value="length", required=false,defaultValue = "10") Integer length,
                                   @RequestParam(value="draw", required=false) Integer draw) {

    try {
        List<Map> chargeRecords= chargeRecordsService.getCarparkChargeToday(start,length,parkId);
        Long total=chargeRecordsService.getCarparkChargeTodayCount(start,length,parkId);
        modelMap.put("data", chargeRecords);
        modelMap.put("draw", draw);
        modelMap.put("recordsTotal", total);
        modelMap.put("recordsFiltered", total);
        this.success(modelMap);
    }catch (BizException e){
        this.failed(modelMap,e);
    }
    return JSON.toJSONString(modelMap);
}


    //在线支付明细查询
    @ResponseBody
    @RequestMapping(value = "getOnlineCharge" , method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public String getOnlineCharge(ModelMap modelMap,  @RequestParam(value = "parkId", required = true) String parkId,
                                               @RequestParam(value="start", required=false,defaultValue = "1") Integer start,
                                               @RequestParam(value="length", required=false,defaultValue = "10") Integer length,
                                               @RequestParam(value="draw", required=false) Integer draw,
                                               @RequestParam(value = "startMonth", required = false) String startMonth,
                                               @RequestParam(value = "stopMonth", required = false) String stopMonth,
                                               @RequestParam(value = "carName", required = false) String carName
 )  {

        try {
            List<Map> chargeRecords= chargeRecordsService.getOnlineCharge(start,length,parkId, startMonth, stopMonth,carName);
            Long total=chargeRecordsService.getOnlineChargeCount(start,length,parkId, startMonth, stopMonth,carName);
            modelMap.put("data", chargeRecords);
            modelMap.put("draw", draw);
            modelMap.put("recordsTotal", total);
            modelMap.put("recordsFiltered", total);
            this.success(modelMap);
        }catch (BizException e){
            this.failed(modelMap,e);
        }
        return JSON.toJSONString(modelMap);
    }
   //查询历史缴费明细
    @ResponseBody
    @RequestMapping(value = "getChargehistory" , method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public String getChargehistory(ModelMap modelMap,  @RequestParam(value = "parkId", required = true) String parkId,
                                  @RequestParam(value="start", required=false,defaultValue = "1") Integer start,
                                  @RequestParam(value="length", required=false,defaultValue = "10") Integer length,
                                  @RequestParam(value="draw", required=false) Integer draw,
                                  @RequestParam(value = "startMonth", required = false) String startMonth,
                                  @RequestParam(value = "stopMonth", required = false) String stopMonth,
                                   @RequestParam(value = "carName", required = false) String carName
    ) {

        try {
            List<Map> chargeRecords= chargeRecordsService.getChargeHistory(start,length,parkId, startMonth, stopMonth,carName);
            Long total=chargeRecordsService.getChargeHistoryCount(start,length,parkId, startMonth, stopMonth,carName);
            modelMap.put("data", chargeRecords);
            modelMap.put("draw", draw);
            modelMap.put("recordsTotal", total);
            modelMap.put("recordsFiltered", total);
            this.success(modelMap);
        }catch (BizException e){
            this.failed(modelMap,e);
        }
        return JSON.toJSONString(modelMap);
    }


    //查询历史预缴费明细
    @ResponseBody
    @RequestMapping(value = "getPreChargehistory" , method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public String getPreChargehistory(ModelMap modelMap,  @RequestParam(value = "parkId", required = true) String parkId,
                                   @RequestParam(value="start", required=false,defaultValue = "1") Integer start,
                                   @RequestParam(value="length", required=false,defaultValue = "10") Integer length,
                                   @RequestParam(value="draw", required=false) Integer draw,
                                   @RequestParam(value = "startMonth", required = false) String startMonth,
                                   @RequestParam(value = "stopMonth", required = false) String stopMonth,
                                   @RequestParam(value = "carName", required = false) String carName
    ) {

        try {
            List<Map> chargeRecords= chargeRecordsService.getPreChargeHistoryData(start,length,parkId, startMonth, stopMonth,carName);
            Long total=chargeRecordsService.getPreChargeHistoryCount(parkId, startMonth, stopMonth,carName);
            modelMap.put("data", chargeRecords);
            modelMap.put("draw", draw);
            modelMap.put("recordsTotal", total);
            modelMap.put("recordsFiltered", total);
            this.success(modelMap);
        }catch (BizException e){
            this.failed(modelMap,e);
        }
        return JSON.toJSONString(modelMap);
    }

    /*
    *
    * 收费规则
    *
    *
    * */
    @ResponseBody
    @RequestMapping(value = "getChargeRules" , method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public String getChargeRules(ModelMap modelMap, HttpSession session
            ,  @RequestParam(value="start", required=false,defaultValue = "0") Integer start,
                                 @RequestParam(value="length", required=false,defaultValue = "10") Integer length,
                                 @RequestParam(value="draw", required=false) Integer draw

    ) {
        AdminUser adminUser = (AdminUser)session.getAttribute(Constant.SESSION_LOGIN_USER);
        String userId=adminUser.getId();

        try {
            List<Map>  chargeRules= chargeRecordsService.getChargeRules(userId,start, length);
            Long total = chargeRecordsService.getChargeRulesCount(userId);
            modelMap.put("data", chargeRules);
            modelMap.put("draw", draw);
            modelMap.put("recordsTotal", total);
            modelMap.put("recordsFiltered", total);
            this.success(modelMap);
        }catch (BizException e){
            this.failed(modelMap,e);
        }
        return JSON.toJSONString(modelMap, SerializerFeature.WriteMapNullValue);
    }

}
