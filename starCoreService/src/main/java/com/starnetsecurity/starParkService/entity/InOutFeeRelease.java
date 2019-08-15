package com.starnetsecurity.starParkService.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2017-10-12.
 */
@Entity
@Table(name = "in_out_fee_release", schema = "vamsserver", catalog = "")
public class InOutFeeRelease {
    private String releaseId;
    private String carNo;
    private String inRecordNo;
    private String parkingLotNo;
    private Timestamp inTime;
    private String inCarRoadNo;
    private String inPhotoName;
    private String relateInPhotoName;
    private String outRecordNo;
    private Timestamp outTime;
    private String outCarRoadNo;
    private String outPicName;
    private String relateOutPicName;
    private String inCameraId;
    private String outCameraId;
    private String releaseReason;
    private Double availableAmount;
    private Double freeAmount;
    private String freeReason;
    private String operatorNo;
    private Timestamp operatorTime;
    private String operationSource;
    private Double realAmount;
    private Timestamp addTime;
    private String userType;
    private String carNoColor;
    private String carNoAttribute;
    private String inOutFeeCarType;
    private String inOperatorId;
    private String inStatus;
    private String outStatus;
    private String operatorName;
    private String carparkName;
    private String inCarRoadName;
    private String outCarRoadName;
    private String inUartDeviceAddr;
    private String outUartDeviceAddr;
    private String carBodyColor;
    private String oldCarNo;
    private String inOutCarNo;
    private String inOutOldCarNo;
    private String chargeReceiveOperatorId;
    private Integer useMark;

    @Id
    @Basic
    @Column(name = "release_id")
    public String getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(String releaseId) {
        this.releaseId = releaseId;
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
    @Column(name = "in_record_no")
    public String getInRecordNo() {
        return inRecordNo;
    }

    public void setInRecordNo(String inRecordNo) {
        this.inRecordNo = inRecordNo;
    }

    @Basic
    @Column(name = "parking_lot_no")
    public String getParkingLotNo() {
        return parkingLotNo;
    }

    public void setParkingLotNo(String parkingLotNo) {
        this.parkingLotNo = parkingLotNo;
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
    @Column(name = "in_car_road_no")
    public String getInCarRoadNo() {
        return inCarRoadNo;
    }

    public void setInCarRoadNo(String inCarRoadNo) {
        this.inCarRoadNo = inCarRoadNo;
    }

    @Basic
    @Column(name = "in_photo_name")
    public String getInPhotoName() {
        return inPhotoName;
    }

    public void setInPhotoName(String inPhotoName) {
        this.inPhotoName = inPhotoName;
    }

    @Basic
    @Column(name = "relate_in_photo_name")
    public String getRelateInPhotoName() {
        return relateInPhotoName;
    }

    public void setRelateInPhotoName(String relateInPhotoName) {
        this.relateInPhotoName = relateInPhotoName;
    }

    @Basic
    @Column(name = "out_record_no")
    public String getOutRecordNo() {
        return outRecordNo;
    }

    public void setOutRecordNo(String outRecordNo) {
        this.outRecordNo = outRecordNo;
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
    @Column(name = "out_car_road_no")
    public String getOutCarRoadNo() {
        return outCarRoadNo;
    }

    public void setOutCarRoadNo(String outCarRoadNo) {
        this.outCarRoadNo = outCarRoadNo;
    }

    @Basic
    @Column(name = "out_pic_name")
    public String getOutPicName() {
        return outPicName;
    }

    public void setOutPicName(String outPicName) {
        this.outPicName = outPicName;
    }

    @Basic
    @Column(name = "relate_out_pic_name")
    public String getRelateOutPicName() {
        return relateOutPicName;
    }

    public void setRelateOutPicName(String relateOutPicName) {
        this.relateOutPicName = relateOutPicName;
    }

    @Basic
    @Column(name = "in_camera_id")
    public String getInCameraId() {
        return inCameraId;
    }

    public void setInCameraId(String inCameraId) {
        this.inCameraId = inCameraId;
    }

    @Basic
    @Column(name = "out_camera_id")
    public String getOutCameraId() {
        return outCameraId;
    }

    public void setOutCameraId(String outCameraId) {
        this.outCameraId = outCameraId;
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
    @Column(name = "available_amount")
    public Double getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(Double availableAmount) {
        this.availableAmount = availableAmount;
    }

    @Basic
    @Column(name = "free_amount")
    public Double getFreeAmount() {
        return freeAmount;
    }

    public void setFreeAmount(Double freeAmount) {
        this.freeAmount = freeAmount;
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
    @Column(name = "operator_no")
    public String getOperatorNo() {
        return operatorNo;
    }

    public void setOperatorNo(String operatorNo) {
        this.operatorNo = operatorNo;
    }

    @Basic
    @Column(name = "operator_time")
    public Timestamp getOperatorTime() {
        return operatorTime;
    }

    public void setOperatorTime(Timestamp operatorTime) {
        this.operatorTime = operatorTime;
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
    @Column(name = "real_amount")
    public Double getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(Double realAmount) {
        this.realAmount = realAmount;
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
    @Column(name = "user_type")
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Basic
    @Column(name = "car_no_color")
    public String getCarNoColor() {
        return carNoColor;
    }

    public void setCarNoColor(String carNoColor) {
        this.carNoColor = carNoColor;
    }

    @Basic
    @Column(name = "car_no_attribute")
    public String getCarNoAttribute() {
        return carNoAttribute;
    }

    public void setCarNoAttribute(String carNoAttribute) {
        this.carNoAttribute = carNoAttribute;
    }

    @Basic
    @Column(name = "in_out_fee_car_type")
    public String getInOutFeeCarType() {
        return inOutFeeCarType;
    }

    public void setInOutFeeCarType(String inOutFeeCarType) {
        this.inOutFeeCarType = inOutFeeCarType;
    }

    @Basic
    @Column(name = "in_operator_id")
    public String getInOperatorId() {
        return inOperatorId;
    }

    public void setInOperatorId(String inOperatorId) {
        this.inOperatorId = inOperatorId;
    }

    @Basic
    @Column(name = "in_status")
    public String getInStatus() {
        return inStatus;
    }

    public void setInStatus(String inStatus) {
        this.inStatus = inStatus;
    }

    @Basic
    @Column(name = "out_status")
    public String getOutStatus() {
        return outStatus;
    }

    public void setOutStatus(String outStatus) {
        this.outStatus = outStatus;
    }

    @Basic
    @Column(name = "operator_name")
    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
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
    @Column(name = "in_car_road_name")
    public String getInCarRoadName() {
        return inCarRoadName;
    }

    public void setInCarRoadName(String inCarRoadName) {
        this.inCarRoadName = inCarRoadName;
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
    @Column(name = "in_uart_device_addr")
    public String getInUartDeviceAddr() {
        return inUartDeviceAddr;
    }

    public void setInUartDeviceAddr(String inUartDeviceAddr) {
        this.inUartDeviceAddr = inUartDeviceAddr;
    }

    @Basic
    @Column(name = "out_uart_device_addr")
    public String getOutUartDeviceAddr() {
        return outUartDeviceAddr;
    }

    public void setOutUartDeviceAddr(String outUartDeviceAddr) {
        this.outUartDeviceAddr = outUartDeviceAddr;
    }

    @Basic
    @Column(name = "car_body_color")
    public String getCarBodyColor() {
        return carBodyColor;
    }

    public void setCarBodyColor(String carBodyColor) {
        this.carBodyColor = carBodyColor;
    }

    @Basic
    @Column(name = "old_car_no")
    public String getOldCarNo() {
        return oldCarNo;
    }

    public void setOldCarNo(String oldCarNo) {
        this.oldCarNo = oldCarNo;
    }

    @Basic
    @Column(name = "in_out_car_no")
    public String getInOutCarNo() {
        return inOutCarNo;
    }

    public void setInOutCarNo(String inOutCarNo) {
        this.inOutCarNo = inOutCarNo;
    }

    @Basic
    @Column(name = "in_out_old_car_no")
    public String getInOutOldCarNo() {
        return inOutOldCarNo;
    }

    public void setInOutOldCarNo(String inOutOldCarNo) {
        this.inOutOldCarNo = inOutOldCarNo;
    }

    @Basic
    @Column(name = "charge_receive_operator_id")
    public String getChargeReceiveOperatorId() {
        return chargeReceiveOperatorId;
    }

    public void setChargeReceiveOperatorId(String chargeReceiveOperatorId) {
        this.chargeReceiveOperatorId = chargeReceiveOperatorId;
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

        InOutFeeRelease that = (InOutFeeRelease) o;

        if (releaseId != null ? !releaseId.equals(that.releaseId) : that.releaseId != null) return false;
        if (carNo != null ? !carNo.equals(that.carNo) : that.carNo != null) return false;
        if (inRecordNo != null ? !inRecordNo.equals(that.inRecordNo) : that.inRecordNo != null) return false;
        if (parkingLotNo != null ? !parkingLotNo.equals(that.parkingLotNo) : that.parkingLotNo != null) return false;
        if (inTime != null ? !inTime.equals(that.inTime) : that.inTime != null) return false;
        if (inCarRoadNo != null ? !inCarRoadNo.equals(that.inCarRoadNo) : that.inCarRoadNo != null) return false;
        if (inPhotoName != null ? !inPhotoName.equals(that.inPhotoName) : that.inPhotoName != null) return false;
        if (relateInPhotoName != null ? !relateInPhotoName.equals(that.relateInPhotoName) : that.relateInPhotoName != null)
            return false;
        if (outRecordNo != null ? !outRecordNo.equals(that.outRecordNo) : that.outRecordNo != null) return false;
        if (outTime != null ? !outTime.equals(that.outTime) : that.outTime != null) return false;
        if (outCarRoadNo != null ? !outCarRoadNo.equals(that.outCarRoadNo) : that.outCarRoadNo != null) return false;
        if (outPicName != null ? !outPicName.equals(that.outPicName) : that.outPicName != null) return false;
        if (relateOutPicName != null ? !relateOutPicName.equals(that.relateOutPicName) : that.relateOutPicName != null)
            return false;
        if (inCameraId != null ? !inCameraId.equals(that.inCameraId) : that.inCameraId != null) return false;
        if (outCameraId != null ? !outCameraId.equals(that.outCameraId) : that.outCameraId != null) return false;
        if (releaseReason != null ? !releaseReason.equals(that.releaseReason) : that.releaseReason != null)
            return false;
        if (availableAmount != null ? !availableAmount.equals(that.availableAmount) : that.availableAmount != null)
            return false;
        if (freeAmount != null ? !freeAmount.equals(that.freeAmount) : that.freeAmount != null) return false;
        if (freeReason != null ? !freeReason.equals(that.freeReason) : that.freeReason != null) return false;
        if (operatorNo != null ? !operatorNo.equals(that.operatorNo) : that.operatorNo != null) return false;
        if (operatorTime != null ? !operatorTime.equals(that.operatorTime) : that.operatorTime != null) return false;
        if (operationSource != null ? !operationSource.equals(that.operationSource) : that.operationSource != null)
            return false;
        if (realAmount != null ? !realAmount.equals(that.realAmount) : that.realAmount != null) return false;
        if (addTime != null ? !addTime.equals(that.addTime) : that.addTime != null) return false;
        if (userType != null ? !userType.equals(that.userType) : that.userType != null) return false;
        if (carNoColor != null ? !carNoColor.equals(that.carNoColor) : that.carNoColor != null) return false;
        if (carNoAttribute != null ? !carNoAttribute.equals(that.carNoAttribute) : that.carNoAttribute != null)
            return false;
        if (inOutFeeCarType != null ? !inOutFeeCarType.equals(that.inOutFeeCarType) : that.inOutFeeCarType != null)
            return false;
        if (inOperatorId != null ? !inOperatorId.equals(that.inOperatorId) : that.inOperatorId != null) return false;
        if (inStatus != null ? !inStatus.equals(that.inStatus) : that.inStatus != null) return false;
        if (outStatus != null ? !outStatus.equals(that.outStatus) : that.outStatus != null) return false;
        if (operatorName != null ? !operatorName.equals(that.operatorName) : that.operatorName != null) return false;
        if (carparkName != null ? !carparkName.equals(that.carparkName) : that.carparkName != null) return false;
        if (inCarRoadName != null ? !inCarRoadName.equals(that.inCarRoadName) : that.inCarRoadName != null)
            return false;
        if (outCarRoadName != null ? !outCarRoadName.equals(that.outCarRoadName) : that.outCarRoadName != null)
            return false;
        if (inUartDeviceAddr != null ? !inUartDeviceAddr.equals(that.inUartDeviceAddr) : that.inUartDeviceAddr != null)
            return false;
        if (outUartDeviceAddr != null ? !outUartDeviceAddr.equals(that.outUartDeviceAddr) : that.outUartDeviceAddr != null)
            return false;
        if (carBodyColor != null ? !carBodyColor.equals(that.carBodyColor) : that.carBodyColor != null) return false;
        if (oldCarNo != null ? !oldCarNo.equals(that.oldCarNo) : that.oldCarNo != null) return false;
        if (inOutCarNo != null ? !inOutCarNo.equals(that.inOutCarNo) : that.inOutCarNo != null) return false;
        if (inOutOldCarNo != null ? !inOutOldCarNo.equals(that.inOutOldCarNo) : that.inOutOldCarNo != null)
            return false;
        if (chargeReceiveOperatorId != null ? !chargeReceiveOperatorId.equals(that.chargeReceiveOperatorId) : that.chargeReceiveOperatorId != null)
            return false;
        if (useMark != null ? !useMark.equals(that.useMark) : that.useMark != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = releaseId != null ? releaseId.hashCode() : 0;
        result = 31 * result + (carNo != null ? carNo.hashCode() : 0);
        result = 31 * result + (inRecordNo != null ? inRecordNo.hashCode() : 0);
        result = 31 * result + (parkingLotNo != null ? parkingLotNo.hashCode() : 0);
        result = 31 * result + (inTime != null ? inTime.hashCode() : 0);
        result = 31 * result + (inCarRoadNo != null ? inCarRoadNo.hashCode() : 0);
        result = 31 * result + (inPhotoName != null ? inPhotoName.hashCode() : 0);
        result = 31 * result + (relateInPhotoName != null ? relateInPhotoName.hashCode() : 0);
        result = 31 * result + (outRecordNo != null ? outRecordNo.hashCode() : 0);
        result = 31 * result + (outTime != null ? outTime.hashCode() : 0);
        result = 31 * result + (outCarRoadNo != null ? outCarRoadNo.hashCode() : 0);
        result = 31 * result + (outPicName != null ? outPicName.hashCode() : 0);
        result = 31 * result + (relateOutPicName != null ? relateOutPicName.hashCode() : 0);
        result = 31 * result + (inCameraId != null ? inCameraId.hashCode() : 0);
        result = 31 * result + (outCameraId != null ? outCameraId.hashCode() : 0);
        result = 31 * result + (releaseReason != null ? releaseReason.hashCode() : 0);
        result = 31 * result + (availableAmount != null ? availableAmount.hashCode() : 0);
        result = 31 * result + (freeAmount != null ? freeAmount.hashCode() : 0);
        result = 31 * result + (freeReason != null ? freeReason.hashCode() : 0);
        result = 31 * result + (operatorNo != null ? operatorNo.hashCode() : 0);
        result = 31 * result + (operatorTime != null ? operatorTime.hashCode() : 0);
        result = 31 * result + (operationSource != null ? operationSource.hashCode() : 0);
        result = 31 * result + (realAmount != null ? realAmount.hashCode() : 0);
        result = 31 * result + (addTime != null ? addTime.hashCode() : 0);
        result = 31 * result + (userType != null ? userType.hashCode() : 0);
        result = 31 * result + (carNoColor != null ? carNoColor.hashCode() : 0);
        result = 31 * result + (carNoAttribute != null ? carNoAttribute.hashCode() : 0);
        result = 31 * result + (inOutFeeCarType != null ? inOutFeeCarType.hashCode() : 0);
        result = 31 * result + (inOperatorId != null ? inOperatorId.hashCode() : 0);
        result = 31 * result + (inStatus != null ? inStatus.hashCode() : 0);
        result = 31 * result + (outStatus != null ? outStatus.hashCode() : 0);
        result = 31 * result + (operatorName != null ? operatorName.hashCode() : 0);
        result = 31 * result + (carparkName != null ? carparkName.hashCode() : 0);
        result = 31 * result + (inCarRoadName != null ? inCarRoadName.hashCode() : 0);
        result = 31 * result + (outCarRoadName != null ? outCarRoadName.hashCode() : 0);
        result = 31 * result + (inUartDeviceAddr != null ? inUartDeviceAddr.hashCode() : 0);
        result = 31 * result + (outUartDeviceAddr != null ? outUartDeviceAddr.hashCode() : 0);
        result = 31 * result + (carBodyColor != null ? carBodyColor.hashCode() : 0);
        result = 31 * result + (oldCarNo != null ? oldCarNo.hashCode() : 0);
        result = 31 * result + (inOutCarNo != null ? inOutCarNo.hashCode() : 0);
        result = 31 * result + (inOutOldCarNo != null ? inOutOldCarNo.hashCode() : 0);
        result = 31 * result + (chargeReceiveOperatorId != null ? chargeReceiveOperatorId.hashCode() : 0);
        result = 31 * result + (useMark != null ? useMark.hashCode() : 0);
        return result;
    }
}
