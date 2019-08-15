package com.starnetsecurity.parkClientServer.sockServer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.starnetsecurity.common.util.CommonUtils;
import okhttp3.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 宏炜 on 2017-12-12.
 */
public class tcpPressureTest extends Thread
{

    public Socket socket;
    public Integer threadCount;
    public static String msg = "I prefer to listen to music and read a book in a quiet place.";

    @Override
    public void run() {
        int i = 10000;
        while(i > 0){
            JSONObject data = new JSONObject();
            data.put("msgCount",i);
            data.put("threadCount",threadCount);
            data.put("content",msg);
            Map<String,Object> Package = new HashedMap();
            Package.put("timestamp", CommonUtils.getTimestamp().getTime() / 1000);
            Package.put("type","pressureTest");
            Package.put("server-version", ParkSocketServer.serverVersion);
            Package.put("client-version",ParkSocketServer.clientVersion);
            Package.put("data", data);
            int res = SocketUtils.sendToClient(socket,JSON.toJSONString(Package, SerializerFeature.WriteMapNullValue));
            System.out.println("threadCount:" + threadCount + "," + "msgCount:" + i + "," + "res:" + res);
            i--;
        }
    }

    public static void main(String[] argus) throws IOException {
        String sendvalue = "MESSAGE sip:192.168.0.250:10004 SIP/2.0\n" +
                "Via: SIP/2.0/TCP 192.168.0.25:10004;rport;branch=z9hG4bK869992607\n" +
                "From: <sip:K1LX81U000124@192.168.0.25>;tag=1227260345\n" +
                "To: <sip:192.168.0.250:10004>\n" +
                "Call-ID: 475611001\n" +
                "CSeq: 51 MESSAGE\n" +
                "Content-Type: Application/MANSCDP+xml\n" +
                "Max-Forwards: 70\n" +
                "User-Agent: STAR-NET SIP UAS V1.0\n" +
                "Content-Length:        88\n" +
                "\n" +
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
    }

    public static String post(String url, Map<String, String> paramsMap) throws IOException {

        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = null;
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);

            if (paramsMap != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> param : paramsMap.entrySet()) {
                    NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
                    paramList.add(pair);
                }
                method.setEntity(new UrlEncodedFormEntity(paramList,"UTF-8"));
            }
            response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
                System.out.println(responseText);
            }
        } catch (Exception ex) {

            throw ex;
        }
        return null;

    }
}
