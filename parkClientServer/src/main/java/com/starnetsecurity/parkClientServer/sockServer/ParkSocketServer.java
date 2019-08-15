package com.starnetsecurity.parkClientServer.sockServer;

import com.starnetsecurity.common.util.Constant;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by 宏炜 on 2017-11-30.
 */
public class ParkSocketServer extends Thread {

    public static String serverVersion;

    public static String clientVersion;

    private SocketUtils socketUtils;

    private static List<SocketClient> clients;

    private static Integer clientsCount = 0;

    public static List<ClientSendThread> clientSendThreads;

    public synchronized static SocketClient addClient(Socket socket,String channelId){
        SocketClient socketClient = new SocketClient(socket,channelId);
        clients.add(socketClient);
        return socketClient;
    }

    public synchronized static void removeClient(SocketClient socketClient){
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
        if(clientsCount <= 0){
            clientsCount = 0;
        }
        return clientsCount;
    }



    public void initServer(){
        clientSendThreads = new ArrayList<>();
        serverVersion = Constant.getPropertie("server.version");
        clientVersion = Constant.getPropertie("client.version");
        initClients();
        initClientsCount();
        this.socketUtils = new SocketUtils();
        this.socketUtils.initServer(12080);
    }

    @Override
    public void run() {
        initServer();
        while (true){
            Socket socket = this.socketUtils.acceptClient();
            SocketClient socketClient = addClient(socket, UUID.randomUUID().toString().replaceAll("-",""));
            addClientsCount();
            boolean isSendExist = false;

            for(ClientSendThread clientSendThread : ParkSocketServer.clientSendThreads){
                if(clientSendThread.getIP().equals(socketClient.getSocket().getInetAddress().getHostAddress())){
                    isSendExist = true;
                }
            }
            if(!isSendExist){
                /** 如果客户端IP变化，则重新建立一个新的连接 **/
                ClientSendThread clientSendThread = new ClientSendThread();
                clientSendThread.init(socket.getInetAddress().getHostAddress());
                clientSendThread.start();
                clientSendThreads.add(clientSendThread);
            }

            ParkBizWorkThread parkBizWorkThread = new ParkBizWorkThread();
            parkBizWorkThread.setSocketClient(socketClient);
            parkBizWorkThread.start();
        }
    }

//    public static void main(String[] argus){
//        ParkSocketServer parkSocketServer = new ParkSocketServer();
//        parkSocketServer.start();
//        ClientHeartThread clientHeartThread = new ClientHeartThread();
//        clientHeartThread.start();
//        while(true){
//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
