package com.jimo.mycost.data.model;

import com.jimo.mycost.MyConst;
import com.jimo.mycost.util.JimoUtil;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 反思记录
 */
@Table(name = "reflection_record")
public class ReflectionRecord {
    @Column(name = "id", isId = true, property = "NOT NULL")
    private long id;
    @Column(name = "big_type")
    private String bigType;
    @Column(name = "small_type")
    private String smallType;
    @Column(name = "day")
    private String day;// 事件发生的日期
    @Column(name = "content")
    private String content; // 事件内容
    @Column(name = "people")
    private String people; // 人物
    @Column(name = "address")
    private String address; // 地点
    @Column(name = "user_name")
    private String userName;
    @Column(name = "update_time")
    private String updateTime;
    @Column(name = "sync_type")
    private int syncType;//0代表已同步，1,2,3代表增删改

    public ReflectionRecord() {
    }

    public ReflectionRecord(String bigType, String smallType, String day,
                            String content, String people, String address) {
        this.bigType = bigType;
        this.smallType = smallType;
        this.day = day;
        this.content = content;
        this.people = people;
        this.address = address;
        this.syncType = MyConst.SYNC_TYPE_INSERT;
        this.userName = "jimo";
        this.updateTime = JimoUtil.getDateTimeNow();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getSyncType() {
        return syncType;
    }

    public void setSyncType(int syncType) {
        this.syncType = syncType;
    }
}
