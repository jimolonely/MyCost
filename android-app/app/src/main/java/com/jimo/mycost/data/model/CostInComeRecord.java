package com.jimo.mycost.data.model;

import android.os.Environment;

import com.jimo.mycost.func.cost.RecyclerViewTempImgItem;

import org.xutils.DbManager;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 17-7-20.
 * 单条支出收入记录
 */
@Table(name = "cost_income_record")
public class CostInComeRecord {
    @Column(name = "id", isId = true, property = "NOT NULL")
    private long id;
    @Column(name = "in_out")
    private Integer inOut;//是收入还是支出，0为支出
    @Column(name = "money")
    private Float money;
    @Column(name = "remark")
    private String remark;
    @Column(name = "c_date")
    private String date;
    @Column(name = "type_name")
    private String typeName;//小类名
    @Column(name = "user_name")
    private String userName;
    @Column(name = "sync_type")
    private int syncType;//0代表已同步，1,2,3代表增删改

    public List<RecyclerViewTempImgItem> getImagePaths(DbManager db) throws DbException {
        final List<ImageRecord> records = db.selector(ImageRecord.class).where("parent_id", "=", this.id).findAll();
        List<RecyclerViewTempImgItem> items = new ArrayList<>();
        for (ImageRecord i : records) {
            final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + i.getImgPath();
            items.add(new RecyclerViewTempImgItem(path));
        }
        return items;
    }

    public CostInComeRecord() {
    }

    public CostInComeRecord(Integer inOut, Float money,
                            String remark, String date, String typeName, String userName, int syncType) {
        this.inOut = inOut;
        this.money = money;
        this.remark = remark;
        this.date = date;
        this.typeName = typeName;
        this.userName = userName;
        this.syncType = syncType;
    }

    public long getId() {
        return id;
    }

    public Integer getInOut() {
        return inOut;
    }

    public Float getMoney() {
        return money;
    }

    public String getRemark() {
        return remark;
    }

    public String getDate() {
        return date;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getUserName() {
        return userName;
    }

    public int getSyncType() {
        return syncType;
    }
}
