package com.starnetsecurity.parkClientServer.quartz;

import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.Constant;
import com.starnetsecurity.common.util.HttpRequestUtils;
import com.starnetsecurity.parkClientServer.init.AppInfo;
import com.starnetsecurity.parkClientServer.service.DeviceManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by JAMESBANG on 2018/11/18.
 */
@Component
public class ParkLoginStatusQuartz {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParkLoginStatusQuartz.class);


    public void run() {
        try {

            if ("on".equals(AppInfo.isConnectCloud)){
                try {
                    String res = HttpRequestUtils.post("http://" + AppInfo.cloudIp + ":" + AppInfo.cloudPort + "/payment/parkInfo/updateCarparkStatus?carparkKey=" + AppInfo.cloudCarparkCode
                            ,null);
                }catch (BizException e){
                    LOGGER.error("车场定时验证失败：" + e.getMessage());
                }
            }
        }catch (Exception ex){
            LOGGER.error("车场定时验证失败：" + ex.getMessage());
        }

    }
}
