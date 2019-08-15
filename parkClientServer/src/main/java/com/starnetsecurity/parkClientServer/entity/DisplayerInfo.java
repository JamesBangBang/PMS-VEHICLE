package com.starnetsecurity.parkClientServer.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2017-11-15.
 */
@Entity
@Table(name = "displayer_info", schema = "vams", catalog = "")
public class DisplayerInfo {
    private String infoId;
    private String firstLineInfo;
    private String secondLineInfo;
    private String thirdLineInfo;
    private String fourthLineInfo;
    private String operationSource;
    private Timestamp addTime;

    @Id
    @Column(name = "info_id")
    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    @Basic
    @Column(name = "first_line_info")
    public String getFirstLineInfo() {
        return firstLineInfo;
    }

    public void setFirstLineInfo(String firstLineInfo) {
        this.firstLineInfo = firstLineInfo;
    }

    @Basic
    @Column(name = "second_line_info")
    public String getSecondLineInfo() {
        return secondLineInfo;
    }

    public void setSecondLineInfo(String secondLineInfo) {
        this.secondLineInfo = secondLineInfo;
    }

    @Basic
    @Column(name = "third_line_info")
    public String getThirdLineInfo() {
        return thirdLineInfo;
    }

    public void setThirdLineInfo(String thirdLineInfo) {
        this.thirdLineInfo = thirdLineInfo;
    }

    @Basic
    @Column(name = "fourth_line_info")
    public String getFourthLineInfo() {
        return fourthLineInfo;
    }

    public void setFourthLineInfo(String fourthLineInfo) {
        this.fourthLineInfo = fourthLineInfo;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DisplayerInfo that = (DisplayerInfo) o;

        if (infoId != null ? !infoId.equals(that.infoId) : that.infoId != null) return false;
        if (firstLineInfo != null ? !firstLineInfo.equals(that.firstLineInfo) : that.firstLineInfo != null)
            return false;
        if (secondLineInfo != null ? !secondLineInfo.equals(that.secondLineInfo) : that.secondLineInfo != null)
            return false;
        if (thirdLineInfo != null ? !thirdLineInfo.equals(that.thirdLineInfo) : that.thirdLineInfo != null)
            return false;
        if (fourthLineInfo != null ? !fourthLineInfo.equals(that.fourthLineInfo) : that.fourthLineInfo != null)
            return false;
        if (operationSource != null ? !operationSource.equals(that.operationSource) : that.operationSource != null)
            return false;
        if (addTime != null ? !addTime.equals(that.addTime) : that.addTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = infoId != null ? infoId.hashCode() : 0;
        result = 31 * result + (firstLineInfo != null ? firstLineInfo.hashCode() : 0);
        result = 31 * result + (secondLineInfo != null ? secondLineInfo.hashCode() : 0);
        result = 31 * result + (thirdLineInfo != null ? thirdLineInfo.hashCode() : 0);
        result = 31 * result + (fourthLineInfo != null ? fourthLineInfo.hashCode() : 0);
        result = 31 * result + (operationSource != null ? operationSource.hashCode() : 0);
        result = 31 * result + (addTime != null ? addTime.hashCode() : 0);
        return result;
    }
}
