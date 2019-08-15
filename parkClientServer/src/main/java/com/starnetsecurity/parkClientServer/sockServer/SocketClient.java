package com.starnetsecurity.parkClientServer.sockServer;

import com.starnetsecurity.parkClientServer.entity.DeviceInfo;
import com.starnetsecurity.parkClientServer.entity.Operator;
import com.starnetsecurity.parkClientServer.entity.PostComputerManage;

import java.net.Socket;
import java.util.UUID;

/**
 * Created by 宏炜 on 2017-12-11.
 */
public class SocketClient {

    private String id;
    private Socket socket;
    private String channelId;
    //仅支持设备连接时存在
    private DeviceInfo deviceInfo;
    private Operator operator;
    private PostComputerManage postComputerManage;

    public SocketClient() {
        this.id = UUID.randomUUID().toString().replaceAll("-","");
    }

    public String getId() {
        return id;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public SocketClient(Socket socket, String channelId){
        this.id = UUID.randomUUID().toString().replaceAll("-","");
        this.socket = socket;
        this.channelId = channelId;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public PostComputerManage getPostComputerManage() {
        return postComputerManage;
    }

    public void setPostComputerManage(PostComputerManage postComputerManage) {
        this.postComputerManage = postComputerManage;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        SocketClient that = (SocketClient)obj;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return true;
    }
}
