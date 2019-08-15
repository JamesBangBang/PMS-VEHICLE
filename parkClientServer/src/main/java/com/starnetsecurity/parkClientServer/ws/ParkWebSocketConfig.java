package com.starnetsecurity.parkClientServer.ws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Created by 宏炜 on 2017-10-26.
 */
@Configuration
@EnableWebMvc
@EnableWebSocket
public class ParkWebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        //前台 可以使用websocket环境
        registry.addHandler(ParkWebSocketHandler(),"/websocket").addInterceptors(new HandshakeInterceptor());


        //前台 不可以使用websocket环境，则使用sockjs进行模拟连接
        registry.addHandler(ParkWebSocketHandler(), "/sockjs/websocket").addInterceptors(new HandshakeInterceptor())
                .withSockJS();
    }


    // websocket 处理类
    @Bean
    public ParkWebSocketHandler ParkWebSocketHandler(){
        return new ParkWebSocketHandler();
    }


}