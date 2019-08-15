package com.starnetsecurity.commonService.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by 宏炜 on 2017-08-02.
 */
@Entity
@Table(name = "wx_user", schema = "vamsserver", catalog = "")
public class WxUser {
    private String id;
    private String nickname;
    private String headimgurl;
    private String country;
    private String city;
    private String province;
    private String remark;
    private Integer subscribed;
    private String telephone;
    private String privilege;
    private String openid;
    private String unionId;
    private Integer registerType;
    private String iosOpenid;
    private String androidOpenid;
    private Integer invalid;
    private String wechatConfigId;
    private Integer userType;
    private Integer status;
    private String token;
    private BigDecimal longitude;
    private BigDecimal latitude;

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
    @Column(name = "nickname")
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Basic
    @Column(name = "headimgurl")
    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    @Basic
    @Column(name = "country")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Basic
    @Column(name = "city")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Basic
    @Column(name = "province")
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Basic
    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Basic
    @Column(name = "subscribed")
    public Integer getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(Integer subscribed) {
        this.subscribed = subscribed;
    }

    @Basic
    @Column(name = "telephone")
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Basic
    @Column(name = "privilege")
    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    @Basic
    @Column(name = "openid")
    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    @Basic
    @Column(name = "union_id")
    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    @Basic
    @Column(name = "register_type")
    public Integer getRegisterType() {
        return registerType;
    }

    public void setRegisterType(Integer registerType) {
        this.registerType = registerType;
    }

    @Basic
    @Column(name = "ios_openid")
    public String getIosOpenid() {
        return iosOpenid;
    }

    public void setIosOpenid(String iosOpenid) {
        this.iosOpenid = iosOpenid;
    }

    @Basic
    @Column(name = "android_openid")
    public String getAndroidOpenid() {
        return androidOpenid;
    }

    public void setAndroidOpenid(String androidOpenid) {
        this.androidOpenid = androidOpenid;
    }

    @Basic
    @Column(name = "invalid")
    public Integer getInvalid() {
        return invalid;
    }

    public void setInvalid(Integer invalid) {
        this.invalid = invalid;
    }

    @Basic
    @Column(name = "wechat_config_id")
    public String getWechatConfigId() {
        return wechatConfigId;
    }

    public void setWechatConfigId(String wechatConfigId) {
        this.wechatConfigId = wechatConfigId;
    }

    @Basic
    @Column(name = "user_type")
    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    @Basic
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Basic
    @Column(name = "longitude")
    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    @Basic
    @Column(name = "latitude")
    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WxUser wxUser = (WxUser) o;

        if (id != null ? !id.equals(wxUser.id) : wxUser.id != null) return false;
        if (nickname != null ? !nickname.equals(wxUser.nickname) : wxUser.nickname != null) return false;
        if (headimgurl != null ? !headimgurl.equals(wxUser.headimgurl) : wxUser.headimgurl != null) return false;
        if (country != null ? !country.equals(wxUser.country) : wxUser.country != null) return false;
        if (city != null ? !city.equals(wxUser.city) : wxUser.city != null) return false;
        if (province != null ? !province.equals(wxUser.province) : wxUser.province != null) return false;
        if (remark != null ? !remark.equals(wxUser.remark) : wxUser.remark != null) return false;
        if (subscribed != null ? !subscribed.equals(wxUser.subscribed) : wxUser.subscribed != null) return false;
        if (telephone != null ? !telephone.equals(wxUser.telephone) : wxUser.telephone != null) return false;
        if (privilege != null ? !privilege.equals(wxUser.privilege) : wxUser.privilege != null) return false;
        if (openid != null ? !openid.equals(wxUser.openid) : wxUser.openid != null) return false;
        if (unionId != null ? !unionId.equals(wxUser.unionId) : wxUser.unionId != null) return false;
        if (registerType != null ? !registerType.equals(wxUser.registerType) : wxUser.registerType != null)
            return false;
        if (iosOpenid != null ? !iosOpenid.equals(wxUser.iosOpenid) : wxUser.iosOpenid != null) return false;
        if (androidOpenid != null ? !androidOpenid.equals(wxUser.androidOpenid) : wxUser.androidOpenid != null)
            return false;
        if (invalid != null ? !invalid.equals(wxUser.invalid) : wxUser.invalid != null) return false;
        if (wechatConfigId != null ? !wechatConfigId.equals(wxUser.wechatConfigId) : wxUser.wechatConfigId != null)
            return false;
        if (userType != null ? !userType.equals(wxUser.userType) : wxUser.userType != null) return false;
        if (status != null ? !status.equals(wxUser.status) : wxUser.status != null) return false;
        if (token != null ? !token.equals(wxUser.token) : wxUser.token != null) return false;
        if (longitude != null ? !longitude.equals(wxUser.longitude) : wxUser.longitude != null) return false;
        if (latitude != null ? !latitude.equals(wxUser.latitude) : wxUser.latitude != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
        result = 31 * result + (headimgurl != null ? headimgurl.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (province != null ? province.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        result = 31 * result + (subscribed != null ? subscribed.hashCode() : 0);
        result = 31 * result + (telephone != null ? telephone.hashCode() : 0);
        result = 31 * result + (privilege != null ? privilege.hashCode() : 0);
        result = 31 * result + (openid != null ? openid.hashCode() : 0);
        result = 31 * result + (unionId != null ? unionId.hashCode() : 0);
        result = 31 * result + (registerType != null ? registerType.hashCode() : 0);
        result = 31 * result + (iosOpenid != null ? iosOpenid.hashCode() : 0);
        result = 31 * result + (androidOpenid != null ? androidOpenid.hashCode() : 0);
        result = 31 * result + (invalid != null ? invalid.hashCode() : 0);
        result = 31 * result + (wechatConfigId != null ? wechatConfigId.hashCode() : 0);
        result = 31 * result + (userType != null ? userType.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        return result;
    }
}
