package com.starnetsecurity.parkClientServer.service.impl;

import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.exception.BizException;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.parkClientServer.clientEnum.LogEnum;
import com.starnetsecurity.parkClientServer.entity.AdminUser;
import com.starnetsecurity.parkClientServer.entity.PostComputerManage;
import com.starnetsecurity.parkClientServer.service.DepartmentService;
import com.starnetsecurity.parkClientServer.service.LogOperationService;
import com.starnetsecurity.parkClientServer.service.PostComputerService;
import com.starnetsecurity.parkClientServer.service.UploadDataToCloudService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 宏炜 on 2017-12-28.
 */
@Service
public class PostComputerServiceImpl implements PostComputerService {

    @Autowired
    HibernateBaseDao baseDao;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    LogOperationService logOperationService;

    @Autowired
    UploadDataToCloudService uploadDataToCloudService;

    @Override
    public List<Map<String, Object>> getPageList(Integer size, Integer page) {
        page = page/size + 1;
        String hql = "from PostComputerManage where useMark >= 0";
        List<PostComputerManage> postComputerManages = (List<PostComputerManage>)baseDao.pageQuery(hql,page,size);
        List<Map<String,Object>> list = new ArrayList<>();
        for(PostComputerManage postComputerManage : postComputerManages){
            Map<String,Object> objectMap = new HashedMap();
            objectMap.put("postComputerId",postComputerManage.getPostComputerId());
            objectMap.put("postComputerName",postComputerManage.getPostComputerName());
            objectMap.put("postComputerIp",postComputerManage.getPostComputerIp());
            objectMap.put("addTime",postComputerManage.getAddTime());
            objectMap.put("status",postComputerManage.getStatus());
            objectMap.put("isAutoDeal",postComputerManage.getIsAutoDeal());
            objectMap.put("isVoicePlay",postComputerManage.getIsVoicePlay());
            if(!StringUtils.isBlank(postComputerManage.getOperationSource())){
                AdminUser adminUser = (AdminUser)baseDao.getById(AdminUser.class,postComputerManage.getOperationSource());
                if(!CommonUtils.isEmpty(adminUser)){
                    objectMap.put("operationName",adminUser.getTrueName());
                }else{
                    objectMap.put("operationName","");
                }
            }else{
                objectMap.put("operationName","");
            }
            list.add(objectMap);
        }
        return list;
    }

    @Override
    public Long getCount() {
        String hql = "select count(*) from PostComputerManage where useMark >= ?";
        return (Long)baseDao.getUnique(hql,0);
    }

    @Override
    public void mergePostComputer(String id,String name, String ip, String isAutoDeal, String isVoicePlay,
                                  AdminUser adminUser,String operateIp) throws BizException {
        if(StringUtils.isBlank(name)){
            throw new BizException("岗亭名称不能为空");
        }
        if(StringUtils.isBlank(ip)){
            throw new BizException("岗亭IP不能为空");
        }
        if (!StringUtils.isBlank(id)){
            //修改岗亭信息
            String hql = "select count(1) from PostComputerManage where postComputerIp = ? and useMark >= 0 and postComputerId <> ?";
            Long ipCount = (Long)baseDao.getUnique(hql,ip,id);
            if(ipCount > 0){
                throw new BizException("岗亭IP已被使用");
            }
        }else {
            String hql = "select count(1) from PostComputerManage where postComputerIp = ? and useMark >= 0";
            Long ipCount = (Long)baseDao.getUnique(hql,ip);
            if(ipCount > 0){
                throw new BizException("岗亭IP已被使用");
            }
        }

        if("on".equals(isAutoDeal)){
            isAutoDeal = "0";
        }else{
            isAutoDeal = "1";
        }
        if("on".equals(isVoicePlay)){
            isVoicePlay = "0";
        }else{
            isVoicePlay = "1";
        }

        PostComputerManage postComputerManage;
        if(CommonUtils.isEmpty(id)){
            postComputerManage = new PostComputerManage();
            postComputerManage.setPostComputerName(name);
            postComputerManage.setPostComputerIp(ip);
            postComputerManage.setIsAutoDeal(isAutoDeal);
            postComputerManage.setIsVoicePlay(isVoicePlay);
            postComputerManage.setSynPort("0");
            postComputerManage.setStatus(0);
            postComputerManage.setAddTime(CommonUtils.getTimestamp());
            postComputerManage.setOperationSource(adminUser.getId());
            postComputerManage.setDepartmentId(departmentService.getUserDepartmentIds(adminUser.getId()).get(0));
            postComputerManage.setUseMark(0);
            baseDao.save(postComputerManage);
            logOperationService.addOperatorLog(LogEnum.operatorPost,"新增岗亭信息，IP为：" + ip
                    ,operateIp,null,adminUser);
            uploadDataToCloudService.uploadPostInfoToServer(postComputerManage);
        }else{
            postComputerManage = (PostComputerManage)baseDao.getById(PostComputerManage.class,id);
            if(CommonUtils.isEmpty(postComputerManage)){
                throw new BizException("岗亭信息不存在");
            }
            postComputerManage.setPostComputerName(name);
            postComputerManage.setPostComputerIp(ip);
            postComputerManage.setIsAutoDeal(isAutoDeal);
            postComputerManage.setIsVoicePlay(isVoicePlay);
            postComputerManage.setSynPort("0");
            postComputerManage.setStatus(0);
            postComputerManage.setAddTime(CommonUtils.getTimestamp());
            postComputerManage.setOperationSource(adminUser.getId());
            postComputerManage.setDepartmentId(departmentService.getUserDepartmentIds(adminUser.getId()).get(0));
            postComputerManage.setUseMark(1);
            baseDao.update(postComputerManage);
            logOperationService.addOperatorLog(LogEnum.operatorPost,"修改岗亭信息，IP为：" + postComputerManage.getPostComputerIp()
                    ,operateIp,null,adminUser);
            uploadDataToCloudService.uploadPostInfoToServer(postComputerManage);
        }

    }

    @Override
    public void deletePostComputer(String id,AdminUser adminUser,String operateIp) {
        if(!StringUtils.isBlank(id)){
            PostComputerManage computerManage = (PostComputerManage)baseDao.getById(PostComputerManage.class,id);
            computerManage.setUseMark(-1);
            baseDao.update(computerManage);
            logOperationService.addOperatorLog(LogEnum.operatorPost,"删除岗亭信息，IP为：" + computerManage.getPostComputerIp()
                    ,operateIp,null,adminUser);
            uploadDataToCloudService.uploadPostInfoToServer(computerManage);
        }
    }
}
