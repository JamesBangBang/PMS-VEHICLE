package com.starnetsecurity.parkClientServer.quartz;

import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.parkClientServer.service.DeviceManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by homew on 2018-03-26.
 */
@Component
public class DeviceTimeSetQuartz {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceTimeSetQuartz.class);

    @Autowired
    DeviceManageService deviceManageService;

    public void run() {
        try {
            try{
                deviceManageService.formatDeviceTime();

            }catch (BizException ex){
                LOGGER.error(ex.getMessage());
            }
        } catch (Exception ex) {
            LOGGER.error("设备校时定时器错误:", ex);
        }
    }
}
