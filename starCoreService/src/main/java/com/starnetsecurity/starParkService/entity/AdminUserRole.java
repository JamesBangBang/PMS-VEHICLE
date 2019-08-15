package com.starnetsecurity.starParkService.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by 宏炜 on 2017-10-12.
 */
@Entity
@Table(name = "admin_user_role", schema = "vamsserver", catalog = "")
public class AdminUserRole {
    private String id;
    private String adminUserId;
    private String adminRoleId;
    private String adminRoleTag;

    @Id
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
    @Column(name = "admin_user_id")
    public String getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(String adminUserId) {
        this.adminUserId = adminUserId;
    }

    @Basic
    @Column(name = "admin_role_id")
    public String getAdminRoleId() {
        return adminRoleId;
    }

    public void setAdminRoleId(String adminRoleId) {
        this.adminRoleId = adminRoleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdminUserRole that = (AdminUserRole) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (adminUserId != null ? !adminUserId.equals(that.adminUserId) : that.adminUserId != null) return false;
        if (adminRoleId != null ? !adminRoleId.equals(that.adminRoleId) : that.adminRoleId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (adminUserId != null ? adminUserId.hashCode() : 0);
        result = 31 * result + (adminRoleId != null ? adminRoleId.hashCode() : 0);
        return result;
    }

    @Basic
    @Column(name = "admin_role_tag")
    public String getAdminRoleTag() {
        return adminRoleTag;
    }

    public void setAdminRoleTag(String adminRoleTag) {
        this.adminRoleTag = adminRoleTag;
    }
}
