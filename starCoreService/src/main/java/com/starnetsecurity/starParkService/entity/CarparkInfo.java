package com.starnetsecurity.starParkService.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2017-10-12.
 */
@Entity
@Table(name = "carpark_info", schema = "vamsserver", catalog = "")
public class CarparkInfo {
    private String carparkId;
    private String carparkName;
    private String carparkType;
    private Integer totalCarSpace;
    private Integer availableCarSpace;
    private String ownCarparkNo;
    private String operationSource;
    private Timestamp addTime;
    private String passTimeWhenBig;
    private Integer carparkNo;
    private String chargeType;
    private Integer checkCount;
    private Timestamp checkTime;
    private Integer ifIncludeCaculate;
    private String isTestRunning;
    private String isClose;
    private Integer criticalValue;
    private String isMulticar;
    private String isAutoOpen;
    private String isOverdueAutoOpen;
    private String closeType;
    private Integer ledMemberCriticalValue;
    private Integer useMark;
    private String departmentId;

    @Id
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
    @Column(name = "carpark_type")
    public String getCarparkType() {
        return carparkType;
    }

    public void setCarparkType(String carparkType) {
        this.carparkType = carparkType;
    }

    @Basic
    @Column(name = "total_car_space")
    public Integer getTotalCarSpace() {
        return totalCarSpace;
    }

    public void setTotalCarSpace(Integer totalCarSpace) {
        this.totalCarSpace = totalCarSpace;
    }

    @Basic
    @Column(name = "available_car_space")
    public Integer getAvailableCarSpace() {
        return availableCarSpace;
    }

    public void setAvailableCarSpace(Integer availableCarSpace) {
        this.availableCarSpace = availableCarSpace;
    }

    @Basic
    @Column(name = "own_carpark_no")
    public String getOwnCarparkNo() {
        return ownCarparkNo;
    }

    public void setOwnCarparkNo(String ownCarparkNo) {
        this.ownCarparkNo = ownCarparkNo;
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
    @Column(name = "pass_time_when_big")
    public String getPassTimeWhenBig() {
        return passTimeWhenBig;
    }

    public void setPassTimeWhenBig(String passTimeWhenBig) {
        this.passTimeWhenBig = passTimeWhenBig;
    }

    @Basic
    @Column(name = "carpark_no")
    public Integer getCarparkNo() {
        return carparkNo;
    }

    public void setCarparkNo(Integer carparkNo) {
        this.carparkNo = carparkNo;
    }

    @Basic
    @Column(name = "charge_type")
    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    @Basic
    @Column(name = "check_count")
    public Integer getCheckCount() {
        return checkCount;
    }

    public void setCheckCount(Integer checkCount) {
        this.checkCount = checkCount;
    }

    @Basic
    @Column(name = "check_time")
    public Timestamp getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Timestamp checkTime) {
        this.checkTime = checkTime;
    }

    @Basic
    @Column(name = "if_include_caculate")
    public Integer getIfIncludeCaculate() {
        return ifIncludeCaculate;
    }

    public void setIfIncludeCaculate(Integer ifIncludeCaculate) {
        this.ifIncludeCaculate = ifIncludeCaculate;
    }

    @Basic
    @Column(name = "is_test_running")
    public String getIsTestRunning() {
        return isTestRunning;
    }

    public void setIsTestRunning(String isTestRunning) {
        this.isTestRunning = isTestRunning;
    }

    @Basic
    @Column(name = "is_close")
    public String getIsClose() {
        return isClose;
    }

    public void setIsClose(String isClose) {
        this.isClose = isClose;
    }

    @Basic
    @Column(name = "critical_value")
    public Integer getCriticalValue() {
        return criticalValue;
    }

    public void setCriticalValue(Integer criticalValue) {
        this.criticalValue = criticalValue;
    }

    @Basic
    @Column(name = "is_multicar")
    public String getIsMulticar() {
        return isMulticar;
    }

    public void setIsMulticar(String isMulticar) {
        this.isMulticar = isMulticar;
    }

    @Basic
    @Column(name = "is_auto_open")
    public String getIsAutoOpen() {
        return isAutoOpen;
    }

    public void setIsAutoOpen(String isAutoOpen) {
        this.isAutoOpen = isAutoOpen;
    }

    @Basic
    @Column(name = "is_overdue_auto_open")
    public String getIsOverdueAutoOpen() {
        return isOverdueAutoOpen;
    }

    public void setIsOverdueAutoOpen(String isOverdueAutoOpen) {
        this.isOverdueAutoOpen = isOverdueAutoOpen;
    }

    @Basic
    @Column(name = "close_type")
    public String getCloseType() {
        return closeType;
    }

    public void setCloseType(String closeType) {
        this.closeType = closeType;
    }

    @Basic
    @Column(name = "led_member_critical_value")
    public Integer getLedMemberCriticalValue() {
        return ledMemberCriticalValue;
    }

    public void setLedMemberCriticalValue(Integer ledMemberCriticalValue) {
        this.ledMemberCriticalValue = ledMemberCriticalValue;
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

        CarparkInfo that = (CarparkInfo) o;

        if (carparkId != null ? !carparkId.equals(that.carparkId) : that.carparkId != null) return false;
        if (carparkName != null ? !carparkName.equals(that.carparkName) : that.carparkName != null) return false;
        if (carparkType != null ? !carparkType.equals(that.carparkType) : that.carparkType != null) return false;
        if (totalCarSpace != null ? !totalCarSpace.equals(that.totalCarSpace) : that.totalCarSpace != null)
            return false;
        if (availableCarSpace != null ? !availableCarSpace.equals(that.availableCarSpace) : that.availableCarSpace != null)
            return false;
        if (ownCarparkNo != null ? !ownCarparkNo.equals(that.ownCarparkNo) : that.ownCarparkNo != null) return false;
        if (operationSource != null ? !operationSource.equals(that.operationSource) : that.operationSource != null)
            return false;
        if (addTime != null ? !addTime.equals(that.addTime) : that.addTime != null) return false;
        if (passTimeWhenBig != null ? !passTimeWhenBig.equals(that.passTimeWhenBig) : that.passTimeWhenBig != null)
            return false;
        if (carparkNo != null ? !carparkNo.equals(that.carparkNo) : that.carparkNo != null) return false;
        if (chargeType != null ? !chargeType.equals(that.chargeType) : that.chargeType != null) return false;
        if (checkCount != null ? !checkCount.equals(that.checkCount) : that.checkCount != null) return false;
        if (checkTime != null ? !checkTime.equals(that.checkTime) : that.checkTime != null) return false;
        if (ifIncludeCaculate != null ? !ifIncludeCaculate.equals(that.ifIncludeCaculate) : that.ifIncludeCaculate != null)
            return false;
        if (isTestRunning != null ? !isTestRunning.equals(that.isTestRunning) : that.isTestRunning != null)
            return false;
        if (isClose != null ? !isClose.equals(that.isClose) : that.isClose != null) return false;
        if (criticalValue != null ? !criticalValue.equals(that.criticalValue) : that.criticalValue != null)
            return false;
        if (isMulticar != null ? !isMulticar.equals(that.isMulticar) : that.isMulticar != null) return false;
        if (isAutoOpen != null ? !isAutoOpen.equals(that.isAutoOpen) : that.isAutoOpen != null) return false;
        if (isOverdueAutoOpen != null ? !isOverdueAutoOpen.equals(that.isOverdueAutoOpen) : that.isOverdueAutoOpen != null)
            return false;
        if (closeType != null ? !closeType.equals(that.closeType) : that.closeType != null) return false;
        if (ledMemberCriticalValue != null ? !ledMemberCriticalValue.equals(that.ledMemberCriticalValue) : that.ledMemberCriticalValue != null)
            return false;
        if (useMark != null ? !useMark.equals(that.useMark) : that.useMark != null) return false;
        if (departmentId != null ? !departmentId.equals(that.departmentId) : that.departmentId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = carparkId != null ? carparkId.hashCode() : 0;
        result = 31 * result + (carparkName != null ? carparkName.hashCode() : 0);
        result = 31 * result + (carparkType != null ? carparkType.hashCode() : 0);
        result = 31 * result + (totalCarSpace != null ? totalCarSpace.hashCode() : 0);
        result = 31 * result + (availableCarSpace != null ? availableCarSpace.hashCode() : 0);
        result = 31 * result + (ownCarparkNo != null ? ownCarparkNo.hashCode() : 0);
        result = 31 * result + (operationSource != null ? operationSource.hashCode() : 0);
        result = 31 * result + (addTime != null ? addTime.hashCode() : 0);
        result = 31 * result + (passTimeWhenBig != null ? passTimeWhenBig.hashCode() : 0);
        result = 31 * result + (carparkNo != null ? carparkNo.hashCode() : 0);
        result = 31 * result + (chargeType != null ? chargeType.hashCode() : 0);
        result = 31 * result + (checkCount != null ? checkCount.hashCode() : 0);
        result = 31 * result + (checkTime != null ? checkTime.hashCode() : 0);
        result = 31 * result + (ifIncludeCaculate != null ? ifIncludeCaculate.hashCode() : 0);
        result = 31 * result + (isTestRunning != null ? isTestRunning.hashCode() : 0);
        result = 31 * result + (isClose != null ? isClose.hashCode() : 0);
        result = 31 * result + (criticalValue != null ? criticalValue.hashCode() : 0);
        result = 31 * result + (isMulticar != null ? isMulticar.hashCode() : 0);
        result = 31 * result + (isAutoOpen != null ? isAutoOpen.hashCode() : 0);
        result = 31 * result + (isOverdueAutoOpen != null ? isOverdueAutoOpen.hashCode() : 0);
        result = 31 * result + (closeType != null ? closeType.hashCode() : 0);
        result = 31 * result + (ledMemberCriticalValue != null ? ledMemberCriticalValue.hashCode() : 0);
        result = 31 * result + (useMark != null ? useMark.hashCode() : 0);
        result = 31 * result + (departmentId != null ? departmentId.hashCode() : 0);
        return result;
    }
}
