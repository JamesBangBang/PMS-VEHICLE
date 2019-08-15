package com.starnetsecurity.starParkService.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2017-10-12.
 */
@Entity
public class Operator {
    private String operatorId;
    private String operatorUserName;
    private String operatorUserPwd;
    private String operatorName;
    private String rightTemplateId;
    private String enable;
    private String operationSource;
    private Timestamp addTime;
    private String operatorDepartment;
    private String operatorJobNumber;
    private String operatorTelephoneNumber;
    private String operatorMail;
    private String operatorRemark;
    private String operatorPic;
    private Integer useMark;
    private String departmentId;

    @Id
    @Column(name = "operator_id")
    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    @Basic
    @Column(name = "operator_user_name")
    public String getOperatorUserName() {
        return operatorUserName;
    }

    public void setOperatorUserName(String operatorUserName) {
        this.operatorUserName = operatorUserName;
    }

    @Basic
    @Column(name = "operator_user_pwd")
    public String getOperatorUserPwd() {
        return operatorUserPwd;
    }

    public void setOperatorUserPwd(String operatorUserPwd) {
        this.operatorUserPwd = operatorUserPwd;
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
    @Column(name = "right_template_id")
    public String getRightTemplateId() {
        return rightTemplateId;
    }

    public void setRightTemplateId(String rightTemplateId) {
        this.rightTemplateId = rightTemplateId;
    }

    @Basic
    @Column(name = "enable")
    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
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
    @Column(name = "operator_department")
    public String getOperatorDepartment() {
        return operatorDepartment;
    }

    public void setOperatorDepartment(String operatorDepartment) {
        this.operatorDepartment = operatorDepartment;
    }

    @Basic
    @Column(name = "operator_job_number")
    public String getOperatorJobNumber() {
        return operatorJobNumber;
    }

    public void setOperatorJobNumber(String operatorJobNumber) {
        this.operatorJobNumber = operatorJobNumber;
    }

    @Basic
    @Column(name = "operator_telephone_number")
    public String getOperatorTelephoneNumber() {
        return operatorTelephoneNumber;
    }

    public void setOperatorTelephoneNumber(String operatorTelephoneNumber) {
        this.operatorTelephoneNumber = operatorTelephoneNumber;
    }

    @Basic
    @Column(name = "operator_mail")
    public String getOperatorMail() {
        return operatorMail;
    }

    public void setOperatorMail(String operatorMail) {
        this.operatorMail = operatorMail;
    }

    @Basic
    @Column(name = "operator_remark")
    public String getOperatorRemark() {
        return operatorRemark;
    }

    public void setOperatorRemark(String operatorRemark) {
        this.operatorRemark = operatorRemark;
    }

    @Basic
    @Column(name = "operator_pic")
    public String getOperatorPic() {
        return operatorPic;
    }

    public void setOperatorPic(String operatorPic) {
        this.operatorPic = operatorPic;
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

        Operator operator = (Operator) o;

        if (operatorId != null ? !operatorId.equals(operator.operatorId) : operator.operatorId != null) return false;
        if (operatorUserName != null ? !operatorUserName.equals(operator.operatorUserName) : operator.operatorUserName != null)
            return false;
        if (operatorUserPwd != null ? !operatorUserPwd.equals(operator.operatorUserPwd) : operator.operatorUserPwd != null)
            return false;
        if (operatorName != null ? !operatorName.equals(operator.operatorName) : operator.operatorName != null)
            return false;
        if (rightTemplateId != null ? !rightTemplateId.equals(operator.rightTemplateId) : operator.rightTemplateId != null)
            return false;
        if (enable != null ? !enable.equals(operator.enable) : operator.enable != null) return false;
        if (operationSource != null ? !operationSource.equals(operator.operationSource) : operator.operationSource != null)
            return false;
        if (addTime != null ? !addTime.equals(operator.addTime) : operator.addTime != null) return false;
        if (operatorDepartment != null ? !operatorDepartment.equals(operator.operatorDepartment) : operator.operatorDepartment != null)
            return false;
        if (operatorJobNumber != null ? !operatorJobNumber.equals(operator.operatorJobNumber) : operator.operatorJobNumber != null)
            return false;
        if (operatorTelephoneNumber != null ? !operatorTelephoneNumber.equals(operator.operatorTelephoneNumber) : operator.operatorTelephoneNumber != null)
            return false;
        if (operatorMail != null ? !operatorMail.equals(operator.operatorMail) : operator.operatorMail != null)
            return false;
        if (operatorRemark != null ? !operatorRemark.equals(operator.operatorRemark) : operator.operatorRemark != null)
            return false;
        if (operatorPic != null ? !operatorPic.equals(operator.operatorPic) : operator.operatorPic != null)
            return false;
        if (useMark != null ? !useMark.equals(operator.useMark) : operator.useMark != null) return false;
        if (departmentId != null ? !departmentId.equals(operator.departmentId) : operator.departmentId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = operatorId != null ? operatorId.hashCode() : 0;
        result = 31 * result + (operatorUserName != null ? operatorUserName.hashCode() : 0);
        result = 31 * result + (operatorUserPwd != null ? operatorUserPwd.hashCode() : 0);
        result = 31 * result + (operatorName != null ? operatorName.hashCode() : 0);
        result = 31 * result + (rightTemplateId != null ? rightTemplateId.hashCode() : 0);
        result = 31 * result + (enable != null ? enable.hashCode() : 0);
        result = 31 * result + (operationSource != null ? operationSource.hashCode() : 0);
        result = 31 * result + (addTime != null ? addTime.hashCode() : 0);
        result = 31 * result + (operatorDepartment != null ? operatorDepartment.hashCode() : 0);
        result = 31 * result + (operatorJobNumber != null ? operatorJobNumber.hashCode() : 0);
        result = 31 * result + (operatorTelephoneNumber != null ? operatorTelephoneNumber.hashCode() : 0);
        result = 31 * result + (operatorMail != null ? operatorMail.hashCode() : 0);
        result = 31 * result + (operatorRemark != null ? operatorRemark.hashCode() : 0);
        result = 31 * result + (operatorPic != null ? operatorPic.hashCode() : 0);
        result = 31 * result + (useMark != null ? useMark.hashCode() : 0);
        result = 31 * result + (departmentId != null ? departmentId.hashCode() : 0);
        return result;
    }
}
