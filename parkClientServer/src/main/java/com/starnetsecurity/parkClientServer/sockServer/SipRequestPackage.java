package com.starnetsecurity.parkClientServer.sockServer;

/**
 * Created by 宏炜 on 2017-12-13.
 */
public class SipRequestPackage {

    private String requestMethod;
    private String host;
    private String sipVersion;
    private String via;
    private String from;
    private String to;
    private String callID;
    private String csEq;
    private String contact;
    private Integer maxForwards;
    private String userAgent;
    private Integer expires;
    private Integer contentLength;
    private String body;
    private String authorization;
    private String contentType;

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Integer getMaxForwards() {
        return maxForwards;
    }

    public void setMaxForwards(Integer maxForwards) {
        this.maxForwards = maxForwards;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Integer getExpires() {
        return expires;
    }

    public void setExpires(Integer expires) {
        this.expires = expires;
    }

    public Integer getContentLength() {
        return contentLength;
    }

    public void setContentLength(Integer contentLength) {
        this.contentLength = contentLength;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void appendHead(String head){

        String[] value = head.split(" ");
        requestMethod = value[0];
        if("P".equals(requestMethod)){
            requestMethod = "MESSAGE";
            host = "UAS";
            sipVersion = "V1.0";
        }else{
            host = value[1];
            sipVersion = value[2];
        }


    }

    public void append(String content){
        String[] value = content.split(": ");
        SIPEnum tag = SIPEnum.valueOf(value[0].replaceAll("-",""));
        switch (tag){
            case Via:
                this.via = value[1];
                break;
            case From:
                this.from = value[1];
                break;
            case To:
                this.to = value[1];
                break;
            case CallID:
                this.callID = value[1];
                break;
            case CSeq:
                this.csEq = value[1];
                break;
            case Contact:
                this.contact = value[1];
                break;
            case MaxForwards:
                this.maxForwards = Integer.parseInt(value[1].trim());
                break;
            case UserAgent:
                this.userAgent = value[1];
                break;
            case Expires:
                this.expires = Integer.parseInt(value[1].trim());
                break;
            case ContentLength:
                this.contentLength = Integer.parseInt(value[1].trim());
                break;
            case Authorization:
                this.authorization = value[1];
                break;
            case ContentType:
                this.contentType = value[1];
                break;

        }
    }

    public void appendBody(String body){
        this.body = body;
    }
}
