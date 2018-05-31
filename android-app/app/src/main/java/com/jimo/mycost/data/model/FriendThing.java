package com.jimo.mycost.data.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.Date;

@Table(name = "friend_thing")
public class FriendThing {
    @Column(name = "id", isId = true, property = "NOT NULL")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "date")
    private String date;
    @Column(name = "thing")
    private String thing;
    @Column(name = "record_time")
    private Date recorTime;
    @Column(name = "user_name")
    private String userName;

    public FriendThing() {
    }

    public FriendThing(String name, String date, String thing) {
        this.name = name;
        this.date = date;
        this.thing = thing;
        this.recorTime = new Date();
        this.userName = "jimo";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getThing() {
        return thing;
    }

    public void setThing(String thing) {
        this.thing = thing;
    }

    public Date getRecorTime() {
        return recorTime;
    }

    public void setRecorTime(Date recorTime) {
        this.recorTime = recorTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
