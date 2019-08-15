package com.starnetsecurity.starParkService.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2017-10-12.
 */
@Entity
@Table(name = "member_wallet", schema = "vamsserver", catalog = "")
public class MemberWallet {
    private String id;
    private String menberNo;
    private String parkingLotId;
    private String carPark;
    private String menberTypeId;
    private Integer packKindId;
    private Integer packChlidKind;
    private Double surplusAmount;
    private Integer surplusNumber;
    private Timestamp effectiveStartTime;
    private Timestamp effectiveEndTime;
    private Double couponAmout;
    private Integer couponHourLong;
    private Double couponDiscount;
    private String synchronizeIpcList;
    private Timestamp addTime;
    private String validMenberCount;
    private Integer useMark;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "menber_no")
    public String getMenberNo() {
        return menberNo;
    }

    public void setMenberNo(String menberNo) {
        this.menberNo = menberNo;
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
    @Column(name = "car_park")
    public String getCarPark() {
        return carPark;
    }

    public void setCarPark(String carPark) {
        this.carPark = carPark;
    }

    @Basic
    @Column(name = "menber_type_id")
    public String getMenberTypeId() {
        return menberTypeId;
    }

    public void setMenberTypeId(String menberTypeId) {
        this.menberTypeId = menberTypeId;
    }

    @Basic
    @Column(name = "pack_kind_id")
    public Integer getPackKindId() {
        return packKindId;
    }

    public void setPackKindId(Integer packKindId) {
        this.packKindId = packKindId;
    }

    @Basic
    @Column(name = "pack_chlid_kind")
    public Integer getPackChlidKind() {
        return packChlidKind;
    }

    public void setPackChlidKind(Integer packChlidKind) {
        this.packChlidKind = packChlidKind;
    }

    @Basic
    @Column(name = "surplus_amount")
    public Double getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(Double surplusAmount) {
        this.surplusAmount = surplusAmount;
    }

    @Basic
    @Column(name = "surplus_number")
    public Integer getSurplusNumber() {
        return surplusNumber;
    }

    public void setSurplusNumber(Integer surplusNumber) {
        this.surplusNumber = surplusNumber;
    }

    @Basic
    @Column(name = "effective_start_time")
    public Timestamp getEffectiveStartTime() {
        return effectiveStartTime;
    }

    public void setEffectiveStartTime(Timestamp effectiveStartTime) {
        this.effectiveStartTime = effectiveStartTime;
    }

    @Basic
    @Column(name = "effective_end_time")
    public Timestamp getEffectiveEndTime() {
        return effectiveEndTime;
    }

    public void setEffectiveEndTime(Timestamp effectiveEndTime) {
        this.effectiveEndTime = effectiveEndTime;
    }

    @Basic
    @Column(name = "coupon_amout")
    public Double getCouponAmout() {
        return couponAmout;
    }

    public void setCouponAmout(Double couponAmout) {
        this.couponAmout = couponAmout;
    }

    @Basic
    @Column(name = "coupon_hour_long")
    public Integer getCouponHourLong() {
        return couponHourLong;
    }

    public void setCouponHourLong(Integer couponHourLong) {
        this.couponHourLong = couponHourLong;
    }

    @Basic
    @Column(name = "coupon_discount")
    public Double getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(Double couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    @Basic
    @Column(name = "synchronize_ipc_list")
    public String getSynchronizeIpcList() {
        return synchronizeIpcList;
    }

    public void setSynchronizeIpcList(String synchronizeIpcList) {
        this.synchronizeIpcList = synchronizeIpcList;
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
    @Column(name = "valid_menber_count")
    public String getValidMenberCount() {
        return validMenberCount;
    }

    public void setValidMenberCount(String validMenberCount) {
        this.validMenberCount = validMenberCount;
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

        MemberWallet that = (MemberWallet) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (menberNo != null ? !menberNo.equals(that.menberNo) : that.menberNo != null) return false;
        if (parkingLotId != null ? !parkingLotId.equals(that.parkingLotId) : that.parkingLotId != null) return false;
        if (carPark != null ? !carPark.equals(that.carPark) : that.carPark != null) return false;
        if (menberTypeId != null ? !menberTypeId.equals(that.menberTypeId) : that.menberTypeId != null) return false;
        if (packKindId != null ? !packKindId.equals(that.packKindId) : that.packKindId != null) return false;
        if (packChlidKind != null ? !packChlidKind.equals(that.packChlidKind) : that.packChlidKind != null)
            return false;
        if (surplusAmount != null ? !surplusAmount.equals(that.surplusAmount) : that.surplusAmount != null)
            return false;
        if (surplusNumber != null ? !surplusNumber.equals(that.surplusNumber) : that.surplusNumber != null)
            return false;
        if (effectiveStartTime != null ? !effectiveStartTime.equals(that.effectiveStartTime) : that.effectiveStartTime != null)
            return false;
        if (effectiveEndTime != null ? !effectiveEndTime.equals(that.effectiveEndTime) : that.effectiveEndTime != null)
            return false;
        if (couponAmout != null ? !couponAmout.equals(that.couponAmout) : that.couponAmout != null) return false;
        if (couponHourLong != null ? !couponHourLong.equals(that.couponHourLong) : that.couponHourLong != null)
            return false;
        if (couponDiscount != null ? !couponDiscount.equals(that.couponDiscount) : that.couponDiscount != null)
            return false;
        if (synchronizeIpcList != null ? !synchronizeIpcList.equals(that.synchronizeIpcList) : that.synchronizeIpcList != null)
            return false;
        if (addTime != null ? !addTime.equals(that.addTime) : that.addTime != null) return false;
        if (validMenberCount != null ? !validMenberCount.equals(that.validMenberCount) : that.validMenberCount != null)
            return false;
        if (useMark != null ? !useMark.equals(that.useMark) : that.useMark != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (menberNo != null ? menberNo.hashCode() : 0);
        result = 31 * result + (parkingLotId != null ? parkingLotId.hashCode() : 0);
        result = 31 * result + (carPark != null ? carPark.hashCode() : 0);
        result = 31 * result + (menberTypeId != null ? menberTypeId.hashCode() : 0);
        result = 31 * result + (packKindId != null ? packKindId.hashCode() : 0);
        result = 31 * result + (packChlidKind != null ? packChlidKind.hashCode() : 0);
        result = 31 * result + (surplusAmount != null ? surplusAmount.hashCode() : 0);
        result = 31 * result + (surplusNumber != null ? surplusNumber.hashCode() : 0);
        result = 31 * result + (effectiveStartTime != null ? effectiveStartTime.hashCode() : 0);
        result = 31 * result + (effectiveEndTime != null ? effectiveEndTime.hashCode() : 0);
        result = 31 * result + (couponAmout != null ? couponAmout.hashCode() : 0);
        result = 31 * result + (couponHourLong != null ? couponHourLong.hashCode() : 0);
        result = 31 * result + (couponDiscount != null ? couponDiscount.hashCode() : 0);
        result = 31 * result + (synchronizeIpcList != null ? synchronizeIpcList.hashCode() : 0);
        result = 31 * result + (addTime != null ? addTime.hashCode() : 0);
        result = 31 * result + (validMenberCount != null ? validMenberCount.hashCode() : 0);
        result = 31 * result + (useMark != null ? useMark.hashCode() : 0);
        return result;
    }
}
