package com.starnetsecurity.parkClientServer.controller;

import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.controller.BaseController;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.parkClientServer.entity.AdminUser;
import com.starnetsecurity.parkClientServer.service.PostComputerService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by 宏炜 on 2017-12-28.
 */
@Controller
@RequestMapping("/post/computer")
public class PostComputerController extends BaseController {

    @Autowired
    PostComputerService postComputerService;

    @ResponseBody
    @RequestMapping("/page/list")
    public Map list(@RequestParam(value="start", required=false,defaultValue = "0") Integer start,
                       @RequestParam(value="length", required=false,defaultValue = "10") Integer length,
                       @RequestParam(value="draw", required=false) Integer draw){
        Map<String,Object> res = new HashedMap();
        try{
            List list = postComputerService.getPageList(length,start);
            Long total = postComputerService.getCount();
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
    @RequestMapping("/merge")
    public Map merge(@RequestBody JSONObject params, HttpServletRequest request){
        Map<String,Object> res = new HashedMap();
        try{
            postComputerService.mergePostComputer(params.getString("postComputerId"),
                    params.getString("postComputerName"),params.getString("postComputerIp"),params.getString("isAutoDeal"),
                    params.getString("isVoicePlay"),(AdminUser) getUser(), CommonUtils.getRuestIpAddress(request));

            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping("/delete")
    public Map delete(String id,HttpServletRequest request){
        Map<String,Object> res = new HashedMap();
        try{
            postComputerService.deletePostComputer(id,(AdminUser)getUser(),CommonUtils.getRuestIpAddress(request));
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }
}
