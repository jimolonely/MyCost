package com.jimo.mycost.data.model;

import com.jimo.mycost.MyConst;
import com.jimo.mycost.util.JimoUtil;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 单条时间开销记录
 */
@Table(name = "time_cost_record")
public class TimeCostRecord {
    @Column(name = "id", isId = true, property = "NOT NULL")
    private long id;
    @Column(name = "start")
    private String start;
    @Column(name = "end")
    private String end;
    @Column(name = "day")
    private String day;
    @Column(name = "big_type")
    private String bigType;
    @Column(name = "small_type")
    private String smallType;
    @Column(name = "remark")
    private String remark; // 备注
    @Column(name = "user_name")
    private String userName;
    @Column(name = "update_time")
    private String updateTime;
    @Column(name = "sync_type")
    private int syncType;//0代表已同步，1,2,3代表增删改

    public TimeCostRecord() {
    }

    public TimeCostRecord(String start, String end, String day,
                          String bigType, String smallType, String remark) {
        this.start = start;
        this.end = end;
        this.day = day;
        this.bigType = bigType;
        this.smallType = smallType;
        this.remark = remark;
        this.syncType = MyConst.SYNC_TYPE_INSERT;
        this.userName = "jimo";
        this.updateTime = JimoUtil.getDateTimeNow();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getBigType() {
        return bigType;
    }

    public void setBigType(String bigType) {
        this.bigType = bigType;
    }

    public String getSmallType() {
        return smallType;
    }

    public void setSmallType(String smallType) {
        this.smallType = smallType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getSyncType() {
        return syncType;
    }

    public void setSyncType(int syncType) {
        this.syncType = syncType;
    }
}
