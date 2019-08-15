package com.starnetsecurity.parkClientServer.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by JAMESBANG on 2018/8/26.
 */
public interface GetIndexDataService {
    /**
     * 获取某一天的进场数，inParkDay为0代表当天，1代表昨天
     * @param inParkDay
     * @return
     */
    Integer getInCount(Integer inParkDay);

    /**
     * 获取某一天的出场数，inParkDay为0代表当天，1代表昨天
     * @param outParkDay
     * @return
     */
    Integer getOutCount(Integer outParkDay);

    /**
     * 获取全部车位
     * @return
     */
    Integer getTotalPark();

    /**
     * 获取剩余车位
     * @return
     */
    Integer getSurplusPark();

    /**
     * 获取车位临界值
     * @return
     */
    Integer getWarningLine();

    /**
     * 获取一天内某个时段的车辆进出数量，beginIndex从0到11
     * @param beginIndex
     * @return
     */
    JSONObject getInOutCountToDayByTime(Integer beginIndex);

    /**
     * 获取昨天一天内某个时段的车辆进出数量，beginIndex从0到11
     * @param beginIndex
     * @return
     */
    JSONObject getInOutCountYesterdayByTime(Integer beginIndex);

    /**
     * 根据车辆类型获取入场车辆数
     * @param carType
     * @return
     */
    Integer getInCountByCarType(String carType, Integer inParkDay);

    /**
     * 获取当天24小时的进出车辆信息
     * @return
     */
    JSONObject getTodayInoutInfo();

    /**
     * 获取实时监控表格数据
     * @return
     */
    JSONArray getRealTableInfo(Integer showCount);

    /**
     * @Author chenbinbin
     * @Description 删除多余数据
     * @Date 10:23 2019/7/16
     * @Param []
     * @return void
     **/
    void deleteUselessData();
}
