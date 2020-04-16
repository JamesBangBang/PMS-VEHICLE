package com.starnetsecurity.parkClientServer.sockServer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.codec.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;

/**
 * Created by 宏炜 on 2017-11-30.
 */
public class SocketUtils {

    private final static String iOExceptionMsg = "IO异常";

    private final static String bindExceptionMsg = "端口绑定异常";

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketUtils.class);

    private ServerSocket serverSocket;

    public void initServer(int port){
        try{
            if(!CommonUtils.isEmpty(this.serverSocket)){
                LOGGER.error("serverSocket 初始化错误,重复初始化,请先行销毁");
            }
            this.serverSocket = new ServerSocket(port);
        }catch (IOException ex){
            LOGGER.error("serverSocket 初始化失败:{}",iOExceptionMsg,ex);
        }catch (BizException ex){
            LOGGER.error("serverSocket 初始化失败:{}",bindExceptionMsg,ex);
        }
    }

    public Socket acceptClient(){
        try {
            Socket socket = this.serverSocket.accept();
            return socket;
        } catch (IOException ex) {
            LOGGER.error("serverSocket 监听失败:{}",iOExceptionMsg,ex);
        }
        return null;
    }

    public void destroyServerSocket(){
        try {
            this.serverSocket.close();
        } catch (IOException ex) {
            LOGGER.error("serverSocket 关闭失败:{}",iOExceptionMsg,ex);
        }
    }

    public static int sendToClient(Socket socket,String msg) throws BizException{
        try{
            if(socket.isClosed()){
                return -1;
            }
            if(StringUtils.isBlank(msg)){
                LOGGER.error("发送失败,发送内容为空");
                return 0;
            }
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream,"GBK"),true);
            int len = msg.getBytes("GBK").length;

//            if(msgBytes.length > 1024000){
//                LOGGER.error("发送失败,数据长度超过1024000,发送数据长度为:{}",msgBytes.length);
//                return 0;
//            }

//            byte[] size = DecimalToHex(msgBytes.length);
//            byte[] sendBytes = new byte[size.length + msgBytes.length];
//            System.arraycopy(size,0,sendBytes,0,size.length);
//            System.arraycopy(msgBytes,0,sendBytes,size.length,msgBytes.length);
//            outputStream.write(sendBytes);
            msg = DecimalToHexString(len) + msg;
            printWriter.print(msg);
            printWriter.flush();
            return len + 4;
        }catch (IOException ex){
            LOGGER.error("向[{}]:[{}]发送失败:{}",socket.getInetAddress(),socket.getPort(),iOExceptionMsg,ex);
            throw new BizException("数据发送异常:" + iOExceptionMsg);
        }
    }

    public static int sendSIPToClient(Socket socket,String msg) throws BizException{
        try{
            if(StringUtils.isBlank(msg)){
                LOGGER.error("发送失败,发送内容为空");
                return 0;
            }

            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream,"UTF-8"),true);
            printWriter.print(msg);
            printWriter.flush();
            return msg.length();
        }catch (IOException ex){
            throw new BizException("数据发送异常:" + iOExceptionMsg);
        }
    }

    public static int sendHeartPackage(SocketClient socketClient, JSONObject data) throws BizException{
        String uid = UUID.randomUUID().toString().replaceAll("-","");
        Map<String,Object> heart = new HashedMap();
        heart.put("timestamp", CommonUtils.getTimestamp().getTime() / 1000);
        heart.put("type",ClientBizEnum.heartCheck.name());
        heart.put("server-version", ParkSocketServer.serverVersion);
        heart.put("client-version",ParkSocketServer.clientVersion);
        heart.put("uid", uid);
        heart.put("data", data);
        heart.put("isNeedRecv","0");
        SendPackage sendPackage = new SendPackage();
        sendPackage.setSocketClient(socketClient);
        sendPackage.setMsg(JSON.toJSONString(heart,SerializerFeature.WriteMapNullValue));
        sendPackage.setBizEnum(ClientBizEnum.heartCheck);
        sendPackage.setUid(uid);
        sendPackage.setIsNeedRecv(0);
        for(ClientSendThread clientSendThread : ParkSocketServer.clientSendThreads){
            if(clientSendThread.getIP().equals(socketClient.getSocket().getInetAddress().getHostAddress())){
                clientSendThread.getSendQueue().offer(sendPackage);
            }
        }

        return 0;
    }

    public static String receiveMsgFromClient(Socket socket) throws BizException{
        try {
            InputStream inputStream = socket.getInputStream();
            if(inputStream.available() < 4){
                return null;
            }
            byte[] head = new byte[4];
            int headSize = 0;
            while(headSize < 4){
                int ret = inputStream.read(head,headSize,4 - headSize);
                if(ret < 0){
                    throw new BizException("接收数据异常:接收返回:" + ret);
                }
                headSize += ret;
            }
            int contentLength = HexToDecimal(head);
            if(contentLength < 0){
                return null;
            }
            int contentSize = 0;
            byte[] content = new byte[contentLength];
            while(contentSize < contentLength){
                int ret = inputStream.read(content,contentSize,contentLength - contentSize);
                if(ret < 0){
                    throw new BizException("接收数据异常:接收返回:" + ret);
                }
                contentSize += ret;
            }
            String msg = new String(content,"UTF-8");
            return msg;
        } catch (IOException ex) {
            LOGGER.error("接收数据异常:{}",iOExceptionMsg,ex);
            throw new BizException("接收数据异常:" + iOExceptionMsg);
        }
    }

    public static SipRequestPackage readSIPFromClient(Socket socket) throws BizException{
        try {

            InputStream inputStream = socket.getInputStream();
            if(inputStream.available() <= 0){
                return null;
            }

            String msg = "";
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            int i = 0;
            SipRequestPackage sipRequestPackage = null;
            while(true){
                String line = bufferedReader.readLine();
                if(StringUtils.isBlank(line)){
                    break;
                }
                if(i == 0){
                    sipRequestPackage = new SipRequestPackage();
                    if (!sipRequestPackage.appendHead(line)){
                        sipRequestPackage = null;
                        break;
                    }
                }else{
                    sipRequestPackage.append(line);
                }
                msg += line;
                i++;
            }
            if(sipRequestPackage != null && sipRequestPackage.getContentLength() > 0){
                int bodySize = 0;
                char[] body = new char[sipRequestPackage.getContentLength()];
                while(bodySize < sipRequestPackage.getContentLength()){
                    int ret = bufferedReader.read(body,bodySize,sipRequestPackage.getContentLength() - bodySize);
                    if(ret > 0){
                        bodySize += ret;
                    }
                }
                String bodyStr = String.valueOf(body);
                msg += bodyStr;
                sipRequestPackage.appendBody(bodyStr);
            }

            return sipRequestPackage;
        } catch (IOException ex) {
            LOGGER.error("接收数据异常:{}",iOExceptionMsg,ex);
            throw new BizException("接收数据异常:" + iOExceptionMsg);
        }catch (Exception ex){
            LOGGER.error("接收数据异常:{}","未知异常",ex);
            throw new BizException("接收数据异常:未知异常");
        }
    }

    public static String receivePackage(Socket socket){
        return receiveMsgFromClient(socket);
    }

    public static String receiveBase64Package(Socket socket) throws BizException{
        String Base64Msg = receiveMsgFromClient(socket);
        if(StringUtils.isBlank(Base64Msg)){
            return null;
        }
        String msg = Base64.decodeToString(Base64Msg);
        return msg;
    }

    public static int sendBizPackage(SocketClient socketClient,JSONObject data,String type) throws BizException{
        if(data.containsKey("msg")){
            String msg = data.getString("msg");
            msg = Base64.encodeToString(msg.getBytes());
            data.put("msg",msg);
        }

        String uid = UUID.randomUUID().toString().replaceAll("-","");
        Map<String,Object> Package = new HashedMap();
        Package.put("timestamp", CommonUtils.getTimestamp().getTime() / 1000);
        Package.put("type",type);
        Package.put("server-version", ParkSocketServer.serverVersion);
        Package.put("client-version",ParkSocketServer.clientVersion);
        Package.put("uid", uid);
        Package.put("data", data);


        SendPackage sendPackage = new SendPackage();
        if(ClientBizEnum.CarPlateIndPrev.name().equals(type)){
            Package.put("isNeedRecv","1");
            sendPackage.setIsNeedRecv(1);
        }else{
            Package.put("isNeedRecv","0");
            sendPackage.setIsNeedRecv(0);
        }
        sendPackage.setSocketClient(socketClient);
        sendPackage.setMsg(JSON.toJSONString(Package,SerializerFeature.WriteMapNullValue));
        sendPackage.setBizEnum(ClientBizEnum.valueOf(type));
        sendPackage.setUid(uid);

        for(ClientSendThread clientSendThread : ParkSocketServer.clientSendThreads){
            if(clientSendThread.getIP().equals(socketClient.getSocket().getInetAddress().getHostAddress())){
                clientSendThread.getSendQueue().offer(sendPackage);
            }
        }
        return 0;
    }

    public static int sendBase64Package(SocketClient socketClient,String msg) throws BizException {
        try {
            String base64msg = Base64.encodeToString(msg.getBytes("UTF-8"));
            SendPackage sendPackage = new SendPackage();
            sendPackage.setSocketClient(socketClient);
            sendPackage.setMsg(base64msg);
            for(ClientSendThread clientSendThread : ParkSocketServer.clientSendThreads){
                if(clientSendThread.getIP().equals(socketClient.getSocket().getInetAddress().getHostAddress())){
                    clientSendThread.getSendQueue().offer(sendPackage);
                }
            }
            return 0;
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("无法Base64编码数据:{}",msg);
        }
        return 0;
    }

    public static void closeSocket(Socket socket){
        try {
            if(socket != null && !socket.isClosed() && socket.isConnected()){
                socket.close();
            }

        } catch (IOException e) {
            LOGGER.error("关闭来自[{}]:[{}]远程主机的连接异常{}",socket.getInetAddress(),socket.getPort(),iOExceptionMsg,e);
        }
    }

    public static byte[] format128Decimal(int size){
        int a = size % 128;
        int b = size / 128;
        byte[] resByte = new byte[2];
        resByte[0] = (byte)b;
        resByte[1] = (byte)a;
        return resByte;
    }

    public static int format128DecimalToDecimal(byte[] bytes){
        int a = bytes[0];
        int b = bytes[1];
        int size = a * 128 + b;
        return size;
    }

    public static int HexToDecimal(byte[] bytes){
        String hex = new String(bytes);
        try{
            int res = Integer.parseInt(hex,16);
            return res;
        }catch (NumberFormatException e){
            return -1;
        }
    }

    public static byte[] DecimalToHex(Integer size) throws UnsupportedEncodingException {
        String res = Integer.toHexString(size);
        while(res.length() < 4){
            res = "0" + res;
        }
        return res.getBytes("UTF-8");
    }

    public static String DecimalToHexString(Integer size){
        String res = Integer.toHexString(size);
        while(res.length() < 4){
            res = "0" + res;
        }
        return res;
    }

}
