package com.starnetsecurity.parkClientServer.sockServer;

import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.parkClientServer.init.BizUnitTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 宏炜 on 2017-11-30.
 */
public class ClientHeartThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHeartThread.class);

    @Override
    public void run() {
        while(true){
            SocketClient socketClient = null;
            try{
                for(int i = 0; i < ParkSocketServer.getClientsCount(); i++){
                    socketClient = ParkSocketServer.getClient(i);

                    boolean statusCheck = true;
                    if(CommonUtils.isEmpty(socketClient)){
                        statusCheck = false;
                    }
                    if(!socketClient.getSocket().isConnected()){
                        statusCheck = false;
                    }
                    if(socketClient.getSocket().isClosed()){
                        statusCheck = false;
                    }
                    if(!statusCheck){
                        //ParkSocketServer.removeClient(socketClient);
                        //ParkSocketServer.removeClientsCount();
                        //BizUnitTool.clientBizService.updatePostComputerStatus(socketClient.getPostComputerManage(),0);
                        continue;
                    }
                    JSONObject data = new JSONObject();
                    SocketUtils.sendHeartPackage(socketClient,data);

                }
                Thread.sleep(3000);
            }catch (BizException ex){
                if(socketClient != null){

                }

            }catch (InterruptedException e) {
                LOGGER.error("心跳包间隔异常",e);
            }catch(Exception e){
                if(socketClient != null){
                    LOGGER.error("心跳线未知异常，IP{}",socketClient.getSocket().getInetAddress().getHostAddress(),e);
                }else{
                    LOGGER.error("心跳线未知异常，IP = null ",e);
                }
            }
        }
    }
}
