package com.starnetsecurity.parkClientServer.sockServer;

/**
 * Created by Administrator on 2018/6/13.
 */
public class DevSendPackage {

    private SocketClient socketClient;
    private String msg;

    public SocketClient getSocketClient() {
        return socketClient;
    }

    public void setSocketClient(SocketClient socketClient) {
        this.socketClient = socketClient;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
