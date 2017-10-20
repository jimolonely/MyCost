package com.jimo.mycost;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jimo.mycost.model.CostInComeRecord;
import com.jimo.mycost.model.MonthCost;
import com.jimo.mycost.ui.AddCostActivity;
import com.jimo.mycost.util.JimoUtil;
import com.jimo.mycost.view.DayCostItem;
import com.jimo.mycost.view.DayCostItemAdapter;

import org.json.JSONArray;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.db.sqlite.WhereBuilder;
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

    private List<DayCostItem> dayCostItems;
    private DayCostItemAdapter dayCostItemAdapter;

    private float inCome = 0;
    private float cost = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        initRecordData();

        initViews();

    }

    /**
     * 更新ListView
     */
    private void initViews() {
        dayCostItemAdapter = new DayCostItemAdapter(dayCostItems, this);
        listViewCost.setAdapter(dayCostItemAdapter);

        //当点击item时需跳转到新页面展示
        listViewCost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                DayCostItem dayCostTitle = dayCostItems.get(i);
                //TODO 显示item
            }
        });

        //长按时提示删除
        listViewCost.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, final View view, int i, long l) {
                final DayCostItem item = dayCostItems.get(i);
                if (item.getItemType() == MyConst.ITEM_TYPE2) {
                    JimoUtil.mySnackbar(tv_income, "这个不可以删哦");
                    return true;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("删除");
                builder.setMessage("确定删除吗?");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DbManager db = MyApp.dbManager;
                        WhereBuilder wb = WhereBuilder.b();
                        wb.and("id", "=", item.getId());
                        try {
                            db.delete(CostInComeRecord.class, wb);
                            queryData();
                            dayCostItemAdapter.notifyDataSetChanged();
                            JimoUtil.mySnackbar(view, "删除成功");
                        } catch (DbException e) {
                            JimoUtil.mySnackbar(view, "删除失败");
                            e.printStackTrace();
                        }
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
                return true;
            }
        });
    }


    private void initRecordData() {
        dayCostItems = new ArrayList<>();
        queryData();
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
                    db.selector(CostInComeRecord.class).orderBy("c_date", true).limit(10).findAll();
            fillTitles(costInComeRecords);
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
     */
    private void fillTitles(List<CostInComeRecord> costInComeRecords) {
        if (costInComeRecords == null) {
            return;
        }
        Set<String> dates = new HashSet<>();
        for (CostInComeRecord c : costInComeRecords) {
            if (!dates.contains(c.getDate())) {
                dates.add(c.getDate());
                dayCostItems.add(new DayCostItem(c.getDate(), MyConst.ITEM_TYPE2,
                        c.getTypeName(), String.valueOf(c.getMoney()), c.getId()));
                for (CostInComeRecord tc : costInComeRecords) {
                    String d = tc.getDate();
                    if (d != null && d.equals(c.getDate())) {
                        dayCostItems.add(new DayCostItem(tc.getDate(), MyConst.ITEM_TYPE1,
                                tc.getTypeName() + "  " + tc.getRemark(), String.valueOf(tc.getMoney()), tc.getId()));
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


    @Override
    protected void onResume() {
        super.onResume();
        //刷新列表
        queryData();
        dayCostItemAdapter.notifyDataSetChanged();
    }

    /**
     * 点击刷新列表
     *
     * @param view
     */
    @Event(R.id.ib_refresh)
    private void ibRefreshClick(View view) {
        //刷新列表
        queryData();
        dayCostItemAdapter.notifyDataSetChanged();
        JimoUtil.mySnackbar(view, "已刷新");
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
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //传输数据
                uploadData(v);
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    private void uploadData(final View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("上传中...");
        builder.setMessage("正在上传数据...");
        builder.setCancelable(false);
        builder.setPositiveButton("后台", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
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
