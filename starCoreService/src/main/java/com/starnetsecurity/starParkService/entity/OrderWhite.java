package com.starnetsecurity.starParkService.entity;

import com.starnetsecurity.starParkService.entityEnum.OrderWhiteEnum;
import com.starnetsecurity.starParkService.entityEnum.PayTypeEnum;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2017-08-23.
 */
@Entity
@Table(name = "order_white", schema = "vamsserver", catalog = "")
public class OrderWhite {
    private String id;
    private String carNo;
    private String parkId;
    private OrderWhiteEnum status;
    private BigDecimal fee;
    private String transactionId;
    private String remark;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String openid;
    private Long version;
    private Timestamp payTime;
    private PayTypeEnum payType;
    private String memberId;
    private String userId;
    private String wxConfigId;

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
    @Column(name = "car_no")
    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    @Basic
    @Column(name = "park_id")
    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    public OrderWhiteEnum getStatus() {
        return status;
    }

    public void setStatus(OrderWhiteEnum status) {
        this.status = status;
    }

    @Basic
    @Column(name = "fee")
    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    @Basic
    @Column(name = "transaction_id")
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "update_time")
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Basic
    @Column(name = "openid")
    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    @Version
    @Column(name = "version")
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Basic
    @Column(name = "pay_time")
    public Timestamp getPayTime() {
        return payTime;
    }

    public void setPayTime(Timestamp payTime) {
        this.payTime = payTime;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "pay_type")
    public PayTypeEnum getPayType() {
        return payType;
    }

    public void setPayType(PayTypeEnum payType) {
        this.payType = payType;
    }

    @Basic
    @Column(name = "member_id")
    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    @Basic
    @Column(name = "user_id")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "wx_config_id")
    public String getWxConfigId() {
        return wxConfigId;
    }

    public void setWxConfigId(String wxConfigId) {
        this.wxConfigId = wxConfigId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderWhite that = (OrderWhite) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (carNo != null ? !carNo.equals(that.carNo) : that.carNo != null) return false;
        if (parkId != null ? !parkId.equals(that.parkId) : that.parkId != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (fee != null ? !fee.equals(that.fee) : that.fee != null) return false;
        if (transactionId != null ? !transactionId.equals(that.transactionId) : that.transactionId != null)
            return false;
        if (remark != null ? !remark.equals(that.remark) : that.remark != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;
        if (openid != null ? !openid.equals(that.openid) : that.openid != null) return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;
        if (payTime != null ? !payTime.equals(that.payTime) : that.payTime != null) return false;
        if (payType != null ? !payType.equals(that.payType) : that.payType != null) return false;
        if (memberId != null ? !memberId.equals(that.memberId) : that.memberId != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (wxConfigId != null ? !wxConfigId.equals(that.wxConfigId) : that.wxConfigId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (carNo != null ? carNo.hashCode() : 0);
        result = 31 * result + (parkId != null ? parkId.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (fee != null ? fee.hashCode() : 0);
        result = 31 * result + (transactionId != null ? transactionId.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (openid != null ? openid.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (payTime != null ? payTime.hashCode() : 0);
        result = 31 * result + (payType != null ? payType.hashCode() : 0);
        result = 31 * result + (memberId != null ? memberId.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (wxConfigId != null ? wxConfigId.hashCode() : 0);
        return result;
    }
}
