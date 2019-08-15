package com.starnetsecurity.parkClientServer.entity;

import javax.persistence.*;

/**
 * Created by 宏炜 on 2017-11-15.
 */
@Entity
@Table(name = "cn_db_addtablepartition", schema = "vams", catalog = "")
public class CnDbAddtablepartition {
    private String addtablepartitionId;
    private String tableSchema;
    private String tableName;
    private String splitparkeyDatatype;
    private int splitparRange;
    private byte splistparNum;

    @Id
    @Column(name = "addtablepartition_id")
    public String getAddtablepartitionId() {
        return addtablepartitionId;
    }

    public void setAddtablepartitionId(String addtablepartitionId) {
        this.addtablepartitionId = addtablepartitionId;
    }

    @Basic
    @Column(name = "table_schema")
    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
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
    @Column(name = "splitparkey_datatype")
    public String getSplitparkeyDatatype() {
        return splitparkeyDatatype;
    }

    public void setSplitparkeyDatatype(String splitparkeyDatatype) {
        this.splitparkeyDatatype = splitparkeyDatatype;
    }

    @Basic
    @Column(name = "splitpar_range")
    public int getSplitparRange() {
        return splitparRange;
    }

    public void setSplitparRange(int splitparRange) {
        this.splitparRange = splitparRange;
    }

    @Basic
    @Column(name = "splistpar_num")
    public byte getSplistparNum() {
        return splistparNum;
    }

    public void setSplistparNum(byte splistparNum) {
        this.splistparNum = splistparNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CnDbAddtablepartition that = (CnDbAddtablepartition) o;

        if (splitparRange != that.splitparRange) return false;
        if (splistparNum != that.splistparNum) return false;
        if (addtablepartitionId != null ? !addtablepartitionId.equals(that.addtablepartitionId) : that.addtablepartitionId != null)
            return false;
        if (tableSchema != null ? !tableSchema.equals(that.tableSchema) : that.tableSchema != null) return false;
        if (tableName != null ? !tableName.equals(that.tableName) : that.tableName != null) return false;
        if (splitparkeyDatatype != null ? !splitparkeyDatatype.equals(that.splitparkeyDatatype) : that.splitparkeyDatatype != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = addtablepartitionId != null ? addtablepartitionId.hashCode() : 0;
        result = 31 * result + (tableSchema != null ? tableSchema.hashCode() : 0);
        result = 31 * result + (tableName != null ? tableName.hashCode() : 0);
        result = 31 * result + (splitparkeyDatatype != null ? splitparkeyDatatype.hashCode() : 0);
        result = 31 * result + splitparRange;
        result = 31 * result + (int) splistparNum;
        return result;
    }
}
