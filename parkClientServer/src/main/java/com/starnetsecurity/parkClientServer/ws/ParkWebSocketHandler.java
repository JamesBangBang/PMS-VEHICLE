package com.starnetsecurity.parkClientServer.ws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.parkClientServer.entity.AdminUser;
import com.starnetsecurity.parkClientServer.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 宏炜 on 2017-10-26.
 */
public class ParkWebSocketHandler implements WebSocketHandler {

    private static Logger log = LoggerFactory.getLogger(ParkWebSocketHandler.class);

    public static final  List<WebSocketUser> socketUser = new ArrayList<>();

    @Autowired
    UserService userService;

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        ////log.info("接入新用户.......");

    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        Object o = webSocketMessage.getPayload();
        if(o instanceof String){
            String params = String.valueOf(o);
            JSONObject json = JSON.parseObject(params);
            String userId = json.getString("userId");
            AdminUser adminUser = userService.getAdminUserById(userId);
            WebSocketUser webSocketUser = new WebSocketUser();
            webSocketUser.setAdminUser(adminUser);
            webSocketUser.setWebSocketSession(webSocketSession);
            socketUser.add(webSocketUser);
            ////log.info("标识登录用户:{}",adminUser.getUserName());
        }

    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        WebSocketUser current = null;
        for(WebSocketUser webSocketUser : socketUser){
            if(webSocketUser.getWebSocketSession().getId().equals(webSocketSession.getId())){
                current = webSocketUser;
            }
        }
        if(!CommonUtils.isEmpty(current)){
            socketUser.remove(current);
        }

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
