package com.starnetsecurity.parkClientServer.service;


import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.parkClientServer.entity.InoutRecordInfo;
import com.starnetsecurity.parkClientServer.entity.OrderInoutRecord;
import com.starnetsecurity.parkClientServer.entity.OrderTransaction;
import com.starnetsecurity.parkClientServer.entity.WechatPayFailUrl;

import java.sql.Timestamp;
import java.util.List;

public interface NewOrderParkService {

    /**
     * 上传入场订单
     * @param inoutRecordInfo
     * @param orderOldId
     * @param orderInoutRecordNew
     * @throws BizException
     */
    void addInParkOrderToCloud(InoutRecordInfo inoutRecordInfo, String orderOldId,OrderInoutRecord orderInoutRecordNew) throws BizException;

    /**
     * 上传出场订单
     * @param inoutRecordInfo
     * @param orderInoutRecord
     * @param orderTransaction
     * @throws BizException
     */
    void addOutParkOrderToCloud(InoutRecordInfo inoutRecordInfo, OrderInoutRecord orderInoutRecord, OrderTransaction orderTransaction) throws BizException;

    /**
     * 传递AB车缴费时间
     * @param orderId
     * @param inChargeTime
     */
    void addChargeInTimeInfoToCloud(String orderId, String inChargeTime);

    /**
     * 获取上传失败的订单信息
     * @return
     */
    List<WechatPayFailUrl> getWechatPayFailUrlList();

    /**
     * 根据标识和url获取要传送的Jason
     * @param isUsed
     * @param url
     * @return
     */
    JSONObject getOrderInfoByUrl(Integer isUsed,String url);

    /**
     * 删除重新上传成功的订单
     * @param failId
     */
    void deleteSuccessInfo(String failId);

    /**
     * @Author chenbinbin
     * @Description 处理重新上传失败订单，将入场时间变成当前时间，增加备注
     * @Date 13:56 2019/8/16
     * @Param [wechatPayFailUrl]
     * @return void
     **/
    void handleFailInfo(WechatPayFailUrl wechatPayFailUrl);

    /**
     * 处理MQ传输的消息
     * @param jsonObject
     * @throws BizException
     */
    void handleMqInfo(JSONObject jsonObject) throws BizException;

    /**
     * 处理入场订单传输失败问题
     * @param inoutId
     * @param orderId
     * @param orderOldId
     */
    void handleInParkSubmitFail(String inoutId,String orderId,String orderOldId);

    /**
     * 处理出场订单传输失败问题
     * @param inoutId
     * @param orderId
     * @param transId
     */
    void handleOutParkSubmitFail(String inoutId,String orderId,String transId);

    /**
     * 处理AB车订单传输失败问题
     * @param orderId
     * @param inChargeTime
     */
    void handleChargeInTimeSubmitFail(String orderId,String inChargeTime);

    /**
     * 处理mqtt返回的月卡缴费数据
     * @param jsonObject
     */
    void monthCardRenewal(JSONObject jsonObject);

    /**
     * 处理mqtt返回的临停缴费处理数据
     * @param jsonObject
     */
    void parkingChargeRenewal(JSONObject jsonObject);

    /**
     * @Author chenbinbin
     * @Description 入场数据重传
     * @Date 10:11 2019/8/16
     * @Param [jsonObject]
     * @return void
     **/
    void inParkInfoRePush(JSONObject jsonObject);

    /**
     * 处理mqtt返回的无感支付标识数据
     * @param jsonObject
     */
    void uparkChargeMarkRenewal(JSONObject jsonObject);

    /**
     * 处理mqtt返回的无感支付成功标识
     * @param jsonObject
     */
    void uparkPaySuccessRenewal(JSONObject jsonObject);

    /**
     * 推送账单至智慧云平台，金额大于300的时候
     * @param billNo 账单唯一标识，非空
     * @param usrNum 车牌号，非空
     * @param parkNo 车场ID，非空
     * @param parkName 车场名称，非空
     * @param billAt 账单金额，非空
     * @param payAt  实际金额，非空
     * @param startTime 入场时间，非空
     * @param payTime 出场时间，非空
     * @param reserve 备注信息，可为空
     */
    void pushBillToUparkCloud(String billNo,String usrNum,String parkNo,String parkName,String billAt,String payAt,
                              String startTime,String payTime,String reserve);


}