package com.starnetsecurity.parkClientServer.sockServer;

import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Administrator on 2018/4/27.
 */
public class ClientSendThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientSendThread.class);

    private String IP;

    private boolean status;

    private String curUid;

    public final static long timeout_1 = 2000;

    public final static long timeout_2 = 3000;

    public final static long timeout_3 = 4000;

    private ConcurrentLinkedQueue<SendPackage> sendQueue;

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public ConcurrentLinkedQueue<SendPackage> getSendQueue() {
        return sendQueue;
    }

    public void setSendQueue(ConcurrentLinkedQueue<SendPackage> sendQueue) {
        this.sendQueue = sendQueue;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getCurUid() {
        return curUid;
    }

    public void setCurUid(String curUid) {
        this.curUid = curUid;
    }

    public void init(String IP){
        this.IP = IP;
        this.sendQueue = new ConcurrentLinkedQueue<SendPackage>();
    }

    @Override
    public void run() {
        int waitTimes = 0;
        while(true){

            SendPackage sendPackage = null;
            try{
                if(CollectionUtils.isEmpty(this.sendQueue)){
                    Thread.sleep(20);
                }else{
                    sendPackage = this.sendQueue.poll();
                    if(sendPackage.getIsNeedRecv() == 1){
                        waitTimes = 0;
                        status = false;
                        curUid = sendPackage.getUid();
                    }else{
                        waitTimes = 4;
                        status = false;
                        curUid = sendPackage.getUid();
                    }

                    while(!status){
                        if(waitTimes < 4){
                            LOGGER.info("等待客户端回复：尝试第 {} 次发送信息",(waitTimes + 1));
                        }
                        for(int i = 0; i < ParkSocketServer.getClientsCount(); i++){
                            SocketClient client = ParkSocketServer.getClient(i);

                            if(CommonUtils.isEmpty(sendPackage.getSocketClient())){
                                break;
                            }
                            if(CommonUtils.isEmpty(sendPackage.getSocketClient().getSocket())){
                                break;
                            }
                            if(CommonUtils.isEmpty(client.getSocket())){
                                break;
                            }
                            if(sendPackage.getSocketClient().getSocket().getInetAddress().getHostAddress().equals(client.getSocket().getInetAddress().getHostAddress())){
                                try{

                                    int ret = SocketUtils.sendToClient(client.getSocket(),sendPackage.getMsg());

                                }catch (BizException ex){
                                    LOGGER.error("客户端数据业务发送异常:{}",ex.getMessage(),ex);

                                }catch (Exception e){
                                    LOGGER.error("客户端数据业务发送未知异常，数据发送过程失败",e);
                                }

                            }
                        }

                        waitTimes++;
                        if(waitTimes == 1){
                            for(int time = 0;time < timeout_1;time += 50){
                                if(status){
                                    break;
                                }else{
                                    Thread.sleep(50);
                                }
                            }

                        }else if(waitTimes == 2){
                            for(int time = 0;time < timeout_2;time += 50){
                                if(status){
                                    break;
                                }else{
                                    Thread.sleep(50);
                                }
                            }
                        }else if(waitTimes == 3){
                            for(int time = 0;time < timeout_3;time += 50){
                                if(status){
                                    break;
                                }else{
                                    Thread.sleep(50);
                                }
                            }
                        }else{
                            break;
                        }
                    }
                }
            } catch (Exception e){
                LOGGER.error("客户端数据业务发送未知异常，检索客户端过程失败",e);
            }
        }
    }
}
