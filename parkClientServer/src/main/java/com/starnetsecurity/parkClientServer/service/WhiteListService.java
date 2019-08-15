package com.starnetsecurity.parkClientServer.service;

import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.parkClientServer.entity.AdminUser;

import java.util.List;
import java.util.Map;

/**
 * Created by JamesBangBang on 2017/10/26.
 */
public interface WhiteListService {
    List<Map<String, Object>> getWhiteList(String carNo,
                                                  String chargeTypeName,
                                                  String driverName,
                                                  String driverTelephoneNumber,
                                                  String driverInfo,
                                                  String carparkId,
                                                  String depId,
                                                  String packageKind,
                                                  String overdueTime,
                                                  Integer start,
                                                  Integer length) throws BizException;

    Integer getWhiteListCount(String carNo,
                                     String chargeTypeName,
                                     String driverName,
                                     String driverTelephoneNumber,
                                     String driverInfo,
                                     String carparkId,
                                     String depId,
                                     String overdueTime) throws BizException;

    List getAllCarPark();

    List getAllPackage(String carparkId);

    /**
     * 新增白名单
     * @param params
     * @param adminUser
     * @param ip
     * @throws BizException
     */
    void saveOwner(Map params,AdminUser adminUser,String ip) throws BizException;

    /**
     * 删除白名单
     * @param id
     * @param ip
     */
    void delOwner(String id,String ip);

    List editOwner(String id);

    boolean isCarNo(Map params);

    /**
     * 更新白名单
     * @param params
     * @param adminUser
     * @param ip
     * @throws BizException
     */
    void updateOwner(JSONObject params, AdminUser adminUser,String ip) throws BizException;

    List<Map<String, Object>> getWhiteListExport(String carNo,
                                                  String chargeTypeName,
                                                  String driverName,
                                                  String driverTelephoneNumber,
                                                  String driverInfo,
                                                  String carparkId,
                                                  String depId,
                                                  String packageKind) throws BizException;

    void  saveOwnerList(List<Map<String, Object>> params,AdminUser adminUser) throws BizException;


}
