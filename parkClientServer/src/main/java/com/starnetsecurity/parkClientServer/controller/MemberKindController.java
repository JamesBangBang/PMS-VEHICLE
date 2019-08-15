package com.starnetsecurity.parkClientServer.controller;

import com.alibaba.fastjson.JSON;
import com.starnetsecurity.common.controller.BaseController;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.util.Constant;
import com.starnetsecurity.common.util.PoiExcelUtil;
import com.starnetsecurity.common.web.MediaTypes;
import com.starnetsecurity.parkClientServer.chargeDetail.MemberKindDto;
import com.starnetsecurity.parkClientServer.entity.AdminUser;
import com.starnetsecurity.parkClientServer.init.AppInfo;
import com.starnetsecurity.parkClientServer.service.CarparkService;
import com.starnetsecurity.parkClientServer.service.MemberKindService;
import com.starnetsecurity.parkClientServer.service.WhiteListService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by 宏炜 on 2018-01-18.
 */
@Controller
@RequestMapping("/member/kind")
public class MemberKindController extends BaseController {


    @Autowired
    MemberKindService memberKindService;
    @Autowired
    WhiteListService whiteListService;
    @Autowired
    CarparkService carparkService;

    @ResponseBody
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Map add(@RequestBody MemberKindDto memberKindDto){

        Map res = new HashedMap();
        try {
            memberKindService.addMemberKindInfo(memberKindDto.getMemberKindInfo(),memberKindDto.getFeeSetDetail());
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }


    @ResponseBody
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public Map delete(String memberKindId){

        Map res = new HashedMap();
        try {
            memberKindService.deleteKindInfo(memberKindId);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "/get",method = RequestMethod.POST)
    public Map get(String memberKindId){

        Map res = new HashedMap();
        try {
            Map data = memberKindService.getMemberKindInfo(memberKindId);
            res.put("data",data);
            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }

    @ResponseBody
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public Map update(@RequestBody MemberKindDto memberKindDto){

        Map res = new HashedMap();
        try {
            if(!StringUtils.isBlank(memberKindDto.getMemberKindInfo().getString("id"))){
                memberKindService.updateMemberKindInfo(memberKindDto.getMemberKindInfo(),memberKindDto.getFeeSetDetail());
            }

            this.success(res);
        }catch (BizException ex){
            this.failed(res,ex);
        }
        return res;
    }



    //导入
    @ResponseBody
    @RequestMapping(value = "uploadExcel", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    public void uploadExcel(@RequestParam(value = "file", required = true) MultipartFile file,
                            @RequestParam(value = "importType", required = false) String importType,
                            @RequestParam(value = "operatorId", required = false) String operatorId,
                            @RequestParam(value = "operatorName", required = false) String operatorName,
                            HttpServletRequest request, ModelMap modelMap, HttpSession session, HttpServletResponse response) {
        String issueRet = "";
        try {

            if (file.getSize()==0){
                issueRet="1";
                throw new BizException("请选择文件");
            }
            String relativePath = "blackFile";
            Map resMap = CommonUtils.uploadFile(relativePath, file,request);
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            list = PoiExcelUtil.readExcelToListOrgMap(AppInfo.appRootRealPath + "/" + (String) resMap.get("serverPath"));
            String columnsNumber=(String) list.get(0).get("value10");
            String marking="";
            if(columnsNumber==null)
            {
                issueRet="1";
                throw new BizException("报表格式不正确，请导入相同字段数报表");
            }

            whiteListService.saveOwnerList(list,(AdminUser) getUser());

            this.success(modelMap);
        } catch (BizException ex) {
            issueRet="1";
            this.failed(modelMap, ex);
        }
        try {
            response.setContentType("text/html;charset=utf-8");
            modelMap.put("issueRet", issueRet);
            response.getWriter().write(JSON.toJSONString(modelMap));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //导出
    //导出报表
    @RequestMapping(value = "exportExcel")
    public void exportExcel(
                            @RequestParam(value = "carNo", required = false, defaultValue = "") String carNo,
                            @RequestParam(value = "chargeTypeName", required = false, defaultValue = "") String chargeTypeName,
                            @RequestParam(value = "driverName", required = false, defaultValue = "") String driverName,
                            @RequestParam(value = "driverTelephoneNumber", required = false, defaultValue = "") String driverTelephoneNumber,
                            @RequestParam(value = "driverInfo", required = false, defaultValue = "") String driverInfo,
                            @RequestParam(value = "carparkId", required = false, defaultValue = "") String carparkId,
                            @RequestParam(value = "depId", required = false, defaultValue = "") String depId,
                            @RequestParam(value = "packageKind", required = false, defaultValue = "") String packageKind,
                            HttpServletResponse response){
        Timestamp exportBeginTime = new Timestamp(System.currentTimeMillis());
        Timestamp exportEndTime = new Timestamp(System.currentTimeMillis());
        List<Map<String,Object>> headList = new ArrayList<Map<String, Object>>(
                Arrays.asList(
                        new HashMap<String, Object>(){{
                            put("key","carNo");
                            put("name","车牌");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","carparkName");
                            put("name","车场名称");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","driverName");
                            put("name","车主姓名");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","driverTelephoneNumber");
                            put("name","联系方式");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","driverInfo");
                            put("name","车主信息");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","chargeTypeName");
                            put("name","收费类型");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","starTime");
                            put("name","预设开始时间");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","endTime");
                            put("name","预设结束时间");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","surplusNumber");
                            put("name","预设次数");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","surplusAmount");
                            put("name","预设金额");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","parkingLot");
                            put("name","车位编号");
                        }},
                        new HashMap<String, Object>(){{
                            put("key","menberCount");
                            put("name","预设有效车数");
                        }}
                )
        );
        try {
            List<Map<String, Object>> list = whiteListService.getWhiteListExport(carNo,
                    chargeTypeName,
                    driverName,
                    driverTelephoneNumber,
                    driverInfo,
                    carparkId,
                    depId,
                    packageKind
                   );
            String sheetName="白名单导出";//表头
            PoiExcelUtil.ListMapExpotToExcel(sheetName, exportBeginTime, exportEndTime, headList, list, response);
        } catch (BizException e) {
            e.printStackTrace();
        }

    }







    public Object getUser(){
        Subject subject = SecurityUtils.getSubject();
        if(subject.isRemembered() || subject.isAuthenticated()){
            return subject.getSession().getAttribute(Constant.SESSION_LOGIN_USER);
        }else{
            return null;
        }
    }
}
