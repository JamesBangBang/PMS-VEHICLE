package com.starnetsecurity.parkClientServer.service;

import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.parkClientServer.chargeDetail.ChargeTypeDetail;
import com.starnetsecurity.parkClientServer.chargeDetail.FeeSetDetail;
import com.starnetsecurity.parkClientServer.entity.*;

/**
 * Created by JAMESBANG on 2018/11/18.
 */
public interface UploadDataToCloudService {
    /**
     * 上传车场信息到云服务器
     * @param carparkInfo
     */
    void uploadParkInfoToServer(CarparkInfo carparkInfo);

    /**
     * 上传车道信息到云服务器
     * @param inOutCarRoadInfo
     */
    void uploadRoadInfoToServer(InOutCarRoadInfo inOutCarRoadInfo);

    /**
     * 上传操作员信息到云服务器
     * @param operator
     */
    void uploadOperatorInfoToServer(Operator operator);

    /**
     * 上传岗亭信息到云服务器
     * @param postComputerManage
     */
    void uploadPostInfoToServer(PostComputerManage postComputerManage);

    /**
     * 上传收费类型信息到云服务器
     * @param memberKind
     * @param chargeTypeDetail
     * @param feeSetDetail
     */
    void uploadMemberKindInfoToServer(MemberKind memberKind, ChargeTypeDetail chargeTypeDetail, FeeSetDetail feeSetDetail);

    /**
     * 上传白名单信息到云服务器
     * @param memberWallet
     * @param orderPrechargeRecord
     * @param orderTransaction
     */
    void uploadMemberWalletInfoToServer(MemberWallet memberWallet,OrderPrechargeRecord orderPrechargeRecord,OrderTransaction orderTransaction);


}
