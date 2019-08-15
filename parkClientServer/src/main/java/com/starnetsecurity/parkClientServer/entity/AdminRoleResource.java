package com.starnetsecurity.parkClientServer.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by 宏炜 on 2017-10-12.
 */
@Entity
@Table(name = "admin_role_resource", schema = "vams", catalog = "")
public class AdminRoleResource {
    private String id;
    private String adminRoleId;
    private String adminResourceId;
    private String adminResourcePermission;
    private Byte haveChildren;

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
    @Column(name = "admin_role_id")
    public String getAdminRoleId() {
        return adminRoleId;
    }

    public void setAdminRoleId(String adminRoleId) {
        this.adminRoleId = adminRoleId;
    }

    @Basic
    @Column(name = "admin_resource_id")
    public String getAdminResourceId() {
        return adminResourceId;
    }

    public void setAdminResourceId(String adminResourceId) {
        this.adminResourceId = adminResourceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdminRoleResource that = (AdminRoleResource) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (adminRoleId != null ? !adminRoleId.equals(that.adminRoleId) : that.adminRoleId != null) return false;
        if (adminResourceId != null ? !adminResourceId.equals(that.adminResourceId) : that.adminResourceId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (adminRoleId != null ? adminRoleId.hashCode() : 0);
        result = 31 * result + (adminResourceId != null ? adminResourceId.hashCode() : 0);
        return result;
    }

    @Basic
    @Column(name = "admin_resource_permission")
    public String getAdminResourcePermission() {
        return adminResourcePermission;
    }

    public void setAdminResourcePermission(String adminResourcePermission) {
        this.adminResourcePermission = adminResourcePermission;
    }

    @Basic
    @Column(name = "have_children")
    public Byte getHaveChildren() {
        return haveChildren;
    }

    public void setHaveChildren(Byte haveChildren) {
        this.haveChildren = haveChildren;
    }
}
