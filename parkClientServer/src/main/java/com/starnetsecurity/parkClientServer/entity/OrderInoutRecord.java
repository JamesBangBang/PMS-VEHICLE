package com.starnetsecurity.parkClientServer.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2017-12-25.
 */
@Entity
@Table(name = "order_inout_record", schema = "vams", catalog = "")
public class OrderInoutRecord {
    private String chargeInfoId;
    private String carNo;
    private String carparkId;
    private String carparkName;
    private String inCarRoadId;
    private String inCarRoadName;
    private String outCarRoadId;
    private String outCarRoadName;
    private Timestamp chargeTime;
    private Timestamp inTime;
    private Timestamp inChargeTime;
    private Timestamp outTime;
    private Integer stayTime;
    private Double chargeReceivableAmount;
    private Double chargeActualAmount;
    private Double chargePreAmount;
    private String inRecordId;
    private String outRecordId;
    private Byte releaseType;
    private String releaseReason;
    private Byte carNoAttribute;
    private String carType;
    private String chargeOperatorId;
    private String chargeOperatorName;
    private String chargePostId;
    private String chargePostName;
    private String remark;
    private Timestamp addTime;
    private Timestamp updateTime;
    private String inPictureName;
    private String outPictureName;

    @Id
    @GenericGenerator(name="systemUUID",strategy="uuid")
    @GeneratedValue(generator="systemUUID")
    @Basic
    @Column(name = "charge_info_id")
    public String getChargeInfoId() {
        return chargeInfoId;
    }

    public void setChargeInfoId(String chargeInfoId) {
        this.chargeInfoId = chargeInfoId;
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
    @Column(name = "carpark_id")
    public String getCarparkId() {
        return carparkId;
    }

    public void setCarparkId(String carparkId) {
        this.carparkId = carparkId;
    }

    @Basic
    @Column(name = "carpark_name")
    public String getCarparkName() {
        return carparkName;
    }

    public void setCarparkName(String carparkName) {
        this.carparkName = carparkName;
    }

    @Basic
    @Column(name = "in_car_road_id")
    public String getInCarRoadId() {
        return inCarRoadId;
    }

    public void setInCarRoadId(String inCarRoadId) {
        this.inCarRoadId = inCarRoadId;
    }

    @Basic
    @Column(name = "in_car_road_name")
    public String getInCarRoadName() {
        return inCarRoadName;
    }

    public void setInCarRoadName(String inCarRoadName) {
        this.inCarRoadName = inCarRoadName;
    }

    @Basic
    @Column(name = "out_car_road_id")
    public String getOutCarRoadId() {
        return outCarRoadId;
    }

    public void setOutCarRoadId(String outCarRoadId) {
        this.outCarRoadId = outCarRoadId;
    }

    @Basic
    @Column(name = "out_car_road_name")
    public String getOutCarRoadName() {
        return outCarRoadName;
    }

    public void setOutCarRoadName(String outCarRoadName) {
        this.outCarRoadName = outCarRoadName;
    }

    @Basic
    @Column(name = "charge_time")
    public Timestamp getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(Timestamp chargeTime) {
        this.chargeTime = chargeTime;
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
    @Column(name = "in_charge_time")
    public Timestamp getInChargeTime() {
        return inChargeTime;
    }

    public void setInChargeTime(Timestamp inChargeTime) {
        this.inChargeTime = inChargeTime;
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
    @Column(name = "stay_time")
    public Integer getStayTime() {
        return stayTime;
    }

    public void setStayTime(Integer stayTime) {
        this.stayTime = stayTime;
    }

    @Basic
    @Column(name = "charge_receivable_amount")
    public Double getChargeReceivableAmount() {
        return chargeReceivableAmount;
    }

    public void setChargeReceivableAmount(Double chargeReceivableAmount) {
        this.chargeReceivableAmount = chargeReceivableAmount;
    }

    @Basic
    @Column(name = "charge_pre_amount")
    public Double getChargePreAmount() {
        return chargePreAmount;
    }

    public void setChargePreAmount(Double chargePreAmount) {
        this.chargePreAmount = chargePreAmount;
    }

    @Basic
    @Column(name = "charge_actual_amount")
    public Double getChargeActualAmount() {
        return chargeActualAmount;
    }

    public void setChargeActualAmount(Double chargeActualAmount) {
        this.chargeActualAmount = chargeActualAmount;
    }
    @Basic
    @Column(name = "in_record_id")
    public String getInRecordId() {
        return inRecordId;
    }

    public void setInRecordId(String inRecordId) {
        this.inRecordId = inRecordId;
    }

    @Basic
    @Column(name = "out_record_id")
    public String getOutRecordId() {
        return outRecordId;
    }

    public void setOutRecordId(String outRecordId) {
        this.outRecordId = outRecordId;
    }

    @Basic
    @Column(name = "release_type")
    public Byte getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(Byte releaseType) {
        this.releaseType = releaseType;
    }

    @Basic
    @Column(name = "release_reason")
    public String getReleaseReason() {
        return releaseReason;
    }

    public void setReleaseReason(String releaseReason) {
        this.releaseReason = releaseReason;
    }

    @Basic
    @Column(name = "car_no_attribute")
    public Byte getCarNoAttribute() {
        return carNoAttribute;
    }

    public void setCarNoAttribute(Byte carNoAttribute) {
        this.carNoAttribute = carNoAttribute;
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
    @Column(name = "charge_operator_id")
    public String getChargeOperatorId() {
        return chargeOperatorId;
    }

    public void setChargeOperatorId(String chargeOperatorId) {
        this.chargeOperatorId = chargeOperatorId;
    }

    @Basic
    @Column(name = "charge_operator_name")
    public String getChargeOperatorName() {
        return chargeOperatorName;
    }

    public void setChargeOperatorName(String chargeOperatorName) {
        this.chargeOperatorName = chargeOperatorName;
    }

    @Basic
    @Column(name = "charge_post_id")
    public String getChargePostId() {
        return chargePostId;
    }

    public void setChargePostId(String chargePostId) {
        this.chargePostId = chargePostId;
    }

    @Basic
    @Column(name = "charge_post_name")
    public String getChargePostName() {
        return chargePostName;
    }

    public void setChargePostName(String chargePostName) {
        this.chargePostName = chargePostName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderInoutRecord that = (OrderInoutRecord) o;

        if (chargeInfoId != null ? !chargeInfoId.equals(that.chargeInfoId) : that.chargeInfoId != null) return false;
        if (carNo != null ? !carNo.equals(that.carNo) : that.carNo != null) return false;
        if (carparkId != null ? !carparkId.equals(that.carparkId) : that.carparkId != null) return false;
        if (carparkName != null ? !carparkName.equals(that.carparkName) : that.carparkName != null) return false;
        if (inCarRoadId != null ? !inCarRoadId.equals(that.inCarRoadId) : that.inCarRoadId != null) return false;
        if (inCarRoadName != null ? !inCarRoadName.equals(that.inCarRoadName) : that.inCarRoadName != null)
            return false;
        if (outCarRoadId != null ? !outCarRoadId.equals(that.outCarRoadId) : that.outCarRoadId != null) return false;
        if (outCarRoadName != null ? !outCarRoadName.equals(that.outCarRoadName) : that.outCarRoadName != null)
            return false;
        if (chargeTime != null ? !chargeTime.equals(that.chargeTime) : that.chargeTime != null) return false;
        if (inTime != null ? !inTime.equals(that.inTime) : that.inTime != null) return false;
        if (inChargeTime != null ? !inChargeTime.equals(that.inChargeTime) : that.inChargeTime != null) return false;
        if (outTime != null ? !outTime.equals(that.outTime) : that.outTime != null) return false;
        if (stayTime != null ? !stayTime.equals(that.stayTime) : that.stayTime != null) return false;
        if (chargeReceivableAmount != null ? !chargeReceivableAmount.equals(that.chargeReceivableAmount) : that.chargeReceivableAmount != null)
            return false;
        if (chargeActualAmount != null ? !chargeActualAmount.equals(that.chargeActualAmount) : that.chargeActualAmount != null)
            return false;
        if (inRecordId != null ? !inRecordId.equals(that.inRecordId) : that.inRecordId != null) return false;
        if (outRecordId != null ? !outRecordId.equals(that.outRecordId) : that.outRecordId != null) return false;
        if (releaseType != null ? !releaseType.equals(that.releaseType) : that.releaseType != null) return false;
        if (releaseReason != null ? !releaseReason.equals(that.releaseReason) : that.releaseReason != null)
            return false;
        if (carNoAttribute != null ? !carNoAttribute.equals(that.carNoAttribute) : that.carNoAttribute != null)
            return false;
        if (carType != null ? !carType.equals(that.carType) : that.carType != null) return false;
        if (chargeOperatorId != null ? !chargeOperatorId.equals(that.chargeOperatorId) : that.chargeOperatorId != null)
            return false;
        if (chargeOperatorName != null ? !chargeOperatorName.equals(that.chargeOperatorName) : that.chargeOperatorName != null)
            return false;
        if (chargePostId != null ? !chargePostId.equals(that.chargePostId) : that.chargePostId != null) return false;
        if (chargePostName != null ? !chargePostName.equals(that.chargePostName) : that.chargePostName != null)
            return false;
        if (remark != null ? !remark.equals(that.remark) : that.remark != null) return false;
        if (addTime != null ? !addTime.equals(that.addTime) : that.addTime != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = chargeInfoId != null ? chargeInfoId.hashCode() : 0;
        result = 31 * result + (carNo != null ? carNo.hashCode() : 0);
        result = 31 * result + (carparkId != null ? carparkId.hashCode() : 0);
        result = 31 * result + (carparkName != null ? carparkName.hashCode() : 0);
        result = 31 * result + (inCarRoadId != null ? inCarRoadId.hashCode() : 0);
        result = 31 * result + (inCarRoadName != null ? inCarRoadName.hashCode() : 0);
        result = 31 * result + (outCarRoadId != null ? outCarRoadId.hashCode() : 0);
        result = 31 * result + (outCarRoadName != null ? outCarRoadName.hashCode() : 0);
        result = 31 * result + (chargeTime != null ? chargeTime.hashCode() : 0);
        result = 31 * result + (inTime != null ? inTime.hashCode() : 0);
        result = 31 * result + (inChargeTime != null ? inChargeTime.hashCode() : 0);
        result = 31 * result + (outTime != null ? outTime.hashCode() : 0);
        result = 31 * result + (stayTime != null ? stayTime.hashCode() : 0);
        result = 31 * result + (chargeReceivableAmount != null ? chargeReceivableAmount.hashCode() : 0);
        result = 31 * result + (chargeActualAmount != null ? chargeActualAmount.hashCode() : 0);
        result = 31 * result + (inRecordId != null ? inRecordId.hashCode() : 0);
        result = 31 * result + (outRecordId != null ? outRecordId.hashCode() : 0);
        result = 31 * result + (releaseType != null ? releaseType.hashCode() : 0);
        result = 31 * result + (releaseReason != null ? releaseReason.hashCode() : 0);
        result = 31 * result + (carNoAttribute != null ? carNoAttribute.hashCode() : 0);
        result = 31 * result + (carType != null ? carType.hashCode() : 0);
        result = 31 * result + (chargeOperatorId != null ? chargeOperatorId.hashCode() : 0);
        result = 31 * result + (chargeOperatorName != null ? chargeOperatorName.hashCode() : 0);
        result = 31 * result + (chargePostId != null ? chargePostId.hashCode() : 0);
        result = 31 * result + (chargePostName != null ? chargePostName.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        result = 31 * result + (addTime != null ? addTime.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        return result;
    }

    @Basic
    @Column(name = "in_picture_name")
    public String getInPictureName() {
        return inPictureName;
    }

    public void setInPictureName(String inPictureName) {
        this.inPictureName = inPictureName;
    }

    @Basic
    @Column(name = "out_picture_name")
    public String getOutPictureName() {
        return outPictureName;
    }

    public void setOutPictureName(String outPictureName) {
        this.outPictureName = outPictureName;
    }
}
