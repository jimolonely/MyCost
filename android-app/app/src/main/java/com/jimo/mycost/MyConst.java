package com.jimo.mycost;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by root on 17-7-22.
 */

public class MyConst {
    public static final int SYNC_TYPE_SYNCED = 0;
    public static final int SYNC_TYPE_INSERT = 1;
    public static final int SYNC_TYPE_DEL = 2;
    public static final int SYNC_TYPE_UPDATE = 3;

    public static final int IN_COME = 1;
    public static final int COST = 0;

    public static final int ITEM_TYPE1 = 0;
    public static final int ITEM_TYPE2 = 1;

    public static final String UPLOAD_URL = "http://mycost.ngrok.cc/upload";

    public static String[] bodyData = {"体重", "胸围", "腰围"};
    public static String[] bodyDataUnit = {"kg", "cm", "cm"};//数据的单位


    //获取用户名
    public static String getUserName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", "jimo");
    }
}
