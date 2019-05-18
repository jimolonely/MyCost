package com.jimo.mycost.data.dto;

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class BarEntryColorList {
    private List<BarEntry> barEntries;
    private List<String> xVals;
    private int[] colors;

    public BarEntryColorList(List<BarEntryWithColor> list) {
        List<BarEntryWithColor> barEntryWithColors = list;
        sortByBar(barEntryWithColors);
        barEntries = new ArrayList<>();
        xVals = new ArrayList<>();
        colors = new int[barEntryWithColors.size()];
        int i = 0;
        for (BarEntryWithColor barEntryWithColor : barEntryWithColors) {
            BarEntry barEntry = barEntryWithColor.getBarEntry();
            // 因为虽然按y值排序了，但x没变，还是会按x轴的顺序显示，所以需要重写x值
            barEntry.setX(i);
            barEntries.add(barEntry);
            xVals.add(barEntryWithColor.getxVal());
            colors[i++] = barEntryWithColor.getColor();
        }
    }

    /**
     * 降序排列
     */
    private void sortByBar(List<BarEntryWithColor> barEntries) {
        barEntries.sort((b1, b2) -> (int) (b2.getBarEntry().getY() - b1.getBarEntry().getY()));
    }

    public List<BarEntry> getBarEntries() {
        return barEntries;
    }

    public List<String> getxVals() {
        return xVals;
    }

    public int[] getColors() {
        return colors;
    }
}
