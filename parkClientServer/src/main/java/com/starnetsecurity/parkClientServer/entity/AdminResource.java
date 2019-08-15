package com.starnetsecurity.parkClientServer.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by 宏炜 on 2017-10-12.
 */
@Entity
@Table(name = "admin_resource", schema = "vams", catalog = "")
public class AdminResource {
    private String id;
    private String parentId;
    private String parentPath;
    private String resourceName;
    private String resourceType;
    private Integer priority;
    private String permission;
    private String status;
    private String targetLink;
    private String icon;
    private String resourceTargetId;
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
    @Column(name = "parent_id")
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Basic
    @Column(name = "parent_path")
    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    @Basic
    @Column(name = "resource_name")
    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    @Basic
    @Column(name = "resource_type")
    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    @Basic
    @Column(name = "priority")
    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Basic
    @Column(name = "permission")
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
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
    @Column(name = "target_link")
    public String getTargetLink() {
        return targetLink;
    }

    public void setTargetLink(String targetLink) {
        this.targetLink = targetLink;
    }

    @Basic
    @Column(name = "icon")
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Basic
    @Column(name = "resource_target_id")
    public String getResourceTargetId() {
        return resourceTargetId;
    }

    public void setResourceTargetId(String resourceTargetId) {
        this.resourceTargetId = resourceTargetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdminResource that = (AdminResource) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (parentId != null ? !parentId.equals(that.parentId) : that.parentId != null) return false;
        if (parentPath != null ? !parentPath.equals(that.parentPath) : that.parentPath != null) return false;
        if (resourceName != null ? !resourceName.equals(that.resourceName) : that.resourceName != null) return false;
        if (resourceType != null ? !resourceType.equals(that.resourceType) : that.resourceType != null) return false;
        if (priority != null ? !priority.equals(that.priority) : that.priority != null) return false;
        if (permission != null ? !permission.equals(that.permission) : that.permission != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (targetLink != null ? !targetLink.equals(that.targetLink) : that.targetLink != null) return false;
        if (icon != null ? !icon.equals(that.icon) : that.icon != null) return false;
        if (resourceTargetId != null ? !resourceTargetId.equals(that.resourceTargetId) : that.resourceTargetId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + (parentPath != null ? parentPath.hashCode() : 0);
        result = 31 * result + (resourceName != null ? resourceName.hashCode() : 0);
        result = 31 * result + (resourceType != null ? resourceType.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (permission != null ? permission.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (targetLink != null ? targetLink.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        result = 31 * result + (resourceTargetId != null ? resourceTargetId.hashCode() : 0);
        return result;
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
