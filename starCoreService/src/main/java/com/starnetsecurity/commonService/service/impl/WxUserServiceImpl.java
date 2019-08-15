package com.starnetsecurity.commonService.service.impl;

import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.commonService.entity.WxUser;
import com.starnetsecurity.commonService.service.WxUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 宏炜 on 2017-08-02.
 */
@Service
public class WxUserServiceImpl implements WxUserService {

    @Autowired
    HibernateBaseDao baseDao;

    @Override
    public WxUser getUserByOpenidAndConfigId(String openid,String configId) {
        String hql = "from WxUser where openid = ? and wechatConfigId = ?";
        return (WxUser)baseDao.getUnique(hql,openid,configId);
    }

    @Override
    public WxUser getUserById(String id) {
        return (WxUser)baseDao.getById(WxUser.class,id);
    }

    @Override
    public WxUser getUserByUnionId(String unionId) {
        return null;
    }

    @Override
    public void saveWxUser(WxUser wxUser) throws BizException {
        if(wxUser == null){
            throw new BizException("用户信息不能为空");
        }
        if(StringUtils.isBlank(wxUser.getOpenid())){
            throw new BizException("openid不能为空");
        }
        if(StringUtils.isBlank(wxUser.getWechatConfigId())){
            throw new BizException("WechatConfigId不能为空");
        }
        baseDao.save(wxUser);

    }
}
