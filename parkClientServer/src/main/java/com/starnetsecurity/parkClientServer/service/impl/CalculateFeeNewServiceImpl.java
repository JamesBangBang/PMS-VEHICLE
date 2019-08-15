package com.starnetsecurity.parkClientServer.service.impl;

import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.util.DateUtils;
import com.starnetsecurity.parkClientServer.entity.CarparkInfo;
import com.starnetsecurity.parkClientServer.entity.ChargeType;
import com.starnetsecurity.parkClientServer.entity.FeeSet;
import com.starnetsecurity.parkClientServer.entity.MemberKind;
import com.starnetsecurity.parkClientServer.service.CalculateFeeNewService;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by JAMESBANG on 2018/2/1.
 */
@Service
public class CalculateFeeNewServiceImpl implements CalculateFeeNewService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CalculateFeeNewServiceImpl.class);

    @Autowired
    HibernateBaseDao hibernateBaseDao;

    /*
     *计算计费金额
     */
    @Override
    public double calculateParkingFeeNew(String carno, String userType, String carType, String parkId, Timestamp inTime, Timestamp outTime) throws ParseException {
        String carno_temp = new String(carno);
        String userType_temp = new String(userType);
        String carType_temp = new String(carType);
        String parkId_temp = new String(parkId);

        double chargeFee = 0;
        int parkingMin;      //停车时长
        int chargeType;      //收费类型
        int freeDuration;    //免费时长
        int firstTime;       //第一时段（分钟）
        double firstTimeFee; //第一时段收费金额
        double secondTimeAfterGapFee;  //第二段时间收费金额
        double addFee;       //加收费用
        double feeLimit;     //收费限额
        int freeDurationEx;    //下个时段免费时长（分钟）
        int firstTimeEx;       //下个时段第一时段（分钟）
        double firstTimeFeeEx; //下个时段第一时段收费金额
        double secondTimeAfterGapFeeEx;  //下个时段第二段时间收费金额
        double addFeeEx;       //下个时段加收费用
        double feeLimitEx;     //下个时段收费限额
        String isFeeSection;   //是否分段收费，1-是，其他-以出场时间收费
        String feeStartTime;   //下个时段开始时间
        String feeEndTime;     //下个时段结束时间
        double totalFeeLimit;  //全天收费限额

        //获取收费信息
        try {
            CarparkInfo carparkInfo = (CarparkInfo)hibernateBaseDao.getById(CarparkInfo.class,parkId_temp);
            if (!CommonUtils.isEmpty(carparkInfo.getOwnCarparkNo())){
                //纳入父场计费
                if (carparkInfo.getIfIncludeCaculate().equals(1))
                    return chargeFee;
            }
            Set<MemberKind> memberKinds = carparkInfo.getMemberKinds();
            FeeSet feeSet;
            ChargeType chargeTypeTemp = null;
            for(MemberKind memberKind : memberKinds){
                if (memberKind.getUseType().equals(Integer.parseInt(userType))){
                    if (memberKind.getPackageKind().equals(-3))
                        return 0;
                    chargeTypeTemp = (ChargeType)hibernateBaseDao.getById(ChargeType.class,Long.parseLong(memberKind.getChargeRuleTemplate()));
                    break;
                }
            }

            chargeType = Integer.parseInt(chargeTypeTemp.getChargeType());
            if (carType_temp.equals("1")){
                feeSet = chargeTypeTemp.getFeeSubModeSamll();
            }else if (carType_temp.equals("2")){
                feeSet = chargeTypeTemp.getFeeSubModeBig();
            }else {
                feeSet = chargeTypeTemp.getFeeSubModeOther();
            }

            freeDuration = CommonUtils.isEmpty(feeSet.getFreeDuration()) ? 0 : feeSet.getFreeDuration();
            firstTime = CommonUtils.isEmpty(feeSet.getFirstTime()) ? 0 : feeSet.getFirstTime();
            firstTimeFee = CommonUtils.isEmpty(feeSet.getFirstTimeFee()) ? 0 : feeSet.getFirstTimeFee();
            secondTimeAfterGapFee = CommonUtils.isEmpty(feeSet.getSecondTimeAfterGapFee()) ? 0 : feeSet.getSecondTimeAfterGapFee();
            addFee = CommonUtils.isEmpty(feeSet.getAddFee()) ? 0 : feeSet.getAddFee();
            feeLimit = CommonUtils.isEmpty(feeSet.getFeeLimit()) ? 0 : feeSet.getFeeLimit();
            freeDurationEx = CommonUtils.isEmpty(feeSet.getFreeDurationEx()) ? 0 : feeSet.getFreeDurationEx();
            firstTimeEx = CommonUtils.isEmpty(feeSet.getFirstTimeEx()) ? 0 : feeSet.getFirstTimeEx();
            firstTimeFeeEx = CommonUtils.isEmpty(feeSet.getFirstTimeFeeEx()) ? 0 : feeSet.getFirstTimeFeeEx();
            secondTimeAfterGapFeeEx = CommonUtils.isEmpty(feeSet.getSecondTimeAfterGapFeeEx()) ? 0 : feeSet.getSecondTimeAfterGapFeeEx();
            addFeeEx = CommonUtils.isEmpty(feeSet.getAddFeeEx()) ? 0 : feeSet.getAddFeeEx();
            feeLimitEx = CommonUtils.isEmpty(feeSet.getFeeLimitEx()) ? 0 : feeSet.getFeeLimitEx();
            isFeeSection = feeSet.getIsFeeSection();
            feeStartTime = feeSet.getFeeStartTime();
            feeEndTime =  feeSet.getFeeEndTime();
            totalFeeLimit = CommonUtils.isEmpty(feeSet.getTotalFeeLimit()) ? 0 : feeSet.getTotalFeeLimit();

            //统一时间格式
            Date carInTime = new Timestamp(inTime.getTime());
            Date carOutTime = new Timestamp(outTime.getTime());

            if (carInTime.getTime() >= carOutTime.getTime()) {
                return chargeFee;
            }
            else{
                long parkingSec = (carOutTime.getTime()-carInTime.getTime())/1000;
                parkingMin = (int)(parkingSec/60);           //停车时长（分钟）
            }

            switch (chargeType){
                case 0:
                {
                    if (parkingMin<=0){
                        chargeFee = 0;
                    }
                    else{
                        chargeFee = chargeByTimeRelativeNew(parkingMin, freeDuration, firstTime, firstTimeFee,
                                secondTimeAfterGapFee, addFee, feeLimit, freeDurationEx,
                                firstTimeEx, firstTimeFeeEx, secondTimeAfterGapFeeEx, addFeeEx,
                                feeLimitEx, isFeeSection,feeStartTime,feeEndTime,totalFeeLimit);
                    }
                    break;
                }
                case 1:
                {
                    if (parkingMin<=0){
                        chargeFee = 0;
                    }
                    else{
                        if ("1".equals(isFeeSection)){
                            Timestamp outTimeDay = new Timestamp(outTime.getTime() - 24*60*60*1000);
                            String sqlStr = "select sum(charge_receivable_amount) as availableFee from order_inout_record where car_no = :carNo " +
                                    "and carpark_id = :parkId and out_time < :outTime and out_time > :outTimeDay and use_mark >= 0";
                            SQLQuery sqlQuery = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sqlStr);
                            sqlQuery.setParameter("carNo",carno_temp);
                            sqlQuery.setParameter("parkId",parkId_temp);
                            sqlQuery.setParameter("outTime",outTime);
                            sqlQuery.setParameter("outTimeDay",outTimeDay);
                            Object resObj = sqlQuery.uniqueResult();
                            double availableAmount;
                            if(resObj == null || CommonUtils.isEmpty(resObj)){
                                availableAmount = 0;
                            }else{
                                availableAmount = (Double)resObj;
                            }
                            if (availableAmount > 0)
                                return chargeFee;
                        }

                        chargeFee = chargeByCountRelativeNew(parkingMin, freeDuration, firstTime, firstTimeFee,
                                secondTimeAfterGapFee, addFee, feeLimit, freeDurationEx,
                                firstTimeEx, firstTimeFeeEx, secondTimeAfterGapFeeEx, addFeeEx,
                                feeLimitEx, isFeeSection,feeStartTime,feeEndTime,totalFeeLimit);
                    }
                    break;
                }
                case 2:
                {
                    if (parkingMin<=0){
                        chargeFee = 0;
                    }
                    else{
                        chargeFee = chargeByTimeAllDayNew(carInTime,carOutTime,parkingMin, freeDuration, firstTime, firstTimeFee,
                                secondTimeAfterGapFee, addFee, feeLimit, freeDurationEx,
                                firstTimeEx, firstTimeFeeEx, secondTimeAfterGapFeeEx, addFeeEx,
                                feeLimitEx, isFeeSection,feeStartTime,feeEndTime,totalFeeLimit);
                    }
                    break;
                }
                case 3:
                {
                    if (parkingMin<=0){
                        chargeFee = 0;
                    }
                    else{
                        chargeFee = chargeByTimeAbsoluteNew(carInTime,carOutTime,parkingMin, freeDuration, firstTime, firstTimeFee,
                                secondTimeAfterGapFee, addFee, feeLimit, freeDurationEx,
                                firstTimeEx, firstTimeFeeEx, secondTimeAfterGapFeeEx, addFeeEx,
                                feeLimitEx, isFeeSection,feeStartTime,feeEndTime,totalFeeLimit);
                    }
                    break;
                }
                case 4:
                {
                    if (parkingMin<=0){
                        chargeFee = 0;
                    }
                    else{
                        chargeFee = chargeByCountAbsoluteNew(carno_temp,parkId_temp,carInTime,carOutTime,parkingMin, freeDuration, firstTime, firstTimeFee,
                                secondTimeAfterGapFee, addFee, feeLimit, freeDurationEx,
                                firstTimeEx, firstTimeFeeEx, secondTimeAfterGapFeeEx, addFeeEx,
                                feeLimitEx, isFeeSection,feeStartTime,feeEndTime,totalFeeLimit);
                    }
                    break;
                }
                case 5:
                {
                    if (parkingMin<=0){
                        chargeFee = 0;
                    }
                    else{
                        chargeFee = chargeByCountAbsoluteAddNew(carno_temp,parkId_temp,carInTime,carOutTime,parkingMin, freeDuration, firstTime, firstTimeFee,
                                secondTimeAfterGapFee, addFee, feeLimit, freeDurationEx,
                                firstTimeEx, firstTimeFeeEx, secondTimeAfterGapFeeEx, addFeeEx,
                                feeLimitEx, isFeeSection,feeStartTime,feeEndTime,totalFeeLimit);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            LOGGER.error("计费错误");
        }


        return  chargeFee;
    }

    /**
     * 模板1
     * 按时长（相对时段）计费
     */
    @Override
    public double chargeByTimeRelativeNew(int parkingMin,int freeDuration,int firstTime,double firstTimeFee,
                                       double secondTimeAfterGapFee,double addFee,double feeLimit,int freeDurationEx,
                                       int firstTimeEx,double firstTimeFeeEx,double secondTimeAfterGapFeeEx,double addFeeEx,
                                       double feeLimitEx,String isFeeSection,String feeStartTime,String feeEndTime,
                                       double totalFeeLimit) {
        double lastCalFee = 0;
        double tempFee;
        int parkingHour;
        int hourSurplus;

        try {
            if (parkingMin <= freeDuration)
                lastCalFee = 0;
            else {
                double firstTimeForSpecial = 0;
                if (firstTime <= 0){
                    firstTimeForSpecial = 0.5;
                }
                if (firstTimeForSpecial > 0){
                    parkingHour = (parkingMin - 1) / 60 + 1;  //换算成小时
                    BigDecimal parkingHourFs = new BigDecimal(parkingHour);
                    BigDecimal firstTimeFs = new BigDecimal(firstTimeForSpecial);
                    BigDecimal firstTimeExFs = firstTimeFs;
                    BigDecimal addFeeFs = new BigDecimal(addFee);
                    BigDecimal addFeeExFs = new BigDecimal(addFeeEx);
                    BigDecimal oneFee = new BigDecimal(1);
                    BigDecimal twelveFee = new BigDecimal(12);

                    if (new Double(0).equals(feeLimit) && new Double(0).equals(feeLimitEx)) {
                        if (parkingHourFs.compareTo(firstTimeFs) <= 0)
                            lastCalFee = addFee;
                        else {

                            lastCalFee = addFee + ((parkingHourFs.subtract(firstTimeFs).subtract(oneFee)).divide(firstTimeExFs.add(oneFee),2,BigDecimal.ROUND_UP).multiply(addFeeExFs)).doubleValue();
                            //lastCalFee = addFee + ((parkingHour - firstTime - 1) / firstTimeEx + 1) * addFeeEx;
                        }
                    }

                    if (feeLimit > 0 && feeLimitEx > 0) {
                        lastCalFee = ((parkingHour - 1) / 24) * feeLimitEx;  //超过多少天

                        if (new Double(0).equals(lastCalFee)) {    //未超过一天
                            if (parkingHourFs.compareTo(firstTimeFs) <= 0) {           //小于第一时段
                                if (addFee <= feeLimit)
                                    lastCalFee = addFee;
                                else
                                    lastCalFee = feeLimit;
                            }

                            if (new Integer(24).equals(parkingHour))
                                lastCalFee = feeLimitEx;

                            if (parkingHourFs.compareTo(firstTimeFs) > 0 && parkingHour < 24) {
                                if (parkingHour > 12) {
                                    //lastCalFee = feeLimit + (((parkingHourFs.subtract(twelveFee).subtract(oneFee)).divide(firstTimeExFs.add(oneFee),2,BigDecimal.ROUND_UP)).multiply(addFeeExFs)).doubleValue();
                                    //lastCalFee = feeLimit + ((parkingHour - 12 - 1) / firstTimeEx + 1) * addFeeEx;
                                    lastCalFee = new BigDecimal(feeLimit + ((parkingHour - 12 - 1) / firstTimeForSpecial + 1) * addFeeEx).setScale(2,BigDecimal.ROUND_UP).doubleValue();
                                    if (lastCalFee > feeLimitEx)
                                        lastCalFee = feeLimitEx;         //取最小的值
                                } else {
                                    //lastCalFee = addFee + (((parkingHourFs.subtract(firstTimeFs).subtract(oneFee)).divide(firstTimeExFs.add(oneFee),2,BigDecimal.ROUND_UP)).multiply(addFeeExFs)).doubleValue();
                                    lastCalFee = new BigDecimal(addFee + ((parkingHour - firstTimeForSpecial - 1) / firstTimeForSpecial + 1) * addFeeEx).setScale(2,BigDecimal.ROUND_UP).doubleValue();
                                    if (lastCalFee > feeLimit)
                                        lastCalFee = feeLimit;                  //取最小值
                                }
                            }
                        } else {
                            if (new Integer(0).equals(parkingHour % 24))
                                lastCalFee = lastCalFee + feeLimitEx;
                            else {
                                hourSurplus = parkingHour % 24;
                                if (hourSurplus > 12) {
                                    //tempFee = feeLimit + ((new BigDecimal(hourSurplus - 13)).divide(firstTimeExFs.add(oneFee)).multiply(addFeeExFs)).doubleValue();
                                    //tempFee = feeLimit + ((hourSurplus - 12 - 1) / firstTimeEx + 1) * addFeeEx;
                                    tempFee = new BigDecimal(feeLimit + ((hourSurplus - 12 - 1) / firstTimeForSpecial + 1) * addFeeEx).doubleValue();
                                    if (tempFee >= feeLimitEx)
                                        lastCalFee = lastCalFee + feeLimitEx;
                                    else
                                        lastCalFee = lastCalFee + tempFee;
                                } else {
                                    //tempFee = feeLimit + ((new BigDecimal(hourSurplus - 1)).divide(firstTimeExFs.add(oneFee)).multiply(addFeeExFs)).doubleValue();
                                    //tempFee = ((hourSurplus - 1) / firstTimeEx + 1) * addFeeEx;
                                    tempFee = new BigDecimal(((hourSurplus - 1) / firstTimeForSpecial + 1) * addFeeEx).doubleValue();
                                    if (tempFee >= feeLimit)
                                        lastCalFee = lastCalFee + feeLimit;
                                    else
                                        lastCalFee = lastCalFee + tempFee;
                                }
                            }
                        }
                    }

                    if (new Double(0).equals(feeLimit) && feeLimitEx > 0) {
                        lastCalFee = ((parkingHour - 1) / 24) * feeLimitEx;

                        if (new Double(0).equals(lastCalFee)) {                        //未超过一天
                            if (parkingHourFs.compareTo(firstTimeFs) <= 0) {           //不超过第一时段
                                if (addFee <= feeLimitEx)
                                    lastCalFee = addFee;
                                else
                                    lastCalFee = feeLimitEx;
                            }

                            if (new Integer(24).equals(parkingHour))
                                lastCalFee = feeLimitEx;

                            if (parkingHourFs.compareTo(firstTimeFs) > 0 && parkingHour < 24) {
                                lastCalFee = addFee + ((parkingHourFs.subtract(firstTimeFs).subtract(oneFee)).divide(firstTimeExFs.add(oneFee),2,BigDecimal.ROUND_UP).multiply(addFeeExFs)).doubleValue();
                                //lastCalFee = addFee + ((parkingHour - firstTime - 1) / firstTimeEx + 1) * addFeeEx;
                                lastCalFee = new BigDecimal(addFee + ((parkingHour - firstTimeForSpecial - 1) / firstTimeForSpecial + 1) * addFeeEx).setScale(2,BigDecimal.ROUND_UP).doubleValue();
                                if (lastCalFee >= feeLimitEx)
                                    lastCalFee = feeLimitEx;
                            }
                        } else {
                            if (new Integer(0).equals(parkingHour % 24))
                                lastCalFee = lastCalFee + feeLimitEx;
                            else {
                                hourSurplus = parkingHour % 24;
                                //tempFee = (new BigDecimal(hourSurplus-1).divide(firstTimeExFs.add(oneFee),2,BigDecimal.ROUND_UP).multiply(addFeeExFs)).doubleValue();
                                //tempFee = ((hourSurplus - 1) / firstTimeEx + 1) * addFeeEx;
                                tempFee = new BigDecimal(((hourSurplus - 1) / firstTimeForSpecial + 1) * addFeeEx).setScale(2,BigDecimal.ROUND_UP).doubleValue();
                                if (tempFee <= feeLimitEx)
                                    lastCalFee = lastCalFee + tempFee;
                                else
                                    lastCalFee = lastCalFee + feeLimitEx;
                            }

                        }
                    }

                    if (feeLimit > 0 && new Double(0).equals(feeLimitEx)) {
                        lastCalFee = ((parkingHour - 1) / 12) * feeLimit;    //按12小时进行封顶计费
                        if (new Double(0).equals(lastCalFee)) {
                            if (parkingHourFs.compareTo(firstTimeFs) <= 0) {
                                if (addFee <= feeLimit)
                                    lastCalFee = addFee;
                                else
                                    lastCalFee = feeLimit;
                            }

                            if (new Integer(12).equals(parkingHour))
                                lastCalFee = feeLimit;

                            if (parkingHourFs.compareTo(firstTimeFs) > 0 && parkingHour < 12) {
                                //lastCalFee = addFee + ((parkingHourFs.subtract(firstTimeFs).subtract(oneFee)).divide(firstTimeExFs.add(oneFee),2,BigDecimal.ROUND_UP).multiply(addFeeExFs)).doubleValue();
                                //lastCalFee = addFee + ((parkingHour - firstTime - 1) / firstTimeEx + 1) * addFeeEx;
                                lastCalFee = new BigDecimal(addFee + ((parkingHour - firstTime - 1) / firstTimeForSpecial + 1) * addFeeEx).setScale(2,BigDecimal.ROUND_UP).doubleValue();
                                if (lastCalFee > feeLimit)
                                    lastCalFee = feeLimit;
                            }
                        } else {
                            if (new Integer(0).equals(parkingHour % 12))
                                lastCalFee = lastCalFee + feeLimit;
                            else {
                                //tempFee = (new BigDecimal((parkingHour % 12) - 1).divide(firstTimeExFs.add(oneFee),2,BigDecimal.ROUND_UP).multiply(addFeeExFs)).doubleValue();
                                //tempFee = (((parkingHour % 12) - 1) / firstTimeEx + 1) * addFeeEx;
                                tempFee = new BigDecimal((((parkingHour % 12) - 1) / firstTimeForSpecial + 1) * addFeeEx).setScale(2,BigDecimal.ROUND_UP).doubleValue();
                                if (tempFee <= feeLimit)
                                    lastCalFee = lastCalFee + tempFee;
                                else
                                    lastCalFee = lastCalFee + feeLimit;
                            }

                        }
                    }
                }else {
                    parkingHour = (parkingMin - 1) / 60 + 1;  //换算成小时
                    if (new Double(0).equals(feeLimit) && new Double(0).equals(feeLimitEx)) {
                        if (parkingHour <= firstTime)
                            lastCalFee = addFee;
                        else
                            lastCalFee = addFee + ((parkingHour - firstTime - 1) / firstTimeEx + 1) * addFeeEx;
                    }
                    if (feeLimit > 0 && feeLimitEx > 0) {
                        lastCalFee = ((parkingHour - 1) / 24) * feeLimitEx;  //超过多少天

                        if (new Double(0).equals(lastCalFee)) {    //未超过一天
                            if (parkingHour <= firstTime) {           //小于第一时段
                                if (addFee <= feeLimit)
                                    lastCalFee = addFee;
                                else
                                    lastCalFee = feeLimit;
                            }

                            if (new Integer(24).equals(parkingHour))
                                lastCalFee = feeLimitEx;

                            if (parkingHour > firstTime && parkingHour < 24) {
                                if (parkingHour > 12) {
                                    lastCalFee = feeLimit + ((parkingHour - 12 - 1) / firstTimeEx + 1) * addFeeEx;
                                    if (lastCalFee > feeLimitEx)
                                        lastCalFee = feeLimitEx;         //取最小的值
                                } else {
                                    lastCalFee = addFee + ((parkingHour - firstTime - 1) / firstTimeEx + 1) * addFeeEx;
                                    if (lastCalFee > feeLimit)
                                        lastCalFee = feeLimit;                  //取最小值
                                }
                            }
                        } else {
                            if (new Integer(0).equals(parkingHour % 24))
                                lastCalFee = lastCalFee + feeLimitEx;
                            else {
                                hourSurplus = parkingHour % 24;
                                if (hourSurplus > 12) {
                                    tempFee = feeLimit + ((hourSurplus - 12 - 1) / firstTimeEx + 1) * addFeeEx;
                                    if (tempFee >= feeLimitEx)
                                        lastCalFee = lastCalFee + feeLimitEx;
                                    else
                                        lastCalFee = lastCalFee + tempFee;
                                } else {
                                    tempFee = ((hourSurplus - 1) / firstTimeEx + 1) * addFeeEx;
                                    if (tempFee >= feeLimit)
                                        lastCalFee = lastCalFee + feeLimit;
                                    else
                                        lastCalFee = lastCalFee + tempFee;
                                }
                            }
                        }
                    }

                    if (new Double(0).equals(feeLimit) && feeLimitEx > 0) {
                        lastCalFee = ((parkingHour - 1) / 24) * feeLimitEx;

                        if (new Double(0).equals(lastCalFee)) {                        //未超过一天
                            if (parkingHour <= firstTime) {           //不超过第一时段
                                if (addFee <= feeLimitEx)
                                    lastCalFee = addFee;
                                else
                                    lastCalFee = feeLimitEx;
                            }

                            if (new Integer(24).equals(parkingHour))
                                lastCalFee = feeLimitEx;

                            if (parkingHour > firstTime && parkingHour < 24) {
                                lastCalFee = addFee + ((parkingHour - firstTime - 1) / firstTimeEx + 1) * addFeeEx;
                                if (lastCalFee >= feeLimitEx)
                                    lastCalFee = feeLimitEx;
                            }
                        } else {
                            if (new Integer(0).equals(parkingHour % 24))
                                lastCalFee = lastCalFee + feeLimitEx;
                            else {
                                hourSurplus = parkingHour % 24;
                                tempFee = ((hourSurplus - 1) / firstTimeEx + 1) * addFeeEx;
                                if (tempFee <= feeLimitEx)
                                    lastCalFee = lastCalFee + tempFee;
                                else
                                    lastCalFee = lastCalFee + feeLimitEx;
                            }

                        }
                    }

                    if (feeLimit > 0 && new Double(0).equals(feeLimitEx)) {
                        lastCalFee = ((parkingHour - 1) / 12) * feeLimit;    //按12小时进行封顶计费
                        if (new Double(0).equals(lastCalFee)) {
                            if (parkingHour <= firstTime) {
                                if (addFee <= feeLimit)
                                    lastCalFee = addFee;
                                else
                                    lastCalFee = feeLimit;
                            }

                            if (new Integer(12).equals(parkingHour))
                                lastCalFee = feeLimit;

                            if (parkingHour > firstTime && parkingHour < 12) {
                                lastCalFee = addFee + ((parkingHour - firstTime - 1) / firstTimeEx + 1) * addFeeEx;
                                if (lastCalFee > feeLimit)
                                    lastCalFee = feeLimit;
                            }
                        } else {
                            if (new Integer(0).equals(parkingHour % 12))
                                lastCalFee = lastCalFee + feeLimit;
                            else {
                                tempFee = (((parkingHour % 12) - 1) / firstTimeEx + 1) * addFeeEx;
                                if (tempFee <= feeLimit)
                                    lastCalFee = lastCalFee + tempFee;
                                else
                                    lastCalFee = lastCalFee + feeLimit;
                            }

                        }
                    }
                }

            }
        } catch (Exception e) {
            LOGGER.error("计费模板1计费错误");
        }

        return lastCalFee;
    }

    /**
     * 模板2
     * 按次（相对时段）计费
     */
    @Override
    public double chargeByCountRelativeNew(int parkingMin,int freeDuration,int firstTime,double firstTimeFee,
                                        double secondTimeAfterGapFee,double addFee,double feeLimit,int freeDurationEx,
                                        int firstTimeEx,double firstTimeFeeEx,double secondTimeAfterGapFeeEx,double addFeeEx,
                                        double feeLimitEx,String isFeeSection,String feeStartTime,String feeEndTime,
                                        double totalFeeLimit) {
        double lastCalFee = 0;

        try {
            if (parkingMin <= freeDuration)
                lastCalFee = 0;
            else {
                if (parkingMin <= firstTime * 60)
                    lastCalFee = firstTimeFee;
                else
                    lastCalFee = secondTimeAfterGapFee;

                if (parkingMin > 24 * 60)
                    lastCalFee = lastCalFee + (parkingMin / (24 * 60)) * addFee;
            }
        } catch (Exception e) {
            LOGGER.error("计费模板2计费错误");
        }

        return lastCalFee;
    }

    /**
     * 按时长（全天）计费
     * 模板3
     */
    @Override
    public double chargeByTimeAllDayNew(Date inTime,Date outTime,int parkingMin,int freeDuration,int firstTime,double firstTimeFee,
                                     double secondTimeAfterGapFee,double addFee,double feeLimit,int freeDurationEx,
                                     int firstTimeEx,double firstTimeFeeEx,double secondTimeAfterGapFeeEx,double addFeeEx,
                                     double feeLimitEx,String isFeeSection,String feeStartTime,String feeEndTime,
                                     double totalFeeLimit) throws ParseException {
        double chargeFee = 0;

        try {
            if (parkingMin <= freeDuration)
                chargeFee = 0;
            else {
                if (parkingMin <= firstTime * 60)
                    chargeFee = ((parkingMin - 1) / 60 + 1) * firstTimeFee;
                else
                    chargeFee = firstTime * firstTimeFee + ((parkingMin -1) / 60 + 1 - firstTime) * secondTimeAfterGapFee;

                //判断是否需要夜间收费
                if (addFee > 0){
                    int dateCount = 0;

                    Calendar calSec = Calendar.getInstance();

                    Date timeTemp = Timestamp.valueOf(DateUtils.formatDate(inTime) + " " + feeStartTime);


                    if (timeTemp.getTime() < inTime.getTime()){
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(timeTemp);
                        cal.add(Calendar.DATE,1); //日期加一天
                        timeTemp = cal.getTime();
                    }

                    while (inTime.getTime() < timeTemp.getTime() && timeTemp.getTime() < outTime.getTime()){
                        dateCount++;
                        calSec.setTime(timeTemp);
                        calSec.add(Calendar.DATE,1); //日期加一天
                        timeTemp = calSec.getTime();
                    }
                    chargeFee = chargeFee + addFee * dateCount;

                }
                if (chargeFee > feeLimit && feeLimit > 0)
                    chargeFee = feeLimit;
            }
        } catch (Exception e) {
            LOGGER.error("计费模板3计费错误");
        }


        return chargeFee;
    }

    /**
     * 按时长（绝对时段）计费
     * 模板4
     */
    @Override
    public double chargeByTimeAbsoluteNew(Date inTime,Date outTime,int parkingMin,int freeDuration,int firstTime,double firstTimeFee,
                                       double secondTimeAfterGapFee,double addFee,double feeLimit,int freeDurationEx,
                                       int firstTimeEx,double firstTimeFeeEx,double secondTimeAfterGapFeeEx,double addFeeEx,
                                       double feeLimitEx,String isFeeSection,String feeStartTime,String feeEndTime,
                                       double totalFeeLimit){
        double chargeFee = 0;
        boolean ret = false;
        Date dtBeginEx = new Date();
        Date dtEndEx = new Date();
        Date dtSection = new Date();
        Date dtSectionEx = new Date();
        Date beginTemp = new Date();
        Date endTemp = new Date();
        Date[] dateArr = new Date[2];
        dateArr[0] = beginTemp;
        dateArr[1] = endTemp;
        try {
            if (parkingMin <= freeDuration)
                return chargeFee;
            else {
                while (!ret){
                    ret = toSectionDateTimeNew(inTime,outTime,dateArr,feeStartTime);
                    dtBeginEx = dateArr[0];
                    dtEndEx = dateArr[1];
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String timeStr = sdf.format(dtBeginEx) + " " + feeStartTime;
                    String timeStrEx = sdf.format(dtBeginEx) + " " + feeEndTime;
                    dtSection = Timestamp.valueOf(timeStr);
                    dtSectionEx = Timestamp.valueOf(timeStrEx);
                    double feeSectionLimit = 0;

                    if ("1".equals(isFeeSection)){  //分段方式计算
                        if (dtBeginEx.getTime() < dtSection.getTime()){
                            feeSectionLimit = totalFeeLimit > 0 ? totalFeeLimit : feeLimitEx;

                            chargeFee = chargeFee + calSectionFeeNew(dtBeginEx,dtEndEx,freeDuration,firstTimeEx,firstTimeFeeEx,secondTimeAfterGapFeeEx,feeSectionLimit);
                        }
                        else {
                            if (dtBeginEx.getTime() < dtSectionEx.getTime()){
                                if (dtEndEx.getTime() <= dtSectionEx.getTime()){
                                    feeSectionLimit = totalFeeLimit > 0 ? totalFeeLimit : feeLimit;
                                    chargeFee = chargeFee + calSectionFeeNew(dtBeginEx,dtEndEx,freeDuration,firstTime,firstTimeFee,secondTimeAfterGapFee,feeSectionLimit);
                                }
                                else {
                                    double tempFee = calSectionFeeNew(dtBeginEx,dtSectionEx,freeDuration,firstTime,firstTimeFee,secondTimeAfterGapFee,totalFeeLimit>0 ? 0 : feeLimit)
                                            + calSectionFeeNew(dtSectionEx,dtEndEx,freeDurationEx,firstTimeEx,firstTimeFeeEx,secondTimeAfterGapFeeEx,totalFeeLimit>0 ? 0 : feeLimitEx);
                                    chargeFee = chargeFee + (totalFeeLimit > 0 ? (totalFeeLimit > tempFee ? tempFee : totalFeeLimit) : tempFee);
                                }
                            }
                            else {
                                chargeFee = chargeFee + calSectionFeeNew(dtBeginEx,dtEndEx,freeDurationEx,firstTimeEx,firstTimeFeeEx,secondTimeAfterGapFeeEx,totalFeeLimit>0 ? totalFeeLimit : feeLimitEx);
                            }
                        }
                    }
                    else {     //以出场时间计算
                        if (dtBeginEx.getTime() < dtSection.getTime())
                            chargeFee = chargeFee + calSectionFeeNew(dtBeginEx,dtEndEx,freeDurationEx,firstTimeEx,firstTimeFeeEx,secondTimeAfterGapFeeEx,totalFeeLimit>0 ? totalFeeLimit : feeLimitEx);
                        else {
                            if (dtEndEx.getTime() <= dtSectionEx.getTime())
                                chargeFee = chargeFee + calSectionFeeNew(dtBeginEx,dtEndEx,freeDuration,firstTime,firstTimeFee,secondTimeAfterGapFee,totalFeeLimit>0 ? totalFeeLimit : feeLimit);
                            else
                                chargeFee = chargeFee + calSectionFeeNew(dtBeginEx,dtEndEx,freeDurationEx,firstTimeEx,firstTimeFeeEx,secondTimeAfterGapFeeEx,totalFeeLimit>0 ? totalFeeLimit : feeLimitEx);
                        }

                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("计费模板4计费错误");
        }


        return chargeFee;
    }

    /**
     * 按次（绝对时段）计费
     * 模板5
     */
    @Override
    public double chargeByCountAbsoluteNew(String carno,String parkId,Date inTime,Date outTime,int parkingMin,int freeDuration,int firstTime,double firstTimeFee,
                                        double secondTimeAfterGapFee,double addFee,double feeLimit,int freeDurationEx,
                                        int firstTimeEx,double firstTimeFeeEx,double secondTimeAfterGapFeeEx,double addFeeEx,
                                        double feeLimitEx,String isFeeSection,String feeStartTime,String feeEndTime,
                                        double totalFeeLimit){
        double chargeFee = 0;

        try {
            if (parkingMin > freeDuration){
                boolean ret = false;
                Date dtBeginEx = new Date();
                Date dtEndEx = new Date();
                Date dtSection = new Date();
                Date dtSectionEx = new Date();
                Date beginTemp = new Date();
                Date endTemp = new Date();
                Date[] dateArr = new Date[2];
                dateArr[0] = beginTemp;
                dateArr[1] = endTemp;
                outTime = DateUtils.addMinute(outTime,-freeDuration);
                while (!ret){
                    ret = toSectionDateTimeNew(inTime,outTime,dateArr,feeStartTime);
                    dtBeginEx = dateArr[0];
                    dtEndEx = dateArr[1];
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String timeStr = sdf.format(dtBeginEx) + " " + feeStartTime;
                    String timeStrEx = sdf.format(dtBeginEx) + " " + feeEndTime;
                    dtSection = Timestamp.valueOf(timeStr);
                    dtSectionEx = Timestamp.valueOf(timeStrEx);

                    if (dtBeginEx.getTime() < dtSection.getTime()){
                        if ("1".equals(isFeeSection)){
                            chargeFee = chargeFee + judgeLastDayFeeNew(carno,parkId,dtBeginEx,dtEndEx,DateUtils.addMinute(dtSection,-1440),
                                    DateUtils.addMinute(dtSectionEx,-1440),firstTimeFee,
                                    firstTimeFeeEx,totalFeeLimit,freeDuration);
                        }
                        else{
                            if (totalFeeLimit > firstTimeFeeEx)
                                chargeFee = chargeFee + firstTimeFeeEx;
                            else
                                chargeFee = chargeFee + (totalFeeLimit > 0 ? totalFeeLimit :firstTimeFeeEx);
                        }

                    } else {
                        if (dtBeginEx.getTime() < dtSectionEx.getTime()){
                            if (dtEndEx.getTime() <= dtSectionEx.getTime()){
                                if ("1".equals(isFeeSection)){
                                    chargeFee = chargeFee + judgeLastDayFeeNew(carno,parkId,dtBeginEx,dtEndEx,dtSection,dtSectionEx,
                                            firstTimeFee,firstTimeFeeEx,totalFeeLimit,freeDuration);
                                }
                                else{
                                    if (totalFeeLimit > firstTimeFee)
                                        chargeFee = chargeFee + firstTimeFee;
                                    else
                                        chargeFee = chargeFee + (totalFeeLimit > 0 ? totalFeeLimit :firstTimeFee);
                                }

                            }
                            else { //2
                                if ("1".equals(isFeeSection)){
                                    chargeFee = chargeFee + judgeLastDayFeeNew(carno,parkId,dtBeginEx,dtEndEx,dtSection,dtSectionEx,
                                            firstTimeFee,firstTimeFeeEx,totalFeeLimit,freeDuration);
                                }
                                else{
                                    if (totalFeeLimit > (firstTimeFee + firstTimeFeeEx))
                                        chargeFee = chargeFee + firstTimeFee + firstTimeFeeEx;
                                    else
                                        chargeFee = chargeFee + (totalFeeLimit > 0 ? totalFeeLimit :(firstTimeFee + firstTimeFeeEx));
                                }
                            }
                        }
                        else{
                            if ("1".equals(isFeeSection)){
                                chargeFee = chargeFee + judgeLastDayFeeNew(carno,parkId,dtBeginEx,dtEndEx,dtSection,dtSectionEx,
                                        firstTimeFee,firstTimeFeeEx,totalFeeLimit,freeDuration);
                            }
                            else{
                                if (totalFeeLimit > firstTimeFeeEx)
                                    chargeFee = chargeFee + firstTimeFeeEx;
                                else
                                    chargeFee = chargeFee + (totalFeeLimit > 0 ? totalFeeLimit :firstTimeFeeEx);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("计费模板5计费错误");
        }

        return chargeFee;
    }

    /**
     * 按次（绝对时段）计费2
     * 模板6
     */
    @Override
    public double chargeByCountAbsoluteAddNew(String carno,String parkId,Date inTime,Date outTime,int parkingMin,int freeDuration,int firstTime,double firstTimeFee,
                                           double secondTimeAfterGapFee,double addFee,double feeLimit,int freeDurationEx,
                                           int firstTimeEx,double firstTimeFeeEx,double secondTimeAfterGapFeeEx,double addFeeEx,
                                           double feeLimitEx,String isFeeSection,String feeStartTime,String feeEndTime,
                                           double totalFeeLimit) {
        double chargeFee = 0;

        try {
            if (parkingMin > freeDuration){
                boolean ret = false;
                Date dtBeginEx = new Date();
                Date dtEndEx = new Date();
                Date dtSection = new Date();
                Date dtSectionEx = new Date();
                Date beginTemp = new Date();
                Date endTemp = new Date();
                Date[] dateArr = new Date[2];
                dateArr[0] = beginTemp;
                dateArr[1] = endTemp;
                outTime = DateUtils.addMinute(outTime,-freeDuration);
                while (!ret){
                    ret = toSectionDateTimeNew(inTime,outTime,dateArr,feeStartTime);
                    dtBeginEx = dateArr[0];
                    dtEndEx = dateArr[1];
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String timeStr = sdf.format(dtBeginEx) + " " + feeStartTime;
                    String timeStrEx = sdf.format(dtBeginEx) + " " + feeEndTime;
                    dtSection = Timestamp.valueOf(timeStr);
                    dtSectionEx = Timestamp.valueOf(timeStrEx);

                    if (dtBeginEx.getTime() < dtSection.getTime()) {
                        chargeFee = chargeFee + judgeSectionInoutFeeNew(carno, parkId, dtBeginEx, dtEndEx, freeDuration,DateUtils.addMinute(dtSection, -1440),
                                DateUtils.addMinute(dtSectionEx, -1440), firstTimeFee,secondTimeAfterGapFee,addFee,
                                firstTimeFeeEx, secondTimeAfterGapFeeEx,addFeeEx,totalFeeLimit);
                    }
                    else {
                        chargeFee = chargeFee + judgeSectionInoutFeeNew(carno, parkId, dtBeginEx, dtEndEx, freeDuration,dtSection,
                                dtSectionEx, firstTimeFee,secondTimeAfterGapFee,addFee,
                                firstTimeFeeEx, secondTimeAfterGapFeeEx,addFeeEx,totalFeeLimit);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("计费模板6计费错误");
        }

        return chargeFee;
    }
    /**
     *  重置起始时间
     */
    @Override
    public boolean toSectionDateTimeNew(Date beginTime,Date endTime,Date[] dateTempArr,String dateTimeSection){
        boolean res = true;
        try {
            if (beginTime.getTime() > endTime.getTime())
                return res;

            Date timeSectionTemp;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            String timeStr = sdf.format(beginTime);
            timeStr += " " + dateTimeSection;
            timeSectionTemp = Timestamp.valueOf(timeStr);

            if (beginTime.getTime() < timeSectionTemp.getTime()){
                dateTempArr[0].setTime(beginTime.getTime());
                if (endTime.getTime() < timeSectionTemp.getTime())
                    dateTempArr[1].setTime(endTime.getTime());
                else {
                    dateTempArr[1].setTime(timeSectionTemp.getTime());
                    beginTime.setTime(timeSectionTemp.getTime());
                    res = false;
                }
            } else {
                dateTempArr[0].setTime(beginTime.getTime());
                Calendar cal = Calendar.getInstance();
                cal.setTime(timeSectionTemp);
                cal.add(Calendar.DATE,1);
                timeSectionTemp = cal.getTime();
                if (endTime.getTime() <= timeSectionTemp.getTime())
                    dateTempArr[1].setTime(endTime.getTime());
                else{
                    dateTempArr[1].setTime(timeSectionTemp.getTime());
                    beginTime.setTime(timeSectionTemp.getTime());
                    res = false;
                }
            }
        } catch (Exception e) {
            LOGGER.error("重置起始时间错误");
        }
        return res;
    }


    /**
     * 分段计费
     */
    @Override
    public double calSectionFeeNew(Date inTime,Date outTime,int freeDuration,int firstTime,double firstTimeFee,
                                double secondTimeAfterGapFee,double feeLimit) {
        double sectionFee = 0;
        try {
            int sectionMin = 0;

            long parkingSec = (outTime.getTime()-inTime.getTime())/1000;
            sectionMin = (int)(parkingSec/60);    //分段停车时长

            if (sectionMin > freeDuration){
                if (sectionMin <= firstTime * 60)
                    sectionFee = ((sectionMin - 1) / 60 + 1) * firstTimeFee;
                else
                    sectionFee = (firstTime * firstTimeFee) + ((sectionMin - 1) / 60 + 1 - firstTime) * secondTimeAfterGapFee;

                if (sectionFee > feeLimit && feeLimit > 0)
                    sectionFee = feeLimit;
            }
        } catch (Exception e) {
            LOGGER.error("分段计费错误");
        }

        return sectionFee;
    }

    /**
     * 判断过去24小时是否收费
     * @param carno
     * @param inTime
     * @param outTime
     * @param beginTime
     * @param endTime
     * @param feeFirst
     * @param feeSec
     * @param totalLimit
     * @param freeTime
     * @return
     * @throws Exception
     */
    @Override
    public double judgeLastDayFeeNew(String carno, String parkId,Date inTime,Date outTime,Date beginTime,Date endTime,
                                  double feeFirst,double feeSec,double totalLimit,int freeTime){
        double alChargeFee = 0;
        try {
            Date sqlBeginTime = DateUtils.addMinute(beginTime,freeTime);
            Date sqlEndTime = DateUtils.addMinute(beginTime,1440);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String beginStr = sdf.format(sqlBeginTime);
            String endStr = sdf.format(sqlEndTime);
            Timestamp beginStamp = Timestamp.valueOf(beginStr);
            Timestamp endStamp = Timestamp.valueOf(endStr);
            Timestamp outTimeTemp = null;
            Date dayOutTime;

            String sqlStr = "select charge_receivable_amount,out_time from order_inout_record where car_no = :carNo and carpark_id = :parkId and out_time < :endStamp"
                    + " and out_time > :beginStamp and use_mark >= 0";
            SQLQuery sqlQuery = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sqlStr);
            sqlQuery.setParameter("carNo",carno);
            sqlQuery.setParameter("parkId",parkId);
            sqlQuery.setParameter("endStamp",endStamp);
            sqlQuery.setParameter("beginStamp",beginStamp);

            sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List<Map> paidList = (List<Map>)sqlQuery.list();
            for(Map map : paidList){
                if(Double.valueOf(map.get("charge_receivable_amount").toString()) > 0){
                    outTimeTemp = (Timestamp)(map.get("out_time"));
                }
            }
            if (!CommonUtils.isEmpty(outTimeTemp)){
                dayOutTime = Timestamp.valueOf(outTimeTemp.toString());
                if (dayOutTime.getTime() <= DateUtils.addMinute(endTime,freeTime).getTime()
                        && (outTime.getTime() > endTime.getTime())){
                    if (totalLimit >= (feeFirst+feeSec))
                        alChargeFee = feeSec;
                    else
                        alChargeFee = (totalLimit > 0 ? totalLimit : (feeFirst + feeSec))-feeFirst;
                }
            }
            else {
                if (inTime.getTime() < endTime.getTime() && outTime.getTime() <= endTime.getTime())
                    alChargeFee = totalLimit > 0 ? (Math.min(totalLimit,feeFirst)) : feeFirst;

                if (inTime.getTime() < endTime.getTime() && outTime.getTime() > endTime.getTime())
                    alChargeFee = totalLimit > 0 ? (Math.min(totalLimit,feeFirst+feeSec)) : (feeFirst+feeSec);

                if (inTime.getTime() >= endTime.getTime() && outTime.getTime() > endTime.getTime())
                    alChargeFee = totalLimit > 0 ? (Math.min(totalLimit,feeSec)) : feeSec;
            }
        } catch (Exception e) {
            LOGGER.error("判断过去24小时是否计费错误");
        }

        return alChargeFee;
    }

    /**
     *
     */
    @Override
    public double judgeSectionInoutFeeNew(String carno,String parkId,Date inTime,Date outTime,int freeTime,Date beginTime,Date endTime,
                                       double feeFirst,double feeSec,double feeThird,double feeFour,double feeFive,
                                       double feeSix,double totalLimit) {
        double alChargeFee = 0;
        try {
            Date sqlBeginTime = beginTime;
            Date sqlEndTime = DateUtils.addMinute(beginTime,1440);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String beginStr = sdf.format(sqlBeginTime);
            String endStr = sdf.format(sqlEndTime);
            Timestamp beginStamp = Timestamp.valueOf(beginStr);
            Timestamp endStamp = Timestamp.valueOf(endStr);
            Timestamp outTimeTemp = null;
            Date dayOutTime;
            double fee = 0;
            double feeTotal = 0;
            int inoutDayCount = 0;
            int inoutNightCount = 0;
            boolean isFirst = true;

            String sqlStr = "select charge_receivable_amount,out_time from order_inout_record where car_no = :carNo and carpark_id = :parkId and out_time < :endStamp and out_time > :beginStamp and use_mark >= 0";
            SQLQuery sqlQuery = hibernateBaseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sqlStr);
            sqlQuery.setParameter("carNo",carno);
            sqlQuery.setParameter("parkId",parkId);
            sqlQuery.setParameter("endStamp",endStamp);
            sqlQuery.setParameter("beginStamp",beginStamp);

            sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List<Map> paidList = (List<Map>)sqlQuery.list();
            for(Map map : paidList){
                fee = Double.valueOf(map.get("charge_receivable_amount").toString());
                outTimeTemp = (Timestamp)(map.get("out_time"));
                dayOutTime = Timestamp.valueOf(outTimeTemp.toString());
                if (dayOutTime.getTime() <= endTime.getTime() &&
                        dayOutTime.getTime() > (DateUtils.addMinute(beginTime,freeTime).getTime())){
                    inoutDayCount = inoutDayCount + 1;
                    if (isFirst){
                        isFirst = false;
                        feeTotal = feeTotal + feeFirst;
                    }
                    else
                        feeTotal = feeTotal + fee;
                }

                if (dayOutTime.getTime() > endTime.getTime() &&
                        dayOutTime.getTime() > (DateUtils.addMinute(endTime,freeTime).getTime())){
                    inoutNightCount = inoutNightCount + 1;
                    if (isFirst){
                        isFirst = false;
                        feeTotal = feeTotal + feeFour;
                    }
                    else
                        feeTotal = feeTotal + fee;
                }
            }

            if (inTime.getTime() < endTime.getTime() && outTime.getTime() <= endTime.getTime()){
                switch (inoutDayCount) {
                    case 0 :
                        alChargeFee = feeFirst;
                        break;
                    case 1 :
                        alChargeFee = feeSec;
                        break;
                    default:
                        alChargeFee = feeThird;
                        break;
                }
            }

            if (inTime.getTime() < endTime.getTime() && outTime.getTime() > endTime.getTime()){
                switch (inoutDayCount) {
                    case 0 :
                        alChargeFee = feeFirst;
                        break;
                    case 1 :
                        alChargeFee = feeSec;
                        break;
                    default:
                        alChargeFee = feeThird;
                        break;
                }
                alChargeFee = alChargeFee + feeFour;
            }

            if (inTime.getTime() >= endTime.getTime() && outTime.getTime() > endTime.getTime()){
                switch (inoutNightCount) {
                    case 0 :
                        alChargeFee = feeFour;
                        break;
                    case 1 :
                        alChargeFee = feeFive;
                        break;
                    default:
                        alChargeFee = feeSix;
                        break;
                }
            }

            if (totalLimit > 0){
                if (totalLimit < (alChargeFee + feeTotal))
                    alChargeFee = totalLimit - feeTotal;
            }
            if (alChargeFee < 0)
                alChargeFee = 0;
        } catch (HibernateException e) {
            LOGGER.error("判断分段计费错误");
        }

        return alChargeFee;
    }
}
