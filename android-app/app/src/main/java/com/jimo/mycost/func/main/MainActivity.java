package com.jimo.mycost.func.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import com.jimo.mycost.MyApp;
import com.jimo.mycost.MyConst;
import com.jimo.mycost.R;
import com.jimo.mycost.data.dto.CloudFileEntry;
import com.jimo.mycost.data.model.CostInComeRecord;
import com.jimo.mycost.data.model.MonthCost;
import com.jimo.mycost.func.cloud.AddUserInfoDialog;
import com.jimo.mycost.func.cloud.CloudFileListDialog;
import com.jimo.mycost.func.cost.CostAddActivity;
import com.jimo.mycost.func.record.TimeCostActivity;
import com.jimo.mycost.util.JimoUtil;
import com.jimo.mycost.view.MyCostView;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@ContentView(R.layout.activity_main)
public class MainActivity extends Activity {

    @ViewInject(R.id.tv_month_total)
    TextView tv_total;
    @ViewInject(R.id.tv_month_cost)
    TextView tv_cost;
    @ViewInject(R.id.tv_month_income)
    TextView tv_income;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 申请权限
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        }, 0);
        x.view().inject(this);
        initViews();
    }

    /**
     *
     */
    private void initViews() {
        refresh();
    }

    /**
     * 从数据库查出记录存在List里
     */
    private void queryData() {
        DbManager db = MyApp.dbManager;
        //查询月记录
        int month = getMonth();
        int year = getYear();
        try {
            MonthCost monthCost = db.selector(MonthCost.class).
                    where("month", "=", month).and("year", "=", year).and("in_out", "=", MyConst.COST).
                    and("user_name", "=", MyConst.getUserName(this)).findFirst();
            MonthCost monthInCome = db.selector(MonthCost.class).
                    where("month", "=", month).and("year", "=", year).and("in_out", "=", MyConst.IN_COME).
                    and("user_name", "=", MyConst.getUserName(this)).findFirst();
            Float c = 0f;
            Float i = 0f;
            if (monthCost != null) {
                c = monthCost.getMoney();
            }
            if (monthInCome != null) {
                i = monthInCome.getMoney();
            }
            DecimalFormat format = new DecimalFormat("#.##");
            tv_cost.setText("-" + format.format(c));
            tv_income.setText("+" + format.format(i));
            tv_total.setText(month + "月: " + format.format(i - c) + "元");
        } catch (DbException e) {
            JimoUtil.mySnackbar(tv_income, "月记录查询出错");
            e.printStackTrace();
        }
    }


    /**
     * 点击跳转页面
     */
    public void ibAddClick(View v) {
        Intent intent = new Intent(this, CostAddActivity.class);
        startActivity(intent);
    }

    public void ibTimerClick(View view) {
        Intent intent = new Intent(this, TimeCostActivity.class);
        startActivity(intent);
    }

    /**
     * 跳到数据统计页面
     *
     * @param v
     */
    public void clickToStatistic(View v) {
        JimoUtil.myToast(this, "待实现~~");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //刷新列表
//        queryData();
//        costDayItemAdapter.notifyDataSetChanged();
    }

    /**
     * 点击刷新列表
     *
     * @param view
     */
    @Event(R.id.ib_refresh)
    private void ibRefreshClick(View view) {
        //刷新列表
        refresh();
        JimoUtil.mySnackbar(view, "已刷新");
    }

    private void refresh() {
        queryData();
    }

    /**
     * 传数据到云端
     *
     * @param view
     */
    @Event(R.id.ib_up_to_cloud)
    private void ibToCloudClick(View view) throws IOException {
        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("上传数据");
        builder.setMessage("是否上传?");
        final View v = view;
        builder.setPositiveButton("ok", (dialogInterface, i) -> {
            //传输数据
            uploadData(v);
            dialogInterface.dismiss();
        });
        builder.create().show();*/

        syncToCloud();
    }

    /**
     * 0.判断坚果云基础信息是否填了，没有就弹出填写框
     * 1.首次同步，先创建目录mycost-db
     * 2.加载列表，展示数据库文件的修改时间，文件大小
     * 3.再次同步，覆写文件
     */
    void syncToCloud() throws IOException {
        // 0.
        if (MyConst.getCloudUserName(this) == null) {
            // 弹框填信息,记得验证
            new AddUserInfoDialog().show(getFragmentManager(), (name, pass) -> {
                if (isRightUserInfo(name, pass)) {
                    // 存起来
                    SharedPreferences pref = getApplication()
                            .getSharedPreferences("cloud-user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("username", name);
                    editor.putString("password", pass);
                    editor.apply();
                    JimoUtil.myToast(getApplicationContext(), "保存成功");
                } else {
                    JimoUtil.myToast(getApplicationContext(), "不正确的信息");
                }
            }, getApplicationContext());
            return;
        }

        Sardine sardine = SardineFactory.begin(MyConst.getUserName(this),
                MyConst.getCloudUserPass(this));
        // 1.
        if (!sardine.exists(MyConst.CLOUD_DB_PATH)) {
            sardine.createDirectory(MyConst.CLOUD_DB_PATH);
        }

        // 2.
        List<DavResource> resources = sardine.list(MyConst.CLOUD_DB_PATH);
        List<CloudFileEntry> entries = new ArrayList<>();
        for (DavResource resource : resources) {
            String name = resource.getName();
            if (name.startsWith("mycost.db")) {
                long size = resource.getContentLength() / 1024;
                String time = JimoUtil.formatDate(resource.getModified());
                entries.add(new CloudFileEntry(name, size, time));
            }
        }
        // 传递给dialog展示
        new CloudFileListDialog().show(getFragmentManager(), getApplicationContext(), entries);
    }

    private boolean isRightUserInfo(String name, String pass) {
        try {
            Sardine sardine = SardineFactory.begin(name, pass);
            sardine.list(MyConst.CLOUD_DAV_PATH);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void uploadData(final View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("上传中...");
        builder.setMessage("正在上传数据...");
        builder.setCancelable(false);
        builder.setPositiveButton("后台", (dialogInterface, i) -> dialogInterface.dismiss());
        final AlertDialog dialog = builder.create();
        dialog.show();

        //查出数据封装在params里
        RequestParams params = new RequestParams(MyConst.UPLOAD_URL);
        DbManager db = MyApp.dbManager;
        //查存每条没上传的记录
        try {
            List<CostInComeRecord> costInComeRecords =
                    db.selector(CostInComeRecord.class).
                            where("sync_type", "!=", MyConst.SYNC_TYPE_SYNCED).findAll();
            //用户名和密码
            params.addParameter("username", "jimo");
            String jsonString = JSON.toJSONString(costInComeRecords);
            params.addParameter("data", jsonString);
        } catch (DbException e) {
            e.printStackTrace();
        }

        final Callback.Cancelable post = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("xxxxx", result);
                JimoUtil.mySnackbar(view, "上传完成");
                dialog.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                JimoUtil.mySnackbar(view, "已中止");
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                builder.setMessage("错误：" + ex.getMessage());
                JimoUtil.mySnackbar(view, "错误：" + ex.getMessage());
            }

            @Override
            public void onFinished() {

            }
        });

    }

    public int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public void clickToCost(View view) {
        startActivity(new Intent(this, CostActivity.class));
    }

    public void clickToInCome(View view) {
        startActivity(new Intent(this, InComeActivity.class));
    }

    public void clickToLife(View view) {
        startActivity(new Intent(this, LifeActivity.class));
    }

    public void clickToFriend(View view) {
        startActivity(new Intent(this, FriendActivity.class));
    }

    public void clickToRecord(View view) {
        startActivity(new Intent(this, RecordActivity.class));
    }

    public void clickToTime(View view) {
        startActivity(new Intent(this, TimeActivity.class));
    }

    public void clickToReflect(View view) {
        startActivity(new Intent(this, ReflectActivity.class));
    }
}
