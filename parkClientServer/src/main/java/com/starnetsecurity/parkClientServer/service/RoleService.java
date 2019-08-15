package com.starnetsecurity.parkClientServer.service;

import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.parkClientServer.entity.AdminRole;

import java.util.List;
import java.util.Map;

/**
 * Created by 宏炜 on 2017-02-15.
 */
public interface RoleService {

    List<AdminRole> getAdminRoleSelect();

    String getUserRoleId(String userId);

    List<AdminRole> getAdminRoleForPage(Integer start, Integer length);

    Long countAdminRole();

    AdminRole mergeAdminRole(Map params) throws BizException;

    void deleteAdminRole(String roleId) throws BizException;
}
