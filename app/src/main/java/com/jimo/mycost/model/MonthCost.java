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


}
