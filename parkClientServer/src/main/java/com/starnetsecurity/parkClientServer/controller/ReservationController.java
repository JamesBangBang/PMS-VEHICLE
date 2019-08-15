package com.starnetsecurity.parkClientServer.controller;

import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.controller.BaseController;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.web.MediaTypes;
import com.starnetsecurity.parkClientServer.entity.MemberWallet;
import com.starnetsecurity.parkClientServer.service.ReservationService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 陈峰 on 2017/10/20.
 */
@Controller
@RequestMapping("reservation")
public class ReservationController extends BaseController{
    @Autowired
    ReservationService reservationService;

    /**
     * 是否车场重复
     * @param params
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "isCarparkRepeat", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Boolean isCarparkRepeat(@RequestBody Map params){
        String carparkId = (String) params.get("carparkId");
        String carNo = (String) params.get("carNo");
        String memberWalletId = (String) params.get("memberWalletId");
        if(StringUtils.isBlank(carparkId)|| StringUtils.isBlank(carNo)){
            return true;
        }
        return reservationService.isCarparkRepeat(carparkId, carNo, memberWalletId);
    }

    /**
     * 新增或修改预约记录
     * @param driverFileId
     * @param memberWalletId
     * @param carparkId
     * @param reservationTypeName
     * @param carNo
     * @param driverName
     * @param driverTelephoneNumber
     * @param driverInfo
     * @param effectiveStartTime
     * @param effectiveEndTime
     * @param controlMode
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "controllReservationRecord", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map controllReservationRecord(@RequestParam(value = "driverFileId", required = false) String driverFileId,
                                             @RequestParam(value = "memberWalletId", required = false) String memberWalletId,
                                             @RequestParam(value = "carparkId", required = true) String carparkId,
                                             @RequestParam(value = "reservationTypeName", required = false) String reservationTypeName,
                                             @RequestParam(value = "carNo", required = true) String carNo,
                                             @RequestParam(value = "driverName", required = false) String driverName,
                                             @RequestParam(value = "driverTelephoneNumber", required = false) String driverTelephoneNumber,
                                             @RequestParam(value = "driverInfo", required = false) String driverInfo,
                                             @RequestParam(value = "effectiveStartTime", required = true) String effectiveStartTime,
                                             @RequestParam(value = "effectiveEndTime", required = true) String effectiveEndTime,
                                             /*controlMode  0-新增  1-更新*/
                                             @RequestParam(value = "controlMode", required = false, defaultValue = "0") String controlMode){

        Map ret = new HashedMap();
        Integer result = -1;
        try{
            if(("0").equals(controlMode)){

                result = reservationService.addReservationRecord(driverFileId,
                                                             memberWalletId,
                                                             carparkId,
                                                             carNo,
                                                             reservationTypeName,
                                                             driverName,
                                                             driverTelephoneNumber,
                                                             driverInfo,
                                                             effectiveStartTime,
                                                             effectiveEndTime);
            }else{
                result = reservationService.updateReservationRecord(driverFileId,
                memberWalletId,
                carparkId,
                carNo,
                reservationTypeName,
                driverName,
                driverTelephoneNumber,
                driverInfo,
                effectiveStartTime,
                effectiveEndTime);
            }
            this.success(ret);
        }catch (BizException e){
            this.failed(ret, e);
        }
        return ret;
    }

    /**
     * 获取预约信息列表
     * start：开始显示的条数；
     * length：从开始位置，需要显示的条数；
     * carNo：车主信息车牌号；
     * driverName：车主信息名称；
     * driverFileId：预约车主信息的编号；
     * driverTelephoneNumber：车主电话号码；
     * driverInfo：车主信息；
     * beginTimeMin：车主有效开始日期的最小值；
     * beginTimeMax：车主有效开始日期的最大值；
     * endTimeMin：车主有效结束日期的最小值；
     * endTimeMax：车主有效结束日期的最大值；
     * driverFileId：车主记录的主键
     * carNo：预约车主信息的车牌号；
     * carparkId：需要限定的车场id；
     * depId：需要限定的单位id；
     */
    @ResponseBody
    @RequestMapping(value = "getReservattionList", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map getReservattionList(@RequestParam(value = "start", required = true, defaultValue = "0") Integer start,
                                   @RequestParam(value = "length", required = true, defaultValue = "10") Integer length,
                                   @RequestParam(value = "draw", required = true) Integer draw,
                                   @RequestParam(value = "carNo", required = false) String carNo,
                                   @RequestParam(value = "reservationTypeName", required = false) String reservationTypeName,
                                   @RequestParam(value = "driverName", required = false) String driverName,
                                   @RequestParam(value = "driverTelephoneNumber", required = false) String driverTelephoneNumber,
                                   @RequestParam(value = "driverInfo", required = false) String driverInfo,
                                   @RequestParam(value = "beginTimeMin", required = false) String beginTimeMin,
                                   @RequestParam(value = "beginTimeMax", required = false) String beginTimeMax,
                                   @RequestParam(value = "endTimeMin", required = false) String endTimeMin,
                                   @RequestParam(value = "endTimeMax", required = false) String endTimeMax,
                                   @RequestParam(value = "driverFileId", required = false) String driverFileId,
                                   @RequestParam(value = "carparkId", required = false) String carparkId,
                                   @RequestParam(value = "depId", required = false) String depId){
        Map<String, Object> ret = new HashMap<>();
        try{
            List<Map<String, Object>> list = reservationService.getReservattionList(start,
                                                                                     length,
                                                                                     reservationTypeName,
                                                                                     carNo,
                                                                                     driverName,
                                                                                     driverTelephoneNumber,
                                                                                     driverInfo,
                                                                                     beginTimeMin,
                                                                                     beginTimeMax,
                                                                                     endTimeMin,
                                                                                     endTimeMax,
                                                                                     driverFileId,
                                                                                     carparkId,
                                                                                     depId);
            Integer total = reservationService.getReservattionListCount(start,
                                                                        length,
                                                                        reservationTypeName,
                                                                        carNo,
                                                                        driverName,
                                                                        driverTelephoneNumber,
                                                                        driverInfo,
                                                                        beginTimeMin,
                                                                        beginTimeMax,
                                                                        endTimeMin,
                                                                        endTimeMax,
                                                                        driverFileId,
                                                                        carparkId,
                                                                        depId);
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

    /**
     * 删除预约信息
     * driverFileId：预约车主信息的编号；
     * carNo：预约车主信息的车牌号；
     */
    @ResponseBody
    @RequestMapping(value = "deleteReservation", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map deleteReservation(@RequestParam(value = "driverFileId", required = true) String driverFileId,
                                 @RequestParam(value = "memberWalletId", required = true) String memberWalletId,
                                 @RequestParam(value = "carNo", required = false) String carNo){
        Map<String, Object> ret = new HashedMap();
        try{
            Integer result = reservationService.deleteReservation(driverFileId, memberWalletId, carNo);
            if(result >= 0)
                this.success(ret);
            else
                this.failed(ret);
        }catch (BizException e){
            this.failed(ret, e);
        }
        return ret;
    }

    /**
     * 预约车申请
     * @param params
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "applyReservation", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public JSONObject applyReservation(@RequestBody JSONObject params){
        JSONObject res = new JSONObject();
        try {
            if (reservationService.applyReservation(params) >= 0) {
                res.put("result", 0);
                res.put("msg", "预约车申请成功");
                res.put("data",null);
            }else {
                res.put("result", -1);
                res.put("msg", "预约车申请失败");
                res.put("data",null);
            }
        } catch (BizException e) {
            res.put("result",-1);
            res.put("msg",e.getMessage());
            res.put("data",null);
        }
        return res;
    }

    /**
     * 预约车列表展示
     * @param params
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getReservationList", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public JSONObject showReservationList(@RequestBody JSONObject params){
        JSONObject res = new JSONObject();
        try {
            List<JSONObject> memberWalletList = reservationService.showReservationList(params);
            res.put("result",0);
            res.put("msg","获取预约车列表成功");
            res.put("data",memberWalletList);
        } catch (Exception e) {
            res.put("result",-1);
            res.put("msg","获取预约车列表失败");
            res.put("data",null);
        }
        return res;
    }

    /**
     * 预约车审核
     * @param params
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "handleReservation", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public JSONObject handleReservation(@RequestBody JSONObject params){
        JSONObject res = new JSONObject();
        try {
            if (reservationService.handleReservation(params) >= 0) {
                res.put("result", 0);
                res.put("msg", "审核预约车成功");
                res.put("data",null);
            }else {
                res.put("result",-1);
                res.put("msg","审核预约车失败");
                res.put("data",null);
            }
        } catch (Exception e) {
            res.put("result",-1);
            res.put("msg","审核预约车失败");
            res.put("data",null);
        }
        return res;
    }

    /**
     * 获取黑名单列表
     * @param start
     * @param length
     * @param draw
     * @param carNo
     * @param driverName
     * @param driverTelephoneNumber
     * @param driverInfo
     * @param beginTimeMin
     * @param beginTimeMax
     * @param endTimeMin
     * @param endTimeMax
     * @param carparkId
     * @param depId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getBlacklist", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map getBlacklist(
                                   @RequestParam(value = "start", required = true, defaultValue = "0") Integer start,
                                   @RequestParam(value = "length", required = true, defaultValue = "10") Integer length,
                                   @RequestParam(value = "draw", required = true) Integer draw,
                                   @RequestParam(value = "carNo", required = false,defaultValue = "") String carNo,
                                   @RequestParam(value = "driverName", required = false,defaultValue = "") String driverName,
                                   @RequestParam(value = "driverTelephoneNumber", required = false,defaultValue = "") String driverTelephoneNumber,
                                   @RequestParam(value = "driverInfo", required = false,defaultValue = "") String driverInfo,
                                   @RequestParam(value = "beginTimeMin", required = false) String beginTimeMin,
                                   @RequestParam(value = "beginTimeMax", required = false) String beginTimeMax,
                                   @RequestParam(value = "endTimeMin", required = false) String endTimeMin,
                                   @RequestParam(value = "endTimeMax", required = false) String endTimeMax,
                                   @RequestParam(value = "carparkId", required = false,defaultValue = "") String carparkId,
                                   @RequestParam(value = "depId", required = false,defaultValue = "") String depId){
        Map<String, Object> res = new HashMap<>();
            try {
                List<Map<String, Object>> list = reservationService.getBlacklist(
                        start,
                        length,
                        carNo,
                        driverName,
                        carparkId,
                        beginTimeMin,
                        beginTimeMax,
                        endTimeMin,
                        endTimeMax,
                        depId);
                Integer total = reservationService.getBlacklistCount(
                        start,
                        length,
                        carNo,
                        driverName,
                        carparkId,
                        beginTimeMin,
                        beginTimeMax,
                        endTimeMin,
                        endTimeMax,
                        depId);
            res.put("data", list);
            res.put("draw", draw);
            res.put("recordsTotal", total);
            res.put("recordsFiltered", total);
            this.success(res);
            }catch (BizException e){
                this.failed(res, e);
            }
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "isBlacklistCarparkRepeat", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Boolean isBlacklistCarparkRepeat(@RequestBody Map params){
        String carparkId = (String) params.get("carparkId");
        String carNo = (String) params.get("carNo");
        String memberWalletId = (String) params.get("memberWalletId");
        if(("").equals(carparkId) || ("").equals(carNo) ||
                CommonUtils.isEmpty(carparkId) || CommonUtils.isEmpty(carNo)){
            return true;
        }
        return reservationService.isBlacklistCarparkRepeat(carparkId, carNo,memberWalletId);
    }

    //保存黑名单
    @ResponseBody
    @RequestMapping(value = "saveBlacklist",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map saveBlacklist(@RequestBody Map params){
        Map<String,Object> res = new HashMap<>();
            try {
                reservationService.saveBlacklist(params);
                this.success(res);
            } catch (BizException e) {
                this.failed(res, e);
            }
            return res;
    }

    //编辑初始化
    @ResponseBody
    @RequestMapping(value = "getEditBlacklist",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map getEditBlacklist(@RequestBody Map params){
        Map<String,Object> res = new HashMap<>();
        try {
            List<Map<String, Object>> list = reservationService.getEditBlacklist(params);
            res.put("data",list);
            this.success(res);
            } catch (BizException e) {
            this.failed(res, e);
            }

        return res;
    }
    //删除黑名单信息
    @ResponseBody
    @RequestMapping("delBlacklist")
    public Map delBlacklist(@RequestBody JSONObject params){
        Map<String,Object> res = new HashedMap();
        try{
            reservationService.delBlacklist(params);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }
}
