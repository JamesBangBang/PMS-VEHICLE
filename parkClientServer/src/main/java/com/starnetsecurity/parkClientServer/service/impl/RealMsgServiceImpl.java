package com.starnetsecurity.parkClientServer.service.impl;

import com.starnetsecurity.parkClientServer.ws.ParkWebSocketHandler;
import com.starnetsecurity.parkClientServer.ws.WebSocketUser;
import com.starnetsecurity.starParkService.service.RealMsgService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by 宏炜 on 2017-10-26.
 */
public class RealMsgServiceImpl implements RealMsgService {

    @Override
    public void pushInOutMsg(Map<String,Object> params) throws IOException {
        String type = (String)params.get("type");
        Map<String,String> msg = (Map<String,String>)params.get("msg");
        List<String> users = (List<String>)params.get("users");
        for(WebSocketUser webSocketUser : ParkWebSocketHandler.socketUser){
            if(users.contains(webSocketUser.getUserName())){
                webSocketUser.sendMsg(msg);
            }
        }
    }
}
