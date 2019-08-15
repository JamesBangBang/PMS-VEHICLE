package com.starnetsecurity.parkClientServer.controller;

import com.alibaba.fastjson.JSONObject;
import com.starnetsecurity.common.controller.BaseController;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.web.MediaTypes;
import com.starnetsecurity.parkClientServer.allinpay.BaseRsp;
import com.starnetsecurity.parkClientServer.allinpay.FuncUtil;
import com.starnetsecurity.parkClientServer.allinpay.QueryRsp;
import com.starnetsecurity.parkClientServer.init.AppInfo;
import com.starnetsecurity.parkClientServer.service.AllinpayService;
import com.starnetsecurity.parkClientServer.service.impl.AllinpayServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by JAMESBANG on 2019/1/21.
 */
@Controller
@RequestMapping("allinPay")
public class AllinpayController extends BaseController{
    private static final Logger LOGGER = LoggerFactory.getLogger(AllinpayController.class);

    @Autowired
    AllinpayService allinpayService;

    /**
     * 获取订单信息
     * @param carno
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getChargeInfoByCarno", method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public JSONObject getChargeInfoByCarno(@RequestParam(value = "carno")String carno){
        LOGGER.info("1111");
        JSONObject res = new JSONObject();
        try {
            JSONObject data = allinpayService.getChargeInfoByCarno(carno);
            if (!CommonUtils.isEmpty(data)){
                res.put("code",200);
                res.put("data",data);
                res.put("msg","获取停车信息成功");
            }else {
                res.put("code",400);
                res.put("data",null);
                res.put("msg","获取停车信息失败");
            }
        }catch (BizException e){
            throw new BizException("获取停车信息失败");
        }
        return res;
    }

    /**
     * 返回url
     * @param jsonObject
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "createOrderUrl", method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public JSONObject createOrderUrl(@RequestBody JSONObject jsonObject){
        JSONObject res = new JSONObject();
        try {
            String data = allinpayService.createOrderUrl(jsonObject);
            if (!CommonUtils.isEmpty(data)){
                res.put("code",200);
                res.put("data",data);
                res.put("msg","构造URL成功");
            }else {
                res.put("code",400);
                res.put("data","");
                res.put("msg","构造URL失败");
            }
        }catch (BizException e){
            throw new BizException("构造URL失败");
        }
        return res;
    }

    /**
     * 数据同步接口
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping(value = "dataSynchronize", method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public void dataSynchronize(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{

        request.setCharacterEncoding("utf-8");
        TreeMap<String,String> params = getParams(request);
        String rspStr = "";
        try {
            if(FuncUtil.validSign(params, AppInfo.allinpayKey)){//验签成功,进行业务处理
                //验签成功后,分公司在此跟商户的系统进行个性化对接
                QueryRsp rsp = allinpayService.returnParkingInfo(params);
                rspStr = FuncUtil.toJsonResult(rsp);
            } else{//验签失败
                rspStr = getDefaultMsg("验签失败");
            }
        } catch (Exception e) {//处理异常
            // TODO: handle exception
            e.printStackTrace();
            rspStr = getDefaultMsg("处理异常");
        }
        finally{
            if(!FuncUtil.isEmpty(rspStr)){
                response.getOutputStream().write(rspStr.getBytes("utf-8"));
            }
            response.flushBuffer();
        }
    }

    /**
     * 异步通知接口
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "orderNotify", method = RequestMethod.POST,produces = MediaTypes.JSON_UTF_8)
    public void orderNotify(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        request.setCharacterEncoding("utf-8");
        TreeMap<String,String> params = getParams(request);
        String rspStr = "";
        try {
            if(FuncUtil.validSign(params, AppInfo.allinpayKey)){//验签成功,进行业务处理
                //验签成功后,分公司在此跟商户的系统进行个性化对接
                if (allinpayService.chargeSucessNotify(params)) {
                    rspStr = "success";
                }else {
                    rspStr = "error";
                }
            }
            else{//验签失败
                rspStr = "error";
                LOGGER.error("通联支付数据返回验签失败");
            }
        } catch (Exception e) {//处理异常
            // TODO: handle exception
            e.printStackTrace();
            rspStr = "error";
        }
        finally{
            if(!FuncUtil.isEmpty(rspStr)){
                response.getOutputStream().write(rspStr.getBytes("utf-8"));
            }
            response.flushBuffer();
        }
    }

    private String getDefaultMsg(String errmsg){
        BaseRsp rsp = BaseRsp.getFaildResult(errmsg);
        return FuncUtil.toJsonResult(rsp);
    }

    private TreeMap<String, String> getParams(HttpServletRequest request){
        TreeMap<String, String> map = new TreeMap();
        Map reqMap = request.getParameterMap();
        for(Object key:reqMap.keySet()){
            map.put(key.toString(), ((String[])reqMap.get(key))[0]);
        }
        return map;
    }


}
