package com.starnetsecurity.parkClientServer.sockServer;

import com.starnetsecurity.common.util.CommonUtils;

import java.net.Socket;
import java.util.Random;
import java.util.UUID;

/**
 * Created by 宏炜 on 2017-12-14.
 */
public class SIPUtils {

    public static String getTag(){
        Random random = new Random();
        int a = random.nextInt(9) + 1;

        String res = "";
        res += a;
        for(int i = 0; i < 9; i++){
            int b = (int)(10*(Math.random()));
            res += b;
        }
        return res;
    }

    public static String getNonce(){
        return UUID.randomUUID().toString().replace("-","");
    }

    public static SipResponsePackage responseSuccess(SipResponsePackage response, SipRequestPackage request){
        response.setSipVersion(request.getSipVersion());
        response.setCode("200");
        response.setResult("OK");
        response.setVia(request.getVia());
        response.setFrom(request.getFrom());
        response.setTo(request.getTo() + ";tag=" + getTag());
        response.setCallID(request.getCallID());
        response.setCsEq(request.getCsEq());
        return response;

    }

    public static int responseAuthNotify(Socket socket, SipRequestPackage request){
        if(CommonUtils.isEmpty(request)){
            return 0;
        }
        SipResponsePackage response = new SipResponsePackage();
        response = responseSuccess(response,request);
        response.setContentType("Application/MANSCDP+xml");

        String resxml = "<?xml version=\"1.0\"?>" +
                "<Respond>" +
                "<CmdType>AuthNotify</CmdType>" +
                "<Status>OK</Status></Respond>";
        response.setBody(resxml);
        return SocketUtils.sendSIPToClient(socket,response.toString());
    }

    public static int responseKeepAlive(SocketClient socketClient, SipRequestPackage request){
        if(CommonUtils.isEmpty(request)){
            return 0;
        }
        SipResponsePackage response = new SipResponsePackage();
        response = responseSuccess(response,request);
        response.setContentType("Application/MANSCDP+xml");
        String ip = request.getFrom().split("@")[1];
        ip = ip.split(">")[0];
        String resxml = "<?xml version=\"1.0\"?>" +
                "<Respond>" +
                "<CmdType>KeepAlive</CmdType>" +
                "<DeviceIP>" + ip + "</DeviceIP>" +
                "</Respond>";
        response.setBody(resxml);

        DevSendPackage devSendPackage = new DevSendPackage();
        devSendPackage.setMsg(response.toString());
        devSendPackage.setSocketClient(socketClient);
        for(DeviceSendThread deviceSendThread : DeviceSIPServer.devSendThreads){
            if(socketClient.getSocket().getInetAddress().getHostAddress().equals(deviceSendThread.getIP())){
                deviceSendThread.getSendQueue().offer(devSendPackage);
            }

        }
        return 0;
    }

    public static int responseCarPlateIndPrev(Socket socket, SipRequestPackage request){
        if(CommonUtils.isEmpty(request)){
            return 0;
        }
        SipResponsePackage response = new SipResponsePackage();
        response = responseSuccess(response,request);
        response.setContentType("Application/MANSCDP+xml");

        String resxml = "<?xml version=\"1.0\"?>" +
                "<Respond>" +
                "<CmdType>CarPlateIndPrev</CmdType>" +
                "<Status>OK</Status>" +
                "</Respond>";
        response.setBody(resxml);
        return SocketUtils.sendSIPToClient(socket,response.toString());
    }


    public static int responseCarPlateInd(Socket socket, SipRequestPackage request){
        if(CommonUtils.isEmpty(request)){
            return 0;
        }
        SipResponsePackage response = new SipResponsePackage();
        response = responseSuccess(response,request);
        response.setContentType("Application/MANSCDP+xml");

        String resxml = "<?xml version=\"1.0\"?>" +
                "<Respond>" +
                "<CmdType>CarPlateInd</CmdType>" +
                "<Status>OK</Status>" +
                "</Respond>";
        response.setBody(resxml);
        return SocketUtils.sendSIPToClient(socket,response.toString());
    }
}
