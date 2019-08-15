package com.starnetsecurity.starParkService.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2017-10-12.
 */
@Entity
@Table(name = "charge_receive_record", schema = "vamsserver", catalog = "")
@IdClass(ChargeReceiveRecordPK.class)
public class ChargeReceiveRecord {
    private String chargeReceiveId;
    private String chargeReceiveCarNo;
    private String chargeReceiveType;
    private String chargeReceiveSubType;
    private String chargeReceiveCarpark;
    private String chargeReceivePost;
    private Double chargeReceiveAmount;
    private String chargeReceivePostName;
    private String chargeReceiveOperatorId;
    private Timestamp chargeReceiveTime;
    private String chargeReceiveOperation;
    private String operationSource;
    private String chargeReleaseId;
    private String memo;
    private String isCenterUsed;
    private Timestamp addTime;
    private BigDecimal chargeDiscountAmount;
    private String chargeDiscountInfo;
    private Integer useMark;

    @Id
    @Column(name = "charge_receive_id")
    public String getChargeReceiveId() {
        return chargeReceiveId;
    }

    public void setChargeReceiveId(String chargeReceiveId) {
        this.chargeReceiveId = chargeReceiveId;
    }

    @Basic
    @Column(name = "charge_receive_car_no")
    public String getChargeReceiveCarNo() {
        return chargeReceiveCarNo;
    }

    public void setChargeReceiveCarNo(String chargeReceiveCarNo) {
        this.chargeReceiveCarNo = chargeReceiveCarNo;
    }

    @Basic
    @Column(name = "charge_receive_type")
    public String getChargeReceiveType() {
        return chargeReceiveType;
    }

    public void setChargeReceiveType(String chargeReceiveType) {
        this.chargeReceiveType = chargeReceiveType;
    }

    @Basic
    @Column(name = "charge_receive_sub_type")
    public String getChargeReceiveSubType() {
        return chargeReceiveSubType;
    }

    public void setChargeReceiveSubType(String chargeReceiveSubType) {
        this.chargeReceiveSubType = chargeReceiveSubType;
    }

    @Basic
    @Column(name = "charge_receive_carpark")
    public String getChargeReceiveCarpark() {
        return chargeReceiveCarpark;
    }

    public void setChargeReceiveCarpark(String chargeReceiveCarpark) {
        this.chargeReceiveCarpark = chargeReceiveCarpark;
    }

    @Basic
    @Column(name = "charge_receive_post")
    public String getChargeReceivePost() {
        return chargeReceivePost;
    }

    public void setChargeReceivePost(String chargeReceivePost) {
        this.chargeReceivePost = chargeReceivePost;
    }

    @Basic
    @Column(name = "charge_receive_amount")
    public Double getChargeReceiveAmount() {
        return chargeReceiveAmount;
    }

    public void setChargeReceiveAmount(Double chargeReceiveAmount) {
        this.chargeReceiveAmount = chargeReceiveAmount;
    }

    @Basic
    @Column(name = "charge_receive_post_name")
    public String getChargeReceivePostName() {
        return chargeReceivePostName;
    }

    public void setChargeReceivePostName(String chargeReceivePostName) {
        this.chargeReceivePostName = chargeReceivePostName;
    }

    @Basic
    @Column(name = "charge_receive_operator_id")
    public String getChargeReceiveOperatorId() {
        return chargeReceiveOperatorId;
    }

    public void setChargeReceiveOperatorId(String chargeReceiveOperatorId) {
        this.chargeReceiveOperatorId = chargeReceiveOperatorId;
    }

    @Id
    @Column(name = "charge_receive_time")
    public Timestamp getChargeReceiveTime() {
        return chargeReceiveTime;
    }

    public void setChargeReceiveTime(Timestamp chargeReceiveTime) {
        this.chargeReceiveTime = chargeReceiveTime;
    }

    @Basic
    @Column(name = "charge_receive_operation")
    public String getChargeReceiveOperation() {
        return chargeReceiveOperation;
    }

    public void setChargeReceiveOperation(String chargeReceiveOperation) {
        this.chargeReceiveOperation = chargeReceiveOperation;
    }

    @Basic
    @Column(name = "operation_source")
    public String getOperationSource() {
        return operationSource;
    }

    public void setOperationSource(String operationSource) {
        this.operationSource = operationSource;
    }

    @Basic
    @Column(name = "charge_release_id")
    public String getChargeReleaseId() {
        return chargeReleaseId;
    }

    public void setChargeReleaseId(String chargeReleaseId) {
        this.chargeReleaseId = chargeReleaseId;
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
    @Column(name = "is_center_used")
    public String getIsCenterUsed() {
        return isCenterUsed;
    }

    public void setIsCenterUsed(String isCenterUsed) {
        this.isCenterUsed = isCenterUsed;
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
    @Column(name = "charge_discount_amount")
    public BigDecimal getChargeDiscountAmount() {
        return chargeDiscountAmount;
    }

    public void setChargeDiscountAmount(BigDecimal chargeDiscountAmount) {
        this.chargeDiscountAmount = chargeDiscountAmount;
    }

    @Basic
    @Column(name = "charge_discount_info")
    public String getChargeDiscountInfo() {
        return chargeDiscountInfo;
    }

    public void setChargeDiscountInfo(String chargeDiscountInfo) {
        this.chargeDiscountInfo = chargeDiscountInfo;
    }

    @Basic
    @Column(name = "use_mark")
    public Integer getUseMark() {
        return useMark;
    }

    public void setUseMark(Integer useMark) {
        this.useMark = useMark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChargeReceiveRecord that = (ChargeReceiveRecord) o;

        if (chargeReceiveId != null ? !chargeReceiveId.equals(that.chargeReceiveId) : that.chargeReceiveId != null)
            return false;
        if (chargeReceiveCarNo != null ? !chargeReceiveCarNo.equals(that.chargeReceiveCarNo) : that.chargeReceiveCarNo != null)
            return false;
        if (chargeReceiveType != null ? !chargeReceiveType.equals(that.chargeReceiveType) : that.chargeReceiveType != null)
            return false;
        if (chargeReceiveSubType != null ? !chargeReceiveSubType.equals(that.chargeReceiveSubType) : that.chargeReceiveSubType != null)
            return false;
        if (chargeReceiveCarpark != null ? !chargeReceiveCarpark.equals(that.chargeReceiveCarpark) : that.chargeReceiveCarpark != null)
            return false;
        if (chargeReceivePost != null ? !chargeReceivePost.equals(that.chargeReceivePost) : that.chargeReceivePost != null)
            return false;
        if (chargeReceiveAmount != null ? !chargeReceiveAmount.equals(that.chargeReceiveAmount) : that.chargeReceiveAmount != null)
            return false;
        if (chargeReceivePostName != null ? !chargeReceivePostName.equals(that.chargeReceivePostName) : that.chargeReceivePostName != null)
            return false;
        if (chargeReceiveOperatorId != null ? !chargeReceiveOperatorId.equals(that.chargeReceiveOperatorId) : that.chargeReceiveOperatorId != null)
            return false;
        if (chargeReceiveTime != null ? !chargeReceiveTime.equals(that.chargeReceiveTime) : that.chargeReceiveTime != null)
            return false;
        if (chargeReceiveOperation != null ? !chargeReceiveOperation.equals(that.chargeReceiveOperation) : that.chargeReceiveOperation != null)
            return false;
        if (operationSource != null ? !operationSource.equals(that.operationSource) : that.operationSource != null)
            return false;
        if (chargeReleaseId != null ? !chargeReleaseId.equals(that.chargeReleaseId) : that.chargeReleaseId != null)
            return false;
        if (memo != null ? !memo.equals(that.memo) : that.memo != null) return false;
        if (isCenterUsed != null ? !isCenterUsed.equals(that.isCenterUsed) : that.isCenterUsed != null) return false;
        if (addTime != null ? !addTime.equals(that.addTime) : that.addTime != null) return false;
        if (chargeDiscountAmount != null ? !chargeDiscountAmount.equals(that.chargeDiscountAmount) : that.chargeDiscountAmount != null)
            return false;
        if (chargeDiscountInfo != null ? !chargeDiscountInfo.equals(that.chargeDiscountInfo) : that.chargeDiscountInfo != null)
            return false;
        if (useMark != null ? !useMark.equals(that.useMark) : that.useMark != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = chargeReceiveId != null ? chargeReceiveId.hashCode() : 0;
        result = 31 * result + (chargeReceiveCarNo != null ? chargeReceiveCarNo.hashCode() : 0);
        result = 31 * result + (chargeReceiveType != null ? chargeReceiveType.hashCode() : 0);
        result = 31 * result + (chargeReceiveSubType != null ? chargeReceiveSubType.hashCode() : 0);
        result = 31 * result + (chargeReceiveCarpark != null ? chargeReceiveCarpark.hashCode() : 0);
        result = 31 * result + (chargeReceivePost != null ? chargeReceivePost.hashCode() : 0);
        result = 31 * result + (chargeReceiveAmount != null ? chargeReceiveAmount.hashCode() : 0);
        result = 31 * result + (chargeReceivePostName != null ? chargeReceivePostName.hashCode() : 0);
        result = 31 * result + (chargeReceiveOperatorId != null ? chargeReceiveOperatorId.hashCode() : 0);
        result = 31 * result + (chargeReceiveTime != null ? chargeReceiveTime.hashCode() : 0);
        result = 31 * result + (chargeReceiveOperation != null ? chargeReceiveOperation.hashCode() : 0);
        result = 31 * result + (operationSource != null ? operationSource.hashCode() : 0);
        result = 31 * result + (chargeReleaseId != null ? chargeReleaseId.hashCode() : 0);
        result = 31 * result + (memo != null ? memo.hashCode() : 0);
        result = 31 * result + (isCenterUsed != null ? isCenterUsed.hashCode() : 0);
        result = 31 * result + (addTime != null ? addTime.hashCode() : 0);
        result = 31 * result + (chargeDiscountAmount != null ? chargeDiscountAmount.hashCode() : 0);
        result = 31 * result + (chargeDiscountInfo != null ? chargeDiscountInfo.hashCode() : 0);
        result = 31 * result + (useMark != null ? useMark.hashCode() : 0);
        return result;
    }
}
