package com.starnetsecurity.parkClientServer.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2017-11-15.
 */
@Entity
@Table(name = "cn_db_backupimport", schema = "vams", catalog = "")
public class CnDbBackupimport {
    private String backupimportId;
    private String backupimportName;
    private String backupimportValue;
    private String backupimportType;
    private Timestamp insertTime;

    @Id
    @Column(name = "backupimport_id")
    public String getBackupimportId() {
        return backupimportId;
    }

    public void setBackupimportId(String backupimportId) {
        this.backupimportId = backupimportId;
    }

    @Basic
    @Column(name = "backupimport_name")
    public String getBackupimportName() {
        return backupimportName;
    }

    public void setBackupimportName(String backupimportName) {
        this.backupimportName = backupimportName;
    }

    @Basic
    @Column(name = "backupimport_value")
    public String getBackupimportValue() {
        return backupimportValue;
    }

    public void setBackupimportValue(String backupimportValue) {
        this.backupimportValue = backupimportValue;
    }

    @Basic
    @Column(name = "backupimport_type")
    public String getBackupimportType() {
        return backupimportType;
    }

    public void setBackupimportType(String backupimportType) {
        this.backupimportType = backupimportType;
    }

    @Basic
    @Column(name = "insert_time")
    public Timestamp getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Timestamp insertTime) {
        this.insertTime = insertTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CnDbBackupimport that = (CnDbBackupimport) o;

        if (backupimportId != null ? !backupimportId.equals(that.backupimportId) : that.backupimportId != null)
            return false;
        if (backupimportName != null ? !backupimportName.equals(that.backupimportName) : that.backupimportName != null)
            return false;
        if (backupimportValue != null ? !backupimportValue.equals(that.backupimportValue) : that.backupimportValue != null)
            return false;
        if (backupimportType != null ? !backupimportType.equals(that.backupimportType) : that.backupimportType != null)
            return false;
        if (insertTime != null ? !insertTime.equals(that.insertTime) : that.insertTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = backupimportId != null ? backupimportId.hashCode() : 0;
        result = 31 * result + (backupimportName != null ? backupimportName.hashCode() : 0);
        result = 31 * result + (backupimportValue != null ? backupimportValue.hashCode() : 0);
        result = 31 * result + (backupimportType != null ? backupimportType.hashCode() : 0);
        result = 31 * result + (insertTime != null ? insertTime.hashCode() : 0);
        return result;
    }
}
