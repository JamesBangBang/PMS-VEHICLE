package com.starnetsecurity.parkClientServer.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.starnetsecurity.common.controller.BaseController;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.util.PoiExcelUtil;
import com.starnetsecurity.common.web.MediaTypes;
import com.starnetsecurity.parkClientServer.entityEnum.payStatusEnum;
import com.starnetsecurity.parkClientServer.entityEnum.payTypeEnum;
import com.starnetsecurity.parkClientServer.service.ReportStatisticsService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by 陈峰 on 2017/10/13.
 */
@Controller
@RequestMapping("report")
public class ReportStatisticsController extends BaseController{
    @Autowired
    ReportStatisticsService reportStatisticsService;

    /**
     * 根据车场获取当前单位信息
     * carparkId：车场编号；
     */
    @ResponseBody
    @RequestMapping(value = "getDepInfo", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public String getDepInfo(@RequestBody Map params){
        Map ret = new HashedMap();
        try{
            String carparkId = (String) params.get("carparkId");
            List<Map<String, Object>> list = reportStatisticsService.getDepInfo(carparkId);
            ret.put("data", list);
            this.success(ret);
        }catch (BizException e){
            this.failed(ret, e);
        }
        return  JSON.toJSONString(ret);
    }

    /**
     * 获取收费子类型
     * depId：单位编号；
     * carparkId：车场编号；
     */
    @ResponseBody
    @RequestMapping(value = "getChargeReceiveSubType", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map getChargeReceiveSubType(@RequestParam(value = "depId", required = false) String depId,
                                       @RequestParam(value = "carparkId", required = false) String carparkId){
        Map ret = new HashedMap();
        try{
            List<Map<String, Object>> list = reportStatisticsService.getChargeReceiveSubType(depId, carparkId);
            ret.put("data", list);
            this.success(ret);
        }catch (BizException e){
            this.failed(ret, e);
        }
        return ret;
    }

    /**
     * 获取车场信息
     * depId：单位编号；
     */
    @ResponseBody
    @RequestMapping(value = "getCarparkList", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map getCarparkList(@RequestParam(value = "depId", required = false) String depId,
                              @RequestParam(value = "carparkId", required = false) String carparkId,
                              @RequestParam(value = "ownCarparkNo", required = false) String ownCarparkNo){
        Map<String, Object> ret = new HashedMap();
        try{
            List<Map<String, Object>> list = reportStatisticsService.getCarparkList(depId, carparkId, ownCarparkNo);
            ret.put("data", list);
            this.success(ret);
        }catch (BizException e){
            this.failed(ret, e);
        }
        return ret;
    }

    /**
     * 获取岗亭信息
     * depId：单位编号；
     * boothId：岗亭编号；
     */
    @ResponseBody
    @RequestMapping(value = "getBoothList", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map getBoothList(@RequestParam(value = "depId", required = false) String depId,
                               @RequestParam(value = "boothId", required = false) String boothId){
        Map ret = new HashedMap();
        try{
            List<Map<String, Object>> list = reportStatisticsService.getBoothList(depId, boothId);
            ret.put("data", list);
            this.success(ret);
        }catch (BizException e){
            this.failed(ret, e);
        }
        return ret;
    }

    /**
     * 获取收费类型
     * depId：单位编号；
     * carparkId：车场编号；
     */
    @ResponseBody
    @RequestMapping(value = "getChargeTypeInfo", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map getChargeTypeInfo(@RequestParam(value = "depId", required = false) String depId,
                                       @RequestParam(value = "carparkId", required = false) String carparkId){
        Map ret = new HashedMap();
        try{
            List<Map<String, Object>> list = reportStatisticsService.getChargeTypeInfo(depId, carparkId);
            ret.put("data", list);
            this.success(ret);
        }catch (BizException e){
            this.failed(ret, e);
        }
        return ret;
    }

    /**
     * 岗亭车流统计
     * @param start
     * @param length
     * @param draw
     * @param beginTime
     * @param endTime
     * @param depId
     * @param depName
     * @param carparkId
     * @param carparkName
     * @param boothId
     * @param boothName
     * @param inCountMin
     * @param inCountMax
     * @param outCountMin
     * @param outCountMax
     * @param carCountMin
     * @param carCountMax
     * @param sumMinuteMin
     * @param sumMinuteMax
     * @param maxMinuteMin
     * @param maxMinuteMax
     * @param minMinuteMin
     * @param minMinuteMax
     * @param avgMinuteMin
     * @param avgMinuteMax
     * @param reportType
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getBoothVehicleFlowReport", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public String getBoothVehicleFlowReport(@RequestParam(value="start", required=false,defaultValue = "0") Integer start,
                                           @RequestParam(value="length", required=false,defaultValue = "10") Integer length,
                                           @RequestParam(value="draw", required=true) Integer draw,
                                           @RequestParam(value = "beginTime", required = true) String beginTime,
                                           @RequestParam(value = "endTime", required = true) String endTime,

                                           @RequestParam(value = "depId", required = false) String depId,
                                           @RequestParam(value = "depName", required = false) String depName,
                                           @RequestParam(value = "carparkId", required = false) String carparkId,
                                           @RequestParam(value = "carparkName", required = false) String carparkName,

                                           @RequestParam(value = "boothId", required = false) String boothId,
                                           @RequestParam(value = "boothName", required = false) String boothName,
                                           @RequestParam(value = "inCountMin", required = false) Integer inCountMin,
                                           @RequestParam(value = "inCountMax", required = false) Integer inCountMax,
                                           @RequestParam(value = "outCountMin", required = false) Integer outCountMin,
                                           @RequestParam(value = "outCountMax", required = false) Integer outCountMax,
                                           @RequestParam(value = "carCountMin", required = false) Integer carCountMin,
                                           @RequestParam(value = "carCountMax", required = false) Integer carCountMax,
                                           @RequestParam(value = "sumMinuteMin", required = false) Integer sumMinuteMin,
                                           @RequestParam(value = "sumMinuteMax", required = false) Integer sumMinuteMax,
                                           @RequestParam(value = "maxMinuteMin", required = false) Integer maxMinuteMin,
                                           @RequestParam(value = "maxMinuteMax", required = false) Integer maxMinuteMax,
                                           @RequestParam(value = "minMinuteMin", required = false) Integer minMinuteMin,
                                           @RequestParam(value = "minMinuteMax", required = false) Integer minMinuteMax,
                                           @RequestParam(value = "avgMinuteMin", required = false) Integer avgMinuteMin,
                                           @RequestParam(value = "avgMinuteMax", required = false) Integer avgMinuteMax,
                                           @RequestParam(value = "reportType", required = false) String reportType){
        Map<String, Object> res = new HashedMap();
        try{

            List<Map<String, Object>> list = reportStatisticsService.getBoothVehicleFlowReport(start,
                                                                                                length,
                                                                                                beginTime,
                                                                                                endTime,
                                                                                                depId,
                                                                                                depName,
                                                                                                carparkId,
                                                                                                carparkName,
                                                                                                boothId,
                                                                                                boothName,
                                                                                                inCountMin,
                                                                                                inCountMax,
                                                                                                outCountMin,
                                                                                                outCountMax,
                                                                                                carCountMin,
                                                                                                carCountMax,
                                                                                                sumMinuteMin,
                                                                                                sumMinuteMax,
                                                                                                maxMinuteMin,
                                                                                                maxMinuteMax,
                                                                                                minMinuteMin,
                                                                                                minMinuteMax,
                                                                                                avgMinuteMin,
                                                                                                avgMinuteMax,
                                                                                                reportType);
            Integer total = reportStatisticsService.getBoothVehicleFlowReportCount(start,
                                                                                    length,
                                                                                    beginTime,
                                                                                    endTime,
                                                                                    depId,
                                                                                    depName,
                                                                                    carparkId,
                                                                                    carparkName,
                                                                                    boothId,
                                                                                    boothName,
                                                                                    inCountMin,
                                                                                    inCountMax,
                                                                                    outCountMin,
                                                                                    outCountMax,
                                                                                    carCountMin,
                                                                                    carCountMax,
                                                                                    sumMinuteMin,
                                                                                    sumMinuteMax,
                                                                                    maxMinuteMin,
                                                                                    maxMinuteMax,
                                                                                    minMinuteMin,
                                                                                    minMinuteMax,
                                                                                    avgMinuteMin,
                                                                                    avgMinuteMax,
                                                                                    reportType);
            res.put("data", list);
            res.put("draw", draw);
            res.put("recordsTotal", total);
            res.put("recordsFiltered", total);
            this.success(res);

        }catch (BizException e){
            this.failed(res, e);
        }
        return JSON.toJSONString(res, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 车辆属性车流统计
     * @param start
     * @param length
     * @param draw
     * @param beginTime
     * @param endTime
     * @param depId
     * @param depName
     * @param carparkId
     * @param carparkName
     * @param carType
     * @param inCountMin
     * @param inCountMax
     * @param outCountMin
     * @param outCountMax
     * @param carCountMin
     * @param carCountMax
     * @param sumMinuteMin
     * @param sumMinuteMax
     * @param maxMinuteMin
     * @param maxMinuteMax
     * @param minMinuteMin
     * @param minMinuteMax
     * @param avgMinuteMin
     * @param avgMinuteMax
     * @param reportType
     * @param isExistDuration
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getVehicleAttributeReport", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public String getVehicleAttributeReport(@RequestParam(value="start", required=false,defaultValue = "0") Integer start,
                                           @RequestParam(value="length", required=false,defaultValue = "10") Integer length,
                                           @RequestParam(value="draw", required=true) Integer draw,
                                           @RequestParam(value = "beginTime", required = true) String beginTime,
                                           @RequestParam(value = "endTime", required = true) String endTime,

                                           @RequestParam(value = "depId", required = false) String depId,
                                           @RequestParam(value = "depName", required = false) String depName,
                                           @RequestParam(value = "carparkId", required = false) String carparkId,
                                           @RequestParam(value = "carparkName", required = false) String carparkName,

                                           @RequestParam(value = "carType", required = false) String carType,
                                           @RequestParam(value = "inCountMin", required = false) Integer inCountMin,
                                           @RequestParam(value = "inCountMax", required = false) Integer inCountMax,
                                           @RequestParam(value = "outCountMin", required = false) Integer outCountMin,
                                           @RequestParam(value = "outCountMax", required = false) Integer outCountMax,
                                           @RequestParam(value = "carCountMin", required = false) Integer carCountMin,
                                           @RequestParam(value = "carCountMax", required = false) Integer carCountMax,
                                           @RequestParam(value = "sumMinuteMin", required = false) Integer sumMinuteMin,
                                           @RequestParam(value = "sumMinuteMax", required = false) Integer sumMinuteMax,
                                           @RequestParam(value = "maxMinuteMin", required = false) Integer maxMinuteMin,
                                           @RequestParam(value = "maxMinuteMax", required = false) Integer maxMinuteMax,
                                           @RequestParam(value = "minMinuteMin", required = false) Integer minMinuteMin,
                                           @RequestParam(value = "minMinuteMax", required = false) Integer minMinuteMax,
                                           @RequestParam(value = "avgMinuteMin", required = false) Integer avgMinuteMin,
                                           @RequestParam(value = "avgMinuteMax", required = false) Integer avgMinuteMax,
                                           @RequestParam(value = "reportType", required = false) String reportType,
                                           /*0-不需要停车时长， 1-需要停车时长*/
                                           @RequestParam(value = "isExistDuration", required = false) String isExistDuration){
        Map<String, Object> res = new HashedMap();
        try{

            List<Map<String, Object>> list = reportStatisticsService.getVehicleAttributeReport(start,
                                                                                            length,
                                                                                            beginTime,
                                                                                            endTime,
                                                                                            depId,
                                                                                            depName,
                                                                                            carparkId,
                                                                                            carparkName,
                                                                                            carType,
                                                                                            inCountMin,
                                                                                            inCountMax,
                                                                                            outCountMin,
                                                                                            outCountMax,
                                                                                            carCountMin,
                                                                                            carCountMax,
                                                                                            sumMinuteMin,
                                                                                            sumMinuteMax,
                                                                                            maxMinuteMin,
                                                                                            maxMinuteMax,
                                                                                            minMinuteMin,
                                                                                            minMinuteMax,
                                                                                            avgMinuteMin,
                                                                                            avgMinuteMax,
                                                                                            reportType);
//            Subject subject = SecurityUtils.getSubject();
//            Session session = subject.getSession();
//            AdminUser adminUser = (AdminUser) session.getAttribute(Constant.SESSION_LOGIN_USER);
//

            Integer total = reportStatisticsService.getVehicleAttributeReportCount(start,
                                                                                    length,
                                                                                    beginTime,
                                                                                    endTime,
                                                                                    depId,
                                                                                    depName,
                                                                                    carparkId,
                                                                                    carparkName,
                                                                                    carType,
                                                                                    inCountMin,
                                                                                    inCountMax,
                                                                                    outCountMin,
                                                                                    outCountMax,
                                                                                    carCountMin,
                                                                                    carCountMax,
                                                                                    sumMinuteMin,
                                                                                    sumMinuteMax,
                                                                                    maxMinuteMin,
                                                                                    maxMinuteMax,
                                                                                    minMinuteMin,
                                                                                    minMinuteMax,
                                                                                    avgMinuteMin,
                                                                                    avgMinuteMax,
                                                                                    reportType);
            res.put("data", list);
            res.put("draw", draw);
            res.put("recordsTotal", total);
            res.put("recordsFiltered", total);
            this.success(res);

        }catch (BizException e){
            this.failed(res, e);
        }
        return JSON.toJSONString(res, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 停车情况统计报表
     * @param start
     * @param length
     * @param draw
     * @param beginTime
     * @param endTime
     * @param depId
     * @param depName
     * @param carparkId
     * @param carparkName
     * @param parkingDurationSection
     * @param inCountMin
     * @param inCountMax
     * @param outCountMin
     * @param outCountMax
     * @param carCountMin
     * @param carCountMax
     * @param sumMinuteMin
     * @param sumMinuteMax
     * @param maxMinuteMin
     * @param maxMinuteMax
     * @param minMinuteMin
     * @param minMinuteMax
     * @param avgMinuteMin
     * @param avgMinuteMax
     * @param reportType
     * @param isExistDuration
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getParkingSituationReport", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public String getParkingSituationReport(@RequestParam(value="start", required=false,defaultValue = "0") Integer start,
                                           @RequestParam(value="length", required=false,defaultValue = "10") Integer length,
                                           @RequestParam(value="draw", required=true) Integer draw,
                                           @RequestParam(value = "beginTime", required = true) String beginTime,
                                           @RequestParam(value = "endTime", required = true) String endTime,

                                           @RequestParam(value = "depId", required = false) String depId,
                                           @RequestParam(value = "depName", required = false) String depName,
                                           @RequestParam(value = "carparkId", required = false) String carparkId,
                                           @RequestParam(value = "carparkName", required = false) String carparkName,

                                           @RequestParam(value = "parkingDurationSection", required = false) String parkingDurationSection,
                                           @RequestParam(value = "inCountMin", required = false) Integer inCountMin,
                                           @RequestParam(value = "inCountMax", required = false) Integer inCountMax,
                                           @RequestParam(value = "outCountMin", required = false) Integer outCountMin,
                                           @RequestParam(value = "outCountMax", required = false) Integer outCountMax,
                                           @RequestParam(value = "carCountMin", required = false) Integer carCountMin,
                                           @RequestParam(value = "carCountMax", required = false) Integer carCountMax,
                                           @RequestParam(value = "sumMinuteMin", required = false) Integer sumMinuteMin,
                                           @RequestParam(value = "sumMinuteMax", required = false) Integer sumMinuteMax,
                                           @RequestParam(value = "maxMinuteMin", required = false) Integer maxMinuteMin,
                                           @RequestParam(value = "maxMinuteMax", required = false) Integer maxMinuteMax,
                                           @RequestParam(value = "minMinuteMin", required = false) Integer minMinuteMin,
                                           @RequestParam(value = "minMinuteMax", required = false) Integer minMinuteMax,
                                           @RequestParam(value = "avgMinuteMin", required = false) Integer avgMinuteMin,
                                           @RequestParam(value = "avgMinuteMax", required = false) Integer avgMinuteMax,
                                           @RequestParam(value = "reportType", required = false) String reportType,
                                           /*0-不需要停车时长， 1-需要停车时长*/
                                           @RequestParam(value = "isExistDuration", required = false) String isExistDuration){
        Map<String, Object> res = new HashedMap();
        try{

            List<Map<String, Object>> list = reportStatisticsService.getParkingSituationReport(start,
                                                                                            length,
                                                                                            beginTime,
                                                                                            endTime,
                                                                                            depId,
                                                                                            depName,
                                                                                            carparkId,
                                                                                            carparkName,
                                                                                            parkingDurationSection,
                                                                                            inCountMin,
                                                                                            inCountMax,
                                                                                            outCountMin,
                                                                                            outCountMax,
                                                                                            carCountMin,
                                                                                            carCountMax,
                                                                                            sumMinuteMin,
                                                                                            sumMinuteMax,
                                                                                            maxMinuteMin,
                                                                                            maxMinuteMax,
                                                                                            minMinuteMin,
                                                                                            minMinuteMax,
                                                                                            avgMinuteMin,
                                                                                            avgMinuteMax,
                                                                                            reportType,
                                                                                            /*0-不需要停车时长， 1-需要停车时长*/
                                                                                            isExistDuration);
            Integer total = reportStatisticsService.getParkingSituationReportCount(start,
                                                                                    length,
                                                                                    beginTime,
                                                                                    endTime,
                                                                                    depId,
                                                                                    depName,
                                                                                    carparkId,
                                                                                    carparkName,
                                                                                    parkingDurationSection,
                                                                                    inCountMin,
                                                                                    inCountMax,
                                                                                    outCountMin,
                                                                                    outCountMax,
                                                                                    carCountMin,
                                                                                    carCountMax,
                                                                                    sumMinuteMin,
                                                                                    sumMinuteMax,
                                                                                    maxMinuteMin,
                                                                                    maxMinuteMax,
                                                                                    minMinuteMin,
                                                                                    minMinuteMax,
                                                                                    avgMinuteMin,
                                                                                    avgMinuteMax,
                                                                                    reportType,
                                                                                    /*0-不需要停车时长， 1-需要停车时长*/
                                                                                    isExistDuration);
            res.put("data", list);
            res.put("draw", draw);
            res.put("recordsTotal", total);
            res.put("recordsFiltered", total);


            this.success(res);
        }catch (BizException e){
            this.failed(res, e);
        }
        return JSON.toJSONString(res, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 收费金额分段统计报表
     * @param start
     * @param length
     * @param draw
     * @param beginTime
     * @param endTime
     * @param depId
     * @param depName
     * @param carparkId
     * @param carparkName
     * @param chargeAmountSection
     * @param chargeCountMin
     * @param chargeCountMax
     * @param availSumAmountMin
     * @param availSumAmountMax
     * @param availMaxAmountMin
     * @param availMaxAmountMax
     * @param availMinAmountMin
     * @param availMinAmountMax
     * @param availAvgAmountMin
     * @param availAvgAmountMax
     * @param realSumAmountMin
     * @param realSumAmountMax
     * @param realMaxAmountMin
     * @param realMaxAmountMax
     * @param realMinAmountMin
     * @param realMinAmountMax
     * @param realAvgAmountMin
     * @param realAvgAmountMax
     * @param reportType
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getAmountSectionReport", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public String getAmountSectionReport(@RequestParam(value="start", required=false,defaultValue = "0") Integer start,
                                           @RequestParam(value="length", required=false,defaultValue = "10") Integer length,
                                           @RequestParam(value="draw", required=true) Integer draw,
                                           @RequestParam(value = "beginTime", required = true) String beginTime,
                                           @RequestParam(value = "endTime", required = true) String endTime,

                                           @RequestParam(value = "depId", required = false) String depId,
                                           @RequestParam(value = "depName", required = false) String depName,
                                           @RequestParam(value = "carparkId", required = false) String carparkId,
                                           @RequestParam(value = "carparkName", required = false) String carparkName,
                                           @RequestParam(value = "chargeAmountSection", required = false) String chargeAmountSection,
                                           @RequestParam(value = "chargeCountMin", required = false) Integer chargeCountMin,
                                           @RequestParam(value = "chargeCountMax", required = false) Integer chargeCountMax,
                                           @RequestParam(value = "availSumAmountMin", required = false) Float availSumAmountMin,
                                           @RequestParam(value = "availSumAmountMax", required = false) Float availSumAmountMax,
                                           @RequestParam(value = "availMaxAmountMin", required = false) Float availMaxAmountMin,
                                           @RequestParam(value = "availMaxAmountMax", required = false) Float availMaxAmountMax,
                                           @RequestParam(value = "availMinAmountMin", required = false) Float availMinAmountMin,
                                           @RequestParam(value = "availMinAmountMax", required = false) Float availMinAmountMax,
                                           @RequestParam(value = "availAvgAmountMin", required = false) Float availAvgAmountMin,
                                           @RequestParam(value = "availAvgAmountMax", required = false) Float availAvgAmountMax,
                                           @RequestParam(value = "realSumAmountMin", required = false) Float realSumAmountMin,
                                           @RequestParam(value = "realSumAmountMax", required = false) Float realSumAmountMax,
                                           @RequestParam(value = "realMaxAmountMin", required = false) Float realMaxAmountMin,
                                           @RequestParam(value = "realMaxAmountMax", required = false) Float realMaxAmountMax,
                                           @RequestParam(value = "realMinAmountMin", required = false) Float realMinAmountMin,
                                           @RequestParam(value = "realMinAmountMax", required = false) Float realMinAmountMax,
                                           @RequestParam(value = "realAvgAmountMin", required = false) Float realAvgAmountMin,
                                           @RequestParam(value = "realAvgAmountMax", required = false) Float realAvgAmountMax,
                                           @RequestParam(value = "reportType", required = false) String reportType){
        Map<String, Object> res = new HashedMap();
        try{

            List<Map<String, Object>> list = reportStatisticsService.getAmountSectionReport(start,
                                                                                            length,
                                                                                            beginTime,
                                                                                            endTime,
                                                                                            depId,
                                                                                            depName,
                                                                                            carparkId,
                                                                                            carparkName,
                                                                                            chargeAmountSection,
                                                                                            chargeCountMin,
                                                                                            chargeCountMax,
                                                                                            availSumAmountMin,
                                                                                            availSumAmountMax,
                                                                                            availMaxAmountMin,
                                                                                            availMaxAmountMax,
                                                                                            availMinAmountMin,
                                                                                            availMinAmountMax,
                                                                                            availAvgAmountMin,
                                                                                            availAvgAmountMax,
                                                                                            realSumAmountMin,
                                                                                            realSumAmountMax,
                                                                                            realMaxAmountMin,
                                                                                            realMaxAmountMax,
                                                                                            realMinAmountMin,
                                                                                            realMinAmountMax,
                                                                                            realAvgAmountMin,
                                                                                            realAvgAmountMax,
                                                                                            reportType);
            Integer total = reportStatisticsService.getAmountSectionReportCount(start,
                                                                                length,
                                                                                beginTime,
                                                                                endTime,
                                                                                depId,
                                                                                depName,
                                                                                carparkId,
                                                                                carparkName,
                                                                                chargeAmountSection,
                                                                                chargeCountMin,
                                                                                chargeCountMax,
                                                                                availSumAmountMin,
                                                                                availSumAmountMax,
                                                                                availMaxAmountMin,
                                                                                availMaxAmountMax,
                                                                                availMinAmountMin,
                                                                                availMinAmountMax,
                                                                                availAvgAmountMin,
                                                                                availAvgAmountMax,
                                                                                realSumAmountMin,
                                                                                realSumAmountMax,
                                                                                realMaxAmountMin,
                                                                                realMaxAmountMax,
                                                                                realMinAmountMin,
                                                                                realMinAmountMax,
                                                                                realAvgAmountMin,
                                                                                realAvgAmountMax,
                                                                                reportType);
            res.put("data", list);
            res.put("draw", draw);
            res.put("recordsTotal", total);
            res.put("recordsFiltered", total);
            this.success(res);

        }catch (BizException e){
            this.failed(res, e);
        }
        return JSON.toJSONString(res, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 车辆属性收费分析统计表
     * @param start
     * @param length
     * @param draw
     * @param beginTime
     * @param endTime
     * @param depId
     * @param depName
     * @param carparkId
     * @param carparkName
     * @param carType
     * @param chargeCountMin
     * @param chargeCountMax
     * @param availSumAmountMin
     * @param availSumAmountMax
     * @param availMaxAmountMin
     * @param availMaxAmountMax
     * @param availMinAmountMin
     * @param availMinAmountMax
     * @param availAvgAmountMin
     * @param availAvgAmountMax
     * @param realSumAmountMin
     * @param realSumAmountMax
     * @param realMaxAmountMin
     * @param realMaxAmountMax
     * @param realMinAmountMin
     * @param realMinAmountMax
     * @param realAvgAmountMin
     * @param realAvgAmountMax
     * @param reportType
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getAttributeChargeReport", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public String getAttributeChargeReport(@RequestParam(value="start", required=false,defaultValue = "0") Integer start,
                                           @RequestParam(value="length", required=false,defaultValue = "10") Integer length,
                                           @RequestParam(value="draw", required=true) Integer draw,
                                           @RequestParam(value = "beginTime", required = true) String beginTime,
                                           @RequestParam(value = "endTime", required = true) String endTime,

                                           @RequestParam(value = "depId", required = false) String depId,
                                           @RequestParam(value = "depName", required = false) String depName,
                                           @RequestParam(value = "carparkId", required = false) String carparkId,
                                           @RequestParam(value = "carparkName", required = false) String carparkName,
                                           @RequestParam(value = "carType", required = false) String carType,
                                           @RequestParam(value = "chargeCountMin", required = false) Integer chargeCountMin,
                                           @RequestParam(value = "chargeCountMax", required = false) Integer chargeCountMax,
                                           @RequestParam(value = "availSumAmountMin", required = false) Float availSumAmountMin,
                                           @RequestParam(value = "availSumAmountMax", required = false) Float availSumAmountMax,
                                           @RequestParam(value = "availMaxAmountMin", required = false) Float availMaxAmountMin,
                                           @RequestParam(value = "availMaxAmountMax", required = false) Float availMaxAmountMax,
                                           @RequestParam(value = "availMinAmountMin", required = false) Float availMinAmountMin,
                                           @RequestParam(value = "availMinAmountMax", required = false) Float availMinAmountMax,
                                           @RequestParam(value = "availAvgAmountMin", required = false) Float availAvgAmountMin,
                                           @RequestParam(value = "availAvgAmountMax", required = false) Float availAvgAmountMax,
                                           @RequestParam(value = "realSumAmountMin", required = false) Float realSumAmountMin,
                                           @RequestParam(value = "realSumAmountMax", required = false) Float realSumAmountMax,
                                           @RequestParam(value = "realMaxAmountMin", required = false) Float realMaxAmountMin,
                                           @RequestParam(value = "realMaxAmountMax", required = false) Float realMaxAmountMax,
                                           @RequestParam(value = "realMinAmountMin", required = false) Float realMinAmountMin,
                                           @RequestParam(value = "realMinAmountMax", required = false) Float realMinAmountMax,
                                           @RequestParam(value = "realAvgAmountMin", required = false) Float realAvgAmountMin,
                                           @RequestParam(value = "realAvgAmountMax", required = false) Float realAvgAmountMax,
                                           @RequestParam(value = "reportType", required = false) String reportType){
        Map<String, Object> res = new HashedMap();
        try{

            List<Map<String, Object>> list = reportStatisticsService.getAttributeChargeReport(start,
                                                                                            length,
                                                                                            beginTime,
                                                                                            endTime,
                                                                                            depId,
                                                                                            depName,
                                                                                            carparkId,
                                                                                            carparkName,
                                                                                            carType,
                                                                                            chargeCountMin,
                                                                                            chargeCountMax,
                                                                                            availSumAmountMin,
                                                                                            availSumAmountMax,
                                                                                            availMaxAmountMin,
                                                                                            availMaxAmountMax,
                                                                                            availMinAmountMin,
                                                                                            availMinAmountMax,
                                                                                            availAvgAmountMin,
                                                                                            availAvgAmountMax,
                                                                                            realSumAmountMin,
                                                                                            realSumAmountMax,
                                                                                            realMaxAmountMin,
                                                                                            realMaxAmountMax,
                                                                                            realMinAmountMin,
                                                                                            realMinAmountMax,
                                                                                            realAvgAmountMin,
                                                                                            realAvgAmountMax,
                                                                                            reportType);
            Integer total = reportStatisticsService.getAttributeChargeReportCount(start,
                                                                                length,
                                                                                beginTime,
                                                                                endTime,
                                                                                depId,
                                                                                depName,
                                                                                carparkId,
                                                                                carparkName,
                                                                                carType,
                                                                                chargeCountMin,
                                                                                chargeCountMax,
                                                                                availSumAmountMin,
                                                                                availSumAmountMax,
                                                                                availMaxAmountMin,
                                                                                availMaxAmountMax,
                                                                                availMinAmountMin,
                                                                                availMinAmountMax,
                                                                                availAvgAmountMin,
                                                                                availAvgAmountMax,
                                                                                realSumAmountMin,
                                                                                realSumAmountMax,
                                                                                realMaxAmountMin,
                                                                                realMaxAmountMax,
                                                                                realMinAmountMin,
                                                                                realMinAmountMax,
                                                                                realAvgAmountMin,
                                                                                realAvgAmountMax,
                                                                                reportType);
            res.put("data", list);
            res.put("draw", draw);
            res.put("recordsTotal", total);
            res.put("recordsFiltered", total);
            this.success(res);

        }catch (BizException e){
            this.failed(res, e);
        }
        return JSON.toJSONString(res, SerializerFeature.WriteMapNullValue);
    }

    @ResponseBody
    @RequestMapping(value = "getChargeTypeReport", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public String getChargeTypeReport(@RequestParam(value="start", required=false,defaultValue = "1") Integer start,
                                      @RequestParam(value="length", required=false,defaultValue = "10") Integer length,
                                      @RequestParam(value="draw", required=true) Integer draw,
                                      @RequestParam(value = "beginTime", required = true) String beginTime,
                                      @RequestParam(value = "endTime", required = true) String endTime,

                                      @RequestParam(value = "depId", required = false) String depId,
                                      @RequestParam(value = "depName", required = false) String depName,
                                      @RequestParam(value = "carparkId", required = false) String carparkId,
                                      @RequestParam(value = "carparkName", required = false) String carparkName,
                                      @RequestParam(value = "chargeSubType", required = false) String chargeSubType,
                                      @RequestParam(value = "chargeCountMin", required = false) Integer chargeCountMin,
                                      @RequestParam(value = "chargeCountMax", required = false) Integer chargeCountMax,
                                      @RequestParam(value = "availSumAmountMin", required = false) Float availSumAmountMin,
                                      @RequestParam(value = "availSumAmountMax", required = false) Float availSumAmountMax,
                                      @RequestParam(value = "availMaxAmountMin", required = false) Float availMaxAmountMin,
                                      @RequestParam(value = "availMaxAmountMax", required = false) Float availMaxAmountMax,
                                      @RequestParam(value = "availMinAmountMin", required = false) Float availMinAmountMin,
                                      @RequestParam(value = "availMinAmountMax", required = false) Float availMinAmountMax,
                                      @RequestParam(value = "availAvgAmountMin", required = false) Float availAvgAmountMin,
                                      @RequestParam(value = "availAvgAmountMax", required = false) Float availAvgAmountMax,
                                      @RequestParam(value = "realSumAmountMin", required = false) Float realSumAmountMin,
                                      @RequestParam(value = "realSumAmountMax", required = false) Float realSumAmountMax,
                                      @RequestParam(value = "realMaxAmountMin", required = false) Float realMaxAmountMin,
                                      @RequestParam(value = "realMaxAmountMax", required = false) Float realMaxAmountMax,
                                      @RequestParam(value = "realMinAmountMin", required = false) Float realMinAmountMin,
                                      @RequestParam(value = "realMinAmountMax", required = false) Float realMinAmountMax,
                                      @RequestParam(value = "realAvgAmountMin", required = false) Float realAvgAmountMin,
                                      @RequestParam(value = "realAvgAmountMax", required = false) Float realAvgAmountMax,
                                      @RequestParam(value = "reportType", required = false) String reportType){
        Map<String, Object> res = new HashedMap();
        try{

            List<Map<String, Object>> list = reportStatisticsService.getChargeTypeReport(start,
                                                                                        length,
                                                                                        beginTime,
                                                                                        endTime,
                                                                                        depId,
                                                                                        depName,
                                                                                        carparkId,
                                                                                        carparkName,
                                                                                        chargeSubType,
                                                                                        chargeCountMin,
                                                                                        chargeCountMax,
                                                                                        availSumAmountMin,
                                                                                        availSumAmountMax,
                                                                                        availMaxAmountMin,
                                                                                        availMaxAmountMax,
                                                                                        availMinAmountMin,
                                                                                        availMinAmountMax,
                                                                                        availAvgAmountMin,
                                                                                        availAvgAmountMax,
                                                                                        realSumAmountMin,
                                                                                        realSumAmountMax,
                                                                                        realMaxAmountMin,
                                                                                        realMaxAmountMax,
                                                                                        realMinAmountMin,
                                                                                        realMinAmountMax,
                                                                                        realAvgAmountMin,
                                                                                        realAvgAmountMax,
                                                                                        reportType);
            Integer total = reportStatisticsService.getChargeTypeReportCount(start,
                                                                                length,
                                                                                beginTime,
                                                                                endTime,
                                                                                depId,
                                                                                depName,
                                                                                carparkId,
                                                                                carparkName,
                                                                                chargeSubType,
                                                                                chargeCountMin,
                                                                                chargeCountMax,
                                                                                availSumAmountMin,
                                                                                availSumAmountMax,
                                                                                availMaxAmountMin,
                                                                                availMaxAmountMax,
                                                                                availMinAmountMin,
                                                                                availMinAmountMax,
                                                                                availAvgAmountMin,
                                                                                availAvgAmountMax,
                                                                                realSumAmountMin,
                                                                                realSumAmountMax,
                                                                                realMaxAmountMin,
                                                                                realMaxAmountMax,
                                                                                realMinAmountMin,
                                                                                realMinAmountMax,
                                                                                realAvgAmountMin,
                                                                                realAvgAmountMax,
                                                                                reportType);
            res.put("data", list);
            res.put("draw", draw);
            res.put("recordsTotal", total);
            res.put("recordsFiltered", total);
            this.success(res);

        }catch (BizException e){
            this.failed(res, e);
        }
        return JSON.toJSONString(res, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 岗亭收费报表
     * @param start
     * @param length
     * @param draw
     * @param beginTime
     * @param endTime
     * @param depId
     * @param depName
     * @param carparkId
     * @param carparkName
     * @param boothId
     * @param boothName
     * @param chargeCountMin
     * @param chargeCountMax
     * @param availSumAmountMin
     * @param availSumAmountMax
     * @param availMaxAmountMin
     * @param availMaxAmountMax
     * @param availMinAmountMin
     * @param availMinAmountMax
     * @param availAvgAmountMin
     * @param availAvgAmountMax
     * @param realSumAmountMin
     * @param realSumAmountMax
     * @param realMaxAmountMin
     * @param realMaxAmountMax
     * @param realMinAmountMin
     * @param realMinAmountMax
     * @param realAvgAmountMin
     * @param realAvgAmountMax
     * @param reportType
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getBoothChargeReport", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public String getBoothChargeReport(@RequestParam(value="start", required=false,defaultValue = "0") Integer start,
                                       @RequestParam(value="length", required=false,defaultValue = "10") Integer length,
                                       @RequestParam(value="draw", required=true) Integer draw,
                                       @RequestParam(value = "beginTime", required = true) String beginTime,
                                       @RequestParam(value = "endTime", required = true) String endTime,

                                       @RequestParam(value = "depId", required = false) String depId,
                                       @RequestParam(value = "depName", required = false) String depName,
                                       @RequestParam(value = "carparkId", required = false) String carparkId,
                                       @RequestParam(value = "carparkName", required = false) String carparkName,
                                       @RequestParam(value = "boothId", required = false) String boothId,
                                       @RequestParam(value = "boothName", required = false) String boothName,
                                       @RequestParam(value = "chargeCountMin", required = false) Integer chargeCountMin,
                                       @RequestParam(value = "chargeCountMax", required = false) Integer chargeCountMax,
                                       @RequestParam(value = "availSumAmountMin", required = false) Float availSumAmountMin,
                                       @RequestParam(value = "availSumAmountMax", required = false) Float availSumAmountMax,
                                       @RequestParam(value = "availMaxAmountMin", required = false) Float availMaxAmountMin,
                                       @RequestParam(value = "availMaxAmountMax", required = false) Float availMaxAmountMax,
                                       @RequestParam(value = "availMinAmountMin", required = false) Float availMinAmountMin,
                                       @RequestParam(value = "availMinAmountMax", required = false) Float availMinAmountMax,
                                       @RequestParam(value = "availAvgAmountMin", required = false) Float availAvgAmountMin,
                                       @RequestParam(value = "availAvgAmountMax", required = false) Float availAvgAmountMax,
                                       @RequestParam(value = "realSumAmountMin", required = false) Float realSumAmountMin,
                                       @RequestParam(value = "realSumAmountMax", required = false) Float realSumAmountMax,
                                       @RequestParam(value = "realMaxAmountMin", required = false) Float realMaxAmountMin,
                                       @RequestParam(value = "realMaxAmountMax", required = false) Float realMaxAmountMax,
                                       @RequestParam(value = "realMinAmountMin", required = false) Float realMinAmountMin,
                                       @RequestParam(value = "realMinAmountMax", required = false) Float realMinAmountMax,
                                       @RequestParam(value = "realAvgAmountMin", required = false) Float realAvgAmountMin,
                                       @RequestParam(value = "realAvgAmountMax", required = false) Float realAvgAmountMax,
                                       @RequestParam(value = "reportType", required = false) String reportType){
        Map<String, Object> res = new HashedMap();
        try{

            List<Map<String, Object>> list = reportStatisticsService.getBoothChargesReport(start,
                                                                                           length,
                                                                                           beginTime,
                                                                                           endTime,
                                                                                           depId,
                                                                                           depName,
                                                                                           carparkId,
                                                                                           carparkName,
                                                                                           boothId,
                                                                                           boothName,
                                                                                           chargeCountMin,
                                                                                           chargeCountMax,
                                                                                           availSumAmountMin,
                                                                                           availSumAmountMax,
                                                                                           availMaxAmountMin,
                                                                                           availMaxAmountMax,
                                                                                           availMinAmountMin,
                                                                                           availMinAmountMax,
                                                                                           availAvgAmountMin,
                                                                                           availAvgAmountMax,
                                                                                           realSumAmountMin,
                                                                                           realSumAmountMax,
                                                                                           realMaxAmountMin,
                                                                                           realMaxAmountMax,
                                                                                           realMinAmountMin,
                                                                                           realMinAmountMax,
                                                                                           realAvgAmountMin,
                                                                                           realAvgAmountMax,
                                                                                           reportType);
            Integer total = reportStatisticsService.getBoothChargesReportCount(start,
                                                                                length,
                                                                                beginTime,
                                                                                endTime,
                                                                                depId,
                                                                                depName,
                                                                                carparkId,
                                                                                carparkName,
                                                                                boothId,
                                                                                boothName,
                                                                                chargeCountMin,
                                                                                chargeCountMax,
                                                                                availSumAmountMin,
                                                                                availSumAmountMax,
                                                                                availMaxAmountMin,
                                                                                availMaxAmountMax,
                                                                                availMinAmountMin,
                                                                                availMinAmountMax,
                                                                                availAvgAmountMin,
                                                                                availAvgAmountMax,
                                                                                realSumAmountMin,
                                                                                realSumAmountMax,
                                                                                realMaxAmountMin,
                                                                                realMaxAmountMax,
                                                                                realMinAmountMin,
                                                                                realMinAmountMax,
                                                                                realAvgAmountMin,
                                                                                realAvgAmountMax,
                                                                                reportType);
            res.put("data", list);
            res.put("draw", draw);
            res.put("recordsTotal", total);
            res.put("recordsFiltered", total);
            this.success(res);

        }catch (BizException e){
            this.failed(res, e);
        }
        return JSON.toJSONString(res, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 收费情况统计报表
     * @param start
     * @param length
     * @param draw
     * @param beginTime
     * @param endTime
     * @param chargeReceiveCarNo
     * @param depName
     * @param carparkName
     * @param chargeReceiveSubType
     * @param chargeReceiveAmountMin
     * @param chargeReceiveAmountMax
     * @param chargeDiscountInfo
     * @param chargeReceivePostName
     * @param memo
     * @param operatorName
     * @param chargeReceiveId
     * @param carparkId
     * @param depId
     * @param chargeReceivePost
     * @param chargeReceiveOperatorId
     * @param chargeReceiveCarpark
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getAllChargeReport", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map getAllChargeReport(
                                     @RequestParam(value="start", required=false,defaultValue = "0") Integer start,
                                     @RequestParam(value="length", required=false,defaultValue = "10") Integer length,
                                     @RequestParam(value="draw", required=true) Integer draw,
                                     @RequestParam(value = "beginTime", required = true) String beginTime,
                                     @RequestParam(value = "endTime", required = true) String endTime,
                                     @RequestParam(value = "chargeReceiveCarNo", required = false) String chargeReceiveCarNo,
                                     @RequestParam(value = "depName", required = false) String depName,
                                     @RequestParam(value = "carparkName", required = false) String carparkName,
                                     @RequestParam(value = "chargeReceiveSubType", required = false) String chargeReceiveSubType,
                                     @RequestParam(value = "chargeReceiveAmountMin", required = false) Float chargeReceiveAmountMin,
                                     @RequestParam(value = "chargeReceiveAmountMax", required = false) Float chargeReceiveAmountMax,
                                     @RequestParam(value = "chargeDiscountInfo", required = false) String chargeDiscountInfo,
                                     @RequestParam(value = "chargeReceivePostName", required = false) String chargeReceivePostName,
                                     @RequestParam(value = "memo", required = false) String memo,
                                     @RequestParam(value = "operatorName", required = false) String operatorName,
                                     @RequestParam(value = "chargeReceiveId", required = false) String chargeReceiveId,
                                     @RequestParam(value = "carparkId", required = false) String carparkId,
                                     @RequestParam(value = "depId", required = false) String depId,
                                     @RequestParam(value = "chargeReceivePost", required = false) String chargeReceivePost,
                                     @RequestParam(value = "chargeReceiveOperatorId", required = false) String chargeReceiveOperatorId,
                                     @RequestParam(value = "chargeReceiveCarpark", required = false) String chargeReceiveCarpark){
        Map<String,Object> res = new HashedMap();
        try{
            if(!("").equals(operatorName) && !CommonUtils.isEmpty(operatorName) &&
               !("").equals(chargeReceiveOperatorId) && !CommonUtils.isEmpty(chargeReceiveOperatorId)){
                if(chargeReceiveOperatorId.equals("-2") || chargeReceiveOperatorId.equals("1")){
                    operatorName = "";
                }
            }
            /*if(!chargeReceivePostName.equals("") && !CommonUtils.isEmpty(chargeReceivePostName) &&
               !chargeReceivePost.equals("") && !CommonUtils.isEmpty(chargeReceivePost)){
                if(chargeReceivePost.equals("0ae8172b7957a7a295cb0c291d204688") || chargeReceiveOperatorId.equals("WECHAT_PAY")){
                    chargeReceivePostName = "";
                }
            }*/
            //Integer end = length + start -1;
            List<Map<String, Object>> list = reportStatisticsService.getAllChargesReport(start,
                                                                                         length,
                                                                                         beginTime,
                                                                                         endTime,
                                                                                         chargeReceiveCarNo,
                                                                                         depName,
                                                                                         carparkName,
                                                                                         chargeReceiveSubType,
                                                                                         chargeReceiveAmountMin,
                                                                                         chargeReceiveAmountMax,
                                                                                         chargeDiscountInfo,
                                                                                         chargeReceivePostName,
                                                                                         memo,
                                                                                         operatorName,
                                                                                         chargeReceiveId,
                                                                                         carparkId,
                                                                                         depId,
                                                                                         chargeReceivePost,
                                                                                         chargeReceiveOperatorId,
                                                                                         chargeReceiveCarpark);
            Integer total = reportStatisticsService.getAllChargesReportCount(start,
                                                                            length,
                                                                            beginTime,
                                                                            endTime,
                                                                            chargeReceiveCarNo,
                                                                            depName,
                                                                            carparkName,
                                                                            chargeReceiveSubType,
                                                                            chargeReceiveAmountMin,
                                                                            chargeReceiveAmountMax,
                                                                            chargeDiscountInfo,
                                                                            chargeReceivePostName,
                                                                            memo,
                                                                            operatorName,
                                                                            chargeReceiveId,
                                                                            carparkId,
                                                                            depId,
                                                                            chargeReceivePost,
                                                                            chargeReceiveOperatorId,
                                                                            chargeReceiveCarpark);

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

    //导出收费情况统计报表1
    @RequestMapping(value = "getVehicleExcel", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public void exportVehicleExcel(
            @RequestParam(value="sheetName", required=false,defaultValue = "XSL") String sheetName,
            @RequestParam(value = "beginTime", required = true) String beginTime,
            @RequestParam(value = "endTime", required = true) String endTime,
            @RequestParam(value = "chargeReceiveCarNo", required = false) String chargeReceiveCarNo,
            @RequestParam(value = "depName", required = false) String depName,
            @RequestParam(value = "carparkName", required = false) String carparkName,
            @RequestParam(value = "chargeReceiveSubType", required = false) String chargeReceiveSubType,
            @RequestParam(value = "chargeReceiveAmountMin", required = false) Float chargeReceiveAmountMin,
            @RequestParam(value = "chargeReceiveAmountMax", required = false) Float chargeReceiveAmountMax,
            @RequestParam(value = "chargeDiscountInfo", required = false) String chargeDiscountInfo,
            @RequestParam(value = "chargeReceivePostName", required = false) String chargeReceivePostName,
            @RequestParam(value = "memo", required = false) String memo,
            @RequestParam(value = "operatorName", required = false) String operatorName,
            @RequestParam(value = "chargeReceiveId", required = false) String chargeReceiveId,
            @RequestParam(value = "carparkId", required = false) String carparkId,
            @RequestParam(value = "depId", required = false) String depId,
            @RequestParam(value = "chargeReceivePost", required = false) String chargeReceivePost,
            @RequestParam(value = "chargeReceiveOperatorId", required = false) String chargeReceiveOperatorId,
            @RequestParam(value = "chargeReceiveCarpark", required = false) String chargeReceiveCarpark,
            HttpServletResponse response
    ){
       /* try {
            sheetName = java.net.URLDecoder.decode(sheetName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        Timestamp exportBeginTime = new Timestamp(System.currentTimeMillis());
        Timestamp exportEndTime = new Timestamp(System.currentTimeMillis());
        try{
            exportBeginTime = Timestamp.valueOf(beginTime);
            exportEndTime = Timestamp.valueOf(endTime);
        }catch (Exception e){
            e.printStackTrace();
        }

        List<Map<String,Object>> headList = new ArrayList<Map<String, Object>>(
                Arrays.asList(
                        new HashMap<String, Object>(){{
                            put("key","chargeReceiveCarNo");
                            put("name","车牌号");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","depName");
                            put("name","单位名称");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","carparkName");
                            put("name","车场名称");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","chargeReceiveSubType");
                            put("name","收费类型");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","chargeRealAmount");
                            put("name","交易金额");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","chargeDiscountInfo");
                            put("name","优惠方式");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","chargeReceivePostName");
                            put("name","岗亭名称");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","chargeReceiveTime");
                            put("name","交易时间");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","memo");
                            put("name","备注");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","operatorName");
                            put("name","操作员姓名");
                        }}
                )
        );

        List vehicledataList = reportStatisticsService.getAllChargesReport(null,
                null,
                beginTime,
                endTime,
                chargeReceiveCarNo,
                depName,
                carparkName,
                chargeReceiveSubType,
                chargeReceiveAmountMin,
                chargeReceiveAmountMax,
                chargeDiscountInfo,
                chargeReceivePostName,
                memo,
                operatorName,
                chargeReceiveId,
                carparkId,
                depId,
                chargeReceivePost,
                chargeReceiveOperatorId,
                chargeReceiveCarpark);
        PoiExcelUtil.ListMapExpotToExcel(sheetName,exportBeginTime,exportEndTime,headList,vehicledataList,response);
    }

    //导出岗亭收费情况分析表2
    @RequestMapping(value = "getBoothChargeExcel", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public void exportBoothChargeExcel(
            @RequestParam(value="sheetName", required=false,defaultValue = "XSL") String sheetName,
            @RequestParam(value = "beginTime", required = true) String beginTime,
            @RequestParam(value = "endTime", required = true) String endTime,

            @RequestParam(value = "depId", required = false) String depId,
            @RequestParam(value = "depName", required = false) String depName,
            @RequestParam(value = "carparkId", required = false) String carparkId,
            @RequestParam(value = "carparkName", required = false) String carparkName,
            @RequestParam(value = "boothId", required = false) String boothId,
            @RequestParam(value = "boothName", required = false) String boothName,
            @RequestParam(value = "chargeCountMin", required = false) Integer chargeCountMin,
            @RequestParam(value = "chargeCountMax", required = false) Integer chargeCountMax,
            @RequestParam(value = "availSumAmountMin", required = false) Float availSumAmountMin,
            @RequestParam(value = "availSumAmountMax", required = false) Float availSumAmountMax,
            @RequestParam(value = "availMaxAmountMin", required = false) Float availMaxAmountMin,
            @RequestParam(value = "availMaxAmountMax", required = false) Float availMaxAmountMax,
            @RequestParam(value = "availMinAmountMin", required = false) Float availMinAmountMin,
            @RequestParam(value = "availMinAmountMax", required = false) Float availMinAmountMax,
            @RequestParam(value = "availAvgAmountMin", required = false) Float availAvgAmountMin,
            @RequestParam(value = "availAvgAmountMax", required = false) Float availAvgAmountMax,
            @RequestParam(value = "realSumAmountMin", required = false) Float realSumAmountMin,
            @RequestParam(value = "realSumAmountMax", required = false) Float realSumAmountMax,
            @RequestParam(value = "realMaxAmountMin", required = false) Float realMaxAmountMin,
            @RequestParam(value = "realMaxAmountMax", required = false) Float realMaxAmountMax,
            @RequestParam(value = "realMinAmountMin", required = false) Float realMinAmountMin,
            @RequestParam(value = "realMinAmountMax", required = false) Float realMinAmountMax,
            @RequestParam(value = "realAvgAmountMin", required = false) Float realAvgAmountMin,
            @RequestParam(value = "realAvgAmountMax", required = false) Float realAvgAmountMax,
            @RequestParam(value = "reportType", required = false) String reportType,
            HttpServletResponse response
    ){
        Timestamp exportBeginTime = new Timestamp(System.currentTimeMillis());
        Timestamp exportEndTime = new Timestamp(System.currentTimeMillis());
        try{
            exportBeginTime = Timestamp.valueOf(beginTime);
            exportEndTime = Timestamp.valueOf(endTime);
        }catch (Exception e){
            e.printStackTrace();
        }

        List<Map<String,Object>> headList = new ArrayList<Map<String, Object>>(
                Arrays.asList(
                        new HashMap<String, Object>(){{
                            put("key","boothName");
                            put("name","岗亭名称");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","statisticsTime");
                            put("name","时间");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","chargeCount");
                            put("name","收费次数");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","availSumAmount");
                            put("name","应收总金额");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","availMaxAmount");
                            put("name","单笔最大金额（应收）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","availMinAmout");
                            put("name","单笔最小金额（应收）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","availAvgAmout");
                            put("name","平均收费金额（应收）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","realSumAmount");
                            put("name","实收总金额");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","realMaxAmount");
                            put("name","单笔最大金额（实收）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","realMinAmout");
                            put("name","单笔最小金额（实收）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","realAvgAmout");
                            put("name","平均收费金额（实收）");
                        }}
                )
        );

        List boothChargedataList = reportStatisticsService.getBoothChargesReport(null,
                null,
                beginTime,
                endTime,
                depId,
                depName,
                carparkId,
                carparkName,
                boothId,
                boothName,
                chargeCountMin,
                chargeCountMax,
                availSumAmountMin,
                availSumAmountMax,
                availMaxAmountMin,
                availMaxAmountMax,
                availMinAmountMin,
                availMinAmountMax,
                availAvgAmountMin,
                availAvgAmountMax,
                realSumAmountMin,
                realSumAmountMax,
                realMaxAmountMin,
                realMaxAmountMax,
                realMinAmountMin,
                realMinAmountMax,
                realAvgAmountMin,
                realAvgAmountMax,
                reportType);
        PoiExcelUtil.ListMapExpotToExcel(sheetName,exportBeginTime,exportEndTime,headList,boothChargedataList,response);
    }

    //导出收费类型统计分析表3
    @RequestMapping(value = "getChargeTypeExcel", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public void exportChargeTypeExcel(
            @RequestParam(value="sheetName", required=false,defaultValue = "XSL") String sheetName,
            @RequestParam(value = "beginTime", required = true) String beginTime,
            @RequestParam(value = "endTime", required = true) String endTime,

            @RequestParam(value = "depId", required = false) String depId,
            @RequestParam(value = "depName", required = false) String depName,
            @RequestParam(value = "carparkId", required = false) String carparkId,
            @RequestParam(value = "carparkName", required = false) String carparkName,
            @RequestParam(value = "chargeSubType", required = false) String chargeSubType,
            @RequestParam(value = "chargeCountMin", required = false) Integer chargeCountMin,
            @RequestParam(value = "chargeCountMax", required = false) Integer chargeCountMax,
            @RequestParam(value = "availSumAmountMin", required = false) Float availSumAmountMin,
            @RequestParam(value = "availSumAmountMax", required = false) Float availSumAmountMax,
            @RequestParam(value = "availMaxAmountMin", required = false) Float availMaxAmountMin,
            @RequestParam(value = "availMaxAmountMax", required = false) Float availMaxAmountMax,
            @RequestParam(value = "availMinAmountMin", required = false) Float availMinAmountMin,
            @RequestParam(value = "availMinAmountMax", required = false) Float availMinAmountMax,
            @RequestParam(value = "availAvgAmountMin", required = false) Float availAvgAmountMin,
            @RequestParam(value = "availAvgAmountMax", required = false) Float availAvgAmountMax,
            @RequestParam(value = "realSumAmountMin", required = false) Float realSumAmountMin,
            @RequestParam(value = "realSumAmountMax", required = false) Float realSumAmountMax,
            @RequestParam(value = "realMaxAmountMin", required = false) Float realMaxAmountMin,
            @RequestParam(value = "realMaxAmountMax", required = false) Float realMaxAmountMax,
            @RequestParam(value = "realMinAmountMin", required = false) Float realMinAmountMin,
            @RequestParam(value = "realMinAmountMax", required = false) Float realMinAmountMax,
            @RequestParam(value = "realAvgAmountMin", required = false) Float realAvgAmountMin,
            @RequestParam(value = "realAvgAmountMax", required = false) Float realAvgAmountMax,
            @RequestParam(value = "reportType", required = false) String reportType,
            HttpServletResponse response
    ){
        Timestamp exportBeginTime = new Timestamp(System.currentTimeMillis());
        Timestamp exportEndTime = new Timestamp(System.currentTimeMillis());
        try{
            exportBeginTime = Timestamp.valueOf(beginTime);
            exportEndTime = Timestamp.valueOf(endTime);
        }catch (Exception e){
            e.printStackTrace();
        }

        List<Map<String,Object>> headList = new ArrayList<Map<String, Object>>(
                Arrays.asList(
                        new HashMap<String, Object>(){{
                            put("key","chargeSubType");
                            put("name","收费类型");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","statisticsTime");
                            put("name","时间");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","chargeCount");
                            put("name","收费次数");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","availSumAmount");
                            put("name","应收总金额");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","availMaxAmount");
                            put("name","单笔最大金额（应收）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","availMinAmout");
                            put("name","单笔最小金额（应收）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","availAvgAmout");
                            put("name","平均收费金额（应收）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","realSumAmount");
                            put("name","实收总金额");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","realMaxAmount");
                            put("name","单笔最大金额（实收）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","realMinAmout");
                            put("name","单笔最小金额（实收）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","realAvgAmout");
                            put("name","平均收费金额（实收）");
                        }}
                )
        );

        List chargeTypedataList = reportStatisticsService.getChargeTypeReport(null,
                null,
                beginTime,
                endTime,
                depId,
                depName,
                carparkId,
                carparkName,
                chargeSubType,
                chargeCountMin,
                chargeCountMax,
                availSumAmountMin,
                availSumAmountMax,
                availMaxAmountMin,
                availMaxAmountMax,
                availMinAmountMin,
                availMinAmountMax,
                availAvgAmountMin,
                availAvgAmountMax,
                realSumAmountMin,
                realSumAmountMax,
                realMaxAmountMin,
                realMaxAmountMax,
                realMinAmountMin,
                realMinAmountMax,
                realAvgAmountMin,
                realAvgAmountMax,
                reportType);
        PoiExcelUtil.ListMapExpotToExcel(sheetName,exportBeginTime,exportEndTime,headList,chargeTypedataList,response);
    }

    //导出车辆属性收费分析表4
    @RequestMapping(value = "getAttributeChargeExcel", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public void exportAttributeChargeExcel(
            @RequestParam(value="sheetName", required=false,defaultValue = "XSL") String sheetName,
            @RequestParam(value = "beginTime", required = true) String beginTime,
            @RequestParam(value = "endTime", required = true) String endTime,

            @RequestParam(value = "depId", required = false) String depId,
            @RequestParam(value = "depName", required = false) String depName,
            @RequestParam(value = "carparkId", required = false) String carparkId,
            @RequestParam(value = "carparkName", required = false) String carparkName,
            @RequestParam(value = "carType", required = false) String carType,
            @RequestParam(value = "chargeCountMin", required = false) Integer chargeCountMin,
            @RequestParam(value = "chargeCountMax", required = false) Integer chargeCountMax,
            @RequestParam(value = "availSumAmountMin", required = false) Float availSumAmountMin,
            @RequestParam(value = "availSumAmountMax", required = false) Float availSumAmountMax,
            @RequestParam(value = "availMaxAmountMin", required = false) Float availMaxAmountMin,
            @RequestParam(value = "availMaxAmountMax", required = false) Float availMaxAmountMax,
            @RequestParam(value = "availMinAmountMin", required = false) Float availMinAmountMin,
            @RequestParam(value = "availMinAmountMax", required = false) Float availMinAmountMax,
            @RequestParam(value = "availAvgAmountMin", required = false) Float availAvgAmountMin,
            @RequestParam(value = "availAvgAmountMax", required = false) Float availAvgAmountMax,
            @RequestParam(value = "realSumAmountMin", required = false) Float realSumAmountMin,
            @RequestParam(value = "realSumAmountMax", required = false) Float realSumAmountMax,
            @RequestParam(value = "realMaxAmountMin", required = false) Float realMaxAmountMin,
            @RequestParam(value = "realMaxAmountMax", required = false) Float realMaxAmountMax,
            @RequestParam(value = "realMinAmountMin", required = false) Float realMinAmountMin,
            @RequestParam(value = "realMinAmountMax", required = false) Float realMinAmountMax,
            @RequestParam(value = "realAvgAmountMin", required = false) Float realAvgAmountMin,
            @RequestParam(value = "realAvgAmountMax", required = false) Float realAvgAmountMax,
            @RequestParam(value = "reportType", required = false) String reportType,
            HttpServletResponse response
    ){
        Timestamp exportBeginTime = new Timestamp(System.currentTimeMillis());
        Timestamp exportEndTime = new Timestamp(System.currentTimeMillis());
        try{
            exportBeginTime = Timestamp.valueOf(beginTime);
            exportEndTime = Timestamp.valueOf(endTime);
        }catch (Exception e){
            e.printStackTrace();
        }

        List<Map<String,Object>> headList = new ArrayList<Map<String, Object>>(
                Arrays.asList(
                        new HashMap<String, Object>(){{
                            put("key","carType");
                            put("name","车辆属性");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","statisticsTime");
                            put("name","时间");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","chargeCount");
                            put("name","收费次数");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","availSumAmount");
                            put("name","应收总金额");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","availMaxAmount");
                            put("name","单笔最大金额（应收）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","availMinAmout");
                            put("name","单笔最小金额（应收）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","availAvgAmout");
                            put("name","平均收费金额（应收）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","realSumAmount");
                            put("name","实收总金额");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","realMaxAmount");
                            put("name","单笔最大金额（实收）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","realMinAmout");
                            put("name","单笔最小金额（实收）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","realAvgAmout");
                            put("name","平均收费金额（实收）");
                        }}
                )
        );

        List attributeChargedataList = reportStatisticsService.getAttributeChargeReport(null,
                null,
                beginTime,
                endTime,
                depId,
                depName,
                carparkId,
                carparkName,
                carType,
                chargeCountMin,
                chargeCountMax,
                availSumAmountMin,
                availSumAmountMax,
                availMaxAmountMin,
                availMaxAmountMax,
                availMinAmountMin,
                availMinAmountMax,
                availAvgAmountMin,
                availAvgAmountMax,
                realSumAmountMin,
                realSumAmountMax,
                realMaxAmountMin,
                realMaxAmountMax,
                realMinAmountMin,
                realMinAmountMax,
                realAvgAmountMin,
                realAvgAmountMax,
                reportType);
        PoiExcelUtil.ListMapExpotToExcel(sheetName,exportBeginTime,exportEndTime,headList,attributeChargedataList,response);
    }

    //导出收费金额分段统计表5
    @RequestMapping(value = "getAmountSubsectionExcel", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public void exportAmountSubsectionExcel(
            @RequestParam(value="sheetName", required=false,defaultValue = "XSL") String sheetName,
            @RequestParam(value = "beginTime", required = true) String beginTime,
            @RequestParam(value = "endTime", required = true) String endTime,

            @RequestParam(value = "depId", required = false) String depId,
            @RequestParam(value = "depName", required = false) String depName,
            @RequestParam(value = "carparkId", required = false) String carparkId,
            @RequestParam(value = "carparkName", required = false) String carparkName,
            @RequestParam(value = "chargeAmountSection", required = false) String chargeAmountSection,
            @RequestParam(value = "chargeCountMin", required = false) Integer chargeCountMin,
            @RequestParam(value = "chargeCountMax", required = false) Integer chargeCountMax,
            @RequestParam(value = "availSumAmountMin", required = false) Float availSumAmountMin,
            @RequestParam(value = "availSumAmountMax", required = false) Float availSumAmountMax,
            @RequestParam(value = "availMaxAmountMin", required = false) Float availMaxAmountMin,
            @RequestParam(value = "availMaxAmountMax", required = false) Float availMaxAmountMax,
            @RequestParam(value = "availMinAmountMin", required = false) Float availMinAmountMin,
            @RequestParam(value = "availMinAmountMax", required = false) Float availMinAmountMax,
            @RequestParam(value = "availAvgAmountMin", required = false) Float availAvgAmountMin,
            @RequestParam(value = "availAvgAmountMax", required = false) Float availAvgAmountMax,
            @RequestParam(value = "realSumAmountMin", required = false) Float realSumAmountMin,
            @RequestParam(value = "realSumAmountMax", required = false) Float realSumAmountMax,
            @RequestParam(value = "realMaxAmountMin", required = false) Float realMaxAmountMin,
            @RequestParam(value = "realMaxAmountMax", required = false) Float realMaxAmountMax,
            @RequestParam(value = "realMinAmountMin", required = false) Float realMinAmountMin,
            @RequestParam(value = "realMinAmountMax", required = false) Float realMinAmountMax,
            @RequestParam(value = "realAvgAmountMin", required = false) Float realAvgAmountMin,
            @RequestParam(value = "realAvgAmountMax", required = false) Float realAvgAmountMax,
            @RequestParam(value = "reportType", required = false) String reportType,
            HttpServletResponse response
    ){
        Timestamp exportBeginTime = new Timestamp(System.currentTimeMillis());
        Timestamp exportEndTime = new Timestamp(System.currentTimeMillis());
        try{
            exportBeginTime = Timestamp.valueOf(beginTime);
            exportEndTime = Timestamp.valueOf(endTime);
        }catch (Exception e){
            e.printStackTrace();
        }

        List<Map<String,Object>> headList = new ArrayList<Map<String, Object>>(
                Arrays.asList(
                        new HashMap<String, Object>(){{
                            put("key","chargeAmountSection");
                            put("name","收费金额");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","statisticsTime");
                            put("name","时间");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","chargeCount");
                            put("name","收费次数");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","availSumAmount");
                            put("name","应收总金额");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","availMaxAmount");
                            put("name","单笔最大金额（应收）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","availMinAmout");
                            put("name","单笔最小金额（应收）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","availAvgAmout");
                            put("name","平均收费金额（应收）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","realSumAmount");
                            put("name","实收总金额");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","realMaxAmount");
                            put("name","单笔最大金额（实收）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","realMinAmout");
                            put("name","单笔最小金额（实收）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","realAvgAmout");
                            put("name","平均收费金额（实收）");
                        }}
                )
        );

        List amountSubsectiondataList = reportStatisticsService.getAmountSectionReport(null,
                null,
                beginTime,
                endTime,
                depId,
                depName,
                carparkId,
                carparkName,
                chargeAmountSection,
                chargeCountMin,
                chargeCountMax,
                availSumAmountMin,
                availSumAmountMax,
                availMaxAmountMin,
                availMaxAmountMax,
                availMinAmountMin,
                availMinAmountMax,
                availAvgAmountMin,
                availAvgAmountMax,
                realSumAmountMin,
                realSumAmountMax,
                realMaxAmountMin,
                realMaxAmountMax,
                realMinAmountMin,
                realMinAmountMax,
                realAvgAmountMin,
                realAvgAmountMax,
                reportType);
        PoiExcelUtil.ListMapExpotToExcel(sheetName,exportBeginTime,exportEndTime,headList,amountSubsectiondataList,response);
    }

    //导出停车情况统计报表6
    @RequestMapping(value = "getParkingSituationExcel", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public void exportParkingSituationExcel(
            @RequestParam(value="sheetName", required=false,defaultValue = "XSL") String sheetName,
            @RequestParam(value = "beginTime", required = true) String beginTime,
            @RequestParam(value = "endTime", required = true) String endTime,

            @RequestParam(value = "depId", required = false) String depId,
            @RequestParam(value = "depName", required = false) String depName,
            @RequestParam(value = "carparkId", required = false) String carparkId,
            @RequestParam(value = "carparkName", required = false) String carparkName,

            @RequestParam(value = "parkingDurationSection", required = false) String parkingDurationSection,
            @RequestParam(value = "inCountMin", required = false) Integer inCountMin,
            @RequestParam(value = "inCountMax", required = false) Integer inCountMax,
            @RequestParam(value = "outCountMin", required = false) Integer outCountMin,
            @RequestParam(value = "outCountMax", required = false) Integer outCountMax,
            @RequestParam(value = "carCountMin", required = false) Integer carCountMin,
            @RequestParam(value = "carCountMax", required = false) Integer carCountMax,
            @RequestParam(value = "sumMinuteMin", required = false) Integer sumMinuteMin,
            @RequestParam(value = "sumMinuteMax", required = false) Integer sumMinuteMax,
            @RequestParam(value = "maxMinuteMin", required = false) Integer maxMinuteMin,
            @RequestParam(value = "maxMinuteMax", required = false) Integer maxMinuteMax,
            @RequestParam(value = "minMinuteMin", required = false) Integer minMinuteMin,
            @RequestParam(value = "minMinuteMax", required = false) Integer minMinuteMax,
            @RequestParam(value = "avgMinuteMin", required = false) Integer avgMinuteMin,
            @RequestParam(value = "avgMinuteMax", required = false) Integer avgMinuteMax,
            @RequestParam(value = "reportType", required = false) String reportType,
                                           /*0-不需要停车时长， 1-需要停车时长*/
            @RequestParam(value = "isExistDuration", required = false) String isExistDuration,
            HttpServletResponse response
    ){
        Timestamp exportBeginTime = new Timestamp(System.currentTimeMillis());
        Timestamp exportEndTime = new Timestamp(System.currentTimeMillis());
        try{
            exportBeginTime = Timestamp.valueOf(beginTime);
            exportEndTime = Timestamp.valueOf(endTime);
        }catch (Exception e){
            e.printStackTrace();
        }

        List<Map<String,Object>> headList = new ArrayList<Map<String, Object>>(
                Arrays.asList(
                        new HashMap<String, Object>(){{
                            put("key","statisticsTime");
                            put("name","时间");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","inCount");
                            put("name","进场次数");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","outCount");
                            put("name","出场次数");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","carCount");
                            put("name","进出车辆数");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","sumMinute");
                            put("name","停车时长（分钟）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","maxMinute");
                            put("name","最大值（分钟）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","minMinute");
                            put("name","最小值（分钟）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","avgMinute");
                            put("name","平均值（分钟）");
                        }}
                )
        );

        List parkingSituationdataList = reportStatisticsService.getParkingSituationReport(null,
                null,
                beginTime,
                endTime,
                depId,
                depName,
                carparkId,
                carparkName,
                parkingDurationSection,
                inCountMin,
                inCountMax,
                outCountMin,
                outCountMax,
                carCountMin,
                carCountMax,
                sumMinuteMin,
                sumMinuteMax,
                maxMinuteMin,
                maxMinuteMax,
                minMinuteMin,
                minMinuteMax,
                avgMinuteMin,
                avgMinuteMax,
                reportType,
                                                                                            /*0-不需要停车时长， 1-需要停车时长*/
                isExistDuration);

        PoiExcelUtil.ListMapExpotToExcel(sheetName,exportBeginTime,exportEndTime,headList,parkingSituationdataList,response);
    }

    //导出停车时长分段统计表7
    @RequestMapping(value = "getParkingLengthExcel", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public void exportParkingLengthExcel(
            @RequestParam(value="sheetName", required=false,defaultValue = "XSL") String sheetName,
            @RequestParam(value = "beginTime", required = true) String beginTime,
            @RequestParam(value = "endTime", required = true) String endTime,

            @RequestParam(value = "depId", required = false) String depId,
            @RequestParam(value = "depName", required = false) String depName,
            @RequestParam(value = "carparkId", required = false) String carparkId,
            @RequestParam(value = "carparkName", required = false) String carparkName,

            @RequestParam(value = "parkingDurationSection", required = false) String parkingDurationSection,
            @RequestParam(value = "inCountMin", required = false) Integer inCountMin,
            @RequestParam(value = "inCountMax", required = false) Integer inCountMax,
            @RequestParam(value = "outCountMin", required = false) Integer outCountMin,
            @RequestParam(value = "outCountMax", required = false) Integer outCountMax,
            @RequestParam(value = "carCountMin", required = false) Integer carCountMin,
            @RequestParam(value = "carCountMax", required = false) Integer carCountMax,
            @RequestParam(value = "sumMinuteMin", required = false) Integer sumMinuteMin,
            @RequestParam(value = "sumMinuteMax", required = false) Integer sumMinuteMax,
            @RequestParam(value = "maxMinuteMin", required = false) Integer maxMinuteMin,
            @RequestParam(value = "maxMinuteMax", required = false) Integer maxMinuteMax,
            @RequestParam(value = "minMinuteMin", required = false) Integer minMinuteMin,
            @RequestParam(value = "minMinuteMax", required = false) Integer minMinuteMax,
            @RequestParam(value = "avgMinuteMin", required = false) Integer avgMinuteMin,
            @RequestParam(value = "avgMinuteMax", required = false) Integer avgMinuteMax,
            @RequestParam(value = "reportType", required = false) String reportType,
                                           /*0-不需要停车时长， 1-需要停车时长*/
            @RequestParam(value = "isExistDuration", required = false) String isExistDuration,
            HttpServletResponse response
    ){
        Timestamp exportBeginTime = new Timestamp(System.currentTimeMillis());
        Timestamp exportEndTime = new Timestamp(System.currentTimeMillis());
        try{
            exportBeginTime = Timestamp.valueOf(beginTime);
            exportEndTime = Timestamp.valueOf(endTime);
        }catch (Exception e){
            e.printStackTrace();
        }

        List<Map<String,Object>> headList = new ArrayList<Map<String, Object>>(
                Arrays.asList(
                        new HashMap<String, Object>(){{
                            put("key","parkingDurationSection");
                            put("name","停车时长");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","statisticsTime");
                            put("name","时间");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","inCount");
                            put("name","进场次数");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","outCount");
                            put("name","出场次数");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","carCount");
                            put("name","进出车辆数");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","sumMinute");
                            put("name","停车时长（分钟）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","maxMinute");
                            put("name","最大值（分钟）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","minMinute");
                            put("name","最小值（分钟）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","avgMinute");
                            put("name","平均值（分钟）");
                        }}
                )
        );

        List parkingLengthdataList = reportStatisticsService.getParkingSituationReport(null,
                null,
                beginTime,
                endTime,
                depId,
                depName,
                carparkId,
                carparkName,
                parkingDurationSection,
                inCountMin,
                inCountMax,
                outCountMin,
                outCountMax,
                carCountMin,
                carCountMax,
                sumMinuteMin,
                sumMinuteMax,
                maxMinuteMin,
                maxMinuteMax,
                minMinuteMin,
                minMinuteMax,
                avgMinuteMin,
                avgMinuteMax,
                reportType,
                                                                                                                                                                /*0-不需要停车时长， 1-需要停车时长*/
                isExistDuration);
        PoiExcelUtil.ListMapExpotToExcel(sheetName,exportBeginTime,exportEndTime,headList,parkingLengthdataList,response);
    }

    //导出车辆属性车流统计表8
    @RequestMapping(value = "getVehicleAttributeExcel", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public void exportVehicleAttributeExcel(
            @RequestParam(value="sheetName", required=false,defaultValue = "XSL") String sheetName,
            @RequestParam(value = "beginTime", required = true) String beginTime,
            @RequestParam(value = "endTime", required = true) String endTime,

            @RequestParam(value = "depId", required = false) String depId,
            @RequestParam(value = "depName", required = false) String depName,
            @RequestParam(value = "carparkId", required = false) String carparkId,
            @RequestParam(value = "carparkName", required = false) String carparkName,

            @RequestParam(value = "carType", required = false) String carType,
            @RequestParam(value = "inCountMin", required = false) Integer inCountMin,
            @RequestParam(value = "inCountMax", required = false) Integer inCountMax,
            @RequestParam(value = "outCountMin", required = false) Integer outCountMin,
            @RequestParam(value = "outCountMax", required = false) Integer outCountMax,
            @RequestParam(value = "carCountMin", required = false) Integer carCountMin,
            @RequestParam(value = "carCountMax", required = false) Integer carCountMax,
            @RequestParam(value = "sumMinuteMin", required = false) Integer sumMinuteMin,
            @RequestParam(value = "sumMinuteMax", required = false) Integer sumMinuteMax,
            @RequestParam(value = "maxMinuteMin", required = false) Integer maxMinuteMin,
            @RequestParam(value = "maxMinuteMax", required = false) Integer maxMinuteMax,
            @RequestParam(value = "minMinuteMin", required = false) Integer minMinuteMin,
            @RequestParam(value = "minMinuteMax", required = false) Integer minMinuteMax,
            @RequestParam(value = "avgMinuteMin", required = false) Integer avgMinuteMin,
            @RequestParam(value = "avgMinuteMax", required = false) Integer avgMinuteMax,
            @RequestParam(value = "reportType", required = false) String reportType,
                                           /*0-不需要停车时长， 1-需要停车时长*/
            @RequestParam(value = "isExistDuration", required = false) String isExistDuration,
            HttpServletResponse response
    ){
        Timestamp exportBeginTime = new Timestamp(System.currentTimeMillis());
        Timestamp exportEndTime = new Timestamp(System.currentTimeMillis());
        try{
            exportBeginTime = Timestamp.valueOf(beginTime);
            exportEndTime = Timestamp.valueOf(endTime);
        }catch (Exception e){
            e.printStackTrace();
        }

        List<Map<String,Object>> headList = new ArrayList<Map<String, Object>>(
                Arrays.asList(
                        new HashMap<String, Object>(){{
                            put("key","carType");
                            put("name","车辆属性");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","statisticsTime");
                            put("name","时间");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","inCount");
                            put("name","进场次数");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","outCount");
                            put("name","出场次数");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","carCount");
                            put("name","进出车辆数");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","sumMinute");
                            put("name","停车时长（分钟）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","maxMinute");
                            put("name","最大值（分钟）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","minMinute");
                            put("name","最小值（分钟）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","avgMinute");
                            put("name","平均值（分钟）");
                        }}
                )
        );

        List vehicleAttributedataList = reportStatisticsService.getVehicleAttributeReport(null,
                null,
                beginTime,
                endTime,
                depId,
                depName,
                carparkId,
                carparkName,
                carType,
                inCountMin,
                inCountMax,
                outCountMin,
                outCountMax,
                carCountMin,
                carCountMax,
                sumMinuteMin,
                sumMinuteMax,
                maxMinuteMin,
                maxMinuteMax,
                minMinuteMin,
                minMinuteMax,
                avgMinuteMin,
                avgMinuteMax,
                reportType);
        PoiExcelUtil.ListMapExpotToExcel(sheetName,exportBeginTime,exportEndTime,headList,vehicleAttributedataList,response);
    }

    //导出岗亭车流统计报表9
    @RequestMapping(value = "getBoxCarExcel", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public void exportBoxCarExcel(
            @RequestParam(value="sheetName", required=false,defaultValue = "XSL") String sheetName,
            @RequestParam(value = "beginTime", required = true) String beginTime,
            @RequestParam(value = "endTime", required = true) String endTime,

            @RequestParam(value = "depId", required = false) String depId,
            @RequestParam(value = "depName", required = false) String depName,
            @RequestParam(value = "carparkId", required = false) String carparkId,
            @RequestParam(value = "carparkName", required = false) String carparkName,

            @RequestParam(value = "boothId", required = false) String boothId,
            @RequestParam(value = "boothName", required = false) String boothName,
            @RequestParam(value = "inCountMin", required = false) Integer inCountMin,
            @RequestParam(value = "inCountMax", required = false) Integer inCountMax,
            @RequestParam(value = "outCountMin", required = false) Integer outCountMin,
            @RequestParam(value = "outCountMax", required = false) Integer outCountMax,
            @RequestParam(value = "carCountMin", required = false) Integer carCountMin,
            @RequestParam(value = "carCountMax", required = false) Integer carCountMax,
            @RequestParam(value = "sumMinuteMin", required = false) Integer sumMinuteMin,
            @RequestParam(value = "sumMinuteMax", required = false) Integer sumMinuteMax,
            @RequestParam(value = "maxMinuteMin", required = false) Integer maxMinuteMin,
            @RequestParam(value = "maxMinuteMax", required = false) Integer maxMinuteMax,
            @RequestParam(value = "minMinuteMin", required = false) Integer minMinuteMin,
            @RequestParam(value = "minMinuteMax", required = false) Integer minMinuteMax,
            @RequestParam(value = "avgMinuteMin", required = false) Integer avgMinuteMin,
            @RequestParam(value = "avgMinuteMax", required = false) Integer avgMinuteMax,
            @RequestParam(value = "reportType", required = false) String reportType,
            HttpServletResponse response
    ){
        Timestamp exportBeginTime = new Timestamp(System.currentTimeMillis());
        Timestamp exportEndTime = new Timestamp(System.currentTimeMillis());
        try{
            exportBeginTime = Timestamp.valueOf(beginTime);
            exportEndTime = Timestamp.valueOf(endTime);
        }catch (Exception e){
            e.printStackTrace();
        }

        List<Map<String,Object>> headList = new ArrayList<Map<String, Object>>(
                Arrays.asList(
                        new HashMap<String, Object>(){{
                            put("key","boothName");
                            put("name","岗亭名称");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","statisticsTime");
                            put("name","时间");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","inCount");
                            put("name","进场次数");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","outCount");
                            put("name","出场次数");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","carCount");
                            put("name","进出车辆数");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","sumMinute");
                            put("name","停车时长（分钟）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","maxMinute");
                            put("name","最大值（分钟）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","minMinute");
                            put("name","最小值（分钟）");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","avgMinute");
                            put("name","平均值（分钟）");
                        }}
                )
        );

        List boxCardataList = reportStatisticsService.getBoothVehicleFlowReport(null,
                null,
                beginTime,
                endTime,
                depId,
                depName,
                carparkId,
                carparkName,
                boothId,
                boothName,
                inCountMin,
                inCountMax,
                outCountMin,
                outCountMax,
                carCountMin,
                carCountMax,
                sumMinuteMin,
                sumMinuteMax,
                maxMinuteMin,
                maxMinuteMax,
                minMinuteMin,
                minMinuteMax,
                avgMinuteMin,
                avgMinuteMax,
                reportType);
        PoiExcelUtil.ListMapExpotToExcel(sheetName,exportBeginTime,exportEndTime,headList,boxCardataList,response);
    }



    //预缴费
    @ResponseBody
    @RequestMapping(value = "prePayment",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map prePayment(@RequestParam(value="start", required=true,defaultValue = "0") Integer start,
                     @RequestParam(value="length", required=true,defaultValue = "10") Integer length,
                     @RequestParam(value = "orderOrechargeCarno", required = false,defaultValue = "") String orderOrechargeCarno,
                     @RequestParam(value = "chargeMemberKindName", required = false,defaultValue = "") String chargeMemberKindName,
                     @RequestParam(value = "orderPrechargeCarparkName", required = false,defaultValue = "") String orderPrechargeCarparkName,
                     @RequestParam(value = "startTime", required = false,defaultValue = "") String startTime,
                     @RequestParam(value = "endTime", required = false,defaultValue = "") String endTime,
                     @RequestParam(value = "excelStart", required = false,defaultValue = "") String excelStart,
                     @RequestParam(value = "excelEnd", required = false,defaultValue = "") String excelEnd,
                     @RequestParam(value="draw", required=false,defaultValue = "") Integer draw){
        Map res = new HashedMap();
        try {
            Timestamp beginTime = CommonUtils.getTodayStartTimeStamp();
            if (!CommonUtils.isEmpty(startTime)) {
                beginTime = Timestamp.valueOf(startTime.replaceAll("/", "-"));
            }
            Timestamp overTime = CommonUtils.getTodayEndTimeStamp();
            if (!CommonUtils.isEmpty(endTime)) {
                overTime = Timestamp.valueOf(endTime.replaceAll("/", "-"));
            }
            if (beginTime.getTime() > overTime.getTime()) {
                throw new BizException("有效开始时间不得大于有效结束时间！");
            }
            List<Map<String,Object>> typeList=reportStatisticsService.prePaymentType();
            Map type=new HashMap();
            type.put("typeList",typeList);
            List list=new ArrayList();
            list=reportStatisticsService.prePayment(start,length,orderOrechargeCarno,chargeMemberKindName,orderPrechargeCarparkName,
                    CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",beginTime),CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",overTime),excelStart,excelEnd);
            Long total = reportStatisticsService.countPrePayment(orderOrechargeCarno,chargeMemberKindName,orderPrechargeCarparkName,
                    CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",beginTime),CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",overTime),excelStart,excelEnd);
            res.put("data",list);
            res.put("draw",draw);
            res.put("recordsTotal", total);
            res.put("recordsFiltered", total);
            this.success(res);
        }catch (BizException e) {
            this.failed(res, e);
        }
        return res;
    }


    //获取预缴费类型
    @ResponseBody
    @RequestMapping(value = "prePaymentType",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map prePaymentType(@RequestBody Map params){
        Map res = new HashedMap();
        try {

            List<Map<String,Object>> typeList=reportStatisticsService.prePaymentType();
            res.put("typeList",typeList);
            this.success(res);
        }catch (BizException e) {
            this.failed(res, e);
        }
        return res;
    }


    //预缴费
    @RequestMapping(value ="prePaymentExcel")
    public void prePaymentExcel(@RequestParam(value = "orderOrechargeCarno", required = false,defaultValue = "") String orderOrechargeCarno,
                                @RequestParam(value = "chargeMemberKindName", required = false,defaultValue = "") String chargeMemberKindName,
                                @RequestParam(value = "orderPrechargeCarparkName", required = false,defaultValue = "") String orderPrechargeCarparkName,
                                @RequestParam(value = "excelStart", required = false,defaultValue = "") String excelStart,
                                @RequestParam(value = "excelEnd", required = false,defaultValue = "") String excelEnd,
                                @RequestParam(value = "startTime", required = false,defaultValue = "") String startTime,
                                @RequestParam(value = "endTime", required = false,defaultValue = "") String endTime,
                                HttpServletResponse response){
        Map res = new HashedMap();
        try {
            List list=new ArrayList();
            String sheetName="预缴费统计报表";
            Timestamp exportBeginTime = new Timestamp(System.currentTimeMillis());
            Timestamp exportEndTime = new Timestamp(System.currentTimeMillis());
            try{
                exportBeginTime = Timestamp.valueOf(excelStart);
                exportEndTime = Timestamp.valueOf(excelEnd);
            }catch (Exception e){
                e.printStackTrace();
            }

            List<Map<String,Object>> headList = new ArrayList<Map<String, Object>>(
                    Arrays.asList(
                            new HashMap<String, Object>(){{
                                put("key","order_precharge_carno");
                                put("name","车牌号");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","order_precharge_carpark_name");
                                put("name","车场名称");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","charge_member_kind_name");
                                put("name","缴费类型");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","order_precharge_operator_name");
                                put("name","操作员姓名");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","order_precharge_receivable_amount");
                                put("name","应收金额");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","order_precharge_actual_amount");
                                put("name","实收金额");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","order_precharge_free_amount");
                                put("name","免费金额");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","free_reason");
                                put("name","免费原因");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","memo");
                                put("name","备注");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","order_precharge_time");
                                put("name","缴费时间");
                            }}
                    )
            );
            list=reportStatisticsService.prePayment(null,null,orderOrechargeCarno,chargeMemberKindName,orderPrechargeCarparkName,startTime,endTime,excelStart,excelEnd);
            PoiExcelUtil.ListMapExpotToExcel(sheetName,exportBeginTime,exportEndTime,headList,list,response);
            this.success(res);
        }catch (Exception e){
        e.printStackTrace();
    }

}

    @ResponseBody
    @RequestMapping(value = "paySelect",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map paySelect(){
        Map res = new HashedMap();
        Map data = new HashedMap();
        List<Map<String,Object>> payStatusSelect = new ArrayList<>();
        for (payStatusEnum e : payStatusEnum.values()) {
            Map<String,Object> map = new HashedMap();
            map.put("name",e.name());
            map.put("desc",e.getDesc());
            payStatusSelect.add(map);
        }
        data.put("payStatusSelect",payStatusSelect);
        List<Map<String,Object>> payTypeSelect = new ArrayList<>();
        for (payTypeEnum e : payTypeEnum.values()) {
            Map<String,Object> map = new HashedMap();
            if (e.name().equals("A") || e.name().equals("C") || e.name().equals("E") || e.name().equals("G") || e.name().equals("H")) {
                map.put("name", e.name());
                map.put("desc", e.getDesc());
                payTypeSelect.add(map);
            }
        }
        data.put("payTypeSelect",payTypeSelect);
        res.put("data", data);
        this.success(res);
        return res;
    }


    /**
     * 流水表
     */
    @ResponseBody
    @RequestMapping(value = "tranSaction",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map tranSaction(@RequestParam(value="start", required=false,defaultValue = "0") Integer start,
                          @RequestParam(value="length", required=false,defaultValue = "10") Integer length,
                          @RequestParam(value = "startTime", required = false) String startTime,
                          @RequestParam(value = "endTime", required = false) String endTime,
                          @RequestParam(value = "payType", required = false) String payType,
                          @RequestParam(value = "payStatus", required = false) String payStatus,
                          @RequestParam(value = "excelStart", required = false) String excelStart,
                          @RequestParam(value = "excelEnd", required = false) String excelEnd,
                          @RequestParam(value = "markInfo", required = false) String markInfo,
                          @RequestParam(value="carparkName", required=false) String carparkName,
                           @RequestParam(value="carno", required=false) String carno,
                          @RequestParam(value = "tag", required = false)  String tag,
                          @RequestParam(value="draw", required=false) Integer draw,
                          HttpSession session){
        Map res = new HashedMap();
        try {
            session.setAttribute("startTime", startTime);
            session.setAttribute("endTime", endTime);
            session.setAttribute("payType", payType);
            session.setAttribute("payStatus", payStatus);
            session.setAttribute("excelStart", excelStart);
            session.setAttribute("excelEnd", excelEnd);
            session.setAttribute("markInfo", markInfo);
            session.setAttribute("carparkName", carparkName);
            session.setAttribute("carno", carno);

            if (!CommonUtils.isEmpty(tag))
            {
                start=0;
                length=null;
            }

            List list;
            list=reportStatisticsService.tranSaction(start,length,startTime,endTime,payType,payStatus,excelStart,excelEnd,markInfo,carparkName,carno);
            Long total = reportStatisticsService.countTranSaction(startTime,endTime,payType,payStatus,excelStart,excelEnd,markInfo,carparkName,carno);
            res.put("data",list);
            res.put("draw",draw);
            res.put("recordsTotal", total);
            res.put("recordsFiltered", total);

            this.success(res);
        }catch (BizException e) {
            this.failed(res, e);
        }
        return res;
    }

    /**
     * 流水表报表
     */
    @RequestMapping(value = "tranSactionExcel")
    public void tranSactionExcel(HttpServletResponse response,HttpSession session){
        Map res = new HashedMap();
        try {

            String startTime= (String)session.getAttribute("startTime");
            String endTime= (String)session.getAttribute("endTime");
            String payType= (String)session.getAttribute("payType");
            String payStatus= (String)session.getAttribute("payStatus");
            String excelStart= (String)session.getAttribute("excelStart");
            String excelEnd= (String)session.getAttribute("excelEnd");
            String markInfo= (String)session.getAttribute("markInfo");
            String carparkName= (String)session.getAttribute("carparkName");
            String carno= (String)session.getAttribute("carno");



            List list=new ArrayList();
            String sheetName="交易流水统计报表";
            Timestamp exportBeginTime = new Timestamp(System.currentTimeMillis());
            Timestamp exportEndTime = new Timestamp(System.currentTimeMillis());
            try{
                exportBeginTime = Timestamp.valueOf(excelStart);
                exportEndTime = Timestamp.valueOf(excelEnd);
            }catch (Exception e){
                e.printStackTrace();
            }

            List<Map<String,Object>> headList = new ArrayList<Map<String, Object>>(
                    Arrays.asList(
                            new HashMap<String, Object>(){{
                                put("key","car_no");
                                put("name","车牌号码");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","carpark_name");
                                put("name","车场名称");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","pay_type");
                                put("name","支付类型");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","pay_time");
                                put("name","支付时间");
                            }},
                            /*new HashMap<String, Object>(){{
                                put("key","pay_status");
                                put("name","支付状态");
                            }},*/

                            new HashMap<String, Object>(){{
                                put("key","total_fee");
                                put("name","总金额");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","transaction_mark");
                                put("name","备注");
                            }}
                    )
            );
            list=reportStatisticsService.tranSactionList(startTime,endTime,payType,payStatus,excelStart,excelEnd,markInfo,carparkName,carno);
            PoiExcelUtil.ListMapExpotToExcel(sheetName,exportBeginTime,exportEndTime,headList,list,response);
            this.success(res);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 获取交易流水表各项金额
     * @param startTime
     * @param endTime
     * @param payType
     * @param payStatus
     * @param markInfo
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getTransactionFee",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map getTransactionFee(@RequestParam(value = "startTime", required = false) String startTime,
                                 @RequestParam(value = "endTime", required = false) String endTime,
                                 @RequestParam(value = "payType", required = false) String payType,
                                 @RequestParam(value = "payStatus", required = false) String payStatus,
                                 @RequestParam(value = "markInfo", required = false) String markInfo,
                                 @RequestParam(value = "carparkName", required = false) String carparkName,
                                 @RequestParam(value = "carno", required = false) String carno,
                                 HttpSession session){
        Map res = new HashedMap();
        try {
            session.setAttribute("startTime", startTime);
            session.setAttribute("endTime", endTime);
            session.setAttribute("payType", payType);
            session.setAttribute("payStatus", payStatus);
            session.setAttribute("excelStart", startTime);
            session.setAttribute("excelEnd", endTime);
            session.setAttribute("markInfo", markInfo);
            session.setAttribute("carparkName", carparkName);
            session.setAttribute("carno", carno);
            res = reportStatisticsService.getTranSactionFee(startTime,endTime,payType,payStatus,startTime,endTime,markInfo,carparkName,carno);
            this.success(res);
        }catch (BizException e) {
            this.failed(res, e);
        }
        return res;

    }


     /* 根据车场获取当前单位信息
     * carparkId：车场编号；*/
    @ResponseBody
    @RequestMapping(value = "getDeptId", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public String getDeptId(@RequestBody Map params){
        Map ret = new HashedMap();
        try{
            String carparkId = (String) params.get("carparkId");
            Map s= reportStatisticsService.getDeptId(carparkId);
            Map s2=new HashMap();
            List list=new ArrayList();
            String getDeptId=(String) s.get("department_id");
            s2.put("departmentId",getDeptId);
            list.add(s2);
            ret.put("data", list);
            this.success(ret);
        }catch (BizException e){
            this.failed(ret, e);
        }
        return  JSON.toJSONString(ret);
    }
}
