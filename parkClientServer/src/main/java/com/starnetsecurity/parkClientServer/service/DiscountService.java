package com.starnetsecurity.parkClientServer.service;

import java.util.List;
import java.util.Map;

/**
 * Created by JAMESBANG on 2018/3/20.
 */
public interface DiscountService {
    List GetDisCountInfoList(String discountType,Integer start,Integer length);

    Long GetDiscountListCount(String discountType);

    Map<String,Object> GetDisCountDetailInfo(String discountID);

    void SaveDiscountInfo(Map discountParams);

    void DeleteDiscountInfo(String discountID);

    boolean IsDiscountNameRepeat(String controlMode,String discountName,String discountId);

}
