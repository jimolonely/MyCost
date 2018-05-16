package com.jimo.mycost.adapter;

public class ItemLifeSearchResult {
    private String imgUrl;
    private String title;

    public ItemLifeSearchResult(String imgUrl, String title) {
        this.imgUrl = imgUrl;
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "ItemLifeSearchResult{" +
                "imgUrl='" + imgUrl + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
