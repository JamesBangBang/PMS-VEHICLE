package com.starnetsecurity.starParkService.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2017-10-12.
 */
@Entity
@Table(name = "post_computer_manage", schema = "vamsserver", catalog = "")
public class PostComputerManage {
    private String postComputerId;
    private String postComputerName;
    private String postComputerIp;
    private String operationSource;
    private String postComputerMac;
    private Timestamp addTime;
    private String synPort;
    private Integer status;
    private String parymentUnitId;
    private String isAutoDeal;
    private String isVoicePlay;
    private Integer useMark;
    private String departmentId;

    @Id
    @Column(name = "post_computer_id")
    public String getPostComputerId() {
        return postComputerId;
    }

    public void setPostComputerId(String postComputerId) {
        this.postComputerId = postComputerId;
    }

    @Basic
    @Column(name = "post_computer_name")
    public String getPostComputerName() {
        return postComputerName;
    }

    public void setPostComputerName(String postComputerName) {
        this.postComputerName = postComputerName;
    }

    @Basic
    @Column(name = "post_computer_ip")
    public String getPostComputerIp() {
        return postComputerIp;
    }

    public void setPostComputerIp(String postComputerIp) {
        this.postComputerIp = postComputerIp;
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
    @Column(name = "post_computer_mac")
    public String getPostComputerMac() {
        return postComputerMac;
    }

    public void setPostComputerMac(String postComputerMac) {
        this.postComputerMac = postComputerMac;
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
    @Column(name = "syn_port")
    public String getSynPort() {
        return synPort;
    }

    public void setSynPort(String synPort) {
        this.synPort = synPort;
    }

    @Basic
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "paryment_unit_id")
    public String getParymentUnitId() {
        return parymentUnitId;
    }

    public void setParymentUnitId(String parymentUnitId) {
        this.parymentUnitId = parymentUnitId;
    }

    @Basic
    @Column(name = "is_auto_deal")
    public String getIsAutoDeal() {
        return isAutoDeal;
    }

    public void setIsAutoDeal(String isAutoDeal) {
        this.isAutoDeal = isAutoDeal;
    }

    @Basic
    @Column(name = "is_voice_play")
    public String getIsVoicePlay() {
        return isVoicePlay;
    }

    public void setIsVoicePlay(String isVoicePlay) {
        this.isVoicePlay = isVoicePlay;
    }

    @Basic
    @Column(name = "use_mark")
    public Integer getUseMark() {
        return useMark;
    }

    public void setUseMark(Integer useMark) {
        this.useMark = useMark;
    }

    @Basic
    @Column(name = "department_id")
    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PostComputerManage that = (PostComputerManage) o;

        if (postComputerId != null ? !postComputerId.equals(that.postComputerId) : that.postComputerId != null)
            return false;
        if (postComputerName != null ? !postComputerName.equals(that.postComputerName) : that.postComputerName != null)
            return false;
        if (postComputerIp != null ? !postComputerIp.equals(that.postComputerIp) : that.postComputerIp != null)
            return false;
        if (operationSource != null ? !operationSource.equals(that.operationSource) : that.operationSource != null)
            return false;
        if (postComputerMac != null ? !postComputerMac.equals(that.postComputerMac) : that.postComputerMac != null)
            return false;
        if (addTime != null ? !addTime.equals(that.addTime) : that.addTime != null) return false;
        if (synPort != null ? !synPort.equals(that.synPort) : that.synPort != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (parymentUnitId != null ? !parymentUnitId.equals(that.parymentUnitId) : that.parymentUnitId != null)
            return false;
        if (isAutoDeal != null ? !isAutoDeal.equals(that.isAutoDeal) : that.isAutoDeal != null) return false;
        if (isVoicePlay != null ? !isVoicePlay.equals(that.isVoicePlay) : that.isVoicePlay != null) return false;
        if (useMark != null ? !useMark.equals(that.useMark) : that.useMark != null) return false;
        if (departmentId != null ? !departmentId.equals(that.departmentId) : that.departmentId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = postComputerId != null ? postComputerId.hashCode() : 0;
        result = 31 * result + (postComputerName != null ? postComputerName.hashCode() : 0);
        result = 31 * result + (postComputerIp != null ? postComputerIp.hashCode() : 0);
        result = 31 * result + (operationSource != null ? operationSource.hashCode() : 0);
        result = 31 * result + (postComputerMac != null ? postComputerMac.hashCode() : 0);
        result = 31 * result + (addTime != null ? addTime.hashCode() : 0);
        result = 31 * result + (synPort != null ? synPort.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (parymentUnitId != null ? parymentUnitId.hashCode() : 0);
        result = 31 * result + (isAutoDeal != null ? isAutoDeal.hashCode() : 0);
        result = 31 * result + (isVoicePlay != null ? isVoicePlay.hashCode() : 0);
        result = 31 * result + (useMark != null ? useMark.hashCode() : 0);
        result = 31 * result + (departmentId != null ? departmentId.hashCode() : 0);
        return result;
    }
}
