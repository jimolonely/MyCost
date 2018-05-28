package com.jimo.mycost.func.life;

public class LifeItemSearchResult {
    private String imgUrl;
    private String title;
    private String type;//对应主题的类型,比如电影有科幻,爱情等,书籍有文学,科学等
    private String creators;//书的作者或电影导演
    private String pubdate;//上映时间
    private String rating;//豆瓣评分 7.5/10
    private String remark;//其他备注,书籍的页数,出版社,梗概,电影的演员

    public LifeItemSearchResult(String imgUrl, String title, String type, String creators,
                                String pubdate, String remark, String rating) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.type = type;
        this.creators = creators;
        this.pubdate = pubdate;
        this.remark = remark;
        this.rating = rating;
    }

    public String getType() {
        return type;
    }

    public String getCreators() {
        return creators;
    }

    public String getPubdate() {
        return pubdate;
    }

    public String getRemark() {
        return remark;
    }

    public String getRating() {
        return rating;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "LifeItemSearchResult{" +
                "imgUrl='" + imgUrl + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", creators='" + creators + '\'' +
                ", pubdate='" + pubdate + '\'' +
                ", rating='" + rating + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
