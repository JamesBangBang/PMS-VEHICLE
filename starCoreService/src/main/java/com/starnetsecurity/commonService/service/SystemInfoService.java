package com.starnetsecurity.commonService.service;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.DBObject;

/**
 * Created by 宏炜 on 2017-07-10.
 */
public interface SystemInfoService {

    void mergeSystemInfo(JSONObject jsonObject) throws Exception;

    DBObject getSystemInfo() throws Exception;
}
