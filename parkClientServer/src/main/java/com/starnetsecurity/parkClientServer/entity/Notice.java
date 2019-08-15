package com.starnetsecurity.parkClientServer.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * Created by 宏炜 on 2017-11-15.
 */
@Entity
public class Notice {
    private String noticeId;
    private String noticeContent;
    private Timestamp noticeShowBeginTime;
    private Timestamp noticeShowEndTime;
    private String operationSource;
    private Timestamp addTime;
    private String noticeType;

    @Id
    @Basic
    @Column(name = "notice_id")
    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    @Basic
    @Column(name = "Notice_content")
    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    @Basic
    @Column(name = "Notice_show_begin_time")
    public Timestamp getNoticeShowBeginTime() {
        return noticeShowBeginTime;
    }

    public void setNoticeShowBeginTime(Timestamp noticeShowBeginTime) {
        this.noticeShowBeginTime = noticeShowBeginTime;
    }

    @Basic
    @Column(name = "Notice_show_end_time")
    public Timestamp getNoticeShowEndTime() {
        return noticeShowEndTime;
    }

    public void setNoticeShowEndTime(Timestamp noticeShowEndTime) {
        this.noticeShowEndTime = noticeShowEndTime;
    }

    @Basic
    @Column(name = "operation_source")
    public String getOperationSource() {
        return operationSource;
    }

    public void setOperationSource(String operationSource) {
        this.operationSource = operationSource;
    }

    @Basic
    @Column(name = "add_time")
    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    @Basic
    @Column(name = "notice_type")
    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Notice notice = (Notice) o;

        if (noticeId != null ? !noticeId.equals(notice.noticeId) : notice.noticeId != null) return false;
        if (noticeContent != null ? !noticeContent.equals(notice.noticeContent) : notice.noticeContent != null)
            return false;
        if (noticeShowBeginTime != null ? !noticeShowBeginTime.equals(notice.noticeShowBeginTime) : notice.noticeShowBeginTime != null)
            return false;
        if (noticeShowEndTime != null ? !noticeShowEndTime.equals(notice.noticeShowEndTime) : notice.noticeShowEndTime != null)
            return false;
        if (operationSource != null ? !operationSource.equals(notice.operationSource) : notice.operationSource != null)
            return false;
        if (addTime != null ? !addTime.equals(notice.addTime) : notice.addTime != null) return false;
        if (noticeType != null ? !noticeType.equals(notice.noticeType) : notice.noticeType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = noticeId != null ? noticeId.hashCode() : 0;
        result = 31 * result + (noticeContent != null ? noticeContent.hashCode() : 0);
        result = 31 * result + (noticeShowBeginTime != null ? noticeShowBeginTime.hashCode() : 0);
        result = 31 * result + (noticeShowEndTime != null ? noticeShowEndTime.hashCode() : 0);
        result = 31 * result + (operationSource != null ? operationSource.hashCode() : 0);
        result = 31 * result + (addTime != null ? addTime.hashCode() : 0);
        result = 31 * result + (noticeType != null ? noticeType.hashCode() : 0);
        return result;
    }
}
