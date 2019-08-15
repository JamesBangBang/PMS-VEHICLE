package com.starnetsecurity.parkClientServer.service;

import java.util.List;

public interface RealRecordsService {
    /**
     * @Author chenbinbin
     * @Description 获取实时进出记录
     * @Date 14:36 2019/6/28
     * @Param [start, length, carName, driverName, carparkName, carRoadName, inoutFlag, startTime, endTime, excelStart, excelEnd]
     * @return java.util.List
     **/
    List realinout(Integer start, Integer length, String carName, String driverName, String carparkName, String carRoadName, String inoutFlag, String startTime, String endTime, String excelStart, String excelEnd);

    /**
     * @Author chenbinbin
     * @Description 获取实时进出记录条数
     * @Date 14:36 2019/6/28
     * @Param [carName, driverName, carparkName, carRoadName, inoutFlag, startTime, endTime, excelStart, excelEnd]
     * @return java.lang.Long
     **/
    Long countRealinout(String carName, String driverName, String carparkName, String carRoadName, String inoutFlag, String startTime, String endTime, String excelStart, String excelEnd);

    /**
     * @Author chenbinbin
     * @Description 获取实时缴费记录
     * @Date 14:40 2019/6/28
     * @Param [start, length, carNo, carNoAttribute, carparkName, inCarRoadName, outCarRoadName, chargePostName, inStartTime, inEndTime, outStartTime, outEndTime, excelStart, excelEnd]
     * @return java.util.List
     **/
    List realTimePaymentList(Integer start, Integer length, String carNo, String carNoAttribute, String carparkName, String inCarRoadName, String outCarRoadName, String chargePostName, String inStartTime, String inEndTime, String outStartTime, String outEndTime, String excelStart, String excelEnd);

    /**
     * @Author chenbinbin
     * @Description 获取实时缴费记录条数
     * @Date 14:40 2019/6/28
     * @Param [carNo, carNoAttribute, carparkName, inCarRoadName, outCarRoadName, chargePostName, inStartTime, inEndTime, outStartTime, outEndTime, excelStart, excelEnd]
     * @return java.lang.Long
     **/
    Long countRealTimePaymentList(String carNo, String carNoAttribute, String carparkName, String inCarRoadName, String outCarRoadName, String chargePostName, String inStartTime, String inEndTime, String outStartTime, String outEndTime, String excelStart, String excelEnd);

    List realInoutList(String carName, String carType, String carparkName, String carRoadName, String inoutFlag, String startTime, String endTime, String excelStart, String excelEnd);

    List payList(String carNo, String carNoAttribute, String carparkName, String inCarRoadName, String outCarRoadName, String chargePostName, String inStartTime, String inEndTime, String outStartTime, String outEndTime, String excelStart, String excelEnd);

    String getRecordImage(String id);

    List getCarParkRoadSelect(String parkId);

    List getCarParkPostSelect();

    /**
     * @Author chenbinbin
     * @Description 获取场内车辆记录
     * @Date 15:02 2019/6/28
     * @Param [start, length, carNo, carNoAttribute, carparkName, inCarRoadName, inStartTime, inEndTime]
     * @return java.util.List
     **/
    List carInParkList(Integer start, Integer length, String carNo, String carNoAttribute, String carparkName, String inCarRoadName, String parkOverTime,
                       String inStartTime, String inEndTime);

    /**
     * @Author chenbinbin
     * @Description 获取场内车辆记录条数
     * @Date 15:02 2019/6/28
     * @Param [carNo, carNoAttribute, carparkName, inCarRoadName, inStartTime, inEndTime]
     * @return java.lang.Long
     **/
    Long carInParkCount(String carNo, String carNoAttribute, String carparkName, String inCarRoadName, String parkOverTime,
                        String inStartTime, String inEndTime);

    /**
     * @Author chenbinbin
     * @Description 删除场内车辆
     * @Date 15:53 2019/8/6
     * @Param [carNo, carparkName]
     * @return void
     **/
    void deleteCarInPark(String carNo,String carparkName);

    /**
     * @Author chenbinbin
     * @Description 获取异常通行记录
     * @Date 16:32 2019/7/1
     * @Param [start, length, carNo, carNoAttribute, carparkName, outCarRoadName, outStartTime, outEndTime]
     * @return java.util.List
     **/
    List abnormalPassList(Integer start, Integer length, String carNo, String carNoAttribute, String carparkName, String outCarRoadName,
                          String outStartTime, String outEndTime);

    /**
     * @Author chenbinbin
     * @Description 获取异常通行记录条数
     * @Date 16:32 2019/7/1
     * @Param [carNo, carNoAttribute, carparkName, outCarRoadName, outStartTime, outEndTime]
     * @return java.lang.Long
     **/
    Long abnormalPassCount(String carNo, String carNoAttribute, String carparkName, String outCarRoadName,
                           String outStartTime, String outEndTime);
}
