package com.starnetsecurity.parkClientServer.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.common.util.Util;
import com.starnetsecurity.commonService.bizEnum.BizEnum;
import com.starnetsecurity.commonService.entity.BizData;
import com.starnetsecurity.commonService.util.DeviceManageUtils;
import com.starnetsecurity.commonService.util.StarnetDeviceUtils;
import com.starnetsecurity.parkClientServer.clientEnum.LogEnum;
import com.starnetsecurity.parkClientServer.entity.*;
import com.starnetsecurity.parkClientServer.service.CarparkService;
import com.starnetsecurity.parkClientServer.service.LogOperationService;
import com.starnetsecurity.parkClientServer.service.UploadDataToCloudService;
import com.sun.javafx.collections.MappingChange;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

@Service
public class CarparkServiceImpl implements CarparkService {

    @Autowired
    HibernateBaseDao baseDao;

    @Autowired
    LogOperationService logOperationService;

    @Autowired
    UploadDataToCloudService uploadDataToCloudService;

    @Override
    public List<Map<String, Object>> getCaparks(String userId) {
        String hql = "from AdminUser where userName = ?";
        AdminUser adminUser = (AdminUser) baseDao.getUnique(hql,"root");

        String admin_org_resource_hql = "from AdminOrgResource  where adminUserId ='" + userId + "'  and resType='PARK' " +
                "order by recPriority desc";
        List<AdminOrgResource> adminOrgResources = (List<AdminOrgResource>) baseDao.queryForList(admin_org_resource_hql);
        List<CarparkInfo> carparkInfos = new ArrayList<>();
        if(adminUser.getId().equals(userId)){
            carparkInfos = (List<CarparkInfo>)baseDao.queryForList("from CarparkInfo where useMark >= 0");
        }else{
            if (!CollectionUtils.isEmpty(adminOrgResources)) {
                for (AdminOrgResource adminOrgResource : adminOrgResources) {
                    String carpark_info_hql = "from CarparkInfo WHERE  carparkId= ? and useMark >= 0";
                    CarparkInfo carparkInfo = (CarparkInfo) baseDao.getUnique(carpark_info_hql, adminOrgResource.getResId());
                    if (!CommonUtils.isEmpty(carparkInfo)) {
                        carparkInfos.add(carparkInfo);
                    }
                }
            }
        }

        List<Map<String, Object>> res = new ArrayList<>();
        for (CarparkInfo carparkInfo : carparkInfos) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("carparkId", carparkInfo.getCarparkId());
            obj.put("carparkName", carparkInfo.getCarparkName());
            obj.put("lon", carparkInfo.getLon());
            obj.put("lat", carparkInfo.getLat());
            res.add(obj);
        }
        return res;
    }

    @Override
    public CarparkInfo getRemainPark(String parkId) {
        String carparkInfo_hql = "from CarparkInfo  where carparkId ='" + parkId + "'";
        CarparkInfo carparkInfo = (CarparkInfo) baseDao.getUnique(carparkInfo_hql);
        return carparkInfo;
    }


    @Override
    public List<Object[]> getRemainParkhistory(String parkId) {
        String getCarparkChargeRecordsToday_sql = "SELECT\n" +
                "\tDATE_FORMAT(add_time, '%Y-%m-%d'),\n" +
                "\tcount(1)\n" +
                "FROM\n" +
                "\tinout_record_info\n" +
                "WHERE\n" +
                "\tcarpark_id = '" + parkId + "'\n" +
                "AND inout_flag = 0\n" +
                "AND add_time >= DATE_SUB(\n" +
                "\tDATE_FORMAT(now(), '%Y-%m-%d'),\n" +
                "\tINTERVAL 6 DAY\n" +
                ")\n" +
                "GROUP BY\n" +
                "\tDATE_FORMAT(add_time, '%Y-%m-%d');";
        return (List<Object[]>)baseDao.getSQLList(getCarparkChargeRecordsToday_sql);
    }

    @Override
    public List<Map<String, Object>> getParkId(AdminUser adminUser) {
        String getParkId_sql = "SELECT\n" +
                "\tae.res_id,c.carpark_name,c.department_id\n" +
                " FROM\n" +
                "\t admin_org_resource ae\n" +
                " LEFT JOIN carpark_info c ON ae.res_id = c.carpark_id" +
                " WHERE\n" +
                "\tae.res_type = 'PARK'\n" +
                "and ae.admin_user_id = '" + adminUser.getId() + "'  and c.use_mark >= 0" +
                " ORDER BY \n" +
                "\tae.rec_priority DESC LIMIT 0,1 ";
        if(adminUser.getUserName().equals("root")){
            getParkId_sql = "select carpark_id as res_id,carpark_name,department_id from carpark_info limit 0,1";
        }

        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(getParkId_sql);
        List<Map<String, Object>> parkIds = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return parkIds;
    }

    //获取车场列表
    @Override
    public List getCarParksInfoList(List<CarparkInfo> carparkInfos, Integer start, Integer length) {
        String sql = "SELECT " +
                "\ta.*," +
                "\tb.carpark_name AS parentCarparkName\n" +
                "FROM\n" +
                "\tcarpark_info a\n" +
                "\tLEFT  JOIN carpark_info b ON a.own_carpark_no = b.carpark_id AND b.use_mark>=0\n" +
                "WHERE\n" +
                "a.carpark_id IN (\n" +
                "\t:carparkIds\n" +
                ") AND a.use_mark >= 0";

        SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        List<String> carparksId = new ArrayList<>();
        for (CarparkInfo carparkInfo : carparkInfos) {
            carparksId.add(carparkInfo.getCarparkId());
        }
        if(CollectionUtils.isEmpty(carparksId)){
            return Collections.EMPTY_LIST;
        }
        query.setParameterList("carparkIds", carparksId);
        query.setFirstResult(start).setMaxResults(length);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List list = query.list();
        return list;
    }

    @Override
    public Long countCarParksInfo(List<CarparkInfo> carparkInfos) {
        String sql = "SELECT COUNT(1) FROM\n" +
                "carpark_info a\n" +
                "WHERE\n" +
                " a.carpark_id IN (\n" +
                "\t:carparkIds\n" +
                ") AND a.use_mark >= 0";
        SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        List<String> carparksId = new ArrayList<>();
        for (CarparkInfo carparkInfo : carparkInfos) {
            carparksId.add(carparkInfo.getCarparkId());
        }
        if(CollectionUtils.isEmpty(carparksId)){
            return 0L;
        }
        query.setParameterList("carparkIds", carparksId);
        return ((BigInteger) query.uniqueResult()).longValue();
    }

    //获取车场详细信息
    @Override
    public Map<String, Object> getCarParksInfo(String carParkId) {

        CarparkInfo carparkInfo = (CarparkInfo) baseDao.getById(CarparkInfo.class, carParkId);
        if (CommonUtils.isEmpty(carparkInfo)) {
            return Collections.EMPTY_MAP;
        }

        String tmpStr = JSON.toJSONString(carparkInfo, SerializerFeature.WriteMapNullValue);
        JSONObject res = JSON.parseObject(tmpStr);

        if (!StringUtils.isBlank(carparkInfo.getOperationSource())) {
            PostComputerManage postComputerManage = (PostComputerManage) baseDao.getById(PostComputerManage.class, carparkInfo.getOperationSource());
            res.put("postComputerName", postComputerManage.getPostComputerName());
        } else {
            res.put("postComputerName", "");
        }
        if (!StringUtils.isBlank(carparkInfo.getOwnCarparkNo())) {
            CarparkInfo ownCarpark = (CarparkInfo) baseDao.getById(CarparkInfo.class, carparkInfo.getOwnCarparkNo());
            res.put("ownCarparkName", ownCarpark.getCarparkName());
        } else {
            res.put("ownCarparkName", "");
        }

        if (!StringUtils.isBlank(carparkInfo.getDepartmentInfo().getDepId())) {
            DepartmentInfo departmentInfo = (DepartmentInfo) baseDao.getById(DepartmentInfo.class, carparkInfo.getDepartmentInfo().getDepId());
            res.put("departmentName", departmentInfo.getDepName());
        } else {
            res.put("departmentName", "");
        }

        //判断是否存在
        if (!StringUtils.isBlank(carparkInfo.getIsMulticar())) {
            if (carparkInfo.getIsMulticar().equals("0")) {
                res.put("isMulticar", "no");
            } else {
                res.put("isMulticar", "yes");
            }
        }

        if (carparkInfo.getIsOverdueAutoOpen().equals("0")) {
            res.put("isOverdueAutoOpen", "no");
        } else {
            res.put("isOverdueAutoOpen", "yes");
        }
        if (carparkInfo.getIsAutoOpen().equals("0")) {
            res.put("isAutoOpen", "yes");
        } else {
            res.put("isAutoOpen", "no");
        }
        if ("0".equals(carparkInfo.getCloseType())) {
            res.put("closeType", "yes");
        } else {
            res.put("closeType", "no");
        }
        if (carparkInfo.getIsClose().equals("0")) {
            res.put("isClose", "no");
        } else {
            res.put("isClose", "yes");
        }
        //判断是否存在
        if (carparkInfo.getIfIncludeCaculate() != null) {
            if (carparkInfo.getIfIncludeCaculate().equals(0)) {
                res.put("ifIncludeCaculate", "no");
            } else {
                res.put("ifIncludeCaculate", "yes");
            }
        }

        if (carparkInfo.getIsTestRunning().equals("0")) {
            res.put("isTestRunning", "no");
        } else {
            res.put("isTestRunning", "yes");
        }
        res.put("carparkId", carparkInfo.getCarparkId());

//        String hql = "from Parks where clientId = ?";
//        Parks parks = (Parks)baseDao.getUnique(hql,carParkId);
//        res.put("lon",parks.getLon());
//        res.put("lat",parks.getLat());
        return res;
    }

    @Override
    public List<Map<String, Object>> getCarParksInfoSelect(List<String> depId) {

        String hql = "from CarparkInfo where departmentInfo.id in (:departmentIds)";
        Query query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
        query.setParameterList("departmentIds", depId);

        List<CarparkInfo> carparkInfos = (List<CarparkInfo>) query.list();
        List<Map<String, Object>> res = new ArrayList<>();
        for (CarparkInfo carparkInfo : carparkInfos) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("carparkId", carparkInfo.getCarparkId());
            obj.put("carparkName", carparkInfo.getCarparkName());
            res.add(obj);
        }


        return res;
    }

    @Override
    public List<String> getUserCarParksIdList(String userId) {
        String hql = "from AdminUser where userName = ?";
        AdminUser adminUser = (AdminUser) baseDao.getUnique(hql,"root");
        if(adminUser.getId().equals(userId)){
            hql = "select carparkId from CarparkInfo where useMark >= 0";
            return (List<String>) baseDao.queryForList(hql);
        }else{
            hql = "select resId from AdminOrgResource where adminUserId = ? and resType = 'PARK'";
            return (List<String>) baseDao.queryForList(hql, userId);
        }

    }

    //更新停车场信息
    @Override
    public void saveParks(CarparkInfo parks,String ip,AdminUser adminUser) {
        parks.setUseMark(1);
        baseDao.save(parks);
        String hql = "FROM MemberKind WHERE carparkInfo = ? AND useType = 0";
        MemberKind memberKindOut = (MemberKind)baseDao.getUnique(hql,parks);
        memberKindOut.setKindName(parks.getCarparkName() + "外部车收费类型");
        memberKindOut.setUseMark(1);
        baseDao.save(memberKindOut);

        hql = "FROM MemberKind WHERE carparkInfo = ? AND useType = 1";
        MemberKind memberKindIn = (MemberKind)baseDao.getUnique(hql,parks);
        memberKindIn.setKindName(parks.getCarparkName() + "内部车收费类型");
        memberKindIn.setUseMark(1);
        baseDao.save(memberKindIn);
        logOperationService.addOperatorLog(LogEnum.operatePark,"更新" + parks.getCarparkName() + "的信息",ip,null,adminUser);

        uploadDataToCloudService.uploadParkInfoToServer(parks);
        uploadDataToCloudService.uploadMemberKindInfoToServer(memberKindOut,null,null);
        uploadDataToCloudService.uploadMemberKindInfoToServer(memberKindIn,null,null);

    }

    @Override
    public List getLastCarparkInOutRecord(String carparkId) {
        String hql = "from InoutRecordInfo where inoutStatus < 2 order by inoutTime desc";

        List<InoutRecordInfo> inoutRecordInfos = (List<InoutRecordInfo>)baseDao.pageQuery(hql,1,10);
        List<Map<String, String>> resList = new ArrayList<>();
        for (int i = (inoutRecordInfos.size() - 1); i >= 0; i --) {
            InoutRecordInfo inoutRecordInfo = inoutRecordInfos.get(i);
            Map<String, String> map = new HashedMap();
            map.put("in_out_car_no", inoutRecordInfo.getCarNo());
            map.put("in_out_time", CommonUtils.formatTimeStamp("yyyy-MM-dd HH:mm:ss", inoutRecordInfo.getInoutTime()));
            map.put("road_name", inoutRecordInfo.getCarRoadName());
            Byte inoutFlag = (byte)0;
            if(inoutRecordInfo.getInoutFlag().equals(inoutFlag)){
                map.put("real_amount","入场");
            }else{
                hql = "from OrderInoutRecord where outRecordId = ?";
                OrderInoutRecord orderInoutRecord = (OrderInoutRecord)baseDao.getUnique(hql,inoutRecordInfo.getInoutRecordId());
                if(!CommonUtils.isEmpty(orderInoutRecord)){
                    Double fee = new BigDecimal(orderInoutRecord.getChargeActualAmount()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    map.put("real_amount",fee + " 元");
                }else{
                    map.put("real_amount","0 元");
                }

            }

            resList.add(map);
        }
        return resList;

    }

    @Override
    public List<Map<String, Object>> getUserDepartments(String userId) {
        String sql = "select dep_id as departmentId,dep_name as departmentName from department_info";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @Override
    public List<Map<String, Object>> getOwnCarparkInfo(String userId) {
        String hql = "from AdminUser where userName = ?";
        AdminUser adminUser = (AdminUser)baseDao.getUnique(hql,"root");
        String sql;
        if(adminUser.getId().equals(userId)){
            sql = "SELECT\n" +
                    " carpark_id as carparkId, carpark_name as carparkName\n" +
                    "from carpark_info where use_mark >= 0\n";
        }else{
            sql = "SELECT\n" +
                    " carpark_id as carparkId," +
                    " carpark_name as carparkName, " +
                    " admin_user_id as userId " +
                    " \n" +
                    " FROM\n" +
                    " admin_org_resource\n" +
                    " LEFT JOIN carpark_info ON admin_org_resource.res_id = carpark_info.carpark_id\n" +
                    " WHERE\n" +
                    " admin_user_id = '" + userId + "' " +
                    " AND carpark_info.use_mark >= 0 " +
                    " AND res_type = 'PARK' AND (own_carpark_no = '' OR own_carpark_no IS  NULL )";
        }


        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @Override
    public List<Map<String, Object>> getUserDepartmentsParks(String userId, String departId) {
        String hql = "from AdminUser where userName = ?";
        AdminUser adminUser = (AdminUser)baseDao.getUnique(hql,"root");
        String sql;
        if(adminUser.getId().equals(userId)){
            sql = "SELECT\n" +
                    "\tcarpark_id AS caparkId,\n" +
                    "\tcarpark_name AS carparkName\n" +
                    "FROM\n" +
                    "\tcarpark_info\n" +
                    "WHERE\n" +
                    "\tuse_mark >= 0";
        }else{
            sql = "SELECT\n" +
                    "\tcarpark_name as carparkName,carpark_id as caparkId\n" +
                    "FROM\n" +
                    "\tadmin_org_resource\n" +
                    "LEFT JOIN carpark_info ON admin_org_resource.res_id = carpark_info.carpark_id\n" +
                    "WHERE\n" +
                    " admin_user_id = '" + userId + "' " +
                    " AND carpark_info.use_mark >= 0" +
                    "AND res_type = 'PARK'\n" +
                    "AND carpark_info.department_id = '" + departId + "'";
        }

        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @Override
    public boolean isCarparkNameRepeat(String controlMode, String departId, String carparkId, String carparkName) {
        boolean res = false;
        //新建车场
        if (controlMode.equals("add")) {
            String addSql = "SELECT\n" +
                    "\tcarpark_id\n" +
                    "FROM\n" +
                    "\tcarpark_info\n" +
                    "WHERE\n" +
                    "\tcarpark_name = '" + carparkName + "'" +
                    " AND use_mark >= 0";
            SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(addSql);
            if (sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list().size() > 0) {
                res = true;
            }
        } else {
            //修改车场
            String editSql = "SELECT\n" +
                    "\tcarpark_id\n" +
                    "FROM\n" +
                    "\tcarpark_info\n" +
                    "WHERE\n" +
                    "\tcarpark_name = '" + carparkName + "'" +
                    " AND carpark_id <> '" + carparkId + "'" +
                    " AND use_mark >= 0";
            SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(editSql);
            if (sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list().size() > 0) {
                res = true;
            }
        }
        return res;
    }


    //增加车场信息
    @Override
    public void insertCarparkInfo(Map carparkInfo,AdminUser adminUser,String ip) {
        String carparkName = (String) carparkInfo.get("carparkName");
        String controlMode = (String) carparkInfo.get("controlMode");
        String hql = "from DepartmentInfo where useMark >= 0";
        DepartmentInfo departmentInfo = (DepartmentInfo)baseDao.getUnique(hql);
        Integer addTotalCarSpace = Integer.parseInt(String.valueOf(carparkInfo.get("addTotalCarSpace")));
        String addIsTestRunningg = (String) carparkInfo.get("addIsTestRunningg");
        String addPassTimeWhenBig = (String) carparkInfo.get("addPassTimeWhenBig");
        String ifIncludeCalculate = (String)carparkInfo.get("ifIncludeCalculate");

        String addCriticalValue = (String) carparkInfo.get("addCriticalValue");

        BigDecimal addLat = null;
        if (!CommonUtils.isEmpty(carparkInfo.get("addLat"))) {
            addLat = new BigDecimal(carparkInfo.get("addLat").toString());
        }

        Integer addAvailableCarSpace = Integer.parseInt(String.valueOf(carparkInfo.get("addAvailableCarSpace")));
        String addIsClose = (String) carparkInfo.get("addIsClose");


        String addIsOverdueAutoOpen = (String) carparkInfo.get("addIsOverdueAutoOpen");
        String addOwnCarparkName = (String) carparkInfo.get("addOwnCarparkName");
        String isAutoOpen = (String) carparkInfo.get("isAutoOpen");
        String addCloseType = (String) carparkInfo.get("addCloseType");


        Integer addLedMemberCriticalValue = Integer.parseInt(String.valueOf(carparkInfo.get("addLedMemberCriticalValue")));

        BigDecimal addLon = null;
        if (!CommonUtils.isEmpty(carparkInfo.get("addLon"))) {
            addLon = new BigDecimal(carparkInfo.get("addLon").toString());
        }
        CarparkInfo carparkInfo1 = new CarparkInfo();
        carparkInfo1.setCarparkName(carparkName);
        if (controlMode.equals("add")) {
            carparkInfo1.setUseMark(0);
        }

        if(!CommonUtils.isEmpty(addCriticalValue)){
            carparkInfo1.setCriticalValue(Integer.parseInt(addCriticalValue));
        }

        carparkInfo1.setDepartmentInfo(departmentInfo);
        carparkInfo1.setTotalCarSpace(addTotalCarSpace);
        carparkInfo1.setPassTimeWhenBig(addPassTimeWhenBig);
        carparkInfo1.setLat(addLat);

        carparkInfo1.setAvailableCarSpace(addAvailableCarSpace);
        carparkInfo1.setCarparkNo(addAvailableCarSpace);
        carparkInfo1.setOwnCarparkNo(addOwnCarparkName);

        if (addCloseType.equals("yes")) {
            carparkInfo1.setCloseType("0");
        } else {
            carparkInfo1.setCloseType("1");
        }
        if (!CommonUtils.isEmpty(ifIncludeCalculate)) {
            if (ifIncludeCalculate.equals("yes"))
                carparkInfo1.setIfIncludeCaculate(1);
            else
                carparkInfo1.setIfIncludeCaculate(0);
        }else
            carparkInfo1.setIfIncludeCaculate(0);

        if (addIsClose.equals("yes")) {
            carparkInfo1.setIsClose("1");
        } else {
            carparkInfo1.setIsClose("0");
        }

        if (isAutoOpen.equals("yes")) {
            carparkInfo1.setIsAutoOpen("1");
        } else {
            carparkInfo1.setIsAutoOpen("0");
        }
        if (addIsOverdueAutoOpen.equals("yes")) {
            carparkInfo1.setIsOverdueAutoOpen("1");
        } else {
            carparkInfo1.setIsOverdueAutoOpen("0");
        }

        if (addIsTestRunningg.equals("yes")) {
            carparkInfo1.setIsTestRunning("1");
        } else {
            carparkInfo1.setIsTestRunning("0");
        }

        carparkInfo1.setLedMemberCriticalValue(addLedMemberCriticalValue);
        carparkInfo1.setLon(addLon);
        carparkInfo1.setAddTime(CommonUtils.getTimestamp());
        baseDao.save(carparkInfo1);

        String mapSql = "from AdminOrgResource  where resId = ? and adminUserId = ? and resType = ?";
        AdminOrgResource orgResource = (AdminOrgResource)baseDao.getUnique(mapSql,carparkInfo1.getCarparkId(),adminUser.getId(),"PARK");
        if(CommonUtils.isEmpty(orgResource)){
            orgResource = new AdminOrgResource();
            orgResource.setAdminUserId(adminUser.getId());
            orgResource.setResId(carparkInfo1.getCarparkId());
            orgResource.setResType("PARK");
            orgResource.setRecPriority(0);
            baseDao.save(orgResource);
        }

        String carparkId = carparkInfo1.getCarparkId();
        String outCarparkName=carparkName+"外部车收费类型";
        String initCarparkName=carparkName+"内部车收费类型";
        Integer outPackageKind = -3;
        Integer initPackageKind=-3;
        Integer packageChildKind=-3;
        Integer isDelete=1;
        String isStatistic="1";
        Integer outUseType=0;
        Integer initUseType=1;
        MemberKind outMemberKind=new MemberKind();
        MemberKind initMemberKind=new MemberKind();
        CarparkInfo outCarparkInfo=new CarparkInfo();
        CarparkInfo initCarparkInfo=new CarparkInfo();
        outCarparkInfo.setCarparkId(carparkId);
        outMemberKind.setCarparkInfo(outCarparkInfo);
        initCarparkInfo.setCarparkId(carparkId);
        initMemberKind.setCarparkInfo(initCarparkInfo);
        initMemberKind.setKindName(initCarparkName);
        outMemberKind.setKindName(outCarparkName);
        initMemberKind.setKindName(initCarparkName);
        outMemberKind.setPackageKind(outPackageKind);
        initMemberKind.setPackageKind(initPackageKind);
        outMemberKind.setIsDelete(isDelete);
        initMemberKind.setIsDelete(isDelete);
        outMemberKind.setIsStatistic(isStatistic);
        initMemberKind.setIsStatistic(isStatistic);
        initMemberKind.setUseType(initUseType);
        outMemberKind.setUseType(outUseType);
        initMemberKind.setPackageChildKind(packageChildKind);
        outMemberKind.setPackageChildKind(packageChildKind);
        initMemberKind.setUseMark(0);
        outMemberKind.setUseMark(0);
        baseDao.save(initMemberKind);
        baseDao.save(outMemberKind);
        logOperationService.addOperatorLog(LogEnum.operatePark,"新增" + carparkInfo1.getCarparkName() + "的信息",ip,null,adminUser);

        uploadDataToCloudService.uploadParkInfoToServer(carparkInfo1);
        uploadDataToCloudService.uploadMemberKindInfoToServer(initMemberKind,null,null);
        uploadDataToCloudService.uploadMemberKindInfoToServer(outMemberKind,null,null);
    }

    //删除车场信息
    @Override
    public void deleteCarparkInfo(String carparkInfoId,AdminUser adminUser,String ip) {
        //删除车场信息
        CarparkInfo carparkInfo = (CarparkInfo) baseDao.getById(CarparkInfo.class,carparkInfoId);
        String carparkName = carparkInfo.getCarparkName();
        carparkInfo.setUseMark(-1);
        baseDao.update(carparkInfo);
        logOperationService.addOperatorLog(LogEnum.operatePark,"删除" + carparkName + "的信息",ip,null,adminUser);
        uploadDataToCloudService.uploadParkInfoToServer(carparkInfo);

        //删除车道信息
        String hql = "from InOutCarRoadInfo where ownCarparkNo = ? and useMark >= 0";
        List<InOutCarRoadInfo> inOutCarRoadInfoList = (List<InOutCarRoadInfo>)baseDao.queryForList(hql,carparkInfo.getCarparkId());
        for (InOutCarRoadInfo inOutCarRoadInfo : inOutCarRoadInfoList){
            inOutCarRoadInfo.setUseMark(-1);
            baseDao.update(inOutCarRoadInfo);
            uploadDataToCloudService.uploadRoadInfoToServer(inOutCarRoadInfo);
        }

        //删除绑定的车场默认的收费类型信息
        hql = "from MemberKind where carparkInfo = ? and useMark >= 0";
        List<MemberKind> memberKindList = (List<MemberKind>)baseDao.queryForList(hql,carparkInfo);
        for (MemberKind memberKind : memberKindList){
            memberKind.setUseMark(-1);
            baseDao.update(memberKind);
            uploadDataToCloudService.uploadMemberKindInfoToServer(memberKind,null,null);
        }

    }


    @Override
    public List getAllCarParksInfo() {
        List<Map> list = new ArrayList<>();
        String sql = "SELECT * FROM carpark_info WHERE use_mark>=0";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        list = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list;
    }

    @Override
    public List getAllPostInfo() {
        List<Map> list = new ArrayList<>();
        String sql = "SELECT * FROM post_computer_manage WHERE use_mark>=0";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        list = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list;
    }

    //保存车道信息
    @Override
    public void saveCarRoad(Map params,AdminUser adminUser,String ip) {
        String carRoadName = (String) params.get("carRoadName");
        String carRoadParkId = (String) params.get("carRoadParkId");
        String carType = (String) params.get("carRoadType");
        String postId = (String)params.get("carRoadPostId");
        InOutCarRoadInfo inOutCarRoadInfo = new InOutCarRoadInfo();
        inOutCarRoadInfo.setCarRoadName(carRoadName);
        inOutCarRoadInfo.setOwnCarparkNo(carRoadParkId);
        inOutCarRoadInfo.setCarRoadType(carType);
        inOutCarRoadInfo.setManageComputerId(postId);
        //有些字段必须有值
        inOutCarRoadInfo.setAutoPassType("1");
        inOutCarRoadInfo.setUseMark(0);



        //模糊查询插入
        //白名单
        if(!CommonUtils.isEmpty(params.get("white"))){
            List<List<Map<String, Object>>> listWhite=  (List<List<Map<String, Object>>>)  params.get("white");
            String white="";
            if(!CommonUtils.isEmpty(listWhite)){
                for(int j=0;j<listWhite.size();j++){
                    String  ss= String.valueOf(listWhite.get(j));
                    white+=ss;
                }
                white = white.substring(0,white.length() - 1);
                inOutCarRoadInfo.setAutoMatchCarNoPos(white);
            }



            if(!CommonUtils.isEmpty(params.get("minwhite"))){
                String minwhite= (String) params.get("minwhite");
                inOutCarRoadInfo.setAutoMatchLeastBite(Integer.parseInt(minwhite));
            }

            inOutCarRoadInfo.setIsAutoMatchCarNo("1");
            if(!CommonUtils.isEmpty(params.get("temp"))){
                inOutCarRoadInfo.setIsAutoMatchTmpCarNo("1");
            }else {
                inOutCarRoadInfo.setIsAutoMatchTmpCarNo("0");
            }

        }

        List<List<Map<String,Object>>> list = (List<List<Map<String, Object>>>) params.get("result");
        String whiteResult="";
        if(!CommonUtils.isEmpty(list)){
            for(int j=0;j<list.size();j++)
            {
                String  ss= String.valueOf(list.get(j));
                whiteResult+=ss+";";
            }
            whiteResult = whiteResult.substring(0,whiteResult.length() - 1);
            whiteResult = whiteResult.replaceAll(" ,, ","");
            whiteResult= whiteResult.replaceAll("\\[","");
            whiteResult= whiteResult.replaceAll("\\]","");
            inOutCarRoadInfo.setWhiteIntelligentCorrection(whiteResult);
        }

        List<List<Map<String,Object>>>  allCarnoList = (List<List<Map<String, Object>>>) params.get("resultAllCarno");
        String tempResult="";
        if(!CommonUtils.isEmpty(allCarnoList)){
            for(int j=0;j<allCarnoList.size();j++)
            {
                String  ss= String.valueOf(allCarnoList.get(j));
                tempResult+=ss+";";
            }
            tempResult = tempResult.substring(0,tempResult.length() - 1);

            tempResult = tempResult.replaceAll(" ,, ","");
            tempResult= tempResult.replaceAll("\\[","");
            tempResult= tempResult.replaceAll("\\]","");
            inOutCarRoadInfo.setIntelligentCorrection(tempResult);
        }

        baseDao.save(inOutCarRoadInfo);
        uploadDataToCloudService.uploadRoadInfoToServer(inOutCarRoadInfo);
        logOperationService.addOperatorLog(LogEnum.operateRoad,"新增车道：" + inOutCarRoadInfo.getCarRoadName(),ip,null,adminUser);

    }

    //判断同一车场下车道是否已存在
    @Override
    public boolean isisCarRoadNameRepeat(Map params) {
        boolean res = false;
        String carRoadParkId = (String) params.get("carRoadParkId");
        String carRoadName = (String) params.get("carRoadName");
        String sql = "SELECT car_road_id FROM in_out_car_road_info\n" +
                "WHERE car_road_name='" + carRoadName +" ' AND own_carpark_no='" + carRoadParkId +"'";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        if (sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list().size() > 0) {
            res = true;
        }
        return res;

    }

    //获取车道信息
    @Override
    public List getCarRoadInfoList(List<Map<String, Object>> caparksOld, Integer start, Integer length) {
        List<String> carParksId = new ArrayList<>();
        for (Map<String, Object> carParkId : caparksOld) {
            carParksId.add((String)carParkId.get("carparkId"));
        }
        if(CollectionUtils.isEmpty(carParksId)){
            return Collections.EMPTY_LIST;
        }
        String sql="SELECT a.*,b.carpark_name,c.post_computer_name FROM in_out_car_road_info a LEFT JOIN carpark_info b\n" +
                "ON a.own_carpark_no = b.carpark_id LEFT JOIN post_computer_manage c ON a.manage_computer_id = c.post_computer_id\n" +
                "WHERE a.own_carpark_no IN(:carParksId)\n" +
                "AND a.use_mark >= 0 AND b.use_mark >= 0";
        SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        query.setParameterList("carParksId", carParksId);
        query.setFirstResult(start).setMaxResults(length);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List list = query.list();
        return query.list();
    }

    @Override
    public Long countCarRoadInfoList(List<Map<String, Object>> caparksOld) {
        List<String> carParksId = new ArrayList<>();
        for (Map<String, Object> carParkId : caparksOld) {
            carParksId.add((String)carParkId.get("carparkId"));
        }
        if(CollectionUtils.isEmpty(carParksId)){
            return 0L;
        }
        String sql="SELECT COUNT(1) FROM in_out_car_road_info a,carpark_info b\n" +
                "WHERE a.own_carpark_no=b.carpark_id\n" +
                "AND  a.own_carpark_no IN(:carParksId) AND a.use_mark >=0 AND b.use_mark>=0";
        SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        query.setParameterList("carParksId", carParksId);
        return ((BigInteger) query.uniqueResult()).longValue();
    }

    //删除车道信息
    @Override
    public void deleteCarRoadInfo(String id,AdminUser adminUser,String ip) {
        InOutCarRoadInfo inOutCarRoadInfo = (InOutCarRoadInfo)baseDao.getById(InOutCarRoadInfo.class,id);
        String carRoadName = inOutCarRoadInfo.getCarRoadName();
        inOutCarRoadInfo.setUseMark(-1);
        baseDao.update(inOutCarRoadInfo);
        logOperationService.addOperatorLog(LogEnum.operateRoad,"删除车道：" + carRoadName,ip,null,adminUser);
        uploadDataToCloudService.uploadRoadInfoToServer(inOutCarRoadInfo);
    }

    @Override
    public Map detailsCarRoadInfo(Map params) {
        String carRoadId=(String)params.get("carRoadId");
        String carParkId=(String)params.get("carParkId");

        String sql="SELECT a.*,b.carpark_name FROM in_out_car_road_info a,carpark_info b\n" +
                "WHERE a.own_carpark_no=b.carpark_id AND a.car_road_id='" + carRoadId +" ' AND b.carpark_id='" + carParkId +" '"
                + " AND a.use_mark >=0 AND b.use_mark>=0";

        SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List list = query.list();
        Map obj=new HashMap();
        obj=(Map)list.get(0);
        return obj;
    }

    //更新车道信息
    @Override
    public void updateCarRoad(InOutCarRoadInfo inOutCarRoadInfo,Map params,AdminUser adminUser,String ip) {
        String carRoadId=(String) params.get("carRoadId");
        String carRoadName=(String) params.get("carRoadName");
        String carRoadParkId=(String) params.get("carRoadParkId");
        String carRoadType=(String) params.get("carRoadType");
        String carRoadPostId = (String) params.get("carRoadPostId");
        //修改信息
        inOutCarRoadInfo.setCarRoadId(carRoadId);
        inOutCarRoadInfo.setCarRoadName(carRoadName);
        inOutCarRoadInfo.setOwnCarparkNo(carRoadParkId);
        inOutCarRoadInfo.setCarRoadType(carRoadType);
        inOutCarRoadInfo.setManageComputerId(carRoadPostId);


        boolean isAutoMatchTemp= (boolean) params.get("isAutoMatchTemp");
        boolean isAutoMatch= (boolean) params.get("isAutoMatch");
        if(isAutoMatch){
            inOutCarRoadInfo.setIsAutoMatchCarNo("1");
        }else {
            inOutCarRoadInfo.setIsAutoMatchCarNo("0");
        }
        if(isAutoMatchTemp){
            inOutCarRoadInfo.setIsAutoMatchTmpCarNo("1");
        }else {
            inOutCarRoadInfo.setIsAutoMatchTmpCarNo("0");
        }


        //模糊查询插入
        //白名单
        //模糊查询插入
        //白名单
        if(!CommonUtils.isEmpty(params.get("white"))){
            List<List<Map<String, Object>>> listWhite=  (List<List<Map<String, Object>>>)  params.get("white");
            String white="";
            if(!CommonUtils.isEmpty(listWhite)){
                for(int j=0;j<listWhite.size();j++){
                    String  ss= String.valueOf(listWhite.get(j));
                    white+=ss;
                }
                white = white.substring(0,white.length() - 1);
                inOutCarRoadInfo.setAutoMatchCarNoPos(white);
            }

            if(!CommonUtils.isEmpty(params.get("minwhite"))){
                String minwhite= (String) params.get("minwhite");
                inOutCarRoadInfo.setAutoMatchLeastBite(Integer.parseInt(minwhite));
            }
        }

        List<List<Map<String,Object>>> list = (List<List<Map<String, Object>>>) params.get("result");
        String whiteResult="";
        if(!CommonUtils.isEmpty(list)){
            for(int j=0;j<list.size();j++)
            {
                String  ss= String.valueOf(list.get(j));
                whiteResult+=ss+";";
            }
            whiteResult = whiteResult.substring(0,whiteResult.length() - 1);
            whiteResult = whiteResult.replaceAll(" ,, ","");
            whiteResult= whiteResult.replaceAll("\\[","");
            whiteResult= whiteResult.replaceAll("\\]","");
            inOutCarRoadInfo.setWhiteIntelligentCorrection(whiteResult);
        }

        List<List<Map<String,Object>>>  carnoList = (List<List<Map<String, Object>>>) params.get("resultAllCarno");
        String tempResult="";
        if(!CommonUtils.isEmpty(carnoList)){
            for(int j=0;j<carnoList.size();j++)
            {
                String  ss= String.valueOf(carnoList.get(j));
                tempResult+=ss+";";
            }
            tempResult = tempResult.substring(0,tempResult.length() - 1);

            tempResult = tempResult.replaceAll(" ,, ","");
            tempResult= tempResult.replaceAll("\\[","");
            tempResult= tempResult.replaceAll("\\]","");
            inOutCarRoadInfo.setIntelligentCorrection(tempResult);
        }

        inOutCarRoadInfo.setUseMark(1);
        baseDao.save(inOutCarRoadInfo);
        uploadDataToCloudService.uploadRoadInfoToServer(inOutCarRoadInfo);
        JSONObject paramsIntellMatch = new JSONObject();
        if(CommonUtils.isEmpty(params.get("result"))){
            paramsIntellMatch.put("enabledIntellMatch",0);
            paramsIntellMatch.put("count",0);

        }else{
            List<List<String>> matchArray = (List<List<String>>)params.get("result");
            for(int i = 0; i < matchArray.size(); i++){
                List<String> sd = matchArray.get(i);

                String src = sd.get(0);
                String dst = sd.get(2);
                paramsIntellMatch.put("src" + (i + 1),src);
                paramsIntellMatch.put("dst" + (i + 1),dst);
                paramsIntellMatch.put("count",(i + 1));
            }
            paramsIntellMatch.put("enabledIntellMatch",1);

        }


        JSONObject paramsWhite = new JSONObject();
        if(isAutoMatch){
            paramsWhite.put("enabledFuzzyMatch",1);
        }else{
            paramsWhite.put("enabledFuzzyMatch",0);
        }
        if(!CommonUtils.isEmpty(params.get("white"))){
            List<String> whiteArray = (List<String>)params.get("white");
            for(int i = 1; i <= 7; i ++){
                boolean isExist = false;
                for(String w : whiteArray){
                    if(w.replaceAll(";","").equals(i + "")){
                        isExist = true;
                    }
                }
                String key;
                switch (i){
                    case 1:
                        key = "word0";
                        break;
                    case 2:
                        key = "wordA";
                        break;

                    default:
                        key = "word" + (i - 2);
                        break;
                }
                if(isExist){
                    paramsWhite.put(key,0);
                }else{
                    paramsWhite.put(key,1);
                }
            }
        }
        if(!CommonUtils.isEmpty(params.get("minwhite"))){
            String minwhite = (String) params.get("minwhite");
            paramsWhite.put("minMatchNum",minwhite);
        }else{
            paramsWhite.put("minMatchNum",0);
        }
        DeviceManageUtils deviceManageUtils = new StarnetDeviceUtils();

        String hql = "from DeviceInfo where ownCarRoad = ? and useMark >= 0";
        List<DeviceInfo> deviceInfos = (List<DeviceInfo>)baseDao.queryForList(hql,inOutCarRoadInfo.getCarRoadId());
        for(DeviceInfo deviceInfo : deviceInfos){
            JSONObject whiteSet = deviceManageUtils.getWhiteSet(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd());

            whiteSet.putAll(paramsWhite);
            deviceManageUtils.updateWhiteSet(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd(),whiteSet);
            deviceManageUtils.updateSetIntellMatch(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd(),paramsIntellMatch);
        }
        logOperationService.addOperatorLog(LogEnum.operateRoad,"修改车道：" + inOutCarRoadInfo.getCarRoadName(),ip,null,adminUser);
    }

    @Override
    public InOutCarRoadInfo getRemainCarRoad(String carRoadId) {
        //实体直接查询
        String sql = "from InOutCarRoadInfo  where carRoadId ='" + carRoadId + "'";
        InOutCarRoadInfo inOutCarRoadInfo = (InOutCarRoadInfo) baseDao.getUnique(sql);
        return inOutCarRoadInfo;
    }

    @Override
    public Integer get7DayInCount(String parId) {
        String sql = "SELECT\n" +
                "\tcount(1)\n" +
                "FROM\n" +
                "\tinout_record_info\n" +
                "WHERE\n" +
                "\tcarpark_id = '" + parId + "'\n" +
                "AND inout_flag = 0\n" +
                "AND inout_status < 2 " +
                "AND add_time >= '" + CommonUtils.getTodayStartTimeStamp().toString() + "'" +
                " AND add_time <= '" + CommonUtils.getTodayEndTimeStamp().toString() + "'";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        BigInteger bigInteger = (BigInteger)sqlQuery.uniqueResult();
        return bigInteger.intValue();
    }

    @Override
    public String getCparkIdByName(String parkName) {
        String  hql = "from CarparkInfo where carparkName  =? and useMark >= 0";
        CarparkInfo carparkInfo = (CarparkInfo)baseDao.getUnique(hql,parkName);
        String  carparkId=null;
        if(!CommonUtils.isEmpty(carparkInfo)){
            carparkId=carparkInfo.getCarparkId();
        }
        return carparkId;

    }

    //开启和关闭车队模式
    @Override
    public boolean controlFleetMode(String controlMode, String roadId) {
        boolean res = true;
        DeviceManageUtils deviceManageUtils = new StarnetDeviceUtils();
        //根据车道ID获取IPC信息
        String hql = "select device_id,sub_ipc_id from device_info where device_type = '0' and own_car_road = '" + roadId + "' and use_mark >= 0";
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
        List<Map<String,String>> list = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        String deviceId = "";
        if (list.size() > 1) {
            for (Map map : list) {
                if (StringUtils.isBlank(map.get("sub_ipc_id").toString())){
                    deviceId = map.get("device_id").toString();
                    break;
                }
            }
        }else {
            if (!CommonUtils.isEmpty(list)) {
                deviceId = list.get(0).get("device_id").toString();
            }

        }
        DeviceInfo deviceInfo = new DeviceInfo();
        if (!StringUtils.isBlank(deviceId))
            deviceInfo = (DeviceInfo) baseDao.getById(DeviceInfo.class, deviceId);
        else {
            res = false;
            return res;
        }

        JSONObject params = new JSONObject();
        if (controlMode.equals("open")){
            params.put("enabled",0);
            params.put("openMode",1);
            params.put("enabledCmd",1);
            params.put("enabledTriger",0);
            params.put("openDelay",0);
            params.put("closeDelay",0);
            deviceManageUtils.openRoadGate(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),
                    deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd());
            deviceManageUtils.UpdateGateControlInfo(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),
                    deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd(),params);
        }else {
            params.put("enabled",1);
            params.put("openMode",1);
            params.put("enabledCmd",1);
            params.put("enabledTriger",0);
            params.put("openDelay",0);
            params.put("closeDelay",0);
            deviceManageUtils.closeRoadGate(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),
                    deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd());
            deviceManageUtils.UpdateGateControlInfo(deviceInfo.getDeviceIp(),Integer.parseInt(deviceInfo.getDevicePort()),
                    deviceInfo.getDeviceUsername(),deviceInfo.getDevicePwd(),params);
        }
        return res;
    }
}
