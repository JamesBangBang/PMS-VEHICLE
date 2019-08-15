package com.starnetsecurity.parkClientServer.controller;

import com.alibaba.fastjson.JSON;
import com.starnetsecurity.common.controller.BaseController;
import com.starnetsecurity.common.util.DateUtils;
import com.starnetsecurity.common.web.MediaTypes;
import com.starnetsecurity.parkClientServer.service.UnusualRecordsService;
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

@Controller
@RequestMapping("unusual")
public class UnusualController extends BaseController {
    @Autowired
    UnusualRecordsService unusualRecordsService;


    //近七天停车场异常记录
    @ResponseBody
    @RequestMapping(value = "getUnusualRecords" ,method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public String getUnusualRecords(ModelMap modelMap, @RequestParam(value = "parkId", required = true) String parkId) throws NoSuchFieldException {
        List<Object[]> unusualRecords = unusualRecordsService.getUnusualRecords(parkId);

        List<Object[]> data = new ArrayList<>();
        Date now = new Date();
        for(int i = 6; i >= 0; i--){
            int j = -i;
            Date tmp = DateUtils.addDay(now,j);
            boolean isExist = false;
            Object[] findObjects = null;
            for(Object[] objects : unusualRecords){
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
    //查询当天的异常记录
    @ResponseBody
    @RequestMapping(value = "getUnusualRecordsToday" , method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public String getUnusualRecordsToday(ModelMap modelMap,  @RequestParam(value = "parkId", required = true) String parkId) throws NoSuchFieldException {
        Integer unusualRecords = unusualRecordsService.getUnusualRecordsToday(parkId);
        modelMap.put("data", unusualRecords);
        this.success(modelMap);
        return JSON.toJSONString(modelMap);
    }
}
