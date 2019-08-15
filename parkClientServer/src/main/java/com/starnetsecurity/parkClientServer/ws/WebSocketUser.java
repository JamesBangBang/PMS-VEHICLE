package com.starnetsecurity.parkClientServer.ws;

import com.alibaba.fastjson.JSON;
import com.starnetsecurity.parkClientServer.entity.AdminUser;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;

/**
 * Created by 宏炜 on 2017-10-26.
 */
public class WebSocketUser {

    private AdminUser adminUser;
    private WebSocketSession webSocketSession;

    public AdminUser getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }

    public WebSocketSession getWebSocketSession() {
        return webSocketSession;
    }

    public void setWebSocketSession(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
    }

    public String getUserName(){
        return adminUser.getUserName();
    }

    public void sendMsg(Map<String,String> params) throws IOException {
        TextMessage textMessage = new TextMessage(JSON.toJSONString(params), true);
        this.webSocketSession.sendMessage(textMessage);
    }
}
