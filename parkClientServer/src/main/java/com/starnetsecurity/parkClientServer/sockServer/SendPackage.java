package com.starnetsecurity.parkClientServer.sockServer;

/**
 * Created by Administrator on 2018/4/27.
 */
public class SendPackage {

    private SocketClient socketClient;
    private String msg;
    private ClientBizEnum bizEnum;
    private String uid;
    private int isNeedRecv;

    public int getIsNeedRecv() {
        return isNeedRecv;
    }

    public void setIsNeedRecv(int isNeedRecv) {
        this.isNeedRecv = isNeedRecv;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ClientBizEnum getBizEnum() {
        return bizEnum;
    }

    public void setBizEnum(ClientBizEnum bizEnum) {
        this.bizEnum = bizEnum;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public SocketClient getSocketClient() {
        return socketClient;
    }

    public void setSocketClient(SocketClient socketClient) {
        this.socketClient = socketClient;
    }
}
