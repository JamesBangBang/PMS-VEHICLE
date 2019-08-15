package com.starnetsecurity.parkClientServer.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.web.MediaTypes;
import com.starnetsecurity.parkClientServer.service.CarparkService;
import com.starnetsecurity.parkClientServer.service.GetIndexDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by JAMESBANG on 2018/8/24.
 * 综合平台获取数据的接口
 */
@Controller
public class GetIndexDataController {
    @Autowired
    CarparkService carparkService;

    @Autowired
    GetIndexDataService getIndexDataService;

    /**
     * 获取首页车辆管理模块的文字数据
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "index/percentToVehicle", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map getPercentToVehicle() throws Exception {
        JSONObject res = new JSONObject();
        res.put("status",200);
        res.put("message","");

        JSONObject data = new JSONObject();
        JSONObject percentObjData = new JSONObject();

        JSONObject inVehicle = new JSONObject();
        Integer inCountToday = getIndexDataService.getInCount(0);
        inVehicle.put("text","进车流(辆车)");
        inVehicle.put("data",inCountToday);
        percentObjData.put("inVehicle",inVehicle);

        JSONObject percentInVehicle = new JSONObject();
        Integer inCountYesterday = getIndexDataService.getInCount(1);
        percentInVehicle.put("text","同比昨天");
        if (inCountToday.equals(0))
            percentInVehicle.put("data","0%");
        else
            percentInVehicle.put("data",new BigDecimal((double)(inCountToday-inCountYesterday)*100/inCountToday).setScale(1, BigDecimal.ROUND_HALF_UP)+"%");
        percentObjData.put("percentInVehicle",percentInVehicle);

        JSONObject outVehicle = new JSONObject();
        Integer outCountToday = getIndexDataService.getOutCount(0);
        outVehicle.put("text","出车流(辆车)");
        outVehicle.put("data",outCountToday);
        percentObjData.put("outVehicle",outVehicle);

        JSONObject percentOutVehicle = new JSONObject();
        Integer outCountYesterday = getIndexDataService.getOutCount(1);
        percentOutVehicle.put("text","同比昨天");
        if (outCountToday.equals(0))
            percentOutVehicle.put("data","0%");
        else
            percentOutVehicle.put("data",new BigDecimal((double)(outCountToday-outCountYesterday)*100/outCountToday).setScale(1, BigDecimal.ROUND_HALF_UP)+"%");
        percentObjData.put("percentOutVehicle",percentOutVehicle);

        JSONObject retention = new JSONObject();
        Integer retentionCountToday = inCountToday - outCountToday > 0 ? inCountToday - outCountToday : 0;
        retention.put("text","滞留车流(辆车)");
        retention.put("data",retentionCountToday);
        percentObjData.put("retention",retention);

        JSONObject percentRetention = new JSONObject();
        Integer retentionCountYesterday = inCountYesterday - outCountYesterday > 0 ? inCountYesterday - outCountYesterday : 0;
        String retentionPercent = retentionCountYesterday > 0 ? new BigDecimal((double)(outCountToday-outCountYesterday)*100/retentionCountYesterday).setScale(1, BigDecimal.ROUND_HALF_UP)+"%" : "0%";
        percentRetention.put("text","同比昨天");
        percentRetention.put("data",retentionPercent);
        percentObjData.put("percentRetention",percentRetention);

        JSONObject surplusPark = new JSONObject();
        Integer surplusParkCount = getIndexDataService.getSurplusPark();
        surplusPark.put("text","剩余车位：");
        surplusPark.put("data",surplusParkCount);
        percentObjData.put("surplusPark",surplusPark);

        JSONObject inOutVehicle = new JSONObject();
        inOutVehicle.put("text","驶入/驶离：");
        inOutVehicle.put("data",inCountToday + "/" + outCountToday);
        percentObjData.put("inOutVehicle",inOutVehicle);

        JSONObject percentInOutVehicle = new JSONObject();
        Integer countOne = (inCountToday * outCountYesterday - inCountYesterday * outCountToday) * outCountToday;
        Integer countTwo = outCountYesterday * outCountToday * inCountToday;
        percentInOutVehicle.put("text","同比昨天");
        percentInOutVehicle.put("data",countTwo > 0 ? new BigDecimal((double)(countOne)*100/countTwo).setScale(1, BigDecimal.ROUND_HALF_UP)+"%" : "0%");
        percentObjData.put("percentInOutVehicle",percentInOutVehicle);

        data.put("percentObjData",percentObjData);
        res.put("data",data);
        return res;
    }

    /**
     * 获取首页车辆管理模块的图表数据
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "index/rateVehicle", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map getRateVehicle() throws Exception {
        JSONObject res = new JSONObject();
        res.put("status",200);
        res.put("message","");
        JSONObject data = new JSONObject();

        JSONObject chartData = new JSONObject();
        JSONArray columns = new JSONArray();
        columns.add("type");
        columns.add("value");
        chartData.put("columns",columns);
        JSONArray rows = new JSONArray();
        JSONObject rowsData = new JSONObject();
        rowsData.put("type","空余车位预警");
        rowsData.put("value",getIndexDataService.getSurplusPark());
        rows.add(rowsData);
        chartData.put("rows",rows);
        data.put("chartData",chartData);


        JSONObject vChartOptions = new JSONObject();
        JSONObject text = new JSONObject();
        text.put("text","剩余车位预警");
        vChartOptions.put("title",text);
        data.put("vChartOptions",vChartOptions);

        res.put("data",data);
        return res;
    }

    /**
     * 获取首页车辆管理模块的折线图数据
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "index/vehicleLineData", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map getVehicleLineData() throws Exception {
        JSONObject res = new JSONObject();
        res.put("status",200);
        res.put("message","");
        JSONObject data = new JSONObject();

        JSONObject chartData = new JSONObject();
        JSONArray columns = new JSONArray();
        columns.add("date");
        columns.add("驶入车辆");
        columns.add("驶出车辆");
        chartData.put("columns",columns);

        JSONArray rows = new JSONArray();
        for (int i = 0;i < 12;i++){
            JSONObject rowsData = getIndexDataService.getInOutCountToDayByTime(i);
            rows.add(rowsData);
        }
        chartData.put("rows",rows);
        data.put("chartData",chartData);

        JSONObject vChartOptions = new JSONObject();
        JSONObject text = new JSONObject();
        text.put("text","出入车辆");
        vChartOptions.put("title",text);
        data.put("vChartOptions",vChartOptions);

        res.put("data",data);
        return res;
    }

    /**
     * 获取车辆管理模块的统计数据
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "vehicle/vehicleObjData", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map getVehicleObjData() throws Exception {
        JSONObject res = new JSONObject();
        res.put("status",200);
        res.put("message","");
        JSONObject data = new JSONObject();

        JSONObject totalVehicle = new JSONObject();
        Integer inCountToday = getIndexDataService.getInCount(0);
        totalVehicle.put("text","总车辆(辆车)");
        totalVehicle.put("data",inCountToday);
        data.put("totalVehicle",totalVehicle);

        JSONObject percentTotalVehicle = new JSONObject();
        Integer inCountYesterday = getIndexDataService.getInCount(1);
        percentTotalVehicle.put("text","同比昨天");
        if (inCountToday.equals(0))
            percentTotalVehicle.put("data","0%");
        else
            percentTotalVehicle.put("data",new BigDecimal((double)(inCountToday-inCountYesterday)*100/inCountToday).setScale(1, BigDecimal.ROUND_HALF_UP)+"%");
        data.put("percentTotalVehicle",percentTotalVehicle);

        JSONObject teacherVehicle = new JSONObject();
        Integer inSchoolCarToday = getIndexDataService.getInCountByCarType("免费车",0);
        teacherVehicle.put("text","教职工车辆");
        teacherVehicle.put("data",inSchoolCarToday);
        data.put("teacherVehicle",teacherVehicle);

        JSONObject percentTeacherVehicle = new JSONObject();
        Integer inSchoolCarYesterday = getIndexDataService.getInCountByCarType("免费车",1);
        percentTeacherVehicle.put("text","同比昨天");
        percentTeacherVehicle.put("data",inSchoolCarToday.equals(0) ? "0%" : new BigDecimal((double)(inSchoolCarToday-inSchoolCarYesterday)*100/inSchoolCarToday).setScale(1, BigDecimal.ROUND_HALF_UP)+"%");
        data.put("percentTeacherVehicle",percentTeacherVehicle);

        JSONObject schoolVehicle = new JSONObject();
        Integer inTempCarToday = getIndexDataService.getInCountByCarType("临时车",0);
        schoolVehicle.put("text","外来车辆");
        schoolVehicle.put("data",inTempCarToday);
        data.put("schoolVehicle",schoolVehicle);

        JSONObject percentSchoolVehicle = new JSONObject();
        Integer inTempCarYesterday = getIndexDataService.getInCountByCarType("临时车",1);
        percentSchoolVehicle.put("text","同比昨天");
        percentSchoolVehicle.put("data",inTempCarToday.equals(0) ? "0%" : new BigDecimal((double)(inTempCarToday-inTempCarYesterday)*100/inTempCarToday).setScale(1, BigDecimal.ROUND_HALF_UP)+"%");
        data.put("percentSchoolVehicle",percentSchoolVehicle);

        JSONObject publicVehicle = new JSONObject();
        Integer inPublicCarToday = getIndexDataService.getInCountByCarType("预约车",0);
        publicVehicle.put("text","公务车辆");
        publicVehicle.put("data",inPublicCarToday);
        data.put("publicVehicle",publicVehicle);

        JSONObject percentPublicVehicle = new JSONObject();
        Integer inPublicCarYesterday = getIndexDataService.getInCountByCarType("预约车",1);
        percentPublicVehicle.put("text","同比昨天");
        percentPublicVehicle.put("data",inPublicCarToday.equals(0) ? "0%" : new BigDecimal((double)(inPublicCarToday-inPublicCarYesterday)*100/inPublicCarToday).setScale(1, BigDecimal.ROUND_HALF_UP)+"%");
        data.put("percentPublicVehicle",percentPublicVehicle);

        JSONObject otherVehicle = new JSONObject();
        Integer inOtherCarToday = inCountToday - inSchoolCarToday - inTempCarToday - inPublicCarToday;
        otherVehicle.put("text","其他车辆");
        otherVehicle.put("data",inOtherCarToday);
        data.put("otherVehicle",otherVehicle);

        JSONObject percentOtherVehicle = new JSONObject();
        Integer inOtherCarYesterday = inCountYesterday - inSchoolCarYesterday - inTempCarYesterday - inPublicCarYesterday;
        percentOtherVehicle.put("text","同比昨天");
        percentOtherVehicle.put("data",inOtherCarToday.equals(0) ? "0%" : new BigDecimal((double)(inOtherCarToday-inOtherCarYesterday)*100/inOtherCarToday).setScale(1, BigDecimal.ROUND_HALF_UP)+"%");
        data.put("percentOtherVehicle",percentOtherVehicle);

        res.put("data",data);
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "vehicle/vehicleFunnel", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map getVehicleFunnel() throws Exception {
        JSONObject res = new JSONObject();
        res.put("status",200);
        res.put("message","");
        JSONObject data = new JSONObject();

        JSONObject chartData = new JSONObject();
        JSONArray columns = new JSONArray();
        columns.add("状态");
        columns.add("数值");
        chartData.put("columns",columns);

        JSONArray rows = new JSONArray();
        JSONObject rowWarningLine = new JSONObject();
        Integer warningLine = getIndexDataService.getWarningLine();
        rowWarningLine.put("状态","车辆警戒值");
        rowWarningLine.put("数值",warningLine);
        rows.add(rowWarningLine);

        JSONObject rowTotalPark = new JSONObject();
        Integer totalPark = getIndexDataService.getTotalPark();
        rowTotalPark.put("状态","停车位总数");
        rowTotalPark.put("数值",totalPark);
        rows.add(rowTotalPark);

        JSONObject rowSurplusPark = new JSONObject();
        Integer surplusPark = getIndexDataService.getSurplusPark();
        rowSurplusPark.put("状态","剩余停车位");
        rowSurplusPark.put("数值",surplusPark);
        rows.add(rowSurplusPark);

        JSONObject rowhistory = new JSONObject();
        Integer history = 0;
        rowhistory.put("状态","历史平均值");
        rowhistory.put("数值",history);
        rows.add(rowhistory);

        JSONObject rowDiffrence = new JSONObject();
        Integer diffrence = 0;
        rowDiffrence.put("状态","平均值差值");
        rowDiffrence.put("数值",diffrence);
        rows.add(rowDiffrence);

        JSONObject outCountInfo = new JSONObject();
        Integer outCount = getIndexDataService.getOutCount(0);
        outCountInfo.put("状态","总出校车辆");
        outCountInfo.put("数值",outCount);
        rows.add(outCountInfo);

        chartData.put("rows",rows);
        data.put("chartData",chartData);
        res.put("data",data);
        return res;
    }

    /**
     * 获取车辆管理模块的统计数据
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "vehicle/trendVehicleLine", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map getTrendVehicleLine() throws Exception {
        JSONObject res = new JSONObject();
        res.put("status",200);
        res.put("message","");
        JSONObject data = new JSONObject();

        JSONObject chartData = new JSONObject();
        JSONArray columns = new JSONArray();
        columns.add("date");
        columns.add("驶入车辆");
        columns.add("驶出车辆");
        chartData.put("columns",columns);

        JSONArray rows = new JSONArray();
        for (int i = 0;i < 12;i++){
            JSONObject rowsData = getIndexDataService.getInOutCountYesterdayByTime(i);
            rows.add(rowsData);
        }
        chartData.put("rows",rows);
        data.put("chartData",chartData);

        JSONObject vChartOptions = new JSONObject();
        JSONObject text = new JSONObject();
        text.put("text","车辆进出趋势(昨天)");
        vChartOptions.put("title",text);
        data.put("vChartOptions",vChartOptions);

        res.put("data",data);
        return res;
    }

    /**
     * 车辆管理接口
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "eduPlatIndex/carManagement", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map carManagement() throws Exception {
        JSONObject res = new JSONObject();
        res.put("status",200);
        res.put("message","");
        JSONObject data = new JSONObject();
        //入场车辆数
        Integer inCountToday = getIndexDataService.getInCount(0);
        data.put("arriveData",inCountToday);
        Integer inCountYesterday = getIndexDataService.getInCount(1);
        if (inCountToday.equals(0))
            data.put("arrivePercentData","0%");
        else
            data.put("arrivePercentData",new BigDecimal((double)(inCountToday-inCountYesterday)*100/inCountToday).setScale(1, BigDecimal.ROUND_HALF_UP)+"%");

        //出场车辆数
        Integer outCountToday = getIndexDataService.getOutCount(0);
        data.put("leaveData",outCountToday);
        Integer outCountYesterday = getIndexDataService.getOutCount(1);
        if (outCountToday.equals(0))
            data.put("leavePercentData","0%");
        else
            data.put("leavePercentData",new BigDecimal((double)(outCountToday-outCountYesterday)*100/outCountToday).setScale(1, BigDecimal.ROUND_HALF_UP)+"%");

        //滞留车辆
        Integer retentionCountToday = inCountToday - outCountToday > 0 ? inCountToday - outCountToday : 0;
        data.put("retentionData",retentionCountToday);
        Integer retentionCountYesterday = inCountYesterday - outCountYesterday > 0 ? inCountYesterday - outCountYesterday : 0;
        String retentionPercent = retentionCountYesterday > 0 ? new BigDecimal((double)(outCountToday-outCountYesterday)*100/retentionCountYesterday).setScale(1, BigDecimal.ROUND_HALF_UP)+"%" : "0%";
        data.put("retentionPercentData",retentionPercent);

        //剩余车位
        Integer surplusParkCount = getIndexDataService.getSurplusPark();
        data.put("restParkSpace",surplusParkCount);

        //车辆出入比
        data.put("inOut",inCountToday + "/" + outCountToday);

        res.put("data",data);
        return res;
    }

    /**
     * 车位预警
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "eduPlatIndex/parkingSpaceWarn", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map parkingSpaceWarn() throws Exception {
        JSONObject res = new JSONObject();
        res.put("status",200);
        res.put("message","");
        JSONObject data = new JSONObject();
        Integer totalParkCount = getIndexDataService.getTotalPark();
        Integer surplusParkCount = getIndexDataService.getSurplusPark();
        data.put("name","车位预警");
        BigDecimal rateValue = new BigDecimal((float)surplusParkCount/totalParkCount);
        data.put("value",(rateValue.setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue()) * 100);
        res.put("data",data);
        return res;
    }

    /**
     * 实时车流量
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "eduPlatIndex/realTimeFlow", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map realTimeFlow() throws Exception {
        JSONObject res = new JSONObject();
        res.put("status",200);
        res.put("message","");
        JSONObject data = new JSONObject();
        data = getIndexDataService.getTodayInoutInfo();
        res.put("data",data);
        return res;
    }

    /**
     * 实时监控
     * @param showCount
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "eduPlatIndex/realTimeMonitor", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map realTimeMonitor(@RequestParam(value = "showCount") Integer showCount) throws Exception {
        JSONObject res = new JSONObject();
        res.put("status",200);
        res.put("message","");
        if (showCount > 0) {
            JSONArray data = getIndexDataService.getRealTableInfo(showCount);
            res.put("data",data);
        }else {
            res.put("data",null);
        }

        return res;
    }

    /**
     * 车辆每日进出信息
     * @param showCount
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "eduPlatIndex/inOutCarLineInfo", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map inOutCarLineInfo(@RequestParam(value = "showCount") Integer showCount) throws Exception {
        JSONObject res = new JSONObject();
        res.put("status",200);
        res.put("message","");
        JSONObject data = new JSONObject();

        res.put("data",data);
        return res;
    }

    /**
     * 车场总体信息
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "eduPlatIndex/parkOverallInfo", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map parkOverallInfo() throws Exception {
        JSONObject res = new JSONObject();
        res.put("status",200);
        res.put("message","");
        JSONObject data = new JSONObject();
        getIndexDataService.deleteUselessData();
        res.put("data",data);
        return res;
    }

    /**
     * 车辆类型统计
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "eduPlatIndex/carTypeStatistics", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public Map carTypeStatistics() throws Exception {
        JSONObject res = new JSONObject();

        res.put("status",200);
        res.put("message","");
        JSONObject data = new JSONObject();

        Integer inCountToday = getIndexDataService.getInCount(0);
        Integer inCountYesterday = getIndexDataService.getInCount(1);
        //总车数
        data.put("totalVehicle",inCountToday);
        data.put("totalVehicleBar",100);
        data.put("totalVehiclePercent",inCountToday.equals(0) ? "0%" : new BigDecimal((double)(inCountToday-inCountYesterday)*100/inCountToday).setScale(0, BigDecimal.ROUND_HALF_UP)+"%");

        //教职工车辆
        Integer inSchoolCarToday = getIndexDataService.getInCountByCarType("免费车",0);
        Integer inSchoolCarYesterday = getIndexDataService.getInCountByCarType("免费车",1);
        data.put("teacherVehicle",inSchoolCarToday);
        data.put("teacherVehicleBar",inCountToday.equals(0)? "0" : new BigDecimal((double)(inSchoolCarToday)*100/inCountToday).setScale(2, BigDecimal.ROUND_HALF_UP));
        data.put("teacherVehiclePercent",inSchoolCarToday.equals(0) ? "0%" : new BigDecimal((double)(inSchoolCarToday-inSchoolCarYesterday)*100/inSchoolCarToday).setScale(0, BigDecimal.ROUND_HALF_UP)+"%");

        //外来车辆
        Integer inTempCarToday = getIndexDataService.getInCountByCarType("临时车",0);
        Integer inTempCarYesterday = getIndexDataService.getInCountByCarType("临时车",1);
        data.put("schoolVehicle",inTempCarToday);
        data.put("schoolVehicleBar",inCountToday.equals(0)? "0" : new BigDecimal((double)(inTempCarToday)*100/inCountToday).setScale(2, BigDecimal.ROUND_HALF_UP));
        data.put("schoolVehiclePercent",inTempCarToday.equals(0) ? "0%" : new BigDecimal((double)(inTempCarToday-inTempCarYesterday)*100/inTempCarToday).setScale(0, BigDecimal.ROUND_HALF_UP)+"%");

        //公务车辆
        Integer inPublicCarToday = getIndexDataService.getInCountByCarType("预约车",0);
        Integer inPublicCarYesterday = getIndexDataService.getInCountByCarType("预约车",1);
        data.put("publicVehicle",inPublicCarToday);
        data.put("publicVehicleBar",inCountToday.equals(0)? "0" : new BigDecimal((double)(inPublicCarToday)*100/inCountToday).setScale(2, BigDecimal.ROUND_HALF_UP));
        data.put("publicVehiclePercent",inPublicCarToday.equals(0) ? "0%" : new BigDecimal((double)(inPublicCarToday-inPublicCarYesterday)*100/inPublicCarToday).setScale(0, BigDecimal.ROUND_HALF_UP)+"%");


        //其他车辆
        Integer inOtherCarToday = inCountToday - inSchoolCarToday - inTempCarToday - inPublicCarToday;
        Integer inOtherCarYesterday = inCountYesterday - inSchoolCarYesterday - inTempCarYesterday - inPublicCarYesterday;
        data.put("otherVehicle",inOtherCarToday);
        data.put("otherVehicleBar",inCountToday.equals(0)? "0" : new BigDecimal((double)(inOtherCarToday)*100/inCountToday).setScale(2, BigDecimal.ROUND_HALF_UP));
        data.put("otherVehiclePercent",inOtherCarToday.equals(0) ? "0%" : new BigDecimal((double)(inOtherCarToday-inOtherCarYesterday)*100/inOtherCarToday).setScale(0, BigDecimal.ROUND_HALF_UP)+"%");

        res.put("data",data);
        return res;
    }
}
