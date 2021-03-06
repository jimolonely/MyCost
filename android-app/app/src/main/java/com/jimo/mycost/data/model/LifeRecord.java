package com.jimo.mycost.data.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.Date;

@Table(name = "life_record")
public class LifeRecord {
    @Column(name = "id", isId = true, property = "NOT NULL")
    private long id;
    @Column(name = "name") //名字
    private String name;
    @Column(name = "theme")
    private String theme;//主题类型,比如电影,书籍
    @Column(name = "type")
    private String type;//对应主题的类型,比如电影有科幻,爱情等,书籍有文学,科学等
    @Column(name = "creators")
    private String creators;//书的作者或电影导演
    @Column(name = "pubdate")
    private String pubdate;//上映时间
    @Column(name = "remark")
    private String remark;//其他备注,书籍的页数,出版社,梗概,电影的演员
    @Column(name = "rating")
    private String rating;//豆瓣评分 7.5/10

    @Column(name = "score")
    private float score;//我的评分
    @Column(name = "comment")
    private String comment;//评论
    @Column(name = "mood")
    private String mood;//看完的心情
    @Column(name = "spend_time")
    private String spendTime;//花的时间

    @Column(name = "spend_hour")
    private double spendHour;

    // 开始看的时间
    @Column(name = "start_time")
    private String startTime;
    // 看完的时间
    @Column(name = "end_time")
    private String endTime;

    @Column(name = "time")
    private String time;//看的时间
    @Column(name = "record_time")
    private Date recordTime;//存储时间
    @Column(name = "user_name")
    private String userName;

    /**
     * 给电影的
     */
/*    public LifeRecord(String name, String theme, String type, String creators, String pubdate,
                      String remark, String rating, float score, String comment,
                      String mood, String spendTime, String time) {
        this.name = name;
        this.theme = theme;
        this.type = type;
        this.creators = creators;
        this.pubdate = pubdate;
        this.remark = remark;
        this.rating = rating;
        this.score = score;
        this.comment = comment;
        this.mood = mood;
        this.spendTime = spendTime;
        this.time = time;
        this.recordTime = new Date();
        this.userName = "jimo";
    }*/
    public LifeRecord(String name, String theme, String type, String creators, String pubdate,
                      String remark, String rating, float score, String comment,
                      String mood, double spendHour, String time) {
        this.name = name;
        this.theme = theme;
        this.type = type;
        this.creators = creators;
        this.pubdate = pubdate;
        this.remark = remark;
        this.rating = rating;
        this.score = score;
        this.comment = comment;
        this.mood = mood;
        this.spendHour = spendHour;
        this.startTime = time;
        this.time = time;
        this.recordTime = new Date();
        this.userName = "jimo";
    }

    public LifeRecord() {
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTheme() {
        return theme;
    }

    public String getType() {
        return type;
    }

    public float getScore() {
        return score;
    }

    public String getComment() {
        return comment;
    }

    public String getMood() {
        return mood;
    }

    public String getSpendTime() {
        return spendTime;
    }

    public String getTime() {
        return time;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public double getSpendHour() {
        return spendHour;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
