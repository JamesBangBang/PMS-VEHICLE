package com.starnetsecurity.parkClientServer.service;

import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.parkClientServer.entity.MemberWallet;

import java.util.List;
import java.util.Map;

/**
 * Created by 陈峰 on 2017/10/20.
 */
public interface ReservationService {
    List<Map<String, Object>> getReservattionList(Integer start,
                                                         Integer length,
                                                         String reservationTypeName,
                                                         String carNo,
                                                         String driverName,
                                                         String driverTelephoneNumber,
                                                         String driverInfo,
                                                         String beginTimeMin,
                                                         String beginTimeMax,
                                                         String endTimeMin,
                                                         String endTimeMax,
                                                         String driverFileId,
                                                         String carparkId,
                                                         String depId) throws BizException;

    Integer getReservattionListCount(Integer start,
                                            Integer length,
                                            String reservationTypeName,
                                            String carNo,
                                            String driverName,
                                            String driverTelephoneNumber,
                                            String driverInfo,
                                            String beginTimeMin,
                                            String beginTimeMax,
                                            String endTimeMin,
                                            String endTimeMax,
                                            String driverFileId,
                                            String carparkId,
                                            String depId) throws BizException;

    Integer deleteReservation(String driverFileId, String memberWalletId, String carNo) throws BizException;

    Integer addReservationRecord(String driverFileId,
                                        String memberWalletId,
                                        String carparkId,
                                        String carNo,
                                        String reservationTypeName,
                                        String driverName,
                                        String driverTelephoneNumber,
                                        String driverInfo,
                                        String effectiveStartTime,
                                        String effectiveEndTime) throws BizException;

    Integer updateReservationRecord(String driverFileId,
                                           String memberWalletId,
                                           String carparkId,
                                           String carNo,
                                           String reservationTypeName,
                                           String driverName,
                                           String driverTelephoneNumber,
                                           String driverInfo,
                                           String effectiveStartTime,
                                           String effectiveEndTime) throws BizException;

    Integer applyReservation(JSONObject params);

    List<JSONObject> showReservationList(JSONObject params);

    Integer handleReservation(JSONObject params);

    Boolean isCarparkRepeat(String carparkId, String carNo, String memberWalletId) throws BizException;

    List<Map<String,Object>> getBlacklist(Integer start,
                                          Integer length,
                                          String carNo,
                                          String driverName,
                                          String carparkId,
                                          String beginTimeMin,
                                          String beginTimeMax,
                                          String endTimeMin,
                                          String endTimeMax,
                                          String depId);

    Integer getBlacklistCount(Integer start,
                           Integer length,
                           String carNo,
                           String driverName,
                           String carparkId,
                           String beginTimeMin,
                           String beginTimeMax,
                           String endTimeMin,
                           String endTimeMax,
                           String depId);

    Boolean isBlacklistCarparkRepeat(String carparkId, String carNo,String memberWalletId);

    void saveBlacklist(Map params);

    List<Map<String,Object>> getEditBlacklist(Map params);

    void delBlacklist(JSONObject params);

}
