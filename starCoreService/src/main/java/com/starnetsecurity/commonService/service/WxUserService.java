package com.starnetsecurity.commonService.service;

import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.commonService.entity.WxUser;

/**
 * Created by 宏炜 on 2017-08-02.
 */
public interface WxUserService {

    WxUser getUserByOpenidAndConfigId(String openid,String configId);

    WxUser getUserById(String id);

    WxUser getUserByUnionId(String unionId);

    void saveWxUser(WxUser wxUser) throws BizException;
}
