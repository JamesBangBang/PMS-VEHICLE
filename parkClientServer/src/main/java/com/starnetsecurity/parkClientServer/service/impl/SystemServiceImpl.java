package com.starnetsecurity.parkClientServer.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.parkClientServer.entity.DepartmentInfo;
import com.starnetsecurity.parkClientServer.entity.MemberKind;
import com.starnetsecurity.parkClientServer.service.SystemService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 宏炜 on 2018-01-22.
 */
@Service
public class SystemServiceImpl implements SystemService {

    @Autowired
    HibernateBaseDao baseDao;

    @Override
    public Map<String, Object> getSystemInfo() {
        String hql = "from DepartmentInfo";
        DepartmentInfo departmentInfo = ((List<DepartmentInfo>)baseDao.queryForList(hql)).get(0);
        Map<String,Object> res = new HashedMap();
        res.put("priorityArea",departmentInfo.getPriorityCity());
        res.put("defaultMemberId",departmentInfo.getDefaultMemberId());
        return res;
    }

    @Override
    public void updateSystemInfo(JSONObject params) {
        String priorityArea = params.getString("priorityArea");
        String defaultMemberId = params.getString("defaultMemberId");
        String hql = "from DepartmentInfo";
        DepartmentInfo departmentInfo = ((List<DepartmentInfo>)baseDao.queryForList(hql)).get(0);
        departmentInfo.setPriorityCity(priorityArea);
        departmentInfo.setDefaultMemberId(defaultMemberId);
        baseDao.update(departmentInfo);
    }

    @Override
    public List<Map<String,Object>> getMemberInfo() {
        List<Map<String,Object>> res = new ArrayList<>();
        String hql = "from MemberKind where carparkInfo is null and useMark >= 0";
        List<MemberKind> list = (List<MemberKind>) baseDao.queryForList(hql);
        for (MemberKind memberKind : list){
            Map<String,Object> map = new HashedMap();
            map.put("memberId",memberKind.getId());
            map.put("memberName",memberKind.getKindName());
            res.add(map);
        }
        return res;
    }
}
