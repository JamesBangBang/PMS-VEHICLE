package com.starnetsecurity.parkClientServer.service;

import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.parkClientServer.allinpay.QueryRsp;
import com.starnetsecurity.parkClientServer.entity.OrderTransaction;

import java.util.TreeMap;

/**
 * Created by JAMESBANG on 2019/1/21.
 */
public interface AllinpayService {
    /**
     * 根据车牌查询停车信息
     * @param carno
     * @return
     */
    JSONObject getChargeInfoByCarno(String carno);

    /**
     * 车主确认停车信息，生成订单，构造自带参数的当面付订单接口url
     * @param jsonObject
     * @return
     */
    String createOrderUrl(JSONObject jsonObject);

    /**
     * 数据同步接口，根据订单ID查询停车信息，通联服务端专用
     * @param treeMap
     * @return
     */
    QueryRsp returnParkingInfo(TreeMap treeMap);

    /**
     * 异步通知，并且应答
     * @param treeMap
     * @return
     */
    boolean chargeSucessNotify(TreeMap treeMap);

    /**
     * 根据ID获取订单信息
     * @param id
     * @return
     */
    OrderTransaction getOrderTransById(String id);

    /**
     * 根据ID删除订单信息
     * @param id
     */
    void deleteOrderTransById(String id);

    /**
     * 根据订单ID查询订单状态，如果支付成功返回true
     * @param id
     * @return
     */
    boolean queryOrderStatus(String id);
}
