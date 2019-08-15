package com.starnetsecurity.parkClientServer.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.util.HttpRequestUtils;
import com.starnetsecurity.parkClientServer.allinpay.AllinQueryThread;
import com.starnetsecurity.parkClientServer.allinpay.FuncUtil;
import com.starnetsecurity.parkClientServer.allinpay.QueryRsp;
import com.starnetsecurity.parkClientServer.entity.CarparkInfo;
import com.starnetsecurity.parkClientServer.entity.MemberWallet;
import com.starnetsecurity.parkClientServer.entity.OrderInoutRecord;
import com.starnetsecurity.parkClientServer.entity.OrderTransaction;
import com.starnetsecurity.parkClientServer.entityEnum.payStatusEnum;
import com.starnetsecurity.parkClientServer.entityEnum.payTypeEnum;
import com.starnetsecurity.parkClientServer.init.AppInfo;
import com.starnetsecurity.parkClientServer.service.AllinpayService;
import com.starnetsecurity.parkClientServer.service.CalculateFeeNewService;
import com.starnetsecurity.parkClientServer.sockServer.ParkSocketServer;
import com.starnetsecurity.parkClientServer.sockServer.SocketClient;
import com.starnetsecurity.parkClientServer.sockServer.SocketUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.codec.*;
import org.apache.shiro.codec.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.tree.Tree;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by JAMESBANG on 2019/1/21.
 */
@Service
public class AllinpayServiceImpl implements AllinpayService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AllinpayServiceImpl.class);

    @Autowired
    HibernateBaseDao baseDao;

    @Autowired
    CalculateFeeNewService calculateFeeNewService;

    @Override
    public JSONObject getChargeInfoByCarno(String carno) {
        JSONObject res = new JSONObject();
        try {
            String hql = "from OrderInoutRecord where carNo = ?  and inTime <= ? and outRecordId IS NULL order by inTime desc";
            List<OrderInoutRecord> orderInoutRecordList = (List<OrderInoutRecord>)baseDao.queryForList(hql,carno,CommonUtils.getTimestamp());
            if (CommonUtils.isEmpty(orderInoutRecordList) || orderInoutRecordList.size() <= 0){
                return null;
            }
            /** 获取入场信息，按照入场时间排序，获取最新的一条 **/
            OrderInoutRecord orderInoutRecord = orderInoutRecordList.get(0);
            String carparkId = orderInoutRecord.getCarparkId();
            String carparkName = orderInoutRecord.getCarparkName();
            hql = "from MemberWallet where menberNo like ? and carPark = ? AND useMark >= 0";
            MemberWallet memberWallet = (MemberWallet)baseDao.getUnique(hql,"%" + carno + "%",carparkId);
            if (CommonUtils.isEmpty(memberWallet)){
                Timestamp outTime = CommonUtils.getTimestamp();
                Timestamp inTime = orderInoutRecord.getInTime();
                Long stayTime = (outTime.getTime() - inTime.getTime()) / 1000;
                int parkingHour = (int)(stayTime/3600);
                int parkingMinute = (int)((stayTime-parkingHour*3600)/60);
                String parkingLen = String.valueOf(parkingHour) + "时" + String.valueOf(parkingMinute) + "分";
                Double chargeAmount = 0D;
                try {
                    chargeAmount = calculateFeeNewService.calculateParkingFeeNew(carno, "0", "1",carparkId, inTime, outTime);
                }catch (Exception e){
                    LOGGER.error("计费异常",e);
                    chargeAmount = 0D;
                }

                BigDecimal accountSum = new BigDecimal("0.00");
                String accountHql = "from OrderTransaction where orderId = ? and payStatus = ? order by addTime desc";
                List<OrderTransaction> orderTransactionList = (List<OrderTransaction>)baseDao.queryForList(accountHql,orderInoutRecord.getChargeInfoId(),payStatusEnum.HAS_PAID);
                if (orderTransactionList.size() > 0) {
                    //流水表收费总金额
                    for (OrderTransaction orderTransaction : orderTransactionList){
                        accountSum = accountSum.add(orderTransaction.getTotalFee());
                    }
                }

                if (accountSum.doubleValue() > 0){
                    chargeAmount = ((new BigDecimal(chargeAmount.toString())).subtract(accountSum)).doubleValue();
                    if (chargeAmount < 0){
                        chargeAmount = new BigDecimal("0.00").doubleValue();
                    }
                }

                if (chargeAmount > 0) {
                    res.put("carno", carno);
                    /** carparkId和orderId是传回的时候使用 **/
                    res.put("carparkId", carparkId);
                    res.put("carparkName", carparkName);
                    res.put("inTime", CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss", inTime));
                    res.put("parkingLen", parkingLen);
                    res.put("chargeAmount", chargeAmount);
                    res.put("orderId", orderInoutRecord.getChargeInfoId());
                }else {
                    return null;
                }

            }else {
                /** 白名单不进行手机支付 **/
                return null;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public String createOrderUrl(JSONObject jsonObject) {
        /** 车主确认停车信息，生成订单，构造自带参数的当面付订单接口url **/
        String res = "";
        try {
            //先建立交易流水表，然后把交易流水表的ID当做订单ID
            OrderTransaction orderTransaction = new OrderTransaction();
            orderTransaction.setOrderId(jsonObject.get("orderId") + "");
            orderTransaction.setPayType(payTypeEnum.G);
            orderTransaction.setPayTime(CommonUtils.getTimestamp());
            orderTransaction.setPayStatus(payStatusEnum.UN_PAID);
            //orderTransaction.setPayTypeName(payTypeEnum.BOSS_PAY.getDesc());
            orderTransaction.setTotalFee(new BigDecimal(jsonObject.get("chargeAmount") + ""));
            orderTransaction.setRealFee(new BigDecimal(jsonObject.get("chargeAmount") + ""));
            orderTransaction.setAddTime(CommonUtils.getTimestamp());
            orderTransaction.setUpdateTime(CommonUtils.getTimestamp());
            baseDao.save(orderTransaction);

            OrderInoutRecord orderInoutRecord = (OrderInoutRecord)baseDao.getById(OrderInoutRecord.class,orderTransaction.getOrderId());
            String reserveStr = "05|Q1#" + orderInoutRecord.getCarNo() + "|Q2#|Q3#" + orderInoutRecord.getCarparkName();

            //包装签名所需的参数
            String amt = (orderTransaction.getTotalFee().multiply(new BigDecimal("100"))).setScale(0,BigDecimal.ROUND_UP) + "";
            Map<String, String> packParams = new HashedMap();
            packParams.put("appid",AppInfo.allinPayAppId);
            packParams.put("c",AppInfo.allinpayClientId);
            packParams.put("oid",orderTransaction.getTransactionId());
            packParams.put("amt",amt);
            //packParams.put("returl","");
            //packParams.put("trxreserve",reserveStr);

            String signStr = createSign(packParams,AppInfo.allinpayKey,"utf-8");
            res = "https://syb.allinpay.com/sappweb/usertrans/cuspay?appid=" + AppInfo.allinPayAppId + "&c=" + AppInfo.allinpayClientId
                    + "&oid=" + orderTransaction.getTransactionId() + "&amt=" + amt   + "&sign=" + signStr;
            LOGGER.info("通联支付创建支付链接" + res);
        } catch (Exception e) {
            LOGGER.error("生成订单失败");
        }
        return res;
    }

    @Override
    public QueryRsp returnParkingInfo(TreeMap treeMap) {
        LOGGER.info("通联数据同步开始");
        QueryRsp res = new QueryRsp();
        try {
            String bizseq = FuncUtil.getMapValue(treeMap, "bizseq");

            res.initHead();
            res.setTrxcode(AppInfo.allinpayTrxCode);
            res.setBizseq(bizseq);
            if (CommonUtils.isEmpty(bizseq)){
                res.setRetcode("9999");
                res.setRetmsg("订单号不能为空");
                res.setAmount(0L);
                return res;
            }

            OrderTransaction orderTransaction = (OrderTransaction) baseDao.getById(OrderTransaction.class,bizseq);
            if (!CommonUtils.isEmpty(orderTransaction)){
                if (orderTransaction.getPayStatus().equals(payStatusEnum.UN_PAID)){
                    res.setRetcode("0000");
                    res.setRetmsg("获取订单信息成功");
                    res.setAmount(orderTransaction.getTotalFee().intValue() * 100);
                    OrderInoutRecord orderInoutRecord = (OrderInoutRecord)baseDao.getById(OrderInoutRecord.class,orderTransaction.getOrderId());
                    String reserveStr = "05|Q1#" + orderInoutRecord.getCarNo() + "|Q2#|Q3#" + orderInoutRecord.getCarparkName();
                    res.setTrxreserve(reserveStr);
                    /** 订单号加入到线程中查询 **/
                    JSONObject transInfo = new JSONObject();
                    transInfo.put("transactionId",orderTransaction.getTransactionId());
                    transInfo.put("chargeTime",CommonUtils.getTimestamp().toString());
                    AllinQueryThread.setSuspend(true);
                    AllinQueryThread.addTransIdToList(transInfo);
                    AllinQueryThread.setSuspend(false);
                }else {
                    res.setRetcode("9999");
                    res.setRetmsg("订单已失效");
                    res.setAmount(0L);
                    res.setTrxreserve("05");
                }
            }else {
                res.setRetcode("9999");
                res.setRetmsg("未知订单");
                res.setAmount(0L);
                res.setTrxreserve("05");
            }

        } catch (Exception e) {
            LOGGER.error("通联数据同步失败");
        }
        LOGGER.info("通联数据同步成功");
        return res;
    }

    @Override
    public boolean chargeSucessNotify(TreeMap treeMap) {
        LOGGER.info("通联数据异步通知开始");
        boolean res = false;
        try {
            String bizseq = FuncUtil.getMapValue(treeMap, "bizseq");
            String amount = FuncUtil.getMapValue(treeMap,"amount");

            if (CommonUtils.isEmpty(bizseq)){
                LOGGER.error("订单号不能为空");
                return res;
            }

            BigDecimal totalAmount = (new BigDecimal(amount)).divide(new BigDecimal("100"),2,BigDecimal.ROUND_UP);
            Double chargeAmount = totalAmount.doubleValue();


            OrderTransaction orderTransaction = (OrderTransaction) baseDao.getById(OrderTransaction.class,bizseq);
            if (!CommonUtils.isEmpty(orderTransaction)){
                if (orderTransaction.getPayStatus().equals(payStatusEnum.UN_PAID)){
                    orderTransaction.setTotalFee(totalAmount);
                    orderTransaction.setRealFee(totalAmount);
                    orderTransaction.setPayStatus(payStatusEnum.HAS_PAID);
                    orderTransaction.setPayTime(CommonUtils.getTimestamp());
                    baseDao.update(orderTransaction);

                    OrderInoutRecord orderInoutRecordRes = (OrderInoutRecord) baseDao.getById(OrderInoutRecord.class,orderTransaction.getOrderId());

                    if (!CommonUtils.isEmpty(orderInoutRecordRes)) {
                        try {
                            JSONObject json = new JSONObject();
                            json.put("carno", Base64.encodeToString(orderInoutRecordRes.getCarNo().getBytes()));
                            json.put("intime", orderInoutRecordRes.getInTime().toString());
                            json.put("chargePreAmount", chargeAmount.toString());
                            for (int i = 0; i < ParkSocketServer.getClientsCount(); i++) {
                                SocketClient client = ParkSocketServer.getClient(i);
                                json.put("result", "0");
                                SocketUtils.sendBizPackage(client, json, "WxChatPay");
                            }
                        } catch (BizException ex) {
                            LOGGER.error("通联支付信息推送失败:{},车牌:{},车道:{}", ex.getMessage(), orderInoutRecordRes.getCarNo(), orderInoutRecordRes.getInCarRoadName());
                        }
                    }
                    res = true;
                }else {
                    LOGGER.error("订单已失效");
                    return res;
                }
            }else {
                LOGGER.error("未知订单");
                return res;
            }

        } catch (Exception e) {
            LOGGER.error("异步通知失败");
        }
        LOGGER.info("通联数据异步通知成功");
        return res;
    }

    @Override
    public OrderTransaction getOrderTransById(String id) {
        OrderTransaction res = null;
        try {
            res = (OrderTransaction)baseDao.getById(OrderTransaction.class,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public void deleteOrderTransById(String id) {
        try {
            baseDao.deleteById(OrderTransaction.class,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean queryOrderStatus(String id) {
        LOGGER.info("通联数据查询开始");
        boolean res = false;
        try {
            Map<String, String> packParams = new HashedMap();
            packParams.put("cusid",AppInfo.allinpayBussinessId);
            packParams.put("appid",AppInfo.allinPayAppId);
            packParams.put("trxdate",CommonUtils.formatTimeStamp("MMdd",CommonUtils.getTimestamp()));
            packParams.put("orderid",id);
            packParams.put("trxid","");
            packParams.put("termno",AppInfo.allinpayClientId);
            packParams.put("resendnotify","1");
            packParams.put("randomstr",FuncUtil.getRandcode(8));
            String signStr = createSign(packParams,AppInfo.allinpayKey,"utf-8");
            packParams.put("sign",signStr);

            String responseStr = HttpRequestUtils.post("https://vsp.allinpay.com/apiweb/tranx/queryorder",packParams);
            if (CommonUtils.isEmpty(responseStr)){
                return false;
            }
            JSONObject resJson = JSON.parseObject(responseStr);
            if ("SUCCESS".equals(resJson.get("retcode") + "")){
                if ("0000".equals(resJson.get("trxstatus") + "")){
                    LOGGER.info("通联数据查询交易状态成功");
                    AllinQueryThread.setSuspend(true);
                    String orderid = resJson.get("orderid") + "";
                    String amount = resJson.get("amount") + "";
                    BigDecimal totalAmount = (new BigDecimal(amount)).divide(new BigDecimal("100"),2,BigDecimal.ROUND_UP);
                    Double chargeAmount = totalAmount.doubleValue();


                    OrderTransaction orderTransaction = (OrderTransaction) baseDao.getById(OrderTransaction.class,orderid);
                    if (!CommonUtils.isEmpty(orderTransaction)){
                        if (orderTransaction.getPayStatus().equals(payStatusEnum.UN_PAID)){
                            orderTransaction.setTotalFee(totalAmount);
                            orderTransaction.setRealFee(totalAmount);
                            orderTransaction.setPayStatus(payStatusEnum.HAS_PAID);
                            orderTransaction.setPayTime(CommonUtils.getTimestamp());
                            baseDao.update(orderTransaction);

                            OrderInoutRecord orderInoutRecordRes = (OrderInoutRecord) baseDao.getById(OrderInoutRecord.class,orderTransaction.getOrderId());
                            if (!CommonUtils.isEmpty(orderInoutRecordRes)) {
                                try {
                                    JSONObject json = new JSONObject();
                                    json.put("carno", Base64.encodeToString(orderInoutRecordRes.getCarNo().getBytes()));
                                    json.put("intime", orderInoutRecordRes.getInTime().toString());
                                    json.put("chargePreAmount", chargeAmount.toString());
                                    for (int i = 0; i < ParkSocketServer.getClientsCount(); i++) {
                                        SocketClient client = ParkSocketServer.getClient(i);
                                        json.put("result", "0");
                                        SocketUtils.sendBizPackage(client, json, "WxChatPay");
                                    }
                                } catch (BizException ex) {
                                    LOGGER.error("微信缴费信息推送失败:{},车牌:{},车道:{}", ex.getMessage(), orderInoutRecordRes.getCarNo(), orderInoutRecordRes.getInCarRoadName());
                                } finally {
                                    AllinQueryThread.setSuspend(false);
                                }
                            }else {
                                AllinQueryThread.setSuspend(false);
                            }
                            res = true;
                        }else {
                            LOGGER.error("订单已失效");
                            return res;
                        }
                    }else {
                        LOGGER.error("未知订单");
                        return res;
                    }
                }else{
                    return false;
                }
            }else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * 获取入场记录
     * @param carPlate
     * @param carparkId
     * @param outTime
     * @return
     */
    private OrderInoutRecord GetMatchInfo(String carPlate, String carparkId, Timestamp outTime){
        OrderInoutRecord matchInfo = new OrderInoutRecord();

        String hql = "from OrderInoutRecord where carNo = ? and carparkId = ? and "
                + "inTime <= ? and outRecordId IS NULL";
        matchInfo = (OrderInoutRecord)baseDao.getUnique(hql,carPlate,carparkId,outTime);
        return  matchInfo;
    }

    private String createSign(Map<String, String> packParams, String mchKey, String charSet) {
        String sign = null;
        try{
            /** 过滤参数数组中的空值和签名参数 **/
            packParams.put("key",mchKey);
            Map<String, String> filterParams = paramFilter(packParams);
            /** 把Map所有元素排序(按参数名ASCII码从小到大排序（字典序）)，并按照“参数=参数值”的模式用“&”字符拼接成参数字符串字符串 **/
            String paramStr = createParamString(filterParams);
            /** MD5运算生成签名，这里是第一次签名，用于调用统一下单接口。生成的签名必须是全大写的，所以需要一次转换 **/
            sign = sign(paramStr, mchKey, charSet);
        }catch (Exception e) {
            LOGGER.error("生成统一下单接口的签名失败！");
        }
        return sign;
    }

    /**
     * 过滤空值
     * @param signArray
     * @return
     */
    private Map<String, String> paramFilter(Map<String, String> signArray) {
        Map<String, String> result = new HashMap<String, String>();
        if (signArray == null || signArray.size() == 0) {
            return result;
        }
        for (String key : signArray.keySet()) {
            String value = signArray.get(key);
            /** 签名和签名类型有关的参数不参与生成签名 **/
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
                    || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    private String createParamString(Map<String, String> params) {
        /** 获取全部的key值列表 **/
        List<String> keys = new ArrayList<String>(params.keySet());
        /** 按参数名ASCII码字典序从小到大排序 **/
        Collections.sort(keys);
        String res = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {// 拼接时，最后一个参数后面不要拼接上&字符
                res = res + key + "=" + value;
            } else {
                res = res + key + "=" + value + "&";
            }
        }
        return res;
    }

    private String sign(String text, String mchkey, String input_charset) throws Exception{
        String res = md5(text.getBytes(input_charset)).toUpperCase();
        return res;
    }

    private byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

    private boolean validSign(TreeMap<String,String> param,String appkey) throws Exception{
        if(param!=null&&!param.isEmpty()){
            if(!param.containsKey("sign")){
                return false;
            }
            param.put("key", appkey);//将分配的appkey加入排序
            StringBuilder sb = new StringBuilder();
            String sign = param.get("sign").toString();
            param.remove("sign");
            for(String key:param.keySet()){
                String value = param.get(key);
                if(!CommonUtils.isEmpty(value))
                    sb.append(key).append("=").append(value).append("&");
            }
            if(sb.length()>0){
                sb.deleteCharAt(sb.length()-1);
            }
            String blank = sb.toString();
            System.out.println(blank+";"+sign);
            return sign.toLowerCase().equals(md5(blank.getBytes("utf-8")));
        }
        return false;
    }

    private String md5(byte[] b) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(b);
            byte[] hash = md.digest();
            StringBuffer outStrBuf = new StringBuffer(32);
            for (int i = 0; i < hash.length; i++) {
                int v = hash[i] & 0xFF;
                if (v < 16) {
                    outStrBuf.append('0');
                }
                outStrBuf.append(Integer.toString(v, 16).toLowerCase());
            }
            return outStrBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new String(b);
        }

    }

    public static void main(String[] args) {
        AllinpayServiceImpl allinpayService = new AllinpayServiceImpl();
        /*Map<String, String> packParams = new HashedMap();
        String timeStr = CommonUtils.formatTimeStamp("yyyyMMddHHmmss",CommonUtils.getTimestamp());
        String ranStr = FuncUtil.getRandcode(8);
        packParams.put("trxcode","VSP501");
        packParams.put("appid","00149681");
        packParams.put("cusid","55139106513YBSB");
        packParams.put("timestamp",timeStr);
        packParams.put("randomstr",ranStr);
        packParams.put("bizseq","4028c5226882c3b6016882c56f930000");
        packParams.put("trxstatus","0000");
        packParams.put("amount","500");
        packParams.put("trxid","11111");
        packParams.put("srctrxid","22222");
        packParams.put("trxday","20190125");
        packParams.put("paytime",timeStr);
        packParams.put("termid","33333");
        packParams.put("termbatchid","44444");
        packParams.put("traceno","55555");
        String signStr = allinpayService.createSign(packParams,"8222eb720945b48d8cd2e0d3c0ea5ad1","utf-8");*/

        Map<String, String> packParams = new HashedMap();
        String timeStr = CommonUtils.formatTimeStamp("yyyyMMddHHmmss",CommonUtils.getTimestamp());
        String ranStr = FuncUtil.getRandcode(8);
        packParams.put("appid","00149681");
        packParams.put("cusid","55139106513YBSB");
        packParams.put("trxcode","T001");
        packParams.put("version","01");
        packParams.put("timestamp",timeStr);
        packParams.put("randomstr",ranStr);
        packParams.put("source","2");
        packParams.put("termid","HJXL9MUU");
        packParams.put("bizseq","4028c5226882c3b6016882de02970007");
        packParams.put("trxreserve","05|");
        String signStr = allinpayService.createSign(packParams,"8222eb720945b48d8cd2e0d3c0ea5ad1","utf-8");
        System.out.printf(signStr);
    }


}
