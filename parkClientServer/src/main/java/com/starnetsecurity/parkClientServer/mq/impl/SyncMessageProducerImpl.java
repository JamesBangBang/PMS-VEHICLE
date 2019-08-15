package com.starnetsecurity.parkClientServer.mq.impl;

import com.starnetsecurity.parkClientServer.mq.SyncMessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 宏炜 on 2017-10-09.
 */
@Service
public class SyncMessageProducerImpl implements SyncMessageProducer {

    private static Logger log = LoggerFactory.getLogger(SyncMessageProducerImpl.class);
//
//    @Autowired
//    AmqpTemplate amqpTemplate;
//
//    @Override
//    public void sendSyncDataToQueue(String queueKey, Object object) {
//        amqpTemplate.convertAndSend(queueKey, object);
//    }
}
