package com.starnetsecurity.starParkService.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2017-10-12.
 */
@Entity
@Table(name = "driver_file", schema = "vamsserver", catalog = "")
public class DriverFile {
    private String driverFileId;
    private String carNo;
    private String driverName;
    private String parkingLotId;
    private String feeTypeId;
    private String ifIssueCard;
    private String cardType;
    private String cardReaderCode;
    private String ifSpecialParkingLot;
    private String ifWhiteListBlackList;
    private String ifOrderWhiteList;
    private Timestamp orderBeginTime;
    private Timestamp orderEndTime;
    private String ifMonthlyRent;
    private Timestamp monthlyRentBeginTime;
    private Timestamp monthlyRentEndTime;
    private Timestamp oldMonthlyRentEndTime;
    private String operationSource;
    private Timestamp addTime;
    private String carPic;
    private String driverInfo;
    private String driverTelephoneNumber;
    private String driverCarparkId;
    private Integer synchronizeToIpcStatus;
    private String carparkName;
    private String parkingLotName;
    private String carBodyColor;
    private Integer memberKindId;
    private String reservationTypeName;
    private String coverId;
    private Integer useMark;
    private String departmentId;

    @Id
    @Column(name = "driver_file_id")
    public String getDriverFileId() {
        return driverFileId;
    }

    public void setDriverFileId(String driverFileId) {
        this.driverFileId = driverFileId;
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
    @Column(name = "driver_name")
    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    @Basic
    @Column(name = "parking_lot_id")
    public String getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(String parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    @Basic
    @Column(name = "fee_type_id")
    public String getFeeTypeId() {
        return feeTypeId;
    }

    public void setFeeTypeId(String feeTypeId) {
        this.feeTypeId = feeTypeId;
    }

    @Basic
    @Column(name = "if_issue_card")
    public String getIfIssueCard() {
        return ifIssueCard;
    }

    public void setIfIssueCard(String ifIssueCard) {
        this.ifIssueCard = ifIssueCard;
    }

    @Basic
    @Column(name = "card_type")
    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    @Basic
    @Column(name = "card_reader_code")
    public String getCardReaderCode() {
        return cardReaderCode;
    }

    public void setCardReaderCode(String cardReaderCode) {
        this.cardReaderCode = cardReaderCode;
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
    @Column(name = "if_white_list_black_list")
    public String getIfWhiteListBlackList() {
        return ifWhiteListBlackList;
    }

    public void setIfWhiteListBlackList(String ifWhiteListBlackList) {
        this.ifWhiteListBlackList = ifWhiteListBlackList;
    }

    @Basic
    @Column(name = "if_order_white_list")
    public String getIfOrderWhiteList() {
        return ifOrderWhiteList;
    }

    public void setIfOrderWhiteList(String ifOrderWhiteList) {
        this.ifOrderWhiteList = ifOrderWhiteList;
    }

    @Basic
    @Column(name = "order_begin_time")
    public Timestamp getOrderBeginTime() {
        return orderBeginTime;
    }

    public void setOrderBeginTime(Timestamp orderBeginTime) {
        this.orderBeginTime = orderBeginTime;
    }

    @Basic
    @Column(name = "order_end_time")
    public Timestamp getOrderEndTime() {
        return orderEndTime;
    }

    public void setOrderEndTime(Timestamp orderEndTime) {
        this.orderEndTime = orderEndTime;
    }

    @Basic
    @Column(name = "if_monthly_rent")
    public String getIfMonthlyRent() {
        return ifMonthlyRent;
    }

    public void setIfMonthlyRent(String ifMonthlyRent) {
        this.ifMonthlyRent = ifMonthlyRent;
    }

    @Basic
    @Column(name = "monthly_rent_begin_time")
    public Timestamp getMonthlyRentBeginTime() {
        return monthlyRentBeginTime;
    }

    public void setMonthlyRentBeginTime(Timestamp monthlyRentBeginTime) {
        this.monthlyRentBeginTime = monthlyRentBeginTime;
    }

    @Basic
    @Column(name = "monthly_rent_end_time")
    public Timestamp getMonthlyRentEndTime() {
        return monthlyRentEndTime;
    }

    public void setMonthlyRentEndTime(Timestamp monthlyRentEndTime) {
        this.monthlyRentEndTime = monthlyRentEndTime;
    }

    @Basic
    @Column(name = "old_monthly_rent_end_time")
    public Timestamp getOldMonthlyRentEndTime() {
        return oldMonthlyRentEndTime;
    }

    public void setOldMonthlyRentEndTime(Timestamp oldMonthlyRentEndTime) {
        this.oldMonthlyRentEndTime = oldMonthlyRentEndTime;
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
    @Column(name = "car_pic")
    public String getCarPic() {
        return carPic;
    }

    public void setCarPic(String carPic) {
        this.carPic = carPic;
    }

    @Basic
    @Column(name = "driver_info")
    public String getDriverInfo() {
        return driverInfo;
    }

    public void setDriverInfo(String driverInfo) {
        this.driverInfo = driverInfo;
    }

    @Basic
    @Column(name = "driver_telephone_number")
    public String getDriverTelephoneNumber() {
        return driverTelephoneNumber;
    }

    public void setDriverTelephoneNumber(String driverTelephoneNumber) {
        this.driverTelephoneNumber = driverTelephoneNumber;
    }

    @Basic
    @Column(name = "driver_carpark_id")
    public String getDriverCarparkId() {
        return driverCarparkId;
    }

    public void setDriverCarparkId(String driverCarparkId) {
        this.driverCarparkId = driverCarparkId;
    }

    @Basic
    @Column(name = "synchronize_to_ipc_status")
    public Integer getSynchronizeToIpcStatus() {
        return synchronizeToIpcStatus;
    }

    public void setSynchronizeToIpcStatus(Integer synchronizeToIpcStatus) {
        this.synchronizeToIpcStatus = synchronizeToIpcStatus;
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
    @Column(name = "parking_lot_name")
    public String getParkingLotName() {
        return parkingLotName;
    }

    public void setParkingLotName(String parkingLotName) {
        this.parkingLotName = parkingLotName;
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
    @Column(name = "member_kind_id")
    public Integer getMemberKindId() {
        return memberKindId;
    }

    public void setMemberKindId(Integer memberKindId) {
        this.memberKindId = memberKindId;
    }

    @Basic
    @Column(name = "reservation_type_name")
    public String getReservationTypeName() {
        return reservationTypeName;
    }

    public void setReservationTypeName(String reservationTypeName) {
        this.reservationTypeName = reservationTypeName;
    }

    @Basic
    @Column(name = "cover_id")
    public String getCoverId() {
        return coverId;
    }

    public void setCoverId(String coverId) {
        this.coverId = coverId;
    }

    @Basic
    @Column(name = "use_mark")
    public Integer getUseMark() {
        return useMark;
    }

    public void setUseMark(Integer useMark) {
        this.useMark = useMark;
    }

    @Basic
    @Column(name = "department_id")
    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DriverFile that = (DriverFile) o;

        if (driverFileId != null ? !driverFileId.equals(that.driverFileId) : that.driverFileId != null) return false;
        if (carNo != null ? !carNo.equals(that.carNo) : that.carNo != null) return false;
        if (driverName != null ? !driverName.equals(that.driverName) : that.driverName != null) return false;
        if (parkingLotId != null ? !parkingLotId.equals(that.parkingLotId) : that.parkingLotId != null) return false;
        if (feeTypeId != null ? !feeTypeId.equals(that.feeTypeId) : that.feeTypeId != null) return false;
        if (ifIssueCard != null ? !ifIssueCard.equals(that.ifIssueCard) : that.ifIssueCard != null) return false;
        if (cardType != null ? !cardType.equals(that.cardType) : that.cardType != null) return false;
        if (cardReaderCode != null ? !cardReaderCode.equals(that.cardReaderCode) : that.cardReaderCode != null)
            return false;
        if (ifSpecialParkingLot != null ? !ifSpecialParkingLot.equals(that.ifSpecialParkingLot) : that.ifSpecialParkingLot != null)
            return false;
        if (ifWhiteListBlackList != null ? !ifWhiteListBlackList.equals(that.ifWhiteListBlackList) : that.ifWhiteListBlackList != null)
            return false;
        if (ifOrderWhiteList != null ? !ifOrderWhiteList.equals(that.ifOrderWhiteList) : that.ifOrderWhiteList != null)
            return false;
        if (orderBeginTime != null ? !orderBeginTime.equals(that.orderBeginTime) : that.orderBeginTime != null)
            return false;
        if (orderEndTime != null ? !orderEndTime.equals(that.orderEndTime) : that.orderEndTime != null) return false;
        if (ifMonthlyRent != null ? !ifMonthlyRent.equals(that.ifMonthlyRent) : that.ifMonthlyRent != null)
            return false;
        if (monthlyRentBeginTime != null ? !monthlyRentBeginTime.equals(that.monthlyRentBeginTime) : that.monthlyRentBeginTime != null)
            return false;
        if (monthlyRentEndTime != null ? !monthlyRentEndTime.equals(that.monthlyRentEndTime) : that.monthlyRentEndTime != null)
            return false;
        if (oldMonthlyRentEndTime != null ? !oldMonthlyRentEndTime.equals(that.oldMonthlyRentEndTime) : that.oldMonthlyRentEndTime != null)
            return false;
        if (operationSource != null ? !operationSource.equals(that.operationSource) : that.operationSource != null)
            return false;
        if (addTime != null ? !addTime.equals(that.addTime) : that.addTime != null) return false;
        if (carPic != null ? !carPic.equals(that.carPic) : that.carPic != null) return false;
        if (driverInfo != null ? !driverInfo.equals(that.driverInfo) : that.driverInfo != null) return false;
        if (driverTelephoneNumber != null ? !driverTelephoneNumber.equals(that.driverTelephoneNumber) : that.driverTelephoneNumber != null)
            return false;
        if (driverCarparkId != null ? !driverCarparkId.equals(that.driverCarparkId) : that.driverCarparkId != null)
            return false;
        if (synchronizeToIpcStatus != null ? !synchronizeToIpcStatus.equals(that.synchronizeToIpcStatus) : that.synchronizeToIpcStatus != null)
            return false;
        if (carparkName != null ? !carparkName.equals(that.carparkName) : that.carparkName != null) return false;
        if (parkingLotName != null ? !parkingLotName.equals(that.parkingLotName) : that.parkingLotName != null)
            return false;
        if (carBodyColor != null ? !carBodyColor.equals(that.carBodyColor) : that.carBodyColor != null) return false;
        if (memberKindId != null ? !memberKindId.equals(that.memberKindId) : that.memberKindId != null) return false;
        if (reservationTypeName != null ? !reservationTypeName.equals(that.reservationTypeName) : that.reservationTypeName != null)
            return false;
        if (coverId != null ? !coverId.equals(that.coverId) : that.coverId != null) return false;
        if (useMark != null ? !useMark.equals(that.useMark) : that.useMark != null) return false;
        if (departmentId != null ? !departmentId.equals(that.departmentId) : that.departmentId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = driverFileId != null ? driverFileId.hashCode() : 0;
        result = 31 * result + (carNo != null ? carNo.hashCode() : 0);
        result = 31 * result + (driverName != null ? driverName.hashCode() : 0);
        result = 31 * result + (parkingLotId != null ? parkingLotId.hashCode() : 0);
        result = 31 * result + (feeTypeId != null ? feeTypeId.hashCode() : 0);
        result = 31 * result + (ifIssueCard != null ? ifIssueCard.hashCode() : 0);
        result = 31 * result + (cardType != null ? cardType.hashCode() : 0);
        result = 31 * result + (cardReaderCode != null ? cardReaderCode.hashCode() : 0);
        result = 31 * result + (ifSpecialParkingLot != null ? ifSpecialParkingLot.hashCode() : 0);
        result = 31 * result + (ifWhiteListBlackList != null ? ifWhiteListBlackList.hashCode() : 0);
        result = 31 * result + (ifOrderWhiteList != null ? ifOrderWhiteList.hashCode() : 0);
        result = 31 * result + (orderBeginTime != null ? orderBeginTime.hashCode() : 0);
        result = 31 * result + (orderEndTime != null ? orderEndTime.hashCode() : 0);
        result = 31 * result + (ifMonthlyRent != null ? ifMonthlyRent.hashCode() : 0);
        result = 31 * result + (monthlyRentBeginTime != null ? monthlyRentBeginTime.hashCode() : 0);
        result = 31 * result + (monthlyRentEndTime != null ? monthlyRentEndTime.hashCode() : 0);
        result = 31 * result + (oldMonthlyRentEndTime != null ? oldMonthlyRentEndTime.hashCode() : 0);
        result = 31 * result + (operationSource != null ? operationSource.hashCode() : 0);
        result = 31 * result + (addTime != null ? addTime.hashCode() : 0);
        result = 31 * result + (carPic != null ? carPic.hashCode() : 0);
        result = 31 * result + (driverInfo != null ? driverInfo.hashCode() : 0);
        result = 31 * result + (driverTelephoneNumber != null ? driverTelephoneNumber.hashCode() : 0);
        result = 31 * result + (driverCarparkId != null ? driverCarparkId.hashCode() : 0);
        result = 31 * result + (synchronizeToIpcStatus != null ? synchronizeToIpcStatus.hashCode() : 0);
        result = 31 * result + (carparkName != null ? carparkName.hashCode() : 0);
        result = 31 * result + (parkingLotName != null ? parkingLotName.hashCode() : 0);
        result = 31 * result + (carBodyColor != null ? carBodyColor.hashCode() : 0);
        result = 31 * result + (memberKindId != null ? memberKindId.hashCode() : 0);
        result = 31 * result + (reservationTypeName != null ? reservationTypeName.hashCode() : 0);
        result = 31 * result + (coverId != null ? coverId.hashCode() : 0);
        result = 31 * result + (useMark != null ? useMark.hashCode() : 0);
        result = 31 * result + (departmentId != null ? departmentId.hashCode() : 0);
        return result;
    }
}
