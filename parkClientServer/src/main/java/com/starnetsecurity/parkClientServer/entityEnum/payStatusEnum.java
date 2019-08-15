package com.starnetsecurity.parkClientServer.entityEnum;

/**
 * Created by JAMESBANG on 2018/1/12.
 */
public enum payStatusEnum {
    UN_PAID("未支付"),
    HAS_PAID("已支付"),
    OVERTIME_PAID("支付超时");

    private String desc;
    payStatusEnum(String desc){
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
