package com.starnetsecurity.parkClientServer.service.impl;

import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.parkClientServer.entity.CarparkInfo;
import com.starnetsecurity.parkClientServer.entity.DiscountType;
import com.starnetsecurity.parkClientServer.service.DiscountService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.codec.Base64;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by JAMESBANG on 2018/3/20.
 */
@Service

public class DiscountServiceImpl implements DiscountService {
    @Autowired
    HibernateBaseDao baseDao;

    @Override
    public List GetDisCountInfoList(String discountType, Integer start, Integer length) {
        String hql = "";
        if (!CommonUtils.isEmpty(discountType))
            hql = "SELECT * FROM discount_type WHERE discount_type = :discountType";
        else
            hql = "SELECT * FROM discount_type";
        SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
        if (!CommonUtils.isEmpty(discountType))
            query.setParameter("discountType",discountType);
        query.setFirstResult(start).setMaxResults(length);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List list = query.list();
        return list;
    }

    @Override
    public Long GetDiscountListCount(String discountType) {
        String hql = "";
        if (!CommonUtils.isEmpty(discountType))
            hql = "SELECT * FROM discount_type WHERE discount_type = :discountType";
        else
            hql = "SELECT * FROM discount_type";
        SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
        if (!CommonUtils.isEmpty(discountType))
            query.setParameter("discountType",discountType);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List list = query.list();
        return Long.valueOf(list.size());
    }

    @Override
    public Map<String,Object> GetDisCountDetailInfo(String discountID) {
        Map<String,Object> res = new HashedMap();
        DiscountType discountType = (DiscountType)baseDao.getById(DiscountType.class,discountID);
        res.put("editDiscountName",discountType.getDiscountTypeName());
        res.put("editDiscountType",discountType.getDiscountType());
        res.put("editDiscountInfo",discountType.getDiscountInfo());
        res.put("edtDiscountCompanyName",discountType.getDiscountCompanyName());

        return res;
    }

    @Override
    public void SaveDiscountInfo(Map discountParams) {
        if (discountParams.get("controlMode").toString().equals("add")){
            DiscountType discountType = new DiscountType();
            discountType.setDiscountTypeName(discountParams.get("discountName").toString());
            discountType.setDiscountType(discountParams.get("discountType").toString());
            String discountInfo = "";
            if (discountParams.get("discountType").toString().equals("0")) {
                discountType.setDiscountTypeDuration(discountParams.get("discountDuration").toString());
                discountInfo = "优惠" + discountParams.get("discountDuration").toString() + "分钟";
            }
            else if (discountParams.get("discountType").toString().equals("1")) {
                discountType.setDiscountFee(Float.valueOf(discountParams.get("discountDuration").toString()));
                discountInfo = "优惠" + discountParams.get("discountDuration").toString() + "元";
            }
            else if (discountParams.get("discountType").toString().equals("2")) {
                discountType.setDiscount(discountParams.get("discountDuration").toString());
                discountInfo = "优惠" + discountParams.get("discountDuration").toString() + "%";
            }else
                discountInfo = "完全免费";
            discountType.setAddTime(CommonUtils.getTimestamp());
            discountType.setDiscountCompanyName(discountParams.get("discountCompanyName").toString());
            discountType.setDiscountInfo(discountInfo);
            baseDao.save(discountType);
        }else{
            DiscountType discountType = (DiscountType)baseDao.getById(DiscountType.class,discountParams.get("discountId").toString());
            discountType.setDiscountTypeName(discountParams.get("discountName").toString());
            discountType.setDiscountCompanyName(discountParams.get("discountCompanyName").toString());
            baseDao.save(discountType);
        }
    }

    @Override
    public void DeleteDiscountInfo(String discountID) {
        baseDao.deleteById(DiscountType.class,discountID);
    }

    @Override
    public boolean IsDiscountNameRepeat(String controlMode, String discountName,String discountId) {
        boolean res = false;
        if (controlMode.equals("add")){
            String hql = "from DiscountType where discountTypeName = ?";
            DiscountType discountType = (DiscountType) baseDao.getUnique(hql,discountName);
            if (!CommonUtils.isEmpty(discountType))
                res = true;
        }else {
            String hql = "from DiscountType where discountTypeName = ?";
            DiscountType discountType = (DiscountType) baseDao.getUnique(hql,discountName);
            if (!discountType.getDiscountTypeId().equals(discountId)){
                res = true;
            }
        }
        return res;
    }
}
