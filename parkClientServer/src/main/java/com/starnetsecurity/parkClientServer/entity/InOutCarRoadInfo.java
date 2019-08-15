package com.starnetsecurity.parkClientServer.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2017-11-15.
 */
@Entity
@Table(name = "in_out_car_road_info", schema = "vams", catalog = "")
public class InOutCarRoadInfo {
    private String carRoadId;
    private String carRoadName;
    private String carRoadType;
    private String ownCarparkNo;
    private String operationSource;
    private Timestamp addTime;
    private String manageComputerId;
    private String autoPassType;
    private Integer carRoadNo;
    private String multiplexRoadId;
    private String isSupportTwoWay;
    private String isAutoMatchCarNo;
    private String autoMatchCarNoPos;
    private Integer autoMatchLeastBite;
    private String isAutoMatchTmpCarNo;
    private String matchTmpCarNoPos;
    private Integer matchTmpLeastBite;
    private String intelligentCorrection;
    private String whiteIntelligentCorrection;
    private Integer useMark;

    @Id
    @GenericGenerator(name="systemUUID",strategy="uuid")
    @GeneratedValue(generator="systemUUID")
    @Column(name = "car_road_id")
    public String getCarRoadId() {
        return carRoadId;
    }

    public void setCarRoadId(String carRoadId) {
        this.carRoadId = carRoadId;
    }

    @Basic
    @Column(name = "car_road_name")
    public String getCarRoadName() {
        return carRoadName;
    }

    public void setCarRoadName(String carRoadName) {
        this.carRoadName = carRoadName;
    }

    @Basic
    @Column(name = "car_road_type")
    public String getCarRoadType() {
        return carRoadType;
    }

    public void setCarRoadType(String carRoadType) {
        this.carRoadType = carRoadType;
    }

    @Basic
    @Column(name = "own_carpark_no")
    public String getOwnCarparkNo() {
        return ownCarparkNo;
    }

    public void setOwnCarparkNo(String ownCarparkNo) {
        this.ownCarparkNo = ownCarparkNo;
    }

    @Basic
    @Column(name = "operation_source")
    public String getOperationSource() {
        return operationSource;
    }

    public void setOperationSource(String operationSource) {
        this.operationSource = operationSource;
    }

    @Basic
    @Column(name = "add_time")
    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    @Basic
    @Column(name = "manage_computer_id")
    public String getManageComputerId() {
        return manageComputerId;
    }

    public void setManageComputerId(String manageComputerId) {
        this.manageComputerId = manageComputerId;
    }

    @Basic
    @Column(name = "auto_pass_type")
    public String getAutoPassType() {
        return autoPassType;
    }

    public void setAutoPassType(String autoPassType) {
        this.autoPassType = autoPassType;
    }

    @Basic
    @Column(name = "car_road_no")
    public Integer getCarRoadNo() {
        return carRoadNo;
    }

    public void setCarRoadNo(Integer carRoadNo) {
        this.carRoadNo = carRoadNo;
    }

    @Basic
    @Column(name = "multiplex_road_id")
    public String getMultiplexRoadId() {
        return multiplexRoadId;
    }

    public void setMultiplexRoadId(String multiplexRoadId) {
        this.multiplexRoadId = multiplexRoadId;
    }

    @Basic
    @Column(name = "is_support_two_way")
    public String getIsSupportTwoWay() {
        return isSupportTwoWay;
    }

    public void setIsSupportTwoWay(String isSupportTwoWay) {
        this.isSupportTwoWay = isSupportTwoWay;
    }

    @Basic
    @Column(name = "is_auto_match_car_no")
    public String getIsAutoMatchCarNo() {
        return isAutoMatchCarNo;
    }

    public void setIsAutoMatchCarNo(String isAutoMatchCarNo) {
        this.isAutoMatchCarNo = isAutoMatchCarNo;
    }

    @Basic
    @Column(name = "auto_match_car_no_pos")
    public String getAutoMatchCarNoPos() {
        return autoMatchCarNoPos;
    }

    public void setAutoMatchCarNoPos(String autoMatchCarNoPos) {
        this.autoMatchCarNoPos = autoMatchCarNoPos;
    }

    @Basic
    @Column(name = "auto_match_least_bite")
    public Integer getAutoMatchLeastBite() {
        return autoMatchLeastBite;
    }

    public void setAutoMatchLeastBite(Integer autoMatchLeastBite) {
        this.autoMatchLeastBite = autoMatchLeastBite;
    }

    @Basic
    @Column(name = "is_auto_match_tmp_car_no")
    public String getIsAutoMatchTmpCarNo() {
        return isAutoMatchTmpCarNo;
    }

    public void setIsAutoMatchTmpCarNo(String isAutoMatchTmpCarNo) {
        this.isAutoMatchTmpCarNo = isAutoMatchTmpCarNo;
    }

    @Basic
    @Column(name = "match_tmp_car_no_pos")
    public String getMatchTmpCarNoPos() {
        return matchTmpCarNoPos;
    }

    public void setMatchTmpCarNoPos(String matchTmpCarNoPos) {
        this.matchTmpCarNoPos = matchTmpCarNoPos;
    }

    @Basic
    @Column(name = "match_tmp_least_bite")
    public Integer getMatchTmpLeastBite() {
        return matchTmpLeastBite;
    }

    public void setMatchTmpLeastBite(Integer matchTmpLeastBite) {
        this.matchTmpLeastBite = matchTmpLeastBite;
    }

    @Basic
    @Column(name = "intelligent_correction")
    public String getIntelligentCorrection() {
        return intelligentCorrection;
    }

    public void setIntelligentCorrection(String intelligentCorrection) {
        this.intelligentCorrection = intelligentCorrection;
    }

    @Basic
    @Column(name = "white_intelligent_correction")
    public String getWhiteIntelligentCorrection() {
        return whiteIntelligentCorrection;
    }

    public void setWhiteIntelligentCorrection(String whiteIntelligentCorrection) {
        this.whiteIntelligentCorrection = whiteIntelligentCorrection;
    }

    @Basic
    @Column(name = "use_mark")
    public Integer getUseMark() {
        return useMark;
    }

    public void setUseMark(Integer useMark) {
        this.useMark = useMark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InOutCarRoadInfo that = (InOutCarRoadInfo) o;

        if (carRoadId != null ? !carRoadId.equals(that.carRoadId) : that.carRoadId != null) return false;
        if (carRoadName != null ? !carRoadName.equals(that.carRoadName) : that.carRoadName != null) return false;
        if (carRoadType != null ? !carRoadType.equals(that.carRoadType) : that.carRoadType != null) return false;
        if (ownCarparkNo != null ? !ownCarparkNo.equals(that.ownCarparkNo) : that.ownCarparkNo != null) return false;
        if (operationSource != null ? !operationSource.equals(that.operationSource) : that.operationSource != null)
            return false;
        if (addTime != null ? !addTime.equals(that.addTime) : that.addTime != null) return false;
        if (manageComputerId != null ? !manageComputerId.equals(that.manageComputerId) : that.manageComputerId != null)
            return false;
        if (autoPassType != null ? !autoPassType.equals(that.autoPassType) : that.autoPassType != null) return false;
        if (carRoadNo != null ? !carRoadNo.equals(that.carRoadNo) : that.carRoadNo != null) return false;
        if (multiplexRoadId != null ? !multiplexRoadId.equals(that.multiplexRoadId) : that.multiplexRoadId != null)
            return false;
        if (isSupportTwoWay != null ? !isSupportTwoWay.equals(that.isSupportTwoWay) : that.isSupportTwoWay != null)
            return false;
        if (isAutoMatchCarNo != null ? !isAutoMatchCarNo.equals(that.isAutoMatchCarNo) : that.isAutoMatchCarNo != null)
            return false;
        if (autoMatchCarNoPos != null ? !autoMatchCarNoPos.equals(that.autoMatchCarNoPos) : that.autoMatchCarNoPos != null)
            return false;
        if (autoMatchLeastBite != null ? !autoMatchLeastBite.equals(that.autoMatchLeastBite) : that.autoMatchLeastBite != null)
            return false;
        if (isAutoMatchTmpCarNo != null ? !isAutoMatchTmpCarNo.equals(that.isAutoMatchTmpCarNo) : that.isAutoMatchTmpCarNo != null)
            return false;
        if (matchTmpCarNoPos != null ? !matchTmpCarNoPos.equals(that.matchTmpCarNoPos) : that.matchTmpCarNoPos != null)
            return false;
        if (matchTmpLeastBite != null ? !matchTmpLeastBite.equals(that.matchTmpLeastBite) : that.matchTmpLeastBite != null)
            return false;
        if (intelligentCorrection != null ? !intelligentCorrection.equals(that.intelligentCorrection) : that.intelligentCorrection != null)
            return false;
        if (whiteIntelligentCorrection != null ? !whiteIntelligentCorrection.equals(that.whiteIntelligentCorrection) : that.whiteIntelligentCorrection != null)
            return false;
        if (useMark != null ? !useMark.equals(that.useMark) : that.useMark != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = carRoadId != null ? carRoadId.hashCode() : 0;
        result = 31 * result + (carRoadName != null ? carRoadName.hashCode() : 0);
        result = 31 * result + (carRoadType != null ? carRoadType.hashCode() : 0);
        result = 31 * result + (ownCarparkNo != null ? ownCarparkNo.hashCode() : 0);
        result = 31 * result + (operationSource != null ? operationSource.hashCode() : 0);
        result = 31 * result + (addTime != null ? addTime.hashCode() : 0);
        result = 31 * result + (manageComputerId != null ? manageComputerId.hashCode() : 0);
        result = 31 * result + (autoPassType != null ? autoPassType.hashCode() : 0);
        result = 31 * result + (carRoadNo != null ? carRoadNo.hashCode() : 0);
        result = 31 * result + (multiplexRoadId != null ? multiplexRoadId.hashCode() : 0);
        result = 31 * result + (isSupportTwoWay != null ? isSupportTwoWay.hashCode() : 0);
        result = 31 * result + (isAutoMatchCarNo != null ? isAutoMatchCarNo.hashCode() : 0);
        result = 31 * result + (autoMatchCarNoPos != null ? autoMatchCarNoPos.hashCode() : 0);
        result = 31 * result + (autoMatchLeastBite != null ? autoMatchLeastBite.hashCode() : 0);
        result = 31 * result + (isAutoMatchTmpCarNo != null ? isAutoMatchTmpCarNo.hashCode() : 0);
        result = 31 * result + (matchTmpCarNoPos != null ? matchTmpCarNoPos.hashCode() : 0);
        result = 31 * result + (matchTmpLeastBite != null ? matchTmpLeastBite.hashCode() : 0);
        result = 31 * result + (intelligentCorrection != null ? intelligentCorrection.hashCode() : 0);
        result = 31 * result + (whiteIntelligentCorrection != null ? whiteIntelligentCorrection.hashCode() : 0);
        result = 31 * result + (useMark != null ? useMark.hashCode() : 0);
        return result;
    }
}
