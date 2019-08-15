package com.starnetsecurity.parkClientServer.service;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by JAMESBANG on 2017/11/17.
 */
public interface CalculateChargeService {
    double calculateParkingFee(String carno, String userType, String carType, String parkId, Timestamp inTime, Timestamp outTime) throws Exception;

    double chargeByTimeRelative(int parkingMin,int freeDuration,int firstTime,double firstTimeFee,
                                double secondTimeAfterGapFee,double addFee,double feeLimit,int freeDurationEx,
                                int firstTimeEx,double firstTimeFeeEx,double secondTimeAfterGapFeeEx,double addFeeEx,
                                double feeLimitEx,String isFeeSection,String feeStartTime,String feeEndTime,
                                double totalFeeLimit) throws Exception;

    double chargeByCountRelative(int parkingMin,int freeDuration,int firstTime,double firstTimeFee,
                                 double secondTimeAfterGapFee,double addFee,double feeLimit,int freeDurationEx,
                                 int firstTimeEx,double firstTimeFeeEx,double secondTimeAfterGapFeeEx,double addFeeEx,
                                 double feeLimitEx,String isFeeSection,String feeStartTime,String feeEndTime,
                                 double totalFeeLimit) throws Exception;

    double chargeByTimeAllDay(Date inTime, Date outTime, int parkingMin, int freeDuration, int firstTime, double firstTimeFee,
                              double secondTimeAfterGapFee, double addFee, double feeLimit, int freeDurationEx,
                              int firstTimeEx, double firstTimeFeeEx, double secondTimeAfterGapFeeEx, double addFeeEx,
                              double feeLimitEx, String isFeeSection, String feeStartTime, String feeEndTime,
                              double totalFeeLimit) throws Exception;

    double chargeByTimeAbsolute(Date inTime,Date outTime,int parkingMin,int freeDuration,int firstTime,double firstTimeFee,
                                double secondTimeAfterGapFee,double addFee,double feeLimit,int freeDurationEx,
                                int firstTimeEx,double firstTimeFeeEx,double secondTimeAfterGapFeeEx,double addFeeEx,
                                double feeLimitEx,String isFeeSection,String feeStartTime,String feeEndTime,
                                double totalFeeLimit) throws Exception;

    double chargeByCountAbsolute(String carno,String serviceParkId,Date inTime,Date outTime,int parkingMin,int freeDuration,int firstTime,double firstTimeFee,
                                 double secondTimeAfterGapFee,double addFee,double feeLimit,int freeDurationEx,
                                 int firstTimeEx,double firstTimeFeeEx,double secondTimeAfterGapFeeEx,double addFeeEx,
                                 double feeLimitEx,String isFeeSection,String feeStartTime,String feeEndTime,
                                 double totalFeeLimit) throws Exception;

    double chargeByCountAbsoluteAdd(String carno,String serviceParkId,Date inTime,Date outTime,int parkingMin,int freeDuration,int firstTime,double firstTimeFee,
                                    double secondTimeAfterGapFee,double addFee,double feeLimit,int freeDurationEx,
                                    int firstTimeEx,double firstTimeFeeEx,double secondTimeAfterGapFeeEx,double addFeeEx,
                                    double feeLimitEx,String isFeeSection,String feeStartTime,String feeEndTime,
                                    double totalFeeLimit) throws Exception;

    boolean toSectionDateTime(Date beginTime,Date endTime,Date beginTemp,Date endTemp,String dateTimeSection) throws Exception;

    /**
     * 分段计费
     * @return
     * @throws Exception
     */
    double calSectionFee(Date inTime,Date outTime,int freeDuration,int firstTime,double firstTimeFee,
                         double secondTimeAfterGapFee,double feeLimit) throws Exception;

    /**
     * 判断过去24小时是否缴费
     */
    double judgeLastDayFee(String carno,String parkId,Date inTime,Date outTime,Date beginTime,Date endTime,
                           double feeFirst,double feeSec,double totalLimit,int freeTime) throws Exception;

    /**
     * 判断过去是否有进出场缴费
     */
    double judgeSectionInoutFee(String carno,String parkId,Date inTime,Date outTime,int freeTime,Date beginTime,Date endTime,
                                double feeFirst,double feeSec,double feeThird,double feeFour,double feeFive,
                                double feeSix,double totalLimit) throws Exception;
}
