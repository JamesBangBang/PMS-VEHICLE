package com.starnetsecurity.parkClientServer.quartz;

import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.parkClientServer.entity.CarparkRegister;
import com.starnetsecurity.parkClientServer.init.AppInfo;
import com.starnetsecurity.parkClientServer.mq.ParkLotConsumerConfiguration;
import com.starnetsecurity.parkClientServer.service.ParkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by 宏炜 on 2017-08-15.
 */
@Component
public class ParkMqQuartz {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParkMqQuartz.class);

    @Autowired
    ParkService parkService;

//    @Autowired
//    RabbitQueueListenerThreadFactory rabbitQueueListenerThreadFactory;

//    @Autowired
//    CachingConnectionFactory cachingConnectionFactory;

    public void run() {

        try {
            LOGGER.info("车场消息队列定时器启动");
            try{
                List<CarparkRegister> list = parkService.getRegisteredParks();
                for(CarparkRegister carparkRegister : list){
                    boolean isExit = false;
                    for(ParkLotConsumerConfiguration parkLotConsumerConfiguration : AppInfo.rabbitQueueListenerThreads){
                        if(carparkRegister.getServiceId().equals(parkLotConsumerConfiguration.getQueueName())){
                            isExit = true;
                        }
                    }
//                    if(!isExit){
//                        ParkLotConsumerConfiguration parkLotConsumerConfiguration = new ParkLotConsumerConfiguration(carparkRegisterEntity.getServiceId(),carparkRegisterEntity.getServiceId() + "_key",cachingConnectionFactory,parkService);
//                        AppInfo.rabbitQueueListenerThreads.add(parkLotConsumerConfiguration);
//                    }
                }

            }catch (BizException ex){
                LOGGER.error(ex.getMessage());
            }
        } catch (Exception ex) {
            LOGGER.error("车场消息队列定时器错误:", ex);
        }
    }
}
