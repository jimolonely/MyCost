package com.jimo.mycost.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by root on 17-7-20.
 * 单条支出收入记录
 */
@Table(name = "cost_income_record", onCreated = "")
public class CostInComeRecord {
    @Column(name = "id", isId = true, autoGen = true, property = "NOT NULL")
    private int id;
    @Column(name = "in_out")
    private Integer inOut;//是收入还是支出，0为支出
    @Column(name = "money")
    private Float money;
    @Column(name = "remark")
    private String remark;
    @Column(name = "c_date")
    private String date;
    @Column(name = "type_name")
    private String typeName;//小类名
    @Column(name = "user_name")
    private String userName;

    public CostInComeRecord() {
    }

    public CostInComeRecord(Integer inOut, Float money, String remark, String date, String typeName, String userName) {
        this.inOut = inOut;
        this.money = money;
        this.remark = remark;
        this.date = date;
        this.typeName = typeName;
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public Integer getInOut() {
        return inOut;
    }

    public Float getMoney() {
        return money;
    }

    public String getRemark() {
        return remark;
    }

    public String getDate() {
        return date;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getUserName() {
        return userName;
    }
}
