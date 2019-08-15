package com.starnetsecurity.commonService.util;

import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.util.CommonUtils;
import okhttp3.*;
import okhttp3.RequestBody;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.codec.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by 宏炜 on 2017-12-19.
 */
public class StarnetDeviceUtils implements DeviceManageUtils {

    private static Logger logger = LoggerFactory.getLogger(StarnetDeviceUtils.class);

    public static final String deviceUrl = "http://IP_ADDRESS/cgi-bin/";

    public static String getCgiUrl(String ip,Integer port,String uri,String actionType){
        if(port != null){
            if(!port.equals(80)){
                ip = ip + ":" + port;
            }
        }
        return deviceUrl.replaceAll("IP_ADDRESS",ip) + uri + (StringUtils.isBlank(actionType) ? "" : ("?action=" + actionType));
    }

    public static String getAuthorization(String username,String password){
        try {
            return "BASIC " + Base64.encodeToString((username + ":" + password).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String post(String url, Map<String,Object> paramsMap,String authorization) {

        StringBuffer params = new StringBuffer("");
        if(!CommonUtils.isEmpty(paramsMap)) {
            int i = 0;
            for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
                if (!CommonUtils.isEmpty(entry.getValue())) {
                    if (i == 0) {
                        params.append(entry.getKey()).append("=").append(entry.getValue());
                    } else {
                        params.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                    }
                    i++;
                }
            }
        }

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=gbk");
        RequestBody body = RequestBody.create(mediaType, params.toString());
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Authorization",authorization)
                    .addHeader("Content-Length",body.contentLength() + "")
                    .build();
            Response response = client.newBuilder().connectTimeout(500, TimeUnit.MILLISECONDS)
                    .readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).execute();
            switch (response.code()){
                case 200:
                    String resBody = new String(response.body().bytes(),"GBK");
                    response.body().close();
                    return resBody;
                case 400:
                    logger.error("cig设备协议请求错误400,请求URL:{},错误原因:{}",url,response.body().string());
                    response.body().close();
                    return null;
                case 401:
                    logger.error("cig设备协议请求错误401,请求URL:{}",url);
                    response.body().close();
                    return null;
                case 404:
                    logger.error("cig设备协议请求错误404,请求URL:{}",url);
                    response.body().close();
                    return null;
                case 500:
                    logger.error("cig设备协议请求错误500,请求URL:{}",url);
                    response.body().close();
                    return null;
            }
        } catch (IOException e) {
            logger.error("cig设备协议请求失败,请求URL:{}",url,e);
        }
        return null;
    }

    public static int postThread(String uri, Map<String,Object> paramsMap,String authorization) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            // 发送POST请求必须设置为true
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //设置连接超时时间和读取超时时间
            conn.setConnectTimeout(300);
            conn.setReadTimeout(500);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Authorization", authorization);
            conn.setRequestProperty("Charset", "GBK");

            out = new OutputStreamWriter(conn.getOutputStream(),"GBK");
            // POST的请求参数写在正文中
            StringBuffer params = new StringBuffer("");
            if(!CommonUtils.isEmpty(paramsMap)) {
                int i = 0;
                for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
                    if (!CommonUtils.isEmpty(entry.getValue())) {
                        if (i == 0) {
                            params.append(entry.getKey()).append("=").append(entry.getValue());
                        } else {
                            params.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                        }
                        i++;
                    }
                }
            }
            out.write(params.toString());
            out.flush();
            out.close();
            // 取得输入流，并使用Reader读取
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "GBK"));

        } catch (IOException e) {
            logger.error("cig设备协议请求失败,请求URL:{}",uri,e);
            return -1;
        }
        //关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                logger.error("cig设备协议请求失败,请求URL:{}",uri,ex);
            }
        }
        return 200;
    }

    public static JSONObject get(String url, Map<String,Object> paramsMap,String authorization) {

        StringBuffer params = new StringBuffer("");
        if(!CommonUtils.isEmpty(paramsMap)){
            for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
                if(!CommonUtils.isEmpty(entry.getValue())){
                    params.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
        }


        url += params.toString();
        OkHttpClient client = new OkHttpClient();
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Authorization", authorization)
                    .build();

            Response response = client.newBuilder().connectTimeout(500, TimeUnit.MILLISECONDS)
                    .readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).execute();
            switch (response.code()){
                case 200:
                    String body = new String(response.body().bytes(),"GBK");
                    response.body().close();
                    return parseJson(body);
                case 400:
                    logger.error("cig设备协议请求错误400,请求URL:{},错误原因:{}",url,response.body().string());
                    response.body().close();
                    return null;
                case 401:
                    logger.error("cig设备协议请求错误401,请求URL:{}",url);
                    response.body().close();
                    return null;
                case 404:
                    logger.error("cig设备协议请求错误404,请求URL:{}",url);
                    response.body().close();
                    return null;
            }
        } catch (IOException e) {
            logger.error("cig设备协议请求失败,请求URL:{}",url,e);
        }
        return null;
    }

    public static int getCode(String url, Map<String,Object> paramsMap,String authorization) {

        StringBuffer params = new StringBuffer("");
        if(!CommonUtils.isEmpty(paramsMap)){
            for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
                if(!CommonUtils.isEmpty(entry.getValue())){
                    params.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
        }


        url += params.toString();
        OkHttpClient client = new OkHttpClient();
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Authorization", authorization)
                    .build();

            Response response = client.newBuilder().connectTimeout(300, TimeUnit.MILLISECONDS)
                    .readTimeout(500, TimeUnit.MILLISECONDS).build().newCall(request).execute();
            switch (response.code()){
                case 200:
                    response.body().close();
                    return 200;
                case 400:
                    logger.error("cig设备协议请求错误400,请求URL:{},错误原因:{}",url,response.body().string());
                    response.body().close();
                    return 400;
                case 401:
                    logger.error("cig设备协议请求错误401,请求URL:{}",url);
                    return response.code();
                case 404:
                    logger.error("cig设备协议请求错误404,请求URL:{}",url);
                    response.body().close();
                    return 404;
                case 500:
                    logger.error("cig设备协议请求错误500,请求URL:{}",url);
                    response.body().close();
                    return 500;
                case 405:
                    logger.error("cig设备协议请求错误405,请求URL:{}",url);
                    response.body().close();
                    return 405;
                default:
                    logger.error("cig设备协议请求错误{},请求URL:{}",response.code(),url);
                    int res = response.code();
                    response.body().close();
                    return res;
            }
        } catch (IOException e) {
            logger.error("cig设备协议请求失败,请求URL:{}",url,e);
        }
        return -1;

    }

    public static JSONObject parseJson(String responseBody){
        if(StringUtils.isBlank(responseBody)){
            return null;
        }
        responseBody = responseBody.replaceAll(",","");
        JSONObject res = new JSONObject();
        String[] keyValues = responseBody.split("\r\n");
        for(String keyValue : keyValues){
            String[] tmp = keyValue.split("=");
            String key = tmp[0];
            String value = "";
            if(tmp.length == 2){
                value = tmp[1];
            }
            res.put(key,value);
        }
        return res;
    }

    @Override
    public void updateDeviceName(String ip,Integer port,String username,String password,String deviceName) {
        Map<String,Object> params = new HashedMap();
        params.put("deviceName",deviceName);
        params.put("action","set");
        post(getCgiUrl(ip,port,"device.cgi",null),params,getAuthorization(username,password));
    }

    @Override
    public String getDeviceName(String ip,Integer port, String username, String password) {
        JSONObject res = get(getCgiUrl(ip,port,"device.cgi","get"),null,getAuthorization(username,password));
        return res.getString("deviceName");
    }

    @Override
    public JSONObject getDeviceInfo(String ip,Integer port, String username, String password) {
        return get(getCgiUrl(ip,port,"system.cgi","get"),null,getAuthorization(username,password));
    }

    @Override
    public void updateDeviceTimeSet(String ip,Integer port, String username, String password, JSONObject params) {
        params.put("type",1);
        params.put("timezoneID",20);
        params.put("ntp.ntpServerLoc1","210.72.145.44");
        params.put("ntp.ntpCheckInterval",4);
        params.put("action","set");
        post(getCgiUrl(ip,port,"time.cgi",null),params,getAuthorization(username,password));
    }

    @Override
    public JSONObject getDeviceTimeSet(String ip,Integer port, String username, String password) {
        return get(getCgiUrl(ip,port,"time.cgi","get"),null,getAuthorization(username,password));
    }

    @Override
    public List getDeviceLog(String ip,Integer port, String username, String password, JSONObject params) {
        params.put("action","get");
        //String res = post(getCgiUrl(ip,port,"log.cgi",null),params,getAuthorization(username,password));
        return null;
    }

    @Override
    public void updateSceneInfo(String ip,Integer port, String username, String password, JSONObject params) {
        params.put("action","set");
        post(getCgiUrl(ip,port,"vdcscene.cgi",null),params,getAuthorization(username,password));
    }

    @Override
    public JSONObject getSceneInfo(String ip,Integer port, String username, String password) {
        return get(getCgiUrl(ip,port,"vdcscene.cgi","get"),null,getAuthorization(username,password));
    }

    @Override
    public void updateRecoSceneInfo(String ip,Integer port, String username, String password, JSONObject params) {
        params.put("action","set");
        post(getCgiUrl(ip,port,"vdcalg.cgi",null),params,getAuthorization(username,password));
    }

    @Override
    public JSONObject getRecoSceneInfo(String ip,Integer port, String username, String password) {
        return get(getCgiUrl(ip,port,"vdcalg.cgi","get"),null,getAuthorization(username,password));
    }

    @Override
    public void updateSysInfo(String ip,Integer port, String username, String password, JSONObject params) {
        params.put("action","set");
        post(getCgiUrl(ip,port,"vdcsyscfg.cgi",null),params,getAuthorization(username,password));
    }

    @Override
    public JSONObject getSysInfo(String ip,Integer port, String username, String password) {
        return get(getCgiUrl(ip,port,"vdcsyscfg.cgi","get"),null,getAuthorization(username,password));
    }

    @Override
    public void updateWhiteSet(String ip,Integer port, String username, String password, JSONObject params) {
        params.put("action","set");
        post(getCgiUrl(ip,port,"carwhiteset.cgi",null),params,getAuthorization(username,password));
    }

    @Override
    public JSONObject getWhiteSet(String ip,Integer port, String username, String password) {
        return get(getCgiUrl(ip,port,"carwhiteset.cgi","get"),null,getAuthorization(username,password));
    }

    @Override
    public void addWhiteMember(String ip,Integer port, String username, String password, JSONObject params) {
        params.put("action","add");
        post(getCgiUrl(ip,port,"carwhitedata.cgi",null),params,getAuthorization(username,password));
    }

    @Override
    public void addWhiteMemberList(String ip,Integer port, String username, String password, JSONObject params,int count) {
        count--;
        String url = getCgiUrl(ip,port,"carwhitedata.cgi",null);
        String authorization = getAuthorization(username,password);

        StringBuffer paramsStr = new StringBuffer("action=updateall&count=" + count);
        for(int i = 0;i < count; i++) {
            String T_GroupId = String.valueOf(params.get("T_GroupId" + (i + 1)));
            paramsStr.append("&").append("T_GroupId" + (i + 1)).append("=").append(T_GroupId);
            String T_owner = String.valueOf(params.get("T_owner" + (i + 1)));
            paramsStr.append("&").append("T_owner" + (i + 1)).append("=").append(T_owner);
            String T_plate = String.valueOf(params.get("T_plate" + (i + 1)));
            paramsStr.append("&").append("T_plate" + (i + 1)).append("=").append(T_plate);
            String T_card = String.valueOf(params.get("T_card" + (i + 1)));
            paramsStr.append("&").append("T_card" + (i + 1)).append("=").append(T_card);
            String T_start = String.valueOf(params.get("T_start" + (i + 1)));
            paramsStr.append("&").append("T_start" + (i + 1)).append("=").append(T_start);
            String T_end = String.valueOf(params.get("T_end" + (i + 1)));
            paramsStr.append("&").append("T_end" + (i + 1)).append("=").append(T_end);
        }

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=gbk");
        RequestBody body = RequestBody.create(mediaType, paramsStr.toString());
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Authorization",authorization)
                    .addHeader("Content-Length",body.contentLength() + "")
                    .build();
            Response response = client.newBuilder().connectTimeout(1000, TimeUnit.MILLISECONDS)
                    .readTimeout(10000, TimeUnit.MILLISECONDS).build().newCall(request).execute();
            switch (response.code()){
                case 200:
                    String resBody = new String(response.body().bytes(),"GBK");
                    response.body().close();
                    break;
                case 400:
                    logger.error("cig设备协议请求错误400,请求URL:{},错误原因:{}",url,response.body().string());
                    response.body().close();
                    break;
                case 401:
                    logger.error("cig设备协议请求错误401,请求URL:{}",url);
                    response.body().close();
                    break;
                case 404:
                    logger.error("cig设备协议请求错误404,请求URL:{}",url);
                    response.body().close();
                    break;
                case 500:
                    logger.error("cig设备协议请求错误500,请求URL:{}",url);
                    response.body().close();
                    break;
            }
        } catch (IOException e) {
            logger.error("cig设备协议请求失败,请求URL:{}",url,e);
        }
    }


    @Override
    public void delWhiteMember(String ip,Integer port, String username, String password, JSONObject params) {
        params.put("action","delete");
        post(getCgiUrl(ip,port,"carwhitedata.cgi",null),params,getAuthorization(username,password));
    }

    @Override
    public List getWhiteMemberList(String ip,Integer port, String username, String password) {
        //get(getCgiUrl(ip,port,"carwhitedata.cgi","get"),null,getAuthorization(username,password));
        return null;
    }

    @Override
    public void updateWhiteMember(String ip,Integer port, String username, String password, JSONObject params) {
        params.put("action","update");
        post(getCgiUrl(ip,port,"carwhitedata.cgi",null),params,getAuthorization(username,password));
    }

    @Override
    public void importWhiteMemberList(String ip,Integer port, String username, String password, List<JSONObject> list) {
        JSONObject params = new JSONObject();
        params.put("action","updateall");
        params.put("count",list.size());
        int i = 1;
        for(JSONObject json : list){
            params.put("T_GroupId" + i,json.get("T_GroupId"));
            params.put("T_owner" + i,json.get("T_owner"));
            params.put("T_plate" + i,json.get("T_plate"));
            params.put("T_card" + i,json.get("T_card"));
            params.put("T_start" + i,json.get("T_start"));
            params.put("T_end" + i,json.get("T_end"));
            i++;
        }

        post(getCgiUrl(ip,port,"carwhitedata.cgi",null),params,getAuthorization(username,password));
    }

    @Override
    public JSONObject queryWhiteMember(String ip,Integer port, String username, String password, JSONObject params) {
        return null;
    }

    @Override
    public void addBlackMember(String ip, Integer port, String username, String password, JSONObject params) {
        params.put("action","add");
        post(getCgiUrl(ip,port,"carblackdata.cgi",null),params,getAuthorization(username,password));
    }

    @Override
    public void updateBlackMember(String ip, Integer port, String username, String password, JSONObject params) {
        params.put("action","update");
        post(getCgiUrl(ip,port,"carblackdata.cgi",null),params,getAuthorization(username,password));
    }

    @Override
    public void delBlackMember(String ip, Integer port, String username, String password, JSONObject params) {
        params.put("action","delete");
        post(getCgiUrl(ip,port,"carblackdata.cgi",null),params,getAuthorization(username,password));
    }

    @Override
    public void UpdateGateControlInfo(String ip, Integer port, String username, String password, JSONObject params) {
        logger.info("更新道闸控制:{}",ip);
        params.put("action","set");
        post(getCgiUrl(ip,port,"carwhitelink.cgi",null),params,getAuthorization(username,password));
    }

    @Override
    public void openRoadGate(String ip,Integer port, String username, String password) {
        logger.info("执行抬杠操作:{}",ip);
        DeviceCgiThread deviceCgiThread = new DeviceCgiThread();
        deviceCgiThread.setIp(ip);
        deviceCgiThread.setPort(port);
        deviceCgiThread.setCgi("carwhitelink.cgi");
        deviceCgiThread.setUrlCgi("manualopen");
        deviceCgiThread.setParams(null);
        deviceCgiThread.setUsername(username);
        deviceCgiThread.setPassword(password);
        deviceCgiThread.setMethod("get");
        deviceCgiThread.start();

    }

    @Override
    public void closeRoadGate(String ip,Integer port, String username, String password) {
        DeviceCgiThread deviceCgiThread = new DeviceCgiThread();
        deviceCgiThread.setIp(ip);
        deviceCgiThread.setPort(port);
        deviceCgiThread.setCgi("carwhitelink.cgi");
        deviceCgiThread.setUrlCgi("manualclose");
        deviceCgiThread.setParams(null);
        deviceCgiThread.setUsername(username);
        deviceCgiThread.setPassword(password);
        deviceCgiThread.setMethod("get");
        deviceCgiThread.start();

    }

    @Override
    public void stopRoadGate(String ip,Integer port, String username, String password) {
        DeviceCgiThread deviceCgiThread = new DeviceCgiThread();
        deviceCgiThread.setIp(ip);
        deviceCgiThread.setPort(port);
        deviceCgiThread.setCgi("carwhitelink.cgi");
        deviceCgiThread.setUrlCgi("manualstop");
        deviceCgiThread.setParams(null);
        deviceCgiThread.setUsername(username);
        deviceCgiThread.setPassword(password);
        deviceCgiThread.setMethod("get");
        deviceCgiThread.start();

    }

    @Override
    public void updateBroadcastDisplayInfo(String ip,Integer port, String username, String password, JSONObject params) {
        params.put("action","set");
        postThread(getCgiUrl(ip,port,"vdcdisplay.cgi",null),params,getAuthorization(username,password));
    }

    @Override
    public JSONObject getBroadcastDisplayInfo(String ip,Integer port, String username, String password) {
        return get(getCgiUrl(ip,port,"vdcdisplay.cgi","get"),null,getAuthorization(username,password));
    }

    @Override
    public void screenOutput(String ip,Integer port, String username, String password, JSONObject params,List<JSONObject> resList) {
        params.put("action","set");
        resList.add(params);
//        DeviceCgiThread deviceCgiThread = new DeviceCgiThread();
//        deviceCgiThread.setIp(ip);
//        deviceCgiThread.setPort(port);
//        deviceCgiThread.setCgi("vdcledctrl.cgi");
//        deviceCgiThread.setUrlCgi(null);
//        deviceCgiThread.setParams(params);
//        deviceCgiThread.setUsername(username);
//        deviceCgiThread.setPassword(password);
//        deviceCgiThread.setMethod("post");
//        deviceCgiThread.start();

    }

    @Override
    public void updateAutoErrorRecoInfo(String ip,Integer port, String username, String password, List<JSONObject> list,int enabledIntellMatch) {
        JSONObject params = new JSONObject();
        params.put("action","set");
        if(enabledIntellMatch > 0){
            params.put("enabledIntellMatch",1);
        }else{
            params.put("enabledIntellMatch",0);
        }

        params.put("count",list.size());
        int i = 1;
        for(JSONObject json : list){
            params.put("src" + i,json.get("src"));
            params.put("dst" + i,json.get("dst"));
            i++;
        }

        post(getCgiUrl(ip,port,"carwhiteset.cgi",null),params,getAuthorization(username,password));
    }

    @Override
    public JSONObject getAutoErrorRecoInfo(String ip,Integer port, String username, String password) {
        return null;
    }

    @Override
    public void audioOutput(String ip,Integer port, String username, String password, JSONObject params) {
        params.put("action","set");
        post(getCgiUrl(ip,port,"vdcttsctrl.cgi",null),params,getAuthorization(username,password));
    }

    @Override
    public void showScreenAudioTest(String ip,Integer port, String username, String password, JSONObject params) {
        get(getCgiUrl(ip,port,"ledsoundtest.cgi",null),null,getAuthorization(username,password));
    }

    @Override
    public void showAudioBroadcastTest(String ip,Integer port, String username, String password, JSONObject params) {
        get(getCgiUrl(ip,port,"ttssoundtest.cgi",null),null,getAuthorization(username,password));
    }

    @Override
    public void updateVideoSetInfo(String ip,Integer port, String username, String password, JSONObject params) {
        params.put("action","updateAll");
        post(getCgiUrl(ip,port,"channels.cgi",null),params,getAuthorization(username,password));
    }

    @Override
    public JSONObject getVideoSetInfo(String ip,Integer port, String username, String password) {
        return null;
    }

    @Override
    public void updateChannelNameAndTime(String ip,Integer port, String username, String password, JSONObject params) {
        params.put("action","set");
        post(getCgiUrl(ip,port,"osd.cgi",null),params,getAuthorization(username,password));
    }

    @Override
    public void updateOsdoverlay(String ip,Integer port, String username, String password, JSONObject params) {
        params.put("action","set");
        post(getCgiUrl(ip,port,"osdoverlay.cgi",null),params,getAuthorization(username,password));
    }

    @Override
    public JSONObject getChannelNameAndTime(String ip,Integer port, String username, String password) {
        return get(getCgiUrl(ip,port,"osd.cgi","get"),null,getAuthorization(username,password));
    }

    @Override
    public JSONObject getOsdoverlay(String ip,Integer port, String username, String password) {
        return get(getCgiUrl(ip,port,"osdoverlay.cgi","get"),null,getAuthorization(username,password));
    }

    @Override
    public JSONObject getDeviceNetParams(String ip,Integer port, String username, String password) {
        return get(getCgiUrl(ip,port,"basicNetwork.cgi","get"),null,getAuthorization(username,password));
    }

    @Override
    public void setDeviceNetParams(String ip,Integer port, String username, String password, JSONObject params) {
        params.put("action","set");
        post(getCgiUrl(ip,port,"basicNetwork.cgi",null),params,getAuthorization(username,password));
    }

    @Override
    public JSONObject getDevicePortInfo(String ip,Integer port, String username, String password) {
        return get(getCgiUrl(ip,port,"port.cgi","get"),null,getAuthorization(username,password));
    }

    @Override
    public void setDevicePortInfo(String ip,Integer port, String username, String password, JSONObject params) {
        params.put("action","set");
        post(getCgiUrl(ip,port,"port.cgi",null),params,getAuthorization(username,password));
    }

    @Override
    public JSONObject getIPCParams(String ip,Integer port, String username, String password) {
        JSONObject res = get(getCgiUrl(ip,port,"image.cgi","getCameraSetting"),null,getAuthorization(username,password));
        Set<String> keys = res.keySet();
        for(String key : keys){
            res.put(key,res.getString(key).split("\\[")[0]);
        }
        return res;
    }

    @Override
    public void setIPCParams(String ip,Integer port, String username, String password) {

    }

    @Override
    public JSONObject getLightInfo(String ip,Integer port, String username, String password) {
        return get(getCgiUrl(ip,port,"vdclightctrl.cgi","get"),null,getAuthorization(username,password));
    }

    @Override
    public void setLightInfo(String ip,Integer port, String username, String password,JSONObject params) {
        params.put("action","set");
        post(getCgiUrl(ip,port,"vdclightctrl.cgi",null),params,getAuthorization(username,password));
    }

    @Override
    public void reboot(String ip, Integer port, String username, String password) {
        get(getCgiUrl(ip,port,"reboot.cgi",null),null,getAuthorization(username,password));
    }

    @Override
    public void formatSD(String ip, Integer port, String username, String password) {
        get(getCgiUrl(ip,port,"sdcard.cgi","format") + "&recordtotal=8",null,getAuthorization(username,password));
    }

    @Override
    public JSONObject getCarwhitelinkInfo(String ip, Integer port, String username, String password) {
        return get(getCgiUrl(ip,port,"carwhitelink.cgi","get"),null,getAuthorization(username,password));
    }

    @Override
    public void updateCarwhitelinkInfo(String ip, Integer port, String username, String password, JSONObject params) {
        params.put("action","set");
        post(getCgiUrl(ip,port,"carwhitelink.cgi",null),params,getAuthorization(username,password));
    }

    @Override
    public void updateDeviceSystem(String ip, Integer port, String username, String password, MultipartFile file) {
        String authorization = getAuthorization(username,password);
        String url = deviceUrl.replaceAll("IP_ADDRESS",ip) + "upgradeFirmware.cgi";

        OkHttpClient client = new OkHttpClient();
        try {

            RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file.getBytes());

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addPart(Headers.of(
                            "Content-Disposition",
                            "form-data; name=\"upgradefile\"; filename=\"" + file.getName() + "\"")
                            , fileBody)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("Authorization", authorization)
                    .build();

            Response response = client.newBuilder().connectTimeout(300, TimeUnit.MILLISECONDS)
                    .readTimeout(30000, TimeUnit.MILLISECONDS).build().newCall(request).execute();
            switch (response.code()){
                case 200:
                    String body = new String(response.body().bytes(),"GBK");
                case 400:
                    logger.error("cig设备协议请求错误400,请求URL:{},错误原因:{}",url,response.body().string());
                case 401:
                    logger.error("cig设备协议请求错误401,请求URL:{}",url);

                case 404:
                    logger.error("cig设备协议请求错误404,请求URL:{}",url);
            }
        } catch (IOException e) {
            logger.error("cig设备协议请求失败,请求URL:{}",url,e);
        }
    }

    @Override
    public void setDeviceVoice(String ip, Integer port, String username, String password, Integer voice) {

    }

    @Override
    public void updateSetIntellMatch(String ip, Integer port, String username, String password, JSONObject params) {
        params.put("action","setIntellMatch");
        post(getCgiUrl(ip,port,"carwhiteset.cgi",null),params,getAuthorization(username,password));
    }

    @Override
    public void setFourEightFiveInfo(String ip, Integer port, String username, String password, JSONObject params) {
        params.put("action","set");
        post(getCgiUrl(ip,port,"vdcuart.cgi",null),params,getAuthorization(username,password));
    }


    public static void main(String[] argus) throws UnsupportedEncodingException {

        StarnetDeviceUtils starnetDeviceUtils = new StarnetDeviceUtils();
        JSONObject whiteParamsList = new JSONObject();
        whiteParamsList.put("T_GroupId1" ,0);
        whiteParamsList.put("T_owner1" , "陈慕林2");
        whiteParamsList.put("T_plate1","闽AB619F");
        whiteParamsList.put("T_card1","13600800583");
        whiteParamsList.put("T_start1","2018-04-03 00:00:00");
        whiteParamsList.put("T_end1","2018-05-03 00:00:00");

        whiteParamsList.put("T_GroupId2" ,0);
        whiteParamsList.put("T_owner2" , "三毛2");
        whiteParamsList.put("T_plate2","闽A10003");
        whiteParamsList.put("T_card2","18655984516");
        whiteParamsList.put("T_start2","2018-04-02 00:00:00");
        whiteParamsList.put("T_end2","2018-05-02 00:00:00");

        whiteParamsList.put("count",2);
        starnetDeviceUtils.addWhiteMemberList("192.168.16.220",80,"admin","123456",whiteParamsList,2);
    }
}
