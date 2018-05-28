package com.jimo.mycost.data.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by jimo on 17-11-21.
 * 主题表
 */
@Table(name = "time_subject")
public class Subject {
    @Column(name = "id", isId = true, autoGen = true, property = "NOT NULL")
    private int id;
    @Column(name = "subject_name")
    private String SubjectName;
    @Column(name = "end_time")
    private String endTime;
    @Column(name = "sync_type", property = "default 1")
    private int syncType;//0代表已同步，1,2,3代表增删改

    public Subject() {
    }

    public Subject(String subjectName, String endTime, int syncType) {
        SubjectName = subjectName;
        this.endTime = endTime;
        this.syncType = syncType;
    }

    public int getSyncType() {
        return syncType;
    }

    public void setSyncType(int syncType) {
        this.syncType = syncType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubjectName() {
        return SubjectName;
    }

    public void setSubjectName(String subjectName) {
        SubjectName = subjectName;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
