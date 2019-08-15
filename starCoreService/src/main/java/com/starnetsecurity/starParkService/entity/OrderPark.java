package com.starnetsecurity.starParkService.entity;

import com.starnetsecurity.starParkService.entityEnum.OrderParkEnum;
import com.starnetsecurity.starParkService.entityEnum.OutCheckEnum;
import com.starnetsecurity.starParkService.entityEnum.PayTypeEnum;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2017-07-13.
 */
@Entity
@Table(name = "order_park", schema = "vamsserver", catalog = "")
public class OrderPark {

    private String id;
    private String carNo;
    private String parkId;
    private Timestamp inTime;
    private Timestamp outTime;
    private OrderParkEnum status;
    private BigDecimal fee;
    private String transactionId;
    private String remark;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String openid;
    private Long version;
    private Timestamp payTime;
    private String carType;
    private String inOrOut;
    private PayTypeEnum payType;
    private OutCheckEnum outCheck;
    private String inImage;
    private String outImage;
    private Timestamp realOutTime;

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
    @Column(name = "in_time")
    public Timestamp getInTime() {
        return inTime;
    }

    public void setInTime(Timestamp inTime) {
        this.inTime = inTime;
    }

    @Basic
    @Column(name = "out_time")
    public Timestamp getOutTime() {
        return outTime;
    }

    public void setOutTime(Timestamp outTime) {
        this.outTime = outTime;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    public OrderParkEnum getStatus() {
        return status;
    }

    public void setStatus(OrderParkEnum status) {
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
    @Column(name = "car_type")
    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    @Basic
    @Column(name = "in_or_out")
    public String getInOrOut() {
        return inOrOut;
    }

    public void setInOrOut(String inOrOut) {
        this.inOrOut = inOrOut;
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
    @Enumerated(EnumType.STRING)
    @Column(name = "out_check")
    public OutCheckEnum getOutCheck() {
        return outCheck;
    }

    public void setOutCheck(OutCheckEnum outCheck) {
        this.outCheck = outCheck;
    }

    @Basic
    @Column(name = "in_image")
    public String getInImage() {
        return inImage;
    }

    public void setInImage(String inImage) {
        this.inImage = inImage;
    }

    @Basic
    @Column(name = "out_image")
    public String getOutImage() {
        return outImage;
    }

    public void setOutImage(String outImage) {
        this.outImage = outImage;
    }

    @Basic
    @Column(name = "real_out_time")
    public Timestamp getRealOutTime() {
        return realOutTime;
    }

    public void setRealOutTime(Timestamp realOutTime) {
        this.realOutTime = realOutTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderPark orderPark = (OrderPark) o;

        if (id != null ? !id.equals(orderPark.id) : orderPark.id != null) return false;
        if (carNo != null ? !carNo.equals(orderPark.carNo) : orderPark.carNo != null) return false;
        if (parkId != null ? !parkId.equals(orderPark.parkId) : orderPark.parkId != null) return false;
        if (inTime != null ? !inTime.equals(orderPark.inTime) : orderPark.inTime != null) return false;
        if (outTime != null ? !outTime.equals(orderPark.outTime) : orderPark.outTime != null) return false;
        if (status != null ? !status.equals(orderPark.status) : orderPark.status != null) return false;
        if (fee != null ? !fee.equals(orderPark.fee) : orderPark.fee != null) return false;
        if (transactionId != null ? !transactionId.equals(orderPark.transactionId) : orderPark.transactionId != null)
            return false;
        if (remark != null ? !remark.equals(orderPark.remark) : orderPark.remark != null) return false;
        if (createTime != null ? !createTime.equals(orderPark.createTime) : orderPark.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(orderPark.updateTime) : orderPark.updateTime != null) return false;
        if (openid != null ? !openid.equals(orderPark.openid) : orderPark.openid != null) return false;
        if (version != null ? !version.equals(orderPark.version) : orderPark.version != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (carNo != null ? carNo.hashCode() : 0);
        result = 31 * result + (parkId != null ? parkId.hashCode() : 0);
        result = 31 * result + (inTime != null ? inTime.hashCode() : 0);
        result = 31 * result + (outTime != null ? outTime.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (fee != null ? fee.hashCode() : 0);
        result = 31 * result + (transactionId != null ? transactionId.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (openid != null ? openid.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }
}
