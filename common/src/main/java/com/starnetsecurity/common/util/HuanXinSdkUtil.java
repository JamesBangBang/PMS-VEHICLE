package com.starnetsecurity.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.axis.encoding.Base64;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by 63267 on 2017/5/22.
 */
public class HuanXinSdkUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(HuanXinSdkUtil.class);

    private final static String userRegistUrl = "https://a1.easemob.com/ORG_NAME/APP_NAME/users";

    private final static String messagesUrl = " https://a1.easemob.com/ORG_NAME/APP_NAME/messages";

    private final static String tokenUrl = "https://a1.easemob.com/ORG_NAME/APP_NAME/token";


    public static boolean registUser(String hxOrgName,String hxAppName,String username,String password,String nickname){
        try {
            Map<String,Object> obj = new HashedMap();
            obj.put("username",username);
            obj.put("password",password);
            obj.put("nickname",nickname);
            // 创建url资源
            URL url = new URL(getUserRegistUrl(hxOrgName,hxAppName));
            // 建立http连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置允许输出
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 设置不用缓存
            conn.setUseCaches(false);
            // 设置传递方式
            conn.setRequestMethod("POST");
            //转换为字节数组
            // 设置文件类型:
            conn.setRequestProperty("Content-Type", "application/json");
            // 开始连接请求
            conn.connect();
            OutputStream out = conn.getOutputStream();
            // 写入请求的字符串
            String jsonStr = JSONObject.toJSONString(obj);
            out.write(jsonStr.getBytes());
            out.flush();
            out.close();
            // 请求返回的状态
            if (conn.getResponseCode() == 200) {
                // 请求返回的数据
                InputStream in = conn.getInputStream();
                String res = null;
                try {
                    byte[] data = new byte[in.available()];
                    in.read(data);
                    // 转成字符串
                    res = new String(data);

                    return true;
                } catch (Exception e1) {
                    LOGGER.error("环信注册用户读取信息异常 用户名：{}",username);
                }
            } else {
                LOGGER.error("环信注册用户失败 用户名：{}，错误码：",username,conn.getResponseCode());
            }
        } catch (Exception e) {
            LOGGER.error("环信注册用户异常 用户名：{}",username);
        }
        return false;
    }

    public static String getUserRegistUrl(String hxOrgName,String hxAppName){
        return userRegistUrl.replaceAll("ORG_NAME", hxOrgName).replaceAll("APP_NAME",hxAppName);
    }

    public static String getMessagesUrl(String hxOrgName,String hxAppName){
        return messagesUrl.replaceAll("ORG_NAME", hxOrgName).replaceAll("APP_NAME",hxAppName);
    }

    public static String getTokenUrl(String hxOrgName,String hxAppName){
        return tokenUrl.replaceAll("ORG_NAME", hxOrgName).replaceAll("APP_NAME",hxAppName);
    }

    public static boolean sendMsg(String hxOrgName,String hxAppName,String accessToken, List<String> users,Map<String,String> msg){
        try {
            Map<String,Object> obj = new HashedMap();
            obj.put("target_type","users");
            obj.put("target",users);
            Map<String,Object> content = new HashedMap();
            content.put("type","txt");
            content.put("msg", Base64.encode(JSON.toJSONBytes(msg)));
            obj.put("msg",content);

            // 创建url资源
            URL url = new URL(getMessagesUrl(hxOrgName,hxAppName));
            // 建立http连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置允许输出
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 设置不用缓存
            conn.setUseCaches(false);
            // 设置传递方式
            conn.setRequestMethod("POST");
            //转换为字节数组
            // 设置文件类型:
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization","Bearer " + accessToken);

            // 开始连接请求
            conn.connect();
            OutputStream out = conn.getOutputStream();
            // 写入请求的字符串
            String jsonStr = JSONObject.toJSONString(obj);

            out.write(jsonStr.getBytes());
            out.flush();
            out.close();
            // 请求返回的状态
            if (conn.getResponseCode() == 200) {
                // 请求返回的数据
                InputStream in = conn.getInputStream();
                String res = null;
                try {
                    byte[] data = new byte[in.available()];
                    in.read(data);
                    // 转成字符串
                    res = new String(data);

                    return true;
                } catch (Exception e) {
                    LOGGER.error("环信群发信息异常，信息{}",msg,e);
                }
            } else {
                LOGGER.error("环信群发信息异常，信息：{}，错误码：{}",msg,conn.getResponseCode());
            }
        } catch (Exception e) {
            LOGGER.error("环信群发信息异常 信息：{}",msg,e);
        }
        return false;
    }

    public static String getAccessToken(String hxOrgName,String hxAppName,String clientId,String clientSecret){
        try {
            Map<String,Object> obj = new HashedMap();
            obj.put("grant_type","client_credentials");
            obj.put("client_id",clientId);
            obj.put("client_secret",clientSecret);
            // 创建url资源
            URL url = new URL(getTokenUrl(hxOrgName,hxAppName));
            // 建立http连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置允许输出
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 设置不用缓存
            conn.setUseCaches(false);
            // 设置传递方式
            conn.setRequestMethod("POST");
            //转换为字节数组
            // 设置文件类型:
            conn.setRequestProperty("Content-Type", "application/json");
            // 开始连接请求
            conn.connect();
            OutputStream out = conn.getOutputStream();
            // 写入请求的字符串
            String jsonStr = JSONObject.toJSONString(obj);
            out.write(jsonStr.getBytes());
            out.flush();
            out.close();
            // 请求返回的状态
            if (conn.getResponseCode() == 200) {
                // 请求返回的数据
                InputStream in = conn.getInputStream();
                String res = null;
                try {
                    byte[] data = new byte[in.available()];
                    in.read(data);
                    // 转成字符串
                    res = new String(data);
                    JSONObject jsonObject = (JSONObject) JSON.parseObject(res);
                    return jsonObject.getString("access_token");
                } catch (Exception e1) {
                    LOGGER.error("环信授权获取token失败",e1);
                }
            } else {
                LOGGER.error("环信授权获取token失败",conn.getResponseCode());
            }
        } catch (Exception e) {
            LOGGER.error("环信授权获取token失败",e);
        }
        return null;
    }
}
