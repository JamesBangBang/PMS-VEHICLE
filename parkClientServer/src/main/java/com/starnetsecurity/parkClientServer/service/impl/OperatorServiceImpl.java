package com.starnetsecurity.parkClientServer.service.impl;

import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.parkClientServer.entity.AdminOrgResource;
import com.starnetsecurity.parkClientServer.entity.InOutCarRoadInfo;
import com.starnetsecurity.parkClientServer.entity.Operator;
import com.starnetsecurity.parkClientServer.service.OperatorService;
import com.starnetsecurity.parkClientServer.service.UploadDataToCloudService;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OperatorServiceImpl implements OperatorService {

    @Autowired
    HibernateBaseDao baseDao;

    @Autowired
    UploadDataToCloudService uploadDataToCloudService;

    @Override
    public List<Map> getOperatorhistory(String userId,Integer pageNum, Integer pageSize, String start, String end,String logName) {
        String admin_org_resource_hql ="from AdminOrgResource  where adminUserId ='"+userId+"'  and resType='DEPARTMENT' " +
                "order by recPriority desc";
        List<AdminOrgResource> adminOrgResources = (List<AdminOrgResource>)baseDao.queryForList(admin_org_resource_hql);
       String departId=null;
        for(AdminOrgResource adminOrgResource : adminOrgResources){
            departId=adminOrgResource.getResId();
        }

        String getOperatorhistory_sql ="SELECT\n" +
                "\tlog_type_name,\n" +
                "\to.add_time,\n" +
                "\to.log_source,\n" +
                "\to.log_desc,\n" +
                "\to.operator_name\n" +
                "FROM\n" +
                "\toperator_log o" +
                " WHERE o.department_id = '" +departId+"'" ;
        if (!CommonUtils.isEmpty(logName)) {
            getOperatorhistory_sql+="and log_type_name like '"+("%"+logName+"%")+"' \n" ;
        }
        if (!CommonUtils.isEmpty(start)&&!CommonUtils.isEmpty(end)) {
            getOperatorhistory_sql+="and o.add_time >='"+start+"' and o.add_time <='"+end+"'\n" ;
        }
        getOperatorhistory_sql += " order by o.add_time desc";
        List<Map>  Operatorhistory = (List<Map>) baseDao.getSQLList(getOperatorhistory_sql,pageNum,pageSize);
        return Operatorhistory;
    }


    @Override
    public Long getOperatorhistoryCount(String userId,Integer pageNum, Integer pageSize, String start, String end,String logName) {
        String admin_org_resource_hql ="from AdminOrgResource  where adminUserId ='"+userId+"'  and resType='DEPARTMENT' " +
                "order by recPriority desc";
        List<AdminOrgResource> adminOrgResources = (List<AdminOrgResource>)baseDao.queryForList(admin_org_resource_hql);
        String departId=null;
        for(AdminOrgResource adminOrgResource : adminOrgResources){
            departId=adminOrgResource.getResId();
        }

        String getOperatorhistory_sql ="SELECT\n" +
               "count(1)"+
                "FROM\n" +
                " operator_log o " +
                " WHERE o.department_id = '" +departId+"'" ;
        if (!CommonUtils.isEmpty(logName)) {
            getOperatorhistory_sql+="and log_type_name like '"+("%"+logName+"%")+"' \n" ;
        }
        if (!CommonUtils.isEmpty(start)&&!CommonUtils.isEmpty(end)) {
            getOperatorhistory_sql+="and o.add_time >='"+start+"' and o.add_time <='"+end+"'\n" ;
        }
        SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().
                createSQLQuery(getOperatorhistory_sql);
        return ((BigInteger)query.uniqueResult()).longValue();
    }

    @Override
    public List getOperatorList(Integer start, Integer length) {

        List list=new ArrayList();
        String sql="SELECT * FROM operator WHERE use_mark >= 0";
        SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        query.setFirstResult(start).setMaxResults(length);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        list=query.list();
        return list;
    }

    @Override
    public Long countOperatorList() {
        String sql="SELECT count(1) FROM operator WHERE use_mark >= 0";
        SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        return ((BigInteger) query.uniqueResult()).longValue();
    }

    @Override
    public int isPwdRepeat(Map params) {
        int res=0;
        String operatorLoginName = (String) params.get("operatorLoginName");
        String operatorName = (String) params.get("operatorName");
        String loginNameSql = "SELECT operator_id FROM operator\n" +
                "WHERE operator_user_name='"+operatorLoginName+"' AND use_mark >= 0";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(loginNameSql);
        if (sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list().size() > 0) {
            res = 1;
        } else {
            String nameSql = "SELECT operator_id FROM operator\n" +
                    "WHERE operator_name='"+operatorName+"' AND use_mark >= 0";
            SQLQuery sqlQuery1 = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(nameSql);
            if (sqlQuery1.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list().size() > 0) {
                res = 2;
            }
        }
        return res;
    }

    @Override
    public void operatorSave(Map params) {
        Operator operator=new Operator();
        operator.setOperatorUserName((String) params.get("operatorLoginName"));
        operator.setOperatorUserPwd((String) params.get("pwd"));
        operator.setOperatorName((String) params.get("operatorName"));
        operator.setUseMark(0);
        baseDao.save(operator);
        uploadDataToCloudService.uploadOperatorInfoToServer(operator);
    }

    @Override
    public void deleteOperator(String id) {
        Operator operator = (Operator) baseDao.getById(Operator.class,id);
        operator.setUseMark(-1);
        baseDao.update(operator);
        uploadDataToCloudService.uploadOperatorInfoToServer(operator);
    }

    @Override
    public void editOperator(Operator operator) {
        operator.setUseMark(1);
        baseDao.update(operator);
        uploadDataToCloudService.uploadOperatorInfoToServer(operator);
    }

    @Override
    public Operator getRemainOperator(String opertorId) {
        //实体直接查询
        String sql = "from Operator  where operatorId ='" + opertorId + "'";
        Operator operator = (Operator) baseDao.getUnique(sql);
        return operator;
    }

}
