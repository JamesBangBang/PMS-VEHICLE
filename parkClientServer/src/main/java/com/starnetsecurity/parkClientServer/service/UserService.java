package com.starnetsecurity.parkClientServer.service;

import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.parkClientServer.entity.AdminUser;

import java.util.List;
import java.util.Map;

/**
 * Created by lubin on 2017/1/15.
 */

public interface UserService {

    void updateAdminUser(AdminUser adminUser);

    List getAdminUserListForPage(Integer pageSize, Integer pageNum,boolean isRoleExist);

    Long countAdminUser();

    AdminUser mergeUser(Map params) throws BizException;

    void deleteAdminUser(String id) throws BizException;

    AdminUser getAdminUserById(String userId);
}
