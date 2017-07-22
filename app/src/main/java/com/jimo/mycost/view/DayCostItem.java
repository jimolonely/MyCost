package com.jimo.mycost.view;

/**
 * Created by root on 17-7-22.
 */

public class DayCostItem {
    private String type;
    private String money;

    public DayCostItem(String type, String money) {
        this.type = type;
        this.money = money;
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
