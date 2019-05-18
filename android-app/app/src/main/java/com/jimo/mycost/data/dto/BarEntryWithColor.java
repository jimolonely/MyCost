package com.jimo.mycost.data.dto;

import com.github.mikephil.charting.data.BarEntry;

/**
 * 组合model
 */
public class BarEntryWithColor {
    private BarEntry barEntry;
    private int color;
    private String xVal;

    public BarEntryWithColor() {
    }

    public BarEntryWithColor(BarEntry barEntry, int color, String xVal) {
        this.barEntry = barEntry;
        this.color = color;
        this.xVal = xVal;
    }

    public String getxVal() {
        return xVal;
    }

    public void setxVal(String xVal) {
        this.xVal = xVal;
    }

    public BarEntry getBarEntry() {
        return barEntry;
    }

    public void setBarEntry(BarEntry barEntry) {
        this.barEntry = barEntry;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
