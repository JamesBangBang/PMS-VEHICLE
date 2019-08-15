package com.starnetsecurity.parkClientServer.service;

import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.parkClientServer.chargeDetail.FeeSetDetail;
import com.starnetsecurity.parkClientServer.entity.AdminUser;
import com.starnetsecurity.parkClientServer.entity.CarparkInfo;
import com.starnetsecurity.parkClientServer.entity.MemberKind;

import java.util.List;
import java.util.Map;

public interface MemberKindService {
    Map getMemberKindInfo(String memberKindId);

    void updateMemberKindInfo(Map param,FeeSetDetail feeSetDetail);

    void addMemberKindInfo(Map param,FeeSetDetail feeSetDetail);

    void deleteKindInfo(String memberKindId);


}
