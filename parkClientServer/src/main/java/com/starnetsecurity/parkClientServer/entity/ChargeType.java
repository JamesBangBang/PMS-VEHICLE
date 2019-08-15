package com.starnetsecurity.parkClientServer.entity;

import javax.persistence.*;

/**
 * Created by 宏炜 on 2017-11-15.
 */
@Entity
@Table(name = "charge_type", schema = "vams", catalog = "")
public class ChargeType {
    private long chargeTypeId;
    private String chargeType;
    private MemberKind memberKind;
    private FeeSet feeSubModeSamll;
    private FeeSet feeSubModeBig;
    private FeeSet feeSubModeMoto;
    private FeeSet feeSubModeOther;
    private String addTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "charge_type_id")
    public long getChargeTypeId() {
        return chargeTypeId;
    }

    public void setChargeTypeId(long chargeTypeId) {
        this.chargeTypeId = chargeTypeId;
    }

    @Basic
    @Column(name = "charge_type")
    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    @ManyToOne(targetEntity = MemberKind.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_member")
    public MemberKind getMemberKind() {
        return memberKind;
    }

    public void setMemberKind(MemberKind memberKind) {
        this.memberKind = memberKind;
    }

    @ManyToOne(targetEntity = FeeSet.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "fee_sub_mode_samll_id")
    public FeeSet getFeeSubModeSamll() {
        return feeSubModeSamll;
    }

    public void setFeeSubModeSamll(FeeSet feeSubModeSamll) {
        this.feeSubModeSamll = feeSubModeSamll;
    }

    @ManyToOne(targetEntity = FeeSet.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "fee_sub_mode_big_id")
    public FeeSet getFeeSubModeBig() {
        return feeSubModeBig;
    }

    public void setFeeSubModeBig(FeeSet feeSubModeBig) {
        this.feeSubModeBig = feeSubModeBig;
    }

    @ManyToOne(targetEntity = FeeSet.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "fee_sub_mode_moto_id")
    public FeeSet getFeeSubModeMoto() {
        return feeSubModeMoto;
    }

    public void setFeeSubModeMoto(FeeSet feeSubModeMoto) {
        this.feeSubModeMoto = feeSubModeMoto;
    }

    @ManyToOne(targetEntity = FeeSet.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "fee_sub_mode_other_id")
    public FeeSet getFeeSubModeOther() {
        return feeSubModeOther;
    }

    public void setFeeSubModeOther(FeeSet feeSubModeOther) {
        this.feeSubModeOther = feeSubModeOther;
    }

    @Basic
    @Column(name = "add_time")
    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChargeType that = (ChargeType) o;

        if (chargeTypeId != that.chargeTypeId) return false;
        if (chargeType != null ? !chargeType.equals(that.chargeType) : that.chargeType != null) return false;
        if (addTime != null ? !addTime.equals(that.addTime) : that.addTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (chargeTypeId ^ (chargeTypeId >>> 32));
        result = 31 * result + (chargeType != null ? chargeType.hashCode() : 0);
        result = 31 * result + (addTime != null ? addTime.hashCode() : 0);
        return result;
    }
}
