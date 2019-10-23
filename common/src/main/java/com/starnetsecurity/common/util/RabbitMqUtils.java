package com.starnetsecurity.common.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by 宏炜 on 2017-07-31.
 */
public class RabbitMqUtils {

    private static Logger log = LoggerFactory.getLogger(RabbitMqUtils.class);

    private static ConnectionFactory connectionFactory;
    private static final String exchangeName = "parkExchange";

    private static String userName;
    private static String password;
    private static String host;
    private static Integer port;

    public static void init(String userName,String password,String host,Integer port){
        RabbitMqUtils.userName = userName;
        RabbitMqUtils.password = password;
        RabbitMqUtils.host = host;
        RabbitMqUtils.port = port;
        connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername(userName);
        connectionFactory.setPassword(password);
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
    }

    public static boolean sendMsg(String queueName,String msg){
        try {
            Connection conn = connectionFactory.newConnection();
            Channel channel = conn.createChannel();
            channel.exchangeDeclare(exchangeName, "direct", true);
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, exchangeName, queueName + "_key");
            channel.basicPublish(exchangeName, queueName + "_key", null, msg.getBytes());
            channel.close();
            conn.close();
            return true;
        } catch (IOException e) {
            return false;
        } catch (TimeoutException e) {
            return false;
        } catch (Exception e){
            return false;
        }
    }

    public static Channel getReceiveChannel(String queueName){
        try {
            Connection conn = connectionFactory.newConnection();
            Channel channel = conn.createChannel();
            channel.exchangeDeclare(exchangeName, "direct", true);
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, exchangeName, queueName + "_key");
            return channel;
        } catch (IOException e) {
            log.error("消息队列接收IO异常，exchangeName：{}，queueName：{}，errorMsg：",exchangeName,queueName,e);
        } catch (TimeoutException e) {
            log.error("消息队列接收超时异常，exchangeName：{}，queueName：{}，errorMsg：",exchangeName,queueName,e);
        }
        return null;
    }


}
