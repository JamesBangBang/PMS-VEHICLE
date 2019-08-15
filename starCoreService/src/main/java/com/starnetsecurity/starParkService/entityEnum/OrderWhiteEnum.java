package com.starnetsecurity.starParkService.entityEnum;

/**
 * Created by 宏炜 on 2017-08-23.
 */
public enum OrderWhiteEnum {

    UNPAID("未支付"),
    PAID_SUCCESS("支付成功");

    private String desc;

    OrderWhiteEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
