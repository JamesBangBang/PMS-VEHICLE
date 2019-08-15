package com.starnetsecurity.commonService.util;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Created by JAMESBANG on 2018/6/28.
 */
public class DeviceLedFourThread extends Thread{
    public String ip;
    public Integer port;
    public String username;
    public String password;
    public List<JSONObject> paramsList;
    public static final String cgi = "vdctransport.cgi";

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

    public List<JSONObject> getParamsList() {
        return paramsList;
    }

    public void setParamsList(List<JSONObject> paramsList) {
        this.paramsList = paramsList;
    }

    @Override
    public void run() {
        for (JSONObject params : paramsList){
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            StarnetDeviceUtils.postThread(StarnetDeviceUtils.getCgiUrl(this.ip,this.port,this.cgi,null),params,StarnetDeviceUtils.getAuthorization(username,password));
        }
    }
}
