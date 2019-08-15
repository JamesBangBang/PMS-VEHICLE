package com.starnetsecurity.parkClientServer.service;

import com.starnetsecurity.parkClientServer.clientEnum.LogEnum;
import com.starnetsecurity.parkClientServer.entity.AdminUser;
import com.starnetsecurity.parkClientServer.entity.Operator;

/**
 * Created by JAMESBANG on 2018/1/23.
 */
public interface LogOperationService {

    void addOperatorLog(LogEnum logEnum, String logContent, String ip, Operator operator, AdminUser adminUser);
}
