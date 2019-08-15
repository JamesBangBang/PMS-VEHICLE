package com.starnetsecurity.parkClientServer.controller;

import com.starnetsecurity.common.controller.BaseController;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.web.MediaTypes;
import com.starnetsecurity.parkClientServer.service.CalculateFeeNewService;
import com.starnetsecurity.parkClientServer.service.OrderParkService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.codec.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.Map;

/**
 * Created by JamesBangBang on 2017/7/18.
 * 接收出入口软件传来的数据
 */
@Controller
@RequestMapping("order")
public class ConnectWithDelphi extends BaseController{

    @Autowired
    OrderParkService orderParkService;

    @Autowired
    CalculateFeeNewService calculateFeeNewService;

    /**
     * 获取同一管理单位下面的所有的车道信息
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "getRoadInfo", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map getRoadInfo() throws Exception{
        Map<String,Object> res = new HashedMap();
        try{
            res = orderParkService.getManualRoadInfo();
            this.success(res);
        }catch (BizException ex){
            this.failed(res);
        }
        return res;
    }

    /**
     * 获取交接班信息
     * @param operatorUserName
     * @param loginTime
     * @param token
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "getShiftInfo", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map getShiftInfo(@RequestParam(value = "operatorUserName",required = true) String operatorUserName,
                            @RequestParam(value = "loginTime",required = true) String loginTime,
                            @RequestParam(value = "token",required = true) String token) throws Exception{
        Map<String,Object> res = new HashedMap();
        try{
            res = orderParkService.getShiftInfo(operatorUserName,loginTime,token);
            this.success(res);
        }catch (BizException ex){
            this.failed(res);
        }
        return res;
    }

    /**
     * 获取会员信息
     * @param carno
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "getMemberInfo", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map getMemberInfo(@RequestParam(value = "carno",required = true) String carno) throws Exception{
        Map<String,Object> res = new HashedMap();
        try{
            carno = URLDecoder.decode(carno,"gbk");
            res = orderParkService.getMemberWalletInfo(carno);
            this.success(res);
        }catch (BizException ex){
            this.failed(res);
        }
        return res;
    }

    /**
     * 获取设备信息
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "getIpcDeviceInfo", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map getIpcDeviceInfo() throws Exception{
        Map<String,Object> res = new HashedMap();
        try{
            res = orderParkService.getDeviceInfo();
            this.success(res);
        }catch (BizException ex){
            this.failed(res);
        }
        return res;
    }

    /**
     * 获取车场及进出信息
     * @param ips
     * @param operatorUserName
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "getParkingInfo", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map getParkingInfo(@RequestParam(value = "ips",required = true) String ips,
                              @RequestParam(value = "operatorUserName",required = true) String operatorUserName,
                              @RequestParam(value = "loginTime",required = true) String loginTime) throws Exception{
        Map<String,Object> res = new HashedMap();
        try{
            res = orderParkService.getParkingInfo(ips,operatorUserName,loginTime);
            this.success(res);
        }catch (BizException ex){
            this.failed(res);
        }
        return res;
    }

    /**
     * 道闸控制
     * @param deviceIp
     * @param controlMode
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "controlOpenGate", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map controlOpenGate(@RequestParam(value = "ips",required = true) String deviceIp,
                               @RequestParam(value = "controlMode",required = true) String controlMode,
                               @RequestParam(value = "operatorName",required = false)String operatorName,
                               @RequestParam(value = "postIp",required = false)String postIp) throws Exception{
        Map<String,Object> res = new HashedMap();
        try{
            res = orderParkService.controlOpenGate(deviceIp,controlMode,operatorName,postIp);
            this.success(res);
        }catch (BizException ex){
            this.failed(res);
        }
        return res;
    }

    /**
     * 获取车场及进出信息
     * @param carparkId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "getMatchCarnoInfo", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map getMatchCarnoInfo(@RequestParam(value = "carparkId",required = true) String carparkId,
                                 @RequestParam(value = "carno",required = false,defaultValue = "") String carno,
                                 @RequestParam(value = "startTime",required = true) Long startTime,
                                 @RequestParam(value = "endTime",required = true) Long endTime,
                                 @RequestParam(value = "pageNum",required = false,defaultValue = "0") Integer pageNum) throws Exception{
        Map<String,Object> res = new HashedMap();
        try{
            if(!StringUtils.isBlank(carno))
                carno = URLDecoder.decode(carno,"gbk");
            res = orderParkService.getMatchCarnoInfo(carparkId,carno,pageNum,startTime,endTime);
            this.success(res);
        }catch (BizException ex){
            this.failed(res);
        }
        return res;
    }

    /**
     * 重新匹配信息
     * @param inRecordId
     * @param reMatchTime
     * @param outCarno
     * @param carnoColor
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "reMatchInfo", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map GetReMatchInfo(@RequestParam(value = "inRecordId",required = true) String inRecordId,
                              @RequestParam(value = "reMatchTime",required = true) String reMatchTime,
                              @RequestParam(value = "outCarno",required = true) String outCarno,
                              @RequestParam(value = "carnoColor",required = true) String carnoColor) throws Exception{
        Map<String,Object> res = new HashedMap();
        try{

            res = orderParkService.getRematchCarnoInfo(inRecordId,reMatchTime,outCarno,carnoColor);
            this.success(res);
        }catch (BizException ex){
            this.failed(res);
        }
        return res;
    }

    /**
     * 获取场内支付金额
     * @param carparkId
     * @param outCarno
     * @param outTime
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "getPrechargeInfo", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map GetPreChargeInfo(@RequestParam(value = "carparkId",required = true) String carparkId,
                              @RequestParam(value = "outCarno",required = true) String outCarno,
                                @RequestParam(value = "outTime",required = true) String outTime) throws Exception{
        Map<String,Object> res = new HashedMap();
        try{
            outCarno = URLDecoder.decode(outCarno,"gbk");
            res = orderParkService.getPrechargeInfo(carparkId,outCarno,outTime);
            this.success(res);
        }catch (BizException ex){
            this.failed(res);
        }
        return res;
    }

    /**
     * 切换车型重新获取收费金额
     * @param carparkId
     * @param outCarno
     * @param outTime
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "getAmountByCarType", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map GetAmountByCarType(@RequestParam(value = "carparkId",required = true) String carparkId,
                                @RequestParam(value = "outCarno",required = true) String outCarno,
                                  @RequestParam(value = "inTime",required = true) String inTime,
                                @RequestParam(value = "outTime",required = true) String outTime,
                                  @RequestParam(value = "userType",required = true) String userType,
                                  @RequestParam(value = "carType",required = true) String carType) throws Exception{
        Map<String,Object> res = new HashedMap();
        try{
            outCarno = URLDecoder.decode(outCarno,"gbk");
            Timestamp chargeInTime = Timestamp.valueOf(inTime.replaceAll("/","-"));
            Timestamp chargeOutTime = Timestamp.valueOf(outTime.replaceAll("/","-"));
            Double chargeAmount = 0.0;
            if ("0".equals(userType) || "1".equals(userType))
                chargeAmount = calculateFeeNewService.calculateParkingFeeNew(outCarno,userType,carType,carparkId,chargeInTime,chargeOutTime);
            res.put("outCarno", Base64.encodeToString(outCarno.getBytes()));
            res.put("chargeAmount",chargeAmount.toString());
            this.success(res);
        }catch (BizException ex){
            this.failed(res);
        }
        return res;
    }



}
