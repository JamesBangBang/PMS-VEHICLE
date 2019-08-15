package com.starnetsecurity.parkClientServer.init;

import com.starnetsecurity.parkClientServer.service.ClientBizService;
import com.starnetsecurity.parkClientServer.service.DeviceBizService;

/**
 * Created by 宏炜 on 2017-12-11.
 */
public class BizUnitTool {

    public static ClientBizService clientBizService;

    public static DeviceBizService deviceBizService;

    public ClientBizService getClientBizService() {
        return clientBizService;
    }

    public void setClientBizService(ClientBizService clientBizService) {
        this.clientBizService = clientBizService;
    }

    public DeviceBizService getDeviceBizService() {
        return deviceBizService;
    }

    public void setDeviceBizService(DeviceBizService deviceBizService) {
        this.deviceBizService = deviceBizService;
    }

    public void init(){

    }
}
