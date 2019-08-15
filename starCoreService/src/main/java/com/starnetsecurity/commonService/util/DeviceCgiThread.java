package com.starnetsecurity.commonService.util;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by Administrator on 2018/4/26.
 */
public class DeviceCgiThread extends Thread {

    public String ip;
    public Integer port;
    public String username;
    public String password;
    public JSONObject params;
    public String method;
    public String cgi;
    public String urlCgi = null;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public JSONObject getParams() {
        return params;
    }

    public void setParams(JSONObject params) {
        this.params = params;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCgi() {
        return cgi;
    }

    public void setCgi(String cgi) {
        this.cgi = cgi;
    }

    public String getUrlCgi() {
        return urlCgi;
    }

    public void setUrlCgi(String urlCgi) {
        this.urlCgi = urlCgi;
    }

    @Override
    public void run() {
        int sendTimes = 3;
        int curTime = 0;
        int result = 0;
        while(curTime <= sendTimes && result != 200){
            if(this.method.equals("get")){
                result = StarnetDeviceUtils.getCode(StarnetDeviceUtils.getCgiUrl(this.ip,this.port,this.cgi,this.urlCgi),params,StarnetDeviceUtils.getAuthorization(username,password));
            }else{
                result = StarnetDeviceUtils.postThread(StarnetDeviceUtils.getCgiUrl(this.ip,this.port,this.cgi,this.urlCgi),params,StarnetDeviceUtils.getAuthorization(username,password));
            }
            curTime ++;
        }
    }

//    public static void main(String[] argus){
//        int i = 0;
//        while(i < 10){
//            int sendTimes = 1;
//            int curTime = 0;
//            int result = 0;
//            while(curTime <= sendTimes && result != 200){
//
//                result = StarnetDeviceUtils.getCode(StarnetDeviceUtils.getCgiUrl("192.168.16.220",80,"carwhitelink.cgi","manualopen"),null,StarnetDeviceUtils.getAuthorization("admin","123456"));
//                System.out.println(result);
//                curTime ++;
//            }
//            try {
//                Thread.sleep(300);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            i++;
//        }
//
//    }
}
