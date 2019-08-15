package com.starnetsecurity.parkClientServer.entity;

import com.starnetsecurity.parkClientServer.entityEnum.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2017-12-25.
 */
@Entity
@Table(name = "order_transaction", schema = "vams", catalog = "")
public class OrderTransaction {
    private String transactionId;
    private String orderId;
    private payTypeEnum payType;
    private Timestamp payTime;
    private payStatusEnum payStatus;
    private String payTypeName;
    private BigDecimal totalFee;
    private BigDecimal realFee;
    private String discountType;
    private BigDecimal discountFee;
    private Timestamp addTime;
    private Timestamp updateTime;
    private String transactionMark;

    @Id
    @GenericGenerator(name="systemUUID",strategy="uuid")
    @GeneratedValue(generator="systemUUID")
    @Column(name = "transaction_id")
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Basic
    @Column(name = "order_id")
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "pay_type")
    public payTypeEnum getPayType() {
        return payType;
    }

    public void setPayType(payTypeEnum payType) {
        this.payType = payType;
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
    @Column(name = "pay_status")
    public payStatusEnum getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(payStatusEnum payStatus) {
        this.payStatus = payStatus;
    }

    @Basic
    @Column(name = "pay_type_name")
    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }

    @Basic
    @Column(name = "total_fee")
    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    @Basic
    @Column(name = "real_fee")
    public BigDecimal getRealFee() {
        return realFee;
    }

    public void setRealFee(BigDecimal realFee) {
        this.realFee = realFee;
    }

    @Basic
    @Column(name = "discount_type")
    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    @Basic
    @Column(name = "discount_fee")
    public BigDecimal getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(BigDecimal discountFee) {
        this.discountFee = discountFee;
    }

    @Basic
    @Column(name = "add_time")
    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
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
    @Column(name = "transaction_mark")
    public String getTransactionMark() {
        return transactionMark;
    }

    public void setTransactionMark(String transactionMark) {
        this.transactionMark = transactionMark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderTransaction that = (OrderTransaction) o;

        if (transactionId != null ? !transactionId.equals(that.transactionId) : that.transactionId != null)
            return false;
        if (orderId != null ? !orderId.equals(that.orderId) : that.orderId != null) return false;
        if (payType != null ? !payType.equals(that.payType) : that.payType != null) return false;
        if (payTime != null ? !payTime.equals(that.payTime) : that.payTime != null) return false;
        if (payStatus != null ? !payStatus.equals(that.payStatus) : that.payStatus != null) return false;
        if (payTypeName != null ? !payTypeName.equals(that.payTypeName) : that.payTypeName != null) return false;
        if (totalFee != null ? !totalFee.equals(that.totalFee) : that.totalFee != null) return false;
        if (realFee != null ? !realFee.equals(that.realFee) : that.realFee != null) return false;
        if (discountType != null ? !discountType.equals(that.discountType) : that.discountType != null) return false;
        if (discountFee != null ? !discountFee.equals(that.discountFee) : that.discountFee != null) return false;
        if (addTime != null ? !addTime.equals(that.addTime) : that.addTime != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;
        if (transactionMark != null ? !transactionMark.equals(that.transactionMark) : that.transactionMark != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = transactionId != null ? transactionId.hashCode() : 0;
        result = 31 * result + (orderId != null ? orderId.hashCode() : 0);
        result = 31 * result + (payType != null ? payType.hashCode() : 0);
        result = 31 * result + (payTime != null ? payTime.hashCode() : 0);
        result = 31 * result + (payStatus != null ? payStatus.hashCode() : 0);
        result = 31 * result + (payTypeName != null ? payTypeName.hashCode() : 0);
        result = 31 * result + (totalFee != null ? totalFee.hashCode() : 0);
        result = 31 * result + (realFee != null ? realFee.hashCode() : 0);
        result = 31 * result + (discountType != null ? discountType.hashCode() : 0);
        result = 31 * result + (discountFee != null ? discountFee.hashCode() : 0);
        result = 31 * result + (addTime != null ? addTime.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (transactionMark != null ? transactionMark.hashCode() : 0);
        return result;
    }
}
