package com.starnetsecurity.commonService.bizEnum;

/**
 * Created by 宏炜 on 2017-11-07.
 */
public enum BizEnum {

    CAR_PARK_ADD("新增车场"),

    CAR_PARK_DELETE("删除车场"),

    CAR_PARK_EDIT("修改车场"),

    WHITE_MEMBER_ADD("白名单人员添加"),

    WHITE_MEMBER_DELETE("白名单人员删除"),

    WHITE_MEMBER_EDIT("白名单人员修改"),

    PARKING_SPACE_RESERVED("预约停车位"),

    PARKING_SPACE_CANCEL("取消预约停车位"),

    DEVICE_CONFIG_SET("设备配置"),

    ROAD_GATE_OPEN("道闸打开"),

    ROAD_GATE_CLOSE("道闸关闭");

    private String desc;

    BizEnum(String desc){
        this.desc = desc;
    }

    public String getDesc(){
        return this.desc;
    }
}
