package com.starnetsecurity.parkClientServer.service.impl;

import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.util.Constant;
import com.starnetsecurity.parkClientServer.clientEnum.LogEnum;
import com.starnetsecurity.parkClientServer.entity.AdminUser;
import com.starnetsecurity.parkClientServer.entity.DepartmentInfo;
import com.starnetsecurity.parkClientServer.entity.Operator;
import com.starnetsecurity.parkClientServer.entity.OperatorLog;
import com.starnetsecurity.parkClientServer.service.LogOperationService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

/**
 * Created by JAMESBANG on 2018/1/23.
 */
@Service
public class LogOperationServiceImpl implements LogOperationService {
    @Autowired
    HibernateBaseDao baseDao;

    @Override
    public void addOperatorLog(LogEnum logEnum, String logContent,String ip,Operator operator,AdminUser adminUser) {

        String hql = "from DepartmentInfo where useMark >= 0";
        DepartmentInfo departmentInfo = (DepartmentInfo)baseDao.getUnique(hql);
        if(!CommonUtils.isEmpty(adminUser)){
            OperatorLog operatorLog = new OperatorLog();
            operatorLog.setLogType(String.valueOf(logEnum.ordinal()));
            operatorLog.setLogTypeName(logEnum.getDesc());
            operatorLog.setLogSource("ADMIN:(" + ip + ")");
            operatorLog.setOperatorNo(adminUser.getId());
            operatorLog.setOperatorName(adminUser.getUserName());
            operatorLog.setLogDesc(logContent);
            operatorLog.setAddTime(CommonUtils.getTimestamp());
            operatorLog.setDepartmentId(departmentInfo.getDepId());
            baseDao.save(operatorLog);
        }else{
            OperatorLog operatorLog = new OperatorLog();
            operatorLog.setLogType(String.valueOf(logEnum.ordinal()));
            operatorLog.setLogTypeName(logEnum.getDesc());
            operatorLog.setLogSource("OPERATOR:(" + ip + ")");
            if (!CommonUtils.isEmpty(operator)) {
                operatorLog.setOperatorNo(operator.getOperatorId());
                operatorLog.setOperatorName(operator.getOperatorName());
            }
            operatorLog.setLogDesc(logContent);
            operatorLog.setAddTime(CommonUtils.getTimestamp());
            operatorLog.setDepartmentId(departmentInfo.getDepId());
            baseDao.save(operatorLog);
        }


    }

}
