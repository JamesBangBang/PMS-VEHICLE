package com.starnetsecurity.parkClientServer.clientEnum;

/**
 * Created by JAMESBANGBANG on 2018/1/23.
 */
public enum LogEnum {
    operateBasicInfo("基本信息配置"),
    operatePark("停车场信息配置"),
    operateRoad("车道信息配置"),
    operateCamera("IPC信息配置"),
    operateGate("道闸信息配置"),
    operateOtherDevice("第三方设备配置"),
    operateCharge("收费信息配置"),
    operateUser("操作人员信息配置"),
    operateDriver("车主信息配置"),
    operateGateControl("道闸控制"),
    clientLogin("客户端登录"),
    clientLoginOut("客户端登出"),
    dealCarno("车辆通行处理"),
    operatorPost("岗亭信息配置"),
    controlGate("道闸控制");

    private String desc;

    LogEnum (String desc){
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

}
