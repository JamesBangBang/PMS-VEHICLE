package com.starnetsecurity.parkClientServer.service;

import java.util.List;
import java.util.Map;

public interface ChargeRecordsService {
    public List<Object[]> getChargeRecords(String parkId);
    public Double getChargeRecordsToday(String parkId);
    public List<Map>  getChargeRecordsTodayDetail(String parkId);
    public List<Map> getCarparkChargeToday(Integer pageNum, Integer pageSize, String parkId);
    public Long getCarparkChargeTodayCount(Integer pageNum, Integer pageSize, String parkId);
    public List<Map> getOnlineCharge(Integer pageNum, Integer pageSize, String parkId, String start, String end, String carName);
    public Long getOnlineChargeCount(Integer pageNum, Integer pageSize, String parkId, String start, String end, String carName);
    public Long getChargeHistoryCount(Integer pageNum, Integer pageSize, String parkId, String start, String end, String carName);
    public List<Map> getChargeHistory(Integer pageNum, Integer pageSize, String parkId, String start, String end, String carName);
    public List<Map> getChargeRules(String userId, Integer pageNum, Integer pageSize);
    public Long getChargeRulesCount(String userId);
//    public Map getChargeRulesInfo(String carParkId);

//    public List<Map> getChargeRulesDetail(String parkId);

    List getPreChargeHistoryData(Integer pageNum, Integer pageSize, String parkId, String start, String end, String carName);
    Long getPreChargeHistoryCount(String parkId, String start, String end, String carName);

}
