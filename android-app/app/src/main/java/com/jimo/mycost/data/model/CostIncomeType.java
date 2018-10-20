package com.jimo.mycost.data.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 开销和收入的类型，开销分4大类，收入目前只有一大类，然后大类李里还有小类
 *
 * @author jimo
 * @date 18-10-20 上午8:19
 */
@Table(name = "cost_income_type")
public class CostIncomeType {
    @Column(name = "id", isId = true, property = "NOT NULL")
    private long id;

    /**
     * 大类，比如开销里的餐饮、交通
     */
    @Column(name = "big_type")
    private String bigType;

    /**
     * 小类，比如交通里的地铁，公交
     */
    @Column(name = "small_type")
    private String smallType;

    /**
     * 这个type是区分开销还是收入的，0代表开销，1代表收入
     */
    @Column(name = "type")
    private int type;

    public static final int TYPE_COST = 0;
    public static final int TYPE_INCOME = 1;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "sync_type", property = "default 1")
    private int syncType;//0代表已同步，1,2,3代表增删改

    public CostIncomeType() {
    }

    public CostIncomeType(String bigType, String smallType, int type, String createTime, int syncType) {
        this.bigType = bigType;
        this.smallType = smallType;
        this.type = type;
        this.createTime = createTime;
        this.syncType = syncType;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static int getTypeCost() {
        return TYPE_COST;
    }

    public static int getTypeIncome() {
        return TYPE_INCOME;
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
