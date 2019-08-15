package com.starnetsecurity.parkClientServer.sockServer;

/**
 * Created by 宏炜 on 2017-12-12.
 */
public enum ClientBizEnum {

    login("登录"),
    logout("登出"),
    heartCheck("心跳"),
    pressureTest("压力测试"),
    CarPlateIndPrev("车辆扫牌信息预推送"),
    CarPlateInd("车辆扫牌信息完整推送"),
    outPark("车辆出场"),
    inPark("车辆入场"),
    CarPlateManual("车牌手动录入"),
    shiftOperator("客户端交接班"),
    shiftOut("客户端交接班登出"),
    recvCheck("客户端业务回复"),
    WxChatPay("微信支付返回");


    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    ClientBizEnum(String desc){

        this.desc = desc;
    }
}
