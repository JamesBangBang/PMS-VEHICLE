package com.starnetsecurity.parkClientServer.sockServer;

import com.alibaba.fastjson.JSON;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2017-12-13.
 */
public class DeviceSIPWorkThread extends Thread {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParkBizWorkThread.class);

    private SocketClient socketClient;

    private Timestamp activeTime;

    public SocketClient getSocketClient() {
        return socketClient;
    }

    public void setSocketClient(SocketClient socketClient) {
        this.socketClient = socketClient;
    }

    @Override
    public void run() {
        this.activeTime = CommonUtils.getTimestamp();
        while(true){
            try{
                boolean statusCheck = true;
                if(CommonUtils.isEmpty(socketClient)){
                    statusCheck = false;
                }
                if(CommonUtils.isEmpty(socketClient.getSocket())){
                    statusCheck = false;
                }
                if(!socketClient.getSocket().isConnected()){
                    statusCheck = false;
                }
                if(socketClient.getSocket().isClosed()){
                    statusCheck = false;
                }
                if(!statusCheck){
                    LOGGER.error("设备已离线,工作线程退出,设备IP：{}",socketClient.getSocket().getInetAddress());
                    break;
                }
                SipRequestPackage request = SocketUtils.readSIPFromClient(this.socketClient.getSocket());

                if(!CommonUtils.isEmpty(request)){
                    SIPRequestTypeEnum sipRequestTypeEnum;
                    if(request.getRequestMethod().equals("SIP/2.0")){
                        sipRequestTypeEnum = SIPRequestTypeEnum.MESSAGE;
                    }else{
                        sipRequestTypeEnum = SIPRequestTypeEnum.valueOf(request.getRequestMethod());
                    }

                    DeviceSIPServer.cseqMap.put(socketClient.getSocket().getInetAddress().getHostAddress(),Integer.parseInt(request.getCsEq().split(" ")[0]));
                    DeviceSIPServer.fromMap.put(socketClient.getSocket().getInetAddress().getHostAddress(),request.getFrom());
                    DeviceSIPServer.callIdMap.put(socketClient.getSocket().getInetAddress().getHostAddress(),request.getCallID());
                    switch (sipRequestTypeEnum){
                        case REGISTER:
                            if(request.getCsEq().equals("1 REGISTER")){
                                SipResponsePackage response = new SipResponsePackage();
                                response.setCode("401");
                                response.setResult("Unauthorized");
                                response.setSipVersion(request.getSipVersion());
                                response.setVia(request.getVia().replaceAll("rport","rport=" + socketClient.getSocket().getPort()));
                                response.setFrom(request.getFrom());
                                response.setTo(request.getTo() + ";tag=" + SIPUtils.getTag());
                                response.setCallID(request.getCallID());
                                response.setCsEq(request.getCsEq());
                                response.setUserAgent(request.getUserAgent());
                                response.setWWWAuthenticate("Digest realm=\"af.star-net.cn\",nonce=\"" + SIPUtils.getNonce() + "\", algorithm=MD5");
                                SocketUtils.sendSIPToClient(socketClient.getSocket(),response.toString());

                            }else{
                                SipResponsePackage response = new SipResponsePackage();
                                response.setSipVersion(request.getSipVersion());
                                response.setCode("200");
                                response.setResult("OK");
                                response.setVia(request.getVia().replaceAll("rport","rport=" + socketClient.getSocket().getPort()));
                                response.setFrom(request.getFrom());
                                response.setTo(request.getTo() + ";tag=" + SIPUtils.getTag());
                                response.setCallID(request.getCallID());
                                response.setCsEq(request.getCsEq());
                                response.setUserAgent(request.getUserAgent());
                                SocketUtils.sendSIPToClient(socketClient.getSocket(),response.toString());
                            }
                            break;
                        case MESSAGE:
                            if(!StringUtils.isBlank(request.getBody())){
                                try {
                                    Document document = DocumentHelper.parseText(request.getBody());
                                    Element rootElement = document.getRootElement();
                                    Element cmdTypeElement = rootElement.element("CmdType");
                                    String cmdType = cmdTypeElement.getText();
                                    DeviceSIPEnum deviceSIPEnum = DeviceSIPEnum.valueOf(cmdType);
                                    switch(deviceSIPEnum){
                                        case AuthNotify:
                                            SIPXmlBizTool.dealAuthNotify(rootElement,socketClient.getSocket().getInetAddress().getHostAddress(),socketClient);
                                            SIPUtils.responseAuthNotify(socketClient.getSocket(),request);
                                            break;
                                        case KeepAlive:
                                            Timestamp now = CommonUtils.getTimestamp();
                                            this.activeTime = now;
                                            SIPXmlBizTool.dealKeepAlive(socketClient.getDeviceInfo().getDeviceId());
                                            SIPUtils.responseKeepAlive(socketClient,request);
                                            break;
                                        case CarPlateIndPrev:
                                            SIPXmlBizTool.dealCarPlateIndPrev(rootElement,socketClient);
                                            SIPUtils.responseCarPlateIndPrev(socketClient.getSocket(),request);
                                            break;
                                        case CarPlateInd:
                                            SIPXmlBizTool.dealCarPlateInd(rootElement,socketClient);
                                            SIPUtils.responseCarPlateInd(socketClient.getSocket(),request);
                                            break;
                                        default:
                                            break;

                                    }
                                }catch (Exception e){
                                    LOGGER.error("Exception异常:{}",JSON.toJSONString(request),e);
                                    DeviceSIPServer.removeClient(socketClient);
                                    SocketUtils.closeSocket(socketClient.getSocket());
                                    socketClient.setSocket(null);
                                }
                            }
                            break;
                    }

                }
                if(CommonUtils.isEmpty(request)){
                    Thread.sleep(20);
                }
            } catch (InterruptedException e) {

            } catch (BizException ex){

                DeviceSIPServer.removeClient(socketClient);
                SocketUtils.closeSocket(socketClient.getSocket());
                socketClient.setSocket(null);
                LOGGER.error("设备数据处理异常,离线处理:",ex);
                break;
            }finally {
                int second = 30;
                Timestamp now = CommonUtils.getTimestamp();
                if((now.getTime() - activeTime.getTime()) > (second * 1000)){
                    LOGGER.error("超过{}秒未检测到心跳包数据,设备下线处理,设备IP:{}:",second,socketClient.getSocket().getInetAddress());
                    DeviceSIPServer.removeClient(socketClient);
                    SocketUtils.closeSocket(socketClient.getSocket());
                    socketClient.setSocket(null);
                    break;
                }
            }
        }
    }

}
