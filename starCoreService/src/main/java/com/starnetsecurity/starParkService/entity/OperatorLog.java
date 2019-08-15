package com.starnetsecurity.starParkService.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2017-10-12.
 */
@Entity
@Table(name = "operator_log", schema = "vamsserver", catalog = "")
public class OperatorLog {
    private String operatorLogId;
    private String logType;
    private String logTypeName;
    private String logSource;
    private String operatorNo;
    private String logDesc;
    private String operationSource;
    private Timestamp addTime;
    private String operatorName;
    private Integer useMark;
    private String departmentId;

    @Id
    @Column(name = "operator_log_id")
    public String getOperatorLogId() {
        return operatorLogId;
    }

    public void setOperatorLogId(String operatorLogId) {
        this.operatorLogId = operatorLogId;
    }

    @Basic
    @Column(name = "log_type")
    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    @Basic
    @Column(name = "log_type_name")
    public String getLogTypeName() {
        return logTypeName;
    }

    public void setLogTypeName(String logTypeName) {
        this.logTypeName = logTypeName;
    }

    @Basic
    @Column(name = "log_source")
    public String getLogSource() {
        return logSource;
    }

    public void setLogSource(String logSource) {
        this.logSource = logSource;
    }

    @Basic
    @Column(name = "operator_no")
    public String getOperatorNo() {
        return operatorNo;
    }

    public void setOperatorNo(String operatorNo) {
        this.operatorNo = operatorNo;
    }

    @Basic
    @Column(name = "log_desc")
    public String getLogDesc() {
        return logDesc;
    }

    public void setLogDesc(String logDesc) {
        this.logDesc = logDesc;
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
    @Column(name = "operator_name")
    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
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

        OperatorLog that = (OperatorLog) o;

        if (operatorLogId != null ? !operatorLogId.equals(that.operatorLogId) : that.operatorLogId != null)
            return false;
        if (logType != null ? !logType.equals(that.logType) : that.logType != null) return false;
        if (logTypeName != null ? !logTypeName.equals(that.logTypeName) : that.logTypeName != null) return false;
        if (logSource != null ? !logSource.equals(that.logSource) : that.logSource != null) return false;
        if (operatorNo != null ? !operatorNo.equals(that.operatorNo) : that.operatorNo != null) return false;
        if (logDesc != null ? !logDesc.equals(that.logDesc) : that.logDesc != null) return false;
        if (operationSource != null ? !operationSource.equals(that.operationSource) : that.operationSource != null)
            return false;
        if (addTime != null ? !addTime.equals(that.addTime) : that.addTime != null) return false;
        if (operatorName != null ? !operatorName.equals(that.operatorName) : that.operatorName != null) return false;
        if (useMark != null ? !useMark.equals(that.useMark) : that.useMark != null) return false;
        if (departmentId != null ? !departmentId.equals(that.departmentId) : that.departmentId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = operatorLogId != null ? operatorLogId.hashCode() : 0;
        result = 31 * result + (logType != null ? logType.hashCode() : 0);
        result = 31 * result + (logTypeName != null ? logTypeName.hashCode() : 0);
        result = 31 * result + (logSource != null ? logSource.hashCode() : 0);
        result = 31 * result + (operatorNo != null ? operatorNo.hashCode() : 0);
        result = 31 * result + (logDesc != null ? logDesc.hashCode() : 0);
        result = 31 * result + (operationSource != null ? operationSource.hashCode() : 0);
        result = 31 * result + (addTime != null ? addTime.hashCode() : 0);
        result = 31 * result + (operatorName != null ? operatorName.hashCode() : 0);
        result = 31 * result + (useMark != null ? useMark.hashCode() : 0);
        result = 31 * result + (departmentId != null ? departmentId.hashCode() : 0);
        return result;
    }
}
