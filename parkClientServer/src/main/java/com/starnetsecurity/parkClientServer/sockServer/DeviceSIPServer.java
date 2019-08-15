package com.starnetsecurity.parkClientServer.sockServer;

import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.parkClientServer.init.BizUnitTool;

import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by 宏炜 on 2017-12-13.
 */
public class DeviceSIPServer extends Thread{

    private SocketUtils socketUtils;

    private static List<SocketClient> clients;

    private static Integer clientsCount = 0;

    public static List<DeviceSendThread> devSendThreads;

    public static JSONObject callIdMap;

    public static JSONObject fromMap;

    public static JSONObject cseqMap;

    public synchronized static SocketClient addClient(Socket socket, String channelId){
        SocketClient socketClient = new SocketClient(socket,channelId);
        clients.add(socketClient);
        return socketClient;
    }

    public synchronized static void removeClient(SocketClient socketClient){
        if(!CommonUtils.isEmpty(socketClient.getDeviceInfo()))
        {
            BizUnitTool.deviceBizService.updateDeviceStatus(socketClient.getDeviceInfo().getDeviceId(),"0");
        }
        boolean ret = clients.remove(socketClient);
        if (ret){
            clientsCount -= 1;
            if(clientsCount <= 0){
                clientsCount = 0;
            }
        }
    }

    public synchronized static SocketClient getClient(int i){
        return clients.get(i);
    }

    public synchronized static void initClients(){
        clients = new ArrayList<>();
    }

    public synchronized static int getClientsCount(){
        return clientsCount;
    }

    public synchronized static void initClientsCount(){
        clientsCount = 0;
    }

    public synchronized static int addClientsCount(){
        clientsCount += 1;
        return clientsCount;
    }

    public synchronized static int removeClientsCount(){
        clientsCount -= 1;
        return clientsCount;
    }



    public void initServer(){
        callIdMap = new JSONObject();
        fromMap = new JSONObject();
        cseqMap = new JSONObject();
        devSendThreads = new ArrayList<>();
        initClients();
        initClientsCount();
        this.socketUtils = new SocketUtils();
        this.socketUtils.initServer(10004);
    }

    @Override
    public void run() {
        initServer();
        while (true){
            Socket socket = this.socketUtils.acceptClient();
            try {
                socket.setReceiveBufferSize(512 * 1024);
                socket.setKeepAlive(true);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            SocketClient socketClient = addClient(socket, UUID.randomUUID().toString().replaceAll("-",""));
            addClientsCount();

            boolean isSendExist = false;

            for(DeviceSendThread deviceSendThread : DeviceSIPServer.devSendThreads){
                if(deviceSendThread.getIP().equals(socketClient.getSocket().getInetAddress().getHostAddress())){
                    isSendExist = true;
                }
            }
            if(!isSendExist){
                DeviceSendThread deviceSendThread = new DeviceSendThread();
                deviceSendThread.init(socket.getInetAddress().getHostAddress());
                deviceSendThread.start();
                devSendThreads.add(deviceSendThread);
            }

            DeviceSIPWorkThread deviceSIPWorkThread = new DeviceSIPWorkThread();
            deviceSIPWorkThread.setSocketClient(socketClient);
            deviceSIPWorkThread.start();
        }
    }

//    public static void main(String[] argus){
//        DeviceSIPServer deviceSIPServer = new DeviceSIPServer();
//        deviceSIPServer.start();
//        while(true){
//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
