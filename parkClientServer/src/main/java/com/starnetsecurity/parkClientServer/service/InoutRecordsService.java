package com.starnetsecurity.parkClientServer.service;

import com.mongodb.DBObject;

import java.util.List;
import java.util.Map;

public interface InoutRecordsService {
    public List<Object[]> getInOutRecords(String parkId);
    public Integer getInOutRecordsToday(String parkId);
    public List<Map> getInOutRecordsTodayDetail(String parkId);
//    public List<DBObject> getInOutRecordsTodayRun(String parkId);
    public List<Map> getCarparkOutToday(Integer pageNum, Integer pageSize, String parkId);
    public Long getCarparkOutTodayCount(Integer pageNum, Integer pageSize, String parkId);
    public List<Map> getCarparkOutTodaydetail(Integer pageNum, Integer pageSize, String parkId, String start, String end, String carName);
    public Long getCarparkOutTodaydetailCount(Integer pageNum, Integer pageSize, String parkId, String start, String end, String carName);
}
