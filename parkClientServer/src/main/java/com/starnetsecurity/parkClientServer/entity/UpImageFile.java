package com.starnetsecurity.parkClientServer.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2017-11-15.
 */
@Entity
@Table(name = "up_image_file", schema = "vams", catalog = "")
public class UpImageFile {
    private long id;
    private String upFileName;
    private String filePath;
    private Byte isUpload;
    private Timestamp createTime;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "up_file_name")
    public String getUpFileName() {
        return upFileName;
    }

    public void setUpFileName(String upFileName) {
        this.upFileName = upFileName;
    }

    @Basic
    @Column(name = "file_path")
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Basic
    @Column(name = "is_upload")
    public Byte getIsUpload() {
        return isUpload;
    }

    public void setIsUpload(Byte isUpload) {
        this.isUpload = isUpload;
    }

    @Basic
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UpImageFile that = (UpImageFile) o;

        if (id != that.id) return false;
        if (upFileName != null ? !upFileName.equals(that.upFileName) : that.upFileName != null) return false;
        if (filePath != null ? !filePath.equals(that.filePath) : that.filePath != null) return false;
        if (isUpload != null ? !isUpload.equals(that.isUpload) : that.isUpload != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (upFileName != null ? upFileName.hashCode() : 0);
        result = 31 * result + (filePath != null ? filePath.hashCode() : 0);
        result = 31 * result + (isUpload != null ? isUpload.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        return result;
    }
}
