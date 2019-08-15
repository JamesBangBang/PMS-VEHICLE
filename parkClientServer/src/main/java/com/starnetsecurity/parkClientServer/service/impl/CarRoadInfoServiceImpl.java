package com.starnetsecurity.parkClientServer.service.impl;

import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.parkClientServer.entity.InOutCarRoadInfo;
import com.starnetsecurity.parkClientServer.service.CarRoadInfoService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 宏炜 on 2018-01-09.
 */
@Service
public class CarRoadInfoServiceImpl implements CarRoadInfoService {

    @Autowired
    HibernateBaseDao baseDao;

    @Override
    public List getCarRoadSelect() {

        String hql = "from InOutCarRoadInfo where useMark >= 0";
        List<InOutCarRoadInfo> inOutCarRoadInfos = (List<InOutCarRoadInfo>)baseDao.queryForList(hql);

        List<Map> list = new ArrayList<>();

        for(InOutCarRoadInfo inOutCarRoadInfo : inOutCarRoadInfos){
            Map params = new HashedMap();
            params.put("id",inOutCarRoadInfo.getCarRoadId());
            params.put("name",inOutCarRoadInfo.getCarRoadName());
            list.add(params);
        }
        return list;
    }
}
