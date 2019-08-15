package com.starnetsecurity.parkClientServer.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Created by 宏炜 on 2017-10-12.
 */
@Entity
@Table(name = "admin_user", schema = "vams", catalog = "")
public class AdminUser {
    private String id;
    private String userName;
    private String userPwd;
    private String trueName;
    private String email;
    private String telephone;
    private Timestamp createTime;
    private String identifyNo;
    private String resign;
    private String status;
    private String sex;
    private String qq;
    private String salt;
    private String token;
    private String hxUsername;
    private String hxPassword;
    private String sysTag;
    private String wxOpenId;
    private String wxNickname;
    private String wxHeadImageUrl;
    private Timestamp updateTime;

    public AdminUser() {
        this.id = UUID.randomUUID().toString().replaceAll("-","");
    }

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "user_pwd")
    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    @Basic
    @Column(name = "true_name")
    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "identify_no")
    public String getIdentifyNo() {
        return identifyNo;
    }

    public void setIdentifyNo(String identifyNo) {
        this.identifyNo = identifyNo;
    }

    @Basic
    @Column(name = "resign")
    public String getResign() {
        return resign;
    }

    public void setResign(String resign) {
        this.resign = resign;
    }

    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "sex")
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Basic
    @Column(name = "qq")
    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    @Basic
    @Column(name = "salt")
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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
    @Column(name = "sys_tag")
    public String getSysTag() {
        return sysTag;
    }

    public void setSysTag(String sysTag) {
        this.sysTag = sysTag;
    }

    @Basic
    @Column(name = "wx_open_id")
    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    @Basic
    @Column(name = "wx_nickname")
    public String getWxNickname() {
        return wxNickname;
    }

    public void setWxNickname(String wxNickname) {
        this.wxNickname = wxNickname;
    }

    @Basic
    @Column(name = "wx_head_image_url")
    public String getWxHeadImageUrl() {
        return wxHeadImageUrl;
    }

    public void setWxHeadImageUrl(String wxHeadImageUrl) {
        this.wxHeadImageUrl = wxHeadImageUrl;
    }

    @Basic
    @Column(name = "update_time")
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdminUser adminUser = (AdminUser) o;

        if (id != null ? !id.equals(adminUser.id) : adminUser.id != null) return false;
        if (userName != null ? !userName.equals(adminUser.userName) : adminUser.userName != null) return false;
        if (userPwd != null ? !userPwd.equals(adminUser.userPwd) : adminUser.userPwd != null) return false;
        if (trueName != null ? !trueName.equals(adminUser.trueName) : adminUser.trueName != null) return false;
        if (email != null ? !email.equals(adminUser.email) : adminUser.email != null) return false;
        if (telephone != null ? !telephone.equals(adminUser.telephone) : adminUser.telephone != null) return false;
        if (createTime != null ? !createTime.equals(adminUser.createTime) : adminUser.createTime != null) return false;
        if (identifyNo != null ? !identifyNo.equals(adminUser.identifyNo) : adminUser.identifyNo != null) return false;
        if (resign != null ? !resign.equals(adminUser.resign) : adminUser.resign != null) return false;
        if (status != null ? !status.equals(adminUser.status) : adminUser.status != null) return false;
        if (sex != null ? !sex.equals(adminUser.sex) : adminUser.sex != null) return false;
        if (qq != null ? !qq.equals(adminUser.qq) : adminUser.qq != null) return false;
        if (salt != null ? !salt.equals(adminUser.salt) : adminUser.salt != null) return false;
        if (token != null ? !token.equals(adminUser.token) : adminUser.token != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (userPwd != null ? userPwd.hashCode() : 0);
        result = 31 * result + (trueName != null ? trueName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (telephone != null ? telephone.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (identifyNo != null ? identifyNo.hashCode() : 0);
        result = 31 * result + (resign != null ? resign.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (qq != null ? qq.hashCode() : 0);
        result = 31 * result + (salt != null ? salt.hashCode() : 0);
        result = 31 * result + (token != null ? token.hashCode() : 0);
        return result;
    }

    @Basic
    @Column(name = "hx_username")
    public String getHxUsername() {
        return hxUsername;
    }

    public void setHxUsername(String hxUsername) {
        this.hxUsername = hxUsername;
    }

    @Basic
    @Column(name = "hx_password")
    public String getHxPassword() {
        return hxPassword;
    }

    public void setHxPassword(String hxPassword) {
        this.hxPassword = hxPassword;
    }
}
