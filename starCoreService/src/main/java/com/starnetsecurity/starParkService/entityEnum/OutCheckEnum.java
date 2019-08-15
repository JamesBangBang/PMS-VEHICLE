package com.starnetsecurity.starParkService.entityEnum;

/**
 * Created by 宏炜 on 2017-08-16.
 */
public enum OutCheckEnum {

    ALREADY_OUT_PARK("已离场"),
    NOT_LEAVE_PARK("未离场");

    private String desc;

    OutCheckEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
