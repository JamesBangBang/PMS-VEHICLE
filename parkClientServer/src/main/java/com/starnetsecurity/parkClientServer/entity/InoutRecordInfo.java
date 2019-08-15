package com.starnetsecurity.parkClientServer.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2017-12-25.
 */
@Entity
@Table(name = "inout_record_info", schema = "vams", catalog = "")
public class InoutRecordInfo {
    private String inoutRecordId;
    private String carNoOld;
    private String carNo;
    private Timestamp inoutTime;
    private Byte inoutFlag;
    private Byte inoutStatus;
    private String carparkId;
    private String carparkName;
    private String carRoadId;
    private String carRoadName;
    private String postId;
    private String postName;
    private String cameraId;
    private String photoCapturePicName;
    private Byte carNoColor;
    private Byte carBodyColor;
    private String ifSpecialParkingLot;
    private String carType;
    private String operatorId;
    private String operatorName;
    private String remark;
    private Byte useMark;
    private Timestamp addTime;

    @Id
    @GenericGenerator(name="systemUUID",strategy="uuid")
    @GeneratedValue(generator="systemUUID")
    @Basic
    @Column(name = "inout_record_id")
    public String getInoutRecordId() {
        return inoutRecordId;
    }

    public void setInoutRecordId(String inoutRecordId) {
        this.inoutRecordId = inoutRecordId;
    }

    @Basic
    @Column(name = "car_no_old")
    public String getCarNoOld() {
        return carNoOld;
    }

    public void setCarNoOld(String carNoOld) {
        this.carNoOld = carNoOld;
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
    @Column(name = "inout_time")
    public Timestamp getInoutTime() {
        return inoutTime;
    }

    public void setInoutTime(Timestamp inoutTime) {
        this.inoutTime = inoutTime;
    }

    @Basic
    @Column(name = "inout_flag")
    public Byte getInoutFlag() {
        return inoutFlag;
    }

    public void setInoutFlag(Byte inoutFlag) {
        this.inoutFlag = inoutFlag;
    }

    @Basic
    @Column(name = "inout_status")
    public Byte getInoutStatus() {
        return inoutStatus;
    }

    public void setInoutStatus(Byte inoutStatus) {
        this.inoutStatus = inoutStatus;
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
    @Column(name = "car_road_id")
    public String getCarRoadId() {
        return carRoadId;
    }

    public void setCarRoadId(String carRoadId) {
        this.carRoadId = carRoadId;
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
    @Column(name = "post_id")
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    @Basic
    @Column(name = "post_name")
    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    @Basic
    @Column(name = "camera_id")
    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
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
    @Column(name = "car_no_color")
    public Byte getCarNoColor() {
        return carNoColor;
    }

    public void setCarNoColor(Byte carNoColor) {
        this.carNoColor = carNoColor;
    }

    @Basic
    @Column(name = "car_body_color")
    public Byte getCarBodyColor() {
        return carBodyColor;
    }

    public void setCarBodyColor(Byte carBodyColor) {
        this.carBodyColor = carBodyColor;
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
    @Column(name = "car_type")
    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    @Basic
    @Column(name = "operator_id")
    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
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
    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Basic
    @Column(name = "use_mark")
    public Byte getUseMark() {
        return useMark;
    }

    public void setUseMark(Byte useMark) {
        this.useMark = useMark;
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

        InoutRecordInfo that = (InoutRecordInfo) o;

        if (inoutRecordId != null ? !inoutRecordId.equals(that.inoutRecordId) : that.inoutRecordId != null)
            return false;
        if (carNoOld != null ? !carNoOld.equals(that.carNoOld) : that.carNoOld != null) return false;
        if (carNo != null ? !carNo.equals(that.carNo) : that.carNo != null) return false;
        if (inoutTime != null ? !inoutTime.equals(that.inoutTime) : that.inoutTime != null) return false;
        if (inoutFlag != null ? !inoutFlag.equals(that.inoutFlag) : that.inoutFlag != null) return false;
        if (inoutStatus != null ? !inoutStatus.equals(that.inoutStatus) : that.inoutStatus != null) return false;
        if (carparkId != null ? !carparkId.equals(that.carparkId) : that.carparkId != null) return false;
        if (carparkName != null ? !carparkName.equals(that.carparkName) : that.carparkName != null) return false;
        if (carRoadId != null ? !carRoadId.equals(that.carRoadId) : that.carRoadId != null) return false;
        if (carRoadName != null ? !carRoadName.equals(that.carRoadName) : that.carRoadName != null) return false;
        if (postId != null ? !postId.equals(that.postId) : that.postId != null) return false;
        if (postName != null ? !postName.equals(that.postName) : that.postName != null) return false;
        if (cameraId != null ? !cameraId.equals(that.cameraId) : that.cameraId != null) return false;
        if (photoCapturePicName != null ? !photoCapturePicName.equals(that.photoCapturePicName) : that.photoCapturePicName != null)
            return false;
        if (carNoColor != null ? !carNoColor.equals(that.carNoColor) : that.carNoColor != null) return false;
        if (carBodyColor != null ? !carBodyColor.equals(that.carBodyColor) : that.carBodyColor != null) return false;
        if (ifSpecialParkingLot != null ? !ifSpecialParkingLot.equals(that.ifSpecialParkingLot) : that.ifSpecialParkingLot != null)
            return false;
        if (carType != null ? !carType.equals(that.carType) : that.carType != null) return false;
        if (operatorId != null ? !operatorId.equals(that.operatorId) : that.operatorId != null) return false;
        if (operatorName != null ? !operatorName.equals(that.operatorName) : that.operatorName != null) return false;
        if (remark != null ? !remark.equals(that.remark) : that.remark != null) return false;
        if (useMark != null ? !useMark.equals(that.useMark) : that.useMark != null) return false;
        if (addTime != null ? !addTime.equals(that.addTime) : that.addTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = inoutRecordId != null ? inoutRecordId.hashCode() : 0;
        result = 31 * result + (carNoOld != null ? carNoOld.hashCode() : 0);
        result = 31 * result + (carNo != null ? carNo.hashCode() : 0);
        result = 31 * result + (inoutTime != null ? inoutTime.hashCode() : 0);
        result = 31 * result + (inoutFlag != null ? inoutFlag.hashCode() : 0);
        result = 31 * result + (inoutStatus != null ? inoutStatus.hashCode() : 0);
        result = 31 * result + (carparkId != null ? carparkId.hashCode() : 0);
        result = 31 * result + (carparkName != null ? carparkName.hashCode() : 0);
        result = 31 * result + (carRoadId != null ? carRoadId.hashCode() : 0);
        result = 31 * result + (carRoadName != null ? carRoadName.hashCode() : 0);
        result = 31 * result + (postId != null ? postId.hashCode() : 0);
        result = 31 * result + (postName != null ? postName.hashCode() : 0);
        result = 31 * result + (cameraId != null ? cameraId.hashCode() : 0);
        result = 31 * result + (photoCapturePicName != null ? photoCapturePicName.hashCode() : 0);
        result = 31 * result + (carNoColor != null ? carNoColor.hashCode() : 0);
        result = 31 * result + (carBodyColor != null ? carBodyColor.hashCode() : 0);
        result = 31 * result + (ifSpecialParkingLot != null ? ifSpecialParkingLot.hashCode() : 0);
        result = 31 * result + (carType != null ? carType.hashCode() : 0);
        result = 31 * result + (operatorId != null ? operatorId.hashCode() : 0);
        result = 31 * result + (operatorName != null ? operatorName.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        result = 31 * result + (useMark != null ? useMark.hashCode() : 0);
        result = 31 * result + (addTime != null ? addTime.hashCode() : 0);
        return result;
    }
}
