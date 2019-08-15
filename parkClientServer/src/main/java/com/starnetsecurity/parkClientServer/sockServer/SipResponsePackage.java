package com.starnetsecurity.parkClientServer.sockServer;

import com.starnetsecurity.common.util.CommonUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by 宏炜 on 2017-12-13.
 */
public class SipResponsePackage {

    private String sipVersion;
    private String via;
    private String from;
    private String to;
    private String callID;
    private String csEq;
    private String contentType;
    private String body;
    private String WWWAuthenticate;
    private String userAgent;
    private String code;
    private String result;

    public String getSipVersion() {
        return sipVersion;
    }

    public void setSipVersion(String sipVersion) {
        this.sipVersion = sipVersion;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCallID() {
        return callID;
    }

    public void setCallID(String callID) {
        this.callID = callID;
    }

    public String getCsEq() {
        return csEq;
    }

    public void setCsEq(String csEq) {
        this.csEq = csEq;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getWWWAuthenticate() {
        return WWWAuthenticate;
    }

    public void setWWWAuthenticate(String WWWAuthenticate) {
        this.WWWAuthenticate = WWWAuthenticate;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        String res = this.sipVersion + " " + this.code + " " + this.result + "\r\n" +
                "Via: " + this.via + "\r\n" +
                "From: " + this.from + "\r\n" +
                "To: " + this.to + "\r\n" +
                "Call-ID: " + this.callID + "\r\n" +
                "CSeq: " + this.csEq + "\r\n";

        if(!CommonUtils.isEmpty(this.WWWAuthenticate)){
            res += "WWW-Authenticate: " + this.WWWAuthenticate + "\r\n";
        }
        if(!CommonUtils.isEmpty(this.userAgent)){
            res += "User-Agent: " + this.userAgent + "\r\n";
        }

        if(!CommonUtils.isEmpty(this.contentType)){
            res += "Content-Type: " + this.contentType + "\r\n";
        }
        if(!CommonUtils.isEmpty(this.body)){
            res += "Content-Length: " + this.body.length() + "\r\n";

        }else{
            res += "Content-Length: 0\r\n";
        }
        res += "\r\n";
        if(!StringUtils.isBlank(this.body)){
            res += body;
        }

        return res;
    }
}
