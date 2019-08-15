package com.starnetsecurity.starParkService.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2017-10-12.
 */
@Entity
@Table(name = "in_out_record", schema = "vamsserver", catalog = "")
public class InOutRecord {
    private String inOutId;
    private String inOutCarNo;
    private String inOutParkingLotNo;
    private Timestamp inOutTime;
    private String inOutFlag;
    private String inOutCarRoadId;
    private String inOutStatus;
    private String photoCapturePicName;
    private String relatePhotoCapturePicName;
    private String operationSource;
    private Timestamp addTime;
    private String inOutCameraId;
    private String userType;
    private String carNoColor;
    private String carNoAttribute;
    private String ifSpecialParkingLot;
    private String inOutCarType;
    private String inOutOperatorId;
    private String operatorName;
    private String carparkName;
    private String carRoadName;
    private String inOutUartDeviceAddr;
    private String inOutCarBodyColor;
    private String isRubbish;
    private String isTemporaryRecord;
    private String inOutCarTypeId;
    private String inOutCarMemberId;
    private String inOutOldCarNo;
    private String isRecordMatching;
    private String isChargeMatching;
    private String chargeReceiveOperatorId;
    private Integer useMark;

    @Id
    @Column(name = "in_out_id")
    public String getInOutId() {
        return inOutId;
    }

    public void setInOutId(String inOutId) {
        this.inOutId = inOutId;
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
    @Column(name = "in_out_parking_lot_no")
    public String getInOutParkingLotNo() {
        return inOutParkingLotNo;
    }

    public void setInOutParkingLotNo(String inOutParkingLotNo) {
        this.inOutParkingLotNo = inOutParkingLotNo;
    }

    @Basic
    @Column(name = "in_out_time")
    public Timestamp getInOutTime() {
        return inOutTime;
    }

    public void setInOutTime(Timestamp inOutTime) {
        this.inOutTime = inOutTime;
    }

    @Basic
    @Column(name = "in_out_flag")
    public String getInOutFlag() {
        return inOutFlag;
    }

    public void setInOutFlag(String inOutFlag) {
        this.inOutFlag = inOutFlag;
    }

    @Basic
    @Column(name = "in_out_car_road_id")
    public String getInOutCarRoadId() {
        return inOutCarRoadId;
    }

    public void setInOutCarRoadId(String inOutCarRoadId) {
        this.inOutCarRoadId = inOutCarRoadId;
    }

    @Basic
    @Column(name = "in_out_status")
    public String getInOutStatus() {
        return inOutStatus;
    }

    public void setInOutStatus(String inOutStatus) {
        this.inOutStatus = inOutStatus;
    }

    @Basic
    @Column(name = "photo_capture_pic_name")
    public String getPhotoCapturePicName() {
        return photoCapturePicName;
    }

    public void setPhotoCapturePicName(String photoCapturePicName) {
        this.photoCapturePicName = photoCapturePicName;
    }

    @Basic
    @Column(name = "relate_photo_capture_pic_name")
    public String getRelatePhotoCapturePicName() {
        return relatePhotoCapturePicName;
    }

    public void setRelatePhotoCapturePicName(String relatePhotoCapturePicName) {
        this.relatePhotoCapturePicName = relatePhotoCapturePicName;
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
    @Column(name = "add_time")
    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    @Basic
    @Column(name = "in_out_camera_id")
    public String getInOutCameraId() {
        return inOutCameraId;
    }

    public void setInOutCameraId(String inOutCameraId) {
        this.inOutCameraId = inOutCameraId;
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
    @Column(name = "if_special_parking_lot")
    public String getIfSpecialParkingLot() {
        return ifSpecialParkingLot;
    }

    public void setIfSpecialParkingLot(String ifSpecialParkingLot) {
        this.ifSpecialParkingLot = ifSpecialParkingLot;
    }

    @Basic
    @Column(name = "in_out_car_type")
    public String getInOutCarType() {
        return inOutCarType;
    }

    public void setInOutCarType(String inOutCarType) {
        this.inOutCarType = inOutCarType;
    }

    @Basic
    @Column(name = "in_out_operator_id")
    public String getInOutOperatorId() {
        return inOutOperatorId;
    }

    public void setInOutOperatorId(String inOutOperatorId) {
        this.inOutOperatorId = inOutOperatorId;
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
    @Column(name = "car_road_name")
    public String getCarRoadName() {
        return carRoadName;
    }

    public void setCarRoadName(String carRoadName) {
        this.carRoadName = carRoadName;
    }

    @Basic
    @Column(name = "in_out_uart_device_addr")
    public String getInOutUartDeviceAddr() {
        return inOutUartDeviceAddr;
    }

    public void setInOutUartDeviceAddr(String inOutUartDeviceAddr) {
        this.inOutUartDeviceAddr = inOutUartDeviceAddr;
    }

    @Basic
    @Column(name = "in_out_car_body_color")
    public String getInOutCarBodyColor() {
        return inOutCarBodyColor;
    }

    public void setInOutCarBodyColor(String inOutCarBodyColor) {
        this.inOutCarBodyColor = inOutCarBodyColor;
    }

    @Basic
    @Column(name = "is_rubbish")
    public String getIsRubbish() {
        return isRubbish;
    }

    public void setIsRubbish(String isRubbish) {
        this.isRubbish = isRubbish;
    }

    @Basic
    @Column(name = "is_temporary_record")
    public String getIsTemporaryRecord() {
        return isTemporaryRecord;
    }

    public void setIsTemporaryRecord(String isTemporaryRecord) {
        this.isTemporaryRecord = isTemporaryRecord;
    }

    @Basic
    @Column(name = "in_out_car_type_id")
    public String getInOutCarTypeId() {
        return inOutCarTypeId;
    }

    public void setInOutCarTypeId(String inOutCarTypeId) {
        this.inOutCarTypeId = inOutCarTypeId;
    }

    @Basic
    @Column(name = "in_out_car_member_id")
    public String getInOutCarMemberId() {
        return inOutCarMemberId;
    }

    public void setInOutCarMemberId(String inOutCarMemberId) {
        this.inOutCarMemberId = inOutCarMemberId;
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
    @Column(name = "is_record_matching")
    public String getIsRecordMatching() {
        return isRecordMatching;
    }

    public void setIsRecordMatching(String isRecordMatching) {
        this.isRecordMatching = isRecordMatching;
    }

    @Basic
    @Column(name = "is_charge_matching")
    public String getIsChargeMatching() {
        return isChargeMatching;
    }

    public void setIsChargeMatching(String isChargeMatching) {
        this.isChargeMatching = isChargeMatching;
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

        InOutRecord that = (InOutRecord) o;

        if (inOutId != null ? !inOutId.equals(that.inOutId) : that.inOutId != null) return false;
        if (inOutCarNo != null ? !inOutCarNo.equals(that.inOutCarNo) : that.inOutCarNo != null) return false;
        if (inOutParkingLotNo != null ? !inOutParkingLotNo.equals(that.inOutParkingLotNo) : that.inOutParkingLotNo != null)
            return false;
        if (inOutTime != null ? !inOutTime.equals(that.inOutTime) : that.inOutTime != null) return false;
        if (inOutFlag != null ? !inOutFlag.equals(that.inOutFlag) : that.inOutFlag != null) return false;
        if (inOutCarRoadId != null ? !inOutCarRoadId.equals(that.inOutCarRoadId) : that.inOutCarRoadId != null)
            return false;
        if (inOutStatus != null ? !inOutStatus.equals(that.inOutStatus) : that.inOutStatus != null) return false;
        if (photoCapturePicName != null ? !photoCapturePicName.equals(that.photoCapturePicName) : that.photoCapturePicName != null)
            return false;
        if (relatePhotoCapturePicName != null ? !relatePhotoCapturePicName.equals(that.relatePhotoCapturePicName) : that.relatePhotoCapturePicName != null)
            return false;
        if (operationSource != null ? !operationSource.equals(that.operationSource) : that.operationSource != null)
            return false;
        if (addTime != null ? !addTime.equals(that.addTime) : that.addTime != null) return false;
        if (inOutCameraId != null ? !inOutCameraId.equals(that.inOutCameraId) : that.inOutCameraId != null)
            return false;
        if (userType != null ? !userType.equals(that.userType) : that.userType != null) return false;
        if (carNoColor != null ? !carNoColor.equals(that.carNoColor) : that.carNoColor != null) return false;
        if (carNoAttribute != null ? !carNoAttribute.equals(that.carNoAttribute) : that.carNoAttribute != null)
            return false;
        if (ifSpecialParkingLot != null ? !ifSpecialParkingLot.equals(that.ifSpecialParkingLot) : that.ifSpecialParkingLot != null)
            return false;
        if (inOutCarType != null ? !inOutCarType.equals(that.inOutCarType) : that.inOutCarType != null) return false;
        if (inOutOperatorId != null ? !inOutOperatorId.equals(that.inOutOperatorId) : that.inOutOperatorId != null)
            return false;
        if (operatorName != null ? !operatorName.equals(that.operatorName) : that.operatorName != null) return false;
        if (carparkName != null ? !carparkName.equals(that.carparkName) : that.carparkName != null) return false;
        if (carRoadName != null ? !carRoadName.equals(that.carRoadName) : that.carRoadName != null) return false;
        if (inOutUartDeviceAddr != null ? !inOutUartDeviceAddr.equals(that.inOutUartDeviceAddr) : that.inOutUartDeviceAddr != null)
            return false;
        if (inOutCarBodyColor != null ? !inOutCarBodyColor.equals(that.inOutCarBodyColor) : that.inOutCarBodyColor != null)
            return false;
        if (isRubbish != null ? !isRubbish.equals(that.isRubbish) : that.isRubbish != null) return false;
        if (isTemporaryRecord != null ? !isTemporaryRecord.equals(that.isTemporaryRecord) : that.isTemporaryRecord != null)
            return false;
        if (inOutCarTypeId != null ? !inOutCarTypeId.equals(that.inOutCarTypeId) : that.inOutCarTypeId != null)
            return false;
        if (inOutCarMemberId != null ? !inOutCarMemberId.equals(that.inOutCarMemberId) : that.inOutCarMemberId != null)
            return false;
        if (inOutOldCarNo != null ? !inOutOldCarNo.equals(that.inOutOldCarNo) : that.inOutOldCarNo != null)
            return false;
        if (isRecordMatching != null ? !isRecordMatching.equals(that.isRecordMatching) : that.isRecordMatching != null)
            return false;
        if (isChargeMatching != null ? !isChargeMatching.equals(that.isChargeMatching) : that.isChargeMatching != null)
            return false;
        if (chargeReceiveOperatorId != null ? !chargeReceiveOperatorId.equals(that.chargeReceiveOperatorId) : that.chargeReceiveOperatorId != null)
            return false;
        if (useMark != null ? !useMark.equals(that.useMark) : that.useMark != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = inOutId != null ? inOutId.hashCode() : 0;
        result = 31 * result + (inOutCarNo != null ? inOutCarNo.hashCode() : 0);
        result = 31 * result + (inOutParkingLotNo != null ? inOutParkingLotNo.hashCode() : 0);
        result = 31 * result + (inOutTime != null ? inOutTime.hashCode() : 0);
        result = 31 * result + (inOutFlag != null ? inOutFlag.hashCode() : 0);
        result = 31 * result + (inOutCarRoadId != null ? inOutCarRoadId.hashCode() : 0);
        result = 31 * result + (inOutStatus != null ? inOutStatus.hashCode() : 0);
        result = 31 * result + (photoCapturePicName != null ? photoCapturePicName.hashCode() : 0);
        result = 31 * result + (relatePhotoCapturePicName != null ? relatePhotoCapturePicName.hashCode() : 0);
        result = 31 * result + (operationSource != null ? operationSource.hashCode() : 0);
        result = 31 * result + (addTime != null ? addTime.hashCode() : 0);
        result = 31 * result + (inOutCameraId != null ? inOutCameraId.hashCode() : 0);
        result = 31 * result + (userType != null ? userType.hashCode() : 0);
        result = 31 * result + (carNoColor != null ? carNoColor.hashCode() : 0);
        result = 31 * result + (carNoAttribute != null ? carNoAttribute.hashCode() : 0);
        result = 31 * result + (ifSpecialParkingLot != null ? ifSpecialParkingLot.hashCode() : 0);
        result = 31 * result + (inOutCarType != null ? inOutCarType.hashCode() : 0);
        result = 31 * result + (inOutOperatorId != null ? inOutOperatorId.hashCode() : 0);
        result = 31 * result + (operatorName != null ? operatorName.hashCode() : 0);
        result = 31 * result + (carparkName != null ? carparkName.hashCode() : 0);
        result = 31 * result + (carRoadName != null ? carRoadName.hashCode() : 0);
        result = 31 * result + (inOutUartDeviceAddr != null ? inOutUartDeviceAddr.hashCode() : 0);
        result = 31 * result + (inOutCarBodyColor != null ? inOutCarBodyColor.hashCode() : 0);
        result = 31 * result + (isRubbish != null ? isRubbish.hashCode() : 0);
        result = 31 * result + (isTemporaryRecord != null ? isTemporaryRecord.hashCode() : 0);
        result = 31 * result + (inOutCarTypeId != null ? inOutCarTypeId.hashCode() : 0);
        result = 31 * result + (inOutCarMemberId != null ? inOutCarMemberId.hashCode() : 0);
        result = 31 * result + (inOutOldCarNo != null ? inOutOldCarNo.hashCode() : 0);
        result = 31 * result + (isRecordMatching != null ? isRecordMatching.hashCode() : 0);
        result = 31 * result + (isChargeMatching != null ? isChargeMatching.hashCode() : 0);
        result = 31 * result + (chargeReceiveOperatorId != null ? chargeReceiveOperatorId.hashCode() : 0);
        result = 31 * result + (useMark != null ? useMark.hashCode() : 0);
        return result;
    }
}
