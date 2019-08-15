package com.starnetsecurity.parkClientServer.service;

import com.starnetsecurity.parkClientServer.entity.Operator;

import java.util.List;
import java.util.Map;

public interface OperatorService {
    List<Map> getOperatorhistory(String userId, Integer pageNum, Integer pageSize, String start, String end, String logName);

    Long getOperatorhistoryCount(String userId, Integer pageNum, Integer pageSize, String start, String end, String logName);

    List getOperatorList(Integer start, Integer length);

    Long countOperatorList();

    int isPwdRepeat(Map params);

    void operatorSave(Map params);

    void deleteOperator(String id);

    void editOperator(Operator operator);

    Operator getRemainOperator(String opertorId);

}
