package com.starnetsecurity.parkClientServer.controller;

import com.alibaba.fastjson.JSON;
import com.starnetsecurity.common.controller.BaseController;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.Constant;
import com.starnetsecurity.common.web.MediaTypes;
import com.starnetsecurity.parkClientServer.entity.AdminUser;
import com.starnetsecurity.parkClientServer.service.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by fuhh on 2016-10-20.
 */
@Controller
@RequestMapping("operator")
public class OperatorController extends BaseController {
    @Autowired
    OperatorService operatorService;
    //操作日志
    @ResponseBody
    @RequestMapping(value = "getOperator"  , method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public String getOperator(ModelMap modelMap, HttpSession session,
                              @RequestParam(value="start", required=false,defaultValue = "1") Integer start,
                              @RequestParam(value="length", required=false,defaultValue = "10") Integer length,
                              @RequestParam(value="draw", required=false) Integer draw,
                              @RequestParam(value = "startMonth", required = false) String startMonth,
                              @RequestParam(value = "stopMonth", required = false) String stopMonth,
                              @RequestParam(value = "logName", required = false) String logName) throws NoSuchFieldException {
        AdminUser adminUser = (AdminUser)session.getAttribute(Constant.SESSION_LOGIN_USER);
        String userId=adminUser.getId();
        try {
            List<Map>  operatorhistorys= operatorService.getOperatorhistory(userId,start, length, startMonth,stopMonth,logName);
            Long total=operatorService.getOperatorhistoryCount(userId,start, length, startMonth,stopMonth,logName);
            modelMap.put("data", operatorhistorys);
            modelMap.put("draw", draw);
            modelMap.put("recordsTotal", total);
            modelMap.put("recordsFiltered", total);
            this.success(modelMap);
        }catch (BizException e){
            this.failed(modelMap,e);
        }
        return JSON.toJSONString(modelMap);
    }
}
