package com.starnetsecurity.starParkService.entityEnum;

/**
 * Created by 宏炜 on 2017-07-18.
 */
public enum OrderParkEnum {

    UNCONFIRMED("未确认"),
    FEE_TIMEOUT("金额固定"),
    UNPAID("未支付"),
    PAYING("支付中"),
    PAID_SUCCESS("支付成功"),
    COMPLETE("完成");

    private String desc;

    OrderParkEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
