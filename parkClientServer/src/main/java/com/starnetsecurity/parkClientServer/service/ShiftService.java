package com.starnetsecurity.parkClientServer.service;

import java.util.List;
import java.util.Map;

public interface ShiftService {
    public List<Map> getShiftMapInfoList(Integer pageNum, Integer pageSize, String parkId, String boxName);
    public Long getShiftMapInfoCount(Integer pageNum, Integer pageSize, String parkId, String start, String end, String boxName);

}
