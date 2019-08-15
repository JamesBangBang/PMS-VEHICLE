package com.starnetsecurity.parkClientServer.service.impl;

import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.parkClientServer.entity.CarparkInfo;
import com.starnetsecurity.parkClientServer.service.InOutMsgService;
import com.starnetsecurity.parkClientServer.ws.ParkWebSocketHandler;
import com.starnetsecurity.parkClientServer.ws.WebSocketUser;
import org.apache.commons.collections.map.HashedMap;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 宏炜 on 2018-01-21.
 */
@Service
public class InOutMsgServiceImpl implements InOutMsgService {

    @Autowired
    HibernateBaseDao baseDao;

    @Override
    public void carInMsgPush(String parkId, String carNo, String time, String port) throws IOException {
        Map<String,String> msg = new HashedMap();
        CarparkInfo carparkInfo = (CarparkInfo)baseDao.getById(CarparkInfo.class,parkId);
        msg.put("carNo",carNo);
        msg.put("time",time);
        msg.put("parkName",carparkInfo.getCarparkName());
        msg.put("parkId",carparkInfo.getCarparkId());
        msg.put("info","入场");
        msg.put("port",port);
        List<String> users = new ArrayList<>();
        String sql = "select user_name from admin_user where id in (select admin_user_id from admin_org_resource where res_type='PARK' and res_id = :parkId)";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter("parkId",carparkInfo.getCarparkId());
        List<String> list = sqlQuery.list();

        for(String username : list){
            users.add(String.valueOf(username));
        }

        Map<String,Object> data = new HashedMap();
        data.put("msg",msg);
        data.put("users",users);
        data.put("type","inout");
        pushInOutMsg(data);
    }

    @Override
    public void carOutMsgPush(String parkId, String carNo, String time, String port, BigDecimal fee) throws IOException {
        Map<String,String> msg = new HashedMap();
        CarparkInfo carparkInfo = (CarparkInfo)baseDao.getById(CarparkInfo.class,parkId);
        msg.put("carNo",carNo);
        msg.put("time",time);
        msg.put("parkName",carparkInfo.getCarparkName());
        msg.put("parkId",carparkInfo.getCarparkId());
        msg.put("info",(CommonUtils.isEmpty(fee) ? 0 : fee.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + " 元");
        msg.put("port",port);
        List<String> users = new ArrayList<>();
        String sql = "select user_name from admin_user where id in (select admin_user_id from admin_org_resource where res_type='PARK' and res_id = :parkId)";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter("parkId",carparkInfo.getCarparkId());
        List<String> list = sqlQuery.list();

        for(String username : list){
            users.add(String.valueOf(username));
        }

        Map<String,Object> data = new HashedMap();
        data.put("msg",msg);
        data.put("users",users);
        data.put("type","inout");
        pushInOutMsg(data);
    }

    @Override
    public void pushInOutMsg(Map<String, Object> params) throws IOException {
        String type = (String)params.get("type");
        Map<String,String> msg = (Map<String,String>)params.get("msg");
        List<String> users = (List<String>)params.get("users");
        for(WebSocketUser webSocketUser : ParkWebSocketHandler.socketUser){
            if(users.contains(webSocketUser.getUserName())){
                webSocketUser.sendMsg(msg);
            }
        }
    }
}
