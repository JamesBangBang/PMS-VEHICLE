package com.starnetsecurity.parkClientServer.service;

import com.starnetsecurity.parkClientServer.entity.AdminUser;
import com.starnetsecurity.parkClientServer.entity.CarparkInfo;
import com.starnetsecurity.parkClientServer.entity.InOutCarRoadInfo;

import java.util.List;
import java.util.Map;

public interface CarparkService {

    List<Map<String,Object>> getCaparks(String userId);

    CarparkInfo getRemainPark(String parkId);

    List<Object[]> getRemainParkhistory(String userId);

    List<Map<String, Object>>  getParkId(AdminUser adminUser);

    List getCarParksInfoList(List<CarparkInfo> carparkInfos, Integer start, Integer length);

    Long countCarParksInfo(List<CarparkInfo> carparkInfos);

    Map<String,Object> getCarParksInfo(String carParkId);

    List<Map<String,Object>> getCarParksInfoSelect(List<String> depId);

    List<String> getUserCarParksIdList(String userId);

    void  saveParks(CarparkInfo carparkInfo,String ip,AdminUser adminUser);

    List getLastCarparkInOutRecord(String carparId);

    List<Map<String, Object>> getUserDepartments(String userId);

    List<Map<String, Object>> getOwnCarparkInfo(String userId);

    List<Map<String, Object>> getUserDepartmentsParks(String userId, String departId);

    boolean isCarparkNameRepeat(String controlMode, String departId, String carparkId, String carparkName);

    void insertCarparkInfo(Map carparkInfo,AdminUser adminUser,String ip);

    void deleteCarparkInfo(String  carparkInfoId,AdminUser adminUser,String ip);


    List getAllCarParksInfo();

    List getAllPostInfo();

    void saveCarRoad(Map params,AdminUser adminUser,String ip);

    boolean isisCarRoadNameRepeat(Map params);

    List getCarRoadInfoList(List<Map<String, Object>> caparksOld, Integer start, Integer length);

    Long countCarRoadInfoList(List<Map<String, Object>> caparksOld);

    void deleteCarRoadInfo(String id,AdminUser adminUser,String ip);

    Map detailsCarRoadInfo(Map params);

    void updateCarRoad(InOutCarRoadInfo inOutCarRoadInfo,Map params,AdminUser adminUser,String ip);

    InOutCarRoadInfo getRemainCarRoad(String carRoadId);

    Integer get7DayInCount(String parId);

    String  getCparkIdByName(String parkName);

    boolean controlFleetMode(String controlMode,String roadId);
}
