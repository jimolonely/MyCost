package com.jimo.mycost.data.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.Date;

/**
 * 存储图片在系统中的位置
 */
@Table(name = "image_record")
public class ImageRecord {

    @Column(name = "id", isId = true, property = "NOT NULL")
    private long id;
    @Column(name = "parent_id")
    private long parentId;//多对一的关系
    @Column(name = "type")
    private String type;//类型,记账的,娱乐生活的等等
    @Column(name = "img_path")
    private String imgPath;

    @Column(name = "record_time")
    private Date recordTime;//存储时间
    @Column(name = "user_name")
    private String userName;

    public ImageRecord(long parentId, String type, String imgPath) {
        this.parentId = parentId;
        this.type = type;
        this.imgPath = imgPath;
        this.recordTime = new Date();
        this.userName = "jimo";
    }

    public ImageRecord() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
