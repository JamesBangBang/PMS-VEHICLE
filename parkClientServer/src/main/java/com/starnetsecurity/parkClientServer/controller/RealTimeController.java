package com.starnetsecurity.parkClientServer.controller;

import com.starnetsecurity.common.controller.BaseController;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.util.PoiExcelUtil;
import com.starnetsecurity.common.web.MediaTypes;
import com.starnetsecurity.parkClientServer.entity.AdminUser;
import com.starnetsecurity.parkClientServer.service.RealRecordsService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;

@Controller
@RequestMapping("real")
public class RealTimeController extends BaseController {
    @Autowired
    RealRecordsService realRecordsService;

    @ResponseBody
    @RequestMapping(value = "getInoutImage",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map getInoutImage(String inRecordId,String outRecordId){
        Map res = new HashedMap();
        try {

            Map data = new HashedMap();
            data.put("inImg",realRecordsService.getRecordImage(inRecordId));
            data.put("outImg",realRecordsService.getRecordImage(outRecordId));
            this.success(res);
        }catch (BizException e) {
            this.failed(res, e);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "pay",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map pay(@RequestParam(value="start", required=false,defaultValue = "0") Integer start,
                   @RequestParam(value="length", required=false,defaultValue = "10") Integer length,
                   @RequestParam(value = "carNo", required = false) String carNo,
                   @RequestParam(value = "carNoAttribute", required = false) String carNoAttribute,
                   @RequestParam(value = "carparkName", required = false) String carparkName,
                   @RequestParam(value = "inCarRoadName", required = false) String inCarRoadName,
                   @RequestParam(value = "outCarRoadName", required = false) String outCarRoadName,
                   @RequestParam(value = "chargePostName", required = false) String chargePostName,
                   @RequestParam(value = "inStartTime", required = false) String inStartTime,
                   @RequestParam(value = "inEndTime", required = false) String inEndTime,
                   @RequestParam(value = "OutStartTime", required = false) String OutStartTime,
                   @RequestParam(value = "OutEndTime", required = false) String OutEndTime,
                   @RequestParam(value = "excelStart", required = false) String excelStart,
                   @RequestParam(value = "excelEnd", required = false) String excelEnd,
                   @RequestParam(value="draw", required=false) Integer draw){
        Map res = new HashedMap();
        try {

            List list = realRecordsService.realTimePaymentList(start,length,carNo,carNoAttribute,carparkName,inCarRoadName,outCarRoadName,chargePostName,inStartTime,inEndTime,
                    OutStartTime,OutEndTime,excelStart,excelEnd);
            Long total = realRecordsService.countRealTimePaymentList(carNo,carNoAttribute,carparkName,inCarRoadName,outCarRoadName,chargePostName,inStartTime,inEndTime,
                    OutStartTime,OutEndTime,excelStart,excelEnd);


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

    @ResponseBody
    @RequestMapping(value = "carInPark",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map carInPark(@RequestParam(value="start", required=false,defaultValue = "0") Integer start,
                   @RequestParam(value="length", required=false,defaultValue = "10") Integer length,
                   @RequestParam(value = "carNo", required = false) String carNo,
                   @RequestParam(value = "carNoAttribute", required = false) String carNoAttribute,
                   @RequestParam(value = "carparkName", required = false) String carparkName,
                   @RequestParam(value = "inCarRoadName", required = false) String inCarRoadName,
                   @RequestParam(value = "parkOvertime", required = false) String parkOvertime,
                   @RequestParam(value = "inStartTime", required = false) String inStartTime,
                   @RequestParam(value = "inEndTime", required = false) String inEndTime,
                   @RequestParam(value="draw", required=false) Integer draw){
        Map res = new HashedMap();
        try {
            List list = realRecordsService.carInParkList(start,length,carNo,carNoAttribute,carparkName,inCarRoadName,parkOvertime,inStartTime,inEndTime);
            Long total = realRecordsService.carInParkCount(carNo,carNoAttribute,carparkName,inCarRoadName,parkOvertime,inStartTime,inEndTime);
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

    @ResponseBody
    @RequestMapping(value = "deleteCarInPark",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map deleteCarInPark(@RequestParam(value = "carNo", required = false) String carNo,
                         @RequestParam(value = "carparkName", required = false) String carparkName){
        Map res = new HashedMap();
        try {
            AdminUser adminUser = (AdminUser)getUser();
            if (!"root".equals(adminUser.getUserName())){
                throw new BizException("当前用户无权限进行该操作!");
            }
            realRecordsService.deleteCarInPark(carNo,carparkName);
            this.success(res);
        }catch (BizException e) {
            this.failed(res, e);
        }
        return res;
    }

    @RequestMapping(value = "carInParkExcel")
    public void carInParkExcel (@RequestParam(value = "carNo", required = false) String carNo,
                          @RequestParam(value = "carNoAttribute", required = false) String carNoAttribute,
                          @RequestParam(value = "carparkName", required = false) String carparkName,
                          @RequestParam(value = "inCarRoadName", required = false) String inCarRoadName,
                          @RequestParam(value = "parkOvertime", required = false) String parkOvertime,
                          @RequestParam(value = "inStartTime", required = false) String inStartTime,
                          @RequestParam(value = "inEndTime", required = false) String inEndTime,
                          HttpServletResponse response){
        Map res = new HashedMap();
        try {
            List list = new ArrayList();
            List listExcel=new ArrayList();
            String sheetName="场内车辆统计报表";

            List<Map<String,Object>> headList = new ArrayList<Map<String, Object>>(
                    Arrays.asList(
                            new HashMap<String, Object>(){{
                                put("key","car_no");
                                put("name","车牌号");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","carpark_name");
                                put("name","车场名称");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","in_time");
                                put("name","入场时间");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","in_car_road_name");
                                put("name"," 入场车道");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","stayTime");
                                put("name","停车时长");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","car_type");
                                put("name","车辆属性");
                            }}

                    )
            );
            Timestamp beginTime = CommonUtils.getTodayStartTimeStamp();
            Timestamp overTime = CommonUtils.getTodayEndTimeStamp();
            if (!CommonUtils.isEmpty(inStartTime))
                beginTime = Timestamp.valueOf(inStartTime);
            if (!CommonUtils.isEmpty(inEndTime))
                overTime = Timestamp.valueOf(inEndTime);

            list=realRecordsService.carInParkList(null,null,carNo,carNoAttribute,carparkName,inCarRoadName,parkOvertime,inStartTime,inEndTime);
            for(int i = 0; i<list.size(); i++)
            {
                Map ss= (Map)list.get(i);
                if (!CommonUtils.isEmpty(ss.get("stayTime"))) {
                    Long parkingTime = Long.valueOf(String.valueOf(ss.get("stayTime")));
                    Long parkingDays = parkingTime / (60 * 60 * 24);
                    Long parkingHours = (parkingTime % (60 * 60 * 24)) / (60 * 60);
                    Long parkingMinutes = (parkingTime % (60 * 60)) / (60);
                    Long parkingSeconds = parkingTime % 60;
                    String tag = new String();
                    tag = parkingDays.equals(0L)? tag : tag + parkingDays + "天";
                    tag = parkingHours.equals(0L)? tag : tag + parkingHours + "时";
                    tag = parkingMinutes.equals(0L)? tag : tag + parkingMinutes + "分";
                    tag = tag + parkingSeconds + "秒";
                    ss.put("stayTime",tag);
                }

                listExcel.add(ss);
            }

            PoiExcelUtil.ListMapExpotToExcel(sheetName,beginTime,overTime,headList,listExcel,response);
            this.success(res);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "abnormalPass",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map abnormalPass(@RequestParam(value="start", required=false,defaultValue = "0") Integer start,
                         @RequestParam(value="length", required=false,defaultValue = "10") Integer length,
                         @RequestParam(value = "carNo", required = false) String carNo,
                         @RequestParam(value = "carNoAttribute", required = false) String carNoAttribute,
                         @RequestParam(value = "carparkName", required = false) String carparkName,
                         @RequestParam(value = "outCarRoadName", required = false) String outCarRoadName,
                         @RequestParam(value = "outStartTime", required = false) String outStartTime,
                         @RequestParam(value = "outEndTime", required = false) String outEndTime,
                         @RequestParam(value="draw", required=false) Integer draw){
        Map res = new HashedMap();
        try {

            List list = realRecordsService.abnormalPassList(start,length,carNo,carNoAttribute,carparkName,outCarRoadName,outStartTime,outEndTime);
            Long total = realRecordsService.abnormalPassCount(carNo,carNoAttribute,carparkName,outCarRoadName,outStartTime,outEndTime);


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

    @RequestMapping(value = "abnormalPassExcel")
    public void abnormalPassExcel (@RequestParam(value = "carNo", required = false) String carNo,
                                   @RequestParam(value = "carNoAttribute", required = false) String carNoAttribute,
                                   @RequestParam(value = "carparkName", required = false) String carparkName,
                                   @RequestParam(value = "outCarRoadName", required = false) String outCarRoadName,
                                   @RequestParam(value = "outStartTime", required = false) String outStartTime,
                                   @RequestParam(value = "outEndTime", required = false) String outEndTime,
                                   HttpServletResponse response){
        Map res = new HashedMap();
        try {
            List list = new ArrayList();
            List listExcel=new ArrayList();
            String sheetName="异常通行统计报表";

            List<Map<String,Object>> headList = new ArrayList<Map<String, Object>>(
                    Arrays.asList(
                            new HashMap<String, Object>(){{
                                put("key","car_no");
                                put("name","车牌号");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","carpark_name");
                                put("name","车场名称");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","out_time");
                                put("name","出场时间");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","out_car_road_name");
                                put("name","出场车道");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","charge_receivable_amount");
                                put("name","停车费用");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","release_type");
                                put("name","放行方式");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","car_type");
                                put("name","车辆属性");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","charge_post_name");
                                put("name","收费岗亭");
                            }}
                    )
            );
            Timestamp beginTime = CommonUtils.getTodayStartTimeStamp();
            Timestamp overTime = CommonUtils.getTodayEndTimeStamp();
            if (!CommonUtils.isEmpty(outStartTime))
                beginTime = Timestamp.valueOf(outStartTime);
            if (!CommonUtils.isEmpty(outEndTime))
                overTime = Timestamp.valueOf(outEndTime);

            list = realRecordsService.abnormalPassList(null,null,carNo,carNoAttribute,carparkName,outCarRoadName,outStartTime,outEndTime);
            for(int i = 0; i<list.size(); i++)
            {
                Map ss= (Map)list.get(i);
                if (!CommonUtils.isEmpty(ss.get("charge_receivable_amount"))) {
                    String tag = String.valueOf(ss.get("charge_receivable_amount"));
                    ss.put("charge_receivable_amount",tag+"元");
                }else {
                    ss.put("charge_receivable_amount","0.0元");
                }

                if (!CommonUtils.isEmpty(ss.get("release_type"))) {
                    Integer tag=Integer.parseInt(String.valueOf(ss.get("release_type")));
                    if(tag==1)
                    {
                        ss.put("release_type","收费");
                    }else if(tag==3) {
                        ss.put("release_type","免费");
                    }
                }

                listExcel.add(ss);
            }

            PoiExcelUtil.ListMapExpotToExcel(sheetName,beginTime,overTime,headList,listExcel,response);
            this.success(res);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping(value = "payRoadPostSelect",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map payRoadPostSelect(@RequestParam(value="parkId", required=true) String parkId){
        Map res = new HashedMap();
        try {
            Map<String,Object> data = new HashedMap();
            List postData = realRecordsService.getCarParkPostSelect();
            List roadData = realRecordsService.getCarParkRoadSelect(parkId);
            data.put("postData",postData);
            data.put("roadData",roadData);
            res.put("data",data);
            this.success(res);
        }catch (BizException e) {
            this.failed(res, e);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "inout",method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public Map inout(@RequestParam(value="start", required=false,defaultValue = "0") Integer start,
                     @RequestParam(value="length", required=false,defaultValue = "10") Integer length,
                     @RequestParam(value = "carName", required = false) String carName,
                     @RequestParam(value = "driverName", required = false) String driverName,
                     @RequestParam(value = "carparkName", required = false) String carparkName,
                     @RequestParam(value = "carRoadName", required = false) String carRoadName,
                     @RequestParam(value = "inoutFlag", required = false) String inoutFlag,
                     @RequestParam(value = "startTime", required = false) String startTime,
                     @RequestParam(value = "endTime", required = false) String endTime,
                     @RequestParam(value = "excelStart", required = false) String excelStart,
                     @RequestParam(value = "excelEnd", required = false) String excelEnd,
                     @RequestParam(value="draw", required=false) Integer draw){
        Map res = new HashedMap();
        try {
            Timestamp beginTime = CommonUtils.getTodayStartTimeStamp();
            Timestamp overTime = CommonUtils.getTodayEndTimeStamp();
            if (!CommonUtils.isEmpty(startTime))
                beginTime = Timestamp.valueOf(startTime);
            if (!CommonUtils.isEmpty(endTime))
                overTime = Timestamp.valueOf(endTime);

            List list = realRecordsService.realinout(start,length,carName,driverName,carparkName,carRoadName,inoutFlag, CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",beginTime), CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",overTime),excelStart,excelEnd);
            Long total = realRecordsService.countRealinout(carName,driverName,carparkName,carRoadName,inoutFlag, CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",beginTime), CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss",overTime),excelStart,excelEnd);
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


    //实时进出报表
    @RequestMapping(value = "inoutExcel")
    public void inoutExcel (@RequestParam(value = "carName", required = false,defaultValue = "") String carName,
                            @RequestParam(value = "driverName", required = false,defaultValue = "") String driverName,
                            @RequestParam(value = "carparkName", required = false,defaultValue = "") String carparkName,
                            @RequestParam(value = "carRoadName", required = false,defaultValue = "") String carRoadName,
                            @RequestParam(value = "inoutFlag", required = false,defaultValue = "") String inoutFlag,
                            @RequestParam(value = "startTime", required = false,defaultValue = "") String startTime,
                            @RequestParam(value = "endTime", required = false,defaultValue = "") String endTime,
                            @RequestParam(value = "excelStart", required = false,defaultValue = "") String excelStart,
                            @RequestParam(value = "excelEnd", required = false,defaultValue = "") String excelEnd,
                             HttpServletResponse response){
        Map res = new HashedMap();
        try {
            List list = new ArrayList();
            List listExcel=new ArrayList();
            String sheetName="实时进出统计报表";

            Timestamp exportBeginTime = new Timestamp(System.currentTimeMillis());
            Timestamp exportEndTime = new Timestamp(System.currentTimeMillis());
            try{
                 exportBeginTime = CommonUtils.isEmpty(excelStart)? CommonUtils.getTodayStartTimeStamp() : Timestamp.valueOf(excelStart);
                 exportEndTime = CommonUtils.isEmpty(excelEnd)? CommonUtils.getTodayEndTimeStamp() : Timestamp.valueOf(excelEnd);
            }catch (Exception e){
                e.printStackTrace();
            }

            List<Map<String,Object>> headList = new ArrayList<Map<String, Object>>(
                    Arrays.asList(
                            new HashMap<String, Object>(){{
                                put("key","car_no");
                                put("name","车牌号");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","car_no_color");
                                put("name","车牌颜色");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","car_type");
                                put("name","车辆属性");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","inout_flag");
                                put("name","进出类型");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","carpark_name");
                                put("name","车场名称");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","car_road_name");
                                put("name","车道名称");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","inout_status");
                                put("name","进出场状态");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","inout_time");
                                put("name","进出场时间");
                            }},
//                            new HashMap<String, Object>(){{
//                                put("key","post_name");
//                                put("name","岗亭名称");
//                            }},
                            new HashMap<String, Object>(){{
                                put("key","operator_name");
                                put("name","操作员名称");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","remark");
                                put("name","备注");
                            }}
                    )
            );

            startTime = CommonUtils.isEmpty(startTime)? CommonUtils.getTodayStartTimeStamp().toString() :startTime;
            endTime = CommonUtils.isEmpty(endTime)? CommonUtils.getTodayEndTimeStamp().toString() :endTime;
            list = realRecordsService.realinout(null,null,carName,driverName,carparkName,carRoadName,inoutFlag,startTime,endTime,excelStart,excelEnd);

            for(int i = 0; i<list.size(); i++)
            {
                Map ss= (Map)list.get(i);
                if (!CommonUtils.isEmpty(ss.get("car_no_color"))) {
                    Integer tag=Integer.parseInt(String.valueOf(ss.get("car_no_color")));
                    if(tag==0)
                    {
                        ss.put("car_no_color","未知");
                    }else if(tag==1) {
                        ss.put("car_no_color","蓝底白字");
                    }
                    else if(tag==2) {
                        ss.put("car_no_color","黄底黑字");
                    }
                    else if(tag==3) {
                        ss.put("car_no_color","白底黑字");
                    }
                    else if(tag==4) {
                        ss.put("car_no_color","黑底白字");
                    }
                    else if(tag==5) {
                        ss.put("car_no_color","绿底白字");
                    }else if(tag==6) {
                        ss.put("car_no_color","新能源车");
                    }
                }
                Integer inoutType = 0;  //默认是入场
                if (!CommonUtils.isEmpty(ss.get("inout_flag"))) {
                    Integer tag=Integer.parseInt(String.valueOf(ss.get("inout_flag")));
                    inoutType = tag;
                    if(tag==0)
                    {
                        ss.put("inout_flag","进场");
                    }else if(tag==1) {
                        ss.put("inout_flag","出场");
                    }
                }
                if (!CommonUtils.isEmpty(ss.get("inout_status"))) {
                    Integer tag=Integer.parseInt(String.valueOf(ss.get("inout_status")));
                    if(tag==0)
                    {
                        ss.put("inout_status","自动放行");
                    }else if(tag==1) {
                        ss.put("inout_status","手动放行");
                    }
                    else if(tag==2) {
                        if (inoutType.equals(0)) {
                            ss.put("inout_status", "禁止入场");
                        }else {
                            ss.put("inout_status", "禁止出场");
                        }
                    }
                    else if(tag==3) {
                        ss.put("inout_status","离线数据");
                    }
                }
             listExcel.add(ss);
            }

            PoiExcelUtil.ListMapExpotToExcel(sheetName,exportBeginTime,exportEndTime,headList,listExcel,response);
            this.success(res);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //实时缴费报表
    @RequestMapping(value = "payExcel")
    public void payExcel (@RequestParam(value = "carNo", required = false) String carNo,
                          @RequestParam(value = "carNoAttribute", required = false) String carNoAttribute,
                          @RequestParam(value = "carparkName", required = false) String carparkName,
                          @RequestParam(value = "inCarRoadName", required = false) String inCarRoadName,
                          @RequestParam(value = "outCarRoadName", required = false) String outCarRoadName,
                          @RequestParam(value = "chargePostName", required = false) String chargePostName,
                          @RequestParam(value = "inStartTime", required = false) String inStartTime,
                          @RequestParam(value = "inEndTime", required = false) String inEndTime,
                          @RequestParam(value = "OutStartTime", required = false) String OutStartTime,
                          @RequestParam(value = "OutEndTime", required = false) String OutEndTime,
                          @RequestParam(value = "excelStart", required = false) String excelStart,
                          @RequestParam(value = "excelEnd", required = false) String excelEnd,
                          HttpServletResponse response){
        Map res = new HashedMap();
        try {
            List list=new ArrayList();
            List listExcel=new ArrayList();
            String sheetName="实时缴费统计报表";

            List<Map<String,Object>> headList = new ArrayList<Map<String, Object>>(
                    Arrays.asList(
                            new HashMap<String, Object>(){{
                                put("key","car_no");
                                put("name","车牌号");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","carpark_name");
                                put("name","车场名称");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","in_time");
                                put("name","入场时间");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","in_car_road_name");
                                put("name"," 入场车道");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","out_time");
                                put("name","出场时间");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","out_car_road_name");
                                put("name","出场车道");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","stay_time");
                                put("name","停车时长");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","charge_receivable_amount");
                                put("name","应收金额");
                            }},
                            /*new HashMap<String, Object>(){{
                                put("key","charge_actual_amount");
                                put("name","实收金额");
                            }},*/
                            new HashMap<String, Object>(){{
                                put("key","charge_pre_amount");
                                put("name","提前缴费");
                            }},
                            /*new HashMap<String, Object>(){{
                                put("key","charge_free_amount");
                                put("name","免费金额");
                            }},*/
                            new HashMap<String, Object>(){{
                                put("key","release_type");
                                put("name","放行方式");
                            }},
                            /*new HashMap<String, Object>(){{
                                put("key","release_reason");
                                put("name","放行原因");
                            }},*/
                            new HashMap<String, Object>(){{
                                put("key","car_type");
                                put("name","车辆属性");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","charge_post_name");
                                put("name","收费岗亭");
                            }},
                            new HashMap<String, Object>(){{
                                put("key","charge_time");
                                put("name","收费时间");
                            }}

                    )
            );
            Timestamp beginTime = CommonUtils.getTodayStartTimeStamp();
            Timestamp overTime = CommonUtils.getTodayEndTimeStamp();
            if (!CommonUtils.isEmpty(excelStart))
                beginTime = Timestamp.valueOf(excelStart);
            if (!CommonUtils.isEmpty(excelEnd))
                overTime = Timestamp.valueOf(excelEnd);

            list=realRecordsService.payList(carNo,carNoAttribute,carparkName,inCarRoadName,outCarRoadName,chargePostName,inStartTime,inEndTime,
                    OutStartTime,OutEndTime,
                    excelStart,excelEnd);
            for(int i = 0; i<list.size(); i++)
            {
                Map ss= (Map)list.get(i);
                if (!CommonUtils.isEmpty(ss.get("stay_time"))) {
                    Long parkingTime = Long.valueOf(String.valueOf(ss.get("stay_time")));
                    Long parkingDays = parkingTime / (60 * 60 * 24);
                    Long parkingHours = (parkingTime % (60 * 60 * 24)) / (60 * 60);
                    Long parkingMinutes = (parkingTime % (60 * 60)) / (60);
                    Long parkingSeconds = parkingTime % 60;
                    String tag = new String();
                    tag = parkingDays.equals(0L)? tag : tag + parkingDays + "天";
                    tag = parkingHours.equals(0L)? tag : tag + parkingHours + "时";
                    tag = parkingMinutes.equals(0L)? tag : tag + parkingMinutes + "分";
                    tag = tag + parkingSeconds + "秒";
                    ss.put("stay_time",tag);
                }
                if (!CommonUtils.isEmpty(ss.get("charge_receivable_amount"))) {
                    String tag = String.valueOf(ss.get("charge_receivable_amount"));
                    ss.put("charge_receivable_amount",tag+"元");
                }
                /*if (!CommonUtils.isEmpty(ss.get("charge_actual_amount"))) {
                    String tag = String.valueOf(ss.get("charge_actual_amount"));
                    ss.put("charge_actual_amount",tag+"元");
                }*/
                if (!CommonUtils.isEmpty(ss.get("charge_pre_amount"))) {
                    String tag = String.valueOf(ss.get("charge_pre_amount"));
                    ss.put("charge_pre_amount",tag+"元");
                }else {
                    ss.put("charge_pre_amount","0.0元");
                }
                /*if (!CommonUtils.isEmpty(ss.get("charge_free_amount"))) {
                    String tag = String.valueOf(ss.get("charge_free_amount"));
                    ss.put("charge_free_amount",tag+"元");
                }*/
                if (!CommonUtils.isEmpty(ss.get("release_type"))) {
                    Integer tag=Integer.parseInt(String.valueOf(ss.get("release_type")));
                    if(tag==1)
                    {
                        ss.put("release_type","收费");
                    }else if(tag==3) {
                        ss.put("release_type","免费");
                    }
                }

                listExcel.add(ss);
            }

            PoiExcelUtil.ListMapExpotToExcel(sheetName,beginTime,overTime,headList,listExcel,response);
            this.success(res);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
