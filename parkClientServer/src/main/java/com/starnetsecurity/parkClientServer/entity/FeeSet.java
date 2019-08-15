package com.starnetsecurity.parkClientServer.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by 宏炜 on 2017-11-17.
 */
@Entity
@Table(name = "fee_set", schema = "vams", catalog = "")
public class FeeSet {
    private String feeSubId;
    private Integer freeDuration;
    private Integer firstTime;
    private Double firstTimeFee;
    private Double secondTimeAfterGapFee;
    private Double addFee;
    private Double feeLimit;
    private Integer freeDurationEx;
    private Integer firstTimeEx;
    private Double firstTimeFeeEx;
    private Double secondTimeAfterGapFeeEx;
    private Double addFeeEx;
    private Double feeLimitEx;
    private String isFeeSection;
    private String feeStartTime;
    private String feeEndTime;
    private Double totalFeeLimit;
    private Set<ChargeType> feeSubModeSamlls;
    private Set<ChargeType> feeSubModeBigs;
    private Set<ChargeType> feeSubModeMotos;
    private Set<ChargeType> feeSubModeOthers;

    @Id
    @GenericGenerator(name="systemUUID",strategy="uuid")
    @GeneratedValue(generator="systemUUID")
    @Column(name = "fee_sub_id")
    public String getFeeSubId() {
        return feeSubId;
    }

    public void setFeeSubId(String feeSubId) {
        this.feeSubId = feeSubId;
    }

    @Basic
    @Column(name = "free_duration")
    public Integer getFreeDuration() {
        return freeDuration;
    }

    public void setFreeDuration(Integer freeDuration) {
        this.freeDuration = freeDuration;
    }

    @Basic
    @Column(name = "first_time")
    public Integer getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Integer firstTime) {
        this.firstTime = firstTime;
    }

    @Basic
    @Column(name = "first_time_fee")
    public Double getFirstTimeFee() {
        return firstTimeFee;
    }

    public void setFirstTimeFee(Double firstTimeFee) {
        this.firstTimeFee = firstTimeFee;
    }

    @Basic
    @Column(name = "second_time_after_gap_fee")
    public Double getSecondTimeAfterGapFee() {
        return secondTimeAfterGapFee;
    }

    public void setSecondTimeAfterGapFee(Double secondTimeAfterGapFee) {
        this.secondTimeAfterGapFee = secondTimeAfterGapFee;
    }

    @Basic
    @Column(name = "add_fee")
    public Double getAddFee() {
        return addFee;
    }

    public void setAddFee(Double addFee) {
        this.addFee = addFee;
    }

    @Basic
    @Column(name = "fee_limit")
    public Double getFeeLimit() {
        return feeLimit;
    }

    public void setFeeLimit(Double feeLimit) {
        this.feeLimit = feeLimit;
    }

    @Basic
    @Column(name = "free_durationEx")
    public Integer getFreeDurationEx() {
        return freeDurationEx;
    }

    public void setFreeDurationEx(Integer freeDurationEx) {
        this.freeDurationEx = freeDurationEx;
    }

    @Basic
    @Column(name = "first_timeEx")
    public Integer getFirstTimeEx() {
        return firstTimeEx;
    }

    public void setFirstTimeEx(Integer firstTimeEx) {
        this.firstTimeEx = firstTimeEx;
    }

    @Basic
    @Column(name = "first_time_feeEx")
    public Double getFirstTimeFeeEx() {
        return firstTimeFeeEx;
    }

    public void setFirstTimeFeeEx(Double firstTimeFeeEx) {
        this.firstTimeFeeEx = firstTimeFeeEx;
    }

    @Basic
    @Column(name = "second_time_after_gap_feeEx")
    public Double getSecondTimeAfterGapFeeEx() {
        return secondTimeAfterGapFeeEx;
    }

    public void setSecondTimeAfterGapFeeEx(Double secondTimeAfterGapFeeEx) {
        this.secondTimeAfterGapFeeEx = secondTimeAfterGapFeeEx;
    }

    @Basic
    @Column(name = "add_feeEx")
    public Double getAddFeeEx() {
        return addFeeEx;
    }

    public void setAddFeeEx(Double addFeeEx) {
        this.addFeeEx = addFeeEx;
    }

    @Basic
    @Column(name = "fee_limitEx")
    public Double getFeeLimitEx() {
        return feeLimitEx;
    }

    public void setFeeLimitEx(Double feeLimitEx) {
        this.feeLimitEx = feeLimitEx;
    }

    @Basic
    @Column(name = "is_fee_Section")
    public String getIsFeeSection() {
        return isFeeSection;
    }

    public void setIsFeeSection(String isFeeSection) {
        this.isFeeSection = isFeeSection;
    }

    @Basic
    @Column(name = "fee_start_time")
    public String getFeeStartTime() {
        return feeStartTime;
    }

    public void setFeeStartTime(String feeStartTime) {
        this.feeStartTime = feeStartTime;
    }

    @Basic
    @Column(name = "fee_end_time")
    public String getFeeEndTime() {
        return feeEndTime;
    }

    public void setFeeEndTime(String feeEndTime) {
        this.feeEndTime = feeEndTime;
    }

    @Basic
    @Column(name = "total_fee_limit")
    public Double getTotalFeeLimit() {
        return totalFeeLimit;
    }

    public void setTotalFeeLimit(Double totalFeeLimit) {
        this.totalFeeLimit = totalFeeLimit;
    }

    @OneToMany(mappedBy = "feeSubModeSamll",fetch = FetchType.LAZY)
    public Set<ChargeType> getFeeSubModeSamlls() {
        return feeSubModeSamlls;
    }

    public void setFeeSubModeSamlls(Set<ChargeType> feeSubModeSamlls) {
        this.feeSubModeSamlls = feeSubModeSamlls;
    }

    @OneToMany(mappedBy = "feeSubModeBig",fetch = FetchType.LAZY)
    public Set<ChargeType> getFeeSubModeBigs() {
        return feeSubModeBigs;
    }

    public void setFeeSubModeBigs(Set<ChargeType> feeSubModeBigs) {
        this.feeSubModeBigs = feeSubModeBigs;
    }

    @OneToMany(mappedBy = "feeSubModeMoto",fetch = FetchType.LAZY)
    public Set<ChargeType> getFeeSubModeMotos() {
        return feeSubModeMotos;
    }

    public void setFeeSubModeMotos(Set<ChargeType> feeSubModeMotos) {
        this.feeSubModeMotos = feeSubModeMotos;
    }

    @OneToMany(mappedBy = "feeSubModeOther",fetch = FetchType.LAZY)
    public Set<ChargeType> getFeeSubModeOthers() {
        return feeSubModeOthers;
    }

    public void setFeeSubModeOthers(Set<ChargeType> feeSubModeOthers) {
        this.feeSubModeOthers = feeSubModeOthers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeeSet feeSet = (FeeSet) o;

        if (feeSubId != null ? !feeSubId.equals(feeSet.feeSubId) : feeSet.feeSubId != null) return false;
        if (freeDuration != null ? !freeDuration.equals(feeSet.freeDuration) : feeSet.freeDuration != null)
            return false;
        if (firstTime != null ? !firstTime.equals(feeSet.firstTime) : feeSet.firstTime != null) return false;
        if (firstTimeFee != null ? !firstTimeFee.equals(feeSet.firstTimeFee) : feeSet.firstTimeFee != null)
            return false;
        if (secondTimeAfterGapFee != null ? !secondTimeAfterGapFee.equals(feeSet.secondTimeAfterGapFee) : feeSet.secondTimeAfterGapFee != null)
            return false;
        if (addFee != null ? !addFee.equals(feeSet.addFee) : feeSet.addFee != null) return false;
        if (feeLimit != null ? !feeLimit.equals(feeSet.feeLimit) : feeSet.feeLimit != null) return false;
        if (freeDurationEx != null ? !freeDurationEx.equals(feeSet.freeDurationEx) : feeSet.freeDurationEx != null)
            return false;
        if (firstTimeEx != null ? !firstTimeEx.equals(feeSet.firstTimeEx) : feeSet.firstTimeEx != null) return false;
        if (firstTimeFeeEx != null ? !firstTimeFeeEx.equals(feeSet.firstTimeFeeEx) : feeSet.firstTimeFeeEx != null)
            return false;
        if (secondTimeAfterGapFeeEx != null ? !secondTimeAfterGapFeeEx.equals(feeSet.secondTimeAfterGapFeeEx) : feeSet.secondTimeAfterGapFeeEx != null)
            return false;
        if (addFeeEx != null ? !addFeeEx.equals(feeSet.addFeeEx) : feeSet.addFeeEx != null) return false;
        if (feeLimitEx != null ? !feeLimitEx.equals(feeSet.feeLimitEx) : feeSet.feeLimitEx != null) return false;
        if (isFeeSection != null ? !isFeeSection.equals(feeSet.isFeeSection) : feeSet.isFeeSection != null)
            return false;
        if (feeStartTime != null ? !feeStartTime.equals(feeSet.feeStartTime) : feeSet.feeStartTime != null)
            return false;
        if (feeEndTime != null ? !feeEndTime.equals(feeSet.feeEndTime) : feeSet.feeEndTime != null) return false;
        if (totalFeeLimit != null ? !totalFeeLimit.equals(feeSet.totalFeeLimit) : feeSet.totalFeeLimit != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = feeSubId != null ? feeSubId.hashCode() : 0;
        result = 31 * result + (freeDuration != null ? freeDuration.hashCode() : 0);
        result = 31 * result + (firstTime != null ? firstTime.hashCode() : 0);
        result = 31 * result + (firstTimeFee != null ? firstTimeFee.hashCode() : 0);
        result = 31 * result + (secondTimeAfterGapFee != null ? secondTimeAfterGapFee.hashCode() : 0);
        result = 31 * result + (addFee != null ? addFee.hashCode() : 0);
        result = 31 * result + (feeLimit != null ? feeLimit.hashCode() : 0);
        result = 31 * result + (freeDurationEx != null ? freeDurationEx.hashCode() : 0);
        result = 31 * result + (firstTimeEx != null ? firstTimeEx.hashCode() : 0);
        result = 31 * result + (firstTimeFeeEx != null ? firstTimeFeeEx.hashCode() : 0);
        result = 31 * result + (secondTimeAfterGapFeeEx != null ? secondTimeAfterGapFeeEx.hashCode() : 0);
        result = 31 * result + (addFeeEx != null ? addFeeEx.hashCode() : 0);
        result = 31 * result + (feeLimitEx != null ? feeLimitEx.hashCode() : 0);
        result = 31 * result + (isFeeSection != null ? isFeeSection.hashCode() : 0);
        result = 31 * result + (feeStartTime != null ? feeStartTime.hashCode() : 0);
        result = 31 * result + (feeEndTime != null ? feeEndTime.hashCode() : 0);
        result = 31 * result + (totalFeeLimit != null ? totalFeeLimit.hashCode() : 0);
        return result;
    }
}
