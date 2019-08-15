package com.starnetsecurity.parkClientServer.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by 宏炜 on 2017-11-15.
 */
@Entity
@Table(name = "department_info", schema = "vams", catalog = "")
public class DepartmentInfo {
    private String depId;
    private String depName;
    private String depFullName;
    private String englishName;
    private String address;
    private String phone;
    private String fax;
    private String postno;
    private String webAddress;
    private String email;
    private String operationSource;
    private Timestamp addTime;
    private String priorityCity;
    private String defaultMemberId;
    private Integer useMark;
    private Set<CarparkInfo> carparkInfos = new HashSet<CarparkInfo>();

    @Id
    @Column(name = "dep_id")
    public String getDepId() {
        return depId;
    }

    public void setDepId(String depId) {
        this.depId = depId;
    }

    @Basic
    @Column(name = "dep_name")
    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    @Basic
    @Column(name = "dep_full_name")
    public String getDepFullName() {
        return depFullName;
    }

    public void setDepFullName(String depFullName) {
        this.depFullName = depFullName;
    }

    @Basic
    @Column(name = "english_name")
    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    @Basic
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "fax")
    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @Basic
    @Column(name = "postno")
    public String getPostno() {
        return postno;
    }

    public void setPostno(String postno) {
        this.postno = postno;
    }

    @Basic
    @Column(name = "web_address")
    public String getWebAddress() {
        return webAddress;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
    @Column(name = "priority_city")
    public String getPriorityCity() {
        return priorityCity;
    }

    public void setPriorityCity(String priorityCity) {
        this.priorityCity = priorityCity;
    }

    @Basic
    @Column(name = "default_member_id")
    public String getDefaultMemberId() {
        return defaultMemberId;
    }

    public void setDefaultMemberId(String defaultMemberId) {
        this.defaultMemberId = defaultMemberId;
    }

    @Basic
    @Column(name = "use_mark")
    public Integer getUseMark() {
        return useMark;
    }

    public void setUseMark(Integer useMark) {
        this.useMark = useMark;
    }

    @OneToMany(mappedBy = "departmentInfo",fetch = FetchType.LAZY)
    public Set<CarparkInfo> getCarparkInfos() {
        return carparkInfos;
    }

    public void setCarparkInfos(Set<CarparkInfo> carparkInfos) {
        this.carparkInfos = carparkInfos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DepartmentInfo that = (DepartmentInfo) o;

        if (depId != null ? !depId.equals(that.depId) : that.depId != null) return false;
        if (depName != null ? !depName.equals(that.depName) : that.depName != null) return false;
        if (depFullName != null ? !depFullName.equals(that.depFullName) : that.depFullName != null) return false;
        if (englishName != null ? !englishName.equals(that.englishName) : that.englishName != null) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (fax != null ? !fax.equals(that.fax) : that.fax != null) return false;
        if (postno != null ? !postno.equals(that.postno) : that.postno != null) return false;
        if (webAddress != null ? !webAddress.equals(that.webAddress) : that.webAddress != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (operationSource != null ? !operationSource.equals(that.operationSource) : that.operationSource != null)
            return false;
        if (addTime != null ? !addTime.equals(that.addTime) : that.addTime != null) return false;
        if (priorityCity != null ? !priorityCity.equals(that.priorityCity) : that.priorityCity != null) return false;
        if (defaultMemberId != null ? !defaultMemberId.equals(that.defaultMemberId) : that.defaultMemberId != null) return false;
        if (useMark != null ? !useMark.equals(that.useMark) : that.useMark != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = depId != null ? depId.hashCode() : 0;
        result = 31 * result + (depName != null ? depName.hashCode() : 0);
        result = 31 * result + (depFullName != null ? depFullName.hashCode() : 0);
        result = 31 * result + (englishName != null ? englishName.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (fax != null ? fax.hashCode() : 0);
        result = 31 * result + (postno != null ? postno.hashCode() : 0);
        result = 31 * result + (webAddress != null ? webAddress.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (operationSource != null ? operationSource.hashCode() : 0);
        result = 31 * result + (addTime != null ? addTime.hashCode() : 0);
        result = 31 * result + (priorityCity != null ? priorityCity.hashCode() : 0);
        result = 31 * result + (defaultMemberId != null ? defaultMemberId.hashCode() : 0);
        result = 31 * result + (useMark != null ? useMark.hashCode() : 0);
        return result;
    }
}
