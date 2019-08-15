package com.starnetsecurity.parkClientServer.clientEnum;

/**
 * Created by 宏炜 on 2017-08-23.
 */
public enum ServiceEnum {

    whitePayNotify("白名单预交费支付通知"),
    payNotify("车辆出入场支付通知");

    private String desc;

    ServiceEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
