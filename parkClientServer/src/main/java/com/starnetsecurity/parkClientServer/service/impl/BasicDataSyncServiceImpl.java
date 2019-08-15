package com.starnetsecurity.parkClientServer.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.AesUtil;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.util.Constant;
import com.starnetsecurity.common.util.HttpRequestUtils;
import com.starnetsecurity.parkClientServer.entity.AdminRole;
import com.starnetsecurity.parkClientServer.entity.AdminUser;
import com.starnetsecurity.parkClientServer.service.BasicDataSyncService;
import okhttp3.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BasicDataSyncServiceImpl implements BasicDataSyncService {

    final String ORG_MERGE = "/resource/manage/merge";
    final String ORG_DELETE = "/resource/manage/delete";
    final String PERSON_MERGE = "/person/manage/merge";
    final String PERSON_DELETE = "/person/manage/delete";
    final String USERURL_MERGE = "/user/manage/merge";
    final String USERURL_DELETE = "/user/manage/delete";
    final String ROLEURL_MERGE = "/role/manage/merge";
    final String ROLEURL_DELETE = "/role/manage/delete";

    @Autowired
    HibernateBaseDao hibernateBaseDao;

    @Override
    public void regist() {
        if(!isUseBasicPlatform()) return;
        String serverIP = Constant.getPropertie("SYNC.BASIC_SERVER_IP");
        Integer serverPORT = Integer.parseInt(Constant.getPropertie("SYNC.BASIC_SERVER_PORT"));
        String URL = "http://" + serverIP + ":" + serverPORT.toString() +"/sub/system/biz/register";
        String IP = Constant.getPropertie("SYNC.SYS_IP");
        Integer PORT = Integer.parseInt(Constant.getPropertie("SYNC.SYS_PORT"));
        String icon = Constant.getPropertie("SYNC.SYS_ICON");
        String mainUri = Constant.getPropertie("SYNC.SYS_MAIN_URI");
        String permission = Constant.getPropertie("SYNC.SYS_TAG");
        String pushUri = Constant.getPropertie("SYNC.SYS_PUSH_URI");
        String serverName  = Base64.decodeToString(Constant.getPropertie("SYNC.SYS_NAME"));

        subSysRegist(URL,
                IP,
                PORT,
                icon,
                mainUri,
                permission,
                pushUri,
                serverName);

    }

    @Override
    public void subSysRegist(String URL, String IP, Integer PORT, String icon, String mainUri, String permission, String pushUri, String serverName) throws BizException {
        if(!isUseBasicPlatform()) return;
        String outPutString = "";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("IP", IP);
        jsonObject.put("PORT", PORT);
        jsonObject.put("icon", icon);
        jsonObject.put("mainUri", mainUri);
        jsonObject.put("permission", permission);
        jsonObject.put("pushUri", pushUri);
        jsonObject.put("serverName", serverName);
        outPutString = JSON.toJSONString(jsonObject);
        try {
            httpRequestEx(URL, outPutString);
        }catch (IOException e){
            throw new BizException("注册系统失败！");
        }
    }

    @Override
    public void synChronizeSystemAdminRole(AdminRole adminRole, List<String> roleResourceList, String CtrlType) throws BizException {
        if(isUseBasicPlatform()) {
            try {
                if ("0".equals(CtrlType) || "1".equals(CtrlType)) {
                    //同步merge操作
                    JSONObject jsonObject = new JSONObject();
                    JSONObject role = new JSONObject();
                    List<String> permissions = roleResourceList;

                    role.put("id", adminRole.getId());
                    role.put("roleName", adminRole.getRoleName());
                    role.put("description", adminRole.getDescription());
                    role.put("status", adminRole.getStatus());
                    role.put("roleTag", adminRole.getRoleTag());
                    role.put("status", adminRole.getStatus());
                    role.put("sysTag", Constant.getPropertie(""));

                    jsonObject.put("roleForm", role);
                    jsonObject.put("permissions", permissions);
                    jsonObject.put("sysTag", Constant.getPropertie("SYNC.SYS_TAG"));
                    String url = getCompleteURL(ROLEURL_MERGE);
                    String outPutString = JSON.toJSONString(jsonObject);
                    if (SendSynMessage(url, outPutString) < 0) {
                        throw new BizException("操作角色信息，与平台同步数据失败!");
                    }
                } else if ("2".equals(CtrlType)) {
                    //删除操作同步到平台
                    String url = getCompleteURL(ROLEURL_DELETE);
                    String outPutString = "roleId=" + adminRole.getId() + "&sysTag="+Constant.getPropertie("SYNC.SYS_TAG");
                    if (SendSynFormMessage(url, outPutString) < 0) {
                        throw new BizException("删除角色信息，与平台同步数据失败!");
                    }
                }
            } catch (IOException e) {
                //网络断开，不能与基础平台同步，直接抛出异常
                throw new BizException("与平台同步数据失败！请检查网络");
            }
        }
    }

    @Override
    public Boolean isUseBasicPlatform() {
        String isUseBasicPlatform = Constant.getPropertie("isUseBasicPlatform");
        if(CommonUtils.isEmpty(isUseBasicPlatform)) return false;
        if("1".equals(isUseBasicPlatform)) return true;
        else return false;
    }

    @Override
    public void setToken(String token) {
        Constant.setPropertie("token", token);
    }

    @Override
    public String getToken() {
        Map<String,Object> res = new HashedMap();
        String hql = "from AdminUser where username = ?";
        AdminUser adminUser = (AdminUser) hibernateBaseDao.getUnique(hql,"root");
        res.put("loginTime",CommonUtils.getTimestamp());
        res.put("username",adminUser.getUserName());
        res.put("password",adminUser.getUserPwd());
        res.put("rememberMe",false);
        String deToken = JSON.toJSONString(res, SerializerFeature.WriteMapNullValue);
        AesCipherService aesCipherService = new AesCipherService();
        String encodeToken = aesCipherService.encrypt(deToken.getBytes(), Hex.decode(AesUtil.aesKey)).toBase64().replaceAll("\\+", "-").replaceAll("/", "*");
        return encodeToken;
    }

    public void httpRequestEx(String URL, String outPutString) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, outPutString);
        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .build();

        Response response = client.newCall(request).execute();
    }

    public String getCompleteURL(String url){
        return "http://" + Constant.getPropertie("SYNC.BASIC_SERVER_IP") + ":" + Constant.getPropertie("SYNC.BASIC_SERVER_PORT") + url + "?token=" + getToken();
    }

    public Integer SendSynFormMessage(String URL, String outPutString) throws IOException{
        Integer result = null;
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, outPutString);
            Request request = new Request.Builder()
                    .url(URL)
                    .post(body)
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("cache-control", "no-cache")
                    .build();
            Response response = client.newCall(request).execute();
            JSONObject json = (JSONObject)JSON.parse(response.body().bytes());
            result = json.getInteger("result");
            //判断是否token过期，重新获取
            reRegistrySubSystem(result);
            response.body().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 需要使用到基础平台，才与基础平台进行数据同步，返回值：0-成功  <0-失败
     * @param URL
     * @param outPutString
     * @return
     * @throws IOException
     */
    public Integer SendSynMessage(String URL, String outPutString) throws IOException,BizException{
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, outPutString);
        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .build();

        Response response = client.newCall(request).execute();
//        String json = new String();
        JSONObject json = (JSONObject)JSON.parse(response.body().bytes());
        if(CommonUtils.isEmpty(json)) throw new IOException("");
        Integer result = json.getInteger("result");
        //判断是否token过期，重新获取
        reRegistrySubSystem(result);
        return result;
    }

    public void reRegistrySubSystem(Integer result) throws IOException{
        //判断result是否小于-1000，是的话，表示token过期，重新获取注册
        if(result < -1000){
            try {
                regist();
            }catch (BizException e){
                throw new IOException("token过期，重新获取token失败！");
            }
        }
    }

    /**
     * merge角色信息，包括角色对应权限的操作，这个操作不需要再同步到平台
     * @param adminRole
     * @param permissions
     * @throws BizException
     */
    @Override
    public void mergeAdminRole(AdminRole adminRole, List<String> permissions) throws BizException{
        hibernateBaseDao.merge(adminRole);
    }

    @Override
    public void deleteAdminRole(String id) throws BizException{
        try {
            String hql = "delete from AdminRoleResource where adminRoleId = ?";
            hibernateBaseDao.update(hql, id);
            hql = "delete from AdminRole where id = ?";
            hibernateBaseDao.update(hql, id);
        }catch (Exception e){
            throw new BizException("同步时，删除角色信息失败！");
        }
    }

    /**
     * merge用户信息，这个操作不需要再同步到平台
     * @param adminUser
     * @throws BizException
     */
    @Override
    public void mergeAdminUser(AdminUser adminUser) throws BizException{
        hibernateBaseDao.merge(adminUser);
    }

    @Override
    public void deleteAdminUser(String id) throws BizException{
        try {
            String hql = "delete from AdminUser where id = ?";
            hibernateBaseDao.update(hql, id);
        }catch (Exception e){
            throw new BizException("同步时，删除用户信息失败！");
        }
    }

    @Override
    public void synChronizeSystemAdminUser(AdminUser adminUser,  String roleId, String CtrlType) throws BizException{
        if(isUseBasicPlatform()) {
            try {
                if ("0".equals(CtrlType) || "1".equals(CtrlType)) {
                    //同步merge操作
                    JSONObject jsonObject = new JSONObject();
                    JSONObject user = new JSONObject();
                    user.put("id", adminUser.getId());
                    user.put("userName", adminUser.getUserName());
                    user.put("userPwd", adminUser.getUserPwd());
                    user.put("trueName", adminUser.getTrueName());
                    user.put("telephone", adminUser.getTelephone());
                    user.put("email", adminUser.getEmail());
                    user.put("sex", adminUser.getSex());
                    user.put("identifyNo", adminUser.getIdentifyNo());
                    user.put("resign", adminUser.getResign());
                    user.put("qq", adminUser.getQq());
                    user.put("status", adminUser.getStatus());
                    user.put("sysTag", adminUser.getSysTag());

                    List<String> roleIds = new ArrayList<>();
                    roleIds.add(roleId);
                    jsonObject.put("user", user);
                    jsonObject.put("roles", roleIds);
                    jsonObject.put("sysTag", Constant.getPropertie("SYNC.SYS_TAG"));
                    String url = getCompleteURL(USERURL_MERGE);
                    String outPutString = JSON.toJSONString(jsonObject);
                    if (SendSynMessage(url, outPutString) < 0) {
                        throw new BizException("操作用户信息，与平台同步数据失败!");
                    }
                } else if ("2".equals(CtrlType)) {
                    //删除操作同步到平台
                    String url = getCompleteURL(USERURL_DELETE);
                    String outPutString = "userId=" + adminUser.getId()+ "&sysTag="+Constant.getPropertie("SYNC.SYS_TAG");
                    if (SendSynFormMessage(url, outPutString) < 0) {
                        throw new BizException("删除用户信息，与平台同步数据失败!");
                    }
                }
            } catch (IOException e) {
                //网络断开，不能与基础平台同步，直接抛出异常
                throw new BizException("与平台同步数据失败！请检查网络");
            }
        }
    }

}