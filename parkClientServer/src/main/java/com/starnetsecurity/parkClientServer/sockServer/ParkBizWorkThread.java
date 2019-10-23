package com.starnetsecurity.parkClientServer.sockServer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.util.HttpRequestUtils;
import com.starnetsecurity.parkClientServer.entity.PostComputerManage;
import com.starnetsecurity.parkClientServer.init.BizUnitTool;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.Map;

/**
 * Created by 宏炜 on 2017-11-30.
 */
public class ParkBizWorkThread extends Thread {
    @Autowired
    HibernateBaseDao baseDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(ParkBizWorkThread.class);

    private SocketClient socketClient;

    public final static String notSupportBizMsg = "暂不支持该业务";

    public final static String exceptionResultMsg = "系统服务器发生未知异常";

    public final static String JSONExceptionMsg = "JSON数据报文格式错误";

    public final static String bizExceptionResult = "-1";

    public final static String exceptionResult = "-2";

    public final static String successResult = "0";

    private Timestamp activeTime;


    public SocketClient getSocketClient() {
        return socketClient;
    }

    public void setSocketClient(SocketClient socketClient) {
        this.socketClient = socketClient;
    }

    @Override
    public void run() {
        activeTime = CommonUtils.getTimestamp();
        while(true){
            try{
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
                    ParkSocketServer.removeClient(socketClient);
                    BizUnitTool.clientBizService.updatePostComputerStatus(socketClient.getPostComputerManage(),0);
                    break;
                }
                String msg = SocketUtils.receivePackage(this.socketClient.getSocket());


                if(!StringUtils.isBlank(msg)){
                    JSONObject json = null;
                    try{
                        json = JSON.parseObject(msg);
                        ClientBizEnum type = ClientBizEnum.valueOf(json.getString("type"));
                        JSONObject params = json.getJSONObject("data");
                        if(CommonUtils.isEmpty(socketClient.getOperator()) && !StringUtils.isBlank(params.getString("token"))){
                            tokenReconnect(socketClient,params.getString("token"));
                        }
                        if(!type.equals(ClientBizEnum.heartCheck) && !type.equals(ClientBizEnum.login)){
                            if(CommonUtils.isEmpty(socketClient.getOperator())){
                                throw new BizException("用户未登录");
                            }
                        }
                        JSONObject resJson = null;
                        switch (type){
                            case heartCheck:
                                Timestamp now = CommonUtils.getTimestamp();
                                if(!CommonUtils.isEmpty(socketClient.getOperator())){
                                    BizUnitTool.clientBizService.updatePostComputerStatus(socketClient.getPostComputerManage(),1);
                                }
                                this.activeTime = now;
                                break;
                            case login:
                                resJson = BizUnitTool.clientBizService.login(params.getString("username"),params.getString("password"),params.getString("ip"),socketClient);
                                break;
                            case logout:
                                resJson = BizUnitTool.clientBizService.logout(socketClient);
                                break;
                            case outPark:
                                resJson = BizUnitTool.clientBizService.handleInOutRecord(params,socketClient);
                                break;
                            case inPark:
                                resJson = BizUnitTool.clientBizService.handleInOutRecord(params,socketClient);
                                break;
                            case CarPlateManual:
                                resJson = BizUnitTool.clientBizService.pushManualRecord(params,socketClient);
                                break;
                            case shiftOperator:
                                resJson = BizUnitTool.clientBizService.shiftOperator(params,socketClient);
                                break;
                            case shiftOut:
                                resJson = BizUnitTool.clientBizService.shiftLogout(socketClient);
                                break;
                            case recvCheck:
                                String uid = params.getString("uid");
                                if(StringUtils.isBlank(uid)){
                                    break;
                                }
                                for(ClientSendThread clientSendThread : ParkSocketServer.clientSendThreads){
                                    if(StringUtils.isBlank(clientSendThread.getCurUid())){
                                        continue;
                                    }
                                    if(clientSendThread.getCurUid().equals(uid)){
                                        clientSendThread.setStatus(true);
                                    }
                                }
                                break;

                        }
                        if(!CommonUtils.isEmpty(resJson)){
                            resJson.put("result","0");
                            SocketUtils.sendBizPackage(socketClient, resJson, json.getString("type"));
                        }

                    }catch (JSONException e){
                        SocketUtils.sendBizPackage(socketClient,getJSONExceptionResult(),"null_type");
                        continue;
                    }catch (IllegalArgumentException e){
                        SocketUtils.sendBizPackage(socketClient,getBizExceptionResult(notSupportBizMsg),json.getString("type"));
                        continue;
                    }catch (BizException e){
                        SocketUtils.sendBizPackage(socketClient,getBizExceptionResult(e.getMessage()),json.getString("type"));
                        continue;
                    }catch (Exception e){
                        SocketUtils.sendBizPackage(socketClient,getExceptionResult(),json.getString("type"));
                        continue;
                    }

                }
                if(StringUtils.isBlank(msg)){
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) {
            } catch (BizException ex){
                ParkSocketServer.removeClient(socketClient);
                BizUnitTool.clientBizService.updatePostComputerStatus(socketClient.getPostComputerManage(),0);
                break;
            }finally {
                int second = 9;
                Timestamp now = CommonUtils.getTimestamp();
                if((now.getTime() - activeTime.getTime()) > ( second * 1000)){
                    for (int i = 0; i < ParkSocketServer.getClientsCount(); i++) {
                        SocketClient client = ParkSocketServer.getClient(i);
                        if (socketClient.getSocket().getInetAddress().getHostAddress().equals(client.getSocket().getInetAddress().getHostAddress())) {
                            ParkSocketServer.removeClient(client);
                        }
                    }
                    BizUnitTool.clientBizService.updatePostComputerStatus(socketClient.getPostComputerManage(),0);
                    break;
                }
            }
        }
    }

    public static JSONObject getBizExceptionResult(String msg){
        JSONObject data = new JSONObject();
        data.put("result",bizExceptionResult);
        data.put("msg",msg);
        return data;
    }

    public static JSONObject getExceptionResult(){
        JSONObject data = new JSONObject();
        data.put("result",exceptionResult);
        data.put("msg",exceptionResultMsg);
        return data;
    }

    public static JSONObject getJSONExceptionResult(){
        JSONObject data = new JSONObject();
        data.put("result",bizExceptionResult);
        data.put("msg",JSONExceptionMsg);
        return data;
    }

    public static void tokenReconnect(SocketClient socketClient,String token) throws BizException{
        AesCipherService aesCipherService = new AesCipherService();
        ByteSource byteSource = aesCipherService.decrypt(Hex.decode(token),Hex.decode(HttpRequestUtils.aesKey));
        String deToken = new String(byteSource.getBytes());
        JSONObject params = JSON.parseObject(deToken);
        BizUnitTool.clientBizService.reconnectLogin(params.getString("username"),params.getString("password"),params.getString("ips"),socketClient);
    }
}
