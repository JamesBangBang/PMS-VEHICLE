package com.starnetsecurity.parkClientServer.service.impl;

import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.parkClientServer.entity.DepartmentInfo;
import com.starnetsecurity.parkClientServer.service.DepartmentService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by 宏炜 on 2017-10-24.
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    HibernateBaseDao baseDao;

    @Override
    public List<Map<String,Object>> getDepartmentInfoSelect() {
        String hql = "from DepartmentInfo";
        List<DepartmentInfo> departmentInfos = (List<DepartmentInfo>)baseDao.queryForList(hql);
        List<Map<String,Object>> res = new ArrayList<>();
        for(DepartmentInfo departmentInfo : departmentInfos){
            Map<String,Object> obj = new HashMap<>();
            obj.put("depId",departmentInfo.getDepId());
            obj.put("depName",departmentInfo.getDepName());
            res.add(obj);
        }
        return res;
    }

    @Override
    public List<String> getUserDepartmentIds(String userId) {
        String hql = "select resId from AdminOrgResource where adminUserId = ? and resType = 'DEPARTMENT'";
        List<String> list = (List<String>)baseDao.queryForList(hql,userId);
        if(CollectionUtils.isEmpty(list)){
            return Collections.EMPTY_LIST;
        }else{
            return list;
        }
    }
}
