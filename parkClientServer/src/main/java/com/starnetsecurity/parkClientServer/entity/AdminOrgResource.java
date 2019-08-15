package com.starnetsecurity.parkClientServer.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "admin_org_resource", schema = "vams", catalog = "")
public class AdminOrgResource {
    private String id;
    private String resId;
    private String resType;
    private String resDesc;
    private String adminUserId;
    private Integer recPriority;

    @Id
    @Basic
    @GenericGenerator(name="systemUUID",strategy="uuid")
    @GeneratedValue(generator="systemUUID")
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "res_id")
    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    @Basic
    @Column(name = "res_type")
    public String getResType() {
        return resType;
    }

    public void setResType(String resType) {
        this.resType = resType;
    }

    @Basic
    @Column(name = "res_desc")
    public String getResDesc() {
        return resDesc;
    }

    public void setResDesc(String resDesc) {
        this.resDesc = resDesc;
    }

    @Basic
    @Column(name = "admin_user_id")
    public String getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(String adminUserId) {
        this.adminUserId = adminUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdminOrgResource that = (AdminOrgResource) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (resId != null ? !resId.equals(that.resId) : that.resId != null) return false;
        if (resType != null ? !resType.equals(that.resType) : that.resType != null) return false;
        if (resDesc != null ? !resDesc.equals(that.resDesc) : that.resDesc != null) return false;
        if (adminUserId != null ? !adminUserId.equals(that.adminUserId) : that.adminUserId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (resId != null ? resId.hashCode() : 0);
        result = 31 * result + (resType != null ? resType.hashCode() : 0);
        result = 31 * result + (resDesc != null ? resDesc.hashCode() : 0);
        result = 31 * result + (adminUserId != null ? adminUserId.hashCode() : 0);
        return result;
    }

    @Basic
    @Column(name = "rec_priority")
    public Integer getRecPriority() {
        return recPriority;
    }

    public void setRecPriority(Integer recPriority) {
        this.recPriority = recPriority;
    }
}
