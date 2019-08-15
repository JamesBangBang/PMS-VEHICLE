package com.starnetsecurity.parkClientServer.service.impl;

import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.parkClientServer.service.AccountService;
import com.starnetsecurity.parkClientServer.entity.*;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 宏炜 on 2016-12-12.
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    HibernateBaseDao baseDao;

    @Override
    public AdminUser getAdminUserByUsername(String username) {
        String hql = "from AdminUser where userName = ?";
        return (AdminUser) baseDao.getUnique(hql,username);
    }

   @Override
    public Set<String> getRolesByUsername(String username) {
        AdminUser user = getAdminUserByUsername(username);
        String hql = "from AdminUserRole where adminUserId = ?";
        List<AdminUserRole> adminUserRoles = (List<AdminUserRole>)baseDao.queryForList(hql,user.getId());
        HashSet<String> roleTag = new HashSet<>();
        if(!CollectionUtils.isEmpty(adminUserRoles)){
            for(AdminUserRole adminUserRole : adminUserRoles){
                roleTag.add(adminUserRole.getAdminRoleTag());
            }
        }

        return roleTag;
    }

    @Override
    public Set<AdminRole> getRoles(List<AdminUserRole> adminUserRoles) {
        List<String> roleIds = new ArrayList<>();
        for(AdminUserRole adminUserRole : adminUserRoles){
            roleIds.add(adminUserRole.getAdminRoleId());
        }
        String hql = "from AdminRole where id in (:roleIds)";
        Query query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
        query.setParameterList("roleIds",roleIds);
        return new HashSet<>(query.list());
    }

    @Override
    public Set<String> getResourcesByUsername(String username) {
        AdminUser user = getAdminUserByUsername(username);
        String hql = "from AdminUserRole where adminUserId = ?";
        List<AdminUserRole> adminUserRoles = (List<AdminUserRole>)baseDao.queryForList(hql,user.getId());
        Set<AdminRole> adminRoles = getRoles(adminUserRoles);
        List<String> adminRoleIds = new ArrayList<>();
        for(AdminRole adminRole : adminRoles){
            adminRoleIds.add(adminRole.getId());
        }
        hql = "from AdminRoleResource where adminRoleId in (:adminRoleIds)";
        Query query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
        List<AdminRoleResource> adminRoleResources = (List<AdminRoleResource>)query.setParameterList("adminRoleIds",adminRoleIds);
        Set<String> resources = new HashSet<>();
        for(AdminRoleResource adminRoleResource : adminRoleResources){
            resources.add(adminRoleResource.getAdminResourcePermission());
        }
        return resources;
    }

    @Override
    public Set<AdminResource> getResources(List<AdminRoleResource> adminRoleResources) {
        List<String> resourceIds = new ArrayList<>();
        for(AdminRoleResource adminRoleResource : adminRoleResources){
            resourceIds.add(adminRoleResource.getAdminResourceId());
        }
        String hql = "from AdminResource where id in (:resourceIds)";
        Query query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
        query.setParameterList("resourceIds",resourceIds);
        return new HashSet<>(query.list());
    }

    @Override
    public int updateAdminUserTokenByAdminUser(AdminUser adminUser) {
        return 0;
    }

    @Override
    public AdminUser getAdminUserByToken(String token) {
        return null;
    }

    @Override
    public List<AdminResource> getMenuResourcesByUsername(String username) {
        AdminUser user = getAdminUserByUsername(username);
        String hql = "from AdminUserRole where adminUserId = ?";
        List<AdminUserRole> adminUserRoles = (List<AdminUserRole>)baseDao.queryForList(hql,user.getId());
        Set<AdminRole> adminRoles = getRoles(adminUserRoles);
        List<String> adminRoleIds = new ArrayList<>();
        for(AdminRole adminRole : adminRoles){
            adminRoleIds.add(adminRole.getId());
        }
        hql = "from AdminRoleResource where adminRoleId in (:adminRoleIds)";
        Query query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
        List<AdminRoleResource> adminRoleResources = (List<AdminRoleResource>)query.setParameterList("adminRoleIds",adminRoleIds);

        List<String> resourceIds = new ArrayList<>();
        for(AdminRoleResource adminRoleResource : adminRoleResources){
            resourceIds.add(adminRoleResource.getAdminResourceId());
        }

        hql = "from SystemType where typeName = ?";
        SystemType systemType = (SystemType)baseDao.getUnique(hql,"MENU_RESOURCE");
        hql = "from AdminResource where id in (:resourceIds) and resourceType = :resourceType and status != 'DISABLED'";
        query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
        query.setParameterList("resourceIds",resourceIds);
        query.setParameter("resourceType",systemType.getId());
        return query.list();
    }

}
