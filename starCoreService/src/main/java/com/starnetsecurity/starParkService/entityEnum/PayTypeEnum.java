package com.starnetsecurity.starParkService.entityEnum;

/**
 * Created by 宏炜 on 2017-08-08.
 */
public enum PayTypeEnum {
    WX_PAY("微信"),
    ALI_PAY("支付宝"),
    BOLINK("泊链"),
    CASH_PAY("现金");

    private String desc;

    PayTypeEnum(String desc){
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
