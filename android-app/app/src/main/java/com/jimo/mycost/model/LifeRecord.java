package com.jimo.mycost.model;

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
    private int score;//我的评分
    @Column(name = "comment")
    private String comment;//评论
    @Column(name = "mood")
    private String mood;//看完的心情
    @Column(name = "spend_time")
    private int spendTime;//花的时间,转成秒
    @Column(name = "time")
    private String time;//看的时间
    @Column(name = "record_time")
    private Date recordTime;//存储时间
    @Column(name = "user_name")
    private String userName;

    public LifeRecord(String name, String theme, String type, int score,
                      String comment, String mood, int spendTime, String time) {
        this.name = name;
        this.theme = theme;
        this.type = type;
        this.score = score;
        this.comment = comment;
        this.mood = mood;
        this.spendTime = spendTime;
        this.time = time;
        this.recordTime = new Date();
        this.userName = "jimo";
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

    public int getScore() {
        return score;
    }

    public String getComment() {
        return comment;
    }

    public String getMood() {
        return mood;
    }

    public int getSpendTime() {
        return spendTime;
    }

    public String getTime() {
        return time;
    }

    public Date getRecordTime() {
        return recordTime;
    }
}
