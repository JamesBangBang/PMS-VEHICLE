package com.starnetsecurity.parkClientServer.service;

import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.parkClientServer.entity.AdminUser;

import java.util.List;
import java.util.Map;

/**
 * Created by 宏炜 on 2017-12-28.
 */
public interface PostComputerService {

    List<Map<String,Object>> getPageList(Integer size, Integer page);

    Long getCount();

    void mergePostComputer(String id, String name, String ip, String isAutoDeal,
                           String isVoicePlay,AdminUser adminUser,String operateIp) throws BizException;

    void deletePostComputer(String id,AdminUser adminUser,String operateIp);
}
