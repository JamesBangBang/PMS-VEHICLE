package com.starnetsecurity.payService.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by 宏炜 on 2017-07-14.
 */
@Entity
@Table(name = "wechat_config", schema = "vamsserver", catalog = "")
public class WechatConfig {
    private String id;
    private String appid;
    private String appsecret;
    private String mchId;
    private String mchKey;
    private String appname;

    @Id
    @GenericGenerator(name="systemUUID",strategy="uuid")
    @GeneratedValue(generator="systemUUID")
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "appid")
    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    @Basic
    @Column(name = "appsecret")
    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }

    @Basic
    @Column(name = "mchId")
    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    @Basic
    @Column(name = "mchKey")
    public String getMchKey() {
        return mchKey;
    }

    public void setMchKey(String mchKey) {
        this.mchKey = mchKey;
    }

    @Basic
    @Column(name = "appname")
    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WechatConfig that = (WechatConfig) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (appid != null ? !appid.equals(that.appid) : that.appid != null) return false;
        if (appsecret != null ? !appsecret.equals(that.appsecret) : that.appsecret != null) return false;
        if (mchId != null ? !mchId.equals(that.mchId) : that.mchId != null) return false;
        if (mchKey != null ? !mchKey.equals(that.mchKey) : that.mchKey != null) return false;
        if (appname != null ? !appname.equals(that.appname) : that.appname != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (appid != null ? appid.hashCode() : 0);
        result = 31 * result + (appsecret != null ? appsecret.hashCode() : 0);
        result = 31 * result + (mchId != null ? mchId.hashCode() : 0);
        result = 31 * result + (mchKey != null ? mchKey.hashCode() : 0);
        result = 31 * result + (appname != null ? appname.hashCode() : 0);
        return result;
    }
}
