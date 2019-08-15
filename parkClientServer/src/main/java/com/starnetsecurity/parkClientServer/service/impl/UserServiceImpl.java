package com.starnetsecurity.parkClientServer.service.impl;

import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.util.Constant;
import com.starnetsecurity.parkClientServer.service.BasicDataSyncService;
import com.starnetsecurity.parkClientServer.service.UserService;
import com.starnetsecurity.parkClientServer.entity.AdminOrgResource;
import com.starnetsecurity.parkClientServer.entity.AdminRole;
import com.starnetsecurity.parkClientServer.entity.AdminUser;
import com.starnetsecurity.parkClientServer.entity.AdminUserRole;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.hibernate.SQLQuery;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lubin on 2017/1/15.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    HibernateBaseDao baseDao;

    @Autowired
    BasicDataSyncService basicDataSyncService;

    @Override
    public void updateAdminUser(AdminUser adminUser) {
        String hql = "from AdminUserRole where adminUserId = ?";
        List<AdminUserRole> adminUserRoles = (List<AdminUserRole>)baseDao.queryForList(hql,adminUser.getId());
        String roleItems = "";
        if(!CollectionUtils.isEmpty(adminUserRoles)){
            roleItems = adminUserRoles.get(0).getAdminRoleId();
        }
        basicDataSyncService.synChronizeSystemAdminUser(adminUser,roleItems,"0");
        baseDao.update(adminUser);
    }

    @Override
    public List getAdminUserListForPage(Integer pageSize, Integer pageNum,boolean isRoleExist) {
        String sql = "select a.id,a.user_name as userName,a.true_name as trueName,a.email,a.telephone,a.sex,a.qq from admin_user a left join admin_user_role b " +
                "on a.id = b.admin_user_id ";
        if(isRoleExist){
            sql += "where b.id is not null";
        }else{
            sql += "where b.id is null";
        }

        SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        query.setFirstResult(pageNum).setMaxResults(pageSize);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public Long countAdminUser() {
        String hql = "select count(1) from AdminUser";
        return (Long)baseDao.getUnique(hql);
    }

    @Override
    public AdminUser mergeUser(Map params) throws BizException {
        Object departmentItems = params.get("departmentItems");
        String email = (String)params.get("email");
        String id = (String)params.get("id");
        Object parkItems = params.get("parkItems");
        String qq = (String)params.get("qq");
        String roleItems = (String)params.get("roleItems");
        String sex = (String)params.get("sex");
        String telephone = (String)params.get("telephone");
        String trueName = (String)params.get("trueName");
        String userName = (String)params.get("userName");
        if(!CommonUtils.isEmpty(userName)){
            userName = userName.toLowerCase();
        }


        AdminUser adminUser;
        if(StringUtils.isBlank(id)){
            adminUser = new AdminUser();
            adminUser.setUserPwd(new SimpleHash("md5","123456",null,1).toString());
            adminUser.setCreateTime(CommonUtils.getTimestamp());
            String hql = "select count(1) from AdminUser where userName = ?";
            Long count = (Long)baseDao.getUnique(hql,userName);
            if(count > 0){
                throw new BizException("用户名已存在");
            }
            adminUser.setUserName(userName);
            adminUser.setCreateTime(CommonUtils.getTimestamp());
            adminUser.setSysTag(Constant.getPropertie("SYNC.SYS_TAG"));
        }else{
            adminUser = (AdminUser) baseDao.getById(AdminUser.class,id);
        }
        adminUser.setQq(qq);
        adminUser.setEmail(email);
        adminUser.setTelephone(telephone);
        adminUser.setSex(sex);
        adminUser.setTrueName(trueName);
        adminUser.setUpdateTime(CommonUtils.getTimestamp());
        baseDao.save(adminUser);

        if (parkItems instanceof String) {
            String parkId = (String)parkItems;
            String hql = "delete from AdminOrgResource where adminUserId = ? and resType = 'PARK'";
            baseDao.update(hql,adminUser.getId());
            AdminOrgResource adminOrgResource = new AdminOrgResource();
            adminOrgResource.setResType("PARK");
            adminOrgResource.setRecPriority(0);
            adminOrgResource.setResId(parkId);
            adminOrgResource.setAdminUserId(adminUser.getId());
            baseDao.save(adminOrgResource);
        }else {
            List<String> parkIds = (List<String>) parkItems;
            String hql = "delete from AdminOrgResource where adminUserId = ? and resType = 'PARK'";
            baseDao.update(hql,adminUser.getId());

            for (int i = 0; i < parkIds.size(); i++) {
                String parkId = parkIds.get(i);
                AdminOrgResource adminOrgResource = new AdminOrgResource();
                adminOrgResource.setResType("PARK");
                adminOrgResource.setRecPriority(i);
                adminOrgResource.setResId(parkId);
                adminOrgResource.setAdminUserId(adminUser.getId());
                baseDao.save(adminOrgResource);
            }
        }
        String hql = "delete from AdminUserRole where adminUserId = ?";
        baseDao.update(hql,adminUser.getId());
        AdminUserRole adminUserRole = new AdminUserRole();
        adminUserRole.setAdminUserId(adminUser.getId());
        adminUserRole.setAdminRoleId(roleItems);
        AdminRole adminRole = (AdminRole)baseDao.getById(AdminRole.class,roleItems);
        adminUserRole.setAdminRoleTag(adminRole.getRoleTag());
        baseDao.save(adminUserRole);

        hql = "delete from AdminOrgResource where adminUserId = ? and resType = 'DEPARTMENT'";
        baseDao.update(hql,adminUser.getId());
        if (departmentItems instanceof String) {
            AdminOrgResource adminOrgResource = new AdminOrgResource();
            adminOrgResource.setResType("DEPARTMENT");
            adminOrgResource.setRecPriority(0);
            adminOrgResource.setResId(String.valueOf(departmentItems));
            adminOrgResource.setAdminUserId(adminUser.getId());
            baseDao.save(adminOrgResource);
        }else {
            List<String> depIds = (List<String>) departmentItems;
            for (int i = 0; i < depIds.size(); i++) {
                AdminOrgResource adminOrgResource = new AdminOrgResource();
                adminOrgResource.setResType("DEPARTMENT");
                adminOrgResource.setRecPriority(0);
                adminOrgResource.setResId(depIds.get(i));
                adminOrgResource.setAdminUserId(adminUser.getId());
                baseDao.save(adminOrgResource);
            }
        }
        basicDataSyncService.synChronizeSystemAdminUser(adminUser,roleItems,"0");
        return adminUser;
    }

    @Override
    public void deleteAdminUser(String id) throws BizException {
        AdminUser adminUser = (AdminUser) baseDao.getById(AdminUser.class,id);
        if(!CommonUtils.isEmpty(adminUser)){
            if("root".equals(adminUser.getUserName())){
                throw new BizException("该用户无法删除");
            }
        }
        String hql = "delete from AdminOrgResource where adminUserId = ?";
        baseDao.update(hql,id);
        basicDataSyncService.synChronizeSystemAdminUser(adminUser,null,"2");
        baseDao.deleteById(AdminUser.class,id);
    }

    @Override
    public AdminUser getAdminUserById(String userId) {
        return (AdminUser) baseDao.getById(AdminUser.class,userId);
    }
}
