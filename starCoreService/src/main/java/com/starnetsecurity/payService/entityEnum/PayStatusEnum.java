package com.starnetsecurity.payService.entityEnum;

/**
 * Created by 宏炜 on 2017-07-13.
 */
public enum PayStatusEnum {
    UN_PAID("未支付"),
    HAS_PAID("已支付");

    private String desc;
    PayStatusEnum(String desc){
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
