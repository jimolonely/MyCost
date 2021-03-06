package com.jimo.mycost;

import android.app.Application;
import android.util.Log;

import org.xutils.DbManager;
import org.xutils.x;

/**
 * Created by root on 17-7-19.
 */

public class MyApp extends Application {

    static DbManager.DaoConfig daoConfig;
    public static DbManager dbManager;

    @Override
    public void onCreate() {
        super.onCreate();
        /*ActivityManager manager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        manager.getRunningTasks(1);
        ActivityCompat.requestPermissions(, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        }, 0);*/

        x.Ext.init(this);
        x.Ext.setDebug(false); //输出debug日志，开启会影响性能
        daoConfig = new DbManager.DaoConfig()
                .setDbName("mycost.db")
                // 不设置dbDir时, 默认存储在app的私有目录. /data/user/0/com.jimo.mycost/databases
//                .setDbDir(new File("/mnt/sdcard"))
//                .setDbDir(new File("/storage/emulated/Android/data/com.jimo.mycost/files"))
                .setDbVersion(5)
                .setDbOpenListener(db -> {
                    // 开启WAL, 对写入加速提升巨大
                    db.getDatabase().enableWriteAheadLogging();
                })
                .setDbUpgradeListener((db, oldVersion, newVersion) -> {
                    /*try {
                        db.addColumn(TimeCostRecord.class, "remark");
                    } catch (DbException e) {
                        e.printStackTrace();
                    }*/
                    // db.addColumn(...);
                    // db.dropTable(...);
                    // ...
                    // or
                    // db.dropDb();
                }).setTableCreateListener((db, table) -> Log.i("创建表", table.getName()));
        dbManager = x.getDb(daoConfig);
    }


}
