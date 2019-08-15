package com.starnetsecurity.parkClientServer.controller;

import com.starnetsecurity.common.controller.BaseController;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.web.MediaTypes;
import com.starnetsecurity.parkClientServer.entity.AdminUser;
import com.starnetsecurity.parkClientServer.service.DiscountService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JAMESBANG on 2018/3/20.
 */
@Controller
@RequestMapping("discount")
public class DiscountController extends BaseController{
    @Autowired
    DiscountService discountService;


    /**
     * 获取优惠信息列表
     * @param discountType
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getDiscountInfo",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map getDiscountInfo(@RequestParam(value="start", required=false,defaultValue = "1") Integer start,
                               @RequestParam(value="length", required=false,defaultValue = "10") Integer length,
                               @RequestParam(value="draw", required=true) Integer draw,
                               @RequestParam(value = "discountType",required = false) String discountType) throws NoSuchFieldException {
        Map<String,Object> res = new HashedMap();
        try{
            List data= discountService.GetDisCountInfoList(discountType,start,length);
            res.put("data",data);
            res.put("draw", draw);
            Long total = discountService.GetDiscountListCount(discountType);
            res.put("recordsTotal", total);
            res.put("recordsFiltered", total);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    /**
     * 获取优惠信息详情
     * @param discountId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getDetailDiscountInfo",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map getDetailDiscountInfo(@RequestParam(value = "discountId",required = false) String discountId) throws NoSuchFieldException {
        Map<String,Object> res = new HashedMap();
        try{
            Map<String,Object> data= discountService.GetDisCountDetailInfo(discountId);
            res.put("data",data);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    /**
     * 判断优惠名称是否重复
     * @param params
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "isDiscountNameRepeat",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public boolean isDiscountNameRepeat(@RequestBody Map params) throws NoSuchFieldException {
        boolean res = false;
        res = discountService.IsDiscountNameRepeat(params.get("controlMode").toString(), params.get("discountName").toString(), params.get("discountId").toString());
        return res;
    }

    /**
     * 判断优惠名称是否重复
     * @param params
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "saveDiscountInfo",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map saveDiscountInfo(@RequestBody Map params) throws NoSuchFieldException {
        Map<String,Object> res = new HashedMap();
        discountService.SaveDiscountInfo(params);
        res.put("result",0);
        return res;
    }

    /**
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "deleteDiscountInfo",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map deleteDiscountInfo(@RequestParam(value = "discountTypeId",required = true) String id){
        Map<String,Object> res = new HashMap<>();
        try {
            discountService.DeleteDiscountInfo(id);
            this.success(res);
        }catch (BizException e) {
            this.failed(res, e);
        }
        return res;
    }



}
