package com.starnetsecurity.parkClientServer.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Created by 宏炜 on 2017-10-12.
 */
@Entity
@Table(name = "admin_role", schema = "vams", catalog = "")
public class AdminRole {
    private String id;
    private String roleTag;
    private String roleName;
    private String description;
    private String status;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String sysTag;

    public AdminRole() {
        id = UUID.randomUUID().toString().replaceAll("-","");
    }

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "role_tag")
    public String getRoleTag() {
        return roleTag;
    }

    public void setRoleTag(String roleTag) {
        this.roleTag = roleTag;
    }

    @Basic
    @Column(name = "role_name")
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "update_time")
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Basic
    @Column(name = "sys_tag")
    public String getSysTag() {
        return sysTag;
    }

    public void setSysTag(String sysTag) {
        this.sysTag = sysTag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdminRole adminRole = (AdminRole) o;

        if (id != null ? !id.equals(adminRole.id) : adminRole.id != null) return false;
        if (roleTag != null ? !roleTag.equals(adminRole.roleTag) : adminRole.roleTag != null) return false;
        if (roleName != null ? !roleName.equals(adminRole.roleName) : adminRole.roleName != null) return false;
        if (description != null ? !description.equals(adminRole.description) : adminRole.description != null)
            return false;
        if (status != null ? !status.equals(adminRole.status) : adminRole.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (roleTag != null ? roleTag.hashCode() : 0);
        result = 31 * result + (roleName != null ? roleName.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
