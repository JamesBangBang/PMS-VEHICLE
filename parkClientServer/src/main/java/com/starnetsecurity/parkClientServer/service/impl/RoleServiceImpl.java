package com.starnetsecurity.parkClientServer.service.impl;

import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.util.Constant;
import com.starnetsecurity.parkClientServer.entity.AdminResource;
import com.starnetsecurity.parkClientServer.entity.AdminRole;
import com.starnetsecurity.parkClientServer.entity.AdminRoleResource;
import com.starnetsecurity.parkClientServer.service.BasicDataSyncService;
import com.starnetsecurity.parkClientServer.service.RoleService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * Created by 宏炜 on 2017-02-15.
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    BasicDataSyncService basicDataSyncService;

    @Autowired
    HibernateBaseDao baseDao;

    @Override
    public List<AdminRole> getAdminRoleSelect() {
        String hql = "from AdminRole";
        return (List<AdminRole>)baseDao.queryForList(hql);
    }

    @Override
    public String getUserRoleId(String userId) {
        String hql = "select adminRoleId from AdminUserRole where adminUserId = ?";
        List<String> list = (List<String>)baseDao.queryForList(hql,userId);
        if(CollectionUtils.isEmpty(list)){
            return null;
        }else{
            return list.get(0);
        }
    }

    @Override
    public List<AdminRole> getAdminRoleForPage(Integer start, Integer length) {

        String hql = "from AdminRole";
        start = start / length + 1;
        return (List<AdminRole>)baseDao.pageQuery(hql,start,length);

    }

    @Override
    public Long countAdminRole() {
        String hql = "select count(1) from AdminRole";
        return (Long)baseDao.getUnique(hql);
    }

    @Override
    public AdminRole mergeAdminRole(Map params) throws BizException {
        String id = (String)params.get("id");
        String roleTag = (String)params.get("roleTag");
        if(!CommonUtils.isEmpty(roleTag)){
            roleTag = roleTag.toUpperCase();
        }
        String description = (String)params.get("description");
        String roleName = (String)params.get("roleName");
        List<String> resourceIds = (List<String>)params.get("resourceIds");
        AdminRole adminRole;
        if(StringUtils.isBlank(id)){
            adminRole = new AdminRole();
            String hql = "select count(1) from AdminRole where roleTag = ?";
            Long count = (Long)baseDao.getUnique(hql,roleTag);
            if(count > 0){
                throw new BizException("角色标识已存在");
            }
            adminRole.setCreateTime(CommonUtils.getTimestamp());
            adminRole.setRoleTag(roleTag);
            adminRole.setSysTag(Constant.getPropertie("SYNC.SYS_TAG"));
        }else{
            adminRole = (AdminRole) baseDao.getById(AdminRole.class,id);
        }

        adminRole.setRoleName(roleName);
        adminRole.setDescription(description);

        adminRole.setUpdateTime(CommonUtils.getTimestamp());
        baseDao.save(adminRole);
        String hql = "delete from AdminRoleResource where adminRoleId = ?";
        baseDao.update(hql,adminRole.getId());
        for(String resourceId : resourceIds){
            AdminRoleResource adminRoleResource = new AdminRoleResource();
            adminRoleResource.setAdminRoleId(adminRole.getId());
            adminRoleResource.setAdminResourceId(resourceId);
            AdminResource adminResource = (AdminResource)baseDao.getById(AdminResource.class,resourceId);
            adminRoleResource.setAdminResourcePermission(adminResource.getPermission());
            adminRoleResource.setHaveChildren(adminResource.getHaveChildren());
            baseDao.save(adminRoleResource);
        }

        basicDataSyncService.synChronizeSystemAdminRole(adminRole,resourceIds,"0");
        return adminRole;
    }

    @Override
    public void deleteAdminRole(String roleId) throws BizException {
        AdminRole adminRole = (AdminRole)baseDao.getById(AdminRole.class,roleId);
        if(adminRole.getRoleTag().toUpperCase().equals("root".toUpperCase())){
            throw new BizException("无法删除该角色");
        }
        String hql = "delete from AdminRoleResource where adminRoleId = ?";
        baseDao.update(hql,roleId);
        basicDataSyncService.synChronizeSystemAdminRole(adminRole,null,"2");
        baseDao.deleteById(AdminRole.class,roleId);


    }
}
