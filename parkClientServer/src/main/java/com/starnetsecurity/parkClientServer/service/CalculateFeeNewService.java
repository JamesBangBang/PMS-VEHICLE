package com.starnetsecurity.parkClientServer.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by JAMESBANG on 2018/2/1.
 * 用于新版出入口的计费
 */
public interface CalculateFeeNewService {
    double calculateParkingFeeNew(String carno, String userType, String carType, String parkId, Timestamp inTime, Timestamp outTime) throws ParseException;

    double chargeByTimeRelativeNew(int parkingMin,int freeDuration,int firstTime,double firstTimeFee,
                                double secondTimeAfterGapFee,double addFee,double feeLimit,int freeDurationEx,
                                int firstTimeEx,double firstTimeFeeEx,double secondTimeAfterGapFeeEx,double addFeeEx,
                                double feeLimitEx,String isFeeSection,String feeStartTime,String feeEndTime,
                                double totalFeeLimit) ;

    double chargeByCountRelativeNew(int parkingMin,int freeDuration,int firstTime,double firstTimeFee,
                                 double secondTimeAfterGapFee,double addFee,double feeLimit,int freeDurationEx,
                                 int firstTimeEx,double firstTimeFeeEx,double secondTimeAfterGapFeeEx,double addFeeEx,
                                 double feeLimitEx,String isFeeSection,String feeStartTime,String feeEndTime,
                                 double totalFeeLimit) ;

    double chargeByTimeAllDayNew(Date inTime, Date outTime, int parkingMin, int freeDuration, int firstTime, double firstTimeFee,
                              double secondTimeAfterGapFee, double addFee, double feeLimit, int freeDurationEx,
                              int firstTimeEx, double firstTimeFeeEx, double secondTimeAfterGapFeeEx, double addFeeEx,
                              double feeLimitEx, String isFeeSection, String feeStartTime, String feeEndTime,
                              double totalFeeLimit) throws ParseException;

    double chargeByTimeAbsoluteNew(Date inTime,Date outTime,int parkingMin,int freeDuration,int firstTime,double firstTimeFee,
                                double secondTimeAfterGapFee,double addFee,double feeLimit,int freeDurationEx,
                                int firstTimeEx,double firstTimeFeeEx,double secondTimeAfterGapFeeEx,double addFeeEx,
                                double feeLimitEx,String isFeeSection,String feeStartTime,String feeEndTime,
                                double totalFeeLimit);

    double chargeByCountAbsoluteNew(String carno,String serviceParkId,Date inTime,Date outTime,int parkingMin,int freeDuration,int firstTime,double firstTimeFee,
                                 double secondTimeAfterGapFee,double addFee,double feeLimit,int freeDurationEx,
                                 int firstTimeEx,double firstTimeFeeEx,double secondTimeAfterGapFeeEx,double addFeeEx,
                                 double feeLimitEx,String isFeeSection,String feeStartTime,String feeEndTime,
                                 double totalFeeLimit) ;

    double chargeByCountAbsoluteAddNew(String carno,String serviceParkId,Date inTime,Date outTime,int parkingMin,int freeDuration,int firstTime,double firstTimeFee,
                                    double secondTimeAfterGapFee,double addFee,double feeLimit,int freeDurationEx,
                                    int firstTimeEx,double firstTimeFeeEx,double secondTimeAfterGapFeeEx,double addFeeEx,
                                    double feeLimitEx,String isFeeSection,String feeStartTime,String feeEndTime,
                                    double totalFeeLimit) ;

    boolean toSectionDateTimeNew(Date beginTime,Date endTime,Date[] dateTempArr,String dateTimeSection);

    /**
     * 分段计费
     * @return
     * @throws Exception
     */
    double calSectionFeeNew(Date inTime,Date outTime,int freeDuration,int firstTime,double firstTimeFee,
                         double secondTimeAfterGapFee,double feeLimit);

    /**
     * 判断过去24小时是否缴费
     */
    double judgeLastDayFeeNew(String carno,String parkId,Date inTime,Date outTime,Date beginTime,Date endTime,
                           double feeFirst,double feeSec,double totalLimit,int freeTime);

    /**
     * 判断过去是否有进出场缴费
     */
    double judgeSectionInoutFeeNew(String carno,String parkId,Date inTime,Date outTime,int freeTime,Date beginTime,Date endTime,
                                double feeFirst,double feeSec,double feeThird,double feeFour,double feeFive,
                                double feeSix,double totalLimit);
}
