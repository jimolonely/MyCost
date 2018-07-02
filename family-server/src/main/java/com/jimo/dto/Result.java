package com.jimo.dto;

/**
 * Created by jimo on 18-7-2.
 */
public class Result {
    private boolean ok;
    private String msg;
    private Object data;

    public Result() {
        this(true, "", null);
    }

    public Result(Object data) {
        this(true, "", data);
    }

    public Result(boolean ok, String msg, Object data) {
        this.ok = ok;
        this.msg = msg;
        this.data = data;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
