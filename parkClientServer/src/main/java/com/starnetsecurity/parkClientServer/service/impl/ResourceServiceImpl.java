package com.starnetsecurity.parkClientServer.service.impl;

import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.parkClientServer.entity.SystemType;
import com.starnetsecurity.parkClientServer.service.ResourceService;
import com.starnetsecurity.parkClientServer.entity.AdminResource;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 宏炜 on 2017-02-15.
 */
@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    HibernateBaseDao baseDao;

    public List getMenuTree(String parentId) {

        List<Map<String,Object>> list = new ArrayList<>();
        String hql = "from SystemType where typeName = ?";
        SystemType systemType = (SystemType)baseDao.getUnique(hql,"MENU_RESOURCE");

        hql = "from AdminResource where parentId = ? and resourceType = ? and status != 'DISABLED' order by priority";
        List<AdminResource> adminResources =  (List<AdminResource>)baseDao.queryForList(hql,parentId,systemType.getId());
        Map<String,Object> state = new HashMap<>();
        state.put("undetermined",false);
        for(AdminResource adminResource : adminResources){
            Map<String,Object> map = new HashMap<>();
            map.put("id",adminResource.getId());
            map.put("text",adminResource.getResourceName());
            map.put("children",true);
            map.put("state",state);
            map.put("parent", parentId);
            list.add(map);
        }

        return list;
    }

    @Override
    public List getRoleMenuIdResources(String roleId) {
        String hql = "select adminResourceId from AdminRoleResource where adminRoleId = ? and haveChildren != 1";
        return baseDao.queryForList(hql,roleId);
    }

    @Override
    public List getRoleRootMenuResources(String roleId) {
        String hql = "select adminResourceId from AdminRoleResource where adminRoleId = ? and haveChildren = 1";
        return baseDao.queryForList(hql,roleId);
    }

    @Override
    public List getUserMenuResource(String userId) {
        String hql = "from SystemType where typeName = ?";
        SystemType systemType = (SystemType)baseDao.getUnique(hql,"MENU_RESOURCE");

        String sql = "SELECT\n" +
                "\tb.id,\n" +
                "\tb.resource_name as resourceName,\n" +
                "\tb.parent_id as parentId,\n" +
                "\tb.target_link as targetLink,\n" +
                " b.priority " +
                "FROM\n" +
                "\tadmin_role_resource a\n" +
                "LEFT JOIN admin_resource b ON a.admin_resource_id = b.id\n" +
                "WHERE\n" +
                "\tb.resource_type = '" + systemType.getId() + "'\n and b.status !='DISABLED' " +
                "AND a.admin_role_id IN (\n" +
                "\tSELECT\n" +
                "\t\tc.admin_role_id\n" +
                "\tFROM\n" +
                "\t\tadmin_user_role c\n" +
                "\tWHERE\n" +
                "\t\tc.admin_user_id = :userId\n" +
                ")\n" +
                "ORDER BY b.priority";
        SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        query.setParameter("userId",userId);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }
}

