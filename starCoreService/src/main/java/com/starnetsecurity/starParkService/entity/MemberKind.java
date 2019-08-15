package com.starnetsecurity.starParkService.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2017-10-12.
 */
@Entity
@Table(name = "member_kind", schema = "vamsserver", catalog = "")
public class MemberKind {
    private String id;
    private String kindName;
    private String memo;
    private Integer packageKind;
    private String monthPackage;
    private String countPackage;
    private Integer packageChildKind;
    private String chargeRuleTemplate;
    private String chargeRuleList;
    private String parkId;
    private Integer isDelete;
    private String isStatistic;
    private String voicePath;
    private String showColor;
    private Timestamp addTime;
    private Integer useType;
    private Integer useMark;
    private String departmentId;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "kind_name")
    public String getKindName() {
        return kindName;
    }

    public void setKindName(String kindName) {
        this.kindName = kindName;
    }

    @Basic
    @Column(name = "memo")
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Basic
    @Column(name = "package_kind")
    public Integer getPackageKind() {
        return packageKind;
    }

    public void setPackageKind(Integer packageKind) {
        this.packageKind = packageKind;
    }

    @Basic
    @Column(name = "month_package")
    public String getMonthPackage() {
        return monthPackage;
    }

    public void setMonthPackage(String monthPackage) {
        this.monthPackage = monthPackage;
    }

    @Basic
    @Column(name = "count_package")
    public String getCountPackage() {
        return countPackage;
    }

    public void setCountPackage(String countPackage) {
        this.countPackage = countPackage;
    }

    @Basic
    @Column(name = "package_child_kind")
    public Integer getPackageChildKind() {
        return packageChildKind;
    }

    public void setPackageChildKind(Integer packageChildKind) {
        this.packageChildKind = packageChildKind;
    }

    @Basic
    @Column(name = "charge_rule_template")
    public String getChargeRuleTemplate() {
        return chargeRuleTemplate;
    }

    public void setChargeRuleTemplate(String chargeRuleTemplate) {
        this.chargeRuleTemplate = chargeRuleTemplate;
    }

    @Basic
    @Column(name = "charge_rule_list")
    public String getChargeRuleList() {
        return chargeRuleList;
    }

    public void setChargeRuleList(String chargeRuleList) {
        this.chargeRuleList = chargeRuleList;
    }

    @Basic
    @Column(name = "park_id")
    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    @Basic
    @Column(name = "is_delete")
    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    @Basic
    @Column(name = "is_statistic")
    public String getIsStatistic() {
        return isStatistic;
    }

    public void setIsStatistic(String isStatistic) {
        this.isStatistic = isStatistic;
    }

    @Basic
    @Column(name = "voice_path")
    public String getVoicePath() {
        return voicePath;
    }

    public void setVoicePath(String voicePath) {
        this.voicePath = voicePath;
    }

    @Basic
    @Column(name = "show_color")
    public String getShowColor() {
        return showColor;
    }

    public void setShowColor(String showColor) {
        this.showColor = showColor;
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
    @Column(name = "use_type")
    public Integer getUseType() {
        return useType;
    }

    public void setUseType(Integer useType) {
        this.useType = useType;
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

        MemberKind that = (MemberKind) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (kindName != null ? !kindName.equals(that.kindName) : that.kindName != null) return false;
        if (memo != null ? !memo.equals(that.memo) : that.memo != null) return false;
        if (packageKind != null ? !packageKind.equals(that.packageKind) : that.packageKind != null) return false;
        if (monthPackage != null ? !monthPackage.equals(that.monthPackage) : that.monthPackage != null) return false;
        if (countPackage != null ? !countPackage.equals(that.countPackage) : that.countPackage != null) return false;
        if (packageChildKind != null ? !packageChildKind.equals(that.packageChildKind) : that.packageChildKind != null)
            return false;
        if (chargeRuleTemplate != null ? !chargeRuleTemplate.equals(that.chargeRuleTemplate) : that.chargeRuleTemplate != null)
            return false;
        if (chargeRuleList != null ? !chargeRuleList.equals(that.chargeRuleList) : that.chargeRuleList != null)
            return false;
        if (parkId != null ? !parkId.equals(that.parkId) : that.parkId != null) return false;
        if (isDelete != null ? !isDelete.equals(that.isDelete) : that.isDelete != null) return false;
        if (isStatistic != null ? !isStatistic.equals(that.isStatistic) : that.isStatistic != null) return false;
        if (voicePath != null ? !voicePath.equals(that.voicePath) : that.voicePath != null) return false;
        if (showColor != null ? !showColor.equals(that.showColor) : that.showColor != null) return false;
        if (addTime != null ? !addTime.equals(that.addTime) : that.addTime != null) return false;
        if (useType != null ? !useType.equals(that.useType) : that.useType != null) return false;
        if (useMark != null ? !useMark.equals(that.useMark) : that.useMark != null) return false;
        if (departmentId != null ? !departmentId.equals(that.departmentId) : that.departmentId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (kindName != null ? kindName.hashCode() : 0);
        result = 31 * result + (memo != null ? memo.hashCode() : 0);
        result = 31 * result + (packageKind != null ? packageKind.hashCode() : 0);
        result = 31 * result + (monthPackage != null ? monthPackage.hashCode() : 0);
        result = 31 * result + (countPackage != null ? countPackage.hashCode() : 0);
        result = 31 * result + (packageChildKind != null ? packageChildKind.hashCode() : 0);
        result = 31 * result + (chargeRuleTemplate != null ? chargeRuleTemplate.hashCode() : 0);
        result = 31 * result + (chargeRuleList != null ? chargeRuleList.hashCode() : 0);
        result = 31 * result + (parkId != null ? parkId.hashCode() : 0);
        result = 31 * result + (isDelete != null ? isDelete.hashCode() : 0);
        result = 31 * result + (isStatistic != null ? isStatistic.hashCode() : 0);
        result = 31 * result + (voicePath != null ? voicePath.hashCode() : 0);
        result = 31 * result + (showColor != null ? showColor.hashCode() : 0);
        result = 31 * result + (addTime != null ? addTime.hashCode() : 0);
        result = 31 * result + (useType != null ? useType.hashCode() : 0);
        result = 31 * result + (useMark != null ? useMark.hashCode() : 0);
        result = 31 * result + (departmentId != null ? departmentId.hashCode() : 0);
        return result;
    }
}
