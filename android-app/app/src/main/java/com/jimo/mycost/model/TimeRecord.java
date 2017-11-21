package com.jimo.mycost.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by jimo on 17-11-21.
 * 记录时间的表，与主题是多对一关系
 */
@Table(name = "time_record")
public class TimeRecord {
    @Column(name = "id", isId = true, autoGen = true, property = "NOT NULL")
    private long id;
    @Column(name = "subject_name")
    private String subjectName;
    @Column(name = "time_len")
    private String timeLen;
    @Column(name = "start_time")
    private String startTime;
    @Column(name = "end_time")
    private String endTime;

    public TimeRecord() {
    }

    public TimeRecord(String subjectName, String timeLen, String startTime, String endTime) {
        this.subjectName = subjectName;
        this.timeLen = timeLen;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTimeLen() {
        return timeLen;
    }

    public void setTimeLen(String timeLen) {
        this.timeLen = timeLen;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
