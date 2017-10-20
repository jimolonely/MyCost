package com.jimo.mycost.view;

/**
 * Created by root on 17-7-22.
 */

public class DayCostItem {
    private String date;
    private int itemType;
    private String type;//cost type
    private String money;
    private Long id;

    public DayCostItem(String date, int itemType, String type, String money, Long id) {
        this.date = date;
        this.itemType = itemType;
        this.type = type;
        this.money = money;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getType() {

        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
