package com.starnetsecurity.parkClientServer.controller;

import com.alibaba.fastjson.JSON;
import com.mongodb.DBObject;
import com.starnetsecurity.common.controller.BaseController;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.DateUtils;
import com.starnetsecurity.common.web.MediaTypes;
import com.starnetsecurity.parkClientServer.service.InoutRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by fuhh on 2016-10-16.
 */
@Controller
@RequestMapping("inout")
public class CarInOutController extends BaseController {

    @Autowired
    InoutRecordsService inoutRecordsService;


    //近七日出场车辆
    @ResponseBody
    @RequestMapping(value = "getInoutRecords" , method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public String getInoutRecords(ModelMap modelMap, @RequestParam(value = "parkId", required = true) String parkId) throws NoSuchFieldException {
        List<Object[]> inOutRecords = inoutRecordsService.getInOutRecords(parkId);

        List<Object[]> data = new ArrayList<>();
        Date now = new Date();
        for(int i = 6; i >= 0; i--){
            int j = -i;
            Date tmp = DateUtils.addDay(now,j);
            boolean isExist = false;
            Object[] findObjects = null;
            for(Object[] objects : inOutRecords){
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

    //查询当天的停车场记录
    @ResponseBody
    @RequestMapping(value = "getInoutRecordsToday" , method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public String getInoutRecordsToday(ModelMap modelMap, @RequestParam(value = "parkId", required = true) String parkId) throws NoSuchFieldException {
        Integer inOutRecords = inoutRecordsService.getInOutRecordsToday(parkId);
        modelMap.put("data", inOutRecords);
        this.success(modelMap);
        return JSON.toJSONString(modelMap);
    }

    //查询当天的停车场记录组成
    @ResponseBody
    @RequestMapping(value = "getInoutRecordsTodayDetail" , method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public String getInoutRecordsTodayDetail(ModelMap modelMap, @RequestParam(value = "parkId", required = true) String parkId) throws NoSuchFieldException {
        List<Map> inOutRecords = inoutRecordsService.getInOutRecordsTodayDetail(parkId);
        modelMap.put("data", inOutRecords);
        this.success(modelMap);
        return JSON.toJSONString(modelMap);
    }

//    //查询当天实时车辆记录
//    @ResponseBody
//    @RequestMapping(value = "getInoutRecordsTodayRun" , method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
//    public String getInoutRecordsTodayRun(ModelMap modelMap, @RequestParam(value = "parkId", required = true) String parkId) throws NoSuchFieldException {
//        List<DBObject> inOutRecords = inoutRecordsService.getInOutRecordsTodayRun(parkId);
//        modelMap.put("data", inOutRecords);
//        this.success(modelMap);
//        return JSON.toJSONString(modelMap);
//    }

    //查询当天实时进


    @ResponseBody
    @RequestMapping(value = "getCarparkInOutToday" , method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public String getCarparkInOutToday(ModelMap modelMap, @RequestParam(value = "parkId", required = true) String parkId,
                                       @RequestParam(value = "start", required = false, defaultValue = "1") Integer start,
                                       @RequestParam(value = "length", required = false, defaultValue = "10") Integer length,
                                       @RequestParam(value = "draw", required = false) Integer draw) throws NoSuchFieldException {

        try {
            List<Map> inOutRecords = inoutRecordsService.getCarparkOutToday(start, length, parkId);
            Long total = inoutRecordsService.getCarparkOutTodayCount(start, length, parkId);
            modelMap.put("data", inOutRecords);
            modelMap.put("draw", draw);
            modelMap.put("recordsTotal", total);
            modelMap.put("recordsFiltered", total);
            this.success(modelMap);
        } catch (BizException e) {
            this.failed(modelMap, e);
        }
        return JSON.toJSONString(modelMap);
    }


    //查询车辆历史进出明细

    @ResponseBody
    @RequestMapping(value = "getCarparkInOutHistory", method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public String getCarparkInOutHistory(ModelMap modelMap, @RequestParam(value = "parkId", required = true) String parkId,
                                         @RequestParam(value = "start", required = false, defaultValue = "1") Integer start,
                                         @RequestParam(value = "length", required = false, defaultValue = "10") Integer length,
                                         @RequestParam(value = "draw", required = false) Integer draw,
                                         @RequestParam(value = "startMonth", required = false) String startMonth,
                                         @RequestParam(value = "stopMonth", required = false) String stopMonth,
                                         @RequestParam(value = "carName", required = false) String carName) throws NoSuchFieldException {

        try {
            List<Map> inOutRecords = inoutRecordsService.getCarparkOutTodaydetail(start, length, parkId, startMonth, stopMonth,carName);
            Long total = inoutRecordsService.getCarparkOutTodaydetailCount(start, length, parkId, startMonth, stopMonth,carName);
            modelMap.put("data", inOutRecords);
            modelMap.put("draw", draw);
            modelMap.put("recordsTotal", total);
            modelMap.put("recordsFiltered", total);
            this.success(modelMap);
        } catch (BizException e) {
            this.failed(modelMap, e);
        }
        return JSON.toJSONString(modelMap);


    }

}
