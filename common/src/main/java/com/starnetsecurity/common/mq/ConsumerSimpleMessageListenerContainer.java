package com.starnetsecurity.common.mq;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

/**
 * Created by 宏炜 on 2017-10-13.
 */
public class ConsumerSimpleMessageListenerContainer extends SimpleMessageListenerContainer {
    public void startConsumers() throws Exception {
        super.doStart();
    }
}
