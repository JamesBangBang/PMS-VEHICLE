package com.starnetsecurity.parkClientServer.service;

import com.starnetsecurity.parkClientServer.entity.*;

import java.util.List;
import java.util.Set;

/**
 * Created by 宏炜 on 2016-12-12.
 */
public interface AccountService {

    /**
     * 获取用户信息by用户名
     * @param username 用户名
     * @return AdminUser用户实体
     */
    public AdminUser getAdminUserByUsername(String username);

    /**
     * 获取用户角色信息by用户名
     * @param username 用户名
     * @return Set<String>角色信息串
     */
    public Set<String> getRolesByUsername(String username);

    /**
     * 获取角色信息
     * @param adminUserRoles 角色ID串
     * @return Set<String>角色信息串
     */
    public Set<AdminRole> getRoles(List<AdminUserRole> adminUserRoles);

    /**
     * 获取用户资源信息by用户名
     * @param username 用户名
     * @return Set<String>资源信息串
     */
    public Set<String> getResourcesByUsername(String username);

    /**
     * 获取资源信息
     * @param adminRoleResources 资源id信息串
     * @return Set<String>资源permission信息串
     */
    public Set<AdminResource> getResources(List<AdminRoleResource> adminRoleResources);


    /**
     * 设置用户的token信息by用户名
     * @param adminUser 用户
     * @return 返回受影响行数
     */
    public int updateAdminUserTokenByAdminUser(AdminUser adminUser);

    /**
     * 获取用户信息by token
     * @param token token字串
     * @return 用户对象
     */
    public AdminUser getAdminUserByToken(String token);

    /**
     * 获取用户菜单资源信息by用户名
     * @param username 用户名
     * @return List<Resource> 资源列表
     */
    public List<AdminResource> getMenuResourcesByUsername(String username);

}
