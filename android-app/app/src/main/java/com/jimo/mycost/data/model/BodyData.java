package com.jimo.mycost.data.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by jimo on 18-2-27.
 * 身体数据
 */
@Table(name = "body_data")
public class BodyData {
    @Column(name = "id", isId = true, property = "NOT NULL")
    private long id;
    @Column(name = "body_part")
    private String bodyPart;
    @Column(name = "value")
    private Float value;
    @Column(name = "date")
    private String date;
    @Column(name = "unit")
    private String unit;
    @Column(name = "sync_type")
    private int syncType;//0代表已同步，1,2,3代表增删改
    @Column(name = "user_name")
    private String userName;

    public BodyData() {
    }

    public BodyData(String bodyPart, Float value, String date, String unit) {
        this.bodyPart = bodyPart;
        this.value = value;
        this.date = date;
        this.unit = unit;
        this.syncType = 1;
        this.userName = "jimo";
    }

    public long getId() {
        return id;
    }

    public String getBodyPart() {
        return bodyPart;
    }

    public Float getValue() {
        return value;
    }

    public String getDate() {
        return date;
    }

    public int getSyncType() {
        return syncType;
    }

    public String getUserName() {
        return userName;
    }
}
