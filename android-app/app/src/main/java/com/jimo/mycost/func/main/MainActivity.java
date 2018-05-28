package com.jimo.mycost.func.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jimo.mycost.MyApp;
import com.jimo.mycost.MyConst;
import com.jimo.mycost.R;
import com.jimo.mycost.func.cost.show.CostDayItem;
import com.jimo.mycost.data.model.CostInComeRecord;
import com.jimo.mycost.data.model.MonthCost;
import com.jimo.mycost.func.cost.show.CostDayItemAdapter;
import com.jimo.mycost.func.cost.show.RecyclerViewTempImgAdapter;
import com.jimo.mycost.func.time.TimeCostActivity;
import com.jimo.mycost.util.JimoUtil;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ContentView(R.layout.activity_main)
public class MainActivity extends Activity {

    @ViewInject(R.id.lv_cost)
    ListView listViewCost;
    @ViewInject(R.id.tv_month_total)
    TextView tv_total;
    @ViewInject(R.id.tv_month_cost)
    TextView tv_cost;
    @ViewInject(R.id.tv_month_income)
    TextView tv_income;

    private List<CostDayItem> dayCostItems;
    private CostDayItemAdapter costDayItemAdapter;

    final int SHOW_LIMIT = 15;//主页面显示的记录条数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        initViews();
    }

    /**
     * 更新ListView
     */
    private void initViews() {
        dayCostItems = new ArrayList<>();
        costDayItemAdapter = new CostDayItemAdapter(dayCostItems, this);
        listViewCost.setAdapter(costDayItemAdapter);

        //当点击item时需跳转到新页面展示
        listViewCost.setOnItemClickListener((adapterView, view, i, l) -> {
//                CostDayItem dayCostTitle = dayCostItems.get(i);
            //TODO 显示item
        });

        refresh();
    }


    /**
     * 从数据库查出记录存在List里
     */
    private void queryData() {
        dayCostItems.clear();
        DbManager db = MyApp.dbManager;
        //查存每条记录
        try {
            List<CostInComeRecord> costInComeRecords =
                    db.selector(CostInComeRecord.class).orderBy("c_date", true).limit(SHOW_LIMIT).findAll();
            fillTitles(costInComeRecords, db);
        } catch (DbException e) {
            e.printStackTrace();
        }

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
     * 解析数据构造double ListView的数据
     * //TODO 待优化
     *
     * @param costInComeRecords
     * @param db
     */
    private void fillTitles(List<CostInComeRecord> costInComeRecords, DbManager db) throws DbException {
        if (costInComeRecords == null) {
            return;
        }
        Set<String> dates = new HashSet<>();
        for (CostInComeRecord c : costInComeRecords) {
            if (!dates.contains(c.getDate())) {
                dates.add(c.getDate());
                dayCostItems.add(new CostDayItem(c.getDate(), MyConst.ITEM_TYPE2));
                for (CostInComeRecord tc : costInComeRecords) {
                    String d = tc.getDate();
                    if (d != null && d.equals(c.getDate())) {
                        final RecyclerViewTempImgAdapter adapter = new RecyclerViewTempImgAdapter(this, tc.getImagePaths(db));
                        dayCostItems.add(new CostDayItem(tc.getDate(), MyConst.ITEM_TYPE1,
                                tc.getTypeName(), String.valueOf(tc.getMoney()), tc.getRemark(), tc.getId(), adapter));
                    }
                }
            }
        }
    }

    /**
     * 点击跳转页面
     */
    @Event(R.id.ib_add)
    private void ibAddClick(View v) {
        Intent intent = new Intent(this, AddCostActivity.class);
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
    public void ibStatisticClick(View v) {
        Intent intent = new Intent(this, DataStatisticActivity.class);
        startActivity(intent);
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
        costDayItemAdapter.notifyDataSetChanged();
    }

    /**
     * 传数据到云端
     *
     * @param view
     */
    @Event(R.id.ib_up_to_cloud)
    private void ibToCloudClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("上传数据");
        builder.setMessage("是否上传?");
        final View v = view;
        builder.setPositiveButton("ok", (dialogInterface, i) -> {
            //传输数据
            uploadData(v);
            dialogInterface.dismiss();
        });
        builder.create().show();
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
}
