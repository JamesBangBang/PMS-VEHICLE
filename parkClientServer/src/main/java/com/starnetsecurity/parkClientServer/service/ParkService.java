package com.starnetsecurity.parkClientServer.service;

import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.exception.BizException;
import java.util.List;

/**
 * Created by 宏炜 on 2017-08-01.
 */
public interface ParkService {

    List getRegisteredParks();

    void addChargeReceiveRecord(JSONObject params) throws BizException;

    void addChargeReceiveRecord(String payTypeName,String payServerName,String payTypeTag,String opreator_id,String carNo,String total,String parkId) throws BizException;

    void addRechargeRecord(String id,String carno,String parkId,String fee,String payTime,
                           String kindId,String effectiveEndTime,
                           String surplusNum,String surplusAmount,String kindName);

    void getCarparkList();

    void addBolinkRechargeRecord(String id,String carno,String parkId,String fee,String payTime,
                                  String kindId,String effectiveEndTime,
                                  String surplusNum,String surplusAmount,String kindName);
}
