package com.starnetsecurity.parkClientServer.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2017-11-15.
 */
@Entity
@Table(name = "sync_rule", schema = "vams", catalog = "")
public class SyncRule {
    private long id;
    private String mergeSql;
    private String deleteSql;
    private String tableName;
    private Timestamp syncTime;
    private Integer mergeFlag;
    private Integer mergeSuccessFlag;
    private Integer deleteFlag;
    private Integer deleteSuccessFlag;
    private Integer status;
    private Integer numPerTimes;
    private String parimaryName;
    private String syncFlagName;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "merge_sql")
    public String getMergeSql() {
        return mergeSql;
    }

    public void setMergeSql(String mergeSql) {
        this.mergeSql = mergeSql;
    }

    @Basic
    @Column(name = "delete_sql")
    public String getDeleteSql() {
        return deleteSql;
    }

    public void setDeleteSql(String deleteSql) {
        this.deleteSql = deleteSql;
    }

    @Basic
    @Column(name = "table_name")
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Basic
    @Column(name = "sync_time")
    public Timestamp getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(Timestamp syncTime) {
        this.syncTime = syncTime;
    }

    @Basic
    @Column(name = "merge_flag")
    public Integer getMergeFlag() {
        return mergeFlag;
    }

    public void setMergeFlag(Integer mergeFlag) {
        this.mergeFlag = mergeFlag;
    }

    @Basic
    @Column(name = "merge_success_flag")
    public Integer getMergeSuccessFlag() {
        return mergeSuccessFlag;
    }

    public void setMergeSuccessFlag(Integer mergeSuccessFlag) {
        this.mergeSuccessFlag = mergeSuccessFlag;
    }

    @Basic
    @Column(name = "delete_flag")
    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @Basic
    @Column(name = "delete_success_flag")
    public Integer getDeleteSuccessFlag() {
        return deleteSuccessFlag;
    }

    public void setDeleteSuccessFlag(Integer deleteSuccessFlag) {
        this.deleteSuccessFlag = deleteSuccessFlag;
    }

    @Basic
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "num_per_times")
    public Integer getNumPerTimes() {
        return numPerTimes;
    }

    public void setNumPerTimes(Integer numPerTimes) {
        this.numPerTimes = numPerTimes;
    }

    @Basic
    @Column(name = "parimary_name")
    public String getParimaryName() {
        return parimaryName;
    }

    public void setParimaryName(String parimaryName) {
        this.parimaryName = parimaryName;
    }

    @Basic
    @Column(name = "sync_flag_name")
    public String getSyncFlagName() {
        return syncFlagName;
    }

    public void setSyncFlagName(String syncFlagName) {
        this.syncFlagName = syncFlagName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SyncRule syncRule = (SyncRule) o;

        if (id != syncRule.id) return false;
        if (mergeSql != null ? !mergeSql.equals(syncRule.mergeSql) : syncRule.mergeSql != null) return false;
        if (deleteSql != null ? !deleteSql.equals(syncRule.deleteSql) : syncRule.deleteSql != null) return false;
        if (tableName != null ? !tableName.equals(syncRule.tableName) : syncRule.tableName != null) return false;
        if (syncTime != null ? !syncTime.equals(syncRule.syncTime) : syncRule.syncTime != null) return false;
        if (mergeFlag != null ? !mergeFlag.equals(syncRule.mergeFlag) : syncRule.mergeFlag != null) return false;
        if (mergeSuccessFlag != null ? !mergeSuccessFlag.equals(syncRule.mergeSuccessFlag) : syncRule.mergeSuccessFlag != null)
            return false;
        if (deleteFlag != null ? !deleteFlag.equals(syncRule.deleteFlag) : syncRule.deleteFlag != null) return false;
        if (deleteSuccessFlag != null ? !deleteSuccessFlag.equals(syncRule.deleteSuccessFlag) : syncRule.deleteSuccessFlag != null)
            return false;
        if (status != null ? !status.equals(syncRule.status) : syncRule.status != null) return false;
        if (numPerTimes != null ? !numPerTimes.equals(syncRule.numPerTimes) : syncRule.numPerTimes != null)
            return false;
        if (parimaryName != null ? !parimaryName.equals(syncRule.parimaryName) : syncRule.parimaryName != null)
            return false;
        if (syncFlagName != null ? !syncFlagName.equals(syncRule.syncFlagName) : syncRule.syncFlagName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (mergeSql != null ? mergeSql.hashCode() : 0);
        result = 31 * result + (deleteSql != null ? deleteSql.hashCode() : 0);
        result = 31 * result + (tableName != null ? tableName.hashCode() : 0);
        result = 31 * result + (syncTime != null ? syncTime.hashCode() : 0);
        result = 31 * result + (mergeFlag != null ? mergeFlag.hashCode() : 0);
        result = 31 * result + (mergeSuccessFlag != null ? mergeSuccessFlag.hashCode() : 0);
        result = 31 * result + (deleteFlag != null ? deleteFlag.hashCode() : 0);
        result = 31 * result + (deleteSuccessFlag != null ? deleteSuccessFlag.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (numPerTimes != null ? numPerTimes.hashCode() : 0);
        result = 31 * result + (parimaryName != null ? parimaryName.hashCode() : 0);
        result = 31 * result + (syncFlagName != null ? syncFlagName.hashCode() : 0);
        return result;
    }
}
