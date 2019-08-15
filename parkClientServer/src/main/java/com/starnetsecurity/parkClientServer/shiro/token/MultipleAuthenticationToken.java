package com.starnetsecurity.parkClientServer.shiro.token;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * Created by 宏炜 on 2016-12-15.
 */
public class MultipleAuthenticationToken extends UsernamePasswordToken {

    private String tokenStr;
    private String dogSn;
    private int loginType;

    public MultipleAuthenticationToken(){
        this.setRememberMe(false);
    }

    public MultipleAuthenticationToken(String username, char[] password){
        this(username, (char[])password, false, (String)null, null, null);
    }

    public MultipleAuthenticationToken(String username, String password) {
        this(username, (char[])(password != null?password.toCharArray():null), false, (String)null, null, null);
    }

    public MultipleAuthenticationToken(String username, char[] password, String host) {
        this(username, password, false, host, null, null);
    }

    public MultipleAuthenticationToken(String username, String password, String host) {
        this(username, password != null?password.toCharArray():null, false, host, null,null);
    }

    public MultipleAuthenticationToken(String username, char[] password, boolean rememberMe) {
        this(username, (char[])password, rememberMe, (String)null, null, null);
    }

    public MultipleAuthenticationToken(String username, String password, boolean rememberMe) {
        this(username, (char[])(password != null?password.toCharArray():null), rememberMe, (String)null , null, null);
    }

    public MultipleAuthenticationToken(String username, String password, boolean rememberMe, String host,String tokenStr) {
        this(username, (char[])(password != null?password.toCharArray():null), rememberMe, host ,tokenStr,null);
    }

    public MultipleAuthenticationToken(String username, String password, boolean rememberMe, String host,String tokenStr,String dogSn) {
        this(username, (char[])(password != null?password.toCharArray():null), rememberMe, host ,tokenStr,dogSn);
    }

    public MultipleAuthenticationToken(String username, char[] password, boolean rememberMe, String host,String tokenStr,String dogSn) {
        this.setRememberMe(false);
        this.setUsername(username);
        this.setPassword(password);
        this.setRememberMe(rememberMe);
        this.setHost(host);
        this.tokenStr = tokenStr;
        this.dogSn = dogSn;
    }

    public MultipleAuthenticationToken(String username, String password, boolean rememberMe, String host) {
        this(username, password != null?password.toCharArray():null, rememberMe, host, null, null);
    }

    public String getTokenStr() {
        return tokenStr;
    }

    public void setTokenStr(String tokenStr) {
        this.tokenStr = tokenStr;
    }

    public String getDogSn() {
        return dogSn;
    }

    public void setDogSn(String dogSn) {
        this.dogSn = dogSn;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }
}
