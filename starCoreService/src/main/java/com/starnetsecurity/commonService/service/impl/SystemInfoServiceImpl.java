package com.starnetsecurity.commonService.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.DBObject;
import com.starnetsecurity.common.mongo.MongoTable;
import com.starnetsecurity.common.mongoDao.MongoBaseDao;
import com.starnetsecurity.commonService.service.SystemInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Created by 宏炜 on 2017-07-10.
 */
@Service
public class SystemInfoServiceImpl implements SystemInfoService {

    @Autowired
    MongoBaseDao mongoBaseDao;

    @Override
    public void mergeSystemInfo(JSONObject jsonObject) throws Exception {
        mongoBaseDao.insert(jsonObject, MongoTable.system_info);
    }

    public DBObject getSystemInfo() throws Exception{
        return (DBObject) mongoBaseDao.findOne(Collections.EMPTY_MAP,DBObject.class,MongoTable.system_info);
    }
}
