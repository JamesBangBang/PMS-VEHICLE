package com.starnetsecurity.parkClientServer.service;

import java.util.List;

/**
 * Created by 宏炜 on 2017-02-15.
 */
public interface ResourceService {

    List getMenuTree(String parentId);

    List getRoleMenuIdResources(String roleId);

    List getRoleRootMenuResources(String roleId);

    List getUserMenuResource(String userId);
}
