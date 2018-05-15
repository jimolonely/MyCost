package com.jimo.mycost.adapter;

/**
 * Created by jimo on 17-11-25.
 */

public class ItemDayTime {
    private String subject;
    private String time;

    public ItemDayTime(String subject, String time) {
        this.subject = subject;
        this.time = time;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
