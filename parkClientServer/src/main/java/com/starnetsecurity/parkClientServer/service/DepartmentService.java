package com.starnetsecurity.parkClientServer.service;

import com.starnetsecurity.parkClientServer.entity.DepartmentInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by 宏炜 on 2017-10-24.
 */
public interface DepartmentService {

    List<Map<String,Object>> getDepartmentInfoSelect();

    List<String> getUserDepartmentIds(String userId);
}
