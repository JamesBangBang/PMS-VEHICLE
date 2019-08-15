package com.starnetsecurity.parkClientServer.service.impl;

import com.starnetsecurity.common.dao.HibernateBaseDao;
import com.starnetsecurity.common.util.CommonUtils;
import com.starnetsecurity.parkClientServer.entity.*;
import com.starnetsecurity.parkClientServer.service.RealRecordsService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RealRecordsServiceImpl implements RealRecordsService {

    @Autowired
    HibernateBaseDao baseDao;

    @Override
    public List realinout(Integer start, Integer length, String carName, String driverName, String carparkName, String carRoadName, String inoutFlag, String startTime, String endTime,String excelStart,String excelEnd) {
//        String hql = "FROM InoutRecordInfo Where 1=1";
        String hql = "SELECT * FROM inout_record_info Where 1=1";

        if (!CommonUtils.isEmpty(carName)) {
            hql += " And car_no like '" + ("%" + carName + "%") + "' \n";
        }
        if (!CommonUtils.isEmpty(driverName) && !driverName.equals("undefined")) {
            String searchCarno = GetAllMemberNo(driverName);
            if (!CommonUtils.isEmpty(searchCarno))
                hql += " And car_no in (" + searchCarno + ") \n";
        }
        if (!CommonUtils.isEmpty(carparkName)) {
            hql += " And carpark_name like '" + ("%" + carparkName + "%") + "' \n";
        }
        if (!CommonUtils.isEmpty(carRoadName)) {
            hql += " And car_road_name like '" + ("%" + carRoadName + "%") + "' \n";
        }
        if (!CommonUtils.isEmpty(inoutFlag)) {
            Integer flag = Integer.parseInt(inoutFlag);
            hql += " And inout_flag = '" + flag + "' \n";
        }
        if (!CommonUtils.isEmpty(startTime) && !CommonUtils.isEmpty(endTime)) {
            hql += " And inout_time >= '" + startTime + "'And inout_time <= '" + endTime + "' \n";
        }

        if (!CommonUtils.isEmpty(excelStart) && !CommonUtils.isEmpty(excelEnd)) {
            hql += " And inout_time   >= '" + excelStart + "'And inout_time   <= '" + excelEnd + "' \n";
        }
        hql += " ORDER BY inout_time DESC";
//        start = start / length + 1;
//        return (List) baseDao.pageQuery(hql, start, length);
        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
        if (!CommonUtils.isEmpty(start)){
            sqlQuery.setFirstResult(start);
        }
        if(!CommonUtils.isEmpty(length)){
            sqlQuery.setMaxResults(length);
        }
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @Override
    public Long countRealinout(String carName, String driverName, String carparkName, String carRoadName, String inoutFlag, String startTime, String endTime,String excelStart,String excelEnd) {
        String hql = "select count(1) from inout_record_info Where 1=1";
        if (!CommonUtils.isEmpty(carName)) {
            hql += " And car_no like '" + ("%" + carName + "%") + "' \n";
        }
        if (!CommonUtils.isEmpty(driverName)) {
            String searchCarno = GetAllMemberNo(driverName);
            if (!CommonUtils.isEmpty(searchCarno))
                hql += " And car_no in (" + searchCarno + ") \n";
        }
        if (!CommonUtils.isEmpty(carparkName)) {
            hql += " And carpark_name like '" + ("%" + carparkName + "%") + "' \n";
        }
        if (!CommonUtils.isEmpty(carRoadName)) {
            hql += " And car_road_name like '" + ("%" + carRoadName + "%") + "' \n";
        }
        if (!CommonUtils.isEmpty(inoutFlag)) {
            Integer flag = Integer.parseInt(inoutFlag);
            hql += " And inout_flag = '" + flag + "' \n";
        }
        if (!CommonUtils.isEmpty(startTime) && !CommonUtils.isEmpty(endTime)) {
            hql += " And inout_time >= '" + startTime + "'And inout_time <= '" + endTime + "' \n";
        }
        if (!CommonUtils.isEmpty(excelStart) && !CommonUtils.isEmpty(excelEnd)) {
            hql += " And inout_time   >= '" + excelStart + "'And inout_time   <= '" + excelEnd + "' \n";
        }
        SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
        return ((BigInteger) query.uniqueResult()).longValue();
//        return (Long) baseDao.getUnique(hql);
    }

    @Override
    public List realTimePaymentList(Integer start, Integer length, String carNo, String carNoAttribute, String carparkName, String inCarRoadName, String outCarRoadName, String chargePostName, String inStartTime, String inEndTime, String outStartTime, String outEndTime,String excelStart,String excelEnd) {
//        String hql = "FROM OrderInoutRecord Where 1=1";

        String hql = "SELECT * FROM  order_inout_record  Where 1=1 and out_record_id is not null and out_record_id != '' \n ";
        if (!CommonUtils.isEmpty(carNo)) {
            hql +=" And car_no like '" + ("%" + carNo + "%") + "'\n";
        }
        if (!CommonUtils.isEmpty(carNoAttribute)) {
            hql +=" And car_type ='" + carNoAttribute + "' \n";
        }
        if (!CommonUtils.isEmpty(carparkName)) {
            hql +=" And carpark_name like '%"+carparkName+"%' \n";
        }
        if (!CommonUtils.isEmpty(inCarRoadName)) {
            hql += " And in_car_road_id ='" + inCarRoadName + "' \n";
        }
        if (!CommonUtils.isEmpty(outCarRoadName)) {
            hql += " And out_car_road_id ='" + outCarRoadName + "' \n";
        }
        if (!CommonUtils.isEmpty(chargePostName)) {
            hql += " And charge_post_id ='" + chargePostName + "' \n";
        }
        if (!CommonUtils.isEmpty(inStartTime) && !CommonUtils.isEmpty(inEndTime)) {
            hql += " And in_time >='" + inStartTime + "'And in_time <= '" + inEndTime + "' \n";
            hql += " ORDER BY in_time DESC";
        }
        if (!CommonUtils.isEmpty(outStartTime) && !CommonUtils.isEmpty(outEndTime)) {
            hql += " And out_time >='" + outStartTime + "'And out_time <= '" + outEndTime + "' \n";
            hql += " ORDER BY out_time DESC";
        }
        if (!CommonUtils.isEmpty(excelStart) && !CommonUtils.isEmpty(excelEnd)) {
            hql += " And in_time >='" + excelStart + "'And in_time <= '" + excelEnd + "' \n";
            hql += " And out_time >='" + excelStart + "'And out_time <= '" + excelEnd + "' \n";
            hql += " ORDER BY out_time DESC";
        }


        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
        if (!CommonUtils.isEmpty(start)){
            sqlQuery.setFirstResult(start);
        }
        if(!CommonUtils.isEmpty(length)){
            sqlQuery.setMaxResults(length);
        }
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @Override
    public Long countRealTimePaymentList(String carNo, String carNoAttribute, String carparkName, String inCarRoadName, String outCarRoadName, String chargePostName, String inStartTime, String inEndTime, String outStartTime, String outEndTime,String excelStart,String excelEnd) {
        String hql = "select count(1) FROM order_inout_record  Where 1=1 and out_record_id is not null and out_record_id != ''";

        if (!CommonUtils.isEmpty(carNo)) {
            hql +=" And car_no like '" + ("%" + carNo + "%") + "'\n";
        }
        if (!CommonUtils.isEmpty(carNoAttribute)) {
            hql +=" And car_type ='" + carNoAttribute + "' \n";
        }
        if (!CommonUtils.isEmpty(carparkName)) {
            hql +=" And carpark_name like '%"+carparkName+"%' \n";
        }
        if (!CommonUtils.isEmpty(inCarRoadName)) {
            hql += " And in_car_road_id ='" + inCarRoadName + "' \n";
        }
        if (!CommonUtils.isEmpty(outCarRoadName)) {
            hql += " And out_car_road_id ='" + outCarRoadName + "' \n";
        }
        if (!CommonUtils.isEmpty(chargePostName)) {
            hql += " And charge_post_id ='" + chargePostName + "' \n";
        }
        if (!CommonUtils.isEmpty(inStartTime) && !CommonUtils.isEmpty(inEndTime)) {
            hql += " And in_time >='" + inStartTime + "'And in_time <= '" + inEndTime + "' \n";
            hql += " ORDER BY in_time DESC";
        }
        if (!CommonUtils.isEmpty(outStartTime) && !CommonUtils.isEmpty(outEndTime)) {
            hql += " And out_time >='" + outStartTime + "'And out_time <= '" + outEndTime + "' \n";
            hql += " ORDER BY out_time DESC";
        }
        if (!CommonUtils.isEmpty(excelStart) && !CommonUtils.isEmpty(excelEnd)) {
            hql += " And in_time >='" + excelStart + "'And in_time <= '" + excelEnd + "' \n";
            hql += " And out_time >='" + excelStart + "'And out_time <= '" + excelEnd + "' \n";
        }

        SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
        return ((BigInteger) query.uniqueResult()).longValue();
    }

    @Override
    public List realInoutList(String carName, String carType, String carparkName, String carRoadName, String inoutFlag, String startTime, String endTime,String excelStart,String excelEnd) {
        String hql = "SELECT * FROM  inout_record_info a Where 1=1 \n ";
        if (!CommonUtils.isEmpty(carName)) {
            hql += " And car_no like '" + ("%" + carName + "%") + "' \n";
        }
        if (!CommonUtils.isEmpty(carType)) {
            hql += " And car_type ='" + carType + "' \n";
        }
        if (!CommonUtils.isEmpty(carparkName)) {
            hql += " And carpark_name = '" + carparkName + "' \n";
        }
        if (!CommonUtils.isEmpty(carRoadName)) {
            hql += " And car_road_name = '" + carRoadName + "' \n";
        }
        if (!CommonUtils.isEmpty(inoutFlag)) {
            Integer flag = Integer.parseInt(inoutFlag);
            hql += " And inout_flag = '" + flag + "' \n";
        }
        if (!CommonUtils.isEmpty(startTime) && !CommonUtils.isEmpty(endTime)) {
            hql += " And inout_time >= '" + startTime + "'And inout_time <= '" + endTime + "' \n";
        }

        if (!CommonUtils.isEmpty(excelStart) && !CommonUtils.isEmpty(excelEnd)) {
            hql += " And inout_time   >= '" + excelStart + "'And inout_time   <= '" + excelEnd + "' \n";
        }

        SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public List payList(String carNo, String carNoAttribute, String carparkName, String inCarRoadName, String outCarRoadName, String chargePostName, String inStartTime, String inEndTime, String outStartTime, String outEndTime ,String excelStart,String excelEnd) {
        String hql = "SELECT * FROM  order_inout_record  Where 1=1 and out_record_id is not null and out_record_id != '' \n ";
        if (!CommonUtils.isEmpty(carNo)) {
            hql +=" And car_no like '" + ("%" + carNo + "%") + "'\n";
        }
        if (!CommonUtils.isEmpty(carNoAttribute)) {
            hql +=" And car_type ='" + carNoAttribute + "' \n";
        }
        if (!CommonUtils.isEmpty(carparkName)) {
            hql +=" And carpark_name like '%"+carparkName+"%' \n";
        }
        if (!CommonUtils.isEmpty(inCarRoadName)) {
            hql += " And in_car_road_id ='" + inCarRoadName + "' \n";
        }
        if (!CommonUtils.isEmpty(outCarRoadName)) {
            hql += " And out_car_road_id ='" + outCarRoadName + "' \n";
        }
        if (!CommonUtils.isEmpty(chargePostName)) {
            hql += " And charge_post_id ='" + chargePostName + "' \n";
        }
        if (!CommonUtils.isEmpty(inStartTime) && !CommonUtils.isEmpty(inEndTime)) {
            hql += " And in_time >='" + inStartTime + "'And in_time <= '" + inEndTime + "' \n";
            hql += " ORDER BY in_time DESC";
        }
        if (!CommonUtils.isEmpty(outStartTime) && !CommonUtils.isEmpty(outEndTime)) {
            hql += " And out_time >='" + outStartTime + "'And out_time <= '" + outEndTime + "' \n";
            hql += " ORDER BY out_time DESC";
        }
        if (!CommonUtils.isEmpty(excelStart) && !CommonUtils.isEmpty(excelEnd)) {
            hql += " And in_time >='" + excelStart + "'And in_time <= '" + excelEnd + "' \n";
            hql += " And out_time >='" + excelStart + "'And out_time <= '" + excelEnd + "' \n";
            hql += " ORDER BY out_time DESC";
        }
        SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public String getRecordImage(String id) {
        InoutRecordInfo inoutRecordInfo = (InoutRecordInfo)baseDao.getById(InoutRecordInfo.class,id);
        return inoutRecordInfo.getPhotoCapturePicName();
    }

    @Override
    public List getCarParkRoadSelect(String parkId) {

        String hql = "from InOutCarRoadInfo where useMark >= 0";
        List<InOutCarRoadInfo> inOutCarRoadInfos = (List<InOutCarRoadInfo>)baseDao.queryForList(hql);
        List<Map<String,Object>> list = new ArrayList<>();
        for(InOutCarRoadInfo inOutCarRoadInfo : inOutCarRoadInfos){
            Map<String,Object> map = new HashedMap();
            map.put("id",inOutCarRoadInfo.getCarRoadId());
            map.put("name",inOutCarRoadInfo.getCarRoadName());
            map.put("type",inOutCarRoadInfo.getCarRoadType());
            list.add(map);
        }
        return list;
    }

    @Override
    public List getCarParkPostSelect() {

        String hql = "from PostComputerManage where useMark >= 0";
        List<PostComputerManage> postComputerManages = (List<PostComputerManage>)baseDao.queryForList(hql);
        List<Map<String,Object>> list = new ArrayList<>();
        for(PostComputerManage postComputerManage : postComputerManages){
            Map<String,Object> map = new HashedMap();
            map.put("id",postComputerManage.getPostComputerId());
            map.put("name",postComputerManage.getPostComputerName());
            list.add(map);
        }
        return list;
    }

    @Override
    public List carInParkList(Integer start, Integer length, String carNo, String carNoAttribute, String carparkName, String inCarRoadName,String parkOverTime,
                              String inStartTime, String inEndTime) {
        String hql = "SELECT car_no,carpark_name,in_time,in_car_road_name,(TIMESTAMPDIFF(SECOND,in_time,NOW())) stayTime,car_type,in_picture_name "
                   + " FROM  order_inout_record  Where out_record_id is null ";
        if (!CommonUtils.isEmpty(carNo)) {
            hql +=" And car_no like '" + ("%" + carNo + "%") + "'\n";
        }
        if (!CommonUtils.isEmpty(carNoAttribute)) {
            hql +=" And car_type ='" + carNoAttribute + "' \n";
        }
        if (!CommonUtils.isEmpty(carparkName)) {
            hql +=" And carpark_name like '%"+carparkName+"%' \n";
        }
        if (!CommonUtils.isEmpty(inCarRoadName)) {
            hql += " And in_car_road_id ='" + inCarRoadName + "' \n";
        }
        if (!CommonUtils.isEmpty(parkOverTime)){
            hql += " And TIMESTAMPDIFF(SECOND,in_time,NOW()) >= '" + Integer.valueOf(parkOverTime) * 60 * 60 + "' ";
        }
        if (!CommonUtils.isEmpty(inStartTime) && !CommonUtils.isEmpty(inEndTime)) {
            hql += " And in_time >='" + inStartTime + "'And in_time <= '" + inEndTime + "' \n";
        }
        hql += " ORDER BY in_time ASC";

        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
        if (!CommonUtils.isEmpty(start)){
            sqlQuery.setFirstResult(start);
        }
        if(!CommonUtils.isEmpty(length)){
            sqlQuery.setMaxResults(length);
        }
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @Override
    public Long carInParkCount(String carNo, String carNoAttribute, String carparkName, String inCarRoadName, String parkOverTime,
                               String inStartTime, String inEndTime) {
        String hql = "select count(1) FROM order_inout_record  Where out_record_id is null";

        if (!CommonUtils.isEmpty(carNo)) {
            hql +=" And car_no like '" + ("%" + carNo + "%") + "'\n";
        }
        if (!CommonUtils.isEmpty(carNoAttribute)) {
            hql +=" And car_type ='" + carNoAttribute + "' \n";
        }
        if (!CommonUtils.isEmpty(carparkName)) {
            hql +=" And carpark_name like '%"+carparkName+"%' \n";
        }
        if (!CommonUtils.isEmpty(inCarRoadName)) {
            hql += " And in_car_road_id ='" + inCarRoadName + "' \n";
        }
        if (!CommonUtils.isEmpty(parkOverTime)){
            hql += " And TIMESTAMPDIFF(SECOND,in_time,NOW()) >= '" + Integer.valueOf(parkOverTime) * 60 * 60 + "' ";
        }
        if (!CommonUtils.isEmpty(inStartTime) && !CommonUtils.isEmpty(inEndTime)) {
            hql += " And in_time >='" + inStartTime + "'And in_time <= '" + inEndTime + "' \n";
        }


        SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
        return ((BigInteger) query.uniqueResult()).longValue();
    }

    @Override
    public void deleteCarInPark(String carNo, String carparkName) {
        String hql = "from OrderInoutRecord where carNo = ? and carparkName = ? and "
                + " outRecordId IS NULL";
        OrderInoutRecord orderInoutRecord = (OrderInoutRecord)baseDao.getUnique(hql,carNo,carparkName);
        if (!CommonUtils.isEmpty(orderInoutRecord)){
            String carparkId = orderInoutRecord.getCarparkId();
            String inRecordId = orderInoutRecord.getInRecordId();
            InoutRecordInfo inoutRecordInfo = (InoutRecordInfo)baseDao.getById(InoutRecordInfo.class,inRecordId);
            if (!CommonUtils.isEmpty(inoutRecordInfo)){
                baseDao.deleteById(InoutRecordInfo.class,inRecordId);
            }
            baseDao.deleteById(OrderInoutRecord.class,orderInoutRecord.getChargeInfoId());

            CarparkInfo carparkInfo = (CarparkInfo) baseDao.getById(CarparkInfo.class,carparkId);
            if (carparkInfo.getAvailableCarSpace() < carparkInfo.getTotalCarSpace()){
                carparkInfo.setAvailableCarSpace(carparkInfo.getAvailableCarSpace()+1);
                carparkInfo.setCarparkNo(carparkInfo.getCarparkNo()+1);
                baseDao.save(carparkInfo);
            }
        }
    }

    @Override
    public List abnormalPassList(Integer start, Integer length, String carNo, String carNoAttribute, String carparkName, String outCarRoadName, String outStartTime, String outEndTime) {
        String hql = "SELECT * FROM  order_inout_record  Where in_record_id IS NULL AND out_record_id IS NOT NULL ";
        if (!CommonUtils.isEmpty(carNo)) {
            hql +=" And car_no like '" + ("%" + carNo + "%") + "'\n";
        }
        if (!CommonUtils.isEmpty(carNoAttribute)) {
            hql +=" And car_type ='" + carNoAttribute + "' \n";
        }
        if (!CommonUtils.isEmpty(carparkName)) {
            hql +=" And carpark_name like '%"+carparkName+"%' \n";
        }
        if (!CommonUtils.isEmpty(outCarRoadName)) {
            hql += " And out_car_road_id ='" + outCarRoadName + "' \n";
        }

        if (!CommonUtils.isEmpty(outStartTime) && !CommonUtils.isEmpty(outEndTime)) {
            hql += " And out_time >='" + outStartTime + "'And out_time <= '" + outEndTime + "' \n";
        }
        hql += " ORDER BY out_time DESC ";

        SQLQuery sqlQuery = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);
        if (!CommonUtils.isEmpty(start)){
            sqlQuery.setFirstResult(start);
        }
        if(!CommonUtils.isEmpty(length)){
            sqlQuery.setMaxResults(length);
        }
        return sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @Override
    public Long abnormalPassCount(String carNo, String carNoAttribute, String carparkName, String outCarRoadName, String outStartTime, String outEndTime) {
        String hql = "SELECT COUNT(1) FROM  order_inout_record  Where in_record_id IS NULL AND out_record_id IS NOT NULL ";
        if (!CommonUtils.isEmpty(carNo)) {
            hql +=" And car_no like '" + ("%" + carNo + "%") + "'\n";
        }
        if (!CommonUtils.isEmpty(carNoAttribute)) {
            hql +=" And car_type ='" + carNoAttribute + "' \n";
        }
        if (!CommonUtils.isEmpty(carparkName)) {
            hql +=" And carpark_name like '%"+carparkName+"%' \n";
        }
        if (!CommonUtils.isEmpty(outCarRoadName)) {
            hql += " And out_car_road_id ='" + outCarRoadName + "' \n";
        }

        if (!CommonUtils.isEmpty(outStartTime) && !CommonUtils.isEmpty(outEndTime)) {
            hql += " And out_time >='" + outStartTime + "'And out_time <= '" + outEndTime + "' \n";
        }
        hql += " ORDER BY out_time DESC ";

        SQLQuery query = baseDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql);

        return ((BigInteger) query.uniqueResult()).longValue();
    }

    private String GetAllMemberNo(String driverName){
        String res = "";
        String hql = "from MemberWallet where driverName = ? and useMark >= 0";
        StringBuffer resBuf = new StringBuffer();
        List<MemberWallet> memberWallets = (List<MemberWallet>)baseDao.queryForList(hql,driverName);
        if (memberWallets.size() > 0){
            for (MemberWallet memberWallet : memberWallets){
                String[] resArray = memberWallet.getMenberNo().split(",");
                for (int i = 0;i<resArray.length;i++){
                    if (!StringUtils.isBlank(resArray[i]))
                        resBuf.append("'" + resArray[i] + "',");
                }
            }
        }
        res = resBuf.toString();
        if (!CommonUtils.isEmpty(res)) {
            String resLastStr = res.substring(res.length() - 1, res.length());
            if (resLastStr.equals(","))
                res = res.substring(0, res.length() - 1);
        }
        return res;
    }


}
