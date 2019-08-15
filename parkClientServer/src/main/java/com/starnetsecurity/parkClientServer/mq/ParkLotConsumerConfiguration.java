package com.starnetsecurity.parkClientServer.mq;

import com.starnetsecurity.common.mq.ConsumerSimpleMessageListenerContainer;
import com.starnetsecurity.parkClientServer.service.ParkService;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

/**
 * Created by 宏炜 on 2017-10-13.
 */
public class ParkLotConsumerConfiguration {

    private String queueName;
    private String routingKey;
    private RabbitTemplate rabbitTemplate;

    private ConnectionFactory connectionFactory;

    private ParkService parkService;

    public ParkService getParkService() {
        return parkService;
    }

    public void setParkService(ParkService parkService) {
        this.parkService = parkService;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public RabbitTemplate getRabbitTemplate() {
        return rabbitTemplate;
    }

    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public ParkLotConsumerConfiguration(String queueName,String routingKey, ConnectionFactory connectionFactory,ParkService parkService) throws Exception {
        this.parkService = parkService;
        this.queueName = queueName;
        this.routingKey = routingKey;
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.declareQueue(new Queue(this.queueName));
        ConsumerSimpleMessageListenerContainer container = new ConsumerSimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(this.queueName);
        container.setMessageListener(new MessageListenerAdapter(new ReceiveMsgHandler(this.parkService)));
        container.setMessageConverter(new Jackson2JsonMessageConverter());
        container.startConsumers();
    }

}
