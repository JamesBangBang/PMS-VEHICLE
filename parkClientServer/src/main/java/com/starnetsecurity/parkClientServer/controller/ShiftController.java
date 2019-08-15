package com.starnetsecurity.parkClientServer.controller;

import com.alibaba.fastjson.JSON;
import com.starnetsecurity.common.controller.BaseController;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.web.MediaTypes;
import com.starnetsecurity.parkClientServer.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by fuhh on 2016-10-16.
 */
@Controller
@RequestMapping("shift")
public class ShiftController  extends BaseController {

//员工交接班
    @Autowired
    ShiftService shiftService;
    @ResponseBody
    @RequestMapping(value = "getshifts", method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public String getshifts(ModelMap modelMap, @RequestParam(value = "parkId", required = true) String parkId,
                                       @RequestParam(value = "start", required = false, defaultValue = "1") Integer start,
                                       @RequestParam(value = "length", required = false, defaultValue = "10") Integer length,
                                       @RequestParam(value = "draw", required = false) Integer draw,
                                       @RequestParam(value = "startMonth", required = false) String startMonth,
                                       @RequestParam(value = "stopMonth", required = false) String stopMonth,
                                       @RequestParam(value = "boxName", required = false) String boxName
                                       ) throws NoSuchFieldException {
        try {
            List<Map> inOutRecords = shiftService.getShiftMapInfoList(start, length, parkId,boxName);
            Long total = shiftService.getShiftMapInfoCount( start, length,parkId,startMonth,stopMonth,boxName);
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
