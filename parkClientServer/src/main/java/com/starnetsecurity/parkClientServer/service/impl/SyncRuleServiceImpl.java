package com.starnetsecurity.parkClientServer.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.nio.SemaphoreExecutor;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.parkClientServer.entity.SyncRule;
import com.starnetsecurity.parkClientServer.mq.SyncMessageProducer;
import com.starnetsecurity.parkClientServer.service.SyncRuleService;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by 宏炜 on 2017-10-09.
 */
@Service
public class SyncRuleServiceImpl implements SyncRuleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncRuleServiceImpl.class);

    @Autowired
    HibernateBaseDao baseDao;

    @Autowired
    SyncMessageProducer syncMessageProducer;


    @Override
    public List<SyncRule> getSyncRuleList() {
        String hql = "from SyncRule where status = 1";
        return (List<SyncRule>)baseDao.queryForList(hql);
    }

    @Override
    public void updateExecuteSyncRule(List<SyncRule> syncRules) throws Exception {

        SemaphoreExecutor executor = new SemaphoreExecutor(syncRules.size(), "syncRulesThread");
        final CountDownLatch cdl = new CountDownLatch(syncRules.size());

        for (final SyncRule syncRule : syncRules) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject json = (JSONObject)JSON.toJSON(syncRule);

                        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(syncRule.getMergeSql());
                        sqlQuery.setParameter("mergeFlag",syncRule.getMergeFlag());
                        sqlQuery.setMaxResults(syncRule.getNumPerTimes());
                        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                        List<Map> mergeList = (List<Map>)sqlQuery.list();

                        sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(syncRule.getDeleteSql());
                        sqlQuery.setParameter("deleteFlag",syncRule.getDeleteFlag());
                        sqlQuery.setMaxResults(syncRule.getNumPerTimes());
                        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                        List<Map> deleteList = (List<Map>)sqlQuery.list();

                        if(!CollectionUtils.isEmpty(mergeList)){
                            json.put("sync_data",mergeList);
                            json.put("sync_type","merge");
//                            syncMessageProducer.sendSyncDataToQueue(AppInfo.mainSyncMq,json);
                            List<String> dataIds = new ArrayList<>();
                            for(Map data : mergeList){
                                dataIds.add(String.valueOf(data.get(syncRule.getParimaryName())));
                            }
                            String sql = "update " + syncRule.getTableName() + " set " + syncRule.getSyncFlagName() + " = " + syncRule.getMergeSuccessFlag() + " where " + syncRule.getParimaryName() + " in ( :dataIds )";
                            sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
                            sqlQuery.setParameterList("dataIds",dataIds);
                            sqlQuery.executeUpdate();
                        }

                        if(!CollectionUtils.isEmpty(deleteList)){
                            json.put("sync_data",deleteList);
                            json.put("sync_type","delete");

//                            syncMessageProducer.sendSyncDataToQueue(AppInfo.mainSyncMq,json);
                            List<String> dataIds = new ArrayList<>();
                            for(Map data : deleteList){
                                dataIds.add(String.valueOf(data.get(syncRule.getParimaryName())));
                            }
                            String sql = "update " + syncRule.getTableName() + " set " + syncRule.getSyncFlagName() + " = " + syncRule.getDeleteSuccessFlag() + " where " + syncRule.getParimaryName() + " in ( :dataIds )";
                            sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
                            sqlQuery.setParameterList("dataIds",dataIds);
                            sqlQuery.executeUpdate();
                        }
                        syncRule.setSyncTime(CommonUtils.getTimestamp());
                        baseDao.update(syncRule);
                    } catch (Exception ex) {
                        LOGGER.error("数据自动同步定时器发生错误:{}", ex);
                    } finally {
                        cdl.countDown();
                    }
                }
            });
        }
        try {
            cdl.await();
        } catch (InterruptedException e) {
            LOGGER.error("数据自动同步定时器发生错误 InterruptedException :", e);
        }



    }
}
