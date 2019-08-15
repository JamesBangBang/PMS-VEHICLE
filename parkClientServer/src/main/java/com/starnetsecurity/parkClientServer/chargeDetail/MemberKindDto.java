package com.starnetsecurity.parkClientServer.chargeDetail;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by 宏炜 on 2018-01-18.
 */
public class MemberKindDto {

    public FeeSetDetail feeSetDetail;
    public JSONObject memberKindInfo;

    public FeeSetDetail getFeeSetDetail() {
        return feeSetDetail;
    }

    public void setFeeSetDetail(FeeSetDetail feeSetDetail) {
        this.feeSetDetail = feeSetDetail;
    }

    public JSONObject getMemberKindInfo() {
        return memberKindInfo;
    }

    public void setMemberKindInfo(JSONObject memberKindInfo) {
        this.memberKindInfo = memberKindInfo;
    }
}
