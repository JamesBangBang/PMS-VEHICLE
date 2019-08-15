package com.starnetsecurity.parkClientServer.entity;

import javax.persistence.*;

/**
 * Created by 宏炜 on 2017-11-15.
 */
@Entity
@Table(name = "basic_config", schema = "vams", catalog = "")
public class BasicConfig {
    private String configName;
    private String configValue;
    private String configDesc;
    private String configId;
    private String departmentId;
    private Integer useMark;

    @Id
    @Column(name = "config_name")
    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    @Basic
    @Column(name = "config_value")
    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    @Basic
    @Column(name = "config_desc")
    public String getConfigDesc() {
        return configDesc;
    }

    public void setConfigDesc(String configDesc) {
        this.configDesc = configDesc;
    }

    @Basic
    @Column(name = "config_id")
    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    @Basic
    @Column(name = "department_id")
    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    @Basic
    @Column(name = "use_mark")
    public Integer getUseMark() {
        return useMark;
    }

    public void setUseMark(Integer useMark) {
        this.useMark = useMark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicConfig that = (BasicConfig) o;

        if (configName != null ? !configName.equals(that.configName) : that.configName != null) return false;
        if (configValue != null ? !configValue.equals(that.configValue) : that.configValue != null) return false;
        if (configDesc != null ? !configDesc.equals(that.configDesc) : that.configDesc != null) return false;
        if (configId != null ? !configId.equals(that.configId) : that.configId != null) return false;
        if (departmentId != null ? !departmentId.equals(that.departmentId) : that.departmentId != null) return false;
        if (useMark != null ? !useMark.equals(that.useMark) : that.useMark != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = configName != null ? configName.hashCode() : 0;
        result = 31 * result + (configValue != null ? configValue.hashCode() : 0);
        result = 31 * result + (configDesc != null ? configDesc.hashCode() : 0);
        result = 31 * result + (configId != null ? configId.hashCode() : 0);
        result = 31 * result + (departmentId != null ? departmentId.hashCode() : 0);
        result = 31 * result + (useMark != null ? useMark.hashCode() : 0);
        return result;
    }
}
