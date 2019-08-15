package com.starnetsecurity.parkClientServer.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by 宏炜 on 2018-01-22.
 */
public interface SystemService {

    Map<String,Object> getSystemInfo();

    void updateSystemInfo(JSONObject params);

    List<Map<String,Object>> getMemberInfo();

}
