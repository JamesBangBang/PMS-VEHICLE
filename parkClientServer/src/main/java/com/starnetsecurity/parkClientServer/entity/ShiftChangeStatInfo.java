package com.starnetsecurity.parkClientServer.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2017-11-15.
 */
@Entity
@Table(name = "shift_change_stat_info", schema = "vams", catalog = "")
public class ShiftChangeStatInfo {
    private String shiftChangeId;
    private String shiftGiver;
    private Timestamp shiftGiverWorkTime;
    private String shiftTaker;
    private Timestamp shiftChangeTime;
    private Double receivableAmount;
    private Double derateAmount;
    private Double realAmount;
    private String operationSource;
    private Timestamp addTime;
    private String abnormalRemark;
    private Integer cardGetCount;
    private Integer cardSendCount;
    private Integer cardShiftInCount;
    private Integer cardShiftOutCount;
    private String boothName;
    private String departmentId;
    private Integer useMark;
    private String carparkId;

    @Id
    @GenericGenerator(name="systemUUID",strategy="uuid")
    @GeneratedValue(generator="systemUUID")
    @Column(name = "shift_change_id")
    public String getShiftChangeId() {
        return shiftChangeId;
    }

    public void setShiftChangeId(String shiftChangeId) {
        this.shiftChangeId = shiftChangeId;
    }

    @Basic
    @Column(name = "shift_giver")
    public String getShiftGiver() {
        return shiftGiver;
    }

    public void setShiftGiver(String shiftGiver) {
        this.shiftGiver = shiftGiver;
    }

    @Basic
    @Column(name = "shift_giver_work_time")
    public Timestamp getShiftGiverWorkTime() {
        return shiftGiverWorkTime;
    }

    public void setShiftGiverWorkTime(Timestamp shiftGiverWorkTime) {
        this.shiftGiverWorkTime = shiftGiverWorkTime;
    }

    @Basic
    @Column(name = "shift_taker")
    public String getShiftTaker() {
        return shiftTaker;
    }

    public void setShiftTaker(String shiftTaker) {
        this.shiftTaker = shiftTaker;
    }

    @Basic
    @Column(name = "shift_change_time")
    public Timestamp getShiftChangeTime() {
        return shiftChangeTime;
    }

    public void setShiftChangeTime(Timestamp shiftChangeTime) {
        this.shiftChangeTime = shiftChangeTime;
    }

    @Basic
    @Column(name = "receivable_amount")
    public Double getReceivableAmount() {
        return receivableAmount;
    }

    public void setReceivableAmount(Double receivableAmount) {
        this.receivableAmount = receivableAmount;
    }

    @Basic
    @Column(name = "derate_amount")
    public Double getDerateAmount() {
        return derateAmount;
    }

    public void setDerateAmount(Double derateAmount) {
        this.derateAmount = derateAmount;
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
    @Column(name = "abnormal_remark")
    public String getAbnormalRemark() {
        return abnormalRemark;
    }

    public void setAbnormalRemark(String abnormalRemark) {
        this.abnormalRemark = abnormalRemark;
    }

    @Basic
    @Column(name = "card_get_count")
    public Integer getCardGetCount() {
        return cardGetCount;
    }

    public void setCardGetCount(Integer cardGetCount) {
        this.cardGetCount = cardGetCount;
    }

    @Basic
    @Column(name = "card_send_count")
    public Integer getCardSendCount() {
        return cardSendCount;
    }

    public void setCardSendCount(Integer cardSendCount) {
        this.cardSendCount = cardSendCount;
    }

    @Basic
    @Column(name = "card_shift_in_count")
    public Integer getCardShiftInCount() {
        return cardShiftInCount;
    }

    public void setCardShiftInCount(Integer cardShiftInCount) {
        this.cardShiftInCount = cardShiftInCount;
    }

    @Basic
    @Column(name = "card_shift_out_count")
    public Integer getCardShiftOutCount() {
        return cardShiftOutCount;
    }

    public void setCardShiftOutCount(Integer cardShiftOutCount) {
        this.cardShiftOutCount = cardShiftOutCount;
    }

    @Basic
    @Column(name = "booth_name")
    public String getBoothName() {
        return boothName;
    }

    public void setBoothName(String boothName) {
        this.boothName = boothName;
    }

    @Basic
    @Column(name = "department_id")
    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
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
    @Column(name = "carpark_id")
    public String getCarparkId() {
        return carparkId;
    }

    public void setCarparkId(String carparkId) {
        this.carparkId = carparkId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShiftChangeStatInfo that = (ShiftChangeStatInfo) o;

        if (shiftChangeId != null ? !shiftChangeId.equals(that.shiftChangeId) : that.shiftChangeId != null)
            return false;
        if (shiftGiver != null ? !shiftGiver.equals(that.shiftGiver) : that.shiftGiver != null) return false;
        if (shiftGiverWorkTime != null ? !shiftGiverWorkTime.equals(that.shiftGiverWorkTime) : that.shiftGiverWorkTime != null)
            return false;
        if (shiftTaker != null ? !shiftTaker.equals(that.shiftTaker) : that.shiftTaker != null) return false;
        if (shiftChangeTime != null ? !shiftChangeTime.equals(that.shiftChangeTime) : that.shiftChangeTime != null)
            return false;
        if (receivableAmount != null ? !receivableAmount.equals(that.receivableAmount) : that.receivableAmount != null)
            return false;
        if (derateAmount != null ? !derateAmount.equals(that.derateAmount) : that.derateAmount != null) return false;
        if (realAmount != null ? !realAmount.equals(that.realAmount) : that.realAmount != null) return false;
        if (operationSource != null ? !operationSource.equals(that.operationSource) : that.operationSource != null)
            return false;
        if (addTime != null ? !addTime.equals(that.addTime) : that.addTime != null) return false;
        if (abnormalRemark != null ? !abnormalRemark.equals(that.abnormalRemark) : that.abnormalRemark != null)
            return false;
        if (cardGetCount != null ? !cardGetCount.equals(that.cardGetCount) : that.cardGetCount != null) return false;
        if (cardSendCount != null ? !cardSendCount.equals(that.cardSendCount) : that.cardSendCount != null)
            return false;
        if (cardShiftInCount != null ? !cardShiftInCount.equals(that.cardShiftInCount) : that.cardShiftInCount != null)
            return false;
        if (cardShiftOutCount != null ? !cardShiftOutCount.equals(that.cardShiftOutCount) : that.cardShiftOutCount != null)
            return false;
        if (boothName != null ? !boothName.equals(that.boothName) : that.boothName != null) return false;
        if (departmentId != null ? !departmentId.equals(that.departmentId) : that.departmentId != null) return false;
        if (useMark != null ? !useMark.equals(that.useMark) : that.useMark != null) return false;
        if (carparkId != null ? !carparkId.equals(that.carparkId) : that.carparkId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = shiftChangeId != null ? shiftChangeId.hashCode() : 0;
        result = 31 * result + (shiftGiver != null ? shiftGiver.hashCode() : 0);
        result = 31 * result + (shiftGiverWorkTime != null ? shiftGiverWorkTime.hashCode() : 0);
        result = 31 * result + (shiftTaker != null ? shiftTaker.hashCode() : 0);
        result = 31 * result + (shiftChangeTime != null ? shiftChangeTime.hashCode() : 0);
        result = 31 * result + (receivableAmount != null ? receivableAmount.hashCode() : 0);
        result = 31 * result + (derateAmount != null ? derateAmount.hashCode() : 0);
        result = 31 * result + (realAmount != null ? realAmount.hashCode() : 0);
        result = 31 * result + (operationSource != null ? operationSource.hashCode() : 0);
        result = 31 * result + (addTime != null ? addTime.hashCode() : 0);
        result = 31 * result + (abnormalRemark != null ? abnormalRemark.hashCode() : 0);
        result = 31 * result + (cardGetCount != null ? cardGetCount.hashCode() : 0);
        result = 31 * result + (cardSendCount != null ? cardSendCount.hashCode() : 0);
        result = 31 * result + (cardShiftInCount != null ? cardShiftInCount.hashCode() : 0);
        result = 31 * result + (cardShiftOutCount != null ? cardShiftOutCount.hashCode() : 0);
        result = 31 * result + (boothName != null ? boothName.hashCode() : 0);
        result = 31 * result + (departmentId != null ? departmentId.hashCode() : 0);
        result = 31 * result + (useMark != null ? useMark.hashCode() : 0);
        result = 31 * result + (carparkId != null ? carparkId.hashCode() : 0);
        return result;
    }
}
