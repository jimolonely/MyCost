package com.jimo.mycost.data.model;

import com.jimo.mycost.MyConst;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 时间大类的颜色
 *
 * @author jimo
 * @date 19-06-19 20:37
 */
@Table(name = "time_color")
public class TimeColor {
    /**
     * 大类
     */
    @Column(name = "big_type", isId = true)
    private String bigType;

    @Column(name = "color")
    private String color;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "sync_type", property = "default 1")
    private int syncType;//0代表已同步，1,2,3代表增删改

    public TimeColor() {
    }

    public TimeColor(String bigType, String color, String createTime) {
        this.bigType = bigType;
        this.color = color;
        this.createTime = createTime;
        this.syncType = MyConst.SYNC_TYPE_INSERT;
    }

    public String getBigType() {
        return bigType;
    }

    public void setBigType(String bigType) {
        this.bigType = bigType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getSyncType() {
        return syncType;
    }

    public void setSyncType(int syncType) {
        this.syncType = syncType;
    }
}
