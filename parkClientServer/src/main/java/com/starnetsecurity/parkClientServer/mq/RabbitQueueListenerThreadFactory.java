package com.starnetsecurity.parkClientServer.mq;

import com.starnetsecurity.parkClientServer.service.ParkService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by 宏炜 on 2017-08-08.
 */
public class RabbitQueueListenerThreadFactory {

    @Autowired
    ParkService parkService;

    public RabbitQueueListenerThread createInstance(){
        RabbitQueueListenerThread rabbitQueueListenerThread = new RabbitQueueListenerThread();
        rabbitQueueListenerThread.setParkService(parkService);
        return rabbitQueueListenerThread;
    }
}
