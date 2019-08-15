package com.starnetsecurity.parkClientServer.controller;

import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.controller.BaseController;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.web.MediaTypes;
import com.starnetsecurity.parkClientServer.entity.AdminUser;
import com.starnetsecurity.parkClientServer.service.WhiteListService;
import org.apache.axis.utils.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JamesBangBang on 2017/10/26.
 */
@Controller
@RequestMapping("whitelist")
public class WhiteListController extends BaseController{
    @Autowired
    WhiteListService whiteListService;

    /**
     * 白名单列表
     * @param start
     * @param length
     * @param draw
     * @param carNo
     * @param chargeTypeName
     * @param driverName
     * @param driverTelephoneNumber
     * @param driverInfo
     * @param driverFileId
     * @param carparkId
     * @param depId
     * @param packageKind
     * @param overdueTime
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getWhiteList", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map getWhiteList(@RequestParam(value="start", required=false,defaultValue = "0") Integer start,
                                   @RequestParam(value="length", required=false,defaultValue = "10") Integer length,
                                   @RequestParam(value="draw", required=false) Integer draw,
                                   @RequestParam(value = "carNo", required = false,defaultValue = "") String carNo,
                                   @RequestParam(value = "chargeTypeName", required = false,defaultValue = "") String chargeTypeName,
                                   @RequestParam(value = "driverName", required = false,defaultValue = "") String driverName,
                                   @RequestParam(value = "driverTelephoneNumber", required = false,defaultValue = "") String driverTelephoneNumber,
                                   @RequestParam(value = "driverInfo", required = false,defaultValue = "") String driverInfo,
                                   @RequestParam(value = "driverFileId", required = false,defaultValue = "") String driverFileId,
                                   @RequestParam(value = "carparkId", required = false,defaultValue = "") String carparkId,
                                   @RequestParam(value = "depId", required = true) String depId,
                                   @RequestParam(value = "packageKind", required = false,defaultValue = "") String packageKind,
                                   @RequestParam(value = "overdueTime",required = false,defaultValue = "") String overdueTime){
        Map<String, Object> ret = new HashMap<>();
        try{
            List<Map<String, Object>> list = whiteListService.getWhiteList(carNo,
                    chargeTypeName,
                    driverName,
                    driverTelephoneNumber,
                    driverInfo,
                    carparkId,
                    depId,
                    packageKind,
                    overdueTime,
                    start,
                    length);
            Integer total = whiteListService.getWhiteListCount(carNo,
                    chargeTypeName,
                    driverName,
                    driverTelephoneNumber,
                    driverFileId,
                    carparkId,
                    depId,
                    overdueTime);
            ret.put("data", list);
            ret.put("draw", draw);
            ret.put("recordsTotal", total);
            ret.put("recordsFiltered", total);
            this.success(ret);
        }catch (BizException e){
            this.failed(ret, e);
        }
        return ret;
    }



    //获取车场信息
    @ResponseBody
    @RequestMapping(value = "getAllCarPark", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map getAllCarPark(){
        Map<String, Object> res = new HashMap<>();
        try{
            List list = whiteListService.getAllCarPark();
            res.put("data",list);
            this.success(res);
        }catch (BizException e){
            this.failed(res, e);
        }
        return res;
    }

    //获取套餐信息
    @ResponseBody
    @RequestMapping(value = "getAllPackage", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map getAllPackage(String carparkId){
        Map<String, Object> res = new HashMap<>();
        try{
            List list=whiteListService.getAllPackage(carparkId);
            res.put("data",list);
            this.success(res);
        }catch (BizException e){
            this.failed(res, e);
        }
        return res;
    }

    //保存会员信息
    @ResponseBody
    @RequestMapping(value="saveOwner",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map saveOwner(@RequestBody Map params, HttpServletRequest request){
        Map<String,Object> res = new HashMap<>();
        try {
            whiteListService.saveOwner(params,(AdminUser) getUser(), CommonUtils.getRuestIpAddress(request));
            this.success(res);
        } catch (BizException e) {
            this.failed(res, e);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping(value="updateOwner",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map updateOwner(@RequestBody JSONObject params, HttpServletRequest request){
        Map<String,Object> res = new HashMap<>();
        try {
            whiteListService.updateOwner(params,(AdminUser) getUser(), CommonUtils.getRuestIpAddress(request));
            this.success(res);
        } catch (BizException e) {
            this.failed(res, e);
        }
        return res;
    }


    //会员信息删除
    @ResponseBody
    @RequestMapping(value = "delOwner",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map delOwner(@RequestBody Map params, HttpServletRequest request){
        Map<String,Object> res = new HashMap<>();
        try {
            String id = (String)params.get("id");
            whiteListService.delOwner(id, CommonUtils.getRuestIpAddress(request));
            this.success(res);
        }catch (BizException e) {
            this.failed(res, e);
        }
        return res;
    }


    //编辑会员新
    @ResponseBody
    @RequestMapping(value = "editOwner", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map editOwner(@RequestBody Map params){
        Map<String, Object> res = new HashMap<>();
        try{
            String id = (String) params.get("id");
            List list = whiteListService.editOwner(id);
            res.put("data",list);
            this.success(res);
        }catch (BizException e){
            this.failed(res, e);
        }
        return res;
    }

    //判断同一车场下，车牌存在不
    @ResponseBody
    @RequestMapping(value="isCarNo",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public boolean isCarNo(@RequestBody Map params){
        boolean res = false;
        res=whiteListService.isCarNo(params);
        return res;
    }





}

