package com.starnetsecurity.parkClientServer.service;

import java.util.List;
import java.util.Map;

public interface UnusualRecordsService {

    List<Object[]> getUnusualRecords(String parkId);

    Integer getUnusualRecordsToday(String parkId);
}
