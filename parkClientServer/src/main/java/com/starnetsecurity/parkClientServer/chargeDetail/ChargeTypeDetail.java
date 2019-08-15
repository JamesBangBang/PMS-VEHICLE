package com.starnetsecurity.parkClientServer.chargeDetail;

/**
 * Created by JAMESBANG on 2018/11/20.
 */
public class ChargeTypeDetail {
    private long chargeTypeId;
    private String chargeType;
    private String memberKindId;
    private String feeSubModeSmallId;
    private String feeSubModeBigId;
    private String feeSubModeOtherId;
    private String addTime;

    public String getFeeSubModeBigId() {
        return feeSubModeBigId;
    }

    public void setFeeSubModeBigId(String feeSubModeBigId) {
        this.feeSubModeBigId = feeSubModeBigId;
    }

    public String getFeeSubModeOtherId() {
        return feeSubModeOtherId;
    }

    public void setFeeSubModeOtherId(String feeSubModeOtherId) {
        this.feeSubModeOtherId = feeSubModeOtherId;
    }

    public long getChargeTypeId() {
        return chargeTypeId;
    }

    public void setChargeTypeId(long chargeTypeId) {
        this.chargeTypeId = chargeTypeId;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getMemberKindId() {
        return memberKindId;
    }

    public void setMemberKindId(String memberKindId) {
        this.memberKindId = memberKindId;
    }

    public String getFeeSubModeSmallId() {
        return feeSubModeSmallId;
    }

    public void setFeeSubModeSmallId(String feeSubModeSmallId) {
        this.feeSubModeSmallId = feeSubModeSmallId;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
}
