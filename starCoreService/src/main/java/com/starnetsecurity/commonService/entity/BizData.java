package com.starnetsecurity.commonService.entity;

import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.commonService.bizEnum.BizEnum;

import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2017-11-07.
 */
public class BizData {

    private BizEnum bizType;
    private Timestamp time;
    private JSONObject data;

    public BizEnum getBizType() {
        return bizType;
    }

    public void setBizType(BizEnum bizType) {
        this.bizType = bizType;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
