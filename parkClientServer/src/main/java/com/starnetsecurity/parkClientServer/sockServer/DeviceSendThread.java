package com.starnetsecurity.parkClientServer.sockServer;

import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Administrator on 2018/6/13.
 */
public class DeviceSendThread extends Thread{

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceSendThread.class);

    public final static long timeout_1 = 2000;

    public final static long timeout_2 = 3000;

    public final static long timeout_3 = 4000;

    private ConcurrentLinkedQueue<DevSendPackage> sendQueue;

    private String IP;

    private boolean status;

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public ConcurrentLinkedQueue<DevSendPackage> getSendQueue() {
        return sendQueue;
    }

    public void setSendQueue(ConcurrentLinkedQueue<DevSendPackage> sendQueue) {
        this.sendQueue = sendQueue;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void init(String IP){
        this.IP = IP;
        this.sendQueue = new ConcurrentLinkedQueue<DevSendPackage>();
    }

    @Override
    public void run() {
        int waitTimes = 0;
        while(true){

            DevSendPackage devSendPackage = null;
            try{
                if(CollectionUtils.isEmpty(this.sendQueue)){
                    Thread.sleep(20);
                }else{
                    devSendPackage = this.sendQueue.poll();
                    waitTimes = 4;
                    status = false;

                    while(!status){
                        if(waitTimes < 4){
                            LOGGER.info("等待设备回复：尝试第 {} 次发送信息",(waitTimes + 1));
                        }
                        for(int i = 0; i < DeviceSIPServer.getClientsCount(); i++){
                            SocketClient client = DeviceSIPServer.getClient(i);

                            if(CommonUtils.isEmpty(devSendPackage.getSocketClient())){
                                break;
                            }
                            if(CommonUtils.isEmpty(devSendPackage.getSocketClient().getSocket())){
                                break;
                            }
                            if(CommonUtils.isEmpty(client.getSocket())){
                                break;
                            }
                            if(devSendPackage.getSocketClient().getSocket().getInetAddress().getHostAddress().equals(client.getSocket().getInetAddress().getHostAddress())){
                                try{

                                    int ret = SocketUtils.sendSIPToClient(client.getSocket(),devSendPackage.getMsg().replaceAll("PORT",client.getSocket().getPort() + ""));

                                }catch (BizException ex){
                                    LOGGER.error("设备数据业务发送异常:{}",ex.getMessage(),ex);

                                }catch (Exception e){
                                    LOGGER.error("设备数据业务发送未知异常，数据发送过程失败",e);
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
                LOGGER.error("设备数据业务发送未知异常，检索客户端过程失败",e);
            }
        }
    }
}
