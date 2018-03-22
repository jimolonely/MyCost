package com.jimo.mycost.model;

/**
 * Created by jimo on 18-3-22.
 * 封装数据
 */

public class RangeDate {
    private String beginDate;
    private String endDate;

    public RangeDate(String beginDate, String endDate) {
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return "RangeDate{" +
                "beginDate='" + beginDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
