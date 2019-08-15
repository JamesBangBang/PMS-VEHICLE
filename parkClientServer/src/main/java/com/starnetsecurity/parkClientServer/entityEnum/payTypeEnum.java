package com.starnetsecurity.parkClientServer.entityEnum;

/**
 * Created by JAMESBANG on 2018/1/12.
 */
public enum payTypeEnum {
    WX_PAY("微信"),
    ALI_PAY("支付宝"),
    BOLINK("泊链"),
    CASH_PAY("现金"),
    BOSS_PAY("通联支付"),
    A("临停现金支付"),
    B("月卡现金支付"),
    C("临停微信缴费"),
    D("月卡微信支付"),
    E("临停支付宝支付"),
    F("月卡支付宝支付"),
    G("通联支付"),
    H("银联支付");

    private String desc;

    payTypeEnum(String desc){
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
