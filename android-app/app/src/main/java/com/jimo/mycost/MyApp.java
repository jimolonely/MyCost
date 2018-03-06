package com.jimo.mycost;

import android.app.Application;
import android.util.Log;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.x;

import java.io.File;

/**
 * Created by root on 17-7-19.
 */

public class MyApp extends Application {

    static DbManager.DaoConfig daoConfig;
    public static DbManager dbManager;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(false); //输出debug日志，开启会影响性能
        daoConfig = new DbManager.DaoConfig()
                .setDbName("mycost.db")
                // 不设置dbDir时, 默认存储在app的私有目录.
//                .setDbDir(new File("/mnt/sdcard"))
                .setDbVersion(2)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        // 开启WAL, 对写入加速提升巨大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        // TODO: ...
                        // db.addColumn(...);
                        // db.dropTable(...);
                        // ...
                        // or
                        // db.dropDb();
                    }
                }).setTableCreateListener(new DbManager.TableCreateListener() {
                    @Override
                    public void onTableCreated(DbManager db, TableEntity<?> table) {
                        Log.i("创建表", table.getName());
                    }
                });
        dbManager = x.getDb(daoConfig);
    }


}
