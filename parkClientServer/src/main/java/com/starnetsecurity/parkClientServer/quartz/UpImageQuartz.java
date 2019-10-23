package com.starnetsecurity.parkClientServer.quartz;

import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.parkClientServer.service.UpImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by 宏炜 on 2017-08-17.
 */
@Component
public class UpImageQuartz {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpImageQuartz.class);

    @Autowired
    UpImageService upImageService;

    public void run() {
        try {
            try{
                upImageService.updateUpload24HourImage();
            }catch (BizException ex){
                LOGGER.error(ex.getMessage());
            }
        } catch (Exception ex) {
        }
    }
}
