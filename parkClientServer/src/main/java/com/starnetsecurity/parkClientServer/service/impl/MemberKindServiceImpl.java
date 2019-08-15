package com.starnetsecurity.parkClientServer.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.parkClientServer.chargeDetail.ChargeTypeDetail;
import com.starnetsecurity.parkClientServer.chargeDetail.FeeSetDetail;
import com.starnetsecurity.parkClientServer.entity.*;
import com.starnetsecurity.parkClientServer.service.CarparkService;
import com.starnetsecurity.parkClientServer.service.MemberKindService;
import com.starnetsecurity.parkClientServer.service.UploadDataToCloudService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

@Service
public class MemberKindServiceImpl implements MemberKindService {

    @Autowired
    UploadDataToCloudService uploadDataToCloudService;

    @Autowired
    HibernateBaseDao baseDao;

    //根据收费类型ID获取收费详情
    @Override
    public Map getMemberKindInfo(String memberKindId) {
        Map<String,Object> map = new HashedMap();
        MemberKind memberKind = (MemberKind) baseDao.getById(MemberKind.class,memberKindId);
        if (!CommonUtils.isEmpty(memberKind)){
            Integer packageChildKind = memberKind.getPackageChildKind();
            if(memberKind.getPackageKind().equals(2)){
                String hql = "from ChargeType where chargeType = ? and memberKind = ?";
                ChargeType chargeType = (ChargeType)baseDao.getUnique(hql,packageChildKind.toString(),memberKind);
                FeeSet feeSetSmall = chargeType.getFeeSubModeSamll();
                FeeSet feeSetBig = chargeType.getFeeSubModeBig();
                FeeSet feeSetOther = chargeType.getFeeSubModeOther();

                FeeSetDetail feeSetDetail = new FeeSetDetail();
                feeSetDetail.setSmallFreeDuration(feeSetSmall.getFreeDuration());
                feeSetDetail.setSmallFirstTime(feeSetSmall.getFirstTime());
                feeSetDetail.setSmallFirstTimeFee(feeSetSmall.getFirstTimeFee());
                feeSetDetail.setSmallSecondTimeAfterGapFee(feeSetSmall.getSecondTimeAfterGapFee());
                feeSetDetail.setSmallAddFee(feeSetSmall.getAddFee());
                feeSetDetail.setSmallFeeLimit(feeSetSmall.getFeeLimit());
                feeSetDetail.setSmallFreeDurationEx(feeSetSmall.getFreeDurationEx());
                feeSetDetail.setSmallFirstTimeEx(feeSetSmall.getFirstTimeEx());
                feeSetDetail.setSmallFirstTimeFeeEx(feeSetSmall.getFirstTimeFeeEx());
                feeSetDetail.setSmallSecondTimeAfterGapFeeEx(feeSetSmall.getSecondTimeAfterGapFeeEx());
                feeSetDetail.setSmallAddFeeEx(feeSetSmall.getAddFeeEx());
                feeSetDetail.setSmallFeeLimitEx(feeSetSmall.getFeeLimitEx());
                feeSetDetail.setSmallIsFeeSection(feeSetSmall.getIsFeeSection());
                feeSetDetail.setSmallFeeStartTime(feeSetSmall.getFeeStartTime());
                feeSetDetail.setSmallFeeEndTime(feeSetSmall.getFeeEndTime());
                feeSetDetail.setSmallTotalFeeLimit(feeSetSmall.getTotalFeeLimit());

                feeSetDetail.setBigFreeDuration(feeSetBig.getFreeDuration());
                feeSetDetail.setBigFirstTime(feeSetBig.getFirstTime());
                feeSetDetail.setBigFirstTimeFee(feeSetBig.getFirstTimeFee());
                feeSetDetail.setBigSecondTimeAfterGapFee(feeSetBig.getSecondTimeAfterGapFee());
                feeSetDetail.setBigAddFee(feeSetBig.getAddFee());
                feeSetDetail.setBigFeeLimit(feeSetBig.getFeeLimit());
                feeSetDetail.setBigFreeDurationEx(feeSetBig.getFreeDurationEx());
                feeSetDetail.setBigFirstTimeEx(feeSetBig.getFirstTimeEx());
                feeSetDetail.setBigFirstTimeFeeEx(feeSetBig.getFirstTimeFeeEx());
                feeSetDetail.setBigSecondTimeAfterGapFeeEx(feeSetBig.getSecondTimeAfterGapFeeEx());
                feeSetDetail.setBigAddFeeEx(feeSetBig.getAddFeeEx());
                feeSetDetail.setBigFeeLimitEx(feeSetBig.getFeeLimitEx());
                feeSetDetail.setBigIsFeeSection(feeSetBig.getIsFeeSection());
                feeSetDetail.setBigFeeStartTime(feeSetBig.getFeeStartTime());
                feeSetDetail.setBigFeeEndTime(feeSetBig.getFeeEndTime());
                feeSetDetail.setBigTotalFeeLimit(feeSetBig.getTotalFeeLimit());

                feeSetDetail.setOtherFreeDuration(feeSetOther.getFreeDuration());
                feeSetDetail.setOtherFirstTime(feeSetOther.getFirstTime());
                feeSetDetail.setOtherFirstTimeFee(feeSetOther.getFirstTimeFee());
                feeSetDetail.setOtherSecondTimeAfterGapFee(feeSetOther.getSecondTimeAfterGapFee());
                feeSetDetail.setOtherAddFee(feeSetOther.getAddFee());
                feeSetDetail.setOtherFeeLimit(feeSetOther.getFeeLimit());
                feeSetDetail.setOtherFreeDurationEx(feeSetOther.getFreeDurationEx());
                feeSetDetail.setOtherFirstTimeEx(feeSetOther.getFirstTimeEx());
                feeSetDetail.setOtherFirstTimeFeeEx(feeSetOther.getFirstTimeFeeEx());
                feeSetDetail.setOtherSecondTimeAfterGapFeeEx(feeSetOther.getSecondTimeAfterGapFeeEx());
                feeSetDetail.setOtherAddFeeEx(feeSetOther.getAddFeeEx());
                feeSetDetail.setOtherFeeLimitEx(feeSetOther.getFeeLimitEx());
                feeSetDetail.setOtherIsFeeSection(feeSetOther.getIsFeeSection());
                feeSetDetail.setOtherFeeStartTime(feeSetOther.getFeeStartTime());
                feeSetDetail.setOtherFeeEndTime(feeSetOther.getFeeEndTime());
                feeSetDetail.setOtherTotalFeeLimit(feeSetOther.getTotalFeeLimit());

                map.put("feeSetDetail",feeSetDetail);
            }

        }else {
            map.put("feeSetDetail",Collections.EMPTY_MAP);
        }
        return map;
    }

    //更新收费类型
    @Override
    public void updateMemberKindInfo(Map param, FeeSetDetail feeSetDetail) {
        MemberKind memberKind = (MemberKind)baseDao.getById(MemberKind.class,param.get("id").toString());
        ChargeType chargeTypeOld = null;
        if (memberKind.getPackageKind().equals(2)){
            //原来为临时计费
            String hql = "from ChargeType where memberKind = ?";
            chargeTypeOld = (ChargeType)baseDao.getUnique(hql,memberKind);
        }
        ChargeType chargeType = new ChargeType();
        if (param.get("packageKind").toString().equals("2")){
            String hql = "from ChargeType where memberKind = ?";
            chargeType = (ChargeType)baseDao.getUnique(hql,memberKind);
        }
        memberKind.setKindName(param.get("kindName").toString());
        memberKind.setMemo(param.get("memo").toString());
        memberKind.setPackageKind(Integer.valueOf(param.get("packageKind").toString()));


        if (param.get("packageKind").toString().equals("0")) {
            memberKind.setMonthPackage(param.get("monthPackage").toString());
            memberKind.setPackageChildKind(0);
            memberKind.setChargeRuleTemplate("");
        }else if (param.get("packageKind").toString().equals("1")) {
            memberKind.setCountPackage(param.get("countPackage").toString());
            memberKind.setPackageChildKind(1);
            memberKind.setChargeRuleTemplate("");
        }else if (param.get("packageKind").toString().equals("2")){ //临时缴费
            memberKind.setPackageChildKind(Integer.valueOf(param.get("packageChildKind").toString()));
            memberKind.setChargeRuleList((String)param.get("chargeRuleList"));
        }else if (param.get("packageKind").toString().equals("-3")){
            memberKind.setPackageChildKind(-3);
            memberKind.setChargeRuleTemplate("");
        }

        if (!("2".equals(param.get("packageKind") + ""))){
            if (!CommonUtils.isEmpty(chargeTypeOld)) {
                String feeSetSmallId = chargeTypeOld.getFeeSubModeSamll().getFeeSubId();
                String feeSetBigId = chargeTypeOld.getFeeSubModeBig().getFeeSubId();
                String feeSetOtherId = chargeTypeOld.getFeeSubModeOther().getFeeSubId();
                baseDao.deleteById(ChargeType.class, chargeTypeOld.getChargeTypeId());
                baseDao.deleteById(FeeSet.class, feeSetSmallId);
                baseDao.deleteById(FeeSet.class, feeSetBigId);
                baseDao.deleteById(FeeSet.class, feeSetOtherId);
            }
        }

        memberKind.setIsStatistic((String)param.get("isStatistic"));
        memberKind.setVoicePath((String)param.get("voicePath"));
        memberKind.setShowColor((String)param.get("showColor"));
        memberKind.setUseMark(1);
        baseDao.save(memberKind);

        FeeSet feeSetsmall;
        FeeSet feeSetBig;
        FeeSet feeSetOther;
        if (memberKind.getPackageKind().equals(2)){
            if (!CommonUtils.isEmpty(chargeType)){
                chargeType.setChargeType(memberKind.getPackageChildKind().toString());
                chargeType.setAddTime(CommonUtils.getTimestamp().toString());
                chargeType.setMemberKind(memberKind);
                feeSetsmall = chargeType.getFeeSubModeSamll();
                feeSetBig = chargeType.getFeeSubModeBig();
                feeSetOther = chargeType.getFeeSubModeOther();
                if(CommonUtils.isEmpty(feeSetsmall)) {
                    feeSetsmall = new FeeSet();
                }
                if(CommonUtils.isEmpty(feeSetBig)){
                    feeSetBig = new FeeSet();
                }
                if(CommonUtils.isEmpty(feeSetOther)){
                    feeSetOther = new FeeSet();
                }


                feeSetsmall.setFreeDuration(feeSetDetail.getSmallFreeDuration());
                feeSetsmall.setFirstTime(feeSetDetail.getSmallFirstTime());
                feeSetsmall.setFirstTimeFee(feeSetDetail.getSmallFirstTimeFee());
                feeSetsmall.setSecondTimeAfterGapFee(feeSetDetail.getSmallSecondTimeAfterGapFee());
                feeSetsmall.setAddFee(feeSetDetail.getSmallAddFee());
                feeSetsmall.setFeeLimit(feeSetDetail.getSmallFeeLimit());
                feeSetsmall.setFreeDurationEx(feeSetDetail.getSmallFreeDurationEx());
                feeSetsmall.setFirstTimeEx(feeSetDetail.getSmallFirstTimeEx());
                feeSetsmall.setFirstTimeFeeEx(feeSetDetail.getSmallFirstTimeFeeEx());
                feeSetsmall.setSecondTimeAfterGapFeeEx(feeSetDetail.getSmallSecondTimeAfterGapFeeEx());
                feeSetsmall.setAddFeeEx(feeSetDetail.getSmallAddFeeEx());
                feeSetsmall.setFeeLimitEx(feeSetDetail.getSmallFeeLimitEx());
                feeSetsmall.setIsFeeSection(feeSetDetail.getSmallIsFeeSection());
                feeSetsmall.setFeeStartTime(feeSetDetail.getSmallFeeStartTime());
                feeSetsmall.setFeeEndTime(feeSetDetail.getSmallFeeEndTime());
                feeSetsmall.setTotalFeeLimit(feeSetDetail.getSmallTotalFeeLimit());

                feeSetBig.setFreeDuration(feeSetDetail.getBigFreeDuration());
                feeSetBig.setFirstTime(feeSetDetail.getBigFirstTime());
                feeSetBig.setFirstTimeFee(feeSetDetail.getBigFirstTimeFee());
                feeSetBig.setSecondTimeAfterGapFee(feeSetDetail.getBigSecondTimeAfterGapFee());
                feeSetBig.setAddFee(feeSetDetail.getBigAddFee());
                feeSetBig.setFeeLimit(feeSetDetail.getBigFeeLimit());
                feeSetBig.setFreeDurationEx(feeSetDetail.getBigFreeDurationEx());
                feeSetBig.setFirstTimeEx(feeSetDetail.getBigFirstTimeEx());
                feeSetBig.setFirstTimeFeeEx(feeSetDetail.getBigFirstTimeFeeEx());
                feeSetBig.setSecondTimeAfterGapFeeEx(feeSetDetail.getBigSecondTimeAfterGapFeeEx());
                feeSetBig.setAddFeeEx(feeSetDetail.getBigAddFeeEx());
                feeSetBig.setFeeLimitEx(feeSetDetail.getBigFeeLimitEx());
                feeSetBig.setIsFeeSection(feeSetDetail.getBigIsFeeSection());
                feeSetBig.setFeeStartTime(feeSetDetail.getBigFeeStartTime());
                feeSetBig.setFeeEndTime(feeSetDetail.getBigFeeEndTime());
                feeSetBig.setTotalFeeLimit(feeSetDetail.getBigTotalFeeLimit());

                feeSetOther.setFreeDuration(feeSetDetail.getOtherFreeDuration());
                feeSetOther.setFirstTime(feeSetDetail.getOtherFirstTime());
                feeSetOther.setFirstTimeFee(feeSetDetail.getOtherFirstTimeFee());
                feeSetOther.setSecondTimeAfterGapFee(feeSetDetail.getOtherSecondTimeAfterGapFee());
                feeSetOther.setAddFee(feeSetDetail.getOtherAddFee());
                feeSetOther.setFeeLimit(feeSetDetail.getOtherFeeLimit());
                feeSetOther.setFreeDurationEx(feeSetDetail.getOtherFreeDurationEx());
                feeSetOther.setFirstTimeEx(feeSetDetail.getOtherFirstTimeEx());
                feeSetOther.setFirstTimeFeeEx(feeSetDetail.getOtherFirstTimeFeeEx());
                feeSetOther.setSecondTimeAfterGapFeeEx(feeSetDetail.getOtherSecondTimeAfterGapFeeEx());
                feeSetOther.setAddFeeEx(feeSetDetail.getOtherAddFeeEx());
                feeSetOther.setFeeLimitEx(feeSetDetail.getOtherFeeLimitEx());
                feeSetOther.setIsFeeSection(feeSetDetail.getOtherIsFeeSection());
                feeSetOther.setFeeStartTime(feeSetDetail.getOtherFeeStartTime());
                feeSetOther.setFeeEndTime(feeSetDetail.getOtherFeeEndTime());
                feeSetOther.setTotalFeeLimit(feeSetDetail.getOtherTotalFeeLimit());
                baseDao.save(feeSetsmall);
                baseDao.save(feeSetBig);
                baseDao.save(feeSetOther);

                chargeType.setFeeSubModeSamll(feeSetsmall);
                chargeType.setFeeSubModeBig(feeSetBig);
                chargeType.setFeeSubModeOther(feeSetOther);
                baseDao.save(chargeType);
                memberKind.setChargeRuleTemplate(String.valueOf(chargeType.getChargeTypeId()));
                baseDao.save(memberKind);
                ChargeTypeDetail chargeTypeDetail = new ChargeTypeDetail();
                chargeTypeDetail.setChargeTypeId(chargeType.getChargeTypeId());
                chargeTypeDetail.setChargeType(chargeType.getChargeType());
                chargeTypeDetail.setMemberKindId(memberKind.getId());
                chargeTypeDetail.setFeeSubModeSmallId(feeSetsmall.getFeeSubId());
                chargeTypeDetail.setFeeSubModeBigId(feeSetBig.getFeeSubId());
                chargeTypeDetail.setFeeSubModeOtherId(feeSetOther.getFeeSubId());
                uploadDataToCloudService.uploadMemberKindInfoToServer(memberKind,chargeTypeDetail,feeSetDetail);
            }else {
                feeSetsmall = new FeeSet();
                feeSetBig = new FeeSet();
                feeSetOther = new FeeSet();

                //先建立Fee_Set，再建立charge_type
                feeSetsmall.setFreeDuration(feeSetDetail.getSmallFreeDuration());
                feeSetsmall.setFirstTime(feeSetDetail.getSmallFirstTime());
                feeSetsmall.setFirstTimeFee(feeSetDetail.getSmallFirstTimeFee());
                feeSetsmall.setSecondTimeAfterGapFee(feeSetDetail.getSmallSecondTimeAfterGapFee());
                feeSetsmall.setAddFee(feeSetDetail.getSmallAddFee());
                feeSetsmall.setFeeLimit(feeSetDetail.getSmallFeeLimit());
                feeSetsmall.setFreeDurationEx(feeSetDetail.getSmallFreeDurationEx());
                feeSetsmall.setFirstTimeEx(feeSetDetail.getSmallFirstTimeEx());
                feeSetsmall.setFirstTimeFeeEx(feeSetDetail.getSmallFirstTimeFeeEx());
                feeSetsmall.setSecondTimeAfterGapFeeEx(feeSetDetail.getSmallSecondTimeAfterGapFeeEx());
                feeSetsmall.setAddFeeEx(feeSetDetail.getSmallAddFeeEx());
                feeSetsmall.setFeeLimitEx(feeSetDetail.getSmallFeeLimitEx());
                feeSetsmall.setIsFeeSection(feeSetDetail.getSmallIsFeeSection());
                feeSetsmall.setFeeStartTime(feeSetDetail.getSmallFeeStartTime());
                feeSetsmall.setFeeEndTime(feeSetDetail.getSmallFeeEndTime());
                feeSetsmall.setTotalFeeLimit(feeSetDetail.getSmallTotalFeeLimit());

                feeSetBig.setFreeDuration(feeSetDetail.getBigFreeDuration());
                feeSetBig.setFirstTime(feeSetDetail.getBigFirstTime());
                feeSetBig.setFirstTimeFee(feeSetDetail.getBigFirstTimeFee());
                feeSetBig.setSecondTimeAfterGapFee(feeSetDetail.getBigSecondTimeAfterGapFee());
                feeSetBig.setAddFee(feeSetDetail.getBigAddFee());
                feeSetBig.setFeeLimit(feeSetDetail.getBigFeeLimit());
                feeSetBig.setFreeDurationEx(feeSetDetail.getBigFreeDurationEx());
                feeSetBig.setFirstTimeEx(feeSetDetail.getBigFirstTimeEx());
                feeSetBig.setFirstTimeFeeEx(feeSetDetail.getBigFirstTimeFeeEx());
                feeSetBig.setSecondTimeAfterGapFeeEx(feeSetDetail.getBigSecondTimeAfterGapFeeEx());
                feeSetBig.setAddFeeEx(feeSetDetail.getBigAddFeeEx());
                feeSetBig.setFeeLimitEx(feeSetDetail.getBigFeeLimitEx());
                feeSetBig.setIsFeeSection(feeSetDetail.getBigIsFeeSection());
                feeSetBig.setFeeStartTime(feeSetDetail.getBigFeeStartTime());
                feeSetBig.setFeeEndTime(feeSetDetail.getBigFeeEndTime());
                feeSetBig.setTotalFeeLimit(feeSetDetail.getBigTotalFeeLimit());

                feeSetOther.setFreeDuration(feeSetDetail.getOtherFreeDuration());
                feeSetOther.setFirstTime(feeSetDetail.getOtherFirstTime());
                feeSetOther.setFirstTimeFee(feeSetDetail.getOtherFirstTimeFee());
                feeSetOther.setSecondTimeAfterGapFee(feeSetDetail.getOtherSecondTimeAfterGapFee());
                feeSetOther.setAddFee(feeSetDetail.getOtherAddFee());
                feeSetOther.setFeeLimit(feeSetDetail.getOtherFeeLimit());
                feeSetOther.setFreeDurationEx(feeSetDetail.getOtherFreeDurationEx());
                feeSetOther.setFirstTimeEx(feeSetDetail.getOtherFirstTimeEx());
                feeSetOther.setFirstTimeFeeEx(feeSetDetail.getOtherFirstTimeFeeEx());
                feeSetOther.setSecondTimeAfterGapFeeEx(feeSetDetail.getOtherSecondTimeAfterGapFeeEx());
                feeSetOther.setAddFeeEx(feeSetDetail.getOtherAddFeeEx());
                feeSetOther.setFeeLimitEx(feeSetDetail.getOtherFeeLimitEx());
                feeSetOther.setIsFeeSection(feeSetDetail.getOtherIsFeeSection());
                feeSetOther.setFeeStartTime(feeSetDetail.getOtherFeeStartTime());
                feeSetOther.setFeeEndTime(feeSetDetail.getOtherFeeEndTime());
                feeSetOther.setTotalFeeLimit(feeSetDetail.getOtherTotalFeeLimit());

                baseDao.save(feeSetsmall);
                baseDao.save(feeSetBig);
                baseDao.save(feeSetOther);

                ChargeType chargeType1 = new ChargeType();
                chargeType1.setMemberKind(memberKind);
                chargeType1.setChargeType(memberKind.getPackageChildKind().toString());
                chargeType1.setAddTime(CommonUtils.getTimestamp().toString());
                chargeType1.setFeeSubModeSamll(feeSetsmall);
                chargeType1.setFeeSubModeBig(feeSetBig);
                chargeType1.setFeeSubModeOther(feeSetOther);
                baseDao.save(chargeType1);
                memberKind.setChargeRuleTemplate(String.valueOf(chargeType1.getChargeTypeId()));
                baseDao.save(memberKind);
                ChargeTypeDetail chargeTypeDetail = new ChargeTypeDetail();
                chargeTypeDetail.setChargeTypeId(chargeType1.getChargeTypeId());
                chargeTypeDetail.setChargeType(chargeType1.getChargeType());
                chargeTypeDetail.setMemberKindId(memberKind.getId());
                chargeTypeDetail.setFeeSubModeSmallId(feeSetsmall.getFeeSubId());
                chargeTypeDetail.setFeeSubModeBigId(feeSetBig.getFeeSubId());
                chargeTypeDetail.setFeeSubModeOtherId(feeSetOther.getFeeSubId());
                uploadDataToCloudService.uploadMemberKindInfoToServer(memberKind,chargeTypeDetail,feeSetDetail);
            }
        }else {
            uploadDataToCloudService.uploadMemberKindInfoToServer(memberKind,null,null);
        }

    }

    //增加收费类型
    @Override
    public void addMemberKindInfo(Map param,FeeSetDetail feeSetDetail) {
        String hql = "select count(1) from MemberKind where kindName = ? and useMark >= 0";
        Long count = (Long) baseDao.getUnique(hql,param.get("kindName").toString());
        if(count > 0){
            throw new BizException("规则名称已存在");
        }

        hql = "from DepartmentInfo where useMark >= 0";
        DepartmentInfo departmentInfo = (DepartmentInfo)baseDao.getUnique(hql);
        String departmentId = departmentInfo.getDepId();

        MemberKind memberKind = new MemberKind();
        FeeSet feeSetsmall = new FeeSet();
        FeeSet feeSetBig = new FeeSet();
        FeeSet feeSetOther = new FeeSet();
        ChargeType chargeType = new ChargeType();
        memberKind.setKindName(param.get("kindName").toString());
        memberKind.setMemo(param.get("memo").toString());
        memberKind.setPackageKind(Integer.valueOf(param.get("packageKind").toString()));

        if (param.get("packageKind").toString().equals("0")) {
            memberKind.setMonthPackage(param.get("monthPackage").toString());
            memberKind.setPackageChildKind(0);
        }else if (param.get("packageKind").toString().equals("1")) {
            memberKind.setCountPackage(param.get("countPackage").toString());
            memberKind.setPackageChildKind(1);
        }else if (param.get("packageKind").toString().equals("2")){ //临时缴费
            memberKind.setPackageChildKind(Integer.valueOf(param.get("packageChildKind").toString()));
            //memberKind.setChargeRuleTemplate("" + chargeType.getChargeTypeId());
            memberKind.setChargeRuleList((String)param.get("chargeRuleList"));
        }else if (param.get("packageKind").toString().equals("-3")){
            memberKind.setPackageChildKind(-3);
        }
        memberKind.setIsStatistic(param.get("isStatistic").toString());
        memberKind.setVoicePath((String)param.get("voicePath"));
        memberKind.setShowColor((String)param.get("showColor"));
        memberKind.setAddTime(CommonUtils.getTimestamp());
        memberKind.setUseMark(0);
        memberKind.setDepartmentId(departmentId);
        baseDao.save(memberKind);


        if(!CommonUtils.isEmpty(feeSetDetail)){
            feeSetsmall.setFreeDuration(feeSetDetail.getSmallFreeDuration());
            feeSetsmall.setFirstTime(feeSetDetail.getSmallFirstTime());
            feeSetsmall.setFirstTimeFee(feeSetDetail.getSmallFirstTimeFee());
            feeSetsmall.setSecondTimeAfterGapFee(feeSetDetail.getSmallSecondTimeAfterGapFee());
            feeSetsmall.setAddFee(feeSetDetail.getSmallAddFee());
            feeSetsmall.setFeeLimit(feeSetDetail.getSmallFeeLimit());
            feeSetsmall.setFreeDurationEx(feeSetDetail.getSmallFreeDurationEx());
            feeSetsmall.setFirstTimeEx(feeSetDetail.getSmallFirstTimeEx());
            feeSetsmall.setFirstTimeFeeEx(feeSetDetail.getSmallFirstTimeFeeEx());
            feeSetsmall.setSecondTimeAfterGapFeeEx(feeSetDetail.getSmallSecondTimeAfterGapFeeEx());
            feeSetsmall.setAddFeeEx(feeSetDetail.getSmallAddFeeEx());
            feeSetsmall.setFeeLimitEx(feeSetDetail.getSmallFeeLimitEx());
            feeSetsmall.setIsFeeSection(feeSetDetail.getSmallIsFeeSection());
            feeSetsmall.setFeeStartTime(feeSetDetail.getSmallFeeStartTime());
            feeSetsmall.setFeeEndTime(feeSetDetail.getSmallFeeEndTime());
            feeSetsmall.setTotalFeeLimit(feeSetDetail.getSmallTotalFeeLimit());

            feeSetBig.setFreeDuration(feeSetDetail.getBigFreeDuration());
            feeSetBig.setFirstTime(feeSetDetail.getBigFirstTime());
            feeSetBig.setFirstTimeFee(feeSetDetail.getBigFirstTimeFee());
            feeSetBig.setSecondTimeAfterGapFee(feeSetDetail.getBigSecondTimeAfterGapFee());
            feeSetBig.setAddFee(feeSetDetail.getBigAddFee());
            feeSetBig.setFeeLimit(feeSetDetail.getBigFeeLimit());
            feeSetBig.setFreeDurationEx(feeSetDetail.getBigFreeDurationEx());
            feeSetBig.setFirstTimeEx(feeSetDetail.getBigFirstTimeEx());
            feeSetBig.setFirstTimeFeeEx(feeSetDetail.getBigFirstTimeFeeEx());
            feeSetBig.setSecondTimeAfterGapFeeEx(feeSetDetail.getBigSecondTimeAfterGapFeeEx());
            feeSetBig.setAddFeeEx(feeSetDetail.getBigAddFeeEx());
            feeSetBig.setFeeLimitEx(feeSetDetail.getBigFeeLimitEx());
            feeSetBig.setIsFeeSection(feeSetDetail.getBigIsFeeSection());
            feeSetBig.setFeeStartTime(feeSetDetail.getBigFeeStartTime());
            feeSetBig.setFeeEndTime(feeSetDetail.getBigFeeEndTime());
            feeSetBig.setTotalFeeLimit(feeSetDetail.getBigTotalFeeLimit());

            feeSetOther.setFreeDuration(feeSetDetail.getOtherFreeDuration());
            feeSetOther.setFirstTime(feeSetDetail.getOtherFirstTime());
            feeSetOther.setFirstTimeFee(feeSetDetail.getOtherFirstTimeFee());
            feeSetOther.setSecondTimeAfterGapFee(feeSetDetail.getOtherSecondTimeAfterGapFee());
            feeSetOther.setAddFee(feeSetDetail.getOtherAddFee());
            feeSetOther.setFeeLimit(feeSetDetail.getOtherFeeLimit());
            feeSetOther.setFreeDurationEx(feeSetDetail.getOtherFreeDurationEx());
            feeSetOther.setFirstTimeEx(feeSetDetail.getOtherFirstTimeEx());
            feeSetOther.setFirstTimeFeeEx(feeSetDetail.getOtherFirstTimeFeeEx());
            feeSetOther.setSecondTimeAfterGapFeeEx(feeSetDetail.getOtherSecondTimeAfterGapFeeEx());
            feeSetOther.setAddFeeEx(feeSetDetail.getOtherAddFeeEx());
            feeSetOther.setFeeLimitEx(feeSetDetail.getOtherFeeLimitEx());
            feeSetOther.setIsFeeSection(feeSetDetail.getOtherIsFeeSection());
            feeSetOther.setFeeStartTime(feeSetDetail.getOtherFeeStartTime());
            feeSetOther.setFeeEndTime(feeSetDetail.getOtherFeeEndTime());
            feeSetOther.setTotalFeeLimit(feeSetDetail.getOtherTotalFeeLimit());

            baseDao.save(feeSetsmall);
            baseDao.save(feeSetBig);
            baseDao.save(feeSetOther);
            chargeType.setMemberKind(memberKind);
            chargeType.setChargeType(memberKind.getPackageChildKind().toString());
            chargeType.setAddTime(CommonUtils.getTimestamp().toString());
            chargeType.setFeeSubModeSamll(feeSetsmall);
            chargeType.setFeeSubModeBig(feeSetBig);
            chargeType.setFeeSubModeOther(feeSetOther);
            baseDao.save(chargeType);
            memberKind.setChargeRuleTemplate(String.valueOf(chargeType.getChargeTypeId()));
            baseDao.save(memberKind);
            ChargeTypeDetail chargeTypeDetail = new ChargeTypeDetail();
            chargeTypeDetail.setChargeTypeId(chargeType.getChargeTypeId());
            chargeTypeDetail.setChargeType(chargeType.getChargeType());
            chargeTypeDetail.setMemberKindId(memberKind.getId());
            chargeTypeDetail.setFeeSubModeSmallId(feeSetsmall.getFeeSubId());
            chargeTypeDetail.setFeeSubModeBigId(feeSetBig.getFeeSubId());
            chargeTypeDetail.setFeeSubModeOtherId(feeSetOther.getFeeSubId());
            uploadDataToCloudService.uploadMemberKindInfoToServer(memberKind,chargeTypeDetail,feeSetDetail);
        }else {
            uploadDataToCloudService.uploadMemberKindInfoToServer(memberKind,null,null);
        }
    }


    //删除收费类型
    @Override
    public void deleteKindInfo(String memberKindId) {
        String hql = "from MemberKind where id = ?";
        MemberKind memberKind = (MemberKind)baseDao.getUnique(hql,memberKindId);
        memberKind.setUseMark(-1);
        baseDao.update(memberKind);
        if (memberKind.getPackageKind().equals(2)){
            hql = "from ChargeType where memberKind = ?";
            ChargeType chargeType = (ChargeType)baseDao.getUnique(hql,memberKind);
            if (!CommonUtils.isEmpty(chargeType)) {
                String feeSetSmallId = chargeType.getFeeSubModeSamll().getFeeSubId();
                String feeSetBigId = chargeType.getFeeSubModeBig().getFeeSubId();
                String feeSetOtherId = chargeType.getFeeSubModeOther().getFeeSubId();
                baseDao.deleteById(ChargeType.class, chargeType.getChargeTypeId());
                baseDao.deleteById(FeeSet.class, feeSetSmallId);
                baseDao.deleteById(FeeSet.class, feeSetBigId);
                baseDao.deleteById(FeeSet.class, feeSetOtherId);
            }
        }
        uploadDataToCloudService.uploadMemberKindInfoToServer(memberKind,null,null);
    }

}
