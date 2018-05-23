package com.jimo.mycost.adapter;

/**
 * Created by root on 17-7-22.
 */

public class ItemDayCost {
    private String date;
    private int itemType;
    private String type;//cost type
    private String money;
    private String remark;
    private Long id;

    private RecyclerViewTempImgAdapter adapter;

    public ItemDayCost(String date, int itemType, String type, String money, String remark, Long id, RecyclerViewTempImgAdapter adapter) {
        this.date = date;
        this.itemType = itemType;
        this.type = type;
        this.money = money;
        this.remark = remark;
        this.id = id;
        this.adapter = adapter;
    }

    public ItemDayCost(String date, int itemType) {
        this.date = date;
        this.itemType = itemType;
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

    public RecyclerViewTempImgAdapter getAdapter() {
        return adapter;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setType(String type) {
        this.type = type;
    }
}
