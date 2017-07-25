package com.jimo.mycost.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by root on 17-7-20.
 * 按月记录的冗余表
 */
@Table(name = "month_cost")
public class MonthCost {
    @Column(name = "id", isId = true, autoGen = true, property = "NOT NULL")
    private int id;

    @Column(name = "year")
    private int year;

    @Column(name = "month")
    private int month;

    @Column(name = "money")
    private Float money;

    @Column(name = "in_out")
    private Integer inOut;//是收入还是支出，0为支出

    @Column(name = "sync_type")
    private int syncType;//0代表已同步，1,2,3代表增删改

    @Column(name = "user_name")
    private String userName;

    public MonthCost() {
    }

    public MonthCost(int year, int month, Float money, Integer inOut, int syncType, String userName) {
        this.year = year;
        this.month = month;
        this.money = money;
        this.inOut = inOut;
        this.syncType = syncType;
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public Integer getInOut() {
        return inOut;
    }

    public void setInOut(Integer inOut) {
        this.inOut = inOut;
    }

    public int getSyncType() {
        return syncType;
    }

    public void setSyncType(int syncType) {
        this.syncType = syncType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
