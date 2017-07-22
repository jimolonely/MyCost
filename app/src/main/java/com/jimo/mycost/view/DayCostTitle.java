package com.jimo.mycost.view;

import java.util.List;

/**
 * Created by root on 17-7-22.
 */

public class DayCostTitle {
    private String date;
    private List<DayCostItem> items;

    public DayCostTitle() {
    }

    public DayCostTitle(String date, List<DayCostItem> items) {
        this.date = date;
        this.items = items;
    }

    public List<DayCostItem> getItems() {
        return items;
    }

    public void setItems(List<DayCostItem> items) {
        this.items = items;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
