package com.starnetsecurity.common.mongoDao;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.starnetsecurity.common.exception.BizException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Repository("mongoDao")
public class MongoBaseDao<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoBaseDao.class);
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 插入一个对象
     *
     * @param object
     * @param collectionName
     */
    public void insert(T object, String collectionName) throws Exception {
        mongoTemplate.insert(object, collectionName);
    }


    /**
     * 批量插入
     *
     * @param datas
     * @param colName
     * @throws Exception
     */
    public void insertAll(Collection<T> datas, String colName) throws Exception {
        if (CollectionUtils.isNotEmpty(datas)) {
            mongoTemplate.insert(datas, colName);
        }
    }

    /**
     * 批量更新
     *
     * @param query
     * @param update
     * @param colName
     * @throws Exception
     */
    public void updateAll(Map<String, Object> query, Map<String, Object> update, String colName) throws Exception {
        Query queryObj = getQuery(query, null, null, null);
        Update updateObj = getUpdate(update);
        mongoTemplate.updateMulti(queryObj, updateObj, colName);
    }

    /**
     * 获取符合条件的一个对象
     *
     * @param params
     * @param objClass
     * @param collectionName
     * @return
     * @throws Exception
     */
    public Object findOne(Map<String, Object> params, Class objClass, String collectionName) throws BizException {
        Query query = getQuery(params, null, null, null);
        Object obj = mongoTemplate.findOne(query, objClass, collectionName);
        return obj;
    }

    /**
     * 获取多个对象 映射到BEAN
     *
     * @param params
     * @param objClass
     * @param collectionName
     * @return
     * @throws Exception
     */
    public Collection<T> getList(Map<String, Object> params, Class objClass, String collectionName, DBObject sort) throws Exception {
        Query query = getQuery(params, null, null, sort);
        Collection obj = mongoTemplate.find(query, objClass, collectionName);
        return obj;
    }

    public Collection<T> getList(Map<String, Object> params, Class objClass, String collectionName) throws Exception {
        Query query = getQuery(params, null, null, null);
        Collection obj = mongoTemplate.find(query, objClass, collectionName);
        return obj;
    }

    public Collection<T> getPageList(Map<String, Object> params, Class objClass, int pageSize, int pageNum, String collectionName) throws Exception {
        Query query = getQuery(params, pageSize, pageNum, null);
        Collection obj = mongoTemplate.find(query, objClass, collectionName);
        return obj;
    }


    public Collection<T> getPageList(Map<String, Object> params, Class objClass, int pageSize, int pageNum, String collectionName, DBObject sort) throws Exception {
        Query query = getQuery(params, pageSize, pageNum, sort);
        Collection obj = mongoTemplate.find(query, objClass, collectionName);
        return obj;
    }

    /**
     * 获取所有对象  映射到对应的BEAN
     *
     * @param objClass
     * @param collectionName
     * @return
     * @throws Exception
     */
    public Collection findAll(Class objClass, String collectionName) throws Exception {
        return mongoTemplate.findAll(objClass, collectionName);
    }

    /**
     * 删除符合条件的多个对象
     *
     * @param params
     * @param collection
     * @return
     */
    public int delete(Map<String, Object> params, String collection) {
        Query query = getQuery(params, null, null, null);
        WriteResult result = mongoTemplate.remove(query, collection);
        return result.getN();
    }

    /**
     * 更新对象
     *
     * @param queryParam
     * @param updateItem
     * @param collection
     * @return
     */
    public int updateOne(Map<String, Object> queryParam, Map<String, Object> updateItem, String collection) {
        Query query = getQuery(queryParam, null, null, null);
        Update update = getUpdate(updateItem);
        WriteResult result = mongoTemplate.updateFirst(query, update, collection);
        return result.getN();
    }

    /**
     * 获取MONGO 更新对象
     *
     * @param updateItem
     * @return
     */
    private Update getUpdate(Map<String, Object> updateItem) {
        //TODO 对于MONGO的各类表达式暂时没有支持,后续需要修改
        Update update = new Update();
        if (MapUtils.isNotEmpty(updateItem)) {
            Set<String> keys = updateItem.keySet();
            for (String key : keys) {
                Object val = updateItem.get(key);
                if (val != null) {
                    //注意set方法与addtoSet方法的区别 前者是替换，后者是追加 在处理数组对象的时候尤为重要
                    update.set(key, val);
                } else {
                    update.set(key, val);
                }
            }
        }
        return update;
    }


    private Query getQuery(Map<String, Object> params, Integer pageSize, Integer pageNo, DBObject sort) {
        //TODO 对于MONGO的各类表达式暂时没有支持,后续需要修改,且应支持分页、排序等功能  QUERY对象本身可以设置相关的属性
        Query query = new Query();
        if (pageSize != null && pageNo != null) {
            query.limit(pageSize);
            query.skip((pageNo - 1) * pageSize);
        }
        if (MapUtils.isNotEmpty(params)) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                Object val = params.get(key);
                if (val != null) {
                    if (key.indexOf("$lt") != -1 || key.indexOf("$gt") != -1) {
                        if (key.equals("$lt")) {
                            query.addCriteria(Criteria.where(key).lt(val));
                        }
                        if (key.equals("$lte")) {
                            query.addCriteria(Criteria.where(key).lte(val));
                        }
                        if (key.equals("$gt")) {
                            query.addCriteria(Criteria.where(key).gt(val));
                        }
                        if (key.equals("$gte")) {
                            query.addCriteria(Criteria.where(key).gte(val));
                        }
                    }
                    query.addCriteria(Criteria.where(key).is(val));
                }
            }
        }

        if (sort != null) {
            for (String key : sort.keySet()) {
                query.with(new Sort(new Sort.Order(Sort.Direction.valueOf((String) sort.get(key)), key)));
            }
        }

        LOGGER.info("query condition :{}", query.toString());
        return query;
    }

    /**
     * 将 JAVA 对象转换成DBObject对象
     *
     * @param obj
     * @return
     * @throws Exception
     */
    private DBObject parse2DBObject(T obj) throws Exception {
        DBObject dbObject = new BasicDBObject();

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(obj);
            if (value != "" && value != null && !field.getName().equals("_class")) {
                dbObject.put(field.getName(), value);
            }
        }

        //for obj un-contain column id,system set an uuid to replaced
        if (dbObject.containsField("id")) {
            Long timestamp = System.currentTimeMillis();
            int hashValue = obj.hashCode();
            String id = timestamp + "_" + hashValue;
            dbObject.put("id", id);
        }

        // for any obj to save,system will point the save time with column satm
        dbObject.put("satm", new Date());

        return dbObject;
    }


    public long count(Map<String, Object> query, String collection) {
        return mongoTemplate.count(getQuery(query, null, null, null), collection);
    }
}
