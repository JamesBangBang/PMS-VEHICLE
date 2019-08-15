package com.starnetsecurity.parkClientServer.sockServer;

import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.parkClientServer.entity.DeviceInfo;
import com.starnetsecurity.parkClientServer.init.BizUnitTool;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by 宏炜 on 2017-12-14.
 */
public class SIPXmlBizTool {

    private static final Logger LOGGER = LoggerFactory.getLogger(SIPXmlBizTool.class);

    public static JSONObject parseXML(Element element){
        if(CommonUtils.isEmpty(element)){
            return null;
        }
        JSONObject json = new JSONObject();
        List<Element> elements = element.elements();
        for(Element node : elements){
            String key = node.getName();
            String value = node.getText();
            json.put(key,value);
        }
        return json;
    }

    public static void dealCarPlateIndPrev(Element element, SocketClient socketClient) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException {
        JSONObject json = parseXML(element);
        String bHistory = json.getString("bHistory");
        if(bHistory.equals("1")){
            BizUnitTool.deviceBizService.dealHistoryCarPlateInd(socketClient,json);
        }else{
            BizUnitTool.deviceBizService.pushCarPlateIndMsg(socketClient,json);
        }
    }

    public static void dealCarPlateInd(Element element,SocketClient socketClient) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException {
        JSONObject json = parseXML(element);
        BizUnitTool.deviceBizService.dealCarPlateInd(socketClient,json);
    }

    public static void dealAuthNotify(Element element,String ip,SocketClient socketClient){
        JSONObject json = parseXML(element);
        BizUnitTool.deviceBizService.mergeDevice(json,ip,socketClient);
    }

    public static void dealKeepAlive(String deviceId){
        BizUnitTool.deviceBizService.updateDeviceStatus(deviceId,"1");
    }
}
