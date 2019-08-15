package com.starnetsecurity.parkClientServer.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2018-01-30.
 */
@Entity
@Table(name = "order_precharge_record", schema = "vams", catalog = "")
public class OrderPrechargeRecord {
    private String orderPrechargeId;
    private String orderPrechargeCarno;
    private String orderPrechargeCarpark;
    private String orderPrechargeCarparkName;
    private Timestamp orderPrechargeTime;
    private String chargeMemberKindId;
    private String chargeMemberKindName;
    private String orderPrechargeOperatorId;
    private String orderPrechargeOperatorName;
    private Double orderPrechargeReceivableAmount;
    private Double orderPrechargeActualAmount;
    private Double orderPrechargeFreeAmount;
    private String freeReason;
    private String memo;
    private Timestamp addTime;

    @Id
    @GenericGenerator(name="systemUUID",strategy="uuid")
    @GeneratedValue(generator="systemUUID")
    @Column(name = "order_precharge_id")
    public String getOrderPrechargeId() {
        return orderPrechargeId;
    }

    public void setOrderPrechargeId(String orderPrechargeId) {
        this.orderPrechargeId = orderPrechargeId;
    }

    @Basic
    @Column(name = "order_precharge_carno")
    public String getOrderPrechargeCarno() {
        return orderPrechargeCarno;
    }

    public void setOrderPrechargeCarno(String orderPrechargeCarno) {
        this.orderPrechargeCarno = orderPrechargeCarno;
    }

    @Basic
    @Column(name = "order_precharge_carpark")
    public String getOrderPrechargeCarpark() {
        return orderPrechargeCarpark;
    }

    public void setOrderPrechargeCarpark(String orderPrechargeCarpark) {
        this.orderPrechargeCarpark = orderPrechargeCarpark;
    }

    @Basic
    @Column(name = "order_precharge_carpark_name")
    public String getOrderPrechargeCarparkName() {
        return orderPrechargeCarparkName;
    }

    public void setOrderPrechargeCarparkName(String orderPrechargeCarparkName) {
        this.orderPrechargeCarparkName = orderPrechargeCarparkName;
    }

    @Basic
    @Column(name = "order_precharge_time")
    public Timestamp getOrderPrechargeTime() {
        return orderPrechargeTime;
    }

    public void setOrderPrechargeTime(Timestamp orderPrechargeTime) {
        this.orderPrechargeTime = orderPrechargeTime;
    }

    @Basic
    @Column(name = "charge_member_kind_id")
    public String getChargeMemberKindId() {
        return chargeMemberKindId;
    }

    public void setChargeMemberKindId(String chargeMemberKindId) {
        this.chargeMemberKindId = chargeMemberKindId;
    }

    @Basic
    @Column(name = "charge_member_kind_name")
    public String getChargeMemberKindName() {
        return chargeMemberKindName;
    }

    public void setChargeMemberKindName(String chargeMemberKindName) {
        this.chargeMemberKindName = chargeMemberKindName;
    }

    @Basic
    @Column(name = "order_precharge_operator_id")
    public String getOrderPrechargeOperatorId() {
        return orderPrechargeOperatorId;
    }

    public void setOrderPrechargeOperatorId(String orderPrechargeOperatorId) {
        this.orderPrechargeOperatorId = orderPrechargeOperatorId;
    }

    @Basic
    @Column(name = "order_precharge_operator_name")
    public String getOrderPrechargeOperatorName() {
        return orderPrechargeOperatorName;
    }

    public void setOrderPrechargeOperatorName(String orderPrechargeOperatorName) {
        this.orderPrechargeOperatorName = orderPrechargeOperatorName;
    }

    @Basic
    @Column(name = "order_precharge_receivable_amount")
    public Double getOrderPrechargeReceivableAmount() {
        return orderPrechargeReceivableAmount;
    }

    public void setOrderPrechargeReceivableAmount(Double orderPrechargeReceivableAmount) {
        this.orderPrechargeReceivableAmount = orderPrechargeReceivableAmount;
    }

    @Basic
    @Column(name = "order_precharge_actual_amount")
    public Double getOrderPrechargeActualAmount() {
        return orderPrechargeActualAmount;
    }

    public void setOrderPrechargeActualAmount(Double orderPrechargeActualAmount) {
        this.orderPrechargeActualAmount = orderPrechargeActualAmount;
    }

    @Basic
    @Column(name = "order_precharge_free_amount")
    public Double getOrderPrechargeFreeAmount() {
        return orderPrechargeFreeAmount;
    }

    public void setOrderPrechargeFreeAmount(Double orderPrechargeFreeAmount) {
        this.orderPrechargeFreeAmount = orderPrechargeFreeAmount;
    }

    @Basic
    @Column(name = "free_reason")
    public String getFreeReason() {
        return freeReason;
    }

    public void setFreeReason(String freeReason) {
        this.freeReason = freeReason;
    }

    @Basic
    @Column(name = "memo")
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Basic
    @Column(name = "add_time")
    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderPrechargeRecord that = (OrderPrechargeRecord) o;

        if (orderPrechargeId != null ? !orderPrechargeId.equals(that.orderPrechargeId) : that.orderPrechargeId != null)
            return false;
        if (orderPrechargeCarno != null ? !orderPrechargeCarno.equals(that.orderPrechargeCarno) : that.orderPrechargeCarno != null)
            return false;
        if (orderPrechargeCarpark != null ? !orderPrechargeCarpark.equals(that.orderPrechargeCarpark) : that.orderPrechargeCarpark != null)
            return false;
        if (orderPrechargeCarparkName != null ? !orderPrechargeCarparkName.equals(that.orderPrechargeCarparkName) : that.orderPrechargeCarparkName != null)
            return false;
        if (orderPrechargeTime != null ? !orderPrechargeTime.equals(that.orderPrechargeTime) : that.orderPrechargeTime != null)
            return false;
        if (chargeMemberKindId != null ? !chargeMemberKindId.equals(that.chargeMemberKindId) : that.chargeMemberKindId != null)
            return false;
        if (chargeMemberKindName != null ? !chargeMemberKindName.equals(that.chargeMemberKindName) : that.chargeMemberKindName != null)
            return false;
        if (orderPrechargeOperatorId != null ? !orderPrechargeOperatorId.equals(that.orderPrechargeOperatorId) : that.orderPrechargeOperatorId != null)
            return false;
        if (orderPrechargeOperatorName != null ? !orderPrechargeOperatorName.equals(that.orderPrechargeOperatorName) : that.orderPrechargeOperatorName != null)
            return false;
        if (orderPrechargeReceivableAmount != null ? !orderPrechargeReceivableAmount.equals(that.orderPrechargeReceivableAmount) : that.orderPrechargeReceivableAmount != null)
            return false;
        if (orderPrechargeActualAmount != null ? !orderPrechargeActualAmount.equals(that.orderPrechargeActualAmount) : that.orderPrechargeActualAmount != null)
            return false;
        if (orderPrechargeFreeAmount != null ? !orderPrechargeFreeAmount.equals(that.orderPrechargeFreeAmount) : that.orderPrechargeFreeAmount != null)
            return false;
        if (freeReason != null ? !freeReason.equals(that.freeReason) : that.freeReason != null) return false;
        if (memo != null ? !memo.equals(that.memo) : that.memo != null) return false;
        if (addTime != null ? !addTime.equals(that.addTime) : that.addTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = orderPrechargeId != null ? orderPrechargeId.hashCode() : 0;
        result = 31 * result + (orderPrechargeCarno != null ? orderPrechargeCarno.hashCode() : 0);
        result = 31 * result + (orderPrechargeCarpark != null ? orderPrechargeCarpark.hashCode() : 0);
        result = 31 * result + (orderPrechargeCarparkName != null ? orderPrechargeCarparkName.hashCode() : 0);
        result = 31 * result + (orderPrechargeTime != null ? orderPrechargeTime.hashCode() : 0);
        result = 31 * result + (chargeMemberKindId != null ? chargeMemberKindId.hashCode() : 0);
        result = 31 * result + (chargeMemberKindName != null ? chargeMemberKindName.hashCode() : 0);
        result = 31 * result + (orderPrechargeOperatorId != null ? orderPrechargeOperatorId.hashCode() : 0);
        result = 31 * result + (orderPrechargeOperatorName != null ? orderPrechargeOperatorName.hashCode() : 0);
        result = 31 * result + (orderPrechargeReceivableAmount != null ? orderPrechargeReceivableAmount.hashCode() : 0);
        result = 31 * result + (orderPrechargeActualAmount != null ? orderPrechargeActualAmount.hashCode() : 0);
        result = 31 * result + (orderPrechargeFreeAmount != null ? orderPrechargeFreeAmount.hashCode() : 0);
        result = 31 * result + (freeReason != null ? freeReason.hashCode() : 0);
        result = 31 * result + (memo != null ? memo.hashCode() : 0);
        result = 31 * result + (addTime != null ? addTime.hashCode() : 0);
        return result;
    }
}
