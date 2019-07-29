package com.jimo.mycost;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

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

    public static final int ITEM_TYPE_TITLE = 0;
    public static final int ITEM_TYPE_WITH_PHOTO = 1;
    public static final int ITEM_TYPE_NO_PHOTO = 2;//没有图片

    public static final String UPLOAD_URL = "http://mycost.ngrok.cc/upload";

    public static String[] bodyData = {"体重", "胸围", "腰围"};
    public static String[] bodyDataUnit = {"kg", "cm", "cm"};//数据的单位

    public static String DOUBAN_BOOK_API = "https://api.douban.com/v2/book/search";
    public static String DOUBAN_KEY = "0df993c66c0c636e29ecbb5344252a4a";
    public static String DOUBAN_MOVIE_API = "https://api.douban.com/v2/movie/search";

    public static String[] themeData = {"电影", "书籍"};

    public static final String IMG_TYPE_COST = "cost";
    public static final String IMG_TYPE_INCOME = "income";
    public static final String IMG_TYPE_LIFE = "life";
    public static final String IMG_TYPE_FRIEND_THING = "friend_thing";


    public static final String IMG_SAVE_PATH = "MyCost/images";

    //获取用户名
    public static String getUserName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", "jimo");
    }

    /**
     * 音频记录的key
     */
    public static final String RECORD_PATH_KEY = "record_path_key";

    /**
     * 上传数据库到坚果云
     */
    private static final String CLOUD_DAV_PATH = "https://dav.jianguoyun.com/dav/";
    public static final String CLOUD_DB_PATH = CLOUD_DAV_PATH + "mycost-db";

    public static String getCloudUserName(Context context) {
        SharedPreferences pref = context.getSharedPreferences("cloud-user", Context.MODE_PRIVATE);
        return pref.getString("username", null);
    }

    public static String getCloudUserPass(Context context) {
        SharedPreferences pref = context.getSharedPreferences("cloud-user", Context.MODE_PRIVATE);
        return pref.getString("password", null);
    }

}
